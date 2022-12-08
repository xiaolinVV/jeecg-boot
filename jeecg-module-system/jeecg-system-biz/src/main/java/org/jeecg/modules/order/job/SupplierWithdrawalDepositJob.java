package org.jeecg.modules.order.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.provider.service.IProviderRechargeRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 示例不带参定时任务
 *
 * @Author Scott
 */
@Slf4j
@Component
public class SupplierWithdrawalDepositJob implements Job {

    @Autowired
    private IProviderRechargeRecordService providerRechargeRecordService;
    /**
     * 供应商每天两点自动提现
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(" 系统自动提现调用定时任务!  时间:" + DateUtils.getTimestamp()));
        /**
         *系统自动提现调用
         */
        providerRechargeRecordService.addStoreRechargeRecord();
    }
}
