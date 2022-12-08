package com.alibaba.p4p.param;

public class AlibabaCpsOpenOfferDTO {

    private String loginId;

    /**
     * @return login id
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * 设置login id     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    private String companyName;

    /**
     * @return 公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置公司名称     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    private Long offerId;

    /**
     * @return offer id
     */
    public Long getOfferId() {
        return offerId;
    }

    /**
     * 设置offer id     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    private Long sellerId;

    /**
     * @return 卖家ID
     */
    public Long getSellerId() {
        return sellerId;
    }

    /**
     * 设置卖家ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    private String title;

    /**
     * @return offer名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置offer名称     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTitle(String title) {
        this.title = title;
    }

    private String url;

    /**
     * @return offer链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置offer链接     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setUrl(String url) {
        this.url = url;
    }

    private String imgUrl;

    /**
     * @return offer主图
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置offer主图     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private Double ratio;

    /**
     * @return 佣金比例
     */
    public Double getRatio() {
        return ratio;
    }

    /**
     * 设置佣金比例     *
     * 参数示例：<pre>百分比</pre>     
     * 此参数必填
     */
    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    private Integer type;

    /**
     * @return 推广类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置推广类型     *
     * 参数示例：<pre>0:全店 1:主推</pre>     
     * 此参数必填
     */
    public void setType(Integer type) {
        this.type = type;
    }

    private Double price;

    /**
     * @return 商品价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置商品价格     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    private String unit;

    /**
     * @return 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置单位     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    private Integer saleQuantity;

    /**
     * @return 销量(月)
     */
    public Integer getSaleQuantity() {
        return saleQuantity;
    }

    /**
     * 设置销量(月)     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSaleQuantity(Integer saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    private String categoryId;

    /**
     * @return 类目树id
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 设置类目树id     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    private Integer tkCnt;

    /**
     * @return 月推广量
     */
    public Integer getTkCnt() {
        return tkCnt;
    }

    /**
     * 设置月推广量     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTkCnt(Integer tkCnt) {
        this.tkCnt = tkCnt;
    }

    private Double tkCommission;

    /**
     * @return 月支出佣金
     */
    public Double getTkCommission() {
        return tkCommission;
    }

    /**
     * 设置月支出佣金     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTkCommission(Double tkCommission) {
        this.tkCommission = tkCommission;
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
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setQuantityBegin(Integer quantityBegin) {
        this.quantityBegin = quantityBegin;
    }

    private Double scoreFh;

    /**
     * @return 发货速度
     */
    public Double getScoreFh() {
        return scoreFh;
    }

    /**
     * 设置发货速度     *
     * 参数示例：<pre>+代表比行业高，-代表比行业低</pre>     
     * 此参数必填
     */
    public void setScoreFh(Double scoreFh) {
        this.scoreFh = scoreFh;
    }

    private Double scoreHmRate;

    /**
     * @return 货物描述
     */
    public Double getScoreHmRate() {
        return scoreHmRate;
    }

    /**
     * 设置货物描述     *
     * 参数示例：<pre>+代表比行业高，-代表比行业低</pre>     
     * 此参数必填
     */
    public void setScoreHmRate(Double scoreHmRate) {
        this.scoreHmRate = scoreHmRate;
    }

    private Double scoreXyRate;

    /**
     * @return 响应速度
     */
    public Double getScoreXyRate() {
        return scoreXyRate;
    }

    /**
     * 设置响应速度     *
     * 参数示例：<pre>+代表比行业高，-代表比行业低</pre>     
     * 此参数必填
     */
    public void setScoreXyRate(Double scoreXyRate) {
        this.scoreXyRate = scoreXyRate;
    }

    private Boolean slsjFlag;

    /**
     * @return 实力商家标志
     */
    public Boolean getSlsjFlag() {
        return slsjFlag;
    }

    /**
     * 设置实力商家标志     *
     * 参数示例：<pre>true是；false否</pre>     
     * 此参数必填
     */
    public void setSlsjFlag(Boolean slsjFlag) {
        this.slsjFlag = slsjFlag;
    }

    private Boolean sdrzFlag;

    /**
     * @return 深度认证标志
     */
    public Boolean getSdrzFlag() {
        return sdrzFlag;
    }

    /**
     * 设置深度认证标志     *
     * 参数示例：<pre>true是；false否</pre>     
     * 此参数必填
     */
    public void setSdrzFlag(Boolean sdrzFlag) {
        this.sdrzFlag = sdrzFlag;
    }

    private Boolean jkhyFlag;

    /**
     * @return 进口货源标志
     */
    public Boolean getJkhyFlag() {
        return jkhyFlag;
    }

    /**
     * 设置进口货源标志     *
     * 参数示例：<pre>true是；false否</pre>     
     * 此参数必填
     */
    public void setJkhyFlag(Boolean jkhyFlag) {
        this.jkhyFlag = jkhyFlag;
    }

    private Boolean yjdfFlag;

    /**
     * @return 一件代发标志
     */
    public Boolean getYjdfFlag() {
        return yjdfFlag;
    }

    /**
     * 设置一件代发标志     *
     * 参数示例：<pre>true是；false否</pre>     
     * 此参数必填
     */
    public void setYjdfFlag(Boolean yjdfFlag) {
        this.yjdfFlag = yjdfFlag;
    }

    private Integer tpServiceYear;

    /**
     * @return 诚信通年限
     */
    public Integer getTpServiceYear() {
        return tpServiceYear;
    }

    /**
     * 设置诚信通年限     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTpServiceYear(Integer tpServiceYear) {
        this.tpServiceYear = tpServiceYear;
    }

    private Boolean yhqFlag;

    /**
     * @return 优惠券标志
     */
    public Boolean getYhqFlag() {
        return yhqFlag;
    }

    /**
     * 设置优惠券标志     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setYhqFlag(Boolean yhqFlag) {
        this.yhqFlag = yhqFlag;
    }

    private Double yhqDiscountFee;

    /**
     * @return 优惠券面额，单位为元
     */
    public Double getYhqDiscountFee() {
        return yhqDiscountFee;
    }

    /**
     * 设置优惠券面额，单位为元     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setYhqDiscountFee(Double yhqDiscountFee) {
        this.yhqDiscountFee = yhqDiscountFee;
    }

    private Integer yhqRemainingCount;

    /**
     * @return 优惠券余量
     */
    public Integer getYhqRemainingCount() {
        return yhqRemainingCount;
    }

    /**
     * 设置优惠券余量     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setYhqRemainingCount(Integer yhqRemainingCount) {
        this.yhqRemainingCount = yhqRemainingCount;
    }

    private String yhqExt;

    /**
     * @return 优惠券其他信息
     */
    public String getYhqExt() {
        return yhqExt;
    }

    /**
     * 设置优惠券其他信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setYhqExt(String yhqExt) {
        this.yhqExt = yhqExt;
    }

}
