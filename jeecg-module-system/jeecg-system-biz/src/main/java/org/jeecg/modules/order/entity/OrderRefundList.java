package org.jeecg.modules.order.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
     * 订单id
     */
    @Excel(name = "订单id", width = 15)
    @ApiModelProperty(value = "订单id")
    private java.lang.String orderListId;
    /**
     * 订单号
     */
    @Excel(name = "订单号", width = 15)
    @ApiModelProperty(value = "订单号")
    private java.lang.String orderNo;
    /**
     * 订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；
     */
    @Excel(name = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；", width = 15)
    @ApiModelProperty(value = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；")
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
     * 店铺用户id
     */
    @Excel(name = "店铺用户id", width = 15)
    @ApiModelProperty(value = "店铺用户id")
    private java.lang.String sysUserId;
    /**
     * 退款类型 0=仅退款 1=退货退款 2=换货
     */
    @Excel(name = "退款类型 0=仅退款 1=退货退款 2=换货", width = 15)
    @ApiModelProperty(value = "退款类型 0=仅退款 1=退货退款 2=换货")
    private java.lang.String refundType;
    /**
     * 退款原因
     */
    @Excel(name = "退款原因", width = 15)
    @ApiModelProperty(value = "退款原因")
    private java.lang.String refundReason;
    /**
     * 拒绝退款理由
     */
    @Excel(name = "拒绝退款理由", width = 15)
    @ApiModelProperty(value = "拒绝退款理由")
    private java.lang.String refundExplain;
    /**
     * 申请说明
     */
    @Excel(name = "申请说明", width = 15)
    @ApiModelProperty(value = "申请说明")
    private java.lang.String remarks;
    /**
     * 售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成
     */
    @Excel(name = "售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成", width = 15)
    @ApiModelProperty(value = "售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成")
    private java.lang.String status;
    /**
     * 商品主图相对地址（以json的形式存储多张）
     */
    @Excel(name = "商品主图相对地址（以json的形式存储多张）", width = 15)
    @ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
    private java.lang.String goodMainPicture;
    /**
     * 平台商品id（只做对象映射）
     */
    @Excel(name = "平台商品id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "平台商品id（只做对象映射）")
    private java.lang.String goodListId;
    /**
     * 商品规格id（只做对象映射）
     */
    @Excel(name = "商品规格id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "商品规格id（只做对象映射）")
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
    /**
     * 退款凭证图片，按照顺序逗号隔开
     */
    @Excel(name = "退款凭证图片，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "退款凭证图片，按照顺序逗号隔开")
    private java.lang.String refundCertificate;
    /**
     * 退款金额
     */
    @Excel(name = "退款金额", width = 15)
    @ApiModelProperty(value = "退款金额")
    private java.math.BigDecimal refundPrice;
    /**
     * 退款数量
     */
    @Excel(name = "退款数量", width = 15)
    @ApiModelProperty(value = "退款数量")
    private java.math.BigDecimal refundAmount;

    /**
     * 0=店铺  1=平台
     */
    private String isPlatform;

    /**
     * 商品总计金额（小计）
     */
    @Excel(name = "商品总计金额（小计）", width = 15)
    @ApiModelProperty(value = "商品总计金额（小计）")
    private java.math.BigDecimal goodRecordTotal;
    /**
     * 商品实付款
     */
    @Excel(name = "商品实付款", width = 15)
    @ApiModelProperty(value = "商品实付款")
    private java.math.BigDecimal goodRecordActualPayment;
    /**
     * 商品优惠金额
     */
    @Excel(name = "商品优惠金额", width = 15)
    @ApiModelProperty(value = "商品优惠金额")
    private java.math.BigDecimal goodRecordCoupon;

    /**
     * 礼品卡优惠金额
     */
    private BigDecimal goodRecordGiftCardCoupon;

    /**
     * 总优惠金额
     */
    private BigDecimal goodRecordTotalCoupon;

    /**
     * 商品数量
     */
    @Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
    private java.math.BigDecimal goodRecordAmount;

    /**
     * 关闭原因，关联字典 refund_close_explain
     */
    @ApiModelProperty(value = "关闭原因，关联字典 refund_close_explain")
    @Dict(dicCode = "refund_close_explain")
    private String closeExplain;

    /**
     * 拒绝原因
     */
    private String refusedExplain;

    /**
     * 商家收件人姓名
     */
    private String merchantConsigneeName;

    /**
     * 商家收件地址
     */
    private String merchantConsigneeAddress;

    /**
     * 商家收件手机号
     */
    private String merchantConsigneePhone;

    /**
     * 商家收件地址：省
     */
    private String merchantConsigneeProvinceId;

    /**
     * 商家收件地址：市
     */
    private String merchantConsigneeCityId;

    /**
     * 商家收件地址：区
     */
    private String merchantConsigneeAreaId;


    /**
     * 买家物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；
     */
    private java.lang.String buyerLogisticsCompany;
    /**
     * 买家快递单号
     */
    private java.lang.String buyerTrackingNumber;

    /**
     * 买家物流跟踪信息的json保存（每次查询的时候更新）
     */
    private String buyerLogisticsTracking;

    /**
     * 换货商品规格id，逗号分隔
     */
    @Excel(name = "商品规格id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "商品规格id（只做对象映射）")
    private java.lang.String exchangeGoodSpecificationId;

    /**
     * 换货规格名称，按照顺序逗号隔开
     */
    @Excel(name = "规格名称，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "规格名称，按照顺序逗号隔开")
    private java.lang.String exchangeGoodSpecification;

    /**
     * 换货：买家收货地址
     */
    private java.lang.String exchangeMemberShippingAddress;

    /**
     * 换货：商家物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；
     */
    private java.lang.String merchantLogisticsCompany;
    /**
     * 换货：商家快递单号
     */
    private java.lang.String merchantTrackingNumber;

    /**
     * 换货：商家物流跟踪信息的json保存（每次查询的时候更新）
     */
    private String merchantLogisticsTracking;



}
