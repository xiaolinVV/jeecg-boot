package com.alibaba.trade.param;

import java.util.Date;

public class AlibabaTradeRefundOpLogisticsCompanyModel {

    private String companyName;

    /**
     * @return 快递公司名
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置快递公司名     *
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

    private Date gmtCreate;

    /**
     * @return 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    private Date gmtModified;

    /**
     * @return 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    private Long id;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setId(Long id) {
        this.id = id;
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

    private Boolean supportPrint;

    /**
     * @return 是否支持打印
     */
    public Boolean getSupportPrint() {
        return supportPrint;
    }

    /**
     * 设置是否支持打印     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSupportPrint(Boolean supportPrint) {
        this.supportPrint = supportPrint;
    }

}
