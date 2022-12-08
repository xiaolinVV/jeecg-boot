package com.alibaba.p4p.param;

import java.util.Date;

public class ComAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO {

    private Date createTime;

    /**
     * @return 加入选品组时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置加入选品组时间     *
     * 参数示例：<pre>20190410000000000+0800</pre>     
     * 此参数必填
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Long feedId;

    /**
     * @return 商品id
     */
    public Long getFeedId() {
        return feedId;
    }

    /**
     * 设置商品id     *
     * 参数示例：<pre>11</pre>     
     * 此参数必填
     */
    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    private String title;

    /**
     * @return 商品标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置商品标题     *
     * 参数示例：<pre>标题</pre>     
     * 此参数必填
     */
    public void setTitle(String title) {
        this.title = title;
    }

    private String price;

    /**
     * @return 原则
     */
    public String getPrice() {
        return price;
    }

    /**
     * 设置原则     *
     * 参数示例：<pre>1.1</pre>     
     * 此参数必填
     */
    public void setPrice(String price) {
        this.price = price;
    }

    private String promotionPrice;

    /**
     * @return 超买价
     */
    public String getPromotionPrice() {
        return promotionPrice;
    }

    /**
     * 设置超买价     *
     * 参数示例：<pre>0.9</pre>     
     * 此参数必填
     */
    public void setPromotionPrice(String promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    private String promotionSpace;

    /**
     * @return 优惠空间
     */
    public String getPromotionSpace() {
        return promotionSpace;
    }

    /**
     * 设置优惠空间     *
     * 参数示例：<pre>-</pre>     
     * 此参数必填
     */
    public void setPromotionSpace(String promotionSpace) {
        this.promotionSpace = promotionSpace;
    }

    private Boolean invalid;

    /**
     * @return 是否失效，有效false;无效true，查看invalidInfo字段失效原因
     */
    public Boolean getInvalid() {
        return invalid;
    }

    /**
     * 设置是否失效，有效false;无效true，查看invalidInfo字段失效原因     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    private String invalidInfo;

    /**
     * @return 失效信息
     */
    public String getInvalidInfo() {
        return invalidInfo;
    }

    /**
     * 设置失效信息     *
     * 参数示例：<pre>下架</pre>     
     * 此参数必填
     */
    public void setInvalidInfo(String invalidInfo) {
        this.invalidInfo = invalidInfo;
    }

    private String imgUrl;

    /**
     * @return 商品首图
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置商品首图     *
     * 参数示例：<pre>http://img.com</pre>     
     * 此参数必填
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String saleCount;

    /**
     * @return 销量
     */
    public String getSaleCount() {
        return saleCount;
    }

    /**
     * 设置销量     *
     * 参数示例：<pre>333</pre>     
     * 此参数必填
     */
    public void setSaleCount(String saleCount) {
        this.saleCount = saleCount;
    }

    private String channelPrice;

    /**
     * @return 渠道专属价
     */
    public String getChannelPrice() {
        return channelPrice;
    }

    /**
     * 设置渠道专属价     *
     * 参数示例：<pre>11.12</pre>     
     * 此参数必填
     */
    public void setChannelPrice(String channelPrice) {
        this.channelPrice = channelPrice;
    }

}
