package org.jeecg.utils.alipay;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log
public class AlipayUtils {

    @Value(value = "${jeecg.path.cert}")
    private String cert;

    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 支付宝银行卡转账
     */
    public boolean transfer(String transAmount,String orderNo,String identity,String name){
        //支付宝网关（固定）。
        String url="https://openapi.alipay.com/gateway.do";
        String app_id= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","alipay_app_id");
        String app_privateKey= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","alipay_app_private_key");
        String systemName= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","system_name");

        try {
            String app_cert_path= cert+"/appCertPublicKey_2021002170667266.crt";
            String alipay_cert_path=cert+"/alipayCertPublicKey_RSA2.crt";
            String alipay_root_cert_path=cert+"/alipayRootCert.crt";
            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
            certAlipayRequest.setServerUrl(url);
            certAlipayRequest.setAppId(app_id);
            certAlipayRequest.setPrivateKey(app_privateKey);
            certAlipayRequest.setFormat("json");
            certAlipayRequest.setCharset("UTF-8");
            certAlipayRequest.setSignType("RSA2");
            certAlipayRequest.setCertPath(app_cert_path);
            certAlipayRequest.setAlipayPublicCertPath(alipay_cert_path);
            certAlipayRequest.setRootCertPath(alipay_root_cert_path);
            DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
            AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
            Map<String,Object> payMap= Maps.newHashMap();
            payMap.put("out_biz_no", orderNo);
            payMap.put("trans_amount",transAmount);
            payMap.put("product_code","TRANS_BANKCARD_NO_PWD");
            payMap.put("biz_scene","DIRECT_TRANSFER");
            payMap.put("order_title","提现");
            Map<String,Object> payeeInfo=Maps.newHashMap();
            payeeInfo.put("identity_type","BANKCARD_ACCOUNT");
            payeeInfo.put("identity", StringUtils.trim(identity));
            payeeInfo.put("name",StringUtils.trim(name));
            Map<String,Object> bankcardExtInfo= Maps.newHashMap();
            bankcardExtInfo.put("account_type","2");
            payeeInfo.put("bankcard_ext_info",bankcardExtInfo);
            payMap.put("payee_info",payeeInfo);
            payMap.put("remark", DateUtils.formatDate()+"("+systemName+")");
            Map<String,Object> businessParams=Maps.newHashMap();
            businessParams.put("payer_show_name","服务代理");
            businessParams.put("withdraw_timeliness","T0");
            payMap.put("business_params",businessParams);
            log.info(JSON.toJSONString(payMap));
            request.setBizContent(JSON.toJSONString(payMap));
            AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
            if(response.isSuccess()){
                return true;
            } else {
                return false;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }
}
