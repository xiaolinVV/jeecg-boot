package org.jeecg.modules.store.api;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("before/storeManage")
@Slf4j
public class BeforeStoreManageController {

    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DySmsHelper dySmsHelper;
    /**
     * 根据坐标获取地址列表
     * @param address
     * @return
     */
    @RequestMapping("getAddsress")
    @ResponseBody
    @Cacheable(value = "getAddsress",key ="#address+'_'+#pageSize+'_'+#pageIndex" )
    public Result<Object> getAddsress(String address, @RequestParam(defaultValue = "20",value = "pageSize") Integer pageSize, @RequestParam(defaultValue = "1",value = "pageIndex") Integer pageIndex){

        Result<Object> result=new Result<>();

        if(StringUtils.isBlank(address)){
            result.error500("address的位置信息不能为空");
            return result;
        }

        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/geocoder/v1/?address="+address+"&key="+tencentMapsKey;
        String sig= DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);
        String lat= JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lat");//纬度
        String lng=JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lng");//经度
        String location=lat+","+lng;
        return geocoder(location,pageSize,pageIndex);
    }
    /**
     * 根据坐标获取地址列表
     * @param location
     * @return
     */
    @RequestMapping("geocoder")
    @ResponseBody
    @Cacheable(value = "geocoder",key = "#location+'_'+#pageSize+'_'+#pageIndex")
    public Result<Object> geocoder(String location, @RequestParam(defaultValue = "20",value = "pageSize") Integer pageSize, @RequestParam(defaultValue = "1",value = "pageIndex") Integer pageIndex){

        Result<Object> result=new Result<>();

        if(StringUtils.isBlank(location)){
            result.error500("location的位置信息不能为空");
            return result;
        }

        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/geocoder/v1/?get_poi=1&key="+tencentMapsKey+"&location="+location+"&poi_options=page_size="+pageSize+";page_index="+pageIndex+";policy=2";
        String sig=DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);

        result.setResult(JSON.parseObject(mapResult).getJSONObject("result").getJSONArray("pois"));
        result.success("获取到地图的地址");
        return result;
    }


    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @RequestMapping("verificationCode")
    @ResponseBody
    public Result<String> verificationCode(String phone){
        Result<String> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(phone)){
            result.error500("手机号码不能为空");
            return result;
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(6);
        JSONObject templateParamJson = new JSONObject();
        templateParamJson.put("code", captcha);

//        try {
//            if(dySmsHelper.sendSms(phone, captcha, dySmsHelper.IDENTITY_TEMPLATE_CODE)){
//                //验证码10分钟内有效
//                redisUtil.set(phone, captcha, 600);
//            }else{
//                result.error500("验证码发送失败");
//                return result;
//            }
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
        try {
            if(DySmsHelper.sendSms(phone, templateParamJson, DySmsEnum.IDENTITY_TEMPLATE_CODE)){
                //验证码10分钟内有效
                redisUtil.set(phone, captcha, 600);
            }else{
                result.error500("验证码发送失败");
                return result;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }

        result.success("验证码发送成功");
        return result;
    }

}
