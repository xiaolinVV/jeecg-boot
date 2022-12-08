package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingActivityRewardDTO;
import org.jeecg.modules.marketing.entity.MarketingActivityReward;
import org.jeecg.modules.marketing.vo.MarketingActivityRewardVO;

/**
 * @Description: 活动奖励
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
public interface IMarketingActivityRewardService extends IService<MarketingActivityReward> {

    IPage<MarketingActivityRewardVO> queryPageList(Page<MarketingActivityRewardVO> page, MarketingActivityRewardDTO marketingActivityRewardDTO);
}
