package org.jeecg.modules.marketing.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupManage;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupRecord;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupRecordService;
import org.jeecg.modules.member.service.IMemberAccountCapitalService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.entity.PayCertificateLog;
import org.jeecg.modules.pay.service.IPayCertificateLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Log
public class MarketingCertificateGroupManageJob implements Job{
    @Autowired
    private IMarketingCertificateGroupManageService iMarketingCertificateGroupManageService;
    @Autowired
    private IMarketingCertificateGroupRecordService iMarketingCertificateGroupRecordService;
    @Autowired
    private IPayCertificateLogService iPayCertificateLogService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;
    @Autowired
    private PayUtils payUtils;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("拼好券超时过期定时器(每5分钟执行一次)");
        List<MarketingCertificateGroupManage> marketingCertificateGroupManageList = iMarketingCertificateGroupManageService.list(new LambdaQueryWrapper<MarketingCertificateGroupManage>()
                .eq(MarketingCertificateGroupManage::getDelFlag, "0")
                .eq(MarketingCertificateGroupManage::getStatus, "0")
                .le(MarketingCertificateGroupManage::getEndTime, new Date())
        );
        marketingCertificateGroupManageList.forEach(mcm->{
            iMarketingCertificateGroupManageService.updateById(mcm
                    .setStatus("2")
            );
            iMarketingCertificateGroupRecordService.list(new LambdaQueryWrapper<MarketingCertificateGroupRecord>()
                    .eq(MarketingCertificateGroupRecord::getDelFlag,"0")
                    .eq(MarketingCertificateGroupRecord::getMarketingCertificateGroupManageId,mcm.getId())
            ).forEach(mcgr->{
                iMarketingCertificateGroupRecordService.updateById(mcgr
                        .setStatus("2")
                );
                PayCertificateLog payCertificateLog = iPayCertificateLogService.getById(mcgr.getPayCertificateLogId());
                //订单款项退回
                Map<String, Object> response =payUtils.refund(payCertificateLog.getPayPrice(),payCertificateLog.getSerialNumber(),payCertificateLog.getSerialNumber());
                if (payCertificateLog.getBalance().doubleValue()>0){
                    //计算退还资金
                    iMemberListService.addBlance(payCertificateLog.getMemberListId(),payCertificateLog.getBalance(),payCertificateLog.getId(),"14");
                }
                if (payCertificateLog.getWelfarePayments().doubleValue()>0){
                    //退还福利金
                    iMemberWelfarePaymentsService.addWelfarePayments(payCertificateLog.getMemberListId(),payCertificateLog.getWelfarePayments(),"22",payCertificateLog.getId(),"");
                }
            });

        });
    }
}
