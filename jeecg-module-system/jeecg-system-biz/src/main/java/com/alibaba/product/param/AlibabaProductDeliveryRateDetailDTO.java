package com.alibaba.product.param;

public class AlibabaProductDeliveryRateDetailDTO {

    private Boolean isSysRate;

    /**
     * @return 是否系统模板
     */
    public Boolean getIsSysRate() {
        return isSysRate;
    }

    /**
     * 设置是否系统模板     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setIsSysRate(Boolean isSysRate) {
        this.isSysRate = isSysRate;
    }

    private String toAreaCodeText;

    /**
     * @return 地址编码文本，用顿号隔开。例如：上海、福建省、广东省
     */
    public String getToAreaCodeText() {
        return toAreaCodeText;
    }

    /**
     * 设置地址编码文本，用顿号隔开。例如：上海、福建省、广东省     *
     * 参数示例：<pre>上海、福建省、广东省</pre>     
     * 此参数必填
     */
    public void setToAreaCodeText(String toAreaCodeText) {
        this.toAreaCodeText = toAreaCodeText;
    }

    private AlibabaProductDeliveryRateDTO rateDTO;

    /**
     * @return 普通子模板费率
     */
    public AlibabaProductDeliveryRateDTO getRateDTO() {
        return rateDTO;
    }

    /**
     * 设置普通子模板费率     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setRateDTO(AlibabaProductDeliveryRateDTO rateDTO) {
        this.rateDTO = rateDTO;
    }

    private AlibabaProductDeliverySysRateDTO sysRateDTO;

    /**
     * @return 系统子模板费率
     */
    public AlibabaProductDeliverySysRateDTO getSysRateDTO() {
        return sysRateDTO;
    }

    /**
     * 设置系统子模板费率     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setSysRateDTO(AlibabaProductDeliverySysRateDTO sysRateDTO) {
        this.sysRateDTO = sysRateDTO;
    }

}
