package org.jeecg.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jeecg.modules.order.entity.OrderProviderTemplate;
import org.jeecg.modules.order.mapper.OrderProviderTemplateMapper;
import org.jeecg.modules.order.service.IOrderProviderTemplateService;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 订单运费模板信息表
 * @Author: jeecg-boot
 * @Date:   2020-06-16
 * @Version: V1.0
 */
@Service
public class OrderProviderTemplateServiceImpl extends ServiceImpl<OrderProviderTemplateMapper, OrderProviderTemplate> implements IOrderProviderTemplateService {
    @Autowired
    private IProviderManageService iProviderManageService;

    /**
     * 查询订单运费模板列表
     * @param orderProviderIdList
     * @return
     */
    @Override
   public List<Map<String,Object>> getOrderProviderTemplateMaps(List<String> orderProviderIdList){
      return baseMapper.getOrderProviderTemplateMaps( orderProviderIdList);
   }

    @Override
    public List<Map<String, Object>> getOrderProviderTemplateByOrderId(String orderListId) {
        List<Map<String,Object>> orderProviderTemplateMaps= Lists.newArrayList();
        baseMapper.selectList(new LambdaQueryWrapper<OrderProviderTemplate>().eq(OrderProviderTemplate::getOrderListId,orderListId)).forEach(opt->{
            Map<String,Object> orderProviderTemplateMap= Maps.newHashMap();
            orderProviderTemplateMap.put("providerManageName",iProviderManageService.getOne(new LambdaQueryWrapper<ProviderManage>().eq(ProviderManage::getSysUserId,opt.getSysUserId())).getName());
            orderProviderTemplateMap.put("templateName",opt.getTemplateName());
            orderProviderTemplateMap.put("accountingRules",opt.getAccountingRules());//计费规则
            orderProviderTemplateMap.put("chargeMode",opt.getChargeMode());//计费方式
            orderProviderTemplateMap.put("quantity",opt.getQuantity());//总数
            orderProviderTemplateMap.put("shipFee",opt.getShipFee());//运费
            orderProviderTemplateMaps.add(orderProviderTemplateMap);
        });
        return orderProviderTemplateMaps;
    }
}
