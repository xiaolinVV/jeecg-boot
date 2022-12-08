package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.MarketingLiveBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingLiveLottery;
import org.jeecg.modules.marketing.entity.MarketingLiveStreaming;
import org.jeecg.modules.marketing.mapper.MarketingLiveStreamingMapper;
import org.jeecg.modules.marketing.service.IMarketingLiveBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingLiveLotteryService;
import org.jeecg.modules.marketing.service.IMarketingLiveStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 直播管理
 * @Author: jeecg-boot
 * @Date:   2021-04-24
 * @Version: V1.0
 */
@Service
public class MarketingLiveStreamingServiceImpl extends ServiceImpl<MarketingLiveStreamingMapper, MarketingLiveStreaming> implements IMarketingLiveStreamingService {
    @Autowired
    private IMarketingLiveBaseSettingService iMarketingLiveBaseSettingService;
    @Autowired
    private IMarketingLiveLotteryService iMarketingLiveLotteryService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public IPage<Map<String, Object>> getMarketingLiveStreamingList(Page<Map<String, Object>> page) {
        return baseMapper.getMarketingLiveStreamingList(page);
    }

    @Override
    public Map<String, Object> getMarketingLiveStreamingListById(String id) {
        MarketingLiveStreaming marketingLiveStreaming=this.getById(id);
        marketingLiveStreaming.setOnlineNumber(marketingLiveStreaming.getOnlineNumber().add(new BigDecimal(1)));

        this.saveOrUpdate(marketingLiveStreaming);
        Map<String, Object> marketingLiveStreamingListById = baseMapper.getMarketingLiveStreamingListById(id);
        String s = redisTemplate.opsForValue().get(id);
        if (StringUtils.isBlank(s)){
            redisTemplate.opsForValue().set(id,String.valueOf(marketingLiveStreamingListById.get("onlineNumber")),10, TimeUnit.HOURS);
        }else {
            redisTemplate.opsForValue().set(id,String.valueOf(marketingLiveStreamingListById.get("onlineNumber")),0);
        }
        MarketingLiveBaseSetting marketingLiveBaseSetting = iMarketingLiveBaseSettingService.getOne(new LambdaQueryWrapper<MarketingLiveBaseSetting>()
                .eq(MarketingLiveBaseSetting::getDelFlag, "0")
                .orderByDesc(MarketingLiveBaseSetting::getCreateTime)
                .last("limit 1")
        );
        marketingLiveStreamingListById.put("liveDescription",marketingLiveBaseSetting.getLiveDescription());
        MarketingLiveLottery marketingLiveLottery = iMarketingLiveLotteryService.getOne(new LambdaQueryWrapper<MarketingLiveLottery>()
                .eq(MarketingLiveLottery::getDelFlag, "0")
                .eq(MarketingLiveLottery::getMarketingLiveStreamingId, marketingLiveStreaming.getId())
                .eq(MarketingLiveLottery::getCancelNumber, "0")
                .eq(MarketingLiveLottery::getStatus, "0")
                .apply("date_format(lottery_time,'%Y-%m-%d %H:%i:%s') > {0}",DateUtils.now())
                .orderByAsc(MarketingLiveLottery::getLotteryNumber)
                .last("limit 1")
        );
        if (marketingLiveLottery != null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("lotteryName",marketingLiveLottery.getLotteryName());
            map.put("lotteryTime",iMarketingLiveLotteryService.getTimeInfo(marketingLiveLottery.getId()));
            map.put("lotteryQualification",marketingLiveLottery.getLotteryQualification());
            map.put("lotteryId",marketingLiveLottery.getId());
            marketingLiveStreamingListById.put("lottery",map);
        }else {
            marketingLiveStreamingListById.put("lottery","");
        }

        return marketingLiveStreamingListById;
    }
}
