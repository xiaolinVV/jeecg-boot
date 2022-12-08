package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingLeagueAward;
import org.jeecg.modules.marketing.mapper.MarketingLeagueAwardMapper;
import org.jeecg.modules.marketing.service.IMarketingLeagueAwardService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟专区-资金分配明细
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
@Service
public class MarketingLeagueAwardServiceImpl extends ServiceImpl<MarketingLeagueAwardMapper, MarketingLeagueAward> implements IMarketingLeagueAwardService {

    @Autowired
    private IMemberListService iMemberListService;

    @Override
    public boolean add(String marketingLeagueBuyerRecordId, String memberListId, String marketingLeagueIdentityId, BigDecimal award, String rewardType,String marketingLeagueTypeId) {
        if(award.doubleValue()==0){
            return false;
        }
        return this.save(new MarketingLeagueAward()
                .setMarketingLeagueBuyerRecordId(marketingLeagueBuyerRecordId)
                .setMemberListId(memberListId)
                .setMarketingLeagueIdentityId(marketingLeagueIdentityId)
                .setAward(award)
                .setMarketingLeagueTypeId(marketingLeagueTypeId)
                .setRewardType(rewardType));
    }

    @Override
    @Transactional
    public void award(String marketingLeagueBuyerRecordId) {
        this.list(new LambdaQueryWrapper<MarketingLeagueAward>()
                .eq(MarketingLeagueAward::getMarketingLeagueBuyerRecordId,marketingLeagueBuyerRecordId)
                .eq(MarketingLeagueAward::getIssue,"0")).forEach(m->{
            String tradeType="";
            if(m.getRewardType().equals("0")){
                tradeType="46";
            }
            if(m.getRewardType().equals("1")){
                tradeType="47";
            }
            if(m.getRewardType().equals("2")){
                tradeType="39";
            }
            if(m.getRewardType().equals("3")){
                tradeType="40";
            }
            if(m.getRewardType().equals("4")){
                tradeType="41";
            }
            if(m.getRewardType().equals("5")){
                tradeType="43";
            }
            if(m.getRewardType().equals("6")){
                tradeType="45";
            }
            if(m.getRewardType().equals("7")){
                tradeType="44";
            }
            m.setIssue("1");
            if(this.saveOrUpdate(m)){
                if(!iMemberListService.addBlance(m.getMemberListId(),m.getAward(),m.getId(),tradeType)){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }else{
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        });
    }

    @Override
    public Double cumulativeRewards(String memberId) {
        return baseMapper.cumulativeRewards(memberId);
    }

    @Override
    public List<Map<String, Object>> getMarketingLeagueAwardBymarketingLeagueTypeId(String memberId) {
        return baseMapper.getMarketingLeagueAwardBymarketingLeagueTypeId(memberId);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingLeagueAwardList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getMarketingLeagueAwardList(page,paramMap);
    }

    @Override
    public List<Map<String, Object>> getMarketingLeagueAwardListById(String marketingLeagueBuyerRecordId) {
        return baseMapper.getMarketingLeagueAwardListById(marketingLeagueBuyerRecordId);
    }
}
