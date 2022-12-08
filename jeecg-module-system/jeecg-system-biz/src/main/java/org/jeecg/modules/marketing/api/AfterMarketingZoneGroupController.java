package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 专区团
 */

@RequestMapping("after/marketingZoneGroup")
@Controller
public class AfterMarketingZoneGroupController {


    @Autowired
    private IMarketingZoneGroupBaseSettingService iMarketingZoneGroupBaseSettingService;

    @Autowired
    private IMarketingZoneGroupTimeService iMarketingZoneGroupTimeService;

    @Autowired
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;

    @Autowired
    private IMarketingZoneGroupOrderService iMarketingZoneGroupOrderService;

    @Autowired
    private IMarketingZoneGroupService iMarketingZoneGroupService;

    @Autowired
    private IMarketingIntegralTaskBaseSettingService iMarketingIntegralTaskBaseSettingService;


    @Autowired
    private IMarketingZoneGroupManageService iMarketingZoneGroupManageService;

    /**
     * 专区团首页
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<?> index(@RequestAttribute("memberId") String memberId,
                           @RequestHeader("softModel") String softModel){
        Map<String,Object> resultMap= Maps.newHashMap();

        MarketingZoneGroupBaseSetting marketingZoneGroupBaseSetting=iMarketingZoneGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupBaseSetting>()
                .eq(MarketingZoneGroupBaseSetting::getStatus,"1"));
        if(marketingZoneGroupBaseSetting==null){
            return Result.error("专区团设置不存在");
        }

        resultMap.put("anotherName",marketingZoneGroupBaseSetting.getAnotherName());
        resultMap.put("marketingZoneGroupRecordMap",iMarketingZoneGroupRecordService.getIndexByStatus());
        //获取任务设置
        MarketingIntegralTaskBaseSetting marketingIntegralTaskBaseSetting = iMarketingIntegralTaskBaseSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralTaskBaseSetting>()
                .eq(MarketingIntegralTaskBaseSetting::getDelFlag, "0")
                .last("limit 1")
        );
        //判断任务入口显隐
        if (marketingIntegralTaskBaseSetting !=null){
            if (marketingIntegralTaskBaseSetting.getStatus().equals("0")){
                resultMap.put("isViewMarketingIntegralTask","0");
            }else {
                if (marketingIntegralTaskBaseSetting.getPointsDisplay().equals("0")){
                    resultMap.put("isViewMarketingIntegralTask","1");
                }else if (marketingIntegralTaskBaseSetting.getPointsDisplay().equals("1")){
                    resultMap.put("isViewMarketingIntegralTask",softModel.equals("0")?"1":"0");
                }else {
                    resultMap.put("isViewMarketingIntegralTask",softModel.equals("1")||softModel.equals("2")?"1":"0");
                }
            }
        }else {
            resultMap.put("isViewMarketingIntegralTask","0");
        }

        //时间性判断
        try {
            resultMap.put("dayStartTime",marketingZoneGroupBaseSetting.getDayStartTime());
            resultMap.put("dayEndTime",marketingZoneGroupBaseSetting.getDayEndTime());
            resultMap.put("ruleDescription",marketingZoneGroupBaseSetting.getRuleDescription());
            resultMap.put("coverPlan",marketingZoneGroupBaseSetting.getCoverPlan());
            resultMap.put("posters",marketingZoneGroupBaseSetting.getPosters());
            resultMap.put("shareTitle",marketingZoneGroupBaseSetting.getShareTitle());
            resultMap.put("shareDescription",marketingZoneGroupBaseSetting.getShareDescription());
            //建团次数和参团次数
            MarketingZoneGroupTime marketingZoneGroupTime = iMarketingZoneGroupTimeService.getOne(new LambdaQueryWrapper<MarketingZoneGroupTime>()
                    .eq(MarketingZoneGroupTime::getMemberListId, memberId)
                    .orderByDesc(MarketingZoneGroupTime::getCreateTime)
                    .last("limit 1"));
            if(marketingZoneGroupTime!=null){
                resultMap.put("groupNumber",marketingZoneGroupTime.getGroupNumber());
                resultMap.put("spellGroup",marketingZoneGroupTime.getSpellGroup());
            }else{
                marketingZoneGroupTime=new MarketingZoneGroupTime();
                marketingZoneGroupTime.setMemberListId(memberId);
                iMarketingZoneGroupTimeService.save(marketingZoneGroupTime);
                resultMap.put("groupNumber",0);
                resultMap.put("spellGroup",0);
            }
            //拼团记录
            resultMap.put("marketingZoneGroupRecordCount",iMarketingZoneGroupRecordService.count(new LambdaQueryWrapper<MarketingZoneGroupRecord>().eq(MarketingZoneGroupRecord::getMemberListId,memberId)));

            //拼中商品
            resultMap.put("marketingZoneGroupOrderCount",iMarketingZoneGroupOrderService.count(new LambdaQueryWrapper<MarketingZoneGroupOrder>().eq(MarketingZoneGroupOrder::getMemberListId,memberId)));
           //专区列表
            List<MarketingZoneGroup> marketingZoneGroupList=iMarketingZoneGroupService.list(new LambdaQueryWrapper<MarketingZoneGroup>().eq(MarketingZoneGroup::getStatus,"1").orderByAsc(MarketingZoneGroup::getSort));
            resultMap.put("marketingZoneGroup",marketingZoneGroupList);
            //时间判断
            Date startTime= DateUtils.parseDate(DateUtils.formatDate()+" "+marketingZoneGroupBaseSetting.getDayStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingZoneGroupBaseSetting.getDayEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>=startTime.getTime()&&new Date().getTime()<=endTime.getTime()){
                resultMap.put("isOpen","1");
                marketingZoneGroupList.forEach(mp->{
                    //判断有没有可以参加的团
                    long marketingZoneGroupManageCount=iMarketingZoneGroupManageService.count(new LambdaQueryWrapper<MarketingZoneGroupManage>().eq(MarketingZoneGroupManage::getStatus,"0").eq(MarketingZoneGroupManage::getMarketingZoneGroupId,mp.getId()));
                    if(marketingZoneGroupManageCount==0){
                        //创建团
                        int i=5;
                        while (i>0){
                            MarketingZoneGroupManage marketingZoneGroupManage=new MarketingZoneGroupManage();
                            marketingZoneGroupManage.setSerialNumber(OrderNoUtils.getOrderNo());
                            marketingZoneGroupManage.setZoneName(mp.getZoneName());
                            marketingZoneGroupManage.setPrice(mp.getPrice());
                            marketingZoneGroupManage.setVirtualGroupMembers(mp.getVirtualGroupMembers());
                            marketingZoneGroupManage.setActualGroupSize(mp.getActualGroupSize());
                            marketingZoneGroupManage.setStartTime(new Date());
                            marketingZoneGroupManage.setMarketingZoneGroupId(mp.getId());
                            iMarketingZoneGroupManageService.save(marketingZoneGroupManage);
                            i--;
                        }
                    }
                });


            }else{
                resultMap.put("isOpen","0");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Result.ok(resultMap);
    }



}
