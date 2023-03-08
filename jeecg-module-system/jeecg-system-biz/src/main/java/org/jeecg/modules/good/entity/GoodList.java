package org.jeecg.modules.good.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date:   2019-10-18
 * @Version: V1.0
 */
@Data
@TableName("good_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="good_list对象", description="商品列表")
@Valid
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class GoodList {

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
	//@NotEmpty(message = "平台商品分类不能为空")
	private String goodTypeId;
	/**商品主图相对地址（以json的形式存储多张）*/
	@Excel(name = "商品主图相对地址（以json的形式存储多张）", width = 15)
	@ApiModelProperty(value = "商品主图相对地址（以json的形式存储多张）")
	//@NotEmpty(message = "商品主图不能为空")
	private String mainPicture;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
	@ApiModelProperty(value = "商品名称")
	private String goodName;
	/**商品别名（默认与商品名称相同）*/
	@Excel(name = "商品别名（默认与商品名称相同）", width = 15)
	@ApiModelProperty(value = "商品别名（默认与商品名称相同）")
	private String nickName;
	/**商品市场价*/
	@Excel(name = "商品市场价", width = 15)
	@ApiModelProperty(value = "商品市场价")
	//@NotEmpty(message = "商品市场价不能为空")
	private String marketPrice;
	/**状态：0：停用；1：启用*/
	@Excel(name = "状态：0：停用；1：启用", width = 15)
	@ApiModelProperty(value = "状态：0：停用；1：启用")
	/*	@TableField("'status'")*/
	private String status;
	/**商品描述*/
	@Excel(name = "商品描述", width = 15)
	@ApiModelProperty(value = "商品描述")
	/*	@TableField("'describe'")*/
	private String goodDescribe;
	/**商品视频地址*/
	@Excel(name = "商品视频地址", width = 15)
	@ApiModelProperty(value = "商品视频地址")
	private String goodVideo;
	/**商品详情图多张以json的形式存储*/
	@Excel(name = "商品详情图多张以json的形式存储", width = 15)
	@ApiModelProperty(value = "商品详情图多张以json的形式存储")
	private String detailsGoods;
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
	//@NotEmpty(message = "运费模板不能为空")
	private String providerTemplateId;
	/**服务承诺，来自数据字段逗号隔开*/
	@Excel(name = "服务承诺，来自数据字段逗号隔开", width = 15)
	@ApiModelProperty(value = "服务承诺，来自数据字段逗号隔开")
	//@NotEmpty(message = "服务承诺不能为空")
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

	/**来源:1:京东供应链*/
	@Excel(name = "来源:1:京东供应链", width = 15)
	@ApiModelProperty(value = "来源:1:京东供应链")
	private String source;


	/**来源分类:1:供应链自营产品 2:京东商品 3.普通商品*/
	@Excel(name = "来源分类:1:供应链自营产品 2:京东商品 3.普通商品", width = 15)
	@ApiModelProperty(value = "来源分类:1:供应链自营产品 2:京东商品 3.普通商品")
	private String sourceType;

	/**
	 * 分端显示；0：全部；1：小程序；2：app
	 */
	private String pointsDisplay;
	/**
	 * 排序
	 */
	private BigDecimal sort;


	private String specifications;


	private String goodBrandId;


	private String goodMachineBrandIds;

	private String goodMachineModelIds;

	private String goodMachineBrandNames;

	private String goodMachineModelNames;

	private String searchInfo;

}
