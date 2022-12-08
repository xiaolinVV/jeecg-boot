package com.alibaba.trade.param;

public class AlibabaCreateOrderPreviewResultModel {

    private Long discountFee;

    /**
     * @return 计算完货品金额后再次进行的减免金额. 单位: 分
     */
    public Long getDiscountFee() {
        return discountFee;
    }

    /**
     * 设置计算完货品金额后再次进行的减免金额. 单位: 分     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setDiscountFee(Long discountFee) {
        this.discountFee = discountFee;
    }

    private String[] tradeModeNameList;

    /**
     * @return 当前交易可以支持的交易方式列表。某些场景的创建订单接口需要使用。
     */
    public String[] getTradeModeNameList() {
        return tradeModeNameList;
    }

    /**
     * 设置当前交易可以支持的交易方式列表。某些场景的创建订单接口需要使用。     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTradeModeNameList(String[] tradeModeNameList) {
        this.tradeModeNameList = tradeModeNameList;
    }

    private Boolean status;

    /**
     * @return 状态
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置状态     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    private Boolean taoSampleSinglePromotion;

    /**
     * @return 是否有淘货源单品优惠  false:有单品优惠   true：没有单品优惠
     */
    public Boolean getTaoSampleSinglePromotion() {
        return taoSampleSinglePromotion;
    }

    /**
     * 设置是否有淘货源单品优惠  false:有单品优惠   true：没有单品优惠     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTaoSampleSinglePromotion(Boolean taoSampleSinglePromotion) {
        this.taoSampleSinglePromotion = taoSampleSinglePromotion;
    }

    private Long sumPayment;

    /**
     * @return 订单总费用, 单位为分.
     */
    public Long getSumPayment() {
        return sumPayment;
    }

    /**
     * 设置订单总费用, 单位为分.     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSumPayment(Long sumPayment) {
        this.sumPayment = sumPayment;
    }

    private String message;

    /**
     * @return 返回信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置返回信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMessage(String message) {
        this.message = message;
    }

    private Long sumCarriage;

    /**
     * @return 总运费信息, 单位为分.
     */
    public Long getSumCarriage() {
        return sumCarriage;
    }

    /**
     * 设置总运费信息, 单位为分.     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSumCarriage(Long sumCarriage) {
        this.sumCarriage = sumCarriage;
    }

    private String resultCode;

    /**
     * @return 返回码
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * 设置返回码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    private Long sumPaymentNoCarriage;

    /**
     * @return 不包含运费的货品总费用, 单位为分.
     */
    public Long getSumPaymentNoCarriage() {
        return sumPaymentNoCarriage;
    }

    /**
     * 设置不包含运费的货品总费用, 单位为分.     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSumPaymentNoCarriage(Long sumPaymentNoCarriage) {
        this.sumPaymentNoCarriage = sumPaymentNoCarriage;
    }

    private Long additionalFee;

    /**
     * @return 附加费,单位，分
     */
    public Long getAdditionalFee() {
        return additionalFee;
    }

    /**
     * 设置附加费,单位，分     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setAdditionalFee(Long additionalFee) {
        this.additionalFee = additionalFee;
    }

    private String flowFlag;

    /**
     * @return 订单下单流程
     */
    public String getFlowFlag() {
        return flowFlag;
    }

    /**
     * 设置订单下单流程     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFlowFlag(String flowFlag) {
        this.flowFlag = flowFlag;
    }

    private AlibabaCreateOrderPreviewResultCargoModel[] cargoList;

    /**
     * @return 规格信息
     */
    public AlibabaCreateOrderPreviewResultCargoModel[] getCargoList() {
        return cargoList;
    }

    /**
     * 设置规格信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCargoList(AlibabaCreateOrderPreviewResultCargoModel[] cargoList) {
        this.cargoList = cargoList;
    }

    private AlibabaTradePromotionModel[] shopPromotionList;

    /**
     * @return 可用店铺级别优惠列表
     */
    public AlibabaTradePromotionModel[] getShopPromotionList() {
        return shopPromotionList;
    }

    /**
     * 设置可用店铺级别优惠列表     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setShopPromotionList(AlibabaTradePromotionModel[] shopPromotionList) {
        this.shopPromotionList = shopPromotionList;
    }

    private TradeModelExtensionList[] tradeModelList;

    /**
     * @return 当前交易可以支持的交易方式列表。结果可以参照1688下单预览页面的交易方式。
     */
    public TradeModelExtensionList[] getTradeModelList() {
        return tradeModelList;
    }

    /**
     * 设置当前交易可以支持的交易方式列表。结果可以参照1688下单预览页面的交易方式。     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTradeModelList(TradeModelExtensionList[] tradeModelList) {
        this.tradeModelList = tradeModelList;
    }

}
