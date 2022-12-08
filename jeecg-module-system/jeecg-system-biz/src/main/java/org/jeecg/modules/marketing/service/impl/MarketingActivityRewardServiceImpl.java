package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingActivityRewardDTO;
import org.jeecg.modules.marketing.entity.MarketingActivityReward;
import org.jeecg.modules.marketing.mapper.MarketingActivityRewardMapper;
import org.jeecg.modules.marketing.service.IMarketingActivityRewardService;
import org.jeecg.modules.marketing.vo.MarketingActivityRewardVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 活动奖励
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Service
public class MarketingActivityRewardServiceImpl extends ServiceImpl<MarketingActivityRewardMapper, MarketingActivityReward> implements IMarketingActivityRewardService {

    @Override
    public IPage<MarketingActivityRewardVO> queryPageList(Page<MarketingActivityRewardVO> page, MarketingActivityRewardDTO marketingActivityRewardDTO) {
        return baseMapper.queryPageList(page,marketingActivityRewardDTO);
    }
}
