package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingPrefectureRecommend;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("front/marketingPrefectureRecommendGood")
public class FrontMarketingPrefectureRecommendGoodController {
    @Autowired
    private IMarketingPrefectureRecommendGoodService iMarketingPrefectureRecommendGoodService;
    @Autowired
    private IMarketingPrefectureRecommendService iMarketingPrefectureRecommendService;

    @RequestMapping("getMarketingPrefectureRecommendGoodList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> getMarketingPrefectureRecommendGoodList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                     @RequestParam(name = "marketingPrefectureRecommendId",defaultValue = "") String marketingPrefectureRecommendId,
                                                                                     @RequestParam(name = "marketingPrefectureTypeId",defaultValue = "") String marketingPrefectureTypeId){
        Result<IPage<Map<String, Object>>> result = new Result<>();
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("marketingPrefectureRecommendId",marketingPrefectureRecommendId);
            MarketingPrefectureRecommend marketingPrefectureRecommend = iMarketingPrefectureRecommendService.getById(marketingPrefectureRecommendId);
            if (marketingPrefectureRecommend.getAppearType().equals("1")){
                if (marketingPrefectureRecommend.getRecommendClassify().equals("1")){
                    map.put("marketingPrefectureTypeId",marketingPrefectureTypeId);
                    if (StringUtils.isBlank(marketingPrefectureTypeId)){
                        result.setResult(iMarketingPrefectureRecommendGoodService.getMarketingPrefectureRecommendGoodList(page,map));
                    }else {
                        result.setResult(iMarketingPrefectureRecommendGoodService.getMarketingPrefectureRecommendGoodListOne(page,map));
                    }
                }else {
                    if (StringUtils.isBlank(marketingPrefectureTypeId)){
                        result.setResult(iMarketingPrefectureRecommendGoodService.getMarketingPrefectureRecommendGoodList(page,map));
                    }else {
                        result.setResult(page);
                    }
                }
            }else {
                result.setResult(iMarketingPrefectureRecommendGoodService.getMarketingPrefectureRecommendGoodList(page,map));
            }

        return result;
    }
}
