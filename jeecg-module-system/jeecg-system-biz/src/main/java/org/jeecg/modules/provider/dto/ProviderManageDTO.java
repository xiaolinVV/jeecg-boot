package org.jeecg.modules.provider.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 供应商列表
 * @Author: jeecg-boot
 * @Date:   2020-01-02
 * @Version: V1.0
 */
@Data
@TableName("provider_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="provider_manage对象", description="供应商列表")
public class ProviderManageDTO {
    
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
	private String delFlag;
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
    @ApiModelProperty(value = "供应商id")
	private String sysUserId;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
	private String name;
	/**区域id*/
	@Excel(name = "区域id", width = 15)
    @ApiModelProperty(value = "区域id")
	private String sysAreaId;
	/**区域地址说明*/
	@Excel(name = "区域地址说明", width = 15)
    @ApiModelProperty(value = "区域地址说明")
	private String areaAddress;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private String address;
	/**公司手机号码*/
	@Excel(name = "公司手机号码", width = 15)
    @ApiModelProperty(value = "公司手机号码")
	private String companyPhone;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
	private String linkman;
	/**联系人手机号*/
	@Excel(name = "联系人手机号", width = 15)
    @ApiModelProperty(value = "联系人手机号")
	private String linkphone;
	/**结算类型；0：按日结算；1：按周结算；2：按月结算*/
	@Excel(name = "结算类型；0：按日结算；1：按周结算；2：按月结算", width = 15,dicCode = "provider_account_type")
    @ApiModelProperty(value = "结算类型；0：按日结算；1：按周结算；2：按月结算")
	@Dict(dicCode = "provider_account_type")
	private String accountType;
	/**业务对接人*/
	@Excel(name = "业务对接人", width = 15)
    @ApiModelProperty(value = "业务对接人")
	private String contactPerson;
	/**合作开始时间*/
	@Excel(name = "合作开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "合作开始时间")
	private Date startTime;
	/**合作结束时间*/
	@Excel(name = "合作结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "合作结束时间")
	private Date endTime;
	/**备注说明*/
	@Excel(name = "备注说明", width = 15)
    @ApiModelProperty(value = "备注说明")
	private String remark;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplain;
	/**余额*/
	@Excel(name = "余额", width = 15)
    @ApiModelProperty(value = "余额")
	private java.math.BigDecimal balance;
	/**冻结金额（待结算）*/
	@Excel(name = "冻结金额（待结算）", width = 15)
    @ApiModelProperty(value = "冻结金额（待结算）")
	private java.math.BigDecimal accountFrozen;
	/**不可用金额*/
	@Excel(name = "不可用金额", width = 15)
    @ApiModelProperty(value = "不可用金额")
	private java.math.BigDecimal unusableFrozen;
	/**商品审核*/
	@Excel(name = "商品审核", width = 15)
	@ApiModelProperty(value = "商品审核")
	private Boolean goodAudit;
	/**
	 *结算类型
	 */
	private String accountTypeName;
	/**
	 *合作时间
	 */
	private String timeName;
	/**
	 *状态
	 */
	private String statusName;

	/**
	 *创建时间开始
	 */
	private String createTime_begin;
	/**
	 *创建时间结束
	 */
	private String createTime_end;
	/**
	 * 业务对接人
	 */
	private String contactName;
	//账号
	private String userName;
	//头像
	private String avatar;

}
