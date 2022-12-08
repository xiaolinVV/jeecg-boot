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
 * @Description: 兑换券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_certificate_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_certificate_record对象", description="兑换券记录")
public class MarketingCertificateRecord {
    
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
	/**兑换券id*/
	@Excel(name = "兑换券id", width = 15)
    @ApiModelProperty(value = "兑换券id")
	private String marketingCertificateId;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**核销店铺id，没有核销为空*/
	@Excel(name = "核销店铺id，没有核销为空", width = 15)
    @ApiModelProperty(value = "核销店铺id，没有核销为空")
	private String sysUserId;
	/**兑换券名称*/
	@Excel(name = "兑换券名称", width = 15)
    @ApiModelProperty(value = "兑换券名称")
	private String name;
	/**发行人；0：平台；1：店铺*/
	@Excel(name = "发行人；0：平台；1：店铺", width = 15)
    @ApiModelProperty(value = "发行人；0：平台；1：店铺")
	private String isPlatform;
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
	/**获取渠道*/
	@Excel(name = "获取渠道", width = 15)
    @ApiModelProperty(value = "获取渠道")
	private String theChannel;
	/**核销时间*/
	@Excel(name = "核销时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "核销时间")
	private Date userTime;
	/**核销人；0：平台；1：店铺*/
	@Excel(name = "核销人；0：平台；1：店铺", width = 15)
    @ApiModelProperty(value = "核销人；0：平台；1：店铺")
	private String verificationPeople;
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
	/**核销奖励*/
	@Excel(name = "核销奖励", width = 15)
	@ApiModelProperty(value = "核销奖励")
	private BigDecimal theReward;
	private String marketingChannelId;

	@Excel(name = "赠送人的会员id,没有就为空", width = 15)
	@ApiModelProperty(value = "赠送人的会员id,没有就为空")
	private String giveMemberListId;

	/**送出时间*/
	@Excel(name = "送出时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "送出时间")
	private Date sendTime;
	/**主图*/
	@Excel(name = "主图", width = 15)
	@ApiModelProperty(value = "主图")
	private String  mainPicture;
	/**核销门店: 0: 全平台 1:指定门店*/
	@Excel(name = "核销门店: 0: 全平台 1:指定门店", width = 15)
	@ApiModelProperty(value = "核销门店: 0: 全平台 1:指定门店")
	private String  rewardStore;
	/**券类型: 0:活动券 1:付费券*/
	@Excel(name = "券类型: 0:活动券 1:付费券", width = 15)
	@ApiModelProperty(value = "券类型: 0:活动券 1:付费券")
	private String  isNomal;
	/**市场价*/
	@Excel(name = "市场价", width = 15)
	@ApiModelProperty(value = "市场价")
	private BigDecimal marketPrice;
	/**销售价*/
	@Excel(name = "销售价", width = 15)
	@ApiModelProperty(value = "销售价")
	private BigDecimal  price;
	/**成本价*/
	@Excel(name = "成本价", width = 15)
	@ApiModelProperty(value = "成本价")
	private BigDecimal  costPrice;
	/**仅销售店铺可核销（有记录到销售店铺则该券仅销售店铺可核销）*/
	@Excel(name = "仅销售店铺可核销（有记录到销售店铺则该券仅销售店铺可核销）", width = 15)
	@ApiModelProperty(value = "仅销售店铺可核销（有记录到销售店铺则该券仅销售店铺可核销）")
	private String  sellRewardStore;
	/**同一家店同一天仅能核销1次*/
	@Excel(name = "同一家店同一天仅能核销1次", width = 15)
	@ApiModelProperty(value = "同一家店同一天仅能核销1次")
	private String  rewardDayOne;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
	@ApiModelProperty(value = "封面图")
	private String  coverPlan;
	/**
	 * 二维码地址
	 */
	@Excel(name = "二维码地址", width = 15)
	@ApiModelProperty(value = "二维码地址")
	private String qrAddr;

	/**海报图*/
	@Excel(name = "海报图", width = 15)
	@ApiModelProperty(value = "海报图")
	private String posters;

	/**经度*/
	@Excel(name = "经度", width = 15)
	@ApiModelProperty(value = "经度")
	private BigDecimal longitude;

	/**纬度*/
	@Excel(name = "纬度", width = 15)
	@ApiModelProperty(value = "纬度")
	private BigDecimal latitude;
	/**
	 * 购买来源
	 */
	@Excel(name = "0：店铺；1：平台", width = 15)
	@ApiModelProperty(value = "0：店铺；1：平台")
	private String isBuyPlatform;

	/**
	 * 兑换方式；0：全部兑换；1：任选一个
	 */
	private String certificateType;
	@Excel(name = "活动价", width = 15)
	@ApiModelProperty(value = "活动价")
	private BigDecimal activePrice;
	@Excel(name = "券类型；0：普通；1：秒杀券；2：拼好券", width = 15)
	@ApiModelProperty(value = "券类型；0：普通；1：秒杀券；2：拼好券")
	private String recordType;
	/**券支付记录表id*/
	@Excel(name = "券支付记录表id", width = 15)
	@ApiModelProperty(value = "券支付记录表id")
	private String payCertificateLogId;
}
