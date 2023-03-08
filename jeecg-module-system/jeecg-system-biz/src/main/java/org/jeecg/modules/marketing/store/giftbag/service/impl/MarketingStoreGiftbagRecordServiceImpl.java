package org.jeecg.modules.marketing.store.giftbag.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.service.IMarketingCertificateService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardListService;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagRecord;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeam;
import org.jeecg.modules.marketing.store.giftbag.mapper.MarketingStoreGiftbagRecordMapper;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagDividendService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagRecordService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagTeamService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.pay.entity.PayMarketingStoreGiftbagLog;
import org.jeecg.modules.pay.service.IPayMarketingStoreGiftbagLogService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;

/**
 * @Description: 礼包团-购买记录
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
@Service
public class MarketingStoreGiftbagRecordServiceImpl extends ServiceImpl<MarketingStoreGiftbagRecordMapper, MarketingStoreGiftbagRecord> implements IMarketingStoreGiftbagRecordService {


    @Autowired
    private IPayMarketingStoreGiftbagLogService iPayMarketingStoreGiftbagLogService;

    @Autowired
    private IMemberListService iMemberListService;


    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMarketingStoreGiftbagTeamService iMarketingStoreGiftbagTeamService;

    @Autowired
    private IMarketingStoreGiftbagDividendService iMarketingStoreGiftbagDividendService;

    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;

    @Autowired
    private IMarketingStoreGiftbagListService iMarketingStoreGiftbagListService;

    @Autowired
    private IStoreManageService iStoreManageService;


    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IMarketingStoreGiftCardListService iMarketingStoreGiftCardListService;

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.queryPageList(page,paramMap);
    }

    @Override
    @Transactional
    public void success(String payLogId) {
        PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog=iPayMarketingStoreGiftbagLogService.getById(payLogId);
        if(!payMarketingStoreGiftbagLog.getPayStatus().equals("0")){
            return;
        }
        payMarketingStoreGiftbagLog.setPayStatus("1");
        if(iPayMarketingStoreGiftbagLogService.saveOrUpdate(payMarketingStoreGiftbagLog)){
            //扣除余额
            if (!iMemberListService.subtractBlance(payMarketingStoreGiftbagLog.getMemberListId(), payMarketingStoreGiftbagLog.getBalance(), payMarketingStoreGiftbagLog.getId(), "0")) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            //扣除积分
            if (!iMemberWelfarePaymentsService.subtractWelfarePayments(payMarketingStoreGiftbagLog.getMemberListId(), payMarketingStoreGiftbagLog.getWelfarePayments(), "6", payMarketingStoreGiftbagLog.getId(),"")) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

            //生成礼包团记录
            MarketingStoreGiftbagRecord marketingStoreGiftbagRecord=new MarketingStoreGiftbagRecord();
            marketingStoreGiftbagRecord.setMemebrListId(payMarketingStoreGiftbagLog.getMemberListId());
            marketingStoreGiftbagRecord.setMarketingStoreGiftbagListId(payMarketingStoreGiftbagLog.getMarketingStoreGiftbagListId());
            marketingStoreGiftbagRecord.setPayMarketingStoreGiftbagLogId(payMarketingStoreGiftbagLog.getId());
            marketingStoreGiftbagRecord.setSerialNumber(OrderNoUtils.getOrderNo());
            this.save(marketingStoreGiftbagRecord);

            //团队建立
            MarketingStoreGiftbagTeam marketingStoreGiftbagTeam=iMarketingStoreGiftbagTeamService.createGiftbagTeam(payLogId);

            //奖励发放
            iMarketingStoreGiftbagDividendService.paymentIncentives(payLogId,marketingStoreGiftbagTeam,marketingStoreGiftbagRecord.getId());


            MarketingStoreGiftbagList marketingStoreGiftbagList=iMarketingStoreGiftbagListService.getById(payMarketingStoreGiftbagLog.getMarketingStoreGiftbagListId());

            StoreManage storeManage=iStoreManageService.getById(marketingStoreGiftbagList.getStoreManageId());

            //兑换券生成
            JSON.parseArray(marketingStoreGiftbagList.getCoinCertificate().toString()).forEach(m->{
                JSONObject jsonObject=(JSONObject)m;

                Boolean isContinuous=false;
                if(jsonObject.getInteger("isContinuous").intValue()==1){
                    isContinuous=true;
                }

                iMarketingCertificateService.generate(jsonObject.getString("id"),storeManage.getSysUserId(),jsonObject.getBigDecimal("quantity"),payMarketingStoreGiftbagLog.getMemberListId(),isContinuous);
            });


            //优惠券生成
            JSON.parseArray(marketingStoreGiftbagList.getDiscountCoupon().toString()).forEach(m->{
                JSONObject jsonObject=(JSONObject) m;
                Boolean isContinuous=false;
                if(jsonObject.getInteger("isContinuous").intValue()==1){
                    isContinuous=true;
                }
                iMarketingDiscountService.generate(jsonObject.getString("id"),jsonObject.getBigDecimal("quantity"),payMarketingStoreGiftbagLog.getMemberListId(),isContinuous);
            });


            //礼品卡生成
            JSON.parseArray(marketingStoreGiftbagList.getGiftCard().toString()).forEach(m->{
                JSONObject jsonObject=(JSONObject) m;
                for (int i = 0; i <jsonObject.getBigDecimal("quantity").intValue(); i++) {
                    iMarketingStoreGiftCardListService.generate(jsonObject.getString("id"), payMarketingStoreGiftbagLog.getMemberListId(), "0");
                }
            });



        }else{
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
