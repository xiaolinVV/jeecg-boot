package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeRefundOpQueryOrderRefundOperationListParam extends AbstractAPIRequest<AlibabaTradeRefundOpQueryOrderRefundOperationListResult> {

    public AlibabaTradeRefundOpQueryOrderRefundOperationListParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.refund.OpQueryOrderRefundOperationList", 1);
    }

    private String refundId;

    /**
     * @return 退款单Id
     */
    public String getRefundId() {
        return refundId;
    }

    /**
     * 设置退款单Id     *
     * 参数示例：<pre>TQ1043162**46961198</pre>     
     * 此参数必填
     */
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    private String pageNo;

    /**
     * @return 当前页号
     */
    public String getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页号     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    private String pageSize;

    /**
     * @return 页大小
     */
    public String getPageSize() {
        return pageSize;
    }

    /**
     * 设置页大小     *
     * 参数示例：<pre>100</pre>     
     * 此参数必填
     */
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

}
