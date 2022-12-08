package org.jeecg.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.order.dto.OrderStoreGoodRecordDTO;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.mapper.OrderStoreGoodRecordMapper;
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
    private OrderStoreGoodRecordMapper orderStoreGoodRecordMapper;

    @Override
    public List<Map<String,Object>> getOrderStoreGoodRecordByOrderId(String orderId) {
        return orderStoreGoodRecordMapper.getOrderStoreGoodRecordByOrderId(orderId);
    }
    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    @Override
    public List<OrderStoreGoodRecord>  selectOrderStoreSubListId(String orderStoreSubListId){
        return orderStoreGoodRecordMapper.selectOrderStoreSubListId(orderStoreSubListId);
    };
    /**
     * 根据订单id查询记录商品信息(评价)
     * @param orderId
     * @return
     */
    @Override
   public List<Map<String,Object>> getOrderStoreGoodRecordByOrderIdEvaluate(String orderId){
        return orderStoreGoodRecordMapper.getOrderStoreGoodRecordByOrderIdEvaluate(orderId);
    };
    /***
     * 查询等于店铺列表Id 的集合
     * @param orderStoreSubListId
     * @return
     */
    @Override
   public List<OrderStoreGoodRecordDTO>  selectOrderStoreSubListIdDTO(String orderStoreSubListId){
       return orderStoreGoodRecordMapper.selectOrderStoreSubListIdDTO(orderStoreSubListId);
   };
}
