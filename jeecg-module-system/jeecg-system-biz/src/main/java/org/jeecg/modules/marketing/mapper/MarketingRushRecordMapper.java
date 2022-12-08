package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingRushRecord;

import java.util.Map;

/**
 * @Description: 抢购活动-购买记录
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface MarketingRushRecordMapper extends BaseMapper<MarketingRushRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingRushRecord> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    IPage<Map<String,Object>> findMarketingRushRecord(Page<Map<String,Object>> page,@Param("memberId") String memberId,@Param("status") String status);

    Integer findTodayRushDataCount(@Param("memberId") String memberId,@Param("marketingRushTypeId") String marketingRushTypeId,@Param("status") String status);

    Integer countConsignmentGoods(@Param("marketingRushTypeId") String marketingRushTypeId,@Param("memberId") String memberId);
}
