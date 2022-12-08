package org.jeecg.modules.marketing.service;

import org.jeecg.modules.marketing.entity.MarketingIntegralSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 积分设置
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface IMarketingIntegralSettingService extends IService<MarketingIntegralSetting> {


    /**
     * 获取当前积分
     *
     * @return
     */
    public MarketingIntegralSetting getMarketingIntegralSetting();

    /**
     * 获取配置信息
     * @return
     */
    public Map<String,Object> getgetMarketingIntegralSettingMap(String softModel);

}
