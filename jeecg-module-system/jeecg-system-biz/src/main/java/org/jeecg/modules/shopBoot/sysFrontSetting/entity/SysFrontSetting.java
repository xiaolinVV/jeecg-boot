package org.jeecg.modules.shopBoot.sysFrontSetting.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 商城设置
 * @Author: jeecg-boot
 * @Date:   2022-09-29
 * @Version: V1.0
 */
@Data
@TableName("sys_front_setting")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_front_setting对象", description="商城设置")
public class SysFrontSetting implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**创建年*/
	@Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private java.lang.Integer year;
	/**创建月*/
	@Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private java.lang.Integer month;
	/**创建日*/
	@Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private java.lang.Integer day;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private java.lang.String delFlag;
	/**小程序名称*/
	@Excel(name = "小程序名称", width = 15)
    @ApiModelProperty(value = "小程序名称")
    private java.lang.String name;
	/**小程序描述*/
	@Excel(name = "小程序描述", width = 15)
    @ApiModelProperty(value = "小程序描述")
    private java.lang.String description;
	/**小程序logo*/
	@Excel(name = "小程序logo", width = 15)
    @ApiModelProperty(value = "小程序logo")
    private java.lang.String frontLogo;
	/**首页推广图*/
	@Excel(name = "首页推广图", width = 15)
    @ApiModelProperty(value = "首页推广图")
    private java.lang.String homeGeneralizeAddr;
	/**礼包胶囊图*/
	@Excel(name = "礼包胶囊图", width = 15)
    @ApiModelProperty(value = "礼包胶囊图")
    private java.lang.String giftCapsuleAddr;
	/**小程序头部颜色*/
	@Excel(name = "小程序头部颜色", width = 15)
    @ApiModelProperty(value = "小程序头部颜色")
    private java.lang.String headColor;
	/**领券中心推广图*/
	@Excel(name = "领券中心推广图", width = 15)
    @ApiModelProperty(value = "领券中心推广图")
    private java.lang.String couponCentreAddr;
	/**店铺推广图*/
	@Excel(name = "店铺推广图", width = 15)
    @ApiModelProperty(value = "店铺推广图")
    private java.lang.String storeGeneralizeAddr;
	/**商品默认图*/
	@Excel(name = "商品默认图", width = 15)
    @ApiModelProperty(value = "商品默认图")
    private java.lang.String defaultPicture;
	/**商品数据来源标签(小程序端)*/
	@Excel(name = "商品数据来源标签(小程序端)", width = 15, dicCode = "storeSourceOptions")
	@Dict(dicCode = "storeSourceOptions")
    @ApiModelProperty(value = "商品数据来源标签(小程序端)")
    private java.lang.String goodLabelSmallsoft;
	/**商品数据来源标签(app端)*/
	@Excel(name = "商品数据来源标签(app端)", width = 15, dicCode = "storeSourceOptions")
	@Dict(dicCode = "storeSourceOptions")
    @ApiModelProperty(value = "商品数据来源标签(app端)")
    private java.lang.String goodLabelApp;
	/**首页底部推荐类型*/
	@Excel(name = "首页底部推荐类型", width = 15, dicCode = "indexBottomRecommend")
	@Dict(dicCode = "indexBottomRecommend")
    @ApiModelProperty(value = "首页底部推荐类型")
    private java.lang.String indexBottomRecommend;
}
