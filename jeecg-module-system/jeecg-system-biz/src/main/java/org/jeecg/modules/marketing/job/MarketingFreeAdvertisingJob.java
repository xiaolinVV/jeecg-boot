package org.jeecg.modules.marketing.job;


import lombok.extern.java.Log;
import org.jeecg.modules.marketing.service.IMarketingFreeAdvertisingService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 免单专区广告定时器
 */
@Component
@Log
public class MarketingFreeAdvertisingJob implements Job {

    @Autowired
    private IMarketingFreeAdvertisingService iMarketingFreeAdvertisingService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("免单专区广告定时器：每5分钟运行一次");
        iMarketingFreeAdvertisingService.stopMarketingFreeAdvertising();
    }
}
