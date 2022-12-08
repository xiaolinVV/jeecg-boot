package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeRefundOpQueryBatchRefundByOrderIdAndStatusParam extends
        AbstractAPIRequest<AlibabaTradeRefundOpQueryBatchRefundByOrderIdAndStatusResult> {

    public AlibabaTradeRefundOpQueryBatchRefundByOrderIdAndStatusParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.refund.OpQueryBatchRefundByOrderIdAndStatus", 1);
    }

    private String orderId;

    /**
     * @return 订单id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id     *
     * 参数示例：<pre>151267031**8969811</pre>     
     * 此参数必填
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String queryType;

    /**
     * @return 1：活动；3:退款成功（只支持退款中和退款成功）
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     * 设置1：活动；3:退款成功（只支持退款中和退款成功）     *
     * 参数示例：<pre>3</pre>     
     * 此参数必填
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

}
