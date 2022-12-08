package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingMaterialGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialGood;

import java.util.List;
import java.util.Map;

/**
 * @Description: 素材商品关系表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
public interface MarketingMaterialGoodMapper extends BaseMapper<MarketingMaterialGood> {

   List<MarketingMaterialGoodDTO> getMarketingMaterialGoodDTO(@Param("marketingMaterialListId") String marketingMaterialListId);
   List<Map<String,Object>> getMarketingMaterialGoodMap(@Param("marketingMaterialListId") String marketingMaterialListId);
}
