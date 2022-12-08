package org.jeecg.modules.pay.wapi;


import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pay.api.AfterOtherController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 另类支付方式接口
 */
@RequestMapping("wapi/other")
@Controller
@Log
public class WapiOtherController {


    @Autowired
    private AfterOtherController afterOtherController;



    /**
     * 进入收银台
     *
     * @param price
     * @param memberListId
     * @return
     */
    @RequestMapping("toCashierDesk")
    @ResponseBody
    public Result<?> toCashierDesk(BigDecimal price, String softModel, String memberListId){
       return afterOtherController.toCashierDesk(price,softModel,memberListId);
    }


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
    public Result<?> pay(String memberId,
                         String payModel, String memberListId,
                         @RequestParam(name = "welfarePayments",required = false,defaultValue = "0") BigDecimal welfarePayments,
                         @RequestParam(name = "balance",required = false,defaultValue = "0") BigDecimal balance,
                         @RequestParam(name = "price",required = false,defaultValue = "0") BigDecimal price,
                         HttpServletRequest request){
        return afterOtherController.pay(memberId,payModel,memberListId,welfarePayments,price,balance,request);
    }
}
