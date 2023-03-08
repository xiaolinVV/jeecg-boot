package org.jeecg.modules.order.dto;

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
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Data
@TableName("order_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "order_list对象", description = "订单列表")
public class OrderListExpDTO {


    /**
     * 排序
     */
    @Excel(name = "序号",needMerge = true,orderNum = "1")
    private Integer index;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 25, format = "yyyy-MM-dd HH:mm:ss",needMerge = true,orderNum = "4")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**
     * 创建年
     */

    @ApiModelProperty(value = "创建年")
    private Integer year;
    /**
     * 创建月
     */

    @ApiModelProperty(value = "创建月")
    private Integer month;
    /**
     * 创建日
     */

    @ApiModelProperty(value = "创建日")
    private Integer day;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private String delFlag;
    /**
     * 会员列表id
     */
    @ApiModelProperty(value = "会员列表id")
    private String memberListId;
    /**
     * 订单号
     */
    @Excel(name = "订单号", width = 30,needMerge = true,orderNum = "2")
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /**
     * 订单类型；数据字段获取
     */
    @ApiModelProperty(value = "订单类型；数据字段获取")
    @Dict(dicCode = "order_type")
    private String orderType;
    /**
     * 收货人
     */
    @Excel(name = "收货人", width = 15,needMerge = true,orderNum = "9")
    @ApiModelProperty(value = "收货人")
    private String consignee;
    /**
     * 联系电话
     */
   @Excel(name = "联系电话", width = 15,needMerge = true,orderNum = "10")
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;
    /**
     * 收货地址
     */
    @Excel(name = "收货地址", width = 40,needMerge = true,orderNum = "11")
    @ApiModelProperty(value = "收货地址")
    private String shippingAddress;
    /**
     * 门牌号
     */
    @ApiModelProperty(value = "门牌号")
    private String houseNumber;
    /**
     * 留言
     */
   @Excel(name = "留言", width = 40,needMerge = true,orderNum ="12" )
    @ApiModelProperty(value = "留言")
    private String message;
    /**
     * 商品总价
     */
    @ApiModelProperty(value = "商品总价")
    private BigDecimal goodsTotal;
    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal coupon;
    /**
     * 配送金额
     */
    @ApiModelProperty(value = "配送金额")
    private BigDecimal shipFee;
    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private String marketingDiscountCouponId;
    /**
     * 应付款（支付前标准金额）
     */
    @ApiModelProperty(value = "应付款（支付前标准金额）")
    private BigDecimal customaryDues;
    /**
     * 实付款（支付后标准金额）
     */
    @ApiModelProperty(value = "实付款（支付后标准金额）")
    private BigDecimal actualPayment;

    /**
     * 余额
     */
    @Excel(name = "余额", width = 30,needMerge = true,orderNum = "16")
    private BigDecimal balance;


    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额（实际现金金额）")
    @Excel(name = "第三方支付金额", width = 30,needMerge = true,orderNum = "13")
    private BigDecimal payPrice;

    /**
     * 支付的福利金
     */
    @Excel(name = "支付的积分数量", width = 30,needMerge = true,orderNum = "14")
    @ApiModelProperty(value = "支付的积分数量")
    private BigDecimal payWelfarePayments;


    /**
     * 支付的福利金价值
     */
    @ApiModelProperty(value = "支付的积分价值")
    @Excel(name = "支付的积分价值", width = 30,needMerge = true,orderNum = "15")
    private BigDecimal payWelfarePaymentsPrice;


    /**
     * 有无修改地址（0：无修改地址；1：有修改地址）
     */
    @ApiModelProperty(value = "有无修改地址（0：无修改地址；1：有修改地址）")
    private String isUpdateAddr;
    /**
     * 订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败
     */
    @Excel(name = "订单状态", width = 15,needMerge = true,orderNum = "3")
    @ApiModelProperty(value = "订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败")
    private String status;
    /**
     * 付款时间
     */
    @Excel(name = "付款时间", width = 25, format = "yyyy-MM-dd HH:mm:ss",needMerge = true,orderNum = "5")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付款时间")
    private Date payTime;
    /**
     * 首次发货时间
     */
    @Excel(name = "发货时间", width = 25, format = "yyyy-MM-dd HH:mm:ss",needMerge = true,orderNum = "6")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "首次发货时间")
    private Date shipmentsTime;
    /**
     * 确认收货时间
     */
    @Excel(name = "收货时间", width = 25, format = "yyyy-MM-dd HH:mm:ss",needMerge = true,orderNum = "7")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "确认收货时间")
    private Date deliveryTime;
    /**
     * 子订单数量
     */
    @ApiModelProperty(value = "子订单数量")
    private BigDecimal childOrder;
    /**
     * 关闭类型;查看数据字典
     */
    @ApiModelProperty(value = "关闭类型;查看数据字典")
    @Dict(dicCode = "oder_close_type")
    private String closeType;
    /**
     * 关闭原因
     */
    @ApiModelProperty(value = "关闭原因")
    @Dict(dicCode = "oder_close_explain")
    private String closeExplain;
    /**
     * 关闭时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "关闭时间")
    private Date closeTime;
    /**
     * 推广人
     */
    @ApiModelProperty(value = "推广人")
    private String promoter;
    /**
     * 归属店铺
     */
    @ApiModelProperty(value = "归属店铺")
    private String affiliationStore;
    /**
     * 销售渠道
     */
    @ApiModelProperty(value = "销售渠道")
    private String distributionChannel;
    /**
     * 配送方式；对应数据字典
     */
    @ApiModelProperty(value = "配送方式；对应数据字典")
    @Dict(dicCode = "oder_distribution")
    private String distribution;
    /**
     * 是否部分发货；0：否；1：是
     */
    @ApiModelProperty(value = "是否部分发货；0：否；1：是")
    private String isSender;
    /**
     * 订单完成时间
     */
    @Excel(name = "完成时间", width = 25, format = "yyyy-MM-dd HH:mm:ss",needMerge = true,orderNum = "8")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单完成时间")
    private Date completionTime;
    /**
     * 物流星级
     */
    @ApiModelProperty(value = "物流星级")
    private BigDecimal logisticsStar;
    /**
     * 发货星级
     */
    @ApiModelProperty(value = "发货星级")
    private BigDecimal shippingStar;
    /**
     * 服务星级
     */
    @ApiModelProperty(value = "服务星级")
    private BigDecimal serviceStar;
    /**
     * 评价时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评价时间")
    private Date evaluateTime;
    /**
     * 是否评价；0：未评价；1：已评价
     */
    @ApiModelProperty(value = "是否评价；0：未评价；1：已评价")
    private String isEvaluate;
    /**
     * 成本价（供货价）
     */
    @ApiModelProperty(value = "成本价（供货价）")
    private BigDecimal costPrice;
    /**
     * 商品利润（销售价减去成本价）
     */
    @ApiModelProperty(value = "商品利润（销售价减去成本价）")
    private BigDecimal profit;
    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    private BigDecimal distributionCommission;
    /**
     * 净利润
     */
    @ApiModelProperty(value = "净利润")
    private BigDecimal retainedProfits;
    /**
     * 平台净利润（供货价减去平台成本价）
     */
    @ApiModelProperty(value = "平台净利润（供货价减去平台成本价）")
    private BigDecimal platformRetainedProfits;
    /**
     * 平台成本价
     */
    @ApiModelProperty(value = "平台成本价")
    private BigDecimal platformProfit;

    @ApiModelProperty(value = "福利金")
    private BigDecimal welfarePayments;

    @ApiModelProperty(value = "商品总件数")
    private BigDecimal allNumberUnits;

    @ApiModelProperty(value = "交易流水号")
    private String serialNumber;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    private BigDecimal giveWelfarePayments;
    /**
     * 会员直降金额
     */
    @ApiModelProperty(value = "会员直降金额")
    private BigDecimal vipLowerTotal;

    @ApiModelProperty(value = "推广人类型;0:店铺；1：会员；2：平台")
    private String promoterType;
    /**
     * 会员等级名称
     */
    @ApiModelProperty(value = "会员等级名称")
    private String memberGrade;
    /**
     * 会员等级优惠总金额
     */
    @ApiModelProperty(value = "会员等级优惠总金额")
    private BigDecimal memberDiscountPriceTotal;
    /**
     * 会员等级特权信息
     */
    @ApiModelProperty(value = "会员等级特权信息")
    private String memberGradeContent;

    private String sysAreaId;


    @ExcelCollection(name = "",orderNum = "17")
    private List<OrderProviderListExpDTO> orderProviderListExpDTs;

}
