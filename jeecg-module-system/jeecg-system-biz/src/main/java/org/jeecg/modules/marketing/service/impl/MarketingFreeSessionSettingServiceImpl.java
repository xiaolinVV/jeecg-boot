package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.MarketingFreeBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingFreeSession;
import org.jeecg.modules.marketing.entity.MarketingFreeSessionSetting;
import org.jeecg.modules.marketing.mapper.MarketingFreeSessionSettingMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionService;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 免单场次设置
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
@Log
public class MarketingFreeSessionSettingServiceImpl extends ServiceImpl<MarketingFreeSessionSettingMapper, MarketingFreeSessionSetting> implements IMarketingFreeSessionSettingService {

    @Autowired
    private IMarketingFreeSessionService iMarketingFreeSessionService;

    @Autowired
    private IMarketingFreeBaseSettingService iMarketingFreeBaseSettingService;


    @Override
    public void autoCreate() {
        MarketingFreeSessionSetting marketingFreeSessionSetting=this.getOne(new LambdaQueryWrapper<>());
        //判断为空
        if(marketingFreeSessionSetting==null){
            return;
        }
        //判断是否为自动
        if(marketingFreeSessionSetting.getCreateMode().equals("0")){
            return;
        }
        //判断是否启用
        if(marketingFreeSessionSetting.getExecutingState().equals("0")){
            return;
        }

        MarketingFreeBaseSetting marketingFreeBaseSetting=iMarketingFreeBaseSettingService.getOne(new LambdaQueryWrapper<MarketingFreeBaseSetting>());
        if(marketingFreeBaseSetting==null){
            return;
        }
        //查询执行时间
        if(new Date().getTime()-marketingFreeSessionSetting.getNextTime().getTime()>=0){
            MarketingFreeSession marketingFreeSessionexe=iMarketingFreeSessionService.getOne(new LambdaQueryWrapper<MarketingFreeSession>()
                    .ne(MarketingFreeSession::getStatus,"2")
                    .orderByDesc(MarketingFreeSession::getEndTime)
                    .last("limit 1"));
            if(marketingFreeSessionexe!=null){
                Calendar execalendar=Calendar.getInstance();
                execalendar.setTime(marketingFreeSessionexe.getEndTime());
                execalendar.add(Calendar.DATE,1);
                String exeTime=DateUtils.date2Str(execalendar.getTime(),DateUtils.date_sdf.get());
                if(new Date().getTime()<DateUtils.str2Date(exeTime,DateUtils.date_sdf.get()).getTime()){
                    return;
                }
            }
            //执行场次创建数据
            int activityCycle=marketingFreeSessionSetting.getActivityCycle().intValue();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(marketingFreeSessionSetting.getNextTime());
           //创建场次活动
            MarketingFreeSession marketingFreeSession=new MarketingFreeSession();
            marketingFreeSession.setSerialNumber("T"+calendar.get(Calendar.YEAR)+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DAY_OF_MONTH)+ RandomUtils.nextInt(10,99));//编号
            marketingFreeSession.setMarketingFreeBaseSettingId(marketingFreeBaseSetting.getId());//场次设置id
            marketingFreeSession.setPeriodDays(marketingFreeSessionSetting.getActivityCycle());//周期天数
            marketingFreeSession.setStartTime(calendar.getTime());//开始时间
            calendar.add(Calendar.DATE,activityCycle-1);
            marketingFreeSession.setEndTime(calendar.getTime());//结束时间
            marketingFreeSession.setCreateMode("1");//创建方式；0：手动创建；1：自动创建
            iMarketingFreeSessionService.save(marketingFreeSession);
            log.info("自动创建创次成功id="+marketingFreeSession.getId());
            calendar.add(Calendar.DATE,1);
            marketingFreeSessionSetting.setNextTime(calendar.getTime());
            marketingFreeSessionSetting.setExecutingDegree(marketingFreeSessionSetting.getExecutingDegree().add(new BigDecimal(1)));
            this.saveOrUpdate(marketingFreeSessionSetting);

        }

    }
}
