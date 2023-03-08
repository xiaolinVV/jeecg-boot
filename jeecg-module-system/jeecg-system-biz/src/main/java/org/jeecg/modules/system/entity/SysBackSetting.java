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
 * @Description: 后台设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Data
@TableName("sys_back_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_back_setting对象", description="后台设置")
public class SysBackSetting {
    
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
	/**系统名称*/
	@Excel(name = "系统名称", width = 15)
    @ApiModelProperty(value = "系统名称")
	private String name;
	/**系统描述*/
	@Excel(name = "系统描述", width = 15)
    @ApiModelProperty(value = "系统描述")
	private String description;
	/**版权信息*/
	@Excel(name = "版权信息", width = 15)
    @ApiModelProperty(value = "版权信息")
	private String copyrightInfo;
	/**登录页logo地址*/
	@Excel(name = "登录页logo地址", width = 15)
    @ApiModelProperty(value = "登录页logo地址")
	private String loginLogo;
	/**系统内页logo地址*/
	@Excel(name = "系统内页logo地址", width = 15)
    @ApiModelProperty(value = "系统内页logo地址")
	private String systemRelatedLogo;
	/**标签页logo地址*/
	@Excel(name = "标签页logo地址", width = 15)
    @ApiModelProperty(value = "标签页logo地址")
	private String labelLogo;

	/**备案号*/
	@Excel(name = "备案号", width = 15)
	@ApiModelProperty(value = "备案号")
	private String beian;
	/**备案号链接*/
	@Excel(name = "备案号链接", width = 15)
	@ApiModelProperty(value = "备案号链接")
	private String beianUrl;


}
