package org.jeecg.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * @Description: 商品订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Data
@TableName("order_store_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_store_list对象", description="商品订单列表")
public class OrderStoreList {
    
	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**创建年*/
	@Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
	private Integer year;
	/**创建月*/
	@Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
	private Integer month;
	/**创建日*/
	@Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	@TableLogic
	private String delFlag;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**订单号*/
	@Excel(name = "订单号", width = 15)
    @ApiModelProperty(value = "订单号")
	private String orderNo;
	/**订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；*/
	@Excel(name = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；", width = 15)
    @ApiModelProperty(value = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；")
	@Dict(dicCode = "order_type")
	private String orderType;
	/**收货人*/
	@Excel(name = "收货人", width = 15)
    @ApiModelProperty(value = "收货人")
	private String consignee;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
	private String contactNumber;
	/**收货地址*/
	@Excel(name = "收货地址", width = 15)
    @ApiModelProperty(value = "收货地址")
	private String shippingAddress;
	/**门牌号*/
	@Excel(name = "门牌号", width = 15)
    @ApiModelProperty(value = "门牌号")
	private String houseNumber;
	/**留言*/
	@Excel(name = "留言", width = 15)
    @ApiModelProperty(value = "留言")
	private String message;
	/**商品总价*/
	@Excel(name = "商品总价", width = 15)
    @ApiModelProperty(value = "商品总价")
	private BigDecimal goodsTotal;
	/**优惠金额*/
	@Excel(name = "优惠金额", width = 15)
    @ApiModelProperty(value = "优惠金额")
	private BigDecimal coupon;
	/**配送金额*/
	@Excel(name = "配送金额", width = 15)
    @ApiModelProperty(value = "配送金额")
	private BigDecimal shipFee;
	/**优惠券id*/
	@Excel(name = "优惠券id", width = 15)
    @ApiModelProperty(value = "优惠券id")
	private String marketingDiscountCouponId;
	/**应付款（支付前标准金额）*/
	@Excel(name = "应付款（支付前标准金额）", width = 15)
    @ApiModelProperty(value = "应付款（支付前标准金额）")
	private BigDecimal customaryDues;
	/**实付款（支付后标准金额）*/
	@Excel(name = "实付款（支付后标准金额）", width = 15)
    @ApiModelProperty(value = "实付款（支付后标准金额）")
	private BigDecimal actualPayment;
	/**有无修改地址（0：无修改地址；1：有修改地址）*/
	@Excel(name = "有无修改地址（0：无修改地址；1：有修改地址）", width = 15)
    @ApiModelProperty(value = "有无修改地址（0：无修改地址；1：有修改地址）")
	private String isUpdateAddr;
	/**订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败*/
	@Excel(name = "订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败", width = 15)
    @ApiModelProperty(value = "订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败")
	private String status;
	/**付款时间*/
	@Excel(name = "付款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付款时间")
	private java.util.Date payTime;
	/**首次发货时间*/
	@Excel(name = "首次发货时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "首次发货时间")
	private java.util.Date shipmentsTime;
	/**确认收货时间*/
	@Excel(name = "确认收货时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "确认收货时间")
	private java.util.Date deliveryTime;
	/**子订单数量*/
	@Excel(name = "子订单数量", width = 15)
    @ApiModelProperty(value = "子订单数量")
	private BigDecimal childOrder;
	/**关闭类型；0：超时关闭；1：买家关闭；2：卖家关闭；数据字典：oder_close_type；*/
	@Excel(name = "关闭类型；0：超时关闭；1：买家关闭；2：卖家关闭；数据字典：oder_close_type；", width = 15,dicCode = "oder_close_type")
    @ApiModelProperty(value = "关闭类型；0：超时关闭；1：买家关闭；2：卖家关闭；数据字典：oder_close_type；")
	@Dict(dicCode = "oder_close_type")
	private String closeType;
	/**订单关闭原因；超时关闭：原因0：超时未付款；买家关闭：原因1：订单不能按预计时间送达；原因2：操作有误（商品、地址等选错）；原因3：重复下单/误下单；原因4：其他渠道价格更低；原因5：不想买；原因6：商品无货；原因7：其他原因；卖家关闭：原因8：未及时付款；原因9：买家不想买了 ；原因10：买家信息填写错误，重新拍；原因11：恶意买家/同行捣乱；原因12：缺货；原因13：同城见面交易；原因14：其他原因；数据字典：oder_close_explain*/
	@Excel(name = "订单关闭原因；超时关闭：原因0：超时未付款；买家关闭：原因1：订单不能按预计时间送达；原因2：操作有误（商品、地址等选错）；原因3：重复下单/误下单；原因4：其他渠道价格更低；原因5：不想买；原因6：商品无货；原因7：其他原因；卖家关闭：原因8：未及时付款；原因9：买家不想买了 ；原因10：买家信息填写错误，重新拍；原因11：恶意买家/同行捣乱；原因12：缺货；原因13：同城见面交易；原因14：其他原因；数据字典：oder_close_explain", width = 15)
    @ApiModelProperty(value = "订单关闭原因；超时关闭：原因0：超时未付款；买家关闭：原因1：订单不能按预计时间送达；原因2：操作有误（商品、地址等选错）；原因3：重复下单/误下单；原因4：其他渠道价格更低；原因5：不想买；原因6：商品无货；原因7：其他原因；卖家关闭：原因8：未及时付款；原因9：买家不想买了 ；原因10：买家信息填写错误，重新拍；原因11：恶意买家/同行捣乱；原因12：缺货；原因13：同城见面交易；原因14：其他原因；数据字典：oder_close_explain")
	@Dict(dicCode = "oder_close_explain")
	private String closeExplain;
	/**关闭时间*/
	@Excel(name = "关闭时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "关闭时间")
	private java.util.Date closeTime;
	/**推广人*/
	@Excel(name = "推广人", width = 15)
    @ApiModelProperty(value = "推广人")
	private String promoter;
	/**订单配送方式；0：快递；1：自提；2：配送；数据字典：oder_distribution*/
	@Excel(name = "订单配送方式；0：快递；1：自提；2：配送；数据字典：oder_distribution", width = 15)
    @ApiModelProperty(value = "订单配送方式；0：快递；1：自提；2：配送；数据字典：oder_distribution")
	@Dict(dicCode = "store_distribution_type")
	private String distribution;
	/**订单完成时间*/
	@Excel(name = "订单完成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单完成时间")
	private java.util.Date completionTime;
	/**物流星级*/
	@Excel(name = "物流星级", width = 15)
    @ApiModelProperty(value = "物流星级")
	private BigDecimal logisticsStar;
	/**发货星级*/
	@Excel(name = "发货星级", width = 15)
    @ApiModelProperty(value = "发货星级")
	private BigDecimal shippingStar;
	/**服务星级*/
	@Excel(name = "服务星级", width = 15)
    @ApiModelProperty(value = "服务星级")
	private BigDecimal serviceStar;
	/**评价时间*/
	@Excel(name = "评价时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评价时间")
	private java.util.Date evaluateTime;
	/**是否评价；0：未评价；1：已评价*/
	@Excel(name = "是否评价；0：未评价；1：已评价", width = 15)
    @ApiModelProperty(value = "是否评价；0：未评价；1：已评价")
	private String isEvaluate;
	/**成本价（供货价）*/
	@Excel(name = "成本价（供货价）", width = 15)
    @ApiModelProperty(value = "成本价（供货价）")
	private BigDecimal costPrice;
	/**商品利润*/
	@Excel(name = "商品利润", width = 15)
    @ApiModelProperty(value = "商品利润")
	private BigDecimal profit;
	/**分销佣金*/
	@Excel(name = "分销佣金", width = 15)
    @ApiModelProperty(value = "分销佣金")
	private BigDecimal distributionCommission;
	/**净利润*/
	@Excel(name = "净利润", width = 15)
    @ApiModelProperty(value = "净利润")
	private BigDecimal retainedProfits;
	/**店铺id*/
	@Excel(name = "店铺id", width = 15)
    @ApiModelProperty(value = "店铺id")
	private String sysUserId;
	/**是否部分发货；0：否；1：是*/
	@Excel(name = "是否部分发货；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否部分发货；0：否；1：是")
	private String isSender;
	@Excel(name = "商品总件数", width = 15)
	@ApiModelProperty(value = "商品总件数")
	private BigDecimal allNumberUnits;

	@Excel(name = "交易流水号", width = 15)
	@ApiModelProperty(value = "交易流水号")
	private String serialNumber;

	@Excel(name = "推广人类型;0:店铺；1：会员；2：平台", width = 15)
	@ApiModelProperty(value = "推广人类型;0:店铺；1：会员；2：平台")
	private String promoterType;

	/**
	 * 汇付天下交易流水号
	 */
	private String hftxSerialNumber;


	/**
	 * 支付的福利金
	 */
	private BigDecimal payWelfarePayments;

	/**
	 * 支付的福利金价值
	 */
	private BigDecimal payWelfarePaymentsPrice;

	private String discount;
	/**
	 * 支付金额
	 */
	private BigDecimal payPrice;

	/**
	 * 余额
	 */
	private BigDecimal balance;

	/**
	 * 支付方式；0：微信；1：支付宝
	 */
	private String modePayment;

	/**
	 * 礼品卡优惠金额
	 */
	private BigDecimal giftCardTotal;

	/**
	 * 活动id
	 */
	private String activeId;

	/**
	 * 退款json返回日志
	 */
	private String refundJson;
}
