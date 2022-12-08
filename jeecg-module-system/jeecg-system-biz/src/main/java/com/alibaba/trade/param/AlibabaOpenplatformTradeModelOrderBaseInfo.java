package com.alibaba.trade.param;

import java.math.BigDecimal;
import java.util.Date;

public class AlibabaOpenplatformTradeModelOrderBaseInfo {

    private Date allDeliveredTime;

    /**
     * @return 完全发货时间
     */
    public Date getAllDeliveredTime() {
        return allDeliveredTime;
    }

    /**
     * 设置完全发货时间     *
     * 参数示例：<pre>20180614101942000+0800</pre>     
     * 此参数必填
     */
    public void setAllDeliveredTime(Date allDeliveredTime) {
        this.allDeliveredTime = allDeliveredTime;
    }

    private String businessType;

    /**
     * @return 业务类型。国际站：ta(信保),wholesale(在线批发)。
    中文站：普通订单类型 = "cn";
    大额批发订单类型 = "ws";
    普通拿样订单类型 = "yp";
    一分钱拿样订单类型 = "yf";
    倒批(限时折扣)订单类型 = "fs";
    加工定制订单类型 = "cz";
    协议采购订单类型 = "ag";
    伙拼订单类型 = "hp";
    供销订单类型 = "supply";
    淘工厂订单 = "factory";
    快订下单  = "quick";
    享拼订单  = "xiangpin";
    当面付 = "f2f";
    存样服务 = "cyfw";
    代销订单 = "sp";
    微供订单 = "wg";零售通 = "lst";跨境='cb';分销='distribution';采源宝='cab';加工定制="manufact"
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * 设置业务类型。国际站：ta(信保),wholesale(在线批发)。
    中文站：普通订单类型 = "cn";
    大额批发订单类型 = "ws";
    普通拿样订单类型 = "yp";
    一分钱拿样订单类型 = "yf";
    倒批(限时折扣)订单类型 = "fs";
    加工定制订单类型 = "cz";
    协议采购订单类型 = "ag";
    伙拼订单类型 = "hp";
    供销订单类型 = "supply";
    淘工厂订单 = "factory";
    快订下单  = "quick";
    享拼订单  = "xiangpin";
    当面付 = "f2f";
    存样服务 = "cyfw";
    代销订单 = "sp";
    微供订单 = "wg";零售通 = "lst";跨境='cb';分销='distribution';采源宝='cab';加工定制="manufact"     *
     * 参数示例：<pre>cn</pre>     
     * 此参数必填
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    private String buyerID;

    /**
     * @return 买家主账号id
     */
    public String getBuyerID() {
        return buyerID;
    }

    /**
     * 设置买家主账号id     *
     * 参数示例：<pre>1234531</pre>     
     * 此参数必填
     */
    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    private String buyerMemo;

    /**
     * @return 买家备忘信息
     */
    public String getBuyerMemo() {
        return buyerMemo;
    }

    /**
     * 设置买家备忘信息     *
     * 参数示例：<pre>备忘</pre>     
     * 此参数必填
     */
    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    private Date completeTime;

    /**
     * @return 完成时间
     */
    public Date getCompleteTime() {
        return completeTime;
    }

    /**
     * 设置完成时间     *
     * 参数示例：<pre>20180614101942000+0800</pre>     
     * 此参数必填
     */
    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    private Date createTime;

    /**
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间     *
     * 参数示例：<pre>20180614101942000+0800</pre>     
     * 此参数必填
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private String currency;

    /**
     * @return 币种，币种，整个交易单使用同一个币种。值范围：USD,RMB,HKD,GBP,CAD,AUD,JPY,KRW,EUR
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 设置币种，币种，整个交易单使用同一个币种。值范围：USD,RMB,HKD,GBP,CAD,AUD,JPY,KRW,EUR     *
     * 参数示例：<pre>EUR</pre>     
     * 此参数必填
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    private Long id;

    /**
     * @return 交易id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置交易id     *
     * 参数示例：<pre>1231231231111</pre>     
     * 此参数必填
     */
    public void setId(Long id) {
        this.id = id;
    }

    private Date modifyTime;

