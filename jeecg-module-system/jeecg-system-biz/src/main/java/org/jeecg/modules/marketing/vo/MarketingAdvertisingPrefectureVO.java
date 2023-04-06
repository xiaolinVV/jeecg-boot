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
 * @Description: 专区广告
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Data
@TableName("marketing_advertising_prefecture")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_advertising_prefecture对象", description="专区广告")
public class MarketingAdvertisingPrefectureVO {

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
	/**广告标题*/
	@Excel(name = "广告标题", width = 15)
    @ApiModelProperty(value = "广告标题")
	private String title;
	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
    @ApiModelProperty(value = "图片地址")
	private String pictureAddr;
	/**跳转类型；0：无；1：商品详情*/
	@Excel(name = "跳转类型；0：无；1：商品详情", width = 15)
    @ApiModelProperty(value = "跳转类型；0：无；1：商品详情")
	private String goToType;
	/**如果广告类型为0代表平台商品id，如果为1代表店铺商品的id*/
	@Excel(name = "如果广告类型为0代表平台商品id，如果为1代表店铺商品的id", width = 15)
    @ApiModelProperty(value = "如果广告类型为0代表平台商品id，如果为1代表店铺商品的id")
	private String goodListId;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private Integer sort;
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
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	@TableLogic
	private String delFlag;
	/**专区id*/
	@Excel(name = "专区id", width = 15)
    @ApiModelProperty(value = "专区id")
	private String marketingPrefectureId;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
	@ApiModelProperty(value = "停用说明")
	private String closeExplain;
	/*新增内容**/
	//商品名称
	private String goodName;

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
}