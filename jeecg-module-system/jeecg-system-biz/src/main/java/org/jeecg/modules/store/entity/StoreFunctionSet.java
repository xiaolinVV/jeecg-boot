package org.jeecg.modules.store.entity;

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

/**
 * @Description: 店铺管理-功能设置
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Data
@TableName("store_function_set")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_function_set对象", description="店铺管理-功能设置")
public class StoreFunctionSet {
    
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
	private String delFlag;
	/**竞价活动；0：关闭；1：开启*/
	@Excel(name = "竞价活动；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "竞价活动；0：关闭；1：开启")
	private String biddingActivities;
	/**藏品征集；0：关闭；1：开启*/
	@Excel(name = "藏品征集；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "藏品征集；0：关闭；1：开启")
	private String collectionCollection;
	/**包厢管理；0：关闭；1：开启*/
	@Excel(name = "包厢管理；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "包厢管理；0：关闭；1：开启")
	private String roomsManagement;
	/**桌台管理；0：关闭；1：开启*/
	@Excel(name = "桌台管理；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "桌台管理；0：关闭；1：开启")
	private String tablesManagement;
	/**鉴定中心；0：关闭；1：开启*/
	@Excel(name = "鉴定中心；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "鉴定中心；0：关闭；1：开启")
	private String evaluationCenter;
	/**溯源中心；0：关闭；1：开启*/
	@Excel(name = "溯源中心；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "溯源中心；0：关闭；1：开启")
	private String resourceCenter;
	/**展示中心；0：关闭；1：开启*/
	@Excel(name = "展示中心；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "展示中心；0：关闭；1：开启")
	private String exhibitionCenter;
	/**供需管理；0：关闭；1：开启*/
	@Excel(name = "供需管理；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "供需管理；0：关闭；1：开启")
	private String demandManagement;

	private String independentPublishing;

	private String exhibits;

	private String storeManageId;
}
