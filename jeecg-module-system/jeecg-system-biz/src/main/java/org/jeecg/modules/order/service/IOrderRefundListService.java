package org.jeecg.modules.order.service;

import com.github.yulichang.base.MPJBaseService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.order.dto.ApplyOrderRefundDto;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.entity.OrderStoreList;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date: 2023-04-20
 * @Version: V1.0
 */
public interface IOrderRefundListService extends MPJBaseService<OrderRefundList> {

    /**
     * 通过id 查询
     *
     * @param id
     * @return
     */
    OrderRefundList getOrderRefundListById(String id);

    /**
     * 售后单退款逻辑处理
     *
     * @param orderRefundList     售后单对象
     * @param actualRefundPrice   实际退款现金
     * @param actualRefundBalance 实际退款余额
     */
    void refund(OrderRefundList orderRefundList, BigDecimal actualRefundPrice, BigDecimal actualRefundBalance);

    /**
     * 店铺订单商品售后申请
     *
     * @param applyOrderRefundDto               售后请求参数对象
     * @param memberId                          会员id
     * @param exchangeMemberShippingAddressJson 换货地址json
     */
    void applyOrderStoreRefund(ApplyOrderRefundDto applyOrderRefundDto, String memberId, String exchangeMemberShippingAddressJson);

    /**
     * 平台订单商品售后申请
     *
     * @param applyOrderRefundDto               售后请求参数对象
     * @param memberId                          会员id
     * @param exchangeMemberShippingAddressJson 换货地址json
     */
    void applyOrderRefund(ApplyOrderRefundDto applyOrderRefundDto, String memberId, String exchangeMemberShippingAddressJson);

    /**
     * 查询用户在订单下的所有售后单(进行中或已完成)
     *
     * @param memberId 会员id
     * @param orderId  订单id
     * @return
     */
    List<OrderRefundList> getOrderRefundListByMemberIdAndOrderId(String memberId, String orderId);

    /**
     * 修改申请
     *
     * @param orderRefundList
     * @return
     */
    Result<String> editApplyRefund(OrderRefundList orderRefundList);

    /**
     * 退款退店铺优惠券
     *
     * @param orderStoreList
     * @param orderRefundList
     */
    void refundForSendBackOrderStoreMarketingDiscountCoupon(OrderStoreList orderStoreList, OrderRefundList orderRefundList);


}
