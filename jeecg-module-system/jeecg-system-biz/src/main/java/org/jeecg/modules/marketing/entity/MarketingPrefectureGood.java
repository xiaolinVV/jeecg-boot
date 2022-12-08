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
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
@Data
@TableName("marketing_prefecture_good")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_prefecture_good对象", description="专区商品")
public class MarketingPrefectureGood {
    
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
	/**平台专区id*/
	@Excel(name = "平台专区id", width = 15)
    @ApiModelProperty(value = "平台专区id")
	private String marketingPrefectureId;
	/**平台专区类型id*/
	@Excel(name = "平台专区类型id", width = 15)
    @ApiModelProperty(value = "平台专区类型id")
	private String marketingPrefectureTypeId;
	/**商品列表id*/
	@Excel(name = "商品列表id", width = 15)
    @ApiModelProperty(value = "商品列表id")
	private String goodListId;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplian;
	/**原商品是否可用；0：不可用；1：可用*/
	@Excel(name = "原商品是否可用；0：不可用；1：可用", width = 15)
    @ApiModelProperty(value = "原商品是否可用；0：不可用；1：可用")
	private String srcStatus;
	/**专区价*/
	@Excel(name = "专区价", width = 15)
    @ApiModelProperty(value = "专区价")
	private String prefecturePrice;
	/**福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣*/
	@Excel(name = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣", width = 15)
    @ApiModelProperty(value = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣")
	private String isWelfare;
	/**福利金限额抵扣比例*/
	@Excel(name = "福利金限额抵扣比例", width = 15)
    @ApiModelProperty(value = "福利金限额抵扣比例")
	private String welfareProportion;
	/**赠送福利金；0：不支持；1：支持*/
	@Excel(name = "赠送福利金；0：不支持；1：支持", width = 15)
    @ApiModelProperty(value = "赠送福利金；0：不支持；1：支持")
	private String isGiveWelfare;
	/**赠送福利金比例*/
	@Excel(name = "赠送福利金比例", width = 15)
    @ApiModelProperty(value = "赠送福利金比例")
	private String giveWelfareProportion;
	/**购买天数;-1：不限制；其他代表天数*/
	@Excel(name = "购买天数;-1：不限制；其他代表天数", width = 15)
    @ApiModelProperty(value = "购买天数;-1：不限制；其他代表天数")
	private String buyProportionDay;
	/**可购买件数；-1：不限制；其他代表件数*/
	@Excel(name = "可购买件数；-1：不限制；其他代表件数", width = 15)
    @ApiModelProperty(value = "可购买件数；-1：不限制；其他代表件数")
	private String buyProportionLetter;
	private BigDecimal smallPrefecturePrice;
	/**vip会员免福利金；0：不免；1：免*/
	@Excel(name = "vip会员免福利金；0：不免；1：免", width = 15)
	@ApiModelProperty(value = "vip会员免福利金；0：不免；1：免")
	private String isVipLower;


	/**
	 * 排序
	 */
	private BigDecimal sort;
}
