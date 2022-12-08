package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.marketing.entity.MarketingBusinessCapital;

import java.math.BigDecimal;

/**
 * @Description: 创业资金池配置
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
public interface IMarketingBusinessCapitalService extends IService<MarketingBusinessCapital>,JeecgService<MarketingBusinessCapital> {


    /**
     * 进账资金处理
     *
     * @param marketingBusinessCapitalId
     * @param price
     * @return
     */
    public boolean add(String marketingBusinessCapitalId, BigDecimal price,String tradeType,String tradeNo);

    /**
     * 每日分红
     *
     * @param marketingBusinessCapitalId
     */
    public void dailyDividend(String marketingBusinessCapitalId);

    /**
     * 冻结分红
     *
     * @param memberId
     */
    public void receiveDividends(String memberId);

}
