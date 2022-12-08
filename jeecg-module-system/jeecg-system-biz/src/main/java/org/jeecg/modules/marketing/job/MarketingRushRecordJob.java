package org.jeecg.modules.marketing.job;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingRushGroup;
import org.jeecg.modules.marketing.entity.MarketingRushRecord;
import org.jeecg.modules.marketing.service.IMarketingRushGroupService;
import org.jeecg.modules.marketing.service.IMarketingRushRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log
public class MarketingRushRecordJob implements Job {


    @Autowired
    private IMarketingRushRecordService iMarketingRushRecordService;

    @Autowired
    private IMarketingRushGroupService iMarketingRushGroupService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("抢购奖励分配，每天1点运行");
        while (true) {
            List<MarketingRushRecord> marketingRushRecords = iMarketingRushRecordService.list(new LambdaQueryWrapper<MarketingRushRecord>()
                    .eq(MarketingRushRecord::getClassificationReward, "0")
                    .orderByAsc(MarketingRushRecord::getCreateTime)
                    .last("limit 500"));
            if(marketingRushRecords.size()==0){
                break;
            }
            marketingRushRecords.forEach(m->{
                iMarketingRushRecordService.classificationReward(m.getId());
            });
        }
        log.info("清算已寄售数据");
        while (true){
            List<MarketingRushGroup> marketingRushGroups = iMarketingRushGroupService.list(new LambdaQueryWrapper<MarketingRushGroup>()
                    .eq(MarketingRushGroup::getConsignmentStatus, "1")
                    .eq(MarketingRushGroup::getDistributionRewards, "0")
                    .orderByAsc(MarketingRushGroup::getCreateTime)
                    .last("limit 500"));
            if(marketingRushGroups.size()==0){
                break;
            }
            marketingRushGroups.forEach(m->{
                iMarketingRushRecordService.groupConsignmentDistributionRewards(m.getId());
            });
        }
    }
}
