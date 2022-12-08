package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.vo.MarketingDistributionSettingVO;

/**
 * @Description: 平台分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
public interface MarketingDistributionSettingMapper extends BaseMapper<MarketingDistributionSetting> {

    IPage<MarketingDistributionSettingVO> findDistributionSettingList(Page<MarketingDistributionSettingVO> page,@Param("marketingDistributionSettingVO") MarketingDistributionSettingVO marketingDistributionSettingVO);
}
