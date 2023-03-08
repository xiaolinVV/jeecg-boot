package org.jeecg.modules.marketing.store.giftbag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeam;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeamMember;
import org.jeecg.modules.marketing.store.giftbag.mapper.MarketingStoreGiftbagTeamMapper;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagTeamMemberService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagTeamService;
import org.jeecg.modules.pay.entity.PayMarketingStoreGiftbagLog;
import org.jeecg.modules.pay.service.IPayMarketingStoreGiftbagLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 礼包团列表
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
@Service
public class MarketingStoreGiftbagTeamServiceImpl extends ServiceImpl<MarketingStoreGiftbagTeamMapper, MarketingStoreGiftbagTeam> implements IMarketingStoreGiftbagTeamService {

    @Autowired
    private IPayMarketingStoreGiftbagLogService iPayMarketingStoreGiftbagLogService;


    @Autowired
    private IMarketingStoreGiftbagTeamMemberService iMarketingStoreGiftbagTeamMemberService;


    @Autowired
    private IMarketingStoreGiftbagListService iMarketingStoreGiftbagListService;


    @Override
    @Transactional
    public MarketingStoreGiftbagTeam createGiftbagTeam(String payLogId) {
        PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog=iPayMarketingStoreGiftbagLogService.getById(payLogId);
        MarketingStoreGiftbagList marketingStoreGiftbagList=iMarketingStoreGiftbagListService.getById(payMarketingStoreGiftbagLog.getMarketingStoreGiftbagListId());

        MarketingStoreGiftbagTeam marketingStoreGiftbagTeam=null;
        //有团队的
        if(StringUtils.isNotBlank(payMarketingStoreGiftbagLog.getMarketingStoreGiftbagTeamId())){
            MarketingStoreGiftbagTeam marketingStoreGiftbagTeam1=this.getById(payMarketingStoreGiftbagLog.getMarketingStoreGiftbagTeamId());
            if(marketingStoreGiftbagTeam1.getMembers().intValue()<7){
                marketingStoreGiftbagTeam=marketingStoreGiftbagTeam1;
            }
        }
        //有推广人的
        if(marketingStoreGiftbagTeam==null&&StringUtils.isNotBlank(payMarketingStoreGiftbagLog.getTMemberId())){
                Map<String,Object> tidMap=this.getMarketingStoreGiftbagTeamByMemberId(payMarketingStoreGiftbagLog.getTMemberId(),marketingStoreGiftbagList.getStoreManageId());
                if(tidMap!=null){
                    marketingStoreGiftbagTeam=this.getById(tidMap.get("id").toString());
                }
        }
         //自身有团队的
        if(marketingStoreGiftbagTeam==null){
            Map<String,Object> tidMap=this.getMarketingStoreGiftbagTeamByMemberId(payMarketingStoreGiftbagLog.getMemberListId(),marketingStoreGiftbagList.getStoreManageId());
            if(tidMap!=null){
                marketingStoreGiftbagTeam=this.getById(tidMap.get("id").toString());
            }
        }

        //获取最老的一个进行中的团队
        if(marketingStoreGiftbagTeam==null){
            MarketingStoreGiftbagTeam marketingStoreGiftbagTeam1=this.getOne(new LambdaQueryWrapper<MarketingStoreGiftbagTeam>()
                    .eq(MarketingStoreGiftbagTeam::getStatus,"0")
                    .eq(MarketingStoreGiftbagTeam::getStoreManageId,marketingStoreGiftbagList.getStoreManageId())
                    .orderByAsc(MarketingStoreGiftbagTeam::getCreateTime).last("limit 1"));
            if(marketingStoreGiftbagTeam1!=null){
                marketingStoreGiftbagTeam=marketingStoreGiftbagTeam1;
            }
        }

        //自建团队
        if(marketingStoreGiftbagTeam==null){
            marketingStoreGiftbagTeam=new MarketingStoreGiftbagTeam();
            marketingStoreGiftbagTeam.setMembers(new BigDecimal(0));
            marketingStoreGiftbagTeam.setStatus("0");
            marketingStoreGiftbagTeam.setStoreManageId(marketingStoreGiftbagList.getStoreManageId());
            this.save(marketingStoreGiftbagTeam);
        }

        //创建团队记录
        MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember=new MarketingStoreGiftbagTeamMember();
        marketingStoreGiftbagTeamMember.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeam.getId());
        marketingStoreGiftbagTeamMember.setMemberListId(payMarketingStoreGiftbagLog.getMemberListId());
        marketingStoreGiftbagTeamMember.setTMemberId(payMarketingStoreGiftbagLog.getTMemberId());
        //判断身份
        if(marketingStoreGiftbagTeam.getMembers().intValue()==0){
            marketingStoreGiftbagTeamMember.setIdentity(new BigDecimal(1));
        }

        if(marketingStoreGiftbagTeam.getMembers().intValue()>0&&marketingStoreGiftbagTeam.getMembers().intValue()<=2){
            marketingStoreGiftbagTeamMember.setIdentity(new BigDecimal(2));
        }

        if(marketingStoreGiftbagTeam.getMembers().intValue()>2){
            marketingStoreGiftbagTeamMember.setIdentity(new BigDecimal(3));
        }

        iMarketingStoreGiftbagTeamMemberService.save(marketingStoreGiftbagTeamMember);

