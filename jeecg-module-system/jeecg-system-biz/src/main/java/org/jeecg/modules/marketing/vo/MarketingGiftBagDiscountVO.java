package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 礼包优惠券券映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Data
public class MarketingGiftBagDiscountVO {

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
	/**礼包id*/
	@Excel(name = "礼包id", width = 15)
    @ApiModelProperty(value = "礼包id")
	private String marketingGiftBagId;
	/**优惠券id*/
	@Excel(name = "优惠券id", width = 15)
    @ApiModelProperty(value = "优惠券id")
	private String marketingDiscountId;
	@Excel(name = "发放数量", width = 15)
	@ApiModelProperty(value = "发放数量")
	private BigDecimal distributedAmount;
	@Excel(name = "有效期控制；0：连续有效期；1：相同有效期", width = 15)
	@ApiModelProperty(value = "有效期控制；0：连续有效期；1：相同有效期")
	private String validityType;
	/**优惠券名称*/
	@Excel(name = "优惠券名称", width = 15)
	@ApiModelProperty(value = "优惠券名称")
	private String name;
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
	/**
	 * 店铺商品id数组
	 */
	@Excel(name = "店铺商品id数组", width = 15)
	@ApiModelProperty(value = "店铺商品id数组")
	private String goodStoreListIds;
	@Excel(name = "投放渠道ids", width = 15)
	@ApiModelProperty(value = "投放渠道ids")
	private String channelIds;
	/**
	 * 投放渠道统计
	 */
	private String channelInfo;
	/**
	 * 投放渠道名称
	 */
	private String channelName;
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
	/**
	 * 优惠券已领取数量
	 */
	private String statusGet;
	/**
	 * 优惠券已使用数量
	 */
	private String statusUse;
	/**
	 * 使用门槛
	 */
	private String usingThreshold;

	/**
	 * 优惠内容
	 */
	private String preferentialContent;
	/**
	 * 用券时间
	 */
	private String usrTime;
	/**
	 * 券剩余发行量
	 */
	private String discountSurplus;
	/**
	 * 适用商品
	 */
	private String applyGood;
	/**
	 * 发行人
	 */
	private String issuer;
	/**
	 * 适用商品统计
	 */
	private String goodSkr;
	/**使用人限制；会员类型数据字典：member_type*/
	@Excel(name = "使用人限制；会员类型数据字典：member_type", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "使用人限制；会员类型数据字典：member_type")
	@Dict(dicCode = "member_type")
	private String userRestrict;
	/**发行人*/
	@Excel(name = "发行人", width = 15)
	@ApiModelProperty(value = "发行人")
	private String discountCreateBy;



}
