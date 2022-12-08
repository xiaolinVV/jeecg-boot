package org.jeecg.modules.order.utils;


import com.huifu.adapay.core.exception.BaseAdaPayException;
import com.huifu.adapay.model.Refund;
import lombok.extern.java.Log;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log
public class PayUtils {
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private WeixinPayUtils weixinPayUtils;
    @Autowired
    private WeixinHftxPayUtils weixinHftxPayUtils;

    @Autowired
    private ZhifubaoHftxPayUtils zhifubaoHftxPayUtils;


    /**
     * 退款
     *
     * @param price
     * @param serialNumber
     * @param hftxSerialNumber
     * @return
     */
    public Map<String, Object> refund(BigDecimal price,String serialNumber,String hftxSerialNumber){
        String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
        //汇付天下支付退款
        if(weixinMiniSoftPay.equals("1")&&price.doubleValue()>0){
            try {
                Map<String, Object> refundParams = new HashMap<>();
                refundParams.put("refund_amt", price.setScale(2, RoundingMode.DOWN).toString());
                refundParams.put("refund_order_no", serialNumber);
                return Refund.create(hftxSerialNumber, refundParams);
            } catch (BaseAdaPayException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * 微信和支付宝支付
     *
     * @param payModel
     * @param request
     * @param logId
     * @param allTotalPrice
     * @param notifyUrl
     * @param openId
     * @param divMembers
     * @return
     */
    public Map<String,String> pay(String payModel,HttpServletRequest request, String logId, BigDecimal allTotalPrice, String notifyUrl, String openId, List<Map<String, String>> divMembers){
        Map<String,String> resultMap=null;

        //判断支付方式
        //微信支付
        if(payModel.equals("0")){
            log.info("实际支付金额：allTotalPrice="+allTotalPrice);
            //微信支付
            resultMap=this.payWeixin(request,logId,allTotalPrice,notifyUrl,openId,divMembers);

        }

        //支付宝支付
        if(payModel.equals("1")){
            log.info("实际支付金额：allTotalPrice="+allTotalPrice);
            //支付宝支付
            resultMap=this.payZhifubao(logId,allTotalPrice,notifyUrl,divMembers);
        }
        return resultMap;
    }


    /**
     * 微信支付工具
     *
     * @param request
     * @param logId
     * @param allTotalPrice
     * @param notifyUrl
     * @param openId
     * @return
     */
    public Map<String,String> payWeixin(HttpServletRequest request, String logId, BigDecimal allTotalPrice, String notifyUrl, String openId, List<Map<String, String>> divMembers){
        String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");

        Map<String,String> resultMap=null;

        if(weixinMiniSoftPay.equals("0")) {
            //官方微信支付调起
            resultMap = weixinPayUtils.payWeixin(request, logId, allTotalPrice, openId, notifyUrl);
        }

        if(weixinMiniSoftPay.equals("1")) {
            //汇付天下微信支付
            resultMap = weixinHftxPayUtils.payWeixin(allTotalPrice,logId, notifyUrl, openId, divMembers);
        }
        return resultMap;
    }



    /**
     * 支付宝支付工具
     *
     * @param logId
     * @param allTotalPrice
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payZhifubao(String logId, BigDecimal allTotalPrice,String notifyUrl,List<Map<String, String>> divMembers){
        String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");

        Map<String,String> resultMap=null;

        if(weixinMiniSoftPay.equals("0")) {
            //官方支付宝支付调起
            log.info("官方支付宝支付，还未对接开发，待对接");
        }

        if(weixinMiniSoftPay.equals("1")) {
            //汇付天下微信支付
            resultMap = zhifubaoHftxPayUtils.payZhifubao(allTotalPrice,logId, notifyUrl,divMembers);
        }
        return resultMap;
    }
}
