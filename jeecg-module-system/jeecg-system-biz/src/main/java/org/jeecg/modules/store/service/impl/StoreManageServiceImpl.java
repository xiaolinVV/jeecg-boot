package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ijpay.core.kit.WxPayKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.PasswordUtil;
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
import org.jeecg.modules.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.marketing.entity.MarketingCommingStore;
import org.jeecg.modules.marketing.entity.MarketingWelfarePayments;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;
import org.jeecg.modules.marketing.service.IMarketingCommingStoreService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;
import org.jeecg.modules.store.dto.StoreManageDTO;
import org.jeecg.modules.store.entity.*;
import org.jeecg.modules.store.mapper.StoreManageMapper;
import org.jeecg.modules.store.service.*;
import org.jeecg.modules.store.vo.StoreManageVO;
import org.jeecg.modules.store.vo.StoreWorkbenchVO;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date: 2019-10-14
 * @Version: V1.0
 */
@Service
@Slf4j
public class StoreManageServiceImpl extends ServiceImpl<StoreManageMapper, StoreManage> implements IStoreManageService {
    @Autowired
    @Lazy
    private IStoreWithdrawDepositService iStoreWithdrawDepositService;
    @Autowired
    @Lazy
    private IStoreRechargeRecordService iStoreRechargeRecordService;
    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    @Lazy
    private ISysUserService iSysUserService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private IStorePaymentService iStorePaymentService;

    @Autowired
    @Lazy
    private IMemberListService iMemberListService;

    @Autowired
    private IStoreAccountCapitalService iStoreAccountCapitalService;

    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;

    @Autowired
    private IMemberDistributionRecordService iMemberDistributionRecordService;

    @Autowired
    private ISysAreaService iSysAreaService;

    @Autowired
    private IAgencyManageService iAgencyManageService;

    @Autowired
    private IAgencyRechargeRecordService iAgencyRechargeRecordService;

    @Autowired
    private IAgencyAccountCapitalService iAgencyAccountCapitalService;

    @Autowired
    @Lazy
    private IMarketingWelfarePaymentsService iMarketingWelfarePaymentsService;

    @Autowired
    private WeixinQRUtils weixinQRUtils;

    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;
    @Autowired
    @Lazy
    private IStoreBankCardService iStoreBankCardService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IAllianceRechargeRecordService iAllianceRechargeRecordService;

    @Autowired
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;

    @Autowired
    @Lazy
    private IAllianceManageService iAllianceManageService;

    @Autowired
    private IMarketingCommingStoreService iMarketingCommingStoreService;

    @Autowired
    private IMemberDesignationGroupService iMemberDesignationGroupService;

    @Autowired
    private IStoreSettingService iStoreSettingService;


    @Override
    public List<Map<String, Object>> getAllStoreList() {
        return baseMapper.getAllStoreList();
    }

