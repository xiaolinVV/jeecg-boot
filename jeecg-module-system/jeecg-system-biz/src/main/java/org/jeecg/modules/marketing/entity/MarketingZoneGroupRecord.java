package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 拼团记录
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
@Data
@TableName("marketing_zone_group_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_zone_group_record对象", description="拼团记录")
public class MarketingZoneGroupRecord {
    
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
	private String marketingZoneGroupManageId;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**拼团记录编号*/
	@Excel(name = "拼团记录编号", width = 15)
    @ApiModelProperty(value = "拼团记录编号")
	private String serialNumber;
	/**拼团时间*/
	@Excel(name = "拼团时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "拼团时间")
	private Date tuxedoTime;
	/**拼团商品id*/
	@Excel(name = "拼团商品id", width = 15)
    @ApiModelProperty(value = "拼团商品id")
	private String marketingZoneGroupGoodId;
	/**商品规格id*/
	@Excel(name = "商品规格id", width = 15)
    @ApiModelProperty(value = "商品规格id")
	private String goodSpecificationId;
	/**商品规格*/
	@Excel(name = "商品规格", width = 15)
    @ApiModelProperty(value = "商品规格")
	private String specification;
	/**商品编号*/
	@Excel(name = "商品编号", width = 15)
    @ApiModelProperty(value = "商品编号")
	private String goodNo;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private String goodName;
	/**主图*/
	@Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
	private String mainPicture;
	/**购买数量*/
	@Excel(name = "购买数量", width = 15)
    @ApiModelProperty(value = "购买数量")
	private java.math.BigDecimal quantity;
	/**支付金额*/
	@Excel(name = "支付金额", width = 15)
    @ApiModelProperty(value = "支付金额")
	private java.math.BigDecimal payPrice;
	/**拼团身份；0：参与人；1：发起人*/
	@Excel(name = "拼团身份；0：参与人；1：发起人", width = 15)
    @ApiModelProperty(value = "拼团身份；0：参与人；1：发起人")
	private String identity;
	/**状态；0：未中奖；1：已中奖；2：失败*/
	@Excel(name = "状态；0：未中奖；1：已中奖；2：失败", width = 15)
    @ApiModelProperty(value = "状态；0：未中奖；1：已中奖；2：失败")
	private String status;
	/**
	 * 支付记录id
	 */
	private String payZoneGroupLogId;

	/**
	 * 是否分配奖励；0：未分配；1：已分配
	 */
	private String distributionRewards;

	/**
	 * 分级奖励
	 */
	private String classificationReward;

	@Version
	private Integer version;
}
