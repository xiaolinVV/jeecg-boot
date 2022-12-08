package com.alibaba.logistics.param;

public class AlibabaLogisticsOpenPlatformLogisticsOrder {

    private String logisticsId;

    /**
     * @return 物流信息ID
     */
    public String getLogisticsId() {
        return logisticsId;
    }

    /**
     * 设置物流信息ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    private String logisticsBillNo;

    /**
     * @return 物流单号，运单号
     */
    public String getLogisticsBillNo() {
        return logisticsBillNo;
    }

    /**
     * 设置物流单号，运单号     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLogisticsBillNo(String logisticsBillNo) {
        this.logisticsBillNo = logisticsBillNo;
    }

    private String orderEntryIds;

    /**
     * @return 订单号列表，无子订单的等于主订单编号，否则为对应子订单列表
     */
    public String getOrderEntryIds() {
        return orderEntryIds;
    }

    /**
     * 设置订单号列表，无子订单的等于主订单编号，否则为对应子订单列表     *
     * 参数示例：<pre>129232515787615400,129232515788615400,129232515789615400,129232515790615400</pre>     
     * 此参数必填
     */
    public void setOrderEntryIds(String orderEntryIds) {
        this.orderEntryIds = orderEntryIds;
    }

    private String status;

    /**
     * @return 物流状态。WAITACCEPT:未受理;CANCEL:已撤销;ACCEPT:已受理;TRANSPORT:运输中;NOGET:揽件失败;SIGN:已签收;UNSIGN:签收异常
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置物流状态。WAITACCEPT:未受理;CANCEL:已撤销;ACCEPT:已受理;TRANSPORT:运输中;NOGET:揽件失败;SIGN:已签收;UNSIGN:签收异常     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setStatus(String status) {
        this.status = status;
    }

    private String logisticsCompanyId;

    /**
     * @return 物流公司ID
     */
    public String getLogisticsCompanyId() {
        return logisticsCompanyId;
    }

    /**
     * 设置物流公司ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLogisticsCompanyId(String logisticsCompanyId) {
        this.logisticsCompanyId = logisticsCompanyId;
    }

    private String logisticsCompanyName;

    /**
     * @return 物流公司编码
     */
    public String getLogisticsCompanyName() {
        return logisticsCompanyName;
    }

    /**
     * 设置物流公司编码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLogisticsCompanyName(String logisticsCompanyName) {
        this.logisticsCompanyName = logisticsCompanyName;
    }

    private String remarks;

    /**
     * @return 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    private String serviceFeature;

    /**
     * @return 
     */
    public String getServiceFeature() {
        return serviceFeature;
    }

    /**
     * 设置     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setServiceFeature(String serviceFeature) {
        this.serviceFeature = serviceFeature;
    }

    private String gmtSystemSend;

    /**
     * @return 
     */
    public String getGmtSystemSend() {
        return gmtSystemSend;
    }

    /**
     * 设置     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setGmtSystemSend(String gmtSystemSend) {
        this.gmtSystemSend = gmtSystemSend;
    }

    private AlibabaLogisticsOpenPlatformLogisticsSendGood[] sendGoods;

    /**
     * @return 商品信息
     */
    public AlibabaLogisticsOpenPlatformLogisticsSendGood[] getSendGoods() {
        return sendGoods;
    }

    /**
     * 设置商品信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSendGoods(AlibabaLogisticsOpenPlatformLogisticsSendGood[] sendGoods) {
        this.sendGoods = sendGoods;
    }

    private AlibabaLogisticsOpenPlatformLogisticsReceiver receiver;

    /**
     * @return 收件人信息
     */
    public AlibabaLogisticsOpenPlatformLogisticsReceiver getReceiver() {
        return receiver;
    }

    /**
     * 设置收件人信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setReceiver(AlibabaLogisticsOpenPlatformLogisticsReceiver receiver) {
        this.receiver = receiver;
    }

    private AlibabaLogisticsOpenPlatformLogisticsSender sender;

    /**
     * @return 发件人信息
     */
    public AlibabaLogisticsOpenPlatformLogisticsSender getSender() {
        return sender;
    }

    /**
     * 设置发件人信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSender(AlibabaLogisticsOpenPlatformLogisticsSender sender) {
        this.sender = sender;
    }

}
