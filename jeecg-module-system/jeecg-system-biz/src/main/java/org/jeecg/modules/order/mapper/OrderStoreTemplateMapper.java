package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.entity.OrderStoreTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺订单运费模板信息表
 * @Author: jeecg-boot
 * @Date:   2020-06-22
 * @Version: V1.0
 */
public interface OrderStoreTemplateMapper extends BaseMapper<OrderStoreTemplate> {

    /**
     * 查询订单运费模板列表
     * @param orderStoreSubListIdList
     * @return
     */
    List<Map<String,Object>> getOrderStoreTemplateMaps(@Param("orderStoreSubListIdList") List<String> orderStoreSubListIdList);
}
