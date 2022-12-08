package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralAdvertising;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼购广告
 * @Author: jeecg-boot
 * @Date:   2021-06-27
 * @Version: V1.0
 */
public interface MarketingGroupIntegralAdvertisingMapper extends BaseMapper<MarketingGroupIntegralAdvertising> {

    /**
     * 获取拼购广告
     *
     * @return
     */
    public List<Map<String, Object>> getAdvertising();

}
