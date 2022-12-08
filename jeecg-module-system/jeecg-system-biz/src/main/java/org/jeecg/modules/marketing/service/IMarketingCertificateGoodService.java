package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingCertificateGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateGood;

import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券商品映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface IMarketingCertificateGoodService extends IService<MarketingCertificateGood> {

    List<MarketingCertificateGoodDTO> findGoodByCertificateId(String marketingCertificateId);

    List<Map<String,Object>> findMarketingCertificateSeckillListGoodByCertificateId(String id);

    List<Map<String,Object>> findMarketingCertificateSeckillPageListGoodByCertificateId(Page<Map<String,Object>> page, String id);
}
