package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingFreeGoodType;
import org.jeecg.modules.marketing.mapper.MarketingFreeGoodTypeMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeGoodTypeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单商品类型
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
public class MarketingFreeGoodTypeServiceImpl extends ServiceImpl<MarketingFreeGoodTypeMapper, MarketingFreeGoodType> implements IMarketingFreeGoodTypeService {

    @Override
    public List<Map<String, Object>> getAllMarketingFreeGoodType() {
        return baseMapper.getAllMarketingFreeGoodType();
    }
}
