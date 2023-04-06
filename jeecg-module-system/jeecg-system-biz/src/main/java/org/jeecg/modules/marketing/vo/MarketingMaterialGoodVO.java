package org.jeecg.modules.marketing.vo;

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
 * @Description: 素材商品关系表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Data
@TableName("marketing_material_good")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_material_good对象", description="素材商品关系表")
public class MarketingMaterialGoodVO {

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
	/**素材列表id*/
	@Excel(name = "素材列表id", width = 15)
    @ApiModelProperty(value = "素材列表id")
	private String marketingMaterialListId;
	/**商品列表id*/
	@Excel(name = "商品列表id", width = 15)
    @ApiModelProperty(value = "商品列表id")
	private String goodListId;


	/**新增*/
	/**商品主图相对地址（以json的形式存储多张）*/
	private String mainPicture;
	/**商品名称*/
	private String goodName;
    /**商品分类*/
	private String goodTypeNameSan;//分类-分类-分类
	/**商品分类*/
	private String goodTypeNames;//分类-分类-分类
	/**商品市场价*/
	private String marketPrice;

	/**商品销售价格*/
	private String price;

	/**商品会员价，按照利润比例，根据数据字段设置的比例自动填入*/
	private String vipPrice;

	private String realname;//供应商名称

}