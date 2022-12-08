package com.alibaba.product.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaProductUnfollowCrossborderParam extends AbstractAPIRequest<AlibabaProductUnfollowCrossborderResult> {

    public AlibabaProductUnfollowCrossborderParam() {
        super();
        oceanApiId = new APIId("com.alibaba.product", "alibaba.product.unfollow.crossborder", 1);
    }

    private Long productId;

    /**
     * @return 商品id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 设置商品id     *
     * 参数示例：<pre>36143645361</pre>     
     * 此参数必填
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
