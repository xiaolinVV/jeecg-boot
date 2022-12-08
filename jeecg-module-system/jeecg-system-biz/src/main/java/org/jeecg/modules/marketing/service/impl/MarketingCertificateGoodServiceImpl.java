package org.jeecg.modules.marketing.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingCertificateGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateGood;
import org.jeecg.modules.marketing.mapper.MarketingCertificateGoodMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券商品映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Service
public class MarketingCertificateGoodServiceImpl extends ServiceImpl<MarketingCertificateGoodMapper, MarketingCertificateGood> implements IMarketingCertificateGoodService {
    @Autowired(required = false)
    private MarketingCertificateGoodMapper marketingCertificateGoodMapper;
    @Override
    public List<MarketingCertificateGoodDTO> findGoodByCertificateId(String marketingCertificateId) {
        return marketingCertificateGoodMapper.findGoodByCertificateId(marketingCertificateId);
    }

    @Override
    public List<Map<String, Object>> findMarketingCertificateSeckillListGoodByCertificateId(String id) {
        return baseMapper.findMarketingCertificateSeckillListGoodByCertificateId(id);
    }

    @Override
    public List<Map<String, Object>> findMarketingCertificateSeckillPageListGoodByCertificateId(Page<Map<String, Object>> page, String id) {
        return baseMapper.findMarketingCertificateSeckillPageListGoodByCertificateId(page,id);
    }
}
