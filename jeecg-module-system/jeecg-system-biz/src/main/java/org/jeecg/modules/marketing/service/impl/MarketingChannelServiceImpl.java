package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingChannel;
import org.jeecg.modules.marketing.mapper.MarketingChannelMapper;
import org.jeecg.modules.marketing.service.IMarketingChannelService;
import org.jeecg.modules.marketing.vo.MarketingChannelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 发券渠道
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
@Service
public class MarketingChannelServiceImpl extends ServiceImpl<MarketingChannelMapper, MarketingChannel> implements IMarketingChannelService {
    @Autowired(required = false)
    MarketingChannelMapper marketingChannelMapper;

    @Override
    public IPage<MarketingChannelVO> findMarkeingChannel(Page<MarketingChannelVO> page, MarketingChannelVO marketingChannelVO) {
        return marketingChannelMapper.findMarkeingChannel(page,marketingChannelVO);
    }
}
