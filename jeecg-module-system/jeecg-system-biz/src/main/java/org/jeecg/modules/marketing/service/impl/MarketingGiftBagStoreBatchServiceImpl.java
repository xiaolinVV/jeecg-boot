package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingGiftBagStoreBatch;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagStoreBatchMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagStoreBatchService;
import org.jeecg.modules.store.entity.StoreManage;
import org.springframework.stereotype.Service;

/**
 * @Description: 礼包采购店铺映射
 * @Author: jeecg-boot
 * @Date:   2020-08-29
 * @Version: V1.0
 */
@Service
public class MarketingGiftBagStoreBatchServiceImpl extends ServiceImpl<MarketingGiftBagStoreBatchMapper, MarketingGiftBagStoreBatch> implements IMarketingGiftBagStoreBatchService {

    @Override
    public IPage<StoreManage> findGiftBagStoreById(Page<StoreManage> page, String id) {
        return baseMapper.findGiftBagStoreById(page,id);
    }
}
