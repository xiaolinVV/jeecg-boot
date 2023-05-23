package org.jeecg.modules.order.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date: 2023-04-20
 * @Version: V1.0
 */
@Data
@TableName("order_refund_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "order_refund_list对象", description = "order_refund_list")
public class OrderRefundList implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 申请时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "申请时间")
    private java.util.Date applyTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    /**
     * 余额到账时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "余额到账时间")
    private java.util.Date balanceReceiveTime;
    /**
     * 微信到账时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "微信到账时间")
    private java.util.Date huifuReceiveTime;

    /**
     * 创建年
     */
    @Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private java.lang.Integer year;
    /**
     * 创建月
     */
    @Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private java.lang.Integer month;
    /**
     * 创建日
     */
    @Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private java.lang.Integer day;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private java.lang.String delFlag;
    /**
     * 子订单id（供应商/包裹）
     */
    @Excel(name = "子订单id（供应商/包裹）", width = 15)
    @ApiModelProperty(value = "子订单id（供应商/包裹）")
    private java.lang.String orderSubListId;
    /**
     * 订单id（店铺订单/平台订单）
     */
    @Excel(name = "订单id（店铺订单/平台订单）", width = 15)
    @ApiModelProperty(value = "订单id（店铺订单/平台订单）")
    private java.lang.String orderListId;
    /**
     * 订单号（店铺订单/平台订单）
     */
    @Excel(name = "订单号（店铺订单/平台订单）", width = 15)
    @ApiModelProperty(value = "订单号（店铺订单/平台订单）")
    private java.lang.String orderNo;
    /**
     * 订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；
     */
    @Excel(name = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；", width = 15)
    @ApiModelProperty(value = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；")
    @Dict(dicCode = "order_type")
    private java.lang.String orderType;
    /**
     * 订单商品记录id
     */
    @Excel(name = "订单商品记录id", width = 15)
    @ApiModelProperty(value = "订单商品记录id")
    private java.lang.String orderGoodRecordId;
    /**
     * 会员id
     */
    @Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
    @Dict(dictTable = "member_list",dicCode = "id",dicText = "phone")
    private java.lang.String memberId;
    /**
     * 店铺用户id/平台供应商用户id
     */
    @Excel(name = "店铺用户id/平台供应商用户id", width = 15)
    @ApiModelProperty(value = "店铺用户id/平台供应商用户id")
    private java.lang.String sysUserId;
    /**
     * 退款类型 0=仅退款 1=退货退款 2=换货 关联字典：refund_type
     */
    @Dict(dicCode = "refund_type")
    private java.lang.String refundType;
    /**
     * 退款原因,关联字典：order_store_refund_reason
     */
    @Dict(dicCode = "order_refund_reason")
    private java.lang.String refundReason;
    /**
     * 后台商家拒绝退款理由
     */
    @Excel(name = "后台商家拒绝退款理由", width = 15)
    @ApiModelProperty(value = "后台商家拒绝退款理由")
    private java.lang.String refundExplain;
    /**
     * 售后申请说明
     */
    @Excel(name = "售后申请说明", width = 15)
    @ApiModelProperty(value = "售后申请说明")
    private java.lang.String remarks;
    /**
     * 售后状态 0=待处理 1=待买家退回 2=换货中（等待店铺确认收货） 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成。 关联字典：order_refund_status
     */
    @Dict(dicCode = "order_refund_status")
    private java.lang.String status;
    /**
     * 订单商品主图相对地址（以json的形式存储多张）
     */
    @Excel(name = "订单商品主图相对地址（以json的形式存储多张）", width = 15)
    @ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
    private java.lang.String goodMainPicture;
    /**
     * 订单商品id（只做对象映射）
     */
    @Excel(name = "订单商品id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "订单商品id（只做对象映射）")
    private java.lang.String goodListId;
    /**
     * 订单商品规格id（只做对象映射）
     */
    @Excel(name = "订单商品规格id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "订单商品规格id（只做对象映射）")
    private java.lang.String goodSpecificationId;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
    private java.lang.String goodName;
    /**
     * 规格名称，按照顺序逗号隔开
     */
    @Excel(name = "规格名称，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "规格名称，按照顺序逗号隔开")
    private java.lang.String goodSpecification;

    /**订单商品销售单价*/
    @Excel(name = "订单商品销售单价", width = 15)
    @ApiModelProperty(value = "订单商品销售单价")
    private BigDecimal goodUnitPrice;
    /**
     * 申请退款凭证图片，按照顺序逗号隔开
     */
    @Excel(name = "申请退款凭证图片，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "申请退款凭证图片，按照顺序逗号隔开")
    private java.lang.String refundCertificate;
    /**
     * 申请退款金额
     */
    @Excel(name = "申请退款金额", width = 15)
    @ApiModelProperty(value = "申请退款金额")
    private java.math.BigDecimal refundPrice;
    /**
     * 申请退款数量
     */
    @Excel(name = "申请退款数量", width = 15)
    @ApiModelProperty(value = "申请退款数量")
    private java.math.BigDecimal refundAmount;

    /**
     * 0=店铺  1=平台
     */
    private String isPlatform;

    /**
     * 订单商品总计金额（小计）
     */
    @Excel(name = "订单商品总计金额（小计）", width = 15)
    @ApiModelProperty(value = "订单商品总计金额（小计）")
    private java.math.BigDecimal goodRecordTotal;
    /**
     * 订单商品实付款
     */
    @Excel(name = "订单商品实付款", width = 15)
    @ApiModelProperty(value = "订单商品实付款")
    private java.math.BigDecimal goodRecordActualPayment;
    /**
     * 订单商品优惠卷优惠金额
     */
    @Excel(name = "订单商品优惠卷优惠金额", width = 15)
    @ApiModelProperty(value = "订单商品优惠卷优惠金额")
    private java.math.BigDecimal goodRecordCoupon;

    /**
     * 订单商品礼品卡优惠金额
     */
    private BigDecimal goodRecordGiftCardCoupon;

    /**
     * 订单商品总优惠金额
     */
    private BigDecimal goodRecordTotalCoupon;

    /**
     * 订单商品购买数量
     */
    @Excel(name = "订单商品购买数量", width = 15)
    @ApiModelProperty(value = "订单商品购买数量")
    private java.math.BigDecimal goodRecordAmount;

    /**
     * 订单商品优惠券记录id,多个用逗号分隔
     */
    private String goodRecordMarketingDiscountCouponId;

    /**
     * 售后单关闭原因，关联字典 refund_close_explain
     */
    @ApiModelProperty(value = "关闭原因，关联字典 refund_close_explain")
    @Dict(dicCode = "refund_close_explain")
    private String closeExplain;

    /**
     * 商家拒绝原因
     */
    private String refusedExplain;

    /**
     * 退货/换货商家邮寄信息：商家收件人姓名
     */
    private String merchantConsigneeName;

    /**
     * 退货/换货商家邮寄信息：商家收件地址
     */
    private String merchantConsigneeAddress;

    /**
     * 退货/换货商家邮寄信息：商家收件手机号
     */
    private String merchantConsigneePhone;

    /**
     * 退货/换货商家邮寄信息：商家收件地址：省
     */
    private String merchantConsigneeProvinceId;

    /**
     * 退货/换货商家邮寄信息：商家收件地址：市
     */
    private String merchantConsigneeCityId;

    /**
     * 退货/换货商家邮寄信息：商家收件地址：区
     */
    private String merchantConsigneeAreaId;


    /**
     * 买家寄回物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；
     */
    @Dict(dicCode = "logistics_company")
    private java.lang.String buyerLogisticsCompany;
    /**
     * 买家寄回快递单号
     */
    private java.lang.String buyerTrackingNumber;

    /**
     * 买家寄回物流跟踪信息的json保存（每次查询的时候更新）
     */
    private String buyerLogisticsTracking;

    /**
     * 买家换货商品规格id，逗号分隔
     */
    private java.lang.String exchangeGoodSpecificationId;

    /**
     * 买家换货规格名称，按照顺序逗号隔开
     */
    private java.lang.String exchangeGoodSpecification;

    /**
     * 买家换货：买家收货地址json
     */
    private java.lang.String exchangeMemberShippingAddress;

    /**
     * 换货时卖家寄回：商家物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；
     */
    @Dict(dicCode = "logistics_company")
    private java.lang.String merchantLogisticsCompany;
    /**
     * 换货时卖家寄回：商家快递单号
     */
    private java.lang.String merchantTrackingNumber;

    /**
     * 换货时卖家寄回：商家物流跟踪信息的json保存（每次查询的时候更新）
     */
    private String merchantLogisticsTracking;

    /**
     * 退款json返回日志
     */
    private String refundJson;

    /**
     * 实际退款余额
     */
    private java.math.BigDecimal actualRefundBalance;

    /**
     * 实际退款现金（汇付微信）
     */
    private java.math.BigDecimal actualRefundPrice;

    /**
     * 实际退还礼品卡金额
     */
    private BigDecimal actualRefundGiftCardBalance;

    /**
     * 实际退还抵扣的福利金（专区商品）
     */
    private BigDecimal actualRefundDiscountWelfarePayments;

    /**
     * 订单商品抵扣福利金价值（专区商品）
     */
    private BigDecimal welfarePaymentsPrice;

    /**
     * 订单商品抵扣福利金（专区商品）
     */
    private BigDecimal welfarePayments;

    /**
     * 实际退款优惠券记录id
     */
    private String actualRefundMarketingDiscountCouponId;

    /**
     * 商品赠送的积分
     */
    private BigDecimal goodRecordGiveWelfarePayments;

    /**
     * 买家换货：收货地址id
     */
    @TableField(exist = false)
    private String memberShippingAddressId;

    /**
     * 实际退还积分
     *
     */
    private BigDecimal actualReturnWelfarePayments;

    /**
     * 退款渠道  0=微信 1=余额。 全部勾选则逗号分隔，如0,1
     */
    private String refundChannel;




}
