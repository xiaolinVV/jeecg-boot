package com.alibaba.product.param;

public class AlibabaProductProductShippingInfo {

    private Long freightTemplateID;

    /**
     * @return 运费模板ID，0表示运费说明，1表示卖家承担运费，其他值表示使用运费模版。此参数可调用运费模板相关API获取
     */
    public Long getFreightTemplateID() {
        return freightTemplateID;
    }

    /**
     * 设置运费模板ID，0表示运费说明，1表示卖家承担运费，其他值表示使用运费模版。此参数可调用运费模板相关API获取     *
     * 参数示例：<pre>121133</pre>     
     * 此参数必填
     */
    public void setFreightTemplateID(Long freightTemplateID) {
        this.freightTemplateID = freightTemplateID;
    }

    private Double unitWeight;

    /**
     * @return 重量/毛重
     */
    public Double getUnitWeight() {
        return unitWeight;
    }

    /**
     * 设置重量/毛重     *
     * 参数示例：<pre>121</pre>     
     * 此参数必填
     */
    public void setUnitWeight(Double unitWeight) {
        this.unitWeight = unitWeight;
    }

    private String packageSize;

    /**
     * @return 尺寸，单位是厘米，长宽高范围是1-9999999。1688无需关注此字段
     */
    public String getPackageSize() {
        return packageSize;
    }

    /**
     * 设置尺寸，单位是厘米，长宽高范围是1-9999999。1688无需关注此字段     *
     * 参数示例：<pre>10x20x50</pre>     
     * 此参数必填
     */
    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    private Integer volume;

    /**
     * @return 体积，单位是立方厘米，范围是1-9999999，1688无需关注此字段
     */
    public Integer getVolume() {
        return volume;
    }

    /**
     * 设置体积，单位是立方厘米，范围是1-9999999，1688无需关注此字段     *
     * 参数示例：<pre>500</pre>     
     * 此参数必填
     */
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    private Integer handlingTime;

    /**
     * @return 备货期，单位是天，范围是1-60。1688无需处理此字段
     */
    public Integer getHandlingTime() {
        return handlingTime;
    }

    /**
     * 设置备货期，单位是天，范围是1-60。1688无需处理此字段     *
     * 参数示例：<pre>12</pre>     
     * 此参数必填
     */
    public void setHandlingTime(Integer handlingTime) {
        this.handlingTime = handlingTime;
    }

    private Long sendGoodsAddressId;

    /**
     * @return 发货地址ID，国际站无需处理此字段
     */
    public Long getSendGoodsAddressId() {
        return sendGoodsAddressId;
    }

    /**
     * 设置发货地址ID，国际站无需处理此字段     *
     * 参数示例：<pre>124431</pre>     
     * 此参数必填
     */
    public void setSendGoodsAddressId(Long sendGoodsAddressId) {
        this.sendGoodsAddressId = sendGoodsAddressId;
    }

    private String sendGoodsAddressText;

    /**
     * @return 发货地描述
     */
    public String getSendGoodsAddressText() {
        return sendGoodsAddressText;
    }

    /**
     * 设置发货地描述     *
     * 参数示例：<pre>asda</pre>     
     * 此参数必填
     */
    public void setSendGoodsAddressText(String sendGoodsAddressText) {
        this.sendGoodsAddressText = sendGoodsAddressText;
    }

    private Double suttleWeight;

    /**
     * @return 净重
     */
    public Double getSuttleWeight() {
        return suttleWeight;
    }

    /**
     * 设置净重     *
     * 参数示例：<pre>1001</pre>     
     * 此参数必填
     */
    public void setSuttleWeight(Double suttleWeight) {
        this.suttleWeight = suttleWeight;
    }

    private Double width;

    /**
     * @return 宽度
     */
    public Double getWidth() {
        return width;
    }

    /**
     * 设置宽度     *
     * 参数示例：<pre>30</pre>     
     * 此参数必填
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    private Double height;

    /**
     * @return 高度
     */
    public Double getHeight() {
        return height;
    }

    /**
     * 设置高度     *
     * 参数示例：<pre>20</pre>     
     * 此参数必填
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    private Double length;

    /**
     * @return 长度
     */
    public Double getLength() {
        return length;
    }

    /**
     * 设置长度     *
     * 参数示例：<pre>10</pre>     
     * 此参数必填
     */
    public void setLength(Double length) {
        this.length = length;
    }

    private AlibabaProductFreightTemplate[] freightTemplate;

    /**
     * @return 1
     */
    public AlibabaProductFreightTemplate[] getFreightTemplate() {
        return freightTemplate;
    }

    /**
     * 设置1     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setFreightTemplate(AlibabaProductFreightTemplate[] freightTemplate) {
        this.freightTemplate = freightTemplate;
    }

    private Boolean channelPriceFreePostage;

    /**
     * @return 厂货通渠道专享价是否包邮，要结合非包邮地址，如果收货地址在非包邮地区则商品为不包邮
     */
    public Boolean getChannelPriceFreePostage() {
        return channelPriceFreePostage;
    }

    /**
     * 设置厂货通渠道专享价是否包邮，要结合非包邮地址，如果收货地址在非包邮地区则商品为不包邮     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setChannelPriceFreePostage(Boolean channelPriceFreePostage) {
        this.channelPriceFreePostage = channelPriceFreePostage;
    }

    private ComAlibabaOceanOpenplatformBizTradeResultProductAddressCode[] channelPriceExcludeAreaCodes;

    /**
     * @return 厂货通渠道专享价非包邮地区（地址信息列表，省份信息）
     */
    public ComAlibabaOceanOpenplatformBizTradeResultProductAddressCode[] getChannelPriceExcludeAreaCodes() {
        return channelPriceExcludeAreaCodes;
    }

    /**
     * 设置厂货通渠道专享价非包邮地区（地址信息列表，省份信息）     *
     * 参数示例：<pre>[{}]</pre>     
     * 此参数必填
     */
    public void setChannelPriceExcludeAreaCodes(ComAlibabaOceanOpenplatformBizTradeResultProductAddressCode[] channelPriceExcludeAreaCodes) {
        this.channelPriceExcludeAreaCodes = channelPriceExcludeAreaCodes;
    }

}
