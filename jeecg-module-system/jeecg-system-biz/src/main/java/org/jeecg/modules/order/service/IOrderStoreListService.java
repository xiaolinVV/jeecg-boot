package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.order.dto.OrderStoreListDTO;
import org.jeecg.modules.order.dto.OrderStoreListExportDTO;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.vo.OrderStoreListVO;
import org.jeecg.modules.pay.entity.PayOrderCarLog;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface IOrderStoreListService extends IService<OrderStoreList> {
    /**
     * 平台订单列表
     * @param page
     * @param orderStoreListVO
     * @param sysUserId
     * @return
     */
    IPage<OrderStoreListDTO> getOrderStoreListDto(Page<OrderStoreList> page, OrderStoreListVO orderStoreListVO, String sysUserId);
    /**
     * 发货后平台订单列表
     * @param page
     * @param orderStoreListVO
     * @param sysUserId
     * @return
     */
    IPage<OrderStoreListDTO> getOrderListDtoWaitForReceiving(Page<OrderStoreList> page, OrderStoreListVO orderStoreListVO, String sysUserId);


    /**
     * 平台订单提交
     * @param :storeGood
     * @return
     */
    public OrderStoreList submitOrderStoreGoods(Map<String,Object> storeGood, String memberId, String orderJson, MemberShippingAddress memberShippingAddress,String longitude,String latitude);

    /**
     * 支付成功订单
     * @param id
     * @return
     */
    public Boolean paySuccessOrder(String id, PayOrderCarLog payOrderCarLog);

    /**
     * 关闭交易
     *
     * @param id
     * @param CloseExplain
     * @param closeType
     * @return
     */
    public void abrogateOrder(String id, String CloseExplain, String closeType);


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
     * @param id
     */
    public void affirmOrder(String id);


    /**
     * 待支付订单超时数据
     * @param hour
     * @return
     */
    List<OrderStoreList>   getCancelOrderStoreList( String hour);

    /**
     * 定时器取消订单
     */

    public void cancelOrderStoreListJob();

    /**
     * 返回待支付订单计时器(秒)
     * @param id
     * @param hour
     * @return
     */
    public String getOrderStoreListTimer(String id,String hour);
    /**
     * 确认收货订单超时数据
     * @param hour
     * @return
     */
    List<OrderStoreList>   getStoreConfirmReceiptOrderList(String hour);


    /**
     * 返回确认收货订单计时器(秒)
     * @param id
     * @param hour
     * @return
     */
    String getStoreConfirmReceiptTimer( String id,String hour);
    /**
     * 定时器确认收货
     */
    void storeConfirmReceiptOrderList();

    void confirmReceiptOrderJob();


    void accomplishStoreOrder(String id);
    /**
     * 订单导出列表
     * @param orderStoreListVO
     * @return
     */
    List<OrderStoreListExportDTO> getOrderStoreListDtoExport(Map<String,Object> orderStoreListVO);

}
