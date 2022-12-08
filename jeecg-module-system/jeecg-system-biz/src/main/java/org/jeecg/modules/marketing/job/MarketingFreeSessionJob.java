package org.jeecg.modules.marketing.job;


import lombok.extern.java.Log;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
public class MarketingFreeSessionJob implements Job {

    @Autowired
    private IMarketingFreeSessionService iMarketingFreeSessionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("免单场次自动定时器，5分钟执行一次");
        iMarketingFreeSessionService.autoCreateOrStop();
    }
}
