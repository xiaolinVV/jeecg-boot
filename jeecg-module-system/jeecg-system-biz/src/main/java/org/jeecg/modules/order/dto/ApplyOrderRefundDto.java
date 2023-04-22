package org.jeecg.modules.order.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 店铺订单售后申请表
 * @Author: jeecg-boot
 * @Date: 2023-04-10
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ApplyOrderRefundDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 0=店铺  1=平台
     */
    private String isPlatform;
    /**
     * 退款类型 0=仅退款 1=退货退款 2=换货
     */
    private String refundType;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 申请说明
     */
    private String remarks;
    /**
     * 退款凭证图片，按照顺序逗号隔开
     */
    private String refundCertificate;

    /**
     * 换货：收货地址id
     */
    private String memberShippingAddressId;

    /**
     * 店铺售后单列表
     */
    private List<OrderRefundListDto> orderRefundListDtos;
}
