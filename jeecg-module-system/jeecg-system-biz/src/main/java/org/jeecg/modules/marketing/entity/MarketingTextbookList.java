package org.jeecg.modules.marketing.entity;

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
 * @Description: 教程素材
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Data
@TableName("marketing_textbook_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_textbook_list对象", description="教程素材")
public class MarketingTextbookList {
    
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
	private String delFlag;
	/**栏目id*/
	@Excel(name = "栏目id", width = 15)
    @ApiModelProperty(value = "栏目id")
	private String marketingTextbookColumnId;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
	private String title;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private String sysUserId;
	/**作者*/
	@Excel(name = "作者", width = 15)
    @ApiModelProperty(value = "作者")
	private String author;
	/**关键字多个用逗号隔开*/
	@Excel(name = "关键字多个用逗号隔开", width = 15)
    @ApiModelProperty(value = "关键字多个用逗号隔开")
	private String keyword;
	/**摘要*/
	@Excel(name = "摘要", width = 15)
    @ApiModelProperty(value = "摘要")
	private String abstractDigest;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
    @ApiModelProperty(value = "封面图")
	private String surfacePlot;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String posters;
	/**推广图、分享图*/
	@Excel(name = "推广图、分享图", width = 15)
    @ApiModelProperty(value = "推广图、分享图")
	private String coverPlan;
	/**类型；1：图文素材；2：视频素材*/
	@Excel(name = "类型；1：图文素材；2：视频素材", width = 15)
    @ApiModelProperty(value = "类型；1：图文素材；2：视频素材")
	private String materialType;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
	private Object content;
	/**视频地址*/
	@Excel(name = "视频地址", width = 15)
    @ApiModelProperty(value = "视频地址")
	private String materialVideo;
	/**是否发布；0：否；1：是*/
	@Excel(name = "是否发布；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否发布；0：否；1：是")
	private String isPublish;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**初始浏览数*/
	@Excel(name = "初始浏览数", width = 15)
    @ApiModelProperty(value = "初始浏览数")
	private java.math.BigDecimal initialViews;
	/**初始点赞数*/
	@Excel(name = "初始点赞数", width = 15)
    @ApiModelProperty(value = "初始点赞数")
	private java.math.BigDecimal initialPraise;
}
