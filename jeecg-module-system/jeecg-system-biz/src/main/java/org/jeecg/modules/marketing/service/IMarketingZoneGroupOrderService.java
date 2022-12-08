package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupOrder;

import java.util.Map;

/**
 * @Description: 拼中商品
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
public interface IMarketingZoneGroupOrderService extends IService<MarketingZoneGroupOrder> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, Map<String, Object> requestMap);

    IPage<Map<String,Object>> marketingZoneGroupOrderRecord(Page<Map<String,Object>> page, QueryWrapper<MarketingZoneGroupOrder> queryWrapper, Map<String, Object> requestMap);

    Map<String,Object> memberRecord(String id);

    Map<String,Object> goodInfo(String id);

    IPage<Map<String,Object>> getMarketingZoneGroupOrderPageByGroupingId(Page<Map<String,Object>> page, String id);

    Map<String,Object> getMarketingZoneGroupOrderDetails(MarketingZoneGroupOrder marketingZoneGroupOrder);

    /**
     * 处理寄售业务
     *
     * @param marketingZoneGroupOrderId
     */
    public void consignment(String marketingZoneGroupOrderId);
}
