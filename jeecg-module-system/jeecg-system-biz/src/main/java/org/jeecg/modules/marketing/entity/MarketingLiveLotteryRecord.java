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

import java.util.Date;

/**
 * @Description: 直播管理-中奖记录
 * @Author: jeecg-boot
 * @Date:   2021-09-15
 * @Version: V1.0
 */
@Data
@TableName("marketing_live_lottery_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_live_lottery_record对象", description="直播管理-中奖记录")
public class MarketingLiveLotteryRecord {
    
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
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**奖品单号*/
	@Excel(name = "奖品单号", width = 15)
    @ApiModelProperty(value = "奖品单号")
	private String lotteryNo;
	/**奖品id*/
	@Excel(name = "奖品id", width = 15)
    @ApiModelProperty(value = "奖品id")
	private String marketingLivePrizeId;
	/**奖品类型；0：礼品；1：优惠券*/
	@Excel(name = "奖品类型；0：礼品；1：优惠券", width = 15)
    @ApiModelProperty(value = "奖品类型；0：礼品；1：优惠券")
	private String prizeType;
	/**奖品名称*/
	@Excel(name = "奖品名称", width = 15)
    @ApiModelProperty(value = "奖品名称")
	private String prizeName;
	/**奖品图片*/
	@Excel(name = "奖品图片", width = 15)
    @ApiModelProperty(value = "奖品图片")
	private String prizeImage;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
	private java.math.BigDecimal quantity;
	/**获得时间*/
	@Excel(name = "获得时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "获得时间")
	private Date getTime;
	/**领取状态: 0:未领取 1:已领取*/
	@Excel(name = "领取状态: 0:未领取 1:已领取", width = 15)
    @ApiModelProperty(value = "领取状态: 0:未领取 1:已领取")
	private String drawStatus;
	/**领取时间*/
	@Excel(name = "领取时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "领取时间")
	private Date drawTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**累计时长*/
	@Excel(name = "累计时长", width = 15)
    @ApiModelProperty(value = "累计时长")
	private java.math.BigDecimal totalTimes;
	/**是否中奖: 0: 否; 1: 是*/
	@Excel(name = "是否中奖: 0: 否; 1: 是", width = 15)
    @ApiModelProperty(value = "是否中奖: 0: 否; 1: 是")
	private String isLottery;
	/**直播抽奖id*/
	@Excel(name = "直播抽奖id", width = 15)
    @ApiModelProperty(value = "直播抽奖id")
	private String marketingLiveLotteryId;
	/**收货人*/
	@Excel(name = "收货人", width = 15)
    @ApiModelProperty(value = "收货人")
	private String consignee;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
	private String contactNumber;
	/**收货地址*/
	@Excel(name = "收货地址", width = 15)
    @ApiModelProperty(value = "收货地址")
	private String shippingAddress;
	/**区域id*/
	@Excel(name = "区域id", width = 15)
    @ApiModelProperty(value = "区域id")
	private String sysAreaId;
	/**优惠券记录id*/
	@Excel(name = "优惠券记录id", width = 15)
	@ApiModelProperty(value = "优惠券记录id")
	private String marketingDiscountCouponId;
	/**直播间id*/
	@Excel(name = "直播间id", width = 15)
	@ApiModelProperty(value = "直播间id")
	private String marketingLiveStreamingId;
}
