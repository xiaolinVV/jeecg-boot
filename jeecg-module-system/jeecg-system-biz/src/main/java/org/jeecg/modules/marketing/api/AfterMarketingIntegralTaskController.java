package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 积分任务控制器
 */

@Controller
@RequestMapping("after/marketingIntegralTask")
@Log
public class AfterMarketingIntegralTaskController {

    @Autowired
    private IMarketingIntegralSettingService iMarketingIntegralSettingService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMarketingIntegralTaskService iMarketingIntegralTaskService;
    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMarketingIntegralSignRecordService iMarketingIntegralSignRecordService;

    @Autowired
    private IMarketingIntegralTaskRecordService iMarketingIntegralTaskRecordService;


    @Autowired
    private IMarketingIntegralTaskBaseSettingService iMarketingIntegralTaskBaseSettingService;


    /**
     * 获取单月签到的天
     * @param memberId
     * @return
     */
    @RequestMapping("getMonthSignDay")
    @ResponseBody
    public Result<?> getMonthSignDay(int year,int month,@RequestAttribute("memberId") String memberId){
        //参数校验
        if(year==0){
            return Result.error("年数据不能为空");
        }
        if(month==0){
            return Result.error("月数据不能为空");
        }
        MarketingIntegralTask marketingIntegralTask=iMarketingIntegralTaskService.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getTaskType,"7")
                .eq(MarketingIntegralTask::getTaskeMode,"1")
                .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
        if(marketingIntegralTask==null){
            return Result.error("任务类型不存在");
        }
        List<Integer> dayList=Lists.newArrayList();
        iMarketingIntegralTaskRecordService.list(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                .eq(MarketingIntegralTaskRecord::getYear,year)
                .eq(MarketingIntegralTaskRecord::getMonth,month)
                .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                .eq(MarketingIntegralTaskRecord::getMemberListId,memberId)
                .orderByAsc(MarketingIntegralTaskRecord::getCreateTime)).forEach(m->{
            if(!dayList.contains(m.getDay())){
                dayList.add(m.getDay());
            }
        });
        return Result.ok(dayList);
    }

    /**
     * 签到
     * @return
     */
    @RequestMapping("checkIn")
    @ResponseBody
    public Result<?> checkIn(@RequestAttribute("memberId") String memberId){
        Calendar calendar=Calendar.getInstance();
        MarketingIntegralTask marketingIntegralTask=iMarketingIntegralTaskService.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getTaskType,"7")
                .eq(MarketingIntegralTask::getTaskeMode,"1")
                .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
        if(marketingIntegralTask==null){
            return Result.error("任务类型不存在");
        }
        if(iMarketingIntegralTaskRecordService.count(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                .eq(MarketingIntegralTaskRecord::getYear,calendar.get(Calendar.DAY_OF_YEAR))
                .eq(MarketingIntegralTaskRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingIntegralTaskRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                .eq(MarketingIntegralTaskRecord::getMemberListId,memberId))>0){
            return Result.error("今日已签到");
        }
        iMarketingIntegralTaskService.singleDaily(memberId,"7");
        return Result.ok();
    }


    /**
     * 任务列表
     *
     * @param memberId
     * @return
     */
    @RequestMapping("getTaskList")
    @ResponseBody
    public Result<?> getTaskList(@RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap=Maps.newHashMap();

        MarketingIntegralTaskBaseSetting marketingIntegralTaskBaseSetting=iMarketingIntegralTaskBaseSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralTaskBaseSetting>().eq(MarketingIntegralTaskBaseSetting::getStatus,"1"));
        if(marketingIntegralTaskBaseSetting!=null){
            resultMap.put("ruleDescription",marketingIntegralTaskBaseSetting.getRuleDescription());
        }
        List<Map<String,Object>>  positionMapList=Lists.newArrayList();
        iMarketingIntegralTaskService.list(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getIsView,"1")
                .orderByAsc(MarketingIntegralTask::getSort)).forEach(marketingIntegralTask->{
            Map<String,Object> positionMap=Maps.newHashMap();
            positionMap.put("taskType",marketingIntegralTask.getTaskType());
            positionMap.put("taskTitle",marketingIntegralTask.getTaskTitle());
            positionMap.put("taskImg",marketingIntegralTask.getTaskImg());
            positionMap.put("taskDescription",marketingIntegralTask.getTaskDescription());
            positionMap.put("status",iMarketingIntegralTaskService.getIntegralTaskStatus(marketingIntegralTask.getId(),memberId));//状态；0：未领取，1：已领取；
            positionMap.put("id",marketingIntegralTask.getId());
            positionMap.put("coverPlan",marketingIntegralTask.getCoverPlan());//分享图
            positionMapList.add(positionMap);
        });
        resultMap.put("positionMapList",positionMapList);
        return Result.ok(resultMap);
    }




    /**
     * 积分首页
     *
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<?> index(@RequestAttribute("memberId") String memberId){
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        MemberList memberList=iMemberListService.getById(memberId);
        Map<String,Object> resultMap= Maps.newHashMap();
        MarketingIntegralSetting marketingIntegralSetting=iMarketingIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralSetting>()
                .eq(MarketingIntegralSetting::getStatus,"1"));
        //规则
        resultMap.put("rule",marketingIntegralSetting.getRule());
        resultMap.put("integral",memberList.getIntegral());
        resultMap.put("welfarePayments",memberList.getIntegral().multiply(marketingIntegralSetting.getPrice()).divide(integralValue,2, RoundingMode.DOWN));
        //总签到天数
        resultMap.put("totalSignCount",memberList.getSignCount());
        // 签到奖励周期数（天）
        String signInAwardCycle=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "sign_in_award_cycle");
        //查询是否已签到和签到天数
        Map<String,Object> signMap=Maps.newHashMap();
        signMap.put("memberListId",memberId);
        signMap.put("signTime", DateUtils.formatDate());
        Map<String,Object> signResultMap=iMarketingIntegralSignRecordService.getSignTime(signMap);
        if(signResultMap==null){
            Map<String,Object> signMap2=Maps.newHashMap();
            signMap2.put("memberListId",memberId);
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE,-1);
            signMap2.put("signTime", DateUtils.formatDate(calendar));
            Map<String,Object> signResultMap2=iMarketingIntegralSignRecordService.getSignTime(signMap2);
            if(signResultMap2==null){
                resultMap.put("signCount",0);
                resultMap.put("isSign","0");//未签到
            }else{
                resultMap.put("signCount",signResultMap2.get("signCount"));
                resultMap.put("isSign","0");//未签到
            }
        }else{
            resultMap.put("signCount",signResultMap.get("signCount"));
            resultMap.put("isSign","1");//已签到
        }
        //获取签到奖励
        int i=Integer.parseInt(signInAwardCycle);
        List<String> awardList=Lists.newArrayList();
        for(int j=0;j<i;j++){
            int d=j+1;
            MarketingIntegralTask marketingIntegralTask=iMarketingIntegralTaskService.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                    .eq(MarketingIntegralTask::getStatus,"1")
                    .eq(MarketingIntegralTask::getTaskeMode,"1")
                    .eq(MarketingIntegralTask::getNumberDays,d)
                    .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
            if(marketingIntegralTask==null){
                awardList.add("0");
            }else{
                awardList.add(marketingIntegralTask.getAward().toString());
            }
        }
        resultMap.put("awardList",awardList);
        //奖励栏数据
        List<Map<String,Object>> displayPositionMapList= Lists.newArrayList();
        iMarketingIntegralTaskService.list(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getDisplayPosition,"1")
                .eq(MarketingIntegralTask::getIsView,"1")
                .orderByAsc(MarketingIntegralTask::getSort)).forEach(marketingIntegralTask->{
                    Map<String,Object> displayPositionMap=Maps.newHashMap();
                    displayPositionMap.put("taskType",marketingIntegralTask.getTaskType());
                    displayPositionMap.put("taskTitle",marketingIntegralTask.getTaskTitle());
                    displayPositionMap.put("taskImg",marketingIntegralTask.getTaskImg());
                    displayPositionMap.put("taskDescription",marketingIntegralTask.getTaskDescription());
                    displayPositionMap.put("status",iMarketingIntegralTaskService.getIntegralTaskStatus(marketingIntegralTask.getId(),memberId));//状态；0：未领取，1：已领取；
                    displayPositionMap.put("id",marketingIntegralTask.getId());
                    displayPositionMap.put("coverPlan",marketingIntegralTask.getCoverPlan());//分享图
                    displayPositionMapList.add(displayPositionMap);
        });

        resultMap.put("displayPositionMapList",displayPositionMapList);

        //任务奖励
        List<Map<String,Object>> awardMapList= Lists.newArrayList();
        iMarketingIntegralTaskService.list(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .ne(MarketingIntegralTask::getDisplayPosition,"0")
                .eq(MarketingIntegralTask::getIsView,"1")
                .orderByAsc(MarketingIntegralTask::getSort)).forEach(marketingIntegralTask->{
            Map<String,Object> awardMap=Maps.newHashMap();
            awardMap.put("taskType",marketingIntegralTask.getTaskType());
            awardMap.put("taskTitle",marketingIntegralTask.getTaskTitle());
            awardMap.put("taskDescription",marketingIntegralTask.getTaskDescription());
            awardMap.put("status",iMarketingIntegralTaskService.getIntegralTaskStatus(marketingIntegralTask.getId(),memberId));//状态；0：未领取，1：已领取；
            awardMap.put("id",marketingIntegralTask.getId());
            awardMap.put("coverPlan",marketingIntegralTask.getCoverPlan());//分享图
            awardMapList.add(awardMap);
        });
        resultMap.put("awardMapList",awardMapList);
        return Result.ok(resultMap);
    }

    /**
     * 会员签到
     *
     * @return
     */
    @RequestMapping("sign")
    @ResponseBody
    public Result<?> sign(@RequestAttribute("memberId") String memberId,
                          @RequestParam(defaultValue = "") String inviterMemberId){
        //判断会员是否有签到
        Map<String,Object> signMap=Maps.newHashMap();
        signMap.put("memberListId",memberId);
        signMap.put("signTime", DateUtils.formatDate());
        Map<String,Object> signResultMap=iMarketingIntegralSignRecordService.getSignTime(signMap);
        if(signResultMap!=null){
            return Result.error("会员已签到");
        }
        MemberList memberList=iMemberListService.getById(memberId);
        //判断用户是否是连续签到
        Map<String,Object> signMap2=Maps.newHashMap();
        signMap2.put("memberListId",memberId);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        signMap2.put("signTime", DateUtils.formatDate(calendar));
        Map<String,Object> signResultMap2=iMarketingIntegralSignRecordService.getSignTime(signMap2);
        // 签到奖励周期数（天）
        String signInAwardCycle=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "sign_in_award_cycle");

        int signCount=0;
        int totalsignCount=0;
        //非连续签到
        if(signResultMap2==null){
            memberList.setSignCount(new BigDecimal(1));
            iMemberListService.saveOrUpdate(memberList);
            MarketingIntegralSignRecord marketingIntegralSignRecord=new MarketingIntegralSignRecord();
            marketingIntegralSignRecord.setSignTime(new Date());
            marketingIntegralSignRecord.setMemberListId(memberId);
            iMarketingIntegralSignRecordService.save(marketingIntegralSignRecord);
            signCount=1;
        }else{
            memberList.setSignCount(memberList.getSignCount().add(new BigDecimal(1)));
            iMemberListService.saveOrUpdate(memberList);
            if(Integer.parseInt(signResultMap2.get("signCount").toString())<Integer.parseInt(signInAwardCycle)){
                MarketingIntegralSignRecord marketingIntegralSignRecord=iMarketingIntegralSignRecordService.getById(signResultMap2.get("id").toString());
                marketingIntegralSignRecord.setSignTime(new Date());
                marketingIntegralSignRecord.setSignCount(marketingIntegralSignRecord.getSignCount().add(new BigDecimal(1)));
                iMarketingIntegralSignRecordService.saveOrUpdate(marketingIntegralSignRecord);
                signCount=marketingIntegralSignRecord.getSignCount().intValue();
            }else{
                MarketingIntegralSignRecord marketingIntegralSignRecord=new MarketingIntegralSignRecord();
                marketingIntegralSignRecord.setSignTime(new Date());
                marketingIntegralSignRecord.setMemberListId(memberId);
                iMarketingIntegralSignRecordService.save(marketingIntegralSignRecord);
                signCount=1;
            }

        }
        totalsignCount=memberList.getSignCount().intValue();

        MarketingIntegralTask marketingIntegralTask=iMarketingIntegralTaskService.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getTaskType,"2")
                .eq(MarketingIntegralTask::getNumberDays,signCount)
                .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
        if(marketingIntegralTask!=null){
            //邀请签到奖励
            iMarketingIntegralTaskService.invitedSign(memberId,inviterMemberId);
            //有奖励
            iMarketingIntegralTaskRecordService.addMarketingIntegralTaskRecord(marketingIntegralTask,memberId);
        }
        if(totalsignCount>Integer.parseInt(signInAwardCycle)){
            MarketingIntegralTask marketingIntegralTask2=iMarketingIntegralTaskService.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                    .eq(MarketingIntegralTask::getStatus,"1")
                    .eq(MarketingIntegralTask::getTaskType,"2")
                    .eq(MarketingIntegralTask::getNumberDays,totalsignCount)
                    .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
            if(marketingIntegralTask2!=null){
                iMarketingIntegralTaskRecordService.addMarketingIntegralTaskRecord(marketingIntegralTask2,memberId);
            }
        }
        return Result.ok("签到成功");
    }

    /**
     * 每日浏览
     *
     * @param memberId
     * @return
     */
    @RequestMapping("dailyBrowsing")
    @ResponseBody
    public Result<?> dailyBrowsing(@RequestAttribute("memberId") String memberId){
        return Result.ok(iMarketingIntegralTaskService.dailyBrowsing(memberId));
    }

}
