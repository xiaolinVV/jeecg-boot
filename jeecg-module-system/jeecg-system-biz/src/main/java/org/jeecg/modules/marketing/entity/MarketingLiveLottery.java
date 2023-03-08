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
 * @Description: 直播管理-直播抽奖
 * @Author: jeecg-boot
 * @Date:   2021-09-15
 * @Version: V1.0
 */
@Data
@TableName("marketing_live_lottery")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_live_lottery对象", description="直播管理-直播抽奖")
public class MarketingLiveLottery {
    
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
	/**直播间id*/
	@Excel(name = "直播间id", width = 15)
    @ApiModelProperty(value = "直播间id")
	private String marketingLiveStreamingId;
	/**轮次名称*/
	@Excel(name = "轮次名称", width = 15)
    @ApiModelProperty(value = "轮次名称")
	private String lotteryName;
	/**抽奖编号*/
	@Excel(name = "抽奖编号", width = 15)
    @ApiModelProperty(value = "抽奖编号")
	private String streamNumber;
	/**抽奖轮次*/
	@Excel(name = "抽奖轮次", width = 15)
    @ApiModelProperty(value = "抽奖轮次")
	private java.math.BigDecimal lotteryNumber;
	/**开奖时间*/
	@Excel(name = "开奖时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开奖时间")
	private Date lotteryTime;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：未开奖；1：已开奖", width = 15)
    @ApiModelProperty(value = "状态；0：未开奖；1：已开奖")
	private String status;
	/**取消轮次: 0:未取消 1:已取消*/
	@Excel(name = "取消轮次: 0:未取消 1:已取消", width = 15)
    @ApiModelProperty(value = "取消轮次: 0:未取消 1:已取消")
	private String cancelNumber;
	/**抽奖资格 单位:分钟*/
	@Excel(name = "抽奖资格 单位:分钟", width = 15)
    @ApiModelProperty(value = "抽奖资格 单位:分钟")
	private java.math.BigDecimal lotteryQualification;
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
	@ApiModelProperty(value = "删除说明")
	private String delExplain;
	private String lotteryPrizeType;
	private String lotteryPrizeId;
	private java.math.BigDecimal lotteryPrizeQuantity;
	private java.math.BigDecimal lotteryPrizeTotal;
	private String losingLotteryPrizeType;
	private String losingLotteryPrizeId;
	private java.math.BigDecimal losingLotteryPrizeQuantity;
	private String losingLotteryPrizeTotal;
	@Version
	private Integer version;
}
