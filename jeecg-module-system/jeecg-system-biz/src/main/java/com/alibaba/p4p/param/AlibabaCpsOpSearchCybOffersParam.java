package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsOpSearchCybOffersParam extends AbstractAPIRequest<AlibabaCpsOpSearchCybOffersResult> {

    public AlibabaCpsOpSearchCybOffersParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.op.searchCybOffers", 1);
    }

    private String biztype;

    /**
     * @return 枚举;经营模式;1:生产加工,2:经销批发,3:招商代理,4:商业服务
     */
    public String getBiztype() {
        return biztype;
    }

    /**
     * 设置枚举;经营模式;1:生产加工,2:经销批发,3:招商代理,4:商业服务     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setBiztype(String biztype) {
        this.biztype = biztype;
    }

    private String buyerProtection;

    /**
     * @return 枚举;买家保障,多个值用逗号分割;qtbh:7天包换;swtbh:15天包换
     */
    public String getBuyerProtection() {
        return buyerProtection;
    }

    /**
     * 设置枚举;买家保障,多个值用逗号分割;qtbh:7天包换;swtbh:15天包换     *
     * 参数示例：<pre>qtbh</pre>     
     * 此参数必填
     */
    public void setBuyerProtection(String buyerProtection) {
        this.buyerProtection = buyerProtection;
    }

    private String city;

    /**
     * @return 所在地区- 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置所在地区- 市     *
     * 参数示例：<pre>杭州</pre>     
     * 此参数必填
     */
    public void setCity(String city) {
        this.city = city;
    }

    private String deliveryTimeType;

    /**
     * @return 枚举;发货时间;1:24小时发货;2:48小时发货;3:72小时发货
     */
    public String getDeliveryTimeType() {
        return deliveryTimeType;
    }

    /**
     * 设置枚举;发货时间;1:24小时发货;2:48小时发货;3:72小时发货     *
     * 参数示例：<pre>3</pre>     
     * 此参数必填
     */
    public void setDeliveryTimeType(String deliveryTimeType) {
        this.deliveryTimeType = deliveryTimeType;
    }

    private Boolean descendOrder;

    /**
     * @return 是否倒序;正序: false;倒序:true
     */
    public Boolean getDescendOrder() {
        return descendOrder;
    }

    /**
     * 设置是否倒序;正序: false;倒序:true     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setDescendOrder(Boolean descendOrder) {
        this.descendOrder = descendOrder;
    }

    private String holidayTagId;

    /**
     * @return 商品售卖类型筛选;枚举,多个值用分号分割;免费赊账:50000114
     */
    public String getHolidayTagId() {
        return holidayTagId;
    }

    /**
     * 设置商品售卖类型筛选;枚举,多个值用分号分割;免费赊账:50000114     *
     * 参数示例：<pre>50000114</pre>     
     * 此参数必填
     */
    public void setHolidayTagId(String holidayTagId) {
        this.holidayTagId = holidayTagId;
    }

    private String keyWords;

    /**
     * @return 搜索关键词
     */
    public String getKeyWords() {
        return keyWords;
    }

    /**
     * 设置搜索关键词     *
     * 参数示例：<pre>女装</pre>     
     * 此参数必填
     */
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    private Integer page;

    /**
     * @return 页码
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 设置页码     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    private String pageSize;

    /**
     * @return 页面数量;最大20
     */
    public String getPageSize() {
        return pageSize;
    }

    /**
     * 设置页面数量;最大20     *
     * 参数示例：<pre>10</pre>     
     * 此参数必填
     */
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    private Long postCategoryId;

    /**
     * @return 类目id;4 纺织、皮革 5 电工电气 10 能源 12 交通运输 16 医药、保养 17 工艺品、礼品 57 电子元器件 58 照明工业 64 环保 66 医药、保养 67 办公、文教 69 商务服务 96 家纺家饰 311 童装 312 内衣 1813 玩具 2805 加工 2829 二手设备转让 10165 男装 1038378 鞋 1042954 箱包皮具 127380009 运动服饰 130822002 餐饮生鲜 130823000 性保健品 200514001 床上用品 201128501 直播 1 农业 2 食品酒水 7 数码、电脑 9 冶金矿产 15 日用百货 18 运动装备 33 汽摩及配件 53 传媒、广电 54 服饰配件、饰品 59 五金、工具 68 包装 70 安全、防护 96 家居饰品 97 美妆日化 97 美容护肤/彩妆 1501 母婴用品 10166 女装 10208 仪器仪表 122916001 宠物及园艺 123614001 钢铁 130822220 个护/家清 6 家用电器 8 化工 13 家装、建材 21 办公、文教 55 橡塑 65 机械及行业设备 71 汽摩及配件 72 印刷 73 项目合作 509 通信产品 1426 机床 1043472 毛巾、巾类 122916002 汽车用品	
     */
    public Long getPostCategoryId() {
        return postCategoryId;
    }

    /**
     * 设置类目id;4 纺织、皮革 5 电工电气 10 能源 12 交通运输 16 医药、保养 17 工艺品、礼品 57 电子元器件 58 照明工业 64 环保 66 医药、保养 67 办公、文教 69 商务服务 96 家纺家饰 311 童装 312 内衣 1813 玩具 2805 加工 2829 二手设备转让 10165 男装 1038378 鞋 1042954 箱包皮具 127380009 运动服饰 130822002 餐饮生鲜 130823000 性保健品 200514001 床上用品 201128501 直播 1 农业 2 食品酒水 7 数码、电脑 9 冶金矿产 15 日用百货 18 运动装备 33 汽摩及配件 53 传媒、广电 54 服饰配件、饰品 59 五金、工具 68 包装 70 安全、防护 96 家居饰品 97 美妆日化 97 美容护肤/彩妆 1501 母婴用品 10166 女装 10208 仪器仪表 122916001 宠物及园艺 123614001 钢铁 130822220 个护/家清 6 家用电器 8 化工 13 家装、建材 21 办公、文教 55 橡塑 65 机械及行业设备 71 汽摩及配件 72 印刷 73 项目合作 509 通信产品 1426 机床 1043472 毛巾、巾类 122916002 汽车用品	     *
     * 参数示例：<pre>122916002</pre>     
     * 此参数必填
     */
    public void setPostCategoryId(Long postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    private Double priceStart;

    /**
     * @return 最低价
     */
    public Double getPriceStart() {
        return priceStart;
    }

    /**
     * 设置最低价     *
     * 参数示例：<pre>10.2</pre>     
     * 此参数必填
     */
    public void setPriceStart(Double priceStart) {
        this.priceStart = priceStart;
    }

    private Double priceEnd;

    /**
     * @return 最高价
     */
    public Double getPriceEnd() {
        return priceEnd;
    }

    /**
     * 设置最高价     *
     * 参数示例：<pre>11.2</pre>     
     * 此参数必填
     */
    public void setPriceEnd(Double priceEnd) {
        this.priceEnd = priceEnd;
    }

    private String priceFilterFields;

    /**
     * @return 价格类型;默认分销价;agent_price:分销价;
     */
    public String getPriceFilterFields() {
        return priceFilterFields;
    }

    /**
     * 设置价格类型;默认分销价;agent_price:分销价;     *
     * 参数示例：<pre>agent_price</pre>     
     * 此参数必填
     */
    public void setPriceFilterFields(String priceFilterFields) {
        this.priceFilterFields = priceFilterFields;
    }

    private String province;

    /**
     * @return 所在地区- 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置所在地区- 省     *
     * 参数示例：<pre>浙江</pre>     
     * 此参数必填
     */
    public void setProvince(String province) {
        this.province = province;
    }

    private String sortType;

    /**
     * @return 枚举;排序字段;normal:综合;
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * 设置枚举;排序字段;normal:综合;     *
     * 参数示例：<pre>saleQuantity</pre>     
     * 此参数必填
     */
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    private String tags;

    /**
     * @return 历史遗留，可不用
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置历史遗留，可不用     *
     * 参数示例：<pre>266818</pre>     
     * 此参数必填
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    private String offerTags;

    /**
     * @return 枚举;1387842:渠道专享价商品
     */
    public String getOfferTags() {
        return offerTags;
    }

    /**
     * 设置枚举;1387842:渠道专享价商品     *
     * 参数示例：<pre>1387842</pre>     
     * 此参数必填
     */
    public void setOfferTags(String offerTags) {
        this.offerTags = offerTags;
    }

    private String offerIds;

    /**
     * @return 商品id搜索，多个id用逗号分割
     */
    public String getOfferIds() {
        return offerIds;
    }

    /**
     * 设置商品id搜索，多个id用逗号分割     *
     * 参数示例：<pre>600335270178</pre>     
     * 此参数必填
     */
    public void setOfferIds(String offerIds) {
        this.offerIds = offerIds;
    }

}
