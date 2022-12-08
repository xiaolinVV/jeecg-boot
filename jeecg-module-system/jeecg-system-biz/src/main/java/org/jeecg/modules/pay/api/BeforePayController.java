package org.jeecg.modules.pay.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.entity.StoreSetting;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.store.service.IStoreSettingService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("before/pay")
@Controller
@Slf4j
public class BeforePayController {
    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IStoreSettingService iStoreSettingService;

    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;


    @Autowired
    private ISysDictService iSysDictService;
    /**
     * 开店支付回调（加盟商）
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("registeredShopsOpenStore")
    @ResponseBody
    public Object openStore(String id, HttpServletRequest request){

        log.info("id信息："+id);

        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap = Maps.newHashMap();
        StoreManage storeManage = iStoreManageService.getById(id);
        if(StringUtils.isNotBlank(id)&&storeManage!=null){
            StoreSetting storeSetting=iStoreSettingService.getOne(new LambdaQueryWrapper<>());
            if(storeSetting!=null) {
                objectMap.put("storeManageUrl", storeSetting.getManageAddress());
                objectMap.put("userName", storeManage.getBossPhone());
                objectMap.put("passwd", storeSetting.getInitialPasswd());
            }
            result.setResult(objectMap);
            result.success("开店冲账成功！！！");
            return result;
        }else{
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

//官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {
                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    iStoreManageService.backSucess(params.get("out_trade_no"));
                    //商家端调用支付回调
                    iStoreManageService.paySuccessSJD(params.get("out_trade_no"));
                }
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
//汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    log.info(tradeNo);
                    iStoreManageService.backSucess(tradeNo);
                    //商家端调用支付回调
                    iStoreManageService.paySuccessSJD(tradeNo);
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }
        }
        return "SUCCESS";
    }


    /**
     * 余额支付回调
     * @param request
     * @return
     */
    @RequestMapping("balance")
    @ResponseBody
    public Object balancePay(String id,HttpServletRequest request) {
        Result<String> result =new Result();
        Object str;
        if(id!=null){
            //小程序请求
            StoreRechargeRecord storeRechargeRecord = iStoreRechargeRecordService.getById(id);
            if(storeRechargeRecord!=null){
                storeRechargeRecord.setBackStatus("1");
                storeRechargeRecord.setBackTimes(storeRechargeRecord.getBackTimes().add( new BigDecimal("1")));
                iStoreRechargeRecordService.updateById(storeRechargeRecord);
                result.success("充值成功!");
                return result;
            }else{
                return  result.error500("充值失败!");
            }

        }else {
            //腾讯请求
            String xmlMsg = HttpKit.readData(request);
            log.info("支付通知=" + xmlMsg);
            Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
            String returnCode = params.get("return_code");
            if (WxPayKit.codeIsOk(returnCode)) {
                str = iStoreManageService.pay(params.get("out_trade_no"));
            }else{
                return "SUCCESS";
            }

        }

        return str;
    }

}
