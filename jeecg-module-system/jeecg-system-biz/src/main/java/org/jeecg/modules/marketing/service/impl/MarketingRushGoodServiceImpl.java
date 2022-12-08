package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingRushGood;
import org.jeecg.modules.marketing.mapper.MarketingRushGoodMapper;
import org.jeecg.modules.marketing.service.IMarketingRushGoodService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 抢购活动-分类商品
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
public class MarketingRushGoodServiceImpl extends ServiceImpl<MarketingRushGoodMapper, MarketingRushGood> implements IMarketingRushGoodService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingRushGood> queryWrapper, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,queryWrapper,requestMap);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingRushGoodByTypeId(Page<Map<String, Object>> page, String marketingRushTypeId) {
        return baseMapper.getMarketingRushGoodByTypeId(page,marketingRushTypeId);
    }
}
