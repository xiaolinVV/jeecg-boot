package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeRefundReturnGoodsParam extends AbstractAPIRequest<AlibabaTradeRefundReturnGoodsResult> {

    public AlibabaTradeRefundReturnGoodsParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.refund.returnGoods", 1);
    }

    private String refundId;

    /**
     * @return 退款单号，TQ开头
     */
    public String getRefundId() {
        return refundId;
    }

    /**
     * 设置退款单号，TQ开头     *
     * 参数示例：<pre>TQ36706338027991577</pre>     
     * 此参数必填
     */
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    private String logisticsCompanyNo;

    /**
     * @return 物流公司编码，调用alibaba.logistics.OpQueryLogisticCompanyList.offline接口查询
     */
    public String getLogisticsCompanyNo() {
        return logisticsCompanyNo;
    }

    /**
     * 设置物流公司编码，调用alibaba.logistics.OpQueryLogisticCompanyList.offline接口查询     *
     * 参数示例：<pre>ZTO</pre>     
     * 此参数必填
     */
    public void setLogisticsCompanyNo(String logisticsCompanyNo) {
        this.logisticsCompanyNo = logisticsCompanyNo;
    }

    private String freightBill;

    /**
     * @return 物流公司运单号，请准确填写，否则卖家有权拒绝退款
     */
    public String getFreightBill() {
        return freightBill;
    }

    /**
     * 设置物流公司运单号，请准确填写，否则卖家有权拒绝退款     *
     * 参数示例：<pre>3110044550034338</pre>     
     * 此参数必填
     */
    public void setFreightBill(String freightBill) {
        this.freightBill = freightBill;
    }

    private String description;

    /**
     * @return 发货说明，内容在2-200个字之间
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置发货说明，内容在2-200个字之间     *
     * 参数示例：<pre>发货说明</pre>     
     * 此参数必填
     */
    public void setDescription(String description) {
        this.description = description;
    }

    private String[] vouchers;

    /**
     * @return 凭证图片URLs，必须使用API alibaba.trade.uploadRefundVoucher返回的“图片域名/相对路径”，最多可上传 10 张图片 ；单张大小不超过1M；支持jpg、gif、jpeg、png、和bmp格式。 请上传凭证，以便以后续赔所需（不上传将无法理赔）
     */
    public String[] getVouchers() {
        return vouchers;
    }

    /**
     * 设置凭证图片URLs，必须使用API alibaba.trade.uploadRefundVoucher返回的“图片域名/相对路径”，最多可上传 10 张图片 ；单张大小不超过1M；支持jpg、gif、jpeg、png、和bmp格式。 请上传凭证，以便以后续赔所需（不上传将无法理赔）     *
     * 参数示例：<pre>[https://cbu01.alicdn.com/img/ibank/2019/901/930/11848039109.jpg]</pre>     
     * 此参数必填
     */
    public void setVouchers(String[] vouchers) {
        this.vouchers = vouchers;
    }

}
