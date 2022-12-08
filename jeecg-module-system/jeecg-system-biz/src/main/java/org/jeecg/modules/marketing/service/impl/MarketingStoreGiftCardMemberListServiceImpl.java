package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardMemberList;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardRecord;
import org.jeecg.modules.marketing.mapper.MarketingStoreGiftCardMemberListMapper;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardMemberListService;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺会员礼品卡
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Service
public class MarketingStoreGiftCardMemberListServiceImpl extends ServiceImpl<MarketingStoreGiftCardMemberListMapper, MarketingStoreGiftCardMemberList> implements IMarketingStoreGiftCardMemberListService {
 
    @Autowired
    private IMarketingStoreGiftCardRecordService iMarketingStoreGiftCardRecordService;

    @Override
    public IPage<Map<String, Object>> getByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getByMemberId(page,paramMap);
    }

    @Override
    public List<Map<String, Object>> getGoodTypeOne(Map<String, Object> paramMap) {
        return baseMapper.getGoodTypeOne(paramMap);
    }

    @Override
    public List<Map<String, Object>> getGoodTypeTwo(Map<String, Object> paramMap) {
        return baseMapper.getGoodTypeTwo(paramMap);
    }

    @Override
    public IPage<Map<String, Object>> getGoodList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getGoodList(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> Page, QueryWrapper<MarketingStoreGiftCardMemberList> queryWrapper, Map<String, Object> paramMap) {
        return baseMapper.queryPageList(Page,queryWrapper,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> giveList(Page<Map<String, Object>> page, QueryWrapper<MarketingStoreGiftCardMemberList> queryWrapper, Map<String, Object> paramMap) {
        return baseMapper.giveList(page,queryWrapper,paramMap);
    }

    @Override
    @Transactional
    public void subtractBlance(String marketingStoreGiftCardMemberListId, BigDecimal balance, String tradeNo,String tredeType) {
        if(balance.doubleValue()>0){
            MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=this.getById(marketingStoreGiftCardMemberListId);
            marketingStoreGiftCardMemberList.setDenomination(marketingStoreGiftCardMemberList.getDenomination().subtract(balance));
            this.saveOrUpdate(marketingStoreGiftCardMemberList);
            MarketingStoreGiftCardRecord marketingStoreGiftCardRecord=new MarketingStoreGiftCardRecord();
            marketingStoreGiftCardRecord.setMarketingStoreGiftCardMemberListId(marketingStoreGiftCardMemberListId);
            marketingStoreGiftCardRecord.setGoAndCome("1");
            marketingStoreGiftCardRecord.setAmount(balance);
            marketingStoreGiftCardRecord.setBalance(marketingStoreGiftCardMemberList.getDenomination());
            marketingStoreGiftCardRecord.setPayTime(new Date());
            marketingStoreGiftCardRecord.setTradeNo(tradeNo);
            marketingStoreGiftCardRecord.setTradeType(tredeType);
            iMarketingStoreGiftCardRecordService.save(marketingStoreGiftCardRecord);
        }
    }

    @Override
    @Transactional
    public void addBlance(String marketingStoreGiftCardMemberListId, BigDecimal balance, String tradeNo,String tredeType) {
        if(balance.doubleValue()>0){
            MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=this.getById(marketingStoreGiftCardMemberListId);
            marketingStoreGiftCardMemberList.setDenomination(marketingStoreGiftCardMemberList.getDenomination().add(balance));
            this.saveOrUpdate(marketingStoreGiftCardMemberList);
            MarketingStoreGiftCardRecord marketingStoreGiftCardRecord=new MarketingStoreGiftCardRecord();
            marketingStoreGiftCardRecord.setMarketingStoreGiftCardMemberListId(marketingStoreGiftCardMemberListId);
            marketingStoreGiftCardRecord.setGoAndCome("0");
            marketingStoreGiftCardRecord.setAmount(balance);
            marketingStoreGiftCardRecord.setBalance(marketingStoreGiftCardMemberList.getDenomination());
            marketingStoreGiftCardRecord.setPayTime(new Date());
            marketingStoreGiftCardRecord.setTradeNo(tradeNo);
            marketingStoreGiftCardRecord.setTradeType(tredeType);
            iMarketingStoreGiftCardRecordService.save(marketingStoreGiftCardRecord);
        }
    }
}
