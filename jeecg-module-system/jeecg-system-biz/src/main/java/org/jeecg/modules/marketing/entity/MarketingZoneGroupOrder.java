package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 拼中商品
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
@Data
@TableName("marketing_zone_group_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_zone_group_order对象", description="拼中商品")
public class MarketingZoneGroupOrder {

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
	private String delFlag;
	/**专区管理id*/
	@Excel(name = "专区管理id", width = 15)
	@ApiModelProperty(value = "专区管理id")
	private String marketingZoneGroupManageId;
	/**专区团分组编号id*/
	@Excel(name = "专区团分组编号id", width = 15)
	@ApiModelProperty(value = "专区团分组编号id")
	private String marketingZoneGroupGroupingId;
	/**参团编号*/
	@Excel(name = "参团编号", width = 15)
	@ApiModelProperty(value = "参团编号")
	private String marketingZoneGroupRecordId;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
	@ApiModelProperty(value = "订单编号")
	private String serialNumber;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
	@ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**订单id*/
	@Excel(name = "订单id", width = 15)
	@ApiModelProperty(value = "订单id")
	private String orderListId;
	/**留言*/
	@Excel(name = "留言", width = 15)
	@ApiModelProperty(value = "留言")
	private String message;
	/**专区商品id*/
	@Excel(name = "专区商品id", width = 15)
	@ApiModelProperty(value = "专区商品id")
	private String marketingZoneGroupGoodId;
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
	/**区域id*/
	@Excel(name = "区域id", width = 15)
	@ApiModelProperty(value = "区域id")
	private String sysAreaId;
	/**商品总价*/
	@Excel(name = "商品总价", width = 15)
	@ApiModelProperty(value = "商品总价")
	private java.math.BigDecimal goodsTotal;
	/**配送金额*/
	@Excel(name = "配送金额", width = 15)
	@ApiModelProperty(value = "配送金额")
	private java.math.BigDecimal shipFee;
	/**应付款（支付前标准金额）*/
	@Excel(name = "应付款（支付前标准金额）", width = 15)
	@ApiModelProperty(value = "应付款（支付前标准金额）")
	private java.math.BigDecimal customaryDues;
	/**实付款（支付后标准金额）*/
	@Excel(name = "实付款（支付后标准金额）", width = 15)
	@ApiModelProperty(value = "实付款（支付后标准金额）")
	private java.math.BigDecimal actualPayment;
	/**状态；1：已付款；2：待发货；3：已寄售；4：已完成*/
	@Excel(name = "状态；1：已付款；2：待发货；3：已寄售；4：已完成", width = 15)
	@ApiModelProperty(value = "状态；1：已付款；2：待发货；3：已寄售；4：已完成")
	private String status;
	/**付款时间*/
	@Excel(name = "付款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "付款时间")
	private java.util.Date payTime;
	/**寄售时间*/
	@Excel(name = "寄售时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "寄售时间")
	private java.util.Date consignmentTime;
	/**寄售完成时间*/
	@Excel(name = "寄售完成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "寄售完成时间")
	private java.util.Date consignmentEndTime;
	/**商品规格id*/
	@Excel(name = "商品规格id", width = 15)
	@ApiModelProperty(value = "商品规格id")
	private String goodSpecificationId;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.math.BigDecimal quantity;
	/**支付记录id*/
	@Excel(name = "支付记录id", width = 15)
	@ApiModelProperty(value = "支付记录id")
	private String payZoneGroupLogId;

	@Version
	private Integer version;

	/**
	 * 是否分配奖励；0：未分配；1：已分配
	 */
	private String distributionRewards;
}
