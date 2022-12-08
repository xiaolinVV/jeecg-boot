package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingActivityListRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingActivityListRecord;
import org.jeecg.modules.marketing.mapper.MarketingActivityListRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingActivityListRecordService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 活动记录
 * @Author: jeecg-boot
 * @Date:   2021-05-17
 * @Version: V1.0
 */
@Service
public class MarketingActivityListRecordServiceImpl extends ServiceImpl<MarketingActivityListRecordMapper, MarketingActivityListRecord> implements IMarketingActivityListRecordService {

    @Override
    public IPage<MarketingActivityListRecord> getMarketingActivityListRecordList(Page<MarketingActivityListRecord> page, MarketingActivityListRecordDTO marketingActivityListRecordDTO) {
        return baseMapper.getMarketingActivityListRecordList(page,marketingActivityListRecordDTO);
    }

    @Override
    public IPage<Map<String, Object>> getmarketingActivityListRecordByMemberId(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.getmarketingActivityListRecordByMemberId(page,memberId);
    }
}