    /**
     * @return 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间     *
     * 参数示例：<pre>20180614101942000+0800</pre>     
     * 此参数必填
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    private Date payTime;

    /**
     * @return 付款时间，如果有多次付款，这里返回的是首次付款时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置付款时间，如果有多次付款，这里返回的是首次付款时间     *
     * 参数示例：<pre>20180614101942000+0800</pre>     
     * 此参数必填
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    private Date receivingTime;

    /**
     * @return 收货时间，这里返回的是完全收货时间
     */
    public Date getReceivingTime() {
        return receivingTime;
    }

    /**
     * 设置收货时间，这里返回的是完全收货时间     *
     * 参数示例：<pre>20180614101942000+0800</pre>     
     * 此参数必填
     */
    public void setReceivingTime(Date receivingTime) {
        this.receivingTime = receivingTime;
    }

    private BigDecimal refund;

    /**
     * @return 退款金额，单位为元
     */
    public BigDecimal getRefund() {
        return refund;
    }

    /**
     * 设置退款金额，单位为元     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    private String remark;

    /**
     * @return 备注，1688指下单时的备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注，1688指下单时的备注     *
     * 参数示例：<pre>备注</pre>     
     * 此参数必填
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String sellerID;

    /**
     * @return 卖家主账号id
     */
    public String getSellerID() {
        return sellerID;
    }

    /**
     * 设置卖家主账号id     *
     * 参数示例：<pre>123123123123</pre>     
     * 此参数必填
     */
    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    private String sellerMemo;

    /**
     * @return 卖家备忘信息
     */
    public String getSellerMemo() {
        return sellerMemo;
    }

    /**
     * 设置卖家备忘信息     *
     * 参数示例：<pre>备忘</pre>     
     * 此参数必填
     */
    public void setSellerMemo(String sellerMemo) {
        this.sellerMemo = sellerMemo;
    }

    private BigDecimal shippingFee;

    /**
     * @return 运费，单位为元
     */
    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    /**
     * 设置运费，单位为元     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    private String status;

    /**
     * @return 交易状态，waitbuyerpay:等待买家付款;waitsellersend:等待卖家发货;waitlogisticstakein:等待物流公司揽件;waitbuyerreceive:等待买家收货;waitbuyersign:等待买家签收;signinsuccess:买家已签收;confirm_goods:已收货;success:交易成功;cancel:交易取消;terminated:交易终止;未枚举:其他状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置交易状态，waitbuyerpay:等待买家付款;waitsellersend:等待卖家发货;waitlogisticstakein:等待物流公司揽件;waitbuyerreceive:等待买家收货;waitbuyersign:等待买家签收;signinsuccess:买家已签收;confirm_goods:已收货;success:交易成功;cancel:交易取消;terminated:交易终止;未枚举:其他状态     *
     * 参数示例：<pre>waitbuyerpay</pre>     
     * 此参数必填
     */
    public void setStatus(String status) {
        this.status = status;
    }

    private BigDecimal totalAmount;

    /**
     * @return 应付款总金额，totalAmount = ∑itemAmount + shippingFee，单位为元
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置应付款总金额，totalAmount = ∑itemAmount + shippingFee，单位为元     *
     * 参数示例：<pre>1000</pre>     
     * 此参数必填
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    private String buyerRemarkIcon;

    /**
     * @return 买家备忘标志
     */
    public String getBuyerRemarkIcon() {
        return buyerRemarkIcon;
    }

    /**
     * 设置买家备忘标志     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setBuyerRemarkIcon(String buyerRemarkIcon) {
        this.buyerRemarkIcon = buyerRemarkIcon;
    }

    private String sellerRemarkIcon;

    /**
     * @return 卖家备忘标志
     */
    public String getSellerRemarkIcon() {
        return sellerRemarkIcon;
    }

    /**
     * 设置卖家备忘标志     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setSellerRemarkIcon(String sellerRemarkIcon) {
        this.sellerRemarkIcon = sellerRemarkIcon;
    }

    private Long discount;

    /**
     * @return 折扣信息，单位分
     */
    public Long getDiscount() {
        return discount;
    }

