package org.jeecg.modules.system.formDesign.entity;

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
 * @Description: 表单设计表
 * @Author: jeecg-boot
 * @Date:   2022-08-21
 * @Version: V1.0
 */
@Data
@TableName("form_design")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="form_design对象", description="表单设计表")
public class FormDesign implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**表单名称*/
	@Excel(name = "表单名称", width = 15)
    @ApiModelProperty(value = "表单名称")
    private java.lang.String name;
	/**表单编码*/
	@Excel(name = "表单编码", width = 15)
    @ApiModelProperty(value = "表单编码")
    private java.lang.String code;
	/**表单类型。0：简单表单；1：复杂表单；*/
	@Excel(name = "表单类型。0：简单表单；1：复杂表单；", width = 15)
    @ApiModelProperty(value = "表单类型。0：简单表单；1：复杂表单；")
    private java.lang.Integer type;
	/**表单主题。不配置默认为表单名称*/
	@Excel(name = "表单主题。不配置默认为表单名称", width = 15)
    @ApiModelProperty(value = "表单主题。不配置默认为表单名称")
    private java.lang.String theme;
	/**表单设计数据。*/
	@Excel(name = "表单设计数据。", width = 15)
    @ApiModelProperty(value = "表单设计数据。")
    private java.lang.String designData;
	/**表单js代码。仅当复杂表单才有*/
	@Excel(name = "表单js代码。仅当复杂表单才有", width = 15)
    @ApiModelProperty(value = "表单js代码。仅当复杂表单才有")
    private java.lang.String jsCode;
	/**删除状态(0-正常,1-已删除)*/
	@Excel(name = "删除状态(0-正常,1-已删除)", width = 15)
    @ApiModelProperty(value = "删除状态(0-正常,1-已删除)")
    private java.lang.Integer delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
