package org.jeecg.modules.marketing.store.giftbag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.jeecg.modules.index.utils.SetttingViewUtils;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagSetting;
import org.jeecg.modules.marketing.store.giftbag.mapper.MarketingStoreGiftbagSettingMapper;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 礼包团-设置
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
@Service
public class MarketingStoreGiftbagSettingServiceImpl extends ServiceImpl<MarketingStoreGiftbagSettingMapper, MarketingStoreGiftbagSetting> implements IMarketingStoreGiftbagSettingService {

    @Autowired
    private SetttingViewUtils setttingViewUtils;

    @Autowired
    private IMarketingStoreGiftbagListService iMarketingStoreGiftbagListService;


    @Override
    public MarketingStoreGiftbagSetting getMarketingStoreGiftbagSetting() {
        return this.getOne(new LambdaQueryWrapper<MarketingStoreGiftbagSetting>()
                .eq(MarketingStoreGiftbagSetting::getStatus,"1"));
    }

    @Override
    public void settingView(Map<String, Object> resultMap, String softModel,String storeManageId) {
        MarketingStoreGiftbagSetting marketingStoreGiftbagSetting=this.getMarketingStoreGiftbagSetting();
        Map<String,Object> marketingStoreGiftbagSettingMap= Maps.newHashMap();
        //判断店铺是否有设置数据
        MarketingStoreGiftbagList marketingStoreGiftbagList=iMarketingStoreGiftbagListService.getMarketingStoreGiftbagListByStoreManageId(storeManageId);
        if(marketingStoreGiftbagList==null||marketingStoreGiftbagList.getStatus().equals("0")){
            marketingStoreGiftbagSettingMap.put("isViewMarketingStoreGiftbagSetting","0");
            resultMap.put("marketingStoreGiftbagSettingMap",marketingStoreGiftbagSettingMap);
            return;
        }else {
            setttingViewUtils.setView(resultMap, "marketingStoreGiftbagSettingMap", marketingStoreGiftbagSettingMap, "isViewMarketingStoreGiftbagSetting", marketingStoreGiftbagSetting, softModel);
        }
    }
}
