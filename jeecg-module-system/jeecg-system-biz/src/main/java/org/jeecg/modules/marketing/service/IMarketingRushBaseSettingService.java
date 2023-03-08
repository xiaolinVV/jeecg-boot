package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingRushBaseSetting;

import java.util.Map;

/**
 * @Description: 抢购活动-基础设置
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface IMarketingRushBaseSettingService extends IService<MarketingRushBaseSetting> {

    /**
     * 获取抢购配置信息
     *
     * @return
     */
    public MarketingRushBaseSetting getMarketingRushBaseSetting();

    /**
     * 显示设置
     * @param resultMap
     */
    public void settingView(Map<String,Object> resultMap,String softModel);

}
