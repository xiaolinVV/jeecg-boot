package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeCreateOrderCybMediaParam extends AbstractAPIRequest<AlibabaTradeCreateOrderCybMediaResult> {

    public AlibabaTradeCreateOrderCybMediaParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.createOrder4CybMedia", 1);
    }

    private AlibabaTradeFastAddress addressParam;

    /**
     * @return 收货地址
     */
    public AlibabaTradeFastAddress getAddressParam() {
        return addressParam;
    }

    /**
     * 设置收货地址     *
     * 参数示例：<pre>{"address":"网商路699号","phone": "0517-88990077","mobile": "15251667788","fullName": "张三","postCode": "000000","areaText": "滨江区","townText": "","cityText": "杭州市","provinceText": "浙江省"}</pre>     
     * 此参数必填
     */
    public void setAddressParam(AlibabaTradeFastAddress addressParam) {
        this.addressParam = addressParam;
    }

    private AlibabaTradeFastCargo[] cargoParamList;

    /**
     * @return 购买的商品信息
     */
    public AlibabaTradeFastCargo[] getCargoParamList() {
        return cargoParamList;
    }

    /**
     * 设置购买的商品信息     *
     * 参数示例：<pre>[{"specId": "b266e0726506185beaf205cbae88530d","quantity": 5,"offerId": 554456348334},{"specId": "2ba3d63866a71fbae83909d9b4814f01","quantity": 6,"offerId": 554456348334}]</pre>     
     * 此参数必填
     */
    public void setCargoParamList(AlibabaTradeFastCargo[] cargoParamList) {
        this.cargoParamList = cargoParamList;
    }

    private String message;

    /**
     * @return 买家留言
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置买家留言     *
     * 参数示例：<pre>留言</pre>     
     * 此参数必填
     */
    public void setMessage(String message) {
        this.message = message;
    }

    private String outerOrderInfo;

    /**
     * @return 机构的订单信息，json格式，如果校验不通过，不能创建订单。业务线原因机构订单信息不能放在上面的下单参数结构体中，只能新增该字段用于机构订单信息回写，没有幂等校验，纯记录统计分析。mediaOrderId:机构订单号;phone:电话;offers.id:Long,1688商品id;offers.specId:String,1688商品specId(可能无);offers.price:Long,媒体溢价单价(单位分);offers.num:Long,售卖数量
     */
    public String getOuterOrderInfo() {
        return outerOrderInfo;
    }

    /**
     * 设置机构的订单信息，json格式，如果校验不通过，不能创建订单。业务线原因机构订单信息不能放在上面的下单参数结构体中，只能新增该字段用于机构订单信息回写，没有幂等校验，纯记录统计分析。mediaOrderId:机构订单号;phone:电话;offers.id:Long,1688商品id;offers.specId:String,1688商品specId(可能无);offers.price:Long,媒体溢价单价(单位分);offers.num:Long,售卖数量     *
     * 参数示例：<pre>{"mediaOrderId":11,"phone":"13800138000","offers":[{"id":586053789191,"specId":"af478130f6c683c4c77bb511796617d7","price":12343,"num":1}]}</pre>     
     * 此参数必填
     */
    public void setOuterOrderInfo(String outerOrderInfo) {
        this.outerOrderInfo = outerOrderInfo;
    }

    private String tradeType;

    /**
     * @return 由于不同的商品支持的交易方式不同，没有一种交易方式是全局通用的，所以当前下单可使用的交易方式必须通过下单预览接口的tradeModeNameList获取。交易方式类型说明：fxassure（交易4.0通用担保交易），alipay（大市场通用的支付宝担保交易（目前在做切流，后续会下掉）），period（普通账期交易）, assure（大买家企业采购询报价下单时需要使用的担保交易流程）, creditBuy（诚E赊），bank（银行转账），631staged（631分阶段付款），37staged（37分阶段）；此字段不传则系统默认会选取一个可用的交易方式下单，如果开通了诚E赊默认是creditBuy（诚E赊），未开通诚E赊默认使用的方式是支付宝担宝交易。
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * 设置由于不同的商品支持的交易方式不同，没有一种交易方式是全局通用的，所以当前下单可使用的交易方式必须通过下单预览接口的tradeModeNameList获取。交易方式类型说明：fxassure（交易4.0通用担保交易），alipay（大市场通用的支付宝担保交易（目前在做切流，后续会下掉）），period（普通账期交易）, assure（大买家企业采购询报价下单时需要使用的担保交易流程）, creditBuy（诚E赊），bank（银行转账），631staged（631分阶段付款），37staged（37分阶段）；此字段不传则系统默认会选取一个可用的交易方式下单，如果开通了诚E赊默认是creditBuy（诚E赊），未开通诚E赊默认使用的方式是支付宝担宝交易。     *
     * 参数示例：<pre>fxassure</pre>     
     * 此参数必填
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    private Boolean useChannelPrice;

    /**
     * @return 是否优先使用渠道专享价，不传或传true都是优先使用渠道传享价，传false则不走渠道专享价，走普通的分销价
     */
    public Boolean getUseChannelPrice() {
        return useChannelPrice;
    }

    /**
     * 设置是否优先使用渠道专享价，不传或传true都是优先使用渠道传享价，传false则不走渠道专享价，走普通的分销价     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setUseChannelPrice(Boolean useChannelPrice) {
        this.useChannelPrice = useChannelPrice;
    }

}
