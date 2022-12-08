package org.jeecg.common.util.weixin;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayConstants;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 微信工具类
 */
@Log
public class WeixinUtils {

    //获取微信Openid的请求地址
    public static String WxGetOpenIdUrl = "https://api.weixin.qq.com/sns/jscode2session";

    //获取用户个人信息
    public static String WxGetUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";

    //通过 code 换取网页授权access_token
    public static String WxGzAccessToken="https://api.weixin.qq.com/sns/oauth2/access_token";


    /**
     *
     * 通过 code 换取网页授权access_token
     *
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public static Map<String,Object> getAccessToken(String appid, String secret, String code){
        RestTemplate restTemplate=new RestTemplate();
        String url = WxGzAccessToken+"?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", appid);
        requestMap.put("secret", secret);
        requestMap.put("code", code);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
        log.info("微信公众号获取openid："+responseEntity.getBody());
        return JSON.parseObject(responseEntity.getBody());
    }



    /**
     * 获取openid方法
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public static Map<String,Object> getOpenId(String appid, String secret, String code){
        RestTemplate restTemplate=new RestTemplate();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("appid", appid);
        map.add("secret", secret);
        map.add("js_code", code);
        map.add("grant_type", "authorization_code");
        String result=restTemplate.postForObject(WxGetOpenIdUrl, map, String.class);
        log.info("微信获取openid接口："+result);
        return JSON.parseObject(result);
    }

    /**
     * 获取用户个人信息通过(openId)
     * @param accessToken
     * @param openid
     * @return
     */
    public static Map<String,Object> getUserInfo(String accessToken, String openid){
        RestTemplate restTemplate=new RestTemplate();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("access_token", accessToken);
        map.add("openid", openid);
        String result=restTemplate.postForObject(WxGetUserInfoUrl, map, String.class);
        log.info("获取微信用户信息的接口："+result);
        return JSON.parseObject(result);
    }


    public static String getSign(Map<String,String> map,String key)throws Exception{
        Set<String> keySet = map.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstants.FIELD_SIGN)){
                continue;
            }
            if (map.get(k).trim().length()>0){
                sb.append(k).append("=").append(map.get(k).trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        byte[] array = new byte[0];
        try {
            array = md.digest(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb2 = new StringBuilder();
        for (byte item : array) {
            sb2.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb2.toString().toUpperCase();
    }
    public static void main(String[] args){
//        Map<String,Object> map=getOpenId("wx3726e5aa7e0fe80b","49aafafbf07436bf7669d9d7831651f2","0018y4ti1HVDHu0Fawri1ScLsi18y4ti");
//        System.out.println(map.get("session_key"));
//        System.out.println(map.get("openid"));
//        try {
//            System.out.println(AesCbcUtil.decrypt("RYC6XWdPgV9ulHZy2C0kPIpXJb4OkAN2QMx8i5TRDFzriSofBKTc +8crxKK8r5Nggg2UQlhKbNGeR1FIQ5MWY5k3cqCt8sObEi0JTtsNXuC4gDHjajV5FNSsyPiD8tXz4cQYgil50+lCqt1yGblwRjlxrux2mGvfJ18C3Z2jjDKYtBp2wrv0qRdc+9u6mppFiX9weGfCW+bUaxWi07wvOztwfZP +SqrVXfrzap0LYiHX8phbRFSfNOYMP1fG+Wjvltk7P6JvgL6ycb5/KYydefeer7fJMV +mBKmTxlYixutYOSMZ7+E/rxjiDnXWK62UqC0z13ka3yYLTVAG4IFu3C1mt4mKc08XgmwfcTQO79VhgGgTomZW70htexo77D9/v5i3h6dlQCHadTK +05ruAvXug5W1geAMYWqKgt8tL2y8OaXL9t4RVnpbZS6LCAnFewcN/T1vhFizWObzjg4BI0BR89vncxwjT+e6xAOD1UQ=", "QfKvhJNRuKPuGk5XUMF0dA====", "Rr8Up11Z1LYXx/aHOQp+oA==", "UTF-8"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> massage = getUserInfo("38_Luo9YLyQ1i6uqRyxDBQirTtR-lS1LB67gs8vpmuIVKjLr6u_bgTAnxGJDkLczMtDyo3Xx7zrLsfoGO1oR_73baatfbviXxxP6UxNSrpawE0", "olLpb6QypSckK-mGDFq8ClES1g3M");
//        System.out.println(massage);

        Map<String, Object> map = getOpenId("wx3726e5aa7e0fe80b", "49aafafbf07436bf7669d9d7831651f2", "061QPXFa1fAPMz0aApGa1eZPxS0QPXFx");
        System.out.println(map);
    }


}
