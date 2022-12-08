package org.jeecg.modules.pay.api;


import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.order.utils.WeixinHftxPayUtils;
import org.jeecg.modules.pay.entity.PayShouyinLog;
import org.jeecg.modules.pay.service.IPayShouyinLogService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.service.IStoreCashierRoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/*
* 收银台收款
* */

@RequestMapping("front/payShouyinLog")
@Controller
@Log
public class FrontPayShouyinLogController {


    @Autowired
    private IPayShouyinLogService iPayShouyinLogService;

    @Autowired
    private PayUtils payUtils;

    @Autowired
    private WeixinHftxPayUtils weixinHftxPayUtils;


    @Autowired
    private IStoreCashierRoutingService iStoreCashierRoutingService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    /**
     * 订单日志支付
     *
     * @param
     * @param payModel
     * @return
     */
    @RequestMapping("storeCheckstand")
    @ResponseBody
    public Result<?> storeCheckstand(BigDecimal price,String storeManageId, String remark, String payModel,
                                     @RequestParam(name ="gzOpenId",defaultValue = "",required = false) String gzOpenId,
                                     @RequestAttribute(name = "memberId",required = false) String memberId ){
        Map<String, Object> objectMap= Maps.newHashMap();
        log.info("收银台会员id："+memberId);
        //重组支付日志信息
        PayShouyinLog payShouyinLog=new PayShouyinLog();
        payShouyinLog.setPayModel(payModel);
        payShouyinLog.setMemberListId(memberId);
        payShouyinLog.setAllTotalPrice(price);
        payShouyinLog.setStoreManageId(storeManageId);
        payShouyinLog.setRemark(remark);
        iPayShouyinLogService.save(payShouyinLog);
        //价格
        BigDecimal allTotalPrice=price;

        //设置回调地址
        String notifyUrl = notifyUrlUtils.getNotify("store_checkstand_payback");
        String jsonStr="0";

        log.info("店铺收银回调地址："+notifyUrl);

        if(allTotalPrice.doubleValue()>0) {
            List<Map<String, String>> divMembers = iStoreCashierRoutingService.independentAccountShouYin(payShouyinLog);


            //判断支付方式
            //微信支付
            if(payModel.equals("0")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //微信支付
                Map<String,String> resultMap=weixinHftxPayUtils.payGzWeixin(allTotalPrice,payShouyinLog.getId(),notifyUrl,gzOpenId,divMembers);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payShouyinLog.setSerialNumber(resultMap.get("id"));
                }

                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                payShouyinLog.setPayModel(payModel);
                payShouyinLog.setPayPrice(allTotalPrice);
            }

            //支付宝支付
            if(payModel.equals("1")){
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //支付宝支付
                Map<String,String> resultMap=payUtils.payZhifubao(payShouyinLog.getId(),allTotalPrice,notifyUrl,divMembers);

                jsonStr=resultMap.get("jsonStr");
                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                if(resultMap.get("id")!=null) {
                    payShouyinLog.setSerialNumber(resultMap.get("id"));
                }
                payShouyinLog.setPayModel(payModel);
                payShouyinLog.setPayPrice(allTotalPrice);
            }
            //保存支付日志
            iPayShouyinLogService.saveOrUpdate(payShouyinLog);
        }

        objectMap.put("payShouyinLogId",payShouyinLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payShouyinLog.getId());
        return Result.ok(objectMap);
    }
}
