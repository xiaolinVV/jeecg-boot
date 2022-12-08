package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingBusinessGiftListMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberBusinessDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.pay.entity.PayBusinessGiftLog;
import org.jeecg.modules.pay.service.IPayBusinessGiftLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 创业礼包列表
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Service
@Log
public class MarketingBusinessGiftListServiceImpl extends ServiceImpl<MarketingBusinessGiftListMapper, MarketingBusinessGiftList> implements IMarketingBusinessGiftListService {

    @Autowired
    private IPayBusinessGiftLogService iPayBusinessGiftLogService;

    @Autowired
    private IMarketingBusinessGiftRecordService iMarketingBusinessGiftRecordService;

    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;


    @Autowired
    private IMarketingBusinessCapitalService iMarketingBusinessCapitalService;

    @Autowired
    private IMarketingBusinessDesignationService iMarketingBusinessDesignationService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMemberBusinessDesignationService iMemberBusinessDesignationService;

    @Autowired
    private IMarketingBusinessGiftTeamRecordService marketingBusinessGiftTeamRecordService;

    @Autowired
    private IMarketingBusinessGiftTeamListService iMarketingBusinessGiftTeamListService;

    @Autowired
    private IMarketingBusinessGiftIdentityService iMarketingBusinessGiftIdentityService;

    @Autowired
    private IMarketingBusinessGiftBaseSettingService iMarketingBusinessGiftBaseSettingService;



    @Override
    public IPage<Map<String, Object>> findMarketingBusinessGiftList(Page<Map<String, Object>> page) {
        return baseMapper.findMarketingBusinessGiftList(page);
    }

    @Override
    public IPage<Map<String, Object>> getIsMarketingBusinessGiftList(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.getIsMarketingBusinessGiftList(page,memberId);
    }

