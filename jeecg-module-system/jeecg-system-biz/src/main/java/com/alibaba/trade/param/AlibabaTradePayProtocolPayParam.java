package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradePayProtocolPayParam extends AbstractAPIRequest<AlibabaTradePayProtocolPayResult> {

    public AlibabaTradePayProtocolPayParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.pay.protocolPay", 1);
    }

    private Long orderId;

    /**
     * @return 订单ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID     *
     * 参数示例：<pre>123455678</pre>     
     * 此参数必填
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}
