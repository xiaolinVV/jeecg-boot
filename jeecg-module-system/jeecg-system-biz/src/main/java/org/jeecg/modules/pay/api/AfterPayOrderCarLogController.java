package org.jeecg.modules.pay.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.utils.TotalPayUtils;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.service.IPayOrderCarLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 通过日志id进行支付
 */
@Controller
@RequestMapping("after/payOrderCarLog")
@Log
public class AfterPayOrderCarLogController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private TotalPayUtils totalPayUtils;

    @Autowired
    private IPayOrderCarLogService iPayOrderCarLogService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;


    /**
     * 通过支付日志id支付接口
     *
     * @param memberId
     * @param payModel  支付方式；0：微信支付，1：支付宝支付
     * @param memberListId  会员id
     * @param welfarePayments 积分
     * @param balance 余额
     * @param request
     * @return
     */
    @RequestMapping("pay")
    @ResponseBody
    @Transactional
    public Result<?> pay(@RequestAttribute(value = "memberId",required = false) String memberId,
                         String payModel, String memberListId,
                         @RequestParam(name = "welfarePayments",required = false,defaultValue = "0") BigDecimal welfarePayments,
                         @RequestParam(name = "balance",required = false,defaultValue = "0") BigDecimal balance,
                         @RequestHeader(defaultValue = "") String tMemberId,
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
        if(memberList.getWelfarePayments().subtract(welfarePayments).doubleValue()<0){
            return result.error500("会员积分过大");
        }
        if(memberList.getBalance().subtract(balance).doubleValue()<0){
            return result.error500("会员余额过大");
        }



        PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getOne(new LambdaQueryWrapper<PayOrderCarLog>()
                .eq(PayOrderCarLog::getMemberListId,memberListId).eq(PayOrderCarLog::getPayStatus,"0")
                .orderByDesc(PayOrderCarLog::getCreateTime)
                .last("limit 1"));
        if(payOrderCarLog==null){
            return result.error500("支付日志id不存在");
        }
        log.info("收银台，支付的时候日志id："+payOrderCarLog.getId()+"；余额："+balance+"；积分:"+welfarePayments+"；会员手机："+memberList.getPhone()+"；总金额："+payOrderCarLog.getAllTotalPrice());
        if(payOrderCarLog.getPayStatus().equals("1")){
            return result.error500("支付日志已支付");
        }
        //支付
        Map<String,Object> objectMap=totalPayUtils.payOrderCarLogById(request,payModel,welfarePayments,balance,payOrderCarLog.getId(),memberId,tMemberId);

        result.setResult(objectMap);
        return result;
    }


    /**
     * 计算收银台使用的金额
     *
     * @param memberListId
     * @param welfarePayments
     * @param balance
     * @return
     */
    @RequestMapping("calculatePayPrice")
    @ResponseBody
    public Result<?> calculatePayPrice(String memberListId,
                                       @RequestParam(name = "welfarePayments",required = false,defaultValue = "0") BigDecimal welfarePayments,
                                       @RequestParam(name = "balance",required = false,defaultValue = "0") BigDecimal balance){
        Map<String,Object> resultMap=Maps.newHashMap();
        //判断会员资金信息
        MemberList memberList=iMemberListService.getById(memberListId);
        if(memberList.getWelfarePayments().subtract(welfarePayments).doubleValue()<0){
            return Result.error("会员积分过大");
        }
        if(memberList.getBalance().subtract(balance).doubleValue()<0){
            return Result.error("会员余额过大");
        }

        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getOne(new LambdaQueryWrapper<PayOrderCarLog>()
                .eq(PayOrderCarLog::getMemberListId,memberListId).eq(PayOrderCarLog::getPayStatus,"0")
                .orderByDesc(PayOrderCarLog::getCreateTime)
                .last("limit 1"));
        log.info("收银台支付金额查询日志id："+payOrderCarLog.getId()+"；余额："+balance+"；积分:"+welfarePayments+"；会员手机："+memberList.getPhone()+"；总金额："+payOrderCarLog.getAllTotalPrice());
        if(payOrderCarLog==null){
            return Result.error("支付日志id不存在");
        }
        if(payOrderCarLog.getPayStatus().equals("1")){
            return Result.error("支付日志已支付");
        }
        resultMap.put("welfarePaymentsPrice",welfarePayments.multiply(integralValue));
        resultMap.put("balance",balance);
        //价格
        BigDecimal allTotalPrice=payOrderCarLog.getAllTotalPrice();
        allTotalPrice.subtract(balance).subtract(welfarePayments.multiply(integralValue)).setScale(2, RoundingMode.HALF_UP);
        resultMap.put("allTotalPrice",allTotalPrice);
        return Result.ok(resultMap);
    }
}
