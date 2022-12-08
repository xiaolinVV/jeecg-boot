package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingStoreDistributionSetting;
import org.jeecg.modules.marketing.mapper.MarketingStoreDistributionSettingMapper;
import org.jeecg.modules.marketing.service.IMarketingStoreDistributionSettingService;
import org.jeecg.modules.marketing.vo.MarketingStoreDistributionSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 店铺分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
@Service
public class MarketingStoreDistributionSettingServiceImpl extends ServiceImpl<MarketingStoreDistributionSettingMapper, MarketingStoreDistributionSetting> implements IMarketingStoreDistributionSettingService {
    @Autowired(required = false)
    private MarketingStoreDistributionSettingMapper marketingStoreDistributionSettingMapper;
    @Override
    public IPage<MarketingStoreDistributionSettingVO> findGiftBagStore(Page<MarketingStoreDistributionSettingVO> page, MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO) {
        return marketingStoreDistributionSettingMapper.findGiftBagStore(page,marketingStoreDistributionSettingVO);
    }

    @Override
    public IPage<MarketingStoreDistributionSettingVO> findStoreDistribution(Page<MarketingStoreDistributionSettingVO> page,MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO) {
        return marketingStoreDistributionSettingMapper.findStoreDistribution(page,marketingStoreDistributionSettingVO);
    }
}
