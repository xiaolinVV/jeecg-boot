package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingPrefectureGoodSpecificationDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;
import org.jeecg.modules.marketing.mapper.MarketingPrefectureGoodSpecificationMapper;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodSpecificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 专区商品规格
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
@Service
public class MarketingPrefectureGoodSpecificationServiceImpl extends ServiceImpl<MarketingPrefectureGoodSpecificationMapper, MarketingPrefectureGoodSpecification> implements IMarketingPrefectureGoodSpecificationService {
    /**
     * 专区规格
     * @param marketingPrefectureGoodId
     * @return
     */
    public  List<MarketingPrefectureGoodSpecificationDTO> getMarketingPrefectureGoodSpecification(String marketingPrefectureGoodId){
        return baseMapper.getMarketingPrefectureGoodSpecification(marketingPrefectureGoodId);
    }

    @Override
    public Map<String, Object> selectMarketingPrefectureGoodSpecificationBySmallprice(String marketingPrefectureGoodId) {
        return baseMapper.selectMarketingPrefectureGoodSpecificationBySmallprice(marketingPrefectureGoodId);
    }

    ;

}
