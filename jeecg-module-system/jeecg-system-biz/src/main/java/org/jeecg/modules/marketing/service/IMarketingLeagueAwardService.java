package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingLeagueAward;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟专区-资金分配明细
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
public interface IMarketingLeagueAwardService extends IService<MarketingLeagueAward> {

    /**
     *
     * @param marketingLeagueBuyerRecordId
     * @param memberListId
     * @param marketingLeagueIdentityId
     * @param award
     * @param rewardType
     */
    public boolean add(String marketingLeagueBuyerRecordId, String memberListId, String marketingLeagueIdentityId, BigDecimal award,String rewardType,String marketingLeagueTypeId);


    /**
     * 分配奖励
     * @param marketingLeagueBuyerRecordId
     */
    public void award(String marketingLeagueBuyerRecordId);


    /**
     * 累计奖励
     *
     * @param memberId
     * @return
     */
    public Double cumulativeRewards(String memberId);


    /**
     * 获取交易记录的交易类型
     *
     * @param memberId
     * @return
     */
    public List<Map<String,Object>> getMarketingLeagueAwardBymarketingLeagueTypeId(String memberId);

    /**
     * 获取奖励记录
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingLeagueAwardList(Page<Map<String,Object>> page,Map<String,Object>paramMap);



    /**
     *
     * @param marketingLeagueBuyerRecordId
     * @return
     */
    public List<Map<String,Object>> getMarketingLeagueAwardListById(String marketingLeagueBuyerRecordId);

}
