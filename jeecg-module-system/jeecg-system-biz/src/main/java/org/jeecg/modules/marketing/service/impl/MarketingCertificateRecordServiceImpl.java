package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.common.util.DateUtils;
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
import org.jeecg.modules.marketing.dto.MarketingCertificateRecordDTO;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingCertificateRecordMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.vo.MarketingCertificateRecordVO;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.pay.entity.PayCertificateLog;
import org.jeecg.modules.pay.service.IPayCertificateLogService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.service.ISysAreaService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingCertificateRecordServiceImpl extends ServiceImpl<MarketingCertificateRecordMapper, MarketingCertificateRecord> implements IMarketingCertificateRecordService {

    @Autowired
    private IPayCertificateLogService iPayCertificateLogService;

    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    private ISysAreaService iSysAreaService;

    @Autowired
    private IAgencyManageService iAgencyManageService;

    @Autowired
    private IAgencyRechargeRecordService iAgencyRechargeRecordService;

    @Autowired
    private IAgencyAccountCapitalService iAgencyAccountCapitalService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private IMemberDistributionRecordService iMemberDistributionRecordService;

    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;

    @Autowired
    @Lazy
    private IMarketingCertificateService iMarketingCertificateService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IAllianceManageService iAllianceManageService;

    @Autowired
    private IAllianceRechargeRecordService iAllianceRechargeRecordService;

    @Autowired
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;

    @Autowired
    @Lazy
    private IMemberGradeService iMemberGradeService;

    @Autowired
    @Lazy
    private IMemberGrowthRecordService iMemberGrowthRecordService;

    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    @Lazy
    private IMarketingCertificateGroupManageService iMarketingCertificateGroupManageService;
    @Autowired
    @Lazy
    private IMarketingCertificateGroupRecordService iMarketingCertificateGroupRecordService;
    @Autowired
    @Lazy
    private IMarketingCertificateSeckillRecordService iMarketingCertificateSeckillRecordService;
    @Autowired
    @Lazy
    private IMarketingCertificateGroupListService iMarketingCertificateGroupListService;

    @Autowired
    private IMarketingCertificateGroupBaseSettingService iMarketingCertificateGroupBaseSettingService;
    @Autowired
    @Lazy
    private IMarketingCertificateSeckillListService iMarketingCertificateSeckillListService;

    /**
     * 查询核销券
     * @param qqzixuangu 券码
     * @param sysUserId 店铺Id
     * @return
     */
      @Override
      public IPage<MarketingCertificateRecordDTO> couponVerification(Page<MarketingCertificateRecord> page, String qqzixuangu, String sysUserId){
        return   baseMapper.couponVerification(page,qqzixuangu,sysUserId);
      }

    @Override
    public IPage<Map<String, Object>> findMarketingCertificateRecordByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findMarketingCertificateRecordByMemberId(page,paramMap);
    }

    @Override
    public Map<String, Object> findMarketingCertificateRecordInfo(String id) {
        return baseMapper.findMarketingCertificateRecordInfo(id);
    }

    @Override
    public IPage<MarketingCertificateRecordVO> findCertificateRecord(Page<MarketingCertificateRecordVO> page, MarketingCertificateRecordVO marketingCertificateRecordVO) {
        return baseMapper.findCertificateRecord(page,marketingCertificateRecordVO);
    }

    @Override
    public IPage<MarketingCertificateRecordVO> findStoreCertificateRecord(Page<MarketingCertificateRecordVO> page, MarketingCertificateRecordVO marketingCertificateRecordVO) {
        return baseMapper.findStoreCertificateRecord(page,marketingCertificateRecordVO);
    }

    @Override
    @Transactional
    public Map<String,Object> paySuccess(PayCertificateLog payCertificateLog) {
        Map<String, Object> map = new HashMap<>();
        //状态修改成已支付
        payCertificateLog.setPayStatus("1");
        iPayCertificateLogService.saveOrUpdate(payCertificateLog);
        MemberList memberList=iMemberListService.getById(payCertificateLog.getMemberListId());
        String payLog = payCertificateLog.getPayLog();

        JSONObject jsonObject= JSON.parseObject(payLog);

        //获取兑换券信息
        MarketingCertificate marketingCertificate=iMarketingCertificateService.getById(jsonObject.getString("marketingCertificateId"));

        boolean create = false;

        if (payCertificateLog.getBuyType().equals("1")){
            MarketingCertificateGroupBaseSetting marketingCertificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                    .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0"));
            MarketingCertificateGroupList marketingCertificateGroupList = iMarketingCertificateGroupListService.getById(jsonObject.getString("marketingCertificateGroupListId"));

            MarketingCertificateGroupManage marketingCertificateGroupManage = new MarketingCertificateGroupManage();
            marketingCertificateGroupManage
                    .setGroupNo(OrderNoUtils.getOrderNo())
                    .setMarketingCertificateGroupListId(jsonObject.getString("marketingCertificateGroupListId"))
                    .setCertificateName(marketingCertificate.getName())
                    .setMainPicture(marketingCertificate.getMainPicture())
                    .setMarketPrice(marketingCertificate.getMarketPrice())
                    .setPrice(marketingCertificate.getPrice())
                    .setCostPrice(marketingCertificate.getCostPrice())
                    .setActivityPrice(marketingCertificateGroupList.getActivityPrice())
                    .setNumberClusters(marketingCertificateGroupList.getNumberClusters())
                    .setNumberTuxedo(new BigDecimal(1))
                    .setStatus("0")
                    .setStartTime(new Date());
            if (marketingCertificateGroupBaseSetting.getTimeType().equals("0")){
                marketingCertificateGroupManage.setEndTime(DateUtils.dateAddHour(marketingCertificateGroupBaseSetting.getTimeLimit().intValue()));
            }else {
                marketingCertificateGroupManage.setEndTime(DateUtils.dateAddMinute(marketingCertificateGroupBaseSetting.getTimeLimit().intValue()));
            }
            iMarketingCertificateGroupManageService.save(marketingCertificateGroupManage);
            MarketingCertificateGroupRecord marketingCertificateGroupRecord = new MarketingCertificateGroupRecord()
                    .setMarketingCertificateGroupManageId(marketingCertificateGroupManage.getId())
                    .setTuxedoNumber(OrderNoUtils.getOrderNo())
                    .setMarketingCertificateGroupListId(marketingCertificateGroupList.getId())
                    .setCertificateName(marketingCertificate.getName())
                    .setMainPicture(marketingCertificate.getMainPicture())
                    .setMarketPrice(marketingCertificate.getMarketPrice())
                    .setPrice(marketingCertificate.getPrice())
                    .setCostPrice(marketingCertificate.getCostPrice())
                    .setActivityPrice(marketingCertificateGroupList.getActivityPrice())
                    .setMemberListId(payCertificateLog.getMemberListId())
                    .setGroupIdentity("1")
                    .setStatus("0")
                    .setStartTime(marketingCertificateGroupManage.getStartTime())
                    .setEndTime(marketingCertificateGroupManage.getEndTime())
                    .setPayCertificateLogId(payCertificateLog.getId());

            iMarketingCertificateGroupRecordService.save(marketingCertificateGroupRecord);
            map.put("marketingCertificateGroupRecordId",marketingCertificateGroupRecord.getId());
        }
        if (payCertificateLog.getBuyType().equals("0")||payCertificateLog.getBuyType().equals("2")){
            create = true;
        }
        List<MarketingCertificateRecord> marketingCertificateRecordList = null;
        //生成券
        if (create){
            marketingCertificateRecordList =  iMarketingCertificateService.generate(marketingCertificate.getId(),
                    payCertificateLog.getSysUserId(),
                    jsonObject.getBigDecimal("quantity"),
                    payCertificateLog.getMemberListId(),
                    false);
        }

        if (payCertificateLog.getBuyType().equals("3")){

            MarketingCertificateGroupManage marketingCertificateGroupManage = iMarketingCertificateGroupManageService.getById(jsonObject.getString("marketingCertificateGroupManageId"));

            MarketingCertificateGroupList marketingCertificateGroupList = iMarketingCertificateGroupListService.getById(marketingCertificateGroupManage.getMarketingCertificateGroupListId());
            MarketingCertificateGroupRecord marketingCertificateGroupRecord = new MarketingCertificateGroupRecord()
                    .setMarketingCertificateGroupManageId(marketingCertificateGroupManage.getId())
                    .setTuxedoNumber(OrderNoUtils.getOrderNo())
                    .setMarketingCertificateGroupListId(marketingCertificateGroupList.getId())
                    .setCertificateName(marketingCertificate.getName())
                    .setMainPicture(marketingCertificate.getMainPicture())
                    .setMarketPrice(marketingCertificate.getMarketPrice())
                    .setPrice(marketingCertificate.getPrice())
                    .setCostPrice(marketingCertificate.getCostPrice())
                    .setActivityPrice(marketingCertificateGroupList.getActivityPrice())
                    .setMemberListId(payCertificateLog.getMemberListId())
                    .setGroupIdentity("1")
                    .setStatus("0")
                    .setPayCertificateLogId(payCertificateLog.getId())
                    .setStartTime(marketingCertificateGroupManage.getStartTime())
                    .setEndTime(marketingCertificateGroupManage.getEndTime());
            iMarketingCertificateGroupRecordService.save(marketingCertificateGroupRecord);
            marketingCertificateGroupManage.setNumberTuxedo(marketingCertificateGroupManage.getNumberTuxedo().add(new BigDecimal(1)));
            if (marketingCertificateGroupManage.getNumberClusters().doubleValue()>=marketingCertificateGroupManage.getNumberTuxedo().doubleValue()){
                marketingCertificateGroupManage.setCloudsTime(new Date());
                marketingCertificateGroupManage.setStatus("1");
                List<MarketingCertificateGroupRecord> certificateGroupRecordList = iMarketingCertificateGroupRecordService.list(new LambdaQueryWrapper<MarketingCertificateGroupRecord>()
                        .eq(MarketingCertificateGroupRecord::getDelFlag, "0")
                        .eq(MarketingCertificateGroupRecord::getMarketingCertificateGroupManageId, marketingCertificateGroupManage.getId())
                );
                certificateGroupRecordList.forEach(cgl->{
                    MarketingCertificateRecord marketingCertificateRecord = iMarketingCertificateService.generate(marketingCertificate.getId(),
                            "",
                            new BigDecimal(1),
                            cgl.getMemberListId(),
                            false).get(0);
                    iMarketingCertificateGroupRecordService.updateById(cgl
                            .setStatus("1")
                            .setCloudsTime(marketingCertificateGroupManage.getCloudsTime())
                            .setMarketingCertificateRecordId(marketingCertificateRecord.getId())
                    );
                    this.updateById(marketingCertificateRecord
                            .setPayCertificateLogId(payCertificateLog.getId())
                            .setRecordType("2")
                            .setActivePrice(cgl.getActivityPrice())
                    );
                });
            }
            iMarketingCertificateGroupManageService.updateById(marketingCertificateGroupManage);
            map.put("marketingCertificateGroupRecordId",marketingCertificateGroupRecord.getId());
        }
        if (payCertificateLog.getBuyType().equals("2")){
            if (marketingCertificateRecordList != null){
                MarketingCertificateSeckillList marketingCertificateSeckillList = iMarketingCertificateSeckillListService.getById(jsonObject.getString("marketingCertificateSeckillListId"));

                marketingCertificateRecordList.forEach(mcr->{
                    this.updateById(mcr
                            .setPayCertificateLogId(payCertificateLog.getId())
                            .setRecordType("1")
                            .setActivePrice(marketingCertificateSeckillList.getActivityPrice())

                    );
                });
                MarketingCertificateSeckillRecord marketingCertificateSeckillRecord = new MarketingCertificateSeckillRecord();
                marketingCertificateSeckillRecord
                        .setMarketingCertificateSeckillActivityListId(marketingCertificateSeckillList.getMarketingCertificateSeckillActivityListId())
                        .setMarketingCertificateSeckillListId(marketingCertificateSeckillList.getId())
                        .setMarketingCertificateId(marketingCertificateSeckillList.getMarketingCertificateId())
                        .setCertificateName(marketingCertificate.getName())
                        .setMainPicture(marketingCertificate.getMainPicture())
                        .setMarketPrice(marketingCertificate.getMarketPrice())
                        .setPrice(marketingCertificate.getPrice())
                        .setCostPrice(marketingCertificate.getCostPrice())
                        .setActivityPrice(marketingCertificateSeckillList.getActivityPrice())
                        .setMemberListId(payCertificateLog.getMemberListId())
                        .setQuantity(jsonObject.getBigDecimal("quantity"))
                        .setBuyTime(new Date())
                        .setSerialNumber(OrderNoUtils.getOrderNo())
                        .setPayCertificateLogId(payCertificateLog.getId());
                iMarketingCertificateSeckillRecordService.save(marketingCertificateSeckillRecord);
            }
        }
        if (payCertificateLog.getBuyType().equals("0")){
            if (marketingCertificateRecordList != null){
            marketingCertificateRecordList.forEach(marketingCertificateRecord->{
                String buyExchangeCertificate = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "buy_exchange_certificate");

                LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                        .eq(MemberGrade::getDelFlag,"0")
                        .eq(MemberGrade::getStatus,"1");

                if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0&&buyExchangeCertificate.equals("1")){
                    if (StringUtils.isNotBlank(memberList.getMemberGradeId())){

                        memberList.setGrowthValue(memberList.getGrowthValue().add(marketingCertificateRecord.getPrice()));

                        MemberGrade memberGrade = iMemberGradeService.getById(memberList.getMemberGradeId());

                        if (memberList.getGrowthValue().doubleValue() > memberGrade.getGrowthValueBig().doubleValue()){

                            memberGradeLambdaQueryWrapper.le(MemberGrade::getGrowthValueSmall, memberList.getGrowthValue())
                                    .ge(MemberGrade::getGrowthValueBig, memberList.getGrowthValue())
                                    .orderByAsc(MemberGrade::getSort)
                            ;
                            if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0){
                                MemberGrade grade = iMemberGradeService.list(memberGradeLambdaQueryWrapper).get(0);
                                if (memberList.getGrowthValue().doubleValue()>grade.getGrowthValueSmall().doubleValue()){
                                    memberList.setMemberGradeId(grade.getId());
                                }

                            }
                        }
                    }else {
                        memberList.setGrowthValue(memberList.getGrowthValue().add(marketingCertificateRecord.getPrice()));

                        memberGradeLambdaQueryWrapper.eq(MemberGrade::getStatus, "1")
                                .le(MemberGrade::getGrowthValueSmall, memberList.getGrowthValue())
                                .ge(MemberGrade::getGrowthValueBig, memberList.getGrowthValue())
                                .orderByAsc(MemberGrade::getSort)
                        ;

                        if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0){
                            if (memberList.getMemberType().equals("0")){
                                memberList.setMemberType("1");
                                memberList.setVipTime(new Date());
                            }
                            memberList.setMemberGradeId(iMemberGradeService.list(memberGradeLambdaQueryWrapper).get(0).getId());
                        }else {
                            MemberGrade grade = iMemberGradeService.list(new LambdaQueryWrapper<MemberGrade>()
                                    .eq(MemberGrade::getDelFlag, "0")
                                    .eq(MemberGrade::getStatus, "1")
                                    .orderByDesc(MemberGrade::getSort)).get(0);
                            if (grade.getGrowthValueBig().doubleValue()<=memberList.getGrowthValue().doubleValue()){
                                if (memberList.getMemberType().equals("0")){
                                    memberList.setMemberType("1");
                                    memberList.setVipTime(new Date());
                                }
                                memberList.setMemberGradeId(grade.getId());
                            }
                        }
                    }
                    iMemberListService.saveOrUpdate(memberList);
                    iMemberGrowthRecordService.save(new MemberGrowthRecord()
                            .setMemberListId(memberList.getId())
                            .setTradeNo(marketingCertificateRecord.getQqzixuangu())
                            .setTradeType("3")
                            .setRemark("兑换券购买["+marketingCertificateRecord.getQqzixuangu()+"]")
                            .setGrowthValue(marketingCertificateRecord.getPrice())
                            .setOrderNo(OrderNoUtils.getOrderNo())
                    );
                }
                boolean isExecute=true;

                StoreManage storeManage = iStoreManageService.getOne(new LambdaUpdateWrapper<StoreManage>()
                        .eq(StoreManage::getSysUserId, memberList.getSysUserId())
                        .in(StoreManage::getPayStatus,"1","2"));

                //加盟商
                if (oConvertUtils.isNotEmpty(storeManage)){
                    if (StringUtils.isNotBlank(storeManage.getAllianceUserId())){
                        AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                                .eq(AllianceManage::getDelFlag,"0")
                                .eq(AllianceManage::getStatus,"1")
                                .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                        if (oConvertUtils.isNotEmpty(allianceManage)){
                            //独享控制
                            if(allianceManage.getProfitType().equals("0")){
                                isExecute=false;
                            }
                            if (allianceManage.getCashCouponSalesIncentives().doubleValue() > 0&& allianceManage.getStatus().equals("1") && marketingCertificate.getPromoteCommission().doubleValue()>0) {

                                BigDecimal totalPrice = new BigDecimal(0);

                                //代理余额明细
                                AllianceRechargeRecord allianceRechargeRecord  = new AllianceRechargeRecord();
                                allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                                allianceRechargeRecord.setPayType("6");
                                allianceRechargeRecord.setGoAndCome("0");
                                if (allianceManage.getProfitType().equals("0")){
                                    allianceRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(allianceManage.getCashCouponSalesIncentives().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                }else {
                                    allianceRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(allianceManage.getCashCouponSalesIncentives().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                                }
                                allianceRechargeRecord.setTradeStatus("5");
                                allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                allianceRechargeRecord.setTradeType("2");
                                allianceRechargeRecord.setTradeNo(marketingCertificateRecord.getQqzixuangu());
                                allianceRechargeRecord.setRemark("兑换券购买奖励 ["+marketingCertificateRecord.getQqzixuangu()+"]");
                                iAllianceRechargeRecordService.save(allianceRechargeRecord);

                                allianceManage.setBalance(allianceManage.getBalance().add(allianceRechargeRecord.getAmount()));
                                iAllianceManageService.saveOrUpdate(allianceManage);

                                //代理资金明细
                                AllianceAccountCapital allianceAccountCapital = new AllianceAccountCapital();
                                allianceAccountCapital.setSysUserId(allianceManage.getSysUserId());
                                allianceAccountCapital.setPayType("6");
                                allianceAccountCapital.setGoAndCome("0");
                                allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                                allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                                allianceAccountCapital.setBalance(allianceManage.getBalance());
                                iAllianceAccountCapitalService.save(allianceAccountCapital);

                                log.info("兑换券购买奖励：" + allianceRechargeRecord.getAmount() + "---兑换券奖励金额：" + payCertificateLog.getTotalFee() + "--推荐特通：" + allianceManage.getId());

                            }
                        }
                    }
                }



                if(isExecute) {
                    //代理资金分配
                    List<String> sysAreas = Lists.newArrayList();
                    if (payCertificateLog.getLatitude().doubleValue() > 0 && payCertificateLog.getLongitude().doubleValue() > 0) {
                        String adCode = tengxunMapUtils.findAdcode(payCertificateLog.getLatitude().toString() + "," + payCertificateLog.getLongitude().toString());
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
                                if(sysArea1.getLeve()==2&&oConvertUtils.isNotEmpty(storeManage)){
                                    if (StringUtils.isNotBlank(storeManage.getAllianceUserId())){
                                        AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                                                .eq(AllianceManage::getDelFlag,"0")
                                                .eq(AllianceManage::getStatus,"1")
                                                .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                                        if (oConvertUtils.isNotEmpty(allianceManage)){
                                            if (allianceManage.getMutualAdvantages().equals("0")){
                                                if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                                    AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                                    if (agencyManage != null && agencyManage.getCashCouponSalesIncentives().doubleValue() > 0&& agencyManage.getStatus().equals("1") && marketingCertificate.getPromoteCommission().doubleValue()>0) {

                                                        BigDecimal totalPrice = new BigDecimal(0);

                                                        //推广员
                                                        //会员
                                                        if (agencyManage.getPromoterCommissionRate().doubleValue() > 0&&marketingCertificate.getPromoteCommission().doubleValue()>0) {
                                                            if (memberList.getPromoterType().equals("1")) {

                                                                MemberList proterMemberList = iMemberListService.getById(memberList.getPromoter());

                                                                //会员余额明细
                                                                MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                                                                memberRechargeRecord.setMemberListId(proterMemberList.getId());
                                                                memberRechargeRecord.setPayType("7");
                                                                memberRechargeRecord.setGoAndCome("0");
                                                                memberRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(allianceManage.getCashCouponSalesIncentives().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).multiply(agencyManage.getPromoterCommissionRate().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                                                                memberRechargeRecord.setTradeStatus("5");
                                                                memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                                memberRechargeRecord.setOperator("系统");
                                                                memberRechargeRecord.setRemark("会员购买兑换券 [" + marketingCertificateRecord.getQqzixuangu()+"]");
                                                                memberRechargeRecord.setTMemberListId(payCertificateLog.getMemberListId());
                                                                memberRechargeRecord.setTradeType("2");
                                                                memberRechargeRecord.setTradeNo(marketingCertificateRecord.getQqzixuangu());

                                                                iMemberRechargeRecordService.save(memberRechargeRecord);

                                                                MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                                                                memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                                                                memberDistributionRecord.setGoodPicture(marketingCertificate.getMainPicture());
                                                                memberDistributionRecord.setGoodName(marketingCertificate.getName());
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
                                                                memberAccountCapital.setPayType("7");
                                                                memberAccountCapital.setGoAndCome("0");
                                                                memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                                                                memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                                                                memberAccountCapital.setBalance(proterMemberList.getBalance());
                                                                iMemberAccountCapitalService.save(memberAccountCapital);
                                                                totalPrice = memberRechargeRecord.getAmount();
                                                                log.info("兑换券购买礼包：" + memberRechargeRecord.getAmount() + "---兑换券金额：" + payCertificateLog.getTotalFee() + "--推广会员：" + proterMemberList.getNickName());
                                                            }

                                                        }

                                                        //代理余额明细
                                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                        agencyRechargeRecord.setPayType("6");
                                                        agencyRechargeRecord.setGoAndCome("0");
                                                        agencyRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(allianceManage.getCashCouponSalesIncentives().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).divide(new BigDecimal(100))).subtract(totalPrice).setScale(2,RoundingMode.DOWN));
                                                        agencyRechargeRecord.setTradeStatus("5");
                                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                        agencyRechargeRecord.setTradeType("2");
                                                        agencyRechargeRecord.setTradeNo(marketingCertificateRecord.getQqzixuangu());
                                                        agencyRechargeRecord.setRemark("兑换券购买奖励 ["+marketingCertificateRecord.getQqzixuangu()+"]");
                                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                                        //代理资金明细
                                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                                        agencyAccountCapital.setPayType("6");
                                                        agencyAccountCapital.setGoAndCome("0");
                                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                                        log.info("兑换券购买奖励：" + agencyRechargeRecord.getAmount() + "---兑换券奖励金额：" + payCertificateLog.getTotalFee() + "--推荐代理：" + agencyManage.getId());

                                                    }
                                                }
                                            }else {
                                                if (StringUtils.isNotBlank(allianceManage.getCountyId())){
                                                    SysArea area = iSysAreaService.getById(allianceManage.getCountyId());
                                                    if (StringUtils.isNotBlank(area.getAgencyManageId())) {
                                                        AgencyManage agencyManage = iAgencyManageService.getById(area.getAgencyManageId());
                                                        if (agencyManage != null && agencyManage.getCashCouponSalesIncentives().doubleValue() > 0&& agencyManage.getStatus().equals("1") && marketingCertificate.getPromoteCommission().doubleValue()>0) {

                                                            BigDecimal totalPrice = new BigDecimal(0);

                                                            //推广员
                                                            //会员
                                                            if (agencyManage.getPromoterCommissionRate().doubleValue() > 0&&marketingCertificate.getPromoteCommission().doubleValue()>0) {
                                                                if (memberList.getPromoterType().equals("1")) {

                                                                    MemberList proterMemberList = iMemberListService.getById(memberList.getPromoter());

                                                                    //会员余额明细
                                                                    MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                                                                    memberRechargeRecord.setMemberListId(proterMemberList.getId());
                                                                    memberRechargeRecord.setPayType("7");
                                                                    memberRechargeRecord.setGoAndCome("0");
                                                                    memberRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(allianceManage.getCashCouponSalesIncentives().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).multiply(agencyManage.getPromoterCommissionRate().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                                                                    memberRechargeRecord.setTradeStatus("5");
                                                                    memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                                    memberRechargeRecord.setOperator("系统");
                                                                    memberRechargeRecord.setRemark("会员购买兑换券 [" + marketingCertificateRecord.getQqzixuangu()+"]");
                                                                    memberRechargeRecord.setTMemberListId(payCertificateLog.getMemberListId());
                                                                    memberRechargeRecord.setTradeType("2");
                                                                    memberRechargeRecord.setTradeNo(marketingCertificateRecord.getQqzixuangu());

                                                                    iMemberRechargeRecordService.save(memberRechargeRecord);

                                                                    MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                                                                    memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                                                                    memberDistributionRecord.setGoodPicture(marketingCertificate.getMainPicture());
                                                                    memberDistributionRecord.setGoodName(marketingCertificate.getName());
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
                                                                    memberAccountCapital.setPayType("7");
                                                                    memberAccountCapital.setGoAndCome("0");
                                                                    memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                                                                    memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                                                                    memberAccountCapital.setBalance(proterMemberList.getBalance());
                                                                    iMemberAccountCapitalService.save(memberAccountCapital);
                                                                    totalPrice = memberRechargeRecord.getAmount();
                                                                    log.info("兑换券购买礼包：" + memberRechargeRecord.getAmount() + "---兑换券金额：" + payCertificateLog.getTotalFee() + "--推广会员：" + proterMemberList.getNickName());
                                                                }

                                                            }

                                                            //代理余额明细
                                                            AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                            agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                            agencyRechargeRecord.setPayType("6");
                                                            agencyRechargeRecord.setGoAndCome("0");
                                                            agencyRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(allianceManage.getCashCouponSalesIncentives().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).divide(new BigDecimal(100))).subtract(totalPrice).setScale(2,RoundingMode.DOWN));
                                                            agencyRechargeRecord.setTradeStatus("5");
                                                            agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                            agencyRechargeRecord.setTradeType("2");
                                                            agencyRechargeRecord.setTradeNo(marketingCertificateRecord.getQqzixuangu());
                                                            agencyRechargeRecord.setRemark("兑换券购买奖励 ["+marketingCertificateRecord.getQqzixuangu()+"]");
                                                            iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                            agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                                            iAgencyManageService.saveOrUpdate(agencyManage);

                                                            //代理资金明细
                                                            AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                                            agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                                            agencyAccountCapital.setPayType("6");
                                                            agencyAccountCapital.setGoAndCome("0");
                                                            agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                                            agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                                            agencyAccountCapital.setBalance(agencyManage.getBalance());
                                                            iAgencyAccountCapitalService.save(agencyAccountCapital);

                                                            log.info("兑换券购买奖励：" + agencyRechargeRecord.getAmount() + "---兑换券奖励金额：" + payCertificateLog.getTotalFee() + "--推荐代理：" + agencyManage.getId());

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }else {
                                    if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                        AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                        if (agencyManage != null && agencyManage.getCashCouponSalesIncentives().doubleValue() > 0&& agencyManage.getStatus().equals("1") && marketingCertificate.getPromoteCommission().doubleValue()>0) {

                                            BigDecimal totalPrice = new BigDecimal(0);

                                            //推广员
                                            //会员
                                            if (agencyManage.getPromoterCommissionRate().doubleValue() > 0&&marketingCertificate.getPromoteCommission().doubleValue()>0) {
                                                if (memberList.getPromoterType().equals("1")) {

                                                    MemberList proterMemberList = iMemberListService.getById(memberList.getPromoter());

                                                    //会员余额明细
                                                    MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                                                    memberRechargeRecord.setMemberListId(proterMemberList.getId());
                                                    memberRechargeRecord.setPayType("7");
                                                    memberRechargeRecord.setGoAndCome("0");
                                                    memberRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(agencyManage.getCashCouponSalesIncentives().divide(new BigDecimal(100))).multiply(agencyManage.getPromoterCommissionRate().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                                                    memberRechargeRecord.setTradeStatus("5");
                                                    memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                    memberRechargeRecord.setOperator("系统");
                                                    memberRechargeRecord.setRemark("会员购买兑换券 [" + marketingCertificateRecord.getQqzixuangu()+"]");
                                                    memberRechargeRecord.setTMemberListId(payCertificateLog.getMemberListId());
                                                    memberRechargeRecord.setTradeType("2");
                                                    memberRechargeRecord.setTradeNo(marketingCertificateRecord.getQqzixuangu());

                                                    iMemberRechargeRecordService.save(memberRechargeRecord);

                                                    MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                                                    memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                                                    memberDistributionRecord.setGoodPicture(marketingCertificate.getMainPicture());
                                                    memberDistributionRecord.setGoodName(marketingCertificate.getName());
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
                                                    memberAccountCapital.setPayType("7");
                                                    memberAccountCapital.setGoAndCome("0");
                                                    memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                                                    memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                                                    memberAccountCapital.setBalance(proterMemberList.getBalance());
                                                    iMemberAccountCapitalService.save(memberAccountCapital);
                                                    totalPrice = memberRechargeRecord.getAmount();
                                                    log.info("兑换券购买礼包：" + memberRechargeRecord.getAmount() + "---兑换券金额：" + payCertificateLog.getTotalFee() + "--推广会员：" + proterMemberList.getNickName());
                                                }

                                            }

                                            //代理余额明细
                                            AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                            agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                            agencyRechargeRecord.setPayType("6");
                                            agencyRechargeRecord.setGoAndCome("0");
                                            agencyRechargeRecord.setAmount(marketingCertificate.getPromoteCommission().multiply(agencyManage.getCashCouponSalesIncentives().divide(new BigDecimal(100))).subtract(totalPrice).setScale(2,RoundingMode.DOWN));
                                            agencyRechargeRecord.setTradeStatus("5");
                                            agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                            agencyRechargeRecord.setTradeType("2");
                                            agencyRechargeRecord.setTradeNo(marketingCertificateRecord.getQqzixuangu());
                                            agencyRechargeRecord.setRemark("兑换券购买奖励 ["+marketingCertificateRecord.getQqzixuangu()+"]");
                                            iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                            agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                            iAgencyManageService.saveOrUpdate(agencyManage);

                                            //代理资金明细
                                            AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                            agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                            agencyAccountCapital.setPayType("6");
                                            agencyAccountCapital.setGoAndCome("0");
                                            agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                            agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                            agencyAccountCapital.setBalance(agencyManage.getBalance());
                                            iAgencyAccountCapitalService.save(agencyAccountCapital);

                                            log.info("兑换券购买奖励：" + agencyRechargeRecord.getAmount() + "---兑换券奖励金额：" + payCertificateLog.getTotalFee() + "--推荐代理：" + agencyManage.getId());

                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
            }
        }


        iMarketingCertificateService.saveOrUpdate(marketingCertificate);
        map.put("code","SUCCESS");
        return map;
    }

    @Override
    @Transactional
    public void updateMarketingCertificateRecordJob() {
        baseMapper.updateMarketingCertificateTakeEffect();
        baseMapper.updateMarketingCertificatePastDue();
    }

}
