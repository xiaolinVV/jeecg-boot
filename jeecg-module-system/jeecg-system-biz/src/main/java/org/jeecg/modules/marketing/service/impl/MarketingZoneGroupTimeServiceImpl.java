package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupTimeMapper;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupTimeService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 拼团次数
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
@Service
public class MarketingZoneGroupTimeServiceImpl extends ServiceImpl<MarketingZoneGroupTimeMapper, MarketingZoneGroupTime> implements IMarketingZoneGroupTimeService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingZoneGroupTime> queryWrapper, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,queryWrapper,requestMap);
    }
}
