package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.vo.MarketingDistributionSettingVO;

/**
 * @Description: 平台分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
public interface IMarketingDistributionSettingService extends IService<MarketingDistributionSetting> {

    IPage<MarketingDistributionSettingVO> findDistributionSettingList(Page<MarketingDistributionSettingVO> page, MarketingDistributionSettingVO marketingDistributionSettingVO);
}
