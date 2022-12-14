package org.jeecg.modules.system.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.alliance.service.IAllianceSettingService;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.store.entity.StoreFunctionSet;
import org.jeecg.modules.store.entity.StoreSetting;
import org.jeecg.modules.store.service.IStoreFunctionSetService;
import org.jeecg.modules.store.service.IStoreSettingService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@RequestMapping("front/sysFrontSetting")
@Controller
@Slf4j
public class FrontSysFrontSetting {
    @Autowired
    private ISysFrontSettingService iSysFrontSettingService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;
    @Autowired
    private IStoreSettingService iStoreSettingService;
    @Autowired
    private IMarketingIntegralTaskBaseSettingService iMarketingIntegralTaskBaseSettingService;
    @Autowired
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;


    @Autowired
    private IMarketingCertificateSeckillBaseSettingService iMarketingCertificateSeckillBaseSettingService;


    @Autowired
    private IMarketingActivityRegistrationBaseSettingService iMarketingActivityRegistrationBaseSettingService;


    @Autowired
    private IMarketingStoreGiftCardBaseSettingService iMarketingStoreGiftCardBaseSettingService;

    @Autowired
    private IMarketingGroupIntegralBaseSettingService iMarketingGroupIntegralBaseSettingService;

    @Autowired
    private IMarketingZoneGroupBaseSettingService iMarketingZoneGroupBaseSettingService;

    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;

    @Autowired
    private IMarketingLiveBaseSettingService iMarketingLiveBaseSettingService;

    @Autowired
    private IMarketingIntegralSettingService iMarketingIntegralSettingService;

    @Autowired
    private IMarketingLeagueSettingService iMarketingLeagueSettingService;

    @Autowired
    private IAllianceSettingService iAllianceSettingService;

    @Autowired
    private IStoreFunctionSetService iStoreFunctionSetService;

