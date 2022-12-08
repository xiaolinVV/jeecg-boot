package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingGiftBagCertificateDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagCertificate;

import java.util.List;

/**
 * @Description: 礼包兑换券映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface IMarketingGiftBagCertificateService extends IService<MarketingGiftBagCertificate> {

    List<MarketingGiftBagCertificateDTO> findCertificateById(String marketingGiftBagId);
}
