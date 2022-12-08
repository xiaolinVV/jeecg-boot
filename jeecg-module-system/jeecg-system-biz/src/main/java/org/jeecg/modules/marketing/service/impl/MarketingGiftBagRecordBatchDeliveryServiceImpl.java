package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatchDelivery;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagRecordBatchDeliveryMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchDeliveryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 采购礼包配送信息
 * @Author: jeecg-boot
 * @Date:   2020-09-04
 * @Version: V1.0
 */
@Service
public class MarketingGiftBagRecordBatchDeliveryServiceImpl extends ServiceImpl<MarketingGiftBagRecordBatchDeliveryMapper, MarketingGiftBagRecordBatchDelivery> implements IMarketingGiftBagRecordBatchDeliveryService {

    @Override
    public IPage<Map<String, Object>> findMarketingGiftBagRecordBatchDeliveryById(Page<Map<String, Object>> page, Map<String,Object> map) {
        return baseMapper.findMarketingGiftBagRecordBatchDeliveryById(page,map);
    }

    @Override
    public List<Map<String, Object>> findMarketingGiftBagRecordBatchDeliveryMap(String marketingGiftBagRecordBatchId) {
        return baseMapper.findMarketingGiftBagRecordBatchDeliveryMap(marketingGiftBagRecordBatchId);
    }
}
