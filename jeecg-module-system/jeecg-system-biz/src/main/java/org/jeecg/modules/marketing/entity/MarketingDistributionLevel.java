package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 分销等级
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
@Data
@TableName("marketing_distribution_level")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_distribution_level对象", description="分销等级")
public class MarketingDistributionLevel {
    
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
	/**等级名称*/
	@Excel(name = "等级名称", width = 15)
    @ApiModelProperty(value = "等级名称")
	private String levelName;
	/**图标*/
	@Excel(name = "图标", width = 15)
    @ApiModelProperty(value = "图标")
	private String icon;
	/**级别*/
	@Excel(name = "级别", width = 15)
    @ApiModelProperty(value = "级别")
	private String grade;
	/**获取方式；0：默认获得，1：参与拼购；2：直推人数*/
	@Excel(name = "获取方式；0：默认获得，1：参与拼购；2：直推人数", width = 15)
    @ApiModelProperty(value = "获取方式；0：默认获得，1：参与拼购；2：直推人数")
	private String waysObtain;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
	/**直推人数*/
	@Excel(name = "直推人数", width = 15)
    @ApiModelProperty(value = "直推人数")
	private java.math.BigDecimal direct;
	/**团队人数*/
	@Excel(name = "团队人数", width = 15)
    @ApiModelProperty(value = "团队人数")
	private java.math.BigDecimal teamNumber;
	/**级差奖励*/
	@Excel(name = "级差奖励", width = 15)
    @ApiModelProperty(value = "级差奖励")
	private java.math.BigDecimal differentialReward;
	/**平级奖励*/
	@Excel(name = "平级奖励", width = 15)
    @ApiModelProperty(value = "平级奖励")
	private java.math.BigDecimal levelRewards;
	/**
	 * 级别id
	 */
	private String marketingDistributionLevelId;
	/**
	 *级别人数
	 */
	private java.math.BigDecimal levelNumber;
	/**
	 * 奖励说明
	 */
	@TableField(exist = false)
	private String waysObtainExplain;
}
