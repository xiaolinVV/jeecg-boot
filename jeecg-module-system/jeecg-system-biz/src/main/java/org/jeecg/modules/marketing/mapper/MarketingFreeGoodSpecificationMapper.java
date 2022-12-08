package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodSpecification;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单商品规格
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface MarketingFreeGoodSpecificationMapper extends BaseMapper<MarketingFreeGoodSpecification> {

    /**
     * 根据免单商品id查询规格数据
     *
     * @param marketingFreeGoodListId
     * @return
     */
    public List<Map<String,Object>> selectMarketingFreeGoodSpecificationByMarketingFreeGoodListId(@Param("marketingFreeGoodListId") String marketingFreeGoodListId);


    /**
     * 获取活动价格最低的规格数据
     *
     * @param marketingFreeGoodListId
     * @return
     */
    public Map<String,Object> selectMarketingFreeGoodSpecificationBySmallprice(@Param("marketingFreeGoodListId") String marketingFreeGoodListId);
}
