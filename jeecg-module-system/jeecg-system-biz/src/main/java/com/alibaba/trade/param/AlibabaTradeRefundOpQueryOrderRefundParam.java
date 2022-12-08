package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeRefundOpQueryOrderRefundParam extends AbstractAPIRequest<AlibabaTradeRefundOpQueryOrderRefundResult> {

    public AlibabaTradeRefundOpQueryOrderRefundParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.refund.OpQueryOrderRefund", 1);
    }

    private String refundId;

    /**
     * @return 退款单业务主键 TQ+ID
     */
    public String getRefundId() {
        return refundId;
    }

    /**
     * 设置退款单业务主键 TQ+ID     *
     * 参数示例：<pre>TQ11173622***991577</pre>     
     * 此参数必填
     */
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    private Boolean needTimeOutInfo;

    /**
     * @return 需要退款单的超时信息
     */
    public Boolean getNeedTimeOutInfo() {
        return needTimeOutInfo;
    }

    /**
     * 设置需要退款单的超时信息     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setNeedTimeOutInfo(Boolean needTimeOutInfo) {
        this.needTimeOutInfo = needTimeOutInfo;
    }

    private Boolean needOrderRefundOperation;

    /**
     * @return 需要退款单伴随的所有退款操作信息
     */
    public Boolean getNeedOrderRefundOperation() {
        return needOrderRefundOperation;
    }

    /**
     * 设置需要退款单伴随的所有退款操作信息     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setNeedOrderRefundOperation(Boolean needOrderRefundOperation) {
        this.needOrderRefundOperation = needOrderRefundOperation;
    }

}
