package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingLeagueBuyerRecordMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.entity.OrderList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟专区-购买记录
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
@Service
public class MarketingLeagueBuyerRecordServiceImpl extends ServiceImpl<MarketingLeagueBuyerRecordMapper, MarketingLeagueBuyerRecord> implements IMarketingLeagueBuyerRecordService {

    @Autowired
    private IMarketingLeagueTypeService iMarketingLeagueTypeService;

    @Autowired
    private IMarketingLeagueGoodListService iMarketingLeagueGoodListService;

    @Autowired
    private IMarketingLeagueMemberService iMarketingLeagueMemberService;

    @Autowired
    private IMarketingLeagueIdentityService iMarketingLeagueIdentityService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingLeagueSettingService iMarketingLeagueSettingService;

    @Autowired
    private IMarketingLeagueAwardService iMarketingLeagueAwardService;

    @Override
    @Transactional
    @Async
    public void allocation(OrderList orderList) {

        long count=this.count(new LambdaQueryWrapper<MarketingLeagueBuyerRecord>().eq(MarketingLeagueBuyerRecord::getOrderListId,orderList.getId()));
        if(count>0){
            return;
        }
        MarketingLeagueSetting marketingLeagueSetting=iMarketingLeagueSettingService.getMarketingLeagueSetting();
        if(marketingLeagueSetting==null){
            return;
        }
        MarketingLeagueGoodList marketingLeagueGoodList=iMarketingLeagueGoodListService.getById(orderList.getActiveId());
        if(marketingLeagueGoodList==null){
            return;
        }
        MarketingLeagueType marketingLeagueType=iMarketingLeagueTypeService.getById(marketingLeagueGoodList.getMarketingLeagueTypeId());
        if(marketingLeagueType==null){
            return;
        }

        MarketingLeagueBuyerRecord marketingLeagueBuyerRecord=new MarketingLeagueBuyerRecord();
        marketingLeagueBuyerRecord.setMemberListId(orderList.getMemberListId());
        marketingLeagueBuyerRecord.setOrderListId(orderList.getId());
        marketingLeagueBuyerRecord.setMarketingLeagueTypeId(marketingLeagueType.getId());
        if(this.save(marketingLeagueBuyerRecord)){

            //分配资金
            MarketingLeagueMember marketingLeagueMember=iMarketingLeagueMemberService.getOne(new LambdaQueryWrapper<MarketingLeagueMember>()
                    .eq(MarketingLeagueMember::getMemberListId,orderList.getMemberListId())
                    .eq(MarketingLeagueMember::getAdditionalIdentity,"0"));
            //下单晋升
            MarketingLeagueIdentity marketingLeagueIdentity=iMarketingLeagueIdentityService.getById(marketingLeagueMember.getMarketingLeagueIdentityId());
            if(marketingLeagueIdentity!=null){
                if(marketingLeagueIdentity.getGetWay().equals("0")){//测试记得修改。正式给为：0
                    //非复购
                    MarketingLeagueIdentity marketingLeagueIdentityPro=iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>()
                            .eq(MarketingLeagueIdentity::getGetWay,"1")
                            .orderByDesc(MarketingLeagueIdentity::getCreateTime)
                            .last("limit 1"));
                    if(marketingLeagueIdentityPro!=null){
                        marketingLeagueMember.setMoney(marketingLeagueType.getPrice());
                        marketingLeagueMember.setMarketingLeagueIdentityId(marketingLeagueIdentityPro.getId());
                        marketingLeagueMember.setSrcParantId(marketingLeagueMember.getParantId());
                        iMarketingLeagueMemberService.saveOrUpdate(marketingLeagueMember);
                    }

                    if(StringUtils.isNotBlank(marketingLeagueMember.getParantId())) {
                        //直推奖励
                        BigDecimal pushStraightReward=marketingLeagueType.getPushStraightReward();
                        BigDecimal managerReward=marketingLeagueType.getManagerReward();
                        BigDecimal storeManagerReward=managerReward.multiply(marketingLeagueType.getStoreManagerReward()).divide(new BigDecimal(100),2, RoundingMode.DOWN);;
                        BigDecimal storeManagerSupportAward=managerReward.multiply(marketingLeagueType.getStoreManagerSupportAward()).divide(new BigDecimal(100),2, RoundingMode.DOWN);;
                        boolean isManagerReward=true;
                        int i = 0;
                        MarketingLeagueMember proMarketingLeagueMember = iMarketingLeagueMemberService.getOne(new LambdaQueryWrapper<MarketingLeagueMember>()
                                .eq(MarketingLeagueMember::getMemberListId,marketingLeagueMember.getParantId())
                                .eq(MarketingLeagueMember::getAdditionalIdentity,"0"));
                        while (true) {
                            MarketingLeagueIdentity marketingLeagueIdentityPlus=iMarketingLeagueIdentityService.getById(proMarketingLeagueMember.getMarketingLeagueIdentityId());

                            //店长奖励
                            if(isManagerReward&&marketingLeagueIdentityPlus.getManagerReward().equals("1")){
                                iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), proMarketingLeagueMember.getMemberListId(), marketingLeagueIdentityPlus.getId(), managerReward, "3",marketingLeagueType.getId());
                                isManagerReward=false;
                            }

                            //直推奖励
                            if(i==0&&marketingLeagueIdentityPlus.getPushStraightReward().equals("1")){
                                iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), proMarketingLeagueMember.getMemberListId(), marketingLeagueIdentityPlus.getId(), pushStraightReward, "2",marketingLeagueType.getId());
                                if(iMarketingLeagueIdentityService.getById(proMarketingLeagueMember.getMarketingLeagueIdentityId()).getGetWay().equals("1")) {
                                    //升级关系是否升级为店长
                                    List<MarketingLeagueMember> marketingLeagueMembers = iMarketingLeagueMemberService.list(new LambdaQueryWrapper<MarketingLeagueMember>()
                                            .eq(MarketingLeagueMember::getParantId, proMarketingLeagueMember.getMemberListId())
                                            .eq(MarketingLeagueMember::getAdditionalIdentity, "0"));
                                    if (marketingLeagueMembers.size() >= 3) {
                                        List<MarketingLeagueMember> marketingLeagueMemberLists= Lists.newArrayList();
                                        boolean isHeight = false;
                                        for (MarketingLeagueMember m : marketingLeagueMembers) {
                                            if (!iMarketingLeagueIdentityService.getById(m.getMarketingLeagueIdentityId()).getGetWay().equals("0")) {
                                                marketingLeagueMemberLists.add(m);
                                            }
                                        }
                                        if(marketingLeagueMemberLists.size()>=3){
                                            isHeight = true;
                                        }
                                        if (isHeight) {
                                            MarketingLeagueMember marketingLeagueMember1=new MarketingLeagueMember();
                                            marketingLeagueMember1.setId(proMarketingLeagueMember.getId());
                                            String paramID=proMarketingLeagueMember.getParantId();
                                            marketingLeagueMember1.setParantId(proMarketingLeagueMember.getSrcParantId());
                                            marketingLeagueMember1.setMarketingLeagueIdentityId(iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>().eq(MarketingLeagueIdentity::getGetWay,"2")).getId());
                                            if(!iMarketingLeagueMemberService.updateById(marketingLeagueMember1)){
                                                //手动强制回滚事务，这里一定要第一时间处理
                                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                            }
                                            if(StringUtils.isNotBlank(paramID)) {
                                                for (MarketingLeagueMember m : marketingLeagueMemberLists) {
                                                    m.setParantId(paramID);
                                                    if (!iMarketingLeagueMemberService.saveOrUpdate(m)) {
                                                        //手动强制回滚事务，这里一定要第一时间处理
                                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            proMarketingLeagueMember=iMarketingLeagueMemberService.getOne(new LambdaQueryWrapper<MarketingLeagueMember>()
                                    .eq(MarketingLeagueMember::getMemberListId,proMarketingLeagueMember.getParantId())
                                    .eq(MarketingLeagueMember::getAdditionalIdentity,"0"));

                            if (proMarketingLeagueMember == null) {
                                break;
                            }

                            MarketingLeagueIdentity marketingLeagueIdentityAreas=iMarketingLeagueIdentityService.getById(proMarketingLeagueMember.getMarketingLeagueIdentityId());

                            //店长扶植奖励
                            if(!isManagerReward&&marketingLeagueIdentityAreas.getStoreManagerSupportAward().equals("1")){
                                boolean isstoreManagerSupportAward=true;
                                if(proMarketingLeagueMember.getStoreManagerSupportAward().equals("0")){
                                    List<MarketingLeagueMember> marketingLeagueMembers=iMarketingLeagueMemberService.list(new LambdaQueryWrapper<MarketingLeagueMember>()
                                            .eq(MarketingLeagueMember::getSrcParantId,proMarketingLeagueMember.getMemberListId())
                                            .eq(MarketingLeagueMember::getAdditionalIdentity,"0")
                                            .orderByAsc(MarketingLeagueMember::getCreateTime)
                                            .last("limit 0,3"));
                                    if(marketingLeagueMembers.size()<3){
                                        isstoreManagerSupportAward=false;
                                    }else{
                                        for (MarketingLeagueMember m:marketingLeagueMembers) {
                                            if(!iMarketingLeagueIdentityService.getById(m.getMarketingLeagueIdentityId()).getGetWay().equals("2")){
                                                isstoreManagerSupportAward=false;
                                            }
                                        }
                                    }
                                    if(isstoreManagerSupportAward){
                                        proMarketingLeagueMember.setStoreManagerSupportAward("1");
                                        if(!iMarketingLeagueMemberService.saveOrUpdate(proMarketingLeagueMember)){
                                            //手动强制回滚事务，这里一定要第一时间处理
                                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                        }
                                    }
                                }
                                if(isstoreManagerSupportAward) {
                                    iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), proMarketingLeagueMember.getMemberListId(), marketingLeagueIdentityAreas.getId(), storeManagerSupportAward, "5",marketingLeagueType.getId());
                                }
                            }

                            //店长管理奖励
                            if(!isManagerReward&&marketingLeagueIdentityAreas.getStoreManagerReward().equals("1")){
                                iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), proMarketingLeagueMember.getMemberListId(), marketingLeagueIdentityAreas.getId(), storeManagerReward, "4",marketingLeagueType.getId());
                                break;
                            }
                            i++;
                        }
                    }

                }else{
                    //复购
                    MemberList memberList=iMemberListService.getById(orderList.getMemberListId());
                    if(memberList.getPromoterType().equals("1")) {
                        MemberList proMemberList = iMemberListService.getById(memberList.getPromoter());
                        //复购直推奖励
                        BigDecimal afterPushStraightReward=marketingLeagueType.getPrice().multiply(marketingLeagueType.getAfterPushStraightReward()).divide(new BigDecimal(100),2, RoundingMode.DOWN);
                        MarketingLeagueMember marketingLeagueMemberOne=iMarketingLeagueMemberService.getOne(new LambdaQueryWrapper<MarketingLeagueMember>()
                                .eq(MarketingLeagueMember::getMemberListId,proMemberList.getId())
                                .eq(MarketingLeagueMember::getAdditionalIdentity,"0"));
                        if(!iMarketingLeagueIdentityService.getById(marketingLeagueMemberOne.getMarketingLeagueIdentityId()).getGetWay().equals("0")) {
                            iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), proMemberList.getId(), marketingLeagueMemberOne.getMarketingLeagueIdentityId(), afterPushStraightReward, "0",marketingLeagueType.getId());
                        }
                        if(proMemberList.getPromoterType().equals("1")) {
                            MemberList oneMemberList = iMemberListService.getById(proMemberList.getPromoter());
                            //复购间推奖励
                            BigDecimal betweenPush=marketingLeagueType.getPrice().multiply(marketingLeagueType.getBetweenPush()).divide(new BigDecimal(100),2, RoundingMode.DOWN);
                            MarketingLeagueMember marketingLeagueMemberTwo=iMarketingLeagueMemberService.getOne(new LambdaQueryWrapper<MarketingLeagueMember>()
                                    .eq(MarketingLeagueMember::getMemberListId,oneMemberList.getId())
                                    .eq(MarketingLeagueMember::getAdditionalIdentity,"0"));
                            if(!iMarketingLeagueIdentityService.getById(marketingLeagueMemberTwo.getMarketingLeagueIdentityId()).getGetWay().equals("0")) {
                                iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), oneMemberList.getId(), marketingLeagueMemberTwo.getMarketingLeagueIdentityId(), betweenPush, "1",marketingLeagueType.getId());
                            }
                        }
                    }
                }
            }

            //超级合伙人奖励
            BigDecimal superPartnerAward=marketingLeagueType.getPrice().multiply(marketingLeagueType.getSuperPartnerAward()).divide(new BigDecimal(100),2, RoundingMode.DOWN);
            MarketingLeagueIdentity marketingLeagueIdentity1=iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>().eq(MarketingLeagueIdentity::getGetWay,"4"));
            if(marketingLeagueIdentity1!=null) {
                iMarketingLeagueMemberService.list(new LambdaQueryWrapper<MarketingLeagueMember>()
                        .eq(MarketingLeagueMember::getMarketingLeagueIdentityId,marketingLeagueIdentity1.getId())).forEach(m->{
                    iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), m.getMemberListId(), marketingLeagueIdentity1.getId(), superPartnerAward.multiply(m.getRewardRatio()).setScale(2,RoundingMode.DOWN), "6",marketingLeagueType.getId());
                });
            }

            //城市服务商资金分配

            BigDecimal cityServiceProvider=marketingLeagueType.getCityServiceProvider();

            if(cityServiceProvider.doubleValue()>0&&StringUtils.isNotBlank(orderList.getSysAreaIds())) {
                MarketingLeagueIdentity marketingLeagueIdentity2=iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>().eq(MarketingLeagueIdentity::getGetWay,"5"));

                List<MarketingLeagueMember> marketingLeagueMembers =iMarketingLeagueMemberService.list(new LambdaQueryWrapper<MarketingLeagueMember>()
                        .eq(MarketingLeagueMember::getMarketingLeagueIdentityId,marketingLeagueIdentity2.getId())
                        .eq(MarketingLeagueMember::getSysAreas,orderList.getSysAreaIds()));
                if(marketingLeagueMembers.size()>0){
                    BigDecimal serviceProvider=cityServiceProvider.divide(new BigDecimal(marketingLeagueMembers.size()),2,RoundingMode.DOWN);
                    marketingLeagueMembers.forEach(m->{
                        iMarketingLeagueAwardService.add(marketingLeagueBuyerRecord.getId(), m.getMemberListId(), marketingLeagueIdentity2.getId(), serviceProvider, "7",marketingLeagueType.getId());
                    });
                }
            }

            //分配奖励
            iMarketingLeagueAwardService.award(marketingLeagueBuyerRecord.getId());
        }else{
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

    }

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.queryPageList(page,paramMap);
    }
}
