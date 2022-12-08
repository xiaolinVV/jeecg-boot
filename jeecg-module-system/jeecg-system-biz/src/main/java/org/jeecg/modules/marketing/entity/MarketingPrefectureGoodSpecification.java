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
 * @Description: 专区商品规格
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
@Data
@TableName("marketing_prefecture_good_specification")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_prefecture_good_specification对象", description="专区商品规格")
public class MarketingPrefectureGoodSpecification {
    
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
	/**专区商品id*/
	@Excel(name = "专区商品id", width = 15)
    @ApiModelProperty(value = "专区商品id")
	private String marketingPrefectureGoodId;
	/**商品规格id*/
	@Excel(name = "商品规格id", width = 15)
    @ApiModelProperty(value = "商品规格id")
	private String goodSpecificationId;
	/**专区价*/
	@Excel(name = "专区价", width = 15)
    @ApiModelProperty(value = "专区价")
	private BigDecimal prefecturePrice;
	/**专区折扣*/
	@Excel(name = "专区折扣", width = 15)
    @ApiModelProperty(value = "专区折扣")
	private BigDecimal prefecturePriceProportion;
	/**福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣*/
	@Excel(name = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣", width = 15)
    @ApiModelProperty(value = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣")
	private String isWelfare;
	/**福利金限额抵扣比例*/
	@Excel(name = "福利金限额抵扣比例", width = 15)
    @ApiModelProperty(value = "福利金限额抵扣比例")
	private BigDecimal welfareProportion;
	/**赠送福利金；0：不支持；1：支持*/
	@Excel(name = "赠送福利金；0：不支持；1：支持", width = 15)
    @ApiModelProperty(value = "赠送福利金；0：不支持；1：支持")
	private String isGiveWelfare;
	/**赠送福利金比例*/
	@Excel(name = "赠送福利金比例", width = 15)
    @ApiModelProperty(value = "赠送福利金比例")
	private BigDecimal giveWelfareProportion;
	/**购买天数;-1：不限制；其他代表天数*/
	@Excel(name = "购买天数;-1：不限制；其他代表天数", width = 15)
    @ApiModelProperty(value = "购买天数;-1：不限制；其他代表天数")
	private BigDecimal buyProportionDay;
	/**可购买件数；-1：不限制；其他代表件数*/
	@Excel(name = "可购买件数；-1：不限制；其他代表件数", width = 15)
    @ApiModelProperty(value = "可购买件数；-1：不限制；其他代表件数")
	private BigDecimal buyProportionLetter;
	/**vip会员免福利金；0：不免；1：免*/
	@Excel(name = "vip会员免福利金；0：不免；1：免", width = 15)
	@ApiModelProperty(value = "vip会员免福利金；0：不免；1：免")
	private String isVipLower;

	/**
	 * 积分比例
	 */
	private BigDecimal proportionIntegral;
}
