package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingGiftBagCertificateDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagCertificate;

import java.util.List;

/**
 * @Description: 礼包兑换券映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface MarketingGiftBagCertificateMapper extends BaseMapper<MarketingGiftBagCertificate> {

    List<MarketingGiftBagCertificateDTO> findCertificateById(@Param("marketingGiftBagId") String marketingGiftBagId);

}
