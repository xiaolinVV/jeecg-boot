package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;

import java.util.Map;

/**
 * @Description: 拼团次数
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
public interface IMarketingZoneGroupTimeService extends IService<MarketingZoneGroupTime> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingZoneGroupTime> queryWrapper, Map<String, Object> requestMap);
}
