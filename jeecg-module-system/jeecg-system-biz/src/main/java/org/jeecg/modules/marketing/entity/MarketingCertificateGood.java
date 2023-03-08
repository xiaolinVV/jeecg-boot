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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 兑换券商品映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_certificate_good")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_certificate_good对象", description="兑换券商品映射")
public class MarketingCertificateGood {
    
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
	/**兑换券id*/
	@Excel(name = "兑换券id", width = 15)
    @ApiModelProperty(value = "兑换券id")
	private String marketingCertificateId;
	/**平台商品id*/
	@Excel(name = "平台商品id", width = 15)
    @ApiModelProperty(value = "平台商品id")
	private String goodListId;
	/**0:店铺；1：平台*/
	@Excel(name = "0:店铺；1：平台", width = 15)
	@ApiModelProperty(value = "0:店铺；1：平台")
	private String isPlatform;
	/**可选月份，0：代表全部，其他的代表相应的月*/
	@Excel(name = "可选月份，0：代表全部，其他的代表相应的月", width = 15)
	@ApiModelProperty(value = "可选月份，0：代表全部，其他的代表相应的月")
	private String canMonth;
	/**商品规格id*/
	@Excel(name = "商品规格id", width = 15)
	@ApiModelProperty(value = "商品规格id")
	private String goodSpecificationId;
	/**商品数量*/
	@Excel(name = "商品数量", width = 15)
	@ApiModelProperty(value = "商品数量")
	private BigDecimal quantity;
}
