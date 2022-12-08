package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.dto.MarketingThirdIntegralRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingThirdIntegralRecord;
import org.jeecg.modules.marketing.mapper.MarketingThirdIntegralRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingThirdIntegralRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 第三积分记录
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
@Service
public class MarketingThirdIntegralRecordServiceImpl extends ServiceImpl<MarketingThirdIntegralRecordMapper, MarketingThirdIntegralRecord> implements IMarketingThirdIntegralRecordService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MarketingThirdIntegralRecordDTO marketingThirdIntegralRecordDTO) {
        return baseMapper.queryPageList(page,marketingThirdIntegralRecordDTO);
    }
}
