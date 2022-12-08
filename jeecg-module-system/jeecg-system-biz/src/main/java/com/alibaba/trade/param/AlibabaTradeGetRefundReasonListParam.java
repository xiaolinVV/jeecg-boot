package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeGetRefundReasonListParam extends AbstractAPIRequest<AlibabaTradeGetRefundReasonListResult> {

    public AlibabaTradeGetRefundReasonListParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.getRefundReasonList", 1);
    }

    private Long orderId;

    /**
     * @return 主订单id
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置主订单id     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    private long[] orderEntryIds;

    /**
     * @return 子订单id
     */
    public long[] getOrderEntryIds() {
        return orderEntryIds;
    }

    /**
     * 设置子订单id     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setOrderEntryIds(long[] orderEntryIds) {
        this.orderEntryIds = orderEntryIds;
    }

    private String goodsStatus;

    /**
     * @return 货物状态
     */
    public String getGoodsStatus() {
        return goodsStatus;
    }

    /**
     * 设置货物状态     *
     * 参数示例：<pre>售中等待买家发货:”refundWaitSellerSend"; 售中等待买家收货:"refundWaitBuyerReceive"; 售中已收货（未确认完成交易）:"refundBuyerReceived" 售后未收货:"aftersaleBuyerNotReceived"; 售后已收到货:"aftersaleBuyerReceived"</pre>     
     * 此参数必填
     */
    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

}