    @Override
    public Map<String, Object> setActivity(Map<String, Object> resultMap, String storeManageId) {
        //进店奖励
        //获取长期对应活动
        long takeWayCountScan = iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                .eq(MarketingCommingStore::getValidity, "0")
                .eq(MarketingCommingStore::getStatus, "1")
                .eq(MarketingCommingStore::getTakeWay,"0")
                .eq(MarketingCommingStore::getStoreManageId, storeManageId)
                .le(MarketingCommingStore::getStartTime, DateUtils.now()))+iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                .eq(MarketingCommingStore::getStoreManageId, storeManageId)
                .eq(MarketingCommingStore::getStatus, "1")
                .eq(MarketingCommingStore::getValidity, "1")
                .eq(MarketingCommingStore::getTakeWay,"0")
                .le(MarketingCommingStore::getStartTime, DateUtils.now())
                .ge(MarketingCommingStore::getEndTime, DateUtils.now()));
        if(takeWayCountScan==0){
            resultMap.put("isViewTakeWayCountScan","0");
        }else{
            resultMap.put("isViewTakeWayCountScan","1");
        }

        long takeWayCount = iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                .eq(MarketingCommingStore::getValidity, "0")
                .eq(MarketingCommingStore::getStatus, "1")
                .eq(MarketingCommingStore::getTakeWay,"1")
                .eq(MarketingCommingStore::getStoreManageId, storeManageId)
                .le(MarketingCommingStore::getStartTime, DateUtils.now()))+iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                .eq(MarketingCommingStore::getStoreManageId, storeManageId)
                .eq(MarketingCommingStore::getStatus, "1")
                .eq(MarketingCommingStore::getValidity, "1")
                .eq(MarketingCommingStore::getTakeWay,"1")
                .le(MarketingCommingStore::getStartTime, DateUtils.now())
                .ge(MarketingCommingStore::getEndTime, DateUtils.now()));
        if(takeWayCount==0){
            resultMap.put("isViewTakeWayCount","0");
        }else {
            resultMap.put("isViewTakeWayCount", "1");
        }

        //是否是合伙人
        if(iMemberDesignationGroupService.count(new LambdaQueryWrapper<MemberDesignationGroup>().eq(MemberDesignationGroup::getStoreManageId,storeManageId))>0){
            resultMap.put("isViewMemberDesignationGroup","1");
        }else{
            resultMap.put("isViewMemberDesignationGroup","0");
        }
        return resultMap;
    }

    @Override
    public IPage<Map<String, Object>> findStoreManageList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findStoreManageList(page, paramMap);
    }

    @Override
    public List<StoreManageDTO> findStoreManage(StoreManageDTO storeManageDTO) {
        return baseMapper.findStoreManage(storeManageDTO);
    }

    @Override
    public StoreManageVO findStore(String userId) {
        return baseMapper.findStore(userId);
    }

    @Override
    public IPage<StoreManageDTO> queryStoreManagePage(Page<StoreManageDTO> page, StoreManageVO storeManageVO) {
        return baseMapper.queryStoreManagePage(page, storeManageVO);
    }

    @Override
    @Transactional
    public void paySuccess(String id) {
        // 更新订单信息
        // 发送通知等
        StoreManage storeManage = this.getById(id);
        log.info(id);

        if (storeManage.getPayStatus().equals("0")) {
            //状态修改成已支付
            storeManage.setPayStatus("1");
            //设置开通时间
            Calendar calendar = Calendar.getInstance();
            storeManage.setStartTime(calendar.getTime());
            storeManage.setPayTime(calendar.getTime());
            if (storeManage.getOpenType().equals("0")) {
                calendar.add(1, Calendar.YEAR);
                storeManage.setEndTime(calendar.getTime());
            }
            storeManage.setStatus("1");
            storeManage.setPayType("0");
            //生成系统店铺用户
            SysUser user = new SysUser();
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
            sysUserQueryWrapper.eq("username", storeManage.getBossPhone());
            SysUser sysUser = iSysUserService.getOne(sysUserQueryWrapper);
            StoreSetting storeSetting=iStoreSettingService.getOne(new LambdaQueryWrapper<>());
            if (sysUser != null) {
                user = sysUser;
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                user.setRealname(storeManage.getBossName());
                user.setPassword(storeSetting.getInitialPasswd());
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                iSysUserService.saveOrUpdate(user);
            } else {
                user.setCreateTime(new Date());//设置创建时间
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                user.setRealname(storeManage.getBossName());
                user.setPassword(storeSetting.getInitialPasswd());
                user.setUsername(storeManage.getBossPhone());
                user.setPhone(storeManage.getBossPhone());
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                user.setStatus(1);
                user.setDelFlag(0);
                //设置店铺角色
                QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
                sysRoleQueryWrapper.eq("role_code", "Merchant");
                SysRole sysRole = iSysRoleService.getOne(sysRoleQueryWrapper);
                iSysUserService.addUserWithRole(user, sysRole.getId());
            }
            //设置用户绑定
            storeManage.setSysUserId(user.getId());
            //增加店铺缴费记录
            StorePayment storePayment = new StorePayment();
            storePayment.setDelFlag("0");
            storePayment.setStoreManageId(storeManage.getId());
            storePayment.setBossPhone(storeManage.getBossPhone());
            storePayment.setOpenType(storeManage.getOpenType());
            storePayment.setMoney(storeManage.getMoney());
            storePayment.setStartTime(storeManage.getStartTime());
            storePayment.setEndTime(storeManage.getEndTime());
            storePayment.setPayStatus("1");
            storePayment.setPayType("0");
            storePayment.setPayTime(new Date());
            iStorePaymentService.save(storePayment);
            //标识已开店
            MemberList memberList = iMemberListService.getById(storeManage.getMemberListId());
            if(memberList!=null) {
                memberList.setIsOpenStore("1");
                iMemberListService.saveOrUpdate(memberList);

                //推荐开店奖励百分比
                String openShopReward = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "open_shop_reward"), "%");

                //推荐人奖励
                if (new BigDecimal(openShopReward).doubleValue() > 0 && StringUtils.isNotBlank(memberList.getPromoterType())) {
                    //店铺
                    if (memberList.getPromoterType().equals("0")) {
                        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                        storeManageQueryWrapper.eq("sys_user_id", memberList.getPromoter());
                        storeManageQueryWrapper.in("pay_status", "1", "2");
                        if (this.count(storeManageQueryWrapper) > 0) {
                            StoreManage promoterStoreManage = this.list(storeManageQueryWrapper).get(0);
                            //店铺余额明细
                            StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                            storeRechargeRecord.setStoreManageId(promoterStoreManage.getId());
                            storeRechargeRecord.setPayType("7");
                            storeRechargeRecord.setGoAndCome("0");
                            storeRechargeRecord.setAmount(storeManage.getMoney().multiply(new BigDecimal(openShopReward).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                            storeRechargeRecord.setTradeStatus("5");
                            storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                            storeRechargeRecord.setOperator("系统");
                            storeRechargeRecord.setRemark("推荐开店奖励 [" + storePayment.getId() + "]");
                            storeRechargeRecord.setTradeType("2");
                            storeRechargeRecord.setTradeNo(storePayment.getId());
                            iStoreRechargeRecordService.save(storeRechargeRecord);

                            promoterStoreManage.setBalance(promoterStoreManage.getBalance().add(storeRechargeRecord.getAmount()));
                            this.saveOrUpdate(promoterStoreManage);

                            //店铺资金流水
                            StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                            storeAccountCapital.setStoreManageId(promoterStoreManage.getId());
                            storeAccountCapital.setPayType("7");
                            storeAccountCapital.setGoAndCome("0");
                            storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
                            storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                            storeAccountCapital.setBalance(promoterStoreManage.getBalance());
                            iStoreAccountCapitalService.save(storeAccountCapital);
                            log.info("推荐开店奖励：" + storeRechargeRecord.getAmount() + "---开店金额：" + promoterStoreManage.getBalance() + "--推荐店铺：" + promoterStoreManage.getStoreName());
                        }
                    }
                    //会员
                    if (memberList.getPromoterType().equals("1")) {

                        MemberList proterMemberList = iMemberListService.getById(memberList.getPromoter());

                        //会员余额明细
                        MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                        memberRechargeRecord.setMemberListId(proterMemberList.getId());
                        memberRechargeRecord.setPayType("4");
                        memberRechargeRecord.setGoAndCome("0");
                        memberRechargeRecord.setAmount(storeManage.getMoney().multiply(new BigDecimal(openShopReward).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                        memberRechargeRecord.setTradeStatus("5");
                        memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                        memberRechargeRecord.setOperator("系统");
                        memberRechargeRecord.setRemark("推荐开店奖励 [" + storePayment.getId() + "]");
                        memberRechargeRecord.setTMemberListId(storeManage.getMemberListId());
                        memberRechargeRecord.setTradeType("2");
                        memberRechargeRecord.setTradeNo(storePayment.getId());
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
                        memberAccountCapital.setPayType("4");
                        memberAccountCapital.setGoAndCome("0");
                        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
                        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
                        memberAccountCapital.setBalance(proterMemberList.getBalance());
                        iMemberAccountCapitalService.save(memberAccountCapital);
                        log.info("推荐开店奖励：" + memberRechargeRecord.getAmount() + "---开店金额：" + proterMemberList.getBalance() + "--推荐会员：" + proterMemberList.getNickName());
                    }

                }
            }
            //加盟商奖励
            boolean isExecute=true;
            //加盟商
            if(StringUtils.isNotBlank(storeManage.getAllianceUserId())){
                //处理加盟商业务
                AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                        .eq(AllianceManage::getDelFlag,"0")
                        .eq(AllianceManage::getStatus,"1")
                        .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                if (oConvertUtils.isNotEmpty(allianceManage)){
                    //独享控制
                    if(allianceManage.getProfitType().equals("0")){
                        isExecute=false;
                    }
                    if (allianceManage != null && allianceManage.getStoreCommissionRate().doubleValue() > 0&& allianceManage.getStatus().equals("1")) {
                        //代理余额明细
                        AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                        allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                        allianceRechargeRecord.setPayType("3");
                        allianceRechargeRecord.setGoAndCome("0");
                        if (allianceManage.getProfitType().equals("0")){
                            allianceRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                        }else {
                            allianceRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                        }
                        allianceRechargeRecord.setTradeStatus("5");
                        allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                        allianceRechargeRecord.setTradeType("2");
                        allianceRechargeRecord.setTradeNo(storePayment.getId());
                        allianceRechargeRecord.setRemark("开店奖励 ["+storePayment.getId()+"]");

                        iAllianceRechargeRecordService.save(allianceRechargeRecord);

                        allianceManage.setBalance(allianceManage.getBalance().add(allianceRechargeRecord.getAmount()));
                        iAllianceManageService.saveOrUpdate(allianceManage);

                        //代理资金明细
                        AllianceAccountCapital allianceAccountCapital = new AllianceAccountCapital();
                        allianceAccountCapital.setSysUserId(allianceManage.getSysUserId());
                        allianceAccountCapital.setPayType("3");
                        allianceAccountCapital.setGoAndCome("0");
                        allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                        allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                        allianceAccountCapital.setBalance(allianceManage.getBalance());
                        iAllianceAccountCapitalService.save(allianceAccountCapital);

                        log.info("推荐开店奖励：" + allianceRechargeRecord.getAmount() + "---开店金额：" + allianceManage.getBalance() + "--推荐加盟商：" + allianceManage.getId());
                    }
                }
            }



            //代理奖励
            //代理资金分配
            if(isExecute) {
                    if (StringUtils.isNotBlank(storeManage.getSysAreaId())) {
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
                                            if (agencyManage != null && agencyManage.getStoreCommissionRate().doubleValue() > 0&& agencyManage.getStatus().equals("1")) {
                                                //代理余额明细
                                                AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                agencyRechargeRecord.setPayType("3");
                                                agencyRechargeRecord.setGoAndCome("0");
                                                agencyRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                                                agencyRechargeRecord.setTradeStatus("5");
                                                agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                agencyRechargeRecord.setTradeType("2");
                                                agencyRechargeRecord.setTradeNo(storePayment.getId());
                                                agencyRechargeRecord.setRemark("开店奖励 ["+storePayment.getId()+"]");
                                                iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                                iAgencyManageService.saveOrUpdate(agencyManage);

                                                //代理资金明细
                                                AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                                agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                                agencyAccountCapital.setPayType("3");
                                                agencyAccountCapital.setGoAndCome("0");
                                                agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                                agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                                agencyAccountCapital.setBalance(agencyManage.getBalance());
                                                iAgencyAccountCapitalService.save(agencyAccountCapital);

                                                log.info("推荐开店奖励：" + agencyRechargeRecord.getAmount() + "---开店金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                            }
                                        }
                                    }else {
                                        if (StringUtils.isNotBlank(allianceManage.getCountyId())){
                                            SysArea area = iSysAreaService.getById(allianceManage.getCountyId());
                                            if (StringUtils.isNotBlank(area.getAgencyManageId())) {
                                                AgencyManage agencyManage = iAgencyManageService.getById(area.getAgencyManageId());
                                                if (agencyManage != null && agencyManage.getStoreCommissionRate().doubleValue() > 0&& agencyManage.getStatus().equals("1")) {
                                                    //代理余额明细
                                                    AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                    agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                    agencyRechargeRecord.setPayType("3");
                                                    agencyRechargeRecord.setGoAndCome("0");
                                                    agencyRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                                                    agencyRechargeRecord.setTradeStatus("5");
                                                    agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                    agencyRechargeRecord.setTradeType("2");
                                                    agencyRechargeRecord.setTradeNo(storePayment.getId());
                                                    agencyRechargeRecord.setRemark("开店奖励 ["+storePayment.getId()+"]");
                                                    iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                    agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                                    iAgencyManageService.saveOrUpdate(agencyManage);

                                                    //代理资金明细
                                                    AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                                    agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                                    agencyAccountCapital.setPayType("3");
                                                    agencyAccountCapital.setGoAndCome("0");
                                                    agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                                    agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                                    agencyAccountCapital.setBalance(agencyManage.getBalance());
                                                    iAgencyAccountCapitalService.save(agencyAccountCapital);

                                                    log.info("推荐开店奖励：" + agencyRechargeRecord.getAmount() + "---开店金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }else {
                                if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                    AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                    if (agencyManage != null && agencyManage.getStoreCommissionRate().doubleValue() > 0&& agencyManage.getStatus().equals("1")) {
                                        //代理余额明细
                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                        agencyRechargeRecord.setPayType("3");
                                        agencyRechargeRecord.setGoAndCome("0");
                                        agencyRechargeRecord.setAmount(storeManage.getMoney().multiply(agencyManage.getStoreCommissionRate().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                                        agencyRechargeRecord.setTradeStatus("5");
                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        agencyRechargeRecord.setTradeType("2");
                                        agencyRechargeRecord.setTradeNo(storePayment.getId());
                                        agencyRechargeRecord.setRemark("开店奖励 ["+storePayment.getId()+"]");
                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                        agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                        iAgencyManageService.saveOrUpdate(agencyManage);

                                        //代理资金明细
                                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                        agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                        agencyAccountCapital.setPayType("3");
                                        agencyAccountCapital.setGoAndCome("0");
                                        agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                        agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                        agencyAccountCapital.setBalance(agencyManage.getBalance());
                                        iAgencyAccountCapitalService.save(agencyAccountCapital);

                                        log.info("推荐开店奖励：" + agencyRechargeRecord.getAmount() + "---开店金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                    }
                                }
                            }
                        });
                    }
            }

            //开店赠送福利金

            //开店福利金百分比
            String sendWelfarePayment = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "send_welfare_payment"), "%");

            if (StringUtils.isNotBlank(sendWelfarePayment) && new BigDecimal(sendWelfarePayment).doubleValue() > 0) {
                storeManage.setWelfarePayments(storeManage.getWelfarePayments().add(storeManage.getMoney().multiply(new BigDecimal(sendWelfarePayment).divide(new BigDecimal(100)))).setScale(2,BigDecimal.ROUND_DOWN));
                //成功写入店铺福利金记录表
                MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
                marketingWelfarePayments.setDelFlag("0");//删除状态
                marketingWelfarePayments.setSysUserId(storeManage.getSysUserId());//店铺id
                marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                marketingWelfarePayments.setBargainPayments(storeManage.getMoney().multiply(new BigDecimal(sendWelfarePayment).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));//交易福利金
                marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());//账户福利金
                marketingWelfarePayments.setWeType("1");//类型; 收入
                marketingWelfarePayments.setGoAndCome("平台");//来源or去向
                marketingWelfarePayments.setBargainTime(new Date());//交易时间
                marketingWelfarePayments.setOperator("系统");//操作人
                marketingWelfarePayments.setBalance(storeManage.getBalance());//店铺账户余额
                marketingWelfarePayments.setStatus("1");//交易状态
                marketingWelfarePayments.setSendUser("平台");//赠送人s
                marketingWelfarePayments.setIsPlatform("1");//0,店铺; 1,平台
                marketingWelfarePayments.setUserType("1");//0：商城会员；1：店铺管理员
                marketingWelfarePayments.setGiveExplain("开店赠送["+storePayment.getId()+"]");//赠送说明
                marketingWelfarePayments.setTradeNo(storePayment.getId());//交易单号
                marketingWelfarePayments.setTradeType("5");//福利金交易类型
                iMarketingWelfarePaymentsService.save(marketingWelfarePayments);
                log.info("开店赠送店铺福利金：" + marketingWelfarePayments.getBargainPayments() + "---店铺福利金：" + storeManage.getWelfarePayments());

                //福利金的分层


                String isSendWelfarePayments = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "is_send_welfare_payments");


                //店铺余额充值福利金的利润分配
                //推荐开店奖励百分比
                String welfareSalesReward = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "welfare_sales_reward"), "%");

                //获取福利金设置比例
                QueryWrapper<MarketingWelfarePaymentsSetting> marketingWelfarePaymentsSettingQueryWrapper = new QueryWrapper<>();
                marketingWelfarePaymentsSettingQueryWrapper.eq("del_flag", "0");
                marketingWelfarePaymentsSettingQueryWrapper.eq("status", "1");
                MarketingWelfarePaymentsSetting paymentsSettingServiceOne = iMarketingWelfarePaymentsSettingService.getOne(marketingWelfarePaymentsSettingQueryWrapper);

                BigDecimal b = marketingWelfarePayments.getBargainPayments().multiply(paymentsSettingServiceOne.getProportion().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN);


                if(isSendWelfarePayments.equals("1")||isSendWelfarePayments.equals("3")) {
                    //推荐人奖励
                    if (new BigDecimal(welfareSalesReward).doubleValue() > 0 && memberList != null && StringUtils.isNotBlank(memberList.getPromoterType())) {
                        //店铺
                        if (memberList.getPromoterType().equals("0")) {
                            QueryWrapper<StoreManage> storeManageQueryWrapper1 = new QueryWrapper<>();
                            storeManageQueryWrapper1.eq("sys_user_id", memberList.getPromoter());
                            storeManageQueryWrapper1.eq("pay_status", "1");
                            if (this.count(storeManageQueryWrapper1) > 0) {
                                StoreManage promoterStoreManage = this.list(storeManageQueryWrapper1).get(0);
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
                                this.saveOrUpdate(promoterStoreManage);

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
                        //会员
                        if (memberList.getPromoterType().equals("1")) {

                            MemberList proterMemberList = iMemberListService.getById(memberList.getPromoter());

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
                            log.info("福利金销售奖励：" + memberRechargeRecord.getAmount() + "---会员金额：" + proterMemberList.getBalance() + "--推荐会员：" + proterMemberList.getNickName());
                        }

                    }
                }




                //加盟商
                    if(StringUtils.isNotBlank(storeManage.getAllianceUserId())){
                        //处理加盟商业务
                        AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                                .eq(AllianceManage::getDelFlag,"0")
                                .eq(AllianceManage::getStatus,"1")
                                .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                        if (oConvertUtils.isNotEmpty(allianceManage)){
                            //独享控制
                            if(allianceManage.getProfitType().equals("0")){
                                isExecute=false;
                            }
                            if (allianceManage != null && allianceManage.getWelfareCommissionRate().doubleValue() > 0&& allianceManage.getStatus().equals("1")) {
                                //代理余额明细
                                AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                                allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                                allianceRechargeRecord.setPayType("5");
                                allianceRechargeRecord.setGoAndCome("0");
                                if (allianceManage.getProfitType().equals("0")){
                                    allianceRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
                                }else {
                                    allianceRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
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
                    }


                //代理奖励
                //代理资金分配

                //查询地址确定代理位置
                if(isExecute) {
                    if (isSendWelfarePayments.equals("2") || isSendWelfarePayments.equals("3")) {

                        if (StringUtils.isNotBlank(storeManage.getSysAreaId())) {

                            if (StringUtils.isNotBlank(storeManage.getSysAreaId())) {
                                List<String> sysAreasWelfare = Arrays.asList(StringUtils.split(storeManage.getSysAreaId(), ","));
                                sysAreasWelfare.forEach(sid -> {
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
                                                    if (agencyManage != null && agencyManage.getWelfareCommissionRate().doubleValue() > 0&& agencyManage.getStatus().equals("1")) {
                                                        //代理余额明细
                                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                        agencyRechargeRecord.setPayType("5");
                                                        agencyRechargeRecord.setGoAndCome("0");
                                                        agencyRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
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
                                                        if (agencyManage != null && agencyManage.getWelfareCommissionRate().doubleValue() > 0&& agencyManage.getStatus().equals("1")) {
                                                            //代理余额明细
                                                            AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                            agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                            agencyRechargeRecord.setPayType("5");
                                                            agencyRechargeRecord.setGoAndCome("0");
                                                            agencyRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
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
                                            if (agencyManage != null && agencyManage.getWelfareCommissionRate().doubleValue() > 0&& agencyManage.getStatus().equals("1")) {
                                                //代理余额明细
                                                AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                agencyRechargeRecord.setPayType("5");
                                                agencyRechargeRecord.setGoAndCome("0");
                                                agencyRechargeRecord.setAmount(b.multiply(agencyManage.getWelfareCommissionRate().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN));
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
                }

            }
            this.saveOrUpdate(storeManage);
        }
    }


    @Override
    @Transactional
    public void paySuccessSJD(String id) {
        // 更新订单信息
        // 发送通知等
        StoreManage storeManage = this.getById(id);
        log.info(id);

        if (storeManage.getPayStatus().equals("0")) {
            //状态修改成已支付
            storeManage.setPayStatus("1");
            //设置开通时间
            Calendar calendar = Calendar.getInstance();
            storeManage.setStartTime(calendar.getTime());
            storeManage.setPayTime(calendar.getTime());
            if (storeManage.getOpenType().equals("0")) {
                calendar.add(1, Calendar.YEAR);
                storeManage.setEndTime(calendar.getTime());
            }
            storeManage.setStatus("1");
            storeManage.setPayType("0");
            //生成系统店铺用户
            SysUser user = new SysUser();
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
            sysUserQueryWrapper.eq("username", storeManage.getBossPhone());
            SysUser sysUser = iSysUserService.getOne(sysUserQueryWrapper);
            StoreSetting storeSetting=iStoreSettingService.getOne(new LambdaQueryWrapper<>());
            if (sysUser != null) {
                user = sysUser;
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                user.setRealname(storeManage.getBossName());
                user.setPassword(storeSetting.getInitialPasswd());
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                iSysUserService.saveOrUpdate(user);
            } else {
                user.setCreateTime(new Date());//设置创建时间
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                user.setRealname(storeManage.getBossName());
                user.setPassword(storeSetting.getInitialPasswd());
                user.setUsername(storeManage.getBossPhone());
                user.setPhone(storeManage.getBossPhone());
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                user.setStatus(1);
                user.setDelFlag(0);
                //设置店铺角色
                QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
                sysRoleQueryWrapper.eq("role_code", "Merchant");
                SysRole sysRole = iSysRoleService.getOne(sysRoleQueryWrapper);
                iSysUserService.addUserWithRole(user, sysRole.getId());
            }
            //设置用户绑定
            storeManage.setSysUserId(user.getId());
            //增加店铺缴费记录
            StorePayment storePayment = new StorePayment();
            storePayment.setDelFlag("0");
            storePayment.setStoreManageId(storeManage.getId());
            storePayment.setBossPhone(storeManage.getBossPhone());
            storePayment.setOpenType(storeManage.getOpenType());
            storePayment.setMoney(storeManage.getMoney());
            storePayment.setStartTime(storeManage.getStartTime());
            storePayment.setEndTime(storeManage.getEndTime());
            storePayment.setPayStatus("1");
            storePayment.setPayType("0");
            storePayment.setPayTime(new Date());
            iStorePaymentService.save(storePayment);

            this.saveOrUpdate(storeManage);

            //推荐开店奖励百分比
            String openShopReward = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "open_shop_reward"), "%");
            //推荐人奖励
            if (new BigDecimal(openShopReward).doubleValue() > 0 && StringUtils.isNotBlank(storeManage.getPromoterType())) {
                //店铺
                if (storeManage.getPromoterType().equals("0")) {
                    QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                    storeManageQueryWrapper.eq("sys_user_id", storeManage.getPromoter());
                    storeManageQueryWrapper.in("pay_status", "1", "2");
                    if (this.count(storeManageQueryWrapper) > 0) {
                        StoreManage promoterStoreManage = this.list(storeManageQueryWrapper).get(0);
                        //店铺余额明细
                        StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                        storeRechargeRecord.setStoreManageId(promoterStoreManage.getId());
                        storeRechargeRecord.setPayType("7");
                        storeRechargeRecord.setGoAndCome("0");
                        storeRechargeRecord.setAmount(storeManage.getMoney().multiply(new BigDecimal(openShopReward).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                        storeRechargeRecord.setTradeStatus("5");
                        storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                        storeRechargeRecord.setOperator("系统");
                        storeRechargeRecord.setRemark("推荐开店奖励[" + storePayment.getId()+"]");
                        storeRechargeRecord.setTradeType("2");
                        storeRechargeRecord.setTradeNo(storePayment.getId());
                        iStoreRechargeRecordService.save(storeRechargeRecord);

                        promoterStoreManage.setBalance(promoterStoreManage.getBalance().add(storeRechargeRecord.getAmount()));
                        this.saveOrUpdate(promoterStoreManage);

                        //店铺资金流水
                        StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                        storeAccountCapital.setStoreManageId(promoterStoreManage.getId());
                        storeAccountCapital.setPayType("7");
                        storeAccountCapital.setGoAndCome("0");
                        storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
                        storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                        storeAccountCapital.setBalance(promoterStoreManage.getBalance());
                        iStoreAccountCapitalService.save(storeAccountCapital);
                        log.info("推荐开店奖励：" + storeRechargeRecord.getAmount() + "---开店金额：" + promoterStoreManage.getBalance() + "--推荐店铺：" + promoterStoreManage.getStoreName());
                    }
                }
                if (storeManage.getPromoterType().equals("3")) {
                    //处理加盟商业务
                    QueryWrapper<AllianceManage> allianceManageQueryWrapper = new QueryWrapper<>();
                    allianceManageQueryWrapper.eq("sys_user_id", storeManage.getPromoter());
                    AllianceManage allianceManage = iAllianceManageService.list(allianceManageQueryWrapper).get(0);
                    if (allianceManage != null &&  allianceManage.getStatus().equals("1")) {
                        //代理余额明细
                        AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                        allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                        allianceRechargeRecord.setPayType("8");
                        allianceRechargeRecord.setGoAndCome("0");
                        allianceRechargeRecord.setAmount(storeManage.getMoney().multiply(new BigDecimal(openShopReward).divide(new BigDecimal(100)))
                                .setScale(2, BigDecimal.ROUND_DOWN));
                        allianceRechargeRecord.setTradeStatus("5");
                        allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                        allianceRechargeRecord.setTradeNo(storePayment.getId());
                        allianceRechargeRecord.setRemark("推荐开店奖励["+storePayment.getId()+"]");
                        iAllianceRechargeRecordService.save(allianceRechargeRecord);

                        allianceManage.setBalance(allianceManage.getBalance().add(allianceRechargeRecord.getAmount()));
                        iAllianceManageService.saveOrUpdate(allianceManage);

                        //代理资金明细
                        AllianceAccountCapital allianceAccountCapital = new AllianceAccountCapital();
                        allianceAccountCapital.setSysUserId(allianceManage.getSysUserId());
                        allianceAccountCapital.setPayType("8");
                        allianceAccountCapital.setGoAndCome("0");
                        allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                        allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                        allianceAccountCapital.setBalance(allianceManage.getBalance());
                        iAllianceAccountCapitalService.save(allianceAccountCapital);

                        log.info("推荐开店奖励：" + allianceRechargeRecord.getAmount() + "---开店金额：" + allianceManage.getBalance() + "--推荐加盟商：" + allianceManage.getId());
                    }
                }
            }
            //加盟商奖励
            boolean isExecute = true;
            //加盟商
            if (StringUtils.isNotBlank(storeManage.getAllianceUserId())||storeManage.getPromoterType().equals("3")) {
                //处理加盟商业务
                AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                        .eq(AllianceManage::getDelFlag,"0")
                        .eq(AllianceManage::getStatus,"1")
                        .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                if (oConvertUtils.isNotEmpty(allianceManage)){
                    //独享控制
                    if (allianceManage.getProfitType().equals("0")) {
                        isExecute = false;
                    }
                    if (allianceManage != null && allianceManage.getStoreCommissionRate().doubleValue() > 0 && allianceManage.getStatus().equals("1")) {
                        //代理余额明细
                        AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                        allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                        allianceRechargeRecord.setPayType("3");
                        allianceRechargeRecord.setGoAndCome("0");
                        if (allianceManage.getProfitType().equals("0")){
                            allianceRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().divide(new BigDecimal(100)))
                                    .setScale(2, BigDecimal.ROUND_DOWN));
                        }else {
                            allianceRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100)))
                                    .setScale(2, BigDecimal.ROUND_DOWN));
                        }
                        allianceRechargeRecord.setTradeStatus("5");
                        allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                        allianceRechargeRecord.setTradeType("2");
                        allianceRechargeRecord.setTradeNo(storePayment.getId());
                        allianceRechargeRecord.setRemark("开店奖励["+storePayment.getId()+"]");
                        iAllianceRechargeRecordService.save(allianceRechargeRecord);

                        allianceManage.setBalance(allianceManage.getBalance().add(allianceRechargeRecord.getAmount()));
                        iAllianceManageService.saveOrUpdate(allianceManage);

                        //代理资金明细
                        AllianceAccountCapital allianceAccountCapital = new AllianceAccountCapital();
                        allianceAccountCapital.setSysUserId(allianceManage.getSysUserId());
                        allianceAccountCapital.setPayType("3");
                        allianceAccountCapital.setGoAndCome("0");
                        allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                        allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                        allianceAccountCapital.setBalance(allianceManage.getBalance());
                        iAllianceAccountCapitalService.save(allianceAccountCapital);

                        log.info("推荐开店奖励：" + allianceRechargeRecord.getAmount() + "---开店金额：" + allianceManage.getBalance() + "--推荐加盟商：" + allianceManage.getId());
                    }
                }
            }

            //代理奖励
            //代理资金分配
            if (isExecute) {
                if (StringUtils.isNotBlank(storeManage.getSysAreaId())) {
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
                                        if (agencyManage != null && agencyManage.getStoreCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                            //代理余额明细
                                            AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                            agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                            agencyRechargeRecord.setPayType("3");
                                            agencyRechargeRecord.setGoAndCome("0");
                                            agencyRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100)))
                                                    .setScale(2, BigDecimal.ROUND_DOWN));
                                            agencyRechargeRecord.setTradeStatus("5");
                                            agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                            agencyRechargeRecord.setTradeType("2");
                                            agencyRechargeRecord.setTradeNo(storePayment.getId());
                                            agencyRechargeRecord.setRemark("开店奖励["+storePayment.getId()+"]");
                                            iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                            agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                            iAgencyManageService.saveOrUpdate(agencyManage);

                                            //代理资金明细
                                            AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                            agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                            agencyAccountCapital.setPayType("3");
                                            agencyAccountCapital.setGoAndCome("0");
                                            agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                            agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                            agencyAccountCapital.setBalance(agencyManage.getBalance());
                                            iAgencyAccountCapitalService.save(agencyAccountCapital);

                                            log.info("推荐开店奖励：" + agencyRechargeRecord.getAmount() + "---开店金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                        }
                                    }
                                }else {
                                    if (StringUtils.isNotBlank(allianceManage.getCountyId())){
                                        SysArea area = iSysAreaService.getById(allianceManage.getCountyId());
                                        if (StringUtils.isNotBlank(area.getAgencyManageId())) {
                                            AgencyManage agencyManage = iAgencyManageService.getById(area.getAgencyManageId());
                                            if (agencyManage != null && agencyManage.getStoreCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                                //代理余额明细
                                                AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                agencyRechargeRecord.setPayType("3");
                                                agencyRechargeRecord.setGoAndCome("0");
                                                agencyRechargeRecord.setAmount(storeManage.getMoney().multiply(allianceManage.getStoreCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100)))
                                                        .setScale(2, BigDecimal.ROUND_DOWN));
                                                agencyRechargeRecord.setTradeStatus("5");
                                                agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                agencyRechargeRecord.setTradeType("2");
                                                agencyRechargeRecord.setTradeNo(storePayment.getId());
                                                agencyRechargeRecord.setRemark("开店奖励["+storePayment.getId()+"]");
                                                iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                                iAgencyManageService.saveOrUpdate(agencyManage);

                                                //代理资金明细
                                                AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                                agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                                agencyAccountCapital.setPayType("3");
                                                agencyAccountCapital.setGoAndCome("0");
                                                agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                                agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                                agencyAccountCapital.setBalance(agencyManage.getBalance());
                                                iAgencyAccountCapitalService.save(agencyAccountCapital);

                                                log.info("推荐开店奖励：" + agencyRechargeRecord.getAmount() + "---开店金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                            }
                                        }
                                    }
                                }
                            }
                        }else {
                            if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                if (agencyManage != null && agencyManage.getStoreCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")) {
                                    //代理余额明细
                                    AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                    agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                    agencyRechargeRecord.setPayType("3");
                                    agencyRechargeRecord.setGoAndCome("0");
                                    agencyRechargeRecord.setAmount(storeManage.getMoney().multiply(agencyManage.getStoreCommissionRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                    agencyRechargeRecord.setTradeStatus("5");
                                    agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                    agencyRechargeRecord.setTradeType("2");
                                    agencyRechargeRecord.setTradeNo(storePayment.getId());
                                    agencyRechargeRecord.setRemark("开店奖励["+storePayment.getId()+"]");
                                    iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                    agencyManage.setBalance(agencyManage.getBalance().add(agencyRechargeRecord.getAmount()));
                                    iAgencyManageService.saveOrUpdate(agencyManage);

                                    //代理资金明细
                                    AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                                    agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
                                    agencyAccountCapital.setPayType("3");
                                    agencyAccountCapital.setGoAndCome("0");
                                    agencyAccountCapital.setAmount(agencyRechargeRecord.getAmount());
                                    agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
                                    agencyAccountCapital.setBalance(agencyManage.getBalance());
                                    iAgencyAccountCapitalService.save(agencyAccountCapital);

                                    log.info("推荐开店奖励：" + agencyRechargeRecord.getAmount() + "---开店金额：" + agencyManage.getBalance() + "--推荐代理：" + agencyManage.getId());
                                }
                            }
                        }

                    });
                }

            }

            //开店赠送福利金

            //开店福利金百分比
            String sendWelfarePayment = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "send_welfare_payment"), "%");

            if (StringUtils.isNotBlank(sendWelfarePayment) && new BigDecimal(sendWelfarePayment).doubleValue() > 0) {
                storeManage.setWelfarePayments(storeManage.getWelfarePayments().add(storeManage.getMoney().multiply(new BigDecimal(sendWelfarePayment).divide(new BigDecimal(100)))).setScale(2, BigDecimal.ROUND_DOWN));
                //成功写入店铺福利金记录表
                MarketingWelfarePayments marketingWelfarePayments = new MarketingWelfarePayments();
                marketingWelfarePayments.setDelFlag("0");//删除状态
                marketingWelfarePayments.setSysUserId(storeManage.getSysUserId());//店铺id
                marketingWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
                marketingWelfarePayments.setBargainPayments(storeManage.getMoney().multiply(new BigDecimal(sendWelfarePayment).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));//交易福利金
                marketingWelfarePayments.setWelfarePayments(storeManage.getWelfarePayments());//账户福利金
                marketingWelfarePayments.setWeType("1");//类型; 收入
                marketingWelfarePayments.setGoAndCome("平台");//来源or去向
                marketingWelfarePayments.setBargainTime(new Date());//交易时间
                marketingWelfarePayments.setOperator("系统");//操作人
                marketingWelfarePayments.setBalance(storeManage.getBalance());//店铺账户余额
                marketingWelfarePayments.setStatus("1");//交易状态
                marketingWelfarePayments.setSendUser("平台");//赠送人s
                marketingWelfarePayments.setIsPlatform("1");//0,店铺; 1,平台
                marketingWelfarePayments.setUserType("1");//0：商城会员；1：店铺管理员
                marketingWelfarePayments.setGiveExplain("开店赠送["+storePayment.getId()+"]");//赠送说明
                marketingWelfarePayments.setTradeNo(storePayment.getId());
                marketingWelfarePayments.setTradeType("5");
                iMarketingWelfarePaymentsService.save(marketingWelfarePayments);
                log.info("开店赠送店铺福利金：" + marketingWelfarePayments.getBargainPayments() + "---店铺福利金：" + storeManage.getWelfarePayments());

                /*iMarketingWelfarePaymentsService.save(new MarketingWelfarePayments()
                        .setDelFlag("0")
                        .setSysUserId(marketingWelfarePayments.getSysUserId())
                        .setSerialNumber(OrderNoUtils.getOrderNo())
                        .setBargainPayments(marketingWelfarePayments.getBargainPayments())
                        .setWeType("0")
                        .setGiveExplain("开店赠送["+storePayment.getId()+"]")
                        .setGoAndCome("平台")
                        .setBargainTime(new Date())
                        .setOperator("系统")
                        .setStatus("1")
                        .setSendUser("平台")
                        .setIsPlatform("1")
                        .setUserType("1")
                        .setTradeNo(marketingWelfarePayments.getSerialNumber())
                        .setTradeType("5")
                );*/
                //福利金的分层

                String isSendWelfarePayments = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "is_send_welfare_payments");
                //推荐开店奖励百分比
                String welfareSalesReward = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "welfare_sales_reward"), "%");
                //获取福利金设置比例
                QueryWrapper<MarketingWelfarePaymentsSetting> marketingWelfarePaymentsSettingQueryWrapper = new QueryWrapper<>();
                marketingWelfarePaymentsSettingQueryWrapper.eq("del_flag", "0");
                marketingWelfarePaymentsSettingQueryWrapper.eq("status", "1");
                MarketingWelfarePaymentsSetting paymentsSettingServiceOne = iMarketingWelfarePaymentsSettingService.getOne(marketingWelfarePaymentsSettingQueryWrapper);

                BigDecimal b = marketingWelfarePayments.getBargainPayments().multiply(paymentsSettingServiceOne.getProportion().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN);

                if (isSendWelfarePayments.equals("1") || isSendWelfarePayments.equals("3")) {
                    //推荐人奖励
                    if (new BigDecimal(welfareSalesReward).doubleValue() > 0 && storeManage != null && StringUtils.isNotBlank(storeManage.getPromoterType())) {
                        //店铺
                        if (storeManage.getPromoterType().equals("0")) {
                            QueryWrapper<StoreManage> storeManageQueryWrapper1 = new QueryWrapper<>();
                            storeManageQueryWrapper1.eq("sys_user_id", storeManage.getPromoter());
                            storeManageQueryWrapper1.in("pay_status", "1","2");
                            if (this.count(storeManageQueryWrapper1) > 0) {
                                StoreManage promoterStoreManage = this.list(storeManageQueryWrapper1).get(0);
                                //店铺余额明细
                                StoreRechargeRecord storeRechargeRecordw = new StoreRechargeRecord();
                                storeRechargeRecordw.setStoreManageId(promoterStoreManage.getId());
                                storeRechargeRecordw.setPayType("9");
                                storeRechargeRecordw.setGoAndCome("0");
                                storeRechargeRecordw.setAmount(b.multiply(new BigDecimal(welfareSalesReward).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                                storeRechargeRecordw.setTradeStatus("5");
                                storeRechargeRecordw.setOrderNo(OrderNoUtils.getOrderNo());
                                storeRechargeRecordw.setOperator("系统");
                                storeRechargeRecordw.setRemark("福利金销售奖励[" + marketingWelfarePayments.getSerialNumber()+"]");
                                storeRechargeRecordw.setTradeType("3");
                                storeRechargeRecordw.setTradeNo(marketingWelfarePayments.getSerialNumber());
                                iStoreRechargeRecordService.save(storeRechargeRecordw);

                                promoterStoreManage.setBalance(promoterStoreManage.getBalance().add(storeRechargeRecordw.getAmount()));
                                this.saveOrUpdate(promoterStoreManage);

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
                        if (storeManage.getPromoterType().equals("3")) {
                            //处理加盟商业务
                            QueryWrapper<AllianceManage> allianceManageQueryWrapper = new QueryWrapper<>();
                            allianceManageQueryWrapper.eq("sys_user_id", storeManage.getPromoter());
                            AllianceManage allianceManage = iAllianceManageService.list(allianceManageQueryWrapper).get(0);

                            if (allianceManage.getStatus().equals("1")) {
                                //代理余额明细
                                AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                                allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                                allianceRechargeRecord.setPayType("8");
                                allianceRechargeRecord.setGoAndCome("0");
                                allianceRechargeRecord.setAmount(b.multiply(new BigDecimal(welfareSalesReward).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
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
                                allianceAccountCapital.setPayType("8");
                                allianceAccountCapital.setGoAndCome("0");
                                allianceAccountCapital.setAmount(allianceRechargeRecord.getAmount());
                                allianceAccountCapital.setOrderNo(allianceRechargeRecord.getOrderNo());
                                allianceAccountCapital.setBalance(allianceManage.getBalance());
                                iAllianceAccountCapitalService.save(allianceAccountCapital);

                                log.info("福利金销售奖励：" + allianceRechargeRecord.getAmount() + "---代理金额：" + allianceManage.getBalance() + "--推荐加盟商：" + allianceManage.getId());
                            }
                        }

                    }
                    //加盟商
                    if (StringUtils.isNotBlank(storeManage.getAllianceUserId())||storeManage.getPromoterType().equals("3")) {
                        //处理加盟商业务
                        AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                                .eq(AllianceManage::getDelFlag,"0")
                                .eq(AllianceManage::getStatus,"1")
                                .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                        if (oConvertUtils.isNotEmpty(allianceManage)){
                            //独享控制
                            if (allianceManage.getProfitType().equals("0")) {
                                isExecute = false;
                            }
                            if (allianceManage != null && allianceManage.getWelfareCommissionRate().doubleValue() > 0 && allianceManage.getStatus().equals("1")) {
                                //代理余额明细
                                AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                                allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                                allianceRechargeRecord.setPayType("5");
                                allianceRechargeRecord.setGoAndCome("0");
                                if (allianceManage.getProfitType().equals("0")){
                                    allianceRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().divide(new BigDecimal(100)))
                                            .setScale(2, BigDecimal.ROUND_DOWN));
                                }else {
                                    allianceRechargeRecord.setAmount(b.multiply(allianceManage.getWelfareCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(100)))
                                            .setScale(2, BigDecimal.ROUND_DOWN));
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

                    }

                    //代理奖励
                    //代理资金分配

                    //查询地址确定代理位置
                    if (isExecute) {
                        if (isSendWelfarePayments.equals("2") || isSendWelfarePayments.equals("3")) {

                            if (StringUtils.isNotBlank(storeManage.getSysAreaId())) {

                                if (StringUtils.isNotBlank(storeManage.getSysAreaId())) {
                                    List<String> sysAreasWelfare = Arrays.asList(StringUtils.split(storeManage.getSysAreaId(), ","));
                                    sysAreasWelfare.forEach(sid -> {
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
                                                            agencyRechargeRecord.setRemark("福利金销售奖励[" + marketingWelfarePayments.getSerialNumber() + "]");
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
                                                                    agencyRechargeRecord.setRemark("福利金销售奖励[" + marketingWelfarePayments.getSerialNumber() + "]");
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
                    }
                }
                this.saveOrUpdate(storeManage);
            }
        }
    }

    @Override
    @Transactional
    public void backSucess(String id) {
        StoreManage storeManage = this.getById(id);
        //回调支付冲账
        storeManage.setBackStatus("1");
        storeManage.setBackTimes(storeManage.getBackTimes().add(new BigDecimal(1)));
        this.saveOrUpdate(storeManage);
    }

    @Override
    public StoreWorkbenchVO findStoreWorkbenchVO(String userId) {
        return baseMapper.findStoreWorkbenchVO(userId);
    }

    @Override
    public Map<String, Object> myStore(HttpServletRequest request) {
        String sysUserId = request.getAttribute("sysUserId").toString();
        return baseMapper.myStore(sysUserId);
    }

    @Override
    public Map<String, Object> findStoreInfo(HttpServletRequest request) {
        String sysUserId = request.getAttribute("sysUserId").toString();
        return baseMapper.findStoreInfo(sysUserId);
    }

    @Override
    public Map<String, Object> returnSmallcode(HttpServletRequest request) {
        //获取当前登录人userId
        String sysUserId = request.getAttribute("sysUserId").toString();

        //查出当前登录人店铺
        StoreManage storeManage = this.getOne(new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId, sysUserId));

        //创建返回参数形式
        HashMap<String, Object> map = new HashMap<>();

        //判断当前登录人店铺二维码id是否为空
        if (StringUtils.isBlank(storeManage.getSysSmallcodeId())) {
            //店铺二维码为空时生成店铺二维码
            SysSmallcode smallcode = new SysSmallcode()
                    .setDelFlag("0")
                    .setSysUserId(storeManage.getSysUserId())
                    .setCodeType("0");
            //新增二维码记录
            iSysSmallcodeService.save(smallcode);
            //新增二维码id
            this.updateById(storeManage
                    .setSysSmallcodeId(smallcode.getId()));

            //生成二维码图片
            iSysSmallcodeService.updateById(smallcode
                    .setAddress(weixinQRUtils.getQrCodeByPage(smallcode.getId(),"activeAction/pages/shopIndex/shopIndex")));

            map.put("smallcode", smallcode.getAddress());
            return map;
        } else {
            ////店铺二维码不为空返回
            map.put("smallcode", iSysSmallcodeService
                    .getById(storeManage.getSysSmallcodeId()).getAddress());
            return map;
        }

    }

    @Override
    public Map<String, Object> returnAuthentication(HttpServletRequest request) {
        //获取当前登录人userId
        String sysUserId = request.getAttribute("sysUserId").toString();
        return baseMapper.returnAuthentication(sysUserId);
    }

    @Override
    public Map<String, Object> returnSecurity(HttpServletRequest request) {
        //获取当前登录人userId
        String sysUserId = request.getAttribute("sysUserId").toString();
        return baseMapper.returnSecurity(sysUserId);
    }

    @Override
    public Boolean updateStorePassword(StoreManageVO storeManageVO, HttpServletRequest request) {
        String sysUserId = request.getAttribute("sysUserId").toString();
        SysUser user = iSysUserService.getById(sysUserId);
        String newpassword = PasswordUtil.encrypt(user.getUsername(), storeManageVO.getPassword(), user.getSalt());
        boolean b = iSysUserService.saveOrUpdate(user.setPassword(newpassword));
        return b;
    }

    @Override
    @Transactional
    public String cashOut(String sysUserId, BigDecimal amount) {
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUserId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");
        if(this.count(storeManageQueryWrapper)<=0){
            return "500:查询不到店铺用户信息！！！";
        }
        StoreManage storeManage = this.list(storeManageQueryWrapper).get(0);
        String id = storeManage.getId();//店铺id

        //判断店铺余额
        if (amount.doubleValue() > storeManage.getBalance().doubleValue()){
            return "500:店铺余额不足";
        }
        StoreBankCard storeBankCard = iStoreBankCardService.getOne(new LambdaQueryWrapper<StoreBankCard>()
                .eq(StoreBankCard::getDelFlag,"0")
                .eq(StoreBankCard::getStoreManageId,storeManage.getId())
                .eq(StoreBankCard::getCarType,"0"));
        if (oConvertUtils.isEmpty(storeBankCard)){
            return "500:银行卡信息异常,请重新设置银行卡信息";
        }
        //减去店铺余额(2021年7月16日15:44:54 废弃)
//        storeManage.setBalance(storeManage.getBalance().subtract(amount));

        //添加到不可用金额(2021年7月15日22:37:56废弃)
//        storeManage.setUnusableFrozen(storeManage.getUnusableFrozen().add(amount));

        //获取字典配置提现阈值，大于此数据值则手续费按照大于的取，小于等于此数据值则手续费小于的取
        String withdrawThreshold = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_threshold");
        //提现手续费小于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeLessType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_type");
        //提现手续费大于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeGreaterType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_type");
        //小于阈值固定费用（fixed）
        String withdrawServiceChargeLessFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_fixed");
        //小于阈值百分比（percent）
        String withdrawServiceChargeLessPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_percent");
        //大于阈值固定费用（fixed）
        String withdrawServiceChargeGreaterFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_fixed");
        //大于阈值百分比（percent）
        String withdrawServiceChargeGreaterPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_percent");

        //获取手续费比例2021年7月16日16:17:12废弃
        /*String s = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item",
                "item_value", "item_text", "withdrawal_service_charge"), "%");*/
        StoreWithdrawDeposit storeWithdrawDeposit = new StoreWithdrawDeposit();
        storeWithdrawDeposit.setDelFlag("0");//删除状态
        storeWithdrawDeposit.setStoreManageId(id);//店铺id
        storeWithdrawDeposit.setOrderNo(OrderNoUtils.getOrderNo());//单号
        storeWithdrawDeposit.setPhone(storeManage.getBossPhone());//手机号
        //手续费
//        BigDecimal c = amount.multiply(new BigDecimal(s).divide(new BigDecimal(100)));
        if (amount.doubleValue()<=Double.valueOf(withdrawThreshold)){
            if (withdrawServiceChargeLessType.equals("0")){
                storeWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeLessFixed));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }else {
                storeWithdrawDeposit.setServiceCharge(amount.multiply(new BigDecimal(withdrawServiceChargeLessPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }
        }else {
            if (withdrawServiceChargeGreaterType.equals("0")){
                storeWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeGreaterFixed));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }else {
                storeWithdrawDeposit.setServiceCharge(amount.multiply(new BigDecimal(withdrawServiceChargeGreaterPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }
        }
        storeWithdrawDeposit.setMoney(amount);//提现金额
        storeWithdrawDeposit.setWithdrawalType("2");//提现类型(银行卡)
        storeWithdrawDeposit.setTimeApplication(new Date());//申请时间
        storeWithdrawDeposit.setStatus("0");//状态
        storeWithdrawDeposit.setRemark("余额提现["+storeWithdrawDeposit.getOrderNo()+"]");//备注
        storeWithdrawDeposit.setBankCard(storeBankCard.getBankCard());//银行卡号(支付宝账号)
        storeWithdrawDeposit.setBankName(storeBankCard.getBankName());//开户行名称
        storeWithdrawDeposit.setCardholder(storeBankCard.getCardholder());//持卡人姓名(真实姓名)
        storeWithdrawDeposit.setBossPhone(storeManage.getBossPhone());//老板电话
        storeWithdrawDeposit.setOpeningBank(storeBankCard.getOpeningBank());
        iStoreWithdrawDepositService.save(storeWithdrawDeposit);
        boolean b = subtractStoreBlance(storeManage.getId(), amount, storeWithdrawDeposit.getOrderNo(), "1");
        if (b) {
            return "200:操作成功!";
        } else {
            return "500:操作失败";
        }
    }

    @Override
    public List<StoreManageDTO> findPageList(StoreManageVO storeManageVO) {
        return baseMapper.findPageList(storeManageVO);
    }

    /**
     * 店铺余额
     *
     * @param storeManageVO
     * @return
     */
    @Override
    public IPage<StoreManageDTO> findStoreBalance(Page<StoreManageDTO> page, StoreManageVO storeManageVO) {
        return baseMapper.findStoreBalance(page, storeManageVO);
    }

    @Override
    @Transactional
    public Object pay(String id ) {



                // 更新订单信息
                // 发送通知等
                StoreRechargeRecord storeRechargeRecord = iStoreRechargeRecordService.getById(id);
                log.info(id);
                if (storeRechargeRecord.getTradeStatus().equals("0")) {
                    storeRechargeRecord.setTradeStatus("5");
                    StoreManage storeManage = baseMapper.selectById(storeRechargeRecord.getStoreManageId());
                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    baseMapper.updateById(storeManage);

                    iStoreAccountCapitalService.save(new StoreAccountCapital()
                            .setDelFlag("0")
                            .setStoreManageId(storeManage.getId())
                            .setPayType("4")
                            .setGoAndCome("0")
                            .setAmount(storeRechargeRecord.getAmount())
                            .setOrderNo(storeRechargeRecord.getOrderNo())
                            .setBalance(storeManage.getBalance()));
                }
                //回调支付冲账
                storeRechargeRecord.setPayType("4");
                storeRechargeRecord.setBackTimes(storeRechargeRecord.getBackTimes().add(new BigDecimal(1)));
                storeRechargeRecord.setTradeNo(id);
                iStoreRechargeRecordService.saveOrUpdate(storeRechargeRecord);
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);

    }

    @Override
    public IPage<Map<String, Object>> findCertificateStore(Page<Map<String,Object>> page, HashMap<Object,Object> objectObjectHashMap) {
        return baseMapper.findCertificateStore(page, objectObjectHashMap);
    }

    @Override
    public Map<String, Object> findUseInfo(String sysUserId) {
        return baseMapper.findUseInfo(sysUserId);
    }

    @Override
    public Map<String, Object> findStorePromoter(String id) {
        HashMap<String, Object> map = new HashMap<>();
        StoreManage storeManage = this.getById(id);
        if (StringUtils.isNotBlank(storeManage.getPromoter())){
            if (storeManage.getPromoterType().equals("0")){
                StoreManage manage = this.getOne(new LambdaQueryWrapper<StoreManage>()
                        .eq(StoreManage::getSysUserId, storeManage.getPromoter()));
                if (StringUtils.isNotBlank(manage.getSubStoreName())){
                    map.put("StorePromoter",manage.getStoreName()+"("+manage.getSubStoreName()+")");
                }else {
                    map.put("StorePromoter",manage.getStoreName());
                }
            }
            if (storeManage.getPromoterType().equals("1")){
                MemberList memberList = iMemberListService.getById(storeManage.getPromoter());
                map.put("StorePromoter",memberList.getNickName());
            }
            if (storeManage.getPromoterType().equals("2")){
                map.put("StorePromoter","平台");
            }
            if (storeManage.getPromoterType().equals("3")){
                AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaQueryWrapper<AllianceManage>()
                        .eq(AllianceManage::getSysUserId, storeManage.getPromoter()));
                map.put("StorePromoter",allianceManage.getName());
            }
        }else {
            if (StringUtils.isNotBlank(storeManage.getMemberListId())){
                MemberList memberList = iMemberListService.getById(storeManage.getMemberListId());
                if (memberList.getPromoterType().equals("0")){
                    StoreManage manage = this.getOne(new LambdaQueryWrapper<StoreManage>()
                            .eq(StoreManage::getSysUserId, memberList.getPromoter()));
                    if (oConvertUtils.isNotEmpty(manage)){
                        if (StringUtils.isNotBlank(manage.getSubStoreName())){
                            map.put("StorePromoter",manage.getStoreName()+"("+manage.getSubStoreName()+")");
                        }else {
                            map.put("StorePromoter",manage.getStoreName());
                        }
                    }else {
                        map.put("StorePromoter","无");
                    }
                }
                if (memberList.getPromoterType().equals("1")){
                    MemberList member = iMemberListService.getById(memberList.getPromoter());
                    map.put("StorePromoter",member.getNickName());
                }
                if (memberList.getPromoterType().equals("2")){
                    map.put("StorePromoter","平台");
                }
            }else {
                map.put("StorePromoter","无");
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> findInfoByidAndType(String promoter, String promoterType) {
        HashMap<String, Object> map = new HashMap<>();
        if (promoterType.equals("0")){
            StoreManage storeManage = this.getById(promoter);
            if (oConvertUtils.isNotEmpty(storeManage)){
                if (StringUtils.isNotBlank(storeManage.getSubStoreName())){
                    map.put("information",storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
                }else {
                    map.put("information",storeManage.getStoreName());
                }
            }else {
                map.put("information","查无此人");
            }
        }
        if (promoterType.equals("1")){
            MemberList memberList = iMemberListService.getById(promoter);
            if (oConvertUtils.isNotEmpty(memberList)){
                if (StringUtils.isNotBlank(memberList.getPhone())){
                    map.put("information",memberList.getNickName()+"("+memberList.getPhone()+")");
                }else {
                    map.put("information",memberList.getNickName());
                }

            }else {
                map.put("information","查无此人");
            }
        }
        if (promoterType.equals("2")){
            map.put("information","平台");
        }
        if (promoterType.equals("3")){
            AllianceManage allianceManage = iAllianceManageService.getById(promoter);
            if (oConvertUtils.isNotEmpty(allianceManage)){
                map.put("information",allianceManage.getName());
            }else {
                map.put("information","查无此人");
            }
        }

        return map;
    }

    @Override
    public String updateStorePromoter(StoreManageDTO storeManageDTO) {
        StoreManage storeManage = this.getById(storeManageDTO.getId());
        storeManage.setMemberListId("");
        if (storeManageDTO.getPromoterType().equals("0")){
            StoreManage manage = this.getById(storeManageDTO.getPromoter());
            if (oConvertUtils.isNotEmpty(manage)){
                storeManage.setPromoter(manage.getSysUserId());
                storeManage.setPromoterType(storeManageDTO.getPromoterType());
            }else {
                return "修改失败";
            }
        }
        if (storeManageDTO.getPromoterType().equals("1")){
            MemberList memberList = iMemberListService.getById(storeManageDTO.getPromoter());
            if (oConvertUtils.isNotEmpty(memberList)){
                storeManage.setPromoter(memberList.getId());
                storeManage.setPromoterType(storeManageDTO.getPromoterType());
            }else {
                return "修改失败";
            }
        }
        if (storeManageDTO.getPromoterType().equals("2")){
            storeManage.setPromoter("");
            storeManage.setPromoterType(storeManageDTO.getPromoterType());
        }
        if (storeManageDTO.getPromoterType().equals("3")){
            AllianceManage allianceManage = iAllianceManageService.getById(storeManageDTO.getPromoter());
            if (oConvertUtils.isNotEmpty(allianceManage)){
                storeManage.setPromoter(allianceManage.getSysUserId());
                storeManage.setPromoterType(storeManageDTO.getPromoterType());
            }else {
                return "修改失败";
            }

        }
        boolean b = this.saveOrUpdate(storeManage);
        if (b) {
            return "修改成功";
        }else {
            return "修改失败";
        }

    }

    @Override
    public IPage<StoreManageVO> findAllianceStoreList(Page<StoreManageVO> page, StoreManageDTO storeManageDTO) {
        return baseMapper.findAllianceStoreList(page,storeManageDTO);
    }

    @Override
    public List<Map<String, Object>> getStoreByBossPhone(String bossPhone) {
        return baseMapper.getStoreByBossPhone(bossPhone);
    }

    @Override
    public List<Map<String, Object>> findStoreInfoByStoreName(String storeName) {
        return baseMapper.findStoreInfoByStoreName(storeName);
    }

    @Override
    public IPage<Map<String, Object>> findCityLifeStoreManageList(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findCityLifeStoreManageList(page,paramObjectMap);
    }


    @Override
    public List<Map<String, Object>> storeComplimentaryThree() {
        return baseMapper.storeComplimentaryThree();
    }

    @Override
    public IPage<Map<String, Object>> findCityLifeStoreManageListNew(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findCityLifeStoreManageListNew(page,paramObjectMap);
    }

    @Override
    public IPage<Map<String, Object>> getStoreManageByRecommend(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getStoreManageByRecommend(page,paramMap);
    }

    @Override
    public Map<String, Object> getStoreManageById(String storeManageId) {
        return baseMapper.getStoreManageById(storeManageId);
    }

    @Override
    public IPage<Map<String, Object>> getLabelStoreManage(Page<Map<String, Object>> page, Map<String,Object> paramMap) {
        return baseMapper.getLabelStoreManage(page, paramMap);
    }

    @Override
    public List<MemberDesignationGroupVO> getPartnerSum(String id) {
        return baseMapper.getPartnerSum(id);
    }

    @Override
    public List<Map<String, Object>> getStoreList(String sysUserId) {
        return baseMapper.getStoreList(sysUserId);
    }

    @Override
    public boolean subtractStoreBlance(String storeManageId, BigDecimal subtractBalance, String orderNo, String payType) {
        if(subtractBalance.doubleValue()<=0){
            return true;
        }
        StoreManage storeManage = this.getById(storeManageId);
        if (oConvertUtils.isEmpty(storeManage)){
            return false;
        }
        this.updateById(storeManage.setBalance(storeManage.getBalance().subtract(subtractBalance)));

        StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
        storeAccountCapital.setStoreManageId(storeManage.getId());
        storeAccountCapital.setPayType(payType);
        storeAccountCapital.setGoAndCome("1");
        storeAccountCapital.setAmount(subtractBalance);
        storeAccountCapital.setOrderNo(orderNo);
        storeAccountCapital.setBalance(storeManage.getBalance());
        iStoreAccountCapitalService.save(storeAccountCapital);
        return true;
    }

    @Override
    public boolean addStoreBlance(String storeManageId, BigDecimal addBalance, String orderNo, String payType) {
        if(addBalance.doubleValue()<=0){
            return true;
        }
        StoreManage storeManage = this.getById(storeManageId);
        if (oConvertUtils.isEmpty(storeManage)){
            return false;
        }
        this.updateById(storeManage.setBalance(storeManage.getBalance().add(addBalance)));

        StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
        storeAccountCapital.setStoreManageId(storeManage.getId());
        storeAccountCapital.setPayType(payType);
        storeAccountCapital.setGoAndCome("0");
        storeAccountCapital.setAmount(addBalance);
        storeAccountCapital.setOrderNo(orderNo);
        storeAccountCapital.setBalance(storeManage.getBalance());
        iStoreAccountCapitalService.save(storeAccountCapital);
        return true;
    }

    @Override
    public IPage<Map<String, Object>> getPrivilege(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.getPrivilege(page,memberId);
    }

    @Override
    public Map<String, Object> getPrivilegeInfo(Map<String,Object> paramMap) {
        return baseMapper.getPrivilegeInfo(paramMap);
    }

}
