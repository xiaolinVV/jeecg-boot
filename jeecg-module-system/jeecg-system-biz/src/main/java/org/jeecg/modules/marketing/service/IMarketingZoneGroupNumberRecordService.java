package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupNumberRecord;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 建团次数明细
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
public interface IMarketingZoneGroupNumberRecordService extends IService<MarketingZoneGroupNumberRecord> {

    IPage<Map<String,Object>> findMarketingZoneGroupNumberRecordByMarketingZoneGroupTimeId(Page<Map<String,Object>> page, String marketingZoneGroupTimeId, String goAndCome);

    /**
     * 增加
     * @param marketingZoneGroupTimeId 拼团次数id
     * @param tradeType 交易类型
     * @param groupNumber 建团次数
     * @param tradeNo 交易单号
     * @return
     */
    boolean addMarketingZoneGroupNumberRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal groupNumber, String tradeNo);
    /**
     * 减少
     * @param marketingZoneGroupTimeId 拼团次数id
     * @param tradeType 交易类型
     * @param groupNumber 建团次数
     * @param tradeNo 交易单号
     * @return
     */
    boolean subtractMarketingZoneGroupNumberRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal groupNumber, String tradeNo);
}
