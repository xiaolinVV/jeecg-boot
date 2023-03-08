package org.jeecg.modules.good.dto;

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
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Data
@TableName("good_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="good_list对象", description="商品列表")
public class OpenGoodListDTO {

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
	/**供应链用户id*/
	@Excel(name = "供应链用户id", width = 15)
    @ApiModelProperty(value = "供应链用户id")
	private String sysUserId;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	@TableLogic
	private String delFlag;
	/**平台商品分类id*/
	@Excel(name = "平台商品分类id", width = 15)
    @ApiModelProperty(value = "平台商品分类id")
	private String goodTypeId;
	/**商品主图相对地址（以json的形式存储多张）*/
	@Excel(name = "商品主图相对地址（以json的形式存储多张）", width = 15)
    @ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
	private String mainPicture;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private String goodName;
	/**商品别名（默认与商品名称相同）*/
	@Excel(name = "商品别名（默认与商品名称相同）", width = 15)
    @ApiModelProperty(value = "商品别名（默认与商品名称相同）")
	private String nickName;
	/**商品销售价格*/
	@Excel(name = "商品销售价格", width = 15)
    @ApiModelProperty(value = "商品销售价格")
	private String price;
	/**商品成本价*/
	@Excel(name = "商品成本价", width = 15)
    @ApiModelProperty(value = "商品成本价")
	private String costPrice;
	/**商品供货价，按照利润比例，根据数据字典设置的比例自动填入，供货价比例：supply_price_ratio*/
	@Excel(name = "商品供货价，按照利润比例，根据数据字典设置的比例自动填入，供货价比例：supply_price_ratio", width = 15)
    @ApiModelProperty(value = "商品供货价，按照利润比例，根据数据字典设置的比例自动填入，供货价比例：supply_price_ratio")
	private String supplyPrice;
	/**商品会员价，按照利润比例，根据数据字典设置的比例自动填入，会员价比例：membership_rate*/
	@Excel(name = "商品会员价，按照利润比例，根据数据字典设置的比例自动填入，会员价比例：membership_rate", width = 15)
    @ApiModelProperty(value = "商品会员价，按照利润比例，根据数据字典设置的比例自动填入，会员价比例：membership_rate")
	private String vipPrice;
	/**参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动*/
	@Excel(name = "参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动", width = 15)
    @ApiModelProperty(value = "参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动")
	private String activitiesType;
	/**商品市场价*/
	@Excel(name = "商品市场价", width = 15)
    @ApiModelProperty(value = "商品市场价")
	private String marketPrice;
	/**状态：0：停用；1：启用*/
	@Excel(name = "状态：0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态：0：停用；1：启用")
	private String status;
	/**商品描述*/
	@Excel(name = "商品描述", width = 15)
    @ApiModelProperty(value = "商品描述")
	private String goodDescribe;
	/**商品视频地址*/
	@Excel(name = "商品视频地址", width = 15)
    @ApiModelProperty(value = "商品视频地址")
	private String goodVideo;
	/**商品详情图多张以json的形式存储*/
	@Excel(name = "商品详情图多张以json的形式存储", width = 15)
    @ApiModelProperty(value = "商品详情图多张以json的形式存储")
	private String detailsGoods;
	/**库存*/
	@Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
	private java.math.BigDecimal repertory;
	/**上下架；0：下架；1：上架*/
	@Excel(name = "上下架；0：下架；1：上架", width = 15)
    @ApiModelProperty(value = "上下架；0：下架；1：上架")
	private String frameStatus;
	/**上下架说明*/
	@Excel(name = "上下架说明", width = 15)
    @ApiModelProperty(value = "上下架说明")
	private String frameExplain;
	/**商品编号*/
	@Excel(name = "商品编号", width = 15)
    @ApiModelProperty(value = "商品编号")
	private String goodNo;
	/**状态说明，停用说明*/
	@Excel(name = "状态说明，停用说明", width = 15)
    @ApiModelProperty(value = "状态说明，停用说明")
	private String statusExplain;
	/**规格说明按照json的形式保存*/
	@Excel(name = "规格说明按照json的形式保存", width = 15)
    @ApiModelProperty(value = "规格说明按照json的形式保存")
	private String specification;
	/**有无规格；0：无规格；1：有规格*/
	@Excel(name = "有无规格；0：无规格；1：有规格", width = 15)
    @ApiModelProperty(value = "有无规格；0：无规格；1：有规格")
	private String isSpecification;
	/**运费模板id*/
	@Excel(name = "运费模板id", width = 15)
    @ApiModelProperty(value = "运费模板id")
	private String providerTemplateId;
	/**服务承诺，来自数据字典逗号隔开。0：七天无理由退换货；1：假一赔十；服务承诺：commitment_customers*/
	@Excel(name = "服务承诺，来自数据字典逗号隔开。0：七天无理由退换货；1：假一赔十；服务承诺：commitment_customers", width = 15)
    @ApiModelProperty(value = "服务承诺，来自数据字典逗号隔开。0：七天无理由退换货；1：假一赔十；服务承诺：commitment_customers")
	private String commitmentCustomers;
	/**审核状态：0:草稿；1：待审核；2：审核通过；3：审核不通过*/
	@Excel(name = "审核状态：0:草稿；1：待审核；2：审核通过；3：审核不通过", width = 15)
    @ApiModelProperty(value = "审核状态：0:草稿；1：待审核；2：审核通过；3：审核不通过")
	private String auditStatus;
	/**审核原因*/
	@Excel(name = "审核原因", width = 15)
    @ApiModelProperty(value = "审核原因")
	private String auditExplain;
	/**删除原因*/
	@Excel(name = "删除原因", width = 15)
    @ApiModelProperty(value = "删除原因")
	private String delExplain;
	/**删除时间*/
	@Excel(name = "删除时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "删除时间")
	private java.util.Date delTime;
	/**商品形态；0：常规商品；1：特价商品*/
	@Excel(name = "商品形态；0：常规商品；1：特价商品", width = 15)
    @ApiModelProperty(value = "商品形态；0：常规商品；1：特价商品")
	private String goodForm;
	/**有无销售价区间；0：无；1：有*/
	@Excel(name = "有无销售价区间；0：无；1：有", width = 15)
    @ApiModelProperty(value = "有无销售价区间；0：无；1：有")
	private String priceRange;
	/**销量*/
	@Excel(name = "销量", width = 15)
    @ApiModelProperty(value = "销量")
	private java.math.BigDecimal salesVolume;
	/**最低商品价格*/
	@Excel(name = "最低商品价格", width = 15)
    @ApiModelProperty(value = "最低商品价格")
	private java.math.BigDecimal smallPrice;
	/**最低vip价格*/
	@Excel(name = "最低vip价格", width = 15)
    @ApiModelProperty(value = "最低vip价格")
	private java.math.BigDecimal smallVipPrice;
	/**最低成本价*/
	@Excel(name = "最低成本价", width = 15)
    @ApiModelProperty(value = "最低成本价")
	private java.math.BigDecimal smallCostPrice;
	/**最低供货价*/
	@Excel(name = "最低供货价", width = 15)
    @ApiModelProperty(value = "最低供货价")
	private java.math.BigDecimal smallSupplyPrice;
	/**活动价*/
	@Excel(name = "活动价", width = 15)
    @ApiModelProperty(value = "活动价")
	private java.math.BigDecimal activityPrice;
	/**来源ID*/
	@Excel(name = "来源ID", width = 15)
    @ApiModelProperty(value = "来源ID")
	private String idSource;
	/**来源分类:1:供应链自营产品 2:京东商品 3.普通商品*/
	@Excel(name = "来源分类:1:供应链自营产品 2:京东商品 3.供应链普通商品", width = 15)
    @ApiModelProperty(value = "来源分类:1:供应链自营产品 2:京东商品 3.供应链普通商品")
	private String sourceType;
	/**来源分类:1:供应链自营产品 2:京东商品 3.普通商品*/
	@Excel(name = "来源:1:京东供应链", width = 15)
	@ApiModelProperty(value = "来源:1:京东供应链")
	private String source;
	/**京东图片*/
	@Excel(name = "京东图片", width = 15)
	@ApiModelProperty(value = "京东图片")
	private String originalImg;
	/**京东视频*/
	@Excel(name = "京东视频", width = 15)
	@ApiModelProperty(value = "京东视频")
	private String video;
	/**来源详情图片*/
	@Excel(name = "来源详情图片", width = 15)
	@ApiModelProperty(value = "来源详情图片")
	private String goodsImages;
	/**京东视频*/
	@Excel(name = "规格集合", width = 15)
	@ApiModelProperty(value = "规格集合")
	private List<GoodSpecification> goodSpecificationList;
	/**图片是否已上传:0:未上传,1已上传*/
	private String isImageDownload;
	/**sku对比价url链接*/
	private String skuUrl;
}
