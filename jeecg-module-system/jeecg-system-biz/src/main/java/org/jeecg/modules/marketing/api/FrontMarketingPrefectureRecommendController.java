package org.jeecg.modules.marketing.api;

import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("front/marketingPrefectureRecommend")
public class FrontMarketingPrefectureRecommendController {
    @Autowired
    private IMarketingPrefectureRecommendService iMarketingPrefectureRecommendService;

    /**
     * 获取推荐列表
     * @param id
     * @param marketingPrefectureTypeId
     * @return
     */
    @RequestMapping("getMarketingPrefectureRecommendColumn")
    @ResponseBody
    public Result<Map<String,Object>> getMarketingPrefectureRecommendColumn(String id,
                                           @RequestParam(name="marketingPrefectureTypeId", defaultValue="") String marketingPrefectureTypeId){
        Result<Map<String,Object>>result=new Result<>();
        Map<String,Object>paramMap= Maps.newHashMap();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("appearType","1");
        map.put("marketingPrefectureTypeId",marketingPrefectureTypeId);

        //列表推荐
        paramMap.put("marketingPrefectureRecommendList", iMarketingPrefectureRecommendService.getMarketingPrefectureRecommendColumn(map));
        result.setResult(paramMap);
        result.success("返回推荐列表");
        return result;
    }
}
