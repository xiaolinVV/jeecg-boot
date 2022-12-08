package com.alibaba.trade.param;

import java.util.Date;
import java.util.List;

public class AlibabaTradeRefundOpOrderRefundOperationModel {

    private String afterOperateStatus;

    /**
     * @return 操作后的退款状态
     */
    public String getAfterOperateStatus() {
        return afterOperateStatus;
    }

    /**
     * 设置操作后的退款状态     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setAfterOperateStatus(String afterOperateStatus) {
        this.afterOperateStatus = afterOperateStatus;
    }

    private String beforeOperateStatus;

    /**
     * @return 操作前的退款状态
     */
    public String getBeforeOperateStatus() {
        return beforeOperateStatus;
    }

    /**
     * 设置操作前的退款状态     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setBeforeOperateStatus(String beforeOperateStatus) {
        this.beforeOperateStatus = beforeOperateStatus;
    }

    private Long closeRefundStepId;

    /**
     * @return 分阶段订单正向操作关闭退款时的阶段ID
     */
    public Long getCloseRefundStepId() {
        return closeRefundStepId;
    }

    /**
     * 设置分阶段订单正向操作关闭退款时的阶段ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCloseRefundStepId(Long closeRefundStepId) {
        this.closeRefundStepId = closeRefundStepId;
    }

    private Boolean crmModifyRefund;

    /**
     * @return 是否小二修改过退款单
     */
    public Boolean getCrmModifyRefund() {
        return crmModifyRefund;
    }

    /**
     * 设置是否小二修改过退款单     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCrmModifyRefund(Boolean crmModifyRefund) {
        this.crmModifyRefund = crmModifyRefund;
    }

    private String discription;

    /**
     * @return 描述、说明
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * 设置描述、说明     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setDiscription(String discription) {
        this.discription = discription;
    }

    private String email;

    /**
     * @return 联系人EMAIL
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置联系人EMAIL     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setEmail(String email) {
        this.email = email;
    }

    private String freightBill;

    /**
     * @return 运单号
     */
    public String getFreightBill() {
        return freightBill;
    }

    /**
     * 设置运单号     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFreightBill(String freightBill) {
        this.freightBill = freightBill;
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
     * @return 主键，退款操作记录流水号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键，退款操作记录流水号     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setId(Long id) {
        this.id = id;
    }

    private Integer messageStatus;

    /**
     * @return 凭证状态，1:正常 2:后台小二屏蔽
     */
    public Integer getMessageStatus() {
        return messageStatus;
    }

    /**
     * 设置凭证状态，1:正常 2:后台小二屏蔽     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    private String mobile;

    /**
     * @return 联系人手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置联系人手机     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private Integer msgType;

    /**
     * @return 留言类型 3:小二留言给买家和卖家 4:给买家的留言 5:给卖家的留言 7:cbu的普通留言等同于淘宝的1
     */
    public Integer getMsgType() {
        return msgType;
    }

    /**
     * 设置留言类型 3:小二留言给买家和卖家 4:给买家的留言 5:给卖家的留言 7:cbu的普通留言等同于淘宝的1     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    private String operateRemark;

    /**
     * @return 操作备注
     */
    public String getOperateRemark() {
        return operateRemark;
    }

    /**
     * 设置操作备注     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOperateRemark(String operateRemark) {
        this.operateRemark = operateRemark;
    }

    private Integer operateTypeInt;

    /**
     * @return 操作类型 取代operateType
     */
    public Integer getOperateTypeInt() {
        return operateTypeInt;
    }

    /**
     * 设置操作类型 取代operateType     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOperateTypeInt(Integer operateTypeInt) {
        this.operateTypeInt = operateTypeInt;
    }

    private String operatorId;

    /**
     * @return 操作者-memberID
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作者-memberID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    private String operatorLoginId;

    /**
     * @return 操作者-loginID
     */
    public String getOperatorLoginId() {
        return operatorLoginId;
    }

    /**
     * 设置操作者-loginID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOperatorLoginId(String operatorLoginId) {
        this.operatorLoginId = operatorLoginId;
    }

    private Integer operatorRoleId;

    /**
     * @return 操作者角色名称 买家 卖家 系统
     */
    public Integer getOperatorRoleId() {
        return operatorRoleId;
    }

    /**
     * 设置操作者角色名称 买家 卖家 系统     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOperatorRoleId(Integer operatorRoleId) {
        this.operatorRoleId = operatorRoleId;
    }

    private Long operatorUserId;

    /**
     * @return 操作者-userID
     */
    public Long getOperatorUserId() {
        return operatorUserId;
    }

    /**
     * 设置操作者-userID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOperatorUserId(Long operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    private String phone;

    /**
     * @return 联系人电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系人电话     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String refundAddress;

    /**
     * @return 退货地址
     */
    public String getRefundAddress() {
        return refundAddress;
    }

    /**
     * 设置退货地址     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setRefundAddress(String refundAddress) {
        this.refundAddress = refundAddress;
    }

    private String refundId;

    /**
     * @return 退款记录ID
     */
    public String getRefundId() {
        return refundId;
    }

    /**
     * 设置退款记录ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    private String rejectReason;

    /**
     * @return 卖家拒绝退款原因
     */
    public String getRejectReason() {
        return rejectReason;
    }

    /**
     * 设置卖家拒绝退款原因     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    private List vouchers;

    /**
     * @return 凭证图片地址
     */
    public List getVouchers() {
        return vouchers;
    }

    /**
     * 设置凭证图片地址     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setVouchers(List vouchers) {
        this.vouchers = vouchers;
    }

    private AlibabaTradeRefundOpLogisticsCompanyModel logisticsCompany;

    /**
     * @return 物流公司详情
     */
    public AlibabaTradeRefundOpLogisticsCompanyModel getLogisticsCompany() {
        return logisticsCompany;
    }

    /**
     * 设置物流公司详情     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLogisticsCompany(AlibabaTradeRefundOpLogisticsCompanyModel logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

}
