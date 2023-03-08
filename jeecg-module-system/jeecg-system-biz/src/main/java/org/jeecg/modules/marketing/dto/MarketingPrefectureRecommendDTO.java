package org.jeecg.modules.marketing.dto;

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
 * @Description: 专区推荐
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
@Data
@TableName("marketing_prefecture_recommend")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_prefecture_recommend对象", description="专区推荐")
public class MarketingPrefectureRecommendDTO {
    
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
	/**专区id*/
	@Excel(name = "专区id", width = 15)
    @ApiModelProperty(value = "专区id")
	private String marketingPrefectureId;
	/**推荐名称*/
	@Excel(name = "推荐名称", width = 15)
    @ApiModelProperty(value = "推荐名称")
	private String recommendName;
	/**呈现方式*/
	@Excel(name = "呈现方式; 0:栏目推荐 1:列表推荐", width = 15)
    @ApiModelProperty(value = "呈现方式; 0:栏目推荐 1:列表推荐")
	private String appearType;
	/**推荐封面*/
	@Excel(name = "推荐封面", width = 15)
    @ApiModelProperty(value = "推荐封面")
	private String coverPicture;
	/**分类推荐*/
	@Excel(name = "分类推荐; 0:不显示 1:显示", width = 15)
    @ApiModelProperty(value = "分类推荐; 0:不显示 1:显示")
	private String recommendClassify;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplain;

	@Excel(name = "专区名称", width = 15)
	@ApiModelProperty(value = "专区名称")
	private String prefectureName;

	private String createTime_begin;

	private String createTime_end;

	private String updateTime_begin;

	private String updateTime_end;
}
