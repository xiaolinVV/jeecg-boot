package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingRushGroup;

import java.util.Map;

/**
 * @Description: 抢购活动-寄售记录
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface IMarketingRushGroupService extends IService<MarketingRushGroup> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingRushGroup> queryWrapper, Map<String, Object> requestMap);

    IPage<Map<String,Object>> findMarketingRushGroup(Page<Map<String,Object>> page, String memberId, String consignmentStatus);

    Map<String,Object> getMarketingRushGroupParticulars(Page<Map<String,Object>> page, String id);

    boolean consignmentSales(MarketingRushGroup marketingRushGroup);
}
