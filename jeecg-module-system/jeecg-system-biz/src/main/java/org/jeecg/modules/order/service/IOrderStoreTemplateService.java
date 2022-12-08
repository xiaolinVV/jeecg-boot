package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.order.entity.OrderStoreTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺订单运费模板信息表
 * @Author: jeecg-boot
 * @Date:   2020-06-22
 * @Version: V1.0
 */
public interface IOrderStoreTemplateService extends IService<OrderStoreTemplate> {
    /**
     * 查询订单运费模板列表
     * @param orderStoreSubListIdList
     * @return
     */
    List<Map<String,Object>> getOrderStoreTemplateMaps(List<String> orderStoreSubListIdList);

}
