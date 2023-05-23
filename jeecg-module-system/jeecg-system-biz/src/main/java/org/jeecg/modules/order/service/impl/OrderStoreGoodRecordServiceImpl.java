package org.jeecg.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.order.dto.OrderStoreGoodRecordDTO;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.mapper.OrderStoreGoodRecordMapper;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺订单商品记录列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Service
public class OrderStoreGoodRecordServiceImpl extends ServiceImpl<OrderStoreGoodRecordMapper, OrderStoreGoodRecord> implements IOrderStoreGoodRecordService {

    @Autowired
    IOrderRefundListService orderRefundListService;

    @Override
    public List<Map<String,Object>> getOrderStoreGoodRecordByOrderId(String orderId) {
        List<Map<String, Object>> orderStoreGoodRecordByOrderId = baseMapper.getOrderStoreGoodRecordByOrderId(orderId);
        orderStoreGoodRecordByOrderId.forEach(m -> {
            //查询售后中数量 by zhangshaolin
            LambdaQueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderRefundListLambdaQueryWrapper
                    .eq(OrderRefundList::getOrderGoodRecordId, m.get("id"))
                    .in(OrderRefundList::getStatus, "0", "1", "2", "3", "4", "5");
            long ongoingRefundCount = orderRefundListService.count(orderRefundListLambdaQueryWrapper);
            m.put("ongoingRefundCount",ongoingRefundCount);
        });
        return orderStoreGoodRecordByOrderId;
    }

    @Override
    public List<Map<String, Object>> getOrderStoreGoodRecordByOrderIdAndMarketingDiscountCouponId(String orderId, String marketingDiscountCouponId) {
        return baseMapper.getOrderStoreGoodRecordByOrderIdAndMarketingDiscountCouponId(orderId, marketingDiscountCouponId);
    }

    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    @Override
    public List<OrderStoreGoodRecord>  selectOrderStoreSubListId(String orderStoreSubListId){
        return baseMapper.selectOrderStoreSubListId(orderStoreSubListId);
    };
    /**
     * 根据订单id查询记录商品信息(评价)
     * @param orderId
     * @return
     */
    @Override
   public List<Map<String,Object>> getOrderStoreGoodRecordByOrderIdEvaluate(String orderId){
        return baseMapper.getOrderStoreGoodRecordByOrderIdEvaluate(orderId);
    };
    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    @Override
   public List<OrderStoreGoodRecordDTO>  selectOrderStoreSubListIdDTO(String orderStoreSubListId){
       return baseMapper.selectOrderStoreSubListIdDTO(orderStoreSubListId);
   };
}
