package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.modules.index.utils.SetttingViewUtils;
import org.jeecg.modules.marketing.entity.MarketingRushBaseSetting;
import org.jeecg.modules.marketing.mapper.MarketingRushBaseSettingMapper;
import org.jeecg.modules.marketing.service.IMarketingRushBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 抢购活动-基础设置
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
@Log
public class MarketingRushBaseSettingServiceImpl extends ServiceImpl<MarketingRushBaseSettingMapper, MarketingRushBaseSetting> implements IMarketingRushBaseSettingService {


    @Autowired
    private SetttingViewUtils setttingViewUtils;

    @Override
    public MarketingRushBaseSetting getMarketingRushBaseSetting() {
        return this.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getStatus,"1"));
    }

    @Override
    public void settingView(Map<String, Object> resultMap,String softModel) {
        MarketingRushBaseSetting marketingRushBaseSetting=this.getMarketingRushBaseSetting();
        Map<String,Object> marketingRushBaseSettingMap= Maps.newHashMap();
        setttingViewUtils.setView(resultMap,"marketingRushBaseSettingMap",marketingRushBaseSettingMap,"isViewMarketingRushBaseSetting",marketingRushBaseSetting,softModel);
    }
}
