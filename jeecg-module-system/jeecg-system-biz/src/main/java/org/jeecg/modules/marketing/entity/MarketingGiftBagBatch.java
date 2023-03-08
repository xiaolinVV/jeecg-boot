package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 采购礼包
 * @Author: jeecg-boot
 * @Date:   2020-08-29
 * @Version: V1.0
 */
@Data
@TableName("marketing_gift_bag_batch")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_gift_bag_batch对象", description="采购礼包")
public class MarketingGiftBagBatch {
    
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
	private String delFlag;
	/**礼包名称*/
	@Excel(name = "礼包名称", width = 15)
    @ApiModelProperty(value = "礼包名称")
	private String giftName;
	/**礼包价格*/
	@Excel(name = "礼包价格", width = 15)
    @ApiModelProperty(value = "礼包价格")
	private java.math.BigDecimal price;
	/**发货次数*/
	@Excel(name = "发货次数", width = 15)
    @ApiModelProperty(value = "发货次数")
	private java.math.BigDecimal sendTimes;
	/**分销佣金*/
	@Excel(name = "分销佣金", width = 15)
    @ApiModelProperty(value = "分销佣金")
	private java.math.BigDecimal distributionCommission;
	/**推广奖励,不设置的话就取字典promoter_reward作为默认值*/
	@Excel(name = "推广奖励,不设置的话就取字典promoter_reward作为默认值", width = 15)
    @ApiModelProperty(value = "推广奖励,不设置的话就取字典promoter_reward作为默认值")
	private java.math.BigDecimal promoterReward;
	/**二级推广奖励,不设置的话就取字典promoter_reward_two作为默认值*/
	@Excel(name = "二级推广奖励,不设置的话就取字典promoter_reward_two作为默认值", width = 15)
    @ApiModelProperty(value = "二级推广奖励,不设置的话就取字典promoter_reward_two作为默认值")
	private java.math.BigDecimal promoterRewardTwo;
	/**归属店铺奖励,不设置的话就取字典ownership_shops_reward作为默认值*/
	@Excel(name = "归属店铺奖励,不设置的话就取字典ownership_shops_reward作为默认值", width = 15)
    @ApiModelProperty(value = "归属店铺奖励,不设置的话就取字典ownership_shops_reward作为默认值")
	private java.math.BigDecimal ownershipShopsReward;
	/**渠道店铺奖励,不设置的话就取字典channel_shops_reward作为默认值*/
	@Excel(name = "渠道店铺奖励,不设置的话就取字典channel_shops_reward作为默认值", width = 15)
    @ApiModelProperty(value = "渠道店铺奖励,不设置的话就取字典channel_shops_reward作为默认值")
	private java.math.BigDecimal channelShopsReward;
	/**分配方式：0：按代理比例分配；1：按加盟商比例分配*/
	@Excel(name = "分配方式：0：按代理比例分配；1：按加盟商比例分配", width = 15)
    @ApiModelProperty(value = "分配方式：0：按代理比例分配；1：按加盟商比例分配")
	private String modeDistribution;
	/**分钱模式；0：按照代理或者加盟商的奖励；1：自定义奖励*/
	@Excel(name = "分钱模式；0：按照代理或者加盟商的奖励；1：自定义奖励", width = 15)
    @ApiModelProperty(value = "分钱模式；0：按照代理或者加盟商的奖励；1：自定义奖励")
	private String allocationModel;
	/**省代奖励*/
	@Excel(name = "省代奖励", width = 15)
    @ApiModelProperty(value = "省代奖励")
	private java.math.BigDecimal proviceAward;
	/**市代奖励*/
	@Excel(name = "市代奖励", width = 15)
    @ApiModelProperty(value = "市代奖励")
	private java.math.BigDecimal cityAward;
	/**区县代奖励*/
	@Excel(name = "区县代奖励", width = 15)
    @ApiModelProperty(value = "区县代奖励")
	private java.math.BigDecimal towmAward;
	/**购买人限制：会员类型：0：普通会员；1：vip会员 字典：member_type*/
	@Excel(name = "购买人限制：会员类型：0：普通会员；1：vip会员 字典：member_type", width = 15,dicCode = "member_type")
    @ApiModelProperty(value = "购买人限制：会员类型：0：普通会员；1：vip会员 字典：member_type")
	@Dict(dicCode = "member_type")
	private String buyLimit;
	/**限制购买的会员等级，等级id逗号隔开*/
	@Excel(name = "限制购买的会员等级，等级id逗号隔开", width = 15)
    @ApiModelProperty(value = "限制购买的会员等级，等级id逗号隔开")
	private String buyVipMemberGradeId;
	/**最低购买数量*/
	@Excel(name = "最低购买数量", width = 15)
    @ApiModelProperty(value = "最低购买数量")
	private java.math.BigDecimal smallBuyCount;
	/**礼包可见范围；0：指定门店；1：全平台*/
	@Excel(name = "礼包可见范围；0：指定门店；1：全平台", width = 15)
    @ApiModelProperty(value = "礼包可见范围；0：指定门店；1：全平台")
	private String viewScope;
	/**库存（发行量）*/
	@Excel(name = "库存（发行量）", width = 15)
    @ApiModelProperty(value = "库存（发行量）")
	private java.math.BigDecimal repertory;
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
	private Date endTime;
	/**主图；逗号隔开*/
	@Excel(name = "主图；逗号隔开", width = 15)
    @ApiModelProperty(value = "主图；逗号隔开")
	private String mainPicture;
	/**详情图*/
	@Excel(name = "详情图", width = 15)
    @ApiModelProperty(value = "详情图")
	private String giftDeals;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
	private String coverPlan;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String posters;
	/**礼包说明*/
	@Excel(name = "礼包说明", width = 15)
    @ApiModelProperty(value = "礼包说明")
	private String giftExplain;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplain;
	@Excel(name = "代理是否奖励；0：不奖励；1：奖励；", width = 15)
	@ApiModelProperty(value = "代理是否奖励；0：不奖励；1：奖励；")
	private String isAgencyAward;
	@Excel(name = "加盟商是否奖励；0：不奖励；1：奖励；", width = 15)
	@ApiModelProperty(value = "加盟商是否奖励；0：不奖励；1：奖励；")
	private String isAllianceAward;
}
