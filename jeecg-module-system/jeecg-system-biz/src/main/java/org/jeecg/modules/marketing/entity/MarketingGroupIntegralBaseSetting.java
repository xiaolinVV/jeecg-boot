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

/**
 * @Description: 拼购活动
 * @Author: jeecg-boot
 * @Date:   2021-06-27
 * @Version: V1.0
 */
@Data
@TableName("marketing_group_integral_base_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_group_integral_base_setting对象", description="拼购活动")
public class MarketingGroupIntegralBaseSetting {
    
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
	/**活动别名*/
	@Excel(name = "活动别名", width = 15)
    @ApiModelProperty(value = "活动别名")
	private String anotherName;
	/**分端显示；0：全部；1：小程序；2：app*/
	@Excel(name = "分端显示；0：全部；1：小程序；2：app", width = 15)
    @ApiModelProperty(value = "分端显示；0：全部；1：小程序；2：app")
	private String pointsDisplay;
	/**规则说明*/
	@Excel(name = "规则说明", width = 15)
    @ApiModelProperty(value = "规则说明")
	private Object ruleDescription;
	private String startTime;
	private String endTime;
	/**状态：0：关闭；1：启用*/
	@Excel(name = "状态：0：关闭；1：启用", width = 15)
    @ApiModelProperty(value = "状态：0：关闭；1：启用")
	private String status;
	/**活动次数*/
	@Excel(name = "活动次数", width = 15)
    @ApiModelProperty(value = "活动次数")
	private BigDecimal times;
	/**首页广告图*/
	@Excel(name = "首页广告图", width = 15)
    @ApiModelProperty(value = "首页广告图")
	private String adIndexAddr;

	/**
	 * 中奖次数
	 */
	private BigDecimal winningNumber;

	/**
	 * 参与限制
	 */
	private BigDecimal participationConstraint;
}
