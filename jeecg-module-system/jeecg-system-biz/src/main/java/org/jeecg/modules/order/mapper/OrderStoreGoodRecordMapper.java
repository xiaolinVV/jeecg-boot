package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.dto.OrderStoreGoodRecordDTO;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺订单商品记录列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface OrderStoreGoodRecordMapper extends BaseMapper<OrderStoreGoodRecord> {

    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    List<OrderStoreGoodRecord>  selectOrderStoreSubListId(@Param("orderStoreSubListId")String orderStoreSubListId);

    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    List<OrderStoreGoodRecordDTO>  selectOrderStoreSubListIdDTO(@Param("orderStoreSubListId")String orderStoreSubListId);
    /**
     * 根据订单id查询记录商品信息
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getOrderStoreGoodRecordByOrderId(@Param("orderId") String orderId);

    /**
     * 根据订单id、优惠券记录id查询记录商品信息
     *
     * @param orderId
     * @param marketingDiscountCouponId
     * @return
     */
    List<Map<String, Object>> getOrderStoreGoodRecordByOrderIdAndMarketingDiscountCouponId(@Param("orderId") String orderId, @Param("marketingDiscountCouponId") String marketingDiscountCouponId);


    /**
     * 根据订单id查询记录商品信息(评价)
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getOrderStoreGoodRecordByOrderIdEvaluate(@Param("orderId") String orderId);
}
