package org.jeecg.modules.map.api;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequestMapping("after/map")
@Controller
@Slf4j
public class AfterMapController {


    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 根据坐标获取地址列表（废弃接口，迁移到FrontMapController里面）
     * @param location
     * @return
     */
    @RequestMapping("geocoder")
    @ResponseBody
    @Cacheable(value = "geocoder",key = "#location+'_'+#pageSize+'_'+#pageIndex")
    @Deprecated
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
        String sig=DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);
        String lat=JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lat");//纬度
        String lng=JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lng");//经度
        String location=lat+","+lng;
        return geocoder(location,pageSize,pageIndex);
    }

    /**
     * 根据坐标获取地址列表
     * @param address
     * @return
     */
    @RequestMapping("getAllAddsress")
    @ResponseBody
    @Cacheable(value = "getAllAddsress",key ="#address" )
    public Result<Map<String,Object>> getAllAddsress(String address){

        Result<Map<String,Object>> result=new Result<>();

        Map<String,Object> objectMap= Maps.newHashMap();

        if(StringUtils.isBlank(address)){
            result.error500("address的位置信息不能为空");
            return result;
        }

        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/geocoder/v1/?address="+address+"&key="+tencentMapsKey;
        String sig=DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);
        String lat=JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lat");//纬度
        String lng=JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lng");//经度
        objectMap.put("lat",lat);
        objectMap.put("lng",lng);
        result.setResult(objectMap);
        result.success("通过地址获取坐标成功");
        return result;
    }


    /**
     * 获取区域地址位置
     *
     * @return
     */
    @RequestMapping("getAddressByMap")
    @ResponseBody
    @Cacheable(value = "getAddressByMap" )
    public Result<String> getAddressByMap(){

        Result<String> result=new Result<>();
        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/district/v1/list?key="+tencentMapsKey;
        String sig= DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);
        result.setResult(JSON.toJSONString(JSON.parseObject(mapResult).getJSONArray("result").get(1)));
        result.success("市级数据获取成功");
        return result;
    }

}
