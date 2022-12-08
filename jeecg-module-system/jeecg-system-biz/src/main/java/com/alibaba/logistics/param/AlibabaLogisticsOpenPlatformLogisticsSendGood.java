package com.alibaba.logistics.param;

public class AlibabaLogisticsOpenPlatformLogisticsSendGood {

    private String goodName;

    /**
     * @return 商品名
     */
    public String getGoodName() {
        return goodName;
    }

    /**
     * 设置商品名     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    private String quantity;

    /**
     * @return 商品数量
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * 设置商品数量     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private String unit;

    /**
     * @return 商品单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置商品单位     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

}
