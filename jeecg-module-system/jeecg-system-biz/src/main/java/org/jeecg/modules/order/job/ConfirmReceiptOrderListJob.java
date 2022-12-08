package org.jeecg.modules.order.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmReceiptOrderListJob implements Job {
    @Autowired
    private IOrderListService iOrderListService;
    @Autowired
    private IOrderStoreListService iOrderStoreListService;
    /**
     * 定时器确认收货
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(" Jeecg-Boot 确认收货订单定时器任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
        //平台待支付超时订单
        iOrderListService.confirmReceiptOrderListJob();
        //店铺待支付超时订单
        iOrderStoreListService.storeConfirmReceiptOrderList();
    }
}
