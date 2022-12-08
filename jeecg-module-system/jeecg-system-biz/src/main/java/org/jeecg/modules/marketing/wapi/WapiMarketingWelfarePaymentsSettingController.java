package org.jeecg.modules.marketing.wapi;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 积分设置控制器
 */

@RequestMapping("wapi/marketingWelfarePaymentsSetting")
@Controller
public class WapiMarketingWelfarePaymentsSettingController {

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;


    /**
     * 修改积分价值
     *
     * @return
     */
    @RequestMapping("updateIntegralValue")
    @ResponseBody
    public Result<?> updateIntegralValue(BigDecimal integralValue){
        return Result.ok(iMarketingWelfarePaymentsSettingService.updateIntegralValue(integralValue));
    }
}
