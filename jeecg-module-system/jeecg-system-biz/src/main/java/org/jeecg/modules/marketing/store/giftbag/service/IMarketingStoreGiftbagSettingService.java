package org.jeecg.modules.marketing.store.giftbag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagSetting;

import java.util.Map;

/**
 * @Description: 礼包团-设置
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
public interface IMarketingStoreGiftbagSettingService extends IService<MarketingStoreGiftbagSetting> {



    public MarketingStoreGiftbagSetting getMarketingStoreGiftbagSetting();


    /**
     *  显示设置
     *
     * @param resultMap
     * @param softModel
     */
    public void settingView(Map<String, Object> resultMap, String softModel,String storeManageId);

}
