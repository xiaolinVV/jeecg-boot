package org.jeecg.modules.member.job;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupBaseSettingService;
import org.jeecg.modules.member.entity.MemberWelfarePayments;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
@Log
public class MemberWelfarePaymentsJob implements Job {


    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMarketingZoneGroupBaseSettingService iMarketingZoneGroupBaseSettingService;



    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("福利金代付款处理定时器，仅云创的专区团使用，每天凌晨1点执行");

        MarketingZoneGroupBaseSetting marketingZoneGroupBaseSetting= iMarketingZoneGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupBaseSetting>().eq(MarketingZoneGroupBaseSetting::getStatus,"1").last("limit 1"));

        if(marketingZoneGroupBaseSetting!=null) {
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY,-marketingZoneGroupBaseSetting.getSettlementInterval().intValue());
            log.info(DateUtils.formatDate(calendar,"yyyy-MM-dd HH:mm:ss"));
            while (true) {
                List<MemberWelfarePayments> memberWelfarePayments = iMemberWelfarePaymentsService.list(new LambdaQueryWrapper<MemberWelfarePayments>()
                        .eq(MemberWelfarePayments::getIsFreeze, "1")
                        .eq(MemberWelfarePayments::getTradeStatus, "2")
                        .eq(MemberWelfarePayments::getWeType,"1")
                        .le(MemberWelfarePayments::getCreateTime,calendar.getTime())
                        .orderByAsc(MemberWelfarePayments::getCreateTime)
                        .last("limit 500"));
                if (memberWelfarePayments.size() == 0) {
                    break;
                }
                memberWelfarePayments.forEach(m -> {
                    iMemberWelfarePaymentsService.freezeToNomal(m.getId());
                });
            }
        }
    }
}
