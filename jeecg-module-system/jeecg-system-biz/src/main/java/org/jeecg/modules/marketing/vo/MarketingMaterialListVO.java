package org.jeecg.modules.marketing.vo;

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
import org.jeecg.modules.marketing.dto.MarketingMaterialGoodDTO;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 素材列表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Data
@TableName("marketing_material_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_material_list对象", description="素材列表")
public class MarketingMaterialListVO {
    
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
	/**栏目id*/
	@Excel(name = "栏目id", width = 15)
    @ApiModelProperty(value = "栏目id")
	private String marketingMaterialColumnId;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
	private String title;
	/**作者*/
	@Excel(name = "作者", width = 15)
    @ApiModelProperty(value = "作者")
	private String author;
	/**关键字*/
	@Excel(name = "关键字", width = 15)
    @ApiModelProperty(value = "关键字")
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
	/**初始浏览数*/
	@Excel(name = "初始浏览数", width = 15)
	@ApiModelProperty(value = "初始浏览数")
	private String initialViews;
	/**初始点赞数*/
	@Excel(name = "初始点赞数", width = 15)
	@ApiModelProperty(value = "初始点赞数")
	private String initialPraise;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	@ApiModelProperty(value = "用户id")
	private String sysUserId;

    //新增数据
	/**栏目名称*/
	private String  marketingMaterialColumnName;

	/**商品个数*/
	private String  goodListCount;
	/**素材商品集合*/
	private List<MarketingMaterialGoodDTO> marketingMaterialGoodDTOList;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/**素材商品集合字符串*/
	private String marketingMaterialGoodDTOListStr;

	//实际点赞数
	private BigDecimal dianzanCount;
	//实际浏览记录:
	private BigDecimal browseCount;
	//实际点赞列表:
	private List<Map<String,Object>> dianzanList;
	//实际浏览记录列表:
	private List<Map<String,Object>> browseList;
}
