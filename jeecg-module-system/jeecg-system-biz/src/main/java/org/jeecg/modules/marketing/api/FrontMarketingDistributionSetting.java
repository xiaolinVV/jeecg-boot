package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.service.IMarketingDistributionSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("front/marketingDistributionSetting")
@Controller
public class FrontMarketingDistributionSetting {
    @Autowired
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;

    @RequestMapping("findMarketingDistributionSetting")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingDistributionSetting(HttpServletRequest request){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.list(new LambdaQueryWrapper<MarketingDistributionSetting>()
                .eq(MarketingDistributionSetting::getDelFlag, "0")).get(0);
        map.put("strategy",marketingDistributionSetting.getStrategy());
        result.setResult(map);
        result.success("返回赚钱攻略!");
        return result;
    }
}
