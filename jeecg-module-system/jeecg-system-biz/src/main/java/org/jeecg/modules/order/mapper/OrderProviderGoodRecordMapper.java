package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.order.dto.OrderProviderGoodRecordDTO;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商订单商品记录
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface OrderProviderGoodRecordMapper extends BaseMapper<OrderProviderGoodRecord> {
    /***
     * 查询等于供应商列表Id 的集合
     * @param orderProviderListId
     * @return
     */
    List<OrderProviderGoodRecord> selectOrderProviderListId(@Param("orderProviderListId") String orderProviderListId);

    /***
     * 查询等于供应商列表Id 的集合
     * @param orderProviderListId
     * @return
     */
    List<OrderProviderGoodRecordDTO> selectOrderProviderListIdDTO(@Param("orderProviderListId") String orderProviderListId);

    /**
     * 根据订单id查询记录商品信息
     *
     * @param orderId
     * @return
     */
    List<Map<String, Object>> getOrderProviderGoodRecordByOrderId(@Param("orderId") String orderId);

    List<Map<String, Object>> getOrderProviderGoodRecordByOrderIdAndMarketingDiscountCouponId(@Param("orderId") String orderId, @Param("marketingDiscountCouponId") String marketingDiscountCouponId);


    /**
     * 获取商品记录数据
     *
     * @param id
     * @return
     */
    Map<String,Object> iOrderProviderGoodRecordById(@Param("id") String id);

    /**
     * 根据订单id查询记录商品信息(评价)
     *
     * @param orderId
     * @return
     */
    List<Map<String, Object>> getOrderProviderGoodRecordByOrderIdEvaluate(@Param("orderId") String orderId);


    /**
     * 获取供应商订单1688需要的数值
     *
     * @param orderProviderListId
     * @return
     */
    List<Map<String, Object>> getGoodListByOrderProviderListId(@Param("orderProviderListId") String orderProviderListId);


    @Select("select opgr.*,gl.good_no from order_provider_good_record opgr LEFT JOIN good_list gl ON opgr.good_list_id=gl.id   where order_provider_list_id=#{id}")
    List<OrderProviderGoodRecordDTO> selectByOrderProviderListId(String id);
}
