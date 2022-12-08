package com.alibaba.trade.param;

public class AlibabaCreateOrderPreviewResultCargoModel {

    private Double amount;

    /**
     * @return 产品总金额
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * 设置产品总金额     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private String message;

    /**
     * @return 返回信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置返回信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMessage(String message) {
        this.message = message;
    }

    private Double finalUnitPrice;

    /**
     * @return 最终单价
     */
    public Double getFinalUnitPrice() {
        return finalUnitPrice;
    }

    /**
     * 设置最终单价     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFinalUnitPrice(Double finalUnitPrice) {
        this.finalUnitPrice = finalUnitPrice;
    }

    private String specId;

    /**
     * @return 规格ID，offer内唯一
     */
    public String getSpecId() {
        return specId;
    }

    /**
     * 设置规格ID，offer内唯一     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSpecId(String specId) {
        this.specId = specId;
    }

    private Long skuId;

    /**
     * @return 规格ID，全局唯一
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置规格ID，全局唯一     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    private String resultCode;

    /**
     * @return 返回码
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * 设置返回码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    private Long offerId;

    /**
     * @return 商品ID
     */
    public Long getOfferId() {
        return offerId;
    }

    /**
     * 设置商品ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    private AlibabaTradePromotionModel[] cargoPromotionList;

    /**
     * @return 商品优惠列表
     */
    public AlibabaTradePromotionModel[] getCargoPromotionList() {
        return cargoPromotionList;
    }

    /**
     * 设置商品优惠列表     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCargoPromotionList(AlibabaTradePromotionModel[] cargoPromotionList) {
        this.cargoPromotionList = cargoPromotionList;
    }

}
