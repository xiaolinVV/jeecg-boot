package org.jeecg.modules.marketing.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillActivityList;
import org.jeecg.modules.marketing.service.IMarketingCertificateSeckillActivityListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Log
public class MarketingCertificateSeckillActivityListJob implements Job{
    @Autowired
    private IMarketingCertificateSeckillActivityListService iMarketingCertificateSeckillActivityListService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("限时抢券开启关闭定时器(每5分钟执行一次)");
        iMarketingCertificateSeckillActivityListService.update(new MarketingCertificateSeckillActivityList()
                .setStatus("1"),new LambdaUpdateWrapper<MarketingCertificateSeckillActivityList>()
                .eq(MarketingCertificateSeckillActivityList::getDelFlag,"0")
                .le(MarketingCertificateSeckillActivityList::getStartTime,new Date())
                .ge(MarketingCertificateSeckillActivityList::getEndTime,new Date())
        );
        iMarketingCertificateSeckillActivityListService.update(new MarketingCertificateSeckillActivityList()
                .setStatus("2"),new LambdaUpdateWrapper<MarketingCertificateSeckillActivityList>()
                .eq(MarketingCertificateSeckillActivityList::getDelFlag,"0")
                .le(MarketingCertificateSeckillActivityList::getEndTime,new Date())
        );
    }
}
