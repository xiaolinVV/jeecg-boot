package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
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
public interface IOrderProviderGoodRecordService extends IService<OrderProviderGoodRecord> {
    List<OrderProviderGoodRecord> selectOrderProviderListId(String orderProviderListId);

    /**
     * 根据订单id查询记录商品信息
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getOrderProviderGoodRecordByOrderId(String orderId);
    List<Map<String,Object>> getOrderProviderGoodRecordByOrderIdAndMarketingDiscountCouponId(String orderId,String marketingDiscountCouponId);
    /**
     * 根据订单id查询记录商品信息(评价)
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getOrderProviderGoodRecordByOrderIdEvaluate(String orderId);
    /***
     * 查询等于供应商列表Id 的集合
     * @param orderProviderListId
     * @return
     */
    List<OrderProviderGoodRecordDTO>  selectOrderProviderListIdDTO(@Param("orderProviderListId")String orderProviderListId);


    /**
     * 获取商品记录数据
     *
     * @param id
     * @return
     */
    Map<String,Object> iOrderProviderGoodRecordById(String id);


    /**
     * 获取供应商订单1688需要的数值
     *
     * @param orderProviderListId
     * @return
     */
    List<Map<String,Object>> getGoodListByOrderProviderListId(String orderProviderListId);

}
