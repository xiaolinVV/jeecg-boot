package org.jeecg.modules.marketing.job;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupRecord;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log
public class MarketingZoneGroupRecordJob implements Job {

    @Autowired
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("分级奖励分配定时每天凌晨1点执行");
            while (true){
                List<MarketingZoneGroupRecord> marketingZoneGroupRecords=iMarketingZoneGroupRecordService.list(new LambdaQueryWrapper<MarketingZoneGroupRecord>()
                        .eq(MarketingZoneGroupRecord::getDistributionRewards,"1")
                        .eq(MarketingZoneGroupRecord::getClassificationReward,"0")
                        .orderByDesc(MarketingZoneGroupRecord::getCreateTime)
                        .last("limit 50"));
                if(marketingZoneGroupRecords.size()==0){
                    break;
                }
                marketingZoneGroupRecords.forEach(m->{
                    iMarketingZoneGroupRecordService.classificationReward(m.getId());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
