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
 * @Description: 拼团专区
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
@Data
@TableName("marketing_zone_group")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_zone_group对象", description="拼团专区")
public class MarketingZoneGroup {
    
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
	/**专区名称*/
	@Excel(name = "专区名称", width = 15)
    @ApiModelProperty(value = "专区名称")
	private String zoneName;
	/**专区价格*/
	@Excel(name = "专区价格", width = 15)
    @ApiModelProperty(value = "专区价格")
	private java.math.BigDecimal price;
	/**虚拟成团人数*/
	@Excel(name = "虚拟成团人数", width = 15)
    @ApiModelProperty(value = "虚拟成团人数")
	private java.math.BigDecimal virtualGroupMembers;
	/**实际成团人数*/
	@Excel(name = "实际成团人数", width = 15)
    @ApiModelProperty(value = "实际成团人数")
	private java.math.BigDecimal actualGroupSize;
	/**次数上限（次/日）*/
	@Excel(name = "次数上限（次/日）", width = 15)
    @ApiModelProperty(value = "次数上限（次/日）")
	private java.math.BigDecimal numberCaps;
	/**参团奖励*/
	@Excel(name = "参团奖励", width = 15)
    @ApiModelProperty(value = "参团奖励")
	private java.math.BigDecimal tuxedoReward;
	/**参团奖励类型；0：福利金*/
	@Excel(name = "参团奖励类型；0：福利金", width = 15)
    @ApiModelProperty(value = "参团奖励类型；0：福利金")
	private String groupAwardType;
	/**推荐奖励*/
	@Excel(name = "推荐奖励", width = 15)
    @ApiModelProperty(value = "推荐奖励")
	private java.math.BigDecimal referralBonuses;
	/**推荐奖励类型；0：福利金*/
	@Excel(name = "推荐奖励类型；0：福利金", width = 15)
    @ApiModelProperty(value = "推荐奖励类型；0：福利金")
	private String recommendedRewardTypes;
	/**转化阀值*/
	@Excel(name = "转化阀值", width = 15)
    @ApiModelProperty(value = "转化阀值")
	private java.math.BigDecimal transformationThreshold;
	/**可寄售*/
	@Excel(name = "可寄售", width = 15)
    @ApiModelProperty(value = "可寄售")
	private java.math.BigDecimal canConsignment;
	/**默认发货*/
	@Excel(name = "默认发货", width = 15)
    @ApiModelProperty(value = "默认发货")
	private java.math.BigDecimal defaultShipping;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
	@Excel(name = "删除说明", width = 15)
	@ApiModelProperty(value = "删除说明")
	private String delExplain;
}
