package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingChannel;
import org.jeecg.modules.marketing.vo.MarketingChannelVO;

/**
 * @Description: 发券渠道
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface MarketingChannelMapper extends BaseMapper<MarketingChannel> {
    IPage<MarketingChannelVO> findMarkeingChannel(Page<MarketingChannelVO> page, @Param("marketingChannelVO") MarketingChannelVO marketingChannelVO);
}
