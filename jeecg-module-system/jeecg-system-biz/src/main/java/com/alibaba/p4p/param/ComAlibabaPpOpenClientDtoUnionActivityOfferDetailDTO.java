package com.alibaba.p4p.param;

import java.util.Date;

public class ComAlibabaPpOpenClientDtoUnionActivityOfferDetailDTO {

    private Long offerId;

    /**
     * @return 商品id
     */
    public Long getOfferId() {
        return offerId;
    }

    /**
     * 设置商品id     *
     * 参数示例：<pre>111</pre>     
     * 此参数必填
     */
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    private Long activityId;

    /**
     * @return 营销活动Id
     */
    public Long getActivityId() {
        return activityId;
    }

    /**
     * 设置营销活动Id     *
     * 参数示例：<pre>11</pre>     
     * 此参数必填
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    private String activityName;

    /**
     * @return 活动名称
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * 设置活动名称     *
     * 参数示例：<pre>活动名称</pre>     
     * 此参数必填
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    private Date hotTime;

    /**
     * @return 预热时间,活动未开始,不可用活动价下单; 为null表示无预热时间
     */
    public Date getHotTime() {
        return hotTime;
    }

    /**
     * 设置预热时间,活动未开始,不可用活动价下单; 为null表示无预热时间     *
     * 参数示例：<pre>20190410000000000+0800</pre>     
     * 此参数必填
     */
    public void setHotTime(Date hotTime) {
        this.hotTime = hotTime;
    }

    private Date startTime;

    /**
     * @return 活动开始时间；大于now时，活动有效
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置活动开始时间；大于now时，活动有效     *
     * 参数示例：<pre>20190410000000000+0800</pre>     
     * 此参数必填
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    private Date endTime;

    /**
     * @return 活动结束时间；小于now时，活动有效
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置活动结束时间；小于now时，活动有效     *
     * 参数示例：<pre>20190410000000000+0800</pre>     
     * 此参数必填
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    private Integer beginQuantity;

    /**
     * @return 活动起批量
     */
    public Integer getBeginQuantity() {
        return beginQuantity;
    }

    /**
     * 设置活动起批量     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setBeginQuantity(Integer beginQuantity) {
        this.beginQuantity = beginQuantity;
    }

    private Integer stock;

    /**
     * @return 活动总库存，为null时使用offer原库存
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * 设置活动总库存，为null时使用offer原库存     *
     * 参数示例：<pre>3</pre>     
     * 此参数必填
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    private Integer personLimitCount;

    /**
     * @return 商品本身限购数，非活动价可购买数；-1表示不限，0表示可购买数为0；3个*LimitCount字段都等于-1时，表示没有任何限购
     */
    public Integer getPersonLimitCount() {
        return personLimitCount;
    }

    /**
     * 设置商品本身限购数，非活动价可购买数；-1表示不限，0表示可购买数为0；3个*LimitCount字段都等于-1时，表示没有任何限购     *
     * 参数示例：<pre>-1</pre>     
     * 此参数必填
     */
    public void setPersonLimitCount(Integer personLimitCount) {
        this.personLimitCount = personLimitCount;
    }

    private Integer promotionLimitCount;

    /**
     * @return 限购数，等于0且personLimitCount>0时，可以以原价下单，但不能以活动价下单；-1表示不限数量；3个*LimitCount字段都等于-1时，表示没有任何限购
     */
    public Integer getPromotionLimitCount() {
        return promotionLimitCount;
    }

    /**
     * 设置限购数，等于0且personLimitCount>0时，可以以原价下单，但不能以活动价下单；-1表示不限数量；3个*LimitCount字段都等于-1时，表示没有任何限购     *
     * 参数示例：<pre>-1</pre>     
     * 此参数必填
     */
    public void setPromotionLimitCount(Integer promotionLimitCount) {
        this.promotionLimitCount = promotionLimitCount;
    }

    private Integer activityLimitCount;

    /**
     * @return 活动限购数；该场内活动商品限购数，-1表示不限购；0表示不可购买该场活动所有商品；3个*LimitCount字段都等于-1时，表示没有任何限购
     */
    public Integer getActivityLimitCount() {
        return activityLimitCount;
    }

    /**
     * 设置活动限购数；该场内活动商品限购数，-1表示不限购；0表示不可购买该场活动所有商品；3个*LimitCount字段都等于-1时，表示没有任何限购     *
     * 参数示例：<pre>-1</pre>     
     * 此参数必填
     */
    public void setActivityLimitCount(Integer activityLimitCount) {
        this.activityLimitCount = activityLimitCount;
    }

