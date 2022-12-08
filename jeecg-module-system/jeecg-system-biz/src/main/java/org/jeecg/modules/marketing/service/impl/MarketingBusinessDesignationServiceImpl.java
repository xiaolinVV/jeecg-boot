package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.entity.MarketingBusinessDesignation;
import org.jeecg.modules.marketing.mapper.MarketingBusinessDesignationMapper;
import org.jeecg.modules.marketing.service.IMarketingBusinessDesignationService;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftRecordService;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberBusinessDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;

/**
 * @Description: 创业团队称号管理
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Service
@Log
public class MarketingBusinessDesignationServiceImpl extends ServiceImpl<MarketingBusinessDesignationMapper, MarketingBusinessDesignation> implements IMarketingBusinessDesignationService {

    @Autowired
    private IMemberBusinessDesignationService iMemberBusinessDesignationService;


    @Autowired
    private IMarketingBusinessGiftRecordService iMarketingBusinessGiftRecordService;

    @Autowired
    private IMemberListService iMemberListService;

    @Override
    @Transactional
    public void upgrade(String memberId,String tMemberId) {
        //判断会员是否有推广人
        long memberBusinessDesignationCount=iMemberBusinessDesignationService.count(new LambdaQueryWrapper<MemberBusinessDesignation>().eq(MemberBusinessDesignation::getMemberListId,memberId));
        if(memberBusinessDesignationCount==0){
            MemberBusinessDesignation memberBusinessDesignation=new MemberBusinessDesignation();
            memberBusinessDesignation.setMemberListId(memberId);
            memberBusinessDesignation.setTMemberId(tMemberId);
            MarketingBusinessDesignation marketingBusinessDesignation=this.getOne(new LambdaQueryWrapper<MarketingBusinessDesignation>()
                    .eq(MarketingBusinessDesignation::getIsDefault,"1")
                    .eq(MarketingBusinessDesignation::getStatus,"1")
                    .orderByDesc(MarketingBusinessDesignation::getGrade)
                    .last("limit 1"));
            if(marketingBusinessDesignation!=null){
                memberBusinessDesignation.setMarketingBusinessDesignationId(marketingBusinessDesignation.getId());
            }
            iMemberBusinessDesignationService.save(memberBusinessDesignation);

            //新增会员升级
            int i = 0;
            MemberList proMemberList =iMemberListService.getById(tMemberId);
            while (true){
                if(proMemberList==null){
                    break;
                }
                MemberBusinessDesignation memberBusinessDesignationPro=iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>().eq(MemberBusinessDesignation::getMemberListId,proMemberList.getId()).orderByDesc(MemberBusinessDesignation::getTotalTeam).last("limit 1"));
                if(memberBusinessDesignationPro==null){
                    break;
                }
                if(i==0){
                    memberBusinessDesignationPro.setPushingNumber(memberBusinessDesignationPro.getPushingNumber().add(new BigDecimal(1)));
                }
                memberBusinessDesignationPro.setTotalTeam(memberBusinessDesignationPro.getTotalTeam().add(new BigDecimal(1)));

                //会员本身的称号
                MarketingBusinessDesignation marketingBusinessDesignationB=this.getById(memberBusinessDesignationPro.getMarketingBusinessDesignationId());

                MarketingBusinessDesignation marketingBusinessDesignationPro=this.getOne(new LambdaQueryWrapper<MarketingBusinessDesignation>()
                        .eq(MarketingBusinessDesignation::getGetWay,"2")
                        .eq(MarketingBusinessDesignation::getStatus,"1")
                        .gt(MarketingBusinessDesignation::getGrade,marketingBusinessDesignationB.getGrade())
                        .orderByAsc(MarketingBusinessDesignation::getGrade)
                        .last("limit 1"));
                if (marketingBusinessDesignationPro != null) {
                    //验证直推个数是否达标
                    long count=iMemberBusinessDesignationService.count(new LambdaQueryWrapper<MemberBusinessDesignation>().eq(MemberBusinessDesignation::getTMemberId,proMemberList.getId()).eq(MemberBusinessDesignation::getMarketingBusinessDesignationId,marketingBusinessDesignationPro.getMarketingBusinessDesignationId()));
                    log.info("查询直推数量："+count);
                    log.info(JSON.toJSONString(marketingBusinessDesignationPro));
                    if(count>=marketingBusinessDesignationPro.getPushingNumber().intValue()){
                        memberBusinessDesignationPro.setMarketingBusinessDesignationId(marketingBusinessDesignationPro.getId());
                        memberBusinessDesignationPro.setAmountShare(new BigDecimal(0));
                        memberBusinessDesignationPro.setAfterShots(new BigDecimal(1));
                    }
                }
                if(!iMemberBusinessDesignationService.saveOrUpdate(memberBusinessDesignationPro)){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                log.info("创业团队升级信息："+ JSON.toJSONString(memberBusinessDesignationPro));
                if(StringUtils.isBlank(memberBusinessDesignationPro.getTMemberId())){
                    break;
                }
                proMemberList=iMemberListService.getById(memberBusinessDesignationPro.getTMemberId());
                if(proMemberList==null){
                    break;
                }
                i++;
            }
        }
        //会员自够满升级
        //获取会员当前等级
        MemberBusinessDesignation memberBusinessDesignation=iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                .eq(MemberBusinessDesignation::getMemberListId,memberId)
                .last("limit 1"));
        if(memberBusinessDesignation!=null){
            MarketingBusinessDesignation marketingBusinessDesignation=this.getById(memberBusinessDesignation.getMarketingBusinessDesignationId());
            //获取当前可以升级的称号
            double marketingBusinessGiftRecordSum=iMarketingBusinessGiftRecordService.getSumByMemberId(memberId);
            MarketingBusinessDesignation marketingBusinessDesignationPro=this.getOne(new LambdaQueryWrapper<MarketingBusinessDesignation>()
                    .eq(MarketingBusinessDesignation::getGetWay,"1")
                    .eq(MarketingBusinessDesignation::getStatus,"1")
                    .le(MarketingBusinessDesignation::getMoney,marketingBusinessGiftRecordSum)
                    .orderByDesc(MarketingBusinessDesignation::getGrade)
                    .last("limit 1"));
            if(marketingBusinessDesignationPro!=null){
                if(marketingBusinessDesignation.getGrade().intValue()<marketingBusinessDesignationPro.getGrade().intValue()){
                    memberBusinessDesignation.setMarketingBusinessDesignationId(marketingBusinessDesignationPro.getId());
                    if(!iMemberBusinessDesignationService.saveOrUpdate(memberBusinessDesignation)){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }

            //根据业绩完成次数晋升
            MarketingBusinessDesignation marketingBusinessDesignationTwo=this.getOne(new LambdaQueryWrapper<MarketingBusinessDesignation>()
                    .eq(MarketingBusinessDesignation::getGetWay,"3")
                    .eq(MarketingBusinessDesignation::getStatus,"1")
                    .le(MarketingBusinessDesignation::getCompletionTimes,memberBusinessDesignation.getCompletionTimes())
                    .orderByDesc(MarketingBusinessDesignation::getGrade)
                    .last("limit 1"));

            if(marketingBusinessDesignationTwo!=null){
                if(marketingBusinessDesignation.getGrade().intValue()<marketingBusinessDesignationTwo.getGrade().intValue()){
                    memberBusinessDesignation.setMarketingBusinessDesignationId(marketingBusinessDesignationTwo.getId());
                    if(!iMemberBusinessDesignationService.saveOrUpdate(memberBusinessDesignation)){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }

        }

    }
}
