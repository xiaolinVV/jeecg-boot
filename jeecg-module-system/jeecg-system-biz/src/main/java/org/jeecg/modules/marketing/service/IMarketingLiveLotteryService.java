package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingLiveLottery;

import java.util.Map;

/**
 * @Description: 直播管理-直播抽奖
 * @Author: jeecg-boot
 * @Date:   2021-09-15
 * @Version: V1.0
 */
public interface IMarketingLiveLotteryService extends IService<MarketingLiveLottery> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingLiveLottery> queryWrapper, Map<String, Object> requestMap);

    IPage<Map<String,Object>> getMarketingLiveLotteryListByStreamingId(Page<Map<String,Object>> page, String marketingLiveStreamingId);

    Long getTimeInfo(String id);
}
