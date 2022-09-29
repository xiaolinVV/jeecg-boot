package org.jeecg.modules.shop.storeSetting.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
 * @Description: 店铺设置
 * @Author: jeecg-boot
 * @Date:   2022-09-29
 * @Version: V1.0
 */
@Data
@TableName("store_setting")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="store_setting对象", description="店铺设置")
public class StoreSetting implements Serializable {
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
	/**开店功能*/
	@Excel(name = "开店功能", width = 15, dicCode = "openStore")
	@Dict(dicCode = "openStore")
    @ApiModelProperty(value = "开店功能")
    private java.lang.String openStore;
	/**店铺管理地址*/
	@Excel(name = "店铺管理地址", width = 15)
    @ApiModelProperty(value = "店铺管理地址")
    private java.lang.String manageAddress;
	/**店铺初始密码*/
	@Excel(name = "店铺初始密码", width = 15)
    @ApiModelProperty(value = "店铺初始密码")
    private java.lang.String initialPasswd;
	/**默认服务距离*/
	@Excel(name = "默认服务距离", width = 15)
    @ApiModelProperty(value = "默认服务距离")
    private java.math.BigDecimal serviceDistance;
	/**店铺首页推荐店铺*/
	@Excel(name = "店铺首页推荐店铺", width = 15, dicCode = "storeCut")
	@Dict(dicCode = "storeCut")
    @ApiModelProperty(value = "店铺首页推荐店铺")
    private java.lang.String storeCut;
	/**店铺欢迎弹窗*/
	@Excel(name = "店铺欢迎弹窗", width = 15, dicCode = "autoWelcome")
	@Dict(dicCode = "autoWelcome")
    @ApiModelProperty(value = "店铺欢迎弹窗")
    private java.lang.String autoWelcome;
	/**店铺列表默认排序*/
	@Excel(name = "店铺列表默认排序", width = 15, dicCode = "defaultSort")
	@Dict(dicCode = "defaultSort")
    @ApiModelProperty(value = "店铺列表默认排序")
    private java.lang.String defaultSort;
	/**开店邀请图*/
	@Excel(name = "开店邀请图", width = 15)
    @ApiModelProperty(value = "开店邀请图")
    private java.lang.String inviteFigure;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
    private java.lang.String coverPlan;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
    private java.lang.String posters;
	/**开店宣传图*/
	@Excel(name = "开店宣传图", width = 15)
    @ApiModelProperty(value = "开店宣传图")
    private java.lang.String openPublicityPic;
}
