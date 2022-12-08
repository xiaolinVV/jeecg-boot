package org.jeecg.modules.alliance.Job;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.alliance.service.IAllianceStatementAccountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AllianceStatementAccountJob implements Job {

    @Autowired
    private IAllianceStatementAccountService allianceStatementAccountService;
    /**
     * 生成前一天的对账单数据
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    @ApiOperation(value="加盟商对账单数据-对账单数据", notes="加盟商对账单数据-对账单数据")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(String.format("加盟商对账单数据普通定时任务!  时间:" + DateUtils.getTimestamp()));
        /**
         *对账单数据调用
         * 生成对账单数据
         */
        allianceStatementAccountService.addAllianceStatementAccount();
    }
}
