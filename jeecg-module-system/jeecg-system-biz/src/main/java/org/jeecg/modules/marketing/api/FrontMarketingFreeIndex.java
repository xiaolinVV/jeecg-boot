package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingFreeAnnouncement;
import org.jeecg.modules.marketing.entity.MarketingFreeBaseSetting;
import org.jeecg.modules.marketing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 免单首页
 */
@RequestMapping("front/marketingFreeIndex")
@Controller
@Log
public class FrontMarketingFreeIndex {


    @Autowired
    private IMarketingFreeWinningAnnouncementService iMarketingFreeWinningAnnouncementService;

    @Autowired
    private IMarketingFreeAdvertisingService iMarketingFreeAdvertisingService;

    @Autowired
    private IMarketingFreeSessionService iMarketingFreeSessionService;

    @Autowired
    private IMarketingFreeBaseSettingService iMarketingFreeBaseSettingService;

    @Autowired
    private IMarketingFreeAnnouncementService iMarketingFreeAnnouncementService;

    @Autowired
    private IMarketingFreeGoodListService iMarketingFreeGoodListService;

    @Autowired
    private IMarketingFreeGoodTypeService iMarketingFreeGoodTypeService;


    /**
     * 免单首页
     *
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<Map<String,Object>> index(@RequestHeader(name = "softModel",required = false,defaultValue = "") String softModel){

        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //中奖公告最新10条
        objectMap.put("marketingFreeWinningAnnouncements",iMarketingFreeWinningAnnouncementService.getMarketingFreeWinningAnnouncementIndex());

        //幻灯片
        objectMap.put("marketingFreeAdvertisings",iMarketingFreeAdvertisingService.selectMarketingFreeAdvertisingIndex());

        //场次信息
        objectMap.put("currentSession",iMarketingFreeSessionService.selectCurrentSession());

        //规则
        MarketingFreeBaseSetting marketingFreeBaseSetting=iMarketingFreeBaseSettingService.getOne(new LambdaQueryWrapper<MarketingFreeBaseSetting>());
        objectMap.put("ruleDescription",marketingFreeBaseSetting.getRuleDescription());
        objectMap.put("coverPlan",marketingFreeBaseSetting.getCoverPlan());
        objectMap.put("posters",marketingFreeBaseSetting.getPosters());


        log.info("免单活动分端控制：softModel="+softModel);
        if(marketingFreeBaseSetting.getPointsDisplay().equals("0")){
            log.info("免单活动在任何端显示！！！");
        }else
            //小程序
            if(softModel.equals("0")&&marketingFreeBaseSetting.getPointsDisplay().equals("1")){
                log.info("免单活动在小程序端显示！！！");
            }else if((softModel.equals("1")||softModel.equals("1"))&&marketingFreeBaseSetting.getPointsDisplay().equals("2")){
                log.info("免单活动在app端显示");
            }else{
                return result.error500("免单活动不在你访问的端显示！！！");
            }

        //公告，最新一条

        objectMap.put("marketingFreeAnnouncement",iMarketingFreeAnnouncementService.getOne(new LambdaQueryWrapper<MarketingFreeAnnouncement>()
                .orderByDesc(MarketingFreeAnnouncement::getCreateTime)
                .last("limit 1")).getAnnouncementTitle());


        //强推公告，信息

        objectMap.put("marketingFreeAnnouncementStrongPush",iMarketingFreeAnnouncementService.getOne(new LambdaQueryWrapper<MarketingFreeAnnouncement>()
                .eq(MarketingFreeAnnouncement::getStrongPush,"1")
                .orderByDesc(MarketingFreeAnnouncement::getCreateTime)
                .last("limit 1")));

        //推荐商品列表

        objectMap.put("marketingFreeGoodListByIsRecommend",iMarketingFreeGoodListService.selectMarketingFreeGoodListByIsRecommend());

        //免单商品类型

        objectMap.put("marketingFreeGoodTypes",iMarketingFreeGoodTypeService.getAllMarketingFreeGoodType());

        result.setResult(objectMap);
        result.success("查询免单首页信息！！！");
        return result;
    }
}
