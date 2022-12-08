package org.jeecg.modules.order.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
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
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 供应商订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Data
@TableName("order_provider_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "order_provider_list对象", description = "供应商订单列表")
public class OrderProviderListExpDTO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    /**
     * 创建年
     */
    @ApiModelProperty(value = "创建年")
    private Integer year;
    /**
     * 创建月
     */
    @ApiModelProperty(value = "创建月")
    private Integer month;
    /**
     * 创建日
     */
    @ApiModelProperty(value = "创建日")
    private Integer day;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private String delFlag;
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String memberListId;
    /**
     * 平台订单id
     */
    @ApiModelProperty(value = "平台订单id")
    private String orderListId;
    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private String sysUserId;
    /**
     * 订单ID
     */
    @Excel(name = "供货商订单号", width = 30, needMerge = true)
    @ApiModelProperty(value = "供货商订单号")
    private String orderNo;
    /**
     * 快递
     */
    @ApiModelProperty(value = "快递")
    @Dict(dicCode = "oder_distribution")
    private String distribution;
    /**
     * 供应商运费模板id
     */
    @ApiModelProperty(value = "供应商运费模板id")
    private String providerTemplateId;
    /**
     * 运费模板名称
     */
    @ApiModelProperty(value = "运费模板名称")
    private String templateName;
    /**
     * 计费规则
     */
    @ApiModelProperty(value = "计费规则")
    private String accountingRules;
    /**
     * 计费方式
     */
    @ApiModelProperty(value = "计费方式")
    private String chargeMode;
    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private BigDecimal amount;
    /**
     * 配送金额
     */
    @Excel(name = "运费", width = 15, needMerge = true)
    @ApiModelProperty(value = "配送金额")
    private BigDecimal shipFee;
    /**
     * 父订单id；如果没有父订单，显示：0
     */
    @ApiModelProperty(value = "父订单id；如果没有父订单，显示：0")
    private String parentId;
    /**
     * 物流公司；对应数据字典
     */
    @Excel(name = "物流公司", width = 15, needMerge = true)
    @ApiModelProperty(value = "物流公司；对应数据字典")
    @Dict(dicCode = "logistics_company")
    private String logisticsCompany;
    /**
     * 快递单号
     */
    @Excel(name = "快递单号", width = 30, needMerge = true)
    @ApiModelProperty(value = "快递单号")
    private String trackingNumber;
    /**
     * 供应商发货地址id
     */
    @ApiModelProperty(value = "供应商发货地址id")
    private String providerAddressIdSender;
    /**
     * 供应商退货地址id
     */
    @ApiModelProperty(value = "供应商退货地址id")
    private String providerAddressIdTui;
    /**
     * 物流跟踪信息的json保存（每次查询的时候更新）
     */
    @ApiModelProperty(value = "物流跟踪信息的json保存（每次查询的时候更新）")
    private String logisticsTracking;
    /**
     * 商品总价
     */
    @ApiModelProperty(value = "商品总价")
    private BigDecimal goodsTotal;
    /**
     * 商品总成本价（供货价）
     */
   @Excel(name = "供货价", width = 15, needMerge = true)
    @ApiModelProperty(value = "商品总成本价（供货价）")
    private BigDecimal goodsTotalCost;
    /**
     * 应付款（平台应付款项）
     */
    @ApiModelProperty(value = "应付款（平台应付款项）")
    private BigDecimal customaryDues;
    /**
     * 平台实付款项
     */
    @ApiModelProperty(value = "平台实付款项")
    private BigDecimal actualPayment;
    /**
     * 订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败
     */
    @ApiModelProperty(value = "订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败")
    private String status;

    /**
     * 订单是否发送到中间件:0:未发送;1已发送
     */
    @ApiModelProperty(value = "订单是否发送到中间件:0:未发送;1已发送")
    private String isSend;
    /**
     * 会员等级名称
     */
    @ApiModelProperty(value = "会员等级名称")
    private String memberGrade;
    /**
     * 会员等级优惠总金额
     */
    @ApiModelProperty(value = "会员等级优惠总金额")
    private BigDecimal memberDiscountPriceTotal;
    /**
     * 会员等级特权信息
     */
    private String memberGradeContent;
    /**
     * 淘宝1688订单编号
     */
    private String taoOrderId;

    @Excel(name = "供应商", width = 25, needMerge = true)
    private String providerName;


    @ExcelCollection(name = "")
    private List<OrderProviderGoodRecordExpDTO> orderProviderGoodRecordExpDTOs;

}
