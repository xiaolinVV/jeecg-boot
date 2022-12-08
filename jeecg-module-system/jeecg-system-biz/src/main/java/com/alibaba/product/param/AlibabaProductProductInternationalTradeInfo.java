package com.alibaba.product.param;

public class AlibabaProductProductInternationalTradeInfo {

    private String fobCurrency;

    /**
     * @return FOB价格货币，参见FAQ 货币枚举值
     */
    public String getFobCurrency() {
        return fobCurrency;
    }

    /**
     * 设置FOB价格货币，参见FAQ 货币枚举值     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFobCurrency(String fobCurrency) {
        this.fobCurrency = fobCurrency;
    }

    private String fobMinPrice;

    /**
     * @return FOB最小价格
     */
    public String getFobMinPrice() {
        return fobMinPrice;
    }

    /**
     * 设置FOB最小价格     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFobMinPrice(String fobMinPrice) {
        this.fobMinPrice = fobMinPrice;
    }

    private String fobMaxPrice;

    /**
     * @return FOB最大价格
     */
    public String getFobMaxPrice() {
        return fobMaxPrice;
    }

    /**
     * 设置FOB最大价格     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFobMaxPrice(String fobMaxPrice) {
        this.fobMaxPrice = fobMaxPrice;
    }

    private String fobUnitType;

    /**
     * @return FOB计量单位，参见FAQ 计量单位枚举值
     */
    public String getFobUnitType() {
        return fobUnitType;
    }

    /**
     * 设置FOB计量单位，参见FAQ 计量单位枚举值     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFobUnitType(String fobUnitType) {
        this.fobUnitType = fobUnitType;
    }

    private String[] paymentMethods;

    /**
     * @return 付款方式，参见FAQ 付款方式枚举值
     */
    public String[] getPaymentMethods() {
        return paymentMethods;
    }

    /**
     * 设置付款方式，参见FAQ 付款方式枚举值     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPaymentMethods(String[] paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    private Integer minOrderQuantity;

    /**
     * @return 最小起订量
     */
    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    /**
     * 设置最小起订量     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    private String minOrderUnitType;

    /**
     * @return 最小起订量计量单位，参见FAQ 计量单位枚举值
     */
    public String getMinOrderUnitType() {
        return minOrderUnitType;
    }

    /**
     * 设置最小起订量计量单位，参见FAQ 计量单位枚举值     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMinOrderUnitType(String minOrderUnitType) {
        this.minOrderUnitType = minOrderUnitType;
    }

    private Integer supplyQuantity;

    /**
     * @return supplyQuantity
     */
    public Integer getSupplyQuantity() {
        return supplyQuantity;
    }

    /**
     * 设置supplyQuantity     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSupplyQuantity(Integer supplyQuantity) {
        this.supplyQuantity = supplyQuantity;
    }

    private String supplyUnitType;

    /**
     * @return 供货能力计量单位，参见FAQ 计量单位枚举值
     */
    public String getSupplyUnitType() {
        return supplyUnitType;
    }

    /**
     * 设置供货能力计量单位，参见FAQ 计量单位枚举值     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSupplyUnitType(String supplyUnitType) {
        this.supplyUnitType = supplyUnitType;
    }

    private String supplyPeriodType;

    /**
     * @return 供货能力周期，参见FAQ 时间周期枚举值
     */
    public String getSupplyPeriodType() {
        return supplyPeriodType;
    }

    /**
     * 设置供货能力周期，参见FAQ 时间周期枚举值     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSupplyPeriodType(String supplyPeriodType) {
        this.supplyPeriodType = supplyPeriodType;
    }

    private String deliveryPort;

    /**
     * @return 发货港口
     */
    public String getDeliveryPort() {
        return deliveryPort;
    }

    /**
     * 设置发货港口     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setDeliveryPort(String deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    private String deliveryTime;

    /**
     * @return 发货期限
     */
    public String getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * 设置发货期限     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    private Integer consignmentDate;

    /**
     * @return 新发货期限
     */
    public Integer getConsignmentDate() {
        return consignmentDate;
    }

    /**
     * 设置新发货期限     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setConsignmentDate(Integer consignmentDate) {
        this.consignmentDate = consignmentDate;
    }

    private String packagingDesc;

    /**
     * @return 常规包装
     */
    public String getPackagingDesc() {
        return packagingDesc;
    }

    /**
     * 设置常规包装     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPackagingDesc(String packagingDesc) {
        this.packagingDesc = packagingDesc;
    }

}
