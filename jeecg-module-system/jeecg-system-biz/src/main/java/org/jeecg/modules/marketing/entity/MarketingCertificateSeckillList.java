package org.jeecg.modules.marketing.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 限时抢券列表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Data
@TableName("marketing_certificate_seckill_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_certificate_seckill_list对象", description="限时抢券列表")
public class MarketingCertificateSeckillList {
    
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
	/**活动列表id*/
	@Excel(name = "活动列表id", width = 15)
    @ApiModelProperty(value = "活动列表id")
	private String marketingCertificateSeckillActivityListId;
	/**兑换券id*/
	@Excel(name = "兑换券id", width = 15)
    @ApiModelProperty(value = "兑换券id")
	private String marketingCertificateId;
	/**活动价*/
	@Excel(name = "活动价", width = 15)
    @ApiModelProperty(value = "活动价")
	private java.math.BigDecimal activityPrice;
	/**是否限购；0：不限购；1：限购*/
	@Excel(name = "是否限购；0：不限购；1：限购", width = 15)
    @ApiModelProperty(value = "是否限购；0：不限购；1：限购")
	private String purchase;
	/**限购数量；0：代表不限购*/
	@Excel(name = "限购数量；0：代表不限购", width = 15)
    @ApiModelProperty(value = "限购数量；0：代表不限购")
	private java.math.BigDecimal purchaseNumber;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
	/**加入时间*/
	@Excel(name = "加入时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "加入时间")
	private Date joinDate;
	@Excel(name = "删除说明", width = 15)
	@ApiModelProperty(value = "删除说明")
	private String delExplain;
}
