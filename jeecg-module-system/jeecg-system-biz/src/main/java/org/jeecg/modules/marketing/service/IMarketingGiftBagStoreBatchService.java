package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGiftBagStoreBatch;
import org.jeecg.modules.store.entity.StoreManage;

/**
 * @Description: 礼包采购店铺映射
 * @Author: jeecg-boot
 * @Date:   2020-08-29
 * @Version: V1.0
 */
public interface IMarketingGiftBagStoreBatchService extends IService<MarketingGiftBagStoreBatch> {

    IPage<StoreManage> findGiftBagStoreById(Page<StoreManage> page, String id);
}
