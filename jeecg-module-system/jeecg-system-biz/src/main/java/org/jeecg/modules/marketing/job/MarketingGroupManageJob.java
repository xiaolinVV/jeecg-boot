package org.jeecg.modules.marketing.job;


import lombok.extern.java.Log;
import org.jeecg.modules.marketing.service.IMarketingGroupManageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
public class MarketingGroupManageJob implements Job {

    @Autowired
    private IMarketingGroupManageService iMarketingGroupManageService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("中奖拼团超时失败定时器，每5分钟运行一次");
        iMarketingGroupManageService.closeMarketingGroupManage();
    }
}
