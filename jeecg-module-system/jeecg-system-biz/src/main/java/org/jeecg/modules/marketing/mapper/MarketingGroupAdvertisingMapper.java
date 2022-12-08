package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingGroupAdvertising;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
public interface MarketingGroupAdvertisingMapper extends BaseMapper<MarketingGroupAdvertising> {

    /**
     * 首页广告模块
     *
     * 张靠勤    2021-3-31
     *
     * @return
     */
    public List<Map<String,Object>> selectMarketingGroupAdvertisingIndex();

}
