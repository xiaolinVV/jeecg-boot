package org.jeecg.modules.pay.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.entity.PayMarketingStoreGiftbagLog;
import org.jeecg.modules.pay.service.IPayMarketingStoreGiftbagLogService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.pay.utils.PayLogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("after/payMarketingStoreGiftbagLog")
@Log
public class AfterPayMarketingStoreGiftbagLogController {


    @Autowired
    private IMarketingStoreGiftbagListService iMarketingStoreGiftbagListService;

    @Autowired
    private IPayMarketingStoreGiftbagLogService iPayMarketingStoreGiftbagLogService;

    @Autowired
    private PayLogUtils payLogUtils;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    @Autowired
    private PayUtils payUtils;


    /**
     * 进入收银台
     *
     * @param marketingStoreGiftbagListId
     * @param memberId
     * @return
     */
    @RequestMapping("toCashierDeskMarketingStoreGiftbag")
    @ResponseBody
    public Result<?> toCashierDesk(String marketingStoreGiftbagListId,
                                   String tMemberId,
                                   String marketingStoreGiftbagTeamId,
                                   @RequestHeader("softModel") String softModel,
                                   @RequestAttribute(value = "memberId",required = false) String memberId){
        Map<String,Object> resultMap= Maps.newHashMap();
//参数校验
        if(StringUtils.isBlank(memberId)){
            return Result.error("会员id不能为空");
        }
        if(StringUtils.isBlank(marketingStoreGiftbagListId)){
            return Result.error("礼包团id不能为空");
        }
        MarketingStoreGiftbagList marketingStoreGiftbagList=iMarketingStoreGiftbagListService.getById(marketingStoreGiftbagListId);
//支付日志信息
        PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog=new PayMarketingStoreGiftbagLog();
        payMarketingStoreGiftbagLog.setPayModel("2");
        payMarketingStoreGiftbagLog.setMemberListId(memberId);
        payMarketingStoreGiftbagLog.setTMemberId(tMemberId);
        payMarketingStoreGiftbagLog.setTotalFee(marketingStoreGiftbagList.getPrice());
        payMarketingStoreGiftbagLog.setMarketingStoreGiftbagListId(marketingStoreGiftbagListId);
        payMarketingStoreGiftbagLog.setMarketingStoreGiftbagTeamId(marketingStoreGiftbagTeamId);
        iPayMarketingStoreGiftbagLogService.save(payMarketingStoreGiftbagLog);
//进入收银台
        resultMap.putAll(payLogUtils.toCashierDesk(payMarketingStoreGiftbagLog.getTotalFee(),softModel,payMarketingStoreGiftbagLog.getId(),"gift_bag_group_pay",memberId));
        return Result.ok(resultMap);
    }



    /**
     * 收银台付日志id支付接口
     *
     * @param memberId
     * @param payModel 支付方式；0：微信支付，1：支付宝支付
     * @param memberListId 会员id
     * @param request
     * @return
     */
    @RequestMapping("payMarketingStoreGiftbag")
    @ResponseBody
    @Transactional
    public Result<?> pay(@RequestAttribute(value = "memberId",required = false) String memberId,
                         String payModel, String memberListId,
                         BigDecimal price,
                         @RequestParam(name = "welfarePayments",required = false,defaultValue = "0") BigDecimal welfarePayments,
                         @RequestParam(name = "balance",required = false,defaultValue = "0") BigDecimal balance,
                         HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
//参数校验
        if(StringUtils.isBlank(memberListId)){
            return result.error500("会员id不能为空");
        }
        if(StringUtils.isBlank(payModel)){
            return result.error500("支付方式不能为空");
        }
//判断会员资金信息
        MemberList memberList=iMemberListService.getById(memberListId);
        PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog=iPayMarketingStoreGiftbagLogService.getOne(new LambdaQueryWrapper<PayMarketingStoreGiftbagLog>()
                .eq(PayMarketingStoreGiftbagLog::getMemberListId,memberListId)
                .eq(PayMarketingStoreGiftbagLog::getPayStatus,"0")
                .orderByDesc(PayMarketingStoreGiftbagLog::getCreateTime)
                .last("limit 1"));
        if(payMarketingStoreGiftbagLog==null){
            return result.error500("支付日志id不存在");
        }
        log.info("收银台，支付的时候日志id："+payMarketingStoreGiftbagLog.getId()+"；余额："+balance+"；积分:"+welfarePayments+"；会员手机："+memberList.getPhone()+"；总金额："+payMarketingStoreGiftbagLog.getTotalFee());
        if(payMarketingStoreGiftbagLog.getPayStatus().equals("1")){
            return result.error500("支付日志已支付");
        }
//支付
        Map<String,Object> objectMap=this.payById(request,payModel,welfarePayments,balance,payMarketingStoreGiftbagLog.getId(),memberId);

        result.setResult(objectMap);
        return result;
    }

    /**
     * 订单日志支付
     *
     * @param request
     * @param payModel
     * @param welfarePayments
     * @param balance
     * @param payMarketingStoreGiftbagLogId
     * @param memberId
     * @return
     */
    public Map<String,Object> payById(HttpServletRequest request,String payModel, BigDecimal welfarePayments,BigDecimal balance,String payMarketingStoreGiftbagLogId,String memberId){
        Map<String, Object> objectMap=Maps.newHashMap();

//重组支付日志信息
        PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog=iPayMarketingStoreGiftbagLogService.getById(payMarketingStoreGiftbagLogId);
        payMarketingStoreGiftbagLog.setId(null);
        payMarketingStoreGiftbagLog.setBalance(balance);
        payMarketingStoreGiftbagLog.setWelfarePayments(welfarePayments);
        payMarketingStoreGiftbagLog.setPayModel("2");
        iPayMarketingStoreGiftbagLogService.save(payMarketingStoreGiftbagLog);
//价格
        BigDecimal allTotalPrice=payMarketingStoreGiftbagLog.getTotalFee();

        log.info("支付的余额："+balance);
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        if(welfarePayments.doubleValue()!=0){
            welfarePayments=welfarePayments.multiply(integralValue);
        }
        log.info("支付的积分："+welfarePayments);
        if(allTotalPrice.subtract(balance).subtract(welfarePayments).doubleValue()<=0){
            allTotalPrice=new BigDecimal(0);
        }else{
            allTotalPrice=allTotalPrice.subtract(balance).subtract(welfarePayments);
        }

//设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("gift_bag_group_notify_url");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {
            Map<String,String> resultMap=payUtils.pay(payModel,request,payMarketingStoreGiftbagLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),null);

            jsonStr=resultMap.get("jsonStr");

            if(resultMap.get("id")!=null) {
                payMarketingStoreGiftbagLog.setSerialNumber(resultMap.get("id"));
            }

            objectMap.put("queryUrl",resultMap.get("queryUrl"));
//支付日志
            payMarketingStoreGiftbagLog.setPayParam(resultMap.get("params"));

            payMarketingStoreGiftbagLog.setPayModel(payModel);
            payMarketingStoreGiftbagLog.setPayPrice(allTotalPrice);
//保存支付日志
            iPayMarketingStoreGiftbagLogService.saveOrUpdate(payMarketingStoreGiftbagLog);
        }

        objectMap.put("payLog",payMarketingStoreGiftbagLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payMarketingStoreGiftbagLog.getId());
        return objectMap;
    }
}
