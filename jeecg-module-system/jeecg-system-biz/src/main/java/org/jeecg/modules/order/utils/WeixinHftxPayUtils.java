package org.jeecg.modules.order.utils;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.huifu.adapay.Adapay;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import com.huifu.adapay.model.MerConfig;
import com.huifu.adapay.model.Payment;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;


/**
 * 汇付天下的微信支付
 */

@Component
@Log
public class WeixinHftxPayUtils {

    @Autowired
    private ISysDictService iSysDictService;


    @PostConstruct
    public void init(){

        String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");

        if(StringUtils.isNotBlank(weixinMiniSoftPay)&&weixinMiniSoftPay.equals("1")) {

            /**
             * debug 模式，开启后有详细的日志
             */
            Adapay.debug = false;

            /**
             * prodMode 模式，默认为生产模式，false可以使用mock模式
             */
            Adapay.prodMode = true;

            /**
             * 如果为单服务器，为防止服务器离线后收不到异步消息，请为服务器固定编号。集群服务器的话可以忽略。
             */
            Adapay.deviceID = "test001";

            /**
             * 单商户,配置异步结果监听
             * 请参照NotifyCallbackDemo 实现自己的监听，其中已经加入异步线程池，请自行兼容并发情况，服务端可能会重试推送，商户需要做好幂等去重处理
             */


            String apiKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_api_key");

            String rsaPrivateKey =iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_rsa_private_key");

            MerConfig merConfig = new MerConfig();
            merConfig.setApiKey(apiKey);
            merConfig.setRSAPrivateKey(rsaPrivateKey);
            log.info("汇付天下初始化启动配置!!!!");

            try {
                Adapay.initWithMerConfig(merConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * 汇付天下小程序商家端微信支付
     *
     * @param money
     * @param orderNo
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payWeixinStore(BigDecimal money,String orderNo,String notifyUrl,String openId, List<Map<String, String>> divMembers){
        String apiKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_api_key");
        if(!Adapay.getConfig(Adapay.defaultMerchantKey).getApiKey().equals(apiKey)){
            init();
        }
        Map<String,String> stringMap=Maps.newHashMap();

        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id_store");

        Map<String, Object> paymentParams = Maps.newHashMap();
        paymentParams.put("app_id", appid);
        paymentParams.put("order_no", orderNo);
        paymentParams.put("pay_channel", "wx_lite");
        paymentParams.put("pay_amt", money.setScale(2, RoundingMode.DOWN).toString());
        paymentParams.put("currency", "cny");
        paymentParams.put("goods_title", "商品");
        paymentParams.put("goods_desc", "用于支付流程的商品");
        paymentParams.put("notify_url",notifyUrl);

        if(divMembers!=null&&divMembers.size()!=0){
            paymentParams.put("div_members", JSON.toJSONString(divMembers));
        }

        Map<String, Object> expendParams = Maps.newHashMap();
        expendParams.put("open_id", openId);
        expendParams.put("is_raw", "1");
        paymentParams.put("expend", expendParams);


        log.info("汇付天下支付参数："+paymentParams.toString());

        // 调用创建方法，获取 Payment对象_
        Map<String, Object> payment  = null;
        try {
            payment = Payment.create(paymentParams);
        } catch (BaseAdaPayException e) {
            e.printStackTrace();
        }

        log.info("汇付天下:payment result="+ JSON.toJSONString(payment));

        String jsonStr=JSON.toJSONString(JSON.parseObject(payment.get("expend").toString()).getJSONObject("pay_info"));


        stringMap.put("queryUrl",payment.get("query_url").toString());
        log.info("汇付天下微信使用的参数："+jsonStr);
        stringMap.put("jsonStr",jsonStr);
        stringMap.put("id",payment.get("id").toString());
        return stringMap;
    }



    /**
     * 汇付天下小程序微信支付
     *
     * @param money
     * @param orderNo
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payWeixin(BigDecimal money,String orderNo,String notifyUrl,String openId, List<Map<String, String>> divMembers){
        String apiKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_api_key");
        if(!Adapay.getConfig(Adapay.defaultMerchantKey).getApiKey().equals(apiKey)){
            init();
        }
        Map<String,String> stringMap=Maps.newHashMap();

        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");

        Map<String, Object> paymentParams = Maps.newHashMap();
        paymentParams.put("app_id", appid);
        paymentParams.put("order_no", orderNo);
        paymentParams.put("pay_channel", "wx_lite");
        paymentParams.put("pay_amt", money.setScale(2, RoundingMode.DOWN).toString());
        paymentParams.put("currency", "cny");
        paymentParams.put("goods_title", "商品");
        paymentParams.put("goods_desc", "用于支付流程的商品");
        paymentParams.put("notify_url",notifyUrl);

        if(divMembers!=null&&divMembers.size()!=0){
            paymentParams.put("div_members", JSON.toJSONString(divMembers));
        }

        Map<String, Object> expendParams = Maps.newHashMap();
        expendParams.put("open_id", openId);
        expendParams.put("is_raw", "1");
        paymentParams.put("expend", expendParams);


        log.info("汇付天下支付参数："+paymentParams.toString());

        // 调用创建方法，获取 Payment对象_
        Map<String, Object> payment  = null;
        try {
            payment = Payment.create(paymentParams);
        } catch (BaseAdaPayException e) {
            e.printStackTrace();
        }

        log.info("汇付天下:payment result="+ JSON.toJSONString(payment));

        String jsonStr=JSON.toJSONString(JSON.parseObject(payment.get("expend").toString()).getJSONObject("pay_info"));


        stringMap.put("queryUrl",payment.get("query_url").toString());
        log.info("汇付天下微信使用的参数："+jsonStr);
        stringMap.put("jsonStr",jsonStr);
        stringMap.put("id",payment.get("id").toString());
        return stringMap;
    }



    /**
     * 汇付天下公众号微信支付
     *
     * @param money
     * @param orderNo
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payGzWeixin(BigDecimal money,String orderNo,String notifyUrl,String openId, List<Map<String, String>> divMembers){
        String apiKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_api_key");
        if(!Adapay.getConfig(Adapay.defaultMerchantKey).getApiKey().equals(apiKey)){
            init();
        }
        Map<String,String> stringMap=Maps.newHashMap();

        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");

        Map<String, Object> paymentParams = Maps.newHashMap();
        paymentParams.put("app_id", appid);
        paymentParams.put("order_no", orderNo);
        paymentParams.put("pay_channel", "wx_pub");
        paymentParams.put("pay_amt", money.setScale(2, RoundingMode.DOWN).toString());
        paymentParams.put("currency", "cny");
        paymentParams.put("goods_title", "商品");
        paymentParams.put("goods_desc", "用于支付流程的商品");
        paymentParams.put("notify_url",notifyUrl);

        if(divMembers!=null&&divMembers.size()!=0){
            paymentParams.put("div_members", JSON.toJSONString(divMembers));
        }

        Map<String, Object> expendParams = Maps.newHashMap();
        expendParams.put("open_id", openId);
        expendParams.put("is_raw", "1");
        paymentParams.put("expend", expendParams);


        log.info("汇付天下支付参数："+paymentParams.toString());

        // 调用创建方法，获取 Payment对象_
        Map<String, Object> payment  = null;
        try {
            payment = Payment.create(paymentParams);
        } catch (BaseAdaPayException e) {
            e.printStackTrace();
        }

        log.info("汇付天下:payment result="+ JSON.toJSONString(payment));

        String jsonStr=JSON.toJSONString(JSON.parseObject(payment.get("expend").toString()).getJSONObject("pay_info"));


        stringMap.put("queryUrl",payment.get("query_url").toString());
        log.info("汇付天下微信使用的参数："+jsonStr);
        stringMap.put("jsonStr",jsonStr);
        stringMap.put("id",payment.get("id").toString());
        return stringMap;
    }

}
