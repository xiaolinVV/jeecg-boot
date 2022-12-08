package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingLeagueType;
import org.jeecg.modules.marketing.service.IMarketingLeagueTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* 加盟专区
* */

@RequestMapping("front/marketingLeagueType")
@Controller
public class FrontMarketingLeagueTypeController {

    @Autowired
    private IMarketingLeagueTypeService iMarketingLeagueTypeService;


    /**
     * 获取专区列表
     *
     * @return
     */
    @RequestMapping("getMarketingLeagueTypeAll")
    @ResponseBody
    public Result<?> getMarketingLeagueTypeAll(){
        return Result.ok(iMarketingLeagueTypeService.list(new LambdaQueryWrapper<MarketingLeagueType>().eq(MarketingLeagueType::getStatus,"1").orderByAsc(MarketingLeagueType::getSort)));
    }



}
