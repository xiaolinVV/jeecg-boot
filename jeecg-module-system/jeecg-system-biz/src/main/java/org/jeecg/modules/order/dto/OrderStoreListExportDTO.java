package org.jeecg.modules.order.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@TableName("order_list")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderStoreListExportDTO {
    private String id;//订单Id
    private String phone;//会员电话 (买家账号)
    private String memberType;//会员类型
    private String  consignee;//收货人
    private String  contactNumber;//联系电话
    private String shippingAddress;//收货地址
    private String message;//留言信息
    private String name;//供应商
    private String providerOrderNo;//供应商品订单编号
    private String  goodNo;//商品编号
    private String goodName;//商品名称
    private BigDecimal unitPrice;//单价（销售单价）
    private BigDecimal amount;//数量
    private BigDecimal goodsTotal;//商品总价
    private BigDecimal shipFee;//配送费用
    private BigDecimal  welfarePayments;//福利金抵扣
    private BigDecimal coupon;//优惠券抵扣
    private BigDecimal vipLowerTotal;//会员直抵
    private BigDecimal  actualPayment;//实付款
    private BigDecimal  giveWelfarePayments;//赠送福利金
    //private String promoterName;//推广人
    //private String affiliationStore;//归属店铺
    //private String  distributionChannelName;//销售渠道
    private String status;//订单状态
    private String orderNo;//订单编号
    private String serialNumber;//交易流水号
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;//创建时间
    @Excel(name = "付款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付款时间")
    private Date payTime;//付款时间
    @Excel(name = "发货时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发货时间")
    private Date shipmentsTime;//发货时间
    @Excel(name = "收货时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "收货时间")
    private Date deliveryTime;//收货时间
    @Excel(name = "完成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完成时间")
    private Date completionTime;//完成时间
    @Excel(name = "关闭时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "关闭时间")
    private Date closeTime; //关闭时间
    private String oplId;//供应商订单id
    private BigDecimal oplsGoodsTotal;//供应商商品总价
    private BigDecimal oplsShipFee;//供应商配送费用
    private BigDecimal oplsCustomaryDues;//供应商平台实付款项
    private String oplsStatus;//供应商订单状态
    private String storeName;//店铺名称

}
