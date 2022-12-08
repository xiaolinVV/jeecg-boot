package org.jeecg.modules.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.dto.MemberDistributionLevelDTO;
import org.jeecg.modules.member.entity.MemberDistributionLevel;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.mapper.MemberDistributionLevelMapper;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员分销等级关系
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
@Service
@Log
public class MemberDistributionLevelServiceImpl extends ServiceImpl<MemberDistributionLevelMapper, MemberDistributionLevel> implements IMemberDistributionLevelService {


    @Autowired
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;

    @Autowired
    private IMarketingDistributionLevelService iMarketingDistributionLevelService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    @Lazy
    private IMarketingGroupIntegralManageRecordService iMarketingGroupIntegralManageRecordService;

    @Autowired
    @Lazy
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;

    @Autowired
    private IMemberMarketingDistributionLevelService iMemberMarketingDistributionLevelService;


    @Override
    @Transactional
    public void upgrade(String memberId) {
        //查询会员分销记录信息
        MemberDistributionLevel memberDistributionLevel=this.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,memberId).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
        if(memberDistributionLevel==null){
            memberDistributionLevel=new MemberDistributionLevel();
            memberDistributionLevel.setMemberListId(memberId);
            MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                    .eq(MarketingDistributionLevel::getStatus,"1")
                    .eq(MarketingDistributionLevel::getWaysObtain,"0")
                    .orderByAsc(MarketingDistributionLevel::getGrade)
                    .last("limit 1"));
            if(marketingDistributionLevel!=null){
                memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
            }
            this.save(memberDistributionLevel);
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getPromoterType().equals("1")) {
            int i = 0;
            MemberList proMemberList =iMemberListService.getById(memberList.getPromoter());
            while (true){
                if(proMemberList==null){
                    break;
                }
                MemberDistributionLevel memberDistributionLevelpro=this.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,proMemberList.getId()).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
                if(memberDistributionLevelpro==null){
                    memberDistributionLevelpro=new MemberDistributionLevel();
                    memberDistributionLevelpro.setMemberListId(proMemberList.getId());
                    MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                            .eq(MarketingDistributionLevel::getStatus,"1")
                            .eq(MarketingDistributionLevel::getWaysObtain,"0")
                            .orderByDesc(MarketingDistributionLevel::getGrade)
                            .last("limit 1"));
                    if(marketingDistributionLevel!=null){
                        memberDistributionLevelpro.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                    }
                    this.save(memberDistributionLevelpro);
                }
                if(i==0){
                    memberDistributionLevelpro.setDirect(memberDistributionLevelpro.getDirect().add(new BigDecimal(1)));
                }
                memberDistributionLevelpro.setTeamNumber(memberDistributionLevelpro.getTeamNumber().add(new BigDecimal(1)));
                this.saveOrUpdate(memberDistributionLevelpro);
                proMemberList=iMemberListService.getById(proMemberList.getPromoter());
                if(proMemberList==null){
                    break;
                }
                i++;
            }
        }
    }



    @Override
    @Transactional
    public void teamUpgrade(String memberId) {
        //查询会员分销记录信息
        MemberDistributionLevel memberDistributionLevel=this.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,memberId).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
        if(memberDistributionLevel==null){
            memberDistributionLevel=new MemberDistributionLevel();
            memberDistributionLevel.setMemberListId(memberId);
            MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                    .eq(MarketingDistributionLevel::getStatus,"1")
                    .eq(MarketingDistributionLevel::getWaysObtain,"1")
                    .orderByAsc(MarketingDistributionLevel::getGrade)
                    .last("limit 1"));
            if(marketingDistributionLevel!=null){
                memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
            }
            if(!this.save(memberDistributionLevel)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else{
            MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                    .eq(MarketingDistributionLevel::getStatus,"1")
                    .eq(MarketingDistributionLevel::getWaysObtain,"1")
                    .orderByAsc(MarketingDistributionLevel::getGrade)
                    .last("limit 1"));
            if(marketingDistributionLevel!=null){
                if(StringUtils.isBlank(memberDistributionLevel.getMarketingDistributionLevelId())){
                    memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                }else {
                    if (Integer.parseInt(iMarketingDistributionLevelService.getById(memberDistributionLevel.getMarketingDistributionLevelId()).getGrade()) < Integer.parseInt(marketingDistributionLevel.getGrade())) {
                        memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                    }
                }

            }
            if(!this.saveOrUpdate(memberDistributionLevel)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getPromoterType().equals("1")) {
            int i = 0;
            MemberList proMemberList =iMemberListService.getById(memberList.getPromoter());
            MarketingDistributionSetting marketingDistributionSetting=iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>().eq(MarketingDistributionSetting::getStatus,"1"));
            while (true){
                if(proMemberList==null){
                    break;
                }
                MemberDistributionLevel memberDistributionLevelpro=this.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,proMemberList.getId()).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
                if(memberDistributionLevelpro==null){
                    memberDistributionLevelpro=new MemberDistributionLevel();
                    memberDistributionLevelpro.setMemberListId(proMemberList.getId());
                    MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                            .eq(MarketingDistributionLevel::getStatus,"1")
                            .eq(MarketingDistributionLevel::getWaysObtain,"0")
                            .orderByDesc(MarketingDistributionLevel::getGrade)
                            .last("limit 1"));
                    if(marketingDistributionLevel!=null){
                        memberDistributionLevelpro.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                    }
                    if(!this.save(memberDistributionLevelpro)){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
                if(i==0){
                    memberDistributionLevelpro.setUpgradeDirect(memberDistributionLevelpro.getUpgradeDirect().add(new BigDecimal(1)));
                }
                memberDistributionLevelpro.setUpgradeTeamNumber(memberDistributionLevelpro.getUpgradeTeamNumber().add(new BigDecimal(1)));
                if(marketingDistributionSetting!=null){
                    if(marketingDistributionSetting.getDistributionLevel().equals("1")){
                        //参团次数必须大于0
                        long count=iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>().eq(MarketingGroupIntegralManageRecord::getMemberListId,memberId));
                        long firstCount=iMarketingZoneGroupRecordService.count(new LambdaQueryWrapper<MarketingZoneGroupRecord>().eq(MarketingZoneGroupRecord::getMemberListId,memberId));
                      if(count>0||firstCount>=0) {
                          MarketingDistributionLevel marketingDistributionLevel = iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                                  .eq(MarketingDistributionLevel::getStatus, "1")
                                  .eq(MarketingDistributionLevel::getWaysObtain, "2")
                                  .le(MarketingDistributionLevel::getDirect, memberDistributionLevelpro.getUpgradeDirect())
                                  .le(MarketingDistributionLevel::getTeamNumber, memberDistributionLevelpro.getUpgradeTeamNumber())
                                  .orderByDesc(MarketingDistributionLevel::getGrade)
                                  .last("limit 1"));
                          if (marketingDistributionLevel != null) {
                              if (Integer.parseInt(iMarketingDistributionLevelService.getById(memberDistributionLevelpro.getMarketingDistributionLevelId()).getGrade()) < Integer.parseInt(marketingDistributionLevel.getGrade())) {
                                  memberDistributionLevelpro.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                              }
                          }
                      }
                    }
                }
                if(!this.saveOrUpdate(memberDistributionLevelpro)){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                log.info("会员升级信息："+JSON.toJSONString(memberDistributionLevelpro));
                proMemberList=iMemberListService.getById(proMemberList.getPromoter());
                if(proMemberList==null){
                    break;
                }
                i++;
            }
        }
    }

    @Override
    public void teamRushUpgrade(String memberId) {
        //查询会员分销记录信息
        MemberDistributionLevel memberDistributionLevel=this.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,memberId).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
        if(memberDistributionLevel==null){
            memberDistributionLevel=new MemberDistributionLevel();
            memberDistributionLevel.setMemberListId(memberId);
            MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                    .eq(MarketingDistributionLevel::getStatus,"1")
                    .eq(MarketingDistributionLevel::getWaysObtain,"4")
                    .orderByAsc(MarketingDistributionLevel::getGrade)
                    .last("limit 1"));
            if(marketingDistributionLevel!=null){
                memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
            }
            if(!this.save(memberDistributionLevel)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else{
            MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                    .eq(MarketingDistributionLevel::getStatus,"1")
                    .eq(MarketingDistributionLevel::getWaysObtain,"4")
                    .orderByAsc(MarketingDistributionLevel::getGrade)
                    .last("limit 1"));
            if(marketingDistributionLevel!=null){
                if(StringUtils.isBlank(memberDistributionLevel.getMarketingDistributionLevelId())){
                    memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                }else {
                    if (Integer.parseInt(iMarketingDistributionLevelService.getById(memberDistributionLevel.getMarketingDistributionLevelId()).getGrade()) < Integer.parseInt(marketingDistributionLevel.getGrade())) {
                        memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                    }
                }

            }
            if(!this.saveOrUpdate(memberDistributionLevel)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getPromoterType().equals("1")) {
            int i = 0;
            MemberList proMemberList =iMemberListService.getById(memberList.getPromoter());
            //加入分销关系表
            MemberMarketingDistributionLevel memberMarketingDistributionLevel=iMemberMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MemberMarketingDistributionLevel>()
                    .eq(MemberMarketingDistributionLevel::getMemberListId,proMemberList.getId())
                    .eq(MemberMarketingDistributionLevel::getTMemberListId,memberList.getId()));
            if(memberMarketingDistributionLevel==null){
                memberMarketingDistributionLevel=new MemberMarketingDistributionLevel();
                memberMarketingDistributionLevel.setMemberListId(proMemberList.getId());
                memberMarketingDistributionLevel.setTMemberListId(memberList.getId());
                iMemberMarketingDistributionLevelService.save(memberMarketingDistributionLevel);
            }
            List<String> memberLists= Lists.newArrayList();
            memberLists.add(memberList.getId());
            MarketingDistributionSetting marketingDistributionSetting=iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>().eq(MarketingDistributionSetting::getStatus,"1"));
            while (true){
                if(proMemberList==null){
                    break;
                }
                MemberDistributionLevel memberDistributionLevelpro=this.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,proMemberList.getId()).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
                if(memberDistributionLevelpro==null){
                    memberDistributionLevelpro=new MemberDistributionLevel();
                    memberDistributionLevelpro.setMemberListId(proMemberList.getId());
                    MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                            .eq(MarketingDistributionLevel::getStatus,"1")
                            .eq(MarketingDistributionLevel::getWaysObtain,"0")
                            .orderByDesc(MarketingDistributionLevel::getGrade)
                            .last("limit 1"));
                    if(marketingDistributionLevel!=null){
                        memberDistributionLevelpro.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                    }
                    if(!this.save(memberDistributionLevelpro)){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
                if(i==0){
                    memberDistributionLevelpro.setUpgradeDirect(memberDistributionLevelpro.getUpgradeDirect().add(new BigDecimal(1)));
                }
                memberDistributionLevelpro.setUpgradeTeamNumber(memberDistributionLevelpro.getUpgradeTeamNumber().add(new BigDecimal(1)));
                if(marketingDistributionSetting!=null){
                    if(marketingDistributionSetting.getDistributionLevel().equals("1")){
                        //参团次数必须大于0
                            MarketingDistributionLevel marketingDistributionLevel = iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                                    .eq(MarketingDistributionLevel::getStatus, "1")
                                    .eq(MarketingDistributionLevel::getWaysObtain, "2")
                                    .le(MarketingDistributionLevel::getDirect, memberDistributionLevelpro.getUpgradeDirect())
                                    .le(MarketingDistributionLevel::getTeamNumber, memberDistributionLevelpro.getUpgradeTeamNumber())
                                    .orderByDesc(MarketingDistributionLevel::getGrade)
                                    .last("limit 1"));
                            if (marketingDistributionLevel != null) {
                                if (Integer.parseInt(iMarketingDistributionLevelService.getById(memberDistributionLevelpro.getMarketingDistributionLevelId()).getGrade()) < Integer.parseInt(marketingDistributionLevel.getGrade())) {
                                    memberDistributionLevelpro.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
                                }
                            }

                        MarketingDistributionLevel marketingDistributionLevel2 = iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                                .eq(MarketingDistributionLevel::getStatus, "1")
                                .eq(MarketingDistributionLevel::getWaysObtain, "3")
                                .gt(MarketingDistributionLevel::getGrade,Integer.parseInt(iMarketingDistributionLevelService.getById(memberDistributionLevelpro.getMarketingDistributionLevelId()).getGrade()))
                                .orderByAsc(MarketingDistributionLevel::getGrade)
                                .last("limit 1"));
                        if (marketingDistributionLevel2 != null) {
                            if(memberDistributionLevelpro.getUpgradeDirect().intValue()>=marketingDistributionLevel2.getDirect().intValue()) {
                                if (iMemberMarketingDistributionLevelService.getDistributionLevel(proMemberList.getId(), marketingDistributionLevel2.getMarketingDistributionLevelId()) >= marketingDistributionLevel2.getLevelNumber().intValue()) {
                                    if (Integer.parseInt(iMarketingDistributionLevelService.getById(memberDistributionLevelpro.getMarketingDistributionLevelId()).getGrade()) < Integer.parseInt(marketingDistributionLevel2.getGrade())) {
                                        memberDistributionLevelpro.setMarketingDistributionLevelId(marketingDistributionLevel2.getId());
                                    }
                                }
                            }
                        }
                    }
                }
                if(!this.saveOrUpdate(memberDistributionLevelpro)){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                log.info("会员升级信息："+JSON.toJSONString(memberDistributionLevelpro));
                String proMemberListId=proMemberList.getId();
                memberLists.add(proMemberListId);
                proMemberList=iMemberListService.getById(proMemberList.getPromoter());
                if(proMemberList==null){
                    break;
                }else{
                    //加入分销关系表
                    for (String mid:memberLists) {
                        MemberMarketingDistributionLevel memberMarketingDistributionLevelPro=iMemberMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MemberMarketingDistributionLevel>()
                                .eq(MemberMarketingDistributionLevel::getMemberListId,proMemberList.getId())
                                .eq(MemberMarketingDistributionLevel::getTMemberListId,mid));
                        if(memberMarketingDistributionLevelPro==null){
                            memberMarketingDistributionLevelPro=new MemberMarketingDistributionLevel();
                            memberMarketingDistributionLevelPro.setMemberListId(proMemberList.getId());
                            memberMarketingDistributionLevelPro.setTMemberListId(mid);
                            iMemberMarketingDistributionLevelService.save(memberMarketingDistributionLevelPro);
                        }
                    }
                }
                i++;
            }
        }
    }

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MemberDistributionLevelDTO memberDistributionLevelDTO) {
        return baseMapper.queryPageList(page,memberDistributionLevelDTO);
    }
}
