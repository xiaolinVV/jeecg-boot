package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingActivityList;
import org.jeecg.modules.marketing.mapper.MarketingActivityListMapper;
import org.jeecg.modules.marketing.service.IMarketingActivityListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.Map;

/**
 * @Description: 活动列表
 * @Author: jeecg-boot
 * @Date:   2021-05-17
 * @Version: V1.0
 */
@Service
@Log
public class MarketingActivityListServiceImpl extends ServiceImpl<MarketingActivityListMapper, MarketingActivityList> implements IMarketingActivityListService {

    @Override
    public IPage<MarketingActivityList> getMarketingActivityList(Page<MarketingActivityList> page) {
        return baseMapper.getMarketingActivityList(page);
    }

    @Override
    public Map<String, Object> getMarketingActivityListById(Map<String, Object> paramMap) {
        return baseMapper.getMarketingActivityListById(paramMap);
    }

    @Override
    public void updateStatus() {
        //开始报名
        this.list(new LambdaQueryWrapper<MarketingActivityList>()
                .le(MarketingActivityList::getRegistrationTime,new Date())
                .ge(MarketingActivityList::getStartTime,new Date())
                .eq(MarketingActivityList::getStatus,"1")
                .last("limit 10")).forEach(marketingActivityList->{
            log.info("活动报名开始："+marketingActivityList.getId());
            marketingActivityList.setActiveStatus("3");
            this.saveOrUpdate(marketingActivityList);
        });
        //进行中
        this.list(new LambdaQueryWrapper<MarketingActivityList>()
                .le(MarketingActivityList::getStartTime,new Date())
                .ge(MarketingActivityList::getEndTime,new Date())
                .eq(MarketingActivityList::getStatus,"1")
                .last("limit 10")).forEach(marketingActivityList->{
            log.info("活动进行中开始："+marketingActivityList.getId());
            marketingActivityList.setActiveStatus("1");
            this.saveOrUpdate(marketingActivityList);
        });
        //结束
        this.list(new LambdaQueryWrapper<MarketingActivityList>()
                .le(MarketingActivityList::getEndTime,new Date())
                .eq(MarketingActivityList::getStatus,"1")
                .last("limit 10")).forEach(marketingActivityList->{
            log.info("活动结束："+marketingActivityList.getId());
            marketingActivityList.setActiveStatus("2");
            this.saveOrUpdate(marketingActivityList);
        });
    }
}
