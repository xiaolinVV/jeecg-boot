package org.jeecg.modules.marketing.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.entity.MarketingCertificate;
import org.jeecg.modules.marketing.entity.MarketingCertificateStore;
import org.jeecg.modules.marketing.entity.MarketingCommingStore;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysFrontSetting;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 兑换券api
 */
@RequestMapping("front/marketingCertificate")
@Controller
public class FrontMarketingCertificateController {

    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;

    @Autowired
    private IMarketingCertificateStoreService iMarketingCertificateStoreService;


    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;
    @Autowired
    private ISysFrontSettingService iSysFrontSettingService;
    @Autowired
    private IMarketingCertificateGoodService iMarketingCertificateGoodService;
    @Autowired
    private IMarketingCommingStoreService iMarketingCommingStoreService;
    /**
     * 显示和隐藏兑换券和优惠券
     * @return
     */
    @RequestMapping("isViewMarketingCd")
    @ResponseBody
    public Result<Map<String,Object>> isViewMarketingCd(){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap=Maps.newHashMap();
        QueryWrapper<MarketingCertificate> marketingCertificateQueryWrapper=new QueryWrapper<>();
        marketingCertificateQueryWrapper.eq("is_nomal","1");
        marketingCertificateQueryWrapper.eq("status","1");
        long marketingCertificateCount=iMarketingCertificateService.count(marketingCertificateQueryWrapper);
        if(marketingCertificateCount>0){
            objectMap.put("isViewMarketingCertificate","1");
        }else{
            objectMap.put("isViewMarketingCertificate","0");
        }

        QueryWrapper<MarketingDiscount> marketingDiscountQueryWrapper=new QueryWrapper<>();
        marketingDiscountQueryWrapper.eq("is_nomal","0");
        marketingDiscountQueryWrapper.eq("status","1");
        long marketingDiscountCount=iMarketingDiscountService.count(marketingDiscountQueryWrapper);
        if(marketingDiscountCount>0){
            objectMap.put("isViewMarketingDiscount","1");
        }else{
            objectMap.put("isViewMarketingDiscount","0");
        }
        result.setResult(objectMap);
        result.success("显示隐藏设置成功");
        return  result;
    }





