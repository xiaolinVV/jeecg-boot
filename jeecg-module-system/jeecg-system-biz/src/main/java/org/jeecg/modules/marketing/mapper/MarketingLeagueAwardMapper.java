package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingLeagueAward;

import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟专区-资金分配明细
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
public interface MarketingLeagueAwardMapper extends BaseMapper<MarketingLeagueAward> {


    /**
     * 累计奖励
     * @param memberId
     * @return
     */
    public Double cumulativeRewards(@Param("memberId") String memberId);


    /**
     * 获取交易记录的交易类型
     *
     * @param memberId
     * @return
     */
    public List<Map<String,Object>> getMarketingLeagueAwardBymarketingLeagueTypeId(@Param("memberId") String memberId);


    /**
     *  获取奖励记录
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingLeagueAwardList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object>paramMap);

    /**
     *
     * @param marketingLeagueBuyerRecordId
     * @return
     */
    public List<Map<String,Object>> getMarketingLeagueAwardListById(@Param("marketingLeagueBuyerRecordId") String marketingLeagueBuyerRecordId);

}
