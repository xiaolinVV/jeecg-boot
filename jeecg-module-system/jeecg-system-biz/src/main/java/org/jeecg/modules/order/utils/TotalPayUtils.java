package org.jeecg.modules.order.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.pay.entity.PayCertificateLog;
import org.jeecg.modules.pay.entity.PayGiftBagLog;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.service.IPayCertificateLogService;
import org.jeecg.modules.pay.service.IPayGiftBagLogService;
import org.jeecg.modules.pay.service.IPayOrderCarLogService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.service.IStoreCashierRoutingService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
@Log
public class TotalPayUtils {

    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IPayOrderCarLogService iPayOrderCarLogService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private PayUtils payUtils;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IOrderListService iOrderListService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IPayCertificateLogService iPayCertificateLogService;

    @Autowired
    private IMarketingGiftBagService iMarketingGiftBagService;

    @Autowired
    private IMarketingIntegralSettingService iMarketingIntegralSettingService;


    @Autowired
    @Lazy
    private IOrderProviderListService iOrderProviderListService;

    @Autowired
    @Lazy
    private IOrderProviderGoodRecordService iOrderProviderGoodRecordService;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;

    @Autowired
    private IPayGiftBagLogService iPayGiftBagLogService;

    @Autowired
    private IMarketingGiftBagRecordService iMarketingGiftBagRecordService;

    @Autowired
    private IMarketingGiftRoutingService iMarketingGiftRoutingService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    @Autowired
    private IStoreCashierRoutingService iStoreCashierRoutingService;




    /**
     * 订单支付
     *
     * @param allTotalPrice
     * @param payOrderLog
     * @param memberId
     * @param request
     * @param softModel
     * @return
     */
    public Map<String,Object> payOrder(BigDecimal allTotalPrice, Map<String,Object> payOrderLog, String memberId, HttpServletRequest request,String softModel){
        String isIntegral="0";
        String removePay="0";

            BigDecimal integralPrice=new BigDecimal(0);
        BigDecimal integral=new BigDecimal(0);

            //优惠后期删除
            Map<String,Object> discountMap=Maps.newLinkedHashMap();
            //0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；
            discountMap.put("0","1");
            discountMap.put("1","1");
            discountMap.put("2","1");
            discountMap.put("3","1");
            discountMap.put("4","1");

            MemberList memberList=iMemberListService.getById(memberId);



            //专区比例控制
            if(payOrderLog.containsKey("goods")){
                String orderId=payOrderLog.get("goods").toString();
                OrderList orderList=iOrderListService.getById(orderId);

                if(orderList.getOrderType().equals("5")) {
                    MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(orderList.getActiveId());
                    //判断是否使用积分支付
                    if(marketingPrefecture.getIsIntegral().equals("1")){
                        //获取最高可抵扣比例
                        OrderProviderList orderProviderList=iOrderProviderListService.getOne(new LambdaQueryWrapper<OrderProviderList>().eq(OrderProviderList::getOrderListId,orderList.getId()).last("limit 1"));
                        OrderProviderGoodRecord orderProviderGoodRecord=iOrderProviderGoodRecordService.getOne(new LambdaQueryWrapper<OrderProviderGoodRecord>().eq(OrderProviderGoodRecord::getOrderProviderListId,orderProviderList.getId()).last("limit 1"));
                        MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(new LambdaQueryWrapper<MarketingPrefectureGood>().eq(MarketingPrefectureGood::getMarketingPrefectureId,marketingPrefecture.getId()).eq(MarketingPrefectureGood::getGoodListId,orderProviderGoodRecord.getGoodListId()).eq(MarketingPrefectureGood::getStatus,"1"));
                        MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification=iMarketingPrefectureGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingPrefectureGoodSpecification>().eq(MarketingPrefectureGoodSpecification::getMarketingPrefectureGoodId,marketingPrefectureGood.getId()).eq(MarketingPrefectureGoodSpecification::getGoodSpecificationId,orderProviderGoodRecord.getGoodSpecificationId()));
                        BigDecimal integralTotalPrice=allTotalPrice.multiply(marketingPrefectureGoodSpecification.getProportionIntegral()).divide(new BigDecimal(100),2,RoundingMode.DOWN);

                        MarketingIntegralSetting marketingIntegralSetting=iMarketingIntegralSettingService.getMarketingIntegralSetting();
                        if(marketingIntegralSetting!=null){
                            if(memberList.getIntegral().multiply(marketingIntegralSetting.getPrice()).subtract(integralTotalPrice).doubleValue()>=0){
                                integral=integralTotalPrice.divide(marketingIntegralSetting.getPrice(),2,RoundingMode.HALF_UP);
                                integralPrice=integralTotalPrice;
                                allTotalPrice=allTotalPrice.subtract(integralTotalPrice);
                            }else{
                                allTotalPrice=allTotalPrice.subtract(memberList.getIntegral().multiply(marketingIntegralSetting.getPrice()));
                                integral=memberList.getIntegral();
                                integralPrice=integral.multiply(marketingIntegralSetting.getPrice());
                            }
                        }
                    }
                    isIntegral=marketingPrefecture.getIsIntegral();
                    removePay=marketingPrefecture.getRemovePay();
                }
            }

