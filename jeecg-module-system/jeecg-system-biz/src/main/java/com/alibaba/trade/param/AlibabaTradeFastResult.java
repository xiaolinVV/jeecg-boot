package com.alibaba.trade.param;

public class AlibabaTradeFastResult {

    private Long totalSuccessAmount;

    /**
     * @return 下单成功的订单总金额，单位：分
     */
    public Long getTotalSuccessAmount() {
        return totalSuccessAmount;
    }

    /**
     * 设置下单成功的订单总金额，单位：分     *
     * 参数示例：<pre>1000</pre>     
     * 此参数必填
     */
    public void setTotalSuccessAmount(Long totalSuccessAmount) {
        this.totalSuccessAmount = totalSuccessAmount;
    }

    private String orderId;

    /**
     * @return 下单成功后的订单id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置下单成功后的订单id     *
     * 参数示例：<pre>60241415417789305</pre>     
     * 此参数必填
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private AlibabaTradeFastOffer[] failedOfferList;

    /**
     * @return 下单失败的商品信息
     */
    public AlibabaTradeFastOffer[] getFailedOfferList() {
        return failedOfferList;
    }

    /**
     * 设置下单失败的商品信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFailedOfferList(AlibabaTradeFastOffer[] failedOfferList) {
        this.failedOfferList = failedOfferList;
    }

    private Long postFee;

    /**
     * @return 创建订单时的运费
     */
    public Long getPostFee() {
        return postFee;
    }

    /**
     * 设置创建订单时的运费     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPostFee(Long postFee) {
        this.postFee = postFee;
    }

}
