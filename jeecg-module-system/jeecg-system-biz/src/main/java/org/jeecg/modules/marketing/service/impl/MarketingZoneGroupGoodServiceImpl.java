package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGood;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupGoodMapper;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupGoodService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
@Service
public class MarketingZoneGroupGoodServiceImpl extends ServiceImpl<MarketingZoneGroupGoodMapper, MarketingZoneGroupGood> implements IMarketingZoneGroupGoodService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingZoneGroupGood> queryWrapper, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,queryWrapper,requestMap);
    }

    @Override
    public IPage<Map<String, Object>> getByMarketingZoneGroupId(Page<Map<String, Object>> page, String marketingZoneGroupId) {
        return baseMapper.getByMarketingZoneGroupId(page,marketingZoneGroupId);
    }
}
