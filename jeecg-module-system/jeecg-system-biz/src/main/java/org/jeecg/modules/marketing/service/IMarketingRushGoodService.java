package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingRushGood;

import java.util.Map;

/**
 * @Description: 抢购活动-分类商品
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface IMarketingRushGoodService extends IService<MarketingRushGood> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingRushGood> queryWrapper, Map<String, Object> requestMap);



    /**
     * 根据抢购分类获取抢购商品
     *
     * @param page
     * @param marketingRushTypeId
     * @return
     */
    public IPage<Map<String,Object>> getMarketingRushGoodByTypeId(Page<Map<String,Object>> page,String marketingRushTypeId);
}
