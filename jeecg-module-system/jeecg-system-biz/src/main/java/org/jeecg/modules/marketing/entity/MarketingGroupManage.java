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
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Data
@TableName("marketing_group_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_group_manage对象", description="拼团管理")
public class MarketingGroupManage {
    
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
	/**团号*/
	@Excel(name = "团号", width = 15)
    @ApiModelProperty(value = "团号")
	private String groupNo;
	/**商品编号*/
	@Excel(name = "商品编号", width = 15)
    @ApiModelProperty(value = "商品编号")
	private String goodNo;
	/**商品主图相对地址（以json的形式存储多张）*/
	@Excel(name = "商品主图相对地址（以json的形式存储多张）", width = 15)
    @ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
	private String mainPicture;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private String goodName;
	/**拼团商品id*/
	@Excel(name = "拼团商品id", width = 15)
    @ApiModelProperty(value = "拼团商品id")
	private String marketingGroupGoodListId;
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
	/**活动开始时间*/
	@Excel(name = "活动开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动开始时间")
	private Date startTime;
	/**活动结束时间*/
	@Excel(name = "活动结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动结束时间")
	private Date endTime;
	/**成团人数*/
	@Excel(name = "成团人数", width = 15)
    @ApiModelProperty(value = "成团人数")
	private BigDecimal numberTuxedo;
	/**拼团状态；0：进行中；1：已成功；2：已失败*/
	@Excel(name = "拼团状态；0：进行中；1：已成功；2：已失败", width = 15)
    @ApiModelProperty(value = "拼团状态；0：进行中；1：已成功；2：已失败")
	private String status;
	/**建团时间*/
	@Excel(name = "建团时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "建团时间")
	private Date buildTime;
	/**成团时间*/
	@Excel(name = "成团时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "成团时间")
	private Date successTime;

	/**
	 * 返还比例
	 */
	private BigDecimal returnProportion;

	/**
	 * 参团人数
	 */
	private BigDecimal numberClusters;

	/**
	 * 超时时间
	 */
	private Date timeOutPeriod;

	/**
	 * 参团积分
	 */
	private BigDecimal tuxedoWelfarePayments;

}
