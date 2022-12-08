package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodList;
import org.jeecg.modules.marketing.mapper.MarketingGroupGoodListMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
@Service
@Log
public class MarketingGroupGoodListServiceImpl extends ServiceImpl<MarketingGroupGoodListMapper, MarketingGroupGoodList> implements IMarketingGroupGoodListService {

    @Override
    public IPage<Map<String, Object>> selectMarketingGroupGoodList(Page<Map<String, Object>> page, Map<String, String> paramMap) {
        return baseMapper.selectMarketingGroupGoodList(page,paramMap);
    }

    @Override
    public List<Map<String, Object>> selectMarketingGroupGoodListByIsRecommend() {
        return baseMapper.selectMarketingGroupGoodListByIsRecommend();
    }

    @Override
    public IPage<Map<String, Object>> selectMarketingGroupGoodListByMarketingGroupGoodTypeId(Page<Map<String,Object>> page,Map<String,Object> paramMap) {
        return baseMapper.selectMarketingGroupGoodListByMarketingGroupGoodTypeId(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> selectMarketingGroupGoodListBySearch(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.selectMarketingGroupGoodListBySearch(page,paramMap);
    }

    @Override
    @Transactional
    public void stopMarketingGroupGoodList() {
        this.list(new LambdaQueryWrapper<MarketingGroupGoodList>()
                .eq(MarketingGroupGoodList::getStatus,"1")
                .le(MarketingGroupGoodList::getEndTime,new Date())
                .last("limit 50")).forEach(mggl->{
                    log.info("定时器停止超时中奖拼团商品id："+mggl.getId());
                    mggl.setStatus("2");
                    this.saveOrUpdate(mggl);
        });
    }
}
