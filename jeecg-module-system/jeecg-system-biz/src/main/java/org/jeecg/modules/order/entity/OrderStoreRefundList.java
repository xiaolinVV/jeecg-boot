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
 * @Description: 店铺订单售后申请表
 * @Author: jeecg-boot
 * @Date:   2023-04-10
 * @Version: V1.0
 */
@Data
@TableName("order_store_refund_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="order_store_refund_list对象", description="店铺订单售后申请表")
public class OrderStoreRefundList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
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
	/**供应商订单id*/
	@Excel(name = "供应商订单id", width = 15)
    @ApiModelProperty(value = "供应商订单id")
    private String orderStoreSubListId;
	/**订单id*/
	@Excel(name = "订单id", width = 15)
    @ApiModelProperty(value = "订单id")
    private String orderStoreListId;
	/**订单号*/
	@Excel(name = "订单号", width = 15)
    @ApiModelProperty(value = "订单号")
    private String orderNo;
	/**订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；*/
	@Excel(name = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；", width = 15)
    @ApiModelProperty(value = "订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；")
    private String orderType;
	/**订单商品记录id*/
	@Excel(name = "订单商品记录id", width = 15)
    @ApiModelProperty(value = "订单商品记录id")
    private String orderStoreGoodRecordId;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
    private String memberId;
	/**店铺用户id*/
	@Excel(name = "店铺用户id", width = 15)
    @ApiModelProperty(value = "店铺用户id")
    private String sysUserId;
	/**退款类型 0=仅退款 1=退货退款 2=换货*/
	@Excel(name = "退款类型 0=仅退款 1=退货退款 2=换货", width = 15)
    @ApiModelProperty(value = "退款类型 0=仅退款 1=退货退款 2=换货")
    private String refundType;
	/**退款原因*/
	@Excel(name = "退款原因", width = 15)
    @ApiModelProperty(value = "退款原因")
    private String refundReason;
	/**拒绝退款理由*/
	@Excel(name = "拒绝退款理由", width = 15)
    @ApiModelProperty(value = "拒绝退款理由")
    private String refundExplain;
	/**申请说明*/
	@Excel(name = "申请说明", width = 15)
    @ApiModelProperty(value = "申请说明")
    private String remarks;
	/**售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成*/
	@Excel(name = "售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成", width = 15)
    @ApiModelProperty(value = "售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成")
    private String status;
	/**商品主图相对地址（以json的形式存储多张）*/
	@Excel(name = "商品主图相对地址（以json的形式存储多张）", width = 15)
    @ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
    private String goodMainPicture;
	/**平台商品id（只做对象映射）*/
	@Excel(name = "平台商品id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "平台商品id（只做对象映射）")
    private String goodStoreListId;
	/**商品规格id（只做对象映射）*/
	@Excel(name = "商品规格id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "商品规格id（只做对象映射）")
    private String goodStoreSpecificationId;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
    private String goodName;
	/**规格名称，按照顺序逗号隔开*/
	@Excel(name = "规格名称，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "规格名称，按照顺序逗号隔开")
    private String goodSpecification;
	/**退款凭证图片，按照顺序逗号隔开*/
	@Excel(name = "退款凭证图片，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "退款凭证图片，按照顺序逗号隔开")
    private String refundCertificate;
	/**退款金额*/
	@Excel(name = "退款金额", width = 15)
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;
	/**退款数量*/
	@Excel(name = "退款数量", width = 15)
    @ApiModelProperty(value = "退款数量")
    private BigDecimal refundAmount;
	/**物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；*/
	@Excel(name = "物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；", width = 15)
    @ApiModelProperty(value = "物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；")
    private String logisticsCompany;
	/**快递单号*/
	@Excel(name = "快递单号", width = 15)
    @ApiModelProperty(value = "快递单号")
    private String trackingNumber;
}
