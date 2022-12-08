package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.marketing.dto.MarketingCertificateStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateStore;
import org.jeecg.modules.marketing.mapper.MarketingCertificateStoreMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券门店映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingCertificateStoreServiceImpl extends
        ServiceImpl<MarketingCertificateStoreMapper, MarketingCertificateStore>
        implements IMarketingCertificateStoreService {
    @Autowired(required = false)
    private MarketingCertificateStoreMapper marketingCertificateStoreMapper;
    @Override
    public List<MarketingCertificateStoreDTO> findStoreByCertificateId(String marketingCertificateId) {
        return marketingCertificateStoreMapper.findStoreByCertificateId(marketingCertificateId);
    }

    @Override
    public IPage<Map<String, Object>> findstoreById(Page<Map<String, Object>> page, Map<String,Object> map) {
        return baseMapper.findstoreById(page,map);
    }

    @Override
    public List<Map<String, Object>> findMarketingCertificateSeckillListStoreByCertificateId(String id) {
        return baseMapper.findMarketingCertificateSeckillListStoreByCertificateId(id);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingCertificateSeckillPageListStoreByCertificateId(Page<Map<String, Object>> page, String id, String latitude, String longitude) {
        return baseMapper.findMarketingCertificateSeckillPageListStoreByCertificateId(page,id,latitude,longitude);
    }

}
