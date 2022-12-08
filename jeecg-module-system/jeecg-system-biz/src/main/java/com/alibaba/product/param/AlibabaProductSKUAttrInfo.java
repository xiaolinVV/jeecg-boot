package com.alibaba.product.param;

public class AlibabaProductSKUAttrInfo {

    private Long attributeID;

    /**
     * @return sku属性ID
     */
    public Long getAttributeID() {
        return attributeID;
    }

    /**
     * 设置sku属性ID     *
     * 参数示例：<pre>3216</pre>     
     * 此参数必填
     */
    public void setAttributeID(Long attributeID) {
        this.attributeID = attributeID;
    }

    private String attributeValue;

    /**
     * @return sku值内容
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * 设置sku值内容     *
     * 参数示例：<pre>白色</pre>     
     * 此参数必填
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    private String skuImageUrl;

    /**
     * @return sku图片名
     */
    public String getSkuImageUrl() {
        return skuImageUrl;
    }

    /**
     * 设置sku图片名     *
     * 参数示例：<pre>img/ibank/2018/852/384/9326483258_1660977857.jpg</pre>     
     * 此参数必填
     */
    public void setSkuImageUrl(String skuImageUrl) {
        this.skuImageUrl = skuImageUrl;
    }

    private String attributeDisplayName;

    /**
     * @return sku属性ID所对应的显示名，比如颜色，尺码
     */
    public String getAttributeDisplayName() {
        return attributeDisplayName;
    }

    /**
     * 设置sku属性ID所对应的显示名，比如颜色，尺码     *
     * 参数示例：<pre>颜色</pre>     
     * 此参数必填
     */
    public void setAttributeDisplayName(String attributeDisplayName) {
        this.attributeDisplayName = attributeDisplayName;
    }

    private String attributeName;

    /**
     * @return sku属性ID所对应的显示名，比如颜色，尺码
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * 设置sku属性ID所对应的显示名，比如颜色，尺码     *
     * 参数示例：<pre>颜色</pre>     
     * 此参数必填
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

}
