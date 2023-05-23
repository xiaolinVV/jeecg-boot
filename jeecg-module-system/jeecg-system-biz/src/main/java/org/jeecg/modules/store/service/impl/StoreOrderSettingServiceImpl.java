package org.jeecg.modules.store.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
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
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺订单设置
 * @Author: jeecg-boot
 * @Date: 2022-08-23
 * @Version: V1.0
 */
@Slf4j
@Service
public class StoreOrderSettingServiceImpl extends ServiceImpl<StoreOrderSettingMapper, StoreOrderSetting> implements IStoreOrderSettingService {

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IOrderStoreGoodRecordService iOrderStoreGoodRecordService;

    @Override
    public StoreOrderSetting getStoreOrderSetting(String storeManageId) {
        return this.getOne(new LambdaQueryWrapper<StoreOrderSetting>().eq(StoreOrderSetting::getStoreManageId, storeManageId));
    }

    @Override
    public void success(OrderStoreList orderStoreList) {

        StoreManage storeManage = iStoreManageService.getStoreManageBySysUserId(orderStoreList.getSysUserId());
        if (storeManage == null) {
            log.info("店铺不存在,sysUserId{}", orderStoreList.getSysUserId());
            return;
        }

        //赠送用户积分
        StoreOrderSetting storeOrderSetting = this.getStoreOrderSetting(storeManage.getId());
        if (storeOrderSetting != null && storeOrderSetting.getIsIntegral().equals("1")) {
            orderStoreList.setGiveWelfarePayments(orderStoreList.getActualPayment().subtract(orderStoreList.getShipFee()).multiply(storeOrderSetting.getPresentProportion()).divide(new BigDecimal(100), 2, RoundingMode.DOWN));
            // 拆分每个商品送多少积分 @张少林
            List<Map<String, Object>> storeGoodRecordList = iOrderStoreGoodRecordService.getOrderStoreGoodRecordByOrderId(orderStoreList.getId());
            if (CollUtil.isNotEmpty(storeGoodRecordList)) {
                for (int i = 0; i < storeGoodRecordList.size(); i++) {
                    Map<String, Object> storeOrderGoodRecordMap = storeGoodRecordList.get(i);
                    BigDecimal actualPayment = Convert.toBigDecimal(storeOrderGoodRecordMap.get("actualPayment"), BigDecimal.ZERO);
                    OrderStoreGoodRecord orderStoreGoodRecord = new OrderStoreGoodRecord().setId(Convert.toStr(storeOrderGoodRecordMap.get("id")));
                    BigDecimal temSum = new BigDecimal("0");
                    if (i == storeGoodRecordList.size() - 1) {
                        BigDecimal giveWelfarePayments = NumberUtil.sub(orderStoreList.getGiveWelfarePayments().subtract(orderStoreList.getShipFee()),temSum);
                        orderStoreGoodRecord.setGiveWelfarePayments(giveWelfarePayments);
                    } else {
                        BigDecimal giveWelfarePayments = NumberUtil.mul(orderStoreList.getGiveWelfarePayments(), NumberUtil.div(actualPayment, orderStoreList.getActualPayment().subtract(orderStoreList.getShipFee()), 2));
                        orderStoreGoodRecord.setGiveWelfarePayments(giveWelfarePayments);
                        temSum = NumberUtil.add(temSum,giveWelfarePayments);
                    }
                    iOrderStoreGoodRecordService.updateById(orderStoreGoodRecord);
                }
            }
            if (!iMemberWelfarePaymentsService.addWelfarePayments(orderStoreList.getMemberListId(), orderStoreList.getGiveWelfarePayments(), "0", orderStoreList.getId(), "订单送积分")) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }
}
