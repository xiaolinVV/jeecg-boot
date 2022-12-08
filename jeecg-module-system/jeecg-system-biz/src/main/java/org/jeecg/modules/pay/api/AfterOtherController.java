package org.jeecg.modules.pay.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.service.IPayOrderCarLogService;
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
@RequestMapping("after/other")
@Controller
@Log
public class AfterOtherController {


    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IPayOrderCarLogService iPayOrderCarLogService;


    @Autowired
    private ISysDictService iSysDictService;


    @Autowired
    private PayUtils payUtils;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    /**
     * 进入收银台
     *
     * @param price
     * @param memberListId
     * @return
     */
    @RequestMapping("toCashierDesk")
    @ResponseBody
    public Result<?> toCashierDesk(BigDecimal price, @RequestHeader("softModel") String softModel, String memberListId){
        Map<String,Object> resultMap= Maps.newHashMap();
        //参数校验
        if(StringUtils.isBlank(memberListId)){
            return Result.error("会员id不能为空");
        }
        if(price.doubleValue()<=0){
            return Result.error("金额不能为零");
        }
        MemberList memberList=iMemberListService.getById(memberListId);
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        resultMap.put("integralValue",integralValue);
        resultMap.put("payWelfarePayments",price.divide(integralValue,2, RoundingMode.UP));
        resultMap.put("price",price);
        resultMap.put("memberListId",memberListId);
        resultMap.put("welfarePayments",memberList.getWelfarePayments());
        String payModel="0,1,3";

        //小程序
        if(softModel.equals("0")){
            List<String> payModels= Lists.newArrayList();
            Arrays.asList(StringUtils.split(payModel,","))
                    .stream().filter(model->!model.equals("1"))
                    .forEach(model->{
                        payModels.add(model);
                    });
            payModel=StringUtils.join(payModels,",");
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

        log.info("收银台支付方式payModel："+payModel);
        resultMap.put("payModel",payModel);
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
                         BigDecimal price,
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
        if(price==null){
            return result.error500("支付金额不能为空");
        }
        //判断会员资金信息
        MemberList memberList=iMemberListService.getById(memberListId);
        PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getOne(new LambdaQueryWrapper<PayOrderCarLog>()
                .eq(PayOrderCarLog::getMemberListId,memberListId).eq(PayOrderCarLog::getPayStatus,"0")
                .orderByDesc(PayOrderCarLog::getCreateTime)
                .last("limit 1"));
        log.info("收银台，支付的时候日志id："+payOrderCarLog.getId()+"；余额："+balance+"；积分:"+welfarePayments+"；会员手机："+memberList.getPhone()+"；总金额："+payOrderCarLog.getAllTotalPrice());
        if(payOrderCarLog==null){
            return result.error500("支付日志id不存在");
        }
        if(payOrderCarLog.getPayStatus().equals("1")){
            return result.error500("支付日志已支付");
        }
        //支付
        Map<String,Object> objectMap=this.payOrderCarLogById(price,request,payModel,welfarePayments,balance,payOrderCarLog.getId(),memberId);

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
     * @param payOrderCarLogId
     * @param memberId
     * @return
     */
    public Map<String,Object>  payOrderCarLogById(BigDecimal price,HttpServletRequest request,String payModel, BigDecimal welfarePayments,BigDecimal balance,String payOrderCarLogId,String memberId){
        Map<String, Object> objectMap=Maps.newHashMap();
        //重组支付日志信息
        PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getById(payOrderCarLogId);
        payOrderCarLog.setId(null);
        payOrderCarLog.setBalance(balance);
        payOrderCarLog.setWelfarePayments(welfarePayments);
        payOrderCarLog.setPayModel("2");
        iPayOrderCarLogService.save(payOrderCarLog);
        //价格
        BigDecimal allTotalPrice=price;

        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("notifyUrl");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {
            //判断支付方式
            //微信支付
            if(payModel.equals("0")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payOrderCarLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),null);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payOrderCarLog.setSerialNumber(resultMap.get("id"));
                }

                objectMap.put("queryUrl",resultMap.get("queryUrl"));
                //支付日志
                payOrderCarLog.setPayParam(resultMap.get("params"));

                payOrderCarLog.setPayModel(payModel);
                payOrderCarLog.setPayPrice(allTotalPrice);
            }

            //支付宝支付
            if(payModel.equals("1")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //支付宝支付
                Map<String,String> resultMap=payUtils.payZhifubao(payOrderCarLog.getId(),allTotalPrice,notifyUrl,null);

                jsonStr=resultMap.get("jsonStr");
                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                if(resultMap.get("id")!=null) {
                    payOrderCarLog.setSerialNumber(resultMap.get("id"));
                }
                //支付日志
                payOrderCarLog.setPayParam(resultMap.get("params"));

                payOrderCarLog.setPayModel(payModel);
                payOrderCarLog.setPayPrice(allTotalPrice);
            }
            //保存支付日志
            iPayOrderCarLogService.saveOrUpdate(payOrderCarLog);
        }

        objectMap.put("payOrderCarLogId",payOrderCarLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payOrderCarLog.getId());
        return objectMap;
    }

}
