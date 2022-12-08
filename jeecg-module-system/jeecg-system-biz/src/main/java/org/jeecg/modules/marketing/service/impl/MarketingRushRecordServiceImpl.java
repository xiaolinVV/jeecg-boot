package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingRushRecordMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberDistributionLevel;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Description: 抢购活动-购买记录
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
@Log
public class MarketingRushRecordServiceImpl extends ServiceImpl<MarketingRushRecordMapper, MarketingRushRecord> implements IMarketingRushRecordService {
    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;
    @Autowired
    private IGoodListService iGoodListService;
    @Autowired
    private IMarketingRushTypeService iMarketingRushTypeService;
    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private IMarketingDistributionLevelService iMarketingDistributionLevelService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMarketingRushGroupService iMarketingRushGroupService;

    @Autowired
    private IMarketingIntegralRecordService iMarketingIntegralRecordService;


    @Autowired
    private IMarketingFourthIntegralRecordService iMarketingFourthIntegralRecordService;

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingRushRecord> queryWrapper, Map<String, Object> requestMap) {
        IPage<Map<String, Object>> mapIPage = baseMapper.queryPageList(page, queryWrapper, requestMap);
        /*mapIPage.getRecords().forEach(mp->{
            if (mp.get("mainPicture")!=null){
                mp.put("mainPicture",JSON.parseObject(String.valueOf(mp.get("mainPicture"))).get("0").toString());
            }
        });*/
        return mapIPage;
    }

    @Override
    public IPage<Map<String, Object>> findMarketingRushRecord(Page<Map<String, Object>> page, String memberId, String status) {
        IPage<Map<String, Object>> marketingRushRecord = baseMapper.findMarketingRushRecord(page, memberId, status);
        MarketingRushBaseSetting marketingRushBaseSetting = iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getDelFlag, "0")
                .orderByDesc(MarketingRushBaseSetting::getCreateTime)
                .last("limit 1")
        );
        marketingRushRecord.getRecords().forEach(mrr->{
            GoodSpecification goodSpecificationId = iGoodSpecificationService.getById(mrr.get("goodSpecificationId").toString());
            GoodList goodList = iGoodListService.getById(goodSpecificationId.getGoodListId());
            mrr.put("marketPrice",goodList.getMarketPrice());
            mrr.put("label",marketingRushBaseSetting.getLabel());
            mrr.remove("goodSpecificationId");
        });
        return marketingRushRecord;
    }

    @Override
    public Map<String, Object> getMarketingRushRecordParticulars(String id) {
        HashMap<String, Object> map = new HashMap<>();
        MarketingRushRecord marketingRushRecord = this.getById(id);
        MarketingRushBaseSetting marketingRushBaseSetting = iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getDelFlag, "0")
                .orderByDesc(MarketingRushBaseSetting::getCreateTime)
                .last("limit 1")
        );
        map.put("statusCause",marketingRushRecord.getStatus().equals("0")?"抢购失败，很遗憾抢购失败，系统将退还支付金额到您的账户，请注意查收。":"抢购成功，恭喜您，抢购成功。");

        map.put("mainPicture",marketingRushRecord.getMainPicture());
        map.put("goodName",marketingRushRecord.getGoodName());
        map.put("specification",marketingRushRecord.getSpecification());
        map.put("amount",marketingRushRecord.getAmount());
        map.put("price",marketingRushRecord.getPrice());
        map.put("serialNumber",marketingRushRecord.getSerialNumber());

        map.put("createTime", DateUtils.date2Str(marketingRushRecord.getCreateTime(),DateUtils.datetimeFormat.get()));
        GoodSpecification goodSpecification = iGoodSpecificationService.getById(marketingRushRecord.getGoodSpecificationId());
        GoodList goodList = iGoodListService.getById(goodSpecification.getGoodListId());
        map.put("marketPrice",goodList.getMarketPrice());
        MarketingRushType marketingRushType = iMarketingRushTypeService.getById(marketingRushRecord.getMarketingRushTypeId());
        map.put("rushPrice",marketingRushType.getPrice());
        map.put("label",marketingRushBaseSetting.getLabel());
        return map;
    }

    @Override
    public Map<String, Object> findTodayRushData(String memberId, String marketingRushTypeId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("todayRushDataAll",baseMapper.findTodayRushDataCount(memberId,marketingRushTypeId,"2"));
        map.put("todayRushDataFail",baseMapper.findTodayRushDataCount(memberId,marketingRushTypeId,"0"));
        map.put("todayRushDataSucceed",baseMapper.findTodayRushDataCount(memberId,marketingRushTypeId,"1"));
        /*iMarketingRushGroupService.getOne(new LambdaQueryWrapper<MarketingRushGroup>()
                .eq(MarketingRushGroup::getDelFlag, "0")
                .eq(MarketingRushGroup::getMarketingRushTypeId, marketingRushTypeId)
                .eq(MarketingRushGroup::getMemberListId,memberId)
                .ne(MarketingRushGroup::getConsignmentStatus,"2")
                .orderByDesc(MarketingRushGroup::getCreateTime)
                .last("limit 1")
        );*/
        /*if (marketingRushGroup != null){
            map.put("consignmentGoods",this.count(new LambdaQueryWrapper<MarketingRushRecord>()
                    .eq(MarketingRushRecord::getDelFlag,"0")
                    .eq(MarketingRushRecord::getMarketingRushGroupId,marketingRushGroup.getId())
                    .eq(MarketingRushRecord::getMemberListId,memberId)
                    .eq(MarketingRushRecord::getStatus,"1")
            ));
        }else {
        }*/
        map.put("consignmentGoods",baseMapper.countConsignmentGoods(marketingRushTypeId,memberId));
        return map;
    }

    @Override
    @Transactional
    public void classificationReward(String marketingRushRecordId) {
        MarketingRushRecord marketingRushRecord=this.getById(marketingRushRecordId);

        if(marketingRushRecord.getClassificationReward().equals("1")){
            log.info("数据已经结算过："+marketingRushRecord.getId());
            return;
        }

        MemberList memberList=iMemberListService.getById(marketingRushRecord.getMemberListId());

        MarketingRushType marketingRushType=iMarketingRushTypeService.getById(marketingRushRecord.getMarketingRushTypeId());

        MarketingRushBaseSetting marketingRushBaseSetting=iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getStatus,"1"));

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(marketingRushRecord.getCreateTime());

        List<MarketingRushRecord> marketingRushRecords=this.list(new LambdaQueryWrapper<MarketingRushRecord>()
                .eq(MarketingRushRecord::getYear,calendar.get(Calendar.YEAR))
                .eq(MarketingRushRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingRushRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingRushRecord::getMemberListId,memberList.getId())
                .eq(MarketingRushRecord::getMarketingRushTypeId,marketingRushType.getId())
                .eq(MarketingRushRecord::getClassificationReward,"0"));

        log.info("结算数量:"+marketingRushRecords.size());

        BigDecimal totalPrice=new BigDecimal(0);

        //设置结算状态
        for (MarketingRushRecord m:marketingRushRecords) {
            m.setClassificationReward("1");
            totalPrice=totalPrice.add(m.getPrice());
        }

        log.info("结算金额:"+totalPrice);


        long memberRushCount=this.count(new LambdaQueryWrapper<MarketingRushRecord>()
                .eq(MarketingRushRecord::getYear,calendar.get(Calendar.YEAR))
                .eq(MarketingRushRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingRushRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingRushRecord::getMemberListId,memberList.getId())
                .eq(MarketingRushRecord::getMarketingRushTypeId,marketingRushType.getId()));

        if (memberRushCount>=marketingRushType.getPurchaseLimitation().intValue()) {
            if (marketingRushBaseSetting != null) {

                //推荐拼团奖励
                if (memberList.getPromoterType().equals("1")) {
                    MemberList promotermemberList = iMemberListService.getById(memberList.getPromoter());
                    //判断上一天的拼购次数
                    long count = this.count(new LambdaQueryWrapper<MarketingRushRecord>()
                            .eq(MarketingRushRecord::getYear, calendar.get(Calendar.YEAR))
                            .eq(MarketingRushRecord::getMonth, calendar.get(Calendar.MONTH) + 1)
                            .eq(MarketingRushRecord::getDay, calendar.get(Calendar.DAY_OF_MONTH))
                            .eq(MarketingRushRecord::getMemberListId, promotermemberList.getId())
                            .eq(MarketingRushRecord::getMarketingRushTypeId, marketingRushType.getId()));
                    if (promotermemberList != null && count >= marketingRushType.getPurchaseLimitation().intValue()) {
                        log.info("推荐奖励余额："+marketingRushType.getRecommendGroupRewards().multiply(new BigDecimal(marketingRushRecords.size()).multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.DOWN)));
                        if (!iMemberListService.addBlance(promotermemberList.getId(), marketingRushType.getRecommendGroupRewards().multiply(new BigDecimal(marketingRushRecords.size()).multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.DOWN)), marketingRushRecord.getSerialNumber(), "32")) {
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        //加上积分
                        log.info("推荐奖励积分："+marketingRushType.getRecommendGroupRewards().multiply(new BigDecimal(marketingRushRecords.size()).multiply(new BigDecimal(0.2)).setScale(2, RoundingMode.DOWN)));
                        if (!iMemberWelfarePaymentsService.addWelfarePayments(promotermemberList.getId(), marketingRushType.getRecommendGroupRewards().multiply(new BigDecimal(marketingRushRecords.size()).multiply(new BigDecimal(0.2)).setScale(2, RoundingMode.DOWN)), "38", marketingRushRecord.getSerialNumber(),"")) {
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    } else {
                        log.info("不分配抢购推荐奖励，记录Id：" + marketingRushRecordId + ";会员id：" + promotermemberList.getId());
                    }
                }
                Map<String, Object> gradeMap = Maps.newHashMap();

                String levegrede = null;


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

                        if (StringUtils.isBlank(levegrede)) {
                            levegrede = marketingDistributionLevel.getGrade();
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
                            long count = this.count(new LambdaQueryWrapper<MarketingRushRecord>()
                                    .eq(MarketingRushRecord::getYear, calendar.get(Calendar.YEAR))
                                    .eq(MarketingRushRecord::getMonth, calendar.get(Calendar.MONTH) + 1)
                                    .eq(MarketingRushRecord::getDay, calendar.get(Calendar.DAY_OF_MONTH))
                                    .eq(MarketingRushRecord::getMemberListId, proMemberList.getId())
                                    .eq(MarketingRushRecord::getMarketingRushTypeId, marketingRushType.getId()));
                            if (count < marketingRushType.getPurchaseLimitation().intValue()) {
                                log.info("不分配拼购级差、平级奖励，记录Id：" + marketingRushRecordId + ";会员id：" + proMemberList.getId());
                            } else {
                                //级差奖励
                                if (Integer.parseInt(levegrede) < Integer.parseInt(marketingDistributionLevelpro.getGrade())) {
                                    BigDecimal reward = totalPrice.multiply(marketingDistributionLevelpro.getDifferentialReward().subtract(iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>().eq(MarketingDistributionLevel::getGrade, levegrede)).getDifferentialReward())).divide(new BigDecimal(10000), 2, RoundingMode.DOWN);
                                    log.info("级差余额："+reward.multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.DOWN));
                                    if (!iMemberListService.addBlance(proMemberList.getId(), reward.multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.DOWN), marketingRushRecord.getSerialNumber(), "33")) {
                                        //手动强制回滚事务，这里一定要第一时间处理
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    }
//
//                                    //加上积分
                                    log.info("级差积分："+reward.multiply(new BigDecimal(0.2)).setScale(2, RoundingMode.DOWN));
                                    if (!iMemberWelfarePaymentsService.addWelfarePayments(proMemberList.getId(), reward.multiply(new BigDecimal(0.2)).setScale(2, RoundingMode.DOWN), "39", marketingRushRecord.getSerialNumber(),"")) {
                                        //手动强制回滚事务，这里一定要第一时间处理
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    }
                                }
                                //平级
                                if (marketingDistributionLevelpro.getDifferentialReward().subtract(marketingDistributionLevel.getDifferentialReward()).doubleValue() == 0) {
                                    if (gradeMap.get(marketingDistributionLevelpro.getGrade()) == null) {
                                        gradeMap.put(marketingDistributionLevelpro.getGrade(), "1");
                                        BigDecimal reward = totalPrice.multiply(marketingDistributionLevelpro.getLevelRewards()).divide(new BigDecimal(10000), 2, RoundingMode.DOWN);
                                        log.info("平级余额："+reward.multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.DOWN));
                                        if (!iMemberListService.addBlance(proMemberList.getId(), reward.multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.DOWN), marketingRushRecord.getSerialNumber(), "34")) {
                                            //手动强制回滚事务，这里一定要第一时间处理
                                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                        }
//
//                                        //加上积分
                                        log.info("平级积分："+reward.multiply(new BigDecimal(0.2)).setScale(2, RoundingMode.DOWN));
                                        if (!iMemberWelfarePaymentsService.addWelfarePayments(proMemberList.getId(), reward.multiply(new BigDecimal(0.2)).setScale(2, RoundingMode.DOWN), "40", marketingRushRecord.getSerialNumber(),"")) {
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
                            if (Integer.parseInt(levegrede) < Integer.parseInt(marketingDistributionLevelpro.getGrade())) {
                                levegrede = marketingDistributionLevelpro.getGrade();
                            }
                        }
                    }
                }
                log.info("平级分配等级表：" + JSON.toJSONString(gradeMap));

            }
        }else{
            log.info("不分配任何奖励");
        }

        if(!this.updateBatchById(marketingRushRecords)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    @Transactional
    public void groupDistributionRewards(String marketingRushGroupId) {
        MarketingRushGroup marketingRushGroup=iMarketingRushGroupService.getById(marketingRushGroupId);
        if(marketingRushGroup.getDistributionRewards().equals("1")){
            log.info("分组已结算");
            return;
        }
        List<MarketingRushRecord> marketingRushRecords=this.list(new LambdaQueryWrapper<MarketingRushRecord>()
                .eq(MarketingRushRecord::getMarketingRushGroupId,marketingRushGroup.getId()));

        BigDecimal totalPrice=new BigDecimal(0);

        //设置结算状态
        for (MarketingRushRecord m:marketingRushRecords) {
            m.setClassificationReward("1");
            totalPrice=totalPrice.add(m.getPrice());
        }

        log.info("结算金额:"+totalPrice);

        if (!iMemberListService.addBlance(marketingRushGroup.getMemberListId(), totalPrice, marketingRushGroup.getSerialNumber(), "35")) {
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        marketingRushGroup.setDistributionRewards("1");
        if(!iMarketingRushGroupService.saveOrUpdate(marketingRushGroup)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    @Transactional
    public void groupConsignmentDistributionRewards(String marketingRushGroupId) {
        MarketingRushGroup marketingRushGroup=iMarketingRushGroupService.getById(marketingRushGroupId);
        if(marketingRushGroup.getDistributionRewards().equals("1")){
            log.info("分组已结算");
            return;
        }
        MarketingRushType marketingRushType=iMarketingRushTypeService.getById(marketingRushGroup.getMarketingRushTypeId());

        long marketingRushGroupCount=this.count(new LambdaQueryWrapper<MarketingRushRecord>().eq(MarketingRushRecord::getMarketingRushGroupId,marketingRushGroup.getId()));
        if(marketingRushGroupCount>=marketingRushGroup.getTransformationThreshold().intValue()) {
            marketingRushGroup.setDistributionRewards("1");
            marketingRushGroup.setConsignmentTime(new Date());
            marketingRushGroup.setConsignmentStatus("2");
            marketingRushGroup.setIfPurchase("1");
            marketingRushGroup.setPurchaseTime(new Date());
            if (!iMarketingRushGroupService.saveOrUpdate(marketingRushGroup)) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            if (!iMarketingFourthIntegralRecordService.addFourthIntegral(marketingRushGroup.getMemberListId(), marketingRushType.getIntoBalance(), marketingRushGroup.getSerialNumber(), "7")) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            if (!iMarketingIntegralRecordService.addMarketingIntegralRecord("12", marketingRushType.getIntoShoppingCredits(), marketingRushGroup.getMemberListId(), marketingRushGroup.getSerialNumber())) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else{
            log.info("分组次数不满足");
            marketingRushGroup.setDistributionRewards("1");
            if (!iMarketingRushGroupService.saveOrUpdate(marketingRushGroup)) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }
}