    /**
     * 设置折扣信息，单位分     *
     * 参数示例：<pre>11</pre>     
     * 此参数必填
     */
    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    private AlibabaTradeTradeContact buyerContact;

    /**
     * @return 买家联系人
     */
    public AlibabaTradeTradeContact getBuyerContact() {
        return buyerContact;
    }

    /**
     * 设置买家联系人     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setBuyerContact(AlibabaTradeTradeContact buyerContact) {
        this.buyerContact = buyerContact;
    }

    private String tradeType;

    /**
     * @return 1:担保交易
    2:预存款交易
    3:ETC境外收单交易
    4:即时到帐交易
    5:保障金安全交易
    6:统一交易流程
    7:分阶段付款
    8.货到付款交易
    9.信用凭证支付交易
    10.账期支付交易，50060 交易4.0
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * 设置1:担保交易
    2:预存款交易
    3:ETC境外收单交易
    4:即时到帐交易
    5:保障金安全交易
    6:统一交易流程
    7:分阶段付款
    8.货到付款交易
    9.信用凭证支付交易
    10.账期支付交易，50060 交易4.0     *
     * 参数示例：<pre>50060</pre>     
     * 此参数必填
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    private String refundStatus;

    /**
     * @return 订单的售中退款状态，等待卖家同意：waitselleragree ，待买家修改：waitbuyermodify，等待买家退货：waitbuyersend，等待卖家确认收货：waitsellerreceive，退款成功：refundsuccess，退款失败：refundclose
     */
    public String getRefundStatus() {
        return refundStatus;
    }

    /**
     * 设置订单的售中退款状态，等待卖家同意：waitselleragree ，待买家修改：waitbuyermodify，等待买家退货：waitbuyersend，等待卖家确认收货：waitsellerreceive，退款成功：refundsuccess，退款失败：refundclose     *
     * 参数示例：<pre>refundclose</pre>     
     * 此参数必填
     */
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    private String refundStatusForAs;

    /**
     * @return 订单的售后退款状态
     */
    public String getRefundStatusForAs() {
        return refundStatusForAs;
    }

    /**
     * 设置订单的售后退款状态     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setRefundStatusForAs(String refundStatusForAs) {
        this.refundStatusForAs = refundStatusForAs;
    }

    private Long refundPayment;

    /**
     * @return 退款金额
     */
    public Long getRefundPayment() {
        return refundPayment;
    }

    /**
     * 设置退款金额     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setRefundPayment(Long refundPayment) {
        this.refundPayment = refundPayment;
    }

    private String idOfStr;

    /**
     * @return 交易id(字符串格式)
     */
    public String getIdOfStr() {
        return idOfStr;
    }

    /**
     * 设置交易id(字符串格式)     *
     * 参数示例：<pre>123121212123</pre>     
     * 此参数必填
     */
    public void setIdOfStr(String idOfStr) {
        this.idOfStr = idOfStr;
    }

    private String alipayTradeId;

    /**
     * @return 外部支付交易Id
     */
    public String getAlipayTradeId() {
        return alipayTradeId;
    }

    /**
     * 设置外部支付交易Id     *
     * 参数示例：<pre>123123121111</pre>     
     * 此参数必填
     */
    public void setAlipayTradeId(String alipayTradeId) {
        this.alipayTradeId = alipayTradeId;
    }

    private AlibabaTradeOrderReceiverInfo receiverInfo;

    /**
     * @return 收件人信息
     */
    public AlibabaTradeOrderReceiverInfo getReceiverInfo() {
        return receiverInfo;
    }

    /**
     * 设置收件人信息     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setReceiverInfo(AlibabaTradeOrderReceiverInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    private String buyerLoginId;

    /**
     * @return 买家loginId，旺旺Id
     */
    public String getBuyerLoginId() {
        return buyerLoginId;
    }

    /**
     * 设置买家loginId，旺旺Id     *
     * 参数示例：<pre>alitestforusv01</pre>     
     * 此参数必填
     */
    public void setBuyerLoginId(String buyerLoginId) {
        this.buyerLoginId = buyerLoginId;
    }

    private String sellerLoginId;

    /**
     * @return 卖家oginId，旺旺Id
     */
    public String getSellerLoginId() {
        return sellerLoginId;
    }

