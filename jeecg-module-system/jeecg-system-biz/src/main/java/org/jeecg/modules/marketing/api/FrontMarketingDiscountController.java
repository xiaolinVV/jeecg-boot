package org.jeecg.modules.marketing.api;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.entity.MarketingCommingStore;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.service.IMarketingCommingStoreService;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.service.IMarketingDiscountGoodService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysFrontSetting;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券接口控制层
 */
@RequestMapping("front/marketingDiscount")
@Controller
public class FrontMarketingDiscountController {

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    @Autowired
    private TengxunMapUtils tengxunMapUtils;
    @Autowired
    private ISysFrontSettingService iSysFrontSettingService;
    @Autowired
    private IMarketingDiscountGoodService iMarketingDiscountGoodService;
    @Autowired
    private IMarketingCommingStoreService iMarketingCommingStoreService;

    /**
     * 根据门槛查询优惠券信息
     * @param isThreshold
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingDiscountByIsThreshold")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingDiscountByIsThreshold(String isThreshold,
                                                                                String name,
                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                @RequestAttribute("memberId") String memberId){
        Result<IPage<Map<String,Object>>> result=new Result<>();

        if(StringUtils.isBlank(isThreshold)){
            result.error500("isThreshold不能为空！！！");
            return result;
        }
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        String frontLogo = iSysFrontSettingService.getOne(new LambdaUpdateWrapper<SysFrontSetting>().eq(SysFrontSetting::getDelFlag, "0")).getFrontLogo();
        IPage<Map<String,Object>> marketingDiscountMapIPage=iMarketingDiscountService.findMarketingDiscountByIsThreshold(page,isThreshold,name);
        List<Map<String,Object>> marketingDiscount=marketingDiscountMapIPage.getRecords();
        for (Map<String,Object> md:marketingDiscount) {
            if(md.get("logoAddr")!=null) {
                md.put("logoAddr",new String((byte[])md.get("logoAddr")));
            }else {
                md.put("logoAddr", JSON.parseObject(frontLogo).get("0"));
            }
            if(StringUtils.isNotBlank(memberId)) {
                QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
                marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", md.get("id"));
                marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
                marketingDiscountCouponQueryWrapper.in("status", "0", "1");
                marketingDiscountCouponQueryWrapper.orderByDesc("create_time");
                if (iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper) > 0) {
                    md.put("ifGet", "1");
                    MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.list(marketingDiscountCouponQueryWrapper).get(0);
                    md.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                    md.put("marketingDiscountCouponStatus",marketingDiscountCoupon.getStatus());
                } else {
                    md.put("ifGet", "0");
                }
            }else{
                md.put("ifGet", "0");
            }
        }
        result.setResult(marketingDiscountMapIPage);

        result.success("查询优惠券成功");
        return result;
    }



    /**
     * 根据商品id查询全部优惠券列表
     * @param goodId
     * @param isPlatform
     * @return
     */
    @RequestMapping("findMarketingDiscountByGoodIdAll")
    @ResponseBody
    @Deprecated
    public Result<?> findMarketingDiscountByGoodIdAll(String goodId,
                                                   Integer isPlatform,
                                                   @RequestAttribute(name = "memberId",required = false) String memberId){
        Result<List<Map<String,Object>>> result=new Result<>();
        //参数校验
        //判断数据不为空
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId商品id不能为空！！！");
            return result;
        }
        //组织查询参数
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("goodId",goodId);
        paramMap.put("isPlatform",isPlatform);
        String frontLogo = iSysFrontSettingService.getOne(new LambdaUpdateWrapper<SysFrontSetting>().eq(SysFrontSetting::getDelFlag, "0")).getFrontLogo();
        List<Map<String,Object>> marketingDiscount=iMarketingDiscountService.findMarketingDiscountByGoodId(paramMap);
        for (Map<String,Object> md:marketingDiscount) {
            if(md.get("logoAddr")!=null) {
                md.put("logoAddr",JSON.parseObject((String) md.get("logoAddr")).get("0"));
            }else {
                md.put("logoAddr", JSON.parseObject(frontLogo).get("0"));
            }
            if(StringUtils.isNotBlank(memberId)) {
                QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
                marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", md.get("id"));
                marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
                marketingDiscountCouponQueryWrapper.in("status", "0", "1");
                if (iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper) > 0) {
                    md.put("ifGet", "1");
                    MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.list(marketingDiscountCouponQueryWrapper).get(0);
                    md.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                    md.put("marketingDiscountCouponStatus",marketingDiscountCoupon.getStatus());
                } else {
                    md.put("ifGet", "0");
                }
            }else{
                md.put("ifGet", "0");
            }
            //优惠券说明
            String vouchersWay=md.get("vouchersWay").toString();
            String explainDisCount="";
            if(vouchersWay.equals("0")){
                explainDisCount=md.get("startTime")+"~"+md.get("endTime");
            }
            if(vouchersWay.equals("1")){
                explainDisCount="领取当日起"+md.get("disData")+md.get("monad")+"内";
            }
            if(vouchersWay.equals("2")){
                explainDisCount="领取次日起"+md.get("disData")+md.get("monad")+"内";
            }
            md.put("explainDisCount",explainDisCount);
            if(new BigDecimal(md.get("completely").toString()).doubleValue()>0) {
                md.put("usingThreshold", "满" + md.get("completely") + "元减" + md.get("subtract"));
            }else{
                md.put("usingThreshold", "无门槛减" + md.get("subtract"));
            }
        }
        result.setResult(marketingDiscount);
        result.success("查询优惠券成功");
        return result;
    }

    /**
     * 根据商品id查询优惠券列表
     * @param goodId
     * @param isPlatform
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingDiscountByGoodId")
    @ResponseBody
    @Deprecated
    public Result<IPage<Map<String,Object>>> findMarketingDiscountByGoodId(String goodId,
                                                                           Integer isPlatform,
                                                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                           @RequestAttribute("memberId") String memberId){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //参数校验
        //判断数据不为空
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId商品id不能为空！！！");
            return result;
        }
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("goodId",goodId);
        paramMap.put("isPlatform",isPlatform);
        String frontLogo = iSysFrontSettingService.getOne(new LambdaUpdateWrapper<SysFrontSetting>().eq(SysFrontSetting::getDelFlag, "0")).getFrontLogo();
        IPage<Map<String,Object>> marketingDiscountMapIPage=iMarketingDiscountService.findMarketingDiscountByGoodId(page,paramMap);
        List<Map<String,Object>> marketingDiscount=marketingDiscountMapIPage.getRecords();
        for (Map<String,Object> md:marketingDiscount) {
            if(md.get("logoAddr")!=null) {
                md.put("logoAddr",JSON.parseObject((String) md.get("logoAddr")).get("0"));
            }else {
                md.put("logoAddr", JSON.parseObject(frontLogo).get("0"));
            }
            if(StringUtils.isNotBlank(memberId)) {
                QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
                marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", md.get("id"));
                marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
                marketingDiscountCouponQueryWrapper.in("status", "0", "1");
                if (iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper) > 0) {
                    md.put("ifGet", "1");
                    MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.list(marketingDiscountCouponQueryWrapper).get(0);
                    md.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                    md.put("marketingDiscountCouponStatus",marketingDiscountCoupon.getStatus());
                } else {
                    md.put("ifGet", "0");
                }
            }else{
                md.put("ifGet", "0");
            }
        }
        result.setResult(marketingDiscountMapIPage);
        result.success("查询优惠券成功");
        return result;
    }


    /**
     * 根据店铺id查询优惠券列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingDiscountBySysUserId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingDiscountBySysUserId(@RequestHeader(defaultValue = "") String sysUserId,
                                                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                              @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        //获取券
        IPage<Map<String,Object>> marketingDiscountMapIPage= iMarketingDiscountService.findMarketingDiscountBySysUserId(page,sysUserId);
        String frontLogo = iSysFrontSettingService.getOne(new LambdaUpdateWrapper<SysFrontSetting>()).getFrontLogo();
        marketingDiscountMapIPage.getRecords().forEach(md->{
            if(StringUtils.isNotBlank(memberId)) {
                QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
                marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", md.get("id"));
                marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
                marketingDiscountCouponQueryWrapper.in("status", "0", "1");
                if (iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper) > 0) {
                    md.put("ifGet", "1");
                    MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.list(marketingDiscountCouponQueryWrapper).get(0);
                    md.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                    md.put("marketingDiscountCouponStatus",marketingDiscountCoupon.getStatus());
                } else {
                    md.put("ifGet", "0");
                }
                if(StringUtils.isBlank(String.valueOf(md.get("logoAddr")))){
                    md.put("logoAddr",frontLogo);
                }
            }else{
                md.put("ifGet", "0");
            }
        });

        result.setResult(marketingDiscountMapIPage);
        result.success("查询优惠券成功");
        return result;
    }


    /**
     * 根据优惠券id获取详情信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findMarketingDiscountById")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingDiscountById(String id,
                                                                String location,
                                                                @RequestAttribute("memberId") String memberId){
        Result<Map<String,Object>> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        String frontLogo = iSysFrontSettingService.getOne(new LambdaUpdateWrapper<SysFrontSetting>().eq(SysFrontSetting::getDelFlag, "0")).getFrontLogo();
        Map<String,Object> marketingDiscount =iMarketingDiscountService.findMarketingDiscountById(id);
        if(marketingDiscount.get("logoAddr")!=null) {
            marketingDiscount.put("logoAddr", Convert.toStr(marketingDiscount.get("logoAddr"),""));
        }else {
            marketingDiscount.put("logoAddr",frontLogo);
        }
        if(marketingDiscount.get("isPlatform").equals("0")&&marketingDiscount.get("sysUserId")!=null&&StringUtils.isNotBlank(marketingDiscount.get("sysUserId").toString())) {
            marketingDiscount.put("isViewStore","1");
            QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
            storeManageQueryWrapper.eq("sys_user_id", marketingDiscount.get("sysUserId"));
            storeManageQueryWrapper.in("pay_status", "1","2");
            StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
            if (storeManage.getSubStoreName() == null) {
                marketingDiscount.put("storeName", storeManage.getStoreName());
            } else {
                marketingDiscount.put("storeName", storeManage.getStoreName() + "(" + storeManage.getStoreName() + ")");
            }

            marketingDiscount.put("latitude",storeManage.getLatitude());
            marketingDiscount.put("longitude",storeManage.getLongitude());
            marketingDiscount.put("storeAddress", storeManage.getStoreAddress());
            marketingDiscount.put("takeOutPhone", storeManage.getTakeOutPhone());

             if (StringUtils.isNotBlank(location)) {
                JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,storeManage.getLatitude()+","+storeManage.getLongitude()));
                if(mapJsonArray!=null) {
                    mapJsonArray.stream().forEach(mj -> {
                        JSONObject jb = (JSONObject) mj;
                        BigDecimal dis = new BigDecimal(jb.getString("distance"));
                        if (dis.doubleValue() > 1000) {
                            marketingDiscount.put("distance", dis.divide(new BigDecimal(1000)).setScale(2, RoundingMode.DOWN) + "km");
                        } else {
                            marketingDiscount.put("distance", dis + "m");
                        }
                    });
                }
            } else {
                marketingDiscount.put("distance", "");
            }
        }else{
            marketingDiscount.put("isViewStore","0");
        }
        if(memberId!=null) {
            QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
            marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", marketingDiscount.get("id"));
            marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
            marketingDiscountCouponQueryWrapper.in("status", "0", "1");
            if (iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper) > 0) {
                marketingDiscount.put("ifGet", "1");
                MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.list(marketingDiscountCouponQueryWrapper).get(0);
                marketingDiscount.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                marketingDiscount.put("marketingDiscountCouponStatus",marketingDiscountCoupon.getStatus());
            } else {
                marketingDiscount.put("ifGet", "0");
            }
        }else{
            marketingDiscount.put("ifGet", "0");
        }
        if (marketingDiscount.get("isPlatform").equals("0")){
            marketingDiscount.put("goodList",iMarketingDiscountGoodService.findMarketingDiscountStoreGoodById(id));
        }else if (marketingDiscount.get("isPlatform").equals("1")){
            marketingDiscount.put("goodList",iMarketingDiscountGoodService.findMarketingDiscountGoodById(id));
        }

        result.setResult(marketingDiscount);
        result.success("获取券详情信息");
        return result;
    }

    /**
     * 券中心优惠券列表(免费领券)
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingDiscountPage")
    @ResponseBody
    public Result<?> findMarketingDiscountPage(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               @RequestAttribute("memberId") String memberId){
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        IPage<Map<String, Object>> marketingDiscountPage = iMarketingDiscountService.findMarketingDiscountPage(page);
        List<Map<String, Object>> records = marketingDiscountPage.getRecords();
        records.forEach(rs->{
            if (memberId != null){
                if (rs.get("isGetThe").equals("0")){
                    //不可再次领取
                    if (iMarketingDiscountCouponService.count(new LambdaQueryWrapper<MarketingDiscountCoupon>()
                            .eq(MarketingDiscountCoupon::getDelFlag,"0")
                            .eq(MarketingDiscountCoupon::getMemberListId,memberId.toString())
                            .eq(MarketingDiscountCoupon::getMarketingDiscountId,rs.get("id"))
                    )>0){
                        MarketingDiscountCoupon marketingDiscountCoupon = iMarketingDiscountCouponService.list(new LambdaQueryWrapper<MarketingDiscountCoupon>()
                                .eq(MarketingDiscountCoupon::getDelFlag, "0")
                                .eq(MarketingDiscountCoupon::getMemberListId,memberId.toString())
                                .eq(MarketingDiscountCoupon::getMarketingDiscountId, rs.get("id"))
                                .orderByDesc(MarketingDiscountCoupon::getCreateTime)
                        ).get(0);
                        rs.put("ifGet","1");
                        rs.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                    }else {
                        rs.put("ifGet","0");
                    }
                }else {
                    //可再次领取
                    if (iMarketingDiscountCouponService.count(new LambdaQueryWrapper<MarketingDiscountCoupon>()
                            .eq(MarketingDiscountCoupon::getDelFlag,"0")
                            .eq(MarketingDiscountCoupon::getMemberListId,memberId.toString())
                            .eq(MarketingDiscountCoupon::getMarketingDiscountId,rs.get("id"))
                    )>0){
                        MarketingDiscountCoupon marketingDiscountCoupon = iMarketingDiscountCouponService.list(new LambdaQueryWrapper<MarketingDiscountCoupon>()
                                .eq(MarketingDiscountCoupon::getDelFlag, "0")
                                .eq(MarketingDiscountCoupon::getMemberListId,memberId.toString())
                                .eq(MarketingDiscountCoupon::getMarketingDiscountId, rs.get("id"))
                                .orderByDesc(MarketingDiscountCoupon::getCreateTime)
                        ).get(0);
                        if (marketingDiscountCoupon.getStatus().equals("2")){
                            if (String.valueOf(rs.get("againGet")).contains("1")){
                                rs.put("ifGet","0");
                            }else {
                                rs.put("ifGet","1");
                                rs.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                            }
                        }else if (marketingDiscountCoupon.getStatus().equals("3")){
                            if (String.valueOf(rs.get("againGet")).contains("2")){
                                rs.put("ifGet","0");
                            }else {
                                rs.put("ifGet","1");
                                rs.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                            }
                        }else if (marketingDiscountCoupon.getStatus().equals("5")){
                            if (String.valueOf(rs.get("againGet")).contains("0")){
                                rs.put("ifGet","0");
                            }else {
                                rs.put("ifGet","1");
                                rs.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                            }
                        }else {
                            rs.put("ifGet","1");
                            rs.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                        }

                    }else {
                        rs.put("ifGet","0");
                    }
                }
            }else {
                rs.put("ifGet","0");
            }
        });
        return Result.ok(marketingDiscountPage);
    }

    /**
     * 优惠券适用商品
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingDiscountGoodPageListById")
    @ResponseBody
    public Result<?> findMarketingDiscountGoodPageListById(String id,
                                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        MarketingDiscount marketingDiscount = iMarketingDiscountService.getById(id);
        //判断是否为店铺券
        if (marketingDiscount.getIsPlatform().equals("0")){
            //查询店铺商品
            return Result.ok(iMarketingDiscountGoodService.findMarketingDiscountStoreGoodPageListById(page,id));
        }else{
            //查询平台商品
            return Result.ok(iMarketingDiscountGoodService.findMarketingDiscountGoodPageListById(page,id));
        }

    }

    /**
     * 优惠券店铺
     * @param id
     * @return
     */
    @RequestMapping("findMarketingDiscountStorePageListById")
    @ResponseBody
    public Result<?> findMarketingDiscountStorePageListById(String id,
                                                            @RequestHeader(defaultValue = "") String longitude,
                                                            @RequestHeader(defaultValue = "") String latitude){
        HashMap<String, Object> map = new HashMap<>();

        MarketingDiscount marketingDiscount = iMarketingDiscountService.getById(id);
        if (StringUtils.isNotBlank(marketingDiscount.getSysUserId())){
            StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getDelFlag, "0")
                    .eq(StoreManage::getSysUserId, marketingDiscount.getSysUserId())
            );
            if (storeManage != null) {
                //店铺id
                map.put("id",storeManage.getId());
                //店铺userid
                map.put("sysUserId",storeManage.getSysUserId());
                //店铺名称
                if (storeManage.getSubStoreName() == null) {
                    map.put("storeName", storeManage.getStoreName());
                } else {
                    map.put("storeName", storeManage.getStoreName() + "(" + storeManage.getStoreName() + ")");
                }
                //纬度
                map.put("latitude",storeManage.getLatitude());
                //经度
                map.put("longitude",storeManage.getLongitude());
                //店铺logo
                map.put("logoAddr",storeManage.getLogoAddr());
                //送福利金；0：不显示；1：显示
                map.put("ifViewWelfarePayments",storeManage.getIfViewWelfarePayments());
                //是否开启福利金收款；0：关闭；1：开启
                map.put("isOpenWelfarePayments",storeManage.getIsOpenWelfarePayments());
                String s = "";
                String areaAddress = storeManage.getAreaAddress();
                for (String aa : Arrays.asList(areaAddress.split(","))) {
                    s = s + aa;
                }
                if (iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                        .eq(MarketingCommingStore::getDelFlag,"0")
                        .eq(MarketingCommingStore::getStoreManageId,storeManage.getId())
                        .eq(MarketingCommingStore::getTakeWay,"0")
                )>0){
                    map.put("isViewTakeWayCountScan","1");
                }else {
                    map.put("isViewTakeWayCountScan","0");
                }
                if (iMarketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                        .eq(MarketingCommingStore::getDelFlag,"0")
                        .eq(MarketingCommingStore::getStoreManageId,storeManage.getId())
                        .eq(MarketingCommingStore::getTakeWay,"1")
                )>0){
                    map.put("isViewTakeWayCount","1");
                }else {
                    map.put("isViewTakeWayCount","0");
                }
                //详细地址
                map.put("storeAddress",s+storeManage.getStoreAddress());
                //距离
                String location = latitude +","+ longitude;
                if (StringUtils.isNotBlank(location)) {
                    JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,storeManage.getLatitude()+","+storeManage.getLongitude()));
                    if(mapJsonArray!=null) {
                        mapJsonArray.forEach(mj -> {
                            JSONObject jb = (JSONObject) mj;
                            BigDecimal dis = new BigDecimal(jb.getString("distance"));
                            if (dis.doubleValue() > 1000) {
                                map.put("distance", dis.divide(new BigDecimal(1000)).setScale(2, RoundingMode.DOWN) + "km");
                            } else {
                                map.put("distance", dis + "m");
                            }
                        });
                    }
                } else {
                    map.put("distance", "");
                }
                //客服电话
                map.put("takeOutPhone",storeManage.getTakeOutPhone());

                return Result.ok(map);
            }else {
                return Result.ok("");
            }
        }else {
            return Result.ok("");
        }
    }
}
