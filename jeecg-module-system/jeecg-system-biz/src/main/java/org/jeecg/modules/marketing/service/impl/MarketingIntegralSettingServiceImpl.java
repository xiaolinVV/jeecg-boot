package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingIntegralSetting;
import org.jeecg.modules.marketing.mapper.MarketingIntegralSettingMapper;
import org.jeecg.modules.marketing.service.IMarketingIntegralSettingService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 积分设置
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Service
@Log
public class MarketingIntegralSettingServiceImpl extends ServiceImpl<MarketingIntegralSettingMapper, MarketingIntegralSetting> implements IMarketingIntegralSettingService {

    @Override
    public MarketingIntegralSetting getMarketingIntegralSetting() {
        return this.getOne(new LambdaQueryWrapper<MarketingIntegralSetting>()
                .eq(MarketingIntegralSetting::getStatus,"1"));
    }

    @Override
    public Map<String, Object> getgetMarketingIntegralSettingMap(String softModel) {
        Map<String,Object> resultMap= Maps.newHashMap();
        MarketingIntegralSetting marketingIntegralSetting=this.getMarketingIntegralSetting();
        if(marketingIntegralSetting==null){
            resultMap.put("isViewMarketingIntegral","0");
        }else{
            log.info("积分签到分端控制：softModel=" + softModel);
            resultMap.put("integralName",marketingIntegralSetting.getIntegralName());
            resultMap.put("integralIcon",marketingIntegralSetting.getIntegralIcon());
            if(marketingIntegralSetting.getPointsDisplay().equals("0")){
                log.info("积分在任何端显示！！！");
                resultMap.put("isViewMarketingIntegral", "1");
            }else {
                //小程序
                if (softModel.equals("0") && marketingIntegralSetting.getPointsDisplay().equals("1")) {
                    log.info("积分活动在小程序端显示！！！");
                    resultMap.put("isViewMarketingIntegral", "1");
                } else if ((softModel.equals("1") || softModel.equals("2")) && marketingIntegralSetting.getPointsDisplay().equals("2")) {
                    log.info("积分活动在app端显示");
                    resultMap.put("isViewMarketingIntegral", "1");
                } else {
                    resultMap.put("isViewMarketingIntegral", "0");
                }
            }
        }
        return resultMap;
    }
}
