package org.jeecg.modules.marketing.service;

import org.jeecg.modules.marketing.entity.MarketingFreeSessionSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 免单场次设置
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface IMarketingFreeSessionSettingService extends IService<MarketingFreeSessionSetting> {

    /**
     * 自动创建场次定时器
     */
    public void autoCreate();

}
