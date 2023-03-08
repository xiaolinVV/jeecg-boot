package org.jeecg.modules.marketing.store.giftbag.entity;

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
 * @Description: 礼包团-礼包设置
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
@Data
@TableName("marketing_store_giftbag_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_store_giftbag_list对象", description="礼包团-礼包设置")
public class MarketingStoreGiftbagList {
    
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
	@TableLogic
	private String delFlag;
	/**售价*/
	@Excel(name = "售价", width = 15)
    @ApiModelProperty(value = "售价")
	private java.math.BigDecimal price;
	/**兑换券JSON*/
	@Excel(name = "兑换券JSON", width = 15)
    @ApiModelProperty(value = "兑换券JSON")
	private Object coinCertificate;
	/**优惠券JSON*/
	@Excel(name = "优惠券JSON", width = 15)
    @ApiModelProperty(value = "优惠券JSON")
	private Object discountCoupon;
	/**礼品卡JSON*/
	@Excel(name = "礼品卡JSON", width = 15)
    @ApiModelProperty(value = "礼品卡JSON")
	private Object giftCard;
	/**团长奖励*/
	@Excel(name = "团长奖励", width = 15)
    @ApiModelProperty(value = "团长奖励")
	private java.math.BigDecimal headReward;
	/**分红奖励*/
	@Excel(name = "分红奖励", width = 15)
    @ApiModelProperty(value = "分红奖励")
	private java.math.BigDecimal usingBonuses;
	/**本店分红*/
	@Excel(name = "本店分红", width = 15)
    @ApiModelProperty(value = "本店分红")
	private java.math.BigDecimal shareBonus;
	/**推荐奖励*/
	@Excel(name = "推荐奖励", width = 15)
    @ApiModelProperty(value = "推荐奖励")
	private java.math.BigDecimal referralBonuses;
	/**称号奖励JSON*/
	@Excel(name = "称号奖励JSON", width = 15)
    @ApiModelProperty(value = "称号奖励JSON")
	private Object titleReward;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
    @ApiModelProperty(value = "封面图")
	private String surfacePlot;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
	private String shareFigure;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String postersFigure;
	/**详情图*/
	@Excel(name = "详情图", width = 15)
    @ApiModelProperty(value = "详情图")
	private Object detailsFigure;
	/**状态；0：停用；1启用*/
	@Excel(name = "状态；0：停用；1启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1启用")
	private String status;
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;
}
