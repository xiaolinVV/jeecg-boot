package com.alibaba.product.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaProductFollowParam extends AbstractAPIRequest<AlibabaProductFollowResult> {

    public AlibabaProductFollowParam() {
        super();
        oceanApiId = new APIId("com.alibaba.product", "alibaba.product.follow", 1);
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
     * 参数示例：<pre>3412111233445</pre>     
     * 此参数必填
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
