package org.jeecg.modules.marketing.entity;

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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 建团次数明细
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
@Data
@TableName("marketing_zone_group_number_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_zone_group_number_record对象", description="建团次数明细")
public class MarketingZoneGroupNumberRecord {
    
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
	/**拼团次数id*/
	@Excel(name = "拼团次数id", width = 15)
    @ApiModelProperty(value = "拼团次数id")
	private String marketingZoneGroupTimeId;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
	private String serialNumber;
	/**收入和支出；0：收入；1：支出*/
	@Excel(name = "收入和支出；0：收入；1：支出", width = 15)
    @ApiModelProperty(value = "收入和支出；0：收入；1：支出")
	private String goAndCome;
	/**交易类型，字典*/
	@Excel(name = "交易类型，字典 create_group_add_lessen_type", width = 15,dicCode = "create_group_add_lessen_type")
    @ApiModelProperty(value = "交易类型，字典 create_group_add_lessen_type")
	@Dict(dicCode = "create_group_add_lessen_type")
	private String tradeType;
	/**建团次数*/
	@Excel(name = "建团次数", width = 15)
    @ApiModelProperty(value = "建团次数")
	private java.math.BigDecimal groupNumber;
	/**可用次数*/
	@Excel(name = "可用次数", width = 15)
    @ApiModelProperty(value = "可用次数")
	private java.math.BigDecimal numberAvailable;
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
}
