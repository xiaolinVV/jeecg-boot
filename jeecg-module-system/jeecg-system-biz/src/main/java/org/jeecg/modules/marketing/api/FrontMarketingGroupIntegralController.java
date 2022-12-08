package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralAdvertisingService;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("front/marketingGroupIntegral")
public class FrontMarketingGroupIntegralController {

    @Autowired
    private IMarketingGroupIntegralBaseSettingService  iMarketingGroupIntegralBaseSettingService;

    @Autowired
    private IMarketingGroupIntegralAdvertisingService iMarketingGroupIntegralAdvertisingService;

    @Autowired
    private IMarketingGroupIntegralManageRecordService iMarketingGroupIntegralManageRecordService;


    /**
     * 拼购首页
     *
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<?> index(){
        //设置信息调用
        MarketingGroupIntegralBaseSetting marketingGroupIntegralBaseSetting=iMarketingGroupIntegralBaseSettingService.getOne(new LambdaQueryWrapper<MarketingGroupIntegralBaseSetting>()
                .eq(MarketingGroupIntegralBaseSetting::getStatus,"1"));
        if(marketingGroupIntegralBaseSetting==null){
            return Result.error("找不到拼购设置信息");
        }
        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("ruleDescription",marketingGroupIntegralBaseSetting.getRuleDescription());
        //广告信息
        resultMap.put("marketingGroupIntegralAdvertisingMap",iMarketingGroupIntegralAdvertisingService.getAdvertising());

        //拼购成功信息
        resultMap.put("winningStateMap",iMarketingGroupIntegralManageRecordService.getWinningState());

        return Result.ok(resultMap);
    }
}
