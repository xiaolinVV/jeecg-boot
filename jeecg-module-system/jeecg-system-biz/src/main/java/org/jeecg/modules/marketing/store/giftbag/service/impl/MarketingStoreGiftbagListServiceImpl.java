package org.jeecg.modules.marketing.store.giftbag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.mapper.MarketingStoreGiftbagListMapper;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.springframework.stereotype.Service;

/**
 * @Description: 礼包团-礼包设置
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
@Service
public class MarketingStoreGiftbagListServiceImpl extends ServiceImpl<MarketingStoreGiftbagListMapper, MarketingStoreGiftbagList> implements IMarketingStoreGiftbagListService {

    @Override
    public MarketingStoreGiftbagList getMarketingStoreGiftbagListByStoreManageId(String storeManageId) {
        return this.getOne(new LambdaQueryWrapper<MarketingStoreGiftbagList>().eq(MarketingStoreGiftbagList::getStoreManageId,storeManageId));
    }
}
