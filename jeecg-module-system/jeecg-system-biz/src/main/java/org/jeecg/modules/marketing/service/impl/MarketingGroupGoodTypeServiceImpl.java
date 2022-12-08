package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingGroupGoodType;
import org.jeecg.modules.marketing.mapper.MarketingGroupGoodTypeMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodTypeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-20
 * @Version: V1.0
 */
@Service
public class MarketingGroupGoodTypeServiceImpl extends ServiceImpl<MarketingGroupGoodTypeMapper, MarketingGroupGoodType> implements IMarketingGroupGoodTypeService {

    @Override
    public List<Map<String, Object>> getAllMarketingGroupGoodType() {
        return baseMapper.getAllMarketingGroupGoodType();
    }
}
