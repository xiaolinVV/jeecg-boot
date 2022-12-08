package com.alibaba.product.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCategoryGetParam extends AbstractAPIRequest<AlibabaCategoryGetResult> {

    public AlibabaCategoryGetParam() {
        super();
        oceanApiId = new APIId("com.alibaba.product", "alibaba.category.get", 1);
    }

    private Long categoryID;

    /**
     * @return 类目id,必须大于等于0， 如果为0，则查询所有一级类目
     */
    public Long getCategoryID() {
        return categoryID;
    }

    /**
     * 设置类目id,必须大于等于0， 如果为0，则查询所有一级类目     *
     * 参数示例：<pre>1031910</pre>     
     * 此参数必填
     */
    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

}
