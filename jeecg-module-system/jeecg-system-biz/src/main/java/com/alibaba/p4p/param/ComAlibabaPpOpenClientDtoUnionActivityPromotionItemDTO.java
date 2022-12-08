package com.alibaba.p4p.param;

public class ComAlibabaPpOpenClientDtoUnionActivityPromotionItemDTO {

    private Long skuId;

    /**
     * @return sku优惠结果时有意义；对于区间价的优惠结果，此字段无意义，可能为null
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置sku优惠结果时有意义；对于区间价的优惠结果，此字段无意义，可能为null     *
     * 参数示例：<pre>111</pre>     
     * 此参数必填
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    private Long originalPrice;

    /**
     * @return 原价，单位分
     */
    public Long getOriginalPrice() {
        return originalPrice;
    }

    /**
     * 设置原价，单位分     *
     * 参数示例：<pre>111</pre>     
     * 此参数必填
     */
    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    private Long promotionPrice;

    /**
     * @return 优惠价，单位分
     */
    public Long getPromotionPrice() {
        return promotionPrice;
    }

    /**
     * 设置优惠价，单位分     *
     * 参数示例：<pre>222</pre>     
     * 此参数必填
     */
    public void setPromotionPrice(Long promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

}
