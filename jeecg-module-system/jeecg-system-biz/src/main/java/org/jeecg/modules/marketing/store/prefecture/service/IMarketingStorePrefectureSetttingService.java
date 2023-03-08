package org.jeecg.modules.marketing.store.prefecture.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureSettting;

import java.util.Map;

/**
 * @Description: 店铺专区设置
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface IMarketingStorePrefectureSetttingService extends IService<MarketingStorePrefectureSettting> {

    /**
     * 获取店铺专区配置信息
     *
     * @return
     */
    public MarketingStorePrefectureSettting getMarketingStorePrefectureSettting();

    /**
     * 设置视图展示
     *
     * @param resultMap
     * @param softModel
     * @param storeManageId
     */
    public void settingView(Map<String, Object> resultMap, String softModel, String storeManageId);

}
