package org.jeecg.modules.marketing.service;

import org.jeecg.modules.marketing.entity.MarketingLeagueSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 加盟专区设置
 * @Author: jeecg-boot
 * @Date:   2021-12-16
 * @Version: V1.0
 */
public interface IMarketingLeagueSettingService extends IService<MarketingLeagueSetting> {


    /**
     * 获取配置类
     *
     * @return
     */
    public MarketingLeagueSetting getMarketingLeagueSetting();

    /**
     * 设置显示和隐藏
     *
     * @param softModel
     * @return
     */
    public Map<String,Object> settingMarketingLeagueView(Map<String,Object> resultMap,String softModel);

}
