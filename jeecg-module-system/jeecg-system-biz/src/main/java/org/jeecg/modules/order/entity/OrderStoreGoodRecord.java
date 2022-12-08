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

import java.math.BigDecimal;

/**
 * @Description: 店铺订单商品记录列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Data
@TableName("order_store_good_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_store_good_record对象", description="店铺订单商品记录列表")
public class OrderStoreGoodRecord {
    
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
	/**供应商订单id*/
	@Excel(name = "供应商订单id", width = 15)
    @ApiModelProperty(value = "供应商订单id")
	private String orderStoreSubListId;
	/**商品主图相对地址（以json的形式存储多张）*/
	@Excel(name = "商品主图相对地址（以json的形式存储多张）", width = 15)
    @ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
	private String mainPicture;
	/**平台商品id（只做对象映射）*/
	@Excel(name = "平台商品id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "平台商品id（只做对象映射）")
	private String goodStoreListId;
	/**商品规格id（只做对象映射）*/
	@Excel(name = "商品规格id（只做对象映射）", width = 15)
    @ApiModelProperty(value = "商品规格id（只做对象映射）")
	private String goodStoreSpecificationId;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private String goodName;
	/**规格名称，按照顺序逗号隔开*/
	@Excel(name = "规格名称，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "规格名称，按照顺序逗号隔开")
	private String specification;
	/**商品销售价格*/
	@Excel(name = "商品销售价格", width = 15)
    @ApiModelProperty(value = "商品销售价格")
	private BigDecimal price;
	/**商品会员价.*/
	@Excel(name = "商品会员价.", width = 15)
    @ApiModelProperty(value = "商品会员价.")
	private BigDecimal vipPrice;
	/**商品成本价*/
	@Excel(name = "商品成本价", width = 15)
    @ApiModelProperty(value = "商品成本价")
	private BigDecimal costPrice;
	/**销售单价*/
	@Excel(name = "销售单价", width = 15)
    @ApiModelProperty(value = "销售单价")
	private BigDecimal unitPrice;
	/**销售数量*/
	@Excel(name = "销售数量", width = 15)
    @ApiModelProperty(value = "销售数量")
	private BigDecimal amount;
	/**总计金额（小计）*/
	@Excel(name = "总计金额（小计）", width = 15)
    @ApiModelProperty(value = "总计金额（小计）")
	private BigDecimal total;
	@Excel(name = "重量", width = 15)
	@ApiModelProperty(value = "重量")
	private BigDecimal weight;
}
