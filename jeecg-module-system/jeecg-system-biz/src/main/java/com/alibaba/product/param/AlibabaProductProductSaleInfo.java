package com.alibaba.product.param;

public class AlibabaProductProductSaleInfo {

    private Boolean supportOnlineTrade;

    /**
     * @return 是否支持网上交易。true：支持 false：不支持
     */
    public Boolean getSupportOnlineTrade() {
        return supportOnlineTrade;
    }

    /**
     * 设置是否支持网上交易。true：支持 false：不支持     *
     * 参数示例：<pre>TRUE</pre>     
     * 此参数必填
     */
    public void setSupportOnlineTrade(Boolean supportOnlineTrade) {
        this.supportOnlineTrade = supportOnlineTrade;
    }

    private Boolean mixWholeSale;

    /**
     * @return 是否支持混批
     */
    public Boolean getMixWholeSale() {
        return mixWholeSale;
    }

    /**
     * 设置是否支持混批     *
     * 参数示例：<pre>TRUE</pre>     
     * 此参数必填
     */
    public void setMixWholeSale(Boolean mixWholeSale) {
        this.mixWholeSale = mixWholeSale;
    }

    private Boolean priceAuth;

    /**
     * @return 是否价格私密信息
     */
    public Boolean getPriceAuth() {
        return priceAuth;
    }

    /**
     * 设置是否价格私密信息     *
     * 参数示例：<pre>TRUE</pre>     
     * 此参数必填
     */
    public void setPriceAuth(Boolean priceAuth) {
        this.priceAuth = priceAuth;
    }

    private AlibabaProductProductPriceRange[] priceRanges;

    /**
     * @return 区间价格。按数量范围设定的区间价格
     */
    public AlibabaProductProductPriceRange[] getPriceRanges() {
        return priceRanges;
    }

    /**
     * 设置区间价格。按数量范围设定的区间价格     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setPriceRanges(AlibabaProductProductPriceRange[] priceRanges) {
        this.priceRanges = priceRanges;
    }

    private Double amountOnSale;

    /**
     * @return 可售数量
     */
    public Double getAmountOnSale() {
        return amountOnSale;
    }

    /**
     * 设置可售数量     *
     * 参数示例：<pre>29900</pre>     
     * 此参数必填
     */
    public void setAmountOnSale(Double amountOnSale) {
        this.amountOnSale = amountOnSale;
    }

    private String unit;

    /**
     * @return 计量单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置计量单位     *
     * 参数示例：<pre>件</pre>     
     * 此参数必填
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    private Integer minOrderQuantity;

    /**
     * @return 最小起订量，范围是1-99999。
     */
    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    /**
     * 设置最小起订量，范围是1-99999。     *
     * 参数示例：<pre>3</pre>     
     * 此参数必填
     */
    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    private Integer batchNumber;

    /**
     * @return 每批数量，默认为空或者非零值，该属性不为空时sellunit为必填
     */
    public Integer getBatchNumber() {
        return batchNumber;
    }

    /**
     * 设置每批数量，默认为空或者非零值，该属性不为空时sellunit为必填     *
     * 参数示例：<pre>12</pre>     
     * 此参数必填
     */
    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    private Double retailprice;

    /**
     * @return 建议零售价
     */
    public Double getRetailprice() {
        return retailprice;
    }

    /**
     * 设置建议零售价     *
     * 参数示例：<pre>445</pre>     
     * 此参数必填
     */
    public void setRetailprice(Double retailprice) {
        this.retailprice = retailprice;
    }

    private String tax;

    /**
     * @return 税率相关信息，内容由用户自定
     */
    public String getTax() {
        return tax;
    }

    /**
     * 设置税率相关信息，内容由用户自定     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTax(String tax) {
        this.tax = tax;
    }

    private String sellunit;

    /**
     * @return 售卖单位，如果为批量售卖，代表售卖的单位，该属性不为空时batchNumber为必填，例如1"手"=12“件"的"手"
     */
    public String getSellunit() {
        return sellunit;
    }

    /**
     * 设置售卖单位，如果为批量售卖，代表售卖的单位，该属性不为空时batchNumber为必填，例如1"手"=12“件"的"手"     *
     * 参数示例：<pre>手</pre>     
     * 此参数必填
     */
    public void setSellunit(String sellunit) {
        this.sellunit = sellunit;
    }

    private Integer quoteType;

    /**
     * @return 0-无SKU按数量报价,1-有SKU按规格报价,2-有SKU按数量报价
     */
    public Integer getQuoteType() {
        return quoteType;
    }

    /**
     * 设置0-无SKU按数量报价,1-有SKU按规格报价,2-有SKU按数量报价     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setQuoteType(Integer quoteType) {
        this.quoteType = quoteType;
    }

    private Double consignPrice;

    /**
     * @return 分销基准价。代销场景均使用该价格。有SKU商品查看skuInfo中的consignPrice
     */
    public Double getConsignPrice() {
        return consignPrice;
    }

    /**
     * 设置分销基准价。代销场景均使用该价格。有SKU商品查看skuInfo中的consignPrice     *
     * 参数示例：<pre>445</pre>     
     * 此参数必填
     */
    public void setConsignPrice(Double consignPrice) {
        this.consignPrice = consignPrice;
    }

    private Double cpsSuggestPrice;

    /**
     * @return CPS建议价（单位：元）
     */
    public Double getCpsSuggestPrice() {
        return cpsSuggestPrice;
    }

    /**
     * 设置CPS建议价（单位：元）     *
     * 参数示例：<pre>555</pre>     
     * 此参数必填
     */
    public void setCpsSuggestPrice(Double cpsSuggestPrice) {
        this.cpsSuggestPrice = cpsSuggestPrice;
    }

    private String saleType;

    /**
     * @return 销售方式，按件卖(normal)或者按批卖(batch)，1688站点无需关注此字段
     */
    public String getSaleType() {
        return saleType;
    }

    /**
     * 设置销售方式，按件卖(normal)或者按批卖(batch)，1688站点无需关注此字段     *
     * 参数示例：<pre>normal</pre>     
     * 此参数必填
     */
    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    private Integer deliveryLimit;

    /**
     * @return 发货时间限制（非买保发货周期），按天计算
     */
    public Integer getDeliveryLimit() {
        return deliveryLimit;
    }

    /**
     * 设置发货时间限制（非买保发货周期），按天计算     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setDeliveryLimit(Integer deliveryLimit) {
        this.deliveryLimit = deliveryLimit;
    }

    private Double channelPrice;

    /**
     * @return 厂货通渠道专享价（单位：元）
     */
    public Double getChannelPrice() {
        return channelPrice;
    }

    /**
     * 设置厂货通渠道专享价（单位：元）     *
     * 参数示例：<pre>553</pre>     
     * 此参数必填
     */
    public void setChannelPrice(Double channelPrice) {
        this.channelPrice = channelPrice;
    }

}
