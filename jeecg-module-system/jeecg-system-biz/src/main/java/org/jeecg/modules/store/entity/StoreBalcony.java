package org.jeecg.modules.store.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 包厢列表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Data
@TableName("store_balcony")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_balcony对象", description="包厢列表")
public class StoreBalcony {
    
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
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;
	/**包厢名称*/
	@Excel(name = "包厢名称", width = 15)
    @ApiModelProperty(value = "包厢名称")
	private String balconyName;
	/**人数开始*/
	@Excel(name = "人数开始", width = 15)
    @ApiModelProperty(value = "人数开始")
	private java.math.BigDecimal numberPeopleStart;
	/**人数结束*/
	@Excel(name = "人数结束", width = 15)
    @ApiModelProperty(value = "人数结束")
	private java.math.BigDecimal numberPeopleEnd;
	/**预定时段*/
	@Excel(name = "预定时段", width = 15)
    @ApiModelProperty(value = "预定时段")
	private String scheduledTime;
	/**主图*/
	@Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
	private String mainPicture;
	/**内容介绍*/
	@Excel(name = "内容介绍", width = 15)
    @ApiModelProperty(value = "内容介绍")
	private String introduceContent;
	/**预订须知*/
	@Excel(name = "预订须知", width = 15)
    @ApiModelProperty(value = "预订须知")
	private String bookingInformation;
	/**联系信息*/
	@Excel(name = "联系信息", width = 15)
    @ApiModelProperty(value = "联系信息")
	private String contactInformation;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private String serialNumber;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
}
