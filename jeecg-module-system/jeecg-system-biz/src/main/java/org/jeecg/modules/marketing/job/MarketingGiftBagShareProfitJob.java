package org.jeecg.modules.marketing.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.service.IMarketingEnterMoneyService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MarketingGiftBagShareProfitJob implements Job {

    @Autowired
    private IMarketingEnterMoneyService iMarketingEnterMoneyService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(" 称号分红 !  时间:" + DateUtils.getTimestamp());
        iMarketingEnterMoneyService.marketingGiftBagShareProfit();
    }}
