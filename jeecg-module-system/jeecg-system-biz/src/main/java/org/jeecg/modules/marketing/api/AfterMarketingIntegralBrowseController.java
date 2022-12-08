package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.beanutils.BeanUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.jeecg.modules.marketing.entity.MarketingIntegralBrowse;
import org.jeecg.modules.marketing.service.IMarketingIntegralBrowseService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * 当前浏览活动查询
 */

@Controller
@RequestMapping("after/marketingIntegralBrowse")
public class AfterMarketingIntegralBrowseController {

    @Autowired
    private IMarketingIntegralBrowseService  iMarketingIntegralBrowseService;

    @Autowired
    private IGoodTypeService iGoodTypeService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;


    /**
     * 获取浏览方式
     *
     * @return
     */
    @RequestMapping("getNowMarketingIntegralBrowse")
    @ResponseBody
    public Result<?> getNowMarketingIntegralBrowse(){

        long count=iMarketingIntegralBrowseService.count(new LambdaQueryWrapper<>());
        if(count==0){
            return Result.error("浏览失败");
        }
        MarketingIntegralBrowse marketingIntegralBrowse=iMarketingIntegralBrowseService.getOne(new LambdaQueryWrapper<>());
        if(new Date().getTime()>=marketingIntegralBrowse.getStartTime().getTime()&&new Date().getTime()<=marketingIntegralBrowse.getEndTime().getTime()){
            Map<String,String> marketingIntegralBrowseMap= null;
            try {
                marketingIntegralBrowseMap = BeanUtils.describe(marketingIntegralBrowse);
                if(marketingIntegralBrowse.getGoodTypeId()!=null){
                    marketingIntegralBrowseMap.put("goodName",iGoodTypeService.getById(marketingIntegralBrowse.getGoodTypeId()).getName());
                }
                if(marketingIntegralBrowse.getMarketingPrefectureId()!=null){
                    marketingIntegralBrowseMap.put("marketingPrefectureName",iMarketingPrefectureService.getById(marketingIntegralBrowse.getMarketingPrefectureId()).getPrefectureName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return Result.ok(marketingIntegralBrowseMap);
        }else{
            return Result.error("浏览失败");
        }
    }
}
