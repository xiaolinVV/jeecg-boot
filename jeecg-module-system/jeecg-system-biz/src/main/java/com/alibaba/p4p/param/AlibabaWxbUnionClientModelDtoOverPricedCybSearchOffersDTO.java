package com.alibaba.p4p.param;

public class AlibabaWxbUnionClientModelDtoOverPricedCybSearchOffersDTO {

    private String title;

    /**
     * @return todo
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置todo     *
     * 参数示例：<pre>todo</pre>     
     * 此参数必填
     */
    public void setTitle(String title) {
        this.title = title;
    }

    private String imgUrl;

    /**
     * @return todo
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置todo     *
     * 参数示例：<pre>todo</pre>     
     * 此参数必填
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private Long offerId;

    /**
     * @return todo
     */
    public Long getOfferId() {
        return offerId;
    }

    /**
     * 设置todo     *
     * 参数示例：<pre>todo</pre>     
     * 此参数必填
     */
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    private Long soldOut;

    /**
     * @return todo
     */
    public Long getSoldOut() {
        return soldOut;
    }

    /**
     * 设置todo     *
     * 参数示例：<pre>todo</pre>     
     * 此参数必填
     */
    public void setSoldOut(Long soldOut) {
        this.soldOut = soldOut;
    }

    private Double superBuyerPrice;

    /**
     * @return todo
     */
    public Double getSuperBuyerPrice() {
        return superBuyerPrice;
    }

    /**
     * 设置todo     *
     * 参数示例：<pre>todo</pre>     
     * 此参数必填
     */
    public void setSuperBuyerPrice(Double superBuyerPrice) {
        this.superBuyerPrice = superBuyerPrice;
    }

    private Boolean enable;

    /**
     * @return todo
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * 设置todo     *
     * 参数示例：<pre>todo</pre>     
     * 此参数必填
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    private String profit;

    /**
     * @return todo
     */
    public String getProfit() {
        return profit;
    }

    /**
     * 设置todo     *
     * 参数示例：<pre>todo</pre>     
     * 此参数必填
     */
    public void setProfit(String profit) {
        this.profit = profit;
    }

    private Double currentPrice;

    /**
     * @return 分销价
     */
    public Double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * 设置分销价     *
     * 参数示例：<pre>11.2</pre>     
     * 此参数必填
     */
    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    private String[] offerTags;

    /**
     * @return 标签数组
     */
    public String[] getOfferTags() {
        return offerTags;
    }

    /**
     * 设置标签数组     *
     * 参数示例：<pre>["48小时发货", "15+天包换", "免费赊账"]</pre>     
     * 此参数必填
     */
    public void setOfferTags(String[] offerTags) {
        this.offerTags = offerTags;
    }

    private Double channelPrice;

    /**
     * @return 渠道专属价
     */
    public Double getChannelPrice() {
        return channelPrice;
    }

    /**
     * 设置渠道专属价     *
     * 参数示例：<pre>10.1</pre>     
     * 此参数必填
     */
    public void setChannelPrice(Double channelPrice) {
        this.channelPrice = channelPrice;
    }

}
