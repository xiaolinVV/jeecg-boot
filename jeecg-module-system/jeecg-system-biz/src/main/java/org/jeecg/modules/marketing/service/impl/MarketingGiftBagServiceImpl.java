package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
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
import org.jeecg.modules.marketing.dto.MarketingGiftBagDTO;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.vo.MarketingGiftBagVO;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.pay.entity.PayGiftBagLog;
import org.jeecg.modules.pay.service.IPayGiftBagLogService;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreFranchiser;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreFranchiserService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.service.ISysAreaService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Description: 礼包管理
 * @Author: jeecg-boot
 * @Date: 2019-12-09
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingGiftBagServiceImpl extends ServiceImpl<MarketingGiftBagMapper, MarketingGiftBag> implements IMarketingGiftBagService {

    @Autowired
    private IMarketingGiftBagDiscountService iMarketingGiftBagDiscountService;

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMarketingGiftBagCertificateService iMarketingGiftBagCertificateService;

    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;

    @Autowired
    private IMarketingGiftBagRecordService iMarketingGiftBagRecordService;

    @Autowired
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
    private IMarketingGiftBagStoreService iMarketingGiftBagStoreService;

    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    private IMarketingWelfarePaymentsService iMarketingWelfarePaymentsService;

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
    private IMemberDesignationService iMemberDesignationService;

    @Autowired
    @Lazy
    private IMarketingRecordedMoneyService iMarketingRecordedMoneyService;

    @Autowired
    private IMemberDesignationCountService iMemberDesignationCountService;

    @Autowired
    @Lazy
    private IMemberDesignationGroupService iMemberDesignationGroupService;
    @Autowired
    @Lazy
    private IMemberDesignationMemberListService iMemberDesignationMemberListService;

    @Autowired
    private IMarketingGiftBagCarListService iMarketingGiftBagCarListService;

    @Autowired
    private IMarketingStoreGiftCardListService iMarketingStoreGiftCardListService;

    @Autowired
    private IPayGiftBagLogService iPayGiftBagLogService;


    @Autowired
    private IStoreFranchiserService iStoreFranchiserService;

    @Override
    public IPage<Map<String, Object>> getMarketingGiftBagList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getMarketingGiftBagList(page, paramMap);
    }

    @Override
    @Transactional
    public void marketingGiftBagMemberGradeUpdate(Object memberId, MarketingGiftBag marketingGiftBag, MarketingGiftBagRecord marketingGiftBagRecord) {
        MemberList memberList = iMemberListService.getById(memberId.toString());
        LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                .eq(MemberGrade::getDelFlag, "0")
                .eq(MemberGrade::getStatus, "1");
        long count = iMemberGradeService.count(memberGradeLambdaQueryWrapper);

        //会员成长值来源
        String buyGiftBag = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "buy_gift_bag");
        if (marketingGiftBag.getVipPrivilege().equals("1")) {
            if (memberList.getMemberType().equals("0")) {
                memberList.setMemberType("1");
                memberList.setVipTime(new Date());
            }
            if (count > 0) {
                if (StringUtils.isNotBlank(memberList.getMemberGradeId())) {
                    if (iMemberGradeService.getById(memberList.getMemberGradeId()).getSort().doubleValue() < iMemberGradeService.getById(marketingGiftBag.getSendVipMemberGradeId()).getSort().doubleValue()) {
                        memberList.setMemberGradeId(marketingGiftBag.getSendVipMemberGradeId());
                    }
                } else {
                    memberList.setMemberGradeId(marketingGiftBag.getSendVipMemberGradeId());
                }
            }

        }
        if (count > 0 && buyGiftBag.equals("1")) {
            memberList.setGrowthValue(memberList.getGrowthValue().add(marketingGiftBagRecord.getPrice()));
            memberGradeLambdaQueryWrapper
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
                    .setTradeNo(marketingGiftBagRecord.getId())
                    .setTradeType("2")
                    .setRemark("礼包购买[" + marketingGiftBagRecord.getId() + "]")
                    .setGrowthValue(marketingGiftBagRecord.getPrice())
                    .setOrderNo(OrderNoUtils.getOrderNo()));
        }
        iMemberListService.saveOrUpdate(memberList);
    }
    @Override
    @Transactional
    public void marketingGiftBagMemberDesignationUpdateNew(Object memberId,
                                                           MarketingGiftBag marketingGiftBag,
                                                           MarketingGiftBagRecord marketingGiftBagRecord) {
        if (StringUtils.isNotBlank(marketingGiftBag.getMemberDesignationId())) {
            //购买礼包会员
            MemberList memberList = iMemberListService.getById(memberId.toString());
            //获取礼包赠送称号
            MemberDesignation giftBagemberDesignation = iMemberDesignationService.getById(marketingGiftBag.getMemberDesignationId());
            //获取礼包称号团队
            MemberDesignationGroup memberDesignationGroup = iMemberDesignationGroupService.getById(giftBagemberDesignation.getMemberDesignationGroupId());

            iMemberDesignationGroupService.updateById(memberDesignationGroup
                    .setTotalMembers(memberDesignationGroup.getTotalMembers().add(new BigDecimal(1))));

            //会员称号条件构造器
            LambdaQueryWrapper<MemberDesignationMemberList> memberDesignationMemberListLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignationMemberList>()
                    .eq(MemberDesignationMemberList::getMemberListId, memberList.getId())
                    .eq(MemberDesignationMemberList::getMemberDesignationId, giftBagemberDesignation.getId())
                    .orderByAsc(MemberDesignationMemberList::getCreateTime)
                    .last("limit 1");

            if (iMemberDesignationMemberListService.count(memberDesignationMemberListLambdaQueryWrapper) > 0) {
                //会员在这个group下面称号数如果大于0
                MemberDesignationMemberList memberDesignationMemberList = iMemberDesignationMemberListService.getOne(memberDesignationMemberListLambdaQueryWrapper);
                    iMemberDesignationMemberListService.updateById(memberDesignationMemberList
                            .setTotalGiftSales(memberDesignationMemberList.getTotalGiftSales().add(marketingGiftBagRecord.getPrice()))
                            .setBuyCount(memberDesignationMemberList.getBuyCount().add(new BigDecimal(1))));
                    String tId = "";

                    //是否有团队管理id
                    if (StringUtils.isNotBlank(memberDesignationMemberList.getTManageId())) {
                        tId = memberDesignationMemberList.getTManageId();
                    }

                    List<MemberDesignationCount> designationCountList = iMemberDesignationCountService.list(new LambdaQueryWrapper<MemberDesignationCount>()
                            .eq(MemberDesignationCount::getMemberDesignationId, giftBagemberDesignation.getId())
                            .eq(MemberDesignationCount::getMemberListId, memberList.getId())
                    );
                    if (designationCountList.size() > 0) {
                        MemberDesignationCount memberDesignationCount = designationCountList.get(0);
                        iMemberDesignationCountService.updateById(memberDesignationCount
                                .setTotalMembers(memberDesignationCount.getTotalMembers().add(new BigDecimal(1))));
                    } else {
                        iMemberDesignationCountService.save(new MemberDesignationCount()
                                .setMemberListId(memberList.getId())
                                .setMemberDesignationId(giftBagemberDesignation.getId())
                                .setTotalMembers(new BigDecimal(1))
                        );
                    }

                    //获取上级id,封装成队列
                    while (StringUtils.isNotBlank(tId)) {
                        MemberDesignationMemberList designationMemberList = iMemberDesignationMemberListService.getOne(new LambdaQueryWrapper<MemberDesignationMemberList>()
                                .eq(MemberDesignationMemberList::getMemberDesignationGroupId, memberDesignationMemberList.getMemberDesignationGroupId())
                                .eq(MemberDesignationMemberList::getMemberListId, tId)
                                .orderByAsc(MemberDesignationMemberList::getCreateTime)
                                .last("limit 1")
                        );
                        if(designationMemberList!=null) {
                            iMemberDesignationMemberListService.updateById(designationMemberList
                                    .setTotalGiftSales(designationMemberList.getTotalGiftSales().add(marketingGiftBagRecord.getPrice())));

                            if (StringUtils.isNotBlank(designationMemberList.getTManageId())) {
                                tId = designationMemberList.getTManageId();
                            } else {
                                tId = "";
                            }
                        }else{
                            tId = "";
                        }
                    }

            } else {
                //会员没有称号
                //判断是否有礼包推荐人
                MemberDesignationMemberList memberDesignationMemberList = new MemberDesignationMemberList()
                        .setMemberListId(memberList.getId())
                        .setMemberDesignationId(marketingGiftBag.getMemberDesignationId())
                        .setIsBuyGift("1")
                        .setMemberJoinTime(new Date())
                        .setTotalGiftSales(marketingGiftBagRecord.getPrice())
                        .setMemberDesignationGroupId(giftBagemberDesignation.getMemberDesignationGroupId());
                if (StringUtils.isNotBlank(marketingGiftBagRecord.getTMemberId())) {
                    if (!marketingGiftBagRecord.getTMemberId().equals(memberList.getId())) {
                        memberDesignationMemberList.setOldTManageId(marketingGiftBagRecord.getTMemberId());
                        memberDesignationMemberList.setTManageId(marketingGiftBagRecord.getTMemberId());

                    } else {
                        memberDesignationMemberList.setOldTManageId(memberDesignationGroup.getMemberId());
                        memberDesignationMemberList.setTManageId(memberDesignationGroup.getMemberId());
                    }
                } else {
                    memberDesignationMemberList.setOldTManageId(memberDesignationGroup.getMemberId());
                    memberDesignationMemberList.setTManageId(memberDesignationGroup.getMemberId());
                }
                iMemberDesignationMemberListService.save(memberDesignationMemberList);
                this.totalMemberAddNew(memberDesignationMemberList, marketingGiftBagRecord.getPrice());
            }
            if (marketingGiftBag.getParticipation().doubleValue() > 0) {

                if (StringUtils.isNotBlank(marketingGiftBag.getGiftBagDividendRatio())) {
                    JSONArray dividendRatio = JSONArray.parseArray(marketingGiftBag.getGiftBagDividendRatio());
                    dividendRatio.forEach(dr -> {
                        JSONObject parse = (JSONObject) JSONObject.parse(String.valueOf(dr));

                        MemberDesignation memberDesignations = iMemberDesignationService.getById(parse.getString("memberDesignationId"));

                        BigDecimal participation = parse.getBigDecimal("participation");

                        if (memberDesignations.getIsOpenMoney().equals("1") && participation.doubleValue() > 0) {

                            BigDecimal bigDecimal = marketingGiftBag.getParticipation().multiply(participation.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);

                            iMemberDesignationService.saveOrUpdate(memberDesignations
                                    .setBalance(memberDesignations.getBalance().add(bigDecimal))
                                    .setRecordedMoney(memberDesignations.getRecordedMoney().add(bigDecimal))
                            );
                            MarketingRecordedMoney marketingRecordedMoney = new MarketingRecordedMoney()
                                    .setMemberDesignationId(memberDesignations.getId())
                                    .setTradeNo(marketingGiftBagRecord.getId())
                                    .setTradeType("1")
                                    .setParticipation(participation)
                                    .setParticipationMoney(marketingGiftBag.getParticipation())
                                    .setRecordedMoney(bigDecimal)
                                    .setBalance(memberDesignations.getBalance())
                                    .setTradeTime(new Date())
                                    .setRemark("礼包分红[" + marketingGiftBagRecord.getId() + "]")
                                    .setOrderNo(OrderNoUtils.getOrderNo());
                            iMarketingRecordedMoneyService.save(marketingRecordedMoney);
                            log.info("称号资金：" + memberDesignations.getName() + "入账资金:" + bigDecimal);
                        }

                    });
                } else {

                    List<MemberDesignation> memberDesignationList = iMemberDesignationService.list(new LambdaQueryWrapper<MemberDesignation>()
                            .eq(MemberDesignation::getMemberDesignationGroupId, giftBagemberDesignation.getMemberDesignationGroupId())
                            .orderByAsc(MemberDesignation::getSort));

                    memberDesignationList.forEach(mdl -> {
                        if (mdl.getIsOpenMoney().equals("1") && mdl.getParticipation().doubleValue() > 0) {
                            BigDecimal bigDecimal = marketingGiftBag.getParticipation().multiply(mdl.getParticipation().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                            iMemberDesignationService.saveOrUpdate(mdl
                                    .setBalance(mdl.getBalance().add(bigDecimal))
                                    .setRecordedMoney(mdl.getRecordedMoney().add(bigDecimal))
                            );
                            iMarketingRecordedMoneyService.save(new MarketingRecordedMoney()
                                    .setMemberDesignationId(mdl.getId())
                                    .setTradeNo(marketingGiftBagRecord.getId())
                                    .setTradeType("1")
                                    .setParticipation(mdl.getParticipation())
                                    .setParticipationMoney(marketingGiftBag.getParticipation())
                                    .setRecordedMoney(bigDecimal)
                                    .setBalance(mdl.getBalance())
                                    .setTradeTime(new Date())
                                    .setRemark("礼包分红[" + marketingGiftBagRecord.getId() + "]")
                                    .setOrderNo(OrderNoUtils.getOrderNo())
                            );
                            log.info("称号资金：" + mdl.getName() + "入账资金:" + bigDecimal);
                        }
                    });
                }

            }
            LambdaQueryWrapper<MemberDesignation> memberDesignationLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignation>()
                    .eq(MemberDesignation::getIsDefault, "1")
                    .isNull(MemberDesignation::getMemberDesignationGroupId);
            MemberDesignation memberDesignation = iMemberDesignationService.list(memberDesignationLambdaQueryWrapper).get(0);
            LambdaQueryWrapper<MemberDesignationMemberList> designationMemberListLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignationMemberList>()
                    .eq(MemberDesignationMemberList::getMemberListId, memberList.getId())
                    .eq(MemberDesignationMemberList::getMemberDesignationId, memberDesignation.getId());
            if (iMemberDesignationMemberListService.count(designationMemberListLambdaQueryWrapper) > 0) {
                iMemberDesignationMemberListService.remove(designationMemberListLambdaQueryWrapper);
            }
            log.info("称号结束++++++++++++++++");
        }

    }

    @Override
    public void generateGiftCar(String marketingGiftBagId, String memberId) {
        iMarketingGiftBagCarListService.list(new LambdaQueryWrapper<MarketingGiftBagCarList>()
                .eq(MarketingGiftBagCarList::getMarketingGiftBagId, marketingGiftBagId)).forEach(mgbcl -> {
            for (int i = 0; i < mgbcl.getQuantity().intValue(); i++) {
                iMarketingStoreGiftCardListService.generate(mgbcl.getMarketingStoreGiftCardListId(), memberId, "0");
            }
        });
    }

    private void totalMemberAddNew(MemberDesignationMemberList memberDesignationMemberList, BigDecimal price) {

        String tId = "";

        //是否有团队管理id
        if (StringUtils.isNotBlank(memberDesignationMemberList.getTManageId())) {
            tId = memberDesignationMemberList.getTManageId();
        }

        //获取上级id,封装成队列
        while (StringUtils.isNotBlank(tId)) {
            MemberDesignationMemberList designationMemberList = iMemberDesignationMemberListService.getOne(new LambdaQueryWrapper<MemberDesignationMemberList>()
                    .eq(MemberDesignationMemberList::getMemberDesignationGroupId, memberDesignationMemberList.getMemberDesignationGroupId())
                    .eq(MemberDesignationMemberList::getMemberListId, tId)
                    .orderByAsc(MemberDesignationMemberList::getCreateTime)
                    .last("limit 1")
           ,false );
            if (designationMemberList!=null) {
                iMemberDesignationMemberListService.updateById(designationMemberList
                        .setTotalMembers(designationMemberList.getTotalMembers().add(new BigDecimal(1)))
                        .setTotalGiftSales(designationMemberList.getTotalGiftSales().add(price))
                );

                MemberDesignationCount memberDesignationCount = iMemberDesignationCountService.getOne(new LambdaQueryWrapper<MemberDesignationCount>()
                        .eq(MemberDesignationCount::getMemberDesignationId, memberDesignationMemberList.getMemberDesignationId())
                        .eq(MemberDesignationCount::getMemberListId, designationMemberList.getMemberListId())
                ,false);

                if (memberDesignationCount!=null) {
                    iMemberDesignationCountService.saveOrUpdate(memberDesignationCount
                            .setTotalMembers(memberDesignationCount.getTotalMembers().add(new BigDecimal(1))));
                } else {
                    iMemberDesignationCountService.save(new MemberDesignationCount()
                            .setMemberListId(designationMemberList.getMemberListId())
                            .setMemberDesignationId(memberDesignationMemberList.getMemberDesignationId())
                            .setTotalMembers(new BigDecimal(1))
                    );
                }

                if (StringUtils.isNotBlank(designationMemberList.getTManageId())) {
                    tId = designationMemberList.getTManageId();
                } else {
                    tId = "";
                }
            } else {
                tId = "";
            }

        }
    }

    @Override
    @Transactional
    public void marketingGiftBagMemberWelfarePaymentsUpdate(Object memberId, MarketingGiftBag marketingGiftBag, MarketingGiftBagRecord marketingGiftBagRecord) {
        MemberList memberList = iMemberListService.getById(memberId.toString());
        if (marketingGiftBag.getWelfarePayments().intValue() > 0) {
            memberList.setWelfarePayments(memberList.getWelfarePayments().add(marketingGiftBag.getWelfarePayments()));
            //行成用户福利金记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
            memberWelfarePayments.setDelFlag("0");
            memberWelfarePayments.setMemberListId(memberList.getId());
            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());
            memberWelfarePayments.setBargainPayments(marketingGiftBag.getWelfarePayments());
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());
            memberWelfarePayments.setWeType("1");
            memberWelfarePayments.setWpExplain("购买礼包[" + marketingGiftBagRecord.getId() + "]");
            memberWelfarePayments.setGoAndCome("平台");
            memberWelfarePayments.setBargainTime(new Date());
            memberWelfarePayments.setOperator("系统");
            memberWelfarePayments.setIsPlatform("1");
            memberWelfarePayments.setIsFreeze("0");
            memberWelfarePayments.setTradeNo(marketingGiftBagRecord.getId());
            memberWelfarePayments.setTradeType("1");
            memberWelfarePayments.setTradeStatus("5");
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
            log.info("礼包购买送福利金：" + marketingGiftBag.getWelfarePayments() + "---会员信息：" + memberList.getNickName());

            iMarketingWelfarePaymentsService.save(new MarketingWelfarePayments()
                    .setDelFlag("0")
                    .setMemberListId(memberList.getId())
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setBargainPayments(marketingGiftBag.getWelfarePayments())
                    .setWelfarePayments(memberList.getWelfarePayments())
                    .setWeType("1")
                    .setGiveExplain("购买礼包[" + memberWelfarePayments.getSerialNumber() + "]")
                    .setGoAndCome(memberList.getPhone())
                    .setBargainTime(new Date())
                    .setOperator("系统")
                    .setStatus("1")
                    .setPayMode("2")
                    .setSendUser("平台")
                    .setIsPlatform("1")
                    .setUserType("0")
                    .setTradeNo(memberWelfarePayments.getSerialNumber())
                    .setTradeType("1")
            );
        }
        iMemberListService.saveOrUpdate(memberList);
    }

    @Override
    @Transactional
    public void marketingGiftBagDiscountUpdate(Object memberId, MarketingGiftBag marketingGiftBag) {
        MemberList memberList = iMemberListService.getById(memberId.toString());
        QueryWrapper<MarketingGiftBagDiscount> marketingGiftBagDiscountQueryWrapper = new QueryWrapper<>();
        marketingGiftBagDiscountQueryWrapper.eq("marketing_gift_bag_id", marketingGiftBag.getId());
        List<MarketingGiftBagDiscount> marketingGiftBagDiscounts = iMarketingGiftBagDiscountService.list(marketingGiftBagDiscountQueryWrapper);
        for (MarketingGiftBagDiscount mgb : marketingGiftBagDiscounts) {
            Boolean isContinuous=false;
            if(mgb.getValidityType().equals("0")){
                isContinuous=true;
            }
            iMarketingDiscountService.generate(mgb.getMarketingDiscountId(),mgb.getDistributedAmount(),memberList.getId(),isContinuous);
        }
        log.info("礼包购买发放优惠券：" + marketingGiftBagDiscounts);
    }

    @Override
    @Transactional
    public void marketingGiftBagCertificateUpdate(Object memberId,
                                                  MarketingGiftBag marketingGiftBag,
                                                  String distributionChannel) {
        QueryWrapper<MarketingGiftBagCertificate> marketingGiftBagCertificateQueryWrapper = new QueryWrapper<>();
        marketingGiftBagCertificateQueryWrapper.eq("marketing_gift_bag_id", marketingGiftBag.getId());
        marketingGiftBagCertificateQueryWrapper.eq("del_flag", "0");
        List<MarketingGiftBagCertificate> marketingGiftBagCertificates = iMarketingGiftBagCertificateService.list(marketingGiftBagCertificateQueryWrapper);
        for (MarketingGiftBagCertificate mgc : marketingGiftBagCertificates) {
            //生成兑换券
            iMarketingCertificateService.generate(mgc.getMarketingCertificateId(), distributionChannel, mgc.getDistributedAmount(), memberId.toString(), true);
        }
        log.info("礼包购买发放兑换券：" + marketingGiftBagCertificates);
    }

    @Override
    @Transactional
    public void marketingGiftBagDistributionCommissionUpdate(Object memberId, MarketingGiftBag marketingGiftBag, MarketingGiftBagRecord marketingGiftBagRecord) {
        MemberList memberList = iMemberListService.getById(memberId.toString());
        LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId, memberList.getSysUserId())
                .in(StoreManage::getPayStatus, "1", "2");
        if (marketingGiftBag.getDistributionCommission().doubleValue() > 0) {
            log.info("礼包购买奖励发放开始");
            //礼包资金的分配
            //获取常量信息
            //推广奖励
            BigDecimal promoterReward = marketingGiftBag.getPromoterReward();
            //归属店铺奖励
            BigDecimal ownershipShopsReward = marketingGiftBag.getOwnershipShopsReward();

            //渠道销售奖励
            BigDecimal channelShopsReward = marketingGiftBag.getChannelShopsReward();

            //推广二级奖励
            BigDecimal promoterRewardTwo = marketingGiftBag.getPromoterRewardTwo();

            //归属奖励
            if (ownershipShopsReward.doubleValue() > 0 && StringUtils.isNotBlank(memberList.getSysUserId())) {
                if (iStoreManageService.count(storeManageLambdaQueryWrapper) > 0) {
                    StoreManage storeManage = iStoreManageService.list(storeManageLambdaQueryWrapper).get(0);
                    //店铺余额明细
                    StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                    storeRechargeRecord.setStoreManageId(storeManage.getId());
                    storeRechargeRecord.setPayType("5");
                    storeRechargeRecord.setGoAndCome("0");
                    storeRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(ownershipShopsReward.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                    storeRechargeRecord.setTradeStatus("5");
                    storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                    storeRechargeRecord.setOperator("系统");
                    storeRechargeRecord.setRemark("归属会员购买礼包[" + marketingGiftBagRecord.getId() + "]");//2020年6月13日21:17:15
                    storeRechargeRecord.setTradeType("1");
                    storeRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                    iStoreRechargeRecordService.save(storeRechargeRecord);

                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    iStoreManageService.saveOrUpdate(storeManage);

                    //店铺资金流水
                    StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                    storeAccountCapital.setStoreManageId(storeManage.getId());
                    storeAccountCapital.setPayType("5");
                    storeAccountCapital.setGoAndCome("0");
                    storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
                    storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                    storeAccountCapital.setBalance(storeManage.getBalance());
                    iStoreAccountCapitalService.save(storeAccountCapital);

                    log.info("会员购买礼包：" + storeRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBag.getDistributionCommission() + "--归属店铺：" + storeManage.getStoreName());
                }
            }

            //渠道店铺奖励
            if (channelShopsReward.doubleValue() > 0 && StringUtils.isNotBlank(marketingGiftBagRecord.getDistributionChannel())) {
                QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                storeManageQueryWrapper.eq("sys_user_id", marketingGiftBagRecord.getDistributionChannel());
                storeManageQueryWrapper.in("pay_status", "1", "2");
                if (iStoreManageService.count(storeManageQueryWrapper) > 0) {
                    StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                    //店铺余额明细
                    StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                    storeRechargeRecord.setStoreManageId(storeManage.getId());
                    storeRechargeRecord.setPayType("6");
                    storeRechargeRecord.setGoAndCome("0");
                    storeRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(channelShopsReward.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                    storeRechargeRecord.setTradeStatus("5");
                    storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                    storeRechargeRecord.setOperator("系统");
                    storeRechargeRecord.setRemark("渠道会员购买礼包[" + marketingGiftBagRecord.getId() + "]");
                    storeRechargeRecord.setTradeType("1");
                    storeRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                    iStoreRechargeRecordService.save(storeRechargeRecord);

                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    iStoreManageService.saveOrUpdate(storeManage);

                    //店铺资金流水
                    StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                    storeAccountCapital.setStoreManageId(storeManage.getId());
                    storeAccountCapital.setPayType("6");
                    storeAccountCapital.setGoAndCome("0");
                    storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
                    storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                    storeAccountCapital.setBalance(storeManage.getBalance());
                    iStoreAccountCapitalService.save(storeAccountCapital);

                    log.info("会员购买礼包：" + storeRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBag.getDistributionCommission() + "--渠道店铺：" + storeManage.getStoreName());
                }
            }

            //推广奖励
            if (promoterReward.doubleValue() > 0 && StringUtils.isNotBlank(memberList.getPromoterType())) {
                //店铺
                if (StringUtils.isBlank(marketingGiftBag.getMemberDesignationId()) && memberList.getPromoterType().equals("0")) {
                    QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                    storeManageQueryWrapper.eq("sys_user_id", memberList.getPromoter());
                    storeManageQueryWrapper.in("pay_status", "1", "2");
                    StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                    //店铺余额明细
                    StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                    storeRechargeRecord.setStoreManageId(storeManage.getId());
                    storeRechargeRecord.setPayType("8");//2020年5月23日16:36:23 原为6:渠道销售奖励 现改为8礼包推广奖励
                    storeRechargeRecord.setGoAndCome("0");
                    storeRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(promoterReward.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                    storeRechargeRecord.setTradeStatus("5");
                    storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                    storeRechargeRecord.setOperator("系统");
                    storeRechargeRecord.setRemark("推广会员购买礼包[" + marketingGiftBagRecord.getId() + "]");
                    storeRechargeRecord.setTradeType("1");
                    storeRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
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
                    log.info("会员购买礼包：" + storeRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBag.getDistributionCommission() + "--推广店铺：" + storeManage.getStoreName());
                }
                if (StringUtils.isBlank(marketingGiftBag.getMemberDesignationId())) {
                    //会员 分销团队
                    if (memberList.getPromoterType().equals("1")) {

                        MemberList proterMemberList = iMemberListService.getById(memberList.getPromoter());
                        //会员余额明细
                        MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                        memberRechargeRecord.setMemberListId(proterMemberList.getId());
                        memberRechargeRecord.setPayType("5");
                        memberRechargeRecord.setGoAndCome("0");
                        memberRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(promoterReward.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                        memberRechargeRecord.setTradeStatus("5");
                        memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                        memberRechargeRecord.setOperator("系统");
                        memberRechargeRecord.setRemark("会员购买礼包一级分销奖励[" + marketingGiftBagRecord.getId() + "]");
                        memberRechargeRecord.setTMemberListId(marketingGiftBagRecord.getMemberListId());
                        memberRechargeRecord.setTradeType("1");
                        memberRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                        memberRechargeRecord.setMemberLevel("1"); //2020年7月19日22:06:01新增分销成员级别
                        iMemberRechargeRecordService.save(memberRechargeRecord);

                        MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                        memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                        memberDistributionRecord.setGoodPicture(marketingGiftBag.getMainPicture());
                        memberDistributionRecord.setGoodName(marketingGiftBag.getGiftName());
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
                        memberAccountCapital.setPayType("5");
                        memberAccountCapital.setGoAndCome("0");
                        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                        memberAccountCapital.setBalance(proterMemberList.getBalance());
                        iMemberAccountCapitalService.save(memberAccountCapital);
                        log.info("会员购买礼包：" + memberRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBag.getDistributionCommission() + "--推广一级会员：" + proterMemberList.getNickName());
                        if (promoterRewardTwo.doubleValue() > 0 && proterMemberList.getPromoterType().equals("1")) {
                            MemberList proterMemberListTwo = iMemberListService.getById(proterMemberList.getPromoter());

                            //会员余额明细
                            MemberRechargeRecord memberRechargeRecordTwo = new MemberRechargeRecord();
                            memberRechargeRecordTwo.setMemberListId(proterMemberListTwo.getId());
                            memberRechargeRecordTwo.setPayType("5");
                            memberRechargeRecordTwo.setGoAndCome("0");
                            memberRechargeRecordTwo.setAmount(marketingGiftBag.getDistributionCommission().multiply(promoterRewardTwo.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                            memberRechargeRecordTwo.setTradeStatus("5");
                            memberRechargeRecordTwo.setOrderNo(OrderNoUtils.getOrderNo());
                            memberRechargeRecordTwo.setOperator("系统");
                            memberRechargeRecordTwo.setRemark("会员购买礼包二级分销奖励[" + marketingGiftBagRecord.getId() + "]");
                            memberRechargeRecordTwo.setTMemberListId(marketingGiftBagRecord.getMemberListId());
                            memberRechargeRecordTwo.setTradeType("1");
                            memberRechargeRecordTwo.setTradeNo(marketingGiftBagRecord.getId());
                            memberRechargeRecordTwo.setMemberLevel("2"); //2020年7月19日22:06:01新增分销成员级别
                            iMemberRechargeRecordService.save(memberRechargeRecordTwo);

                            MemberDistributionRecord memberDistributionRecordTwo = new MemberDistributionRecord();
                            memberDistributionRecordTwo.setMemberRechargeRecordId(memberRechargeRecordTwo.getId());
                            memberDistributionRecordTwo.setGoodPicture(marketingGiftBag.getMainPicture());
                            memberDistributionRecordTwo.setGoodName(marketingGiftBag.getGiftName());
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
                            memberAccountCapitalTwo.setPayType("5");
                            memberAccountCapitalTwo.setGoAndCome("0");
                            memberAccountCapitalTwo.setAmount(memberRechargeRecordTwo.getAmount());
                            memberAccountCapitalTwo.setOrderNo(memberRechargeRecordTwo.getOrderNo());
                            memberAccountCapitalTwo.setBalance(proterMemberListTwo.getBalance());
                            iMemberAccountCapitalService.save(memberAccountCapitalTwo);
                            log.info("会员购买礼包：" + memberRechargeRecordTwo.getAmount() + "---礼包金额：" + marketingGiftBag.getDistributionCommission() + "--推广二级会员：" + proterMemberList.getNickName());
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(marketingGiftBagRecord.getTMemberId())) {
                        MemberDesignation memberDesignation = iMemberDesignationService.getById(marketingGiftBag.getMemberDesignationId());
                        //称号团队
                        MemberList proterMember = iMemberListService.getById(marketingGiftBagRecord.getTMemberId());

                        if (proterMember != null) {

                            //会员余额明细
                            MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                            memberRechargeRecord.setMemberListId(proterMember.getId());
                            memberRechargeRecord.setPayType("5");
                            memberRechargeRecord.setGoAndCome("0");
                            memberRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(promoterReward.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                            memberRechargeRecord.setTradeStatus("5");
                            memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                            memberRechargeRecord.setOperator("系统");
                            memberRechargeRecord.setRemark("会员购买礼包一级分销奖励[" + marketingGiftBagRecord.getId() + "]");
                            memberRechargeRecord.setTMemberListId(marketingGiftBagRecord.getMemberListId());
                            memberRechargeRecord.setTradeType("1");
                            memberRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                            memberRechargeRecord.setMemberLevel("1"); //2020年7月19日22:06:01新增分销成员级别
                            iMemberRechargeRecordService.save(memberRechargeRecord);

                            MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                            memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                            memberDistributionRecord.setGoodPicture(marketingGiftBag.getMainPicture());
                            memberDistributionRecord.setGoodName(marketingGiftBag.getGiftName());
                            memberDistributionRecord.setGoodSpecification("-");
                            memberDistributionRecord.setCommission(memberRechargeRecord.getAmount());
                            iMemberDistributionRecordService.save(memberDistributionRecord);

                            proterMember.setBalance(proterMember.getBalance().add(memberRechargeRecord.getAmount()));
                            if (proterMember.getTotalCommission() == null) {
                                proterMember.setTotalCommission(new BigDecimal(0));
                            }
                            proterMember.setTotalCommission(proterMember.getTotalCommission().add(memberRechargeRecord.getAmount()));
                            iMemberListService.saveOrUpdate(proterMember);

                            //会员资金流水
                            MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
                            memberAccountCapital.setMemberListId(proterMember.getId());
                            memberAccountCapital.setPayType("5");
                            memberAccountCapital.setGoAndCome("0");
                            memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                            memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                            memberAccountCapital.setBalance(proterMember.getBalance());
                            iMemberAccountCapitalService.save(memberAccountCapital);
                            log.info("会员购买礼包：" + memberRechargeRecord.getAmount() + "---礼包金额：" + marketingGiftBag.getDistributionCommission() + "--推广一级会员：" + proterMember.getNickName());
                            MemberDesignationMemberList proterMemberList = iMemberDesignationMemberListService.getOne(new LambdaQueryWrapper<MemberDesignationMemberList>()
                                    .eq(MemberDesignationMemberList::getMemberListId, proterMember.getId())
                                    .eq(MemberDesignationMemberList::getMemberDesignationGroupId, memberDesignation.getMemberDesignationGroupId())
                                    .last("limit 1")
                            );
                            if (proterMemberList != null){
                                if (promoterRewardTwo.doubleValue() > 0 && StringUtils.isNotBlank(proterMemberList.getOldTManageId())) {
                                    MemberList proterMemberListTwo = iMemberListService.getById(proterMemberList.getOldTManageId());
                                    //会员余额明细
                                    MemberRechargeRecord memberRechargeRecordTwo = new MemberRechargeRecord();
                                    memberRechargeRecordTwo.setMemberListId(proterMemberListTwo.getId());
                                    memberRechargeRecordTwo.setPayType("5");
                                    memberRechargeRecordTwo.setGoAndCome("0");
                                    memberRechargeRecordTwo.setAmount(marketingGiftBag.getDistributionCommission().multiply(promoterRewardTwo.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                    memberRechargeRecordTwo.setTradeStatus("5");
                                    memberRechargeRecordTwo.setOrderNo(OrderNoUtils.getOrderNo());
                                    memberRechargeRecordTwo.setOperator("系统");
                                    memberRechargeRecordTwo.setRemark("会员购买礼包二级分销奖励[" + marketingGiftBagRecord.getId() + "]");
                                    memberRechargeRecordTwo.setTMemberListId(marketingGiftBagRecord.getMemberListId());
                                    memberRechargeRecordTwo.setTradeType("1");
                                    memberRechargeRecordTwo.setTradeNo(marketingGiftBagRecord.getId());
                                    memberRechargeRecordTwo.setMemberLevel("2"); //2020年7月19日22:06:01新增分销成员级别
                                    iMemberRechargeRecordService.save(memberRechargeRecordTwo);

                                    MemberDistributionRecord memberDistributionRecordTwo = new MemberDistributionRecord();
                                    memberDistributionRecordTwo.setMemberRechargeRecordId(memberRechargeRecordTwo.getId());
                                    memberDistributionRecordTwo.setGoodPicture(marketingGiftBag.getMainPicture());
                                    memberDistributionRecordTwo.setGoodName(marketingGiftBag.getGiftName());
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
                                    memberAccountCapitalTwo.setPayType("5");
                                    memberAccountCapitalTwo.setGoAndCome("0");
                                    memberAccountCapitalTwo.setAmount(memberRechargeRecordTwo.getAmount());
                                    memberAccountCapitalTwo.setOrderNo(memberRechargeRecordTwo.getOrderNo());
                                    memberAccountCapitalTwo.setBalance(proterMemberListTwo.getBalance());
                                    iMemberAccountCapitalService.save(memberAccountCapitalTwo);
                                    log.info("会员购买礼包：" + memberRechargeRecordTwo.getAmount() + "---礼包金额：" + marketingGiftBag.getDistributionCommission() + "--推广二级会员：" + proterMemberListTwo.getNickName());
                                }
                            }
                        }
                    }
                }

            }
            //加盟商控制锁
            boolean isExecute = true;
            //查出会员归属加盟商
            StoreManage storeManage = iStoreManageService.getOne(storeManageLambdaQueryWrapper);

            if (oConvertUtils.isNotEmpty(storeManage)) {
                AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                        .eq(AllianceManage::getDelFlag, "0")
                        .eq(AllianceManage::getStatus, "1")
                        .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                if (oConvertUtils.isNotEmpty(allianceManage)) {
                    //独享控制
                    if (allianceManage.getProfitType().equals("0")) {
                        isExecute = false;
                    }
                    if (marketingGiftBag.getIsAllianceAward().equals("1")) {
                        if (oConvertUtils.isNotEmpty(allianceManage)) {
                            if (allianceManage.getGiftCommissionRate().doubleValue() > 0 && allianceManage.getStatus().equals("1")) {
                                //加盟商余额明细
                                AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                                allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                                allianceRechargeRecord.setPayType("4");
                                allianceRechargeRecord.setGoAndCome("0");
                                //2020年10月16日 共享加盟商新增比例
                                if (allianceManage.getProfitType().equals("1")) {
                                    allianceRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(allianceManage.getGiftCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                } else {
                                    allianceRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(allianceManage.getGiftCommissionRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                }
                                allianceRechargeRecord.setTradeStatus("5");
                                allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                allianceRechargeRecord.setTradeType("1");
                                allianceRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                                allianceRechargeRecord.setRemark("礼包分成[" + marketingGiftBagRecord.getId() + "]");
                                iAllianceRechargeRecordService.save(allianceRechargeRecord);

                                allianceManage.setBalance(allianceManage.getBalance().add(allianceRechargeRecord.getAmount()));
                                iAllianceManageService.saveOrUpdate(allianceManage);

                                //代理资金明细
                                AllianceAccountCapital allianceAccountCapital = new AllianceAccountCapital();
                                allianceAccountCapital.setSysUserId(allianceManage.getSysUserId());
                                allianceAccountCapital.setPayType("4");
                                allianceAccountCapital.setGoAndCome("0");
                                allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                                allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                                allianceAccountCapital.setBalance(allianceManage.getBalance());
                                iAllianceAccountCapitalService.save(allianceAccountCapital);

                                log.info("礼包购买奖励：" + allianceRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBag.getDistributionCommission() + "--推荐加盟商：" + allianceManage.getId());
                            }
                        }
                    }
                }

            }
            //礼包控制要不要送给代理
            if (marketingGiftBag.getIsAgencyAward().equals("1")) {

                if (isExecute) {

                    //代理资金分配
                    List<String> sysAreas = Lists.newArrayList();
                    //回去第一次支付的支付日志
                    PayGiftBagLog payGiftBagLog=iPayGiftBagLogService.getOne(new LambdaQueryWrapper<PayGiftBagLog>()
                            .eq(PayGiftBagLog::getPayStatus,"1")
                            .eq(PayGiftBagLog::getMarketingGiftBagRecordId,marketingGiftBagRecord.getId())
                            .orderByAsc(PayGiftBagLog::getCreateTime));
                    if (StringUtils.isNotBlank(payGiftBagLog.getLatitude())) {
                        String adCode = tengxunMapUtils.findAdcode(payGiftBagLog.getLatitude() + "," + payGiftBagLog.getLongitude());
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
                                //2020年10月16日 新增共享加盟商比例规则分配
                                if (sysArea1.getLeve() == 2 && oConvertUtils.isNotEmpty(storeManage)) {
                                    AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                                            .eq(AllianceManage::getDelFlag, "0")
                                            .eq(AllianceManage::getStatus, "1")
                                            .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                                    if (oConvertUtils.isNotEmpty(allianceManage)) {
                                        if (allianceManage.getMutualAdvantages().equals("0")) {
                                            if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                                AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                                if (agencyManage != null && agencyManage.getGiftCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                                    //代理余额明细
                                                    AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                    agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                    agencyRechargeRecord.setPayType("4");
                                                    agencyRechargeRecord.setGoAndCome("0");
                                                    agencyRechargeRecord.setTradeStatus("5");
                                                    agencyRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(allianceManage.getGiftCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                                    agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                    agencyRechargeRecord.setTradeType("1");
                                                    agencyRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                                                    agencyRechargeRecord.setRemark("礼包分成[" + marketingGiftBagRecord.getId() + "]");
                                                    iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                    agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                                    iAgencyManageService.saveOrUpdate(agencyManage);

                                                    //代理资金明细
                                                    AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                                    agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                                    agencyAccountCapital.setPayType("4");
                                                    agencyAccountCapital.setGoAndCome("0");
                                                    agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                                    agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                                    agencyAccountCapital.setBalance(agencyManage.getBalance());
                                                    iAgencyAccountCapitalService.save(agencyAccountCapital);

                                                    log.info("礼包购买奖励：" + agencyRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBag.getDistributionCommission() + "--推荐代理：" + agencyManage.getId());
                                                }
                                            }
                                        } else {
                                            if (StringUtils.isNotBlank(allianceManage.getCountyId())) {
                                                SysArea area = iSysAreaService.getById(allianceManage.getCountyId());
                                                if (StringUtils.isNotBlank(area.getAgencyManageId())) {
                                                    AgencyManage agencyManage = iAgencyManageService.getById(area.getAgencyManageId());
                                                    if (agencyManage != null && agencyManage.getGiftCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                                        //代理余额明细
                                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                        agencyRechargeRecord.setPayType("4");
                                                        agencyRechargeRecord.setGoAndCome("0");
                                                        agencyRechargeRecord.setTradeStatus("5");
                                                        agencyRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(allianceManage.getGiftCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                        agencyRechargeRecord.setTradeType("1");
                                                        agencyRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                                                        agencyRechargeRecord.setRemark("礼包分成[" + marketingGiftBagRecord.getId() + "]");
                                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                                        //代理资金明细
                                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                                        agencyAccountCapital.setPayType("4");
                                                        agencyAccountCapital.setGoAndCome("0");
                                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                                        log.info("礼包购买奖励：" + agencyRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBag.getDistributionCommission() + "--推荐代理：" + agencyManage.getId());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                        AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                        if (agencyManage != null && agencyManage.getGiftCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                            //代理余额明细
                                            AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                            agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                            agencyRechargeRecord.setPayType("4");
                                            agencyRechargeRecord.setGoAndCome("0");
                                            agencyRechargeRecord.setTradeStatus("5");
                                            agencyRechargeRecord.setAmount(marketingGiftBag.getDistributionCommission().multiply(agencyManage.getGiftCommissionRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                            agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                            agencyRechargeRecord.setTradeType("1");
                                            agencyRechargeRecord.setTradeNo(marketingGiftBagRecord.getId());
                                            agencyRechargeRecord.setRemark("礼包分成[" + marketingGiftBagRecord.getId() + "]");
                                            iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                            agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                            iAgencyManageService.saveOrUpdate(agencyManage);

                                            //代理资金明细
                                            AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                            agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                            agencyAccountCapital.setPayType("4");
                                            agencyAccountCapital.setGoAndCome("0");
                                            agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                            agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                            agencyAccountCapital.setBalance(agencyManage.getBalance());
                                            iAgencyAccountCapitalService.save(agencyAccountCapital);

                                            log.info("礼包购买奖励：" + agencyRechargeRecord.getAmount() + "---礼包奖励金额：" + marketingGiftBag.getDistributionCommission() + "--推荐代理：" + agencyManage.getId());
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void paySuccess(Object memberId, String id) {
        PayGiftBagLog payGiftBagLog = iPayGiftBagLogService.getById(id);

        if(payGiftBagLog.getPayStatus().equals("1")){
            return;
        }

        //礼包余额扣除
        //扣除余额
        iMemberListService.subtractBlance(payGiftBagLog.getMemberListId(), payGiftBagLog.getBalance(), payGiftBagLog.getId(), "17");
        //扣除积分
        iMemberWelfarePaymentsService.subtractWelfarePayments(payGiftBagLog.getMemberListId(), payGiftBagLog.getWelfarePayments(), "25", payGiftBagLog.getId(),"");

        //状态修改成已支付
        payGiftBagLog.setPayStatus("1");
        //设置开通时间
        payGiftBagLog.setPayTime(new Date());


        //扣除礼包的库存
        MarketingGiftBag marketingGiftBag = this.getById(payGiftBagLog.getMarketingGiftBagId());
        marketingGiftBag.setRepertory(marketingGiftBag.getRepertory().subtract(new BigDecimal(1)));
        this.saveOrUpdate(marketingGiftBag);

        MarketingGiftBagRecord marketingGiftBagRecord=null;

        //判断礼包记录id是否存在
        if(StringUtils.isBlank(payGiftBagLog.getMarketingGiftBagRecordId())){
            //获取用户信息
            MemberList memberList=iMemberListService.getById(payGiftBagLog.getMemberListId());
            //生成未购买的礼包记录
            marketingGiftBagRecord=new MarketingGiftBagRecord();
            marketingGiftBagRecord.setMarketingGiftBagId(marketingGiftBag.getId());
            marketingGiftBagRecord.setMemberListId(payGiftBagLog.getMemberListId());
            marketingGiftBagRecord.setGiftNo(OrderNoUtils.getOrderNo());
            marketingGiftBagRecord.setGiftName(marketingGiftBag.getGiftName());
            marketingGiftBagRecord.setPrice(marketingGiftBag.getPrice());
            marketingGiftBagRecord.setPayStatus("1");
            marketingGiftBagRecord.setAffiliationStore(memberList.getSysUserId());//归属店铺
            marketingGiftBagRecord.setResiduePayTimes(marketingGiftBag.getPayTimes());//增加支付次数
            marketingGiftBagRecord.setDistributionChannel(payGiftBagLog.getSysUserId());//归属渠道
            marketingGiftBagRecord.setPromoter(memberList.getPromoter());
            marketingGiftBagRecord.setPromoterType(memberList.getPromoterType());//2020年7月28日11:48:32 新增推广人类型
            marketingGiftBagRecord.setMainPicture(marketingGiftBag.getMainPicture());
            marketingGiftBagRecord.setGiftDeals(marketingGiftBag.getGiftDeals());
            marketingGiftBagRecord.setBuyLimit(marketingGiftBag.getBuyLimit());
            marketingGiftBagRecord.setLimitTimes(marketingGiftBag.getLimitTimes());
            marketingGiftBagRecord.setGiftExplain(marketingGiftBag.getGiftExplain());
            marketingGiftBagRecord.setCoverPlan(marketingGiftBag.getCoverPlan());
            marketingGiftBagRecord.setPosters(marketingGiftBag.getPosters());
            marketingGiftBagRecord.setVipPrivilege(marketingGiftBag.getVipPrivilege());
            marketingGiftBagRecord.setTotalFee(payGiftBagLog.getAllTotalPrice());
            //礼包购买记录新增推广会员id
            if (StringUtils.isNotBlank(payGiftBagLog.getTMemberId())){
                marketingGiftBagRecord.setTMemberId(payGiftBagLog.getTMemberId());
            }
            marketingGiftBagRecord.setDistributionPrivileges(marketingGiftBag.getDistributionPrivileges());
            marketingGiftBagRecord.setTeamPrivileges(marketingGiftBag.getTeamPrivileges());
            iMarketingGiftBagRecordService.save(marketingGiftBagRecord);
            payGiftBagLog.setMarketingGiftBagRecordId(marketingGiftBagRecord.getId());
        }else{
            marketingGiftBagRecord=iMarketingGiftBagRecordService.getById(payGiftBagLog.getMarketingGiftBagRecordId());
            marketingGiftBagRecord.setTotalFee(marketingGiftBagRecord.getTotalFee().add(payGiftBagLog.getAllTotalPrice()));
        }

        iPayGiftBagLogService.saveOrUpdate(payGiftBagLog);
        //扣除支付次数
        marketingGiftBagRecord.setResiduePayTimes(marketingGiftBagRecord.getResiduePayTimes().subtract(new BigDecimal(1)));
        iMarketingGiftBagRecordService.saveOrUpdate(marketingGiftBagRecord);

        if(marketingGiftBagRecord.getResiduePayTimes().intValue()==0) {

            this.marketingGiftBagMemberGradeUpdate(memberId, marketingGiftBag, marketingGiftBagRecord);
            //称号团队
            this.marketingGiftBagMemberDesignationUpdateNew(memberId, marketingGiftBag, marketingGiftBagRecord);

            //会员增加福利金
            this.marketingGiftBagMemberWelfarePaymentsUpdate(memberId, marketingGiftBag, marketingGiftBagRecord);

            //优惠券分发
            this.marketingGiftBagDiscountUpdate(memberId, marketingGiftBag);
            //兑换券的发放
            this.marketingGiftBagCertificateUpdate(memberId, marketingGiftBag, marketingGiftBagRecord.getDistributionChannel());

            //礼品卡发放
            this.generateGiftCar(marketingGiftBag.getId(), marketingGiftBagRecord.getMemberListId());

            //资金分配
            this.marketingGiftBagDistributionCommissionUpdate(memberId, marketingGiftBag, marketingGiftBagRecord);
            //分销福利金
            if(marketingGiftBag.getPromoterWelfarePayments().doubleValue()>0&&StringUtils.isNotBlank(marketingGiftBagRecord.getTMemberId())){
                MemberList memberList = iMemberListService.getById(marketingGiftBagRecord.getTMemberId());
                if (marketingGiftBag.getPromoterWelfarePayments().intValue() > 0) {
                    memberList.setWelfarePayments(memberList.getWelfarePayments().add(marketingGiftBag.getPromoterWelfarePayments()));
                    //行成用户福利金记录
                    MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
                    memberWelfarePayments.setDelFlag("0");
                    memberWelfarePayments.setMemberListId(memberList.getId());
                    memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());
                    memberWelfarePayments.setBargainPayments(marketingGiftBag.getPromoterWelfarePayments());
                    memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());
                    memberWelfarePayments.setWeType("1");
                    memberWelfarePayments.setWpExplain("礼包分享福利金奖励[" + marketingGiftBagRecord.getId() + "]");
                    memberWelfarePayments.setGoAndCome("平台");
                    memberWelfarePayments.setBargainTime(new Date());
                    memberWelfarePayments.setOperator("系统");
                    memberWelfarePayments.setIsPlatform("1");
                    memberWelfarePayments.setIsFreeze("0");
                    memberWelfarePayments.setTradeNo(marketingGiftBagRecord.getId());
                    memberWelfarePayments.setTradeType("27");
                    memberWelfarePayments.setTradeStatus("5");
                    iMemberWelfarePaymentsService.save(memberWelfarePayments);
                    log.info("礼包购买一级推广送福利金：" + marketingGiftBag.getPromoterWelfarePayments() + "---会员信息：" + memberList.getNickName());

                    iMarketingWelfarePaymentsService.save(new MarketingWelfarePayments()
                            .setDelFlag("0")
                            .setMemberListId(memberList.getId())
                            .setSerialNumber(OrderNoUtils.getOrderNo())
                            .setBargainPayments(marketingGiftBag.getPromoterWelfarePayments())
                            .setWelfarePayments(memberList.getWelfarePayments())
                            .setWeType("1")
                            .setGiveExplain("礼包分享福利金奖励[" + memberWelfarePayments.getSerialNumber() + "]")
                            .setGoAndCome(memberList.getPhone())
                            .setBargainTime(new Date())
                            .setOperator("系统")
                            .setStatus("1")
                            .setPayMode("2")
                            .setSendUser("平台")
                            .setIsPlatform("1")
                            .setUserType("0")
                            .setTradeNo(memberWelfarePayments.getSerialNumber())
                            .setTradeType("8")
                    );
                }
                iMemberListService.saveOrUpdate(memberList);
            }

            //分享奖励
            if(marketingGiftBag.getShareRewards().doubleValue()>0){
                if(StringUtils.isNotBlank(payGiftBagLog.getTMemberId())){
                    iMemberListService.addBlance(payGiftBagLog.getTMemberId(),marketingGiftBag.getShareRewards(),marketingGiftBagRecord.getId(),"52");
                }
            }


            //给经销商分钱
            if(marketingGiftBag.getDealerAwards().doubleValue()>0){
                if(StringUtils.isNotBlank(payGiftBagLog.getSysUserId())){
                        StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(payGiftBagLog.getSysUserId());
                        //获取本会员的经销商
                        StoreFranchiser storeFranchiser=iStoreFranchiserService.findStoreFranchiser(payGiftBagLog.getMemberListId(),storeManage.getId());
                        if(storeFranchiser!=null){
                          iStoreFranchiserService.awardStoreFranchiser(storeFranchiser,marketingGiftBag.getDealerAwards(),payGiftBagLog.getMemberListId());
                        }else{
                            StoreFranchiser storeFranchiser1=iStoreFranchiserService.findStoreFranchiser(payGiftBagLog.getTMemberId(),storeManage.getId());
                            if(storeFranchiser1!=null){
                                iStoreFranchiserService.awardStoreFranchiser(storeFranchiser1,marketingGiftBag.getDealerAwards(),payGiftBagLog.getMemberListId());
                                iStoreFranchiserService.joinStoreFranchiser(storeFranchiser1,payGiftBagLog.getMemberListId());
                            }
                        }

                }

            }

        }

    }

    @Override
    public IPage<MarketingGiftBagVO> findMarketingGifiBagPageList(Page<MarketingGiftBagVO> page, MarketingGiftBagVO marketingGiftBagVO) {
        return baseMapper.findMarketingGifiBagPageList(page, marketingGiftBagVO);
    }

    @Override
    public String deleteAndDelExplain(String id, String delExplain) {
        try {
            baseMapper.deleteAndDelExplain(id, delExplain);
            return "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public Result<MarketingGiftBag> add(MarketingGiftBagVO marketingGiftBagVO) {
        Result<MarketingGiftBag> result = new Result<>();
        JSONArray discountIds = JSON.parseArray(marketingGiftBagVO.getDiscountIds());
        JSONArray certificateIds = JSON.parseArray(marketingGiftBagVO.getCertificateIds());
        //获取门店ids
        String storeIds = marketingGiftBagVO.getStoreIds();
        MarketingGiftBag marketingGiftBag = new MarketingGiftBag();
        if (marketingGiftBagVO.getIsPreposition().equals("0")) {
            marketingGiftBagVO.setPrepositionMarketingGiftBag("");
        }
        if (marketingGiftBagVO.getVipPrivilege().equals("0")) {
            marketingGiftBagVO.setSendVipMemberGradeId("");
        }
        BeanUtils.copyProperties(marketingGiftBagVO, marketingGiftBag);
        boolean b = this.save(marketingGiftBag);
        String marketingGiftBagId = marketingGiftBag.getId();

        if (oConvertUtils.isNotEmpty(certificateIds)) {
            for (int i = 0; i < certificateIds.size(); i++) {
                String marketingCertificateId = certificateIds.getJSONObject(i).getString("id");
                BigDecimal distributedAmount = certificateIds.getJSONObject(i).getBigDecimal("distributedAmount");
                String validityType = certificateIds.getJSONObject(i).getString("validityType");
                MarketingGiftBagCertificate marketingGiftBagCertificate = new MarketingGiftBagCertificate();
                marketingGiftBagCertificate.setMarketingGiftBagId(marketingGiftBagId);
                marketingGiftBagCertificate.setMarketingCertificateId(marketingCertificateId);
                marketingGiftBagCertificate.setDistributedAmount(distributedAmount);
                marketingGiftBagCertificate.setValidityType(validityType);
                marketingGiftBagCertificate.setDelFlag("0");
                iMarketingGiftBagCertificateService.save(marketingGiftBagCertificate);
            }
        }
        if (oConvertUtils.isNotEmpty(discountIds)) {
            for (int i = 0; i < discountIds.size(); i++) {
                String marketingDiscountId = discountIds.getJSONObject(i).getString("id");
                BigDecimal distributedAmount = discountIds.getJSONObject(i).getBigDecimal("distributedAmount");
                String validityType = discountIds.getJSONObject(i).getString("validityType");
                MarketingGiftBagDiscount marketingGiftBagDiscount = new MarketingGiftBagDiscount();
                marketingGiftBagDiscount.setMarketingGiftBagId(marketingGiftBagId);
                marketingGiftBagDiscount.setMarketingDiscountId(marketingDiscountId);
                marketingGiftBagDiscount.setDistributedAmount(distributedAmount);
                marketingGiftBagDiscount.setValidityType(validityType);
                marketingGiftBagDiscount.setDelFlag("0");
                iMarketingGiftBagDiscountService.save(marketingGiftBagDiscount);
            }
        }
        //礼品包
        if (StringUtils.isNotBlank(marketingGiftBagVO.getMarketingStoreGiftCardListJson())) {
            JSON.parseArray(marketingGiftBagVO.getMarketingStoreGiftCardListJson()).forEach(msgcl -> {
                JSONObject jsonObject = (JSONObject) msgcl;
                MarketingGiftBagCarList marketingGiftBagCarList = new MarketingGiftBagCarList();
                marketingGiftBagCarList.setMarketingGiftBagId(marketingGiftBagId);
                marketingGiftBagCarList.setMarketingStoreGiftCardListId(jsonObject.getString("id"));
                marketingGiftBagCarList.setQuantity(jsonObject.getBigDecimal("quantity"));
                iMarketingGiftBagCarListService.save(marketingGiftBagCarList);
            });
        }

        //判断门店的ids不为空时
        if (StringUtils.isNotBlank(storeIds)) {
            //转为list
            List<String> storeId = Arrays.asList(StringUtils.split(storeIds, ","));
            for (String s : storeId) {
                MarketingGiftBagStore marketingGiftBagStore = new MarketingGiftBagStore();
                marketingGiftBagStore.setMarketingGiftBagId(marketingGiftBagId);
                marketingGiftBagStore.setSysUserId(s);
                marketingGiftBagStore.setDelFlag("0");
                iMarketingGiftBagStoreService.save(marketingGiftBagStore);
            }
        }
        if (b) {
            result.setMessage("新增成功");
        } else {
            result.error500("新增失败");
        }
        return result;
    }

    @Override
    public Result<MarketingGiftBagDTO> edit(MarketingGiftBagVO marketingGiftBagVO) {
        Result<MarketingGiftBagDTO> result = new Result<>();
        //获取礼包id
        String id = marketingGiftBagVO.getId();
        //获取兑换券ids
        JSONArray certificateIds = JSON.parseArray(marketingGiftBagVO.getCertificateIds());
        //获取优惠券ids
        JSONArray discountIds = JSON.parseArray(marketingGiftBagVO.getDiscountIds());
        //获取门店ids
        String storeIds = marketingGiftBagVO.getStoreIds();

        MarketingGiftBag marketingGiftBag = new MarketingGiftBag();
        if (marketingGiftBagVO.getIsPreposition().equals("0")) {
            marketingGiftBagVO.setPrepositionMarketingGiftBag("");
        }
        if (marketingGiftBagVO.getVipPrivilege().equals("0")) {
            marketingGiftBagVO.setSendVipMemberGradeId("");
        }
        BeanUtils.copyProperties(marketingGiftBagVO, marketingGiftBag);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        marketingGiftBag.setUpdateBy(sysUser.getRealname());
        marketingGiftBag.setUpdateTime(new Date());
        if (oConvertUtils.isNotEmpty(certificateIds)) {
            //查出礼包对应的兑换券
            QueryWrapper<MarketingGiftBagCertificate> marketingGiftBagCertificateQueryWrapper = new QueryWrapper<>();
            marketingGiftBagCertificateQueryWrapper.eq("marketing_gift_bag_id", id);
            //删除礼包对应的兑换券
            iMarketingGiftBagCertificateService.remove(marketingGiftBagCertificateQueryWrapper);

            for (int i = 0; i < certificateIds.size(); i++) {
                String marketingCertificateId = certificateIds.getJSONObject(i).getString("id");
                BigDecimal distributedAmount = certificateIds.getJSONObject(i).getBigDecimal("distributedAmount");
                String validityType = certificateIds.getJSONObject(i).getString("validityType");
                MarketingGiftBagCertificate marketingGiftBagCertificate = new MarketingGiftBagCertificate();
                marketingGiftBagCertificate.setMarketingGiftBagId(id);
                marketingGiftBagCertificate.setMarketingCertificateId((String) marketingCertificateId);
                marketingGiftBagCertificate.setDistributedAmount((BigDecimal) distributedAmount);
                marketingGiftBagCertificate.setValidityType((String) validityType);
                marketingGiftBagCertificate.setDelFlag("0");
                iMarketingGiftBagCertificateService.save(marketingGiftBagCertificate);
            }
        } else {
            //查出礼包对应的兑换券
            QueryWrapper<MarketingGiftBagCertificate> marketingGiftBagCertificateQueryWrapper = new QueryWrapper<>();
            marketingGiftBagCertificateQueryWrapper.eq("marketing_gift_bag_id", id);
            //删除礼包对应的兑换券
            iMarketingGiftBagCertificateService.remove(marketingGiftBagCertificateQueryWrapper);
        }

        if (oConvertUtils.isNotEmpty(discountIds)) {
            QueryWrapper<MarketingGiftBagDiscount> marketingGiftBagDiscountQueryWrapper = new QueryWrapper<>();
            marketingGiftBagDiscountQueryWrapper.eq("marketing_gift_bag_id", id);
            iMarketingGiftBagDiscountService.remove(marketingGiftBagDiscountQueryWrapper);
            for (int i = 0; i < discountIds.size(); i++) {
                String marketingDiscountId = discountIds.getJSONObject(i).getString("id");
                BigDecimal distributedAmount = discountIds.getJSONObject(i).getBigDecimal("distributedAmount");
                String validityType = discountIds.getJSONObject(i).getString("validityType");
                MarketingGiftBagDiscount marketingGiftBagDiscount = new MarketingGiftBagDiscount();
                marketingGiftBagDiscount.setMarketingGiftBagId(id);
                marketingGiftBagDiscount.setMarketingDiscountId(marketingDiscountId);
                marketingGiftBagDiscount.setDistributedAmount(distributedAmount);
                marketingGiftBagDiscount.setValidityType(validityType);
                marketingGiftBagDiscount.setDelFlag("0");
                iMarketingGiftBagDiscountService.save(marketingGiftBagDiscount);
            }
        } else {
            QueryWrapper<MarketingGiftBagDiscount> marketingGiftBagDiscountQueryWrapper = new QueryWrapper<>();
            marketingGiftBagDiscountQueryWrapper.eq("marketing_gift_bag_id", id);
            iMarketingGiftBagDiscountService.remove(marketingGiftBagDiscountQueryWrapper);
        }
        if (oConvertUtils.isNotEmpty(storeIds)) {
            QueryWrapper<MarketingGiftBagStore> marketingGiftBagStoreQueryWrapper = new QueryWrapper<>();
            marketingGiftBagStoreQueryWrapper.eq("marketing_gift_bag_id", id);
            iMarketingGiftBagStoreService.remove(marketingGiftBagStoreQueryWrapper);
            //转为list
            List<String> storeId = Arrays.asList(StringUtils.split(storeIds, ","));
            //逐条插入
            storeId.forEach(g -> {
                MarketingGiftBagStore marketingGiftBagStore = new MarketingGiftBagStore();
                marketingGiftBagStore.setMarketingGiftBagId(id);
                marketingGiftBagStore.setSysUserId(g);
                marketingGiftBagStore.setDelFlag("0");
                iMarketingGiftBagStoreService.save(marketingGiftBagStore);
            });
        } else {
            QueryWrapper<MarketingGiftBagStore> marketingGiftBagStoreQueryWrapper = new QueryWrapper<>();
            marketingGiftBagStoreQueryWrapper.eq("marketing_gift_bag_id", id);
            iMarketingGiftBagStoreService.remove(marketingGiftBagStoreQueryWrapper);
        }

        //礼品包
        if (StringUtils.isNotBlank(marketingGiftBagVO.getMarketingStoreGiftCardListJson())) {

            iMarketingGiftBagCarListService.remove(new LambdaQueryWrapper<MarketingGiftBagCarList>()
                    .eq(MarketingGiftBagCarList::getMarketingGiftBagId,id));

            JSON.parseArray(marketingGiftBagVO.getMarketingStoreGiftCardListJson()).forEach(msgcl -> {
                JSONObject jsonObject = (JSONObject) msgcl;
                MarketingGiftBagCarList marketingGiftBagCarList = iMarketingGiftBagCarListService.getOne(new LambdaQueryWrapper<MarketingGiftBagCarList>()
                        .eq(MarketingGiftBagCarList::getMarketingGiftBagId, id)
                        .eq(MarketingGiftBagCarList::getMarketingStoreGiftCardListId, jsonObject.getString("id")));
                if (marketingGiftBagCarList == null) {
                    marketingGiftBagCarList = new MarketingGiftBagCarList();
                }
                marketingGiftBagCarList.setMarketingGiftBagId(id);
                marketingGiftBagCarList.setMarketingStoreGiftCardListId(jsonObject.getString("id"));
                marketingGiftBagCarList.setQuantity(jsonObject.getBigDecimal("quantity"));
                iMarketingGiftBagCarListService.saveOrUpdate(marketingGiftBagCarList);
            });
        } else {
            iMarketingGiftBagCarListService.remove(new LambdaQueryWrapper<MarketingGiftBagCarList>().eq(MarketingGiftBagCarList::getMarketingGiftBagId, id));
        }

        boolean b = this.saveOrUpdate(marketingGiftBag);
        if (b) {
            result.setMessage("修改成功");
        } else {
            result.error500("修改失败");
        }
        return result;
    }

    @Override
    public IPage<Map<String, Object>> isPrepositionList(Page<Map<String, Object>> page, MarketingGiftBagDTO marketingGiftBagDTO) {
        return baseMapper.isPrepositionList(page, marketingGiftBagDTO);
    }

}
