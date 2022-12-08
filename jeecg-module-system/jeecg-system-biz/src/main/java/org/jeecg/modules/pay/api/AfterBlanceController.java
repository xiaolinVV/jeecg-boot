package org.jeecg.modules.pay.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.entity.PayBalanceLog;
import org.jeecg.modules.pay.service.IPayBalanceLogService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("after/blance")
@Log
public class AfterBlanceController {


    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private PayUtils payUtils;

    @Autowired
    private IPayBalanceLogService iPayBalanceLogService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    /**
     * 进入收银台
     *
     * @param price
     * @param memberId
     * @return
     */
    @RequestMapping("toCashierDesk")
    @ResponseBody
    public Result<?> toCashierDesk(BigDecimal price, @RequestHeader("softModel") String softModel, @RequestAttribute(value = "memberId",required = false) String memberId, HttpServletRequest request){
        Map<String,Object> resultMap= Maps.newHashMap();
        //参数校验
        if(StringUtils.isBlank(memberId)){
            return Result.error("会员id不能为空");
        }
        if(price.doubleValue()<=0){
            return Result.error("金额不能为零");
        }
        resultMap.put("price",price);
        resultMap.put("memberListId",memberId);


        //充值资金限制
        String balanceRechargeLimitStatus = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "balance_recharge_limit_status");

        if(StringUtils.isNotBlank(balanceRechargeLimitStatus)&&balanceRechargeLimitStatus.equals("1")){
            String minimumRechargeAmount = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "minimum_recharge_amount");
            String maximumRechargeAmount = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "maximum_recharge_amount");
            if(price.doubleValue()<new BigDecimal(minimumRechargeAmount).doubleValue()){
                return Result.error("充值金额不能低于："+minimumRechargeAmount);
            }
            if(price.doubleValue()>new BigDecimal(maximumRechargeAmount).doubleValue()){
                return Result.error("充值金额不能高于："+maximumRechargeAmount);
            }
        }

        //重组支付日志信息
        PayBalanceLog payBalanceLog=new PayBalanceLog();
        payBalanceLog.setPayModel("2");
        payBalanceLog.setMemberListId(memberId);
        payBalanceLog.setTotalFee(price);
        iPayBalanceLogService.save(payBalanceLog);
        //价格
        BigDecimal allTotalPrice=price;
        //获取用户信息
        MemberList memberList=iMemberListService.getById(memberId);

        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("balance_recharge_notifyUrl");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {

            //cashier_desk_state   1   收银台状态。0：关闭；1：开启；
            String cashierDeskState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "cashier_desk_state"), "0");
            resultMap.put("cashierDeskState", cashierDeskState);
            //不使用收银台
            if (cashierDeskState.equals("0")) {

                //微信支付
                Map<String, String> resultMaps = payUtils.payWeixin(request, payBalanceLog.getId(), allTotalPrice, notifyUrl, memberList.getOpenid(),null);

                jsonStr = resultMaps.get("jsonStr");

                if (resultMaps.get("id") != null) {
                    payBalanceLog.setSerialNumber(resultMaps.get("id"));
                }

                //支付日志
                payBalanceLog.setPayParam(resultMaps.get("params"));

                //保存支付日志
                iPayBalanceLogService.saveOrUpdate(payBalanceLog);
            }
            //使用收银台
            if (cashierDeskState.equals("1")) {

                jsonStr = "1";

                //小程序原始id
                String originalId = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "original_id"), "0");

                resultMap.put("originalId", originalId);
                resultMap.put("allTotalPrice", allTotalPrice);
                //获取会员余额和积分信息
                resultMap.put("softModel", softModel);

                String payModel = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "balance_recharge_type");

                resultMap.put("payModel", payModel);
                //小程序
                if (softModel.equals("0")) {
                    List<String> payModels = Lists.newArrayList();
                    Arrays.asList(StringUtils.split(payModel, ","))
                            .stream().filter(model -> !model.equals("1"))
                            .forEach(model -> {
                                payModels.add(model);
                            });
                    payModel = StringUtils.join(payModels, ",");

                    log.info("收银台支付方式payModel：" + payModel);

                    resultMap.put("payModel", payModel);
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

            }
        }
        resultMap.put("payBalanceLogId",payBalanceLog.getId());
        resultMap.put("notifyUrl",notifyUrl+"?id="+payBalanceLog.getId());
        resultMap.put("jsonStr",jsonStr);
        return Result.ok(resultMap);
    }



    /**
     * 收银台付日志id支付接口
     *
     * @param memberId
     * @param payModel  支付方式；0：微信支付，1：支付宝支付
     * @param memberListId  会员id
     * @param request
     * @return
     */
    @RequestMapping("pay")
    @ResponseBody
    @Transactional
    public Result<?> pay(@RequestAttribute(value = "memberId",required = false) String memberId,
                         String payModel, String memberListId,
                         BigDecimal price,
                         HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        //参数校验
        if(StringUtils.isBlank(memberListId)){
            return result.error500("会员id不能为空");
        }
        if(StringUtils.isBlank(payModel)){
            return result.error500("支付方式不能为空");
        }
        if(price==null){
            return result.error500("支付金额不能为空");
        }
        //支付
        Map<String,Object> objectMap=this.payBalanceById(price,request,payModel,memberId,memberListId );

        result.setResult(objectMap);
        return result;
    }

    /**
     * 订单日志支付
     *
     * @param request
     * @param payModel
     * @param memberId
     * @return
     */
    public Map<String,Object>  payBalanceById(BigDecimal price,HttpServletRequest request,String payModel,String memberId,String memberListId ){
        Map<String, Object> objectMap=Maps.newHashMap();
        //重组支付日志信息
        PayBalanceLog payBalanceLog=new PayBalanceLog();
        payBalanceLog.setPayModel("2");
        payBalanceLog.setMemberListId(memberListId);
        payBalanceLog.setTotalFee(price);
        iPayBalanceLogService.save(payBalanceLog);
        //价格
        BigDecimal allTotalPrice=price;

        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("balance_recharge_notifyUrl");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {
            //判断支付方式
            //微信支付
            if(payModel.equals("0")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payBalanceLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),null);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payBalanceLog.setSerialNumber(resultMap.get("id"));
                }

                objectMap.put("queryUrl",resultMap.get("queryUrl"));
                //支付日志
                payBalanceLog.setPayParam(resultMap.get("params"));

                payBalanceLog.setPayModel(payModel);
                payBalanceLog.setPayPrice(allTotalPrice);
            }

            //支付宝支付
            if(payModel.equals("1")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //支付宝支付
                Map<String,String> resultMap=payUtils.payZhifubao(payBalanceLog.getId(),allTotalPrice,notifyUrl,null);

                jsonStr=resultMap.get("jsonStr");
                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                if(resultMap.get("id")!=null) {
                    payBalanceLog.setSerialNumber(resultMap.get("id"));
                }
                //支付日志
                payBalanceLog.setPayParam(resultMap.get("params"));

                payBalanceLog.setPayModel(payModel);
                payBalanceLog.setPayPrice(allTotalPrice);
            }
            //保存支付日志
            iPayBalanceLogService.saveOrUpdate(payBalanceLog);
        }

        objectMap.put("payBalanceLogId",payBalanceLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payBalanceLog.getId());
        return objectMap;
    }
}
