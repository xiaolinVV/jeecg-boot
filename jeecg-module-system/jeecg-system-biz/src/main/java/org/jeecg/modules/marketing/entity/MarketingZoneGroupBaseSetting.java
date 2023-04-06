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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 专区团基本设置
 * @Author: jeecg-boot
 * @Date:   2021-07-22
 * @Version: V1.0
 */
@Data
@TableName("marketing_zone_group_base_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_zone_group_base_setting对象", description="专区团基本设置")
public class MarketingZoneGroupBaseSetting {
    
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
	/**活动别名*/
	@Excel(name = "活动别名", width = 15)
    @ApiModelProperty(value = "活动别名")
	private String anotherName;
	/**活动标签*/
	@Excel(name = "活动标签", width = 15)
    @ApiModelProperty(value = "活动标签")
	private String label;
	/**分端显示；0：全部；1：小程序；2：app*/
	@Excel(name = "分端显示；0：全部；1：小程序；2：app", width = 15)
    @ApiModelProperty(value = "分端显示；0：全部；1：小程序；2：app")
	private String pointsDisplay;
	/**开放时间段开始*/
	@Excel(name = "开放时间段开始", width = 15)
    @ApiModelProperty(value = "开放时间段开始")
	private String dayStartTime;
	/**开放时间段结束*/
	@Excel(name = "开放时间段结束", width = 15)
    @ApiModelProperty(value = "开放时间段结束")
	private String dayEndTime;
	/**建团次数*/
	@Excel(name = "建团次数", width = 15)
    @ApiModelProperty(value = "建团次数")
	private BigDecimal groupNumber;
	/**结算周期*/
	@Excel(name = "结算周期", width = 15)
    @ApiModelProperty(value = "结算周期")
	private BigDecimal settlementInterval;
	/**规则简述*/
	@Excel(name = "规则简述", width = 15)
    @ApiModelProperty(value = "规则简述")
	private String rulesBriefly;
	/**规则说明*/
	@Excel(name = "规则说明", width = 15)
    @ApiModelProperty(value = "规则说明")
	private Object ruleDescription;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
	private String coverPlan;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String posters;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
    @ApiModelProperty(value = "封面图")
	private String surfacePlot;
	/**状态；0：关闭；1：开启*/
	@Excel(name = "状态；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "状态；0：关闭；1：开启")
	private String status;
	/**分享标签*/
	@Excel(name = "分享标签", width = 15)
    @ApiModelProperty(value = "分享标签")
	private String shareTitle;
	/**分享描述*/
	@Excel(name = "分享描述", width = 15)
    @ApiModelProperty(value = "分享描述")
	private String shareDescription;
	/**团队奖励条件*/
	@Excel(name = "团队奖励条件", width = 15)
	@ApiModelProperty(value = "团队奖励条件")
	private BigDecimal rewardConditions;
}