package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 称号入账资金
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
@Data
@TableName("marketing_recorded_money")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_recorded_money对象", description="称号入账资金")
public class MarketingRecordedMoney {
    
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
	private String delFlag;
	/**称号id*/
	@Excel(name = "称号id", width = 15)
    @ApiModelProperty(value = "称号id")
	private String memberDesignationId;
	/**交易单号*/
	@Excel(name = "交易单号", width = 15)
    @ApiModelProperty(value = "交易单号")
	private String tradeNo;
	/**交易类型；0：订单；1：礼包*/
	@Excel(name = "交易类型；0：订单；1：礼包", width = 15)
    @ApiModelProperty(value = "交易类型；0：订单；1：礼包")
	private String tradeType;
	/**分红资金比例*/
	@Excel(name = "分红资金比例", width = 15)
    @ApiModelProperty(value = "分红资金比例")
	private java.math.BigDecimal participation;
	/**分红资金*/
	@Excel(name = "分红资金", width = 15)
    @ApiModelProperty(value = "分红资金")
	private java.math.BigDecimal participationMoney;
	/**进账资金*/
	@Excel(name = "进账资金", width = 15)
    @ApiModelProperty(value = "进账资金")
	private java.math.BigDecimal recordedMoney;
	/**称号余额*/
	@Excel(name = "称号余额", width = 15)
    @ApiModelProperty(value = "称号余额")
	private java.math.BigDecimal balance;
	/**交易时间*/
	@Excel(name = "交易时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "交易时间")
	private Date tradeTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
	@ApiModelProperty(value = "流水号")
	private String orderNo;
}
