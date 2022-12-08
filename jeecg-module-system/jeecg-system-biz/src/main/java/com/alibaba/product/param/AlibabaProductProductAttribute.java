package com.alibaba.product.param;

public class AlibabaProductProductAttribute {

    private Long attributeID;

    /**
     * @return 属性ID
     */
    public Long getAttributeID() {
        return attributeID;
    }

    /**
     * 设置属性ID     *
     * 参数示例：<pre>123456</pre>     
     * 此参数必填
     */
    public void setAttributeID(Long attributeID) {
        this.attributeID = attributeID;
    }

    private String attributeName;

    /**
     * @return 属性名称
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * 设置属性名称     *
     * 参数示例：<pre>color</pre>     
     * 此参数必填
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    private Long valueID;

    /**
     * @return 属性值ID
     */
    public Long getValueID() {
        return valueID;
    }

    /**
     * 设置属性值ID     *
     * 参数示例：<pre>123456</pre>     
     * 此参数必填
     */
    public void setValueID(Long valueID) {
        this.valueID = valueID;
    }

    private String value;

    /**
     * @return 属性值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置属性值     *
     * 参数示例：<pre>grey</pre>     
     * 此参数必填
     */
    public void setValue(String value) {
        this.value = value;
    }

    private Boolean isCustom;

    /**
     * @return 是否为自定义属性，国际站无需关注
     */
    public Boolean getIsCustom() {
        return isCustom;
    }

    /**
     * 设置是否为自定义属性，国际站无需关注     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

}
