package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingLeagueSetting;
import org.jeecg.modules.marketing.mapper.MarketingLeagueSettingMapper;
import org.jeecg.modules.marketing.service.IMarketingLeagueSettingService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 加盟专区设置
 * @Author: jeecg-boot
 * @Date:   2021-12-16
 * @Version: V1.0
 */
@Service
@Log
public class MarketingLeagueSettingServiceImpl extends ServiceImpl<MarketingLeagueSettingMapper, MarketingLeagueSetting> implements IMarketingLeagueSettingService {

    @Override
    public MarketingLeagueSetting getMarketingLeagueSetting() {
        return this.getOne(new LambdaQueryWrapper<MarketingLeagueSetting>().eq(MarketingLeagueSetting::getStatus,"1"));
    }

    @Override
    public Map<String, Object> settingMarketingLeagueView(Map<String,Object> resultMap,String softModel) {
        MarketingLeagueSetting marketingLeagueSetting=this.getMarketingLeagueSetting();
        Map<String,Object> marketingLeagueSettingMap= Maps.newHashMap();
        if(marketingLeagueSetting==null){
            marketingLeagueSettingMap.put("isViewMarketingLeagueSettingMap","0");
        }else {
            log.info("加盟专区分端控制：softModel=" + softModel);
            if (marketingLeagueSetting.getPointsDisplay().equals("0")) {
                marketingLeagueSettingMap.put("isViewMarketingLeagueSettingMap", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingLeagueSetting.getPointsDisplay().equals("1")) {
                    marketingLeagueSettingMap.put("isViewMarketingLeagueSettingMap", "1");
                } else if ((softModel.equals("1") || softModel.equals("2")) && marketingLeagueSetting.getPointsDisplay().equals("2")) {
                    marketingLeagueSettingMap.put("isViewMarketingLeagueSettingMap", "1");
                } else {
                    marketingLeagueSettingMap.put("isViewMarketingLeagueSettingMap", "0");
                }
        }
        resultMap.put("marketingLeagueSettingMap",marketingLeagueSettingMap);
        return resultMap;
    }
}
