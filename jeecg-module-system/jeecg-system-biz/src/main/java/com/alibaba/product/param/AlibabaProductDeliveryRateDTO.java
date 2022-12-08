package com.alibaba.product.param;

public class AlibabaProductDeliveryRateDTO {

    private Long firstUnit;

    /**
     * @return 首重（单位：克）或首件（单位：件）
     */
    public Long getFirstUnit() {
        return firstUnit;
    }

    /**
     * 设置首重（单位：克）或首件（单位：件）     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setFirstUnit(Long firstUnit) {
        this.firstUnit = firstUnit;
    }

    private Long firstUnitFee;

    /**
     * @return 首重或首件的价格（单位：分）
     */
    public Long getFirstUnitFee() {
        return firstUnitFee;
    }

    /**
     * 设置首重或首件的价格（单位：分）     *
     * 参数示例：<pre>600</pre>     
     * 此参数必填
     */
    public void setFirstUnitFee(Long firstUnitFee) {
        this.firstUnitFee = firstUnitFee;
    }

    private Long leastExpenses;

    /**
     * @return 最低一票
     */
    public Long getLeastExpenses() {
        return leastExpenses;
    }

    /**
     * 设置最低一票     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setLeastExpenses(Long leastExpenses) {
        this.leastExpenses = leastExpenses;
    }

    private Long nextUnit;

    /**
     * @return 续重件单位
     */
    public Long getNextUnit() {
        return nextUnit;
    }

    /**
     * 设置续重件单位     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setNextUnit(Long nextUnit) {
        this.nextUnit = nextUnit;
    }

    private Long nextUnitFee;

    /**
     * @return 续重件价格（单位：分）
     */
    public Long getNextUnitFee() {
        return nextUnitFee;
    }

    /**
     * 设置续重件价格（单位：分）     *
     * 参数示例：<pre>100</pre>     
     * 此参数必填
     */
    public void setNextUnitFee(Long nextUnitFee) {
        this.nextUnitFee = nextUnitFee;
    }

}
