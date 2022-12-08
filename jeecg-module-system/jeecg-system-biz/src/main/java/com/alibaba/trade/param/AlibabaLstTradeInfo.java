package com.alibaba.trade.param;

public class AlibabaLstTradeInfo {

    private String lstWarehouseType;

    /**
     * @return 零售通仓库类型。customer：虚仓；cainiao：实仓
     */
    public String getLstWarehouseType() {
        return lstWarehouseType;
    }

    /**
     * 设置零售通仓库类型。customer：虚仓；cainiao：实仓     *
     * 参数示例：<pre>cainiao</pre>     
     * 此参数必填
     */
    public void setLstWarehouseType(String lstWarehouseType) {
        this.lstWarehouseType = lstWarehouseType;
    }

}
