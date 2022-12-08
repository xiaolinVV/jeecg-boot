package org.jeecg.modules.marketing.dto;

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
 * @Description: 素材评论表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Data
@TableName("marketing_material_comment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_material_comment对象", description="素材评论表")
public class MarketingMaterialCommentDTO {

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
	/**素材id*/
	@Excel(name = "素材id", width = 15)
    @ApiModelProperty(value = "素材id")
	private String marketingMaterialList;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**为父类评论为0；子评论：为父类评论id*/
	@Excel(name = "为父类评论为0；子评论：为父类评论id", width = 15)
    @ApiModelProperty(value = "为父类评论为0；子评论：为父类评论id")
	private String parentId;
	/**评论内容*/
	@Excel(name = "评论内容", width = 15)
    @ApiModelProperty(value = "评论内容")
	private String content;
	/**评论时间*/
	@Excel(name = "评论时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评论时间")
	private Date commentTime;
	/**状态；0：待审核；1：审核通过；2：审核不通过*/
	@Excel(name = "状态；0：待审核；1：审核通过；2：审核不通过", width = 15)
    @ApiModelProperty(value = "状态；0：待审核；1：审核通过；2：审核不通过")
	private String status;
	/**审核时间*/
	@Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
	/**不通过时间*/
	@Excel(name = "不通过时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "不通过时间")
	private Date closeTime;
	/**不通过原因*/
	@Excel(name = "不通过原因", width = 15)
    @ApiModelProperty(value = "不通过原因")
	private String closeExplain;
	/**被回复人id*/
	@Excel(name = "被回复人id", width = 15)
	@ApiModelProperty(value = "被回复人id")
	private String byReplyId;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	@ApiModelProperty(value = "用户id")
	private String sysUserId;
	/** 评论角色:0.会员 1.管理员*/
	@Excel(name = " 评论角色:0.会员 1.管理员", width = 15)
	@ApiModelProperty(value = " 评论角色:0.会员 1.管理员")
	private String userType;

	//素材标题
	private String title;
	//作者
	private String author;
	//头像
	private String headPortrait;
	//会员昵称
	private String nickName;
	//父级会员昵称
	private String parentNickName;
	//回复会员昵称
	private String  byReplyName;
	//被评论编号
    private String  parentIdS;
}
