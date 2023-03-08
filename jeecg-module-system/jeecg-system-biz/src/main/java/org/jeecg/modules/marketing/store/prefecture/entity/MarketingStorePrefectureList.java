package org.jeecg.modules.marketing.store.prefecture.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 店铺专区
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Data
@TableName("marketing_store_prefecture_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_store_prefecture_list对象", description="店铺专区")
public class MarketingStorePrefectureList {
    
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
	/**图标*/
	@Excel(name = "图标", width = 15)
    @ApiModelProperty(value = "图标")
	private String icon;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
	private String shareFigure;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String postersFigure;
	/**规则*/
	@Excel(name = "规则", width = 15)
    @ApiModelProperty(value = "规则")
	private Object rule;
	/**专区类型；0：普通专区；1：限购专区；2：赠送专区*/
	@Excel(name = "专区类型；0：普通专区；1：限购专区；2：赠送专区", width = 15)
    @ApiModelProperty(value = "专区类型；0：普通专区；1：限购专区；2：赠送专区")
	private String prefectureType;
	/**限制JSON*/
	@Excel(name = "限制JSON", width = 15)
    @ApiModelProperty(value = "限制JSON")
	private String astrict;
	/**赠送JSON*/
	@Excel(name = "赠送JSON", width = 15)
    @ApiModelProperty(value = "赠送JSON")
	private String give;
	/**分红资金*/
	@Excel(name = "分红资金", width = 15)
    @ApiModelProperty(value = "分红资金")
	private java.math.BigDecimal dividend;
	/**本店分红*/
	@Excel(name = "本店分红", width = 15)
    @ApiModelProperty(value = "本店分红")
	private java.math.BigDecimal storeShare;
	/**称号奖励*/
	@Excel(name = "称号奖励", width = 15)
    @ApiModelProperty(value = "称号奖励")
	private Object titleAward;
	/**经销商奖励*/
	@Excel(name = "经销商奖励", width = 15)
    @ApiModelProperty(value = "经销商奖励")
	private java.math.BigDecimal dealerIncentives;
	/**分享人奖励*/
	@Excel(name = "分享人奖励", width = 15)
    @ApiModelProperty(value = "分享人奖励")
	private java.math.BigDecimal shareAward;
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;

	@TableField(exist = false)
	private String storeName;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;

	private String prefectureName;
}
