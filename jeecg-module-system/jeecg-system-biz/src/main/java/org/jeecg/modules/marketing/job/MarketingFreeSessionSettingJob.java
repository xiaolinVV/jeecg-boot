package org.jeecg.modules.marketing.job;


import lombok.extern.java.Log;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionSettingService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 免单场次设置定时器
 */

@Component
@Log
public class MarketingFreeSessionSettingJob implements Job {

    @Autowired
    private IMarketingFreeSessionSettingService iMarketingFreeSessionSettingService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            log.info("自动创次创建定时器5分钟执行一次");
            iMarketingFreeSessionSettingService.autoCreate();
    }
}
