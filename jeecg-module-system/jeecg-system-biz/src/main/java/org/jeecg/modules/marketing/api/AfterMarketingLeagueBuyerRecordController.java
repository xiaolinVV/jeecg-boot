package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingLeagueAwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 *
 * 奖励记录
 *
 */
@RequestMapping("after/marketingLeagueBuyerRecord")
@Controller
public class AfterMarketingLeagueBuyerRecordController {

    @Autowired
    private IMarketingLeagueAwardService iMarketingLeagueAwardService;


    /**
     * 累计奖励
     *
     * @param memberId
     * @return
     */
    @RequestMapping("getGrandTotal")
    @ResponseBody
    public Result<?> getGrandTotal(@RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("cumulativeRewards",iMarketingLeagueAwardService.cumulativeRewards(memberId));
        resultMap.put("marketingLeagueTypeIdList",iMarketingLeagueAwardService.getMarketingLeagueAwardBymarketingLeagueTypeId(memberId));
        return Result.ok(resultMap);
    }

    /**
     * 获取奖励记录
     *
     * @param marketingLeagueTypeId
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingLeagueAwardList")
    @ResponseBody
    public Result<?> getMarketingLeagueAwardList(String marketingLeagueTypeId,
                                                 @RequestAttribute("memberId") String memberId,
                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("marketingLeagueTypeId",marketingLeagueTypeId);
        paramMap.put("memberId",memberId);
        return Result.ok(iMarketingLeagueAwardService.getMarketingLeagueAwardList(new Page<>(pageNo,pageSize),paramMap));
    }
}
