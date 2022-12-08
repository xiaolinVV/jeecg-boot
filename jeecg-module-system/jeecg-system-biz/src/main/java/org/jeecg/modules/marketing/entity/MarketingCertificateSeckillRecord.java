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
 * @Description: 购买记录
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Data
@TableName("marketing_certificate_seckill_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_certificate_seckill_record对象", description="购买记录")
public class MarketingCertificateSeckillRecord {
    
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
	/**活动列表id*/
	@Excel(name = "活动列表id", width = 15)
    @ApiModelProperty(value = "活动列表id")
	private String marketingCertificateSeckillActivityListId;
	/**兑换券id*/
	@Excel(name = "兑换券id", width = 15)
    @ApiModelProperty(value = "兑换券id")
	private String marketingCertificateId;
	/**券名称*/
	@Excel(name = "券名称", width = 15)
    @ApiModelProperty(value = "券名称")
	private String certificateName;
	/**主图*/
	@Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
	private String mainPicture;
	/**市场价*/
	@Excel(name = "市场价", width = 15)
    @ApiModelProperty(value = "市场价")
	private java.math.BigDecimal marketPrice;
	/**销售价*/
	@Excel(name = "销售价", width = 15)
    @ApiModelProperty(value = "销售价")
	private java.math.BigDecimal price;
	/**成本价*/
	@Excel(name = "成本价", width = 15)
    @ApiModelProperty(value = "成本价")
	private java.math.BigDecimal costPrice;
	/**活动价*/
	@Excel(name = "活动价", width = 15)
    @ApiModelProperty(value = "活动价")
	private java.math.BigDecimal activityPrice;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**购买数量*/
	@Excel(name = "购买数量", width = 15)
    @ApiModelProperty(value = "购买数量")
	private java.math.BigDecimal quantity;
	/**购买时间*/
	@Excel(name = "购买时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "购买时间")
	private Date buyTime;
    /**券支付记录表id*/
    @Excel(name = "券支付记录表id", width = 15)
    @ApiModelProperty(value = "券支付记录表id")
    private String payCertificateLogId;
	/**限时抢券列表id*/
	@Excel(name = "限时抢券列表id", width = 15)
	@ApiModelProperty(value = "限时抢券列表id")
	private String marketingCertificateSeckillListId;
	/**编号 */
	@Excel(name = "编号 ", width = 15)
	@ApiModelProperty(value = "编号 ")
	private String serialNumber;
}
