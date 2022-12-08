package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.dto.MarketingGroupIntegralManageRecordDTO;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingGroupIntegralManageRecordMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberDistributionLevel;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberThirdIntegral;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberThirdIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拼购记录
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
@Service
@Log
public class MarketingGroupIntegralManageRecordServiceImpl extends ServiceImpl<MarketingGroupIntegralManageRecordMapper, MarketingGroupIntegralManageRecord> implements IMarketingGroupIntegralManageRecordService {


    @Autowired
    private IMarketingGroupIntegralManageService iMarketingGroupIntegralManageService;

    @Autowired
    private IMarketingGroupIntegralManageListService iMarketingGroupIntegralManageListService;

    @Autowired
    private IMemberThirdIntegralService iMemberThirdIntegralService;

    @Autowired
    private IMarketingFourthIntegralRecordService iMarketingFourthIntegralRecordService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingIntegralRecordService iMarketingIntegralRecordService;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private IMarketingDistributionLevelService iMarketingDistributionLevelService;

    @Autowired
    private IMarketingGroupIntegralBaseSettingService iMarketingGroupIntegralBaseSettingService;


    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MarketingGroupIntegralManageRecordDTO marketingGroupIntegralManageRecordDTO) {
        return baseMapper.queryPageList(page,marketingGroupIntegralManageRecordDTO);
    }

    @Override
    public IPage<Map<String,Object>> getByMarketingGroupIntegralManageListId(Page<Map<String,Object>> page,String marketingGroupIntegralManageListId) {
        return baseMapper.getByMarketingGroupIntegralManageListId(page,marketingGroupIntegralManageListId);
    }

    @Override
    public List<Map<String, Object>> getByMarketingGroupIntegralManageListId(String marketingGroupIntegralManageListId) {
        return baseMapper.getByMarketingGroupIntegralManageListId(marketingGroupIntegralManageListId);
    }

    @Override
    public List<Map<String, Object>> getWinningState() {
        return baseMapper.getWinningState();
    }

    @Override
    @Transactional
    public void distributionRewards(String marketingGroupIntegralManageRecordId) {
        MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecord=this.getById(marketingGroupIntegralManageRecordId);
        MarketingGroupIntegralManageList marketingGroupIntegralManageList=iMarketingGroupIntegralManageListService.getById(marketingGroupIntegralManageRecord.getMarketingGroupIntegralManageListId());
        //判断奖励处理状态是未处理的
        if(marketingGroupIntegralManageRecord.getDistributionRewards().equals("1")){
            return;
        }
        MarketingGroupIntegralManage marketingGroupIntegralManage=iMarketingGroupIntegralManageService.getById(marketingGroupIntegralManageList.getMarketingGroupIntegralManageId());
        //中奖人奖励
        if(marketingGroupIntegralManageRecord.getWinningState().equals("1")){
            marketingGroupIntegralManageRecord.setRewardType("1");
            marketingGroupIntegralManageRecord.setMissedRewards(marketingGroupIntegralManage.getWinningNumber());
            if(!iMemberThirdIntegralService.addThirdIntegral(marketingGroupIntegralManage.getId(),marketingGroupIntegralManageRecord.getMemberListId(),marketingGroupIntegralManage.getWinningNumber(),marketingGroupIntegralManageRecord.getSerialNumber(),"1")){
                log.info("结算第三积分回滚");
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        //未中奖
        if(marketingGroupIntegralManageRecord.getWinningState().equals("0")){
            marketingGroupIntegralManageRecord.setWinningState("2");
            marketingGroupIntegralManageRecord.setMissedRewards(marketingGroupIntegralManage.getMissedRewards());
            if(!iMarketingFourthIntegralRecordService.addFourthIntegral(marketingGroupIntegralManageRecord.getMemberListId(),marketingGroupIntegralManage.getMissedRewards(),marketingGroupIntegralManageRecord.getSerialNumber(),"3")){
                log.info("结算产品券回滚");
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        //参与拼购奖励
        if(!iMemberListService.addBlance(marketingGroupIntegralManageRecord.getMemberListId(),marketingGroupIntegralManage.getGroupRewards(),marketingGroupIntegralManageRecord.getSerialNumber(),"20")){
            log.info("结算参团奖励回滚");
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        marketingGroupIntegralManageRecord.setDistributionRewards("1");
        if(!this.saveOrUpdate(marketingGroupIntegralManageRecord)){
            log.info("结算拼购记录回滚");
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        //判断拼购列表是否结算
        long count=this.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                .eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageListId,marketingGroupIntegralManageList.getId())
                .eq(MarketingGroupIntegralManageRecord::getDistributionRewards,"0"));
        if(marketingGroupIntegralManageList.getDistributionRewards().equals("0")&&count==0){
            marketingGroupIntegralManageList.setDistributionRewards("1");
            if(!iMarketingGroupIntegralManageListService.saveOrUpdate(marketingGroupIntegralManageList)){
                log.info("结算保存团信息回滚");
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

    @Override
    @Transactional
    public void classificationReward(String marketingGroupIntegralManageRecordId) {


        MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecord=this.getById(marketingGroupIntegralManageRecordId);

        if(marketingGroupIntegralManageRecord.getClassificationReward().equals("1")){
            log.info("数据已经结算过："+marketingGroupIntegralManageRecord.getId());
            return;
        }

        MemberList memberList=iMemberListService.getById(marketingGroupIntegralManageRecord.getMemberListId());

        MarketingGroupIntegralManage marketingGroupIntegralManage=iMarketingGroupIntegralManageService.getById(marketingGroupIntegralManageRecord.getMarketingGroupIntegralManageId());

        MarketingGroupIntegralBaseSetting marketingGroupIntegralBaseSetting=iMarketingGroupIntegralBaseSettingService.getOne(new LambdaQueryWrapper<MarketingGroupIntegralBaseSetting>().eq(MarketingGroupIntegralBaseSetting::getStatus,"1"));


        log.info("分级奖励："+marketingGroupIntegralManageRecordId);

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(marketingGroupIntegralManageRecord.getCreateTime());

        //获取会员当前档位的所有拼团记录
        List<MarketingGroupIntegralManageRecord> marketingGroupIntegralManageRecords=this.list(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                .eq(MarketingGroupIntegralManageRecord::getMemberListId,marketingGroupIntegralManageRecord.getMemberListId())
                .eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageId,marketingGroupIntegralManageRecord.getMarketingGroupIntegralManageId())
                .eq(MarketingGroupIntegralManageRecord::getClassificationReward,"0")
                .eq(MarketingGroupIntegralManageRecord::getYear,calendar.get(Calendar.YEAR))
                .eq(MarketingGroupIntegralManageRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingGroupIntegralManageRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingGroupIntegralManageRecord::getDistributionRewards,"1"));

        log.info("结算数量:"+marketingGroupIntegralManageRecords.size());

        //设置结算状态
        for (MarketingGroupIntegralManageRecord m:marketingGroupIntegralManageRecords) {
            m.setClassificationReward("1");
        }
        if(marketingGroupIntegralBaseSetting!=null) {
                //阈值处理
                //获取兑换券数量
                MemberThirdIntegral memberThirdIntegral = iMemberThirdIntegralService.getOne(new LambdaQueryWrapper<MemberThirdIntegral>()
                        .eq(MemberThirdIntegral::getMemberListId, marketingGroupIntegralManageRecord.getMemberListId())
                        .eq(MemberThirdIntegral::getMarketingGroupIntegralManageId, marketingGroupIntegralManage.getId())
                        .last("limit 1"));
                if (memberThirdIntegral != null) {
                    if (memberThirdIntegral.getIntegral().subtract(marketingGroupIntegralManage.getThreshold()).doubleValue() >= 0) {
                        //转入余额
                        if(!iMemberThirdIntegralService.subtractThirdIntegral(marketingGroupIntegralManage.getId(), marketingGroupIntegralManageRecord.getMemberListId(), marketingGroupIntegralManage.getIntoBalance(), marketingGroupIntegralManageRecord.getSerialNumber(), "2")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        if(!iMemberListService.addBlance(marketingGroupIntegralManageRecord.getMemberListId(), marketingGroupIntegralManage.getIntoBalance(), marketingGroupIntegralManageRecord.getSerialNumber(), "19")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        //转入购物积分
                        if(!iMemberThirdIntegralService.subtractThirdIntegral(marketingGroupIntegralManage.getId(), marketingGroupIntegralManageRecord.getMemberListId(), marketingGroupIntegralManage.getIntoShoppingCredits(), marketingGroupIntegralManageRecord.getSerialNumber(), "3")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        if(!iMarketingIntegralRecordService.addMarketingIntegralRecord("7", marketingGroupIntegralManage.getIntoShoppingCredits(), marketingGroupIntegralManageRecord.getMemberListId(), marketingGroupIntegralManageRecord.getSerialNumber())){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }
                }

            //推荐拼团奖励
            if (memberList.getPromoterType().equals("1")) {
                MemberList promotermemberList = iMemberListService.getById(memberList.getPromoter());
                //判断上一天的拼购次数
                int count=this.getRecordCount(promotermemberList.getId(),marketingGroupIntegralManageRecordId,marketingGroupIntegralManage.getId());
                if (promotermemberList != null&&count>=marketingGroupIntegralBaseSetting.getTimes().intValue()) {
                    if(!iMemberListService.addBlance(promotermemberList.getId(), marketingGroupIntegralManage.getRecommendGroupRewards().multiply(new BigDecimal(marketingGroupIntegralManageRecords.size())), marketingGroupIntegralManageRecord.getSerialNumber(), "21")){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }else{
                    log.info("不分配拼购推荐奖励，记录Id："+marketingGroupIntegralManageRecordId+";会员id："+promotermemberList.getId());
                }
            }



            Map<String,Object> gradeMap=Maps.newHashMap();

            String levegrede=null;


            //会员分销奖励
            if (memberList.getPromoterType().equals("1")) {
                //获取自己的会员等级
                MemberDistributionLevel memberDistributionLevel = iMemberDistributionLevelService.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId, memberList.getId()).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
                if (memberDistributionLevel != null) {
                    MemberList proMemberList = iMemberListService.getById(memberList.getPromoter());
                    MarketingDistributionLevel marketingDistributionLevel = iMarketingDistributionLevelService.getById(memberDistributionLevel.getMarketingDistributionLevelId());
                    if (marketingDistributionLevel == null) {
                        return;
                    }

                    if(StringUtils.isBlank(levegrede)){
                        levegrede=marketingDistributionLevel.getGrade();
                    }

                    while (true) {
                        if (proMemberList == null) {
                            break;
                        }
                        MemberDistributionLevel memberDistributionLevelpro = iMemberDistributionLevelService.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId, proMemberList.getId()).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
                        if (memberDistributionLevelpro == null) {
                            break;
                        }
                        MarketingDistributionLevel marketingDistributionLevelpro = iMarketingDistributionLevelService.getById(memberDistributionLevelpro.getMarketingDistributionLevelId());
                        //判断上一天的拼购次数
                        int count=this.getRecordCount(proMemberList.getId(),marketingGroupIntegralManageRecordId,marketingGroupIntegralManage.getId());
                        if (count<marketingGroupIntegralBaseSetting.getTimes().intValue()) {
                            log.info("不分配拼购级差、平级奖励，记录Id："+marketingGroupIntegralManageRecordId+";会员id："+proMemberList.getId());
                        }else {
                            //级差奖励
                            if(Integer.parseInt(levegrede)<Integer.parseInt(marketingDistributionLevelpro.getGrade())){
                                BigDecimal reward = new BigDecimal(99).multiply(marketingDistributionLevelpro.getDifferentialReward().subtract(iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>().eq(MarketingDistributionLevel::getGrade,levegrede)).getDifferentialReward())).divide(new BigDecimal(100), 2, RoundingMode.UP);
                                if(!iMemberListService.addBlance(proMemberList.getId(), reward.multiply(new BigDecimal(0.9)).multiply(new BigDecimal(marketingGroupIntegralManageRecords.size())).setScale(2, RoundingMode.HALF_UP), marketingGroupIntegralManageRecord.getSerialNumber(), "22")){
                                    //手动强制回滚事务，这里一定要第一时间处理
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                }
                                if(!iMarketingIntegralRecordService.addMarketingIntegralRecord("9", reward.multiply(new BigDecimal(0.1)).multiply(new BigDecimal(marketingGroupIntegralManageRecords.size())).setScale(2, RoundingMode.HALF_UP), proMemberList.getId(), marketingGroupIntegralManageRecord.getSerialNumber())){
                                    //手动强制回滚事务，这里一定要第一时间处理
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                }
                            }
                            //平级
                            if (marketingDistributionLevelpro.getDifferentialReward().subtract(marketingDistributionLevel.getDifferentialReward()).doubleValue() == 0) {
                                if (gradeMap.get(marketingDistributionLevelpro.getGrade()) == null) {
                                    gradeMap.put(marketingDistributionLevelpro.getGrade(), "1");
                                    BigDecimal reward = new BigDecimal(99).multiply(marketingDistributionLevelpro.getLevelRewards()).divide(new BigDecimal(100), 2, RoundingMode.UP);
                                    if(!iMemberListService.addBlance(proMemberList.getId(), reward.multiply(new BigDecimal(0.9)).multiply(new BigDecimal(marketingGroupIntegralManageRecords.size())).setScale(2, RoundingMode.HALF_UP), marketingGroupIntegralManageRecord.getSerialNumber(), "23")){
                                        //手动强制回滚事务，这里一定要第一时间处理
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    }
                                    if(!iMarketingIntegralRecordService.addMarketingIntegralRecord("10", reward.multiply(new BigDecimal(0.1)).multiply(new BigDecimal(marketingGroupIntegralManageRecords.size())).setScale(2, RoundingMode.HALF_UP), proMemberList.getId(), marketingGroupIntegralManageRecord.getSerialNumber())){
                                        //手动强制回滚事务，这里一定要第一时间处理
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    }
                                }
                            }
                        }
                        proMemberList = iMemberListService.getById(proMemberList.getPromoter());
                        if (proMemberList == null) {
                            break;
                        }
                        marketingDistributionLevel = marketingDistributionLevelpro;
                        if(Integer.parseInt(levegrede)<Integer.parseInt(marketingDistributionLevelpro.getGrade())){
                            levegrede=marketingDistributionLevelpro.getGrade();
                        }
                    }
                }
            }
            log.info("平级分配等级表："+ JSON.toJSONString(gradeMap));
        }


        if(!this.updateBatchById(marketingGroupIntegralManageRecords)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public int getDistributionRewards(String memberId) {
        return baseMapper.getDistributionRewards(memberId);
    }

    @Override
    public Map<String, Object> getWinning(String marketingGroupIntegralManageListId) {
        return baseMapper.getWinning(marketingGroupIntegralManageListId);
    }

    @Override
    public List<Map<String, Object>> recordsLiquidation() {
        return baseMapper.recordsLiquidation();
    }

    @Override
    public int getRecordCount(String memberId,String id,String mgimi) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("id",id);
        paramMap.put("mgimi",mgimi);
        return baseMapper.getRecordCount(paramMap);
    }
}
