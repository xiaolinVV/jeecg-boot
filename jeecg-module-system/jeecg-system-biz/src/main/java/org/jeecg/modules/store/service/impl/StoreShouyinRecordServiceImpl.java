package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.pay.entity.PayShouyinLog;
import org.jeecg.modules.pay.service.IPayShouyinLogService;
import org.jeecg.modules.store.entity.StoreCashierSetting;
import org.jeecg.modules.store.entity.StoreShouyinRecord;
import org.jeecg.modules.store.mapper.StoreShouyinRecordMapper;
import org.jeecg.modules.store.service.IStoreCashierRoutingService;
import org.jeecg.modules.store.service.IStoreCashierSettingService;
import org.jeecg.modules.store.service.IStoreShouyinRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @Description: 店铺收银记录
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@Service
public class StoreShouyinRecordServiceImpl extends ServiceImpl<StoreShouyinRecordMapper, StoreShouyinRecord> implements IStoreShouyinRecordService {


    @Autowired
    private IPayShouyinLogService iPayShouyinLogService;

    @Autowired
    private IStoreCashierSettingService iStoreCashierSettingService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IStoreCashierRoutingService iStoreCashierRoutingService;

    @Autowired
    private IMemberListService iMemberListService;

    @Override
    @Transactional
    public void success(String payShouyinLogId) {
        PayShouyinLog payShouyinLog=iPayShouyinLogService.getById(payShouyinLogId);
        if(payShouyinLog.getPayStatus().equals("1")){
            return;
        }
        payShouyinLog.setPayStatus("1");

        //余额分账
        iStoreCashierRoutingService.independentAccountShouYinBalance(payShouyinLog);

        //新增店铺收银记录
        StoreShouyinRecord storeShouyinRecord=new StoreShouyinRecord();
        storeShouyinRecord.setOrderNo(OrderNoUtils.getOrderNo());
        storeShouyinRecord.setPayPrice(payShouyinLog.getAllTotalPrice());
        storeShouyinRecord.setPayModel(payShouyinLog.getPayModel());
        storeShouyinRecord.setSerialNumber(payShouyinLog.getSerialNumber());
        storeShouyinRecord.setMemberListId(payShouyinLog.getMemberListId());
        storeShouyinRecord.setWelfarePayments(payShouyinLog.getWelfarePayments());
        storeShouyinRecord.setStoreManageId(payShouyinLog.getStoreManageId());
        storeShouyinRecord.setBrokerage(payShouyinLog.getAllTotalPrice().subtract(payShouyinLog.getSettlementAmount()));
        storeShouyinRecord.setSettlementAmount(payShouyinLog.getSettlementAmount());
        storeShouyinRecord.setIndependentAccount(payShouyinLog.getIndependentAccount());
        storeShouyinRecord.setRemark(payShouyinLog.getRemark());
        if(StringUtils.isNotBlank(storeShouyinRecord.getMemberListId())){
            MemberList memberList=iMemberListService.getById(storeShouyinRecord.getMemberListId());
            if(StringUtils.isNotBlank(memberList.getNickName())){
                storeShouyinRecord.setNickName(memberList.getNickName());
            }else{
                storeShouyinRecord.setNickName(memberList.getPhone());
            }
        }


        //赠送用户积分
        StoreCashierSetting storeCashierSetting=iStoreCashierSettingService.getStoreCashierSetting(payShouyinLog.getStoreManageId());
        if(storeCashierSetting!=null&& StringUtils.isNotBlank(payShouyinLog.getMemberListId())&&storeCashierSetting.getIsIntegral().equals("1")){
            payShouyinLog.setWelfarePayments(payShouyinLog.getAllTotalPrice().multiply(storeCashierSetting.getPresentProportion()).divide(new BigDecimal(100),2, RoundingMode.DOWN));
            storeShouyinRecord.setWelfarePayments(payShouyinLog.getWelfarePayments());
            if(!iMemberWelfarePaymentsService.addWelfarePayments(payShouyinLog.getMemberListId(),payShouyinLog.getWelfarePayments(),"52",payShouyinLogId,"扫码支付赠送")){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        if(!iPayShouyinLogService.saveOrUpdate(payShouyinLog)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        if(!this.save(storeShouyinRecord)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public Map<String, Object> statistics(Map<String, Object> paramMap) {
        return baseMapper.statistics(paramMap);
    }
}
