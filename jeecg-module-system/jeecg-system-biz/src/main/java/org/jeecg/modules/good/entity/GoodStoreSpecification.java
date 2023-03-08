package org.jeecg.modules.good.entity;

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
 * @Description: 店铺商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
@Data
@TableName("good_store_specification")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="good_store_specification对象", description="店铺商品规格")
public class GoodStoreSpecification {
    
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
	/**商品id*/
	@Excel(name = "商品id", width = 15)
    @ApiModelProperty(value = "商品id")
	private String goodStoreListId;
	/**商品销售价格*/
	@Excel(name = "商品销售价格", width = 15)
    @ApiModelProperty(value = "商品销售价格")
	private java.math.BigDecimal price;
	/**商品会员价，按照利润比例，根据数据字段设置的比例自动填入*/
	@Excel(name = "商品会员价，按照利润比例，根据数据字段设置的比例自动填入", width = 15)
    @ApiModelProperty(value = "商品会员价，按照利润比例，根据数据字段设置的比例自动填入")
	private java.math.BigDecimal vipPrice;
	/**商品成本价*/
	@Excel(name = "商品成本价", width = 15)
    @ApiModelProperty(value = "商品成本价")
	private java.math.BigDecimal costPrice;
	/**库存*/
	@Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
	private java.math.BigDecimal repertory;
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
	private java.math.BigDecimal weight;
    /**销量*/
    @Excel(name = "销量", width = 15)
    @ApiModelProperty(value = "销量")
    private String salesVolume;
	/**规格图*/
	@Excel(name = "规格图", width = 15)
	@ApiModelProperty(value = "规格图")
	private String specificationPicture;
}
