package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.order.entity.OrderProviderTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 订单运费模板信息表
 * @Author: jeecg-boot
 * @Date:   2020-06-16
 * @Version: V1.0
 */
public interface IOrderProviderTemplateService extends IService<OrderProviderTemplate> {
    /**
     * 查询订单运费模板列表
     * @param orderProviderIdList
     * @return
     */
    List<Map<String,Object>> getOrderProviderTemplateMaps(List<String> orderProviderIdList);

    /**
     * 根据订单id查询订单运费模板信息
     * 张靠勤   2021-3-18
     * @param orderListId
     * @return
     */
    public List<Map<String,Object>> getOrderProviderTemplateByOrderId(String orderListId);

}
