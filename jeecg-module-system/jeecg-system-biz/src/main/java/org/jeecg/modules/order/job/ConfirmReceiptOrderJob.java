package org.jeecg.modules.order.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmReceiptOrderJob implements Job{
    @Autowired
    private IOrderListService iOrderListService;

    @Autowired
    private IOrderStoreListService iOrderStoreListService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        iOrderListService.confirmReceiptOrderJob();

        iOrderStoreListService.confirmReceiptOrderJob();
    }
}
