package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.MarketingRushBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingRushType;
import org.jeecg.modules.marketing.service.IMarketingRushBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingRushTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;


/**
 * 抢购首页
 */

@Controller
@RequestMapping("after/marketingRush")
public class AfterMarketingRushController {


    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;

    @Autowired
    private IMarketingRushTypeService iMarketingRushTypeService;

    /**
     * 抢购首页
     *
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<?> index(){
        MarketingRushBaseSetting  marketingRushBaseSetting=iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getStatus,"1"));
        if(marketingRushBaseSetting==null){
            return Result.error("抢购设置不存在");
        }
        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("anotherName",marketingRushBaseSetting.getAnotherName());
        resultMap.put("dayStartTime",marketingRushBaseSetting.getDayStartTime());
        resultMap.put("dayEndTime",marketingRushBaseSetting.getDayEndTime());
        resultMap.put("ruleDescription",marketingRushBaseSetting.getRuleDescription());
        //获取分类列表信息
        resultMap.put("marketingRushTypeList",  iMarketingRushTypeService.listMaps(new LambdaQueryWrapper<MarketingRushType>()
                .select(MarketingRushType::getId,MarketingRushType::getRushName)
                .eq(MarketingRushType::getStatus,"1")
                .orderByAsc(MarketingRushType::getSort)));
        //时间判断
        try {
            Date startTime= DateUtils.parseDate(DateUtils.formatDate()+" "+marketingRushBaseSetting.getDayStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingRushBaseSetting.getDayEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>=startTime.getTime()&&new Date().getTime()<=endTime.getTime()) {
                resultMap.put("isOpen", "1");
            }else{
                resultMap.put("isOpen", "0");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Result.ok(resultMap);
    }
}
