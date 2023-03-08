package org.jeecg.modules.marketing.store.prefecture.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureDividend;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;

/**
 * @Description: 店铺专区-记录分红明细
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface IMarketingStorePrefectureDividendService extends IService<MarketingStorePrefectureDividend> {

    /**
     * 奖励分配
     *
     * @param payOrderCarLogId
     * @param marketingStorePrefectureList
     * @param marketingStorePrefectureRecordId
     */
    public void paymentIncentives(String payOrderCarLogId, MarketingStorePrefectureList marketingStorePrefectureList, String marketingStorePrefectureRecordId);

}
