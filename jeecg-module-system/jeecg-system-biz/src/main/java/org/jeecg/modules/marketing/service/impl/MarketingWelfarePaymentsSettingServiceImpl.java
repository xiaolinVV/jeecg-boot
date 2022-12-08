package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;
import org.jeecg.modules.marketing.mapper.MarketingWelfarePaymentsSettingMapper;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Description: 福利金设置
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingWelfarePaymentsSettingServiceImpl extends ServiceImpl<MarketingWelfarePaymentsSettingMapper, MarketingWelfarePaymentsSetting> implements IMarketingWelfarePaymentsSettingService {

    @Override
    public BigDecimal getIntegralValue() {
        MarketingWelfarePaymentsSetting marketingWelfarePaymentsSetting=this.getOne(new LambdaQueryWrapper<MarketingWelfarePaymentsSetting>()
                .eq(MarketingWelfarePaymentsSetting::getStatus,"1"));
        if(marketingWelfarePaymentsSetting==null){
            return new BigDecimal(1);
        }
        return marketingWelfarePaymentsSetting.getIntegralValue();
    }

    @Override
    public boolean updateIntegralValue(BigDecimal integralValue) {
        MarketingWelfarePaymentsSetting marketingWelfarePaymentsSetting=this.getOne(new LambdaQueryWrapper<MarketingWelfarePaymentsSetting>()
                .eq(MarketingWelfarePaymentsSetting::getStatus,"1"));
        if(marketingWelfarePaymentsSetting==null){
            return false;
        }
        marketingWelfarePaymentsSetting.setIntegralValue(integralValue);
        this.saveOrUpdate(marketingWelfarePaymentsSetting);
        return true;
    }
}