    private Date freepostageStartTime;

    /**
     * @return 活动限时包邮开始时间；null 表示不限时
     */
    public Date getFreepostageStartTime() {
        return freepostageStartTime;
    }

    /**
     * 设置活动限时包邮开始时间；null 表示不限时     *
     * 参数示例：<pre>20190410000000000+0800</pre>     
     * 此参数必填
     */
    public void setFreepostageStartTime(Date freepostageStartTime) {
        this.freepostageStartTime = freepostageStartTime;
    }

    private Date freepostageEndTime;

    /**
     * @return 活动限时包邮结束时间；null 表示不限时
     */
    public Date getFreepostageEndTime() {
        return freepostageEndTime;
    }

    /**
     * 设置活动限时包邮结束时间；null 表示不限时     *
     * 参数示例：<pre>20190410000000000+0800</pre>     
     * 此参数必填
     */
    public void setFreepostageEndTime(Date freepostageEndTime) {
        this.freepostageEndTime = freepostageEndTime;
    }

    private ComAlibabaPpOpenClientDtoUnionActivityAreaModelDTO[] excludeAreaList;

    /**
     * @return 免包邮地区，与活动包邮配合使用
     */
    public ComAlibabaPpOpenClientDtoUnionActivityAreaModelDTO[] getExcludeAreaList() {
        return excludeAreaList;
    }

    /**
     * 设置免包邮地区，与活动包邮配合使用     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setExcludeAreaList(ComAlibabaPpOpenClientDtoUnionActivityAreaModelDTO[] excludeAreaList) {
        this.excludeAreaList = excludeAreaList;
    }

    private ComAlibabaPpOpenClientDtoUnionActivityRangePriceDTO rangePrice;

    /**
     * @return 如果offer是范围报价，且价格优惠是折扣的情况，返回折扣计算后的价格范围;优先取该字段，该字段为空时，表示分sku报价，取promotionItemList
     */
    public ComAlibabaPpOpenClientDtoUnionActivityRangePriceDTO getRangePrice() {
        return rangePrice;
    }

    /**
     * 设置如果offer是范围报价，且价格优惠是折扣的情况，返回折扣计算后的价格范围;优先取该字段，该字段为空时，表示分sku报价，取promotionItemList     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setRangePrice(ComAlibabaPpOpenClientDtoUnionActivityRangePriceDTO rangePrice) {
        this.rangePrice = rangePrice;
    }

    private ComAlibabaPpOpenClientDtoUnionActivityPromotionItemDTO[] promotionItemList;

    /**
     * @return 优惠结果，根据优惠方式（PromotionInfo），结合offer的原价信息，计算出优惠结果：每个sku或者每个区间价的促销价，折扣率
     */
    public ComAlibabaPpOpenClientDtoUnionActivityPromotionItemDTO[] getPromotionItemList() {
        return promotionItemList;
    }

    /**
     * 设置优惠结果，根据优惠方式（PromotionInfo），结合offer的原价信息，计算出优惠结果：每个sku或者每个区间价的促销价，折扣率     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setPromotionItemList(ComAlibabaPpOpenClientDtoUnionActivityPromotionItemDTO[] promotionItemList) {
        this.promotionItemList = promotionItemList;
    }

    private ComAlibabaPpOpenClientDtoUnionActivitySkuStockDTO[] skuStockList;

    /**
     * @return sku维度的库存结果
     */
    public ComAlibabaPpOpenClientDtoUnionActivitySkuStockDTO[] getSkuStockList() {
        return skuStockList;
    }

    /**
     * 设置sku维度的库存结果     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setSkuStockList(ComAlibabaPpOpenClientDtoUnionActivitySkuStockDTO[] skuStockList) {
        this.skuStockList = skuStockList;
    }

    private String introOrderFlow;

    /**
     * @return 这里平台会计算一个推荐使用的下单flow，可以用这个flow值调用下单接口
     */
    public String getIntroOrderFlow() {
        return introOrderFlow;
    }

    /**
     * 设置这里平台会计算一个推荐使用的下单flow，可以用这个flow值调用下单接口     *
     * 参数示例：<pre>general</pre>     
     * 此参数必填
     */
    public void setIntroOrderFlow(String introOrderFlow) {
        this.introOrderFlow = introOrderFlow;
    }

}
