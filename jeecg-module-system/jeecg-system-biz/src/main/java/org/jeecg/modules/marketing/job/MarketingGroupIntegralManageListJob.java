package org.jeecg.modules.marketing.job;

import lombok.extern.java.Log;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageRecordService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Log
@DisallowConcurrentExecution
public class MarketingGroupIntegralManageListJob implements Job {
    @Autowired
    private IMarketingGroupIntegralManageRecordService iMarketingGroupIntegralManageRecordService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("拼购奖励分配定时器；3秒运行一次");

        iMarketingGroupIntegralManageRecordService.recordsLiquidation().forEach(idMap->{
            iMarketingGroupIntegralManageRecordService.distributionRewards(idMap.get("id").toString());
        });
    }
}
