package org.jeecg.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.order.dto.OrderProviderGoodRecordDTO;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.mapper.OrderProviderGoodRecordMapper;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
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

    @Override
    public List<OrderProviderGoodRecord> selectOrderProviderListId(String orderProviderListId) {
        return baseMapper.selectOrderProviderListId(orderProviderListId);
    }

    @Override
    public List<Map<String,Object>> getOrderProviderGoodRecordByOrderId(String orderId) {
        return baseMapper.getOrderProviderGoodRecordByOrderId(orderId);
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