        marketingStoreGiftbagTeam.setMembers(marketingStoreGiftbagTeam.getMembers().add(new BigDecimal(1)));
        if(marketingStoreGiftbagTeam.getMembers().intValue()>=7){
            marketingStoreGiftbagTeam.setStatus("1");

            //拆分团队
            //第一副团长
            List<MarketingStoreGiftbagTeamMember> marketingStoreGiftbagTeamMembers2=iMarketingStoreGiftbagTeamMemberService.list(new LambdaQueryWrapper<MarketingStoreGiftbagTeamMember>()
                    .eq(MarketingStoreGiftbagTeamMember::getMarketingStoreGiftbagTeamId,marketingStoreGiftbagTeam.getId())
                    .eq(MarketingStoreGiftbagTeamMember::getIdentity,2)
                    .orderByAsc(MarketingStoreGiftbagTeamMember::getCreateTime));
            //第二副团长
            List<MarketingStoreGiftbagTeamMember> marketingStoreGiftbagTeamMembers3=iMarketingStoreGiftbagTeamMemberService.list(new LambdaQueryWrapper<MarketingStoreGiftbagTeamMember>()
                    .eq(MarketingStoreGiftbagTeamMember::getMarketingStoreGiftbagTeamId,marketingStoreGiftbagTeam.getId())
                    .eq(MarketingStoreGiftbagTeamMember::getIdentity,3).orderByAsc(MarketingStoreGiftbagTeamMember::getCreateTime));

            MarketingStoreGiftbagTeam marketingStoreGiftbagTeamNewOne=new MarketingStoreGiftbagTeam();
            marketingStoreGiftbagTeamNewOne.setStoreManageId(marketingStoreGiftbagTeam.getStoreManageId());
            marketingStoreGiftbagTeamNewOne.setMembers(new BigDecimal(3));
            this.save(marketingStoreGiftbagTeamNewOne);

            MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember1=marketingStoreGiftbagTeamMembers2.get(0);
            marketingStoreGiftbagTeamMember1.setId(null);
            marketingStoreGiftbagTeamMember1.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeamNewOne.getId());
            marketingStoreGiftbagTeamMember1.setIdentity(new BigDecimal(1));
            iMarketingStoreGiftbagTeamMemberService.save(marketingStoreGiftbagTeamMember1);

            MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember3=marketingStoreGiftbagTeamMembers3.get(0);
            marketingStoreGiftbagTeamMember3.setId(null);
            marketingStoreGiftbagTeamMember3.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeamNewOne.getId());
            marketingStoreGiftbagTeamMember3.setIdentity(new BigDecimal(2));
            iMarketingStoreGiftbagTeamMemberService.save(marketingStoreGiftbagTeamMember3);

            MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember4=marketingStoreGiftbagTeamMembers3.get(1);
            marketingStoreGiftbagTeamMember4.setId(null);
            marketingStoreGiftbagTeamMember4.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeamNewOne.getId());
            marketingStoreGiftbagTeamMember4.setIdentity(new BigDecimal(2));
            iMarketingStoreGiftbagTeamMemberService.save(marketingStoreGiftbagTeamMember4);

            MarketingStoreGiftbagTeam marketingStoreGiftbagTeamNewTwo=new MarketingStoreGiftbagTeam();
            marketingStoreGiftbagTeamNewTwo.setMembers(new BigDecimal(3));
            marketingStoreGiftbagTeamNewTwo.setStoreManageId(marketingStoreGiftbagTeam.getStoreManageId());
            this.save(marketingStoreGiftbagTeamNewTwo);
            MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember2= marketingStoreGiftbagTeamMembers2.get(1);
            marketingStoreGiftbagTeamMember2.setId(null);
            marketingStoreGiftbagTeamMember2.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeamNewTwo.getId());
            marketingStoreGiftbagTeamMember2.setIdentity(new BigDecimal(1));
            iMarketingStoreGiftbagTeamMemberService.save(marketingStoreGiftbagTeamMember2);


            MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember5=marketingStoreGiftbagTeamMembers3.get(2);
            marketingStoreGiftbagTeamMember5.setId(null);
            marketingStoreGiftbagTeamMember5.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeamNewTwo.getId());
            marketingStoreGiftbagTeamMember5.setIdentity(new BigDecimal(2));
            iMarketingStoreGiftbagTeamMemberService.save(marketingStoreGiftbagTeamMember5);

            MarketingStoreGiftbagTeamMember marketingStoreGiftbagTeamMember6=marketingStoreGiftbagTeamMembers3.get(3);
            marketingStoreGiftbagTeamMember6.setId(null);
            marketingStoreGiftbagTeamMember6.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeamNewTwo.getId());
            marketingStoreGiftbagTeamMember6.setIdentity(new BigDecimal(2));
            iMarketingStoreGiftbagTeamMemberService.save(marketingStoreGiftbagTeamMember6);

        }
        if(!this.saveOrUpdate(marketingStoreGiftbagTeam)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return marketingStoreGiftbagTeam;
    }

    @Override
    public Map<String, Object> getMarketingStoreGiftbagTeamByMemberId(String memberId,String storeManageId) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("storeManageId",storeManageId);
        return baseMapper.getMarketingStoreGiftbagTeamByMemberId(paramMap);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingStoreGiftbagTeamList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getMarketingStoreGiftbagTeamList(page,paramMap);
    }
}
