package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingIntegralTask;
import org.jeecg.modules.marketing.entity.MarketingIntegralTaskRecord;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;
import org.jeecg.modules.marketing.mapper.MarketingIntegralTaskRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingIntegralRecordService;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskRecordService;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupTimeService;
import org.jeecg.modules.marketing.service.IMarketingZoneSpellGroupRecordService;
import org.jeecg.modules.marketing.vo.MarketingIntegralTaskRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * @Description: 积分任务记录
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Service
public class MarketingIntegralTaskRecordServiceImpl extends ServiceImpl<MarketingIntegralTaskRecordMapper, MarketingIntegralTaskRecord> implements IMarketingIntegralTaskRecordService {


    @Autowired
    private IMarketingIntegralRecordService iMarketingIntegralRecordService;

    @Autowired
    private IMarketingZoneSpellGroupRecordService iMarketingZoneSpellGroupRecordService;

    @Autowired
    private IMarketingZoneGroupTimeService iMarketingZoneGroupTimeService;


    @Override
    @Transactional
    public boolean addMarketingIntegralTaskRecord(MarketingIntegralTask marketingIntegralTask, String memberListId) {
        MarketingIntegralTaskRecord marketingIntegralTaskRecord=new MarketingIntegralTaskRecord();
        marketingIntegralTaskRecord.setMemberListId(memberListId);//会员列表id
        marketingIntegralTaskRecord.setMarketingIntegralTaskId(marketingIntegralTask.getId());//积分任务id
        marketingIntegralTaskRecord.setAward(marketingIntegralTask.getAward());//奖励
        marketingIntegralTaskRecord.setStatus("1");//状态：0：未领取；1：已领取
        marketingIntegralTaskRecord.setDrawTime(new Date());//领取时间
        marketingIntegralTaskRecord.setTaskType(marketingIntegralTask.getTaskType());//任务类型；字典类型：0：注册成功；1：交易密码；2：连续签到；3：每日浏览；4；邀请签到；5：邀请注册
        marketingIntegralTaskRecord.setTaskTitle(marketingIntegralTask.getTaskTitle());//任务标题
        marketingIntegralTaskRecord.setAwardType(marketingIntegralTask.getAwardType());//奖励类型；0：积分
        marketingIntegralTaskRecord.setTaskDescription(marketingIntegralTask.getTaskDescription());//任务描述
        marketingIntegralTaskRecord.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
        this.save(marketingIntegralTaskRecord);
        //奖励水滴积分
        if(marketingIntegralTask.getAwardType().equals("0")) {
            if(!iMarketingIntegralRecordService.addMarketingIntegralRecord(marketingIntegralTaskRecord.getTaskType(), marketingIntegralTaskRecord.getAward(), memberListId, marketingIntegralTaskRecord.getSerialNumber())){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        //奖励专区团参团次数
        if(marketingIntegralTask.getAwardType().equals("1")){
            //建团次数和参团次数
            MarketingZoneGroupTime marketingZoneGroupTime = iMarketingZoneGroupTimeService.getOne(new LambdaQueryWrapper<MarketingZoneGroupTime>()
                    .eq(MarketingZoneGroupTime::getMemberListId, memberListId)
                    .orderByDesc(MarketingZoneGroupTime::getCreateTime)
                    .last("limit 1"));
            if(marketingZoneGroupTime==null){
                marketingZoneGroupTime=new MarketingZoneGroupTime();
                marketingZoneGroupTime.setMemberListId(memberListId);
                iMarketingZoneGroupTimeService.save(marketingZoneGroupTime);
            }
            if(!iMarketingZoneSpellGroupRecordService.addMarketingZoneSpellGroupRecord(marketingZoneGroupTime.getId(),"3",marketingIntegralTaskRecord.getAward(),marketingIntegralTaskRecord.getSerialNumber())){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return true;
    }

    @Override
    public IPage<MarketingIntegralTaskRecordVO> queryPageList(Page<MarketingIntegralTaskRecordVO> page, QueryWrapper<MarketingIntegralTaskRecordVO> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }
}
