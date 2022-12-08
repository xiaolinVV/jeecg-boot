package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingRushGood;

import java.util.Map;

/**
 * @Description: 抢购活动-分类商品
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface MarketingRushGoodMapper extends BaseMapper<MarketingRushGood> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingRushGood> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);


    /**
     * 根据抢购分类获取抢购商品
     *
     * @param page
     * @param marketingRushTypeId
     * @return
     */
    public IPage<Map<String,Object>> getMarketingRushGoodByTypeId(Page<Map<String,Object>> page,@Param("marketingRushTypeId") String marketingRushTypeId);

}
