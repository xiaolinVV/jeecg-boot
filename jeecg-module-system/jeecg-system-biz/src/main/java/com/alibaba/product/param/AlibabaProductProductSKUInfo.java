package com.alibaba.product.param;

public class AlibabaProductProductSKUInfo {

    private AlibabaProductSKUAttrInfo[] attributes;

    /**
     * @return SKU属性值，可填多组信息
     */
    public AlibabaProductSKUAttrInfo[] getAttributes() {
        return attributes;
    }

    /**
     * 设置SKU属性值，可填多组信息     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setAttributes(AlibabaProductSKUAttrInfo[] attributes) {
        this.attributes = attributes;
    }

    private String cargoNumber;

    /**
     * @return 指定规格的货号
     */
    public String getCargoNumber() {
        return cargoNumber;
    }

    /**
     * 设置指定规格的货号     *
     * 参数示例：<pre>666</pre>     
     * 此参数必填
     */
    public void setCargoNumber(String cargoNumber) {
        this.cargoNumber = cargoNumber;
    }

    private Integer amountOnSale;

    /**
     * @return 可销售数量
     */
    public Integer getAmountOnSale() {
        return amountOnSale;
    }

    /**
     * 设置可销售数量     *
     * 参数示例：<pre>1490</pre>     
     * 此参数必填
     */
    public void setAmountOnSale(Integer amountOnSale) {
        this.amountOnSale = amountOnSale;
    }

    private Double retailPrice;

    /**
     * @return 建议零售价
     */
    public Double getRetailPrice() {
        return retailPrice;
    }

    /**
     * 设置建议零售价     *
     * 参数示例：<pre>445</pre>     
     * 此参数必填
     */
    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    private Double price;

    /**
     * @return 报价时该规格的单价
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置报价时该规格的单价     *
     * 参数示例：<pre>445</pre>     
     * 此参数必填
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    private Long skuId;

    /**
     * @return skuId,该规格在所有商品中的唯一标记
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置skuId,该规格在所有商品中的唯一标记     *
     * 参数示例：<pre>3935963888523</pre>     
     * 此参数必填
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    private String specId;

    /**
     * @return specId,该规格在本商品内的唯一标记
     */
    public String getSpecId() {
        return specId;
    }

    /**
     * 设置specId,该规格在本商品内的唯一标记     *
     * 参数示例：<pre>b265340beb52182c6bcfbff1fad02f0b</pre>     
     * 此参数必填
     */
    public void setSpecId(String specId) {
        this.specId = specId;
    }

    private Double consignPrice;

    /**
     * @return 分销基准价。代销场景均使用该价格。无SKU商品查看saleInfo中的consignPrice
     */
    public Double getConsignPrice() {
        return consignPrice;
    }

    /**
     * 设置分销基准价。代销场景均使用该价格。无SKU商品查看saleInfo中的consignPrice     *
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
