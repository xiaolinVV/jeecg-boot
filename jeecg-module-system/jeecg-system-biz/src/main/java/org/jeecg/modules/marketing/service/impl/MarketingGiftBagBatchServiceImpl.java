package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
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
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.dto.MarketingGiftBagBatchDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagBatch;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatchDelivery;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagBatchMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagBatchService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchDeliveryService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchService;
import org.jeecg.modules.marketing.vo.MarketingGiftBagBatchVO;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购礼包
 * @Author: jeecg-boot
 * @Date: 2020-08-29
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingGiftBagBatchServiceImpl extends ServiceImpl<MarketingGiftBagBatchMapper, MarketingGiftBagBatch> implements IMarketingGiftBagBatchService {
    @Autowired
    @Lazy
    private IMarketingGiftBagRecordBatchService iMarketingGiftBagRecordBatchService;
    @Autowired
    @Lazy
    private IMemberListService iMemberListService;
    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    private IStoreAccountCapitalService iStoreAccountCapitalService;

    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;

    @Autowired
    private IAgencyManageService iAgencyManageService;

    @Autowired
    private IAgencyRechargeRecordService iAgencyRechargeRecordService;

    @Autowired
    private IAgencyAccountCapitalService iAgencyAccountCapitalService;

    @Autowired
    private ISysAreaService iSysAreaService;

    @Autowired
    private IMemberDistributionRecordService iMemberDistributionRecordService;

    @Autowired
    private IAllianceRechargeRecordService iAllianceRechargeRecordService;

    @Autowired
    private IAllianceManageService iAllianceManageService;

    @Autowired
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;

    @Autowired
    private IMemberGradeService iMemberGradeService;

    @Autowired
    private IMemberGrowthRecordService iMemberGrowthRecordService;

    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    private IMarketingGiftBagRecordBatchDeliveryService iMarketingGiftBagRecordBatchDeliveryService;

    @Override
    public IPage<MarketingGiftBagBatchVO> queryPageList(Page<MarketingGiftBagBatchVO> page, MarketingGiftBagBatchDTO marketingGiftBagBatchDTO) {
        return baseMapper.queryPageList(page, marketingGiftBagBatchDTO);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingGiftBagBatch(Page<Map<String, Object>> page, MarketingGiftBagBatchDTO marketingGiftBagBatchDTO) {
        return baseMapper.findMarketingGiftBagBatch(page, marketingGiftBagBatchDTO);
    }

    @Override
    @Transactional
    public void paySuccess(String memberId, String id) {

        MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = iMarketingGiftBagRecordBatchService.getById(id);
                marketingGiftBagRecordBatch
                .setPayStatus("1")
                .setPayTime(new Date());
        iMarketingGiftBagRecordBatchService.saveOrUpdate(marketingGiftBagRecordBatch);

        MarketingGiftBagBatch marketingGiftBagBatch = this.getById(marketingGiftBagRecordBatch.getMarketingGiftBagBatchId());
        marketingGiftBagBatch.setRepertory(marketingGiftBagBatch.getRepertory().subtract(new BigDecimal(1)));
        this.saveOrUpdate(marketingGiftBagBatch);

        MemberList memberList = iMemberListService.getById(memberId);
        //会员等级
        LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                .eq(MemberGrade::getDelFlag, "0")
                .eq(MemberGrade::getStatus, "1");

        //会员成长值来源
        String buyGiftBag = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "buy_purchase_gift_bag");

        if (iMemberGradeService.count(memberGradeLambdaQueryWrapper) > 0 && buyGiftBag.equals("1")) {
            memberList.setGrowthValue(memberList.getGrowthValue().add(marketingGiftBagRecordBatch.getActualPayment()));
            memberGradeLambdaQueryWrapper.eq(MemberGrade::getStatus, "1")
                    .le(MemberGrade::getGrowthValueSmall, memberList.getGrowthValue())
                    .ge(MemberGrade::getGrowthValueBig, memberList.getGrowthValue())
                    .orderByAsc(MemberGrade::getSort);

            if (iMemberGradeService.count(memberGradeLambdaQueryWrapper) > 0) {

                MemberGrade memberGrade = iMemberGradeService.list(memberGradeLambdaQueryWrapper).get(0);
                if (StringUtils.isNotBlank(memberList.getMemberGradeId())) {
                    if (memberGrade.getSort().doubleValue() > iMemberGradeService.getById(memberList.getMemberGradeId()).getSort().doubleValue()) {
                        memberList.setMemberGradeId(memberGrade.getId());
                    }
                } else {
                    if (memberList.getMemberType().equals("0")) {
                        memberList.setMemberType("1");
                        memberList.setVipTime(new Date());
                    }
                    memberList.setMemberGradeId(memberGrade.getId());
                }
                iMemberListService.saveOrUpdate(memberList);
            } else {
                MemberGrade grade = iMemberGradeService.list(new LambdaQueryWrapper<MemberGrade>()
                        .eq(MemberGrade::getDelFlag, "0")
                        .eq(MemberGrade::getStatus, "1")
                        .orderByDesc(MemberGrade::getSort)).get(0);
                if (grade.getGrowthValueBig().doubleValue() <= memberList.getGrowthValue().doubleValue()) {
                    if (memberList.getMemberType().equals("0")) {
                        memberList.setMemberType("1");
                        memberList.setVipTime(new Date());
                    }
                    memberList.setMemberGradeId(grade.getId());
                    iMemberListService.saveOrUpdate(memberList);
                }
            }
            iMemberGrowthRecordService.save(new MemberGrowthRecord()
                    .setDelFlag("0")
                    .setMemberListId(memberList.getId())
                    .setTradeNo(marketingGiftBagRecordBatch.getId())
                    .setTradeType("5")
                    .setRemark("购买采购礼包[" + marketingGiftBagRecordBatch.getId() + "]")
                    .setGrowthValue(marketingGiftBagRecordBatch.getActualPayment())
                    .setOrderNo(OrderNoUtils.getOrderNo()));
        }
        //店铺条件构造器
        LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId, memberList.getSysUserId())
                .in(StoreManage::getPayStatus, "1", "2");

        if (marketingGiftBagBatch.getDistributionCommission().doubleValue() > 0) {
            //礼包资金的分配
            //归属店铺奖励
            BigDecimal ownershipShopsReward = marketingGiftBagBatch.getOwnershipShopsReward();
            //归属奖励
            if (ownershipShopsReward.doubleValue() > 0 && StringUtils.isNotBlank(memberList.getSysUserId())) {
                if (iStoreManageService.count(storeManageLambdaQueryWrapper) > 0) {
                    StoreManage storeManage = iStoreManageService.list(storeManageLambdaQueryWrapper).get(0);
                    marketingGiftBagRecordBatch.setAffiliationStore(storeManage.getSysUserId());
                    //店铺余额明细
                    StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                    storeRechargeRecord.setStoreManageId(storeManage.getId());
                    storeRechargeRecord.setPayType("12");
                    storeRechargeRecord.setGoAndCome("0");
                    storeRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(ownershipShopsReward.divide(new BigDecimal(100))));
                    storeRechargeRecord.setTradeStatus("5");
                    storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                    storeRechargeRecord.setOperator("系统");
                    storeRechargeRecord.setRemark("归属会员购买采购礼包[" + marketingGiftBagRecordBatch.getId() + "]");//2020年6月13日21:17:15
                    storeRechargeRecord.setTradeType("1");
                    storeRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                    iStoreRechargeRecordService.save(storeRechargeRecord);

                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    iStoreManageService.saveOrUpdate(storeManage);

                    //店铺资金流水
                    StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                    storeAccountCapital.setStoreManageId(storeManage.getId());
                    storeAccountCapital.setPayType("12");
                    storeAccountCapital.setGoAndCome("0");
                    storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
                    storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                    storeAccountCapital.setBalance(storeManage.getBalance());
                    iStoreAccountCapitalService.save(storeAccountCapital);

                    log.info("会员购买采购礼包：" + storeRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBagBatch.getDistributionCommission() + "--归属店铺：" + storeManage.getStoreName());
                }
            }

            //渠道销售奖励
            BigDecimal channelShopsReward = marketingGiftBagBatch.getChannelShopsReward();

            //渠道店铺奖励
            if (channelShopsReward.doubleValue() > 0 && StringUtils.isNotBlank(marketingGiftBagRecordBatch.getDistributionChannel())) {
                QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                storeManageQueryWrapper.eq("sys_user_id", marketingGiftBagRecordBatch.getDistributionChannel());
                storeManageQueryWrapper.in("pay_status", "1", "2");
                if (iStoreManageService.count(storeManageQueryWrapper) > 0) {
                    StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                    //店铺余额明细
                    StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                    storeRechargeRecord.setStoreManageId(storeManage.getId());
                    storeRechargeRecord.setPayType("13");
                    storeRechargeRecord.setGoAndCome("0");
                    storeRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(channelShopsReward.divide(new BigDecimal(100))));
                    storeRechargeRecord.setTradeStatus("5");
                    storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                    storeRechargeRecord.setOperator("系统");
                    storeRechargeRecord.setRemark("渠道会员购买采购礼包[" + marketingGiftBagRecordBatch.getId() + "]");
                    storeRechargeRecord.setTradeType("1");
                    storeRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                    iStoreRechargeRecordService.save(storeRechargeRecord);

                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    iStoreManageService.saveOrUpdate(storeManage);

                    //店铺资金流水
                    StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                    storeAccountCapital.setStoreManageId(storeManage.getId());
                    storeAccountCapital.setPayType("13");
                    storeAccountCapital.setGoAndCome("0");
                    storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
                    storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                    storeAccountCapital.setBalance(storeManage.getBalance());
                    iStoreAccountCapitalService.save(storeAccountCapital);

                    log.info("会员购买采购礼包：" + storeRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBagBatch.getDistributionCommission() + "--渠道店铺：" + storeManage.getStoreName());
                }
            }

            //推广奖励
            BigDecimal promoterReward = marketingGiftBagBatch.getPromoterReward();

            //推广二级奖励
            BigDecimal promoterRewardTwo = marketingGiftBagBatch.getPromoterRewardTwo();

            //推广奖励
            if (promoterReward.doubleValue() > 0 && StringUtils.isNotBlank(memberList.getPromoterType())) {
                //店铺
                /*if (memberList.getPromoterType().equals("0")) {
                    QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                    storeManageQueryWrapper.eq("sys_user_id", memberList.getPromoter());
                    storeManageQueryWrapper.in("pay_status", "1", "2");
                    StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                    //店铺余额明细
                    StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                    storeRechargeRecord.setStoreManageId(storeManage.getId());
                    storeRechargeRecord.setPayType("8");//2020年5月23日16:36:23 原为6:渠道销售奖励 现改为8礼包推广奖励
                    storeRechargeRecord.setGoAndCome("0");
                    storeRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(promoterReward.divide(new BigDecimal(100))));
                    storeRechargeRecord.setTradeStatus("5");
                    storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                    storeRechargeRecord.setOperator("系统");
                    storeRechargeRecord.setRemark("推广会员购买礼包[" + marketingGiftBagRecordBatch.getId() + "]");
                    storeRechargeRecord.setTradeType("1");
                    storeRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                    iStoreRechargeRecordService.save(storeRechargeRecord);

                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    iStoreManageService.saveOrUpdate(storeManage);

                    //店铺资金流水
                    StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                    storeAccountCapital.setStoreManageId(storeManage.getId());
                    storeAccountCapital.setPayType("8");//2020年5月23日16:36:23 原为6:渠道销售奖励 现改为8礼包推广奖励
                    storeAccountCapital.setGoAndCome("0");
                    storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
                    storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                    storeAccountCapital.setBalance(storeManage.getBalance());
                    iStoreAccountCapitalService.save(storeAccountCapital);
                    log.info("会员购买礼包：" + storeRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推广店铺：" + storeManage.getStoreName());
                }*/
                //会员 分销团队
                if (memberList.getPromoterType().equals("1")) {

                    MemberList proterMemberList = iMemberListService.getById(memberList.getPromoter());
                    marketingGiftBagRecordBatch.setPromoter(proterMemberList.getId());
                    //会员余额明细
                    MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                    memberRechargeRecord.setMemberListId(proterMemberList.getId());
                    memberRechargeRecord.setPayType("9");
                    memberRechargeRecord.setGoAndCome("0");
                    memberRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(promoterReward.divide(new BigDecimal(100))));
                    memberRechargeRecord.setTradeStatus("5");
                    memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                    memberRechargeRecord.setOperator("系统");
                    memberRechargeRecord.setRemark("会员购买采购礼包一级分销奖励[" + marketingGiftBagRecordBatch.getId() + "]");
                    memberRechargeRecord.setTMemberListId(marketingGiftBagRecordBatch.getMemberListId());
                    memberRechargeRecord.setTradeType("1");
                    memberRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                    memberRechargeRecord.setMemberLevel("1"); //2020年7月19日22:06:01新增分销成员级别
                    iMemberRechargeRecordService.save(memberRechargeRecord);

                    MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                    memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                    memberDistributionRecord.setGoodPicture(marketingGiftBagBatch.getMainPicture());
                    memberDistributionRecord.setGoodName(marketingGiftBagBatch.getGiftName());
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
                    memberAccountCapital.setPayType("9");
                    memberAccountCapital.setGoAndCome("0");
                    memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                    memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                    memberAccountCapital.setBalance(proterMemberList.getBalance());
                    iMemberAccountCapitalService.save(memberAccountCapital);
                    log.info("会员购买采购礼包一级分销奖励：" + memberRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推广一级会员：" + proterMemberList.getNickName());
                    if (promoterRewardTwo.doubleValue() > 0 && proterMemberList.getPromoterType().equals("1")) {
                        MemberList proterMemberListTwo = iMemberListService.getById(proterMemberList.getPromoter());
                        marketingGiftBagRecordBatch.setPromoterTwo(proterMemberListTwo.getId());
                        //会员余额明细
                        MemberRechargeRecord memberRechargeRecordTwo = new MemberRechargeRecord();
                        memberRechargeRecordTwo.setMemberListId(proterMemberListTwo.getId());
                        memberRechargeRecordTwo.setPayType("10");
                        memberRechargeRecordTwo.setGoAndCome("0");
                        memberRechargeRecordTwo.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(promoterRewardTwo.divide(new BigDecimal(100))));
                        memberRechargeRecordTwo.setTradeStatus("5");
                        memberRechargeRecordTwo.setOrderNo(OrderNoUtils.getOrderNo());
                        memberRechargeRecordTwo.setOperator("系统");
                        memberRechargeRecordTwo.setRemark("会员购买采购礼包二级分销奖励[" + marketingGiftBagRecordBatch.getId() + "]");
                        memberRechargeRecordTwo.setTMemberListId(marketingGiftBagRecordBatch.getMemberListId());
                        memberRechargeRecordTwo.setTradeType("1");
                        memberRechargeRecordTwo.setTradeNo(marketingGiftBagRecordBatch.getId());
                        memberRechargeRecordTwo.setMemberLevel("2"); //2020年7月19日22:06:01新增分销成员级别
                        iMemberRechargeRecordService.save(memberRechargeRecordTwo);

                        MemberDistributionRecord memberDistributionRecordTwo = new MemberDistributionRecord();
                        memberDistributionRecordTwo.setMemberRechargeRecordId(memberRechargeRecordTwo.getId());
                        memberDistributionRecordTwo.setGoodPicture(marketingGiftBagBatch.getMainPicture());
                        memberDistributionRecordTwo.setGoodName(marketingGiftBagBatch.getGiftName());
                        memberDistributionRecordTwo.setGoodSpecification("-");
                        memberDistributionRecordTwo.setCommission(memberRechargeRecordTwo.getAmount());
                        iMemberDistributionRecordService.save(memberDistributionRecordTwo);

                        proterMemberListTwo.setBalance(proterMemberListTwo.getBalance().add(memberRechargeRecordTwo.getAmount()));
                        if (proterMemberListTwo.getTotalCommission() == null) {
                            proterMemberListTwo.setTotalCommission(new BigDecimal(0));
                        }
                        proterMemberListTwo.setTotalCommission(proterMemberListTwo.getTotalCommission().add(memberRechargeRecordTwo.getAmount()));
                        iMemberListService.saveOrUpdate(proterMemberListTwo);

                        //会员资金流水
                        MemberAccountCapital memberAccountCapitalTwo = new MemberAccountCapital();
                        memberAccountCapitalTwo.setMemberListId(proterMemberListTwo.getId());
                        memberAccountCapitalTwo.setPayType("10");
                        memberAccountCapitalTwo.setGoAndCome("0");
                        memberAccountCapitalTwo.setAmount(memberRechargeRecordTwo.getAmount());
                        memberAccountCapitalTwo.setOrderNo(memberRechargeRecordTwo.getOrderNo());
                        memberAccountCapitalTwo.setBalance(proterMemberListTwo.getBalance());
                        iMemberAccountCapitalService.save(memberAccountCapitalTwo);
                        log.info("会员购买采购礼包二级分销奖励：" + memberRechargeRecordTwo.getAmount() + "---礼包金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推广二级会员：" + proterMemberList.getNickName());
                    }
                }
            }
            //加盟商控制锁
            boolean isExecute=true;

            //查出会员归属加盟商
            StoreManage storeManage = iStoreManageService.getOne(storeManageLambdaQueryWrapper);

            if (oConvertUtils.isNotEmpty(storeManage)){
                AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                        .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                if (oConvertUtils.isNotEmpty(allianceManage)){
                    //独享控制
                    if(allianceManage.getProfitType().equals("0")){
                        isExecute=false;
                    }
                    if (marketingGiftBagBatch.getIsAllianceAward().equals("1")){
                        if (oConvertUtils.isNotEmpty(allianceManage)){
                            if (allianceManage.getGiftCommissionRate().doubleValue() > 0&& allianceManage.getStatus().equals("1")) {
                                marketingGiftBagRecordBatch.setAllianceId(allianceManage.getId());
                                //加盟商余额明细
                                AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                                allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                                allianceRechargeRecord.setPayType("9");
                                allianceRechargeRecord.setGoAndCome("0");
                                allianceRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(allianceManage.getGiftCommissionRate().divide(new BigDecimal(100))));
                                allianceRechargeRecord.setTradeStatus("5");
                                allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                allianceRechargeRecord.setTradeType("1");
                                allianceRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                                allianceRechargeRecord.setRemark("采购礼包分成["+marketingGiftBagRecordBatch.getId()+"]");
                                iAllianceRechargeRecordService.save(allianceRechargeRecord);

                                allianceManage.setBalance(allianceManage.getBalance().add(allianceRechargeRecord.getAmount()));
                                iAllianceManageService.saveOrUpdate(allianceManage);

                                //加盟商资金明细
                                AllianceAccountCapital allianceAccountCapital = new AllianceAccountCapital();
                                allianceAccountCapital.setSysUserId(allianceManage.getSysUserId());
                                allianceAccountCapital.setPayType("9");
                                allianceAccountCapital.setGoAndCome("0");
                                allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                                allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                                allianceAccountCapital.setBalance(allianceManage.getBalance());
                                iAllianceAccountCapitalService.save(allianceAccountCapital);

                                log.info("采购礼包分成：" + allianceRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推荐加盟商：" + allianceManage.getId());
                            }
                        }
                    }
                }

            }

            //礼包控制要不要送给代理
            if(marketingGiftBagBatch.getModeDistribution().equals("0")&&marketingGiftBagBatch.getIsAgencyAward().equals("1")) {

                if(isExecute) {
                    //代理资金分配
                    List<String> sysAreas = Lists.newArrayList();
                    if (marketingGiftBagRecordBatch.getLatitude().doubleValue() > 0 && marketingGiftBagRecordBatch.getLongitude().doubleValue() > 0) {
                        String adCode = tengxunMapUtils.findAdcode(marketingGiftBagRecordBatch.getLatitude().toString() + "," + marketingGiftBagRecordBatch.getLongitude().toString());
                        if (StringUtils.isNotBlank(adCode)) {
                            QueryWrapper<SysArea> sysAreaQueryWrapper = new QueryWrapper<>();
                            sysAreaQueryWrapper.eq("teng_xun_id", adCode);
                            SysArea sysArea = iSysAreaService.getOne(sysAreaQueryWrapper);
                            int level = sysArea.getLeve() - 1;
                            sysAreas.add(sysArea.getId().toString());
                            while (level >= 0) {
                                sysArea = iSysAreaService.getById(sysArea.getParentId());
                                sysAreas.add(sysArea.getId().toString());
                                level--;
                            }
                            sysAreas.forEach(sid -> {
                                SysArea sysArea1 = iSysAreaService.getById(sid);
                                if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                    AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                    if (agencyManage != null && agencyManage.getGiftCommissionRate().doubleValue() > 0&& agencyManage.getStatus().equals("1")) {
                                        if (sysArea1.getLeve()==2){
                                            marketingGiftBagRecordBatch.setTowmId(agencyManage.getId());
                                        }
                                        if (sysArea1.getLeve()==1){
                                            marketingGiftBagRecordBatch.setCityId(agencyManage.getId());
                                        }
                                        if (sysArea1.getLeve()==0){
                                            marketingGiftBagRecordBatch.setProviceId(agencyManage.getId());
                                        }
                                        //代理余额明细
                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                        agencyRechargeRecord.setPayType("8");
                                        agencyRechargeRecord.setGoAndCome("0");
                                        agencyRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(agencyManage.getGiftCommissionRate().divide(new BigDecimal(100))));
                                        agencyRechargeRecord.setTradeStatus("5");
                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        agencyRechargeRecord.setTradeType("1");
                                        agencyRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                                        agencyRechargeRecord.setRemark("采购礼包分成["+marketingGiftBagRecordBatch.getId()+"]");
                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                        //代理资金明细
                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                        agencyAccountCapital.setPayType("8");
                                        agencyAccountCapital.setGoAndCome("0");
                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                        log.info("采购礼包分成：" + agencyRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推荐代理：" + agencyManage.getId());
                                    }
                                }
                            });
                        }
                    }
                }
            }
            //礼包控制要不要送给代理
            if(marketingGiftBagBatch.getModeDistribution().equals("1")) {

                if(isExecute) {
                    //代理资金分配
                    List<String> sysAreas = Lists.newArrayList();
                    if (marketingGiftBagRecordBatch.getLatitude().doubleValue() > 0 && marketingGiftBagRecordBatch.getLongitude().doubleValue() > 0) {
                        String adCode = tengxunMapUtils.findAdcode(marketingGiftBagRecordBatch.getLatitude().toString() + "," + marketingGiftBagRecordBatch.getLongitude().toString());
                        if (StringUtils.isNotBlank(adCode)) {
                            QueryWrapper<SysArea> sysAreaQueryWrapper = new QueryWrapper<>();
                            sysAreaQueryWrapper.eq("teng_xun_id", adCode);
                            SysArea sysArea = iSysAreaService.getOne(sysAreaQueryWrapper);
                            int level = sysArea.getLeve() - 1;
                            sysAreas.add(sysArea.getId().toString());
                            while (level >= 0) {
                                sysArea = iSysAreaService.getById(sysArea.getParentId());
                                sysAreas.add(sysArea.getId().toString());
                                level--;
                            }
                            sysAreas.forEach(sid -> {
                                SysArea sysArea1 = iSysAreaService.getById(sid);
                                if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                    AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                    if (sysArea1.getLeve()==2&&marketingGiftBagBatch.getTowmAward().doubleValue()>0&&agencyManage != null&& agencyManage.getStatus().equals("1")) {
                                        marketingGiftBagRecordBatch.setTowmId(agencyManage.getId());
                                        //代理余额明细
                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                        agencyRechargeRecord.setPayType("8");
                                        agencyRechargeRecord.setGoAndCome("0");
                                        agencyRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(marketingGiftBagBatch.getTowmAward().divide(new BigDecimal(100))));
                                        agencyRechargeRecord.setTradeStatus("5");
                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        agencyRechargeRecord.setTradeType("1");
                                        agencyRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                                        agencyRechargeRecord.setRemark("采购礼包分成["+marketingGiftBagRecordBatch.getId()+"]");
                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                        //代理资金明细
                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                        agencyAccountCapital.setPayType("8");
                                        agencyAccountCapital.setGoAndCome("0");
                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                        log.info("礼包购买奖励：" + agencyRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推荐代理：" + agencyManage.getId());
                                    }
                                    if (sysArea1.getLeve()==1&&marketingGiftBagBatch.getCityAward().doubleValue()>0&&agencyManage != null&& agencyManage.getStatus().equals("1")) {
                                        marketingGiftBagRecordBatch.setCityId(agencyManage.getId());
                                        //代理余额明细
                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                        agencyRechargeRecord.setPayType("8");
                                        agencyRechargeRecord.setGoAndCome("0");
                                        agencyRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(marketingGiftBagBatch.getCityAward().divide(new BigDecimal(100))));
                                        agencyRechargeRecord.setTradeStatus("5");
                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        agencyRechargeRecord.setTradeType("1");
                                        agencyRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                                        agencyRechargeRecord.setRemark("采购礼包分成["+marketingGiftBagRecordBatch.getId()+"]");
                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                        //代理资金明细
                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                        agencyAccountCapital.setPayType("8");
                                        agencyAccountCapital.setGoAndCome("0");
                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                        log.info("采购礼包分成：" + agencyRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推荐代理：" + agencyManage.getId());
                                    }
                                    if (sysArea1.getLeve()==0&&marketingGiftBagBatch.getProviceAward().doubleValue()>0&&agencyManage != null&& agencyManage.getStatus().equals("1")) {
                                        marketingGiftBagRecordBatch.setProviceId(agencyManage.getId());
                                        //代理余额明细
                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                        agencyRechargeRecord.setPayType("8");
                                        agencyRechargeRecord.setGoAndCome("0");
                                        agencyRechargeRecord.setAmount(marketingGiftBagBatch.getDistributionCommission().multiply(marketingGiftBagBatch.getProviceAward().divide(new BigDecimal(100))));
                                        agencyRechargeRecord.setTradeStatus("5");
                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        agencyRechargeRecord.setTradeType("1");
                                        agencyRechargeRecord.setTradeNo(marketingGiftBagRecordBatch.getId());
                                        agencyRechargeRecord.setRemark("采购礼包分成["+marketingGiftBagRecordBatch.getId()+"]");
                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                        //代理资金明细
                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                        agencyAccountCapital.setPayType("8");
                                        agencyAccountCapital.setGoAndCome("0");
                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                        log.info("采购礼包分成：" + agencyRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBagBatch.getDistributionCommission() + "--推荐代理：" + agencyManage.getId());
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
        iMarketingGiftBagRecordBatchService.saveOrUpdate(marketingGiftBagRecordBatch);
        ArrayList<MarketingGiftBagRecordBatchDelivery> marketingGiftBagRecordBatchDeliveryArrayList = new ArrayList<>();

        for (int i = 0; i < marketingGiftBagRecordBatch.getSendTimes().intValue(); i++) {
            marketingGiftBagRecordBatchDeliveryArrayList
                    .add(new MarketingGiftBagRecordBatchDelivery()
                            .setBatchNumber("pc"+i)
                            .setPid("0")
                            .setMarketingGiftBagRecordBatchId(marketingGiftBagRecordBatch.getId())
                    );
        }
        iMarketingGiftBagRecordBatchDeliveryService.saveBatch(marketingGiftBagRecordBatchDeliveryArrayList);
    }
}

