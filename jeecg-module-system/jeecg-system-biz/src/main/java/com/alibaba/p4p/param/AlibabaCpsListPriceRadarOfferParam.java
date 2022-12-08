package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsListPriceRadarOfferParam extends AbstractAPIRequest<AlibabaCpsListPriceRadarOfferResult> {

    public AlibabaCpsListPriceRadarOfferParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.listPriceRadarOffer", 1);
    }

    private long[] offerIds;

    /**
     * @return offerId列表
     */
    public long[] getOfferIds() {
        return offerIds;
    }

    /**
     * 设置offerId列表     *
     * 参数示例：<pre>[2222,3333]</pre>     
     * 此参数必填
     */
    public void setOfferIds(long[] offerIds) {
        this.offerIds = offerIds;
    }

}
