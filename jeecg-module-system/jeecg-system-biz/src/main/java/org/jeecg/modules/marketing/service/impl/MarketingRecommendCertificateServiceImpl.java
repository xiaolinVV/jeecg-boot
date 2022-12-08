package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingRecommendCertificateDTO;
import org.jeecg.modules.marketing.entity.MarketingRecommendCertificate;
import org.jeecg.modules.marketing.mapper.MarketingRecommendCertificateMapper;
import org.jeecg.modules.marketing.service.IMarketingRecommendCertificateService;
import org.jeecg.modules.marketing.vo.MarketingRecommendCertificateVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 推荐兑换券
 * @Author: jeecg-boot
 * @Date:   2020-05-07
 * @Version: V1.0
 */
@Service
public class MarketingRecommendCertificateServiceImpl extends ServiceImpl<MarketingRecommendCertificateMapper, MarketingRecommendCertificate> implements IMarketingRecommendCertificateService {

    @Override
    public IPage<MarketingRecommendCertificateVO> queryPageList(Page<MarketingRecommendCertificate> page, MarketingRecommendCertificateDTO marketingRecommendCertificateDTO) {
        return baseMapper.queryPageList(page,marketingRecommendCertificateDTO);
    }

    @Override
    public List<Map<String, Object>> findMarketingRecommendCertificateList() {
        return baseMapper.findMarketingRecommendCertificateList();
    }
}
