package org.jeecg.modules.order.utils;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.QrCodeKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 微信支付工具类
 */


@Component
@Log
public class WeixinPayUtils {


    @Autowired
    private ISysDictService iSysDictService;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;


    /**
     * 微信官方支付
     * @param request
     * @param serialNumber
     * @param money
     * @param openId
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payWeixin(HttpServletRequest request,String serialNumber,BigDecimal money,String openId,String notifyUrl){

        Map<String,String> stringMap= Maps.newHashMap();

        String jsonStr="0";

        //获取常量信息
        String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID");

        String mchId = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "mchId");

        String partnerKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "partnerKey");


        WxPayApiConfig wxPayApiConfig = WxPayApiConfig.builder().
                appId(appid).
                mchId(mchId).
                partnerKey(partnerKey).
                build();


        String ip = IpKit.getRealIp(request);
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(serialNumber)
                .body("微信小程序支付")
                .attach("厦门靠勤网络科技有限公司厉害")
                .out_trade_no(serialNumber)
                .total_fee(money.multiply(new BigDecimal(100)).toBigInteger().toString())
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        stringMap.put("params",params.toString());

        log.info(xmlResult);
        Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);

        String returnCode = payResult.get("return_code");
        String returnMsg = payResult.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            log.info("支付失败："+returnMsg);
            return null;
        }
        String resultCode = payResult.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            log.info("支付失败："+returnMsg);
            return null;
        }
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = payResult.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayApiConfig.getAppId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        jsonStr = JSON.toJSONString(packageParams);

        log.info("微信支付结果返回:"+jsonStr);

        stringMap.put("jsonStr",jsonStr);

        return stringMap;
    }



    /**
     * 微信官方支付（二维码支付）
     * @param request
     * @param serialNumber
     * @param money
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payWeixinQR(HttpServletRequest request,String serialNumber,BigDecimal money,String notifyUrl){

        Map<String,String> stringMap= Maps.newHashMap();

        String jsonStr="0";

        //获取常量信息
        String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID");

        String mchId = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "mchId");

        String partnerKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "partnerKey");


        WxPayApiConfig wxPayApiConfig = WxPayApiConfig.builder().
                appId(appid).
                mchId(mchId).
                partnerKey(partnerKey).
                build();


        String ip = IpKit.getRealIp(request);
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }


        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(serialNumber)
                .body("微信小程序支付")
                .attach("厦门靠勤网络科技有限公司厉害")
                .out_trade_no(serialNumber)
                .total_fee(money.multiply(new BigDecimal(100)).toBigInteger().toString())
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        stringMap.put("params",params.toString());

        log.info(xmlResult);
        Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);

        String returnCode = payResult.get("return_code");
        String returnMsg = payResult.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            log.info("支付失败："+returnMsg);
            return null;
        }
        String resultCode = payResult.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            log.info("支付失败："+returnMsg);
            return null;
        }
        //生成预付订单success

        String qrCodeUrl = payResult.get("code_url");

        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "files";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "payQRCode2.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;

        Boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                savePath);
        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;

        stringMap.put("dbpath",dbpath);

        return stringMap;
    }


    /**
     * 微信官方支付（商家小程序）
     * @param request
     * @param serialNumber
     * @param money
     * @param openId
     * @param notifyUrl
     * @return
     */
    public Map<String,String> payWeixinStore(HttpServletRequest request,String serialNumber,BigDecimal money,String openId,String notifyUrl){

        Map<String,String> stringMap= Maps.newHashMap();

        String jsonStr="0";

        //获取常量信息
        String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID_store");

        String mchId = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "mchId_store");

        String partnerKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "partnerKey_store");


        WxPayApiConfig wxPayApiConfig = WxPayApiConfig.builder().
                appId(appid).
                mchId(mchId).
                partnerKey(partnerKey).
                build();


        String ip = IpKit.getRealIp(request);
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(serialNumber)
                .body("微信小程序支付")
                .attach("厦门靠勤网络科技有限公司厉害")
                .out_trade_no(serialNumber)
                .total_fee(money.multiply(new BigDecimal(100)).toBigInteger().toString())
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        stringMap.put("params",params.toString());

        log.info(xmlResult);
        Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);

        String returnCode = payResult.get("return_code");
        String returnMsg = payResult.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            log.info("支付失败："+returnMsg);
            return null;
        }
        String resultCode = payResult.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            log.info("支付失败："+returnMsg);
            return null;
        }
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = payResult.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayApiConfig.getAppId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        jsonStr = JSON.toJSONString(packageParams);

        log.info("微信支付结果返回:"+jsonStr);

        stringMap.put("jsonStr",jsonStr);

        return stringMap;
    }
}
