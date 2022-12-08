package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;
import org.jeecg.modules.marketing.entity.MarketingZoneSpellGroupRecord;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupTimeMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneSpellGroupRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingZoneSpellGroupRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 拼团次数明细
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
@Service
public class MarketingZoneSpellGroupRecordServiceImpl extends ServiceImpl<MarketingZoneSpellGroupRecordMapper, MarketingZoneSpellGroupRecord> implements IMarketingZoneSpellGroupRecordService {
    @Autowired(required = false)
    private MarketingZoneGroupTimeMapper marketingZoneGroupTimeMapper;
    @Override
    public IPage<Map<String, Object>> findMarketingZoneGroupRecordByMarketingZoneGroupTimeId(Page<Map<String, Object>> page, String marketingZoneGroupTimeId, String goAndCome) {
        return baseMapper.findMarketingZoneGroupRecordByMarketingZoneGroupTimeId(page,marketingZoneGroupTimeId,goAndCome);
    }

    @Override
    @Transactional
    public boolean addMarketingZoneSpellGroupRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal spellGroup, String tradeNo) {
        MarketingZoneGroupTime marketingZoneGroupTime = marketingZoneGroupTimeMapper.selectById(marketingZoneGroupTimeId);
        if (marketingZoneGroupTime!=null){
            MarketingZoneSpellGroupRecord marketingZoneSpellGroupRecord = new MarketingZoneSpellGroupRecord()
                    .setMarketingZoneGroupTimeId(marketingZoneGroupTime.getId())
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setGoAndCome("0")
                    .setTradeType(tradeType)
                    .setSpellGroup(spellGroup)
                    .setNumberAvailable(marketingZoneGroupTime.getSpellGroup().add(spellGroup))
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo);
            boolean b = this.save(marketingZoneSpellGroupRecord);
            if (b){

                int i = marketingZoneGroupTimeMapper.updateById(marketingZoneGroupTime
                        .setSpellGroup(marketingZoneGroupTime.getSpellGroup().add(spellGroup)));

                if (i==1){
                    return true;
                }else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return false;
                }
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }

        }else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean subtractMarketingZoneSpellGroupRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal spellGroup, String tradeNo) {
        MarketingZoneGroupTime marketingZoneGroupTime = marketingZoneGroupTimeMapper.selectById(marketingZoneGroupTimeId);
        if (marketingZoneGroupTime!=null){
            MarketingZoneSpellGroupRecord marketingZoneSpellGroupRecord = new MarketingZoneSpellGroupRecord()
                    .setMarketingZoneGroupTimeId(marketingZoneGroupTime.getId())
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setGoAndCome("1")
                    .setTradeType(tradeType)
                    .setSpellGroup(spellGroup)
                    .setNumberAvailable(marketingZoneGroupTime.getSpellGroup().subtract(spellGroup))
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo);
            boolean b = this.save(marketingZoneSpellGroupRecord);
            if (b){

                int i = marketingZoneGroupTimeMapper.updateById(marketingZoneGroupTime
                        .setSpellGroup(marketingZoneGroupTime.getSpellGroup().subtract(spellGroup)));

                if (i==1){
                    return true;
                }else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return false;
                }
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }

        }else {
            return false;
        }
    }

}