    @Override
    @Transactional
    public void success(String payBusinessGiftLogId) {
        PayBusinessGiftLog payBusinessGiftLog=iPayBusinessGiftLogService.getById(payBusinessGiftLogId);
        payBusinessGiftLog.setPayStatus("1");
        iPayBusinessGiftLogService.saveOrUpdate(payBusinessGiftLog);
        //扣除余额
        if(!iMemberListService.subtractBlance(payBusinessGiftLog.getMemberListId(), payBusinessGiftLog.getBalance(), payBusinessGiftLog.getId(), "29")){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        //扣除积分
        if(!iMemberWelfarePaymentsService.subtractWelfarePayments(payBusinessGiftLog.getMemberListId(), payBusinessGiftLog.getWelfarePayments(), "35", payBusinessGiftLog.getId(),"")){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        MemberShippingAddress memberShippingAddress = iMemberShippingAddressService.getById(payBusinessGiftLog.getMemberShippingAddressId());
        //获取礼包
        MarketingBusinessGiftList marketingBusinessGiftList=this.getById(payBusinessGiftLog.getMarketingBusinessGiftListId());
        //生成创业礼包记录
        MarketingBusinessGiftRecord marketingBusinessGiftRecord=new MarketingBusinessGiftRecord();
        marketingBusinessGiftRecord.setMarketingBusinessGiftListId(marketingBusinessGiftList.getId());
        marketingBusinessGiftRecord.setSerialNumber(OrderNoUtils.getOrderNo());
        marketingBusinessGiftRecord.setMemberListId(payBusinessGiftLog.getMemberListId());
        marketingBusinessGiftRecord.setGiftName(marketingBusinessGiftList.getGiftName());
        marketingBusinessGiftRecord.setSalesPrice(marketingBusinessGiftList.getSalesPrice());
        marketingBusinessGiftRecord.setTMemberId(payBusinessGiftLog.getTMemberId());
        marketingBusinessGiftRecord.setReferralBonuses(marketingBusinessGiftList.getReferralBonuses());
        marketingBusinessGiftRecord.setPayTime(new Date());
        marketingBusinessGiftRecord.setTradeNo(payBusinessGiftLog.getId());
        marketingBusinessGiftRecord.setPayBusinessGiftLogId(payBusinessGiftLogId);
        if (memberShippingAddress!=null){
            marketingBusinessGiftRecord.setConsignee(memberShippingAddress.getLinkman());
            marketingBusinessGiftRecord.setContactNumber(memberShippingAddress.getPhone());
            marketingBusinessGiftRecord.setShippingAddress(memberShippingAddress.getAreaExplan()+memberShippingAddress.getAreaAddress());
            marketingBusinessGiftRecord.setSysAreaId(memberShippingAddress.getSysAreaId());
        }
        iMarketingBusinessGiftRecordService.save(marketingBusinessGiftRecord);


        //分配推荐奖励
        if(StringUtils.isNotBlank(payBusinessGiftLog.getTMemberId())) {
            //礼包推荐奖励
            if (!iMemberWelfarePaymentsService.addWelfarePayments(payBusinessGiftLog.getTMemberId(), marketingBusinessGiftList.getReferralBonuses(), "36", payBusinessGiftLog.getId(),"")) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        //分配团队
        //判断有没有推荐人
        MarketingBusinessGiftTeamRecord marketingBusinessGiftTeamRecord=null;
        if(StringUtils.isBlank(payBusinessGiftLog.getTMemberId())){
            //没有推荐人
            marketingBusinessGiftTeamRecord=marketingBusinessGiftTeamRecordService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftTeamRecord>()
                    .eq(MarketingBusinessGiftTeamRecord::getStatus,"0")
                    .orderByAsc(MarketingBusinessGiftTeamRecord::getCreateTime)
                    .last("limit 1"));
            if(marketingBusinessGiftTeamRecord==null){
                marketingBusinessGiftTeamRecord=new MarketingBusinessGiftTeamRecord();
                marketingBusinessGiftTeamRecord.setSerialNumber(OrderNoUtils.getOrderNo());
                marketingBusinessGiftTeamRecord.setMemberListId(payBusinessGiftLog.getMemberListId());
                marketingBusinessGiftTeamRecord.setMemberNumber(new BigDecimal(0));
                marketingBusinessGiftTeamRecord.setMarketingBusinessGiftListId(marketingBusinessGiftRecord.getId());
                if(marketingBusinessGiftTeamRecordService.save(marketingBusinessGiftTeamRecord)){
                    iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                            .setGrade(new BigDecimal(2))
                            .setMemberListId(payBusinessGiftLog.getMemberListId()));
                }else{
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }else{
                if(marketingBusinessGiftTeamRecord.getMemberNumber().intValue()<=2){
                    iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                            .setGrade(new BigDecimal(1))
                            .setMemberListId(payBusinessGiftLog.getMemberListId()));
                }else{
                    iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                            .setGrade(new BigDecimal(0))
                            .setMemberListId(payBusinessGiftLog.getMemberListId()));
                }
            }
        }else{
            //判断推荐人是否有在某个进行中的团里
            String marketingBusinessGiftTeamRecordId= null;
            Map<String,Object> recordMap=iMarketingBusinessGiftTeamListService.getRecordIdByMemberId(payBusinessGiftLog.getTMemberId());
            if(recordMap!=null){
                marketingBusinessGiftTeamRecordId=recordMap.get("id").toString();
            }
            if(StringUtils.isBlank(marketingBusinessGiftTeamRecordId)){
                marketingBusinessGiftTeamRecord=marketingBusinessGiftTeamRecordService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftTeamRecord>()
                        .eq(MarketingBusinessGiftTeamRecord::getStatus,"0")
                        .orderByAsc(MarketingBusinessGiftTeamRecord::getCreateTime)
                        .last("limit 1"));
                if(marketingBusinessGiftTeamRecord==null){
                    marketingBusinessGiftTeamRecord=new MarketingBusinessGiftTeamRecord();
                    marketingBusinessGiftTeamRecord.setSerialNumber(OrderNoUtils.getOrderNo());
                    marketingBusinessGiftTeamRecord.setMemberListId(payBusinessGiftLog.getMemberListId());
                    marketingBusinessGiftTeamRecord.setMemberNumber(new BigDecimal(0));
                    marketingBusinessGiftTeamRecord.setMarketingBusinessGiftListId(marketingBusinessGiftRecord.getId());
                    if(marketingBusinessGiftTeamRecordService.save(marketingBusinessGiftTeamRecord)){
                        iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                                .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                                .setGrade(new BigDecimal(2))
                                .setMemberListId(payBusinessGiftLog.getMemberListId()));
                    }else{
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }else{
                    if(marketingBusinessGiftTeamRecord.getMemberNumber().intValue()<=2){
                        iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                                .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                                .setGrade(new BigDecimal(1))
                                .setMemberListId(payBusinessGiftLog.getMemberListId()));
                    }else{
                        iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                                .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                                .setGrade(new BigDecimal(0))
                                .setMemberListId(payBusinessGiftLog.getMemberListId()));
                    }
                }
            }else{
                marketingBusinessGiftTeamRecord=marketingBusinessGiftTeamRecordService.getById(marketingBusinessGiftTeamRecordId);
                    if(marketingBusinessGiftTeamRecord.getMemberNumber().intValue()<=2){
                        iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                                .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                                .setGrade(new BigDecimal(1))
                                .setTMemberListId(payBusinessGiftLog.getTMemberId())
                                .setMemberListId(payBusinessGiftLog.getMemberListId()));
                    }else{
                        iMarketingBusinessGiftTeamListService.save(new MarketingBusinessGiftTeamList()
                                .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecord.getId())
                                .setGrade(new BigDecimal(0))
                                .setTMemberListId(payBusinessGiftLog.getTMemberId())
                                .setMemberListId(payBusinessGiftLog.getMemberListId()));
                    }
                    //推荐人的数量增加
                    MarketingBusinessGiftTeamList marketingBusinessGiftTeamList=iMarketingBusinessGiftTeamListService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftTeamList>()
                            .eq(MarketingBusinessGiftTeamList::getMarketingBusinessGiftTeamRecordId,marketingBusinessGiftTeamRecordId)
                            .eq(MarketingBusinessGiftTeamList::getMemberListId,payBusinessGiftLog.getTMemberId())
                            .last("limit 1"));
                    marketingBusinessGiftTeamList.setTestimonial(marketingBusinessGiftTeamList.getTestimonial().add(new BigDecimal(1)));
                    if(!iMarketingBusinessGiftTeamListService.saveOrUpdate(marketingBusinessGiftTeamList)){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                    //获取推荐奖励比例
                    MarketingBusinessGiftIdentity marketingBusinessGiftIdentity=iMarketingBusinessGiftIdentityService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftIdentity>().eq(MarketingBusinessGiftIdentity::getGrade,marketingBusinessGiftTeamList.getGrade()));
                    //发放推荐奖励
                if(marketingBusinessGiftIdentity!=null) {
                    if (!iMemberListService.addBlance(payBusinessGiftLog.getTMemberId(), marketingBusinessGiftList.getSalesPrice().multiply(marketingBusinessGiftIdentity.getReferralBonuses().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN), payBusinessGiftLog.getId(), "36")) {
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
                }

        }
        marketingBusinessGiftTeamRecord.setMemberNumber(marketingBusinessGiftTeamRecord.getMemberNumber().add(new BigDecimal(1)));
        if(marketingBusinessGiftTeamRecord.getMemberNumber().intValue()>=7){
            marketingBusinessGiftTeamRecord.setStatus("1");
            marketingBusinessGiftTeamRecord.setFunishTime(new Date());
            if(marketingBusinessGiftTeamRecordService.saveOrUpdate(marketingBusinessGiftTeamRecord)){
               //拆团
                List<MarketingBusinessGiftTeamList> marketingBusinessGiftTeamLists=iMarketingBusinessGiftTeamListService.list(new LambdaQueryWrapper<MarketingBusinessGiftTeamList>()
                        .eq(MarketingBusinessGiftTeamList::getMarketingBusinessGiftTeamRecordId,marketingBusinessGiftTeamRecord.getId())
                        .orderByDesc(MarketingBusinessGiftTeamList::getGrade));
                MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting=iMarketingBusinessGiftBaseSettingService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftBaseSetting>().eq(MarketingBusinessGiftBaseSetting::getStatus,"1"));
                if(marketingBusinessGiftBaseSetting!=null){
                    //业绩达标发放
                    MarketingBusinessGiftTeamList marketingBusinessGiftTeamList=marketingBusinessGiftTeamLists.get(0);
                    //业绩次数增加
                    MemberBusinessDesignation memberBusinessDesignationPro = iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                            .eq(MemberBusinessDesignation::getMemberListId, marketingBusinessGiftTeamList.getMemberListId())
                            .orderByDesc(MemberBusinessDesignation::getCreateTime)
                            .last("limit 1")
                    );
                    if(memberBusinessDesignationPro!=null){
                        memberBusinessDesignationPro.setCompletionTimes(memberBusinessDesignationPro.getCompletionTimes().add(new BigDecimal(1)));
                        if(!iMemberBusinessDesignationService.saveOrUpdate(memberBusinessDesignationPro)){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }
                    if(marketingBusinessGiftTeamList.getTestimonial().intValue()>=marketingBusinessGiftBaseSetting.getNumberStandard().intValue()){
                        MarketingBusinessGiftIdentity marketingBusinessGiftIdentity=iMarketingBusinessGiftIdentityService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftIdentity>().eq(MarketingBusinessGiftIdentity::getGrade,marketingBusinessGiftTeamList.getGrade()));
                        if (!iMemberListService.addBlance(marketingBusinessGiftTeamList.getMemberListId(), marketingBusinessGiftList.getSalesPrice().multiply(marketingBusinessGiftIdentity.getStandardReward().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN), payBusinessGiftLog.getId(), "37")) {
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }
                }
                MarketingBusinessGiftTeamRecord marketingBusinessGiftTeamRecordOne=new MarketingBusinessGiftTeamRecord();
                marketingBusinessGiftTeamRecordOne.setSerialNumber(OrderNoUtils.getOrderNo());
                marketingBusinessGiftTeamRecordOne.setMemberListId(marketingBusinessGiftTeamLists.get(1).getMemberListId());
                marketingBusinessGiftTeamRecordOne.setMemberNumber(new BigDecimal(3));
                marketingBusinessGiftTeamRecordOne.setMarketingBusinessGiftListId(payBusinessGiftLog.getMarketingBusinessGiftListId());
                if(marketingBusinessGiftTeamRecordService.save(marketingBusinessGiftTeamRecordOne)) {
                    iMarketingBusinessGiftTeamListService.save(marketingBusinessGiftTeamLists.get(1)
                            .setId(null)
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecordOne.getId())
                            .setGrade(new BigDecimal(2)));
                    iMarketingBusinessGiftTeamListService.save(marketingBusinessGiftTeamLists.get(3)
                            .setId(null)
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecordOne.getId())
                            .setGrade(new BigDecimal(1)));
                    iMarketingBusinessGiftTeamListService.save(marketingBusinessGiftTeamLists.get(4)
                            .setId(null)
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecordOne.getId())
                            .setGrade(new BigDecimal(1)));

                }else{
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }

                MarketingBusinessGiftTeamRecord marketingBusinessGiftTeamRecordTwo=new MarketingBusinessGiftTeamRecord();
                marketingBusinessGiftTeamRecordTwo.setSerialNumber(OrderNoUtils.getOrderNo());
                marketingBusinessGiftTeamRecordTwo.setMemberListId(marketingBusinessGiftTeamLists.get(2).getMemberListId());
                marketingBusinessGiftTeamRecordTwo.setMemberNumber(new BigDecimal(3));
                marketingBusinessGiftTeamRecordTwo.setMarketingBusinessGiftListId(payBusinessGiftLog.getMarketingBusinessGiftListId());

                if(marketingBusinessGiftTeamRecordService.save(marketingBusinessGiftTeamRecordTwo)) {
                    iMarketingBusinessGiftTeamListService.save(marketingBusinessGiftTeamLists.get(2)
                            .setId(null)
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecordTwo.getId())
                            .setGrade(new BigDecimal(2)));
                    iMarketingBusinessGiftTeamListService.save(marketingBusinessGiftTeamLists.get(5)
                            .setId(null)
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecordTwo.getId())
                            .setGrade(new BigDecimal(1)));
                    iMarketingBusinessGiftTeamListService.save(marketingBusinessGiftTeamLists.get(6)
                            .setId(null)
                            .setMarketingBusinessGiftTeamRecordId(marketingBusinessGiftTeamRecordTwo.getId())
                            .setGrade(new BigDecimal(1)));

                }else{
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }else{
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else{
            if(!marketingBusinessGiftTeamRecordService.saveOrUpdate(marketingBusinessGiftTeamRecord)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        //发放发展奖励
        if(marketingBusinessGiftTeamRecord.getMemberNumber().intValue()>3){
            //拆团
            List<MarketingBusinessGiftTeamList> marketingBusinessGiftTeamLists=iMarketingBusinessGiftTeamListService.list(new LambdaQueryWrapper<MarketingBusinessGiftTeamList>()
                    .eq(MarketingBusinessGiftTeamList::getMarketingBusinessGiftTeamRecordId,marketingBusinessGiftTeamRecord.getId())
                    .orderByDesc(MarketingBusinessGiftTeamList::getGrade));

            MarketingBusinessGiftTeamList marketingBusinessGiftTeamList=marketingBusinessGiftTeamLists.get(0);
                //发放发展奖励按照既定的比例发放
                MarketingBusinessGiftIdentity marketingBusinessGiftIdentity=iMarketingBusinessGiftIdentityService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftIdentity>().eq(MarketingBusinessGiftIdentity::getGrade,marketingBusinessGiftTeamList.getGrade()));
                //发放发展奖励按照既定的比例发放
                if(marketingBusinessGiftIdentity!=null) {
                    if (!iMemberListService.addBlance(marketingBusinessGiftTeamList.getMemberListId(), marketingBusinessGiftList.getSalesPrice().multiply(marketingBusinessGiftIdentity.getDevelopmentReward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN), payBusinessGiftLog.getId(), "38")) {
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            MarketingBusinessGiftTeamList marketingBusinessGiftTeamListTwo=marketingBusinessGiftTeamLists.get(1);
            //发放发展奖励按照既定的比例发放
            MarketingBusinessGiftIdentity marketingBusinessGiftIdentityTwo=iMarketingBusinessGiftIdentityService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftIdentity>().eq(MarketingBusinessGiftIdentity::getGrade,marketingBusinessGiftTeamListTwo.getGrade()));
            //发放发展奖励按照既定的比例发放
            if(marketingBusinessGiftIdentity!=null) {
                if (!iMemberListService.addBlance(marketingBusinessGiftTeamListTwo.getMemberListId(), marketingBusinessGiftList.getSalesPrice().multiply(marketingBusinessGiftIdentityTwo.getDevelopmentReward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN), payBusinessGiftLog.getId(), "38")) {
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            MarketingBusinessGiftTeamList marketingBusinessGiftTeamListThree=marketingBusinessGiftTeamLists.get(2);
            //发放发展奖励按照既定的比例发放
            MarketingBusinessGiftIdentity marketingBusinessGiftIdentityThree=iMarketingBusinessGiftIdentityService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftIdentity>().eq(MarketingBusinessGiftIdentity::getGrade,marketingBusinessGiftTeamListThree.getGrade()));
            //发放发展奖励按照既定的比例发放
            if(marketingBusinessGiftIdentity!=null) {
                if (!iMemberListService.addBlance(marketingBusinessGiftTeamListThree.getMemberListId(), marketingBusinessGiftList.getSalesPrice().multiply(marketingBusinessGiftIdentityThree.getDevelopmentReward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN), payBusinessGiftLog.getId(), "38")) {
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }


        //会员等级提升
        iMarketingBusinessDesignationService.upgrade(marketingBusinessGiftRecord.getMemberListId(),marketingBusinessGiftRecord.getTMemberId());


        //涨复投次数
        MemberBusinessDesignation memberBusinessDesignation = iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                .eq(MemberBusinessDesignation::getMemberListId, payBusinessGiftLog.getMemberListId())
                .orderByDesc(MemberBusinessDesignation::getCreateTime)
                .last("limit 1")
        );
        //获取称号资金池
        MarketingBusinessCapital marketingBusinessCapital = iMarketingBusinessCapitalService.getOne(new LambdaQueryWrapper<MarketingBusinessCapital>()
                .eq(MarketingBusinessCapital::getMarketingBusinessDesignationId, memberBusinessDesignation.getMarketingBusinessDesignationId())
                .orderByDesc(MarketingBusinessCapital::getCreateTime)
                .last("limit 1")
        );
        if(marketingBusinessCapital.getInvestmentAmount().doubleValue()!=0){
            memberBusinessDesignation.setAfterShots(memberBusinessDesignation.getAfterShots().add(marketingBusinessGiftRecord.getSalesPrice().divide(marketingBusinessCapital.getInvestmentAmount(),0, RoundingMode.DOWN)));
            if(!iMemberBusinessDesignationService.saveOrUpdate(memberBusinessDesignation)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        //根据资金配置进行资金分配
        iMarketingBusinessCapitalService.list(new LambdaQueryWrapper<MarketingBusinessCapital>().eq(MarketingBusinessCapital::getSessionControl,"1")).forEach(m->{
            if(StringUtils.contains(m.getWeeks(), DateUtils.getWeekOfDate(new Date()))){
                if (!iMarketingBusinessCapitalService.add(m.getId(), marketingBusinessGiftList.getBonusMoney(), "0", marketingBusinessGiftRecord.getSerialNumber())) {
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        });
    }
}
