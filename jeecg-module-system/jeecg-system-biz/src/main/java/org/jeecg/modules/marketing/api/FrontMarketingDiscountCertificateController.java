package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillActivityList;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingDiscountCertificateSetting;
import org.jeecg.modules.marketing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 券中心
 */
@RequestMapping("front/marketingDiscountCertificate")
@Controller
public class FrontMarketingDiscountCertificateController {
    @Autowired
    private IMarketingDiscountCertificateSettingService iMarketingDiscountCertificateSettingService;
    @Autowired
    private IMarketingCertificateSeckillBaseSettingService iMarketingCertificateSeckillBaseSettingService;
    @Autowired
    private IMarketingCertificateSeckillActivityListService iMarketingCertificateSeckillActivityListService;
    @Autowired
    private IMarketingCertificateGroupBaseSettingService iMarketingCertificateGroupBaseSettingService;
    @Autowired
    private IMarketingCertificateGroupListService iMarketingCertificateGroupListService;
    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;
    @Autowired
    private IMarketingCertificateSeckillListService iMarketingCertificateSeckillListService;
    /**
     * 券中心
     * @param softModel 0:小程序；1：android；2：ios；3：H5
     * @return
     */
    @RequestMapping("findMarketingDiscountCertificate")
    @ResponseBody
    public Result<?> findMarketingDiscountCertificate(@RequestHeader(defaultValue = "") String softModel){

        //获取券中心设置
        MarketingDiscountCertificateSetting marketingDiscountCertificateSetting = iMarketingDiscountCertificateSettingService.getOne(new LambdaQueryWrapper<MarketingDiscountCertificateSetting>()
                .eq(MarketingDiscountCertificateSetting::getDelFlag, "0"));
        if (marketingDiscountCertificateSetting.getClientSideShow().equals("0")){
            return Result.ok(returnFindMarketingDiscountCertificate(marketingDiscountCertificateSetting,softModel));
        }else if (marketingDiscountCertificateSetting.getClientSideShow().equals("1")){
            if (softModel.equals("1")||softModel.equals("2")){
                return Result.ok(returnFindMarketingDiscountCertificate(marketingDiscountCertificateSetting,softModel));
            }else {
                return Result.error("券中心未开启");
            }
        }else{
            if (softModel.equals("0")){
                return Result.ok(returnFindMarketingDiscountCertificate(marketingDiscountCertificateSetting,softModel));
            }else {
                return Result.error("券中心未开启");
            }
        }
    }

