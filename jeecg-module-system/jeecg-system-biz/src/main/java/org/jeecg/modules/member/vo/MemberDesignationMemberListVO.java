package org.jeecg.modules.member.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 会员称号关系
 * @Author: jeecg-boot
 * @Date:   2021-03-01
 * @Version: V1.0
 */
@Data
@TableName("member_designation_member_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_designation_member_list对象", description="会员称号关系")
public class MemberDesignationMemberListVO {
    
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
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**称号id*/
	@Excel(name = "称号id", width = 15)
    @ApiModelProperty(value = "称号id")
	private String memberDesignationId;
	/**原团队管理关系会员id*/
	@Excel(name = "原团队管理关系会员id", width = 15)
    @ApiModelProperty(value = "原团队管理关系会员id")
	private String oldTManageId;
	/**团队管理关系会员id*/
	@Excel(name = "团队管理关系会员id", width = 15)
    @ApiModelProperty(value = "团队管理关系会员id")
	private String tManageId;
	/**团队网状总人数*/
	@Excel(name = "团队网状总人数", width = 15)
    @ApiModelProperty(value = "团队网状总人数")
	private java.math.BigDecimal totalMembers;
	/**是否购买礼包；0：否；1：是*/
	@Excel(name = "是否购买礼包；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否购买礼包；0：否；1：是")
	private String isBuyGift;
	/**成员加入时间*/
	@Excel(name = "成员加入时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "成员加入时间")
	private Date memberJoinTime;
	/**是否变更；0：未变更；1:变更*/
	@Excel(name = "是否变更；0：未变更；1:变更", width = 15)
    @ApiModelProperty(value = "是否变更；0：未变更；1:变更")
	private String isChange;
	/**团队变更时间*/
	@Excel(name = "团队变更时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "团队变更时间")
	private Date changeTime;
	/** 团队礼包销售总额*/
	@Excel(name = " 团队礼包销售总额", width = 15)
    @ApiModelProperty(value = " 团队礼包销售总额")
	private java.math.BigDecimal totalGiftSales;
	/**团队管理id */
	@Excel(name = "团队管理id ", width = 15)
	@ApiModelProperty(value = "团队管理id ")
	private String memberDesignationGroupId;
	/**头像绝对地址*/
	@Excel(name = "头像绝对地址", width = 15)
	@ApiModelProperty(value = "头像绝对地址")
	private String headPortrait;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
	@ApiModelProperty(value = "手机号")
	private String phone;
	/**会员昵称*/
	@Excel(name = "会员昵称", width = 15)
	@ApiModelProperty(value = "会员昵称")
	private String nickName;
	/**
	 * 称号团队名称
	 */
	private String groupName;
	//称号名称
	private String designationName;

	private String superiorName;

	private String memberSum;
	private String isViewUnderling;
}
