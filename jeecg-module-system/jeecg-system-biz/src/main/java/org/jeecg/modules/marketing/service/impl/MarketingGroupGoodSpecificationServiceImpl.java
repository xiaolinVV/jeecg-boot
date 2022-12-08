package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingGroupGoodSpecification;
import org.jeecg.modules.marketing.mapper.MarketingGroupGoodSpecificationMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodSpecificationService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
@Service
public class MarketingGroupGoodSpecificationServiceImpl extends ServiceImpl<MarketingGroupGoodSpecificationMapper, MarketingGroupGoodSpecification> implements IMarketingGroupGoodSpecificationService {

    @Override
    public List<Map<String, Object>> selectMarketingGroupGoodSpecificationByMarketingGroupGoodListId(String marketingGroupGoodListId) {
        return baseMapper.selectMarketingGroupGoodSpecificationByMarketingGroupGoodListId(marketingGroupGoodListId);
    }
}
