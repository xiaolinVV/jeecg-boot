package org.jeecg.modules.marketing.store.giftbag.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingRecordedMoney;
import org.jeecg.modules.marketing.service.IMarketingRecordedMoneyService;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagDividend;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeam;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeamMember;
import org.jeecg.modules.marketing.store.giftbag.mapper.MarketingStoreGiftbagDividendMapper;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagDividendService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagTeamMemberService;
import org.jeecg.modules.member.entity.MemberDesignation;
import org.jeecg.modules.member.service.IMemberDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.pay.entity.PayMarketingStoreGiftbagLog;
import org.jeecg.modules.pay.service.IPayMarketingStoreGiftbagLogService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @Description: 礼包团-记录分红明细
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
@Service
@Log
public class MarketingStoreGiftbagDividendServiceImpl extends ServiceImpl<MarketingStoreGiftbagDividendMapper, MarketingStoreGiftbagDividend> implements IMarketingStoreGiftbagDividendService {
    @Autowired
    private IPayMarketingStoreGiftbagLogService iPayMarketingStoreGiftbagLogService;
    @Autowired
    private IMarketingStoreGiftbagListService iMarketingStoreGiftbagListService;
    @Autowired
    private IMarketingStoreGiftbagTeamMemberService iMarketingStoreGiftbagTeamMemberService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private IMemberDesignationService iMemberDesignationService;
    @Autowired
    private IMarketingRecordedMoneyService iMarketingRecordedMoneyService;
    @Override
    @Transactional
    public void paymentIncentives(String payLogId, MarketingStoreGiftbagTeam marketingStoreGiftbagTeam,String marketingStoreGiftbagRecordId) {
        PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog = iPayMarketingStoreGiftbagLogService.getById(payLogId);
        MarketingStoreGiftbagList marketingStoreGiftbagList = iMarketingStoreGiftbagListService.getById(payMarketingStoreGiftbagLog.getMarketingStoreGiftbagListId());
        List<MarketingStoreGiftbagDividend> marketingStoreGiftbagDividends = new ArrayList<>();
        BigDecimal usingBonuses = marketingStoreGiftbagList.getUsingBonuses();
        if (usingBonuses.doubleValue() > 0) {
            BigDecimal shareBonus = usingBonuses.multiply(marketingStoreGiftbagList.getShareBonus()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
            if (shareBonus.doubleValue() > 0) {
                MarketingStoreGiftbagDividend marketingStoreGiftbagDividend = new MarketingStoreGiftbagDividend();
                marketingStoreGiftbagDividend.setMarketingStoreGiftbagRecordId(marketingStoreGiftbagRecordId);
                marketingStoreGiftbagDividend.setRoleName("本店分红");
                marketingStoreGiftbagDividend.setPayType("0");
                marketingStoreGiftbagDividend.setShareBonus(shareBonus);
                marketingStoreGiftbagDividend.setStoreManageId(marketingStoreGiftbagList.getStoreManageId());
                this.save(marketingStoreGiftbagDividend);
                marketingStoreGiftbagDividends.add(marketingStoreGiftbagDividend);
            }

            if (StringUtils.isNotBlank(payMarketingStoreGiftbagLog.getTMemberId())) {
                BigDecimal referralBonuses = usingBonuses.multiply(marketingStoreGiftbagList.getReferralBonuses()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                if (referralBonuses.doubleValue() > 0) {
                    MarketingStoreGiftbagDividend marketingStoreGiftbagDividend = new MarketingStoreGiftbagDividend();
                    marketingStoreGiftbagDividend.setMarketingStoreGiftbagRecordId(marketingStoreGiftbagRecordId);
                    marketingStoreGiftbagDividend.setMemebrListId(payMarketingStoreGiftbagLog.getTMemberId());
                    marketingStoreGiftbagDividend.setRoleName("推荐奖励");
                    marketingStoreGiftbagDividend.setPayType("1");
                    marketingStoreGiftbagDividend.setShareBonus(referralBonuses);
                    this.save(marketingStoreGiftbagDividend);
                    marketingStoreGiftbagDividends.add(marketingStoreGiftbagDividend);
                }
            }

            if (marketingStoreGiftbagTeam.getStatus().equals("1")) {
                MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember = iMarketingStoreGiftbagTeamMemberService.getOne(new LambdaQueryWrapper<MarketingStoreGiftbagTeamMember>()
                        .eq(MarketingStoreGiftbagTeamMember::getMarketingStoreGiftbagTeamId, marketingStoreGiftbagTeam.getId())
                        .eq(MarketingStoreGiftbagTeamMember::getIdentity, 1));
                BigDecimal headReward = marketingStoreGiftbagList.getHeadReward();
                if (headReward.doubleValue() > 0) {
                    MarketingStoreGiftbagDividend marketingStoreGiftbagDividend = new MarketingStoreGiftbagDividend();
                    marketingStoreGiftbagDividend.setMarketingStoreGiftbagRecordId(marketingStoreGiftbagRecordId);
                    marketingStoreGiftbagDividend.setMemebrListId(marketingStoreGiftbagTeamMember.getMemberListId());
                    marketingStoreGiftbagDividend.setRoleName("团长奖励");
                    marketingStoreGiftbagDividend.setPayType("2");
                    marketingStoreGiftbagDividend.setShareBonus(headReward);
                    this.save(marketingStoreGiftbagDividend);
                    marketingStoreGiftbagDividends.add(marketingStoreGiftbagDividend);
                }
            }

            if (usingBonuses.doubleValue() > 0) {
                JSONArray jsonArray = JSON.parseArray(marketingStoreGiftbagList.getTitleReward().toString());
                jsonArray.forEach((ob) -> {
                    JSONObject jsonObject = (JSONObject)ob;
                    BigDecimal titleReward = jsonObject.getBigDecimal("rewards").multiply(usingBonuses).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                    if (titleReward.doubleValue() > 0) {
                        MarketingStoreGiftbagDividend marketingStoreGiftbagDividend = new MarketingStoreGiftbagDividend();
                        marketingStoreGiftbagDividend.setMarketingStoreGiftbagRecordId(marketingStoreGiftbagRecordId);
                        marketingStoreGiftbagDividend.setMemberDesignationId(jsonObject.getString("id"));
                        marketingStoreGiftbagDividend.setRoleName(jsonObject.getString("name"));
                        marketingStoreGiftbagDividend.setPayType("3");
                        marketingStoreGiftbagDividend.setShareBonus(titleReward);
                        this.save(marketingStoreGiftbagDividend);
                        marketingStoreGiftbagDividends.add(marketingStoreGiftbagDividend);
                    }

                });
            }

            marketingStoreGiftbagDividends.forEach((m) -> {
                if (m.getPayType().equals("0")) {
                    if (!this.iStoreManageService.addStoreBlance(m.getStoreManageId(), m.getShareBonus(), m.getId(), "17")) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                } else if (m.getPayType().equals("3")) {
                    MemberDesignation memberDesignation = iMemberDesignationService.getById(m.getMemberDesignationId());
                    this.iMemberDesignationService.saveOrUpdate(memberDesignation.setBalance(memberDesignation.getBalance().add(m.getShareBonus())).setRecordedMoney(memberDesignation.getRecordedMoney().add(m.getShareBonus())));
                    MarketingRecordedMoney marketingRecordedMoney = (new MarketingRecordedMoney()).setMemberDesignationId(memberDesignation.getId()).setTradeNo(m.getId()).setTradeType("1").setParticipation(new BigDecimal(100)).setParticipationMoney(m.getShareBonus()).setRecordedMoney(m.getShareBonus()).setBalance(memberDesignation.getBalance()).setTradeTime(new Date()).setRemark("礼包团分红[" + m.getId() + "]").setOrderNo(OrderNoUtils.getOrderNo());
                    this.iMarketingRecordedMoneyService.save(marketingRecordedMoney);
                    log.info("称号资金：" + memberDesignation.getName() + "入账资金:" + m.getShareBonus());
                } else {
                    String tradeType = "";
                    if (m.getPayType().equals("1")) {
                        tradeType = "54";
                    }

                    if (m.getPayType().equals("2")) {
                        tradeType = "53";
                    }

                    if (!this.iMemberListService.addBlance(m.getMemebrListId(), m.getShareBonus(), m.getId(), tradeType)) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }

            });
        }

    }
}