    /**
     * 设置卖家oginId，旺旺Id     *
     * 参数示例：<pre>alitestforusv02</pre>     
     * 此参数必填
     */
    public void setSellerLoginId(String sellerLoginId) {
        this.sellerLoginId = sellerLoginId;
    }

    private Long buyerUserId;

    /**
     * @return 买家数字id
     */
    public Long getBuyerUserId() {
        return buyerUserId;
    }

    /**
     * 设置买家数字id     *
     * 参数示例：<pre>12314144</pre>     
     * 此参数必填
     */
    public void setBuyerUserId(Long buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    private Long sellerUserId;

    /**
     * @return 卖家数字id
     */
    public Long getSellerUserId() {
        return sellerUserId;
    }

    /**
     * 设置卖家数字id     *
     * 参数示例：<pre>12312422</pre>     
     * 此参数必填
     */
    public void setSellerUserId(Long sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    private String buyerAlipayId;

    /**
     * @return 买家支付宝id
     */
    public String getBuyerAlipayId() {
        return buyerAlipayId;
    }

    /**
     * 设置买家支付宝id     *
     * 参数示例：<pre>12312311233</pre>     
     * 此参数必填
     */
    public void setBuyerAlipayId(String buyerAlipayId) {
        this.buyerAlipayId = buyerAlipayId;
    }

    private String sellerAlipayId;

    /**
     * @return 卖家支付宝id
     */
    public String getSellerAlipayId() {
        return sellerAlipayId;
    }

    /**
     * 设置卖家支付宝id     *
     * 参数示例：<pre>12312311111</pre>     
     * 此参数必填
     */
    public void setSellerAlipayId(String sellerAlipayId) {
        this.sellerAlipayId = sellerAlipayId;
    }

    private Date confirmedTime;

    /**
     * @return 确认时间
     */
    public Date getConfirmedTime() {
        return confirmedTime;
    }

    /**
     * 设置确认时间     *
     * 参数示例：<pre>20180614101942000+0800</pre>     
     * 此参数必填
     */
    public void setConfirmedTime(Date confirmedTime) {
        this.confirmedTime = confirmedTime;
    }

    private String closeReason;

    /**
     * @return 关闭原因。buyerCancel:买家取消订单，sellerGoodsLack:卖家库存不足，other:其它
     */
    public String getCloseReason() {
        return closeReason;
    }

    /**
     * 设置关闭原因。buyerCancel:买家取消订单，sellerGoodsLack:卖家库存不足，other:其它     *
     * 参数示例：<pre>buyerCancel</pre>     
     * 此参数必填
     */
    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    private BigDecimal sumProductPayment;

    /**
     * @return 产品总金额(该订单产品明细表中的产品金额的和)，单位元
     */
    public BigDecimal getSumProductPayment() {
        return sumProductPayment;
    }

    /**
     * 设置产品总金额(该订单产品明细表中的产品金额的和)，单位元     *
     * 参数示例：<pre>1212</pre>     
     * 此参数必填
     */
    public void setSumProductPayment(BigDecimal sumProductPayment) {
        this.sumProductPayment = sumProductPayment;
    }

    private AlibabaTradeStepOrderModel[] stepOrderList;

    /**
     * @return [交易3.0]分阶段交易，分阶段订单list
     */
    public AlibabaTradeStepOrderModel[] getStepOrderList() {
        return stepOrderList;
    }

    /**
     * 设置[交易3.0]分阶段交易，分阶段订单list     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setStepOrderList(AlibabaTradeStepOrderModel[] stepOrderList) {
        this.stepOrderList = stepOrderList;
    }

    private String stepAgreementPath;

    /**
     * @return 分阶段法务协议地址
     */
    public String getStepAgreementPath() {
        return stepAgreementPath;
    }

    /**
     * 设置分阶段法务协议地址     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setStepAgreementPath(String stepAgreementPath) {
        this.stepAgreementPath = stepAgreementPath;
    }

    private Boolean stepPayAll;

    /**
     * @return 是否一次性付款
     */
    public Boolean getStepPayAll() {
        return stepPayAll;
    }

    /**
     * 设置是否一次性付款     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setStepPayAll(Boolean stepPayAll) {
        this.stepPayAll = stepPayAll;
    }

    private String buyerFeedback;

    /**
     * @return 买家留言，不超过500字
     */
    public String getBuyerFeedback() {
        return buyerFeedback;
    }

    /**
     * 设置买家留言，不超过500字     *
     * 参数示例：<pre>留言</pre>     
     * 此参数必填
     */
    public void setBuyerFeedback(String buyerFeedback) {
        this.buyerFeedback = buyerFeedback;
    }

    private Boolean overSeaOrder;

    /**
     * @return 是否海外代发订单，是：true
     */
    public Boolean getOverSeaOrder() {
        return overSeaOrder;
    }

    /**
     * 设置是否海外代发订单，是：true     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setOverSeaOrder(Boolean overSeaOrder) {
        this.overSeaOrder = overSeaOrder;
    }

    private String subBuyerLoginId;

    /**
     * @return 买家子账号
     */
    public String getSubBuyerLoginId() {
        return subBuyerLoginId;
    }

    /**
     * 设置买家子账号     *
     * 参数示例：<pre>alitestforusv02:temp</pre>     
     * 此参数必填
     */
    public void setSubBuyerLoginId(String subBuyerLoginId) {
        this.subBuyerLoginId = subBuyerLoginId;
    }

    private Boolean sellerOrder;

    /**
     * @return 是否自主订单（邀约订单）
     */
    public Boolean getSellerOrder() {
        return sellerOrder;
    }

    /**
     * 设置是否自主订单（邀约订单）     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setSellerOrder(Boolean sellerOrder) {
        this.sellerOrder = sellerOrder;
    }

    private Long preOrderId;

    /**
     * @return 预订单ID
     */
    public Long getPreOrderId() {
        return preOrderId;
    }

    /**
     * 设置预订单ID     *
     * 参数示例：<pre>123123</pre>     
     * 此参数必填
     */
    public void setPreOrderId(Long preOrderId) {
        this.preOrderId = preOrderId;
    }

    private String refundId;

    /**
     * @return 退款单ID
     */
    public String getRefundId() {
        return refundId;
    }

    /**
     * 设置退款单ID     *
     * 参数示例：<pre>TQ4562212313</pre>     
     * 此参数必填
     */
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    private String flowTemplateCode;

    /**
     * @return 4.0交易流程模板code
     */
    public String getFlowTemplateCode() {
        return flowTemplateCode;
    }

    /**
     * 设置4.0交易流程模板code     *
     * 参数示例：<pre>flow</pre>     
     * 此参数必填
     */
    public void setFlowTemplateCode(String flowTemplateCode) {
        this.flowTemplateCode = flowTemplateCode;
    }

    private String buyerLevel;

    /**
     * @return 买家等级
     */
    public String getBuyerLevel() {
        return buyerLevel;
    }

    /**
     * 设置买家等级     *
     * 参数示例：<pre>L1</pre>     
     * 此参数必填
     */
    public void setBuyerLevel(String buyerLevel) {
        this.buyerLevel = buyerLevel;
    }

    private String sellerCreditLevel;

    /**
     * @return 卖家诚信等级
     */
    public String getSellerCreditLevel() {
        return sellerCreditLevel;
    }

    /**
     * 设置卖家诚信等级     *
     * 参数示例：<pre>L1</pre>     
     * 此参数必填
     */
    public void setSellerCreditLevel(String sellerCreditLevel) {
        this.sellerCreditLevel = sellerCreditLevel;
    }

    private AlibabaTradeTradeSellerContact sellerContact;

    /**
     * @return 卖家联系人信息
     */
    public AlibabaTradeTradeSellerContact getSellerContact() {
        return sellerContact;
    }

    /**
     * 设置卖家联系人信息     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setSellerContact(AlibabaTradeTradeSellerContact sellerContact) {
        this.sellerContact = sellerContact;
    }

    private AlibabaTradeBizNewStepOrderModel[] newStepOrderList;

    /**
     * @return [交易4.0]分阶段交易，分阶段订单list
     */
    public AlibabaTradeBizNewStepOrderModel[] getNewStepOrderList() {
        return newStepOrderList;
    }

    /**
     * 设置[交易4.0]分阶段交易，分阶段订单list     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setNewStepOrderList(AlibabaTradeBizNewStepOrderModel[] newStepOrderList) {
        this.newStepOrderList = newStepOrderList;
    }

    private String closeRemark;

    /**
     * @return 关闭订单备注
     */
    public String getCloseRemark() {
        return closeRemark;
    }

    /**
     * 设置关闭订单备注     *
     * 参数示例：<pre>备注</pre>     
     * 此参数必填
     */
    public void setCloseRemark(String closeRemark) {
        this.closeRemark = closeRemark;
    }

    private String closeOperateType;

    /**
     * @return 关闭订单操作类型。CLOSE_TRADE_BY_SELLER:卖家关闭交易,CLOSE_TRADE_BY_BOPS:BOPS后台关闭交易,CLOSE_TRADE_BY_SYSTEM:系统（超时）关闭交易,CLOSE_TRADE_BY_BUYER:买家关闭交易,CLOSE_TRADE_BY_CREADIT:诚信保障投诉关闭
     */
    public String getCloseOperateType() {
        return closeOperateType;
    }

    /**
     * 设置关闭订单操作类型。CLOSE_TRADE_BY_SELLER:卖家关闭交易,CLOSE_TRADE_BY_BOPS:BOPS后台关闭交易,CLOSE_TRADE_BY_SYSTEM:系统（超时）关闭交易,CLOSE_TRADE_BY_BUYER:买家关闭交易,CLOSE_TRADE_BY_CREADIT:诚信保障投诉关闭     *
     * 参数示例：<pre>CLOSE_TRADE_BY_SELLER</pre>     
     * 此参数必填
     */
    public void setCloseOperateType(String closeOperateType) {
        this.closeOperateType = closeOperateType;
    }

    private BigDecimal couponFee;

    /**
     * @return 红包金额，实付金额（totalAmount）已经计算过红包金额
     */
    public BigDecimal getCouponFee() {
        return couponFee;
    }

    /**
     * 设置红包金额，实付金额（totalAmount）已经计算过红包金额     *
     * 参数示例：<pre>7.5</pre>     
     * 此参数必填
     */
    public void setCouponFee(BigDecimal couponFee) {
        this.couponFee = couponFee;
    }

    private String tradeTypeDesc;

    /**
     * @return 下单时指定的交易方式
     */
    public String getTradeTypeDesc() {
        return tradeTypeDesc;
    }

    /**
     * 设置下单时指定的交易方式     *
     * 参数示例：<pre>担保交易</pre>     
     * 此参数必填
     */
    public void setTradeTypeDesc(String tradeTypeDesc) {
        this.tradeTypeDesc = tradeTypeDesc;
    }

    private String[] payChannelList;

    /**
     * @return 支付渠道名称列表。一笔订单可能存在多种支付渠道。枚举值：支付宝,网商银行信任付,诚e赊,对公转账,赊销宝,账期支付,合并支付渠道,支付平台,声明付款,网商电子银行承兑汇票,银行转账,跨境宝,红包,其它
     */
    public String[] getPayChannelList() {
        return payChannelList;
    }

    /**
     * 设置支付渠道名称列表。一笔订单可能存在多种支付渠道。枚举值：支付宝,网商银行信任付,诚e赊,对公转账,赊销宝,账期支付,合并支付渠道,支付平台,声明付款,网商电子银行承兑汇票,银行转账,跨境宝,红包,其它     *
     * 参数示例：<pre>["支付宝","跨境宝","银行转账"]</pre>     
     * 此参数必填
     */
    public void setPayChannelList(String[] payChannelList) {
        this.payChannelList = payChannelList;
    }

    private String tradeTypeCode;

    /**
     * @return 下单时指定的交易方式tradeType
     */
    public String getTradeTypeCode() {
        return tradeTypeCode;
    }

    /**
     * 设置下单时指定的交易方式tradeType     *
     * 参数示例：<pre>assureTrade</pre>     
     * 此参数必填
     */
    public void setTradeTypeCode(String tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }

}
