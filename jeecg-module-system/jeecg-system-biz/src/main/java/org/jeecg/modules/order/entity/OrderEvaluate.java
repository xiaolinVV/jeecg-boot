package org.jeecg.modules.order.entity;

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
 * @Description: 平台商品评价
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
@Data
@TableName("order_evaluate")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_evaluate对象", description="平台商品评价")
public class OrderEvaluate {
    
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
	/**平台订单id*/
	@Excel(name = "平台订单id", width = 15)
    @ApiModelProperty(value = "平台订单id")
	private String orderListId;
	/**供应商订单id*/
	@Excel(name = "供应商订单id", width = 15)
    @ApiModelProperty(value = "供应商订单id")
	private String orderProviderListId;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**平台商品id*/
	@Excel(name = "平台商品id", width = 15)
    @ApiModelProperty(value = "平台商品id")
	private String goodListId;
	/**平台商品规格id*/
	@Excel(name = "平台商品规格id", width = 15)
    @ApiModelProperty(value = "平台商品规格id")
	private String goodSpecificationId;
	/**评价内容*/
	@Excel(name = "评价内容", width = 15)
    @ApiModelProperty(value = "评价内容")
	private String content;
	/**评价图片*/
	@Excel(name = "评价图片", width = 15)
    @ApiModelProperty(value = "评价图片")
	private String pictures;
	/**评价星级*/
	@Excel(name = "评价星级", width = 15)
    @ApiModelProperty(value = "评价星级")
	private java.math.BigDecimal descriptionStar;
	/**状态：0：未审核；1：审核通过；2：审核不通过*/
	@Excel(name = "状态：0：未审核；1：审核通过；2：审核不通过", width = 15)
	@ApiModelProperty(value = "状态：0：未审核；1：审核通过；2：审核不通过")
	private String status;
	/**不通过原因*/
	@Excel(name = "不通过原因", width = 15)
	@ApiModelProperty(value = "不通过原因")
	private String closeExplain;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15)
	@ApiModelProperty(value = "审核时间")
	private java.util.Date auditTime;
}