    /**
     * 小程序控制前端返显接口
     * @param softModel
     * @return
     */
    @RequestMapping("findSysFrontSettingMap")
    @ResponseBody
    public Result<Map<String,Object>> findSysFrontSettingMap(@RequestHeader(defaultValue = "") String softModel){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> sysFrontSettingMap = iSysFrontSettingService.findSysFrontSettingMap();

        if (softModel.equals("0")){
            sysFrontSettingMap.put("goodLabel",sysFrontSettingMap.get("goodLabelSmallsoft"));
        }else {
            sysFrontSettingMap.put("goodLabel",sysFrontSettingMap.get("goodLabelApp"));
        }
        //微信付款到零钱，0：停用；1：启用；
        sysFrontSettingMap.put("wxPayInChange",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","wx_pay_in_change"));
        //微信付款到银行卡，0：停用；1：启用；
        sysFrontSettingMap.put("wxPayToBankCard",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","wx_pay_to_bank_card"));

        sysFrontSettingMap.put("payToBankCard",iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","pay_to_bank_card"));

        LambdaQueryWrapper<MarketingWelfarePaymentsSetting> marketingWelfarePaymentsSettingLambdaQueryWrapper = new LambdaQueryWrapper<MarketingWelfarePaymentsSetting>()
                .eq(MarketingWelfarePaymentsSetting::getDelFlag, "0");

        MarketingWelfarePaymentsSetting marketingWelfarePaymentsSetting = iMarketingWelfarePaymentsSettingService.list(marketingWelfarePaymentsSettingLambdaQueryWrapper).get(0);
        sysFrontSettingMap.put("welfarePaymentsSettingName",marketingWelfarePaymentsSetting.getNickName());
        sysFrontSettingMap.put("integralValue",marketingWelfarePaymentsSetting.getIntegralValue());

        //首页模板样式控制：0：默认模版；1：贡客模版
        String homePageTemplate = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "home_page_template");
        sysFrontSettingMap.put("homePageTemplate",homePageTemplate);

        //获取店铺设置
        StoreSetting storeSetting = iStoreSettingService.getOne(new LambdaQueryWrapper<>());
        if(storeSetting!=null) {
            sysFrontSettingMap.put("storeCut", storeSetting.getStoreCut());
        }

        //版本号
        String smallsoftShopVersion = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "smallsoft_shop_version");
        sysFrontSettingMap.put("smallsoftShopVersion",smallsoftShopVersion);

        String serviceTel = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "service_tel");
        sysFrontSettingMap.put("serviceTel",serviceTel);

        //加入客服信息
        String onlineServiceType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "online_service_type");
        if(StringUtils.isNotBlank(onlineServiceType)){
            String onlineServiceUrl = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "online_service_url");
            sysFrontSettingMap.put("onlineServiceUrl",onlineServiceUrl);
            sysFrontSettingMap.put("onlineServiceType",onlineServiceType);
        }
        String fxzxDownload = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "fxzx_download");
        sysFrontSettingMap.put("fxzxDownload",fxzxDownload);

        String smallsoftFxzxDownload = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "smallsoft_fxzx_download");
        sysFrontSettingMap.put("smallsoftFxzxDownload",smallsoftFxzxDownload);


        String smallsoftGrzxDownload = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "smallsoft_grzx_download");
        sysFrontSettingMap.put("smallsoftGrzxDownload",smallsoftGrzxDownload);


        String gameImgUrl = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "game_img_url"),"-1");
        sysFrontSettingMap.put("gameImgUrl",gameImgUrl);


        String appShopVersion  = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "app_shop_version");
        sysFrontSettingMap.put("appShopVersion",appShopVersion);


        String shareControl  = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "share_control");
        sysFrontSettingMap.put("shareControl",shareControl);

        String changeCityState  = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "change_city_state");
        sysFrontSettingMap.put("changeCityState",changeCityState);

        String floorState  = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "floor_state");
        sysFrontSettingMap.put("floorState",floorState);



        //中奖拼团
        MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingGroupBaseSetting>().eq(MarketingGroupBaseSetting::getStatus,"1"));
        //基础设置信息不能为空
        if(marketingGroupBaseSetting==null){
            sysFrontSettingMap.put("isViewMarketingGroup", "0");
        }else {
            log.info("中奖拼团分端控制：softModel=" + softModel);
            if (marketingGroupBaseSetting.getPointsDisplay().equals("0")) {
                sysFrontSettingMap.put("isViewMarketingGroup", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingGroupBaseSetting.getPointsDisplay().equals("1")) {
                    sysFrontSettingMap.put("isViewMarketingGroup", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingGroupBaseSetting.getPointsDisplay().equals("2")) {
                    sysFrontSettingMap.put("isViewMarketingGroup", "1");
                } else {
                    sysFrontSettingMap.put("isViewMarketingGroup", "0");
                }
        }


        //活动中心
        MarketingActivityRegistrationBaseSetting marketingActivityRegistrationBaseSetting=iMarketingActivityRegistrationBaseSettingService.getOne(new LambdaQueryWrapper<MarketingActivityRegistrationBaseSetting>()
                .eq(MarketingActivityRegistrationBaseSetting::getStatus,"1"));
        if(marketingActivityRegistrationBaseSetting==null){
            sysFrontSettingMap.put("isViewMarketingActivity", "0");
        }else{
            log.info("线下活动分端控制：softModel=" + softModel);
            sysFrontSettingMap.put("anotherName",marketingActivityRegistrationBaseSetting.getAnotherName());
            if (marketingActivityRegistrationBaseSetting.getPointsDisplay().equals("0")) {
                sysFrontSettingMap.put("isViewMarketingActivity", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingActivityRegistrationBaseSetting.getPointsDisplay().equals("1")) {
                    sysFrontSettingMap.put("isViewMarketingActivity", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingActivityRegistrationBaseSetting.getPointsDisplay().equals("2")) {
                    sysFrontSettingMap.put("isViewMarketingActivity", "1");
                } else {
                    sysFrontSettingMap.put("isViewMarketingActivity", "0");
                }
        }

        //拼好券
        MarketingCertificateSeckillBaseSetting marketingCertificateSeckillBaseSetting=iMarketingCertificateSeckillBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateSeckillBaseSetting>());

        if(marketingCertificateSeckillBaseSetting==null){
            sysFrontSettingMap.put("isMarketingCertificateSeckillBase", "0");
        }else{
            log.info("线下活动分端控制：softModel=" + softModel);
            sysFrontSettingMap.put("anotherName",marketingCertificateSeckillBaseSetting.getAnotherName());
            if (marketingCertificateSeckillBaseSetting.getPointsDisplay().equals("0")) {
                sysFrontSettingMap.put("isMarketingCertificateSeckillBase", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingCertificateSeckillBaseSetting.getPointsDisplay().equals("1")) {
                    sysFrontSettingMap.put("isMarketingCertificateSeckillBase", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingCertificateSeckillBaseSetting.getPointsDisplay().equals("2")) {
                    sysFrontSettingMap.put("isMarketingCertificateSeckillBase", "1");
                } else {
                    sysFrontSettingMap.put("isMarketingCertificateSeckillBase", "0");
                }
        }


        //专区团
        MarketingZoneGroupBaseSetting marketingZoneGroupBaseSetting=iMarketingZoneGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupBaseSetting>()
                .eq(MarketingZoneGroupBaseSetting::getStatus,"1"));
        Map<String,Object> marketingZoneGroupBaseSettingMap=Maps.newHashMap();
        //基础设置信息不能为空
        if(marketingZoneGroupBaseSetting==null){
            marketingZoneGroupBaseSettingMap.put("isViewMarketingZoneGroupBaseSetting", "0");
        }else {
            log.info("专区团分端控制：softModel=" + softModel);
            if (marketingZoneGroupBaseSetting.getPointsDisplay().equals("0")) {
                marketingZoneGroupBaseSettingMap.put("isViewMarketingZoneGroupBaseSetting", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingZoneGroupBaseSetting.getPointsDisplay().equals("1")) {
                    marketingZoneGroupBaseSettingMap.put("isViewMarketingZoneGroupBaseSetting", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingZoneGroupBaseSetting.getPointsDisplay().equals("2")) {
                    marketingZoneGroupBaseSettingMap.put("isViewMarketingZoneGroupBaseSetting", "1");
                } else {
                    marketingZoneGroupBaseSettingMap.put("isViewMarketingZoneGroupBaseSetting", "0");
                }
            //显示中奖拼团
            if (marketingZoneGroupBaseSettingMap.get("isViewMarketingZoneGroupBaseSetting").toString().equals("1")) {
                marketingZoneGroupBaseSettingMap.put("surfacePlot", marketingZoneGroupBaseSetting.getSurfacePlot());
            }
        }
        sysFrontSettingMap.put("marketingZoneGroupBaseSettingMap",marketingZoneGroupBaseSettingMap);


        //抢购
        MarketingRushBaseSetting  marketingRushBaseSetting=iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getStatus,"1"));
        Map<String,Object> marketingRushBaseSettingMap=Maps.newHashMap();
        //基础设置信息不能为空
        if(marketingRushBaseSetting==null){
            marketingRushBaseSettingMap.put("isViewMarketingRushBaseSetting", "0");
        }else {
            log.info("专区团分端控制：softModel=" + softModel);
            if (marketingRushBaseSetting.getPointsDisplay().equals("0")) {
                marketingRushBaseSettingMap.put("isViewMarketingRushBaseSetting", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingRushBaseSetting.getPointsDisplay().equals("1")) {
                    marketingRushBaseSettingMap.put("isViewMarketingRushBaseSetting", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingRushBaseSetting.getPointsDisplay().equals("2")) {
                    marketingRushBaseSettingMap.put("isViewMarketingRushBaseSetting", "1");
                } else {
                    marketingRushBaseSettingMap.put("isViewMarketingRushBaseSetting", "0");
                }
            //显示中奖拼团
            if (marketingRushBaseSettingMap.get("isViewMarketingRushBaseSetting").toString().equals("1")) {
                marketingRushBaseSettingMap.put("indexAddress", marketingRushBaseSetting.getIndexAddress());
                marketingRushBaseSettingMap.put("indexBigAddress", marketingRushBaseSetting.getIndexBigAddress());
            }
        }
        sysFrontSettingMap.put("marketingRushBaseSettingMap",marketingRushBaseSettingMap);

        //礼品卡判断
        MarketingStoreGiftCardBaseSetting marketingStoreGiftCardBaseSetting=iMarketingStoreGiftCardBaseSettingService.getOne(new LambdaQueryWrapper<MarketingStoreGiftCardBaseSetting>().eq(MarketingStoreGiftCardBaseSetting::getStatus,"1"));
        Map<String,Object> marketingStoreGiftCardBaseSettingMap= Maps.newHashMap();
        if(marketingStoreGiftCardBaseSetting==null){
            marketingStoreGiftCardBaseSettingMap.put("isViewMarketingStoreGiftCardBaseSettingMap","0");
        }else {
            log.info("礼品卡分端控制：softModel=" + softModel);
            if (marketingStoreGiftCardBaseSetting.getPointsDisplay().equals("0")) {
                marketingStoreGiftCardBaseSettingMap.put("isViewMarketingStoreGiftCardBaseSettingMap", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingStoreGiftCardBaseSetting.getPointsDisplay().equals("1")) {
                    marketingStoreGiftCardBaseSettingMap.put("isViewMarketingStoreGiftCardBaseSettingMap", "1");
                } else if ((softModel.equals("1") || softModel.equals("2")) && marketingStoreGiftCardBaseSetting.getPointsDisplay().equals("2")) {
                    marketingStoreGiftCardBaseSettingMap.put("isViewMarketingStoreGiftCardBaseSettingMap", "1");
                } else {
                    marketingStoreGiftCardBaseSettingMap.put("isViewMarketingStoreGiftCardBaseSettingMap", "0");
                }
            marketingStoreGiftCardBaseSettingMap.put("coverPlan",marketingStoreGiftCardBaseSetting.getCoverPlan());
            marketingStoreGiftCardBaseSettingMap.put("backgroundImage",marketingStoreGiftCardBaseSetting.getBackgroundImage());
        }

        sysFrontSettingMap.put("marketingThirdIntegralSettingMap",marketingStoreGiftCardBaseSettingMap);
        sysFrontSettingMap.put("transactionPasswordState",iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "transaction_password_state"));
        /**
         * 拼购显隐
         */
        List<MarketingGroupIntegralBaseSetting> marketingGroupIntegralBaseSettings = iMarketingGroupIntegralBaseSettingService.list(new LambdaQueryWrapper<MarketingGroupIntegralBaseSetting>()
                .eq(MarketingGroupIntegralBaseSetting::getDelFlag, "0"));
        if (marketingGroupIntegralBaseSettings.size()>0){
            MarketingGroupIntegralBaseSetting marketingGroupIntegralBaseSetting = marketingGroupIntegralBaseSettings.get(0);
            if (marketingGroupIntegralBaseSetting.getPointsDisplay().equals("0")){
                sysFrontSettingMap.put("marketingGroupIntegralBaseSettingView",marketingGroupIntegralBaseSetting.getStatus());
            }else {
                if (marketingGroupIntegralBaseSetting.getPointsDisplay().equals("1")&&softModel.equals("0")){
                    sysFrontSettingMap.put("marketingGroupIntegralBaseSettingView",marketingGroupIntegralBaseSetting.getStatus());
                }else {
                    sysFrontSettingMap.put("marketingGroupIntegralBaseSettingView","0");
                }
                if (marketingGroupIntegralBaseSetting.getPointsDisplay().equals("2")){
                    if (softModel.equals("2")&&softModel.equals("1")){
                        sysFrontSettingMap.put("marketingGroupIntegralBaseSettingView",marketingGroupIntegralBaseSetting.getStatus());
                    }else {
                        sysFrontSettingMap.put("marketingGroupIntegralBaseSettingView","0");
                    }
                }
            }
        }else {
            sysFrontSettingMap.put("marketingGroupIntegralBaseSettingView","0");
        }
        //获取任务设置
        MarketingIntegralTaskBaseSetting marketingIntegralTaskBaseSetting = iMarketingIntegralTaskBaseSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralTaskBaseSetting>()
                .eq(MarketingIntegralTaskBaseSetting::getDelFlag, "0")
                .last("limit 1")
        );
        //判断任务入口显隐
        if (marketingIntegralTaskBaseSetting !=null&&!marketingIntegralTaskBaseSetting.getPointsDisplay().equals("3")){
            if (marketingIntegralTaskBaseSetting.getStatus().equals("0")){
                sysFrontSettingMap.put("isViewMarketingIntegralTask","0");
            }else {
                if (marketingIntegralTaskBaseSetting.getPointsDisplay().equals("0")){
                    sysFrontSettingMap.put("isViewMarketingIntegralTask","1");
                }else if (marketingIntegralTaskBaseSetting.getPointsDisplay().equals("1")){
                    sysFrontSettingMap.put("isViewMarketingIntegralTask",softModel.equals("0")?"1":"0");
                }else {
                    sysFrontSettingMap.put("isViewMarketingIntegralTask",softModel.equals("1")||softModel.equals("2")?"1":"0");
                }
            }
        }else {
            sysFrontSettingMap.put("isViewMarketingIntegralTask","0");
        }
        MarketingLiveBaseSetting marketingLiveBaseSetting = iMarketingLiveBaseSettingService.getOne(new LambdaQueryWrapper<MarketingLiveBaseSetting>()
                .eq(MarketingLiveBaseSetting::getDelFlag, "0")
                .last("limit 1")
        );
        //判断直播显隐
        if (marketingLiveBaseSetting !=null){
            if (marketingLiveBaseSetting.getStatus().equals("0")){
                sysFrontSettingMap.put("isViewmarketingLiveBaseSetting","0");
            }else {
                if (marketingLiveBaseSetting.getPointsDisplay().equals("0")){
                    sysFrontSettingMap.put("isViewmarketingLiveBaseSetting","1");
                }else if (marketingLiveBaseSetting.getPointsDisplay().equals("1")){
                    sysFrontSettingMap.put("isViewmarketingLiveBaseSetting",softModel.equals("0")?"1":"0");
                }else {
                    sysFrontSettingMap.put("isViewmarketingLiveBaseSetting",softModel.equals("1")||softModel.equals("2")?"1":"0");
                }
            }
            sysFrontSettingMap.put("LiveAnotherName",marketingLiveBaseSetting.getAnotherName());
            sysFrontSettingMap.put("LiveIndexAddress",marketingLiveBaseSetting.getIndexAddress());
        }else {
            sysFrontSettingMap.put("isViewmarketingLiveBaseSetting","0");
        }

        //免费积分
        sysFrontSettingMap.putAll(iMarketingIntegralSettingService.getgetMarketingIntegralSettingMap(softModel));

        //加盟专区设置
        iMarketingLeagueSettingService.settingMarketingLeagueView(sysFrontSettingMap,softModel);

        //加盟商设置
        iAllianceSettingService.settingAllianceSettingView(sysFrontSettingMap);

        //预订记录的权限判断
        sysFrontSettingMap.put("roomsManagement",iStoreFunctionSetService.count(new LambdaQueryWrapper<StoreFunctionSet>().eq(StoreFunctionSet::getRoomsManagement,"1")));

        result.success("返回小程序前端设置成功");
        result.setResult(sysFrontSettingMap);
        return result;
    }

}
