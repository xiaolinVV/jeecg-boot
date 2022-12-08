package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.entity.MarketingIntegralSetting;
import org.jeecg.modules.marketing.entity.MarketingIntegralTask;
import org.jeecg.modules.marketing.entity.MarketingIntegralTaskBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingIntegralTaskRecord;
import org.jeecg.modules.marketing.mapper.MarketingIntegralTaskMapper;
import org.jeecg.modules.marketing.service.IMarketingIntegralSettingService;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskRecordService;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @Description: 积分任务
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Service
@Log
public class MarketingIntegralTaskServiceImpl extends ServiceImpl<MarketingIntegralTaskMapper, MarketingIntegralTask> implements IMarketingIntegralTaskService {

    @Autowired
    private IMarketingIntegralSettingService iMarketingIntegralSettingService;

    @Autowired
    private IMarketingIntegralTaskRecordService iMarketingIntegralTaskRecordService;

    @Autowired
    private IMemberListService  iMemberListService;

    @Autowired
    private IMarketingIntegralTaskBaseSettingService iMarketingIntegralTaskBaseSettingService;

    @Override
    public int getIntegralTaskStatus(String marketingIntegralTaskId, String memberId) {
        //0：不可领取，1：未领取；2：已领取
        MarketingIntegralTask marketingIntegralTask=this.getById(marketingIntegralTaskId);
        if(marketingIntegralTask.getTaskeMode().equals("0")){
            //控制会员每日次数
            Calendar calendar=Calendar.getInstance();
            long count=iMarketingIntegralTaskRecordService.count(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                    .eq(MarketingIntegralTaskRecord::getYear,calendar.get(Calendar.YEAR))
                    .eq(MarketingIntegralTaskRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                    .eq(MarketingIntegralTaskRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                    .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                    .eq(MarketingIntegralTaskRecord::getMemberListId,memberId));
            if(count>=marketingIntegralTask.getTimes().intValue()){
                return 2;
            }else {
                return 1;
            }
        }
        if(marketingIntegralTask.getTaskeMode().equals("1")){
            //控制会员每日次数
            Calendar calendar=Calendar.getInstance();
            long count=iMarketingIntegralTaskRecordService.count(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                    .eq(MarketingIntegralTaskRecord::getYear,calendar.get(Calendar.YEAR))
                    .eq(MarketingIntegralTaskRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                    .eq(MarketingIntegralTaskRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                    .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                    .eq(MarketingIntegralTaskRecord::getMemberListId,memberId));
            if(count>=marketingIntegralTask.getTimes().intValue()){
                return 2;
            }else {
                return 1;
            }
        }
        if(marketingIntegralTask.getTaskeMode().equals("2")){
            //控制会员每日次数
            long count=iMarketingIntegralTaskRecordService.count(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                    .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                    .eq(MarketingIntegralTaskRecord::getMemberListId,memberId));
            if(count>=marketingIntegralTask.getTimes().intValue()){
                return 2;
            }else {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean registerSuccess(String memberId) {
        //邀请注册
        invitationRegister(memberId);

        return onlyTask(memberId,"0");
    }

    @Override
    public boolean transactionPassword(String memberId) {
        MemberList  memberList=iMemberListService.getById(memberId);
        //是否设置交易密码
        if(StringUtils.isNotBlank(memberList.getTransactionPassword())){
            return false;
        }
        return onlyTask(memberId,"1");
    }

    @Override
    public boolean invitedSign(String memberId,String tMemberId) {
        MarketingIntegralSetting marketingIntegralSetting=iMarketingIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralSetting>()
                .eq(MarketingIntegralSetting::getStatus,"1"));
        if(marketingIntegralSetting==null){
            return false;
        }
        if(StringUtils.isNotBlank(tMemberId)){
            return manyTimesDay(tMemberId,"4");
        }
        return true;
    }

    @Override
    public boolean invitationRegister(String memberId) {
        MemberList  memberList=iMemberListService.getById(memberId);
        //推广人是会员
        if(memberList.getPromoterType().equals("1")){
            MemberList memberList1=iMemberListService.getById(memberList.getPromoter());
            return manyTimesDay(memberList1.getId(),"5");
        }
        return true;
    }

    @Override
    public BigDecimal dailyBrowsing(String memberId) {
        MarketingIntegralSetting marketingIntegralSetting=iMarketingIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralSetting>()
                .eq(MarketingIntegralSetting::getStatus,"1"));
        if(marketingIntegralSetting==null){
            return new BigDecimal(0);
        }
        MarketingIntegralTask marketingIntegralTask=this.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getTaskType,"3")
                .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
        if(marketingIntegralTask==null){
            return new BigDecimal(0);
        }
        singleDaily(memberId,"3");
        return marketingIntegralTask.getAward();
    }

    @Override
    public boolean manyTimesDay(String memberId, String taskType) {
        MarketingIntegralTaskBaseSetting marketingIntegralTaskBaseSetting=iMarketingIntegralTaskBaseSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralTaskBaseSetting>()
                .eq(MarketingIntegralTaskBaseSetting::getStatus,"1"));
        if(marketingIntegralTaskBaseSetting==null){
            return false;
        }
        MarketingIntegralTask marketingIntegralTask=this.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getTaskType,taskType)
                .eq(MarketingIntegralTask::getTaskeMode,"0")
                .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
        if(marketingIntegralTask==null){
            return false;
        }
        //控制会员每日次数
        Calendar calendar=Calendar.getInstance();
        long count=iMarketingIntegralTaskRecordService.count(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                .eq(MarketingIntegralTaskRecord::getYear,calendar.get(Calendar.YEAR))
                .eq(MarketingIntegralTaskRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingIntegralTaskRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                .eq(MarketingIntegralTaskRecord::getMemberListId,memberId));
        if(count>marketingIntegralTask.getTimes().intValue()){
            return false;
        }
        //有奖励
        return iMarketingIntegralTaskRecordService.addMarketingIntegralTaskRecord(marketingIntegralTask,memberId);
    }

    @Override
    public boolean singleDaily(String memberId, String taskType) {
        MarketingIntegralTaskBaseSetting marketingIntegralTaskBaseSetting=iMarketingIntegralTaskBaseSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralTaskBaseSetting>()
                .eq(MarketingIntegralTaskBaseSetting::getStatus,"1"));
        if(marketingIntegralTaskBaseSetting==null){
            return false;
        }
        MarketingIntegralTask marketingIntegralTask=this.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getTaskType,taskType)
                .eq(MarketingIntegralTask::getTaskeMode,"1")
                .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
        if(marketingIntegralTask==null){
            return false;
        }
        //控制会员每日次数
        Calendar calendar=Calendar.getInstance();
        long count=iMarketingIntegralTaskRecordService.count(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                .eq(MarketingIntegralTaskRecord::getYear,calendar.get(Calendar.YEAR))
                .eq(MarketingIntegralTaskRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingIntegralTaskRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                .eq(MarketingIntegralTaskRecord::getMemberListId,memberId));
        if(count>marketingIntegralTask.getTimes().intValue()){
            return false;
        }
        //有奖励
        return iMarketingIntegralTaskRecordService.addMarketingIntegralTaskRecord(marketingIntegralTask,memberId);
    }

    @Override
    public boolean onlyTask(String memberId, String taskType) {
        MarketingIntegralTaskBaseSetting marketingIntegralTaskBaseSetting=iMarketingIntegralTaskBaseSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralTaskBaseSetting>()
                .eq(MarketingIntegralTaskBaseSetting::getStatus,"1"));
        if(marketingIntegralTaskBaseSetting==null){
            return false;
        }
        MarketingIntegralTask marketingIntegralTask=this.getOne(new LambdaQueryWrapper<MarketingIntegralTask>()
                .eq(MarketingIntegralTask::getStatus,"1")
                .eq(MarketingIntegralTask::getTaskType,taskType)
                .eq(MarketingIntegralTask::getTaskeMode,"2")
                .orderByDesc(MarketingIntegralTask::getCreateTime).last("limit 1"));
        if(marketingIntegralTask==null){
            return false;
        }
        //控制会员每日次数
        long count=iMarketingIntegralTaskRecordService.count(new LambdaQueryWrapper<MarketingIntegralTaskRecord>()
                .eq(MarketingIntegralTaskRecord::getMarketingIntegralTaskId,marketingIntegralTask.getId())
                .eq(MarketingIntegralTaskRecord::getMemberListId,memberId));
        if(count>marketingIntegralTask.getTimes().intValue()){
            return false;
        }
        //有奖励
        return iMarketingIntegralTaskRecordService.addMarketingIntegralTaskRecord(marketingIntegralTask,memberId);
    }


}
