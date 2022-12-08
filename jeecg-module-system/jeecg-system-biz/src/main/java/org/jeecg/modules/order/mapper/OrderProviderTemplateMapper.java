package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.entity.OrderProviderTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 订单运费模板信息表
 * @Author: jeecg-boot
 * @Date: 2020-06-16
 * @Version: V1.0
 */
public interface OrderProviderTemplateMapper extends BaseMapper<OrderProviderTemplate> {

    /**
     * 查询订单运费模板列表
     *
     * @param orderProviderIdList
     * @return
     */
    List<Map<String, Object>> getOrderProviderTemplateMaps(@Param("orderProviderIdList") List<String> orderProviderIdList);


    List<Map<String, Object>> getOrderProviderTemplateMap(@Param("orderProviderId")String orderProviderId);
}
