package org.jeecg.modules.marketing.vo;

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
 * @Description: 推荐兑换券
 * @Author: jeecg-boot
 * @Date:   2020-05-07
 * @Version: V1.0
 */
@Data
@TableName("marketing_recommend_certificate")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_recommend_certificate对象", description="推荐兑换券")
public class MarketingRecommendCertificateVO {
    
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
	/**兑换券id*/
	@Excel(name = "兑换券id", width = 15)
    @ApiModelProperty(value = "兑换券id")
	private String marketingCertificateId;
	/**推荐位：0：无；1：首页；2:还知道；字典维护*/
	@Excel(name = "推荐位：0：无；1：首页；2:还知道；字典维护", width = 15,dicCode = "recommend_local")
    @ApiModelProperty(value = "推荐位：0：无；1：首页；2:还知道；字典维护:recommend_local")
	@Dict(dicCode = "recommend_local")
	private String recommendLocal;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
	private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
	private Date endTime;
	/**状态：0：停用；1：启用*/
	@Excel(name = "状态：0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态：0：停用；1：启用")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplain;
	/**
	 * 兑换券名称
	 */
	private String name;
	/**
	 * 兑换券主图
	 */
	private String mainPicture;
}
