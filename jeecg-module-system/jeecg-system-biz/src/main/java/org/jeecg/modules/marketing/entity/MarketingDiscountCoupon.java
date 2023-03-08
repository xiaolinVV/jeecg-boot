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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 优惠券记录
 * @Author: jeecg-boot
 * @Date:   2019-12-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_discount_coupon")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_discount_coupon对象", description="优惠券记录")
public class MarketingDiscountCoupon {
    
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
	/**优惠金额*/
	@Excel(name = "优惠金额", width = 15)
    @ApiModelProperty(value = "优惠金额")
	private BigDecimal price;
	/**优惠券名称*/
	@Excel(name = "优惠券名称", width = 15)
    @ApiModelProperty(value = "优惠券名称")
	private String name;
	/**有无门槛；0：无；1：有*/
	@Excel(name = "有无门槛；0：无；1：有", width = 15)
    @ApiModelProperty(value = "有无门槛；0：无；1：有")
	private String isThreshold;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**店铺id*/
	@Excel(name = "店铺id", width = 15)
    @ApiModelProperty(value = "店铺id")
	private String sysUserId;
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
	/*优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；*/
	@Excel(name = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；", width = 15,dicCode = "vouchers_status")
    @ApiModelProperty(value = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；")
	@Dict(dicCode = "vouchers_status")
	private String status;
	/**券号*/
	@Excel(name = "券号", width = 15)
    @ApiModelProperty(value = "券号")
	private String qqzixuangu;
	/**领取渠道名称*/
	@Excel(name = "领取渠道名称", width = 15)
    @ApiModelProperty(value = "领取渠道名称")
	private String theChannel;
	/**平台渠道id*/
	@Excel(name = "平台渠道id", width = 15)
    @ApiModelProperty(value = "平台渠道id")
	private String marketingChannelId;
	/**使用时间*/
	@Excel(name = "使用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用时间")
	private Date userTime;
	/**实际抵扣金额（核销默认全抵）*/
	@Excel(name = "实际抵扣金额（核销默认全抵）", width = 15)
    @ApiModelProperty(value = "实际抵扣金额（核销默认全抵）")
	private BigDecimal practicalDeduction;
	/**优惠券的id*/
	@Excel(name = "优惠券的id", width = 15)
    @ApiModelProperty(value = "优惠券的id")
	private String marketingDiscountId;
	/**0:店铺；1：平台*/
	@Excel(name = "0:店铺；1：平台", width = 15)
    @ApiModelProperty(value = "0:店铺；1：平台")
	private String isPlatform;
	/**满多少钱*/
	@Excel(name = "满多少钱", width = 15)
    @ApiModelProperty(value = "满多少钱")
	private BigDecimal completely;
	@Excel(name = "赠送人的会员id,没有就为空", width = 15)
	@ApiModelProperty(value = "赠送人的会员id,没有就为空")
	private String giveMemberListId;
	@Excel(name = "0:购买使用；1：核销使用", width = 15)
	@ApiModelProperty(value = "0:购买使用；1：核销使用")
	private String verification;

	/**赠送设置；0：不支持；1：支持*/
	@Excel(name = "赠送设置；0：不支持；1：支持", width = 15)
	@ApiModelProperty(value = "赠送设置；0：不支持；1：支持")
	private String isGive;
	/**是否过期提醒；0：不提醒；1：提醒*/
	@Excel(name = "是否过期提醒；0：不提醒；1：提醒", width = 15)
	@ApiModelProperty(value = "是否过期提醒；0：不提醒；1：提醒")
	private String isWarn;
	/**过期前多少天提醒*/
	@Excel(name = "过期前多少天提醒", width = 15)
	@ApiModelProperty(value = "过期前多少天提醒")
	private BigDecimal warnDays;
	/**券说明*/
	@Excel(name = "券说明", width = 15)
	@ApiModelProperty(value = "券说明")
	private String discountExplain;
	@Excel(name = "使用人限制；会员类型数据字典：member_type", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "使用人限制；会员类型数据字典：member_type")
	@Dict(dicCode = "member_type")
	private String userRestrict;

	/**送出时间*/
	@Excel(name = "送出时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "送出时间")
	private Date sendTime;

	@Excel(name = "券类型；0：常规；1：活动", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "券类型；0：常规；1：活动")
	private String isNomal;
	/**主图*/
	@Excel(name = "主图", width = 15)
	@ApiModelProperty(value = "主图")
	private String  mainPicture;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
	@ApiModelProperty(value = "封面图")
	private String  coverPlan;
	/**
	 * 二维码地址
	 */
	private String qrAddr;

	/**海报图*/
	@Excel(name = "海报图", width = 15)
	@ApiModelProperty(value = "海报图")
	private String posters;

	@Excel(name = "核销人id", width = 15)
	@ApiModelProperty(value = "核销人id")
	private String cancelAfterVerificationId;

	/**是否唯一；0：否；1：是*/
	@Excel(name = "是否唯一；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否唯一；0：否；1：是")
	private String isUniqueness;
	/**是否分销；0：否；1：是*/
	@Excel(name = "是否分销；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否分销；0：否；1：是")
	private String isDistribution;
	@Excel(name = "核销人类型；0：店铺；1：平台", width = 15)
	@ApiModelProperty(value = "核销人类型；0：店铺；1：平台")
	private String verificationType;
	@Excel(name = "领用限制；会员类型数据字典：member_type", width = 15)
	@ApiModelProperty(value = "领用限制；会员类型数据字典：member_type")
	private String getRestrict;

	/**折扣上限金额，最多可以参与折扣的金额上限（用于折扣券） */
	@Excel(name = "折扣上限金额，最多可以参与折扣的金额上限（用于折扣券） ", width = 15)
	@ApiModelProperty(value = "折扣上限金额，最多可以参与折扣的金额上限（用于折扣券）")
	private BigDecimal discountLimitAmount;
	/**优惠折扣百分比：指参与折扣的力度（用于折扣券） */
	@Excel(name = "优惠折扣百分比：指参与折扣的力度（用于折扣券）", width = 15)
	@ApiModelProperty(value = "优惠折扣百分比：指参与折扣的力度（用于折扣券）")
	private BigDecimal discountPercent;

	/**折扣券已使用金额 */
	@Excel(name = "折扣券已使用金额", width = 15)
	@ApiModelProperty(value = "折扣券已使用金额")
	private BigDecimal discountUseAmount;
	

}