            //组建吊起支付信息
            PayOrderCarLog payOrderCarLog = new PayOrderCarLog();
            payOrderCarLog.setDelFlag("0");
            //订单日志
            payOrderCarLog.setPayOrderLog(JSON.toJSONString(payOrderLog));
            payOrderCarLog.setPayStatus("0");
            payOrderCarLog.setBackStatus("0");
            payOrderCarLog.setBackTimes(new BigDecimal(0));
            payOrderCarLog.setAllTotalPrice(allTotalPrice);
            payOrderCarLog.setMemberListId(memberId);
            payOrderCarLog.setIntegral(integral);
            payOrderCarLog.setIntegralPrice(integralPrice);
            iPayOrderCarLogService.saveOrUpdate(payOrderCarLog);

            Map<String,Object> resultMap=payOrderPub(allTotalPrice,payOrderCarLog,memberId,request,softModel,isIntegral,removePay);
            resultMap.put("discount",discountMap);
        return resultMap;
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
    public Map<String,Object>  payOrderCarLogById(HttpServletRequest request,String payModel, BigDecimal welfarePayments,BigDecimal balance,String payOrderCarLogId,String memberId){
        Map<String, Object> objectMap=Maps.newHashMap();
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        //重组支付日志信息
        PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getById(payOrderCarLogId);
        payOrderCarLog.setId(null);
        payOrderCarLog.setBalance(balance);
        payOrderCarLog.setWelfarePayments(welfarePayments);
        payOrderCarLog.setPayModel("2");
        iPayOrderCarLogService.save(payOrderCarLog);
        //价格
        BigDecimal allTotalPrice=new BigDecimal(0);
        allTotalPrice=allTotalPrice.add(payOrderCarLog.getAllTotalPrice());
        log.info("支付的余额："+balance);

        log.info("支付的积分："+welfarePayments);
        if(allTotalPrice.subtract(balance).subtract(welfarePayments).doubleValue()<=0){
            payOrderCarLog.setAllTotalPrice(new BigDecimal(0));
            allTotalPrice=new BigDecimal(0);
        }else{
            allTotalPrice=allTotalPrice.subtract(balance).subtract(welfarePayments).setScale(2, RoundingMode.HALF_UP);
        }

        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("notifyUrl");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {
            log.info("实际支付金额：allTotalPrice="+allTotalPrice);

            //分账设置
            List<Map<String, String>> divMembers = new ArrayList<>();
            String payOrderLog = payOrderCarLog.getPayOrderLog();
            JSONObject payOrderLogJsonObject = JSON.parseObject(payOrderLog);
            //店铺订单分账
            if (payOrderLogJsonObject.containsKey("storeGoods")) {
                log.info("店铺订单处理" + payOrderCarLog.getId());
                JSONArray storeGoodsJsonArray = payOrderLogJsonObject.getJSONArray("storeGoods");
                for (Object s:storeGoodsJsonArray) {
                    String storeGood=(String) s;
                    divMembers=iStoreCashierRoutingService.independentAccountOrder(divMembers,storeGood);
                }
            }
            log.info("订单支付分账信息："+JSON.toJSONString(divMembers));

            Map<String,String> resultMap=payUtils.pay(payModel,request,payOrderCarLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),divMembers);

            jsonStr=resultMap.get("jsonStr");

            if(resultMap.get("id")!=null) {
                payOrderCarLog.setSerialNumber(resultMap.get("id"));
            }

            objectMap.put("queryUrl",resultMap.get("queryUrl"));
            //支付日志
            payOrderCarLog.setPayParam(resultMap.get("params"));

            payOrderCarLog.setPayModel(payModel);
            payOrderCarLog.setPayPrice(allTotalPrice);
            //保存支付日志
            iPayOrderCarLogService.saveOrUpdate(payOrderCarLog);
        }