    /**
     * 查询兑换券列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingCertificateList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingCertificateList(String name,
                                                                          @RequestHeader(defaultValue = "") String sysUserId,
                                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        IPage<Map<String, Object>> marketingCertificateList = iMarketingCertificateService.findMarketingCertificateList(page, name);
        result.setResult(marketingCertificateList);
        result.success("查询兑换券成功");
        return result;
    }


    /**
     * 查询兑换券列表根据店铺id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingCertificateBySysUserId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingCertificateBySysUserId(@RequestHeader(defaultValue = "") String sysUserId,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        result.setResult(iMarketingCertificateService.findMarketingCertificateBySysUserId(page,sysUserId));
        result.success("查询兑换券成功");
        return result;
    }




    /**
     * 获取兑换券详情(弃用)
     * @param id
     * @return
     */
    @RequestMapping("findMarketingCertificateInfo")
    @ResponseBody
    @Deprecated
    public Result<Map<String,Object>> findMarketingCertificateInfo(String location,String id,
                                                                   @RequestParam(required = false,defaultValue = "1") Integer isPlatform){
        Result<Map<String,Object>> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        //获取兑换券
        Map<String,Object> marketingCertificateObjectMap=iMarketingCertificateService.findMarketingCertificateInfo(id);
        //店铺数据
        List<Map<String,Object>> stores= Lists.newArrayList();
        marketingCertificateObjectMap.put("stores",stores);
        //获取兑换券店铺id集合
        QueryWrapper<MarketingCertificateStore> marketingCertificateStoreQueryWrapper=new QueryWrapper<>();
        marketingCertificateStoreQueryWrapper.eq("marketing_certificate_id",marketingCertificateObjectMap.get("id"));
        List<MarketingCertificateStore> marketingCertificateStores=iMarketingCertificateStoreService.list(marketingCertificateStoreQueryWrapper);

        //店铺坐标集合
        Map<String,Object> mapsMap=Maps.newHashMap();

        for (MarketingCertificateStore mcf:marketingCertificateStores) {
            Map<String,Object> storeMap= Maps.newHashMap();
            QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
            storeManageQueryWrapper.eq("sys_user_id", mcf.getSysUserId());
            storeManageQueryWrapper.in("pay_status", "1","2");
            if(iStoreManageService.count(storeManageQueryWrapper)>0) {
                StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                storeMap.put("sysUserId", mcf.getSysUserId());
                if (storeManage.getSubStoreName() == null) {
                    storeMap.put("storeName", storeManage.getStoreName());
                } else {
                    storeMap.put("storeName", storeManage.getStoreName() + "(" + storeManage.getStoreName() + ")");
                }
                storeMap.put("storeAddress", storeManage.getStoreAddress());
                storeMap.put("takeOutPhone", storeManage.getTakeOutPhone());
                storeMap.put("logoAddr", storeManage.getLogoAddr());
                storeMap.put("latitude",storeManage.getLatitude());
                storeMap.put("longitude",storeManage.getLongitude());
                stores.add(storeMap);
                mapsMap.put(storeManage.getLatitude() + "," + storeManage.getLongitude(), storeMap);
            }
        }

        if(StringUtils.isNotBlank(location)){

            JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,StringUtils.join(mapsMap.keySet(),";")));
            if(mapJsonArray!=null) {
                mapJsonArray.stream().forEach(mj -> {
                    JSONObject jb = (JSONObject) mj;
                    Map<String, Object> s = (Map<String, Object>) mapsMap.get(StringUtils.substringBefore(jb.getJSONObject("to").getString("lat"), ".")
                            + "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lat"), "."), 6, "0")
                            + "," + StringUtils.substringBefore(jb.getJSONObject("to").getString("lng"), ".")
                            + "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lng"), "."), 6, "0"));
                    BigDecimal dis=new BigDecimal(jb.getString("distance"));
                    if(dis.doubleValue()>1000){
                        s.put("distance", dis.divide(new BigDecimal(1000),2) + "km");
                    }else{
                        s.put("distance", dis + "m");
                    }

                });
            }
        }
        marketingCertificateObjectMap.put("isPlatform",isPlatform);
        result.setResult(marketingCertificateObjectMap);
        result.success("获取券详情信息");
        return result;
    }
    /**
     * 获取兑换券详情
     * @param id
     * @return
     */
    @RequestMapping("newFindMarketingCertificateInfo")
    @ResponseBody
    public Result<Map<String,Object>> newFindMarketingCertificateInfo(String id,
                                                                      @RequestHeader(defaultValue = "") String sysUserId,
                                                                      @RequestHeader(defaultValue = "") String longitude,
                                                                      @RequestHeader(defaultValue = "") String latitude,
                                                                      @RequestParam(required = false,defaultValue = "1") String isPlatform,
                                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        Result<Map<String,Object>> result=new Result<>();

        Page<Map<String, Object>> page = new Page<>(pageNo,pageSize);
        //获取兑换券
        Map<String,Object> marketingCertificateObjectMap=iMarketingCertificateService.findMarketingCertificateInfo(id);
        HashMap<Object, Object> objectObjectHashMap = Maps.newHashMap();
        objectObjectHashMap.put("longitude",longitude);
        objectObjectHashMap.put("latitude",latitude);

        //判断是指定门店查出指定门店
        if (marketingCertificateObjectMap.get("rewardStore").equals("1")){
            objectObjectHashMap.put("id",id);
        }
        //判断是全平台查出全平台店铺
        if (marketingCertificateObjectMap.get("rewardStore").equals("0")){
            objectObjectHashMap.put("id",null);
        }

        if (StringUtils.isNotBlank(sysUserId)){
            objectObjectHashMap.put("sysUserId",sysUserId);
        }else {
            objectObjectHashMap.put("sysUserId","");
        }
        IPage<Map<String, Object>> certificateStore = iStoreManageService.findCertificateStore(page, objectObjectHashMap);
        marketingCertificateObjectMap.put("stores",certificateStore);
        remap(latitude+longitude, certificateStore);

        if (oConvertUtils.isNotEmpty(marketingCertificateObjectMap.get("mainPicture"))){
            if (oConvertUtils.isEmpty(JSON.parseObject(marketingCertificateObjectMap.get("mainPicture").toString()).get("0"))){
                SysFrontSetting sysFrontSetting = iSysFrontSettingService.list(new LambdaQueryWrapper<SysFrontSetting>()
                        .eq(SysFrontSetting::getDelFlag, "0")).get(0);
                marketingCertificateObjectMap.put("mainPicture",sysFrontSetting.getFrontLogo());
            }
        }else{
            SysFrontSetting sysFrontSetting = iSysFrontSettingService.list(new LambdaQueryWrapper<SysFrontSetting>()
                    .eq(SysFrontSetting::getDelFlag, "0")).get(0);
            marketingCertificateObjectMap.put("mainPicture",sysFrontSetting.getFrontLogo());
        }
        marketingCertificateObjectMap.put("isPlatform",isPlatform);
        marketingCertificateObjectMap.put("goodList",iMarketingCertificateGoodService.findMarketingCertificateSeckillListGoodByCertificateId(id));
        result.setResult(marketingCertificateObjectMap);
        result.success("获取券详情信息");
        return result;
    }

    private Map<String,Object>remap (String location,IPage<Map<String, Object>> certificateStore){
        HashMap<String, Object> hashMap = Maps.newHashMap();
        certificateStore.getRecords().forEach(re->{

            hashMap.put(re.get("latitude")+","+re.get("longitude"),re);
        });

        if(StringUtils.isNotBlank(location)){

            JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,StringUtils.join(hashMap.keySet(),";")));
            if(mapJsonArray!=null) {
                mapJsonArray.forEach(mj -> {
                    JSONObject jb = (JSONObject) mj;
                    Map<String, Object> s = (Map<String, Object>) hashMap.get(StringUtils.substringBefore(jb.getJSONObject("to").getString("lat"), ".")
                            + "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lat"), "."), 6, "0")
                            + "," + StringUtils.substringBefore(jb.getJSONObject("to").getString("lng"), ".")
                            + "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lng"), "."), 6, "0"));
                    BigDecimal dis=new BigDecimal(jb.getString("distance"));
                    if(dis.doubleValue()>1000){
                        s.put("distance", dis.divide(new BigDecimal(1000),2,RoundingMode.DOWN) + "km");
                    }else{
                        s.put("distance", dis + "m");
                    }

                });
            }
        }

        return hashMap;
    }
    @RequestMapping("findMarketingCertificateSeckillPageListStoreByCertificateId")
    @ResponseBody
    public Result<?> findMarketingCertificateSeckillPageListStoreByCertificateId(String id,
                                                                                 @RequestHeader(defaultValue = "") String longitude,
                                                                                 @RequestHeader(defaultValue = "") String latitude,
                                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Page<Map<String, Object>> page = new Page<>(pageNo,pageSize);
        MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(id);
        IPage<Map<String, Object>> marketingCertificateSeckillPageListStoreByCertificateId = null;
        if (marketingCertificate.getRewardStore().equals("0")){
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("pattern","0");
            paramMap.put("longitude",new BigDecimal(longitude));
            paramMap.put("latitude",new BigDecimal(latitude));
            marketingCertificateSeckillPageListStoreByCertificateId = iStoreManageService.getStoreManageByRecommend(page, paramMap);
        }else {
            marketingCertificateSeckillPageListStoreByCertificateId = iMarketingCertificateStoreService.findMarketingCertificateSeckillPageListStoreByCertificateId(page, id, latitude, longitude);
        }


        marketingCertificateSeckillPageListStoreByCertificateId.getRecords().forEach(mcp->{
            if (iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                    .eq(MarketingCommingStore::getDelFlag,"0")
                    .eq(MarketingCommingStore::getStoreManageId,mcp.get("id"))
                    .eq(MarketingCommingStore::getTakeWay,"0")
            )>0){
                mcp.put("isViewTakeWayCountScan","1");
            }else {
                mcp.put("isViewTakeWayCountScan","0");
            }
            if (iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                    .eq(MarketingCommingStore::getDelFlag,"0")
                    .eq(MarketingCommingStore::getStoreManageId,mcp.get("id"))
                    .eq(MarketingCommingStore::getTakeWay,"1")
            )>0){
                mcp.put("isViewTakeWayCount","1");
            }else {
                mcp.put("isViewTakeWayCount","0");
            }
            String location = latitude + "," +longitude;
            if (StringUtils.isNotBlank(location)) {
                JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,mcp.get("latitude")+","+mcp.get("longitude")));
                if(mapJsonArray!=null) {
                    mapJsonArray.forEach(mj -> {
                        JSONObject jb = (JSONObject) mj;
                        BigDecimal dis = new BigDecimal(jb.getString("distance"));
                        if (dis.doubleValue() > 1000) {
                            mcp.put("distance", dis.divide(new BigDecimal(1000)).setScale(2, RoundingMode.DOWN) + "km");
                        } else {
                            mcp.put("distance", dis + "m");
                        }
                    });
                }
            } else {
                mcp.put("distance", "");
            }
        });
        return Result.ok(marketingCertificateSeckillPageListStoreByCertificateId);
    }
    @RequestMapping("findMarketingCertificateSeckillPageListGoodByCertificateId")
    @ResponseBody
    public Result<?>findMarketingCertificateSeckillPageListGoodByCertificateId(String id,
                                                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Page<Map<String, Object>> page = new Page<>(pageNo,pageSize);
        return Result.ok(iMarketingCertificateGoodService.findMarketingCertificateSeckillPageListGoodByCertificateId(page,id));
    }
}
