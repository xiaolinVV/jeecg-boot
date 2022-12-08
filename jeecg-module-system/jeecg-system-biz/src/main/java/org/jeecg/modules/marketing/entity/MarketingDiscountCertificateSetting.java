package org.jeecg.modules.marketing.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 券中心设置
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Data
@TableName("marketing_discount_certificate_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_discount_certificate_setting对象", description="券中心设置")
public class MarketingDiscountCertificateSetting {
    
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
	private String delFlag;
	/**券中心别名*/
	@Excel(name = "券中心别名", width = 15)
    @ApiModelProperty(value = "券中心别名")
	private String name;
	/**客户端显示: 0:全部 1:仅app 2:仅小程序*/
	@Excel(name = "客户端显示: 0:全部 1:仅app 2:仅小程序", width = 15)
    @ApiModelProperty(value = "客户端显示: 0:全部 1:仅app 2:仅小程序")
	private String clientSideShow;
	/**券中心图标*/
	@Excel(name = "券中心图标", width = 15)
    @ApiModelProperty(value = "券中心图标")
	private String ticketIcon;
	/**默认分享图*/
	@Excel(name = "默认分享图", width = 15)
    @ApiModelProperty(value = "默认分享图")
	private String inviteFigure;
	/**默认海报图*/
	@Excel(name = "默认海报图", width = 15)
    @ApiModelProperty(value = "默认海报图")
	private String coverPlan;
	/**推荐限时抢券数*/
	@Excel(name = "推荐限时抢券数", width = 15)
	@ApiModelProperty(value = "推荐限时抢券数")
	private Integer recommendSeckillNumber;
	/**推荐拼好券数*/
	@Excel(name = "推荐拼好券数", width = 15)
	@ApiModelProperty(value = "推荐拼好券数")
	private Integer recommendGroupNumber;
	/**推荐超值兑换券数 */
	@Excel(name = "推荐超值兑换券数 ", width = 15)
	@ApiModelProperty(value = "推荐超值兑换券数 ")
	private Integer recommendCertificateNumber;
	/**券中心封面图*/
	@Excel(name = "券中心封面图", width = 15)
	@ApiModelProperty(value = "券中心封面图")
	private String ticketSurfacePlot;
	/**券中心广告图*/
	@Excel(name = "券中心广告图", width = 15)
	@ApiModelProperty(value = "券中心广告图")
	private String ticketAdvertisementBanner;
}
