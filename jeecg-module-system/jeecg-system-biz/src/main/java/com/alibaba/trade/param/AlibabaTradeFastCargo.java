package com.alibaba.trade.param;

public class AlibabaTradeFastCargo {

    private Long offerId;

    /**
     * @return 商品对应的offer id
     */
    public Long getOfferId() {
        return offerId;
    }

    /**
     * 设置商品对应的offer id     *
     * 参数示例：<pre>554456348334</pre>     
     * 此参数必填
     */
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    private String specId;

    /**
     * @return 商品规格id
     */
    public String getSpecId() {
        return specId;
    }

    /**
     * 设置商品规格id     *
     * 参数示例：<pre>b266e0726506185beaf205cbae88530d</pre>     
     * 此参数必填
     */
    public void setSpecId(String specId) {
        this.specId = specId;
    }

    private Double quantity;

    /**
     * @return 商品数量(计算金额用)
     */
    public Double getQuantity() {
        return quantity;
    }

    /**
     * 设置商品数量(计算金额用)     *
     * 参数示例：<pre>5</pre>     
     * 此参数必填
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

}
