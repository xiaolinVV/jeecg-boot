package org.jeecg.modules.pay.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.entity.PayZoneGroupLog;
import org.jeecg.modules.pay.service.IPayZoneGroupLogService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 另类支付方式
 */
@RequestMapping("after/zoneGroup")
@Controller
@Log
public class AfterZoneGroupController {


    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private PayUtils payUtils;


    @Autowired
    private IMarketingZoneGroupTimeService iMarketingZoneGroupTimeService;

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
    private IPayZoneGroupLogService iPayZoneGroupLogService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    /**
     * 进入收银台
     *
     * @param marketingZoneGroupId
     * @param marketingZoneGroupGoodId
     * @param specification
     * @param quantity
     * @param memberShippingAddressId
     * @param softModel
     * @param memberListId
     * @return
     */
    @RequestMapping("toCashierDesk")
    @ResponseBody
    public Result<?> toCashierDesk(String marketingZoneGroupId,
                                   String marketingZoneGroupGoodId,
                                   String specification,
                                   BigDecimal  quantity,
                                   String memberShippingAddressId,
                                   String message,
                                   @RequestHeader("softModel") String softModel,
                                   String memberListId){

        //参数校验
        if (StringUtils.isBlank(memberListId)) {
            return Result.error("会员id不能为空");
        }
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
                .eq(MarketingZoneGroupTime::getMemberListId,memberListId)
                .orderByDesc(MarketingZoneGroupTime::getCreateTime)
                .last("limit 1"));
        if(marketingZoneGroupTime==null){
            return Result.error("参团次数信息有误");
        }
        if(marketingZoneGroupTime.getSpellGroup().intValue()<1){
            return Result.error("参团次数不足");
        }
        //时间性判断
        MarketingZoneGroupBaseSetting marketingZoneGroupBaseSetting=iMarketingZoneGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupBaseSetting>()
                .eq(MarketingZoneGroupBaseSetting::getStatus,"1"));
        if(marketingZoneGroupBaseSetting==null){
            return Result.error("专区团设置不存在");
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            //时间判断
            Date startTime= DateUtils.parseDate(DateUtils.formatDate()+" "+marketingZoneGroupBaseSetting.getDayStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingZoneGroupBaseSetting.getDayEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>=startTime.getTime()&&new Date().getTime()<=endTime.getTime()) {
                BigDecimal freight=new BigDecimal(0);
                BigDecimal totalPrice=new BigDecimal(0);
                GoodList goodList=iGoodListService.getById(iMarketingZoneGroupGoodService.getById(marketingZoneGroupGoodId).getGoodListId());
                MemberShippingAddress memberShippingAddress=null;

                if(StringUtils.isBlank(memberShippingAddressId)){
                    memberShippingAddress= iMemberShippingAddressService.getOne(new LambdaQueryWrapper<MemberShippingAddress>()
                            .eq(MemberShippingAddress::getMemberListId,memberListId)
                            .eq(MemberShippingAddress::getIsDefault,"1")
                            .last("limit 1"));
                }else{
                    memberShippingAddress=iMemberShippingAddressService.getById(memberShippingAddressId);
                }

                //获取商品
                GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                        .eq(GoodSpecification::getGoodListId,goodList.getId())
                        .eq(GoodSpecification::getSpecification,specification)
                        .last("limit 1"));
                //有地址计算运费
                if(memberShippingAddress!=null) {
                    List<Map<String,Object>> goods= Lists.newArrayList();
                    Map<String,Object> g=Maps.newHashMap();
                    g.put("goodId",goodList.getId());
                    g.put("quantity",quantity);
                    g.put("price",marketingZoneGroup.getPrice());
                    g.put("goodSpecificationId",goodSpecification.getId());
                    goods.add(g);
                    goods.add(g);
                    freight=iProviderTemplateService.calculateFreight(goods,memberShippingAddress.getSysAreaId());
                }
                resultMap.put("freight",freight);
                totalPrice=totalPrice.add(marketingZoneGroup.getPrice().multiply(quantity)).add(freight);

                Map<String,Object> discountMap=Maps.newLinkedHashMap();
                //0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；
                discountMap.put("0","1");
                discountMap.put("1","1");
                discountMap.put("2","1");
                discountMap.put("3","1");
                discountMap.put("4","1");

                //支付记录
                PayZoneGroupLog payZoneGroupLog=new PayZoneGroupLog();
                payZoneGroupLog.setMarketingZoneGroupId(marketingZoneGroupId);
                payZoneGroupLog.setMarketingZoneGroupGoodId(marketingZoneGroupGoodId);
                payZoneGroupLog.setSpecification(specification);
                payZoneGroupLog.setQuantity(quantity);
                payZoneGroupLog.setTotalPrice(totalPrice);
                payZoneGroupLog.setDiscount(JSON.toJSONString(discountMap));
                payZoneGroupLog.setMemberListId(memberListId);
                if(memberShippingAddress!=null) {
                    payZoneGroupLog.setMemberShippingAddressId(memberShippingAddress.getId());
                }
                payZoneGroupLog.setMessage(message);
                iPayZoneGroupLogService.save(payZoneGroupLog);
                if (totalPrice.doubleValue() <= 0) {
                    return Result.error("金额不能为零");
                }


                //cashier_desk_state   1   收银台状态。0：关闭；1：开启；
                String cashierDeskState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "cashier_desk_state"),"0");
                resultMap.put("cashierDeskState",cashierDeskState);
                MemberList memberList = iMemberListService.getById(memberListId);
                BigDecimal integralValue = iMarketingWelfarePaymentsSettingService.getIntegralValue();
                resultMap.put("integralValue", integralValue);
                resultMap.put("payWelfarePayments", totalPrice.divide(integralValue, 2, RoundingMode.UP));
                resultMap.put("allTotalPrice", totalPrice);
                resultMap.put("memberListId", memberListId);
                resultMap.put("welfarePayments", memberList.getWelfarePayments());
                resultMap.put("balance",memberList.getBalance());
                String payModel = "0,1,2,3";

                //小程序
                if (softModel.equals("0")) {
                    List<String> payModels = Lists.newArrayList();
                    Arrays.asList(StringUtils.split(payModel, ","))
                            .stream().filter(model -> !model.equals("1"))
                            .forEach(model -> {
                                payModels.add(model);
                            });
                    payModel = StringUtils.join(payModels, ",");
                }

                //app是否禁用小程序
                if(softModel.equals("1")||softModel.equals("2")){
                    String closeWxPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "close_wx_pay");
                    if(closeWxPay.equals("1")){
                        List<String> payModels = Lists.newArrayList();
                        Arrays.asList(StringUtils.split(payModel, ","))
                                .stream().filter(model -> !model.equals("0"))
                                .forEach(model -> {
                                    payModels.add(model);
                                });
                        payModel = StringUtils.join(payModels, ",");
                    }
                }


                log.info("收银台支付方式payModel：" + payModel);
                resultMap.put("payModel", payModel);
                resultMap.put("payZoneGroupLogId",payZoneGroupLog.getId());
                resultMap.put("jsonStr","1");
                }else{
                    return Result.error("参团还没开始，请等待参团活动开始");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return Result.ok(resultMap);
    }


    /**
     * 收银台付日志id支付接口
     *
     * @param memberId
     * @param payModel  支付方式；0：微信支付，1：支付宝支付
     * @param memberListId  会员id
     * @param welfarePayments 积分
     * @param balance 余额
     * @param request
     * @return
     */
    @RequestMapping("pay")
    @ResponseBody
    @Transactional
    public Result<?> pay(@RequestAttribute(value = "memberId",required = false) String memberId,
                         String payModel, String memberListId,
                         @RequestParam(name = "welfarePayments",required = false,defaultValue = "0") BigDecimal welfarePayments,
                         @RequestParam(name = "balance",required = false,defaultValue = "0") BigDecimal balance,
                         HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        //参数校验
        if(StringUtils.isBlank(memberListId)){
            return result.error500("会员id不能为空");
        }
        if(StringUtils.isBlank(payModel)){
            return result.error500("支付方式不能为空");
        }

        //判断会员资金信息
        MemberList memberList=iMemberListService.getById(memberListId);
        PayZoneGroupLog payZoneGroupLog=iPayZoneGroupLogService.getOne(new LambdaQueryWrapper<PayZoneGroupLog>()
                .eq(PayZoneGroupLog::getMemberListId,memberListId).eq(PayZoneGroupLog::getPayStatus,"0")
                .orderByDesc(PayZoneGroupLog::getCreateTime)
                .last("limit 1"));
        log.info("收银台，支付的时候日志id："+payZoneGroupLog.getId()+"；余额："+balance+"；积分:"+welfarePayments+"；会员手机："+memberList.getPhone()+"；总金额："+payZoneGroupLog.getTotalPrice());
        if(payZoneGroupLog==null){
            return result.error500("支付日志id不存在");
        }
        if(payZoneGroupLog.getPayStatus().equals("1")){
            return result.error500("支付日志已支付");
        }
        //支付
        Map<String,Object> objectMap=this.payZoneGroupLogById(request,payModel,welfarePayments,balance,payZoneGroupLog.getId(),memberId);

        result.setResult(objectMap);
        return result;
    }

    /**
     * 订单日志支付
     *
     * @param request
     * @param payModel
     * @param welfarePayments
     * @param balance
     * @param payZoneGroupLogId
     * @param memberId
     * @return
     */
    public Map<String,Object>  payZoneGroupLogById(HttpServletRequest request,String payModel, BigDecimal welfarePayments,BigDecimal balance,String payZoneGroupLogId,String memberId){
        Map<String, Object> objectMap=Maps.newHashMap();
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        //重组支付日志信息
        PayZoneGroupLog payZoneGroupLog=iPayZoneGroupLogService.getById(payZoneGroupLogId);
        payZoneGroupLog.setId(null);
        payZoneGroupLog.setBalance(balance);
        payZoneGroupLog.setWelfarePayments(welfarePayments);
        payZoneGroupLog.setPayModel("2");
        iPayZoneGroupLogService.save(payZoneGroupLog);
        Map<String,Object> discountMap= JSON.parseObject(payZoneGroupLog.getDiscount());
        //价格
        BigDecimal allTotalPrice=payZoneGroupLog.getTotalPrice();

        if(balance.doubleValue()!=0){
            balance=balance.divide(((JSONObject) discountMap).getBigDecimal("2"),2,RoundingMode.HALF_UP);
        }
        log.info("支付的余额："+balance);
        if(welfarePayments.doubleValue()!=0){
            welfarePayments=welfarePayments.multiply(integralValue).divide(((JSONObject) discountMap).getBigDecimal("3"),2,RoundingMode.HALF_UP);
        }
        log.info("支付的积分："+welfarePayments);
        if(allTotalPrice.subtract(balance).subtract(welfarePayments).doubleValue()<=0){
            allTotalPrice=new BigDecimal(0);
        }else{
            allTotalPrice=allTotalPrice.subtract(balance).subtract(welfarePayments).setScale(2, RoundingMode.HALF_UP);
        }
        log.info("优惠比例："+JSON.toJSONString(discountMap));

        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("prefecture_group_notify_url");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {
            //判断支付方式
            //微信支付
            if(payModel.equals("0")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payZoneGroupLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),null);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payZoneGroupLog.setSerialNumber(resultMap.get("id"));
                }

                objectMap.put("queryUrl",resultMap.get("queryUrl"));
                //支付日志
                payZoneGroupLog.setPayParam(resultMap.get("params"));

                payZoneGroupLog.setPayModel(payModel);
                payZoneGroupLog.setPayPrice(allTotalPrice);
            }

            //支付宝支付
            if(payModel.equals("1")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //支付宝支付
                Map<String,String> resultMap=payUtils.payZhifubao(payZoneGroupLog.getId(),allTotalPrice,notifyUrl,null);

                jsonStr=resultMap.get("jsonStr");
                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                if(resultMap.get("id")!=null) {
                    payZoneGroupLog.setSerialNumber(resultMap.get("id"));
                }
                //支付日志
                payZoneGroupLog.setPayParam(resultMap.get("params"));

                payZoneGroupLog.setPayModel(payModel);
                payZoneGroupLog.setPayPrice(allTotalPrice);
            }
            //保存支付日志
            iPayZoneGroupLogService.saveOrUpdate(payZoneGroupLog);
        }

        objectMap.put("payZoneGroupLogId",payZoneGroupLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payZoneGroupLog.getId());
        return objectMap;
    }

}
