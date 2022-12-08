package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 创业进账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
@Data
@TableName("marketing_business_make_money")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_business_make_money对象", description="创业进账资金")
public class MarketingBusinessMakeMoney extends JeecgEntity {
    
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
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
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
	private String delFlag;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
	private String serialNumber;
	/**资金配置id*/
	@Excel(name = "资金配置id", width = 15)
    @ApiModelProperty(value = "资金配置id")
	private String marketingBusinessCapitalId;
	/**交易类型；0：创业礼包分红 ;1：非称号分红*/
	@Excel(name = "交易类型；0：创业礼包分红;1：非称号分红", width = 15,dicCode = "member_welfare_deal_type")
    @ApiModelProperty(value = "交易类型；0：创业礼包分红;1：非称号分红")
	@Dict(dicCode = "member_welfare_deal_type")
	private String tradeType;
	/**分红资金*/
	@Excel(name = "分红资金", width = 15)
    @ApiModelProperty(value = "分红资金")
	private java.math.BigDecimal bonusMoney;
	/**进账比例*/
	@Excel(name = "进账比例", width = 15)
    @ApiModelProperty(value = "进账比例")
	private java.math.BigDecimal makeProportion;
	/**进账金额*/
	@Excel(name = "进账金额", width = 15)
    @ApiModelProperty(value = "进账金额")
	private java.math.BigDecimal amount;
	/**资金池余额*/
	@Excel(name = "资金池余额", width = 15)
    @ApiModelProperty(value = "资金池余额")
	private java.math.BigDecimal totalBalance;
	/**交易时间*/
	@Excel(name = "交易时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "交易时间")
	private Date payTime;
	/**交易单号*/
	@Excel(name = "交易单号", width = 15)
    @ApiModelProperty(value = "交易单号")
	private String tradeNo;
	/**进出账；0：入账；1：出账*/
	@Excel(name = "进出账；0：入账；1：出账", width = 15)
    @ApiModelProperty(value = "进出账；0：入账；1：出账")
	private String goAndConme;
	/**出账方式；0：无；1：每日分红；2：手动出账*/
	@Excel(name = "出账方式；0：每日分红；1：手动出账", width = 15)
	@ApiModelProperty(value = "出账方式；0：每日分红；1：手动出账")
	private String accountsWay;
}
