package org.jeecg.modules.vehicle.entity;

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

/**
 * @Description: 停车场设置
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Data
@TableName("vehicle_park_base_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="vehicle_park_base_setting对象", description="停车场设置")
public class VehicleParkBaseSetting {
    
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
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
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
	/**停车场名称*/
	@Excel(name = "停车场名称", width = 15)
    @ApiModelProperty(value = "停车场名称")
	private String parkName;
	/**收费系统；0：本系统；1：科拓收费系统*/
	@Excel(name = "收费系统；0：本系统；1：科拓收费系统", width = 15)
    @ApiModelProperty(value = "收费系统；0：本系统；1：科拓收费系统")
	private String tollCollectionSystem;
	/**收费规则*/
	@Excel(name = "收费规则", width = 15)
    @ApiModelProperty(value = "收费规则")
	private String chargingRules;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
	private Object content;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private String detailedAddress;

	/**
	 * 区域id，逗号隔开
	 */
	private String sysAreaIds;

	/**
	 * 区域名称，逗号隔开
	 */
	private String sysAreaExplain;
}
