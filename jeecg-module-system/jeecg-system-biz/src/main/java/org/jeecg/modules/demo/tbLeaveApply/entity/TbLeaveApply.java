package org.jeecg.modules.demo.tbLeaveApply.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import org.jeecg.modules.flowable.apithird.business.entity.FlowMyBusinessDto;



/**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2022-10-27
 * @Version: V1.0
 */
@Data
@TableName("tb_leave_apply")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tb_leave_apply对象", description="请假申请")
public class TbLeaveApply  extends FlowMyBusinessDto  implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
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
	/**请假人*/
	@Excel(name = "请假人", width = 15)
    @ApiModelProperty(value = "请假人")
    private String name;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;
	/**请假天数*/
	@Excel(name = "请假天数", width = 15)
    @ApiModelProperty(value = "请假天数")
    private Integer days;
	/**请假事由*/
	@Excel(name = "请假事由", width = 15)
    @ApiModelProperty(value = "请假事由")
    private String content;
	/**审批状态*/
	@Excel(name = "审批状态", width = 15, dicCode = "act_status")
	@Dict(dicCode = "act_status")
    @ApiModelProperty(value = "审批状态")
    private String bpmStatus;
}
