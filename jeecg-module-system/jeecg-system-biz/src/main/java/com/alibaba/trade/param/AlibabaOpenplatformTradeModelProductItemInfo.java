package com.alibaba.trade.param;

import java.math.BigDecimal;
import java.util.Date;

public class AlibabaOpenplatformTradeModelProductItemInfo {

    private String cargoNumber;

    /**
     * @return 指定单品货号，国际站无需关注。该字段不一定有值，仅仅在下单时才会把货号记录(如果卖家设置了单品货号的话)。别的订单类型的货号只能通过商品接口去获取。请注意：通过商品接口获取时的货号和下单时的货号可能不一致，因为下单完成后卖家可能修改商品信息，改变了货号。
     */
    public String getCargoNumber() {
        return cargoNumber;
    }

    /**
     * 设置指定单品货号，国际站无需关注。该字段不一定有值，仅仅在下单时才会把货号记录(如果卖家设置了单品货号的话)。别的订单类型的货号只能通过商品接口去获取。请注意：通过商品接口获取时的货号和下单时的货号可能不一致，因为下单完成后卖家可能修改商品信息，改变了货号。     *
     * 参数示例：<pre>指定单品货号，国际站无需关注。该字段不一定有值，仅仅在下单时才会把货号记录(如果卖家设置了单品货号的话)。别的订单类型的货号只能通过商品接口去获取。请注意：通过商品接口获取时的货号和下单时的货号可能不一致，因为下单完成后卖家可能修改商品信息，改变了货号。</pre>     
     * 此参数必填
     */
    public void setCargoNumber(String cargoNumber) {
        this.cargoNumber = cargoNumber;
    }

    private String description;

    /**
     * @return 描述,1688无此信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述,1688无此信息     *
     * 参数示例：<pre>描述,1688无此信息</pre>     
     * 此参数必填
     */
    public void setDescription(String description) {
        this.description = description;
    }

    private BigDecimal itemAmount;

    /**
     * @return 实付金额，单位为元
     */
    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    /**
     * 设置实付金额，单位为元     *
     * 参数示例：<pre>实付金额，单位为元</pre>     
     * 此参数必填
     */
    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    private String name;

    /**
     * @return 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称     *
     * 参数示例：<pre>商品名称</pre>     
     * 此参数必填
     */
    public void setName(String name) {
        this.name = name;
    }

    private BigDecimal price;

    /**
     * @return 原始单价，以元为单位
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置原始单价，以元为单位     *
     * 参数示例：<pre>原始单价，以元为单位</pre>     
     * 此参数必填
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private Long productID;

    /**
     * @return 产品ID（非在线产品为空）
     */
    public Long getProductID() {
        return productID;
    }

    /**
     * 设置产品ID（非在线产品为空）     *
     * 参数示例：<pre>产品ID（非在线产品为空）</pre>     
     * 此参数必填
     */
    public void setProductID(Long productID) {
        this.productID = productID;
    }

    private String[] productImgUrl;

    /**
     * @return 商品图片url
     */
    public String[] getProductImgUrl() {
        return productImgUrl;
    }

