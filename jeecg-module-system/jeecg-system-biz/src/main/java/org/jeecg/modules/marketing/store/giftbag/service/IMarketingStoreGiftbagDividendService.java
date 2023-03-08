package org.jeecg.modules.marketing.store.giftbag.service;

import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagDividend;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeam;

/**
 * @Description: 礼包团-记录分红明细
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
public interface IMarketingStoreGiftbagDividendService extends IService<MarketingStoreGiftbagDividend> {

    /**
     * 发放奖励
     *
     * @param payLogId
     * @param marketingStoreGiftbagTeam
     */
    public void paymentIncentives(String payLogId, MarketingStoreGiftbagTeam marketingStoreGiftbagTeam,String marketingStoreGiftbagRecordId);

}
