package org.jeecg.modules.system.entity;

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
 * @Description: 小程序前端设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Data
@TableName("sys_front_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_front_setting对象", description="小程序前端设置")
public class SysFrontSetting {
    
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
	@TableLogic
	private String delFlag;
	/**客户端名称*/
	@Excel(name = "客户端名称", width = 15)
    @ApiModelProperty(value = "客户端名称")
	private String name;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private String description;
	/**客户端logo*/
	@Excel(name = "客户端logo", width = 15)
    @ApiModelProperty(value = "客户端logo")
	private String frontLogo;
	/**礼包胶囊图*/
	@Excel(name = "礼包胶囊图", width = 15)
    @ApiModelProperty(value = "礼包胶囊图")
	private String giftCapsuleAddr;
	/**首页推广图*/
	@Excel(name = "首页推广图", width = 15)
    @ApiModelProperty(value = "首页推广图")
	private String homeGeneralizeAddr;
	/**领券中心推广图*/
	@Excel(name = "领券中心推广图", width = 15)
    @ApiModelProperty(value = "领券中心推广图")
	private String couponCentreAddr;
	/**店铺推广图*/
	@Excel(name = "店铺推广图", width = 15)
    @ApiModelProperty(value = "店铺推广图")
	private String storeGeneralizeAddr;

	@Excel(name = "商品默认图", width = 15)
	@ApiModelProperty(value = "商品默认图")
	private String defaultPicture;
	@Excel(name = "默认小程序首页头部颜色色值", width = 15)
	@ApiModelProperty(value = "默认小程序首页头部颜色色值")
	private String headColor;

	@Excel(name = "商品数据来源标签；1：京东；2：淘宝；逗号分号", width = 15)
	@ApiModelProperty(value = "商品数据来源标签；1：京东；2：淘宝；逗号分号")
	private String goodLabelSmallsoft;

	@Excel(name = "商品数据来源标签；1：京东；2：淘宝；逗号分号", width = 15)
	@ApiModelProperty(value = "商品数据来源标签；1：京东；2：淘宝；逗号分号")
	private String goodLabelApp;

	/**
	 * 首页底部推荐；0：商品；1：店铺
	 */
	private String indexBottomRecommend;
}
