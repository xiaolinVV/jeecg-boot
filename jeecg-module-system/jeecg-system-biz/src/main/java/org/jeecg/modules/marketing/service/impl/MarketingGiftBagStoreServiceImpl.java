package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingGiftBagStore;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagStoreMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagStoreService;
import org.jeecg.modules.store.dto.StoreManageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 礼包店铺映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Service
public class MarketingGiftBagStoreServiceImpl extends ServiceImpl<MarketingGiftBagStoreMapper, MarketingGiftBagStore> implements IMarketingGiftBagStoreService {
    @Autowired(required = false)
    private MarketingGiftBagStoreMapper marketingGiftBagStoreMapper;
    @Override
    public List<StoreManageDTO> findStoreById(String marketingGiftBagId) {
        return marketingGiftBagStoreMapper.findStoreById(marketingGiftBagId);
    }
}
