package org.jeecg.modules.marketing.api;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 平台专区api
 */
@Controller
@RequestMapping("front/marketingPrefecture")
@Log
public class FrontMarketingPrefectureController {

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IMarketingAdvertisingPrefectureService iMarketingAdvertisingPrefectureService;

    @Autowired
    private IMarketingPrefectureRecommendService iMarketingPrefectureRecommendService;

    /**
     * 平台专区首页
     *
     * @param id
     * @return
     */
    @RequestMapping("indexPrefecture")
    @ResponseBody
    public Result<Map<String,Object>> indexPrefecture(String id){
        Result<Map<String,Object>>result=new Result<>();
        Map<String,Object>paramMap= Maps.newHashMap();

        //参数判断
        if(StringUtils.isBlank(id)){
            result.error500("平台专区id不能为空");
            return result;
        }

        MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(id);

        //跳动图标
        List<String> jurisdiction= Lists.newArrayList();
        //专区价的显示
        if(marketingPrefecture.getPrefecturePriceProportion().doubleValue()<100){
            jurisdiction.add("1");
        }
        //优惠券的显示
        if(marketingPrefecture.getIsDiscount().equals("1")){
            jurisdiction.add("2");
        }
        //福利金抵扣
        if (!marketingPrefecture.getIsWelfare().equals("0")){
            jurisdiction.add("3");
        }
        //赠送福利金图标
        if(marketingPrefecture.getIsGiveWelfare().equals("1")){
            jurisdiction.add("4");
        }
        paramMap.put("jurisdictionLogo",jurisdiction);


        //时间判断和不限时间(时间戳)
        paramMap.put("validTime",marketingPrefecture.getValidTime());
        if(marketingPrefecture.getValidTime().equals("1")){
            paramMap.put("remainingTime",new BigDecimal(marketingPrefecture.getEndTime().getTime()-new Date().getTime())
                    .divide(new BigDecimal(1000)));
        }else{
            paramMap.put("remainingTime",0);
        }

        //广告列表
        paramMap.put("marketingAdvertisingPrefectures",iMarketingAdvertisingPrefectureService.findByMarketingPrefectureId(id));

        //分享图
        paramMap.put("coverPlan",marketingPrefecture.getCoverPlan());
        //海报图
        paramMap.put("posters",marketingPrefecture.getPosters());

        //说明
        paramMap.put("prefectureExplain",marketingPrefecture.getPrefectureExplain());

        //是否显示分类

        paramMap.put("isViewType",marketingPrefecture.getIsViewType());

        //专区推荐：0：不支持；1：支持
        paramMap.put("prefectureRecommended",marketingPrefecture.getPrefectureRecommended());

        if (marketingPrefecture.getPrefectureRecommended().equals("1")){
            paramMap.put("isAllType","2");
        }else {
            //是否支持全部；0：无分类；1：支持；2：不支持
            paramMap.put("isAllType",marketingPrefecture.getIsAllType());
        }


        //专区推荐模块的别名
        paramMap.put("prefectureRecommendedAlias",marketingPrefecture.getPrefectureRecommendedAlias());

        if (marketingPrefecture.getPrefectureRecommended().equals("1")){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",marketingPrefecture.getId());
            map.put("appearType","0");
            //栏目推荐
            paramMap.put("marketingPrefectureRecommendColumn",iMarketingPrefectureRecommendService.getMarketingPrefectureRecommendColumn(map));
            map.put("appearType","1");
            //列表推荐
            paramMap.put("marketingPrefectureRecommendList", iMarketingPrefectureRecommendService.getMarketingPrefectureRecommendColumn(map));
        }
        result.setResult(paramMap);
        result.success("平台专区首页查询成功！！！");
        return result;
    }


    /**
     * 专区首页数据（废弃）（合并到新的首页接口）
     *
     * @return
     */
    @RequestMapping("findPrefectureIndex")
    @ResponseBody
    @Deprecated
    public Result<List<Map<String,Object>>> findPrefectureIndex(@RequestHeader(defaultValue = "0") String softModel){
        Result<List<Map<String,Object>>>result=new Result<>();

        log.info("专区softModel："+softModel);
        result.setResult(iMarketingPrefectureService.findPrefectureIndex(softModel));

        result.success("平台专区首页列表数据！！！");
        return result;
    }
}
