package com.alibaba.product.param;

public class ComAlibabaOceanOpenplatformBizProductCommonModelProductIntelligentInfo {

    private String title;

    /**
     * @return 算法优化后的商品标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置算法优化后的商品标题     *
     * 参数示例：<pre>算法优化后的商品标题</pre>     
     * 此参数必填
     */
    public void setTitle(String title) {
        this.title = title;
    }

    private String[] images;

    /**
     * @return 算法优化后的商品图片
     */
    public String[] getImages() {
        return images;
    }

    /**
     * 设置算法优化后的商品图片     *
     * 参数示例：<pre>["https://cbu01.alicdn.com/img/ibank/2020/932/210/13529012239_321095253.jpg"]</pre>     
     * 此参数必填
     */
    public void setImages(String[] images) {
        this.images = images;
    }

    private ComAlibabaOceanOpenplatformBizProductCommonModelSkuIntelligentInfo[] skuImages;

    /**
     * @return 算法优化后的规格图片
     */
    public ComAlibabaOceanOpenplatformBizProductCommonModelSkuIntelligentInfo[] getSkuImages() {
        return skuImages;
    }

    /**
     * 设置算法优化后的规格图片     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setSkuImages(ComAlibabaOceanOpenplatformBizProductCommonModelSkuIntelligentInfo[] skuImages) {
        this.skuImages = skuImages;
    }

    private String[] descriptionImages;

    /**
     * @return 算法优化后的详情图片
     */
    public String[] getDescriptionImages() {
        return descriptionImages;
    }

    /**
     * 设置算法优化后的详情图片     *
     * 参数示例：<pre>["https://cbu01.alicdn.com/img/ibank/2020/932/210/13529012239_321095253.jpg"]</pre>     
     * 此参数必填
     */
    public void setDescriptionImages(String[] descriptionImages) {
        this.descriptionImages = descriptionImages;
    }

}
