package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingPayIntegralSetting;
import org.jeecg.modules.marketing.service.IMarketingPayIntegralSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 支付即积分
 */

@Controller
@RequestMapping("front/marketingPayIntegralSetting")
public class FrontMarketingPayIntegralSettingController {

    @Autowired
    private IMarketingPayIntegralSettingService iMarketingPayIntegralSettingService;


    /**
     * 获取介绍信息
     *
     * @return
     */
    @RequestMapping("introduce")
    @ResponseBody
    public Result<?> getIntroduce(){
        MarketingPayIntegralSetting marketingPayIntegralSetting=iMarketingPayIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingPayIntegralSetting>()
                .eq(MarketingPayIntegralSetting::getStatus,"1"));
        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("surfacePlot",marketingPayIntegralSetting.getSurfacePlot());
        resultMap.put("introduce",marketingPayIntegralSetting.getIntroduce());
        return Result.ok(resultMap);
    }
}
