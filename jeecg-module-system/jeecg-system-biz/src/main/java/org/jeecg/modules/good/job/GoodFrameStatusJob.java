package org.jeecg.modules.good.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.good.service.IGoodListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GoodFrameStatusJob implements Job {
    @Autowired
    private IGoodListService iGoodListService;

    /**
     * 定时器零库存商品下架
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format("零库存商品下架定时器!  时间:" + DateUtils.getTimestamp()));

        iGoodListService.updateGoodListISRepertoryZero();
    }

}
