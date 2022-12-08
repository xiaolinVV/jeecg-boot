package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManageRecord;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("after/marketingGroupIntegralManageRecord")
public class AfterMarketingGroupIntegralManageRecordController {

    @Autowired
    private IMarketingGroupIntegralManageRecordService iMarketingGroupIntegralManageRecordService;


    /**
     * 拼购记录列表
     *
     * @return
     */
    @RequestMapping("getMarketingGroupIntegralManageRecordList")
    @ResponseBody
    public Result<?> getMarketingGroupIntegralManageRecordList(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                               @RequestParam(name = "winningState",required = false,defaultValue = "") String winningState,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        return Result.ok(iMarketingGroupIntegralManageRecordService.page(new Page<>(pageNo,pageSize),new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                .eq(MarketingGroupIntegralManageRecord::getMemberListId,memberId)
                .in(StringUtils.isNotBlank(winningState),MarketingGroupIntegralManageRecord::getWinningState,StringUtils.split(winningState,","))
                .orderByDesc(MarketingGroupIntegralManageRecord::getParticipationTime)));
    }


    /**
     * 根据id获取拼购详情
     * @param id
     * @return
     */
    @RequestMapping("getMarketingGroupIntegralManageRecordById")
    @ResponseBody
    public Result<?> getMarketingGroupIntegralManageRecordById(String id){
        if(StringUtils.isBlank(id)){
            return Result.error("拼购id不能为空");
        }
        return Result.ok(iMarketingGroupIntegralManageRecordService.getById(id));
    }
}
