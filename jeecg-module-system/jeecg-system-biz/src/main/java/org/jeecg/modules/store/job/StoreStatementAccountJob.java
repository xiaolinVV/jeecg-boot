package org.jeecg.modules.store.job;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.store.service.IStoreStatementAccountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class StoreStatementAccountJob implements Job {
    @Autowired
    private IStoreStatementAccountService storeStatementAccountService;
    /**
     * 生成前一天的对账单数据
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    @ApiOperation(value="店铺对账单数据-对账单数据", notes="店铺对账单数据-对账单数据")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(" Jeecg-Boot 店铺对账单数据定时任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
        /**
         *对账单数据调用
         * 生成对账单数据
         */
        storeStatementAccountService.addStoreStatementAccount();
    }
}
