package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

import java.util.Date;

public class AlibabaTradeGetBuyerOrderListParam extends AbstractAPIRequest<AlibabaTradeGetBuyerOrderListResult> {

    public AlibabaTradeGetBuyerOrderListParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.getBuyerOrderList", 1);
    }

    private String[] bizTypes;

    /**
     * @return 业务类型，支持： "cn"(普通订单类型), "ws"(大额批发订单类型), "yp"(普通拿样订单类型), "yf"(一分钱拿样订单类型), "fs"(倒批(限时折扣)订单类型), "cz"(加工定制订单类型), "ag"(协议采购订单类型), "hp"(伙拼订单类型), "gc"(国采订单类型), "supply"(供销订单类型), "nyg"(nyg订单类型), "factory"(淘工厂订单类型), "quick"(快订下单), "xiangpin"(享拼订单), "nest"(采购商城-鸟巢), "f2f"(当面付), "cyfw"(存样服务), "sp"(代销订单标记), "wg"(微供订单), "factorysamp"(淘工厂打样订单), "factorybig"(淘工厂大货订单)
     */
    public String[] getBizTypes() {
        return bizTypes;
    }

    /**
     * 设置业务类型，支持： "cn"(普通订单类型), "ws"(大额批发订单类型), "yp"(普通拿样订单类型), "yf"(一分钱拿样订单类型), "fs"(倒批(限时折扣)订单类型), "cz"(加工定制订单类型), "ag"(协议采购订单类型), "hp"(伙拼订单类型), "gc"(国采订单类型), "supply"(供销订单类型), "nyg"(nyg订单类型), "factory"(淘工厂订单类型), "quick"(快订下单), "xiangpin"(享拼订单), "nest"(采购商城-鸟巢), "f2f"(当面付), "cyfw"(存样服务), "sp"(代销订单标记), "wg"(微供订单), "factorysamp"(淘工厂打样订单), "factorybig"(淘工厂大货订单)     *
     * 参数示例：<pre>["cn","ws"]</pre>     
     * 此参数必填
     */
    public void setBizTypes(String[] bizTypes) {
        this.bizTypes = bizTypes;
    }

    private Date createEndTime;

    /**
     * @return 下单结束时间
     */
    public Date getCreateEndTime() {
        return createEndTime;
    }

    /**
     * 设置下单结束时间     *
     * 参数示例：<pre>20180802211113000+0800</pre>     
     * 此参数必填
     */
    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    private Date createStartTime;

    /**
     * @return 下单开始时间
     */
    public Date getCreateStartTime() {
        return createStartTime;
    }

    /**
     * 设置下单开始时间     *
     * 参数示例：<pre>20180102211113000+0800</pre>     
     * 此参数必填
     */
    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    private Boolean isHis;

    /**
     * @return 是否查询历史订单表,默认查询当前表，即默认值为false
     */
    public Boolean getIsHis() {
        return isHis;
    }

    /**
     * 设置是否查询历史订单表,默认查询当前表，即默认值为false     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setIsHis(Boolean isHis) {
        this.isHis = isHis;
    }

    private Date modifyEndTime;

    /**
     * @return 查询修改时间结束
     */
    public Date getModifyEndTime() {
        return modifyEndTime;
    }

    /**
     * 设置查询修改时间结束     *
     * 参数示例：<pre>20180802211113000+0800</pre>     
     * 此参数必填
     */
    public void setModifyEndTime(Date modifyEndTime) {
        this.modifyEndTime = modifyEndTime;
    }

    private Date modifyStartTime;

    /**
     * @return 查询修改时间开始
     */
    public Date getModifyStartTime() {
        return modifyStartTime;
    }

    /**
     * 设置查询修改时间开始     *
     * 参数示例：<pre>20180102211113000+0800</pre>     
     * 此参数必填
     */
    public void setModifyStartTime(Date modifyStartTime) {
        this.modifyStartTime = modifyStartTime;
    }

    private String orderStatus;

    /**
     * @return 订单状态，值有 success, cancel(交易取消，违约金等交割完毕), waitbuyerpay(等待卖家付款)， waitsellersend(等待卖家发货), waitbuyerreceive(等待买家收货 )
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置订单状态，值有 success, cancel(交易取消，违约金等交割完毕), waitbuyerpay(等待卖家付款)， waitsellersend(等待卖家发货), waitbuyerreceive(等待买家收货 )     *
     * 参数示例：<pre>success</pre>     
     * 此参数必填
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    private Integer page;

    /**
     * @return 查询分页页码，从1开始
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 设置查询分页页码，从1开始     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    private Integer pageSize;

    /**
     * @return 查询的每页的数量
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置查询的每页的数量     *
     * 参数示例：<pre>20</pre>     
     * 此参数必填
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    private String refundStatus;

    /**
     * @return 退款状态，支持： "waitselleragree"(等待卖家同意), "refundsuccess"(退款成功), "refundclose"(退款关闭), "waitbuyermodify"(待买家修改), "waitbuyersend"(等待买家退货), "waitsellerreceive"(等待卖家确认收货)
     */
    public String getRefundStatus() {
        return refundStatus;
    }

    /**
     * 设置退款状态，支持： "waitselleragree"(等待卖家同意), "refundsuccess"(退款成功), "refundclose"(退款关闭), "waitbuyermodify"(待买家修改), "waitbuyersend"(等待买家退货), "waitsellerreceive"(等待卖家确认收货)     *
     * 参数示例：<pre>refundsuccess</pre>     
     * 此参数必填
     */
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
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
     * 参数示例：<pre>b2b-1624961198</pre>     
     * 此参数必填
     */
    public void setSellerMemberId(String sellerMemberId) {
        this.sellerMemberId = sellerMemberId;
    }

    private Integer sellerRateStatus;

    /**
     * @return 卖家评价状态 (4:已评价,5:未评价,6;不需要评价)
     */
    public Integer getSellerRateStatus() {
        return sellerRateStatus;
    }

    /**
     * 设置卖家评价状态 (4:已评价,5:未评价,6;不需要评价)     *
     * 参数示例：<pre>6</pre>     
     * 此参数必填
     */
    public void setSellerRateStatus(Integer sellerRateStatus) {
        this.sellerRateStatus = sellerRateStatus;
    }

    private String tradeType;

    /**
     * @return 交易类型:
    担保交易(1),
    预存款交易(2),
    ETC境外收单交易(3),
    即时到帐交易(4),
    保障金安全交易(5),
    统一交易流程(6),
    分阶段交易(7),
    货到付款交易(8),
    信用凭证支付交易(9),
    账期支付交易(10),
    1688交易4.0，新分阶段交易(50060),
    当面付的交易流程(50070),
    服务类的交易流程(50080)
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * 设置交易类型:
    担保交易(1),
    预存款交易(2),
    ETC境外收单交易(3),
    即时到帐交易(4),
    保障金安全交易(5),
    统一交易流程(6),
    分阶段交易(7),
    货到付款交易(8),
    信用凭证支付交易(9),
    账期支付交易(10),
    1688交易4.0，新分阶段交易(50060),
    当面付的交易流程(50070),
    服务类的交易流程(50080)     *
     * 参数示例：<pre>50060</pre>     
     * 此参数必填
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    private String productName;

    /**
     * @return 商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置商品名称     *
     * 参数示例：<pre>测试商品</pre>     
     * 此参数必填
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    private Boolean needBuyerAddressAndPhone;

    /**
     * @return 是否需要查询买家的详细地址信息和电话
     */
    public Boolean getNeedBuyerAddressAndPhone() {
        return needBuyerAddressAndPhone;
    }

    /**
     * 设置是否需要查询买家的详细地址信息和电话     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setNeedBuyerAddressAndPhone(Boolean needBuyerAddressAndPhone) {
        this.needBuyerAddressAndPhone = needBuyerAddressAndPhone;
    }

    private Boolean needMemoInfo;

    /**
     * @return 是否需要查询备注信息
     */
    public Boolean getNeedMemoInfo() {
        return needMemoInfo;
    }

    /**
     * 设置是否需要查询备注信息     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setNeedMemoInfo(Boolean needMemoInfo) {
        this.needMemoInfo = needMemoInfo;
    }

}