    private Map<String,Object> returnFindMarketingDiscountCertificate(MarketingDiscountCertificateSetting marketingDiscountCertificateSetting,String softModel){
        HashMap<String, Object> map = new HashMap<>();
        //券中心别名
        map.put("name",marketingDiscountCertificateSetting.getName());
        //券中心海报图
        if (marketingDiscountCertificateSetting != null){
            //海报图
            map.put("coverPlan",marketingDiscountCertificateSetting.getCoverPlan());
            //默认分享图
            map.put("inviteFigure",marketingDiscountCertificateSetting.getInviteFigure());
            //券中心广告图
            map.put("ticketAdvertisementBanner",marketingDiscountCertificateSetting.getTicketAdvertisementBanner());
        }

        //获取限时抢券设置
        List<MarketingCertificateSeckillBaseSetting> marketingCertificateSeckillBaseSettingList = iMarketingCertificateSeckillBaseSettingService.list(new LambdaQueryWrapper<MarketingCertificateSeckillBaseSetting>()
                .eq(MarketingCertificateSeckillBaseSetting::getDelFlag, "0"));
        //是否开启限时抢券设置
        if (marketingCertificateSeckillBaseSettingList.size()>0){

            MarketingCertificateSeckillBaseSetting marketingCertificateSeckillBaseSetting = marketingCertificateSeckillBaseSettingList.get(0);
            if (marketingCertificateSeckillBaseSetting.getStatus().equals("1")){
                //分端显示；0：全部；1：小程序；2：app
                if (marketingCertificateSeckillBaseSetting.getPointsDisplay().equals("0")){

                    map.put("marketingCertificateSeckillBase",getMarketingCertificateSeckillBase(marketingDiscountCertificateSetting));

                }else if (marketingCertificateSeckillBaseSetting.getPointsDisplay().equals("1")){
                    //小程序
                    if (softModel.equals("0")){
                        map.put("marketingCertificateSeckillBase",getMarketingCertificateSeckillBase(marketingDiscountCertificateSetting));
                    }else {
                        map.put("marketingCertificateSeckillBase","");
                    }

                }else if (marketingCertificateSeckillBaseSetting.getPointsDisplay().equals("2")){
                    //app
                    if (softModel.equals("1")||softModel.equals("2")){
                        map.put("marketingCertificateSeckillBase",getMarketingCertificateSeckillBase(marketingDiscountCertificateSetting));
                    }else {
                        map.put("marketingCertificateSeckillBase","");
                    }
                }
            }else {
                map.put("marketingCertificateSeckillBase","");
            }

        }else {
            map.put("marketingCertificateSeckillBase","");
        }

        //获取拼好券设置
        List<MarketingCertificateGroupBaseSetting> marketingCertificateGroupBaseSettingList = iMarketingCertificateGroupBaseSettingService.list(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0")
        );
        //是否开启拼好券设置
        if (marketingCertificateGroupBaseSettingList.size()>0){
            MarketingCertificateGroupBaseSetting marketingCertificateGroupBaseSetting = marketingCertificateGroupBaseSettingList.get(0);
            //判断状态是否开启
            if (marketingCertificateGroupBaseSetting.getStatus().equals("1")){
                if (marketingCertificateGroupBaseSetting.getPointsDisplay().equals("0")){
                    //全部
                    map.put("marketingCertificateGroupBase",iMarketingCertificateGroupListService.pageListBySout(marketingDiscountCertificateSetting.getRecommendGroupNumber()));

                }else if (marketingCertificateGroupBaseSetting.getPointsDisplay().equals("1")){
                    //小程序
                    if (softModel.equals("0")){
                        map.put("marketingCertificateGroupBase",iMarketingCertificateGroupListService.pageListBySout(marketingDiscountCertificateSetting.getRecommendGroupNumber()));
                    }else {
                        map.put("marketingCertificateGroupBase","");
                    }

                }else if (marketingCertificateGroupBaseSetting.getPointsDisplay().equals("2")){
                    //app
                    if (softModel.equals("1")||softModel.equals("2")){
                        map.put("marketingCertificateGroupBase",iMarketingCertificateGroupListService.pageListBySout(marketingDiscountCertificateSetting.getRecommendGroupNumber()));
                    }else {
                        map.put("marketingCertificateGroupBase","");
                    }
                }
            }else {
                map.put("marketingCertificateGroupBase","");
            }

        }else {
            map.put("marketingCertificateGroupBase","");
        }
        //超值兑换券
        map.put("marketingCertificate",iMarketingCertificateService.pageListBySout(marketingDiscountCertificateSetting.getRecommendCertificateNumber()));
        return map;
    }
    private Map<String,Object> getMarketingCertificateSeckillBase(MarketingDiscountCertificateSetting marketingDiscountCertificateSetting){
        HashMap<String, Object> map = new HashMap<>();
        List<MarketingCertificateSeckillActivityList> marketingCertificateSeckillActivityListList = iMarketingCertificateSeckillActivityListService.list(new LambdaQueryWrapper<MarketingCertificateSeckillActivityList>()
                .eq(MarketingCertificateSeckillActivityList::getDelFlag, "0")
                .eq(MarketingCertificateSeckillActivityList::getStatus, "1")
        );
        //限时抢券
        if (marketingCertificateSeckillActivityListList.size()>0){
            MarketingCertificateSeckillActivityList marketingCertificateSeckillActivityList = marketingCertificateSeckillActivityListList.get(0);
            Map<String, Object> info = iMarketingCertificateSeckillActivityListService.getInfo(marketingCertificateSeckillActivityList.getId());
            if (marketingCertificateSeckillActivityList.getEndTime().compareTo(new Date())>0){
                map.put("id",marketingCertificateSeckillActivityList.getId());
                map.put("time",info.get("surplusTime"));
                map.put("data",iMarketingCertificateSeckillListService.pageListBySout(marketingCertificateSeckillActivityList.getId(),marketingDiscountCertificateSetting.getRecommendSeckillNumber()));
            }else {
                List<MarketingCertificateSeckillActivityList> certificateSeckillActivityListList = iMarketingCertificateSeckillActivityListService.list(new LambdaQueryWrapper<MarketingCertificateSeckillActivityList>()
                        .eq(MarketingCertificateSeckillActivityList::getDelFlag, "0")
                        .eq(MarketingCertificateSeckillActivityList::getStatus, "0")
                        .le(MarketingCertificateSeckillActivityList::getStartTime, new Date())
                        .ge(MarketingCertificateSeckillActivityList::getEndTime, new Date())
                );
                if (certificateSeckillActivityListList.size()>0){
                    MarketingCertificateSeckillActivityList marketingCertificateSeckillActivityList1 = certificateSeckillActivityListList.get(0);
                    Map<String, Object> info1 = iMarketingCertificateSeckillActivityListService.getInfo(marketingCertificateSeckillActivityList1.getId());
                    map.put("id",marketingCertificateSeckillActivityList1.getId());
                    map.put("time",info1.get("surplusTime"));
                    map.put("data",iMarketingCertificateSeckillListService.pageListBySout(marketingCertificateSeckillActivityList1.getId(),marketingDiscountCertificateSetting.getRecommendSeckillNumber()));
                }
            }
        }else {
            List<MarketingCertificateSeckillActivityList> certificateSeckillActivityListList = iMarketingCertificateSeckillActivityListService.list(new LambdaQueryWrapper<MarketingCertificateSeckillActivityList>()
                    .eq(MarketingCertificateSeckillActivityList::getDelFlag, "0")
                    .eq(MarketingCertificateSeckillActivityList::getStatus, "0")
                    .le(MarketingCertificateSeckillActivityList::getStartTime, new Date())
                    .ge(MarketingCertificateSeckillActivityList::getEndTime, new Date())
            );
            if (certificateSeckillActivityListList.size()>0){
                MarketingCertificateSeckillActivityList marketingCertificateSeckillActivityList = certificateSeckillActivityListList.get(0);
                Map<String, Object> info = iMarketingCertificateSeckillActivityListService.getInfo(marketingCertificateSeckillActivityList.getId());
                map.put("id",marketingCertificateSeckillActivityList.getId());
                map.put("time",info.get("surplusTime"));
                map.put("data",iMarketingCertificateSeckillListService.pageListBySout(marketingCertificateSeckillActivityList.getId(),marketingDiscountCertificateSetting.getRecommendSeckillNumber()));
            }
        }
        return map;
    }

}
