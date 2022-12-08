package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IOrderStoreGoodRecordService extends IService<OrderStoreGoodRecord> {
    /**
     * 根据订单id查询记录商品信息
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getOrderStoreGoodRecordByOrderId(String orderId);
    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    List<OrderStoreGoodRecord>  selectOrderStoreSubListId(String orderStoreSubListId);
    /**
     * 根据订单id查询记录商品信息(评价)
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getOrderStoreGoodRecordByOrderIdEvaluate(String orderId);
    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    List<OrderStoreGoodRecordDTO>  selectOrderStoreSubListIdDTO(String orderStoreSubListId);
}
