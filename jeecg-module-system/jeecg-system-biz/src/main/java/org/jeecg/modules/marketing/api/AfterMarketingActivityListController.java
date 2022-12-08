package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingActivityList;
import org.jeecg.modules.marketing.entity.MarketingActivityListRecord;
import org.jeecg.modules.marketing.service.IMarketingActivityListRecordService;
import org.jeecg.modules.marketing.service.IMarketingActivityListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * 活动列表
 */
@RequestMapping("after/marketingActivityList")
@Controller
public class AfterMarketingActivityListController {


    @Autowired
    private IMarketingActivityListService iMarketingActivityListService;

    @Autowired
    private IMarketingActivityListRecordService iMarketingActivityListRecordService;


    /**
     * 获取记录详情
     *
     * @param memberId
     * @param id
     * @return
     */
    @RequestMapping("getMarketingActivityListById")
    @ResponseBody
    public Result<?> getMarketingActivityListById(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                  String id){
        if(StringUtils.isBlank(id)){
            return Result.error("活动id不能空");
        }
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberListId",memberId);
        paramMap.put("id",id);
        return Result.ok(iMarketingActivityListService.getMarketingActivityListById(paramMap));
    }


    /**
     *
     * 报名
     *
     * @param memberId
     * @param linkman
     * @param contactNumber
     * @param sex
     * @param marketingActivityListId
     * @return
     */
    @RequestMapping("apply")
    @ResponseBody
    public Result<?> apply(@RequestAttribute(value = "memberId",required = false) String memberId,
                           String linkman,
                           String contactNumber,
                           String sex,
                           String marketingActivityListId){
        //参数校验
        if(StringUtils.isBlank(marketingActivityListId)){
            return Result.error("活动id不能为空");
        }

        if(StringUtils.isBlank(linkman)){
            return Result.error("联系人不能为空");
        }
        if(StringUtils.isBlank(contactNumber)){
            return Result.error("联系电话不能为空");
        }

        if(StringUtils.isBlank(sex)){
            return Result.error("性别不能为空");
        }

        MarketingActivityList marketingActivityList=iMarketingActivityListService.getById(marketingActivityListId);

        //查询报名数量
        long applyCount=iMarketingActivityListRecordService.count(new LambdaQueryWrapper<MarketingActivityListRecord>()
                .eq(MarketingActivityListRecord::getMarketingActivityListId,marketingActivityListId));

        if(marketingActivityList.getPlaces().intValue()<=applyCount){
            return Result.error("报名人数已满");
        }
        //查询用户是否报过名
        long count=iMarketingActivityListRecordService.count(new LambdaQueryWrapper<MarketingActivityListRecord>()
                .eq(MarketingActivityListRecord::getMarketingActivityListId,marketingActivityListId)
                .eq(MarketingActivityListRecord::getMemberListId,memberId));
        if(count>0){
            return Result.error("已报过名");
        }
        //进入报名
        iMarketingActivityListRecordService.save(new MarketingActivityListRecord()
                .setMarketingActivityListId(marketingActivityListId)
                .setMemberListId(memberId)
                .setLinkman(linkman)
                .setContactNumber(contactNumber)
                .setRegistrationTime(new Date())
                .setSex(sex));
        return Result.ok("报名成功");
    }

    /**
     * 我的活动列表
     *
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getmarketingActivityListRecordByMemberId")
    @ResponseBody
    public Result<?> getmarketingActivityListRecordByMemberId(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iMarketingActivityListRecordService.getmarketingActivityListRecordByMemberId(new Page<>(pageNo,pageSize),memberId));
    }

}
