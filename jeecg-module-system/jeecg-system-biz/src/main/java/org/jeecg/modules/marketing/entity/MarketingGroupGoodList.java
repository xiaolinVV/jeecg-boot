package org.jeecg.modules.marketing.entity;

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

/**
 * @Description: 拼团商品列表
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Data
@TableName("marketing_group_good_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_group_good_list对象", description="拼团商品列表")
public class MarketingGroupGoodList {
    
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
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
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
	/**拼团商品类型id*/
	@Excel(name = "拼团商品类型id", width = 15)
    @ApiModelProperty(value = "拼团商品类型id")
	private String marketingGroupGoodTypeId;
	/**商品列表id*/
	@Excel(name = "商品列表id", width = 15)
    @ApiModelProperty(value = "商品列表id")
	private String goodListId;
	/**是否推荐；0：否；1：是*/
	@Excel(name = "是否推荐；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否推荐；0：否；1：是")
	private String isRecommend;
	/**状态：0：停用；1：启用；2：结束*/
	@Excel(name = "状态：0：停用；1：启用；2：结束", width = 15)
    @ApiModelProperty(value = "状态：0：停用；1：启用；2：结束")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
	/**参团人数*/
	@Excel(name = "参团人数", width = 15)
    @ApiModelProperty(value = "参团人数")
	private java.math.BigDecimal numberTuxedo;
	/**参团积分*/
	@Excel(name = "参团积分", width = 15)
    @ApiModelProperty(value = "参团积分")
	private java.math.BigDecimal tuxedoWelfarePayments;
	/**返还比例*/
	@Excel(name = "返还比例", width = 15)
    @ApiModelProperty(value = "返还比例")
	private java.math.BigDecimal returnProportion;
	/**活动时间*/
	@Excel(name = "活动时间", width = 15)
    @ApiModelProperty(value = "活动时间")
	private java.math.BigDecimal activityTime;
	/**活动单位；0：天；1：时*/
	@Excel(name = "活动单位；0：天；1：时", width = 15)
    @ApiModelProperty(value = "活动单位；0：天；1：时")
	private String activityUnit;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
	private java.util.Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
	private java.util.Date endTime;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
}
