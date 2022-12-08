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
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-20
 * @Version: V1.0
 */
@Data
@TableName("marketing_group_base_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_group_base_setting对象", description="拼团基础设置")
public class MarketingGroupBaseSetting {
    
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
	/**活动标签*/
	@Excel(name = "活动标签", width = 15)
    @ApiModelProperty(value = "活动标签")
	private String label;
	/**分端显示；0：全部；1：小程序；2：app*/
	@Excel(name = "分端显示；0：全部；1：小程序；2：app", width = 15)
    @ApiModelProperty(value = "分端显示；0：全部；1：小程序；2：app")
	private String pointsDisplay;
	/**分销佣金；0：不奖励；1：奖励*/
	@Excel(name = "分销佣金；0：不奖励；1：奖励", width = 15)
    @ApiModelProperty(value = "分销佣金；0：不奖励；1：奖励")
	private String distributionCommission;
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
	/**
	 * 商品购买资格有效期
	 */
	private BigDecimal validity;

	/**
	 * 设置推荐数量
	 */
	private BigDecimal recommendCount;

	/**
	 * 状态；0：关闭；1：开启
	 */
	private String status;
}
