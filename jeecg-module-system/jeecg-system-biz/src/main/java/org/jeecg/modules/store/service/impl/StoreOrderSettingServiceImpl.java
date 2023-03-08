package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreOrderSetting;
import org.jeecg.modules.store.mapper.StoreOrderSettingMapper;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Description: 店铺订单设置
 * @Author: jeecg-boot
 * @Date:   2022-08-23
 * @Version: V1.0
 */
@Service
public class StoreOrderSettingServiceImpl extends ServiceImpl<StoreOrderSettingMapper, StoreOrderSetting> implements IStoreOrderSettingService {

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Override
    public StoreOrderSetting getStoreOrderSetting(String storeManageId) {
        return this.getOne(new LambdaQueryWrapper<StoreOrderSetting>().eq(StoreOrderSetting::getStoreManageId,storeManageId));
    }

    @Override
    public void success(OrderStoreList orderStoreList) {

        StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(orderStoreList.getSysUserId());

        //赠送用户积分
        StoreOrderSetting storeOrderSetting=this.getStoreOrderSetting(storeManage.getId());
        if(storeOrderSetting!=null&&storeOrderSetting.getIsIntegral().equals("1")){
            orderStoreList.setGiveWelfarePayments(orderStoreList.getActualPayment().subtract(orderStoreList.getShipFee()).multiply(storeOrderSetting.getPresentProportion()).divide(new BigDecimal(100),2, RoundingMode.DOWN));
            if(!iMemberWelfarePaymentsService.addWelfarePayments(orderStoreList.getMemberListId(),orderStoreList.getGiveWelfarePayments(),"0",orderStoreList.getId(),"订单送积分")){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }
}
