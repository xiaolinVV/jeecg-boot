package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodType;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单商品类型
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface MarketingFreeGoodTypeMapper extends BaseMapper<MarketingFreeGoodType> {

    /**
     * 获取可以使用的所有活动商品类型
     *
     * @return
     */
    public List<Map<String,Object>> getAllMarketingFreeGoodType();

}
