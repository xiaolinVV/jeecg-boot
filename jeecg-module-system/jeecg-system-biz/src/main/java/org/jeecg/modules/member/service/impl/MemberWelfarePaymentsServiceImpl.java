package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.service.IAgencyRechargeRecordService;
import org.jeecg.modules.alliance.entity.AllianceAccountCapital;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.entity.AllianceRechargeRecord;
import org.jeecg.modules.alliance.service.IAllianceAccountCapitalService;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.modules.alliance.service.IAllianceRechargeRecordService;
import org.jeecg.modules.marketing.entity.MarketingWelfarePayments;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsService;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.mapper.MemberWelfarePaymentsMapper;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.vo.MemberWelfarePaymentsVO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.service.ISysAreaService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 会员福利金
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
@Service
@Slf4j
public class MemberWelfarePaymentsServiceImpl extends ServiceImpl<MemberWelfarePaymentsMapper, MemberWelfarePayments> implements IMemberWelfarePaymentsService {
    @Autowired
    @Lazy
    private IMemberListService iMemberListService;
    @Autowired
    @Lazy
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    @Lazy
    private IMarketingWelfarePaymentsService iMarketingWelfarePaymentsService;
    @Autowired
    @Lazy
    private IStoreManageService iStoreManageService;

    @Autowired
    @Lazy
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    @Lazy
    private IStoreAccountCapitalService iStoreAccountCapitalService;
    @Autowired
    @Lazy
    private ISysDictService iSysDictService;
    @Autowired
    @Lazy
    private IMemberRechargeRecordService iMemberRechargeRecordService;
    @Autowired
    @Lazy
    private IMemberAccountCapitalService iMemberAccountCapitalService;
    @Autowired
    @Lazy
    private IAgencyRechargeRecordService iAgencyRechargeRecordService;
    @Autowired
    @Lazy
    private IAgencyAccountCapitalService iAgencyAccountCapitalService;
    @Autowired
    @Lazy
    private IAllianceRechargeRecordService iAllianceRechargeRecordService;
    @Autowired
    @Lazy
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;
    @Autowired
    @Lazy
    private IMemberDistributionRecordService iMemberDistributionRecordService;
    @Autowired
    @Lazy
    private IAllianceManageService iAllianceManageService;
    @Autowired
    @Lazy
    private IAgencyManageService iAgencyManageService;
    @Autowired
    @Lazy
    private ISysAreaService iSysAreaService;
    @Override
    public IPage<Map<String, Object>> findMemberWelfarePaymentsByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findMemberWelfarePaymentsByMemberId(page,paramMap);
    }

    @Override
    public IPage<MemberWelfarePaymentsVO> findMemberWelfarePayments(Page<MemberWelfarePaymentsVO> page, MemberWelfarePaymentsVO memberWelfarePaymentsVO) {
        return baseMapper.findMemberWelfarePayments(page,memberWelfarePaymentsVO);
    }

    @Override
    public IPage<MemberWelfarePaymentsVO> findMemberPayRecord(Page<MemberWelfarePaymentsVO> page, MemberWelfarePaymentsVO memberWelfarePaymentsVO) {
        return baseMapper.findMemberPayRecord(page,memberWelfarePaymentsVO);
    }

    @Override
    public IPage<Map<String, Object>> findMemberWelfarePaymentsPageByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findMemberWelfarePaymentsPageByMemberId(page,paramObjectMap);
    }

    @Override
    @Transactional
    public void PresentedMemberWelfarePayments(MemberList memberList, String amont, String giveExplain, MemberList benefactor) {
        if (!iMemberListService.saveOrUpdate(benefactor
                .setWelfarePayments(benefactor.getWelfarePayments().subtract(new BigDecimal(amont))))){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments()
                .setDelFlag("0")
                .setMemberListId(benefactor.getId())
                .setSerialNumber(OrderNoUtils.getOrderNo())
                .setBargainPayments(new BigDecimal(amont))
                .setWelfarePayments(benefactor.getWelfarePayments())
                .setWeType("0")
                .setGoAndCome(memberList.getNickName()+"(" + memberList.getPhone()+")")
                .setBargainTime(new Date())
                .setOperator(benefactor.getNickName())
                .setIsPlatform("2")
                .setIsFreeze("0")
                .setTradeNo(benefactor.getId())
                .setTradeType("5")
                .setTradeStatus("5");
        memberWelfarePayments.setWpExplain("赠送好友[" + memberWelfarePayments.getSerialNumber()+"]"+giveExplain);
        boolean b = iMemberWelfarePaymentsService.save(memberWelfarePayments);
        if (!b){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        //赠送福利金
        boolean update = iMemberListService.saveOrUpdate(memberList
                .setWelfarePayments(memberList.getWelfarePayments().add(new BigDecimal(amont))));
        if (!update){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        //生成获赠记录
        boolean save = iMemberWelfarePaymentsService.save(new MemberWelfarePayments()
                .setDelFlag("0")
                .setMemberListId(memberList.getId())
                .setSerialNumber(OrderNoUtils.getOrderNo())
                .setBargainPayments(new BigDecimal(amont))
                .setWelfarePayments(memberList.getWelfarePayments())
                .setWeType("1")
                .setWpExplain("好友赠送[" + memberWelfarePayments.getSerialNumber() + "]" + giveExplain)
                .setGoAndCome(benefactor.getNickName() + "(" + benefactor.getPhone() + ")")
                .setBargainTime(new Date())
                .setOperator(benefactor.getNickName())
                .setIsPlatform("2")
                .setIsFreeze("0")
                .setTradeNo(memberWelfarePayments.getSerialNumber())
                .setTradeType("4")
                .setTradeStatus("5")
        );
        if (!save){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public IPage<Map<String, Object>> findfrozenAandUnusableById(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findfrozenAandUnusableById(page,paramObjectMap);
    }

    @Override
    @Transactional
    public void postWelfarePayment(MemberList memberList, BigDecimal bigDecimal, StoreManage storeManage, String giveExplain, BigDecimal proportion) {

        if (storeManage.getWelfarePayments().subtract(bigDecimal).doubleValue()<0){
            BigDecimal b = bigDecimal.subtract(storeManage.getWelfarePayments()).multiply(proportion.divide(new BigDecimal(100)));
            if (storeManage.getBalance()
                    .subtract(b)
                    .doubleValue()>= 0){
                MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
                if (storeManage.getWelfarePayments().doubleValue()>0){
                    marketingWelfarePayments.setWelfarePay(storeManage.getWelfarePayments());//福利金支付
                    marketingWelfarePayments.setPayMode("3");//支付方式
                }else {
                    marketingWelfarePayments.setWelfarePay(new BigDecimal(0));//福利金支付
                    marketingWelfarePayments.setPayMode("1");//支付方式
                }
                iStoreManageService.saveOrUpdate(storeManage
                        .setBalance(storeManage.getBalance()
                                .subtract(b))
                        .setWelfarePayments(new BigDecimal(0)));

                iMemberListService.saveOrUpdate(memberList
                        .setWelfarePayments(memberList.getWelfarePayments().add(bigDecimal)));

                marketingWelfarePayments.setDelFlag("0");//删除状态
                marketingWelfarePayments.setSysUserId(storeManage.getSysUserId());//店铺id
                marketingWelfarePayments.setMemberListId(memberList.getId());//会员id
                marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                marketingWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
                marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());//账户福利金
                marketingWelfarePayments.setWeType("0");//类型
                marketingWelfarePayments.setGiveExplain("店铺赠送会员["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//赠送说明
                marketingWelfarePayments.setGoAndCome(memberList.getPhone());//来源于去向
                marketingWelfarePayments.setBargainTime(new Date());//交易时间
                marketingWelfarePayments.setOperator(storeManage.getBossName());//操作人
                marketingWelfarePayments.setBalance(storeManage.getBalance());//店铺账户余额

                marketingWelfarePayments.setBalancePay(b);//余额支付
                marketingWelfarePayments.setStatus("1");//支付状态

                marketingWelfarePayments.setIsPlatform("0");//0,店铺; 1,平台
                marketingWelfarePayments.setUserType("0");//用户类型
                marketingWelfarePayments.setSendUser(storeManage.getStoreName());//赠送人
                marketingWelfarePayments.setTradeType("3");
                iMarketingWelfarePaymentsService.save(marketingWelfarePayments);
                MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
                memberWelfarePayments.setDelFlag("0");
                memberWelfarePayments.setMemberListId(memberList.getId());//会员id
                memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                memberWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
                memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
                memberWelfarePayments.setWeType("1");//交易类型: 0:支出 1:收入
                memberWelfarePayments.setWpExplain("店铺赠送["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//说明
                if (StringUtils.isBlank(storeManage.getSubStoreName())){
                    memberWelfarePayments.setGoAndCome(storeManage.getStoreName());//来源或者去向
                }else {
                    memberWelfarePayments.setGoAndCome(storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
                }
                memberWelfarePayments.setBargainTime(new Date());//交易时间
                memberWelfarePayments.setOperator(storeManage.getBossName());//操作人
                memberWelfarePayments.setIsPlatform("0");
                memberWelfarePayments.setIsFreeze("0");
                memberWelfarePayments.setTradeNo(marketingWelfarePayments.getSerialNumber());
                memberWelfarePayments.setTradeType("3");
                memberWelfarePayments.setTradeStatus("5");
                iMemberWelfarePaymentsService.save(memberWelfarePayments);
                //店铺余额发生变动写入店铺余额记录中
                StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                storeRechargeRecord.setDelFlag("0");//删除状态
                storeRechargeRecord.setStoreManageId(storeManage.getId());//店铺id
                storeRechargeRecord.setPayType("3");//交易类型
                storeRechargeRecord.setGoAndCome("1");//支出
                storeRechargeRecord.setAmount(b);//交易金额
                storeRechargeRecord.setTradeStatus("5");//交易状态
                storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());//单号
                storeRechargeRecord.setOperator(storeManage.getBossName());//操作人
                storeRechargeRecord.setRemark("店铺赠送福利金"+"["+marketingWelfarePayments.getSerialNumber()+"]");//备注
                storeRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                iStoreRechargeRecordService.save(storeRechargeRecord);
                marketingWelfarePayments.setTradeNo(storeRechargeRecord.getOrderNo());
                    //记录店铺资金记录表中
                    StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                    storeAccountCapital.setDelFlag("0");//删除状态
                    storeAccountCapital.setStoreManageId(storeManage.getId());//店铺id
                    storeAccountCapital.setPayType("3");//交易类型
                    storeAccountCapital.setGoAndCome("1");//支出
                    storeAccountCapital.setAmount(b);//交易金额
                    storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());//单号
                    storeAccountCapital.setBalance(storeManage.getBalance());//账户余额
                    iStoreAccountCapitalService.save(storeAccountCapital);
                    fundAllot(storeManage,b,marketingWelfarePayments);
            }
        }else {
            iStoreManageService.saveOrUpdate(storeManage
                    .setWelfarePayments(storeManage.getWelfarePayments().subtract(bigDecimal)));

            iMemberListService.saveOrUpdate(memberList
                    .setWelfarePayments(memberList.getWelfarePayments().add(bigDecimal)));
            MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
            marketingWelfarePayments.setDelFlag("0");//删除状态
            marketingWelfarePayments.setSysUserId(storeManage.getSysUserId());//店铺id
            marketingWelfarePayments.setMemberListId(memberList.getId());//会员id
            marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            marketingWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
            marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());//账户福利金
            marketingWelfarePayments.setWeType("0");//类型
            marketingWelfarePayments.setGiveExplain("店铺赠送会员["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//赠送说明
            marketingWelfarePayments.setGoAndCome(memberList.getPhone());//来源于去向
            marketingWelfarePayments.setBargainTime(new Date());//交易时间
            marketingWelfarePayments.setOperator(storeManage.getBossName());//操作人
            marketingWelfarePayments.setBalance(storeManage.getBalance());//店铺账户余额
            marketingWelfarePayments.setWelfarePay(bigDecimal);//福利金支付
            marketingWelfarePayments.setBalancePay(new BigDecimal(0));//余额支付
            marketingWelfarePayments.setStatus("1");//支付状态
            marketingWelfarePayments.setPayMode("2");//支付方式
            marketingWelfarePayments.setIsPlatform("0");//0,店铺; 1,平台
            marketingWelfarePayments.setUserType("0");//用户类型
            marketingWelfarePayments.setSendUser(storeManage.getStoreName());//赠送人
            marketingWelfarePayments.setTradeType("3");
            iMarketingWelfarePaymentsService.save(marketingWelfarePayments);
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
            memberWelfarePayments.setDelFlag("0");
            memberWelfarePayments.setMemberListId(memberList.getId());//会员id
            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("1");//交易类型: 0:支出 1:收入
            memberWelfarePayments.setWpExplain("店铺赠送["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//说明
            if (StringUtils.isBlank(storeManage.getSubStoreName())){
                memberWelfarePayments.setGoAndCome(storeManage.getStoreName());//来源或者去向
            }else {
                memberWelfarePayments.setGoAndCome(storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
            }
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator(storeManage.getBossName());//操作人
            memberWelfarePayments.setIsPlatform("0");
            memberWelfarePayments.setIsFreeze("0");
            memberWelfarePayments.setTradeNo(marketingWelfarePayments.getSerialNumber());
            memberWelfarePayments.setTradeType("3");
            memberWelfarePayments.setTradeStatus("5");
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
        }
    }
    private void fundAllot(StoreManage storeManage,BigDecimal b,MarketingWelfarePayments marketingWelfarePayments){
        //店铺余额充值福利金的利润分配
        //推荐开店奖励百分比
        String welfareSalesReward = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "welfare_sales_reward"), "%");

        //福利金推荐奖励类型：0：仅店铺；1：仅会员；2：店铺和会员；
        String welfareSalesRewardType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "welfare_sales_reward_type");
        //推荐人奖励
        if (new BigDecimal(welfareSalesReward).doubleValue() > 0 &&storeManage!=null&& StringUtils.isNotBlank(storeManage.getPromoterType())) {
            //店铺
            if (storeManage.getPromoterType().equals("0")) {
                if (welfareSalesRewardType.equals("0")||welfareSalesRewardType.equals("2")){
                    QueryWrapper<StoreManage> storeManageQueryWrapper1 = new QueryWrapper<>();
                    storeManageQueryWrapper1.eq("sys_user_id", storeManage.getPromoter());
                    storeManageQueryWrapper1.in("pay_status", "1","2");
                    if(iStoreManageService.count(storeManageQueryWrapper1)>0) {
                        StoreManage promoterStoreManage = iStoreManageService.list(storeManageQueryWrapper1).get(0);
                        //店铺余额明细
                        StoreRechargeRecord storeRechargeRecordw = new StoreRechargeRecord();
                        storeRechargeRecordw.setStoreManageId(promoterStoreManage.getId());
                        storeRechargeRecordw.setPayType("9");
                        storeRechargeRecordw.setGoAndCome("0");
                        storeRechargeRecordw.setAmount(b.multiply(new BigDecimal(welfareSalesReward).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                        storeRechargeRecordw.setTradeStatus("5");
                        storeRechargeRecordw.setOrderNo(OrderNoUtils.getOrderNo());
                        storeRechargeRecordw.setOperator("系统");
                        storeRechargeRecordw.setRemark("福利金销售奖励[" + marketingWelfarePayments.getSerialNumber()+"]");
                        storeRechargeRecordw.setTradeType("3");
                        storeRechargeRecordw.setTradeNo(marketingWelfarePayments.getSerialNumber());
                        iStoreRechargeRecordService.save(storeRechargeRecordw);


                        promoterStoreManage.setBalance(promoterStoreManage.getBalance().add(storeRechargeRecordw.getAmount()));
                        iStoreManageService.saveOrUpdate(promoterStoreManage);

                        //店铺资金流水
                        StoreAccountCapital storeAccountCapitalw = new StoreAccountCapital();
                        storeAccountCapitalw.setStoreManageId(promoterStoreManage.getId());
                        storeAccountCapitalw.setPayType("9");
                        storeAccountCapitalw.setGoAndCome("0");
                        storeAccountCapitalw.setAmount(storeRechargeRecordw.getAmount());
                        storeAccountCapitalw.setOrderNo(storeRechargeRecordw.getOrderNo());
                        storeAccountCapitalw.setBalance(promoterStoreManage.getBalance());
                        iStoreAccountCapitalService.save(storeAccountCapitalw);
                        log.info("推荐福利金奖励：" + storeRechargeRecordw.getAmount() + "---店铺金额：" + promoterStoreManage.getBalance() + "--推荐店铺：" + promoterStoreManage.getStoreName());
                    }
                }
            }
            //会员
            if (storeManage.getPromoterType().equals("1")) {
                if (welfareSalesRewardType.equals("1")||welfareSalesRewardType.equals("2")){
                    MemberList proterMemberList = iMemberListService.getById(storeManage.getPromoter());
                    if (oConvertUtils.isNotEmpty(proterMemberList)){
                        //会员余额明细
                        MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                        memberRechargeRecord.setMemberListId(proterMemberList.getId());
                        memberRechargeRecord.setPayType("6");
                        memberRechargeRecord.setGoAndCome("0");
                        memberRechargeRecord.setAmount(b.multiply(new BigDecimal(welfareSalesReward).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                        memberRechargeRecord.setTradeStatus("5");
                        memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                        memberRechargeRecord.setOperator("系统");
                        memberRechargeRecord.setRemark("福利金销售奖励[" + marketingWelfarePayments.getSerialNumber()+"]");
                        memberRechargeRecord.setTMemberListId(storeManage.getMemberListId());
                        memberRechargeRecord.setTradeType("3");
                        memberRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                        iMemberRechargeRecordService.save(memberRechargeRecord);

                        MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                        memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                        memberDistributionRecord.setGoodPicture(storeManage.getLogoAddr());
                        memberDistributionRecord.setGoodName(storeManage.getStoreName());
                        memberDistributionRecord.setGoodSpecification("-");
                        memberDistributionRecord.setCommission(memberRechargeRecord.getAmount());
                        iMemberDistributionRecordService.save(memberDistributionRecord);

                        proterMemberList.setBalance(proterMemberList.getBalance().add(memberRechargeRecord.getAmount()));
                        if (proterMemberList.getTotalCommission() == null) {
                            proterMemberList.setTotalCommission(new BigDecimal(0));
                        }
                        proterMemberList.setTotalCommission(proterMemberList.getTotalCommission().add(memberRechargeRecord.getAmount()));
                        iMemberListService.saveOrUpdate(proterMemberList);

                        //会员资金流水
                        MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
                        memberAccountCapital.setMemberListId(proterMemberList.getId());
                        memberAccountCapital.setPayType("6");
                        memberAccountCapital.setGoAndCome("0");
                        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                        memberAccountCapital.setBalance(proterMemberList.getBalance());
                        iMemberAccountCapitalService.save(memberAccountCapital);
                        log.info("推荐福利金奖励：" + memberRechargeRecord.getAmount() + "---会员金额：" + proterMemberList.getBalance() + "--推荐会员：" + proterMemberList.getNickName());
                    }

                }
            }
        }

        //加盟商资金设置

        boolean isExecute=true;

        //加盟商

        if(storeManage.getPromoterType().equals("3")||StringUtils.isNotBlank(storeManage.getAllianceUserId())){
            //处理加盟商业务
            QueryWrapper<AllianceManage> allianceManageQueryWrapper=new QueryWrapper<>();
            allianceManageQueryWrapper.eq("sys_user_id",storeManage.getAllianceUserId());
            AllianceManage allianceManage= iAllianceManageService.list(allianceManageQueryWrapper).get(0);
            //独享控制
            if(allianceManage.getProfitType().equals("0")){
                isExecute=false;
            }
            if (allianceManage != null && allianceManage.getWelfareCommissionRate().doubleValue() > 0&&allianceManage.getStatus().equals("1")) {
                //代理余额明细
                AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                allianceRechargeRecord.setPayType("5");
                allianceRechargeRecord.setGoAndCome("0");
                if (allianceManage.getProfitType().equals("0")){
                    allianceRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().divide(new BigDecimal(100)))
                            .setScale(2,BigDecimal.ROUND_DOWN));
                }else {
                    allianceRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(100)))
                            .setScale(2,BigDecimal.ROUND_DOWN));
                }
                allianceRechargeRecord.setTradeStatus("5");
                allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                allianceRechargeRecord.setTradeType("3");
                allianceRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                allianceRechargeRecord.setRemark("福利金销售奖励["+marketingWelfarePayments.getSerialNumber()+"]");
                iAllianceRechargeRecordService.save(allianceRechargeRecord);

                allianceManage.setBalance(allianceManage.getBalance().add(allianceRechargeRecord.getAmount()));
                iAllianceManageService.saveOrUpdate(allianceManage);

                //代理资金明细
                AllianceAccountCapital allianceAccountCapital = new AllianceAccountCapital();
                allianceAccountCapital.setSysUserId(allianceManage.getSysUserId());
                allianceAccountCapital.setPayType("5");
                allianceAccountCapital.setGoAndCome("0");
                allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                allianceAccountCapital.setBalance(allianceManage.getBalance());
                iAllianceAccountCapitalService.save(allianceAccountCapital);

                log.info("推荐福利金奖励：" + allianceRechargeRecord.getAmount() + "---代理金额：" + allianceManage.getBalance() + "--推荐加盟商：" + allianceManage.getId());
            }
        }



        //代理奖励
        //代理资金分配

        //查询地址确定代理位置

        if(isExecute) {

            if (storeManage.getLatitude().doubleValue() > 0 && storeManage.getLongitude().doubleValue() > 0) {
                List<String> sysAreas = Arrays.asList(StringUtils.split(storeManage.getSysAreaId(), ","));
                //代理余额明细
                //代理资金明细
                sysAreas.forEach(sid -> {
                    SysArea sysArea1 = iSysAreaService.getById(sid);
                    if(sysArea1.getLeve()==2&&StringUtils.isNotBlank(storeManage.getAllianceUserId())){
                        AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                                .eq(AllianceManage::getDelFlag,"0")
                                .eq(AllianceManage::getStatus,"1")
                                .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                        if (oConvertUtils.isNotEmpty(allianceManage)){
                            if (allianceManage.getMutualAdvantages().equals("0")){
                                if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                    AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                    if (agencyManage != null && agencyManage.getWelfareCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                        //代理余额明细
                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                        agencyRechargeRecord.setPayType("5");
                                        agencyRechargeRecord.setGoAndCome("0");
                                        agencyRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100)))
                                                .setScale(2, BigDecimal.ROUND_DOWN));
                                        agencyRechargeRecord.setTradeStatus("5");
                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        agencyRechargeRecord.setTradeType("3");
                                        agencyRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                                        agencyRechargeRecord.setRemark("福利金销售奖励["+marketingWelfarePayments.getSerialNumber()+"]");
                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                        //代理资金明细
                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                        agencyAccountCapital.setPayType("5");
                                        agencyAccountCapital.setGoAndCome("0");
                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                        log.info("福利金销售奖励：" + agencyRechargeRecord.getAmount() + "---代理金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                    }
                                }
                            }else {
                                if (StringUtils.isNotBlank(allianceManage.getCountyId())){
                                    SysArea area = iSysAreaService.getById(allianceManage.getCountyId());
                                    if (StringUtils.isNotBlank(area.getAgencyManageId())) {
                                        AgencyManage agencyManage = iAgencyManageService.getById(area.getAgencyManageId());
                                        if (agencyManage != null && agencyManage.getWelfareCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                            //代理余额明细
                                            AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                            agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                            agencyRechargeRecord.setPayType("5");
                                            agencyRechargeRecord.setGoAndCome("0");
                                            agencyRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100)))
                                                    .setScale(2, BigDecimal.ROUND_DOWN));
                                            agencyRechargeRecord.setTradeStatus("5");
                                            agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                            agencyRechargeRecord.setTradeType("3");
                                            agencyRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                                            agencyRechargeRecord.setRemark("福利金销售奖励["+marketingWelfarePayments.getSerialNumber()+"]");
                                            iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                            agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                            iAgencyManageService.saveOrUpdate(agencyManage);

                                            //代理资金明细
                                            AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                            agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                            agencyAccountCapital.setPayType("5");
                                            agencyAccountCapital.setGoAndCome("0");
                                            agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                            agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                            agencyAccountCapital.setBalance(agencyManage.getBalance());
                                            iAgencyAccountCapitalService.save(agencyAccountCapital);

                                            log.info("福利金销售奖励：" + agencyRechargeRecord.getAmount() + "---代理金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                            AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                            if (agencyManage != null && agencyManage.getWelfareCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                //代理余额明细
                                AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                agencyRechargeRecord.setPayType("5");
                                agencyRechargeRecord.setGoAndCome("0");
                                agencyRechargeRecord.setAmount(b.multiply(agencyManage.getWelfareCommissionRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                agencyRechargeRecord.setTradeStatus("5");
                                agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                agencyRechargeRecord.setTradeType("3");
                                agencyRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                                agencyRechargeRecord.setRemark("福利金销售奖励["+marketingWelfarePayments.getSerialNumber()+"]");
                                iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                iAgencyManageService.saveOrUpdate(agencyManage);

                                //代理资金明细
                                AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                agencyAccountCapital.setPayType("5");
                                agencyAccountCapital.setGoAndCome("0");
                                agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                agencyAccountCapital.setBalance(agencyManage.getBalance());
                                iAgencyAccountCapitalService.save(agencyAccountCapital);

                                log.info("福利金销售奖励：" + agencyRechargeRecord.getAmount() + "---代理金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                            }
                        }
                    }
                });
            }

        }
    }

    /*sysAreas.stream()
            .map(sid -> iSysAreaService.getById(sid))
            .filter(sysArea1 -> StringUtils.isNotBlank(sysArea1.getAgencyManageId()))
            .map(sysArea1 -> iAgencyManageService.getById(sysArea1.getAgencyManageId()))
            .filter(agencyManage -> agencyManage != null && agencyManage.getWelfareCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1"))
            .forEach(agencyManage -> {
        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
        agencyRechargeRecord.setPayType("5");
        agencyRechargeRecord.setGoAndCome("0");
        agencyRechargeRecord.setAmount(b.multiply(agencyManage.getWelfareCommissionRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
        agencyRechargeRecord.setTradeStatus("5");
        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
        agencyRechargeRecord.setTradeType("3");
        agencyRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
        agencyRechargeRecord.setRemark("福利金销售奖励[" + marketingWelfarePayments.getSerialNumber() + "]");
        iAgencyRechargeRecordService.save(agencyRechargeRecord);
        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
        iAgencyManageService.saveOrUpdate(agencyManage);
        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
        agencyAccountCapital.setPayType("5");
        agencyAccountCapital.setGoAndCome("0");
        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
        agencyAccountCapital.setBalance(agencyManage.getBalance());
        iAgencyAccountCapitalService.save(agencyAccountCapital);
        log.info("推荐福利金奖励：" + agencyRechargeRecord.getAmount() + "---代理金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
    });*/
    @Override
    @Transactional
    public void storeGiveWelfarePayment(MemberList memberList, BigDecimal bigDecimal, StoreManage storeManage, BigDecimal proportion) {

        if (storeManage.getWelfarePayments().subtract(bigDecimal).doubleValue()<0){
            BigDecimal b = bigDecimal.subtract(storeManage.getWelfarePayments()).multiply(proportion.divide(new BigDecimal(100)));
            if (storeManage.getBalance()
                    .subtract(b)
                    .doubleValue()>= 0){
                MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
                if (storeManage.getWelfarePayments().doubleValue()>0){
                    marketingWelfarePayments.setWelfarePay(storeManage.getWelfarePayments());//福利金支付
                    marketingWelfarePayments.setPayMode("3");//支付方式
                }else {
                    marketingWelfarePayments.setWelfarePay(new BigDecimal(0));//福利金支付
                    marketingWelfarePayments.setPayMode("1");//支付方式
                }
                iStoreManageService.saveOrUpdate(storeManage
                        .setBalance(storeManage.getBalance()
                                .subtract(b))
                        .setWelfarePayments(new BigDecimal(0)));

                iMemberListService.saveOrUpdate(memberList
                        .setWelfarePayments(memberList.getWelfarePayments().add(bigDecimal)));

                marketingWelfarePayments.setDelFlag("0");//删除状态
                marketingWelfarePayments.setSysUserId(storeManage.getSysUserId());//店铺id
                marketingWelfarePayments.setMemberListId(memberList.getId());//会员id
                marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                marketingWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
                marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());//账户福利金
                marketingWelfarePayments.setWeType("0");//类型
                marketingWelfarePayments.setGiveExplain("店铺赠送会员["+marketingWelfarePayments.getSerialNumber()+"]");//赠送说明
                marketingWelfarePayments.setGoAndCome(memberList.getPhone());//来源于去向
                marketingWelfarePayments.setBargainTime(new Date());//交易时间
                marketingWelfarePayments.setOperator(storeManage.getBossName());//操作人
                marketingWelfarePayments.setBalance(storeManage.getBalance());//店铺账户余额

                marketingWelfarePayments.setBalancePay(b);//余额支付
                marketingWelfarePayments.setStatus("1");//支付状态

                marketingWelfarePayments.setIsPlatform("0");//0,店铺; 1,平台
                marketingWelfarePayments.setUserType("0");//用户类型
                marketingWelfarePayments.setSendUser(storeManage.getStoreName());//赠送人
                marketingWelfarePayments.setTradeType("7");
                iMarketingWelfarePaymentsService.save(marketingWelfarePayments);
                MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
                memberWelfarePayments.setDelFlag("0");
                memberWelfarePayments.setMemberListId(memberList.getId());//会员id
                memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                memberWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
                memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
                memberWelfarePayments.setWeType("1");//交易类型: 0:支出 1:收入
                memberWelfarePayments.setWpExplain("店铺赠送["+marketingWelfarePayments.getSerialNumber()+"]");//说明
                if (StringUtils.isBlank(storeManage.getSubStoreName())){
                    memberWelfarePayments.setGoAndCome(storeManage.getStoreName());//来源或者去向
                }else {
                    memberWelfarePayments.setGoAndCome(storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
                }
                memberWelfarePayments.setBargainTime(new Date());//交易时间
                memberWelfarePayments.setOperator(storeManage.getBossName());//操作人
                memberWelfarePayments.setIsPlatform("0");
                memberWelfarePayments.setIsFreeze("0");
                memberWelfarePayments.setTradeNo(marketingWelfarePayments.getSerialNumber());
                memberWelfarePayments.setTradeType("8");
                memberWelfarePayments.setTradeStatus("5");
                iMemberWelfarePaymentsService.save(memberWelfarePayments);
                //店铺余额发生变动写入店铺余额记录中
                StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                storeRechargeRecord.setDelFlag("0");//删除状态
                storeRechargeRecord.setStoreManageId(storeManage.getId());//店铺id
                storeRechargeRecord.setPayType("3");//交易类型
                storeRechargeRecord.setGoAndCome("1");//支出
                storeRechargeRecord.setAmount(b);//交易金额
                storeRechargeRecord.setTradeStatus("5");//交易状态
                storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());//单号
                storeRechargeRecord.setOperator(storeManage.getBossName());//操作人
                storeRechargeRecord.setRemark("店铺赠送福利金"+"["+marketingWelfarePayments.getSerialNumber()+"]");//备注
                storeRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                iStoreRechargeRecordService.save(storeRechargeRecord);
                marketingWelfarePayments.setTradeNo(storeRechargeRecord.getOrderNo());
                //记录店铺资金记录表中
                StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                storeAccountCapital.setDelFlag("0");//删除状态
                storeAccountCapital.setStoreManageId(storeManage.getId());//店铺id
                storeAccountCapital.setPayType("3");//交易类型
                storeAccountCapital.setGoAndCome("1");//支出
                storeAccountCapital.setAmount(b);//交易金额
                storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());//单号
                storeAccountCapital.setBalance(storeManage.getBalance());//账户余额
                iStoreAccountCapitalService.save(storeAccountCapital);
                fundAllot(storeManage,b,marketingWelfarePayments);
            }
        }else {
            iStoreManageService.saveOrUpdate(storeManage
                    .setWelfarePayments(storeManage.getWelfarePayments().subtract(bigDecimal)));

            iMemberListService.saveOrUpdate(memberList
                    .setWelfarePayments(memberList.getWelfarePayments().add(bigDecimal)));
            MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
            marketingWelfarePayments.setDelFlag("0");//删除状态
            marketingWelfarePayments.setSysUserId(storeManage.getSysUserId());//店铺id
            marketingWelfarePayments.setMemberListId(memberList.getId());//会员id
            marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            marketingWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
            marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());//账户福利金
            marketingWelfarePayments.setWeType("0");//类型
            marketingWelfarePayments.setGiveExplain("店铺赠送会员["+marketingWelfarePayments.getSerialNumber()+"]");//赠送说明
            marketingWelfarePayments.setGoAndCome(memberList.getPhone());//来源于去向
            marketingWelfarePayments.setBargainTime(new Date());//交易时间
            marketingWelfarePayments.setOperator(storeManage.getBossName());//操作人
            marketingWelfarePayments.setBalance(storeManage.getBalance());//店铺账户余额
            marketingWelfarePayments.setWelfarePay(bigDecimal);//福利金支付
            marketingWelfarePayments.setBalancePay(new BigDecimal(0));//余额支付
            marketingWelfarePayments.setStatus("1");//支付状态
            marketingWelfarePayments.setPayMode("2");//支付方式
            marketingWelfarePayments.setIsPlatform("0");//0,店铺; 1,平台
            marketingWelfarePayments.setUserType("0");//用户类型
            marketingWelfarePayments.setSendUser(storeManage.getStoreName());//赠送人
            marketingWelfarePayments.setTradeType("7");
            iMarketingWelfarePaymentsService.save(marketingWelfarePayments);
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
            memberWelfarePayments.setDelFlag("0");
            memberWelfarePayments.setMemberListId(memberList.getId());//会员id
            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(bigDecimal);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("1");//交易类型: 0:支出 1:收入
            memberWelfarePayments.setWpExplain("店铺赠送["+marketingWelfarePayments.getSerialNumber()+"]");//说明
            if (StringUtils.isBlank(storeManage.getSubStoreName())){
                memberWelfarePayments.setGoAndCome(storeManage.getStoreName());//来源或者去向
            }else {
                memberWelfarePayments.setGoAndCome(storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
            }
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator(storeManage.getBossName());//操作人
            memberWelfarePayments.setIsPlatform("0");
            memberWelfarePayments.setIsFreeze("0");
            memberWelfarePayments.setTradeNo(marketingWelfarePayments.getSerialNumber());
            memberWelfarePayments.setTradeType("8");
            memberWelfarePayments.setTradeStatus("5");
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
        }
    }

    @Override
    public List<MemberWelfarePayments> getAbnormalWelfarePam(HashMap<String, Object> map) {
        return baseMapper.getAbnormalWelfarePam(map);
    }

    @Override
    @Transactional
    public boolean addWelfarePayments(String membeId, BigDecimal welfarePayments, String tradeType,String tradeNo,String remark) {
        if(welfarePayments.doubleValue()>0) {
            log.info("会员积分增加，会员id："+membeId+";数量："+welfarePayments+";类型："+tradeType+";单号："+tradeNo);
            //加入用户积分
            MemberList memberList = iMemberListService.getById(membeId);
            memberList.setWelfarePayments(memberList.getWelfarePayments().add(welfarePayments));

            //生成积分记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();

            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("1");//类型；0：支出；1：收入
            String dictTextByKey = iSysDictService.queryDictTextByKey("member_welfare_deal_type", tradeType);
            if (StringUtils.isBlank(dictTextByKey)){
                memberWelfarePayments.setWpExplain("积分转入[" + tradeNo + "]");//说明
            }else {
                memberWelfarePayments.setWpExplain(dictTextByKey+"[" + tradeNo + "]");//说明
            }
            memberWelfarePayments.setGoAndCome("平台");//来源或者去向
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator("系统");//操作人
            memberWelfarePayments.setRemark(remark);
            memberWelfarePayments.setMemberListId(memberList.getId());//会员列表id
            memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
            memberWelfarePayments.setTradeNo(tradeNo);//交易订单号
            memberWelfarePayments.setIsFreeze("0");//是否冻结福利金，0:不是；1：是冻结；2：不可用
            //会员福利金交易类型；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：积分付款；8：进店奖励；9：参团支付；10：参团奖励；11：转入；12：转出；13：挂卖；14：转出失败退款；15：未成团退款；16：取消挂卖退款；17：订单取消；18：水滴兑换；19：消费奖励；20：订单退款；
            memberWelfarePayments.setTradeType(tradeType);
            memberWelfarePayments.setTradeStatus("5");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
            return iMemberListService.saveOrUpdate(memberList);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean subtractWelfarePayments(String membeId, BigDecimal welfarePayments, String tradeType,String tradeNo,String remark) {
        if(welfarePayments.doubleValue()>0) {
            //加入用户积分
            MemberList memberList = iMemberListService.getById(membeId);
            memberList.setWelfarePayments(memberList.getWelfarePayments().subtract(welfarePayments));
            if(memberList.getWelfarePayments().doubleValue()<0){
                return false;
            }
            //生成积分记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("0");//类型；0：支出；1：收入
            String dictTextByKey = iSysDictService.queryDictTextByKey("member_welfare_deal_type", tradeType);
            if (StringUtils.isBlank(dictTextByKey)){
                memberWelfarePayments.setWpExplain("积分转出[" + tradeNo + "]");//说明
            }else {
                memberWelfarePayments.setWpExplain(dictTextByKey+"[" + tradeNo + "]");//说明
            }
            memberWelfarePayments.setGoAndCome("平台");//来源或者去向
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator("系统");//操作人
            memberWelfarePayments.setRemark(remark);
            memberWelfarePayments.setMemberListId(memberList.getId());//会员列表id
            memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
            memberWelfarePayments.setTradeNo(tradeNo);//交易订单号
            memberWelfarePayments.setIsFreeze("0");//是否冻结福利金，0:不是；1：是冻结；2：不可用
            //会员福利金交易类型；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：积分付款；8：进店奖励；9：参团支付；10：参团奖励；11：转入；12：转出；13：挂卖；14：转出失败退款；15：未成团退款；16：取消挂卖退款；17：订单取消；18：水滴兑换；19：消费奖励；20：订单退款；
            memberWelfarePayments.setTradeType(tradeType);
            memberWelfarePayments.setTradeStatus("5");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
            return iMemberListService.saveOrUpdate(memberList);
        }
        return true;
    }

    @Override
    public boolean addAccountWelfarePayments(String membeId, BigDecimal welfarePayments, String tradeType, String tradeNo) {
        if(welfarePayments.doubleValue()>0) {
            log.info("会员待结算积分增加，会员id："+membeId+";数量："+welfarePayments+";类型："+tradeType+";单号："+tradeNo);
            //加入用户积分
            MemberList memberList = iMemberListService.getById(membeId);
            memberList.setWelfarePaymentsFrozen(memberList.getWelfarePaymentsFrozen().add(welfarePayments));
            //生成积分记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();

            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("1");//类型；0：支出；1：收入
            String dictTextByKey = iSysDictService.queryDictTextByKey("member_welfare_deal_type", tradeType);
            if (StringUtils.isBlank(dictTextByKey)){
                memberWelfarePayments.setWpExplain("积分转入[" + tradeNo + "]");//说明
            }else {
                memberWelfarePayments.setWpExplain(dictTextByKey+"[" + tradeNo + "]");//说明
            }
            memberWelfarePayments.setGoAndCome("平台");//来源或者去向
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator("系统");//操作人
            memberWelfarePayments.setMemberListId(memberList.getId());//会员列表id
            memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
            memberWelfarePayments.setTradeNo(tradeNo);//交易订单号
            memberWelfarePayments.setIsFreeze("1");//是否冻结福利金，0:不是；1：是冻结；2：不可用
            //会员福利金交易类型；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：积分付款；8：进店奖励；9：参团支付；10：参团奖励；11：转入；12：转出；13：挂卖；14：转出失败退款；15：未成团退款；16：取消挂卖退款；17：订单取消；18：水滴兑换；19：消费奖励；20：订单退款；
            memberWelfarePayments.setTradeType(tradeType);
            memberWelfarePayments.setTradeStatus("2");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
            return iMemberListService.saveOrUpdate(memberList);
        }
        return true;
    }

    @Override
    public boolean subtractAccountWelfarePayments(String membeId, BigDecimal welfarePayments, String tradeType, String tradeNo) {
        if(welfarePayments.doubleValue()>0) {
            //加入用户积分
            MemberList memberList = iMemberListService.getById(membeId);
            memberList.setWelfarePaymentsFrozen(memberList.getWelfarePaymentsFrozen().subtract(welfarePayments));
            //生成积分记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("0");//类型；0：支出；1：收入
            String dictTextByKey = iSysDictService.queryDictTextByKey("member_welfare_deal_type", tradeType);
            if (StringUtils.isBlank(dictTextByKey)){
                memberWelfarePayments.setWpExplain("积分转出[" + tradeNo + "]");//说明
            }else {
                memberWelfarePayments.setWpExplain(dictTextByKey+"[" + tradeNo + "]");//说明
            }
            memberWelfarePayments.setGoAndCome("平台");//来源或者去向
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator("系统");//操作人
            memberWelfarePayments.setMemberListId(memberList.getId());//会员列表id
            memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
            memberWelfarePayments.setTradeNo(tradeNo);//交易订单号
            memberWelfarePayments.setIsFreeze("1");//是否冻结福利金，0:不是；1：是冻结；2：不可用
            //会员福利金交易类型；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：积分付款；8：进店奖励；9：参团支付；10：参团奖励；11：转入；12：转出；13：挂卖；14：转出失败退款；15：未成团退款；16：取消挂卖退款；17：订单取消；18：水滴兑换；19：消费奖励；20：订单退款；
            memberWelfarePayments.setTradeType(tradeType);
            memberWelfarePayments.setTradeStatus("2");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
            return iMemberListService.saveOrUpdate(memberList);
        }
        return true;
    }

    @Override
    public boolean addUnusableWelfarePayments(String membeId, BigDecimal welfarePayments, String tradeType, String tradeNo) {
        if(welfarePayments.doubleValue()>0) {
            log.info("会员不可用待结算积分增加，会员id："+membeId+";数量："+welfarePayments+";类型："+tradeType+";单号："+tradeNo);
            //加入用户积分
            MemberList memberList = iMemberListService.getById(membeId);
            memberList.setWelfarePaymentsUnusable(memberList.getWelfarePaymentsUnusable().add(welfarePayments));
            //生成积分记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();

            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("1");//类型；0：支出；1：收入

            String dictTextByKey = iSysDictService.queryDictTextByKey("member_welfare_deal_type", tradeType);
            if (StringUtils.isBlank(dictTextByKey)){
                memberWelfarePayments.setWpExplain("积分转入[" + tradeNo + "]");//说明
            }else {
                memberWelfarePayments.setWpExplain(dictTextByKey+"[" + tradeNo + "]");//说明
            }
            memberWelfarePayments.setGoAndCome("平台");//来源或者去向
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator("系统");//操作人
            memberWelfarePayments.setMemberListId(memberList.getId());//会员列表id
            memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
            memberWelfarePayments.setTradeNo(tradeNo);//交易订单号
            memberWelfarePayments.setIsFreeze("2");//是否冻结福利金，0:不是；1：是冻结；2：不可用
            //会员福利金交易类型；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：积分付款；8：进店奖励；9：参团支付；10：参团奖励；11：转入；12：转出；13：挂卖；14：转出失败退款；15：未成团退款；16：取消挂卖退款；17：订单取消；18：水滴兑换；19：消费奖励；20：订单退款；
            memberWelfarePayments.setTradeType(tradeType);
            memberWelfarePayments.setTradeStatus("2");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
            return iMemberListService.saveOrUpdate(memberList);
        }
        return true;
    }

    @Override
    public boolean subtractUnusableWelfarePayments(String membeId, BigDecimal welfarePayments, String tradeType, String tradeNo) {
        if(welfarePayments.doubleValue()>0) {
            //加入用户积分
            MemberList memberList = iMemberListService.getById(membeId);
            memberList.setWelfarePaymentsUnusable(memberList.getWelfarePaymentsUnusable().subtract(welfarePayments));
            //生成积分记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("0");//类型；0：支出；1：收入

            String dictTextByKey = iSysDictService.queryDictTextByKey("member_welfare_deal_type", tradeType);
            if (StringUtils.isBlank(dictTextByKey)){
                memberWelfarePayments.setWpExplain("积分转出[" + tradeNo + "]");//说明
            }else {
                memberWelfarePayments.setWpExplain(dictTextByKey+"[" + tradeNo + "]");//说明
            }
            memberWelfarePayments.setGoAndCome("平台");//来源或者去向
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator("系统");//操作人
            memberWelfarePayments.setMemberListId(memberList.getId());//会员列表id
            memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
            memberWelfarePayments.setTradeNo(tradeNo);//交易订单号
            memberWelfarePayments.setIsFreeze("2");//是否冻结福利金，0:不是；1：是冻结；2：不可用
            //会员福利金交易类型；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：积分付款；8：进店奖励；9：参团支付；10：参团奖励；11：转入；12：转出；13：挂卖；14：转出失败退款；15：未成团退款；16：取消挂卖退款；17：订单取消；18：水滴兑换；19：消费奖励；20：订单退款；
            memberWelfarePayments.setTradeType(tradeType);
            memberWelfarePayments.setTradeStatus("2");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
            return iMemberListService.saveOrUpdate(memberList);
        }
        return true;
    }

    @Override
    @Transactional
    public void freezeToNomal(String memberWelfarePaymentsId) {
        MemberWelfarePayments memberWelfarePayments=this.getById(memberWelfarePaymentsId);
        memberWelfarePayments.setTradeStatus("5");
        if(this.saveOrUpdate(memberWelfarePayments)){
            //扣除冻结金额
            if(this.subtractAccountWelfarePayments(memberWelfarePayments.getMemberListId(),memberWelfarePayments.getBargainPayments(),memberWelfarePayments.getTradeType(),memberWelfarePayments.getTradeNo())){
                //增加福利金
                if(!this.addWelfarePayments(memberWelfarePayments.getMemberListId(),memberWelfarePayments.getBargainPayments(),memberWelfarePayments.getTradeType(),memberWelfarePayments.getTradeNo(),"")){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }else{
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

}
