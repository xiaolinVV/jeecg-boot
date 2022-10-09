package org.jeecg.modules.demo.cmsCsdnBlog.entity;

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
 * @Description: csdn博客表
 * @Author: jeecg-boot
 * @Date:   2022-10-09
 * @Version: V1.0
 */
@Data
@TableName("cms_csdn_blog")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cms_csdn_blog对象", description="csdn博客表")
public class CmsCsdnBlog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**title*/
	@Excel(name = "title", width = 15)
    @ApiModelProperty(value = "title")
    private String title;
	/**date*/
	@Excel(name = "date", width = 15)
    @ApiModelProperty(value = "date")
    private String postDate;
	/**tags*/
	@Excel(name = "tags", width = 15)
    @ApiModelProperty(value = "tags")
    private String tags;
	/**category*/
	@Excel(name = "category", width = 15)
    @ApiModelProperty(value = "category")
    private String category;
	/**view*/
	@Excel(name = "view", width = 15)
    @ApiModelProperty(value = "view")
    private Integer view;
	/**comments*/
	@Excel(name = "comments", width = 15)
    @ApiModelProperty(value = "comments")
    private Integer comments;
	/**copyright*/
	@Excel(name = "copyright", width = 15)
    @ApiModelProperty(value = "copyright")
    private Integer copyright;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
