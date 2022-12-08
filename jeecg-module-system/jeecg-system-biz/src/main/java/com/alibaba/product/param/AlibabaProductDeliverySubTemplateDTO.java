package com.alibaba.product.param;

public class AlibabaProductDeliverySubTemplateDTO {

    private Integer chargeType;

    /**
     * @return 计件类型。0:重量 1:件数 2:体积
     */
    public Integer getChargeType() {
        return chargeType;
    }

    /**
     * 设置计件类型。0:重量 1:件数 2:体积     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    private Boolean isSysTemplate;

    /**
     * @return 是否系统模板
     */
    public Boolean getIsSysTemplate() {
        return isSysTemplate;
    }

    /**
     * 设置是否系统模板     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setIsSysTemplate(Boolean isSysTemplate) {
        this.isSysTemplate = isSysTemplate;
    }

    private Integer serviceChargeType;

    /**
     * @return 运费承担类型 卖家承担：0；买家承担：1。
     */
    public Integer getServiceChargeType() {
        return serviceChargeType;
    }

    /**
     * 设置运费承担类型 卖家承担：0；买家承担：1。     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setServiceChargeType(Integer serviceChargeType) {
        this.serviceChargeType = serviceChargeType;
    }

    private Integer serviceType;

    /**
     * @return 服务类型。0:快递 1:货运 2:货到付款
     */
    public Integer getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务类型。0:快递 1:货运 2:货到付款     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    private Integer type;

    /**
     * @return 子模板类型 0基准 1增值。默认0。
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置子模板类型 0基准 1增值。默认0。     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setType(Integer type) {
        this.type = type;
    }

}
