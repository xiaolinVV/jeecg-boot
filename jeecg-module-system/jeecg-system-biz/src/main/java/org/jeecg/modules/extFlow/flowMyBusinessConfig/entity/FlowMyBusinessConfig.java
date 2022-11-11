package org.jeecg.modules.extFlow.flowMyBusinessConfig.entity;

import java.io.Serializable;

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
 * @Description:  流程配置表
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
@Data
@TableName("flow_my_business_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="flow_my_business_config对象", description=" 流程配置表")
public class FlowMyBusinessConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**表单类型*/
	@Excel(name = "表单类型", width = 15, dicCode = "ext_flow_form_type")
	@Dict(dicCode = "ext_flow_form_type")
    @ApiModelProperty(value = "表单类型")
    private String formType;
	/**表名/自定义表单 CODE*/
	@Excel(name = "表名/自定义表单 CODE", width = 15)
    @ApiModelProperty(value = "表名/自定义表单 CODE")
    private String tableName;
	/**唯一编码*/
	@Excel(name = "唯一编码", width = 15)
    @ApiModelProperty(value = "唯一编码")
    private String code;
	/**流程状态列名*/
	@Excel(name = "流程状态列名", width = 15)
    @ApiModelProperty(value = "流程状态列名")
    private String bpmStatusColumnName;
	/**标题表达式*/
	@Excel(name = "标题表达式", width = 15)
    @ApiModelProperty(value = "标题表达式")
    private String titleExpression;
    /**流程定义key 一个key会有多个版本的id*/
    @Excel(name = "流程定义key 一个key会有多个版本的id", width = 15)
    @ApiModelProperty(value = "流程定义key 一个key会有多个版本的id")
    private String processDefinitionKey;
    /**积木报表*/
    @Excel(name = "积木报表", width = 15, dictTable = "jimu_report", dicText = "name", dicCode = "id")
    @Dict(dictTable = "jimu_report", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "积木报表")
    private String jimuReportId;
    /**PC表单组件地址*/
    @Excel(name = "PC表单组件地址", width = 15)
    @ApiModelProperty(value = "PC表单组件地址")
    private String pcFormUrl;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
