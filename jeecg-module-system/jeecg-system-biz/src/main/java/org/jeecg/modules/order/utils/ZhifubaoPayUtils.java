package org.jeecg.modules.order.utils;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
@Log
public class ZhifubaoPayUtils {


    @Autowired
    private ISysDictService iSysDictService;


    public Map<String,String> payZhifubao(BigDecimal money, String orderNo, String notifyUrl){

        Map<String,String> stringMap= Maps.newHashMap();
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "alipay_app_id");
        String alipayAppPrivateKey=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "alipay_app_private_key");
        String alipayPayPublicKey=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "alipay_pay_public_key");

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appid,alipayAppPrivateKey,"json","UTF-8",alipayPayPublicKey,"RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(notifyUrl);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNo);
        bizContent.put("total_amount", money.setScale(2, RoundingMode.DOWN).toString());
        bizContent.put("subject", "商品购买");
        bizContent.put("product_code", "QUICK_MSECURITY_PAY");
        //bizContent.put("time_expire", "2022-08-01 22:00:00");

        //// 商品明细信息，按需传入
        //JSONArray goodsDetail = new JSONArray();
        //JSONObject goods1 = new JSONObject();
        //goods1.put("goods_id", "goodsNo1");
        //goods1.put("goods_name", "子商品1");
        //goods1.put("quantity", 1);
        //goods1.put("price", 0.01);
        //goodsDetail.add(goods1);
        //bizContent.put("goods_detail", goodsDetail);

        //// 扩展信息，按需传入
        //JSONObject extendParams = new JSONObject();
        //extendParams.put("sys_service_provider_id", "2088511833207846");
        //bizContent.put("extend_params", extendParams);

        request.setBizContent(bizContent.toString());
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            log.info("返回内容:"+response.getBody());
            if(response.isSuccess()){
                stringMap.put("jsonStr",response.getBody());
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return stringMap;
    }
}
