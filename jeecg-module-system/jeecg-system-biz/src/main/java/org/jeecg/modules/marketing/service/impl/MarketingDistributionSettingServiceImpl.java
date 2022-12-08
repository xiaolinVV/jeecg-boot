package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.mapper.MarketingDistributionSettingMapper;
import org.jeecg.modules.marketing.service.IMarketingDistributionSettingService;
import org.jeecg.modules.marketing.vo.MarketingDistributionSettingVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 平台分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
@Service
public class MarketingDistributionSettingServiceImpl extends ServiceImpl<MarketingDistributionSettingMapper, MarketingDistributionSetting> implements IMarketingDistributionSettingService {

    @Override
    public IPage<MarketingDistributionSettingVO> findDistributionSettingList(Page<MarketingDistributionSettingVO> page, MarketingDistributionSettingVO marketingDistributionSettingVO) {
        return baseMapper.findDistributionSettingList(page,marketingDistributionSettingVO);
    }
}
