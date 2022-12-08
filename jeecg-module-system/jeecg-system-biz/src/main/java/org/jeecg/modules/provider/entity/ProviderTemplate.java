package org.jeecg.modules.provider.entity;

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
 * @Description: 供应商运费模板
 * @Author: jeecg-boot
 * @Date:   2019-10-26
 * @Version: V1.0
 */
@Data
@TableName("provider_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="provider_template对象", description="供应商运费模板")
public class ProviderTemplate {
    
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
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
    @ApiModelProperty(value = "供应商id")
	private String sysUserId;
	/**模板名称*/
	@Excel(name = "模板名称", width = 15)
    @ApiModelProperty(value = "模板名称")
	private String name;
	/**发货地id*/
	@Excel(name = "发货地id", width = 15)
    @ApiModelProperty(value = "发货地id")
	private String sysAreaId;
	/**发货地描述*/
	@Excel(name = "发货地描述", width = 15)
    @ApiModelProperty(value = "发货地描述")
	private String placeDispatch;
	/**包邮配送区域json存储，存储区域id*/
	@Excel(name = "包邮配送区域json存储，存储区域id", width = 15)
    @ApiModelProperty(value = "包邮配送区域json存储，存储区域id")
	private String exemptionPostage;
	/**计费方式；0：按件数计费；1：按重量计费*/
	@Excel(name = "计费方式；0：按件数计费；1：按重量计费", width = 15)
    @ApiModelProperty(value = "计费方式；0：按件数计费；1：按重量计费")
	private String chargeMode;
	/**付邮费区域json描述，制作人过来问我，需要实体配合*/
	@Excel(name = "付邮费区域json描述，制作人过来问我，需要实体配合", width = 15)
    @ApiModelProperty(value = "付邮费区域json描述，制作人过来问我，需要实体配合")
	private String mailDelivery;
	/**模板类型；0：包邮；1：自定义*/
	@Excel(name = "模板类型；0：包邮；1：自定义", width = 15)
    @ApiModelProperty(value = "模板类型；0：包邮；1：自定义")
	private String templateType;
	@Excel(name = "是否默认；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否默认；0：否；1：是")
	private String isTemplate;
	@Excel(name = "删除说明", width = 15)
	@ApiModelProperty(value = "删除说明")
	private String delExplain;
	/**
	 * 外部运费模板id
	 */
	private String freightTemplateId;
}
