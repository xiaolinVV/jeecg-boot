package org.jeecg.modules.marketing.store.giftbag.service;

import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 礼包团-礼包设置
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
public interface IMarketingStoreGiftbagListService extends IService<MarketingStoreGiftbagList> {

    /**
     * 根据店铺id获取数据
     *
     * @param storeManageId
     * @return
     */
    public MarketingStoreGiftbagList getMarketingStoreGiftbagListByStoreManageId(String storeManageId);

}
