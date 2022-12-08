package com.alibaba.trade.param;

public class AlibabaOrderBizInfo {

    private Boolean odsCyd;

    /**
     * @return 是否采源宝订单
     */
    public Boolean getOdsCyd() {
        return odsCyd;
    }

    /**
     * 设置是否采源宝订单     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setOdsCyd(Boolean odsCyd) {
        this.odsCyd = odsCyd;
    }

    private String accountPeriodTime;

    /**
     * @return 账期交易订单的到账时间
     */
    public String getAccountPeriodTime() {
        return accountPeriodTime;
    }

    /**
     * 设置账期交易订单的到账时间     *
     * 参数示例：<pre>yyyy-MM-dd HH:mm:ss</pre>     
     * 此参数必填
     */
    public void setAccountPeriodTime(String accountPeriodTime) {
        this.accountPeriodTime = accountPeriodTime;
    }

    private Boolean creditOrder;

    /**
     * @return 为true，表示下单时选择了诚e赊交易方式。注意不等同于“诚e赊支付”，支付时有可能是支付宝付款，具体支付方式查询tradeTerms.payWay
     */
    public Boolean getCreditOrder() {
        return creditOrder;
    }

    /**
     * 设置为true，表示下单时选择了诚e赊交易方式。注意不等同于“诚e赊支付”，支付时有可能是支付宝付款，具体支付方式查询tradeTerms.payWay     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setCreditOrder(Boolean creditOrder) {
        this.creditOrder = creditOrder;
    }

    private AlibabaCreditOrderForDetail creditOrderDetail;

    /**
     * @return 诚e赊支付详情，只有使用诚e赊付款时返回
     */
    public AlibabaCreditOrderForDetail getCreditOrderDetail() {
        return creditOrderDetail;
    }

    /**
     * 设置诚e赊支付详情，只有使用诚e赊付款时返回     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCreditOrderDetail(AlibabaCreditOrderForDetail creditOrderDetail) {
        this.creditOrderDetail = creditOrderDetail;
    }

    private AlibabaOrderPreOrderForRead preOrderInfo;

    /**
     * @return 预订单信息
     */
    public AlibabaOrderPreOrderForRead getPreOrderInfo() {
        return preOrderInfo;
    }

    /**
     * 设置预订单信息     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setPreOrderInfo(AlibabaOrderPreOrderForRead preOrderInfo) {
        this.preOrderInfo = preOrderInfo;
    }

    private AlibabaLstTradeInfo lstOrderInfo;

    /**
     * @return 零售通订单信息
     */
    public AlibabaLstTradeInfo getLstOrderInfo() {
        return lstOrderInfo;
    }

    /**
     * 设置零售通订单信息     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setLstOrderInfo(AlibabaLstTradeInfo lstOrderInfo) {
        this.lstOrderInfo = lstOrderInfo;
    }

}
