package com.alibaba.product.param;

import java.util.Date;

public class AlibabaProductProductInfo {

    private Long productID;

    /**
     * @return 商品ID
     */
    public Long getProductID() {
        return productID;
    }

    /**
     * 设置商品ID     *
     * 参数示例：<pre>584051070147</pre>     
     * 此参数必填
     */
    public void setProductID(Long productID) {
        this.productID = productID;
    }

    private String productType;

    /**
     * @return 商品类型，在线批发商品(wholesale)或者询盘商品(sourcing)，1688网站缺省为wholesale
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 设置商品类型，在线批发商品(wholesale)或者询盘商品(sourcing)，1688网站缺省为wholesale     *
     * 参数示例：<pre>wholesale</pre>     
     * 此参数必填
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    private Long categoryID;

    /**
     * @return 类目ID，标识商品所属类目
     */
    public Long getCategoryID() {
        return categoryID;
    }

    /**
     * 设置类目ID，标识商品所属类目     *
     * 参数示例：<pre>1048182</pre>     
     * 此参数必填
     */
    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    private AlibabaProductProductAttribute[] attributes;

    /**
     * @return 商品属性和属性值
     */
    public AlibabaProductProductAttribute[] getAttributes() {
        return attributes;
    }

    /**
     * 设置商品属性和属性值     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setAttributes(AlibabaProductProductAttribute[] attributes) {
        this.attributes = attributes;
    }

    private long[] groupID;

    /**
     * @return 分组ID，确定商品所属分组。1688可传入多个分组ID，国际站同一个商品只能属于一个分组，因此默认只取第一个
     */
    public long[] getGroupID() {
        return groupID;
    }

    /**
     * 设置分组ID，确定商品所属分组。1688可传入多个分组ID，国际站同一个商品只能属于一个分组，因此默认只取第一个     *
     * 参数示例：<pre>[107331682]</pre>     
     * 此参数必填
     */
    public void setGroupID(long[] groupID) {
        this.groupID = groupID;
    }

    private String status;

    /**
     * @return 商品状态。published:上网状态;member expired:会员撤销;auto expired:自然过期;expired:过期(包含手动过期与自动过期);member deleted:会员删除;modified:修改;new:新发;deleted:删除;TBD:to be delete;approved:审批通过;auditing:审核中;untread:审核不通过;
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置商品状态。published:上网状态;member expired:会员撤销;auto expired:自然过期;expired:过期(包含手动过期与自动过期);member deleted:会员删除;modified:修改;new:新发;deleted:删除;TBD:to be delete;approved:审批通过;auditing:审核中;untread:审核不通过;     *
     * 参数示例：<pre>published</pre>     
     * 此参数必填
     */
    public void setStatus(String status) {
        this.status = status;
    }

    private String subject;

    /**
     * @return 商品标题，最多128个字符
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置商品标题，最多128个字符     *
     * 参数示例：<pre>高端气质OL韩版雪纺女装套头半高领长袖修身型蕾丝衫</pre>     
     * 此参数必填
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String description;

    /**
     * @return 商品详情描述，可包含图片中心的图片URL
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置商品详情描述，可包含图片中心的图片URL     *
     * 参数示例：<pre>高端气质OL韩版雪纺女装套头半高领长袖修身型蕾丝衫</pre>     
     * 此参数必填
     */
    public void setDescription(String description) {
        this.description = description;
    }

    private String language;

    /**
     * @return 语种，参见FAQ 语种枚举值，1688网站默认传入CHINESE
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置语种，参见FAQ 语种枚举值，1688网站默认传入CHINESE     *
     * 参数示例：<pre>ENGLISH</pre>     
     * 此参数必填
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    private Integer periodOfValidity;

    /**
     * @return 信息有效期，按天计算，国际站无此信息
     */
    public Integer getPeriodOfValidity() {
        return periodOfValidity;
    }

    /**
     * 设置信息有效期，按天计算，国际站无此信息     *
     * 参数示例：<pre>3650</pre>     
     * 此参数必填
     */
    public void setPeriodOfValidity(Integer periodOfValidity) {
        this.periodOfValidity = periodOfValidity;
    }

    private Integer bizType;

    /**
     * @return 业务类型。1：商品，2：加工，3：代理，4：合作，5：商务服务。国际站按默认商品。
     */
    public Integer getBizType() {
        return bizType;
    }

    /**
     * 设置业务类型。1：商品，2：加工，3：代理，4：合作，5：商务服务。国际站按默认商品。     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    private Boolean pictureAuth;

    /**
     * @return 是否图片私密信息，国际站此字段无效
     */
    public Boolean getPictureAuth() {
        return pictureAuth;
    }

