package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

import java.util.Date;

public class AlibabaTradeRefundBuyerQueryOrderRefundListParam extends AbstractAPIRequest<AlibabaTradeRefundBuyerQueryOrderRefundListResult> {

    public AlibabaTradeRefundBuyerQueryOrderRefundListParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.refund.buyer.queryOrderRefundList", 1);
    }

    private Long orderId;

    /**
     * @return 订单Id
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单Id     *
     * 参数示例：<pre>179087886005498520</pre>     
     * 此参数必填
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    private Date applyStartTime;

    /**
     * @return 退款申请时间（起始）
     */
    public Date getApplyStartTime() {
        return applyStartTime;
    }

    /**
     * 设置退款申请时间（起始）     *
     * 参数示例：<pre>20170926114526000+0800</pre>     
     * 此参数必填
     */
    public void setApplyStartTime(Date applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    private Date applyEndTime;

    /**
     * @return 退款申请时间（截止）
     */
    public Date getApplyEndTime() {
        return applyEndTime;
    }

    /**
     * 设置退款申请时间（截止）     *
     * 参数示例：<pre>20220926114526000+0800</pre>     
     * 此参数必填
     */
    public void setApplyEndTime(Date applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    private String[] refundStatusSet;

    /**
     * @return 退款状态列表
     */
    public String[] getRefundStatusSet() {
        return refundStatusSet;
    }

    /**
     * 设置退款状态列表     *
     * 参数示例：<pre>等待卖家同意 waitselleragree;退款成功 refundsuccess;退款关闭 refundclose;待买家修改 waitbuyermodify;等待买家退货 waitbuyersend;等待卖家确认收货 waitsellerreceive</pre>     
     * 此参数必填
     */
    public void setRefundStatusSet(String[] refundStatusSet) {
        this.refundStatusSet = refundStatusSet;
    }

    private String sellerMemberId;

    /**
     * @return 卖家memberId
     */
    public String getSellerMemberId() {
        return sellerMemberId;
    }

    /**
     * 设置卖家memberId     *
     * 参数示例：<pre>b2b-1623492085</pre>     
     * 此参数必填
     */
    public void setSellerMemberId(String sellerMemberId) {
        this.sellerMemberId = sellerMemberId;
    }

    private Integer currentPageNum;

    /**
     * @return 当前页码
     */
    public Integer getCurrentPageNum() {
        return currentPageNum;
    }

    /**
     * 设置当前页码     *
     * 参数示例：<pre>0</pre>     
     * 此参数必填
     */
    public void setCurrentPageNum(Integer currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    private Integer pageSize;

    /**
     * @return 每页条数
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页条数     *
     * 参数示例：<pre>20</pre>     
     * 此参数必填
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    private String logisticsNo;

    /**
     * @return 退货物流单号（传此字段查询时，需同时传入sellerMemberId）
     */
    public String getLogisticsNo() {
        return logisticsNo;
    }

    /**
     * 设置退货物流单号（传此字段查询时，需同时传入sellerMemberId）     *
     * 参数示例：<pre>3101***159271</pre>     
     * 此参数必填
     */
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    private Date modifyStartTime;

    /**
     * @return 退款修改时间(起始)
     */
    public Date getModifyStartTime() {
        return modifyStartTime;
    }

    /**
     * 设置退款修改时间(起始)     *
     * 参数示例：<pre>20170926114526000+0800</pre>     
     * 此参数必填
     */
    public void setModifyStartTime(Date modifyStartTime) {
        this.modifyStartTime = modifyStartTime;
    }

    private Date modifyEndTime;

    /**
     * @return 退款修改时间(截止)
     */
    public Date getModifyEndTime() {
        return modifyEndTime;
    }

    /**
     * 设置退款修改时间(截止)     *
     * 参数示例：<pre>20220926114526000+0800</pre>     
     * 此参数必填
     */
    public void setModifyEndTime(Date modifyEndTime) {
        this.modifyEndTime = modifyEndTime;
    }

    private Integer dipsuteType;

    /**
     * @return 1:售中退款，2:售后退款；0:所有退款单
     */
    public Integer getDipsuteType() {
        return dipsuteType;
    }

    /**
     * 设置1:售中退款，2:售后退款；0:所有退款单     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setDipsuteType(Integer dipsuteType) {
        this.dipsuteType = dipsuteType;
    }

}
