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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 参团记录
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Data
@TableName("marketing_group_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_group_record对象", description="参团记录")
public class MarketingGroupRecord {
    
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
	/**拼团管理id*/
	@Excel(name = "拼团管理id", width = 15)
    @ApiModelProperty(value = "拼团管理id")
	private String marketingGroupManageId;
	/**参团编号*/
	@Excel(name = "参团编号", width = 15)
    @ApiModelProperty(value = "参团编号")
	private String groupRecordNo;
	/**参团时间*/
	@Excel(name = "参团时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "参团时间")
	private Date tuxedoTime;
	/**拼团商品规格*/
	@Excel(name = "拼团商品规格", width = 15)
    @ApiModelProperty(value = "拼团商品规格")
	private String marketingGroupGoodSpecificationId;
	/**规格名称，按照顺序逗号隔开*/
	@Excel(name = "规格名称，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "规格名称，按照顺序逗号隔开")
	private String specification;
	/**市场价*/
	@Excel(name = "市场价", width = 15)
    @ApiModelProperty(value = "市场价")
	private BigDecimal marketPrice;
	/**销售价*/
	@Excel(name = "销售价", width = 15)
    @ApiModelProperty(value = "销售价")
	private BigDecimal smallPrice;
	/**活动价*/
	@Excel(name = "活动价", width = 15)
    @ApiModelProperty(value = "活动价")
	private BigDecimal activityPrice;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**商品数量*/
	@Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
	private BigDecimal quantity;
	/**状态；0：未使用；1：已使用*/
	@Excel(name = "状态；0：未使用；1：已使用", width = 15)
    @ApiModelProperty(value = "状态；0：未使用；1：已使用")
	private String status;
	/**使用时间*/
	@Excel(name = "使用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用时间")
	private Date userTime;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
	private String orderListId;
	/**
	 * 拼团身份；0：参与人；1：发起人
	 */
	private String identityStatus;
	/**
	 * 中奖类型；0：积分；1：购买资格
	 */
	private String rewardType;
	/**
	 * 中奖状态；0：未中奖；1：已中奖
	 */
	private String rewardStatus;

	/**
	 * 中奖数量
	 */
	private BigDecimal rewardNumber;

	/**
	 * 期限
	 */
	private BigDecimal deadline;
	/**
	 * 购买开始时间
	 */
	private Date buyStartTime;
	/**
	 * 购买结束时间
	 */
	private Date buyEndTime;
	/**
	 * 商品编号
	 */
	private String goodNo;

	/**
	 * 商品主图相对地址（以json的形式存储多张）
	 */
	private String mainPicture;

	/**
	 * 商品名称
	 */
	private String goodName;
	/**
	 * 拼团商品id
	 */
	private String marketingGroupGoodListId;

	/**
	 * 参团积分
	 */
	private BigDecimal tuxedoWelfarePayments;
}
