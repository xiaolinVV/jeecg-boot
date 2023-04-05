package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.order.dto.OrderListDTO;
import org.jeecg.modules.order.dto.OrderListExportDTO;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.vo.OrderListVO;
import org.jeecg.modules.pay.entity.PayOrderCarLog;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
public interface IOrderListService extends IService<OrderList> {
    /**
     * 平台订单列表
     *
     * @param page
     * @param orderListVO
     * @param sysUserId
     * @return
     */
    IPage<OrderListDTO> getOrderListDto(Page<OrderList> page, OrderListVO orderListVO, String sysUserId);

    /**
     * 平台订单列表
     *
     * @param page
     * @param orderListVO
     * @param sysUserId
     * @return
     */
    IPage<OrderListDTO> getOrderListDtoToSendTheGoods(Page<OrderList> page, OrderListVO orderListVO, String sysUserId);


    /**
     * 平台订单提交
     *
     * @param goods
     * @return
     */
    public OrderList submitOrderGoods(List<Map<String, Object>> goods, String memberId, String orderJson, MemberShippingAddress memberShippingAddress, String sysUserId, int model, String longitude, String latitude);

    /**
     * 支付成功订单
     *
     * @param id
     * @return
     */
    public Boolean paySuccessOrder(String id, int model, PayOrderCarLog payOrderCarLog);

    /**
     * 根据会员id和状态查询合并订单信息
     *
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> getOrderListByMemberIdAndStatus(Page<Map<String, Object>> page, Map<String, Object> paramMap);

    /**
     * 返回 OrderListDTO对象
     *
     * @param id
     * @return
     */
    OrderListDTO orderListDtoOne(String id);


    /**
     * 关闭交易
     *
     * @param id
     * @param closeExplain
     * @param closeType
     * @return
     */
    public void abrogateOrder(String id, String closeExplain, String closeType);


    /**
     * 订单退款并关闭
     *
     * @param id
     * @param closeExplain
     * @param closeType
     */
    public Result<?> refundAndAbrogateOrder(String id, String closeExplain, String closeType);


    /**
     * 确认收货
     *
     * @param id
     */
    public void affirmOrder(String id);

    /**
     * //是否全部发货,修改orderList的状态内容
     *
     * @param orderProviderList
     * @return
     */
    public void ShipmentOrderModification(OrderProviderList orderProviderList);

    /**
     * 待支付订单超时数据
     *
     * @param hour
     * @return
     */
    List<OrderList> getCancelOrderList(String hour);

    /**
     * 定时器取消订单
     */

    public void cancelOrderListJob();

    /**
     * 待支付订单计时器
     *
     * @return
     */
    public String prepaidOrderTimer(String id, Integer isPlatform);

    /**
     * 返回待支付订单计时器(秒)
     *
     * @param id
     * @param hour
     * @return
     */
    String getOrderListTimer(String id, String hour);

    /**
     * 返回确认订单计时器(秒)
     *
     * @param id
     * @param hour
     * @return
     */
    String getConfirmReceiptTimer(String id, String hour);

    /**
     * 平台/店铺确认收货订单(秒)
     *
     * @param id
     * @param isPlatform
     * @return
     */
    public String confirmReceiptTimer(String id, Integer isPlatform);

    /**
     * 确认订单超时数据
     *
     * @param hour
     * @return
     */
    List<OrderList> getConfirmReceiptOrderList(String hour);

    /**
     * 定时器确认收货订单
     */
    void confirmReceiptOrderListJob();

    void confirmReceiptOrderJob();

    void accomplishOrder(String id);

    /**
     * 购买人订单随机数显示会员信息
     *
     * @return
     */
    List<Map<String, Object>> getOrderMemberHubble();

    /**
     * 订单导出列表
     *
     * @param orderListVO
     * @return
     */
    List<OrderListExportDTO> getOrderListDtoExport(Map<String, Object> orderListVO);

    void orderOff(OrderListDTO orderListDTO);

    List<Map<String, Object>> orderSumList();


    /**
     * 根据专区id
     *
     * @param paramMap
     * @return
     */
    int getPrefectureGoodCount(Map<String, Object> paramMap);

    /**
     * excel导出
     * @param queryWrapper
     * @param response
     */
    void exportXls(OrderListVO queryWrapper, HttpServletResponse response) throws IOException;

    /**
     * 订单发货
     * @param listMap
     * @return
     */
    Result<String> ordereDlivery(String listMap);


}
