package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingChannel;
import org.jeecg.modules.marketing.vo.MarketingChannelVO;

/**
 * @Description: 发券渠道
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface IMarketingChannelService extends IService<MarketingChannel> {

    IPage<MarketingChannelVO> findMarkeingChannel(Page<MarketingChannelVO> page, MarketingChannelVO marketingChannelVO);
}
