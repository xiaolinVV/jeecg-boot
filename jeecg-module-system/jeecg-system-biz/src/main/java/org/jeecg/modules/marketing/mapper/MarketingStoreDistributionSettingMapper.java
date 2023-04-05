package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MemberDistributionDTO;
import org.jeecg.modules.marketing.entity.MarketingStoreDistributionSetting;
import org.jeecg.modules.marketing.vo.MarketingStoreDistributionSettingVO;

import java.util.List;

/**
 * @Description: 店铺分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
public interface MarketingStoreDistributionSettingMapper extends BaseMapper<MarketingStoreDistributionSetting> {

    IPage<MarketingStoreDistributionSettingVO> findGiftBagStore(Page<MarketingStoreDistributionSettingVO> page, @Param("marketingStoreDistributionSettingVO") MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO);

    IPage<MarketingStoreDistributionSettingVO> findStoreDistribution(Page<MarketingStoreDistributionSettingVO> page,@Param("marketingStoreDistributionSettingVO") MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO);

    List<MemberDistributionDTO> findById(String mId);
}
