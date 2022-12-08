package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingGroupIntegralAdvertising;
import org.jeecg.modules.marketing.mapper.MarketingGroupIntegralAdvertisingMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralAdvertisingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼购广告
 * @Author: jeecg-boot
 * @Date:   2021-06-27
 * @Version: V1.0
 */
@Service
public class MarketingGroupIntegralAdvertisingServiceImpl extends ServiceImpl<MarketingGroupIntegralAdvertisingMapper, MarketingGroupIntegralAdvertising> implements IMarketingGroupIntegralAdvertisingService {

    @Override
    public List<Map<String, Object>> getAdvertising() {
        return baseMapper.getAdvertising();
    }
}
