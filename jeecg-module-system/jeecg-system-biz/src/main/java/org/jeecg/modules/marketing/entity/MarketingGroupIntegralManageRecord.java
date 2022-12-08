package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * @Description: 拼购记录
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
@Data
@TableName("marketing_group_integral_manage_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_group_integral_manage_record对象", description="拼购记录")
public class MarketingGroupIntegralManageRecord {
    
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
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
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
	/**活动别名*/
	@Excel(name = "活动别名", width = 15)
    @ApiModelProperty(value = "活动别名")
	private String anotherName;
	/**主图*/
	@Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
	private String mainPicture;
	/**支付方式；0：产品券*/
	@Excel(name = "支付方式；0：产品券", width = 15)
    @ApiModelProperty(value = "支付方式；0：产品券")
	private String payment;
	/**支付金额*/
	@Excel(name = "支付金额", width = 15)
    @ApiModelProperty(value = "支付金额")
	private BigDecimal paymentAmount;
	/**拼购编号*/
	@Excel(name = "拼购编号", width = 15)
    @ApiModelProperty(value = "拼购编号")
	private String serialNumber;
	/**参与时间*/
	@Excel(name = "参与时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "参与时间")
	private java.util.Date participationTime;
	/**购买数量*/
	@Excel(name = "购买数量", width = 15)
    @ApiModelProperty(value = "购买数量")
	private BigDecimal purchaseQuantity;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**拼购身份；0：参与人；1：发起人*/
	@Excel(name = "拼购身份；0：参与人；1：发起人", width = 15)
    @ApiModelProperty(value = "拼购身份；0：参与人；1：发起人")
	private String identity;
	/**中奖状态；0：进行中；1：已中奖；1：未中奖*/
	@Excel(name = "中奖状态；0：进行中；1：已中奖；1：未中奖", width = 15)
    @ApiModelProperty(value = "中奖状态；0：进行中；1：已中奖；1：未中奖")
	private String winningState;
	/**奖励类型；0：产品券*/
	@Excel(name = "奖励类型；0：产品券", width = 15)
    @ApiModelProperty(value = "奖励类型；0：产品券")
	private String rewardType;
	/**奖励数量*/
	@Excel(name = "奖励数量", width = 15)
    @ApiModelProperty(value = "奖励数量")
	private BigDecimal missedRewards;
	/**拼购列表id*/
	@Excel(name = "拼购列表id", width = 15)
    @ApiModelProperty(value = "拼购列表id")
	private String marketingGroupIntegralManageListId;

	/**
	 * 是否分配奖励；0：未分配；1：已分配
	 */
	private String distributionRewards;

	/**
	 * 奖励数值
	 */
	private BigDecimal rewardNumerical;

	/**
	 * 拼购管理id
	 */
	private String marketingGroupIntegralManageId;

	/**
	 * 分级奖励
	 */
	private String classificationReward;

	@Version
	private Integer version;

}
