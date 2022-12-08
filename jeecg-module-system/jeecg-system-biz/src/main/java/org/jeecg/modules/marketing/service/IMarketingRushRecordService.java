package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingRushRecord;

import java.util.Map;

/**
 * @Description: 抢购活动-购买记录
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface IMarketingRushRecordService extends IService<MarketingRushRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingRushRecord> queryWrapper, Map<String, Object> requestMap);

    IPage<Map<String,Object>> findMarketingRushRecord(Page<Map<String,Object>> page, String memberId, String status);

    Map<String,Object> getMarketingRushRecordParticulars(String id);

    Map<String,Object> findTodayRushData(String memberId, String marketingRushTypeId);

    /**
     * 分配抢购奖励
     * @param marketingRushRecordId
     */
    public void classificationReward(String marketingRushRecordId);

    /**
     * 寄售清算
     * @param marketingRushGroupId
     */
    public void groupDistributionRewards(String marketingRushGroupId);


    /**
     * 可寄售清算
     * @param marketingRushGroupId
     */
    public void groupConsignmentDistributionRewards(String marketingRushGroupId);

}
