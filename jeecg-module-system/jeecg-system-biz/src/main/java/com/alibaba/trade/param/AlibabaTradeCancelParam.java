package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeCancelParam extends AbstractAPIRequest<AlibabaTradeCancelResult> {

    public AlibabaTradeCancelParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.cancel", 1);
    }

    private String webSite;

    /**
     * @return 站点信息，指定调用的API是属于国际站（alibaba）还是1688网站（1688）
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * 设置站点信息，指定调用的API是属于国际站（alibaba）还是1688网站（1688）     *
     * 参数示例：<pre>1688</pre>     
     * 此参数必填
     */
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    private Long tradeID;

    /**
     * @return 交易id，订单号
     */
    public Long getTradeID() {
        return tradeID;
    }

    /**
     * 设置交易id，订单号     *
     * 参数示例：<pre>123456</pre>     
     * 此参数必填
     */
    public void setTradeID(Long tradeID) {
        this.tradeID = tradeID;
    }

    private String cancelReason;

    /**
     * @return 原因描述；buyerCancel:买家取消订单;sellerGoodsLack:卖家库存不足;other:其它
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * 设置原因描述；buyerCancel:买家取消订单;sellerGoodsLack:卖家库存不足;other:其它     *
     * 参数示例：<pre>other</pre>     
     * 此参数必填
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    private String remark;

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注     *
     * 参数示例：<pre>备注</pre>     
     * 此参数必填
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
