package org.jeecg.modules.order.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@Component
public class ReturnsTimeoutRefundOrderListJob implements Job {

    @Autowired
    private IOrderRefundListService orderRefundListService;

    /**
     * 退货超时定时器
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(" Jeecg-Boot 退货超时售后单任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
//        退货超时售后单
        orderRefundListService.cancelReturnsTimeoutRefundOrderJob();
    }

}
