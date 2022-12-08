package com.alibaba.trade.param;

public class AlibabaTradeFastOffer {

    private String offerId;

    /**
     * @return 下单失败的商品
     */
    public String getOfferId() {
        return offerId;
    }

    /**
     * 设置下单失败的商品     *
     * 参数示例：<pre>554456348334</pre>     
     * 此参数必填
     */
    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    private String specId;

    /**
     * @return 下单失败商品的规格ID
     */
    public String getSpecId() {
        return specId;
    }

    /**
     * 设置下单失败商品的规格ID     *
     * 参数示例：<pre>b266e0726506185beaf205cbae88530d</pre>     
     * 此参数必填
     */
    public void setSpecId(String specId) {
        this.specId = specId;
    }

    private String errorCode;

    /**
     * @return 下单失败的错误编码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置下单失败的错误编码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorMessage;

    /**
     * @return 下单失败的错误描述
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置下单失败的错误描述     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