    /**
     * 设置商品图片url     *
     * 参数示例：<pre>商品图片url</pre>     
     * 此参数必填
     */
    public void setProductImgUrl(String[] productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    private String productSnapshotUrl;

    /**
     * @return 产品快照url，交易订单产生时会自动记录下当时的商品快照，供后续纠纷时参考
     */
    public String getProductSnapshotUrl() {
        return productSnapshotUrl;
    }

    /**
     * 设置产品快照url，交易订单产生时会自动记录下当时的商品快照，供后续纠纷时参考     *
     * 参数示例：<pre>产品快照url，交易订单产生时会自动记录下当时的商品快照，供后续纠纷时参考</pre>     
     * 此参数必填
     */
    public void setProductSnapshotUrl(String productSnapshotUrl) {
        this.productSnapshotUrl = productSnapshotUrl;
    }

    private BigDecimal quantity;

    /**
     * @return 以unit为单位的数量，例如多少个、多少件、多少箱、多少吨
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * 设置以unit为单位的数量，例如多少个、多少件、多少箱、多少吨     *
     * 参数示例：<pre>以unit为单位的数量，例如多少个、多少件、多少箱、多少吨</pre>     
     * 此参数必填
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    private BigDecimal refund;

    /**
     * @return 退款金额，单位为元
     */
    public BigDecimal getRefund() {
        return refund;
    }

    /**
     * 设置退款金额，单位为元     *
     * 参数示例：<pre>退款金额，单位为元</pre>     
     * 此参数必填
     */
    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    private Long skuID;

    /**
     * @return skuID
     */
    public Long getSkuID() {
        return skuID;
    }

    /**
     * 设置skuID     *
     * 参数示例：<pre>skuID</pre>     
     * 此参数必填
     */
    public void setSkuID(Long skuID) {
        this.skuID = skuID;
    }

    private Integer sort;

    /**
     * @return 排序字段，商品列表按此字段进行排序，从0开始，1688不提供
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序字段，商品列表按此字段进行排序，从0开始，1688不提供     *
     * 参数示例：<pre>排序字段，商品列表按此字段进行排序，从0开始，1688不提供</pre>     
     * 此参数必填
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    private String status;

    /**
     * @return 子订单状态，详细见订单状态说明栏
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置子订单状态，详细见订单状态说明栏     *
     * 参数示例：<pre>waitsellersend</pre>     
     * 此参数必填
     */
    public void setStatus(String status) {
        this.status = status;
    }

    private Long subItemID;

    /**
     * @return 子订单号，或商品明细条目ID
     */
    public Long getSubItemID() {
        return subItemID;
    }

    /**
     * 设置子订单号，或商品明细条目ID     *
     * 参数示例：<pre>子订单号，或商品明细条目ID</pre>     
     * 此参数必填
     */
    public void setSubItemID(Long subItemID) {
        this.subItemID = subItemID;
    }

    private String type;

    /**
     * @return 类型，国际站使用，供卖家标注商品所属类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型，国际站使用，供卖家标注商品所属类型     *
     * 参数示例：<pre>类型，国际站使用，供卖家标注商品所属类型</pre>     
     * 此参数必填
     */
    public void setType(String type) {
        this.type = type;
    }

    private String unit;

    /**
     * @return 售卖单位	例如：个、件、箱、吨
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置售卖单位	例如：个、件、箱、吨     *
     * 参数示例：<pre>售卖单位	例如：个、件、箱、吨</pre>     
     * 此参数必填
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    private String weight;

    /**
     * @return 重量	按重量单位计算的重量，例如：100
     */
    public String getWeight() {
        return weight;
    }

    /**
     * 设置重量	按重量单位计算的重量，例如：100     *
     * 参数示例：<pre>重量	按重量单位计算的重量，例如：100</pre>     
     * 此参数必填
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    private String weightUnit;

    /**
     * @return 重量单位	例如：g，kg，t
     */
    public String getWeightUnit() {
        return weightUnit;
    }

    /**
     * 设置重量单位	例如：g，kg，t     *
     * 参数示例：<pre>重量单位	例如：g，kg，t</pre>     
     * 此参数必填
     */
    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    private AlibabaOpenplatformTradeModelGuaranteeTermsInfo[] guaranteesTerms;

    /**
     * @return 保障条款，此字段仅针对1688
     */
    public AlibabaOpenplatformTradeModelGuaranteeTermsInfo[] getGuaranteesTerms() {
        return guaranteesTerms;
    }

    /**
     * 设置保障条款，此字段仅针对1688     *
     * 参数示例：<pre>保障条款，此字段仅针对1688</pre>     
     * 此参数必填
     */
    public void setGuaranteesTerms(AlibabaOpenplatformTradeModelGuaranteeTermsInfo[] guaranteesTerms) {
        this.guaranteesTerms = guaranteesTerms;
    }

    private String productCargoNumber;

    /**
     * @return 指定商品货号，该字段不一定有值，在下单时才会把货号记录。别的订单类型的货号只能通过商品接口去获取。请注意：通过商品接口获取时的货号和下单时的货号可能不一致，因为下单完成后卖家可能修改商品信息，改变了货号。该字段和cargoNUmber的区别是：该字段是定义在商品级别上的货号，cargoNUmber是定义在单品级别的货号
     */
    public String getProductCargoNumber() {
        return productCargoNumber;
    }

    /**
     * 设置指定商品货号，该字段不一定有值，在下单时才会把货号记录。别的订单类型的货号只能通过商品接口去获取。请注意：通过商品接口获取时的货号和下单时的货号可能不一致，因为下单完成后卖家可能修改商品信息，改变了货号。该字段和cargoNUmber的区别是：该字段是定义在商品级别上的货号，cargoNUmber是定义在单品级别的货号     *
     * 参数示例：<pre>指定商品货号，该字段不一定有值，在下单时才会把货号记录。别的订单类型的货号只能通过商品接口去获取。请注意：通过商品接口获取时的货号和下单时的货号可能不一致，因为下单完成后卖家可能修改商品信息，改变了货号。该字段和cargoNUmber的区别是：该字段是定义在商品级别上的货号，cargoNUmber是定义在单品级别的货号</pre>     
     * 此参数必填
     */
    public void setProductCargoNumber(String productCargoNumber) {
        this.productCargoNumber = productCargoNumber;
    }

    private AlibabaTradeSkuItemDesc[] skuInfos;

    /**
     * @return []
     */
    public AlibabaTradeSkuItemDesc[] getSkuInfos() {
        return skuInfos;
    }

    /**
     * 设置[]     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setSkuInfos(AlibabaTradeSkuItemDesc[] skuInfos) {
        this.skuInfos = skuInfos;
    }

    private Long entryDiscount;

    /**
     * @return 订单明细涨价或降价的金额
     */
    public Long getEntryDiscount() {
        return entryDiscount;
    }

    /**
     * 设置订单明细涨价或降价的金额     *
     * 参数示例：<pre>订单明细涨价或降价的金额</pre>     
     * 此参数必填
     */
    public void setEntryDiscount(Long entryDiscount) {
        this.entryDiscount = entryDiscount;
    }

    private String specId;

    /**
     * @return 订单销售属性ID
     */
    public String getSpecId() {
        return specId;
    }

    /**
     * 设置订单销售属性ID     *
     * 参数示例：<pre>订单销售属性ID</pre>     
     * 此参数必填
     */
    public void setSpecId(String specId) {
        this.specId = specId;
    }

    private BigDecimal quantityFactor;

    /**
     * @return 以unit为单位的quantity精度系数，值为10的幂次，例如:quantityFactor=1000,unit=吨，那么quantity的最小精度为0.001吨
     */
    public BigDecimal getQuantityFactor() {
        return quantityFactor;
    }

    /**
     * 设置以unit为单位的quantity精度系数，值为10的幂次，例如:quantityFactor=1000,unit=吨，那么quantity的最小精度为0.001吨     *
     * 参数示例：<pre>以unit为单位的quantity精度系数，值为10的幂次，例如:quantityFactor=1000,unit=吨，那么quantity的最小精度为0.001吨</pre>     
     * 此参数必填
     */
    public void setQuantityFactor(BigDecimal quantityFactor) {
        this.quantityFactor = quantityFactor;
    }

    private String statusStr;

    /**
     * @return 子订单状态描述
     */
    public String getStatusStr() {
        return statusStr;
    }

    /**
     * 设置子订单状态描述     *
     * 参数示例：<pre>子订单状态描述</pre>     
     * 此参数必填
     */
    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    private String refundStatus;

    /**
     * @return WAIT_SELLER_AGREE 等待卖家同意
    REFUND_SUCCESS 退款成功
    REFUND_CLOSED 退款关闭
    WAIT_BUYER_MODIFY 待买家修改
    WAIT_BUYER_SEND 等待买家退货
    WAIT_SELLER_RECEIVE 等待卖家确认收货
     */
    public String getRefundStatus() {
        return refundStatus;
    }

    /**
     * 设置WAIT_SELLER_AGREE 等待卖家同意
    REFUND_SUCCESS 退款成功
    REFUND_CLOSED 退款关闭
    WAIT_BUYER_MODIFY 待买家修改
    WAIT_BUYER_SEND 等待买家退货
    WAIT_SELLER_RECEIVE 等待卖家确认收货     *
     * 参数示例：<pre>WAIT_SELLER_AGREE 等待卖家同意
    REFUND_SUCCESS 退款成功
    REFUND_CLOSED 退款关闭
    WAIT_BUYER_MODIFY 待买家修改
    WAIT_BUYER_SEND 等待买家退货
    WAIT_SELLER_RECEIVE 等待卖家确认收货</pre>     
     * 此参数必填
     */
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    private String closeReason;

    /**
     * @return 关闭原因
     */
    public String getCloseReason() {
        return closeReason;
    }

    /**
     * 设置关闭原因     *
     * 参数示例：<pre>关闭原因</pre>     
     * 此参数必填
     */
    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    private Integer logisticsStatus;

    /**
     * @return 1 未发货
    2 已发货
    3 已收货
    4 已经退货
    5 部分发货
    8 还未创建物流订单
     */
    public Integer getLogisticsStatus() {
        return logisticsStatus;
    }

    /**
     * 设置1 未发货
    2 已发货
    3 已收货
    4 已经退货
    5 部分发货
    8 还未创建物流订单     *
     * 参数示例：<pre>1 未发货
    2 已发货
    3 已收货
    4 已经退货
    5 部分发货
    8 还未创建物流订单</pre>     
     * 此参数必填
     */
    public void setLogisticsStatus(Integer logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    private Date gmtCreate;

    /**
     * @return 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间     *
     * 参数示例：<pre>创建时间</pre>     
     * 此参数必填
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    private Date gmtModified;

    /**
     * @return 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间     *
     * 参数示例：<pre>修改时间</pre>     
     * 此参数必填
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    private Date gmtCompleted;

    /**
     * @return 明细完成时间
     */
    public Date getGmtCompleted() {
        return gmtCompleted;
    }

    /**
     * 设置明细完成时间     *
     * 参数示例：<pre>明细完成时间</pre>     
     * 此参数必填
     */
    public void setGmtCompleted(Date gmtCompleted) {
        this.gmtCompleted = gmtCompleted;
    }

    private String gmtPayExpireTime;

    /**
     * @return 库存超时时间，格式为“yyyy-MM-dd HH:mm:ss”
     */
    public String getGmtPayExpireTime() {
        return gmtPayExpireTime;
    }

    /**
     * 设置库存超时时间，格式为“yyyy-MM-dd HH:mm:ss”     *
     * 参数示例：<pre>库存超时时间，格式为“yyyy-MM-dd HH:mm:ss”</pre>     
     * 此参数必填
     */
    public void setGmtPayExpireTime(String gmtPayExpireTime) {
        this.gmtPayExpireTime = gmtPayExpireTime;
    }

    private String refundId;

    /**
     * @return 售中退款单号
     */
    public String getRefundId() {
        return refundId;
    }

    /**
     * 设置售中退款单号     *
     * 参数示例：<pre>售中退款单号</pre>     
     * 此参数必填
     */
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    private String subItemIDString;

    /**
     * @return 子订单号，或商品明细条目ID(字符串类型，由于Long类型的ID可能在JS和PHP中处理有问题，所以可以用字符串类型来处理)
     */
    public String getSubItemIDString() {
        return subItemIDString;
    }

    /**
     * 设置子订单号，或商品明细条目ID(字符串类型，由于Long类型的ID可能在JS和PHP中处理有问题，所以可以用字符串类型来处理)     *
     * 参数示例：<pre>子订单号，或商品明细条目ID(字符串类型，由于Long类型的ID可能在JS和PHP中处理有问题，所以可以用字符串类型来处理)</pre>     
     * 此参数必填
     */
    public void setSubItemIDString(String subItemIDString) {
        this.subItemIDString = subItemIDString;
    }

    private String refundIdForAs;

    /**
     * @return 售后退款单号
     */
    public String getRefundIdForAs() {
        return refundIdForAs;
    }

    /**
     * 设置售后退款单号     *
     * 参数示例：<pre>售后退款单号</pre>     
     * 此参数必填
     */
    public void setRefundIdForAs(String refundIdForAs) {
        this.refundIdForAs = refundIdForAs;
    }

    private String erpMaterialCode;

    /**
     * @return ERP物料编码
     */
    public String getErpMaterialCode() {
        return erpMaterialCode;
    }

    /**
     * 设置ERP物料编码     *
     * 参数示例：<pre>MCZ01</pre>     
     * 此参数必填
     */
    public void setErpMaterialCode(String erpMaterialCode) {
        this.erpMaterialCode = erpMaterialCode;
    }

    private String itemDiscountFee;

    /**
     * @return 优惠金额
     */
    public String getItemDiscountFee() {
        return itemDiscountFee;
    }

    /**
     * 设置优惠金额     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setItemDiscountFee(String itemDiscountFee) {
        this.itemDiscountFee = itemDiscountFee;
    }

    private String cantSendReason;

    /**
     * @return 不可发货的原因，拼团、C2M业务都有
     */
    public String getCantSendReason() {
        return cantSendReason;
    }

    /**
     * 设置不可发货的原因，拼团、C2M业务都有     *
     * 参数示例：<pre>未成团，不可发货/该订单已由系统自动安排菜鸟仓发货，请勿重复发货</pre>     
     * 此参数必填
     */
    public void setCantSendReason(String cantSendReason) {
        this.cantSendReason = cantSendReason;
    }

    private Boolean canSendGoods;

    /**
     * @return 当前子订单是否可发货，如果当前子订单状态为待发货状态（waitsellersend），只要canSendGoods为false，不区分业务，该子订单则不可以发货，为true则可以正常发货
     */
    public Boolean getCanSendGoods() {
        return canSendGoods;
    }

    /**
     * 设置当前子订单是否可发货，如果当前子订单状态为待发货状态（waitsellersend），只要canSendGoods为false，不区分业务，该子订单则不可以发货，为true则可以正常发货     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setCanSendGoods(Boolean canSendGoods) {
        this.canSendGoods = canSendGoods;
    }

}
