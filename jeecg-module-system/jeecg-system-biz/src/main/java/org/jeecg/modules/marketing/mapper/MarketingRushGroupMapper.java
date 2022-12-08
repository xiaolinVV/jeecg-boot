package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingRushGroup;

import java.util.Map;

/**
 * @Description: 抢购活动-寄售记录
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface MarketingRushGroupMapper extends BaseMapper<MarketingRushGroup> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingRushGroup> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    IPage<Map<String,Object>> findMarketingRushGroup(Page<Map<String,Object>> page,@Param("memberId") String memberId,@Param("consignmentStatus") String consignmentStatus);

    IPage<Map<String,Object>> findMarketingRushGoodPage(Page<Map<String,Object>> page,@Param("id") String id,@Param("memberListId") String memberListId);
}