        objectMap.put("payOrderCarLogId",payOrderCarLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payOrderCarLog.getId());
        return objectMap;
    }


    /**
     * 兑换券日志支付
     *
     * @param request
     * @param payModel
     * @param welfarePayments
     * @param balance
     * @param payCertificateLogId
     * @param memberId
     * @return
     */
    public Map<String,Object> payCertificateLogById(HttpServletRequest request,String payModel, BigDecimal welfarePayments,BigDecimal balance,String payCertificateLogId,String memberId){
        Map<String, Object> objectMap=Maps.newHashMap();
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        //重组支付日志信息
        PayCertificateLog payCertificateLog=iPayCertificateLogService.getById(payCertificateLogId);
        payCertificateLog.setId(null);
        payCertificateLog.setBalance(balance);
        payCertificateLog.setWelfarePayments(welfarePayments);
        payCertificateLog.setPayModel("2");
        iPayCertificateLogService.save(payCertificateLog);
        Map<String,Object> discountMap=JSON.parseObject(payCertificateLog.getDiscount());
        //价格
        BigDecimal allTotalPrice=new BigDecimal(0);
        allTotalPrice=allTotalPrice.add(payCertificateLog.getTotalFee());
        if(balance.doubleValue()!=0){
            balance=balance.divide(((JSONObject) discountMap).getBigDecimal("2"),2,RoundingMode.HALF_UP);
        }
        log.info("支付的余额："+balance);
        if(welfarePayments.doubleValue()!=0){
            welfarePayments=welfarePayments.multiply(integralValue).divide(((JSONObject) discountMap).getBigDecimal("3"),2,RoundingMode.HALF_UP);
        }
        log.info("支付的积分："+welfarePayments);
        if(allTotalPrice.subtract(balance).subtract(welfarePayments).doubleValue()<=0){
            payCertificateLog.setTotalFee(new BigDecimal(0));
            allTotalPrice=new BigDecimal(0);
        }else{
            allTotalPrice=allTotalPrice.subtract(balance).subtract(welfarePayments).setScale(2, RoundingMode.HALF_UP);
        }


        log.info("优惠比例："+JSON.toJSONString(discountMap));

        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("certificateNotifyUrl");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {
            //判断支付方式
            //微信支付
            if(payModel.equals("0")){
                allTotalPrice=allTotalPrice.multiply(((JSONObject) discountMap).getBigDecimal("0")).setScale(2,RoundingMode.HALF_UP);
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payCertificateLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),null);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payCertificateLog.setSerialNumber(resultMap.get("id"));
                }

                objectMap.put("queryUrl",resultMap.get("queryUrl"));
                //支付日志
                payCertificateLog.setPayParam(resultMap.get("params"));

                payCertificateLog.setPayModel(payModel);
                payCertificateLog.setPayPrice(allTotalPrice);
            }

            //支付宝支付
            if(payModel.equals("1")){
                allTotalPrice=allTotalPrice.multiply(((JSONObject) discountMap).getBigDecimal("1")).setScale(2,RoundingMode.HALF_UP);
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //支付宝支付
                Map<String,String> resultMap=payUtils.payZhifubao(payCertificateLog.getId(),allTotalPrice,notifyUrl,null);

                jsonStr=resultMap.get("jsonStr");
                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                if(resultMap.get("id")!=null) {
                    payCertificateLog.setSerialNumber(resultMap.get("id"));
                }
                //支付日志
                payCertificateLog.setPayParam(resultMap.get("params"));

                payCertificateLog.setPayModel(payModel);
                payCertificateLog.setPayPrice(allTotalPrice);
            }
            //保存支付日志
            iPayCertificateLogService.saveOrUpdate(payCertificateLog);
        }

        objectMap.put("payCertificateLogId",payCertificateLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payCertificateLog.getId());
        return objectMap;
    }



    /**
     * 订单支付公共方法
     *
     * @param allTotalPrice
     * @param payOrderCarLog
     * @param memberId
     * @param request
     * @param softModel
     * @return
     */
    private Map<String,Object> payOrderPub(BigDecimal allTotalPrice,PayOrderCarLog payOrderCarLog,String memberId, HttpServletRequest request,String softModel,String isIntegral,String removePay){
        Map<String,Object> objectMap= Maps.newHashMap();

        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("notifyUrl");

        //获取用户信息
        MemberList memberList=iMemberListService.getById(memberId);

        objectMap.put("memberListId",memberList.getId());

        String jsonStr="1";

        //cashier_desk_state   1   收银台状态。0：关闭；1：开启；
        String cashierDeskState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "cashier_desk_state"),"0");
        objectMap.put("cashierDeskState",cashierDeskState);
            //不使用收银台
            if(cashierDeskState.equals("0")){

                if(allTotalPrice.doubleValue()>0) {

                    //微信支付
                    Map<String, String> resultMap = payUtils.payWeixin(request, payOrderCarLog.getId(), allTotalPrice, notifyUrl, memberList.getOpenid(),null);

                    jsonStr = resultMap.get("jsonStr");

                    if (resultMap.get("id") != null) {
                        payOrderCarLog.setSerialNumber(resultMap.get("id"));
                    }

                    //支付日志
                    payOrderCarLog.setPayParam(resultMap.get("params"));

                    //保存支付日志
                    iPayOrderCarLogService.saveOrUpdate(payOrderCarLog);
                }else{
                    jsonStr="0";
                }
            }
            //使用收银台
            if(cashierDeskState.equals("1")){
                //0,1,2,3  默认支付渠道设置，0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；4；sla支付     默认值为：2   余额支付
                String defaultPay = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "default_pay"),"2");
                //支付方式默认值为默认支付方式
                String payModel=defaultPay;

                //免单专区订单
                if(JSON.parseObject(payOrderCarLog.getPayOrderLog()).getString("marketingFreeGoods")!=null){
                    String freePay = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "free_pay"),"2");
                    payModel=freePay;
                }

                //中奖拼团支付方式
                if(JSON.parseObject(payOrderCarLog.getPayOrderLog()).getString("goods")!=null){
                    String orderId=JSON.parseObject(payOrderCarLog.getPayOrderLog()).getString("goods");
                    OrderList orderList=iOrderListService.getById(orderId);
                    if(orderList.getOrderType().equals("4")) {
                        String winningGroupPay = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "winning_group_pay"),"2");
                        payModel=winningGroupPay;
                    }
                }

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
                    }
                }

                if(isIntegral.equals("1")){
                    List<String> payModels = Lists.newArrayList();
                    Arrays.asList(StringUtils.split(payModel, ","))
                            .stream().filter(model -> !model.equals("3"))
                            .forEach(model -> {
                                payModels.add(model);
                            });
                    payModel = StringUtils.join(payModels, ",");
                }
                if(removePay.equals("1")){
                    List<String> payModels = Lists.newArrayList();
                    Arrays.asList(StringUtils.split(payModel, ","))
                            .stream().filter(model -> !model.equals("2"))
                            .forEach(model -> {
                                payModels.add(model);
                            });
                    payModel = StringUtils.join(payModels, ",");
                }

                log.info("收银台支付方式payModel："+payModel);

                objectMap.put("payModel",payModel);

                String transactionPasswordState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "transaction_password_state"),"0");
                objectMap.put("transactionPasswordState",transactionPasswordState);
                jsonStr="1";

                //小程序原始id
                String originalId = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "original_id"),"0");

                objectMap.put("originalId",originalId);
                objectMap.put("allTotalPrice",allTotalPrice);
                //获取会员余额和积分信息

                objectMap.put("welfarePayments",memberList.getWelfarePayments());
                objectMap.put("balance",memberList.getBalance());
                objectMap.put("softModel",softModel);
                objectMap.put("integral",payOrderCarLog.getIntegral());
                objectMap.put("memberIntegral",memberList.getIntegral());
                MarketingIntegralSetting marketingIntegralSetting=iMarketingIntegralSettingService.getMarketingIntegralSetting();
                if(marketingIntegralSetting!=null){
                    objectMap.put("integralPrice",payOrderCarLog.getIntegralPrice());
                }else{
                    objectMap.put("integralPrice",0);
                }


            }

        objectMap.put("payOrderCarLogId",payOrderCarLog.getId());
        objectMap.put("notifyUrl",notifyUrl+"?id="+payOrderCarLog.getId());
        objectMap.put("jsonStr",jsonStr);
        return objectMap;
    }

    /**
     * 兑换券支付
     *
     * @param request
     * @param marketingCertificateId 券规则id
     * @param quantity 数量
     * @param memberId  会员id
     * @param longitude
     * @param latitude
     * @param totalPrice  总金额
     * @param sysUserId  店铺id
     * @param softModel  软件模型
     * @return
     */
    public Map<String,Object> payCertificate(HttpServletRequest request,
                                             String marketingCertificateId,
                                             BigDecimal quantity,
                                             String memberId,
                                             BigDecimal longitude,
                                             BigDecimal latitude,
                                             BigDecimal totalPrice,
                                             String sysUserId,
                                             String softModel,
                                             String type,
                                             String id){
        Map<String,Object> objectMap= Maps.newHashMap();

        //设置回调地址
        String notifyUrl = notifyUrlUtils.getNotify("certificateNotifyUrl");


        Map<String,Object> marketingCertificateMap=Maps.newHashMap();
        marketingCertificateMap.put("marketingCertificateId",marketingCertificateId);
        marketingCertificateMap.put("quantity",quantity);
        if (type.equals("2")){
            marketingCertificateMap.put("marketingCertificateSeckillListId",id);
        }
        if (type.equals("1")){
            marketingCertificateMap.put("marketingCertificateGroupListId",id);
        }
        if (type.equals("3")){
            marketingCertificateMap.put("marketingCertificateGroupManageId",id);
        }
        //组建吊起支付信息
        PayCertificateLog payCertificateLog = new PayCertificateLog();
        payCertificateLog.setDelFlag("0");
        //订单日志
        payCertificateLog.setPayLog(JSON.toJSONString(marketingCertificateMap));
        payCertificateLog.setPayStatus("0");
        payCertificateLog.setBackStatus("0");
        payCertificateLog.setBackTimes(new BigDecimal(0));
        payCertificateLog.setMemberListId(memberId);
        payCertificateLog.setLongitude(longitude);
        payCertificateLog.setLatitude(latitude);
        payCertificateLog.setTotalFee(totalPrice);
        payCertificateLog.setBuyType(type);
        payCertificateLog.setSysUserId(sysUserId);
        Map<String,Object> discountMap=Maps.newLinkedHashMap();
        //0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；
        discountMap.put("0","1");
        discountMap.put("1","1");
        discountMap.put("2","1");
        discountMap.put("3","1");
        objectMap.put("discount",discountMap);

        payCertificateLog.setDiscount(JSON.toJSONString(discountMap));

        iPayCertificateLogService.saveOrUpdate(payCertificateLog);

        //获取用户信息
        MemberList memberList=iMemberListService.getById(memberId);

        objectMap.put("memberListId",memberList.getId());

        String jsonStr="0";

        if(totalPrice.doubleValue()>0) {

            //cashier_desk_state   1   收银台状态。0：关闭；1：开启；
            String cashierDeskState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "cashier_desk_state"),"0");
            objectMap.put("cashierDeskState",cashierDeskState);
            //不使用收银台
            if(cashierDeskState.equals("0")){

                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payCertificateLog.getId(),totalPrice,notifyUrl,memberList.getOpenid(),null);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payCertificateLog.setSerialNumber(resultMap.get("id"));
                }

                //支付日志
                payCertificateLog.setPayParam(resultMap.get("params"));

                //保存支付日志
                iPayCertificateLogService.saveOrUpdate(payCertificateLog);
            }
            //使用收银台
            if(cashierDeskState.equals("1")){
                //0,1,2,3  默认支付渠道设置，0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；     默认值为：2   余额支付
                String defaultPay = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "default_pay"),"2");
                //支付方式默认值为默认支付方式
                String payModel=defaultPay;

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
                    }
                }

                log.info("收银台支付方式payModel："+payModel);

                objectMap.put("payModel",payModel);

                String transactionPasswordState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "transaction_password_state"),"0");
                objectMap.put("transactionPasswordState",transactionPasswordState);
                jsonStr="1";

                //小程序原始id
                String originalId = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "original_id"),"0");

                objectMap.put("originalId",originalId);
                objectMap.put("allTotalPrice",totalPrice);
                //获取会员余额和积分信息

                objectMap.put("welfarePayments",memberList.getWelfarePayments());
                objectMap.put("balance",memberList.getBalance());
                objectMap.put("softModel",softModel);

            }
        }
        objectMap.put("payCertificateLogId",payCertificateLog.getId());
        objectMap.put("notifyUrl",notifyUrl+"?id="+payCertificateLog.getId());
        objectMap.put("jsonStr",jsonStr);

        return objectMap;
    }




    /**
     *
     * 礼包支付
     *
     * @param memberId
     * @param request
     * @param marketingGiftBagId
     * @param sysUserId
     * @param longitude
     * @param latitude
     * @param tMemberId
     * @param softModel
     * @return
     */
    public Map<String,Object> payGiftBag(String memberId,
                                         HttpServletRequest request,
                                         String marketingGiftBagId,
                                         String sysUserId,
                                         String longitude,
                                         String latitude,
                                         String tMemberId,
                                         String softModel,
                                         String marketingGiftBagRecordId){
        Map<String,Object> objectMap= Maps.newHashMap();

        MarketingGiftBag marketingGiftBag=iMarketingGiftBagService.getById(marketingGiftBagId);

        //设置回调地址

        String notifyUrl=notifyUrlUtils.getNotify("notifyMarketingGiftBagUrl");
        //获取用户信息
        MemberList memberList=iMemberListService.getById(memberId);
        //生成礼包支付记录
        PayGiftBagLog payGiftBagLog=new PayGiftBagLog();
        payGiftBagLog.setBackStatus("0");
        payGiftBagLog.setPayStatus("0");
        payGiftBagLog.setMemberListId(memberId);
        if(StringUtils.isNotBlank(marketingGiftBagRecordId)){
            payGiftBagLog.setMarketingGiftBagRecordId(marketingGiftBagRecordId);
            MarketingGiftBagRecord marketingGiftBagRecord=iMarketingGiftBagRecordService.getById(marketingGiftBagRecordId);
            if(marketingGiftBagRecord.getResiduePayTimes().intValue()==1){
                payGiftBagLog.setAllTotalPrice(marketingGiftBagRecord.getPrice().subtract(marketingGiftBagRecord.getTotalFee()));

            }else{
                payGiftBagLog.setAllTotalPrice(marketingGiftBagRecord.getPrice().subtract(marketingGiftBagRecord.getTotalFee()).multiply(new BigDecimal(RandomUtils.nextInt(5,80))).divide(new BigDecimal(100),2,RoundingMode.DOWN));
            }
        }else{
            if(marketingGiftBag.getPayTimes().intValue()==1) {
                payGiftBagLog.setAllTotalPrice(marketingGiftBag.getPrice());
            }else{
                payGiftBagLog.setAllTotalPrice(marketingGiftBag.getPrice().multiply(new BigDecimal(RandomUtils.nextInt(5,80))).divide(new BigDecimal(100),2,RoundingMode.DOWN));
            }
        }
        if(StringUtils.isNotBlank(sysUserId)){
            payGiftBagLog.setSysUserId(sysUserId);
        }

        payGiftBagLog.setMarketingGiftBagId(marketingGiftBag.getId());
        if (StringUtils.isNotBlank(tMemberId)){
            payGiftBagLog.setTMemberId(tMemberId);
        }
        if(StringUtils.isNotBlank(longitude)){
            payGiftBagLog.setLongitude(longitude);
        }
        if(StringUtils.isNotBlank(latitude)){
            payGiftBagLog.setLatitude(latitude);
        }

        Map<String,Object> discountMap=Maps.newLinkedHashMap();
        //0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；
        discountMap.put("0","1");
        discountMap.put("1","1");
        discountMap.put("2","1");
        discountMap.put("3","1");
        objectMap.put("discount",discountMap);

        payGiftBagLog.setDiscount(JSON.toJSONString(discountMap));

        iPayGiftBagLogService.saveOrUpdate(payGiftBagLog);

        objectMap.put("memberListId",memberList.getId());

        String jsonStr="0";

        BigDecimal totalPrice=payGiftBagLog.getAllTotalPrice();

        if(totalPrice.doubleValue()>0) {

            //cashier_desk_state   1   收银台状态。0：关闭；1：开启；
            String cashierDeskState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "cashier_desk_state"),"0");
            objectMap.put("cashierDeskState",cashierDeskState);
            //不使用收银台
            if(cashierDeskState.equals("0")){

                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payGiftBagLog.getId(),totalPrice,notifyUrl,memberList.getOpenid(),null);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payGiftBagLog.setSerialNumber(resultMap.get("id"));
                }

                //支付日志
                payGiftBagLog.setPayParam(resultMap.get("params"));

                //保存支付日志
                iPayGiftBagLogService.saveOrUpdate(payGiftBagLog);
            }
            //使用收银台
            if(cashierDeskState.equals("1")){
                //0,1,2,3  默认支付渠道设置，0：微信支付；1：支付宝支付；2：余额支付；3：积分支付；     默认值为：2   余额支付
                String giftBagPay = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "gift_bag_pay"),"2");
                //支付方式默认值为默认支付方式
                String payModel=giftBagPay;

                if(!marketingGiftBag.getPaymentMode().equals("-1")){
                    payModel=marketingGiftBag.getPaymentMode();
                }

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
                    }
                }

                log.info("收银台支付方式payModel："+payModel);

                objectMap.put("payModel",payModel);

                String transactionPasswordState = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "transaction_password_state"),"0");
                objectMap.put("transactionPasswordState",transactionPasswordState);
                jsonStr="1";

                //小程序原始id
                String originalId = StringUtils.defaultString(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "original_id"),"0");

                objectMap.put("originalId",originalId);
                objectMap.put("allTotalPrice",totalPrice);
                //获取会员余额和积分信息

                objectMap.put("welfarePayments",memberList.getWelfarePayments());
                objectMap.put("balance",memberList.getBalance());
                objectMap.put("softModel",softModel);

            }
        }
        objectMap.put("payGiftBagLogId",payGiftBagLog.getId());
        objectMap.put("notifyUrl",notifyUrl+"?id="+payGiftBagLog.getId());
        objectMap.put("jsonStr",jsonStr);
        return objectMap;
    }



    /**
     * 礼包日志支付
     *
     * @param request
     * @param payModel
     * @param welfarePayments
     * @param balance
     * @param payGiftBagLogId
     * @param memberId
     * @return
     */
    public Map<String,Object> payMarketingGiftBagRecordById(HttpServletRequest request,String payModel, BigDecimal welfarePayments,BigDecimal balance,String payGiftBagLogId,String memberId){
        Map<String, Object> objectMap=Maps.newHashMap();
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        //重组支付日志信息
        PayGiftBagLog payGiftBagLog=iPayGiftBagLogService.getById(payGiftBagLogId);
        payGiftBagLog.setId(null);
        payGiftBagLog.setBalance(balance);
        payGiftBagLog.setWelfarePayments(welfarePayments);
        payGiftBagLog.setPayModel("2");
        iPayGiftBagLogService.save(payGiftBagLog);
        Map<String,Object> discountMap=JSON.parseObject(payGiftBagLog.getDiscount());
        //价格
        BigDecimal allTotalPrice=new BigDecimal(0);
        allTotalPrice=allTotalPrice.add(payGiftBagLog.getAllTotalPrice());
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

        String notifyUrl = notifyUrlUtils.getNotify("notifyMarketingGiftBagUrl");
        String jsonStr="0";

        if(allTotalPrice.doubleValue()>0) {

            //分账设置
            List<MarketingGiftRouting> marketingGiftRoutings=iMarketingGiftRoutingService.list(new LambdaQueryWrapper<MarketingGiftRouting>().eq(MarketingGiftRouting::getMarketingGiftBagId,payGiftBagLog.getMarketingGiftBagId()));
            List<Map<String, String>> divMembers = new ArrayList<>();
            if(marketingGiftRoutings.size()>0){
                BigDecimal collectingCommission=allTotalPrice;
                for (MarketingGiftRouting marketingGiftRouting:marketingGiftRoutings) {
                    Map<String, String> divMember2 = new HashMap<>(3);
                    divMember2.put("member_id", marketingGiftRouting.getId());
                    BigDecimal div=allTotalPrice.multiply(marketingGiftRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN);
                    divMember2.put("amount",div.toString());
                    divMember2.put("fee_flag", "N");
                    divMembers.add(divMember2);
                    collectingCommission=collectingCommission.subtract(div);
                }
                Map<String, String> divMember = new HashMap<>(3);
                divMember.put("member_id", "0");
                divMember.put("amount",collectingCommission .toString());
                divMember.put("fee_flag", "Y");
                divMembers.add(divMember);
            }

            //判断支付方式
            //微信支付
            if(payModel.equals("0")){
                allTotalPrice=allTotalPrice.multiply(((JSONObject) discountMap).getBigDecimal("0")).setScale(2,RoundingMode.HALF_UP);
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //微信支付
                Map<String,String> resultMap=payUtils.payWeixin(request,payGiftBagLog.getId(),allTotalPrice,notifyUrl,iMemberListService.getById(memberId).getOpenid(),divMembers);

                jsonStr=resultMap.get("jsonStr");

                if(resultMap.get("id")!=null) {
                    payGiftBagLog.setSerialNumber(resultMap.get("id"));
                }

                objectMap.put("queryUrl",resultMap.get("queryUrl"));
                //支付日志
                payGiftBagLog.setPayParam(resultMap.get("params"));
                payGiftBagLog.setRemark(JSON.toJSONString(divMembers));
                payGiftBagLog.setPayModel(payModel);
                payGiftBagLog.setPayPrice(allTotalPrice);
            }

            //支付宝支付
            if(payModel.equals("1")){
                allTotalPrice=allTotalPrice.multiply(((JSONObject) discountMap).getBigDecimal("1")).setScale(2,RoundingMode.HALF_UP);
                log.info("实际支付金额：allTotalPrice="+allTotalPrice);
                //支付宝支付
                Map<String,String> resultMap=payUtils.payZhifubao(payGiftBagLog.getId(),allTotalPrice,notifyUrl,divMembers);

                jsonStr=resultMap.get("jsonStr");
                objectMap.put("queryUrl",resultMap.get("queryUrl"));

                if(resultMap.get("id")!=null) {
                    payGiftBagLog.setSerialNumber(resultMap.get("id"));
                }
                //支付日志
                payGiftBagLog.setPayParam(resultMap.get("params"));
                payGiftBagLog.setRemark(JSON.toJSONString(divMembers));
                payGiftBagLog.setPayModel(payModel);
                payGiftBagLog.setPayPrice(allTotalPrice);
            }
            //保存支付日志
            iPayGiftBagLogService.saveOrUpdate(payGiftBagLog);
        }
        objectMap.put("payGiftBagLogId",payGiftBagLog.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("notifyUrl",notifyUrl+"?id="+payGiftBagLog.getId());
        return objectMap;
    }

}
