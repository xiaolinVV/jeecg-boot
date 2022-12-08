package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingBusinessCapital;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoney;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoneyRecord;
import org.jeecg.modules.marketing.mapper.MarketingBusinessCapitalMapper;
import org.jeecg.modules.marketing.service.IMarketingBusinessCapitalService;
import org.jeecg.modules.marketing.service.IMarketingBusinessMakeMoneyRecordService;
import org.jeecg.modules.marketing.service.IMarketingBusinessMakeMoneyService;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberBusinessDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @Description: 创业资金池配置
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
@Service
@Log
public class MarketingBusinessCapitalServiceImpl extends ServiceImpl<MarketingBusinessCapitalMapper, MarketingBusinessCapital> implements IMarketingBusinessCapitalService {

    @Autowired
    private IMarketingBusinessMakeMoneyService iMarketingBusinessMakeMoneyService;


    @Autowired
    private IMemberBusinessDesignationService iMemberBusinessDesignationService;


    @Autowired
    private IMarketingBusinessMakeMoneyRecordService iMarketingBusinessMakeMoneyRecordService;


    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMemberListService iMemberListService;


    @Override
    @Transactional
    public boolean add(String marketingBusinessCapitalId, BigDecimal price,String tradeType,String tradeNo) {
        MarketingBusinessCapital marketingBusinessCapital=this.getById(marketingBusinessCapitalId);
        MarketingBusinessMakeMoney marketingBusinessMakeMoney=new MarketingBusinessMakeMoney();
        marketingBusinessMakeMoney.setSerialNumber(OrderNoUtils.getOrderNo());
        marketingBusinessMakeMoney.setMarketingBusinessCapitalId(marketingBusinessCapital.getId());
        marketingBusinessMakeMoney.setTradeType(tradeType);
        marketingBusinessMakeMoney.setBonusMoney(price);
        marketingBusinessMakeMoney.setMakeProportion(marketingBusinessCapital.getMakeProportion());
        marketingBusinessMakeMoney.setAmount(marketingBusinessCapital.getMakeProportion().divide(new BigDecimal(100)).multiply(price));
        //资金池余额
        marketingBusinessCapital.setBalance(marketingBusinessCapital.getBalance().add(marketingBusinessMakeMoney.getAmount()));
        //入账资金
        marketingBusinessCapital.setRecordedAmount(marketingBusinessCapital.getRecordedAmount().add(marketingBusinessMakeMoney.getAmount()));
        marketingBusinessMakeMoney.setTotalBalance(marketingBusinessCapital.getBalance());
        marketingBusinessMakeMoney.setPayTime(new Date());
        marketingBusinessMakeMoney.setTradeNo(tradeNo);
        marketingBusinessMakeMoney.setGoAndConme("0");
        log.info("创业礼包入账资金："+ JSON.toJSONString(marketingBusinessMakeMoney));
        if(iMarketingBusinessMakeMoneyService.save(marketingBusinessMakeMoney)){
            if(this.saveOrUpdate(marketingBusinessCapital)){
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void dailyDividend(String marketingBusinessCapitalId) {
        MarketingBusinessCapital marketingBusinessCapital=this.getById(marketingBusinessCapitalId);
        if(marketingBusinessCapital.getBalance().doubleValue()!=0&& StringUtils.isNotBlank(marketingBusinessCapital.getMarketingBusinessDesignationId())) {
            long memberCount = iMemberBusinessDesignationService.count(new LambdaQueryWrapper<MemberBusinessDesignation>().eq(MemberBusinessDesignation::getMarketingBusinessDesignationId, marketingBusinessCapital.getMarketingBusinessDesignationId()));
            if (memberCount != 0) {
                log.info("分红总人数：" + memberCount);
                log.info("分红总余额：" + marketingBusinessCapital.getBalance());
                BigDecimal memberBlance = marketingBusinessCapital.getBalance().divide(new BigDecimal(memberCount), 2, RoundingMode.DOWN);
                log.info("每人分红金额：" + memberBlance);
                //礼包金额出账
            MarketingBusinessMakeMoney marketingBusinessMakeMoney=new MarketingBusinessMakeMoney();
            marketingBusinessMakeMoney.setSerialNumber(OrderNoUtils.getOrderNo());
            marketingBusinessMakeMoney.setMarketingBusinessCapitalId(marketingBusinessCapital.getId());
            marketingBusinessMakeMoney.setTradeType("0");
            marketingBusinessMakeMoney.setBonusMoney(marketingBusinessCapital.getBalance());
            //资金池余额
            marketingBusinessCapital.setBalance(new BigDecimal(0));
            //出账资金
            marketingBusinessCapital.setAccountsAmount(marketingBusinessCapital.getAccountsAmount().add(marketingBusinessMakeMoney.getBonusMoney()));
            marketingBusinessMakeMoney.setTotalBalance(marketingBusinessCapital.getBalance());
            marketingBusinessMakeMoney.setPayTime(new Date());
            marketingBusinessMakeMoney.setTradeNo(marketingBusinessCapital.getId());
            marketingBusinessMakeMoney.setGoAndConme("1");
            log.info("创业礼包出账资金："+ JSON.toJSONString(marketingBusinessMakeMoney));
            if(iMarketingBusinessMakeMoneyService.save(marketingBusinessMakeMoney)){
                if(this.saveOrUpdate(marketingBusinessCapital)){
                    iMemberBusinessDesignationService.list(new LambdaQueryWrapper<MemberBusinessDesignation>().eq(MemberBusinessDesignation::getMarketingBusinessDesignationId, marketingBusinessCapital.getMarketingBusinessDesignationId())).forEach(m->{
                        MarketingBusinessMakeMoneyRecord marketingBusinessMakeMoneyRecord=new MarketingBusinessMakeMoneyRecord();
                        marketingBusinessMakeMoneyRecord.setSerialNumber(OrderNoUtils.getOrderNo());
                        marketingBusinessMakeMoneyRecord.setMarketingBusinessMakeMoneyId(marketingBusinessMakeMoney.getId());
                        marketingBusinessMakeMoneyRecord.setMemberListId(m.getMemberListId());
                        marketingBusinessMakeMoneyRecord.setAmount(memberBlance);
                        if(iMarketingBusinessMakeMoneyRecordService.saveOrUpdate(marketingBusinessMakeMoneyRecord)){
                            if(!iMemberWelfarePaymentsService.addUnusableWelfarePayments(m.getMemberListId(),marketingBusinessMakeMoneyRecord.getAmount(),"34",marketingBusinessMakeMoneyRecord.getSerialNumber())){
                                //手动强制回滚事务，这里一定要第一时间处理
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            }
                        }else{
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    });
                }else{
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            }
        }
    }

    @Override
    @Transactional
    public void receiveDividends(String memberId) {
        MemberList memberList=iMemberListService.getById(memberId);
        MemberBusinessDesignation memberBusinessDesignation = iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                .eq(MemberBusinessDesignation::getMemberListId, memberList.getId())
                .orderByDesc(MemberBusinessDesignation::getCreateTime)
                .last("limit 1")
        );
        if(memberBusinessDesignation==null){
            return;
        }
        //获取称号资金池
        MarketingBusinessCapital marketingBusinessCapital = this.getOne(new LambdaQueryWrapper<MarketingBusinessCapital>()
                .eq(MarketingBusinessCapital::getMarketingBusinessDesignationId, memberBusinessDesignation.getMarketingBusinessDesignationId())
                .orderByDesc(MarketingBusinessCapital::getCreateTime)
                .last("limit 1")
        );
        if (marketingBusinessCapital.getWhetherDividend().equals("0")){
            //不限
            //减去所有冻结金额
            BigDecimal welfarePayments= memberList.getWelfarePaymentsUnusable();
            if(!iMemberWelfarePaymentsService.subtractUnusableWelfarePayments(memberId,welfarePayments,"34",memberId)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            if(!iMemberWelfarePaymentsService.addWelfarePayments(memberId,welfarePayments,"34",memberId,"")){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else{
                while (true){
                    if(memberBusinessDesignation.getAfterShots().intValue()==0){
                        break;
                    }
                    if(iMemberListService.getById(memberId).getWelfarePaymentsUnusable().doubleValue()<=0){
                        break;
                    }
                    BigDecimal  share= marketingBusinessCapital.getAmountShare().subtract(memberBusinessDesignation.getAmountShare());
                    if(memberList.getWelfarePaymentsUnusable().subtract(share).doubleValue()>=0){
                        //减去所有冻结金额
                        if(!iMemberWelfarePaymentsService.subtractUnusableWelfarePayments(memberId,share,"34",memberId)){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        if(!iMemberWelfarePaymentsService.addWelfarePayments(memberId,share,"34",memberId,"")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        memberBusinessDesignation.setAfterShots(memberBusinessDesignation.getAfterShots().subtract(new BigDecimal(1)));
                        memberBusinessDesignation.setAmountShare(new BigDecimal(0));
                    }else{
                        //减去所有冻结金额
                        BigDecimal welfarePayments= memberList.getWelfarePaymentsUnusable();
                        if(!iMemberWelfarePaymentsService.subtractUnusableWelfarePayments(memberId,welfarePayments,"34",memberId)){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        if(!iMemberWelfarePaymentsService.addWelfarePayments(memberId,welfarePayments,"34",memberId,"")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        memberBusinessDesignation.setAmountShare(memberList.getWelfarePaymentsUnusable());
                    }
                }
            iMemberBusinessDesignationService.saveOrUpdate(memberBusinessDesignation);
        }
    }
}
