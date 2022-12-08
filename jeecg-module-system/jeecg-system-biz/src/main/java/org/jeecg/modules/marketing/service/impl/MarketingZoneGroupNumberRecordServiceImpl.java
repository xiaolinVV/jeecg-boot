package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupNumberRecord;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupNumberRecordMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupTimeMapper;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupNumberRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 建团次数明细
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
@Service
public class MarketingZoneGroupNumberRecordServiceImpl extends ServiceImpl<MarketingZoneGroupNumberRecordMapper, MarketingZoneGroupNumberRecord> implements IMarketingZoneGroupNumberRecordService {
    @Autowired(required = false)
    private MarketingZoneGroupTimeMapper marketingZoneGroupTimeMapper;
    @Override
    public IPage<Map<String, Object>> findMarketingZoneGroupNumberRecordByMarketingZoneGroupTimeId(Page<Map<String, Object>> page, String marketingZoneGroupTimeId, String goAndCome) {
        return baseMapper.findMarketingZoneGroupNumberRecordByMarketingZoneGroupTimeId(page,marketingZoneGroupTimeId,goAndCome);
    }

    @Override
    @Transactional
    public boolean addMarketingZoneGroupNumberRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal groupNumber, String tradeNo) {
        MarketingZoneGroupTime marketingZoneGroupTime = marketingZoneGroupTimeMapper.selectById(marketingZoneGroupTimeId);
        if (marketingZoneGroupTime!=null){
            MarketingZoneGroupNumberRecord marketingZoneGroupNumberRecord = new MarketingZoneGroupNumberRecord()
                    .setMarketingZoneGroupTimeId(marketingZoneGroupTime.getId())
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setGoAndCome("0")
                    .setTradeType(tradeType)
                    .setGroupNumber(groupNumber)
                    .setNumberAvailable(marketingZoneGroupTime.getGroupNumber().add(groupNumber))
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo);
            boolean b = this.save(marketingZoneGroupNumberRecord);
            if (b){

                int i = marketingZoneGroupTimeMapper.updateById(marketingZoneGroupTime
                        .setSpellGroup(marketingZoneGroupTime.getGroupNumber().add(groupNumber)));

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
    public boolean subtractMarketingZoneGroupNumberRecord(String marketingZoneGroupTimeId, String tradeType, BigDecimal groupNumber, String tradeNo) {
        MarketingZoneGroupTime marketingZoneGroupTime = marketingZoneGroupTimeMapper.selectById(marketingZoneGroupTimeId);
        if (marketingZoneGroupTime!=null){
            MarketingZoneGroupNumberRecord marketingZoneGroupNumberRecord = new MarketingZoneGroupNumberRecord()
                    .setMarketingZoneGroupTimeId(marketingZoneGroupTime.getId())
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setGoAndCome("1")
                    .setTradeType(tradeType)
                    .setGroupNumber(groupNumber)
                    .setNumberAvailable(marketingZoneGroupTime.getGroupNumber().subtract(groupNumber))
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo);
            boolean b = this.save(marketingZoneGroupNumberRecord);
            if (b){

                int i = marketingZoneGroupTimeMapper.updateById(marketingZoneGroupTime
                        .setSpellGroup(marketingZoneGroupTime.getGroupNumber().subtract(groupNumber)));

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
