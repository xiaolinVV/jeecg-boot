package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingGiftBagCertificateDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagCertificate;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagCertificateMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 礼包兑换券映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Service
public class MarketingGiftBagCertificateServiceImpl extends ServiceImpl<MarketingGiftBagCertificateMapper, MarketingGiftBagCertificate> implements IMarketingGiftBagCertificateService {
    @Autowired(required = false)
    private MarketingGiftBagCertificateMapper marketingGiftBagCertificateMapper;
    @Override
    public List<MarketingGiftBagCertificateDTO> findCertificateById(String marketingGiftBagId) {
        return marketingGiftBagCertificateMapper.findCertificateById(marketingGiftBagId);
    }
}
