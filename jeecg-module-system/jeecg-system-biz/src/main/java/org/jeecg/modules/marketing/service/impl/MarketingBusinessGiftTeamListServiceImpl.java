package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftTeamList;
import org.jeecg.modules.marketing.mapper.MarketingBusinessGiftTeamListMapper;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftTeamListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 团队记录
 * @Author: jeecg-boot
 * @Date:   2021-10-16
 * @Version: V1.0
 */
@Service
public class MarketingBusinessGiftTeamListServiceImpl extends ServiceImpl<MarketingBusinessGiftTeamListMapper, MarketingBusinessGiftTeamList> implements IMarketingBusinessGiftTeamListService {

    @Override
    public List<Map<String, Object>> getByteamRecordId(String marketingBusinessGiftTeamRecordId) {
        return baseMapper.getByteamRecordId(marketingBusinessGiftTeamRecordId);
    }

    @Override
    public IPage<Map<String, Object>> getByMemberId(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.getByMemberId(page,memberId);
    }

    @Override
    public Map<String, Object> getRecordIdByMemberId(String memberId) {
        return baseMapper.getRecordIdByMemberId(memberId);
    }
}
