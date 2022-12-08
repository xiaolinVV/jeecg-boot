package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.dto.MarketingBusinessGiftTeamRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftTeamRecord;
import org.jeecg.modules.marketing.mapper.MarketingBusinessGiftTeamRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftTeamRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 团队记录
 * @Author: jeecg-boot
 * @Date:   2021-10-16
 * @Version: V1.0
 */
@Service
public class MarketingBusinessGiftTeamRecordServiceImpl extends ServiceImpl<MarketingBusinessGiftTeamRecordMapper, MarketingBusinessGiftTeamRecord> implements IMarketingBusinessGiftTeamRecordService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MarketingBusinessGiftTeamRecordDTO marketingBusinessGiftTeamRecordDTO) {
        return baseMapper.queryPageList(page,marketingBusinessGiftTeamRecordDTO);
    }
}
