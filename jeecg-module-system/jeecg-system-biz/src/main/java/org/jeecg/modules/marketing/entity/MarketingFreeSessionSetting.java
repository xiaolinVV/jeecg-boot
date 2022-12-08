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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 免单场次设置
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Data
@TableName("marketing_free_session_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_free_session_setting对象", description="免单场次设置")
public class MarketingFreeSessionSetting {
    
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
	@TableLogic
	private String delFlag;
	/**创建方式；0：手动创建；1：自动创建*/
	@Excel(name = "创建方式；0：手动创建；1：自动创建", width = 15)
    @ApiModelProperty(value = "创建方式；0：手动创建；1：自动创建")
	private String createMode;
	/**活动周期*/
	@Excel(name = "活动周期", width = 15)
    @ApiModelProperty(value = "活动周期")
	private java.math.BigDecimal activityCycle;
	/**执行日期*/
	@Excel(name = "执行日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "执行日期")
	private java.util.Date executionDate;
	/**执行时间；0：当日执行；1：次日开始*/
	@Excel(name = "执行时间；0：当日执行；1：次日开始", width = 15)
    @ApiModelProperty(value = "执行时间；0：当日执行；1：次日开始")
	private String executionTime;
	/**执行状态；0：停用；1：启用*/
	@Excel(name = "执行状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "执行状态；0：停用；1：启用")
	private String executingState;
	/**执行次数*/
	@Excel(name = "执行次数", width = 15)
    @ApiModelProperty(value = "执行次数")
	private java.math.BigDecimal executingDegree;
	/**温馨提示*/
	@Excel(name = "温馨提示", width = 15)
    @ApiModelProperty(value = "温馨提示")
	private String warmPrompt;
	/**下次执行时间*/
	@Excel(name = "下次执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下次执行时间")
	private java.util.Date nextTime;
}
