package org.jeecg.modules.marketing.job;


import lombok.extern.java.Log;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupRecordService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
@DisallowConcurrentExecution
public class MarketingZoneGroupManageJob implements Job {

    @Autowired
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("专区团定时器5秒运行一次");
            iMarketingZoneGroupRecordService.recordsLiquidation().forEach(idMap->{
                iMarketingZoneGroupRecordService.distributionRewards(idMap.get("id").toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
