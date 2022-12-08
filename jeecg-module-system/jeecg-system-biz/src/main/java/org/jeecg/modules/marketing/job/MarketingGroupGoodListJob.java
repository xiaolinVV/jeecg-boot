package org.jeecg.modules.marketing.job;


import lombok.extern.java.Log;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
public class MarketingGroupGoodListJob implements Job {

    @Autowired
    private IMarketingGroupGoodListService iMarketingGroupGoodListService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("拼团商品超时定时器每5分钟运行一次");
        iMarketingGroupGoodListService.stopMarketingGroupGoodList();
    }
}
