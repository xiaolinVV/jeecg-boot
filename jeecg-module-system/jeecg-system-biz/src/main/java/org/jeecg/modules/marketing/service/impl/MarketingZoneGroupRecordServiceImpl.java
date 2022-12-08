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
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupManageMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupRecordMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberDistributionLevel;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.pay.entity.PayZoneGroupLog;
import org.jeecg.modules.pay.mapper.PayZoneGroupLogMapper;
import org.jeecg.modules.pay.service.IPayZoneGroupLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团记录
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
@Service
@Log
public class MarketingZoneGroupRecordServiceImpl extends ServiceImpl<MarketingZoneGroupRecordMapper, MarketingZoneGroupRecord> implements IMarketingZoneGroupRecordService {
    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired(required = false)
    private MarketingZoneGroupManageMapper marketingZoneGroupManageMapper;

    @Autowired(required = false)
    private PayZoneGroupLogMapper payZoneGroupLogMapper;
    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;
    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    @Lazy
    private IMarketingZoneGroupManageService iMarketingZoneGroupManageService;

    @Autowired
    @Lazy
    private IMarketingZoneGroupService iMarketingZoneGroupService;

    @Autowired
    private IPayZoneGroupLogService iPayZoneGroupLogService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingZoneGroupBaseSettingService iMarketingZoneGroupBaseSettingService;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private IMarketingDistributionLevelService iMarketingDistributionLevelService;


    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingZoneGroupRecord> queryWrapper, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,queryWrapper,requestMap);
    }

    @Override
    public List<Map<String,Object>> getIndexByStatus() {
        return baseMapper.getIndexByStatus();
    }


    @Override
    public List<Map<String, Object>> getZoneGroupManageMemberList(String id) {
        return baseMapper.getZoneGroupManageMemberList(id);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingZoneGroupRecordByMemberId(Page<Map<String, Object>> page, HashMap<String, Object> map) {
        return baseMapper.getMarketingZoneGroupRecordByMemberId(page,map);
    }

    @Override
    public int getCountByMemberId(String memberId,String marketingZoneGroupId) {

        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("marketingZoneGroupId",marketingZoneGroupId);
        return baseMapper.getCountByMemberId(paramMap);
    }

    @Override
    public List<Map<String, Object>> recordsLiquidation() {
        return baseMapper.recordsLiquidation();
    }

    @Override
    @Transactional
    public void distributionRewards(String marketingZoneGroupRecordId) {
        MarketingZoneGroupRecord marketingZoneGroupRecord=this.getById(marketingZoneGroupRecordId);
        if(marketingZoneGroupRecord.getDistributionRewards().equals("1")){
            return;
        }

        MarketingZoneGroupManage marketingZoneGroupManage=iMarketingZoneGroupManageService.getById(marketingZoneGroupRecord.getMarketingZoneGroupManageId());
        MarketingZoneGroup marketingZoneGroup=iMarketingZoneGroupService.getById(marketingZoneGroupManage.getMarketingZoneGroupId());
        //未中奖分配
        if(marketingZoneGroupRecord.getStatus().equals("0")){
            //参团奖励
            if(marketingZoneGroup.getGroupAwardType().equals("0")){
                if(!iMemberWelfarePaymentsService.addWelfarePayments(marketingZoneGroupRecord.getMemberListId(),marketingZoneGroup.getTuxedoReward(),"28",marketingZoneGroupRecord.getSerialNumber(),"")){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            //退回资金
            PayZoneGroupLog payZoneGroupLog=iPayZoneGroupLogService.getById(marketingZoneGroupRecord.getPayZoneGroupLogId());
            BigDecimal backBlance=payZoneGroupLog.getBalance().add(payZoneGroupLog.getPayPrice());
            if(!iMemberListService.addBlance(marketingZoneGroupRecord.getMemberListId(),backBlance,marketingZoneGroupRecord.getSerialNumber(),"28")){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            if(!iMemberWelfarePaymentsService.addWelfarePayments(marketingZoneGroupRecord.getMemberListId(),payZoneGroupLog.getWelfarePayments(),"32",marketingZoneGroupRecord.getSerialNumber(),"")){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        marketingZoneGroupRecord.setDistributionRewards("1");
        this.saveOrUpdate(marketingZoneGroupRecord);
    }

    @Override
    @Transactional
    public void classificationReward(String marketingZoneGroupRecordId) {
        MarketingZoneGroupRecord marketingZoneGroupRecord=this.getById(marketingZoneGroupRecordId);
        MemberList memberList=iMemberListService.getById(marketingZoneGroupRecord.getMemberListId());
        if(marketingZoneGroupRecord.getClassificationReward().equals("1")){
            return;
        }

        MarketingZoneGroupManage marketingZoneGroupManage=iMarketingZoneGroupManageService.getById(marketingZoneGroupRecord.getMarketingZoneGroupManageId());
        MarketingZoneGroup marketingZoneGroup=iMarketingZoneGroupService.getById(marketingZoneGroupManage.getMarketingZoneGroupId());


        //推荐拼团奖励
        if (memberList.getPromoterType().equals("1")) {
            MemberList promotermemberList = iMemberListService.getById(memberList.getPromoter());
            //判断上一天的拼购次数
            if (promotermemberList != null) {
                if(!iMemberWelfarePaymentsService.addWelfarePayments(promotermemberList.getId(), marketingZoneGroup.getReferralBonuses(),  "29",marketingZoneGroupRecord.getSerialNumber(),"")){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }else{
                log.info("不分配推荐奖励，记录Id："+marketingZoneGroupRecordId+";会员id："+promotermemberList.getId());
            }
        }

        MarketingZoneGroupBaseSetting marketingZoneGroupBaseSetting=iMarketingZoneGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupBaseSetting>().eq(MarketingZoneGroupBaseSetting::getStatus,"1"));
        if(marketingZoneGroupBaseSetting!=null) {


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
                        int count = this.getRecordCount(proMemberList.getId(), marketingZoneGroupRecordId, marketingZoneGroup.getId());
                        if (count < marketingZoneGroupBaseSetting.getRewardConditions().intValue()) {
                            log.info("不分配级差、平级奖励，记录Id：" + marketingZoneGroupRecordId + ";会员id：" + proMemberList.getId());
                        } else {
                            //级差奖励
                          if(Integer.parseInt(levegrede)<Integer.parseInt(marketingDistributionLevelpro.getGrade())){
                                BigDecimal reward = new BigDecimal(100).multiply(marketingDistributionLevelpro.getDifferentialReward().subtract(iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>().eq(MarketingDistributionLevel::getGrade,levegrede)).getDifferentialReward())).divide(new BigDecimal(100), 2, RoundingMode.UP);
                                if (!iMemberWelfarePaymentsService.addAccountWelfarePayments(proMemberList.getId(), reward, "30", marketingZoneGroupRecord.getSerialNumber())) {
                                    //手动强制回滚事务，这里一定要第一时间处理
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                }
                            }

                            //平级
                            if (marketingDistributionLevelpro.getDifferentialReward().subtract(marketingDistributionLevel.getDifferentialReward()).doubleValue() == 0) {
                                if(gradeMap.get(marketingDistributionLevelpro.getGrade())==null) {
                                    gradeMap.put(marketingDistributionLevelpro.getGrade(), "1");
                                    BigDecimal reward = new BigDecimal(100).multiply(marketingDistributionLevelpro.getLevelRewards()).divide(new BigDecimal(100), 2, RoundingMode.UP);
                                    if (!iMemberWelfarePaymentsService.addAccountWelfarePayments(proMemberList.getId(), reward, "31", marketingZoneGroupRecord.getSerialNumber())) {
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

        marketingZoneGroupRecord.setClassificationReward("1");
        this.saveOrUpdate(marketingZoneGroupRecord);
    }

    @Override
    public int getRecordCount(String memberId, String id, String mgimi) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("id",id);
        paramMap.put("mgimi",mgimi);
        return baseMapper.getRecordCount(paramMap);
    }

    @Override
    public Map<String, Object> getWinning(String marketingZoneGroupManageId) {
        return baseMapper.getWinning(marketingZoneGroupManageId);
    }

    @Override
    public Map<String, Object> getMarketingZoneGroupRecordDetails(MarketingZoneGroupRecord marketingZoneGroupRecord) {
        HashMap<String, Object> map = new HashMap<>();

        MarketingZoneGroupManage marketingZoneGroupManage = marketingZoneGroupManageMapper.selectById(marketingZoneGroupRecord.getMarketingZoneGroupManageId());

        PayZoneGroupLog payZoneGroupLog = payZoneGroupLogMapper.selectById(marketingZoneGroupRecord.getPayZoneGroupLogId());
        GoodSpecification goodSpecification = iGoodSpecificationService.getById(marketingZoneGroupRecord.getGoodSpecificationId());
        GoodList goodList = iGoodListService.getById(goodSpecification.getGoodListId());
        map.put("status",marketingZoneGroupManage.getStatus());

        if (marketingZoneGroupManage.getStatus().equals("0")){
            map.put("statusDeclare","活动进行中，请耐心等待...");
        }else if (marketingZoneGroupManage.getStatus().equals("1")){
            if (marketingZoneGroupRecord.getStatus().equals("0")){
                map.put("statusDeclare","已成团，很遗憾本次活动未中奖，系统将退还支付金额到您的账户，请注意查收。");
            }else if (marketingZoneGroupRecord.getStatus().equals("1")){
                map.put("statusDeclare","已成团，恭喜您已中奖。");
            }else {
                map.put("statusDeclare","未成团，系统将退还支付金额到您的账户，请注意查收。");
            }
        }else {
            map.put("statusDeclare","未成团，系统将退还支付金额到您的账户，请注意查收。");
        }

        map.put("goodName",marketingZoneGroupRecord.getGoodName());

        map.put("mainPicture",marketingZoneGroupRecord.getMainPicture());

        map.put("specification",marketingZoneGroupRecord.getSpecification());

        map.put("price",marketingZoneGroupManage.getPrice());

        map.put("marketPrice",goodList.getMarketPrice());

        map.put("quantity",marketingZoneGroupRecord.getQuantity());
        if (payZoneGroupLog.getBalance().doubleValue()>0){
            map.put("balance","￥"+payZoneGroupLog.getBalance());
        }else {
            map.put("balance","");
        }
        if (payZoneGroupLog.getWelfarePayments().doubleValue()>0){
            BigDecimal integralValue = iMarketingWelfarePaymentsSettingService.getIntegralValue();
            map.put("welfarePayments",payZoneGroupLog.getWelfarePayments().doubleValue()+"福利金≈￥"+integralValue.doubleValue()*payZoneGroupLog.getWelfarePayments().doubleValue());
        }else {
            map.put("welfarePayments","");
        }
        map.put("payModel",payZoneGroupLog.getPayModel());
        if (payZoneGroupLog.getPayPrice().doubleValue()>0){
            map.put("payPrice","￥"+payZoneGroupLog.getPayPrice());
        }else {
            map.put("payPrice","");
        }
        if (marketingZoneGroupRecord.getIdentity().equals("0")){
            map.put("identity","参与人");
        }else{
            map.put("identity","发起人");
        }
        map.put("serialNumber",marketingZoneGroupRecord.getSerialNumber());
        map.put("tuxedoTime", DateUtils.formatTime(marketingZoneGroupRecord.getTuxedoTime().getTime()));
        return map;
    }
}
