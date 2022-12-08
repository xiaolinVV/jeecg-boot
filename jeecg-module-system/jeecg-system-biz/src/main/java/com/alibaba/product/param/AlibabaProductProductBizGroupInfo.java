package com.alibaba.product.param;

public class AlibabaProductProductBizGroupInfo {

    private Boolean support;

    /**
     * @return 是否支持
     */
    public Boolean getSupport() {
        return support;
    }

    /**
     * 设置是否支持     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSupport(Boolean support) {
        this.support = support;
    }

    private String description;

    /**
     * @return 垂直市场名字，如微供市场、货品市场
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置垂直市场名字，如微供市场、货品市场     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setDescription(String description) {
        this.description = description;
    }

    private String code;

    /**
     * @return 垂直市场标记
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置垂直市场标记     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCode(String code) {
        this.code = code;
    }

}
