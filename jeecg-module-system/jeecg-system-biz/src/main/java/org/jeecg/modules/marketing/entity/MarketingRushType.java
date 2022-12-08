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
 * @Description: 抢购活动-分类管理
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Data
@TableName("marketing_rush_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_rush_type对象", description="抢购活动-分类管理")
public class MarketingRushType {
    
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
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
	private String rushName;
	/**价格*/
	@Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
	private java.math.BigDecimal price;
	/**每日限购*/
	@Excel(name = "每日限购", width = 15)
    @ApiModelProperty(value = "每日限购")
	private java.math.BigDecimal purchaseLimitation;
	/**参与奖励*/
	@Excel(name = "参与奖励", width = 15)
    @ApiModelProperty(value = "参与奖励")
	private java.math.BigDecimal groupRewards;
	/**参与奖励类型；0：余额*/
	@Excel(name = "参与奖励类型；0：余额", width = 15)
    @ApiModelProperty(value = "参与奖励类型；0：余额")
	private String groupRewardsType;
	/**推荐奖励*/
	@Excel(name = "推荐奖励", width = 15)
    @ApiModelProperty(value = "推荐奖励")
	private java.math.BigDecimal recommendGroupRewards;
	/**推荐奖励类型；0：余额*/
	@Excel(name = "推荐奖励类型；0：余额", width = 15)
    @ApiModelProperty(value = "推荐奖励类型；0：余额")
	private String recommendGroupRewardsType;
	/**寄售阀值*/
	@Excel(name = "寄售阀值", width = 15)
    @ApiModelProperty(value = "寄售阀值")
	private java.math.BigDecimal transformationThreshold;
	/**可寄售*/
	@Excel(name = "可寄售", width = 15)
    @ApiModelProperty(value = "可寄售")
	private java.math.BigDecimal canConsignment;
	/**专区id*/
	@Excel(name = "专区id", width = 15)
    @ApiModelProperty(value = "专区id")
	private String marketingPrefectureId;
	/**专区分类id*/
	@Excel(name = "专区分类id", width = 15)
    @ApiModelProperty(value = "专区分类id")
	private String marketingPrefectureTypeId;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
    @ApiModelProperty(value = "删除说明")
	private String delExplain;
	/**排序*/
	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**转入余额*/
	@Excel(name = "转入余额", width = 15)
	@ApiModelProperty(value = "转入余额")
	private java.math.BigDecimal intoBalance;
	/**转入购物积分*/
	@Excel(name = "转入购物积分", width = 15)
	@ApiModelProperty(value = "转入购物积分")
	private java.math.BigDecimal intoShoppingCredits;
}
