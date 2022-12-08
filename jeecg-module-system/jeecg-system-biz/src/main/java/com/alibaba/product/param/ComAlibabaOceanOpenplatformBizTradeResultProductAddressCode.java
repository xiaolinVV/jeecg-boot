package com.alibaba.product.param;

public class ComAlibabaOceanOpenplatformBizTradeResultProductAddressCode {

    private String code;

    /**
     * @return 地址编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置地址编码     *
     * 参数示例：<pre>620000</pre>     
     * 此参数必填
     */
    public void setCode(String code) {
        this.code = code;
    }

    private String name;

    /**
     * @return 地址名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置地址名称     *
     * 参数示例：<pre>甘肃省</pre>     
     * 此参数必填
     */
    public void setName(String name) {
        this.name = name;
    }

}
