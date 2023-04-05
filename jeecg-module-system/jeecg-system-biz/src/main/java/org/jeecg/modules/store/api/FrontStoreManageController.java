package org.jeecg.modules.store.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagSettingService;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureSetttingService;
import org.jeecg.modules.member.entity.MemberAttentionStore;
import org.jeecg.modules.member.service.IMemberAttentionStoreService;
import org.jeecg.modules.order.utils.WeixinPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.entity.*;
import org.jeecg.modules.store.service.*;
import org.jeecg.modules.store.util.StoreUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 店铺接口无登录
 */
@RequestMapping("front/storeManage")
@Controller
@Slf4j
public class FrontStoreManageController {

    @Autowired
    private WeixinPayUtils weixinPayUtils;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    private IStoreSettingService iStoreSettingService;

    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    private ISysFrontSettingService iSysFrontSettingService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private ISysDictItemService iSysDictItemService;

    @Autowired
    private IStoreTypeService iStoreTypeService;

    @Autowired
    private IMarketingAdvertisingService iMarketingAdvertisingService;

    @Autowired
    private IMemberAttentionStoreService iMemberAttentionStoreService;

    @Autowired
    private StoreUtils storeUtils;

    @Autowired
    private IStoreLabelRelationService iStoreLabelRelationService;

    @Autowired
    private IStoreLabelService iStoreLabelService;

    @Autowired
    private IStoreCashierSettingService iStoreCashierSettingService;

    @Autowired
    private IStoreFunctionSetService iStoreFunctionSetService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;


    @Autowired
    private IMarketingStoreGiftbagSettingService iMarketingStoreGiftbagSettingService;

    @Autowired
    private IMarketingStorePrefectureSetttingService iMarketingStorePrefectureSetttingService;



