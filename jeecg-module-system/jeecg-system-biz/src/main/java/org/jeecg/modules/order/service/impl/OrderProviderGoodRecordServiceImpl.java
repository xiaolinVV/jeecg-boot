package org.jeecg.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.order.dto.OrderProviderGoodRecordDTO;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.mapper.OrderProviderGoodRecordMapper;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商订单商品记录
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
@Service
public class OrderProviderGoodRecordServiceImpl extends ServiceImpl<OrderProviderGoodRecordMapper, OrderProviderGoodRecord> implements IOrderProviderGoodRecordService {

    @Autowired
    IOrderRefundListService orderRefundListService;

    @Override
    public List<OrderProviderGoodRecord> selectOrderProviderListId(String orderProviderListId) {
        return baseMapper.selectOrderProviderListId(orderProviderListId);
    }

    @Override
    public List<Map<String,Object>> getOrderProviderGoodRecordByOrderId(String orderId) {
        List<Map<String, Object>> providerGoodRecordByOrderId = baseMapper.getOrderProviderGoodRecordByOrderId(orderId);
        providerGoodRecordByOrderId.forEach(m -> {
            //查询售后中数量 by zhangshaolin
            QueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new QueryWrapper<>();
            orderRefundListLambdaQueryWrapper.select("IFNULL(sum('refund_amount'),0) as ongoingRefundCount");
            orderRefundListLambdaQueryWrapper.eq("order_good_record_id", m.get("id"));
            orderRefundListLambdaQueryWrapper.in("status", "0", "1", "2", "3", "4", "5");
            Map<String, Object> map = orderRefundListService.getMap(orderRefundListLambdaQueryWrapper);
            m.put("ongoingRefundCount",map.get("ongoingRefundCount"));
        });
        return providerGoodRecordByOrderId;
    }

    @Override
    public List<Map<String, Object>> getOrderProviderGoodRecordByOrderIdAndMarketingDiscountCouponId(String orderId, String marketingDiscountCouponId) {
        return baseMapper.getOrderProviderGoodRecordByOrderIdAndMarketingDiscountCouponId(orderId,marketingDiscountCouponId);
    }

    /**
     * 根据订单id查询记录商品信息(评价)
     * @param orderId
     * @return
     */
    @Override
    public List<Map<String,Object>> getOrderProviderGoodRecordByOrderIdEvaluate(String orderId){
        return baseMapper.getOrderProviderGoodRecordByOrderIdEvaluate(orderId);
    };
    /***
     * 查询等于供应商列表Id 的集合
     * @param orderProviderListId
     * @return
     */
    @Override
    public List<OrderProviderGoodRecordDTO>  selectOrderProviderListIdDTO(String orderProviderListId){
        return baseMapper.selectOrderProviderListIdDTO(orderProviderListId);
    }

    @Override
    public Map<String, Object> iOrderProviderGoodRecordById(String id) {
        return baseMapper.iOrderProviderGoodRecordById(id);
    }

    @Override
    public List<Map<String, Object>> getGoodListByOrderProviderListId(String orderProviderListId) {
        return baseMapper.getGoodListByOrderProviderListId(orderProviderListId);
    }

}
