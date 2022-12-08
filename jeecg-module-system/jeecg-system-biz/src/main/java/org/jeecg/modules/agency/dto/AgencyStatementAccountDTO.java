package org.jeecg.modules.agency.dto;

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

import java.util.Date;

/**
 * @Description: 代理对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
@Data
@TableName("agency_statement_account")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="agency_statement_account对象", description="代理对账单")
public class AgencyStatementAccountDTO {

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
	@TableLogic
	private String delFlag;
	/**日期*/
	@Excel(name = "日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
	private Date calendarDate;
	/**收入*/
	@Excel(name = "收入", width = 15)
    @ApiModelProperty(value = "收入")
	private java.math.BigDecimal earning;
	/**收入笔数*/
	@Excel(name = "收入笔数", width = 15)
    @ApiModelProperty(value = "收入笔数")
	private java.math.BigDecimal incomeNumber;
	/**支出*/
	@Excel(name = "支出", width = 15)
    @ApiModelProperty(value = "支出")
	private java.math.BigDecimal expenditure;
	/**支出笔数*/
	@Excel(name = "支出笔数", width = 15)
    @ApiModelProperty(value = "支出笔数")
	private java.math.BigDecimal expenditureNumber;
	/**收益*/
	@Excel(name = "收益", width = 15)
    @ApiModelProperty(value = "收益")
	private java.math.BigDecimal revenue;
	/**期初余额*/
	@Excel(name = "期初余额", width = 15)
    @ApiModelProperty(value = "期初余额")
	private java.math.BigDecimal startBalance;
	/**期末余额*/
	@Excel(name = "期末余额", width = 15)
    @ApiModelProperty(value = "期末余额")
	private java.math.BigDecimal endBalance;
	/**代理id*/
	@Excel(name = "代理id", width = 15)
    @ApiModelProperty(value = "代理id")
	private String sysUserId;
	/**
	 * 查询时间开始
	 */
	private String calendarDate_begin;
	/**
	 * 查询时间结束
	 */
	private String calendarDate_end;

	/**代理名称*/
	private String realname;
	/**
	 * 代理账号
	 */
	private String userName;
}
