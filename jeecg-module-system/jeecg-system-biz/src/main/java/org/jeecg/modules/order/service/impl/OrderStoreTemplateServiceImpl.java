package org.jeecg.modules.order.service.impl;

import org.jeecg.modules.order.entity.OrderStoreTemplate;
import org.jeecg.modules.order.mapper.OrderStoreTemplateMapper;
import org.jeecg.modules.order.service.IOrderStoreTemplateService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺订单运费模板信息表
 * @Author: jeecg-boot
 * @Date:   2020-06-22
 * @Version: V1.0
 */
@Service
public class OrderStoreTemplateServiceImpl extends ServiceImpl<OrderStoreTemplateMapper, OrderStoreTemplate> implements IOrderStoreTemplateService {
    /**
     * 查询订单运费模板列表
     * @param orderStoreSubListIdList
     * @return
     */
   @Override
   public List<Map<String,Object>> getOrderStoreTemplateMaps(List<String> orderStoreSubListIdList){
        return baseMapper.getOrderStoreTemplateMaps(orderStoreSubListIdList);
    }
}
