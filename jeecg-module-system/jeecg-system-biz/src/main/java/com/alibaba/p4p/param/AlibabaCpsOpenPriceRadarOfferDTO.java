package com.alibaba.p4p.param;

public class AlibabaCpsOpenPriceRadarOfferDTO {

    private Long offerId;

    /**
     * @return offerId
     */
    public Long getOfferId() {
        return offerId;
    }

    /**
     * 设置offerId     *
     * 参数示例：<pre>1111</pre>     
     * 此参数必填
     */
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    private Double price;

    /**
     * @return 当前商品价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置当前商品价格     *
     * 参数示例：<pre>21.1</pre>     
     * 此参数必填
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    private Double selfAveragePrice;

    /**
     * @return 商品近30天平均成交价	
     */
    public Double getSelfAveragePrice() {
        return selfAveragePrice;
    }

    /**
     * 设置商品近30天平均成交价	     *
     * 参数示例：<pre>22.1</pre>     
     * 此参数必填
     */
    public void setSelfAveragePrice(Double selfAveragePrice) {
        this.selfAveragePrice = selfAveragePrice;
    }

    private Double marketAveragePrice;

    /**
     * @return 零售市场近30天平均成交价	
     */
    public Double getMarketAveragePrice() {
        return marketAveragePrice;
    }

    /**
     * 设置零售市场近30天平均成交价	     *
     * 参数示例：<pre>23.1</pre>     
     * 此参数必填
     */
    public void setMarketAveragePrice(Double marketAveragePrice) {
        this.marketAveragePrice = marketAveragePrice;
    }

}
