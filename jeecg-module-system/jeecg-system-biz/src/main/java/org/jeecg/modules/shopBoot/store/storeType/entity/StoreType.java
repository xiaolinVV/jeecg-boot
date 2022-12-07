package org.jeecg.modules.shopBoot.store.storeType.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 店铺分类
 * @Author: jeecg-boot
 * @Date:   2022-12-07
 * @Version: V1.0
 */
@Data
@TableName("store_type")
@ApiModel(value="store_type对象", description="店铺分类")
public class StoreType implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private java.lang.String id;
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
    private java.lang.String typeName;
	/**分类级别*/
	@Excel(name = "分类级别", width = 15, dicCode = "store_type_level")
	@Dict(dicCode = "store_type_level")
    @ApiModelProperty(value = "分类级别")
    private java.math.BigDecimal level;
	/**分类图片*/
	@Excel(name = "分类图片", width = 15)
    @ApiModelProperty(value = "分类图片")
    private java.lang.String logoAddr;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    @TableField(value = "sort")
    private java.math.BigDecimal rank;
	/**福利金抵扣最低值*/
	@Excel(name = "福利金抵扣最低值", width = 15)
    @ApiModelProperty(value = "福利金抵扣最低值")
    private java.math.BigDecimal smallWelfarePayments;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15, dicCode = "store_type_status")
	@Dict(dicCode = "store_type_status")
    @ApiModelProperty(value = "状态；0：停用；1：启用")
    private java.lang.String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
    private java.lang.String closeExplain;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    @Dict(dictTable = "sys_user",dicCode = "username",dicText = "realname")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新者*/
    @ApiModelProperty(value = "更新者")
    @Dict(dictTable = "sys_user",dicCode = "username",dicText = "realname")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**创建年*/
	@Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private java.lang.Integer year;
	/**创建月*/
	@Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private java.lang.Integer month;
	/**创建日*/
	@Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private java.lang.Integer day;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private java.lang.String delFlag;
	/**父级分类id，0为一级*/
	@Excel(name = "父级分类id，0为一级", width = 15)
    @ApiModelProperty(value = "父级分类id，0为一级")
    private java.lang.String pid;
	/**是否有下一级；0：无；1：有*/
	@Excel(name = "是否有下一级；0：无；1：有", width = 15)
    @ApiModelProperty(value = "是否有下一级；0：无；1：有")
    private java.lang.String hasChild;
	/**停用时间*/
	@Excel(name = "停用时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "停用时间")
    private java.util.Date closeTime;
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
    @ApiModelProperty(value = "删除说明")
    private java.lang.String delExplain;
}
