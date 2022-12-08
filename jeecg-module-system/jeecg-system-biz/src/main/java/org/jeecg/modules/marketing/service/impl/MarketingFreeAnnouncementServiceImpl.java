package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingFreeAnnouncement;
import org.jeecg.modules.marketing.mapper.MarketingFreeAnnouncementMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeAnnouncementService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 免单公告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
public class MarketingFreeAnnouncementServiceImpl extends ServiceImpl<MarketingFreeAnnouncementMapper, MarketingFreeAnnouncement> implements IMarketingFreeAnnouncementService {

    @Override
    public IPage<Map<String, Object>> selectMarketingFreeAnnouncement(Page<Map<String, Object>> page) {
        return baseMapper.selectMarketingFreeAnnouncement(page);
    }

    @Override
    public Map<String, Object> getMarketingFreeAnnouncementById(String marketingFreeAnnouncementId) {
        return baseMapper.getMarketingFreeAnnouncementById(marketingFreeAnnouncementId);
    }

}
