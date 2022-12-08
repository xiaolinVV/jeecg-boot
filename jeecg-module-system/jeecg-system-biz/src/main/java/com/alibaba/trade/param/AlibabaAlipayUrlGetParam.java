package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaAlipayUrlGetParam extends AbstractAPIRequest<AlibabaAlipayUrlGetResult> {

    public AlibabaAlipayUrlGetParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.alipay.url.get", 1);
    }

    private long[] orderIdList;

    /**
     * @return 订单Id列表,最多批量30个订单，订单过多会导致超时，建议一次10个订单
     */
    public long[] getOrderIdList() {
        return orderIdList;
    }

    /**
     * 设置订单Id列表,最多批量30个订单，订单过多会导致超时，建议一次10个订单     *
     * 参数示例：<pre>[74321349391498520]</pre>     
     * 此参数必填
     */
    public void setOrderIdList(long[] orderIdList) {
        this.orderIdList = orderIdList;
    }

}
