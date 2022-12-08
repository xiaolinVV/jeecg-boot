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
 * @Description: 余额支付日志
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
@Data
@TableName("pay_balance_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_balance_log对象", description="余额支付日志")
public class PayBalanceLog {
    
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
	/**支付json日志*/
	@Excel(name = "支付json日志", width = 15)
    @ApiModelProperty(value = "支付json日志")
	private String payLog;
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
	/**支付失败原因*/
	@Excel(name = "支付失败原因", width = 15)
    @ApiModelProperty(value = "支付失败原因")
	private String defeatedExplain;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
    @ApiModelProperty(value = "总金额")
	private java.math.BigDecimal totalFee;
	/**支付单号*/
	@Excel(name = "支付单号", width = 15)
    @ApiModelProperty(value = "支付单号")
	private String serialNumber;
	/**支付方式；0：微信支付，1：支付宝支付*/
	@Excel(name = "支付方式；0：微信支付，1：支付宝支付", width = 15)
    @ApiModelProperty(value = "支付方式；0：微信支付，1：支付宝支付")
	private String payModel;
	/**支付金额*/
	@Excel(name = "支付金额", width = 15)
    @ApiModelProperty(value = "支付金额")
	private java.math.BigDecimal payPrice;

	/**
	 * 支付日志
	 */
	private String payParam;
}
