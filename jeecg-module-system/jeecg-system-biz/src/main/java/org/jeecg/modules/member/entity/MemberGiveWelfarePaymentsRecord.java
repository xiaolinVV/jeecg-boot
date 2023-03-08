package org.jeecg.modules.member.entity;

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

import java.util.Date;

/**
 * @Description: 福利金可获赠数量记录
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Data
@TableName("member_give_welfare_payments_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_give_welfare_payments_record对象", description="福利金可获赠数量记录")
public class MemberGiveWelfarePaymentsRecord {
    
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
	/**可获赠福利金id*/
	@Excel(name = "可获赠福利金id", width = 15)
    @ApiModelProperty(value = "可获赠福利金id")
	private String memberGiveWelfarePaymentsId;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
	private String serialNumber;
	/**交易类型*/
	@Excel(name = "交易类型", width = 15,dicCode = "welfare_given_deal_type")
    @ApiModelProperty(value = "交易类型")
	@Dict(dicCode = "welfare_given_deal_type")
	private String tradeType;
	/**进出账；0：入账；1：出账*/
	@Excel(name = "进出账；0：入账；1：出账", width = 15)
    @ApiModelProperty(value = "进出账；0：入账；1：出账")
	private String goAndCome;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
    @ApiModelProperty(value = "交易金额")
	private java.math.BigDecimal welfarePayments;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
    @ApiModelProperty(value = "总金额")
	private java.math.BigDecimal totalWelfarePayments;
	/**交易时间*/
	@Excel(name = "交易时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "交易时间")
	private Date payTime;
	/**交易单号*/
	@Excel(name = "交易单号", width = 15)
    @ApiModelProperty(value = "交易单号")
	private String tradeNo;
}
