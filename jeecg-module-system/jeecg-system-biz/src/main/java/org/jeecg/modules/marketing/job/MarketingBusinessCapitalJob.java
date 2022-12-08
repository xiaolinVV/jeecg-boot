package org.jeecg.modules.marketing.job;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingBusinessCapital;
import org.jeecg.modules.marketing.service.IMarketingBusinessCapitalService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
public class MarketingBusinessCapitalJob implements Job {


    @Autowired
    private IMarketingBusinessCapitalService iMarketingBusinessCapitalService;


    @Autowired
    private IMemberListService iMemberListService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        iMarketingBusinessCapitalService.list(new LambdaQueryWrapper<MarketingBusinessCapital>()
                .eq(MarketingBusinessCapital::getPaymentsModel,"1")).forEach(m->{
            iMarketingBusinessCapitalService.dailyDividend(m.getId());
        });

        //分配资金
        iMemberListService.list(new LambdaQueryWrapper<MemberList>().gt(MemberList::getWelfarePaymentsUnusable,0)).forEach(m->{
            iMarketingBusinessCapitalService.receiveDividends(m.getId());
        });

    }
}
