package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillActivityList;
import org.jeecg.modules.marketing.mapper.MarketingCertificateSeckillActivityListMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateSeckillActivityListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 活动列表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Service
public class MarketingCertificateSeckillActivityListServiceImpl extends ServiceImpl<MarketingCertificateSeckillActivityListMapper, MarketingCertificateSeckillActivityList> implements IMarketingCertificateSeckillActivityListService {

    @Override
    public Map<String, Object> getInfo(String id) {
        return baseMapper.getInfo(id);
    }
}
