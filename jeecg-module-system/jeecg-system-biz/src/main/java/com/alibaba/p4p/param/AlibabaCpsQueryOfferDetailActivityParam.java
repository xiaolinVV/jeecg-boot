package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsQueryOfferDetailActivityParam extends AbstractAPIRequest<AlibabaCpsQueryOfferDetailActivityResult> {

    public AlibabaCpsQueryOfferDetailActivityParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.queryOfferDetailActivity", 1);
    }

    private Long offerId;

    /**
     * @return 商品id
     */
    public Long getOfferId() {
        return offerId;
    }

    /**
     * 设置商品id     *
     * 参数示例：<pre>591047134663</pre>     
     * 此参数必填
     */
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

}
