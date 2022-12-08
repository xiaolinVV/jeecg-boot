package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.dto.OrderStoreListDTO;
import org.jeecg.modules.order.dto.OrderStoreListExportDTO;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.vo.OrderStoreListVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface OrderStoreListMapper extends BaseMapper<OrderStoreList> {
    /**
     * 订单列表
     * @param page
     * @param orderStoreListVO
     * @return
     */
    IPage<OrderStoreListDTO> getOrderStoreListDto(Page<OrderStoreList> page, @Param("orderStoreListVO") OrderStoreListVO orderStoreListVO);
    /**
     * 待支付订单超时数据
     * @param hour
     * @return
     */
    List<OrderStoreList>   getCancelOrderStoreList(@Param("hour") String hour);
    /**
     * 返回待支付订单计时器(秒)
     * @param id
     * @param hour
     * @return
     */
    String getOrderStoreListTimer(@Param("id") String id,@Param("hour") String hour);
    /**
     * 确认订单超时数据
     * @param hour
     * @return
     */
    List<OrderStoreList>   getStoreConfirmReceiptOrderList(@Param("hour") String hour);
    /**
     * 返回确认收货订单计时器(秒)
     * @param id
     * @param hour
     * @return
     */
    String getStoreConfirmReceiptTimer(@Param("id") String id,@Param("hour") String hour);

    List<OrderStoreList> getOrderCompletion(@Param("hour") String hour);
    /**
     * 订单导出列表
     * @param orderStoreListVO
     * @return
     */
    List<OrderStoreListExportDTO> getOrderStoreListDtoExport(@Param("orderStoreListVO") Map<String,Object> orderStoreListVO);


}
