package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingWelfarePayments;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreWelfarePaymentsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("back/marketingWelfarePayments")
@Slf4j
public class BackMarketingWelfarePaymentsController {
    @Autowired
    private IMarketingWelfarePaymentsService marketingWelfarePaymentsService;
    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IStoreWelfarePaymentsRecordService iStoreWelfarePaymentsRecordService;

    @AutoLog(value = "店铺福利金记录表")
    @ApiOperation(value = "店铺福利金记录表", notes = "店铺福利金记录表")
    @RequestMapping("findStoreWelfarePayments")
    @ResponseBody
    public Result<Map<String, Object>> findStoreWelfarePayments(String weType,
                                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUserId);
        storeManageQueryWrapper.in("pay_status", "1", "2");
        storeManageQueryWrapper.eq("status", "1");
        if (iStoreManageService.count(storeManageQueryWrapper) > 0) {
            Map<String, Object> objectMap = Maps.newHashMap();
            StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
            Page<MarketingWelfarePayments> page = new Page<>(pageNo, pageSize);
            IPage<Map<String, Object>> storeWelfarePaymentMapList = marketingWelfarePaymentsService.findBackStoreWelfarePayments(page, weType, sysUserId);
            objectMap.put("storeWelfarePaymentMapList", storeWelfarePaymentMapList);
            objectMap.put("welfarePayments", storeManage.getWelfarePayments());
            result.setResult(objectMap);
            result.success("返回店铺福利金");
            return result;
        } else {
            return result.error500("店铺信息异常,请联系管理员");
        }
    }

    @AutoLog(value = "店铺福利金赠送记录表")
    @ApiOperation(value = "店铺福利金赠送记录表", notes = "店铺福利金赠送记录表")
    @RequestMapping("getPresenterStoreWelfarePayments")
    @ResponseBody
    public Result<IPage<Map<String, Object>>> getPresenterStoreWelfarePayments(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Result<IPage<Map<String, Object>>> result = new Result<>();

        String sysUserId = request.getAttribute("sysUserId").toString();
        Map<String, Object> objectMap = Maps.newHashMap();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUserId);
        storeManageQueryWrapper.in("pay_status", "1", "2");
        storeManageQueryWrapper.eq("status", "1");
        if (iStoreManageService.count(storeManageQueryWrapper) < 0) {
            result.error500("查询不到店铺用户信息！！！");
            return result;
        }
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);

        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        QueryWrapper<MarketingWelfarePayments> queryWrapperMarketingWelfarePayments = new QueryWrapper();
        queryWrapperMarketingWelfarePayments.select("id,create_time AS createTime,serial_number AS serialNumber,go_and_come AS goAndCome,bargain_payments AS bargainPayments,bargain_time AS bargainTime,welfare_pay AS welfarePay,balance_pay AS balancePay,give_explain AS giveExplain");
        queryWrapperMarketingWelfarePayments.eq("sys_user_id", sysUserId);
        queryWrapperMarketingWelfarePayments.eq("status", "1");
        queryWrapperMarketingWelfarePayments.eq("we_type", "0");
        queryWrapperMarketingWelfarePayments.eq("is_platform", "0");
        queryWrapperMarketingWelfarePayments.orderByDesc("create_time");
        IPage<Map<String, Object>> storeWelfarePaymentMapList = marketingWelfarePaymentsService.pageMaps(page, queryWrapperMarketingWelfarePayments);

        storeWelfarePaymentMapList.getRecords().forEach(spm -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            spm.put("createTime", simpleDateFormat.format(spm.get("createTime")));
            spm.put("bargainTime", simpleDateFormat.format(spm.get("bargainTime")));
        });

        //IPage<Map<String,Object>> storeWelfarePayments = marketingWelfarePaymentsService.pageMaps(page,queryWrapperMarketingWelfarePayments);
        result.setResult(storeWelfarePaymentMapList);
        result.setSuccess(true);
        result.setCode(200);
        return result;
    }

    /**
     * 扫码赠送福利金(扫码过程):商家端
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("sweepCodeWelfare")
    @ResponseBody
    public Result<Map<String, Object>> sweepCodeWelfare(String id, HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();
        //参数校验
        if (StringUtils.isBlank(id)) {
            result.error500("用户二维码不正确，请提供正确的用户二维码！");
            return result;
        }
        MemberList memberList = iMemberListService.getById(id);
        if (oConvertUtils.isEmpty(memberList)) {
            return result.error500("用户二维码不正确，请提供正确的用户二维码！");
        }
        objectMap.put("memberListId", memberList.getId());
        objectMap.put("phone", memberList.getPhone());
        result.setResult(objectMap);
        result.setCode(200);
        return result;
    }

    /**
     * 跳转赠送福利金页面
     *
     * @param request
     * @return
     */
    @RequestMapping("getStoreManageBalanceAndWelfarePayments")
    @ResponseBody
    public Result<Map<String, Object>> getStoreManageBalanceAndWelfarePayments(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();
        String sysUserId = request.getAttribute("sysUserId").toString();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUserId);
        storeManageQueryWrapper.in("pay_status", "1", "2");
        storeManageQueryWrapper.eq("status", "1");
        if (iStoreManageService.count(storeManageQueryWrapper) <= 0) {
            result.error500("查询不到店铺用户信息！！！");
            return result;
        }
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
        objectMap.put("welfarePayments", storeManage.getWelfarePayments());
        objectMap.put("balance", storeManage.getBalance());
        result.setResult(objectMap);
        result.setCode(200);
        return result;
    }

    /**
     * 赠送福利金
     *
     * @param phone welfarePayments giveExplain
     * @param
     * @return
     */
    @RequestMapping("postWelfarePayment")
    @ResponseBody
    public Result<String> postWelfarePayment(String phone,
                                             String welfarePayments,
                                             String giveExplain,
                                             String memberListId,
                                             HttpServletRequest request) {
        Result<String> result = new Result<>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        //赠送福利金金额
        BigDecimal bigDecimal = new BigDecimal(welfarePayments);

        if (bigDecimal.doubleValue() < 0) {
            return result.error500("赠送金额有误,请输入大于0的正数");
        }
        if (StringUtils.isBlank(phone)) {
            return result.error500("请输入手机号码");
        }
        LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId, sysUserId)
                .in(StoreManage::getPayStatus, "1", "2");

        if (iStoreManageService.count(storeManageLambdaQueryWrapper) <= 0) {
            return result.error500("查询不到店铺用户信息！！！");
        }

        //获取福利金比例
        MarketingWelfarePaymentsSetting welfarePaymentsSetting = iMarketingWelfarePaymentsSettingService.getOne(new LambdaUpdateWrapper<MarketingWelfarePaymentsSetting>()
                .eq(MarketingWelfarePaymentsSetting::getStatus, "1"));

        if (oConvertUtils.isEmpty(welfarePaymentsSetting)) {
            return result.error500("获取福利金比例失败,请查看福利金比例!");
        }

        StoreManage storeManage = iStoreManageService.list(storeManageLambdaQueryWrapper).get(0);

        BigDecimal welfarePaymentsSum = storeManage.getBalance()
                .add(storeManage.getWelfarePayments().multiply(welfarePaymentsSetting.getProportion().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_UP));

        if (welfarePaymentsSum.subtract(bigDecimal.multiply(welfarePaymentsSetting.getProportion().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_UP)).doubleValue() < 0) {
            return result.error500("余额不足,请充值!");
        }

        if (StringUtils.isNotBlank(memberListId)) {
            MemberList memberList = iMemberListService.getById(memberListId);
            if (oConvertUtils.isEmpty(memberList)) {
                return result.error500("会员信息异常!");
            } else {
                iMemberWelfarePaymentsService.postWelfarePayment(memberList, bigDecimal, storeManage, giveExplain, welfarePaymentsSetting.getProportion());
            }
        } else {
            LambdaQueryWrapper<MemberList> memberListLambdaQueryWrapper = new LambdaQueryWrapper<MemberList>()
                    .eq(MemberList::getPhone, phone);
            if (iMemberListService.count(memberListLambdaQueryWrapper) > 0) {
                MemberList memberList = iMemberListService.list(memberListLambdaQueryWrapper).get(0);
                iMemberWelfarePaymentsService.postWelfarePayment(memberList, bigDecimal, storeManage, giveExplain, welfarePaymentsSetting.getProportion());
            } else {
                return result.error500("会员信息异常!");
            }
        }
        result.success("赠送成功!");
        return result;

    }

    /**
     * 模糊查询根据手机号
     *
     * @param phone
     * @return
     */
    @AutoLog(value = "会员列表-模糊查询根据手机号")
    @ApiOperation(value = "会员列表-模糊查询根据手机号", notes = "会员列表-模糊查询根据手机号")
    @RequestMapping("likeMemberByPhone")
    @ResponseBody
    public Result<?> likeMemberByPhone(String phone, HttpServletRequest request) {
        QueryWrapper<MemberList> memberListQueryWrapper = new QueryWrapper<>();
        memberListQueryWrapper.eq("phone", phone);
        long size = iMemberListService.count(memberListQueryWrapper);
        if (size == 0) {
            return Result.error("未找到该手机号!!!");
        } else {
            return Result.ok("手机号存在!!!");
        }
    }

    /**
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("getMemberWelfare")
    @ResponseBody
    public Result<Map<String, Object>> getMemberWelfare(String id,
                                                        HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Object o = redisUtil.get(id);
        if (oConvertUtils.isEmpty(o)) {
            return result.error500("用户福利金付款二维码已过期");
        }
        MemberList memberList = iMemberListService.getById(o.toString());
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberName", memberList.getNickName() + " (" + memberList.getPhone() + ")");
        map.put("memberWelfarePayments", memberList.getWelfarePayments());
        map.put("memberId", memberList.getId());
        redisUtil.del(id);
        result.setResult(map);
        result.success("返回会员福利金");
        return result;
    }

    /**
     * 商家收福利金
     *
     * @param memberId
     * @param collectMoney
     * @param gatheringExplain
     * @param request
     * @return
     */
    @RequestMapping("storeCollectWelfare")
    @ResponseBody
    public Result<String> storeCollectWelfare(String memberId,
                                              String collectMoney,
                                              String gatheringExplain,
                                              HttpServletRequest request) {
        Result<String> result = new Result<>();

        String sysUserId = request.getAttribute("sysUserId").toString();

        MemberList memberList = iMemberListService.getById(memberId);

        BigDecimal money = new BigDecimal(collectMoney);

        if (memberList.getWelfarePayments().subtract(money).doubleValue() < 0) {
            return result.error500("会员福利金余额不足");
        }
        LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId, sysUserId)
                .in(StoreManage::getAttestationStatus, "1", "2")
                .in(StoreManage::getPayStatus, "1", "2")
                .eq(StoreManage::getStatus, "1")
                .eq(StoreManage::getIsOpenWelfarePayments, "1");

        if (iStoreManageService.count(storeManageLambdaQueryWrapper) <= 0) {
            return result.error500("店铺信息异常,请联系管理员");
        }

        StoreManage storeManage = iStoreManageService.list(storeManageLambdaQueryWrapper).get(0);

        Boolean b = iStoreWelfarePaymentsRecordService.storeCollectWelfare(memberList, storeManage, money, gatheringExplain);
        if (b){
            result.success("收款成功!");
        }else {
            return result.error500("收款失败");
        }

        return result;
    }
    @RequestMapping("likeMemberInfoByPhone")
    @ResponseBody
    public Result<List<Map<String,Object>>> likeMemberInfoByPhone(String phone,
                                                              HttpServletRequest request){
        Result<List<Map<String, Object>>> result = new Result<>();
        List<Map<String, Object>> maps = iMemberListService.likeMemberByPhone(phone);
        result.setResult(maps);
        result.success("通过手机获取会员信息");
        return result;
    }
}
