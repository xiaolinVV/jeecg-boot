package com.alibaba.logistics.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeGetLogisticsInfosBuyerViewParam extends AbstractAPIRequest<AlibabaTradeGetLogisticsInfosBuyerViewResult> {

    public AlibabaTradeGetLogisticsInfosBuyerViewParam() {
        super();
        oceanApiId = new APIId("com.alibaba.logistics", "alibaba.trade.getLogisticsInfos.buyerView", 1);
    }

    private Long orderId;

    /**
     * @return 订单号
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单号     *
     * 参数示例：<pre>1221434</pre>     
     * 此参数必填
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    private String fields;

    /**
     * @return 需要返回的字段，目前有:company.name,sender,receiver,sendgood。返回的字段要用英文逗号分隔开
     */
    public String getFields() {
        return fields;
    }

    /**
     * 设置需要返回的字段，目前有:company.name,sender,receiver,sendgood。返回的字段要用英文逗号分隔开     *
     * 参数示例：<pre>company,name,sender,receiver,sendgood</pre>     
     * 此参数必填
     */
    public void setFields(String fields) {
        this.fields = fields;
    }

    private String webSite;

    /**
     * @return 是1688业务还是icbu业务
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * 设置是1688业务还是icbu业务     *
     * 参数示例：<pre>1688或者alibaba</pre>     
     * 此参数必填
     */
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

}
