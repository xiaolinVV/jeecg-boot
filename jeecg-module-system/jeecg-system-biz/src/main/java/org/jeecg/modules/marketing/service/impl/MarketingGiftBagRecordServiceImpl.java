package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordService;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordVO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 礼包记录
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Service
public class MarketingGiftBagRecordServiceImpl extends ServiceImpl<MarketingGiftBagRecordMapper, MarketingGiftBagRecord> implements IMarketingGiftBagRecordService {

    @Override
    public IPage<Map<String, Object>> getMarketingGiftBagRecordList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getMarketingGiftBagRecordList(page,paramMap);
    }

    @Override
    public Map<String, Object> findMarketingGiftBagRecordById(String id) {
        return baseMapper.findMarketingGiftBagRecordById(id);
    }

    @Override
    public IPage<MarketingGiftBagRecordVO> findGiftBagRecord(Page<MarketingGiftBagRecordVO> page, MarketingGiftBagRecordVO marketingGiftBagRecordVO) {
        return baseMapper.findGiftBagRecord(page,marketingGiftBagRecordVO);
    }
}
