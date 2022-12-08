package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
public interface MarketingPrefectureGoodSpecificationMapper extends BaseMapper<MarketingPrefectureGoodSpecification> {
   List<MarketingPrefectureGoodSpecificationDTO> getMarketingPrefectureGoodSpecification(@Param("marketingPrefectureGoodId") String marketingPrefectureGoodId);

   /**
    * 获取专区价格最低的规格数据
    *
    * @param marketingPrefectureGoodId
    * @return
    */
   public Map<String,Object> selectMarketingPrefectureGoodSpecificationBySmallprice(@Param("marketingPrefectureGoodId") String marketingPrefectureGoodId);
}
