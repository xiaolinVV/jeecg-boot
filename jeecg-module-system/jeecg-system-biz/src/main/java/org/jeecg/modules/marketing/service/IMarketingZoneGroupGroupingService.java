package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGrouping;

import java.util.Map;

/**
 * @Description: 专区团分组
 * @Author: jeecg-boot
 * @Date:   2021-07-31
 * @Version: V1.0
 */
public interface IMarketingZoneGroupGroupingService extends IService<MarketingZoneGroupGrouping> {

    IPage<Map<String,Object>> findMarketingZoneGroupGroupingByMemberId(Page<Map<String,Object>> page, String memberId);
}
