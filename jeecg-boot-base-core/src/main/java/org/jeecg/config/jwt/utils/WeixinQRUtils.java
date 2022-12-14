package org.jeecg.config.jwt.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.util.gongke.HttpClientUtil;
import org.jeecg.config.jwt.service.RedisTokenManager;
import org.jeecg.config.jwt.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WeixinQRUtils {

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private RedisTokenManager redisTokenManager;
    @Lazy
    @Resource
    private CommonAPI commonApi;
    @Autowired
    private HttpClientUtil httpClientUtil;

    //生成会员端店铺二维码
    public String getQrCode(String scene) {

        //获取常量信息
        String appid = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppID");
        if(appid.equals("-1")){
            return null;
        }
        String appSecret = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppSecret");
        String accessToken = tokenManager.createWeiXinToken(appid, appSecret);
        RestTemplate rest = new RestTemplate();

        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        Map<String, Object> param = new HashMap<>();
        param.put("scene", scene);
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String, Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);
        log.info("调用生成微信URL接口传参:" + param);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
        log.debug("调用小程序生成微信永久小程序码URL接口返回结果:" + new String(entity.getBody()));
        byte[] result = entity.getBody();

        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "smallcode";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "smallcode.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;

        try {
            FileUtils.writeByteArrayToFile(new File(savePath),result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        return dbpath;

    }

    //生成会员二维码
    public String getQrCodeByPage(String scene,String page) {

        //获取常量信息
        String appid = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppID");
        String appSecret = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppSecret");
        String accessToken = tokenManager.createWeiXinToken(appid, appSecret);
        RestTemplate rest = new RestTemplate();

        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        Map<String, Object> param = new HashMap<>();
        param.put("scene", scene);
        param.put("page", page);
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String, Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);
        log.info("调用生成微信URL接口传参:" + param);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
        log.debug("调用小程序生成微信永久小程序码URL接口返回结果:" + new String(entity.getBody()));
        byte[] result = entity.getBody();

        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "smallcode";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "smallcode.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;

        try {
            FileUtils.writeByteArrayToFile(new File(savePath),result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        return dbpath;

    }



    //生成加盟商二维码
    public String getCommercialQrCode(String scene) {

        //获取常量信息
        String appid = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppID_store");
        String appSecret = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppSecret_store");
        String accessToken = tokenManager.createWeiXinToken(appid, appSecret);
        RestTemplate rest = new RestTemplate();

        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        Map<String, Object> param = new HashMap<>();
        param.put("scene", scene);
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String, Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);
        log.info("调用生成微信URL接口传参:" + param);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
        log.debug("调用小程序生成微信永久小程序码URL接口返回结果:" + new String(entity.getBody()));
        byte[] result = entity.getBody();

        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "commercialsmallcode";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "smallcode.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;

        try {
            FileUtils.writeByteArrayToFile(new File(savePath),result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        return dbpath;

    }

    //生成店铺端带地址二维码
    public String getCommercialQrCodeByPage(String scene,String page) {

        //获取常量信息
        String appid = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppID_store");
        String appSecret = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppSecret_store");
        String accessToken = tokenManager.createWeiXinToken(appid, appSecret);
        RestTemplate rest = new RestTemplate();

        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        Map<String, Object> param = new HashMap<>();
        param.put("scene", scene);
        param.put("page", page);
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String, Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);
        log.info("调用生成微信URL接口传参:" + param);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
        log.debug("调用小程序生成微信永久小程序码URL接口返回结果:" + new String(entity.getBody()));
        byte[] result = entity.getBody();

        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "commercialsmallcode";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "smallcode.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;

        try {
            FileUtils.writeByteArrayToFile(new File(savePath),result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        return dbpath;

    }

    //获取小程序用户的openid,unionid
    public JSONObject getSessionByCode(String openid) {//code是从小程序调用wx.login拿到的code

        String appid = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppID");
        String appSecret = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppSecret");
        String  accessToken =  redisTokenManager.createWeiXinToken(appid, appSecret);
        Map<String,String> params = new HashMap<String,String>();
        params.put("access_token",accessToken);
        params.put("lang","zh_CN");
        params.put("openid",openid);
        String  result = httpClientUtil.httpRequestToString("https://api.weixin.qq.com/cgi-bin/user/info", params);
        JSONObject   jsonObject = JSONObject.parseObject(result);
        //String subscribe = jsonObject.get("subscribe").toString();
        return jsonObject;
    }
    //获取小程序用户的unionid
       public   Map<String,Object> oauth2GetOpenid(String code) {
         String GetPageAccessTokenUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
         String appid = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppID");
         String appSecret = commonApi.translateDictFromTable("sys_dict_item", "item_value", "item_text", "AppSecret");

         String requestUrl = GetPageAccessTokenUrl.replace("APPID", appid).replace("SECRET", appSecret).replace("CODE", code);
                 HttpClient client = null;
               Map<String,Object> result =new HashMap<String,Object>();
                try {
                         client = new DefaultHttpClient();
                         HttpGet httpget = new HttpGet(requestUrl);
                         ResponseHandler<String> responseHandler = new BasicResponseHandler();
                         String response = client.execute(httpget, responseHandler);
                         JSONObject OpenidJSONO=JSONObject.parseObject(response);
                         String openid =String.valueOf(OpenidJSONO.get("openid"));
                         String session_key=String.valueOf(OpenidJSONO.get("session_key"));
                         String unionid=String.valueOf(OpenidJSONO.get("unionid"));
                         String errcode=String.valueOf(OpenidJSONO.get("errcode"));
                         String errmsg=String.valueOf(OpenidJSONO.get("errmsg"));
                         result.put("openid", openid);
                         result.put("sessionKey", session_key);
                         result.put("unionid", unionid);
                         result.put("errcode", errcode);
                         result.put("errmsg", errmsg);
                     } catch (Exception e) {
                         e.printStackTrace();
                     } finally {
                         client.getConnectionManager().shutdown();
                     }
                return result;
             }


     //获取公众号用户信息
    public JSONObject getGZHOpenid(String next_openid){

        /*http请求方式: GET（请使用https协议）
        https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
   */
        String appid = "wxce76e2251b91625b";
        String appSecret = "c7d2fa38b5f7ddd86b69ff1f0c5e6f54";
        //https请求方式: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        Map<String,String> paramsToken = new HashMap<String,String>();
        paramsToken.put("grant_type","client_credential");
        paramsToken.put("appid",appid);
        paramsToken.put("secret",appSecret);
        String  resultToken = httpClientUtil.httpRequestToString("https://api.weixin.qq.com/cgi-bin/token", paramsToken);
        JSONObject   jsonObjectToken = JSONObject.parseObject(resultToken);
        String accessToken = jsonObjectToken.get("access_token").toString();
        //String  accessToken =  redisTokenManager.createWeiXinToken(appid, appSecret);
        //{"access_token":"32_kDU_vj0KMNb6Gj3DVSSyBdwJB-wizl6qItiKyntrdKv0lg3lKIQOQVUPF1lM1A6ArdeosR0CNCtVt4zEHUj9GdEpErFfTQZzRSdQ80p-qPFzn-Bl3CStStWXd8FTY0g-zz0pMZR5l64K4qHPVSVcAHAQNQ","expires_in":7200}
        Map<String,String> params = new HashMap<String,String>();
        params.put("access_token",accessToken);
        if(StringUtils.isNotBlank(next_openid)){
            params.put("next_openid",next_openid);
        }

        String  result = httpClientUtil.httpRequestToString("https://api.weixin.qq.com/cgi-bin/user/get", params);
        JSONObject   jsonObject = JSONObject.parseObject(result);
//{"total":8,"count":8,"data":{"openid":["okqMl1nVYADN-PKOezTR3GJM6EZM","okqMl1gR2cVHeKawxXF8-A2pfDyM","okqMl1paJKWlO9eIGFRJGmqqKCYw","okqMl1tT4_A-w0LQSUZeWx9nuZmk","okqMl1kmM0DNkOnA8XVPLIcTcNxE","okqMl1hgH-WS1s1eNQyJpgEf0pLg","okqMl1hJ9xmIRFYuzPcmROUiNpzg","okqMl1izsfa9bi7afBehyxsZiPGo"]},"next_openid":"okqMl1izsfa9bi7afBehyxsZiPGo"}
        JSONObject jsonObjectData =  jsonObject.getJSONObject("data");
        JSONArray jsonArray  = jsonObjectData.getJSONArray("openid");
       Object[] stringList= jsonArray.toArray();

       return jsonObject;
    }

}
