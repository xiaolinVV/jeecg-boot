package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingCertificateRecord;
import org.jeecg.modules.marketing.entity.MarketingCertificateStore;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.service.IMarketingCertificateRecordService;
import org.jeecg.modules.marketing.service.IMarketingCertificateStoreService;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 兑换券核销api
 */
@RequestMapping("back/marketingCertificate")
@Controller
@Slf4j
public class BackMarketingCertificateController {

    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private IMarketingCertificateStoreService iMarketingCertificateStoreService;
    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;
    @Autowired
    private IStoreAccountCapitalService iStoreAccountCapitalService;
    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    /**
     * 券的核销
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("verification")
    @ResponseBody
    public Result<String> verification(String id,
                                       HttpServletRequest request) {
        Result<String> result = new Result<>();
        String sysUserId = request.getAttribute("sysUserId").toString();

        log.info("券记录id: "+id);
        //参数校验
        if (StringUtils.isBlank(id)) {
            return result.error500("id不能为空！！！");
        }

        MarketingCertificateRecord marketingCertificateRecord = iMarketingCertificateRecordService.getById(id);
        if (oConvertUtils.isEmpty(marketingCertificateRecord)) {
            MarketingDiscountCoupon marketingDiscountCoupon = iMarketingDiscountCouponService.getById(id);
            if (oConvertUtils.isEmpty(marketingDiscountCoupon)){
                return result.error500("找不到该券,请联系管理员");
            }
            if (marketingDiscountCoupon.getIsPlatform().equals("1")){
                return result.error500("店铺暂不支持核销平台券");
            }
            if (marketingDiscountCoupon.getStatus().equals("0")){
                return result.error500("该券未生效");
            }
            if (marketingDiscountCoupon.getStatus().equals("2")){
                return result.error500("该券已使用");
            }
            if (marketingDiscountCoupon.getStatus().equals("3")){
                return result.error500("该券已过期");
            }
            if (marketingDiscountCoupon.getStatus().equals("4")){
                return result.error500("该券已失效");
            }
            if (marketingDiscountCoupon.getStatus().equals("5")){
                return result.error500("该券已赠送");
            }
            marketingDiscountCoupon
                    .setStatus("2")
                    .setVerification("1")
                    .setVerificationType("0")
                    .setCancelAfterVerificationId(sysUserId);
            iMarketingDiscountCouponService.updateById(marketingDiscountCoupon);
            return result.success("核销成功");
        }else {
            if (!marketingCertificateRecord.getStatus().equals("1")) {
                return result.error500("券的状态不正确或者已被使用！！！");
            }
            if (iMarketingCertificateRecordService.count(new QueryWrapper<MarketingCertificateRecord>()
                    .eq("marketing_certificate_id", marketingCertificateRecord.getMarketingCertificateId())
                    .eq("sys_user_id", sysUserId)
                    .eq("reward_day_one", "1")
                    .eq("member_list_id", marketingCertificateRecord.getMemberListId())
                    .apply("date_format(user_time,'%Y-%m-%d') = {0}", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
            ) > 0&&marketingCertificateRecord.getRewardDayOne().equals("1")) {
                return result.error500("该用户今日已在本店核销过此券，欢迎明日再次惠顾。");
            }
            if (marketingCertificateRecord.getIsBuyPlatform().equals("0")) {
                if (StringUtils.isNotBlank(marketingCertificateRecord.getSysUserId())){
                    if (sysUserId.equals(marketingCertificateRecord.getSysUserId())){
                        updateStatus(marketingCertificateRecord);
                    }else {
                        return result.error500("核销失败!您无权核销");
                    }
                }else {
                    return ifverification(marketingCertificateRecord, sysUserId);
                }
            } else {
                return ifverification(marketingCertificateRecord, sysUserId);
            }

            result.success("核销成功");
            return  result;
        }

    }

    private Result<String> ifverification(MarketingCertificateRecord marketingCertificateRecord ,
                                          String sysUserId){
        Result<String> result = new Result<>();
        //判断券的核销门店: 0: 全平台 1:指定门店
        if (marketingCertificateRecord.getRewardStore().equals("0")) {
            updateStatus(marketingCertificateRecord.setSysUserId(sysUserId));
        } else {
            //获取兑换券使用店铺
            List<MarketingCertificateStore> list = iMarketingCertificateStoreService.list(new LambdaQueryWrapper<MarketingCertificateStore>()
                    .eq(MarketingCertificateStore::getMarketingCertificateId, marketingCertificateRecord.getMarketingCertificateId())
            );
            //封装店铺userId集合
            List<String> stores = list.stream()
                    .map(MarketingCertificateStore::getSysUserId)
                    .collect(Collectors.toList());
            //判断适用店铺中有没有包含本店
            if (stores.contains(sysUserId)) {
                updateStatus(marketingCertificateRecord.setSysUserId(sysUserId));
            } else {
                return result.error500("核销失败!您无权核销");
            }
        }
        result.success("核销成功!!!");
        return result;
    }
    private void updateStatus(MarketingCertificateRecord marketingCertificateRecord) {
        marketingCertificateRecord.setStatus("2");
        marketingCertificateRecord.setVerificationPeople("1");
        marketingCertificateRecord.setVerification("1");
        marketingCertificateRecord.setUserTime(new Date());
        iMarketingCertificateRecordService.saveOrUpdate(marketingCertificateRecord);
        //判断如果核销奖励大于0时
        if (marketingCertificateRecord.getTheReward().doubleValue() > 0) {
            //查出核销店铺
            StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getSysUserId, marketingCertificateRecord.getSysUserId()));

            if (oConvertUtils.isNotEmpty(storeManage)) {
                //赠送核销奖励
                iStoreManageService.saveOrUpdate(storeManage
                        .setBalance(storeManage.getBalance().add(marketingCertificateRecord.getTheReward())));

                StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord()
                        .setDelFlag("0")
                        .setStoreManageId(storeManage.getId())
                        .setPayType("10")
                        .setGoAndCome("0")
                        .setAmount(marketingCertificateRecord.getTheReward())
                        .setTradeStatus("5")
                        .setOrderNo(OrderNoUtils.getOrderNo())
                        .setOperator("系统")
                        .setRemark("兑换券核销奖励" + marketingCertificateRecord.getMarketingCertificateId())
                        .setTradeNo(marketingCertificateRecord.getId());
                iStoreRechargeRecordService.save(storeRechargeRecord);

                iStoreAccountCapitalService.save(new StoreAccountCapital()
                        .setDelFlag("0")
                        .setStoreManageId(storeManage.getId())
                        .setPayType("10")
                        .setGoAndCome("0")
                        .setAmount(marketingCertificateRecord.getTheReward())
                        .setOrderNo(storeRechargeRecord.getOrderNo())
                        .setBalance(storeManage.getBalance())
                );
            }
        }
    }
}
