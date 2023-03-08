package org.jeecg.modules.good.entity;

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

import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-23
 * @Version: V1.0
 */
@Data
@TableName("good_specification")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="good_specification对象", description="商品规格")
public class GoodSpecification {
    
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
	/**商品id*/
	@Excel(name = "商品id", width = 15)
    @ApiModelProperty(value = "商品id")
	private String goodListId;
	/**商品销售价格*/
	@Excel(name = "商品销售价格", width = 15)
    @ApiModelProperty(value = "商品销售价格")
	@DecimalMax(value="9999999",message="商品销售价格应小于9999999")
	private BigDecimal price;
	/**商品会员价，按照利润比例，根据数据字段设置的比例自动填入*/
	@Excel(name = "商品会员价，按照利润比例，根据数据字段设置的比例自动填入", width = 15)
    @ApiModelProperty(value = "商品会员价，按照利润比例，根据数据字段设置的比例自动填入")
	@DecimalMax(value="9999999",message="商品会员价应小于9999999")
	private BigDecimal vipPrice;
	/**商品成本价*/
	@Excel(name = "商品成本价", width = 15)
    @ApiModelProperty(value = "商品成本价")
	@DecimalMax(value="9999999",message="商品成本价应小于9999999")
	private BigDecimal costPrice;
	/**商品供货价*/
	@Excel(name = "商品供货价", width = 15)
    @ApiModelProperty(value = "商品供货价")
	@DecimalMax(value="9999999",message="商品供货价应小于9999999")
	private BigDecimal supplyPrice;
	/**库存*/
	@Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
	private BigDecimal repertory;
	/**规格名称，按照顺序逗号隔开*/
	@Excel(name = "规格名称，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "规格名称，按照顺序逗号隔开")
	private String specification;
	/**sku编码*/
	@Excel(name = "sku编码", width = 15)
    @ApiModelProperty(value = "sku编码")
	private String skuNo;
	/**重量*/
	@Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
	private BigDecimal weight;
	/**规格图*/
	@Excel(name = "规格图", width = 15)
	@ApiModelProperty(value = "规格图")
	private String specificationPicture;
	/**销量*/
	@Excel(name = "销量", width = 15)
	@ApiModelProperty(value = "销量")
	private String salesVolume;

	private BigDecimal vipTwoPrice;

	private BigDecimal vipThirdPrice;
	private BigDecimal vipFouthPrice;

	/**条形码*/
	@Excel(name = "条形码", width = 15)
	@ApiModelProperty(value = "条形码")
	private String barCode;
}
