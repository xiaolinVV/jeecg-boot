package org.jeecg.modules.provider.job;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.provider.service.IProviderStatementAccountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProviderStatementAccountJob implements Job {
    @Autowired
    private IProviderStatementAccountService providerStatementAccountService;
    /**
     * 生成前一天的对账单数据
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    @ApiOperation(value="供应商对账单数据-对账单数据", notes="供应商对账单数据-对账单数据")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(String.format(" Jeecg-Boot 普通定时任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
        /**
         *对账单数据调用
         * 生成对账单数据
         */
        providerStatementAccountService.addStoreStatementAccount();
    }

}
