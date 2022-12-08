package org.jeecg.modules.order.utils;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.huifu.adapay.Adapay;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import com.huifu.adapay.model.Payment;
import lombok.extern.java.Log;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * 汇付天下的支付宝支付
 */

@Component
@Log
public class ZhifubaoHftxPayUtils {


    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private WeixinHftxPayUtils weixinHftxPayUtils;

    /**
     * 汇付天下支付宝支付
     *
     * @param money
     * @param orderNo
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payZhifubao(BigDecimal money, String orderNo, String notifyUrl, List<Map<String, String>> divMembers){


        String apiKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_api_key");
        if(!Adapay.getConfig(Adapay.defaultMerchantKey).getApiKey().equals(apiKey)){
            weixinHftxPayUtils.init();
        }

        Map<String,String> stringMap=Maps.newHashMap();

        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");

        Map<String, Object> paymentParams = Maps.newHashMap();
        paymentParams.put("app_id", appid);
        paymentParams.put("order_no", orderNo);
        paymentParams.put("pay_channel", "alipay");
        paymentParams.put("pay_amt", money.setScale(2, RoundingMode.DOWN).toString());
        paymentParams.put("currency", "cny");
        paymentParams.put("goods_title", "商品");
        paymentParams.put("goods_desc", "用于支付流程的商品");
        paymentParams.put("notify_url",notifyUrl);


        if(divMembers!=null&&divMembers.size()!=0){
            paymentParams.put("div_members", JSON.toJSONString(divMembers));
        }

        log.info("汇付天下支付参数："+paymentParams.toString());

        // 调用创建方法，获取 Payment对象_
        Map<String, Object> payment  = null;
        try {
            payment = Payment.create(paymentParams);
        } catch (BaseAdaPayException e) {
            e.printStackTrace();
        }

        log.info("汇付天下:payment result="+ JSON.toJSONString(payment));

        String jsonStr="alipays://platformapi/startapp?saId=10000007&qrcode="+JSON.parseObject(JSON.toJSONString(payment)).getJSONObject("expend").getString("pay_info");

        stringMap.put("queryUrl",payment.get("query_url").toString());

        log.info("汇付天下支付宝使用的参数："+jsonStr);
        stringMap.put("jsonStr",jsonStr);
        stringMap.put("id",payment.get("id").toString());
        return stringMap;
    }
}
