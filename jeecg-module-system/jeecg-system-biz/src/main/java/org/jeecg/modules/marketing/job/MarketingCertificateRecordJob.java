package org.jeecg.modules.marketing.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.service.IMarketingCertificateRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MarketingCertificateRecordJob implements Job {
    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(" 每五分钟修改一次优惠券状态 !  时间:" + DateUtils.getTimestamp());
        iMarketingCertificateRecordService.updateMarketingCertificateRecordJob();
    }
}
