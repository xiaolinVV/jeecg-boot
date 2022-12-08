package com.alibaba.logistics.param;

public class AlibabaLogisticsOpLogisticsCompanyModel {

    private Long id;

    /**
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setId(Long id) {
        this.id = id;
    }

    private String companyName;

    /**
     * @return 物流公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置物流公司名称     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    private String companyNo;

    /**
     * @return 物流公司编号
     */
    public String getCompanyNo() {
        return companyNo;
    }

    /**
     * 设置物流公司编号     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    private String companyPhone;

    /**
     * @return 物流公司服务电话
     */
    public String getCompanyPhone() {
        return companyPhone;
    }

    /**
     * 设置物流公司服务电话     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    private String supportPrint;

    /**
     * @return 是否支持打印
     */
    public String getSupportPrint() {
        return supportPrint;
    }

    /**
     * 设置是否支持打印     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSupportPrint(String supportPrint) {
        this.supportPrint = supportPrint;
    }

    private String spelling;

    /**
     * @return 全拼
     */
    public String getSpelling() {
        return spelling;
    }

    /**
     * 设置全拼     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

}
