package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupNumberRecordService;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("after/marketingZoneGroupNumberRecord")
@Controller
@Slf4j
public class AfterMarketingZoneGroupNumberRecordController {

    @Autowired
    private IMarketingZoneGroupNumberRecordService iMarketingZoneGroupNumberRecordService;

    @Autowired
    private IMarketingZoneGroupTimeService iMarketingZoneGroupTimeService;

    @RequestMapping("findMarketingZoneGroupNumberRecordByMarketingZoneGroupTimeId")
    @ResponseBody
    public Result<?> findMarketingZoneGroupNumberRecordByMarketingZoneGroupTimeId(@RequestAttribute(required = false,name = "memberId")String memberId,
                                                                 String goAndCome,
                                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        MarketingZoneGroupTime marketingZoneGroupTime = iMarketingZoneGroupTimeService.getOne(new LambdaQueryWrapper<MarketingZoneGroupTime>()
                .eq(MarketingZoneGroupTime::getMemberListId, memberId)
                .eq(MarketingZoneGroupTime::getDelFlag, "0")
                .last("limit 1")
        );
        if (marketingZoneGroupTime != null){
            return Result.ok(iMarketingZoneGroupNumberRecordService.findMarketingZoneGroupNumberRecordByMarketingZoneGroupTimeId(new Page<>(pageNo,pageSize),marketingZoneGroupTime.getId(),goAndCome));
        }else {
            return Result.ok("");
        }

    }
}
