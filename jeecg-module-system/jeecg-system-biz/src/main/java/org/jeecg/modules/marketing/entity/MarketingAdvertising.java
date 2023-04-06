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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 广告管理
 * @Author: jeecg-boot
 * @Date:   2019-10-10
 * @Version: V1.0
 */
@Data
@TableName("marketing_advertising")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_advertising对象", description="广告管理")
public class MarketingAdvertising {
    
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
	/**平台广告为空，店铺广告填写角色问店铺的商家*/
	@Excel(name = "平台广告为空，店铺广告填写角色问店铺的商家", width = 15)
    @ApiModelProperty(value = "平台广告为空，店铺广告填写角色问店铺的商家")
	private String sysUserId;
	/**广告标题*/
	@Excel(name = "广告标题", width = 15)
    @ApiModelProperty(value = "广告标题")
	private String title;
	/**广告类型；建立数据字典读取，字典名称：ad_type；0:为平台广告；1：店铺广告*/
	@Excel(name = "广告类型；建立数据字典读取，字典名称：ad_type；0:为平台广告；1：店铺广告", width = 15)
    @ApiModelProperty(value = "广告类型；建立数据字典读取，字典名称：ad_type；0:为平台广告；1：店铺广告")
	@Dict(dicCode = "ad_type")
	private String adType;
	/**广告位置；建立数据字典读取，字典名称：ad_location；0:首页；1：分类*/
	@Excel(name = "广告位置；建立数据字典读取，字典名称：ad_location；0:首页；1：分类", width = 15)
    @ApiModelProperty(value = "广告位置；建立数据字典读取，字典名称：ad_location；0:首页；1：分类")
	@Dict(dicCode = "ad_location")
	private String adLocation;
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
	/**详情图*/
	@Excel(name = "详情图", width = 15)
	@ApiModelProperty(value = "详情图")
	private String pictureDetails;
	/**素材Id*/
	@Excel(name = "素材Id", width = 15)
	@ApiModelProperty(value = "素材Id")
	private String marketingMaterialListId;


}