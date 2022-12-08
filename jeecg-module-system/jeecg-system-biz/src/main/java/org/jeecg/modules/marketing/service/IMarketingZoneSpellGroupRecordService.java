package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingZoneSpellGroupRecord;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 拼团次数明细
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
public interface IMarketingZoneSpellGroupRecordService extends IService<MarketingZoneSpellGroupRecord> {

    IPage<Map<String,Object>> findMarketingZoneGroupRecordByMarketingZoneGroupTimeId(Page<Map<String,Object>> page, String marketingZoneGroupTimeId, String goAndCome);

    /**
     * 增加
     * @param marketingZoneGroupTimeId 拼团次数id
     * @param tradeType 交易类型
     * @param spellGroup 拼团次数
     * @param tradeNo 交易单号
     * @return
     */
    boolean addMarketingZoneSpellGroupRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal spellGroup, String tradeNo);
    /**
     * 减少
     * @param marketingZoneGroupTimeId 拼团次数id
     * @param tradeType 交易类型
     * @param spellGroup 拼团次数
     * @param tradeNo 交易单号
     * @return
     */
    boolean subtractMarketingZoneSpellGroupRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal spellGroup, String tradeNo);
}
