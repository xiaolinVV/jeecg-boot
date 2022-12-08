package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingFreeAdvertising;
import org.jeecg.modules.marketing.mapper.MarketingFreeAdvertisingMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeAdvertisingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 免单广告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
@Log
public class MarketingFreeAdvertisingServiceImpl extends ServiceImpl<MarketingFreeAdvertisingMapper, MarketingFreeAdvertising> implements IMarketingFreeAdvertisingService {


    @Override
    public List<Map<String, Object>> selectMarketingFreeAdvertisingIndex() {
        return baseMapper.selectMarketingFreeAdvertisingIndex();
    }

    @Override
    @Transactional
    public void stopMarketingFreeAdvertising() {
        this.list(new LambdaQueryWrapper<MarketingFreeAdvertising>()
                .eq(MarketingFreeAdvertising::getStatus,"1")
                .le(MarketingFreeAdvertising::getEndTime,new Date())
                .last("limit 10")).forEach(mfa->{
                    log.info("免单专区广告定时器停用："+mfa.getAdvertisingTitle());
                    mfa.setStatus("0");
                    this.saveOrUpdate(mfa);
        });
    }
}
