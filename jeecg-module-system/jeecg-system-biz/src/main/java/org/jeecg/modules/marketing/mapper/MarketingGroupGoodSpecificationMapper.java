package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodSpecification;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
public interface MarketingGroupGoodSpecificationMapper extends BaseMapper<MarketingGroupGoodSpecification> {


    /**
     * 根据免单商品id查询规格数据
     * 张靠勤   2021-3-30
     * @param marketingGroupGoodListId
     * @return
     */
    public List<Map<String,Object>> selectMarketingGroupGoodSpecificationByMarketingGroupGoodListId(@Param("marketingGroupGoodListId") String marketingGroupGoodListId);

}
