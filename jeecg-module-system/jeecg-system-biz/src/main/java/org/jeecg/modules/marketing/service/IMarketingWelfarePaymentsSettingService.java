package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;

import java.math.BigDecimal;

/**
 * @Description: 福利金设置
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface IMarketingWelfarePaymentsSettingService extends IService<MarketingWelfarePaymentsSetting> {

    /**
     * 获取积分价值
     *
     * @return
     */
    public BigDecimal getIntegralValue();

    /**
     * 修改积分价值
     *
     * @param integralValue
     * @return
     */
    public boolean updateIntegralValue(BigDecimal integralValue);

}
