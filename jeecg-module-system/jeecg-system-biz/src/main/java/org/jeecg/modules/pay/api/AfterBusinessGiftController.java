package org.jeecg.modules.pay.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftList;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftListService;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftTeamListService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.entity.PayBusinessGiftLog;
import org.jeecg.modules.pay.service.IPayBusinessGiftLogService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 另类支付方式
 */
@RequestMapping("after/businessGift")
@Controller
@Log
public class AfterBusinessGiftController {


    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private PayUtils payUtils;

    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;


    @Autowired
    private IMarketingBusinessGiftListService iMarketingBusinessGiftListService;

    @Autowired
    private IPayBusinessGiftLogService iPayBusinessGiftLogService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    @Autowired
    private IMarketingBusinessGiftTeamListService iMarketingBusinessGiftTeamListService;



    /**
     * 进入收银台
     *

     * @param memberShippingAddressId
     * @param softModel
     * @param memberListId
     * @return
     */
    @RequestMapping("toCashierDesk")
    @ResponseBody
    public Result<?> toCashierDesk(@RequestParam(name = "promotionCode" ,required = false,defaultValue = "") String promotionCode,
                                   String marketingBusinessGiftListId,
                                   String memberShippingAddressId,
                                   String message,
                                   @RequestHeader("softModel") String softModel,
                                   String memberListId){

        Map<String,Object> resultMap=Maps.newHashMap();

        //参数校验
        if (StringUtils.isBlank(memberListId)) {
            return Result.error("会员id不能为空");
        }
        //参数校验
        if(StringUtils.isBlank(marketingBusinessGiftListId)){
            return Result.error("创业礼包id不能为空");
        }

        Map<String,Object> recordMap=iMarketingBusinessGiftTeamListService.getRecordIdByMemberId(memberListId);
        if(recordMap!=null){
            return Result.error("已有进行中的礼包，不可购买");
        }

        MemberShippingAddress memberShippingAddress=null;

        if(StringUtils.isBlank(memberShippingAddressId)){
            memberShippingAddress= iMemberShippingAddressService.getOne(new LambdaQueryWrapper<MemberShippingAddress>()
                    .eq(MemberShippingAddress::getMemberListId,memberListId)
                    .eq(MemberShippingAddress::getIsDefault,"1")
                    .last("limit 1"));
        }else{
            memberShippingAddress=iMemberShippingAddressService.getById(memberShippingAddressId);
        }


        resultMap.put("freight",0);

        Map<String,Object> discountMap=Maps.newLinkedHashMap();
        //0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；
        discountMap.put("0","1");
        discountMap.put("1","1");
        discountMap.put("2","1");
        discountMap.put("3","1");
        discountMap.put("4","1");

        MarketingBusinessGiftList marketingBusinessGiftList=iMarketingBusinessGiftListService.getById(marketingBusinessGiftListId);


        //支付记录
        PayBusinessGiftLog payBusinessGiftLog=new PayBusinessGiftLog();
        payBusinessGiftLog.setTotalPrice(marketingBusinessGiftList.getSalesPrice());
        payBusinessGiftLog.setDiscount(JSON.toJSONString(discountMap));
        payBusinessGiftLog.setMemberListId(memberListId);
        payBusinessGiftLog.setMarketingBusinessGiftListId(marketingBusinessGiftListId);


            if (StringUtils.isNotBlank(promotionCode)) {
                promotionCode = StringUtils.trim(promotionCode);
                MemberList memberList = iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPromotionCode, promotionCode));
                if (memberList != null) {
                    payBusinessGiftLog.setTMemberId(memberList.getId());
                }
            }

        if(memberShippingAddress!=null) {
            payBusinessGiftLog.setMemberShippingAddressId(memberShippingAddress.getId());
        }
        payBusinessGiftLog.setMessage(message);
        iPayBusinessGiftLogService.save(payBusinessGiftLog);
        //cashier_desk_state   1   收银台状态。0：关闭；1：开启；
        String cashierDeskState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "cashier_desk_state"),"0");
        resultMap.put("cashierDeskState",cashierDeskState);
        MemberList memberList = iMemberListService.getById(memberListId);
        BigDecimal integralValue = iMarketingWelfarePaymentsSettingService.getIntegralValue();
        resultMap.put("integralValue", integralValue);
        resultMap.put("payWelfarePayments", marketingBusinessGiftList.getSalesPrice().divide(integralValue, 2, RoundingMode.UP));
        resultMap.put("allTotalPrice", marketingBusinessGiftList.getSalesPrice());
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

                log.info("收银台支付方式payModel：" + payModel);

                resultMap.put("payModel", payModel);
            }
        }

        log.info("收银台支付方式payModel：" + payModel);
        resultMap.put("payModel", payModel);
        resultMap.put("payBusinessGiftLogId",payBusinessGiftLog.getId());
        resultMap.put("jsonStr","1");

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
        PayBusinessGiftLog payBusinessGiftLog=iPayBusinessGiftLogService.getOne(new LambdaQueryWrapper<PayBusinessGiftLog>()
                .eq(PayBusinessGiftLog::getMemberListId,memberListId).eq(PayBusinessGiftLog::getPayStatus,"0")
                .orderByDesc(PayBusinessGiftLog::getCreateTime)
                .last("limit 1"));
        log.info("收银台，支付的时候日志id："+payBusinessGiftLog.getId()+"；余额："+balance+"；积分:"+welfarePayments+"；会员手机："+memberList.getPhone()+"；总金额："+payBusinessGiftLog.getTotalPrice());
        if(payBusinessGiftLog==null){
            return result.error500("支付日志id不存在");
        }
        if(payBusinessGiftLog.getPayStatus().equals("1")){
            return result.error500("支付日志已支付");
        }
        //支付
        Map<String,Object> objectMap=this.payBusinessGiftLogById(request,payModel,welfarePayments,balance,payBusinessGiftLog.getId(),memberId);

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
     * @param payBusinessGiftLogId
     * @param memberId
     * @return
     */
    public Map<String,Object>  payBusinessGiftLogById(HttpServletRequest request,String payModel, BigDecimal welfarePayments,BigDecimal balance,String payBusinessGiftLogId,String memberId){
        Map<String, Object> objectMap=Maps.newHashMap();
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        //重组支付日志信息
        PayBusinessGiftLog payBusinessGiftLog=iPayBusinessGiftLogService.getById(payBusinessGiftLogId);
        payBusinessGiftLog.setId(null);
        payBusinessGiftLog.setBalance(balance);
        payBusinessGiftLog.setWelfarePayments(welfarePayments);
        payBusinessGiftLog.setPayModel("2");
        iPayBusinessGiftLogService.save(payBusinessGiftLog);
        Map<String,Object> discountMap= JSON.parseObject(payBusinessGiftLog.getDiscount());
        //价格
        BigDecimal allTotalPrice=payBusinessGiftLog.getTotalPrice();

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

        String notifyUrl = notifyUrlUtils.getNotify("business_gift_bag_notify_url");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {
            //判断支付方式
            //微信支付
            if(payModel.equals("0")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payBusinessGiftLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),null);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payBusinessGiftLog.setSerialNumber(resultMap.get("id"));
                }

                objectMap.put("queryUrl",resultMap.get("queryUrl"));
                //支付日志
                payBusinessGiftLog.setPayParam(resultMap.get("params"));

                payBusinessGiftLog.setPayModel(payModel);
                payBusinessGiftLog.setPayPrice(allTotalPrice);
            }

            //支付宝支付
            if(payModel.equals("1")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //支付宝支付
                Map<String,String> resultMap=payUtils.payZhifubao(payBusinessGiftLog.getId(),allTotalPrice,notifyUrl,null);

                jsonStr=resultMap.get("jsonStr");
                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                if(resultMap.get("id")!=null) {
                    payBusinessGiftLog.setSerialNumber(resultMap.get("id"));
                }
                //支付日志
                payBusinessGiftLog.setPayParam(resultMap.get("params"));

                payBusinessGiftLog.setPayModel(payModel);
                payBusinessGiftLog.setPayPrice(allTotalPrice);
            }
            //保存支付日志
            iPayBusinessGiftLogService.saveOrUpdate(payBusinessGiftLog);
        }

        objectMap.put("payBusinessGiftLogId",payBusinessGiftLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payBusinessGiftLog.getId());
        return objectMap;
    }

}
