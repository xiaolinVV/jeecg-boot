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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 直播管理-抽奖奖品
 * @Author: jeecg-boot
 * @Date:   2021-09-14
 * @Version: V1.0
 */
@Data
@TableName("marketing_live_prize")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_live_prize对象", description="直播管理-抽奖奖品")
public class MarketingLivePrize {
    
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
	/**奖品类型；0：礼品；1：优惠券*/
	@Excel(name = "奖品类型；0：礼品；1：优惠券", width = 15)
    @ApiModelProperty(value = "奖品类型；0：礼品；1：优惠券")
	private String prizeType;
	/**奖品名称*/
	@Excel(name = "奖品名称", width = 15)
    @ApiModelProperty(value = "奖品名称")
	private String prizeName;
	/**奖品图片*/
	@Excel(name = "奖品图片", width = 15)
    @ApiModelProperty(value = "奖品图片")
	private String prizeImage;
	/**奖品实际库存*/
	@Excel(name = "奖品实际库存", width = 15)
    @ApiModelProperty(value = "奖品实际库存")
	private java.math.BigDecimal repertory;
	/**超库存；0：不允许；1：允许*/
	@Excel(name = "超库存；0：不允许；1：允许", width = 15)
    @ApiModelProperty(value = "超库存；0：不允许；1：允许")
	private String superInventory;
	/**优惠券id*/
	@Excel(name = "优惠券id", width = 15)
    @ApiModelProperty(value = "优惠券id")
	private String marketingDiscountId;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
    @ApiModelProperty(value = "删除说明")
	private String delExplain;
	/**奖品编号*/
	@Excel(name = "奖品编号", width = 15)
	@ApiModelProperty(value = "奖品编号")
	private String prizeSerialNumber;
}
