package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingRecommendCertificateDTO;
import org.jeecg.modules.marketing.entity.MarketingRecommendCertificate;
import org.jeecg.modules.marketing.vo.MarketingRecommendCertificateVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 推荐兑换券
 * @Author: jeecg-boot
 * @Date:   2020-05-07
 * @Version: V1.0
 */
public interface IMarketingRecommendCertificateService extends IService<MarketingRecommendCertificate> {

    IPage<MarketingRecommendCertificateVO> queryPageList(Page<MarketingRecommendCertificate> page, MarketingRecommendCertificateDTO marketingRecommendCertificateDTO);

    List<Map<String,Object>> findMarketingRecommendCertificateList();
}
