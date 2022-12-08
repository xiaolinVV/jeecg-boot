package com.alibaba.trade.param;

public class AlibabaTradePromotionModel {

    private String promotionId;

    /**
     * @return 优惠券ID
     */
    public String getPromotionId() {
        return promotionId;
    }

    /**
     * 设置优惠券ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    private Boolean selected;

    /**
     * @return 是否默认选中
     */
    public Boolean getSelected() {
        return selected;
    }

    /**
     * 设置是否默认选中     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    private String text;

    /**
     * @return 优惠券名称
     */
    public String getText() {
        return text;
    }

    /**
     * 设置优惠券名称     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setText(String text) {
        this.text = text;
    }

    private String desc;

    /**
     * @return 优惠券描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置优惠券描述     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    private Boolean freePostage;

    /**
     * @return 是否免邮
     */
    public Boolean getFreePostage() {
        return freePostage;
    }

    /**
     * 设置是否免邮     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFreePostage(Boolean freePostage) {
        this.freePostage = freePostage;
    }

    private Long discountFee;

    /**
     * @return 减去金额，单位为分
     */
    public Long getDiscountFee() {
        return discountFee;
    }

    /**
     * 设置减去金额，单位为分     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setDiscountFee(Long discountFee) {
        this.discountFee = discountFee;
    }

}
