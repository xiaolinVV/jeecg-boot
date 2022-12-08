package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingStoreDistributionSetting;
import org.jeecg.modules.marketing.vo.MarketingStoreDistributionSettingVO;

/**
 * @Description: 店铺分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
public interface IMarketingStoreDistributionSettingService extends IService<MarketingStoreDistributionSetting> {

    IPage<MarketingStoreDistributionSettingVO> findGiftBagStore(Page<MarketingStoreDistributionSettingVO> page, MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO);

    IPage<MarketingStoreDistributionSettingVO> findStoreDistribution(Page<MarketingStoreDistributionSettingVO> page,MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO);
}
