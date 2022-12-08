package org.jeecg.modules.system.entity;

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
 * @Description: 菜单配置表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
@Data
@TableName("sys_member_menu")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_member_menu对象", description="菜单配置表")
public class SysMemberMenu {
    
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
	/**菜单名称*/
	@Excel(name = "菜单名称", width = 15)
    @ApiModelProperty(value = "菜单名称")
	private String menuName;
	/**英文名称*/
	@Excel(name = "英文名称", width = 15)
    @ApiModelProperty(value = "英文名称")
	private String englishName;
	/**菜单类型；0：非必须；1：必须*/
	@Excel(name = "菜单类型；0：非必须；1：必须", width = 15)
    @ApiModelProperty(value = "菜单类型；0：非必须；1：必须")
	private String menuType;
	/**图标*/
	@Excel(name = "图标", width = 15)
    @ApiModelProperty(value = "图标")
	private String menuLogo;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**状态;0:停用；1：启用*/
	@Excel(name = "状态;0:停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态;0:停用；1：启用")
	private String status;
	/**停用时间*/
	@Excel(name = "停用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "停用时间")
	private Date closeTime;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplian;

	/**选中图标*/
	@Excel(name = "选中图标", width = 15)
	@ApiModelProperty(value = "选中图标")
	private String menuCurrentLogo;

	/**
	 * 审核隐藏；0：隐藏，1：显示
	 */
	private String reviewHidden;

}
