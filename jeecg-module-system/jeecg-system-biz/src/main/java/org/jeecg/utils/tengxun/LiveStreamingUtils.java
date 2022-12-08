package org.jeecg.utils.tengxun;

import com.google.common.collect.Maps;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.DescribeLivePushAuthKeyRequest;
import com.tencentcloudapi.live.v20180801.models.DescribeLivePushAuthKeyResponse;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.java.Log;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
@Log
public class LiveStreamingUtils {

    @Autowired
    private ISysDictService iSysDictService;


    /**
     * 会员票据生成
     *
     * @param userId
     * @return
     */
    public String getSign(String userId){
        String sdkappid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "txy_jstxim_sdkappid");
        String sdksecretkey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "txy_jstxim_sdksecretkey");

        TLSSigAPIv2 tlsSigAPIv2=new TLSSigAPIv2(Long.parseLong(sdkappid),sdksecretkey);
        return tlsSigAPIv2.genUserSig(userId,86400);
    }

    public Map<String,String> getPullAddess(String streamName){
        //播放域名
        String pullStreamAddress = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "pull_stream_address");
        Map<String,String> resultMap= Maps.newHashMap();
        resultMap.put("rtmpBroadcastAddress","rtmp://"+pullStreamAddress+"/live/"+streamName);
        resultMap.put("flvBroadcastAddress","https://"+pullStreamAddress+"/live/"+streamName+".flv");
        resultMap.put("hlsBroadcastAddress","https://"+pullStreamAddress+"/live/"+streamName+".m3u8");
        resultMap.put("udpBroadcastAddress","webrtc://"+pullStreamAddress+"/live/"+streamName);
        return resultMap;
    }


    /**
     * 获取推流地址
     *
     * @param streamName
     * @param txTime
     * @return
     */
   public Map<String,String> getPushAddress(String streamName,long txTime ){
       //推流域名
       String pushStreamAddress = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "push_stream_address");
       Map<String,String> resultMap= Maps.newHashMap();
       String safeUrl=getSafeUrl(getPushAuthKey(),streamName,txTime/1000);
       resultMap.put("pushStreamAddress","rtmp://"+pushStreamAddress+"/live/"+streamName+"?"+safeUrl);
       resultMap.put("obsStreamName","rtmp://"+pushStreamAddress+"/live/");
       resultMap.put("obsStreamAddress",streamName+"?"+safeUrl);
       return resultMap;
   }


    /**
     * 获取推流鉴权key
     * @return
     */
    public String getPushAuthKey(){
        //腾讯云：SecretId
        String tencentCloudSecretid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "tencent_cloud_secretid");
        //腾讯云：SecretKey
        String tencentCloudSecretkey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "tencent_cloud_secretkey");
        //推流域名
        String pushStreamAddress = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "push_stream_address");

        Credential cred = new Credential(tencentCloudSecretid, tencentCloudSecretkey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("live.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        LiveClient client = new LiveClient(cred, "", clientProfile);

        DescribeLivePushAuthKeyRequest req = new DescribeLivePushAuthKeyRequest();
        req.setDomainName(pushStreamAddress);//推流域名
        try {
            DescribeLivePushAuthKeyResponse resp = client.DescribeLivePushAuthKey(req);
            log.info("获取推流鉴权Key："+DescribeLivePushAuthKeyResponse.toJsonString(resp));
            return resp.getPushAuthKeyInfo().getMasterAuthKey();
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return null;
    }



    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /*
     * KEY+ streamName + txTime
     */
    public String getSafeUrl(String key, String streamName, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamName).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
}
