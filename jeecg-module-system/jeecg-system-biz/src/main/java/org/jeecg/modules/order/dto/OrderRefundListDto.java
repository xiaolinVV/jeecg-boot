package org.jeecg.modules.order.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 店铺订单售后申请表
 * @Author: jeecg-boot
 * @Date: 2023-04-10
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "order_store_refund_list对象", description = "店铺订单售后申请表")
public class OrderRefundListDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单商品记录id
     */
    @Excel(name = "订单商品记录id", width = 15)
    @ApiModelProperty(value = "订单商品记录id")
    private String orderGoodRecordId;

    /**
     * 退款原因
     */
    @Excel(name = "退款原因", width = 15)
    @ApiModelProperty(value = "退款原因")
    private String refundReason;
    /**
     * 申请说明
     */
    @Excel(name = "申请说明", width = 15)
    @ApiModelProperty(value = "申请说明")
    private String remarks;
    /**
     * 退款凭证图片，按照顺序逗号隔开
     */
    @Excel(name = "退款凭证图片，按照顺序逗号隔开", width = 15)
    @ApiModelProperty(value = "退款凭证图片，按照顺序逗号隔开")
    private String refundCertificate;
    /**
     * 退款金额
     */
    @Excel(name = "退款金额", width = 15)
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;
    /**
     * 退款数量
     */
    @Excel(name = "退款数量", width = 15)
    @ApiModelProperty(value = "退款数量")
    private BigDecimal refundAmount;

    /**
     * 换货商品规格id
     */
    private java.lang.String exchangeGoodSpecificationId;

    /**
     * 换货规格名称，按照顺序逗号隔开
     */
    private java.lang.String exchangeGoodSpecification;

}
