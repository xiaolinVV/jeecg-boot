package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MarketingGiftBagVO {
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
    private String delFlag;
    /**礼包名称*/
    @Excel(name = "礼包名称", width = 15)
    @ApiModelProperty(value = "礼包名称")
    private String giftName;
    /**礼包价格*/
    @Excel(name = "礼包价格", width = 15)
    @ApiModelProperty(value = "礼包价格")
    private BigDecimal price;
    /**福利金*/
    @Excel(name = "福利金", width = 15)
    @ApiModelProperty(value = "福利金")
    private BigDecimal welfarePayments;
    /**分销佣金*/
    @Excel(name = "分销佣金", width = 15)
    @ApiModelProperty(value = "分销佣金")
    private BigDecimal distributionCommission;
    /**vip特权；0：不赠送；1：赠送：*/
    @Excel(name = "vip特权；0：不赠送；1：赠送：", width = 15)
    @ApiModelProperty(value = "vip特权；0：不赠送；1：赠送：")
    private String vipPrivilege;
    /**会员类型，按照字典,逗号隔开*/
    @Excel(name = "会员类型，按照字典,逗号隔开", width = 15,dicCode = "member_type")
    @ApiModelProperty(value = "会员类型，按照字典,逗号隔开")
    @Dict(dicCode = "member_type")
    private String buyLimit;
    /**每人限制购买次数*/
    @Excel(name = "每人限制购买次数", width = 15)
    @ApiModelProperty(value = "每人限制购买次数")
    private BigDecimal limitTimes;
    /**礼包可见范围；0：指定门店；1：全平台*/
    @Excel(name = "礼包可见范围；0：指定门店；1：全平台", width = 15)
    @ApiModelProperty(value = "礼包可见范围；0：指定门店；1：全平台")
    private String viewScope;
    /**礼包说明*/
    @Excel(name = "礼包说明", width = 15)
    @ApiModelProperty(value = "礼包说明")
    private String giftExplain;
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
    /**状态；0：停用；1：启用*/
    @Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
    private String status;
    /**停用说明*/
    @Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
    private String closeExplain;
    @Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
    private String mainPicture;
    @Excel(name = "详情图", width = 15)
    @ApiModelProperty(value = "详情图")
    private String giftDeals;
    @Excel(name = "发行量", width = 15)
    @ApiModelProperty(value = "发行量")
    private BigDecimal repertory;
    /**分享图*/
    @Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
    private String coverPlan;
    /**海报图*/
    @Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
    private String posters;
    @Excel(name = "推广奖励,不设置的话就取字典promoter_reward作为默认值", width = 15)
    @ApiModelProperty(value = "推广奖励,不设置的话就取字典promoter_reward作为默认值")
    private BigDecimal promoterReward;
    @Excel(name = "归属店铺奖励,不设置的话就取字典ownership_shops_reward作为默认值", width = 15)
    @ApiModelProperty(value = "归属店铺奖励,不设置的话就取字典ownership_shops_reward作为默认值")
    private BigDecimal ownershipShopsReward;
    @Excel(name = "渠道店铺奖励,不设置的话就取字典channel_shops_reward作为默认值", width = 15)
    @ApiModelProperty(value = "渠道店铺奖励,不设置的话就取字典channel_shops_reward作为默认值")
    private BigDecimal channelShopsReward;
    @Excel(name = "代理是否奖励；0：不奖励；1：奖励；", width = 15)
    @ApiModelProperty(value = "代理是否奖励；0：不奖励；1：奖励；")
    private String isAgencyAward;
    @Excel(name = "加盟商是否奖励；0：不奖励；1：奖励；", width = 15)
    @ApiModelProperty(value = "加盟商是否奖励；0：不奖励；1：奖励；")
    private String isAllianceAward;
    @Excel(name = "二级推广奖励,不设置的话就取字典promoter_reward_two作为默认值", width = 15)
    @ApiModelProperty(value = "二级推广奖励,不设置的话就取字典promoter_reward_two作为默认值")
    private BigDecimal promoterRewardTwo;
    @Excel(name = "赠送vip等级id", width = 15)
    @ApiModelProperty(value = "赠送vip等级id")
    private String sendVipMemberGradeId;

    @Excel(name = "限制购买的会员等级，等级id逗号隔开", width = 15)
    @ApiModelProperty(value = "限制购买的会员等级，等级id逗号隔开")
    private String buyVipMemberGradeId;

    @Excel(name = "礼包分红", width = 15)
    @ApiModelProperty(value = "礼包分红")
    private BigDecimal participation;

    @Excel(name = "是否有前置礼包；0：无；1：有", width = 15)
    @ApiModelProperty(value = "是否有前置礼包；0：无；1：有")
    private String isPreposition;

    @Excel(name = "前置礼包的id", width = 15)
    @ApiModelProperty(value = "前置礼包的id")
    private String prepositionMarketingGiftBag;
    @Excel(name = "分销特权", width = 15)
    @ApiModelProperty(value = "分销特权：0：不送分销特权，1：送分销特权；若分销设置与礼包无关则不显示此项且不需要设置此项，若设置的是与礼包相关的配置则前端显示设置项，可设置为赠送或不赠送。对已具备分销特权的会员不产生影响")
    private String distributionPrivileges;

    @Excel(name = "团队特权", width = 15)
    @ApiModelProperty(value = "团队特权：0：不送团队特权，1：送团队特权；若binding_team_relationship_condition 字典设置与礼包无关则不显示此项且不需要设置此项，若设置的是与礼包相关的配置则前端显示设置项，可设置为赠送或不赠送。对已具备团队特权的会员不产生影响")
    private String teamPrivileges;

    @Excel(name = "分配方式", width = 15)
    @ApiModelProperty(value = "分配方式：0：按代理比例分配；1：按加盟商比例分配")
    private String modeDistribution;

    @Excel(name = "分钱模式", width = 15)
    @ApiModelProperty(value = "分钱模式；0：按照代理或者加盟商的奖励；1：自定义奖励")
    private String allocationModel;
    @Excel(name = "省代奖励", width = 15)
    @ApiModelProperty(value = "省代奖励")
    private BigDecimal proviceAward;
    @Excel(name = "市代奖励", width = 15)
    @ApiModelProperty(value = "市代奖励")
    private BigDecimal cityAward;
    @Excel(name = "区县代奖励", width = 15)
    @ApiModelProperty(value = "区县代奖励")
    private BigDecimal towmAward;
    /**
     * 优惠券ids
     */
    private String discountIds;
    /**
     * 门店ids
     */
    private String storeIds;
    /**
     * 兑换券ids
     */
    private String CertificateIds;
    /**
     * 礼包发行时间开始
     * */
    private String startTime_begin;
    /**
     * 礼包发行时间结束
     * */
    private String endTime_end;
    /**
     * 礼包创建时间开始
     * */
    private String createTime_begin;
    /**
     * 礼包创建时间结束
     * */
    private String createTime_end;
    @Excel(name = "礼包分红比例，动态获取称号列表的称号和礼包分红比例，以称号列表的礼包分红比例作为礼包中的称号分红比例默认值，每个礼包可单独控制。 若系统无称号，则礼包创建和编辑时不显示礼包分红和称号分红比例，且participation取默认值0.00，gift_bag_dividend_ratio为null", width = 15)
    @ApiModelProperty(value = "礼包分红比例，动态获取称号列表的称号和礼包分红比例，以称号列表的礼包分红比例作为礼包中的称号分红比例默认值，每个礼包可单独控制。 若系统无称号，则礼包创建和编辑时不显示礼包分红和称号分红比例，且participation取默认值0.00，gift_bag_dividend_ratio为null")
    private String giftBagDividendRatio;
    /**
     * 称号id
     */
    private String memberDesignationId;

    /**
     *礼品卡id
     */
    private String marketingStoreGiftCardListJson;

    /**
     * 支付方式：支付方式；-1：全部；0：微信；1：支付宝；2：余额支付
     */
    private String paymentMode;

    /**
     * 支付次数
     */
    private BigDecimal payTimes;
}
