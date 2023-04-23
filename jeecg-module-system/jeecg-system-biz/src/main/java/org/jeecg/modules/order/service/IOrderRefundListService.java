package org.jeecg.modules.order.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.order.entity.OrderRefundList;
import com.github.yulichang.base.MPJBaseService;

import java.math.BigDecimal;

/**
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date: 2023-04-20
 * @Version: V1.0
 */
public interface IOrderRefundListService extends MPJBaseService<OrderRefundList> {

    /**
     * 通过id 查询
     *
     * @param id
     * @return
     */
    OrderRefundList getOrderRefundListById(String id);

    /**
     * 售后单退款逻辑处理
     *
     * @param orderRefundList     售后单对象
     * @param actualRefundPrice   实际退款现金
     * @param actualRefundBalance 实际退款余额
     */
    public void refund(OrderRefundList orderRefundList, BigDecimal actualRefundPrice, BigDecimal actualRefundBalance);
}
