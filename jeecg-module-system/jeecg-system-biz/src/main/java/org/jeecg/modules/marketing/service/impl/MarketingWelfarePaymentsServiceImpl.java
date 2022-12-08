package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import org.jeecg.modules.marketing.entity.MarketingWelfarePayments;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;
import org.jeecg.modules.marketing.mapper.MarketingWelfarePaymentsMapper;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.marketing.vo.MarketingDisplayVO;
import org.jeecg.modules.marketing.vo.MarketingWelfarePaymentsVO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 店铺福利金
 * @Author: jeecg-boot
 * @Date: 2019-11-16
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingWelfarePaymentsServiceImpl extends ServiceImpl<MarketingWelfarePaymentsMapper, MarketingWelfarePayments> implements IMarketingWelfarePaymentsService {
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    private IStoreAccountCapitalService iStoreAccountCapitalService;
    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private IMemberDistributionRecordService iMemberDistributionRecordService;

    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;

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
    private IAllianceManageService iAllianceManageService;

    @Autowired
    private IAllianceRechargeRecordService iAllianceRechargeRecordService;

    @Autowired
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;

    /**
     * 赠送福利金
     *
     * @param
     * @param
     * @return
     */
    @Override
    @Transactional
    public Result<MarketingWelfarePayments> updateWelfarePaymentsByBoosPhone(JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        BigDecimal welfarePayments = jsonObject.getBigDecimal("welfarePayments");
        String giveExplain = jsonObject.getString("giveExplain");
        Result<MarketingWelfarePayments> result = new Result<>();
        if (StringUtils.isBlank(id)) {
            return result.error500("信息异常,请选择会员");
        }
        if (oConvertUtils.isEmpty(welfarePayments)) {
            return result.error500("请输入金额");
        }
        //获取当前登录人
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查出当前登录人店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUser.getId());
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
        //获取当前登录人店铺福利金
        BigDecimal storeWelfarePayments = storeManage.getWelfarePayments();
        //获取当前登录人店铺余额
        BigDecimal balance = storeManage.getBalance();

        //生成福利金记录表
        MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
        //判断赠送福利金减去账户福利金
        if (storeWelfarePayments.subtract(welfarePayments).doubleValue() < 0) {
            BigDecimal newWelfarePayments = welfarePayments.subtract(storeWelfarePayments);
            //获取福利金比例
            QueryWrapper<MarketingWelfarePaymentsSetting> marketingWelfarePaymentsSettingQueryWrapper = new QueryWrapper<>();
            marketingWelfarePaymentsSettingQueryWrapper.eq("del_flag", "0");
            marketingWelfarePaymentsSettingQueryWrapper.eq("status", "1");
            MarketingWelfarePaymentsSetting welfarePaymentsSetting = iMarketingWelfarePaymentsSettingService.getOne(marketingWelfarePaymentsSettingQueryWrapper);

            if (oConvertUtils.isEmpty(welfarePaymentsSetting)){
                return result.error500("获取福利金比例失败,请查看福利金比例!");
            }
            BigDecimal proportion = welfarePaymentsSetting.getProportion();
            //根据福利金比例算出所需的余额
            BigDecimal b = newWelfarePayments.multiply(proportion.divide(new BigDecimal(100)));
            //判断店铺余额是否充足
            if (balance.subtract(b).doubleValue() > 0) {
                //扣去余额
                storeManage.setBalance(storeManage.getBalance().subtract(b));
                //写入福利金记录表中
                marketingWelfarePayments.setBalance(storeManage.getBalance());//账户剩余余额
                marketingWelfarePayments.setBalancePay(b);//余额支付
                marketingWelfarePayments.setPayMode("1");//支付方式
                marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                //店铺余额发生变动写入店铺余额记录中
                StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                storeRechargeRecord.setDelFlag("0");//删除状态
                storeRechargeRecord.setStoreManageId(storeManage.getId());//店铺id
                storeRechargeRecord.setPayType("3");//交易类型
                storeRechargeRecord.setGoAndCome("1");//支出
                storeRechargeRecord.setAmount(b);//交易金额
                storeRechargeRecord.setTradeStatus("5");//交易状态
                storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());//单号
                storeRechargeRecord.setOperator(sysUser.getRealname());//操作人
                storeRechargeRecord.setRemark("店铺赠送福利金["+marketingWelfarePayments.getSerialNumber()+"]");//备注
                storeRechargeRecord.setTradeNo(marketingWelfarePayments.getSerialNumber());
                iStoreRechargeRecordService.save(storeRechargeRecord);
                marketingWelfarePayments.setTradeNo(storeRechargeRecord.getOrderNo());//店铺赠送福利金交易单号
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


                //店铺余额充值福利金的利润分配
                //推荐开店奖励百分比
                String welfareSalesReward = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "welfare_sales_reward"), "%");
                //福利金推荐奖励类型：0：仅店铺；1：仅会员；2：店铺和会员；
                String welfareSalesRewardType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "welfare_sales_reward_type");
                //推荐人奖励
                if (new BigDecimal(welfareSalesReward).doubleValue() > 0 && StringUtils.isNotBlank(storeManage.getPromoterType())) {
                    //店铺
                    if (storeManage.getPromoterType().equals("0")) {
                        if (welfareSalesRewardType.equals("0")||welfareSalesRewardType.equals("2")){
                            QueryWrapper<StoreManage> storeManageQueryWrapper1 = new QueryWrapper<>();
                            storeManageQueryWrapper1.eq("sys_user_id", storeManage.getPromoter());
                            storeManageQueryWrapper1.eq("pay_status", "1");
                            if(iStoreManageService.count(storeManageQueryWrapper1)>0) {
                                StoreManage promoterStoreManage = iStoreManageService.list(storeManageQueryWrapper1).get(0);
                                //店铺余额明细
                                StoreRechargeRecord storeRechargeRecordw = new StoreRechargeRecord();
                                storeRechargeRecordw.setStoreManageId(promoterStoreManage.getId());
                                storeRechargeRecordw.setPayType("9");
                                storeRechargeRecordw.setGoAndCome("0");
                                storeRechargeRecordw.setAmount(b.multiply(new BigDecimal(welfareSalesReward).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
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
                                log.info("福利金销售奖励：" + storeRechargeRecordw.getAmount() + "---店铺金额：" + promoterStoreManage.getBalance() + "--推荐店铺：" + promoterStoreManage.getStoreName());
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
                                memberRechargeRecord.setAmount(b.multiply(new BigDecimal(welfareSalesReward).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                                memberRechargeRecord.setTradeStatus("5");
                                memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                memberRechargeRecord.setOperator("系统");
                                memberRechargeRecord.setRemark("福利金销售奖励[" + marketingWelfarePayments.getSerialNumber()+"]");
                                memberRechargeRecord.setTMemberListId(storeRechargeRecord.getOrderNo());
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
                                log.info("福利金销售奖励：" + memberRechargeRecord.getAmount() + "---会员金额：" + proterMemberList.getBalance() + "--推荐会员：" + proterMemberList.getNickName());
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
                                        .setScale(2,RoundingMode.DOWN));
                            }else {
                                allianceRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN).divide(new BigDecimal(100)))
                                        .setScale(2,RoundingMode.DOWN));
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

                            log.info("福利金销售奖励：" + allianceRechargeRecord.getAmount() + "---代理金额：" + allianceManage.getBalance() + "--推荐加盟商：" + allianceManage.getId());
                        }
                    }



                //代理奖励
                //代理资金分配

                //查询地址确定代理位置

                if(isExecute) {

                    if (storeManage.getLatitude().doubleValue() > 0 && storeManage.getLongitude().doubleValue() > 0) {
                        List<String> sysAreas = Arrays.asList(StringUtils.split(storeManage.getSysAreaId(), ","));
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
                                                    agencyRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN).divide(new BigDecimal(100)))
                                                            .setScale(2, RoundingMode.DOWN));
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
                                                        agencyRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN).divide(new BigDecimal(100)))
                                                                .setScale(2, RoundingMode.DOWN));
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
                                            agencyRechargeRecord.setAmount(b.multiply(agencyManage.getWelfareCommissionRate().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
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
            } else {
                //不足返回false
                return result.error500("余额不足,请充值");
            }
            //插入记录表中
            marketingWelfarePayments.setWelfarePay(storeManage.getWelfarePayments());//福利金支付
            marketingWelfarePayments.setWelfarePayments(new BigDecimal(0));//账户福利金
            storeManage.setWelfarePayments(new BigDecimal(0));

        } else {
            //如果福利金足够的情况下扣账户福利金
            storeManage.setWelfarePayments(storeManage.getWelfarePayments().subtract(welfarePayments));
            //插入记录表
            marketingWelfarePayments.setWelfarePay(welfarePayments);//福利金支付
            marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());//账户福利金
            marketingWelfarePayments.setBalance(storeManage.getBalance());//店铺账户余额
            marketingWelfarePayments.setBalancePay(new BigDecimal(0));//余额支付
            marketingWelfarePayments.setPayMode("2");//支付方式
            marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
        }
        boolean x = iStoreManageService.saveOrUpdate(storeManage);
        //获取赠送账号
        MemberList memberList = iMemberListService.getById(id);
        iMemberListService.saveOrUpdate(memberList
                .setWelfarePayments(memberList.getWelfarePayments().add(welfarePayments)));
        marketingWelfarePayments.setDelFlag("0");//删除状态
        marketingWelfarePayments.setSysUserId(sysUser.getId());//店铺id
        marketingWelfarePayments.setMemberListId(memberList.getId());//会员id
        marketingWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
        marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());
        marketingWelfarePayments.setWeType("0");//类型
        marketingWelfarePayments.setGiveExplain("店铺赠送会员["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//赠送说明
        marketingWelfarePayments.setGoAndCome(memberList.getPhone());//来源于去向
        marketingWelfarePayments.setBargainTime(new Date());//交易时间
        marketingWelfarePayments.setOperator(sysUser.getUsername());//操作人
        marketingWelfarePayments.setStatus("1");//支付状态
        marketingWelfarePayments.setUserType("0");//用户类型
        marketingWelfarePayments.setIsPlatform("0");//0,店铺; 1,平台
        marketingWelfarePayments.setSendUser(storeManage.getStoreName());//赠送人
        marketingWelfarePayments.setTradeType("3");
        this.save(marketingWelfarePayments);
        //写入会员福利金记录表中
        MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
        memberWelfarePayments.setDelFlag("0");
        memberWelfarePayments.setMemberListId(memberList.getId());//会员id
        memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
        memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
        memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
        memberWelfarePayments.setWeType("1");//交易类型: 0:支出 1:收入
        memberWelfarePayments.setWpExplain("店铺赠送["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//说明
        if (StringUtils.isBlank(storeManage.getSubStoreName())){
            memberWelfarePayments.setGoAndCome(storeManage.getStoreName());//来源或者去向
        }else {
            memberWelfarePayments.setGoAndCome(storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
        }
        memberWelfarePayments.setBargainTime(new Date());//交易时间
        memberWelfarePayments.setOperator(sysUser.getRealname());//操作人
        memberWelfarePayments.setIsPlatform("0");
        memberWelfarePayments.setIsFreeze("0");
        memberWelfarePayments.setTradeNo(marketingWelfarePayments.getSerialNumber());
        memberWelfarePayments.setTradeType("3");
        memberWelfarePayments.setTradeStatus("5");
        iMemberWelfarePaymentsService.save(memberWelfarePayments);
        if (x) {
            result.setCode(200);
            result.setMessage("成功");
        } else {
            result.error500("失败");
        }
        return result;
    }

    @Override
    public IPage<MarketingWelfarePaymentsVO> findWelfarePaymentsTotal(Page<MarketingWelfarePaymentsVO> page, MarketingWelfarePaymentsVO marketingWelfarePaymentsVO) {
        return baseMapper.findWelfarePaymentsTotal(page,marketingWelfarePaymentsVO);
    }

    @Override
    public IPage<MarketingWelfarePaymentsVO> findStoreWelfarePayments(Page<MarketingWelfarePaymentsVO> page,MarketingWelfarePaymentsVO marketingWelfarePaymentsVO) {
        return baseMapper.findStoreWelfarePayments(page,marketingWelfarePaymentsVO);
    }

    @Override
    public IPage<MarketingWelfarePaymentsVO> findWelfarePaymentsBuy(Page<MarketingWelfarePaymentsVO> page, MarketingWelfarePaymentsVO marketingWelfarePaymentsVO) {
        return baseMapper.findWelfarePaymentsBuy(page,marketingWelfarePaymentsVO);
    }

    @Override
    public IPage<MarketingWelfarePaymentsVO> findStoreWelfarePaymentsList(Page<MarketingWelfarePaymentsVO> page, MarketingWelfarePaymentsVO marketingWelfarePaymentsVO) {
        return baseMapper.findStoreWelfarePaymentsList(page,marketingWelfarePaymentsVO);
    }

    /**
     * 平台赠送福利金
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional
    public Result<StoreManage> updateStoreWelfarePayments(JSONObject jsonObject) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Result<StoreManage> result = new Result<>();
        String id = jsonObject.getString("id");
        BigDecimal welfarePayments = jsonObject.getBigDecimal("welfarePayments");
        String giveExplain = jsonObject.getString("giveExplain");
        String userType = jsonObject.getString("userType");
        if (StringUtils.isBlank(id)) {
            return result.error500("信息异常!,请选择会员或店铺");
        }
        if (oConvertUtils.isEmpty(welfarePayments)) {
            return result.error500("不能为空");

        }
        if (StringUtils.isBlank(userType)) {
            return result.error500("请选择用户类型");
        } else {
            if (userType.equals("0")) {
                    //通过手机号码查询出会员
                MemberList getMember = iMemberListService.getById(id);
                if (oConvertUtils.isEmpty(getMember)){
                    return result.error500("会员信息异常,请核对后再操作!");
                }
                boolean b = iMemberListService.saveOrUpdate(getMember.setWelfarePayments(getMember.getWelfarePayments().add(welfarePayments)));
                if (b) {
                    result.setMessage("成功");
                    //成功写入店铺福利金记录表
                    MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
                    marketingWelfarePayments.setDelFlag("0");//删除状态
                    marketingWelfarePayments.setMemberListId(getMember.getId());//会员id
                    marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                    marketingWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
                    marketingWelfarePayments.setWelfarePayments(getMember.getWelfarePayments());
                    marketingWelfarePayments.setWeType("1");//类型; 收入
                    marketingWelfarePayments.setGiveExplain("平台赠送["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//说明
                    marketingWelfarePayments.setGoAndCome("平台赠送");//来源or去向
                    marketingWelfarePayments.setBargainTime(new Date());//交易时间
                    marketingWelfarePayments.setOperator(sysUser.getRealname());//操作人
                    marketingWelfarePayments.setStatus("1");//交易状态
                    marketingWelfarePayments.setPayMode("2");//支付方式
                    marketingWelfarePayments.setSendUser("平台");//赠送人
                    marketingWelfarePayments.setIsPlatform("1");//0,店铺; 1,平台
                    marketingWelfarePayments.setUserType("0");//0：商城会员；1：店铺管理员
                    marketingWelfarePayments.setTradeType("2");
                    marketingWelfarePayments.setTradeNo(getMember.getId());
                    this.save(marketingWelfarePayments);
                    MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
                    memberWelfarePayments.setDelFlag("0");
                    memberWelfarePayments.setMemberListId(getMember.getId());//会员id
                    memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                    memberWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
                    memberWelfarePayments.setWelfarePayments(getMember.getWelfarePayments());//账户福利金
                    memberWelfarePayments.setWeType("1");//交易类型: 0:支出 1:收入
                    memberWelfarePayments.setWpExplain("平台赠送["+marketingWelfarePayments.getSerialNumber()+"]"+giveExplain);//说明
                    memberWelfarePayments.setGoAndCome("平台赠送");//来源或者去向
                    memberWelfarePayments.setBargainTime(new Date());//交易时间
                    memberWelfarePayments.setOperator(sysUser.getRealname());//操作人
                    memberWelfarePayments.setIsPlatform("1");
                    memberWelfarePayments.setIsFreeze("0");
                    memberWelfarePayments.setTradeNo(marketingWelfarePayments.getSerialNumber());
                    memberWelfarePayments.setTradeType("2");
                    memberWelfarePayments.setTradeStatus("5");
                    iMemberWelfarePaymentsService.save(memberWelfarePayments);

                } else {
                     result.error500("失败");
                }

            } else if (userType.equals("1")){
                //查出店铺账户
                StoreManage one = iStoreManageService.getById(id);
                if (oConvertUtils.isEmpty(one)){
                    return result.error500("店铺信息异常,请核对后再操作!");
                }
                boolean b = iStoreManageService.saveOrUpdate(one
                        .setWelfarePayments(one.getWelfarePayments().add(welfarePayments)));
                //判断修改成功与否
                if (b) {
                    //成功写入店铺福利金记录表
                    MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
                    marketingWelfarePayments.setDelFlag("0");//删除状态
                    marketingWelfarePayments.setSysUserId(one.getSysUserId());//店铺id
                    marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                    marketingWelfarePayments.setBargainPayments(welfarePayments);//交易福利金
                    marketingWelfarePayments.setWelfarePayments(one.getWelfarePayments());//账户福利金
                    marketingWelfarePayments.setWeType("1");//类型; 收入
                    marketingWelfarePayments.setGiveExplain("平台赠送["+one.getId()+"]"+giveExplain);
                    marketingWelfarePayments.setGoAndCome("平台赠送");//来源or去向
                    marketingWelfarePayments.setBargainTime(new Date());//交易时间
                    marketingWelfarePayments.setOperator(sysUser.getRealname());//操作人
                    marketingWelfarePayments.setBalance(one.getBalance());//店铺账户余额
                    marketingWelfarePayments.setStatus("1");//交易状态
                    marketingWelfarePayments.setPayMode("2");//支付方式
                    marketingWelfarePayments.setSendUser("平台");//赠送人s
                    marketingWelfarePayments.setIsPlatform("1");//0,店铺; 1,平台
                    marketingWelfarePayments.setUserType("1");//0：商城会员；1：店铺管理员
                    marketingWelfarePayments.setTradeType("4");
                    marketingWelfarePayments.setTradeNo(one.getId());
                    this.save(marketingWelfarePayments);
                    /*this.save(new MarketingWelfarePayments()
                            .setDelFlag("0")
                            .setSysUserId(marketingWelfarePayments.getSysUserId())
                            .setSerialNumber(OrderNoUtils.getOrderNo())
                            .setBargainPayments(marketingWelfarePayments.getBargainPayments())
                            .setWeType("0")
                            .setGiveExplain(marketingWelfarePayments.getGiveExplain())
                            .setGoAndCome("平台赠送")
                            .setBargainTime(new Date())
                            .setOperator("平台")
                            .setStatus("1")
                            .setUserType("1")
                            .setIsPlatform("1")
                            .setTradeNo(marketingWelfarePayments.getSerialNumber())
                            .setTradeType("4")
                    );*/
                    result.setMessage("成功");
                } else {
                    result.error500("赠送失败");
                }
            }
        }
        return result;
    }

    @Override
    public IPage<Map<String, Object>> findBackStoreWelfarePayments(Page<MarketingWelfarePayments> page, String weType, String sysUserId) {
        return baseMapper.findBackStoreWelfarePayments(page,weType,sysUserId);
    }

    @Override
    public List<Map<String, Object>> everydaySendOutWelfare() {
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int i = 7; i >0 ; i--) {
            //推算当前时间前一天日期
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR,-i+1);
            arrayList.add(calendar.get(Calendar.DAY_OF_MONTH));
                String s = calendar.get(Calendar.YEAR) + "-" + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1) + "", 2, "0") + "-" + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH)) + "", 2, "0");
                if (i==7){
                    map.put("data_begin",s);
                }
                if (i==1){
                    String s1 = calendar.get(Calendar.YEAR) + "-" + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1) + "", 2, "0") + "-" + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH)+1) + "", 2, "0");
                    map.put("data_end", s1);
                }
        }

        List<MarketingDisplayVO> marketingDisplayVOS = baseMapper.everydaySendOutWelfare(map);

        Map<Integer, BigDecimal> collect = marketingDisplayVOS.stream().collect(Collectors.
                toMap(MarketingDisplayVO::getMyDate, MarketingDisplayVO::getTotalPrice));

        ArrayList<Map<String, Object>> maps = new ArrayList<>();

        arrayList.forEach(arr->{
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("x",arr);
            hashMap.put("y",collect.getOrDefault(arr,new BigDecimal(0)));
            maps.add(hashMap);
        });
        return maps;
    }

    @Override
    public List<Map<String, Object>> storeComplimentary() {
        return baseMapper.storeComplimentary();
    }

}
