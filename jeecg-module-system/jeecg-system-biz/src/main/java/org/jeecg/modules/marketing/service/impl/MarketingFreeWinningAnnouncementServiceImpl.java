package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingFreeWinningAnnouncement;
import org.jeecg.modules.marketing.mapper.MarketingFreeWinningAnnouncementMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeWinningAnnouncementService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单中奖公告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
public class MarketingFreeWinningAnnouncementServiceImpl extends ServiceImpl<MarketingFreeWinningAnnouncementMapper, MarketingFreeWinningAnnouncement> implements IMarketingFreeWinningAnnouncementService {

    @Override
    public List<Map<String, Object>> getMarketingFreeWinningAnnouncementIndex() {
        return baseMapper.getMarketingFreeWinningAnnouncementIndex();
    }

    @Override
    public IPage<Map<String, Object>> selectMarketingFreeWinningAnnouncementList(Page<Map<String, Object>> page, Map<String, String> paramMap) {
        return baseMapper.selectMarketingFreeWinningAnnouncementList(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingFreeWinningAnnouncementList(Page<Map<String, Object>> page) {
        return baseMapper.getMarketingFreeWinningAnnouncementList(page);
    }
}
