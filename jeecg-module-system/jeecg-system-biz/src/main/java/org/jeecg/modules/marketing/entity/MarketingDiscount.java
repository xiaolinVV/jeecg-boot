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
 * @Description: 优惠券
 * @Author: jeecg-boot
 * @Date:   2019-12-06
 * @Version: V1.0
 */
@Data
@TableName("marketing_discount")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_discount对象", description="优惠券")
public class MarketingDiscount {
    
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
	/**店铺id*/
	@Excel(name = "店铺id", width = 15)
    @ApiModelProperty(value = "店铺id")
	private String sysUserId;
	/**有无门槛；0：无；1：有*/
	@Excel(name = "有无门槛；0：无；1：有", width = 15)
    @ApiModelProperty(value = "有无门槛；0：无；1：有")
	private String isThreshold;
	/**满多少钱*/
	@Excel(name = "满多少钱", width = 15)
    @ApiModelProperty(value = "满多少钱")
	private BigDecimal completely;
	/**减多少钱*/
	@Excel(name = "减多少钱", width = 15)
    @ApiModelProperty(value = "减多少钱")
	private BigDecimal subtract;
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
	/**使用人限制；会员类型数据字典：member_type*/
	@Excel(name = "使用人限制；会员类型数据字典：member_type", width = 15,dicCode = "member_type")
    @ApiModelProperty(value = "使用人限制；会员类型数据字典：member_type")
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
	/**无券可再领取；0:不支持；1：支持*/
	@Excel(name = "无券可再领取；0:不支持；1：支持", width = 15)
    @ApiModelProperty(value = "无券可再领取；0:不支持；1：支持")
	private String isGetThe;
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
	/**再次领取条件；0：已送出；1：已使用；已过期；逗号隔开*/
	@Excel(name = "再次领取条件；0：已送出；1：已使用；已过期；逗号隔开", width = 15,dicCode = "again_get")
    @ApiModelProperty(value = "再次领取条件；0：已送出；1：已使用；已过期；逗号隔开")
	@Dict(dicCode = "again_get")
	private String againGet;
	/**0:店铺；1：平台*/
	@Excel(name = "0:店铺；1：平台", width = 15)
    @ApiModelProperty(value = "0:店铺；1：平台")
	private String isPlatform;
	/**
	 * 领用限制；会员类型数据字典：member_type
	 */
	@Excel(name = "领取人限制", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "领取人限制")
	@Dict(dicCode = "member_type")
	private String getRestrict;

	@Excel(name = "券类型；0：常规；1：活动 2：折扣", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "券类型；0：常规；1：活动 2：折扣")
	private String isNomal;
	/**主图*/
	@Excel(name = "主图", width = 15)
	@ApiModelProperty(value = "主图")
	private String  mainPicture;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
	@ApiModelProperty(value = "分享图")
	private String coverPlan;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
	@ApiModelProperty(value = "海报图")
	private String posters;
	/**是否唯一；0：否；1：是*/
	@Excel(name = "是否唯一；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否唯一；0：否；1：是")
	private String isUniqueness;
	/**是否分销；0：否；1：是*/
	@Excel(name = "是否分销；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否分销；0：否；1：是")
	private String isDistribution;
	/**排序，数字越小前端排序越靠前 */
	@Excel(name = "排序，数字越小前端排序越靠前 ", width = 15)
	@ApiModelProperty(value = "排序，数字越小前端排序越靠前 ")
	private BigDecimal sort;

	/**折扣上限金额，最多可以参与折扣的金额上限（用于折扣券） */
	@Excel(name = "折扣上限金额，最多可以参与折扣的金额上限（用于折扣券） ", width = 15)
	@ApiModelProperty(value = "折扣上限金额，最多可以参与折扣的金额上限（用于折扣券）")
	private BigDecimal discountLimitAmount;
	/**优惠折扣百分比：指参与折扣的力度（用于折扣券） */
	@Excel(name = "优惠折扣百分比：指参与折扣的力度（用于折扣券）", width = 15)
	@ApiModelProperty(value = "优惠折扣百分比：指参与折扣的力度（用于折扣券）")
	private BigDecimal discountPercent;
}
