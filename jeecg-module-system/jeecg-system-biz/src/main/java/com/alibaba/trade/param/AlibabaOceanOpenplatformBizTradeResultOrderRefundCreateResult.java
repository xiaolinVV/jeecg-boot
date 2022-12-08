package com.alibaba.trade.param;

public class AlibabaOceanOpenplatformBizTradeResultOrderRefundCreateResult {

    private String refundId;

    /**
     * @return 创建成功，退款id
     */
    public String getRefundId() {
        return refundId;
    }

    /**
     * 设置创建成功，退款id     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

}