    /**
     * 设置是否图片私密信息，国际站此字段无效     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setPictureAuth(Boolean pictureAuth) {
        this.pictureAuth = pictureAuth;
    }

    private AlibabaProductProductImageInfo image;

    /**
     * @return 商品主图
     */
    public AlibabaProductProductImageInfo getImage() {
        return image;
    }

    /**
     * 设置商品主图     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setImage(AlibabaProductProductImageInfo image) {
        this.image = image;
    }

    private AlibabaProductProductSKUInfo[] skuInfos;

    /**
     * @return sku信息
     */
    public AlibabaProductProductSKUInfo[] getSkuInfos() {
        return skuInfos;
    }

    /**
     * 设置sku信息     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setSkuInfos(AlibabaProductProductSKUInfo[] skuInfos) {
        this.skuInfos = skuInfos;
    }

    private AlibabaProductProductSaleInfo saleInfo;

    /**
     * @return 商品销售信息
     */
    public AlibabaProductProductSaleInfo getSaleInfo() {
        return saleInfo;
    }

    /**
     * 设置商品销售信息     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setSaleInfo(AlibabaProductProductSaleInfo saleInfo) {
        this.saleInfo = saleInfo;
    }

    private AlibabaProductProductShippingInfo shippingInfo;

    /**
     * @return 商品物流信息
     */
    public AlibabaProductProductShippingInfo getShippingInfo() {
        return shippingInfo;
    }

    /**
     * 设置商品物流信息     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setShippingInfo(AlibabaProductProductShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    private AlibabaProductProductExtendInfo[] extendInfos;

    /**
     * @return 商品扩展信息
     */
    public AlibabaProductProductExtendInfo[] getExtendInfos() {
        return extendInfos;
    }

    /**
     * 设置商品扩展信息     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setExtendInfos(AlibabaProductProductExtendInfo[] extendInfos) {
        this.extendInfos = extendInfos;
    }

    private String supplierUserId;

    /**
     * @return 供应商用户ID
     */
    public String getSupplierUserId() {
        return supplierUserId;
    }

