package org.jeecg.modules.pay.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.utils.TotalPayUtils;
import org.jeecg.modules.pay.entity.PayCertificateLog;
import org.jeecg.modules.pay.service.IPayCertificateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;


/**
 *
 * 兑换券收银台支付
 *
 */
@RequestMapping("after/payCertificate")
@Controller
@Log
public class AfterPayCertificateController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IPayCertificateLogService iPayCertificateLogService;

    @Autowired
    private TotalPayUtils totalPayUtils;


    /**
     * 兑换券支付
     *
     * @param memberId(支付归属会员)
     * @param payModel  支付方式；0：微信支付，1：支付宝支付  必传
     * @param memberListId  会员id(订单归属会员，必传)
     * @param welfarePayments 积分（福利金，非必传）
     * @param balance 余额（非必传）
     * @param request
     * @return
     */
    @RequestMapping("pay")
    @ResponseBody
    public Result<?> pay(@RequestAttribute(value = "memberId",required = false) String memberId,
                         String payModel,
                         String memberListId,
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
        if(memberList.getWelfarePayments().subtract(welfarePayments).doubleValue()<0){
            return result.error500("会员积分过大");
        }
        if(memberList.getBalance().subtract(balance).doubleValue()<0){
            return result.error500("会员余额过大");
        }

        PayCertificateLog payCertificateLog=iPayCertificateLogService.getOne(new LambdaQueryWrapper<PayCertificateLog>()
                .eq(PayCertificateLog::getMemberListId,memberListId)
                .eq(PayCertificateLog::getPayStatus,"0")
                .orderByDesc(PayCertificateLog::getCreateTime)
                .last("limit 1"));
        log.info("收银台，支付的时候日志id："+payCertificateLog.getId()+"；余额："+balance+"；积分:"+welfarePayments+"；会员手机："+memberList.getPhone()+"；总金额："+payCertificateLog.getTotalFee());
        if(payCertificateLog==null){
            return result.error500("支付日志id不存在");
        }
        if(payCertificateLog.getPayStatus().equals("1")){
            return result.error500("支付日志已支付");
        }

        result.setResult(totalPayUtils.payCertificateLogById(request,payModel,welfarePayments,balance,payCertificateLog.getId(),memberId));
        return result;
    }
}
