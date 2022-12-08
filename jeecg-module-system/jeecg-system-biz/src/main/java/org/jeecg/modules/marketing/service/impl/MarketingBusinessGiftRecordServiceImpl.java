package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftRecord;
import org.jeecg.modules.marketing.mapper.MarketingBusinessGiftRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftRecordService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 创业礼包购买记录
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Service
public class MarketingBusinessGiftRecordServiceImpl extends ServiceImpl<MarketingBusinessGiftRecordMapper, MarketingBusinessGiftRecord> implements IMarketingBusinessGiftRecordService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingBusinessGiftRecord> queryWrapper, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,queryWrapper,requestMap);
    }

    @Override
    public Map<String, Object> getGiftRecordDetails(String id) {
        return baseMapper.getGiftRecordDetails(id);
    }

    @Override
    public double getSumByMemberId(String memberId) {
        return baseMapper.getSumByMemberId(memberId);
    }
}
