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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 推荐分类
 * @Author: jeecg-boot
 * @Date:   2019-12-07
 * @Version: V1.0
 */
@Data
@TableName("marketing_recommend_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_recommend_type对象", description="推荐分类")
public class MarketingRecommendTypeVO {

	/**主键ID*/
	private String id;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	private Date createTime;
	/**修改人*/
	private String updateBy;
	/**修改时间*/
	private Date updateTime;
	/**创建年*/
	private Integer year;
	/**创建月*/
	private Integer month;
	/**创建日*/
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@TableLogic
	private String delFlag;
	/**平台分类id*/
	private String goodTypeId;
	/**排序*/
	private java.math.BigDecimal sort;
	/**开始时间*/
	private Date startTime;
	/**结束时间*/
	private Date endTime;
	/**状态；0：停用；1：启用*/
	private String status;
	/**分类别名*/
	private String nickName;
	/**三级分类Id*/
	private String goodTypeIdThree;
	/**二级分类Id*/
	private String goodTypeIdTwo;
	/**一级分类Id*/
	private String goodTypeIdOne;
	/**三级分类名称*/
	private String goodTypeThreeName;
	/**二级分类名称*/
	private String goodTypeTwoName;
	/**一级分类名称*/
	private String goodTypeOneName;
	/**字符串拼接分类名称*/
	private String goodTypeNames;
	/**查询条件创建时间*/
	private String createTime_begin;
	/**查询条件创建时间*/
	private String createTime_end;
	/**查询条件开始时间*/
	private String startTime_begin;
	/**查询条件开始时间*/
	private String startTime_end;
	/**查询条件创建时间*/
	private String endTime_begin;
	/**查询条件创建时间*/
	private String endTime_end;
	/**分类级别*/
	private String levels;
	}
