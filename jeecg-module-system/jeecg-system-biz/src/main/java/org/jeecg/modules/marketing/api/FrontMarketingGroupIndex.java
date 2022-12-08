package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingGroupBaseSetting;
import org.jeecg.modules.marketing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 中奖拼团首页
 */
@Controller
@RequestMapping("front/marketingGroupIndex")
@Log
public class FrontMarketingGroupIndex {

    @Autowired
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;

    @Autowired
    private IMarketingGroupAdvertisingService iMarketingGroupAdvertisingService;

    @Autowired
    private IMarketingGroupGoodListService iMarketingGroupGoodListService;

    @Autowired
    private IMarketingGroupGoodTypeService iMarketingGroupGoodTypeService;

    @Autowired
    private IMarketingGroupManageService iMarketingGroupManageService;

    @Autowired
    private IMarketingGroupRecordService iMarketingGroupRecordService;


    /**
     * 中奖拼团首页接口
     *
     * 张靠勤   2021-3-31
     *
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<?> index(@RequestHeader(name = "softModel",required = false,defaultValue = "") String softModel){
        Result<Map<String,Object>> result=new Result<>();

        Map<String,Object> objectMap= Maps.newHashMap();

        //规则信息
        MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>());
        //基础设置信息不能为空
        if(marketingGroupBaseSetting==null){
            return result.error500("拼团基础信息没有设置！！！");
        }
        log.info("中奖拼团分端控制：softModel="+softModel);
        if(marketingGroupBaseSetting.getPointsDisplay().equals("0")){
            log.info("中奖拼团在任何端显示！！！");
        }else
            //小程序
            if(softModel.equals("0")&&marketingGroupBaseSetting.getPointsDisplay().equals("1")){
                log.info("中奖拼团在小程序端显示！！！");
            }else if((softModel.equals("1")||softModel.equals("1"))&&marketingGroupBaseSetting.getPointsDisplay().equals("2")){
                log.info("中奖拼团在app端显示");
            }else{
                return result.error500("中奖拼团不在你访问的端显示！！！");
            }
        objectMap.put("ruleDescription",marketingGroupBaseSetting.getRuleDescription());
        objectMap.put("coverPlan",marketingGroupBaseSetting.getCoverPlan());
        objectMap.put("posters",marketingGroupBaseSetting.getPosters());


        //头部购买订单切换（拼团记录开发完成再补充）
        objectMap.put("marketingGroupRecordByRewardStatus",iMarketingGroupRecordService.getMarketingGroupRecordByRewardStatus());

        //获取18个进行整的拼团商品数据
        objectMap.put("marketingGroupManageByGood",iMarketingGroupManageService.getMarketingGroupManageByGood());


        //幻灯片
        objectMap.put("marketingGroupAdvertisings",iMarketingGroupAdvertisingService.selectMarketingGroupAdvertisingIndex());

        //推荐商品列表

        objectMap.put("marketingGroupGoodListByIsRecommend",iMarketingGroupGoodListService.selectMarketingGroupGoodListByIsRecommend());

        //类型列表信息

        objectMap.put("marketingGroupGoodTypes",iMarketingGroupGoodTypeService.getAllMarketingGroupGoodType());

        result.setResult(objectMap);
        result.success("首页信息查询成功！！！");
        return result;
    }
}
