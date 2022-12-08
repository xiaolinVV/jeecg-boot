package org.jeecg.modules.marketing.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingPrefectureService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MarketingAdvertisingPrefectureJob implements Job {
    @Autowired
    private IMarketingAdvertisingPrefectureService iMarketingAdvertisingPrefectureService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(" 每五分钟检查专区广告是否过期 !  时间:" + DateUtils.getTimestamp());
        iMarketingAdvertisingPrefectureService.getMarketingAdvertisingPrefectureOvertime();
    }
}
