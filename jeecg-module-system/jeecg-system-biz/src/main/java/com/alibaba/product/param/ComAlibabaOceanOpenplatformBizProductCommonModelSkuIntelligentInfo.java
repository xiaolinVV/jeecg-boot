package com.alibaba.product.param;

public class ComAlibabaOceanOpenplatformBizProductCommonModelSkuIntelligentInfo {

    private Long skuId;

    /**
     * @return skuId
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置skuId     *
     * 参数示例：<pre>123433333</pre>     
     * 此参数必填
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    private String imageUrl;

    /**
     * @return 算法处理后的图片地址，未处理则返回原图片地址
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置算法处理后的图片地址，未处理则返回原图片地址     *
     * 参数示例：<pre>https://cbu01.alicdn.com/img/ibank/2020/932/210/13529012239_321095253.jpg</pre>     
     * 此参数必填
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
