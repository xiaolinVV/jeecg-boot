package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingCertificateStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateStore;

import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券门店映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface IMarketingCertificateStoreService extends IService<MarketingCertificateStore> {

    List<MarketingCertificateStoreDTO> findStoreByCertificateId(String marketingCertificateId);

    IPage<Map<String,Object>> findstoreById(Page<Map<String,Object>> page, Map<String,Object> map);

    List<Map<String,Object>> findMarketingCertificateSeckillListStoreByCertificateId(String id);

    IPage<Map<String,Object>> findMarketingCertificateSeckillPageListStoreByCertificateId(Page<Map<String,Object>> page, String id, String latitude, String longitude);

}
