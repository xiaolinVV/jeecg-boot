package org.jeecg.modules.shop.sysBackSetting.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 后台设置
 * @Author: jeecg-boot
 * @Date:   2022-09-28
 * @Version: V1.0
 */
@Data
@TableName("sys_back_setting")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_back_setting对象", description="后台设置")
public class SysBackSetting implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
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
    private java.lang.String delFlag;
	/**系统名称*/
	@Excel(name = "系统名称", width = 15)
    @ApiModelProperty(value = "系统名称")
    private java.lang.String name;
	/**系统描述*/
	@Excel(name = "系统描述", width = 15)
    @ApiModelProperty(value = "系统描述")
    private java.lang.String description;
	/**版权信息*/
	@Excel(name = "版权信息", width = 15)
    @ApiModelProperty(value = "版权信息")
    private java.lang.String copyrightInfo;
	/**备案号*/
	@Excel(name = "备案号", width = 15)
    @ApiModelProperty(value = "备案号")
    private java.lang.String beian;
	/**备案地址*/
	@Excel(name = "备案地址", width = 15)
    @ApiModelProperty(value = "备案地址")
    private java.lang.String beianUrl;
	/**登录页logo*/
	@Excel(name = "登录页logo", width = 15)
    @ApiModelProperty(value = "登录页logo")
    private java.lang.String loginLogo;
	/**系统内页logo*/
	@Excel(name = "系统内页logo", width = 15)
    @ApiModelProperty(value = "系统内页logo")
    private java.lang.String systemRelatedLogo;
	/**标签logo*/
	@Excel(name = "标签logo", width = 15)
    @ApiModelProperty(value = "标签logo")
    private java.lang.String labelLogo;
}
