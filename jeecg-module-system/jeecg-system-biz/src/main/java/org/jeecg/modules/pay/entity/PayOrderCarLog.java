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

import java.math.BigDecimal;

/**
 * @Description: 订单支付日志
 * @Author: jeecg-boot
 * @Date:   2019-12-02
 * @Version: V1.0
 */
@Data
@TableName("pay_order_car_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_order_car_log对象", description="订单支付日志")
public class PayOrderCarLog {
    
	/**主键ID*/
	@TableId(type = IdType.ASSIGN_UUID)
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
	private String payOrderLog;
	/**支付日志*/
	@Excel(name = "支付日志", width = 15)
    @ApiModelProperty(value = "支付日志")
	private String payParam;
	/**支付状态；0:未支付；1：已支付；2：支付失败*/
	@Excel(name = "支付状态；0:未支付；1：已支付；2：支付失败", width = 15)
    @ApiModelProperty(value = "支付状态；0:未支付；1：已支付；2：支付失败")
	private String payStatus;
	/**回调状态；0：未回调；1：已回调*/
	@Excel(name = "回调状态；0：未回调；1：已回调", width = 15)
    @ApiModelProperty(value = "回调状态；0：未回调；1：已回调")
	private String backStatus;
	/**回调次数*/
	@Excel(name = "回调次数", width = 15)
    @ApiModelProperty(value = "回调次数")
	private BigDecimal backTimes;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**支付失败原因*/
	@Excel(name = "支付失败原因", width = 15)
    @ApiModelProperty(value = "支付失败原因")
	private String defeatedExplain;

	/**
	 * 汇付的交易流水号
	 */
	private String serialNumber;
	/**
	 * 交易金额
	 */
	private BigDecimal allTotalPrice;

	private BigDecimal balance;

	private BigDecimal welfarePayments;
	/**
	 * 支付方式；0：微信支付，1：支付宝支付
	 */
	private String payModel;
	/**
	 * 支付金额
	 */
	private BigDecimal payPrice;

	private BigDecimal integral;

	private BigDecimal integralPrice;//积分价值


	private String TMemberId;
}
