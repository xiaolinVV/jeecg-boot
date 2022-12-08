package org.jeecg.modules.marketing.job;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupOrder;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log
public class MarketingZoneGroupOrderJob implements Job {

    @Autowired
    private IMarketingZoneGroupOrderService iMarketingZoneGroupOrderService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("专区团寄售定时器，每天晚上零点执行");
        while (true){
            List<MarketingZoneGroupOrder> marketingZoneGroupOrders= iMarketingZoneGroupOrderService.list(new LambdaQueryWrapper<MarketingZoneGroupOrder>()
                    .eq(MarketingZoneGroupOrder::getStatus,"3")
                    .eq(MarketingZoneGroupOrder::getDistributionRewards,"0")
                    .orderByAsc(MarketingZoneGroupOrder::getCreateTime)
                    .last("limit 50"));
            if(marketingZoneGroupOrders.size()==0){
                break;
            }
            marketingZoneGroupOrders.forEach(m->{
                iMarketingZoneGroupOrderService.consignment(m.getId());
            });
        }
    }
}
