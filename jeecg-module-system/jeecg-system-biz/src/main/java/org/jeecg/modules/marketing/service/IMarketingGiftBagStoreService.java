package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGiftBagStore;
import org.jeecg.modules.store.dto.StoreManageDTO;

import java.util.List;

/**
 * @Description: 礼包店铺映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface IMarketingGiftBagStoreService extends IService<MarketingGiftBagStore> {

    List<StoreManageDTO> findStoreById(String marketingGiftBagId);



}
