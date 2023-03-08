package org.jeecg.modules.index.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.service.IStoreSeriesManageService;
import org.jeecg.modules.system.entity.SysFrontSetting;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * 首页请求需要的控制器
 */

@Controller
@RequestMapping("front/index")
@Slf4j
public class FrontIndexController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMarketingAdvertisingService iMarketingAdvertisingService;


    @Autowired
    private IMarketingGiftBagService iMarketingGiftBagService;

    @Autowired
    private IGoodListService iGoodListService;


    @Autowired
    private IMarketingFreeSessionService iMarketingFreeSessionService;

    @Autowired
    private IMarketingFreeBaseSettingService iMarketingFreeBaseSettingService;

    @Autowired
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IMarketingActivityRegistrationBaseSettingService iMarketingActivityRegistrationBaseSettingService;

    @Autowired
    private IMarketingDiscountCertificateSettingService iMarketingDiscountCertificateSettingService;

    @Autowired
    private IMarketingPayIntegralSettingService iMarketingPayIntegralSettingService;

    @Autowired
    private IMarketingZoneGroupBaseSettingService iMarketingZoneGroupBaseSettingService;

    @Autowired
    private IMarketingBusinessGiftBaseSettingService iMarketingBusinessGiftBaseSettingService;

    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;
    @Autowired
    private IMarketingLiveBaseSettingService iMarketingLiveBaseSettingService;

    @Autowired
    private IMarketingLeagueSettingService iMarketingLeagueSettingService;

    @Autowired
    private IStoreSeriesManageService iStoreSeriesManageService;

    @Autowired
    private ISysFrontSettingService iSysFrontSettingService;

    /**
     * 商城首页
     *
     * @param sysUserId
     * @return
     */
    @RequestMapping("indexNew")
    @ResponseBody
    public Result<?> indexNew(@RequestHeader(defaultValue = "") String sysUserId,
                                             @RequestAttribute(value = "memberId",required = false) String memberId,
                                             @RequestHeader("softModel") String softModel) {

        Map<String, Object> objectMap = Maps.newHashMap();
        //用户登录判断
        Map<String, Object> memberMap = Maps.newHashMap();
        objectMap.put("member", memberMap);
        Map<String, Object> paramObjectMap = Maps.newHashMap();
        paramObjectMap.put("pattern", 0);
        log.info("进入首页sysUserId : " + sysUserId);
        log.info("进入首页softModel："+softModel);
        //首页轮播广告
        objectMap.put("marketingAdvertisings", iMarketingAdvertisingService.findMarketingAdvertisingByAdLocation(paramObjectMap));
        //获取用户需要的信息
        if (StringUtils.isNoneBlank(memberId)) {
            MemberList memberList = iMemberListService.getById(memberId);
            memberMap.put("id", memberList.getId());
            memberMap.put("memberType", memberList.getMemberType());
        }

        //礼包的显示和隐藏
        QueryWrapper<MarketingGiftBag> marketingGiftBagQueryWrapper = new QueryWrapper<>();
        marketingGiftBagQueryWrapper.eq("status", "1");
        marketingGiftBagQueryWrapper.le("start_time", new Date());
        marketingGiftBagQueryWrapper.ge("end_time", new Date());
        marketingGiftBagQueryWrapper.gt("repertory", 0);
        long giftCount = iMarketingGiftBagService.count(marketingGiftBagQueryWrapper);
        if (giftCount > 0) {
            objectMap.put("isViewGift", "1");
        } else {
            objectMap.put("isViewGift", "0");
        }

        if( objectMap.get("isViewGift").toString().equals("0")) {

            //创业礼包
            MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting = iMarketingBusinessGiftBaseSettingService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftBaseSetting>().eq(MarketingBusinessGiftBaseSetting::getStatus, "1"));

            if (marketingBusinessGiftBaseSetting == null) {
                objectMap.put("isViewGift", "0");
            } else {
                log.info("创业礼包端控制：softModel=" + softModel);
                if (marketingBusinessGiftBaseSetting.getPointsDisplay().equals("0")) {
                    objectMap.put("isViewGift", "1");
                } else
                    //小程序
                    if (softModel.equals("0") && marketingBusinessGiftBaseSetting.getPointsDisplay().equals("1")) {
                        objectMap.put("isViewGift", "1");
                    } else if ((softModel.equals("1") || softModel.equals("2")) && marketingBusinessGiftBaseSetting.getPointsDisplay().equals("2")) {
                        objectMap.put("isViewGift", "1");
                    } else {
                        objectMap.put("isViewGift", "0");
                    }
            }
        }


        //搜索商品显隐藏

        QueryWrapper<GoodList> searChGoodListQueryWrapper = new QueryWrapper<>();
        searChGoodListQueryWrapper.eq("status", "1");
        searChGoodListQueryWrapper.eq("frame_status", "1");
        long searChOptionGoodCount = iGoodListService.count(searChGoodListQueryWrapper);
        if (searChOptionGoodCount > 0) {
            objectMap.put("isViewsearChOptionGood", "1");
        } else {
            objectMap.put("isViewsearChOptionGood", "0");
        }

        //首页模板样式控制：0：默认模版；1：贡客模版
        String homePageTemplate = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "home_page_template");
        objectMap.put("homePageTemplate",homePageTemplate);
        if(homePageTemplate.equals("1")){
            objectMap.put("isViewCityLife", "1");
        }else{
            objectMap.put("isViewCityLife", "0");
        }

        //活动中心
        MarketingActivityRegistrationBaseSetting marketingActivityRegistrationBaseSetting=iMarketingActivityRegistrationBaseSettingService.getOne(new LambdaQueryWrapper<MarketingActivityRegistrationBaseSetting>()
                .eq(MarketingActivityRegistrationBaseSetting::getStatus,"1"));
        Map<String,Object> marketingActivityRegistrationBaseSettingMap=Maps.newHashMap();
        if(marketingActivityRegistrationBaseSetting==null){
            marketingActivityRegistrationBaseSettingMap.put("isViewMarketingActivity", "0");
        }else{
            log.info("线下活动分端控制：softModel=" + softModel);
            marketingActivityRegistrationBaseSettingMap.put("anotherName",marketingActivityRegistrationBaseSetting.getAnotherName());
            if (marketingActivityRegistrationBaseSetting.getPointsDisplay().equals("0")) {
                marketingActivityRegistrationBaseSettingMap.put("isViewMarketingActivity", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingActivityRegistrationBaseSetting.getPointsDisplay().equals("1")) {
                    marketingActivityRegistrationBaseSettingMap.put("isViewMarketingActivity", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingActivityRegistrationBaseSetting.getPointsDisplay().equals("2")) {
                    marketingActivityRegistrationBaseSettingMap.put("isViewMarketingActivity", "1");
                } else {
                    marketingActivityRegistrationBaseSettingMap.put("isViewMarketingActivity", "0");
                }
            //显示中奖拼团
            if (marketingActivityRegistrationBaseSettingMap.get("isViewMarketingActivity").toString().equals("1")) {
                marketingActivityRegistrationBaseSettingMap.put("surfacePlot", marketingActivityRegistrationBaseSetting.getSurfacePlot());
            }
        }
        objectMap.put("marketingActivityRegistrationBaseSettingMap",marketingActivityRegistrationBaseSettingMap);

        //专区数据信息
        objectMap.put("marketingPrefecture",iMarketingPrefectureService.findPrefectureIndex(softModel));

        //免单专区的判断
        MarketingFreeBaseSetting marketingFreeBaseSetting=iMarketingFreeBaseSettingService.getOne(new LambdaQueryWrapper<MarketingFreeBaseSetting>().eq(MarketingFreeBaseSetting::getStatus,"1"));
        Map<String,Object> marketingFreeBaseSettingMap=Maps.newHashMap();
        if(marketingFreeBaseSetting==null){
            marketingFreeBaseSettingMap.put("isViewMarketingFree", "0");
        }else{
            log.info("免单专区分端控制：softModel=" + softModel);
            if (marketingFreeBaseSetting.getPointsDisplay().equals("0")) {
                marketingFreeBaseSettingMap.put("isViewMarketingFree", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingFreeBaseSetting.getPointsDisplay().equals("1")) {
                    marketingFreeBaseSettingMap.put("isViewMarketingFree", "1");
                } else if ((softModel.equals("1") || softModel.equals("2")) && marketingFreeBaseSetting.getPointsDisplay().equals("2")) {
                    marketingFreeBaseSettingMap.put("isViewMarketingFree", "1");
                } else {
                    marketingFreeBaseSettingMap.put("isViewMarketingFree", "0");
                }
            //显示中奖拼团
            if (marketingFreeBaseSettingMap.get("isViewMarketingFree").toString().equals("1")) {
                marketingFreeBaseSettingMap.put("surfacePlot",marketingFreeBaseSetting.getSurfacePlot());
                Map<String,Object> currentSessionMap= iMarketingFreeSessionService.selectCurrentSession();
                if(currentSessionMap!=null){
                    marketingFreeBaseSettingMap.put("isPreviewMarketingFree", "1");
                    marketingFreeBaseSettingMap.put("currentSessionMap",currentSessionMap);
                }else{
                    marketingFreeBaseSettingMap.put("isPreviewMarketingFree", "0");
                }

            }
        }
        objectMap.put("marketingFreeBaseSettingMap",marketingFreeBaseSettingMap);


        //中奖拼团
        MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingGroupBaseSetting>().eq(MarketingGroupBaseSetting::getStatus,"1"));
        Map<String,Object> marketingGroupBaseSettingMap=Maps.newHashMap();
        //基础设置信息不能为空
        if(marketingGroupBaseSetting==null){
            marketingGroupBaseSettingMap.put("isViewMarketingGroup", "0");
        }else {
            log.info("中奖拼团分端控制：softModel=" + softModel);
            if (marketingGroupBaseSetting.getPointsDisplay().equals("0")) {
                marketingGroupBaseSettingMap.put("isViewMarketingGroup", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingGroupBaseSetting.getPointsDisplay().equals("1")) {
                    marketingGroupBaseSettingMap.put("isViewMarketingGroup", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingGroupBaseSetting.getPointsDisplay().equals("2")) {
                    marketingGroupBaseSettingMap.put("isViewMarketingGroup", "1");
                } else {
                    marketingGroupBaseSettingMap.put("isViewMarketingGroup", "0");
                }
            //显示中奖拼团
            if (marketingGroupBaseSettingMap.get("isViewMarketingGroup").toString().equals("1")) {
                marketingGroupBaseSettingMap.put("marketingGroupBaseSettingSurfacePlot", marketingGroupBaseSetting.getSurfacePlot());
            }
        }
        objectMap.put("marketingGroupBaseSettingMap",marketingGroupBaseSettingMap);

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
        objectMap.put("marketingZoneGroupBaseSettingMap",marketingZoneGroupBaseSettingMap);

        //直播的显示
        Map<String, Object> marketingLiveStreamingMap=Maps.newHashMap();
        MarketingLiveBaseSetting marketingLiveBaseSetting = iMarketingLiveBaseSettingService.getOne(new LambdaQueryWrapper<MarketingLiveBaseSetting>()
                .eq(MarketingLiveBaseSetting::getDelFlag, "0")
                .last("limit 1")
        );
        //判断直播显隐
        if (marketingLiveBaseSetting !=null){
            if (marketingLiveBaseSetting.getStatus().equals("0")){
                marketingLiveStreamingMap.put("isViewLive","0");
            }else {
                if (marketingLiveBaseSetting.getPointsDisplay().equals("0")){
                    marketingLiveStreamingMap.put("isViewLive","1");
                }else if (marketingLiveBaseSetting.getPointsDisplay().equals("1")){
                    marketingLiveStreamingMap.put("isViewLive",softModel.equals("0")?"1":"0");
                }else {
                    marketingLiveStreamingMap.put("isViewLive",softModel.equals("1")||softModel.equals("2")?"1":"0");
                }
            }
            marketingLiveStreamingMap.put("LiveAnotherName",marketingLiveBaseSetting.getAnotherName());
            marketingLiveStreamingMap.put("LiveIndexAddress",marketingLiveBaseSetting.getIndexAddress());
        }else {
            marketingLiveStreamingMap.put("isViewLive","0");
        }
        objectMap.put("marketingLiveStreamingMap",marketingLiveStreamingMap);

        //单品抢购
        iMarketingRushBaseSettingService.settingView(objectMap,softModel);

        //券中心
        MarketingDiscountCertificateSetting marketingDiscountCertificateSetting=iMarketingDiscountCertificateSettingService.getOne(new LambdaQueryWrapper<>());
        Map<String,Object> marketingDiscountCertificateSettingMap=Maps.newHashMap();
        if(marketingDiscountCertificateSetting==null){
            marketingDiscountCertificateSettingMap.put("isViewmarketingDiscountCertificate", "0");
        }else{
            log.info("线下活动分端控制：softModel=" + softModel);
            if (marketingDiscountCertificateSetting.getClientSideShow().equals("0")) {
                marketingDiscountCertificateSettingMap.put("isViewmarketingDiscountCertificate", "1");
            } else
                //小程序
                if (softModel.equals("0") && marketingDiscountCertificateSetting.getClientSideShow().equals("2")) {
                    marketingDiscountCertificateSettingMap.put("isViewmarketingDiscountCertificate", "1");
                } else if ((softModel.equals("1") || softModel.equals("1")) && marketingDiscountCertificateSetting.getClientSideShow().equals("1")) {
                    marketingDiscountCertificateSettingMap.put("isViewmarketingDiscountCertificate", "1");
                } else {
                    marketingDiscountCertificateSettingMap.put("isViewmarketingDiscountCertificate", "0");
                }
            //显示中奖拼团
            if (marketingDiscountCertificateSettingMap.get("isViewmarketingDiscountCertificate").toString().equals("1")) {
                marketingDiscountCertificateSettingMap.put("ticketSurfacePlot", marketingDiscountCertificateSetting.getTicketSurfacePlot());
            }
        }
        objectMap.put("marketingDiscountCertificateSettingMap",marketingDiscountCertificateSettingMap);


        //支付即积分
        MarketingPayIntegralSetting marketingPayIntegralSetting=iMarketingPayIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingPayIntegralSetting>()
                .eq(MarketingPayIntegralSetting::getStatus,"1"));
        Map<String,Object> marketingPayIntegralSettingMap=Maps.newHashMap();
        if(marketingPayIntegralSetting==null){
            marketingPayIntegralSettingMap.put("isViewmarketingPayIntegralSetting", "0");
        }else{
            if(softModel.equals("0")){
                marketingPayIntegralSettingMap.put("isViewmarketingPayIntegralSetting", "1");
                marketingPayIntegralSettingMap.put("surfacePlot",marketingPayIntegralSetting.getSurfacePlot());
            }else{
                marketingPayIntegralSettingMap.put("isViewmarketingPayIntegralSetting", "0");
            }
        }
        objectMap.put("marketingPayIntegralSettingMap",marketingPayIntegralSettingMap);

        //加盟专区设置
        iMarketingLeagueSettingService.settingMarketingLeagueView(objectMap,softModel);


        //系列数据
        iStoreSeriesManageService.settingStoreSeriesManageByparentIdIndex(objectMap);

        //底部数据
        SysFrontSetting sysFrontSetting=iSysFrontSettingService.getOne(new LambdaQueryWrapper<>());
        objectMap.put("frontLogo",sysFrontSetting.getFrontLogo());
        objectMap.put("indexBottomRecommend",sysFrontSetting.getIndexBottomRecommend());
        return Result.ok(objectMap);
    }
}
