package org.jeecg.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 创业礼包支付日志
 * @Author: jeecg-boot
 * @Date:   2021-08-01
 * @Version: V1.0
 */
@Data
@TableName("pay_business_gift_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_business_gift_log对象", description="创业礼包支付日志")
public class PayBusinessGiftLog {
    
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
	/**创业礼包id*/
	@Excel(name = "创业礼包id", width = 15)
    @ApiModelProperty(value = "创业礼包id")
	private String marketingBusinessGiftListId;
	/**支付金额*/
	@Excel(name = "支付金额", width = 15)
    @ApiModelProperty(value = "支付金额")
	private java.math.BigDecimal totalPrice;
	/**支付方式；0：微信支付，1：支付宝支付*/
	@Excel(name = "支付方式；0：微信支付，1：支付宝支付", width = 15)
    @ApiModelProperty(value = "支付方式；0：微信支付，1：支付宝支付")
	private String payModel;
	/**折扣Json*/
	@Excel(name = "折扣Json", width = 15)
    @ApiModelProperty(value = "折扣Json")
	private String discount;
	/**支付金额*/
	@Excel(name = "支付金额", width = 15)
    @ApiModelProperty(value = "支付金额")
	private java.math.BigDecimal payPrice;
	/**余额*/
	@Excel(name = "余额", width = 15)
    @ApiModelProperty(value = "余额")
	private java.math.BigDecimal balance;
	/**支付日志*/
	@Excel(name = "支付日志", width = 15)
    @ApiModelProperty(value = "支付日志")
	private String payParam;
	/**支付状态；0:未支付；1：已支付；2：支付失败；3：已分配*/
	@Excel(name = "支付状态；0:未支付；1：已支付；2：支付失败；3：已分配", width = 15)
    @ApiModelProperty(value = "支付状态；0:未支付；1：已支付；2：支付失败；3：已分配")
	private String payStatus;
	/**回调状态；0：未回调；1：已回调*/
	@Excel(name = "回调状态；0：未回调；1：已回调", width = 15)
    @ApiModelProperty(value = "回调状态；0：未回调；1：已回调")
	private String backStatus;
	/**回调次数*/
	@Excel(name = "回调次数", width = 15)
    @ApiModelProperty(value = "回调次数")
	private java.math.BigDecimal backTimes;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**交易流水号*/
	@Excel(name = "交易流水号", width = 15)
    @ApiModelProperty(value = "交易流水号")
	private String serialNumber;
	/**收货地址id*/
	@Excel(name = "收货地址id", width = 15)
    @ApiModelProperty(value = "收货地址id")
	private String memberShippingAddressId;
	/**福利金*/
	@Excel(name = "福利金", width = 15)
    @ApiModelProperty(value = "福利金")
	private java.math.BigDecimal welfarePayments;
	/**留言*/
	@Excel(name = "留言", width = 15)
    @ApiModelProperty(value = "留言")
	private String message;

	/**
	 * 推荐人会员id
	 */
	private String tMemberId;
}
