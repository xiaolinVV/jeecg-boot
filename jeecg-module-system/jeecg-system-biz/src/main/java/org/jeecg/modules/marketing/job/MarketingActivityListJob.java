package org.jeecg.modules.marketing.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.marketing.service.IMarketingActivityListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MarketingActivityListJob implements Job {

    @Autowired
    private IMarketingActivityListService  iMarketingActivityListService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("线下活动定时器：5分钟运行一次");
        iMarketingActivityListService.updateStatus();
    }
}
