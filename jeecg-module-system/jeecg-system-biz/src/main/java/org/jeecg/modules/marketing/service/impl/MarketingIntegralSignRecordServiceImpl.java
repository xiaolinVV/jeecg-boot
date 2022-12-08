package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingIntegralSignRecord;
import org.jeecg.modules.marketing.mapper.MarketingIntegralSignRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingIntegralSignRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 签到次数
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Service
public class MarketingIntegralSignRecordServiceImpl extends ServiceImpl<MarketingIntegralSignRecordMapper, MarketingIntegralSignRecord> implements IMarketingIntegralSignRecordService {

    @Override
    public Map<String, Object> getSignTime(Map<String, Object> paramMap) {
        return baseMapper.getSignTime(paramMap);
    }
}
