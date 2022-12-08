package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.order.dto.OrderListDTO;
import org.jeecg.modules.order.dto.OrderListExportDTO;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.vo.OrderListVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface OrderListMapper extends BaseMapper<OrderList> {
    /**
     * 订单列表
     * @param page
     * @param orderListVO
     * @return
     */
    IPage<OrderListDTO> getOrderListDto(Page<OrderList> page,@Param("orderListVO") OrderListVO orderListVO);

    /**
     * 订单列表
     * @param orderListVO
     * @return
     */
    ArrayList<OrderListDTO> orderListDtoExport(@Param("orderListVO") OrderListVO orderListVO);


    /**
     * 根据会员id和状态查询合并订单信息
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getOrderListByMemberIdAndStatus(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

    List<OrderListDTO> orderListDtoOne(@Param("id") String id);

    /**
     * 待支付订单超时数据
     * @param hour
     * @return
     */
    List<OrderList>   getCancelOrderList(@Param("hour") String hour);
    /**
     * 返回待支付订单计时器(秒)
     * @param id
     * @param hour
     * @return
     */
    String getOrderListTimer(@Param("id") String id,@Param("hour") String hour);
    /**
     * 确认订单超时数据
     * @param hour
     * @return
     */
    List<OrderList>   getConfirmReceiptOrderList(@Param("hour") String hour);

    /**
     * 返回确认订单计时器(秒)
     * @param id
     * @param hour
     * @return
     */
    String getConfirmReceiptTimer(@Param("id") String id,@Param("hour") String hour);

    List<OrderList> getOrderCompletion(@Param("hour") String hour);

    /**
     * 购买人订单随机数显示会员信息
     * @return
     */
    List<Map<String,Object>> getOrderMemberHubble();


    /**
     * 订单导出列表
     * @param orderListVO
     * @return
     */
    List<OrderListExportDTO> getOrderListDtoExport(@Param("orderListVO") Map<String,Object> orderListVO);

    List<Map<String,Object>> orderSumList();

    /**
     * 根据专区id
     * @param paramMap
     * @return
     */
    int getPrefectureGoodCount(@Param("paramMap") Map<String,Object> paramMap);

    @Select("select ol.*,ml.nick_name, ml.phone AS memberPhone ,ml.id as memberListId  From order_list ol left JOIN member_list ml   on ol.member_list_id=ml.id  where ol.id=#{orderListId}")
    OrderListDTO selectOrderListById(String orderListId);

    IPage<OrderListDTO> getOrderListDtoNew(Page<OrderList> page,@Param("orderListVO") OrderListVO orderListVO);
}
