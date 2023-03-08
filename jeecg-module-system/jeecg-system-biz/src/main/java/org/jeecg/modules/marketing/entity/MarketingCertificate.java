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
 * @Description: 兑换券
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_certificate")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_certificate对象", description="兑换券")
public class MarketingCertificate {

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
	/**优惠券名称*/
	@Excel(name = "优惠券名称", width = 15)
    @ApiModelProperty(value = "优惠券名称")
	private String name;
	/**用券时间方式；0：标准方式；1：领券当日起；2：领券次日起*/
	@Excel(name = "用券时间方式；0：标准方式；1：领券当日起；2：领券次日起", width = 15)
    @ApiModelProperty(value = "用券时间方式；0：标准方式；1：领券当日起；2：领券次日起")
	private String vouchersWay;
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
	/**多少天、周、月*/
	@Excel(name = "多少天、周、月", width = 15)
    @ApiModelProperty(value = "多少天、周、月")
	private BigDecimal disData;
	/**单位；天、周、月*/
	@Excel(name = "单位；天、周、月", width = 15)
    @ApiModelProperty(value = "单位；天、周、月")
	private String monad;
	/**商品数量*/
	@Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
	private BigDecimal shopTotal;
	/**发放总量*/
	@Excel(name = "发放总量", width = 15)
    @ApiModelProperty(value = "发放总量")
	private BigDecimal total;
	/**使用人限制；会员类型数据字典*/
	@Excel(name = "使用人限制；会员类型数据字典", width = 15,dicCode = "member_type")
    @ApiModelProperty(value = "使用人限制；会员类型数据字典")
	@Dict(dicCode = "member_type")
	private String userRestrict;
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
	/**已发放数量*/
	@Excel(name = "已发放数量", width = 15)
    @ApiModelProperty(value = "已发放数量")
	private BigDecimal releasedQuantity;
	/**状态；0:停用；1：启用*/
	@Excel(name = "状态；0:停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0:停用；1：启用")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String stopExplain;
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
    @ApiModelProperty(value = "删除说明")
	private String delExplain;
	/**核销奖励*/
	@Excel(name = "核销奖励", width = 15)
    @ApiModelProperty(value = "核销奖励")
	private BigDecimal theReward;
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
	/**分享图*/
	@Excel(name = "分享图", width = 15)
	@ApiModelProperty(value = "分享图")
	private String coverPlan;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
	@ApiModelProperty(value = "海报图")
	private String posters;
	/**排序*/
	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
	private BigDecimal sort;
	@Excel(name = "推广佣金", width = 15)
	@ApiModelProperty(value = "推广佣金")
	private BigDecimal promoteCommission;

	/**
	 * 兑换方式；0：全部兑换；1：任选一个
	 */
	private String certificateType;
	@Excel(name = "线上使用: 0 关闭 1开启", width = 15)
	@ApiModelProperty(value = "线上使用: 0 关闭 1开启")
	private Integer aboveUse;

	@Excel(name = "线下使用: 0 关闭 1开启", width = 15)
	@ApiModelProperty(value = "线下使用: 0 关闭 1开启")
	private Integer belowUse;
}
