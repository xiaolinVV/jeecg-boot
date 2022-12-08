package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodList;
import org.jeecg.modules.marketing.mapper.MarketingFreeGoodListMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeGoodListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单商品
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
public class MarketingFreeGoodListServiceImpl extends ServiceImpl<MarketingFreeGoodListMapper, MarketingFreeGoodList> implements IMarketingFreeGoodListService {

    @Override
    public IPage<Map<String, Object>> selectMarketingFreeGoodList(Page<Map<String, Object>> page, Map<String, String> paramMap) {
        return baseMapper.selectMarketingFreeGoodList(page,paramMap);
    }

    @Override
    public List<Map<String, Object>> selectMarketingFreeGoodListByIsRecommend() {
        return baseMapper.selectMarketingFreeGoodListByIsRecommend();
    }

    @Override
    public IPage<Map<String, Object>> selectMarketingFreeGoodListByMarketingFreeGoodTypeId(Page<Map<String, Object>> page, Map<String,Object> paramMap) {
        return baseMapper.selectMarketingFreeGoodListByMarketingFreeGoodTypeId(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> selectMarketingFreeGoodListBySearch(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.selectMarketingFreeGoodListBySearch(page,paramMap);
    }
}
