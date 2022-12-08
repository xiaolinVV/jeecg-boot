package org.jeecg.modules.store.entity;

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
 * @Description: 店铺设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Data
@TableName("store_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_setting对象", description="店铺设置")
public class StoreSetting {
    
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
	/**开店功能;0:关闭；1：开启*/
	@Excel(name = "开店功能;0:关闭；1：开启", width = 15)
    @ApiModelProperty(value = "开店功能;0:关闭；1：开启")
	private String openStore;
	/**店铺管理地址*/
	@Excel(name = "店铺管理地址", width = 15)
    @ApiModelProperty(value = "店铺管理地址")
	private String manageAddress;
	/**初始密码*/
	@Excel(name = "初始密码", width = 15)
    @ApiModelProperty(value = "初始密码")
	private String initialPasswd;
	/**默认服务距离*/
	@Excel(name = "默认服务距离", width = 15)
    @ApiModelProperty(value = "默认服务距离")
	private java.math.BigDecimal serviceDistance;
	/**开店宣传图*/
	@Excel(name = "开店宣传图", width = 15)
    @ApiModelProperty(value = "开店宣传图")
	private String openPublicityPic;
	/**店铺切换;0:关闭；1：开启*/
	@Excel(name = "店铺切换;0:关闭；1：开启", width = 15)
    @ApiModelProperty(value = "店铺切换;0:关闭；1：开启")
	private String storeCut;

	@Excel(name = "自动弹出欢迎弹窗：0：不自动弹出；1：自动弹出", width = 15)
	@ApiModelProperty(value = "自动弹出欢迎弹窗：0：不自动弹出；1：自动弹出")
	private  String autoWelcome;
	@Excel(name = "开店邀请图", width = 15)
	@ApiModelProperty(value = "开店邀请图")
	private String inviteFigure;
	@Excel(name = "分享图", width = 15)
	@ApiModelProperty(value = "分享图")
	private String coverPlan;
	@Excel(name = "海报图", width = 15)
	@ApiModelProperty(value = "海报图")
	private String posters;
	/**
	 *
	 * 默认排序方式；0：按距离；1：按照排序字段
	 */
	private String defaultSort;
}
