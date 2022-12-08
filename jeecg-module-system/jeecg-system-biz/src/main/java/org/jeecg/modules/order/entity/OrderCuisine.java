package org.jeecg.modules.order.entity;

import java.io.Serializable;
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
 * @Description: 菜单订单
 * @Author: jeecg-boot
 * @Date:   2022-05-22
 * @Version: V1.0
 */
@Data
@TableName("order_cuisine")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_cuisine对象", description="菜单订单")
public class OrderCuisine {
    
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
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private String serialNumber;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**桌号*/
	@Excel(name = "桌号", width = 15)
    @ApiModelProperty(value = "桌号")
	private String tableNumber;
	/**人数*/
	@Excel(name = "人数", width = 15)
    @ApiModelProperty(value = "人数")
	private java.math.BigDecimal numberPeople;
	/**商品数量*/
	@Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
	private java.math.BigDecimal quantity;
	/**应付款*/
	@Excel(name = "应付款", width = 15)
    @ApiModelProperty(value = "应付款")
	private java.math.BigDecimal accountPayable;
	/**结算方式；0：未结算；1：客户结算；2：商户结算*/
	@Excel(name = "结算方式；0：未结算；1：客户结算；2：商户结算", width = 15)
    @ApiModelProperty(value = "结算方式；0：未结算；1：客户结算；2：商户结算")
	private String clearingForm;
	/**状态；0：代付款；1：已付款；2：取消*/
	@Excel(name = "状态；0：代付款；1：已付款；2：取消", width = 15)
    @ApiModelProperty(value = "状态；0：代付款；1：已付款；2：取消")
	private String status;
	/**优惠*/
	@Excel(name = "优惠", width = 15)
    @ApiModelProperty(value = "优惠")
	private java.math.BigDecimal discounts;
	/**实付款*/
	@Excel(name = "实付款", width = 15)
    @ApiModelProperty(value = "实付款")
	private java.math.BigDecimal actualPayment;
	/**付款时间*/
	@Excel(name = "付款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付款时间")
	private Date payTime;
}
