package org.jeecg.modules.member.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingThirdIntegralRecord;
import org.jeecg.modules.marketing.service.IMarketingThirdIntegralRecordService;
import org.jeecg.modules.member.entity.MemberThirdIntegral;
import org.jeecg.modules.member.service.IMemberThirdIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("after/memberThirdIntegral")
public class AfterMemberThirdIntegralController {

    @Autowired
    private IMemberThirdIntegralService iMemberThirdIntegralService;


    @Autowired
    private IMarketingThirdIntegralRecordService iMarketingThirdIntegralRecordService;


    /**
     * 第三积分记录
     *
     * @param memberId
     * @param marketingGroupIntegralManageId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingGroupIntegralManageRecord")
    @ResponseBody
    public Result<?> getMarketingGroupIntegralManageRecord(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                           String marketingGroupIntegralManageId,
                                                           @RequestParam(name = "goAndCome",required = false,defaultValue = "") String goAndCome,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        return Result.ok(iMarketingThirdIntegralRecordService.page(new Page<>(pageNo,pageSize),new LambdaQueryWrapper<MarketingThirdIntegralRecord>()
                .eq(MarketingThirdIntegralRecord::getMemberListId,memberId)
                .eq(MarketingThirdIntegralRecord::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId)
                .eq(StringUtils.isNotBlank(goAndCome),MarketingThirdIntegralRecord::getGoAndCome,goAndCome)
                .orderByDesc(MarketingThirdIntegralRecord::getCreateTime)));
    }


    /**
     * 获取第三积分基本信息
     *
     * @return
     */
    @RequestMapping("getMemberThirdIntegralInfo")
    @ResponseBody
    public Result<?> getMemberThirdIntegralInfo( @RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("totalIntegral",iMemberThirdIntegralService.totalIntegral(memberId));
        resultMap.put("iMemberThirdIntegralList",iMemberThirdIntegralService.list(new LambdaQueryWrapper<MemberThirdIntegral>()
                .eq(MemberThirdIntegral::getMemberListId,memberId) ));
        return Result.ok(resultMap);
    }
}
