package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingFreeGoodSpecification;
import org.jeecg.modules.marketing.mapper.MarketingFreeGoodSpecificationMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeGoodSpecificationService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单商品
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
public class MarketingFreeGoodSpecificationServiceImpl extends ServiceImpl<MarketingFreeGoodSpecificationMapper, MarketingFreeGoodSpecification> implements IMarketingFreeGoodSpecificationService {

    @Override
    public List<Map<String, Object>> selectMarketingFreeGoodSpecificationByMarketingFreeGoodListId(String marketingFreeGoodListId) {
        return baseMapper.selectMarketingFreeGoodSpecificationByMarketingFreeGoodListId(marketingFreeGoodListId);
    }

    @Override
    public Map<String, Object> selectMarketingFreeGoodSpecificationBySmallprice(String marketingFreeGoodListId) {
        return baseMapper.selectMarketingFreeGoodSpecificationBySmallprice(marketingFreeGoodListId);
    }
}
