package org.jeecg.modules.pay.entity;

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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * @Description: 收银支付记录
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@Data
@TableName("pay_shouyin_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_shouyin_log对象", description="收银支付记录")
public class PayShouyinLog {
    
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
	@TableLogic
	private String delFlag;
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
	private BigDecimal backTimes;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**支付失败原因*/
	@Excel(name = "支付失败原因", width = 15)
    @ApiModelProperty(value = "支付失败原因")
	private String defeatedExplain;
	/**交易流水号*/
	@Excel(name = "交易流水号", width = 15)
    @ApiModelProperty(value = "交易流水号")
	private String serialNumber;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
    @ApiModelProperty(value = "交易金额")
	private BigDecimal allTotalPrice;
	/**赠送*/
	@Excel(name = "赠送", width = 15)
    @ApiModelProperty(value = "赠送")
	private BigDecimal welfarePayments;
	/**支付方式；0：微信支付，1：支付宝支付*/
	@Excel(name = "支付方式；0：微信支付，1：支付宝支付", width = 15)
    @ApiModelProperty(value = "支付方式；0：微信支付，1：支付宝支付")
	private String payModel;
	/**支付金额*/
	@Excel(name = "支付金额", width = 15)
    @ApiModelProperty(value = "支付金额")
	private BigDecimal payPrice;
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;

	private BigDecimal brokerage;

	private BigDecimal settlementAmount;

	private String independentAccount;

	private String remark;
}
