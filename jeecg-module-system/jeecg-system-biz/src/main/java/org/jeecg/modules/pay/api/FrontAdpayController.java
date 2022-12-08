package org.jeecg.modules.pay.api;


import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 支付开放平台api
 */

@Controller
@RequestMapping("front/adpay")
@Log
public class FrontAdpayController {


    /**
     * 支付开放平台回调地址
     *
     * @return
     */
    @RequestMapping("authRedirect")
    public Result<?> authRedirect(){
        log.info("支付宝授权回调地址");
        return Result.ok();
    }
}