    /**
     * 开店价格参数获取
     * @return
     */
    @RequestMapping("openStoreParam")
    @ResponseBody
    public Result<Map<String,Object>> openStoreParam(){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //终生价
        objectMap.put("lifelong",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","lifelong"));
        //包年价
        objectMap.put("packYears",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","pack_years"));

        result.setResult(objectMap);
        result.success("获取开店价格参数成功");
        return result;
    }


    /**
     * 店铺首页
     *
     * @param storeManageId
     * @param longitude
     * @param latitude
     * @param memberId
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<?> index(String storeManageId,
                           @RequestHeader(defaultValue = "") String longitude,
                           @RequestHeader(defaultValue = "") String latitude,
                           @RequestHeader("softModel") String softModel,
                           @RequestAttribute(value = "memberId",required = false) String memberId){
        //参数校验
        if(StringUtils.isBlank(storeManageId)){
            return Result.error("店铺id不能为空");
        }
        Map<String,Object> resultMap=iStoreManageService.getStoreManageById(storeManageId);
        if (Integer.parseInt(resultMap.get("disCount").toString()) > 0) {
            //获取券
            Page<Map<String, Object>> discountPage = new Page<Map<String, Object>>(1, 4);
            resultMap.put("storeDiscounts", iMarketingDiscountService.findMarketingDiscountBySysUserId(discountPage, resultMap.get("sysUserId").toString()).getRecords());
        } else {
            resultMap.put("storeDiscounts", "");
        }
        String floorState= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","floor_state");
        if(floorState.equals("0")){

            resultMap.put("distance", "");
            if (StringUtils.isNotBlank(latitude) && StringUtils.isNotBlank(longitude)) {
                String s1 = latitude + "," + longitude;
                JSONArray mapJsonArray = JSON.parseArray(tengxunMapUtils.findDistance(s1, resultMap.get("latitude") + "," + resultMap.get("longitude")));
                if (mapJsonArray != null) {
                    for (Object mj:mapJsonArray) {
                        JSONObject jb = (JSONObject) mj;
                        BigDecimal dis = new BigDecimal(jb.getString("distance"));
                        if (dis.doubleValue() > 1000) {
                            resultMap.put("distance", dis.divide(new BigDecimal(1000), 2, RoundingMode.DOWN) + "km");
                        } else {
                            resultMap.put("distance", dis + "m");
                        }
                    }
                }
            }
        }else{
            storeUtils.setFloor(resultMap,storeManageId);
        }
        //店铺广告
        Map<String, Object> paramObjectMap = Maps.newHashMap();
        paramObjectMap.put("pattern", 0);
        paramObjectMap.put("sysUserId", resultMap.get("sysUserId"));
        resultMap.put("marketingAdvertisings", iMarketingAdvertisingService.findMarketingAdvertisingByAdLocation(paramObjectMap));

        //进店奖励
        resultMap=iStoreManageService.setActivity(resultMap,resultMap.get("id").toString());


        if(StringUtils.isNotBlank(memberId)) {
            //进店奖励


            //店铺收藏
            long count=iMemberAttentionStoreService.count(new LambdaQueryWrapper<MemberAttentionStore>()
                    .eq(MemberAttentionStore::getMemberListId,memberId)
                    .eq(MemberAttentionStore::getSysUserId,resultMap.get("sysUserId")));
            if(count==0){
                MemberAttentionStore memberAttentionStore=new MemberAttentionStore();
                memberAttentionStore.setMemberListId(memberId);
                memberAttentionStore.setSysUserId(resultMap.get("sysUserId").toString());
                memberAttentionStore.setAttentionTime(new Date());
                iMemberAttentionStoreService.save(memberAttentionStore);
            }
        }
        //获取标签信息
        setStorelabels(storeManageId,resultMap);


        //店铺权限控制
        StoreFunctionSet storeFunctionSet=iStoreFunctionSetService.getOne(new LambdaQueryWrapper<StoreFunctionSet>().eq(StoreFunctionSet::getStoreManageId,storeManageId));
        if(storeFunctionSet!=null){
            resultMap.put("storeFunctionSet",storeFunctionSet);
        }else{
            resultMap.put("storeFunctionSet",null);
        }

        //礼包团控制
        iMarketingStoreGiftbagSettingService.settingView(resultMap,softModel,storeManageId);

        //店铺专区
        iMarketingStorePrefectureSetttingService.settingView(resultMap,softModel,storeManageId);

        return Result.ok(resultMap);
    }


    /**
     * 设置特色标签信息
     *
     * @param storeManageId
     * @param resultMap
     */
    public void setStorelabels(String storeManageId,Map<String,Object> resultMap){
        //获取标签信息
        List<String> storelabels= Lists.newArrayList();
        iStoreLabelRelationService.list(new LambdaQueryWrapper<StoreLabelRelation>()
                .eq(StoreLabelRelation::getStoreManageId,storeManageId)).forEach(s->{
            storelabels.add(iStoreLabelService.getById(s.getStoreLabelId()).getLabel());
        });

        resultMap.put("storelabels",storelabels);
    }


    /**
     * 获取推荐店铺列表
     * @param longitude
     * @param latitude
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getStoreManageByRecommend")
    @ResponseBody
    public Result<?> getStoreManageByRecommend( @RequestHeader(defaultValue = "") String longitude,
                                                @RequestHeader(defaultValue = "") String latitude,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("longitude",longitude);
        paramMap.put("latitude",latitude);
        paramMap.put("pattern",iStoreSettingService.getOne(new LambdaQueryWrapper<>()).getDefaultSort());
        IPage<Map<String,Object>> storeManageMapList=iStoreManageService.getStoreManageByRecommend(new Page<>(pageNo,pageSize),paramMap);
        if (storeManageMapList.getRecords().size()>0) {
            Map<String, Object> mapsMap = Maps.newHashMap();

            storeManageMapList.getRecords().forEach(s -> {
                //获取商品
                Page<Map<String, Object>> storePage = new Page<Map<String, Object>>(1, 3);
                s.put("storeGoods", iGoodStoreListService.findGoodListBySysUserId(storePage, s.get("sysUserId").toString()).getRecords());
                if (Integer.parseInt(s.get("disCount").toString()) > 0) {
                    //获取券
                    Page<Map<String, Object>> discountPage = new Page<Map<String, Object>>(1, 4);
                    s.put("storeDiscounts", iMarketingDiscountService.findMarketingDiscountBySysUserId(discountPage, s.get("sysUserId").toString()).getRecords());
                } else {
                    s.put("storeDiscounts", "");
                }
                s.put("distance", "");



                //获取标签信息
                setStorelabels(s.get("id").toString(),s);

                //设置楼层
                storeUtils.setFloor(s,s.get("id").toString());

                mapsMap.put(s.get("latitude") + "," + s.get("longitude"), s);
                //进店奖励
                s=iStoreManageService.setActivity(s,s.get("id").toString());
            });

            //位置判断
            storeUtils.setLocation(longitude,latitude,mapsMap);

        }
        return Result.ok(storeManageMapList);
    }



    /**
     * 根据不同方式获取店铺列表
     *
     * @param pattern
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findStoreManageList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findStoreManageList(@RequestParam(name = "storeName",defaultValue = "",required = false) String storeName,
                                                                 Integer pattern,
                                                                 String itemValue,
                                                                 String longitude,
                                                                 String latitude,
                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //参数验证
        if(pattern==null){
            result.error500("pattern不能为空！！！");
            return result;
        }

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("longitude",longitude);
        paramObjectMap.put("latitude",latitude);
        paramObjectMap.put("storeName",storeName);
        paramObjectMap.put("itemValue",itemValue);
        IPage<Map<String,Object>> storeManageMapList=iStoreManageService.findStoreManageList(page,paramObjectMap);
        if (storeManageMapList.getRecords().size()>0){
            Map<String,Object> mapsMap=Maps.newHashMap();

            storeManageMapList.getRecords().stream().forEach(s->{
                //获取商品
                Page<Map<String,Object>> storePage = new Page<Map<String,Object>>(1, 3);
                s.put("storeGoods",iGoodStoreListService.findGoodListBySysUserId(storePage,s.get("sysUserId").toString()).getRecords());
                if(Integer.parseInt(s.get("disCount").toString())>0) {
                    //获取券
                    Page<Map<String, Object>> discountPage = new Page<Map<String, Object>>(1, 4);
                    s.put("storeDiscounts", iMarketingDiscountService.findMarketingDiscountBySysUserId(discountPage, s.get("sysUserId").toString()).getRecords());
                }else{
                    s.put("storeDiscounts", "");
                }
                s.put("distance","");
                //获取标签信息
                setStorelabels(s.get("id").toString(),s);
                //设置楼层
                storeUtils.setFloor(s,s.get("id").toString());
                if(Double.parseDouble(s.get("latitude").toString())!=0){
                    mapsMap.put(s.get("latitude")+","+s.get("longitude"),s);
                }
                //进店奖励
                s=iStoreManageService.setActivity(s,s.get("id").toString());
            });

            //位置判断
            storeUtils.setLocation(longitude,latitude,mapsMap);
        }

        result.setResult(storeManageMapList);
        result.success("查询店铺列表成功");
        return result;
    }


    /**
     * 店铺余额充值
     * @param storeManageId
     * @param
     * @return
     */
    @RequestMapping("payBalance")
    @ResponseBody
    public Result<String> payBalance(String storeManageId,BigDecimal price,HttpServletRequest request){

        Result<String> result=new Result<>();

        if(StringUtils.isBlank(storeManageId)){
            result.error500("storeManageId不能为空！！！");
            return result;
        }

        StoreRechargeRecord storeRechargeRecord=new StoreRechargeRecord();
        storeRechargeRecord.setDelFlag("0");
        storeRechargeRecord.setStoreManageId(storeManageId);
        storeRechargeRecord.setPayType("4");
        storeRechargeRecord.setGoAndCome("0");
        storeRechargeRecord.setAmount(price);
        storeRechargeRecord.setTradeStatus("0");
        storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
        storeRechargeRecord.setOperator("系统");
        storeRechargeRecord.setBackTimes(new BigDecimal(0));
        storeRechargeRecord.setPayment("0");


        iStoreRechargeRecordService.save(storeRechargeRecord);

        //设置回调地址

        String notifyUrl=notifyUrlUtils.getNotify("balance_notifyUrl");


        //官方微信支付调起
        Map<String,String> resultMap= weixinPayUtils.payWeixinQR(request,storeRechargeRecord.getId(),storeRechargeRecord.getAmount(),notifyUrl);

        result.setResult(resultMap.get("dbpath"));

        //支付日志
        storeRechargeRecord.setPayParam(resultMap.get("params"));

        //保存支付日志
        iStoreRechargeRecordService.saveOrUpdate(storeRechargeRecord);
        result.success("生成支付二维码成功");
        return result;
    }

    @RequestMapping("findStoreSetting")
    @ResponseBody
    public Result<Map<String,Object>> findStoreSetting(){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<StoreSetting> storeSettingLambdaQueryWrapper = new LambdaQueryWrapper<StoreSetting>()
                .eq(StoreSetting::getDelFlag, "0");
        if (iStoreSettingService.count(storeSettingLambdaQueryWrapper)>0){
            StoreSetting storeSetting = iStoreSettingService.list(storeSettingLambdaQueryWrapper).get(0);
            map.put("openPublicityPic",storeSetting.getOpenPublicityPic());
            map.put("inviteFigure",storeSetting.getInviteFigure());
            map.put("coverPlan",storeSetting.getCoverPlan());
            map.put("posters",storeSetting.getPosters());
        }else {
            map.put("openPublicityPic","");
            map.put("inviteFigure","");
            map.put("coverPlan","");
            map.put("posters","");
        }
        result.success("返回店铺设置");
        result.setResult(map);
        return result;
    }

    @RequestMapping("findStoreMainType")
    @ResponseBody
    public Result<List<Map<String,Object>>> findStoreMainType(){
        Result<List<Map<String,Object>>> result = new Result<>();
        String storeMainType = iSysDictService.list(new LambdaUpdateWrapper<SysDict>()
                .eq(SysDict::getDictCode, "store_main_type")).get(0).getId();

        List<SysDictItem> list = iSysDictItemService.list(new LambdaUpdateWrapper<SysDictItem>()
                .eq(SysDictItem::getDictId, storeMainType).orderByAsc(SysDictItem::getItemValue));
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(l->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemText",l.getItemText());
            map.put("itemValue",l.getItemValue());
            maps.add(map);
        });
        result.success("返回城市生活分类");
        result.setResult(maps);
        return result;
    }
    /**
     * 根据不同方式获取店铺列表
     *
     * @param pattern
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findCityLifeStoreManageList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findCityLifeStoreManageList(@RequestParam(name = "storeName",defaultValue = "",required = false) String storeName,
                                                                 Integer pattern,
                                                                 String itemValue,
                                                                 String longitude,
                                                                 String latitude,
                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //参数验证
        if(pattern==null){
            result.error500("pattern不能为空！！！");
            return result;
        }

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("longitude",longitude);
        paramObjectMap.put("latitude",latitude);
        paramObjectMap.put("storeName",storeName);
        paramObjectMap.put("itemValue",itemValue);
        IPage<Map<String,Object>> storeManageMapList=iStoreManageService.findCityLifeStoreManageList(page,paramObjectMap);
        if (storeManageMapList.getRecords().size()>0){
            Map<String,Object> mapsMap=Maps.newHashMap();

            storeManageMapList.getRecords().stream().forEach(s->{
                //获取商品
                Page<Map<String,Object>> storePage = new Page<Map<String,Object>>(1, 3);
                s.put("storeGoods",iGoodStoreListService.findGoodListBySysUserId(storePage,s.get("sysUserId").toString()).getRecords());
                if(Integer.parseInt(s.get("disCount").toString())>0) {
                    //获取券
                    Page<Map<String, Object>> discountPage = new Page<Map<String, Object>>(1, 4);
                    s.put("storeDiscounts", iMarketingDiscountService.findMarketingDiscountBySysUserId(discountPage, s.get("sysUserId").toString()).getRecords());
                }else{
                    s.put("storeDiscounts", "");
                }
                s.put("distance","");
                //获取标签信息
                setStorelabels(s.get("id").toString(),s);
                //设置楼层
                storeUtils.setFloor(s,s.get("id").toString());
                mapsMap.put(s.get("latitude")+","+s.get("longitude"),s);
            });

            //位置判断
            storeUtils.setLocation(longitude,latitude,mapsMap);
        }

        result.setResult(storeManageMapList);
        result.success("查询店铺列表成功");
        return result;
    }

    /**
     * 城市生活商家
     *
     * @param pattern
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findCityLifeStoreManageListNew")
    @ResponseBody
    @Deprecated
    public Result<IPage<Map<String,Object>>> findCityLifeStoreManageListNew(Integer pattern,
                                                                            @RequestHeader(defaultValue = "")String longitude,
                                                                            @RequestHeader(defaultValue = "")String latitude,
                                                                         String storeTypeId,
                                                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //参数验证
        if(pattern==null){
            result.error500("pattern不能为空！！！");
            return result;
        }
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("longitude",longitude);
        paramObjectMap.put("latitude",latitude);

        if (StringUtils.isBlank(storeTypeId)){
            paramObjectMap.put("storeTypeId",storeTypeId);
            paramObjectMap.put("hasChild","");
        }else {
            StoreType storeType = iStoreTypeService.getById(storeTypeId);
            paramObjectMap.put("storeTypeId",storeTypeId);
            paramObjectMap.put("hasChild",storeType.getLevel());
        }
        log.info("storeTypeId : "+storeTypeId);
        log.info("longitude : "+longitude);
        log.info("latitude : "+latitude);
        log.info("pattern : "+pattern);
        IPage<Map<String,Object>> storeManageMapList=iStoreManageService.findCityLifeStoreManageListNew(page,paramObjectMap);
        if (storeManageMapList.getRecords().size()>0){
            Map<String,Object> mapsMap=Maps.newHashMap();
            storeManageMapList.getRecords().forEach(s->{
                //获取商品
                Page<Map<String,Object>> storePage = new Page<Map<String,Object>>(1, 3);
                s.put("storeGoods",iGoodStoreListService.findGoodListBySysUserId(storePage,s.get("sysUserId").toString()).getRecords());
                if(Integer.parseInt(s.get("disCount").toString())>0) {
                    //获取券
                    Page<Map<String, Object>> discountPage = new Page<Map<String, Object>>(1, 4);
                    s.put("storeDiscounts", iMarketingDiscountService.findMarketingDiscountBySysUserId(discountPage, s.get("sysUserId").toString()).getRecords());
                }else{
                    s.put("storeDiscounts", "");
                }
                s.put("distance","");
                //获取标签信息
                setStorelabels(s.get("id").toString(),s);
                //设置楼层
                storeUtils.setFloor(s,s.get("id").toString());
                mapsMap.put(s.get("latitude")+","+s.get("longitude"),s);
            });

            //位置判断
            storeUtils.setLocation(longitude,latitude,mapsMap);
        }

        result.setResult(storeManageMapList);
        result.success("查询店铺列表成功");
        return result;
    }

    @RequestMapping("findSysFrontSettingMap")
    @ResponseBody
    public Result<Map<String,Object>> findSysFrontSettingMap(){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> sysFrontSettingMap = iSysFrontSettingService.findSysFrontSettingMap();
        //微信付款到零钱，0：停用；1：启用；
        sysFrontSettingMap.put("wxPayInChange",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","wx_pay_in_change"));
        //微信付款到银行卡，0：停用；1：启用；
        sysFrontSettingMap.put("wxPayToBankCard",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","wx_pay_to_bank_card"));

        sysFrontSettingMap.put("payToBankCard",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","pay_to_bank_card"));

        LambdaQueryWrapper<MarketingWelfarePaymentsSetting> marketingWelfarePaymentsSettingLambdaQueryWrapper = new LambdaQueryWrapper<MarketingWelfarePaymentsSetting>()
                .eq(MarketingWelfarePaymentsSetting::getDelFlag, "0");

        MarketingWelfarePaymentsSetting marketingWelfarePaymentsSetting = iMarketingWelfarePaymentsSettingService.list(marketingWelfarePaymentsSettingLambdaQueryWrapper).get(0);
        sysFrontSettingMap.put("welfarePaymentsSettingName",marketingWelfarePaymentsSetting.getNickName());

        //首页模板样式控制：0：默认模版；1：贡客模版
        String homePageTemplate = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "home_page_template");
        sysFrontSettingMap.put("homePageTemplate",homePageTemplate);



        result.success("返回小程序前端设置成功");
        result.setResult(sysFrontSettingMap);
        return result;
    }


    /**
     * 根据标签id获取店铺列表
     *
     * @param pageNo
     * @param pageSize
     * @param storeLabelId
     * @return
     */
    @RequestMapping("getLabelStoreManage")
    @ResponseBody
    public Result<?> getLabelStoreManage(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                         @RequestParam(name = "floorName",required = false,defaultValue = "") String floorName,
                                         @RequestParam(name = "discount",required = false,defaultValue = "") String discount,
                                         @RequestHeader(defaultValue = "")String longitude,
                                         @RequestHeader(defaultValue = "")String latitude,
                                         String storeLabelId){

        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("storeLabelId",storeLabelId);
        paramMap.put("floorName",floorName);
        paramMap.put("discount",discount);

        IPage<Map<String,Object>> storeManageMapList=iStoreManageService.getLabelStoreManage(new Page<>(pageNo,pageSize),paramMap);
        if (storeManageMapList.getRecords().size()>0){
            Map<String,Object> mapsMap=Maps.newHashMap();

            storeManageMapList.getRecords().stream().forEach(s->{
                //获取商品
                Page<Map<String,Object>> storePage = new Page<Map<String,Object>>(1, 3);
                s.put("storeGoods",iGoodStoreListService.findGoodListBySysUserId(storePage,s.get("sysUserId").toString()).getRecords());
                if(Integer.parseInt(s.get("disCount").toString())>0) {
                    //获取券
                    Page<Map<String, Object>> discountPage = new Page<Map<String, Object>>(1, 4);
                    s.put("storeDiscounts", iMarketingDiscountService.findMarketingDiscountBySysUserId(discountPage, s.get("sysUserId").toString()).getRecords());
                }else{
                    s.put("storeDiscounts", "");
                }
                s.put("distance","");
                //获取标签信息
                setStorelabels(s.get("id").toString(),s);
                //设置楼层
                storeUtils.setFloor(s,s.get("id").toString());
                if(Double.parseDouble(s.get("latitude").toString())!=0){
                    mapsMap.put(s.get("latitude")+","+s.get("longitude"),s);
                }
                //进店奖励
                s=iStoreManageService.setActivity(s,s.get("id").toString());
            });

            //位置判断
            storeUtils.setLocation(longitude,latitude,mapsMap);
        }

        return Result.ok(storeManageMapList);
    }


    /**
     * 获取楼层列表
     *
     * @return
     */
    @RequestMapping("getFloorName")
    @ResponseBody
    public Result<?>  getFloorName(){
        String floorName= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","floor_name");
        return Result.ok(Arrays.asList(StringUtils.split(floorName,",")));
    }

    /**
     * 根据店铺id获取店铺详情
     *
     * @return
     */
    @RequestMapping("getStoreManageById")
    @ResponseBody
    public Result<?> getStoreManageById(String id){
        if(StringUtils.isBlank(id)){
            return Result.error("店铺id不能为空");
        }
        StoreManage storeManage=iStoreManageService.getById(id);
        if(storeManage==null){
            return Result.error("店铺不存在");
        }
        Map<String,Object> resultMap=Maps.newHashMap();
        resultMap.put("id",storeManage.getId());
        if (storeManage.getSubStoreName() == null) {
            resultMap.put("storeName", storeManage.getStoreName());
        } else {
            resultMap.put("storeName", storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
        }
        resultMap.put("logoAddr",storeManage.getLogoAddr());


        //获取微信的appid
        String wechatDeveloperID= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","wechatDeveloperID");
        resultMap.put("appid",wechatDeveloperID);

        //收银台设置
        StoreCashierSetting storeCashierSetting=iStoreCashierSettingService.getStoreCashierSetting(id);

        if(storeCashierSetting==null){
            resultMap.put("isCashier",0);
        }else{
            resultMap.put("isCashier",1);
            resultMap.put("storeCashierSetting",storeCashierSetting);
        }
        return Result.ok(resultMap);
    }

}
