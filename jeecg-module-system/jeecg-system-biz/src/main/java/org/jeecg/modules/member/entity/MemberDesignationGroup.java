package org.jeecg.modules.member.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 团队管理
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Data
@TableName("member_designation_group")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_designation_group对象", description="团队管理")
public class MemberDesignationGroup {
    
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
	/**团队名称*/
	@Excel(name = "团队名称", width = 15)
    @ApiModelProperty(value = "团队名称")
	private String groupName;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberId;
	/**团队总人数*/
	@Excel(name = "团队总人数", width = 15)
	@ApiModelProperty(value = "团队总人数")
	private BigDecimal totalMembers;
	/**店铺id*/
	@Excel(name = "店铺id", width = 15)
	@ApiModelProperty(value = "店铺id")
	private String storeManageId;
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
	@ApiModelProperty(value = "删除说明")
	private String delExplain;
}