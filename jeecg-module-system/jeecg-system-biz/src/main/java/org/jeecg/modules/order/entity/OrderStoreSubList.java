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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 店铺包裹订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Data
@TableName("order_store_sub_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_store_sub_list对象", description="店铺包裹订单列表")
public class OrderStoreSubList {
    
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
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**店铺订单id*/
	@Excel(name = "店铺订单id", width = 15)
    @ApiModelProperty(value = "店铺订单id")
	private String orderStoreListId;
	/**店铺id*/
	@Excel(name = "店铺id", width = 15)
    @ApiModelProperty(value = "店铺id")
	private String sysUserId;
	/**订单ID*/
	@Excel(name = "订单ID", width = 15)
    @ApiModelProperty(value = "订单ID")
	private String orderNo;
	/**快递*/
	@Excel(name = "快递", width = 15)
    @ApiModelProperty(value = "快递")
	@Dict(dicCode = "store_distribution_type")
	private String distribution;
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
	/**总数*/
	@Excel(name = "总数", width = 15)
    @ApiModelProperty(value = "总数")
	private java.math.BigDecimal amount;
	/**配送金额*/
	@Excel(name = "配送金额", width = 15)
    @ApiModelProperty(value = "配送金额")
	private java.math.BigDecimal shipFee;
	/**父订单id；如果没有父订单，显示：0*/
	@Excel(name = "父订单id；如果没有父订单，显示：0", width = 15)
    @ApiModelProperty(value = "父订单id；如果没有父订单，显示：0")
	private String parentId;
	/**物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；*/
	@Excel(name = "物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；", width = 15,dicCode = "logistics_company")
    @ApiModelProperty(value = "物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；")
	@Dict(dicCode = "logistics_company")
	private String logisticsCompany;
	/**快递单号*/
	@Excel(name = "快递单号", width = 15)
    @ApiModelProperty(value = "快递单号")
	private String trackingNumber;
	/**店铺发货地址id*/
	@Excel(name = "店铺发货地址id", width = 15)
    @ApiModelProperty(value = "店铺发货地址id")
	private String storeAddressIdSender;
	/**店铺商退货地址id*/
	@Excel(name = "店铺商退货地址id", width = 15)
    @ApiModelProperty(value = "店铺商退货地址id")
	private String storeAddressIdTui;
	/**物流跟踪信息的json保存（每次查询的时候更新）*/
	@Excel(name = "物流跟踪信息的json保存（每次查询的时候更新）", width = 15)
    @ApiModelProperty(value = "物流跟踪信息的json保存（每次查询的时候更新）")
	private String logisticsTracking;
	/**商品总价*/
	@Excel(name = "商品总价", width = 15)
    @ApiModelProperty(value = "商品总价")
	private java.math.BigDecimal goodsTotal;
	/**应付款（平台应付款项）*/
	@Excel(name = "应付款（平台应付款项）", width = 15)
    @ApiModelProperty(value = "应付款（平台应付款项）")
	private java.math.BigDecimal customaryDues;
	/**平台实付款项*/
	@Excel(name = "平台实付款项", width = 15)
    @ApiModelProperty(value = "平台实付款项")
	private java.math.BigDecimal actualPayment;
	/**订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败*/
	@Excel(name = "订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败", width = 15)
    @ApiModelProperty(value = "订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败")
	private String status;
}
