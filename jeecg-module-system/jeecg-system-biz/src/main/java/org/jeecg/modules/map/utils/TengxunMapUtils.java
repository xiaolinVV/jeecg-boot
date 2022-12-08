package org.jeecg.modules.map.utils;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class TengxunMapUtils {

    @Autowired
    private ISysDictService iSysDictService;


    @Cacheable(value = "findDistance",key = "#from+'_'+#to")
    public String findDistance(String from,String to){
        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/distance/v1/?from="+from+"&key="+tencentMapsKey+"&mode=driving&to="+to;
        String sig= DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);
        log.debug(mapResult);
        if(!JSON.parseObject(mapResult).getString("status").equals("0")){
            return null;
        }
        return JSON.toJSONString(JSON.parseObject(mapResult).getJSONObject("result").getJSONArray("elements"));
    }


    @Cacheable(value = "findAdcode",key = "#location")
    public String findAdcode(String location){
        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/geocoder/v1/?get_poi=1&key="+tencentMapsKey+"&location="+location;
        String sig=DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);
        return JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("ad_info").getString("adcode");
    }
}
