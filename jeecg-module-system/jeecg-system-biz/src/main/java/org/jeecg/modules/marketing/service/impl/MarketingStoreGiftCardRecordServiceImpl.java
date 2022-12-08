package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardRecord;
import org.jeecg.modules.marketing.mapper.MarketingStoreGiftCardRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 店铺礼品卡使用记录
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Service
public class MarketingStoreGiftCardRecordServiceImpl extends ServiceImpl<MarketingStoreGiftCardRecordMapper, MarketingStoreGiftCardRecord> implements IMarketingStoreGiftCardRecordService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingStoreGiftCardRecord> queryWrapper, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,queryWrapper,requestMap);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingStoreGiftCardRecordList(Page<Map<String, Object>> page, HashMap<String, Object> map) {
        return baseMapper.findMarketingStoreGiftCardRecordList(page,map);
    }

    @Override
    public Map<String, Object> findMarketingStoreGiftCardRecordParticulars(String id) {
        return baseMapper.findMarketingStoreGiftCardRecordParticulars(id);
    }
}
