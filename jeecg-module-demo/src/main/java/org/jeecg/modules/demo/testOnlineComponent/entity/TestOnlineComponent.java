package org.jeecg.modules.demo.testOnlineComponent.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;





/**
 * @Description: 测试 Online 表单控件
 * @Author: jeecg-boot
 * @Date:   2022-10-27
 * @Version: V1.0
 */
@Data
@TableName("test_online_component")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_online_component对象", description="测试 Online 表单控件")
public class TestOnlineComponent  implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**文本框*/
	@Excel(name = "文本框", width = 15)
    @ApiModelProperty(value = "文本框")
    private java.lang.String nomalText;
	/**密码框*/
	@Excel(name = "密码框", width = 15)
    @ApiModelProperty(value = "密码框")
    private java.lang.String password;
	/**下拉框*/
	@Excel(name = "下拉框", width = 15, dicCode = "sex")
	@Dict(dicCode = "sex")
    @ApiModelProperty(value = "下拉框")
    private java.lang.String dropSelect;
	/**下拉框*/
	@Excel(name = "下拉框", width = 15, dictTable = "sys_role", dicText = "role_name", dicCode = "role_code")
	@Dict(dictTable = "sys_role", dicText = "role_name", dicCode = "role_code")
    @ApiModelProperty(value = "下拉框")
    private java.lang.String dropSelectTable;
	/**下拉框*/
	@Excel(name = "下拉框", width = 15, dicCode = "rule_conditions")
	@Dict(dicCode = "rule_conditions")
    @ApiModelProperty(value = "下拉框")
    private java.lang.String dropSelectMulti;
	/**下拉框*/
	@Excel(name = "下拉框", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "下拉框")
    private java.lang.String dropSelectMultiTable;
	/**下拉搜索*/
	@Excel(name = "下拉搜索", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "下拉搜索")
    private java.lang.String dropSelectSearch;
	/**单选框*/
	@Excel(name = "单选框", width = 15, dicCode = "sex")
	@Dict(dicCode = "sex")
    @ApiModelProperty(value = "单选框")
    private java.lang.String radio;
	/**多选框*/
	@Excel(name = "多选框", width = 15, dicCode = "database_type")
	@Dict(dicCode = "database_type")
    @ApiModelProperty(value = "多选框")
    private java.lang.String multiSelect;
	/**开关*/
	@Excel(name = "开关", width = 15)
    @ApiModelProperty(value = "开关")
    private java.lang.String testSwitch;
	/**日期*/
	@Excel(name = "日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private java.util.Date nomalDate;
	/**日期时间*/
	@Excel(name = "日期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期时间")
    private java.util.Date completeDate;
	/**时间*/
	@Excel(name = "时间", width = 15)
    @ApiModelProperty(value = "时间")
    private java.lang.String nomalTime;
	/**文件*/
	@Excel(name = "文件", width = 15)
    @ApiModelProperty(value = "文件")
    private java.lang.String nomalFile;
	/**图片*/
	@Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String image;
	/**用户选择*/
	@Excel(name = "用户选择", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "用户选择")
    private java.lang.String userSelect;
	/**部门选择*/
	@Excel(name = "部门选择", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "部门选择")
    private java.lang.String departSelect;
	/**分类字典*/
	@Excel(name = "分类字典", width = 15)
    @ApiModelProperty(value = "分类字典")
    private java.lang.String treesel;
	/**省市区*/
	@Excel(name = "省市区", width = 15)
    @ApiModelProperty(value = "省市区")
    private java.lang.String area;
	/**popup*/
	@Excel(name = "popup", width = 15)
    @ApiModelProperty(value = "popup")
    private java.lang.String popup;
	/**自定义树*/
	@Excel(name = "自定义树", width = 15, dictTable = "sys_category", dicText = "name", dicCode = "id")
	@Dict(dictTable = "sys_category", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "自定义树")
    private java.lang.String customTreesel;
	/**省*/
	@Excel(name = "省", width = 15)
    @ApiModelProperty(value = "省")
    private java.lang.String province;
	/**市*/
	@Excel(name = "市", width = 15)
    @ApiModelProperty(value = "市")
    private java.lang.String city;
	/**县区*/
	@Excel(name = "县区", width = 15)
    @ApiModelProperty(value = "县区")
    private java.lang.String area1;
	/**多行文本*/
	@Excel(name = "多行文本", width = 15)
    @ApiModelProperty(value = "多行文本")
    private java.lang.String multiLimeText;
	/**富文本*/
	@Excel(name = "富文本", width = 15)
    @ApiModelProperty(value = "富文本")
    private java.lang.String editor;
	/**markdown*/
	@Excel(name = "markdown", width = 15)
    @ApiModelProperty(value = "markdown")
    private java.lang.String markdown;
}
