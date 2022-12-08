package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingDisountGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscountGood;
import org.jeecg.modules.marketing.mapper.MarketingDiscountGoodMapper;
import org.jeecg.modules.marketing.service.IMarketingDiscountGoodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券商品映射
 * @Author: jeecg-boot
 * @Date: 2019-11-16
 * @Version: V1.0
 */
@Service
public class MarketingDiscountGoodServiceImpl extends ServiceImpl<MarketingDiscountGoodMapper, MarketingDiscountGood> implements IMarketingDiscountGoodService {

    /**
     * 平台适用商品信息
     * @param
     * @param marketingDiscountId
     * @return
     */
    @Override
    public List<MarketingDisountGoodDTO> findGood(String marketingDiscountId) {
        return baseMapper.findGood(marketingDiscountId);
    }
    /**
     * 店铺适用商品信息
     * @param
     * @param marketingDiscountId
     * @return
     */
    @Override
    public List<MarketingDisountGoodDTO> findStoreGood(String marketingDiscountId) {

        return baseMapper.findStoreGood(marketingDiscountId);
    }

    @Override
    public List<Map<String, Object>> findMarketingDiscountGoodById(String id) {
        return baseMapper.findMarketingDiscountGoodById(id);
    }

    @Override
    public List<Map<String, Object>> findMarketingDiscountStoreGoodById(String id) {
        return baseMapper.findMarketingDiscountStoreGoodById(id);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingDiscountGoodPageListById(Page<Map<String, Object>> page, String id) {
        return baseMapper.findMarketingDiscountGoodPageListById(page,id);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingDiscountStoreGoodPageListById(Page<Map<String, Object>> page, String id) {
        return baseMapper.findMarketingDiscountStoreGoodPageListById(page,id);
    }
}