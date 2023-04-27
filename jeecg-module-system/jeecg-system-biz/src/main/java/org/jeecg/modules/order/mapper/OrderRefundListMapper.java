package org.jeecg.modules.order.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.entity.OrderRefundList;

/**
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date: 2023-04-20
 * @Version: V1.0
 */
public interface OrderRefundListMapper extends MPJBaseMapper<OrderRefundList> {

    /**
     * 返回售后单退货计时器(秒)
     *
     * @param id 售后单id
     * @return
     */
    String getRefundOrderTimer(@Param("id") String id,@Param("hour") String hour);

}
