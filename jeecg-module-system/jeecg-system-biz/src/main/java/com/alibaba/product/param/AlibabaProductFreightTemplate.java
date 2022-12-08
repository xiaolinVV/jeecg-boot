package com.alibaba.product.param;

public class AlibabaProductFreightTemplate {

    private String addressCodeText;

    /**
     * @return 地址区域编码对应的文本（包括省市区，用空格隔开）
     */
    public String getAddressCodeText() {
        return addressCodeText;
    }

    /**
     * 设置地址区域编码对应的文本（包括省市区，用空格隔开）     *
     * 参数示例：<pre>福建省 福州市 鼓楼区</pre>     
     * 此参数必填
     */
    public void setAddressCodeText(String addressCodeText) {
        this.addressCodeText = addressCodeText;
    }

    private String fromAreaCode;

    /**
     * @return 发货地址地区码
     */
    public String getFromAreaCode() {
        return fromAreaCode;
    }

    /**
     * 设置发货地址地区码     *
     * 参数示例：<pre>350102</pre>     
     * 此参数必填
     */
    public void setFromAreaCode(String fromAreaCode) {
        this.fromAreaCode = fromAreaCode;
    }

    private Long id;

    /**
     * @return 地址ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置地址ID     *
     * 参数示例：<pre>1234</pre>     
     * 此参数必填
     */
    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    /**
     * @return 模板名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模板名称     *
     * 参数示例：<pre>2019</pre>     
     * 此参数必填
     */
    public void setName(String name) {
        this.name = name;
    }

    private String remark;

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注     *
     * 参数示例：<pre>2019</pre>     
     * 此参数必填
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    private Integer status;

    /**
     * @return 状态：1表示有效，-1表示失效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：1表示有效，-1表示失效     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    private AlibabaProductDeliverySubTemplateDetailDTO expressSubTemplate;

    /**
     * @return 快递子模版
     */
    public AlibabaProductDeliverySubTemplateDetailDTO getExpressSubTemplate() {
        return expressSubTemplate;
    }

    /**
     * 设置快递子模版     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setExpressSubTemplate(AlibabaProductDeliverySubTemplateDetailDTO expressSubTemplate) {
        this.expressSubTemplate = expressSubTemplate;
    }

    private AlibabaProductDeliverySubTemplateDetailDTO logisticsSubTemplate;

    /**
     * @return 货运子模版
     */
    public AlibabaProductDeliverySubTemplateDetailDTO getLogisticsSubTemplate() {
        return logisticsSubTemplate;
    }

    /**
     * 设置货运子模版     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setLogisticsSubTemplate(AlibabaProductDeliverySubTemplateDetailDTO logisticsSubTemplate) {
        this.logisticsSubTemplate = logisticsSubTemplate;
    }

    private AlibabaProductDeliverySubTemplateDetailDTO codSubTemplate;

    /**
     * @return 货到付款子模版
     */
    public AlibabaProductDeliverySubTemplateDetailDTO getCodSubTemplate() {
        return codSubTemplate;
    }

    /**
     * 设置货到付款子模版     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setCodSubTemplate(AlibabaProductDeliverySubTemplateDetailDTO codSubTemplate) {
        this.codSubTemplate = codSubTemplate;
    }

}
