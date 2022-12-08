package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingFreeAdvertising;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单广告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface MarketingFreeAdvertisingMapper extends BaseMapper<MarketingFreeAdvertising> {

    /**
     * 首页广告查询
     *
     * @return
     */
    public List<Map<String,Object>> selectMarketingFreeAdvertisingIndex();

}
