package org.jeecg.modules.marketing.store.prefecture.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingRecordedMoney;
import org.jeecg.modules.marketing.service.IMarketingRecordedMoneyService;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureDividend;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;
import org.jeecg.modules.marketing.store.prefecture.mapper.MarketingStorePrefectureDividendMapper;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureDividendService;
import org.jeecg.modules.member.entity.MemberDesignation;
import org.jeecg.modules.member.service.IMemberDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.service.IPayOrderCarLogService;
import org.jeecg.modules.store.entity.StoreFranchiser;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreFranchiserService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @Description: 店铺专区-记录分红明细
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Service
@Log
public class MarketingStorePrefectureDividendServiceImpl extends ServiceImpl<MarketingStorePrefectureDividendMapper, MarketingStorePrefectureDividend> implements IMarketingStorePrefectureDividendService {

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberDesignationService iMemberDesignationService;

    @Autowired
    private IMarketingRecordedMoneyService iMarketingRecordedMoneyService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IPayOrderCarLogService iPayOrderCarLogService;

    @Autowired
    private IStoreFranchiserService iStoreFranchiserService;

    @Override
    public void paymentIncentives(String payOrderCarLogId, MarketingStorePrefectureList marketingStorePrefectureList, String marketingStorePrefectureRecordId) {
        if(marketingStorePrefectureList.getDividend().doubleValue()>0){

            PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getById(payOrderCarLogId);


            List<MarketingStorePrefectureDividend> marketingStorePrefectureDividends= Lists.newArrayList();

            //本店分红
            BigDecimal shareBonus=marketingStorePrefectureList.getDividend().multiply(marketingStorePrefectureList.getStoreShare()).divide(new BigDecimal(100),2, RoundingMode.DOWN);
            if(shareBonus.doubleValue()>0){
                MarketingStorePrefectureDividend marketingStorePrefectureDividend=new MarketingStorePrefectureDividend();
                marketingStorePrefectureDividend.setMarketingStorePrefectureRecordId(marketingStorePrefectureRecordId);
                marketingStorePrefectureDividend.setRoleName("本店分红");
                marketingStorePrefectureDividend.setPayType("0");
                marketingStorePrefectureDividend.setShareBonus(shareBonus);
                marketingStorePrefectureDividend.setStoreManageId(marketingStorePrefectureList.getStoreManageId());
                this.save(marketingStorePrefectureDividend);
                marketingStorePrefectureDividends.add(marketingStorePrefectureDividend);
            }

            //分享人奖励
            if(StringUtils.isNotBlank(payOrderCarLog.getTMemberId())) {
                BigDecimal shareAward = marketingStorePrefectureList.getDividend().multiply(marketingStorePrefectureList.getShareAward()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                if (shareAward.doubleValue() > 0) {
                    MarketingStorePrefectureDividend marketingStorePrefectureDividend=new MarketingStorePrefectureDividend();
                    marketingStorePrefectureDividend.setMarketingStorePrefectureRecordId(marketingStorePrefectureRecordId);
                    marketingStorePrefectureDividend.setMemebrListId(payOrderCarLog.getTMemberId());
                    marketingStorePrefectureDividend.setRoleName("推荐奖励");
                    marketingStorePrefectureDividend.setPayType("1");
                    marketingStorePrefectureDividend.setShareBonus(shareAward);
                    this.save(marketingStorePrefectureDividend);
                    marketingStorePrefectureDividends.add(marketingStorePrefectureDividend);
                }
            }

            //称号奖励
            if(marketingStorePrefectureList.getDividend().doubleValue()>0){
                JSONArray jsonArray= JSON.parseArray(marketingStorePrefectureList.getTitleAward().toString());
                jsonArray.forEach(ob->{
                    JSONObject jsonObject=(JSONObject) ob;
                    BigDecimal titleReward=jsonObject.getBigDecimal("rewards").multiply(marketingStorePrefectureList.getDividend()).divide(new BigDecimal(100),2,RoundingMode.DOWN);
                    if(titleReward.doubleValue()>0){
                        MarketingStorePrefectureDividend marketingStorePrefectureDividend=new MarketingStorePrefectureDividend();
                        marketingStorePrefectureDividend.setMarketingStorePrefectureRecordId(marketingStorePrefectureRecordId);
                        marketingStorePrefectureDividend.setMemberDesignationId(jsonObject.getString("id"));
                        marketingStorePrefectureDividend.setRoleName(jsonObject.getString("name"));
                        marketingStorePrefectureDividend.setPayType("3");
                        marketingStorePrefectureDividend.setShareBonus(titleReward);
                        this.save(marketingStorePrefectureDividend);
                        marketingStorePrefectureDividends.add(marketingStorePrefectureDividend);
                    }
                });
            }


            //给经销商分钱
            BigDecimal dealerIncentives=marketingStorePrefectureList.getDividend().multiply(marketingStorePrefectureList.getDealerIncentives()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
            if(dealerIncentives.doubleValue()>0){
                StoreManage storeManage=iStoreManageService.getById(marketingStorePrefectureList.getStoreManageId());
                //获取本会员的经销商
                StoreFranchiser storeFranchiser=iStoreFranchiserService.findStoreFranchiser(payOrderCarLog.getMemberListId(),storeManage.getId());
                if(storeFranchiser!=null){
                    iStoreFranchiserService.awardStoreFranchiser(storeFranchiser,dealerIncentives,payOrderCarLog.getMemberListId());
                }else{
                    StoreFranchiser storeFranchiser1=iStoreFranchiserService.findStoreFranchiser(payOrderCarLog.getTMemberId(),storeManage.getId());
                    if(storeFranchiser1!=null){
                        iStoreFranchiserService.awardStoreFranchiser(storeFranchiser1,dealerIncentives,payOrderCarLog.getMemberListId());
                        iStoreFranchiserService.joinStoreFranchiser(storeFranchiser1,payOrderCarLog.getMemberListId());
                        MarketingStorePrefectureDividend marketingStorePrefectureDividend=new MarketingStorePrefectureDividend();
                        marketingStorePrefectureDividend.setMarketingStorePrefectureRecordId(marketingStorePrefectureRecordId);
                        marketingStorePrefectureDividend.setMemebrListId(storeFranchiser1.getMemberListId());
                        marketingStorePrefectureDividend.setRoleName("经销商奖励");
                        marketingStorePrefectureDividend.setPayType("2");
                        marketingStorePrefectureDividend.setShareBonus(dealerIncentives);
                        this.save(marketingStorePrefectureDividend);
                    }
                }

            }

            //奖励下发
            marketingStorePrefectureDividends.forEach(m->{

                if(m.getPayType().equals("0")){
                    //本店奖励
                    if(!iStoreManageService.addStoreBlance(m.getStoreManageId(),m.getShareBonus(),m.getId(),"18")){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }else if(m.getPayType().equals("3")){
                    MemberDesignation memberDesignation=iMemberDesignationService.getById(m.getMemberDesignationId());
                    iMemberDesignationService.saveOrUpdate(memberDesignation
                            .setBalance(memberDesignation.getBalance().add(m.getShareBonus()))
                            .setRecordedMoney(memberDesignation.getRecordedMoney().add(m.getShareBonus()))
                    );
                    MarketingRecordedMoney marketingRecordedMoney = new MarketingRecordedMoney()
                            .setMemberDesignationId(memberDesignation.getId())
                            .setTradeNo(m.getId())
                            .setTradeType("1")
                            .setParticipation(new BigDecimal(100))
                            .setParticipationMoney(m.getShareBonus())
                            .setRecordedMoney(m.getShareBonus())
                            .setBalance(memberDesignation.getBalance())
                            .setTradeTime(new Date())
                            .setRemark("礼包团分红[" + m.getId() + "]")
                            .setOrderNo(OrderNoUtils.getOrderNo());
                    iMarketingRecordedMoneyService.save(marketingRecordedMoney);
                    log.info("称号资金：" + memberDesignation.getName() + "入账资金:" + m.getShareBonus());
                }else{
                    //其他会员奖励
                    String tradeType="";
                    if(m.getPayType().equals("1")){
                        tradeType="54";
                    }
                    if(!iMemberListService.addBlance(m.getMemebrListId(),m.getShareBonus(),m.getId(),tradeType)){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            });



        }
    }
}
