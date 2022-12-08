package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingRushRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("after/maketingRushRecord")
@Controller
public class AfterMaketingRushRecordController {
    @Autowired
    private IMarketingRushRecordService iMarketingRushRecordService;

    /**
     * 抢购记录
     * @param memberId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingRushRecord")
    @ResponseBody
    public Result<?> findMarketingRushRecord(@RequestAttribute(value = "memberId",required = false) String memberId,
                                             String status,
                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iMarketingRushRecordService.findMarketingRushRecord(new Page<>(pageNo,pageSize),memberId,status));
    }

    /**
     * 抢购详情
     * @param id
     * @return
     */
    @RequestMapping("getMarketingRushRecordParticulars")
    @ResponseBody
    public Result<?>getMarketingRushRecordParticulars(String id){
        if (StringUtils.isBlank(id)){
            return Result.error("前端id未传递!");
        }
        return Result.ok(iMarketingRushRecordService.getMarketingRushRecordParticulars(id));
    }

    /**
     * 抢购活动今日数据统计
     * @param memberId
     * @param marketingRushTypeId
     * @return
     */
    @RequestMapping("findTodayData")
    @ResponseBody
    public Result<?> findTodayRushData(@RequestAttribute(value = "memberId",required = false) String memberId,
                                   String marketingRushTypeId){
        if (StringUtils.isBlank(marketingRushTypeId)){
            return Result.error("前端分类id未传递!");
        }
        return Result.ok(iMarketingRushRecordService.findTodayRushData(memberId,marketingRushTypeId));
    }

}
