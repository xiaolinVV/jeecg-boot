package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingPrefectureGoodSpecificationDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;

import java.util.List;
import java.util.Map;

/**
 * @Description: 专区商品规格
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
public interface IMarketingPrefectureGoodSpecificationService extends IService<MarketingPrefectureGoodSpecification> {
    /**
     * 专区规格
     * @param marketingPrefectureGoodId
     * @return
     */
    List<MarketingPrefectureGoodSpecificationDTO> getMarketingPrefectureGoodSpecification(String marketingPrefectureGoodId);


    /**
     * 获取专区价格最低的规格数据
     *
     * @param marketingPrefectureGoodId
     * @return
     */
    public Map<String,Object> selectMarketingPrefectureGoodSpecificationBySmallprice(String marketingPrefectureGoodId);
}
