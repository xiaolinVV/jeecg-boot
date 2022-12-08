package org.jeecg.modules.marketing.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MarketingAdvertisingJob implements Job {
    @Autowired
    private IMarketingAdvertisingService iMarketingAdvertisingService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(" 每五分钟检查推荐广告是否过期 !  时间:" + DateUtils.getTimestamp());
        iMarketingAdvertisingService.getMarketingAdvertisingOvertime();
    }
}
