package org.jeecg.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

import java.util.Date;

/**
 * @Description: 店铺订单运费模板信息表
 * @Author: jeecg-boot
 * @Date:   2020-06-22
 * @Version: V1.0
 */
@Data
@TableName("order_store_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_store_template对象", description="店铺订单运费模板信息表")
public class OrderStoreTemplate {
    
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
	private String delFlag;
	/**店铺订单id*/
	@Excel(name = "店铺订单id", width = 15)
    @ApiModelProperty(value = "店铺订单id")
	private String orderStoreListId;
	/**店铺id*/
	@Excel(name = "店铺id", width = 15)
    @ApiModelProperty(value = "店铺id")
	private String sysUserId;
	/**店铺子订单id*/
	@Excel(name = "店铺子订单id", width = 15)
    @ApiModelProperty(value = "店铺子订单id")
	private String orderStoreSubListId;
	/**店铺运费模板id*/
	@Excel(name = "店铺运费模板id", width = 15)
    @ApiModelProperty(value = "店铺运费模板id")
	private String storeTemplateId;
	/**运费模板名称*/
	@Excel(name = "运费模板名称", width = 15)
    @ApiModelProperty(value = "运费模板名称")
	private String templateName;
	/**计费规则*/
	@Excel(name = "计费规则", width = 15)
    @ApiModelProperty(value = "计费规则")
	private String accountingRules;
	/**计费方式*/
	@Excel(name = "计费方式", width = 15)
    @ApiModelProperty(value = "计费方式")
	private String chargeMode;
	/**配送金额*/
	@Excel(name = "配送金额", width = 15)
    @ApiModelProperty(value = "配送金额")
	private java.math.BigDecimal shipFee;
	/**订单商品数量*/
	@Excel(name = "订单商品数量", width = 15)
    @ApiModelProperty(value = "订单商品数量")
	private java.math.BigDecimal quantity;
}
