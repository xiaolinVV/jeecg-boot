package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 平台专区
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Data
@TableName("marketing_prefecture")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_prefecture对象", description="平台专区")
public class MarketingPrefecture {
    
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
	/**专区名称*/
	@Excel(name = "专区名称", width = 15)
    @ApiModelProperty(value = "专区名称")
	private String prefectureName;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private BigDecimal sort;
	/**专区标签*/
	@Excel(name = "专区标签", width = 15)
    @ApiModelProperty(value = "专区标签")
	private String prefectureLabel;
	/**logo图标*/
	@Excel(name = "logo图标", width = 15)
    @ApiModelProperty(value = "logo图标")
	private String logoAddr;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
	private String coverPlan;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String posters;
	/**专区介绍*/
	@Excel(name = "专区介绍", width = 15)
    @ApiModelProperty(value = "专区介绍")
	private String prefectureExplain;
	/**是否显示分类；0：不显示；1：显示*/
	@Excel(name = "是否显示分类；0：不显示；1：显示", width = 15)
    @ApiModelProperty(value = "是否显示分类；0：不显示；1：显示")
	private String isViewType;
	/**限制商品;0:不限制；1：限制*/
	@Excel(name = "限制商品;0:不限制；1：限制", width = 15)
    @ApiModelProperty(value = "限制商品;0:不限制；1：限制")
	private String astrictGood;
	/**成本价低于销售价百分比*/
	@Excel(name = "成本价低于销售价百分比", width = 15)
    @ApiModelProperty(value = "成本价低于销售价百分比")
	private BigDecimal astrictPriceProportion;
	/**专区价限制*/
	@Excel(name = "专区价限制", width = 15)
    @ApiModelProperty(value = "专区价限制")
	private BigDecimal prefecturePriceProportion;
	/**优惠券抵扣；0：不支持；1：支持*/
	@Excel(name = "优惠券抵扣；0：不支持；1：支持", width = 15)
    @ApiModelProperty(value = "优惠券抵扣；0：不支持；1：支持")
	private String isDiscount;
	/**福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣*/
	@Excel(name = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣", width = 15)
    @ApiModelProperty(value = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣")
	private String isWelfare;
	/**福利金限额抵扣，最高可抵扣*/
	@Excel(name = "福利金限额抵扣，最高可抵扣", width = 15)
    @ApiModelProperty(value = "福利金限额抵扣，最高可抵扣")
	private BigDecimal bigWelfareProportion;
	/**赠送福利金；0：不支持；1：支持*/
	@Excel(name = "赠送福利金；0：不支持；1：支持", width = 15)
    @ApiModelProperty(value = "赠送福利金；0：不支持；1：支持")
	private String isGiveWelfare;
	/**赠送福利金，最高可送*/
	@Excel(name = "赠送福利金，最高可送", width = 15)
    @ApiModelProperty(value = "赠送福利金，最高可送")
	private BigDecimal bigGiveWelfareProportion;
	/**有效期；0：长期有效；1：短期有效*/
	@Excel(name = "有效期；0：长期有效；1：短期有效", width = 15)
    @ApiModelProperty(value = "有效期；0：长期有效；1：短期有效")
	private String validTime;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
	private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
	@TableField(insertStrategy = FieldStrategy.IGNORED,updateStrategy = FieldStrategy.IGNORED,whereStrategy = FieldStrategy.IGNORED)
	private Date endTime;
	/**状态；0：停用；1：启用；2：草稿*/
	@Excel(name = "状态；0：停用；1：启用；2：草稿", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用；2：草稿")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
	@ApiModelProperty(value = "停用说明")
	private String closeExplain;

	/**vip会员免福利金；0：不免；1：免*/
	@Excel(name = "vip会员免福利金；0：不免；1：免", width = 15)
	@ApiModelProperty(value = "vip会员免福利金；0：不免；1：免")
	private String isVipLower;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 是否显示市场价；0：不显示；1：显示
	 */
	private String isViewMarketPrice;
	/**是否支持全部；0：不支持；1：支持；*/
	@Excel(name = "是否支持全部；0：不支持；1：支持；", width = 15)
	@ApiModelProperty(value = "是否支持全部；0：不支持；1：支持；")
	private String isAllType;

	/**
	 * 专区购买限制；0：商品自定义限购；1：专区限购
	 */
	private String buyLimit;

	/**
	 * 限制购买件数
	 */
	private BigDecimal limitCount;

	/**购买人限制：会员类型：0普通会员；1：vip会员 字典：member_type*/
	@Excel(name = "购买人限制：会员类型：0普通会员；1：vip会员 字典：member_type", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "购买人限制：会员类型：0普通会员；1：vip会员 字典：member_type")
	@Dict(dicCode = "member_type")
	private String buyerLimit;

	/**限制购买的会员等级，等级id逗号隔开*/
	@Excel(name = "限制购买的会员等级，等级id逗号隔开", width = 15)
	@ApiModelProperty(value = "限制购买的会员等级，等级id逗号隔开")
	private String buyVipMemberGradeId;

	@Excel(name = "专区推荐：0：不支持；1：支持", width = 15)
	@ApiModelProperty(value = "专区推荐：0：不支持；1：支持")
	private String prefectureRecommended;

	@Excel(name = "专区推荐模块的别名", width = 15)
	@ApiModelProperty(value = "专区推荐模块的别名")
	private String prefectureRecommendedAlias;

	/**
	 * 分端显示；0：全部；1：小程序；2：app
	 */
	private String pointsDisplay;

	/**
	 * 是否使用积分；0：否；1：是
	 */
	private String isIntegral;
	/**
	 * 移除支付方式: 0: 未开启 1:开启
	 */
	private String removePay;

	/**
	 * 积分比例
	 */
	private BigDecimal proportionIntegral;

	/**
	 * 是否团队成员；0：不是；1：是
	 */
	private String isDesignation;
}
