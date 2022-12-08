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

import java.util.Date;

/**
 * @Description: 抢购活动-基础设置
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Data
@TableName("marketing_rush_base_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_rush_base_setting对象", description="抢购活动-基础设置")
public class MarketingRushBaseSetting {
    
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
	/**首页广告图*/
	@Excel(name = "首页广告图", width = 15)
    @ApiModelProperty(value = "首页广告图")
	private String indexAddress;
	/**首页广告图大*/
	@Excel(name = "首页广告图大", width = 15)
    @ApiModelProperty(value = "首页广告图大")
	private String indexBigAddress;
	/**专区id*/
	@Excel(name = "专区id", width = 15)
    @ApiModelProperty(value = "专区id")
	private String marketingPrefectureId;
	/**专区分类id*/
	@Excel(name = "专区分类id", width = 15)
    @ApiModelProperty(value = "专区分类id")
	private String marketingPrefectureTypeId;
	/**抢购控制*/
	@Excel(name = "抢购控制", width = 15)
    @ApiModelProperty(value = "抢购控制")
	private String rushController;
	/**有效参与奖励；0：不限；1：仅失败；2：仅成功*/
	@Excel(name = "有效参与奖励；0：不限；1：仅失败；2：仅成功", width = 15)
    @ApiModelProperty(value = "有效参与奖励；0：不限；1：仅失败；2：仅成功")
	private String groupRewards;
	/**有效推荐奖励；0：不限；1：仅失败；2：仅成功*/
	@Excel(name = "有效推荐奖励；0：不限；1：仅失败；2：仅成功", width = 15)
    @ApiModelProperty(value = "有效推荐奖励；0：不限；1：仅失败；2：仅成功")
	private String recommendGroupRewards;
	/**有效团队奖励；0：不限；1：仅失败；2：仅成功*/
	@Excel(name = "有效团队奖励；0：不限；1：仅失败；2：仅成功", width = 15)
    @ApiModelProperty(value = "有效团队奖励；0：不限；1：仅失败；2：仅成功")
	private String teamRewards;
	/**团队长需抢购达限额；0：不限；1：限制*/
	@Excel(name = "团队长需抢购达限额；0：不限；1：限制", width = 15)
    @ApiModelProperty(value = "团队长需抢购达限额；0：不限；1：限制")
	private String rewardLimitOne;
	/**团队直推人员需抢购达限额；0：不限；1：限制*/
	@Excel(name = "团队直推人员需抢购达限额；0：不限；1：限制", width = 15)
    @ApiModelProperty(value = "团队直推人员需抢购达限额；0：不限；1：限制")
	private String rewardLimitTwo;
	/**团队奖励时间*/
	@Excel(name = "团队奖励时间", width = 15)
    @ApiModelProperty(value = "团队奖励时间")
	private java.math.BigDecimal rewardTime;
	/**规则*/
	@Excel(name = "规则", width = 15)
    @ApiModelProperty(value = "规则")
	private Object ruleDescription;
	/**状态；0：关闭；1：开启*/
	@Excel(name = "状态；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "状态；0：关闭；1：开启")
	private String status;
}
