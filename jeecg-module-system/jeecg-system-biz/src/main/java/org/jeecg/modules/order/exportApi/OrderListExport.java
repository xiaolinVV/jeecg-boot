package org.jeecg.modules.order.exportApi;

import org.jeecg.modules.order.dto.OrderListExportDTO;
import org.jeecg.modules.order.dto.OrderStoreListExportDTO;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 声明一个类，相当于一个配置文件 bean → 注解@Component
 * 把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
 */

@Component
public class OrderListExport {
    @Autowired
    private IOrderListService orderListService;
    @Autowired
    private IOrderStoreListService orderStoreListService;
    /**
     * 平台订单导出
     * 方法必须包含三个参数：String，String，Map
     * @return 集合类型 ，包含字段：id,name,salary
     */
    public List<OrderListExportDTO> getOrderListDtoExport(String dsName, String datasetName, Map<String, Object> parameters) {

        List<OrderListExportDTO> orderListExportDTOList = orderListService.getOrderListDtoExport(parameters);

        return orderListExportDTOList;

    }

    /**
     * 店铺订单导出
     * @param dsName
     * @param datasetName
     * @param parameters
     * @return
     */
    public List<OrderStoreListExportDTO> getOrderStoreListDtoExport(String dsName, String datasetName, Map<String, Object> parameters) {

        List<OrderStoreListExportDTO> orderListExportDTOList = orderStoreListService.getOrderStoreListDtoExport(parameters);

        return orderListExportDTOList;

    }
}
