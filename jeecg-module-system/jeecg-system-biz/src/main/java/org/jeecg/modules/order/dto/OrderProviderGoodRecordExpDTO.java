package org.jeecg.modules.order.dto;

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
 * @Description: 供应商订单商品记录
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
@Data
@TableName("order_provider_good_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_provider_good_record对象", description="供应商订单商品记录")
public class OrderProviderGoodRecordExpDTO {
    
	/**主键ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**创建年*/
    @ApiModelProperty(value = "创建年")
	private Integer year;
	/**创建月*/
    @ApiModelProperty(value = "创建月")
	private Integer month;
	/**创建日*/
    @ApiModelProperty(value = "创建日")
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	@TableLogic
	private String delFlag;
	/**供应商订单id*/
    @ApiModelProperty(value = "供应商订单id")
	private String orderProviderListId;
	/**商品主图相对地址（以json的形式存储多张）*/
	@Excel(name = "商品主图", width = 25,type =2,height = 25)
    @ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
	private String mainPicture;
	/**平台商品id（只做对象映射）*/
    @ApiModelProperty(value = "平台商品id（只做对象映射）")
	private String goodListId;
	/**商品规格id（只做对象映射）*/
    @ApiModelProperty(value = "商品规格id（只做对象映射）")
	private String goodSpecificationId;
	/**商品名称*/
	@Excel(name = "商品名称", width = 35)
    @ApiModelProperty(value = "商品名称")
	private String goodName;
	/**
	 * sku单号
	 */
	@ApiModelProperty(value = "sku编码")
	@Excel(name = "sku编码", width = 25)
	private String skuNo;

	/**规格名称，按照顺序逗号隔开*/
	@Excel(name = "规格", width = 35)
    @ApiModelProperty(value = "规格名称，按照顺序逗号隔开")
	private String specification;
	/**商品销售价格*/
    @ApiModelProperty(value = "商品销售价格")
	private BigDecimal price;
	/**商品会员价.*/
    @ApiModelProperty(value = "商品会员价.")
	private BigDecimal vipPrice;
	/**商品供货价*/
    @ApiModelProperty(value = "商品供货价")
	private BigDecimal supplyPrice;
	/**商品成本价*/
	@Excel(name = "供货价", width = 15, needMerge = true)
    @ApiModelProperty(value = "商品成本价")
	private BigDecimal costPrice;
	/**销售单价*/
    @ApiModelProperty(value = "销售单价")
	private BigDecimal unitPrice;
	/**销售数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "销售数量")
	private BigDecimal amount;
	/**总计金额（小计）*/
    @ApiModelProperty(value = "总计金额（小计）")
	private BigDecimal total;
	@ApiModelProperty(value = "重量")
	private BigDecimal weight;
    /**来源分类:1:供应链自营产品 2:京东商品 3.普通商品*/
    @ApiModelProperty(value = "来源分类:1:供应链自营产品 2:京东商品 3.普通商品")
    private String sourceType;
	/**专区id*/
	@ApiModelProperty(value = "专区id")
	private String marketingPrefectureId;
	/**专区标签*/
	@ApiModelProperty(value = "专区标签")
	private String prefectureLabel;
	/**
	 * 会员等级名称
	 */
	@ApiModelProperty(value = "会员等级名称")
	private String memberGrade;
	/**
	 * 会员等级优惠金额
	 */
	@ApiModelProperty(value = "会员等级优惠金额")
	private BigDecimal memberDiscountPrice;
	/**
	 * 会员等级特权信息
	 */
	private String memberGradeContent;

	/**
	 * 2020年9月28日12:41:19
	 * 来源：0：本平台；1：广州海融中聚供应链管理有限公司；
	 */
	private String source;


	/**
	 * 商品编码
	 */
	@Excel(name = "商品编码", width = 25)
	private String goodNo;


}
