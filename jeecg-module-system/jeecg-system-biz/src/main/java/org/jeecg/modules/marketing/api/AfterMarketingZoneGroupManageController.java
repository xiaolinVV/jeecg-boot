package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.entity.MarketingZoneGroup;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 拼团
 */

@RequestMapping("after/marketingZoneGroupManage")
@Controller
@Log
public class AfterMarketingZoneGroupManageController {


    @Autowired
    private IMarketingZoneGroupTimeService iMarketingZoneGroupTimeService;

    @Autowired
    private IMarketingZoneGroupManageService iMarketingZoneGroupManageService;

    @Autowired
    private IMarketingZoneGroupBaseSettingService iMarketingZoneGroupBaseSettingService;

    @Autowired
    private IMarketingZoneGroupService iMarketingZoneGroupService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IMarketingZoneGroupGoodService iMarketingZoneGroupGoodService;

    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;

    @Autowired
    private IProviderTemplateService iProviderTemplateService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;





    /**
     * 获取参团进度列表
     *
     * @return
     */
    @RequestMapping("numberTuxedo")
    @ResponseBody
    public Result<?>  numberTuxedo(String marketingZoneGroupId){
        return Result.ok(iMarketingZoneGroupManageService.numberTuxedo(marketingZoneGroupId));
    }


    /**
     *
     * 确认订单
     *
     * @param marketingZoneGroupId
     * @return
     */
    @RequestMapping("confirmOrder")
    @ResponseBody
    public Result<?> confirmOrder(String marketingZoneGroupId,
                            String marketingZoneGroupGoodId,
                            String specification,
                            BigDecimal  quantity,
                            @RequestParam(name = "memberShippingAddressId" ,required = false,defaultValue = "") String memberShippingAddressId,
                            @RequestAttribute(value = "memberId",required = false) String memberId){

        Map<String,Object> resultMap= Maps.newHashMap();
        //参数校验
        if(StringUtils.isBlank(marketingZoneGroupId)){
            return Result.error("专区id不能为空");
        }
        MarketingZoneGroup marketingZoneGroup=iMarketingZoneGroupService.getById(marketingZoneGroupId);
        if(marketingZoneGroup==null){
            return Result.error("专区不存在");
        }
        if(marketingZoneGroup.getStatus().equals("0")){
            return Result.error("专区停止");
        }

        //判断用户拼团可用次数
        MarketingZoneGroupTime marketingZoneGroupTime=iMarketingZoneGroupTimeService.getOne(new LambdaQueryWrapper<MarketingZoneGroupTime>()
                .eq(MarketingZoneGroupTime::getMemberListId,memberId)
                .orderByDesc(MarketingZoneGroupTime::getCreateTime)
                .last("limit 1"));
        if(marketingZoneGroupTime==null){
            return Result.error("参团次数信息有误");
        }
        if(marketingZoneGroupTime.getSpellGroup().intValue()<1){
            return Result.error("参团次数不足");
        }

        //单天用户拼的次数上限判断
        if(iMarketingZoneGroupRecordService.getCountByMemberId(memberId,marketingZoneGroupId)>=marketingZoneGroup.getNumberCaps().intValue()){
            return Result.error("拼团次数超过单天上限，最大上限次数："+marketingZoneGroup.getNumberCaps());
        }


        //时间性判断
        MarketingZoneGroupBaseSetting marketingZoneGroupBaseSetting=iMarketingZoneGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupBaseSetting>()
                .eq(MarketingZoneGroupBaseSetting::getStatus,"1"));
        if(marketingZoneGroupBaseSetting==null){
            return Result.error("专区团设置不存在");
        }
        try {
            //时间判断
            Date startTime= DateUtils.parseDate(DateUtils.formatDate()+" "+marketingZoneGroupBaseSetting.getDayStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingZoneGroupBaseSetting.getDayEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>=startTime.getTime()&&new Date().getTime()<=endTime.getTime()){
                BigDecimal freight=new BigDecimal(0);
                BigDecimal totalPrice=new BigDecimal(0);
                GoodList goodList=iGoodListService.getById(iMarketingZoneGroupGoodService.getById(marketingZoneGroupGoodId).getGoodListId());
                resultMap.put("goodName",goodList.getGoodName());
                resultMap.put("mainPicture",goodList.getMainPicture());
                resultMap.put("id",goodList.getId());
                resultMap.put("marketPrice",goodList.getMarketPrice());
                resultMap.put("specification",specification);
                resultMap.put("label",marketingZoneGroup.getZoneName());
                resultMap.put("price",marketingZoneGroup.getPrice());
                resultMap.put("quantity",quantity);
                MemberShippingAddress memberShippingAddress=null;

                if(StringUtils.isBlank(memberShippingAddressId)){
                    memberShippingAddress= iMemberShippingAddressService.getOne(new LambdaQueryWrapper<MemberShippingAddress>()
                            .eq(MemberShippingAddress::getMemberListId,memberId)
                            .eq(MemberShippingAddress::getIsDefault,"1")
                            .last("limit 1"));
                }else{
                    memberShippingAddress=iMemberShippingAddressService.getById(memberShippingAddressId);
                }
                //收货地址数据
                Map<String, Object> memberShippingAddressMaps = Maps.newHashMap();
                if(memberShippingAddress!=null) {
                    memberShippingAddressMaps.put("isDefault",memberShippingAddress.getIsDefault());
                    memberShippingAddressMaps.put("phone",memberShippingAddress.getPhone());
                    memberShippingAddressMaps.put("houseNumber",memberShippingAddress.getHouseNumber());
                    memberShippingAddressMaps.put("areaAddress",memberShippingAddress.getAreaAddress());
                    memberShippingAddressMaps.put("id",memberShippingAddress.getId());
                    memberShippingAddressMaps.put("linkman",memberShippingAddress.getLinkman());
                    memberShippingAddressMaps.put("sysAreaId",memberShippingAddress.getSysAreaId());
                    memberShippingAddressMaps.put("areaExplan",memberShippingAddress.getAreaExplan());
                    resultMap.put("memberShippingAddressId",memberShippingAddress.getId());
                }
                resultMap.put("memberShippingAddress",memberShippingAddressMaps);

                //获取商品
                GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                        .eq(GoodSpecification::getGoodListId,goodList.getId())
                        .eq(GoodSpecification::getSpecification,specification)
                        .last("limit 1"));

                //有地址计算运费
                //有地址计算运费
                if(memberShippingAddress!=null) {
                    List<Map<String,Object>> goods= Lists.newArrayList();
                    Map<String,Object> g=Maps.newHashMap();
                    g.put("goodId",goodList.getId());
                    g.put("quantity",quantity);
                    g.put("price",marketingZoneGroup.getPrice());
                    g.put("goodSpecificationId",goodSpecification.getId());
                    goods.add(g);
                    freight=iProviderTemplateService.calculateFreight(goods,memberShippingAddress.getSysAreaId());
                }
                resultMap.put("freight",freight);
                resultMap.put("totalPrice",totalPrice.add(marketingZoneGroup.getPrice().multiply(quantity)).add(freight));
                resultMap.put("marketingZoneGroupId",marketingZoneGroupId);
                resultMap.put("marketingZoneGroupGoodId",marketingZoneGroupGoodId);
                resultMap.put("memberListId",memberId);
            }else{
                return Result.error("参团还没开始，请等待参团活动开始");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Result.ok(resultMap);
    }

}