    /**
     * 设置供应商用户ID     *
     * 参数示例：<pre>1234</pre>     
     * 此参数必填
     */
    public void setSupplierUserId(String supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    private Integer qualityLevel;

    /**
     * @return 质量星级(0-5)
     */
    public Integer getQualityLevel() {
        return qualityLevel;
    }

    /**
     * 设置质量星级(0-5)     *
     * 参数示例：<pre>5</pre>     
     * 此参数必填
     */
    public void setQualityLevel(Integer qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    private String supplierLoginId;

    /**
     * @return 供应商loginId
     */
    public String getSupplierLoginId() {
        return supplierLoginId;
    }

    /**
     * 设置供应商loginId     *
     * 参数示例：<pre>alitestforisv01</pre>     
     * 此参数必填
     */
    public void setSupplierLoginId(String supplierLoginId) {
        this.supplierLoginId = supplierLoginId;
    }

    private String categoryName;

    /**
     * @return 类目名
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置类目名     *
     * 参数示例：<pre>连衣裙</pre>     
     * 此参数必填
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private String mainVedio;

    /**
     * @return 主图视频播放地址
     */
    public String getMainVedio() {
        return mainVedio;
    }

    /**
     * 设置主图视频播放地址     *
     * 参数示例：<pre>https://cloud.video.taobao.com/play/u/1685/p/1/e/6/t/1/5224**.mp4</pre>     
     * 此参数必填
     */
    public void setMainVedio(String mainVedio) {
        this.mainVedio = mainVedio;
    }

    private String productCargoNumber;

    /**
     * @return 商品货号，产品属性中的货号
     */
    public String getProductCargoNumber() {
        return productCargoNumber;
    }

    /**
     * 设置商品货号，产品属性中的货号     *
     * 参数示例：<pre>666</pre>     
     * 此参数必填
     */
    public void setProductCargoNumber(String productCargoNumber) {
        this.productCargoNumber = productCargoNumber;
    }

    private Boolean crossBorderOffer;

    /**
     * @return 是否海外代发
     */
    public Boolean getCrossBorderOffer() {
        return crossBorderOffer;
    }

    /**
     * 设置是否海外代发     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setCrossBorderOffer(Boolean crossBorderOffer) {
        this.crossBorderOffer = crossBorderOffer;
    }

    private String referencePrice;

    /**
     * @return 参考价格，返回价格区间，可能为空
     */
    public String getReferencePrice() {
        return referencePrice;
    }

    /**
     * 设置参考价格，返回价格区间，可能为空     *
     * 参数示例：<pre>500</pre>     
     * 此参数必填
     */
    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    private Date createTime;

    /**
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间     *
     * 参数示例：<pre>20181213201638000+0800</pre>     
     * 此参数必填
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Date lastUpdateTime;

    /**
     * @return 最后操作时间
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * 设置最后操作时间     *
     * 参数示例：<pre>20181219175505000+0800</pre>     
     * 此参数必填
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    private Date expireTime;

    /**
     * @return 过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 设置过期时间     *
     * 参数示例：<pre>20281216175505000+0800</pre>     
     * 此参数必填
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    private Date modifyTime;

    /**
     * @return 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间     *
     * 参数示例：<pre>20281216175505000+0800</pre>     
     * 此参数必填
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    private Date approvedTime;

    /**
     * @return 审核时间
     */
    public Date getApprovedTime() {
        return approvedTime;
    }

    /**
     * 设置审核时间     *
     * 参数示例：<pre>20181219175505000+0800</pre>     
     * 此参数必填
     */
    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    private Date lastRepostTime;

    /**
     * @return 最后重发时间
     */
    public Date getLastRepostTime() {
        return lastRepostTime;
    }

    /**
     * 设置最后重发时间     *
     * 参数示例：<pre>20181217090842000+0800</pre>     
     * 此参数必填
     */
    public void setLastRepostTime(Date lastRepostTime) {
        this.lastRepostTime = lastRepostTime;
    }

    private String bookedCount;

    /**
     * @return 成交量
     */
    public String getBookedCount() {
        return bookedCount;
    }

    /**
     * 设置成交量     *
     * 参数示例：<pre>1999</pre>     
     * 此参数必填
     */
    public void setBookedCount(String bookedCount) {
        this.bookedCount = bookedCount;
    }

    private String productLine;

    /**
     * @return 产品线
     */
    public String getProductLine() {
        return productLine;
    }

    /**
     * 设置产品线     *
     * 参数示例：<pre>默认</pre>     
     * 此参数必填
     */
    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    private String detailVedio;

    /**
     * @return 详情视频
     */
    public String getDetailVedio() {
        return detailVedio;
    }

    /**
     * 设置详情视频     *
     * 参数示例：<pre>https://cloud.video.taobao.com/play/u/1685/p/1/e/6/t/1/5224**.mp4</pre>     
     * 此参数必填
     */
    public void setDetailVedio(String detailVedio) {
        this.detailVedio = detailVedio;
    }

    private AlibabaProductProductInternationalTradeInfo internationalTradeInfo;

    /**
     * @return 商品国际贸易信息，1688无需处理此字段
     */
    public AlibabaProductProductInternationalTradeInfo getInternationalTradeInfo() {
        return internationalTradeInfo;
    }

    /**
     * 设置商品国际贸易信息，1688无需处理此字段     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setInternationalTradeInfo(AlibabaProductProductInternationalTradeInfo internationalTradeInfo) {
        this.internationalTradeInfo = internationalTradeInfo;
    }

    private AlibabaProductProductBizGroupInfo[] bizGroupInfos;

    /**
     * @return 产品业务的支持信息,support为false说明不支持.
     */
    public AlibabaProductProductBizGroupInfo[] getBizGroupInfos() {
        return bizGroupInfos;
    }

    /**
     * 设置产品业务的支持信息,support为false说明不支持.     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setBizGroupInfos(AlibabaProductProductBizGroupInfo[] bizGroupInfos) {
        this.bizGroupInfos = bizGroupInfos;
    }

    private String sellerLoginId;

    /**
     * @return 卖家旺旺ID
     */
    public String getSellerLoginId() {
        return sellerLoginId;
    }

    /**
     * 设置卖家旺旺ID     *
     * 参数示例：<pre>卖家旺旺ID</pre>     
     * 此参数必填
     */
    public void setSellerLoginId(String sellerLoginId) {
        this.sellerLoginId = sellerLoginId;
    }

    private ComAlibabaOceanOpenplatformBizProductCommonModelProductIntelligentInfo intelligentInfo;

    /**
     * @return 商品算法智能改写信息，包含算法优化后的商品标题和图片信息，未改写的则直接返回原标题和原图片
     */
    public ComAlibabaOceanOpenplatformBizProductCommonModelProductIntelligentInfo getIntelligentInfo() {
        return intelligentInfo;
    }

    /**
     * 设置商品算法智能改写信息，包含算法优化后的商品标题和图片信息，未改写的则直接返回原标题和原图片     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setIntelligentInfo(ComAlibabaOceanOpenplatformBizProductCommonModelProductIntelligentInfo intelligentInfo) {
        this.intelligentInfo = intelligentInfo;
    }

}
