package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsListOverPricedOfferParam extends AbstractAPIRequest<AlibabaCpsListOverPricedOfferResult> {

    public AlibabaCpsListOverPricedOfferParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.listOverPricedOffer", 1);
    }

    private String categoryIds;

    /**
     * @return 发布类目id
     */
    public String getCategoryIds() {
        return categoryIds;
    }

    /**
     * 设置发布类目id     *
     * 参数示例：<pre>4  纺织、皮革 5  电工电气 10  能源 12  交通运输 16  医药、保养 17  工艺品、礼品 57  电子元器件 58  照明工业 64  环保 66  医药、保养 67  办公、文教 69  商务服务 96  家纺家饰 311  童装 312  内衣 1813  玩具 2805  加工 2829  二手设备转让 10165  男装 1038378  鞋 1042954  箱包皮具 127380009  运动服饰 130822002  餐饮生鲜 130823000  性保健品 200514001  床上用品 201128501  直播 1  农业 2  食品酒水 7  数码、电脑 9  冶金矿产 15  日用百货 18  运动装备 33  汽摩及配件 53  传媒、广电 54  服饰配件、饰品 59  五金、工具 68  包装 70  安全、防护 96  家居饰品 97  美妆日化 97  美容护肤/彩妆 1501  母婴用品 10166  女装 10208  仪器仪表 122916001  宠物及园艺 123614001  钢铁 130822220  个护/家清 6  家用电器 8  化工 13  家装、建材 21  办公、文教 55  橡塑 65  机械及行业设备 71  汽摩及配件 72  印刷 73  项目合作 509  通信产品 1426  机床 1043472  毛巾、巾类 122916002  汽车用品</pre>     
     * 此参数必填
     */
    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    private String keywords;

    /**
     * @return 关键字
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * 设置关键字     *
     * 参数示例：<pre>测试</pre>     
     * 此参数必填
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    private String offerIds;

    /**
     * @return 指定offerIds搜索，支持批量，逗号分割
     */
    public String getOfferIds() {
        return offerIds;
    }

    /**
     * 设置指定offerIds搜索，支持批量，逗号分割     *
     * 参数示例：<pre>123,456</pre>     
     * 此参数必填
     */
    public void setOfferIds(String offerIds) {
        this.offerIds = offerIds;
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
     * 参数示例：<pre>1.1</pre>     
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
     * 参数示例：<pre>3.3</pre>     
     * 此参数必填
     */
    public void setPriceEnd(Double priceEnd) {
        this.priceEnd = priceEnd;
    }

    private Integer quantityBegin;

    /**
     * @return 起批量
     */
    public Integer getQuantityBegin() {
        return quantityBegin;
    }

    /**
     * 设置起批量     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setQuantityBegin(Integer quantityBegin) {
        this.quantityBegin = quantityBegin;
    }

    private Boolean delivery48Hour;

    /**
     * @return 48小时发货
     */
    public Boolean getDelivery48Hour() {
        return delivery48Hour;
    }

    /**
     * 设置48小时发货     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setDelivery48Hour(Boolean delivery48Hour) {
        this.delivery48Hour = delivery48Hour;
    }

    private Boolean freightInsurance;

    /**
     * @return 赠运险费
     */
    public Boolean getFreightInsurance() {
        return freightInsurance;
    }

    /**
     * 设置赠运险费     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setFreightInsurance(Boolean freightInsurance) {
        this.freightInsurance = freightInsurance;
    }

    private Boolean returnGoods7Day;

    /**
     * @return 7天退换
     */
    public Boolean getReturnGoods7Day() {
        return returnGoods7Day;
    }

    /**
     * 设置7天退换     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setReturnGoods7Day(Boolean returnGoods7Day) {
        this.returnGoods7Day = returnGoods7Day;
    }

    private Boolean powerfulMerchant;

    /**
     * @return 实力商家
     */
    public Boolean getPowerfulMerchant() {
        return powerfulMerchant;
    }

    /**
     * 设置实力商家     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setPowerfulMerchant(Boolean powerfulMerchant) {
        this.powerfulMerchant = powerfulMerchant;
    }

    private Boolean brandSite;

    /**
     * @return 品牌站货源
     */
    public Boolean getBrandSite() {
        return brandSite;
    }

    /**
     * 设置品牌站货源     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setBrandSite(Boolean brandSite) {
        this.brandSite = brandSite;
    }

    private String province;

    /**
     * @return 省、区域、直辖市
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省、区域、直辖市     *
     * 参数示例：<pre>华东区</pre>     
     * 此参数必填
     */
    public void setProvince(String province) {
        this.province = province;
    }

    private String city;

    /**
     * @return 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市     *
     * 参数示例：<pre>杭州</pre>     
     * 此参数必填
     */
    public void setCity(String city) {
        this.city = city;
    }

    private Integer biztype;

    /**
     * @return 经营模式;1:生产加工,2:经销批发,3:招商代理,4:商业服务
     */
    public Integer getBiztype() {
        return biztype;
    }

    /**
     * 设置经营模式;1:生产加工,2:经销批发,3:招商代理,4:商业服务     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setBiztype(Integer biztype) {
        this.biztype = biztype;
    }

    private Boolean importOffer;

    /**
     * @return 进口货源
     */
    public Boolean getImportOffer() {
        return importOffer;
    }

    /**
     * 设置进口货源     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setImportOffer(Boolean importOffer) {
        this.importOffer = importOffer;
    }

    private Integer deliverySpeed;

    /**
     * @return 平均发货速度,1:当日;2:次日;3:3日内
     */
    public Integer getDeliverySpeed() {
        return deliverySpeed;
    }

    /**
     * 设置平均发货速度,1:当日;2:次日;3:3日内     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setDeliverySpeed(Integer deliverySpeed) {
        this.deliverySpeed = deliverySpeed;
    }

    private Integer sortType;

    /**
     * @return 排序类型;1:按价格;2:按成交
     */
    public Integer getSortType() {
        return sortType;
    }

    /**
     * 设置排序类型;1:按价格;2:按成交     *
     * 参数示例：<pre>2</pre>     
     * 此参数必填
     */
    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    private Boolean descendOrder;

    /**
     * @return 排序类型
     */
    public Boolean getDescendOrder() {
        return descendOrder;
    }

    /**
     * 设置排序类型     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setDescendOrder(Boolean descendOrder) {
        this.descendOrder = descendOrder;
    }

    private Integer pageNo;

    /**
     * @return 页数
     */
    public Integer getPageNo() {
        return pageNo;
    }

    /**
     * 设置页数     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    private Integer pageSize;

    /**
     * @return 页面条数，最大200
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置页面条数，最大200     *
     * 参数示例：<pre>20</pre>     
     * 此参数必填
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    private String offerTags;

    /**
     * @return 按商品标属性搜索，逗号分割各属性，如伙拼：isTuanPiOffer; 分销：isTbDistributionOffer；微供：isCybMicroSupply；CPS分销小二招商品: cbuTagOverPricedSpecialMarket; 商品力算法推荐商品: cbuTagOverPricedProductPowerIntro;采源宝-严选厂货:cbuTagOverPricedCybYxch;
     */
    public String getOfferTags() {
        return offerTags;
    }

    /**
     * 设置按商品标属性搜索，逗号分割各属性，如伙拼：isTuanPiOffer; 分销：isTbDistributionOffer；微供：isCybMicroSupply；CPS分销小二招商品: cbuTagOverPricedSpecialMarket; 商品力算法推荐商品: cbuTagOverPricedProductPowerIntro;采源宝-严选厂货:cbuTagOverPricedCybYxch;     *
     * 参数示例：<pre>isTuanPiOffer</pre>     
     * 此参数必填
     */
    public void setOfferTags(String offerTags) {
        this.offerTags = offerTags;
    }

}
