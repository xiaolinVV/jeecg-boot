package org.jeecg.modules.order.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.order.dto.ApplyOrderRefundDto;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 张少林
 * @date 2023年04月10日 10:36 下午
 */
@RestController
@RequestMapping(value = "/after/order/refund")
public class AfterOrderRefundController {

    @Autowired
    private IOrderStoreGoodRecordService orderStoreGoodRecordService;

    /**
     * 申请订单售后
     *
     * @param applyOrderRefundDto
     * @param request
     * @return
     */
    @PostMapping(value = "/apply")
    public Result<String> applyOrderRefund(ApplyOrderRefundDto applyOrderRefundDto, HttpServletRequest request) {
        String memberId = Convert.toStr(request.getAttribute("memberId"));
        if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "0")) {
            OrderStoreGoodRecord orderStoreGoodRecord = orderStoreGoodRecordService.getById(applyOrderRefundDto.getOrderStoreGoodRecordId());
            if (orderStoreGoodRecord == null) {
                throw new JeecgBootException("订单商品信息不存在");
            }




        } else if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "1")) {

        }

        return Result.OK();
    }



}
