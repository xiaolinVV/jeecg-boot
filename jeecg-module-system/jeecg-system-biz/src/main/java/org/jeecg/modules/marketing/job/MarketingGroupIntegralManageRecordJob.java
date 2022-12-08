package org.jeecg.modules.marketing.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManageRecord;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Log
public class MarketingGroupIntegralManageRecordJob implements Job {

    @Autowired
    private IMarketingGroupIntegralManageRecordService iMarketingGroupIntegralManageRecordService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("分级奖励定时器：每天凌晨1点运行");
        while (true){
            List<MarketingGroupIntegralManageRecord> marketingGroupIntegralManageRecords=iMarketingGroupIntegralManageRecordService.list(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                    .eq(MarketingGroupIntegralManageRecord::getDistributionRewards,"1")
                    .eq(MarketingGroupIntegralManageRecord::getClassificationReward,"0")
                    .orderByDesc(MarketingGroupIntegralManageRecord::getCreateTime)
                    .last("limit 500"));
            if(marketingGroupIntegralManageRecords.size()==0){
                break;
            }
            marketingGroupIntegralManageRecords.forEach(m->{
                iMarketingGroupIntegralManageRecordService.classificationReward(m.getId());
            });
        }
    }
}
