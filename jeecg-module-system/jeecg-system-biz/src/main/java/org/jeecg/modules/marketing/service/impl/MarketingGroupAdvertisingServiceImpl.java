package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingGroupAdvertising;
import org.jeecg.modules.marketing.mapper.MarketingGroupAdvertisingMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupAdvertisingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
public class MarketingGroupAdvertisingServiceImpl extends ServiceImpl<MarketingGroupAdvertisingMapper, MarketingGroupAdvertising> implements IMarketingGroupAdvertisingService {

    @Override
    public List<Map<String, Object>> selectMarketingGroupAdvertisingIndex() {
        return baseMapper.selectMarketingGroupAdvertisingIndex();
    }

    @Override
    public void stopMarketingGroupAdvertising() {
        this.list(new LambdaQueryWrapper<MarketingGroupAdvertising>()
                .eq(MarketingGroupAdvertising::getStatus,"1")
                .le(MarketingGroupAdvertising::getEndTime,new Date())
                .last("limit 10")).forEach(mga->{
                    log.info("中奖专区专区广告定时器停用："+mga.getAdvertisingTitle());
                    mga.setStatus("0");
                    this.saveOrUpdate(mga);
        });
    }
}
