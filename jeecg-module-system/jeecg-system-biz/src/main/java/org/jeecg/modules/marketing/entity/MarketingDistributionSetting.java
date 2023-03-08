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
 * @Description: 平台分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
@Data
@TableName("marketing_distribution_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_distribution_setting对象", description="平台分销设置")
public class MarketingDistributionSetting {
    
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
	/**普通一级分销比例*/
	@Excel(name = "普通一级分销比例", width = 15)
    @ApiModelProperty(value = "普通一级分销比例")
	private java.math.BigDecimal commonFirst;
	/**普通二级分销比例*/
	@Excel(name = "普通二级分销比例", width = 15)
    @ApiModelProperty(value = "普通二级分销比例")
	private java.math.BigDecimal commonSecond;
	/**会员一级分销比例*/
	@Excel(name = "会员一级分销比例", width = 15)
    @ApiModelProperty(value = "会员一级分销比例")
	private java.math.BigDecimal vipFirst;
	/**会员二级分销比例*/
	@Excel(name = "会员二级分销比例", width = 15)
    @ApiModelProperty(value = "会员二级分销比例")
	private java.math.BigDecimal vipSecond;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**分销海报地址*/
	@Excel(name = "分销海报地址", width = 15)
    @ApiModelProperty(value = "分销海报地址")
	private String distributionPosters;
	/**挣钱攻略*/
	@Excel(name = "挣钱攻略", width = 15)
    @ApiModelProperty(value = "挣钱攻略")
	private String strategy;
	/**归属店铺百分比*/
	@Excel(name = "归属店铺百分比", width = 15)
    @ApiModelProperty(value = "归属店铺百分比")
	private java.math.BigDecimal affiliationStoreAward;
	/**销售渠道百分比*/
	@Excel(name = "销售渠道百分比", width = 15)
    @ApiModelProperty(value = "销售渠道百分比")
	private java.math.BigDecimal distributionChannelAward;
	@Excel(name = "推荐人限制条件: 0:无限制 1:需购买礼包 2:需成为vip会员 3:需购买送vip礼包", width = 15)
	@ApiModelProperty(value = "推荐人限制条件: 0:无限制 1:需购买礼包 2:需成为vip会员 3:需购买送vip礼包")
	private String isThreshold;
	@Excel(name = "绑定关系条件: 0:被推荐人注册 1:被推荐人首次购买礼包 2:被推荐人首次购买送vip礼包", width = 15)
	@ApiModelProperty(value = "绑定关系条件: 0:被推荐人注册 1:被推荐人首次购买礼包 2:被推荐人首次购买送vip礼包")
	private String distributionBuild;
	/**
	 * 佣金类型；0：金额，1：积分
	 */
	private String commissionType;

	/**
	 * 二级延伸；0：否；1：是
	 */
	private String secondaryStretching;

	/**
	 * 分销等级；0：停用；1：启用
	 */
	private String distributionLevel;

}
