package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.utils.MemberUtils;
import org.jeecg.modules.member.utils.PromotionCodeUtils;
import org.jeecg.modules.member.utils.QrCodeUtils;
import org.jeecg.modules.member.vo.MemberDesignationCountVO;
import org.jeecg.modules.member.vo.MemberListVO;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 会员api接口
 */
@RequestMapping("after/member")
@Controller
@Slf4j
public class AfterMemberController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;

    @Autowired
    private IMemberBankCardService iMemberBankCardService;

    @Autowired
    private QrCodeUtils qrCodeUtils;

    @Autowired
    private IOrderListService iOrderListService;
    @Autowired
    private IOrderStoreListService iOrderStoreListService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberGradeService iMemberGradeService;

    @Autowired
    private IMemberDesignationService iMemberDesignationService;

    @Autowired
    private IMemberDesignationCountService iMemberDesignationCountService;
    @Autowired
    private IMemberDesignationMemberListService iMemberDesignationMemberListService;

    @Autowired
    private IMarketingIntegralSettingService iMarketingIntegralSettingService;

    @Autowired
    private IMarketingIntegralTaskService iMarketingIntegralTaskService;

    @Autowired
    private IMarketingFourthIntegralSettingService iMarketingFourthIntegralSettingService;

    @Autowired
    private IMarketingThirdIntegralSettingService iMarketingThirdIntegralSettingService;

    @Autowired
    private IMemberThirdIntegralService iMemberThirdIntegralService;

    @Autowired
    private PromotionCodeUtils promotionCodeUtils;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMarketingDistributionLevelService iMarketingDistributionLevelService;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private MemberUtils memberUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IMemberBusinessDesignationService iMemberBusinessDesignationService;

    @Autowired
    private IMarketingBusinessDesignationService iMarketingBusinessDesignationService;

    @Autowired
    private IMemberGiveWelfarePaymentsService iMemberGiveWelfarePaymentsService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    /**
     * 支付密码验证接口
     *
     * 张靠勤  2021-4-2
     *
     * @param srcTransactionPassword
     * @param memberId
     * @return
     */
    @RequestMapping("srcTransactionPasswordValid")
    @ResponseBody
    public Result<?> srcTransactionPasswordValid(String srcTransactionPassword
            ,@RequestAttribute("memberId") String memberId){
        MemberList memberList=iMemberListService.getById(memberId);
        if(StringUtils.isNotBlank(memberList.getTransactionPassword())&&StringUtils.isBlank(srcTransactionPassword)){
            return Result.error("原交易密码不能为空");
        }
        log.info("支付密码校验：加密密码="+memberList.getTransactionPassword()+"；用户输入密码="+srcTransactionPassword);

        String passwd=null;
        if(StringUtils.isNotBlank(memberList.getTransactionPassword())) {
            passwd = PasswordUtil.decrypt(memberList.getTransactionPassword(), srcTransactionPassword, PasswordUtil.Salt);
        }
        if(StringUtils.isBlank(passwd) ||
                (!PasswordUtil.decrypt(memberList.getTransactionPassword(),srcTransactionPassword,PasswordUtil.Salt).equals(srcTransactionPassword))){
            return Result.error("交易密码错误");
        }
        return Result.ok("支付密码验证成功");
    }


    /**
     * 设置用户交易密码
     *
     * 张靠勤    2021-4-2
     *
     * @param transactionPassword
     * @param memberId
     * @return
     */
    @RequestMapping("settingTransactionPassword")
    @ResponseBody
    public Result<?> settingTransactionPassword(String transactionPassword
            ,@RequestAttribute("memberId") String memberId){
        //参数验证
        if(StringUtils.isBlank(transactionPassword)){
            return Result.error("交易密码参数不能为空");
        }
        //交易密码领取积分
        iMarketingIntegralTaskService.transactionPassword(memberId);
        MemberList memberList=iMemberListService.getById(memberId);
       memberList.setTransactionPassword(PasswordUtil.encrypt(transactionPassword,transactionPassword,PasswordUtil.Salt));
        iMemberListService.saveOrUpdate(memberList);
        return Result.ok("交易密码设置成功");
    }



    /**
     * 获取会员信息
     *
     * @param
     * @return
     */
    @RequestMapping("getMemberInfo")
    @ResponseBody
    public Result<Map<String,Object>> getMemberInfo(@RequestHeader(defaultValue = "") String sysUserId,
                                                    @RequestAttribute("memberId") String memberId,
                                                    @RequestHeader(name = "softModel",required = false,defaultValue = "") String softModel){
        Result<Map<String,Object>> result =new Result<>();

        Map<String,Object> memberObjectMap  = Maps.newHashMap();

        //查询个人中心信息
        MemberList memberList=iMemberListService.getById(memberId);
        //设置默认头像
        if(StringUtils.isBlank(memberList.getHeadPortrait())){
            String memberDefaultPhotoUrl= notifyUrlUtils.getImgUrl("member_default_photo_url");
            memberList.setHeadPortrait(memberDefaultPhotoUrl);
            iMemberListService.saveOrUpdate(memberList);
        }

        memberObjectMap.put("id",memberId);
        memberObjectMap.put("memberType",memberList.getMemberType());
        memberObjectMap.put("welfarePayments",memberList.getWelfarePayments());
        memberObjectMap.put("haveWithdrawal",memberList.getHaveWithdrawal());
        memberObjectMap.put("balance",memberList.getBalance());
        memberObjectMap.put("shareTimes",memberList.getShareTimes());
        memberObjectMap.put("isOpenStore",memberList.getIsOpenStore());
        memberObjectMap.put("phone",memberList.getPhone());
        memberObjectMap.put("accountFrozen",memberList.getAccountFrozen());
        memberObjectMap.put("unusableFrozen",memberList.getUnusableFrozen());
        memberObjectMap.put("welfarePaymentsFrozen",memberList.getWelfarePaymentsFrozen());
        memberObjectMap.put("welfarePaymentsUnusable",memberList.getWelfarePaymentsUnusable());
        memberObjectMap.put("nickName",memberList.getNickName());
        memberObjectMap.put("avatarUrl",memberList.getHeadPortrait());
        memberObjectMap.put("headPortrait",memberList.getHeadPortrait());
        memberObjectMap.put("openid",memberList.getOpenid());
        memberObjectMap.put("createTime", DateUtils.formatDate(memberList.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));

        //是否设置交易密码
        if(StringUtils.isBlank(memberList.getTransactionPassword())){
            memberObjectMap.put("transactionPassword","0");
        }else{
            memberObjectMap.put("transactionPassword","1");
        }

        //增加分享二维码
        if (StringUtils.isBlank(memberList.getSysSmallcodeId())) {
            //添加分享二维码信息
            iMemberListService.addShareQr(memberList,sysUserId);
        }

        //二维码地址
        if(StringUtils.isNotBlank(memberList.getSysSmallcodeId())){
            memberObjectMap.put("address",iSysSmallcodeService.getById(memberList.getSysSmallcodeId()).getAddress());
        }else{
            memberObjectMap.put("address","");
        }

        //用户个人二维码
        if(StringUtils.isBlank(memberList.getQrcodeAddr())){
            memberList.setQrcodeAddr(qrCodeUtils.getMemberQrCode(memberList.getId()));
            iMemberListService.saveOrUpdate(memberList);
        }
        //生成推广码
        if(StringUtils.isBlank(memberList.getPromotionCode())){
            memberList.setPromotionCode(promotionCodeUtils.getCode());
            iMemberListService.saveOrUpdate(memberList);
        }
        memberObjectMap.put("promotionCode",memberList.getPromotionCode());
        memberObjectMap.put("qrcodeAddr",memberList.getQrcodeAddr());
        //券的数量
        QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper=new QueryWrapper<>();
        marketingDiscountCouponQueryWrapper.eq("member_list_id",memberId);
        marketingDiscountCouponQueryWrapper.in("status","0","1");
        memberObjectMap.put("discountCouponCount",iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper));
        //兑换券数量
        QueryWrapper<MarketingCertificateRecord> marketingCertificateRecordQueryWrapper=new QueryWrapper<>();
        marketingCertificateRecordQueryWrapper.eq("member_list_id",memberId);
        marketingCertificateRecordQueryWrapper.in("status","0","1");
        memberObjectMap.put("certificateRecordCount",iMarketingCertificateRecordService.count(marketingCertificateRecordQueryWrapper));

        memberObjectMap.put("sysUserId",memberList.getSysUserId());

        //判断渠道id归属渠道id
        if(StringUtils.isNotBlank(sysUserId)){
            String systemSharingModel= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","system_sharing_model");
            if(systemSharingModel.equals("1")){
                memberObjectMap.put("sysUserId",sysUserId);
            }
        }

        if(StringUtils.isNotBlank(memberList.getSysUserId())){
            QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
            storeManageQueryWrapper.eq("sys_user_id", memberList.getSysUserId());
            storeManageQueryWrapper.in("pay_status", "1","2");
            if(iStoreManageService.count(storeManageQueryWrapper)>0) {
                StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                if (storeManage.getSubStoreName() == null) {
                    memberObjectMap.put("affiliationStore", storeManage.getStoreName());
                } else {
                    memberObjectMap.put("affiliationStore", storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
                }
            }else{
                memberObjectMap.put("affiliationStore","无");
            }
        }else{
            memberObjectMap.put("affiliationStore","无");
        }

        memberObjectMap.put("totalCommission",memberList.getTotalCommission());

        //推荐人
        memberObjectMap.put("promoterMan",memberUtils.getPromoterMan(memberList.getPromoterType(),memberList.getPromoter()));
        /**
         * 会员银行卡
         */
        MemberBankCard memberBankCard = iMemberBankCardService.getOne(new LambdaQueryWrapper<MemberBankCard>().
                eq(MemberBankCard::getMemberListId, memberId)
                        .eq(MemberBankCard::getCarType, "0")
                .orderByDesc(MemberBankCard::getCreateTime)
                .last("limit 1"));
        if (oConvertUtils.isEmpty(memberBankCard)){
            memberObjectMap.put("memberBankCard","");
        }else {
            memberObjectMap.put("memberBankCard",memberBankCard.getBankName()+new StringBuilder(new StringBuilder(memberBankCard.getBankCard())
                    .reverse().toString().substring(0,4)).reverse().toString());
            memberObjectMap.put("memberBankCardInfo",memberBankCard);
        }
        //会员等级刷新
        LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                .eq(MemberGrade::getDelFlag, "0")
                .eq(MemberGrade::getStatus, "1");
        if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0){
            memberObjectMap.put("isViewGrade","1");
            if (memberList.getMemberType().equals("0")){
                memberGradeLambdaQueryWrapper
                        .le(MemberGrade::getGrowthValueSmall, memberList.getGrowthValue())
                        .ge(MemberGrade::getGrowthValueBig, memberList.getGrowthValue())
                        .orderByAsc(MemberGrade::getSort);
                if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0){
                    MemberGrade memberGrade = iMemberGradeService.list(memberGradeLambdaQueryWrapper).get(0);
                    memberList.setMemberGradeId(memberGrade.getId());
                    memberList.setMemberType("1");
                    memberList.setVipTime(new Date());
                    iMemberListService.saveOrUpdate(memberList);
                }
                if (StringUtils.isBlank(memberList.getMemberGradeId())){
                    memberObjectMap.put("memberGradeName","普通会员");
                    memberObjectMap.put("memberGradeLogo","");
                }else {
                    MemberGrade memberGrade = iMemberGradeService.getById(memberList.getMemberGradeId());
                    memberObjectMap.put("memberGradeName",memberGrade.getGradeName());
                    memberObjectMap.put("memberGradeLogo",memberGrade.getGradeLogo());
                }
                memberObjectMap.put("memberGrowthValue",memberList.getGrowthValue());
            }
            if (StringUtils.isNotBlank(memberList.getMemberGradeId())){
                MemberGrade memberGrade = iMemberGradeService.getById(memberList.getMemberGradeId());
                if(memberGrade!=null) {
                    memberObjectMap.put("memberGradeName", memberGrade.getGradeName());
                    memberObjectMap.put("memberGradeLogo", memberGrade.getGradeLogo());
                    memberObjectMap.put("memberGrowthValue", memberList.getGrowthValue());
                }else{
                    memberObjectMap.put("memberGradeName", "");
                    memberObjectMap.put("memberGradeLogo", "");
                    memberObjectMap.put("memberGrowthValue", "");
                }
            }
            if (memberList.getMemberType().equals("1")&&StringUtils.isBlank(memberList.getMemberGradeId())){
                MemberGrade memberGrade = iMemberGradeService.list(memberGradeLambdaQueryWrapper.eq(MemberGrade::getSort, "0")).get(0);
                memberList.setMemberGradeId(memberGrade.getId());
                iMemberListService.saveOrUpdate(memberList);
                memberObjectMap.put("memberGradeName",memberGrade.getGradeName());
                memberObjectMap.put("memberGradeLogo",memberGrade.getGradeLogo());
                memberObjectMap.put("memberGrowthValue",memberList.getGrowthValue());
            }

        }else {
            memberObjectMap.put("isViewGrade","0");
        }

        //分销称号刷新
        MemberDistributionLevel memberDistributionLevelpro=iMemberDistributionLevelService.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,memberId).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
        if(memberDistributionLevelpro==null){
            memberDistributionLevelpro=new MemberDistributionLevel();
            memberDistributionLevelpro.setMemberListId(memberId);
            MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
                    .eq(MarketingDistributionLevel::getStatus,"1")
                    .eq(MarketingDistributionLevel::getWaysObtain,"0")
                    .orderByAsc(MarketingDistributionLevel::getGrade)
                    .last("limit 1"));
            if(marketingDistributionLevel!=null){
                memberDistributionLevelpro.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
            }
            iMemberDistributionLevelService.save(memberDistributionLevelpro);
        }
        if(StringUtils.isBlank(memberDistributionLevelpro.getMarketingDistributionLevelId())){
            memberObjectMap.put("marketingDistributionLevelIcon","");
        }else{
            MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getById(memberDistributionLevelpro.getMarketingDistributionLevelId());
            if(marketingDistributionLevel!=null) {
                memberObjectMap.put("marketingDistributionLevelIcon", marketingDistributionLevel.getIcon());
            }else{
                memberObjectMap.put("marketingDistributionLevelIcon","");
            }
        }


        //会员称号
        LambdaQueryWrapper<MemberDesignation> memberDesignationLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignation>()
                .eq(MemberDesignation::getDelFlag, "0")
                .eq(MemberDesignation::getStatus, "1");
        if (iMemberDesignationService.count(memberDesignationLambdaQueryWrapper)>0){
            memberObjectMap.put("isViewDesignation","1");
            LambdaQueryWrapper<MemberDesignationMemberList> memberDesignationMemberListLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignationMemberList>()
                    .eq(MemberDesignationMemberList::getDelFlag, "0")
                    .eq(MemberDesignationMemberList::getMemberListId, memberList.getId());
            if (iMemberDesignationMemberListService.count(memberDesignationMemberListLambdaQueryWrapper)>0){
                MemberDesignationMemberList memberDesignationMemberList = iMemberDesignationMemberListService.getOne(memberDesignationMemberListLambdaQueryWrapper
                        .orderByDesc(MemberDesignationMemberList::getCreateTime).last("limit 1"));
                MemberDesignation memberDesignation = iMemberDesignationService.getById(memberDesignationMemberList.getMemberDesignationId());
                if(memberDesignationMemberList!=null&&memberDesignation!=null){
                    memberObjectMap.put("memberdesignationName",memberDesignation.getName());
                    memberObjectMap.put("memberDesignationLogoAddr",memberDesignation.getLogoAddr());
                }
            }else {
                MemberDesignation memberDesignation = iMemberDesignationService
                        .getOne(memberDesignationLambdaQueryWrapper
                                .eq(MemberDesignation::getSort,"0")
                                .eq(MemberDesignation::getIsDefault,"1")
                                .isNull(MemberDesignation::getMemberDesignationGroupId)
                                .last("limit 1")
                        );
                iMemberDesignationMemberListService.save(new MemberDesignationMemberList()
                        .setMemberListId(memberList.getId())
                        .setMemberDesignationId(memberDesignation.getId())
                        .setTotalMembers(new BigDecimal(1))
                        .setMemberJoinTime(new Date())
                        .setIsBuyGift("0")
                );

                if (iMemberDesignationCountService.count(new LambdaQueryWrapper<MemberDesignationCount>()
                        .eq(MemberDesignationCount::getDelFlag,"0")
                        .eq(MemberDesignationCount::getMemberDesignationId,memberDesignation.getId())
                        .eq(MemberDesignationCount::getMemberListId,memberList.getId())
                )<=0){
                    iMemberDesignationCountService.save(new MemberDesignationCount()
                            .setDelFlag("0")
                            .setMemberDesignationId(memberDesignation.getId())
                            .setMemberListId(memberList.getId())
                            .setTotalMembers(new BigDecimal(1))
                    );
                }

                memberObjectMap.put("isViewDesignation","1");
                memberObjectMap.put("memberdesignationName",memberDesignation.getName());
                memberObjectMap.put("memberDesignationLogoAddr",memberDesignation.getLogoAddr());
            }
        }else {
            memberObjectMap.put("isViewDesignation","0");
        }
        //积分模块的显示和隐藏
        //用户端福利金收款功能开关：0：关闭；1：开启；
        String welfareGathering= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","welfare_gathering"),"1");
        //welfare_present  用户端福利金赠送功能开关：0：关闭；1：开启；
        String welfarePresent= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","welfare_present"),"1");
        //welfare_payment  用户端福利金线下付款功能开关：0：关闭；1：开启；
        String welfarePayment= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","welfare_payment"),"1");
        //welfare_cash_balance  积分兑换成余额的开关：0：关闭；1：开启；
        String welfareCashBalance= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","welfare_cash_balance"),"1");

        memberObjectMap.put("welfareGathering",welfareGathering);
        memberObjectMap.put("welfarePresent",welfarePresent);
        memberObjectMap.put("welfarePayment",welfarePayment);
        memberObjectMap.put("welfareCashBalance",welfareCashBalance);

        //个人中心跳转类型
        String personalCenterJumpType= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","personal_center_jump_type");
        memberObjectMap.put("personalCenterJumpType",personalCenterJumpType);

        //余额充值的显示和隐藏
        String balanceRechargeState= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","balance_recharge_state"),"1");
        memberObjectMap.put("balanceRechargeState",balanceRechargeState);


        //优惠券开关，关闭后个人中心无法访问我的优惠券。0：关闭；1：开启；
        String discountCouponState= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","discount_coupon_state"),"1");
        memberObjectMap.put("discountCouponState",discountCouponState);

        //兑换券开关，关闭后个人中心无法访问我的兑换券。0：关闭；1：开启；
        String certificateState= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","certificate_state"),"1");
        memberObjectMap.put("certificateState",certificateState);


        //免费积分主页开关，关闭后个人中心无法访问免费积分主页。0：关闭；1：开启；
        String freePointsMainPageState= StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","free_points_main_page_state"),"1");
        memberObjectMap.put("freePointsMainPageState",freePointsMainPageState);


        //免费积分
        memberObjectMap.putAll(iMarketingIntegralSettingService.getgetMarketingIntegralSettingMap(softModel));

        //开店显示：
        String openShopDisplay= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","open_shop_display");
        memberObjectMap.put("openShopDisplay",openShopDisplay);
        memberObjectMap.put("integral",memberList.getIntegral());
        //第四积分的显示和隐藏和第四积分的数量
        MarketingFourthIntegralSetting marketingFourthIntegralSetting=iMarketingFourthIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingFourthIntegralSetting>()
                .eq(MarketingFourthIntegralSetting::getStatus,"1"));
        Map<String,Object> marketingFourthIntegralSettingMap=Maps.newHashMap();
        if(marketingFourthIntegralSetting==null){
            marketingFourthIntegralSettingMap.put("isViewmarketingFourthIntegralSetting","0");
        }else {
            log.info("第四积分端控制：softModel=" + softModel);
            if (marketingFourthIntegralSetting.getPointsDisplay().equals("0")) {
                marketingFourthIntegralSettingMap.put("isViewmarketingFourthIntegralSetting", "1");
            } else
            //小程序
            if (softModel.equals("0") && marketingFourthIntegralSetting.getPointsDisplay().equals("1")) {
                marketingFourthIntegralSettingMap.put("isViewmarketingFourthIntegralSetting", "1");
            } else if ((softModel.equals("1") || softModel.equals("2")) && marketingFourthIntegralSetting.getPointsDisplay().equals("2")) {
                marketingFourthIntegralSettingMap.put("isViewmarketingFourthIntegralSetting", "1");
            } else {
                marketingFourthIntegralSettingMap.put("isViewmarketingFourthIntegralSetting", "0");
            }
            marketingFourthIntegralSettingMap.put("integralName",marketingFourthIntegralSetting.getIntegralName());
        }
        marketingFourthIntegralSettingMap.put("fourthIntegral",memberList.getFourthIntegral());
        memberObjectMap.put("marketingFourthIntegralSettingMap",marketingFourthIntegralSettingMap);

        //判断用户是否设置密码
        if(StringUtils.isBlank(memberList.getPassword())){
            memberObjectMap.put("isSetPassword","0");
        }else{
            memberObjectMap.put("isSetPassword","1");
        }

        //福利金显示和隐藏
        MarketingWelfarePaymentsSetting marketingWelfarePaymentsSetting=iMarketingWelfarePaymentsSettingService.getOne(new LambdaQueryWrapper<MarketingWelfarePaymentsSetting>().eq(MarketingWelfarePaymentsSetting::getStatus,"1"));
        if(marketingWelfarePaymentsSetting==null){
            memberObjectMap.put("isViewMarketingWelfarePaymentsSetting","0");
        }else{
            memberObjectMap.put("isViewMarketingWelfarePaymentsSetting","1");
            memberObjectMap.put("integralValue",marketingWelfarePaymentsSetting.getIntegralValue());
        }


        //第三积分的显示和隐藏
        MarketingThirdIntegralSetting marketingThirdIntegralSetting=iMarketingThirdIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingThirdIntegralSetting>()
                .eq(MarketingThirdIntegralSetting::getStatus,"1"));
        Map<String,Object> marketingThirdIntegralSettingMap=Maps.newHashMap();
        if(marketingThirdIntegralSetting==null){
            marketingThirdIntegralSettingMap.put("isViewMarketingThirdIntegralSettingMap","0");
        }else {

            log.info("第四积分端控制：softModel=" + softModel);
            if (marketingThirdIntegralSetting.getPointsDisplay().equals("0")) {
                marketingThirdIntegralSettingMap.put("isViewMarketingThirdIntegralSettingMap", "1");
            } else
            //小程序
            if (softModel.equals("0") && marketingThirdIntegralSetting.getPointsDisplay().equals("1")) {
                marketingThirdIntegralSettingMap.put("isViewMarketingThirdIntegralSettingMap", "1");
            } else if ((softModel.equals("1") || softModel.equals("2")) && marketingThirdIntegralSetting.getPointsDisplay().equals("2")) {
                marketingThirdIntegralSettingMap.put("isViewMarketingThirdIntegralSettingMap", "1");
            } else {
                marketingThirdIntegralSettingMap.put("isViewMarketingThirdIntegralSettingMap", "0");
            }
            marketingThirdIntegralSettingMap.put("integralName",marketingThirdIntegralSetting.getIntegralName());
            marketingThirdIntegralSettingMap.put("totalIntegral",iMemberThirdIntegralService.totalIntegral(memberId));
        }
        memberObjectMap.put("marketingThirdIntegralSettingMap",marketingThirdIntegralSettingMap);
        MemberBusinessDesignation memberBusinessDesignation = iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                .eq(MemberBusinessDesignation::getMemberListId, memberList.getId())
                .orderByDesc(MemberBusinessDesignation::getCreateTime)
                .last("limit 1")
        );
        if (memberBusinessDesignation != null){
            memberObjectMap.put("isViewMemberBusinessDesignation","1");
            MarketingBusinessDesignation marketingBusinessDesignation = iMarketingBusinessDesignationService.getById(memberBusinessDesignation.getMarketingBusinessDesignationId());
            if (marketingBusinessDesignation!=null){
                memberObjectMap.put("designationName",marketingBusinessDesignation.getDesignationName());
                memberObjectMap.put("icon",marketingBusinessDesignation.getIcon());
            }else {
                memberObjectMap.put("designationName","");
            }
        }else {
            memberObjectMap.put("isViewMemberBusinessDesignation","0");
            memberObjectMap.put("designationName","");
        }
        result.setResult(memberObjectMap);
        result.success("获取会员信息成功");
        return result;
    }

    /**
     * 查询用户佣金信息
     *
     * @return
     */
    @RequestMapping("findMemberPromoter")
    @ResponseBody
    public Result<Map<String,Object>> findMemberPromoter(@RequestAttribute("memberId") String memberId){
        Result<Map<String,Object>> result =new Result<>();

        Map<String,Object> objectMap  = Maps.newHashMap();

        //佣金笔数
        objectMap.put("memberRechargeRecordProtomerCoun",iMemberRechargeRecordService.findMemberRechargeRecordProtomerCount(memberId));

        //提现笔数

        objectMap.put("memberRechargeRecordDepositCount",iMemberRechargeRecordService.findMemberRechargeRecordDepositCount(memberId));

        //团队总人数

        MemberListVO memberDistributionCount = iMemberListService.findMemberDistributionCount(memberId);
        if (oConvertUtils.isEmpty(memberDistributionCount)){
            objectMap.put("memberDistributionCount",0);
        }else {
            MarketingDistributionSetting marketingDistributionSetting=iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>().eq(MarketingDistributionSetting::getStatus,"1"));
            if(marketingDistributionSetting!=null&&marketingDistributionSetting.getDistributionLevel().equals("1")) {
                MemberDistributionLevel memberDistributionLevel = iMemberDistributionLevelService.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId, memberId));
                if (memberDistributionLevel != null && StringUtils.isNotBlank(memberDistributionLevel.getMarketingDistributionLevelId())) {
                    objectMap.put("memberDistributionCount",memberDistributionLevel.getUpgradeTeamNumber());
                } else {
                    objectMap.put("memberDistributionCount",0);
                }
            }else{
                objectMap.put("memberDistributionCount",memberDistributionCount.getMlSum());
            }
        }
        //海报
        QueryWrapper<MarketingDistributionSetting> marketingDistributionSettingQueryWrapper = new QueryWrapper<>();
        marketingDistributionSettingQueryWrapper.eq("status", "1");
        marketingDistributionSettingQueryWrapper.orderByDesc("create_time");
        if (iMarketingDistributionSettingService.count(marketingDistributionSettingQueryWrapper) > 0) {
            MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.list(marketingDistributionSettingQueryWrapper).get(0);
            objectMap.put("distributionPosters",marketingDistributionSetting.getDistributionPosters());
        }else{
            objectMap.put("distributionPosters","");
        }


        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("pattern","0");
        paramObjectMap.put("memberId",memberId);
        //待付款
        objectMap.put("obligation",iMemberRechargeRecordService.findMemberRechargeRecordSum(paramObjectMap));

        paramObjectMap.put("pattern","2");
        //已付款
        objectMap.put("accountPaid",iMemberRechargeRecordService.findMemberRechargeRecordSum(paramObjectMap));

        paramObjectMap.put("pattern","1");
        //待审核
        objectMap.put("pending",iMemberRechargeRecordService.findMemberRechargeRecordWithdrawSum(paramObjectMap));
        paramObjectMap.put("pattern","3");
        //待打款
        objectMap.put("playwith",iMemberRechargeRecordService.findMemberRechargeRecordWithdrawSum(paramObjectMap));
        paramObjectMap.put("pattern","5");
        //已打款
        objectMap.put("haveMoney",iMemberRechargeRecordService.findMemberRechargeRecordWithdrawSum(paramObjectMap));
        paramObjectMap.put("pattern","7");
        //无效
        objectMap.put("invalid",iMemberRechargeRecordService.findMemberRechargeRecordWithdrawSum(paramObjectMap));
        result.setResult(objectMap);
        result.success("获取会员佣金信息成功");
        return result;
    }

    /**
     * 增加分享次数
     * @param
     * @return
     */
    @RequestMapping("addMemberShareTimes")
    @ResponseBody
    public Result<String> addMemberShareTimes(@RequestAttribute("memberId") String memberId){
        Result<String> result=new Result<>();
        MemberList memberList=iMemberListService.getById(memberId);
        //增加分享次数
        memberList.setShareTimes(memberList.getShareTimes().add(new BigDecimal(1)));
        iMemberListService.saveOrUpdate(memberList);
        result.setResult(memberList.getShareTimes().toString());
        result.success("分享次数增加成功");
        return result;
    }

    /**
     * 查询用户的级别按照时间排序
     * @param
     * @return
     */
    @RequestMapping("findMemberLevelList")
    @ResponseBody
    @Deprecated
    public Result<IPage<Map<String,Object>>> findMemberLevelList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                 @RequestAttribute("memberId") String memberId){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);

        result.setResult(iMemberListService.findMemberLevelList(page,memberId));

        result.success("查询会员级别成功");
        return result;
    }

    /**
     * 查询我的订单不同状态的数量,逛好店,收藏接口
     * @param request
     * @return
     */
    @RequestMapping("memberGoodAndOrderCount")
    @ResponseBody
    public Result<Map<String,Object>> memberGoodAndOrderCount(HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> map = Maps.newLinkedHashMap();
        if(StringUtils.isBlank(memberId)){
         return    result.error500("memberId不能为空!");
        }

        //订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败
        //待付款(平台)
        long obligationCount =  iOrderListService.count(new LambdaQueryWrapper<OrderList>().and(or->or
                .eq(OrderList::getDelFlag,"0")
                .eq(OrderList::getMemberListId,memberId)
                .eq(OrderList::getStatus,"0")));
        //待付款(店铺)
        long obligationCountStore =  iOrderStoreListService.count(new LambdaQueryWrapper<OrderStoreList>().and(or->or
                .eq(OrderStoreList::getDelFlag,"0")
                .eq(OrderStoreList::getMemberListId,memberId)
                .eq(OrderStoreList::getStatus,"0")));
        map.put("obligationCount",obligationCount+obligationCountStore);

        //待收货（已发货）(平台)
        long waitForReceivingCount =  iOrderListService.count(new LambdaQueryWrapper<OrderList>().and(or->or
                .eq(OrderList::getDelFlag,"0")
                .eq(OrderList::getMemberListId,memberId)
                .eq(OrderList::getStatus,"2")));
        //待收货（已发货）(店铺)
        long waitForReceivingCountStore =  iOrderStoreListService.count(new LambdaQueryWrapper<OrderStoreList>().and(or->or
                .eq(OrderStoreList::getDelFlag,"0")
                .eq(OrderStoreList::getMemberListId,memberId)
                .eq(OrderStoreList::getStatus,"2")));
        map.put("waitForReceivingCount",waitForReceivingCount+waitForReceivingCountStore);
        //交易成功(平台)
        long dealsAreDoneCount =  iOrderListService.count(new LambdaQueryWrapper<OrderList>().and(or->or
                .eq(OrderList::getDelFlag,"0")
                .eq(OrderList::getMemberListId,memberId)
                .eq(OrderList::getStatus,"3")
                .eq(OrderList::getIsEvaluate,"0")));
        //交易成功(店铺)
        long dealsAreDoneCountStore =  iOrderStoreListService.count(new LambdaQueryWrapper<OrderStoreList>().and(or->or
                .eq(OrderStoreList::getDelFlag,"0")
                .eq(OrderStoreList::getMemberListId,memberId)
                .eq(OrderStoreList::getStatus,"3")
                .eq(OrderStoreList::getIsEvaluate,"0")));
        map.put("dealsAreDoneCount",dealsAreDoneCount+dealsAreDoneCountStore);
        result.setResult(map);
        result.success("查询成功!");
       return   result;
    }

    /**
     * 通过id查询会员信息
     * @param id
     * @return
     */
    @RequestMapping("findMemberById")
    @ResponseBody
    public Result<Map<String,Object>>findMemberById(String id){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        MemberList memberList = iMemberListService.getById(id);
        if (oConvertUtils.isNotEmpty(memberList)){
            map.put("phone",memberList.getPhone());
            map.put("id",memberList.getId());
            MemberGiveWelfarePayments memberGiveWelfarePayments=iMemberGiveWelfarePaymentsService.getOne(new LambdaQueryWrapper<MemberGiveWelfarePayments>()
                    .eq(MemberGiveWelfarePayments::getMemberListId,map.get("id"))
                    .orderByDesc(MemberGiveWelfarePayments::getCreateTime)
                    .last("limit 1"));
            if(memberGiveWelfarePayments==null){
                map.put("giveWelfarePayments",0);
            }else{
                map.put("giveWelfarePayments",memberGiveWelfarePayments.getGiveWelfarePayments());
            }
            result.setResult(map);
            result.success("返回会员信息");
        }else {
            result.error500("会员信息异常,请联系管理员");
        }
        return result;
    }

    @RequestMapping("getMemberManageInfo")
    @ResponseBody
    public Result<Map<String,Object>> getMemberManageInfo(HttpServletRequest request,
                                                          String id,
                                                          String memberDesignationGroupId,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Result<Map<String, Object>> result = new Result<>();
        String memberId=request.getAttribute("memberId").toString();
        Page<Map<String, Object>> page = new Page<>(pageNo,pageSize);
        log.info("memberListId++++++++++"+id);
        if (StringUtils.isBlank(id)){
            result.setResult(memberManageMap(memberId,page,memberId,memberDesignationGroupId));
        }else {
            if (id.equals(memberId)){
                result.setResult(memberManageMap(memberId,page,memberId,memberDesignationGroupId));
            }else {
                result.setResult(memberManageMap(id,page,memberId,memberDesignationGroupId));
            }
        }
        result.success("我的团队");
        return result;
    }

    private Map<String,Object> memberManageMap(String id,Page<Map<String, Object>> page,String memberId,String memberDesignationGroupId){

        log.info("memberDesignationGroupId++++++++++"+memberDesignationGroupId);

        HashMap<String, Object> map = new HashMap<>();

        MemberList memberList = iMemberListService.getById(id);

        MemberDesignationMemberList designationMemberList = iMemberDesignationMemberListService.getById(memberDesignationGroupId);

        MemberDesignationMemberList memberDesignationMemberList = iMemberDesignationMemberListService.list(new LambdaQueryWrapper<MemberDesignationMemberList>()
                .eq(MemberDesignationMemberList::getDelFlag, "0")
                .eq(MemberDesignationMemberList::getMemberListId, memberList.getId())
                .eq(MemberDesignationMemberList::getMemberDesignationGroupId, designationMemberList.getMemberDesignationGroupId())
        ).get(0);

        map.put("id",memberList.getId());
        map.put("logo",memberList.getHeadPortrait());
        map.put("nickName",memberList.getNickName());
        map.put("phone",memberList.getPhone());

        map.put("tManageId",memberDesignationMemberList.getTManageId());
        map.put("isChange",memberDesignationMemberList.getIsChange());

        MemberDesignation memberDesignation = iMemberDesignationService.getById(memberDesignationMemberList.getMemberDesignationId());
        map.put("name",memberDesignation.getName());
        map.put("logoAddr",memberDesignation.getLogoAddr());
        map.put("totalMembers",memberDesignationMemberList.getTotalMembers());

        /*int count = iMemberListService.count(new LambdaQueryWrapper<MemberList>()
                .eq(MemberList::getDelFlag, "0")
                .eq(MemberList::getStatus, "1")
                .eq(MemberList::getTManageId, memberList.getId())
        );*/
        long count = iMemberDesignationMemberListService.count(new LambdaQueryWrapper<MemberDesignationMemberList>()
                .eq(MemberDesignationMemberList::getDelFlag, "0")
                .eq(MemberDesignationMemberList::getMemberDesignationGroupId, memberDesignationMemberList.getMemberDesignationGroupId())
                .eq(MemberDesignationMemberList::getTManageId, memberList.getId())
        );
        if (oConvertUtils.isEmpty(memberDesignationMemberList.getMemberJoinTime())){
            map.put("buyGiftTime","");
        }else {
            map.put("buyGiftTime",memberDesignationMemberList.getMemberJoinTime().getTime());
        }
        if (memberDesignationMemberList.getIsChange().equals("1")){
            map.put("isViewDesignate","0");
        }else {
            if (oConvertUtils.isNotEmpty(memberDesignationMemberList.getMemberJoinTime())){
                //当日23点59时59分
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                        23, 59, 59);
                //当日0点
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                        0, 0, 0);
                if (memberDesignationMemberList.getMemberJoinTime().getTime()>calendar1.getTime().getTime()&&memberDesignationMemberList.getMemberJoinTime().getTime()<calendar2.getTime().getTime()&&count<=0){
                    if (StringUtils.isNotBlank(memberDesignationMemberList.getTManageId())){
                        if (!memberDesignationMemberList.getTManageId().equals(memberId)){
                            map.put("isViewDesignate","0");
                        }else {
                            map.put("isViewDesignate","1");
                        }
                    }
                }else {
                    map.put("isViewDesignate","0");
                }
            }else {
                map.put("isViewDesignate","0");
            }
        }

        map.put("memberDirect",count);
        //团队礼包销售总额
        map.put("totalGiftSales",memberDesignationMemberList.getTotalGiftSales());

        List<MemberDesignationCountVO> listByMemberId = iMemberDesignationCountService.findListByMemberId(memberList.getId(),memberDesignationMemberList.getMemberDesignationGroupId());
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        listByMemberId.forEach(lbm->{
            HashMap<String, Object> objectHashMap = new HashMap<>();
            objectHashMap.put("designationName",lbm.getName());
            objectHashMap.put("designationTotalMembers",lbm.getTotalMembers());
            maps.add(objectHashMap);
        });
        map.put("designationList",maps);
        map.put("levelDownList",iMemberListService.getmemberListByTManageId(page,memberList.getId(),memberDesignationMemberList.getMemberDesignationGroupId()));
        return map;
    }

    /**
     * 推荐人列表信息
     *
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("pushingNumber")
    @ResponseBody
    public Result<?> pushingNumber( @RequestAttribute("memberId") String memberId,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        IPage<Map<String,Object>> resultMap=iMemberListService.pushingNumber(new Page<>(pageNo,pageSize),memberId);
        MarketingDistributionSetting marketingDistributionSetting=iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>().eq(MarketingDistributionSetting::getStatus,"1"));
        resultMap.getRecords().forEach(r->{
            //会员等级
            if(r.get("memberType").toString().equals("1")){
                if(r.get("memberGradeId")!=null) {
                    MemberGrade memberGrade = iMemberGradeService.getById(r.get("memberGradeId").toString());
                    if(memberGrade==null){
                        r.put("memberGradeIcon","");
                    }else{
                        r.put("memberGradeIcon",memberGrade.getGradeLogo());
                    }
                }else{
                    r.put("memberGradeIcon","");
                }
            }else{
                r.put("memberGradeIcon","");
            }
            //推荐人信息
            if(r.get("promoter")!=null) {
                r.put("promoterMan", memberUtils.getPromoterMan(r.get("promoterType").toString(), r.get("promoter").toString()));
            }else{
                r.put("promoterMan","无");
            }
            //归属店铺
            if(r.get("sysUserId")!=null&&!r.get("sysUserId").toString().equals("")){
                StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                        .eq(StoreManage::getSysUserId,r.get("sysUserId").toString())
                        .in(StoreManage::getPayStatus,"1","2")
                        .last("limit 1"));
                if (storeManage.getSubStoreName() == null) {
                    r.put("storeName",storeManage.getStoreName());
                } else {
                    r.put("storeName",storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
                }
            }else{
                r.put("storeName","无");
            }
            //分销等级
            MemberDistributionLevel memberDistributionLevel=iMemberDistributionLevelService.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId,r.get("id").toString()).orderByDesc(MemberDistributionLevel::getTeamNumber).last("limit 1"));
            if(memberDistributionLevel!=null&&StringUtils.isNotBlank(memberDistributionLevel.getMarketingDistributionLevelId())){
                MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getById(memberDistributionLevel.getMarketingDistributionLevelId());
                if(marketingDistributionLevel!=null){
                    r.put("distributionLevelIcon",marketingDistributionLevel.getIcon());
                }else{
                    r.put("distributionLevelIcon","");
                }
            }else{
                r.put("distributionLevelIcon","");
            }
            //好友人数
            if(marketingDistributionSetting!=null&&marketingDistributionSetting.getDistributionLevel().equals("1")){
                if(memberDistributionLevel!=null) {
                    r.put("distributionCount", memberDistributionLevel.getUpgradeTeamNumber());
                }else {
                    r.put("distributionCount", 0);
                }
            }else{
                if(r.get("promoter")!=null) {
                    r.put("distributionCount", iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPromoter,r.get("id").toString())));
                }else{
                    r.put("distributionCount", 0);
                }
            }
        });
        return Result.ok(resultMap);
    }

    @RequestMapping("getDistributionCount")
    @ResponseBody
    public Result<?> getDistributionCount(@RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap=Maps.newHashMap();
        MarketingDistributionSetting marketingDistributionSetting=iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>().eq(MarketingDistributionSetting::getStatus,"1"));
        if(marketingDistributionSetting!=null&&marketingDistributionSetting.getDistributionLevel().equals("1")) {
            MemberDistributionLevel memberDistributionLevel = iMemberDistributionLevelService.getOne(new LambdaQueryWrapper<MemberDistributionLevel>().eq(MemberDistributionLevel::getMemberListId, memberId));
            if (memberDistributionLevel != null && StringUtils.isNotBlank(memberDistributionLevel.getMarketingDistributionLevelId())) {
                resultMap.put("pushingNumber",memberDistributionLevel.getUpgradeDirect());
                resultMap.put("betweenNumber",memberDistributionLevel.getUpgradeTeamNumber().subtract(memberDistributionLevel.getUpgradeDirect()));
                resultMap.put("totalNumber",memberDistributionLevel.getUpgradeTeamNumber());
            } else {
                resultMap.put("pushingNumber",0);
                resultMap.put("betweenNumber",0);
                resultMap.put("totalNumber",0);
            }
        }else{
            resultMap.put("pushingNumber",iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPromoter,memberId)));
            resultMap.put("betweenNumber",iMemberListService.betweenPush(memberId));
            resultMap.put("totalNumber",Integer.parseInt(resultMap.get("pushingNumber").toString())+Integer.parseInt(resultMap.get("betweenNumber").toString()));
        }
        return Result.ok(resultMap);
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping("updatePasswd")
    @ResponseBody
    public Result<?> updatePasswd(@RequestAttribute("memberId") String memberId,String password,String phone,String captcha){
        if(StringUtils.isBlank(password)){
            return Result.error("密码不能为空");
        }
        if(StringUtils.isBlank(phone)){
            return Result.error("手机号不能为空");
        }
        if(StringUtils.isBlank(captcha)){
            return Result.error("验证码不能为空");
        }
        //核对验证码
        Object captchaContent=redisUtil.get(phone);
        if(captchaContent==null||!captchaContent.toString().equals(captcha)){
            return Result.error("验证码不正确");
        }
        MemberList memberList=iMemberListService.getById(memberId);
        password=StringUtils.trim(password);
        memberList.setPassword(PasswordUtil.encrypt(password,password,PasswordUtil.Salt));
        iMemberListService.saveOrUpdate(memberList);
        return Result.ok();
    }
}
