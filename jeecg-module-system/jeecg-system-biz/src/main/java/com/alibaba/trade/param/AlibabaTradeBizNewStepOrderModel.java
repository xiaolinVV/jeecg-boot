package com.alibaba.trade.param;

import java.math.BigDecimal;
import java.util.Date;

public class AlibabaTradeBizNewStepOrderModel {

    private Date gmtStart;

    /**
     * @return 阶段开始时间
     */
    public Date getGmtStart() {
        return gmtStart;
    }

    /**
     * 设置阶段开始时间     *
     * 参数示例：<pre>20180604092517000+0800</pre>     
     * 此参数必填
     */
    public void setGmtStart(Date gmtStart) {
        this.gmtStart = gmtStart;
    }

    private Date gmtPay;

    /**
     * @return 付款时间
     */
    public Date getGmtPay() {
        return gmtPay;
    }

    /**
     * 设置付款时间     *
     * 参数示例：<pre>20180604093243000+0800</pre>     
     * 此参数必填
     */
    public void setGmtPay(Date gmtPay) {
        this.gmtPay = gmtPay;
    }

    private Date gmtEnd;

    /**
     * @return 阶段结束时间
     */
    public Date getGmtEnd() {
        return gmtEnd;
    }

    /**
     * 设置阶段结束时间     *
     * 参数示例：<pre>20180604093243000+0800</pre>     
     * 此参数必填
     */
    public void setGmtEnd(Date gmtEnd) {
        this.gmtEnd = gmtEnd;
    }

    private Integer stepNo;

    /**
     * @return 阶段顺序编号
     */
    public Integer getStepNo() {
        return stepNo;
    }

    /**
     * 设置阶段顺序编号     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    private Boolean lastStep;

    /**
     * @return 是否最后一个阶段
     */
    public Boolean getLastStep() {
        return lastStep;
    }

    /**
     * 设置是否最后一个阶段     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setLastStep(Boolean lastStep) {
        this.lastStep = lastStep;
    }

    private String stepName;

    /**
     * @return 阶段名称
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * 设置阶段名称     *
     * 参数示例：<pre>全款交易</pre>     
     * 此参数必填
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    private Integer activeStatus;

    /**
     * @return 激活状态。0表示未激活，1表示已激活
     */
    public Integer getActiveStatus() {
        return activeStatus;
    }

    /**
     * 设置激活状态。0表示未激活，1表示已激活     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    private Integer payStatus;

    /**
     * @return 阶段付款状态。1未付款、2已付款、8付款前取消、12溢短补付款
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * 设置阶段付款状态。1未付款、2已付款、8付款前取消、12溢短补付款     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    private Integer logisticsStatus;

    /**
     * @return 物流环节状态：1未发货、2已发货、3已收货、4已全部退货、7发货前取消
     */
    public Integer getLogisticsStatus() {
        return logisticsStatus;
    }

    /**
     * 设置物流环节状态：1未发货、2已发货、3已收货、4已全部退货、7发货前取消     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setLogisticsStatus(Integer logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    private BigDecimal payFee;

    /**
     * @return 阶段应付款（包含运费），单位为元
     */
    public BigDecimal getPayFee() {
        return payFee;
    }

    /**
     * 设置阶段应付款（包含运费），单位为元     *
     * 参数示例：<pre>0.03</pre>     
     * 此参数必填
     */
    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }

    private BigDecimal paidFee;

    /**
     * @return 阶段已付款（包含运费），单位为元
     */
    public BigDecimal getPaidFee() {
        return paidFee;
    }

    /**
     * 设置阶段已付款（包含运费），单位为元     *
     * 参数示例：<pre>0.03</pre>     
     * 此参数必填
     */
    public void setPaidFee(BigDecimal paidFee) {
        this.paidFee = paidFee;
    }

    private BigDecimal goodsFee;

    /**
     * @return 阶段商品价格分摊 ，单位为元
     */
    public BigDecimal getGoodsFee() {
        return goodsFee;
    }

    /**
     * 设置阶段商品价格分摊 ，单位为元     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setGoodsFee(BigDecimal goodsFee) {
        this.goodsFee = goodsFee;
    }

    private BigDecimal adjustFee;

    /**
     * @return 阶段调整价格 ，单位为元
     */
    public BigDecimal getAdjustFee() {
        return adjustFee;
    }

    /**
     * 设置阶段调整价格 ，单位为元     *
     * 参数示例：<pre>-3175.97</pre>     
     * 此参数必填
     */
    public void setAdjustFee(BigDecimal adjustFee) {
        this.adjustFee = adjustFee;
    }

    private BigDecimal discountFee;

    /**
     * @return 阶段优惠价格，单位为元
     */
    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    /**
     * 设置阶段优惠价格，单位为元     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    private BigDecimal postFee;

    /**
     * @return 阶段的应付邮费，单位为元
     */
    public BigDecimal getPostFee() {
        return postFee;
    }

    /**
     * 设置阶段的应付邮费，单位为元     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    private BigDecimal paidPostFee;

    /**
     * @return 阶段已付的邮费，单位为元
     */
    public BigDecimal getPaidPostFee() {
        return paidPostFee;
    }

    /**
     * 设置阶段已付的邮费，单位为元     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setPaidPostFee(BigDecimal paidPostFee) {
        this.paidPostFee = paidPostFee;
    }

}
