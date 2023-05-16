package org.jeecg.modules.pay.api;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupRecord;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagRecordService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.pay.entity.*;
import org.jeecg.modules.pay.service.*;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.store.service.IStoreShouyinRecordService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付回调
 */
@RequestMapping("front/pay")
@Controller
@Slf4j
public class FrontPayController {

    @Autowired
    private IPayOrderCarLogService iPayOrderCarLogService;

    @Autowired
    private IOrderListService iOrderListService;

    @Autowired
    private IOrderStoreListService iOrderStoreListService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingGiftBagService iMarketingGiftBagService;

    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    private IPayCertificateLogService iPayCertificateLogService;

    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;

    @Autowired
    private IMarketingGiftBagBatchService iMarketingGiftBagBatchService;

    @Autowired
    private IMarketingGiftBagRecordBatchService iMarketingGiftBagRecordBatchService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMarketingCertificateGroupRecordService iMarketingCertificateGroupRecordService;

    @Autowired
    private IPayBalanceLogService iPayBalanceLogService;


    @Autowired
    private IMarketingZoneGroupManageService iMarketingZoneGroupManageService;

    @Autowired
    private IPayZoneGroupLogService iPayZoneGroupLogService;


    @Autowired
    private IMarketingBusinessGiftListService iMarketingBusinessGiftListService;

    @Autowired
    private IPayBusinessGiftLogService iPayBusinessGiftLogService;

    @Autowired
    private IMarketingIntegralRecordService iMarketingIntegralRecordService;

    @Autowired
    private IPayGiftBagLogService iPayGiftBagLogService;

    @Autowired
    private IPayShouyinLogService iPayShouyinLogService;

    @Autowired
    private IStoreShouyinRecordService iStoreShouyinRecordService;

    @Autowired
    private IPayMarketingStoreGiftbagLogService iPayMarketingStoreGiftbagLogService;

    @Autowired
    private IMarketingStoreGiftbagRecordService iMarketingStoreGiftbagRecordService;

    @Autowired
    private IOrderRefundListService orderRefundListService;

    @Autowired
    private IMarketingDiscountCouponService marketingDiscountCouponService;

    @Autowired
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;


    /**
     * 兑换券回调（加盟商已优化）
     *
     * @param id
     * @param memberId
     * @return
     */
    @RequestMapping("submitCertificate")
    @ResponseBody
    public Result<?> submitCertificate(String id,
                                       HttpServletRequest request,
                                       @RequestAttribute(value = "memberId", required = false) String memberId) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(memberId)) {
            PayCertificateLog payCertificateLog = iPayCertificateLogService.getById(id);
            String blanceType = "";
            String welfarePaymentsType = "";
            if (payCertificateLog.getBuyType().equals("0")) {
                blanceType = "16";
                welfarePaymentsType = "24";
            } else if (payCertificateLog.getBuyType().equals("1")) {
                blanceType = "13";
                welfarePaymentsType = "21";
            } else if (payCertificateLog.getBuyType().equals("2")) {
                blanceType = "15";
                welfarePaymentsType = "23";
            } else if (payCertificateLog.getBuyType().equals("3")) {
                blanceType = "13";
                welfarePaymentsType = "21";
            }
            //扣除余额
            iMemberListService.subtractBlance(payCertificateLog.getMemberListId(), payCertificateLog.getBalance(), payCertificateLog.getId(), blanceType);
            //扣除积分
            iMemberWelfarePaymentsService.subtractWelfarePayments(payCertificateLog.getMemberListId(), payCertificateLog.getWelfarePayments(), welfarePaymentsType, payCertificateLog.getId(), "");
            if (payCertificateLog == null) {
                return Result.error("id参数找不到相关的支付日志");
            }
            //手机端支付冲账
            if (!memberId.equals(payCertificateLog.getMemberListId())) {
                return Result.error("冲账token中的信息不正确，请确认冲账的用户身份！！！");
            }
            //回调支付冲账
            payCertificateLog.setBackStatus("1");
            payCertificateLog.setBackTimes(payCertificateLog.getBackTimes().add(new BigDecimal(1)));
            iPayCertificateLogService.saveOrUpdate(payCertificateLog);
            if (StringUtils.isBlank(payCertificateLog.getPayParam())) {
                if (payCertificateLog.getPayStatus().equals("0")) {
                    map = iMarketingCertificateRecordService.paySuccess(payCertificateLog);
                    return Result.ok(map);
                }
            }
        } else {

            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {
                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    tradeNo = params.get("out_trade_no");
                    // 更新订单信息
                    // 发送通知等
                    PayCertificateLog payCertificateLog = iPayCertificateLogService.getById(tradeNo);
                    String blanceType = "";
                    String welfarePaymentsType = "";
                    if (payCertificateLog.getBuyType().equals("0")) {
                        blanceType = "16";
                        welfarePaymentsType = "24";
                    } else if (payCertificateLog.getBuyType().equals("1")) {
                        blanceType = "13";
                        welfarePaymentsType = "21";
                    } else if (payCertificateLog.getBuyType().equals("2")) {
                        blanceType = "15";
                        welfarePaymentsType = "23";
                    } else if (payCertificateLog.getBuyType().equals("3")) {
                        blanceType = "13";
                        welfarePaymentsType = "21";
                    }
                    //扣除余额
                    iMemberListService.subtractBlance(payCertificateLog.getMemberListId(), payCertificateLog.getBalance(), payCertificateLog.getId(), blanceType);
                    //扣除积分
                    iMemberWelfarePaymentsService.subtractWelfarePayments(payCertificateLog.getMemberListId(), payCertificateLog.getWelfarePayments(), welfarePaymentsType, payCertificateLog.getId(), "");
                    log.info(tradeNo);
                    if (payCertificateLog.getPayStatus().equals("0")) {
                        map = iMarketingCertificateRecordService.paySuccess(payCertificateLog);
                        return Result.ok(map);
                    }
                }
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return Result.ok(WxPayKit.toXml(xml));
            }
            //汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    // 更新订单信息
                    // 发送通知等
                    PayCertificateLog payCertificateLog = iPayCertificateLogService.getById(tradeNo);
                    String blanceType = "";
                    String welfarePaymentsType = "";
                    if (payCertificateLog.getBuyType().equals("0")) {
                        blanceType = "16";
                        welfarePaymentsType = "24";
                    } else if (payCertificateLog.getBuyType().equals("1")) {
                        blanceType = "13";
                        welfarePaymentsType = "21";
                    } else if (payCertificateLog.getBuyType().equals("2")) {
                        blanceType = "15";
                        welfarePaymentsType = "23";
                    } else if (payCertificateLog.getBuyType().equals("3")) {
                        blanceType = "13";
                        welfarePaymentsType = "21";
                    }
                    //扣除余额
                    iMemberListService.subtractBlance(payCertificateLog.getMemberListId(), payCertificateLog.getBalance(), payCertificateLog.getId(), blanceType);
                    //扣除积分
                    iMemberWelfarePaymentsService.subtractWelfarePayments(payCertificateLog.getMemberListId(), payCertificateLog.getWelfarePayments(), welfarePaymentsType, payCertificateLog.getId(), "");
                    log.info(tradeNo);
                    if (payCertificateLog.getPayStatus().equals("0")) {
                        map = iMarketingCertificateRecordService.paySuccess(payCertificateLog);
                        log.info("回调数据" + map);
                    } else if (payCertificateLog.getPayStatus().equals("1")) {
                        MarketingCertificateGroupRecord marketingCertificateGroupRecord = iMarketingCertificateGroupRecordService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupRecord>()
                                .eq(MarketingCertificateGroupRecord::getDelFlag, "0")
                                .eq(MarketingCertificateGroupRecord::getPayCertificateLogId, payCertificateLog.getId())
                        );
                        map.put("marketingCertificateGroupRecordId", marketingCertificateGroupRecord.getId());
                        map.put("code", "SUCCESS");
                    }
                    return Result.ok(map);
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }
        }
        PayCertificateLog payCertificateLog = iPayCertificateLogService.getById(id);
        if (payCertificateLog.getBuyType().equals("1") || payCertificateLog.getBuyType().equals("3")) {
            MarketingCertificateGroupRecord marketingCertificateGroupRecord = iMarketingCertificateGroupRecordService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupRecord>()
                    .eq(MarketingCertificateGroupRecord::getDelFlag, "0")
                    .eq(MarketingCertificateGroupRecord::getPayCertificateLogId, payCertificateLog.getId())
            );
            map.put("marketingCertificateGroupRecordId", marketingCertificateGroupRecord.getId());
            map.put("code", "SUCCESS");
        }
        return Result.ok(map);
    }

    /**
     * 退款回调
     *
     * @param request
     * @return
     */
    @RequestMapping("refundCallback")
    @ResponseBody
    public Result<String> refundCallback(HttpServletRequest request) {
        String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
        if (weixinMiniSoftPay.equals("1")) {
            String data = request.getParameter("data");
            log.info("退款回调的data数据：" + data);
            JSONObject jsonObject = JSON.parseObject(data);
            if (jsonObject.getString("status").equals("succeeded")) {
                String refundId = jsonObject.getString("refund_order_no");
                // 更新订单信息
                // 发送通知等
                OrderRefundList orderRefundList = orderRefundListService.getById(refundId);
                log.info("退款回调id：" + refundId);
                if (StrUtil.equals(orderRefundList.getStatus(), "3")) {
                    BigDecimal refund_amt = jsonObject.getBigDecimal("refund_amt");
                    orderRefundList.setActualRefundPrice(refund_amt);
                    orderRefundList.setStatus("4");
                    orderRefundList.setRefundJson(data);
                    orderRefundListService.updateById(orderRefundList);
                    //判断当前商品所有金额全部退款后，退还优惠券
                    if (StrUtil.equals(orderRefundList.getIsPlatform(), "0")) {
                        OrderStoreList orderStoreList = iOrderStoreListService.getById(orderRefundList.getOrderListId());
                        orderRefundListService.refundForSendBackOrderStoreMarketingDiscountCoupon(orderStoreList, orderRefundList);
                        orderRefundListService.updateOrderStoreForRefund(orderStoreList,orderRefundList);
                    } else if (StrUtil.equals(orderRefundList.getIsPlatform(), "1")) {
                        OrderList orderList = iOrderListService.getById(orderRefundList.getOrderListId());
                        orderRefundListService.refundForSendBackOrderMarketingDiscountCoupon(orderList, orderRefundList);
                        orderRefundListService.updateOrderForRefund(orderList,orderRefundList);
                        OrderProviderGoodRecord orderProviderGoodRecord = new OrderProviderGoodRecord().setId(orderRefundList.getOrderGoodRecordId()).setStatus("3");
                        orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
                    }
                }
            } else {
                log.info("汇付天下微信退款失败：" + data);
            }
        }
        return Result.OK();
    }

    /**
     * 订单回调（加盟商完成）
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("submitOrder")
    @ResponseBody
    public Object submitOrder(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {
            PayOrderCarLog payOrderCarLog = iPayOrderCarLogService.getById(id);
            if (payOrderCarLog == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
            if (payOrderCarLog.getBackStatus().equals("0")) {
                //回调支付冲账
                payOrderCarLog.setBackStatus("1");
                payOrderCarLog.setBackTimes(payOrderCarLog.getBackTimes().add(new BigDecimal(1)));
                iPayOrderCarLogService.saveOrUpdate(payOrderCarLog);
                if (StringUtils.isBlank(payOrderCarLog.getPayParam())) {
                    if (payOrderCarLog.getPayStatus().equals("0") && payOrderCarLog.getPayPrice().doubleValue() == 0) {
                        orderPaySuccess(payOrderCarLog);
                    }
                }
            }
            result.success("订单冲账成功！！！");
            return result;
        } else {

            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {
                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    tradeNo = params.get("out_trade_no");
                    // 更新订单信息
                    // 发送通知等
                    PayOrderCarLog payOrderCarLog = iPayOrderCarLogService.getById(tradeNo);
                    log.info("订单回调支付流水号：" + tradeNo);

                    if (payOrderCarLog.getPayStatus().equals("0")) {
                        orderPaySuccess(payOrderCarLog);
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
            //汇付天下微信支付和支付宝回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    // 更新订单信息
                    // 发送通知等
                    PayOrderCarLog payOrderCarLog = iPayOrderCarLogService.getById(tradeNo);
                    log.info("订单回调支付流水号：" + tradeNo);

                    if (payOrderCarLog.getPayStatus().equals("0")) {
                        orderPaySuccess(payOrderCarLog);
                    }
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }

        }
        return "SUCCESS";
    }

    /**
     * 支付订单回调
     *
     * @param payOrderCarLog
     */
    @Transactional
    public void orderPaySuccess(PayOrderCarLog payOrderCarLog) {
        log.info("进入订单处理流水号：" + payOrderCarLog.getId());
        if (payOrderCarLog.getPayStatus().equals("0")) {
            //扣除余额
            if (!iMemberListService.subtractBlance(payOrderCarLog.getMemberListId(), payOrderCarLog.getBalance(), payOrderCarLog.getId(), "0")) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            //扣除积分
            if (!iMemberWelfarePaymentsService.subtractWelfarePayments(payOrderCarLog.getMemberListId(), payOrderCarLog.getWelfarePayments(), "6", payOrderCarLog.getId(), "")) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

            //扣除积分
            if (!iMarketingIntegralRecordService.subtractMarketingIntegralRecord("8", payOrderCarLog.getIntegral(), payOrderCarLog.getMemberListId(), payOrderCarLog.getId())) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        //状态修改成已支付
        payOrderCarLog.setPayStatus("1");
        iPayOrderCarLogService.saveOrUpdate(payOrderCarLog);
        String payOrderLog = payOrderCarLog.getPayOrderLog();
        JSONObject payOrderLogJsonObject = JSON.parseObject(payOrderLog);

        //平台订单
        if (payOrderLogJsonObject.containsKey("goods")) {
            log.info("普通订单处理" + payOrderCarLog.getId());
            iOrderListService.paySuccessOrder(payOrderLogJsonObject.getString("goods"), 0, payOrderCarLog);
        }
        //免单订单
        if (payOrderLogJsonObject.containsKey("marketingFreeGoods")) {

            log.info("免单专区订单处理" + payOrderCarLog.getId());
            iOrderListService.paySuccessOrder(payOrderLogJsonObject.getString("marketingFreeGoods"), 1, payOrderCarLog);
        }

        //店铺订单
        if (payOrderLogJsonObject.containsKey("storeGoods")) {
            log.info("店铺订单处理" + payOrderCarLog.getId());
            JSONArray storeGoodsJsonArray = payOrderLogJsonObject.getJSONArray("storeGoods");
            storeGoodsJsonArray.forEach(storeGood -> iOrderStoreListService.paySuccessOrder(storeGood.toString(), payOrderCarLog));
        }
    }

    /**
     * 开店支付回调（加盟商）
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("openStore")
    @ResponseBody
    public Object openStore(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {
            result.success("开店冲账成功！！！");
            return result;
        } else {

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
                    iStoreManageService.paySuccess(params.get("out_trade_no"));
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
                    iStoreManageService.backSucess(tradeNo);
                    iStoreManageService.paySuccess(tradeNo);
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }
        }
        return "SUCCESS";
    }

    /**
     * 礼包支付回调
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("marketingGiftBag")
    @ResponseBody
    public Object marketingGiftBag(String id,
                                   HttpServletRequest request,
                                   @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();
        log.info("接收到的会员id+" + memberId);
        if (StringUtils.isNotBlank(memberId)) {
            log.info("进入礼包冲账!!!!!");
            PayGiftBagLog payGiftBagLog = iPayGiftBagLogService.getById(id);

            if (payGiftBagLog == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
            //手机端支付冲账
            if (!memberId.equals(payGiftBagLog.getMemberListId())) {
                result.error500("冲账token中的信息不正确，请确认冲账的用户身份！！！");
                return JSON.toJSONString(result);
            }
            //回调支付冲账
            payGiftBagLog.setBackStatus("1");
            payGiftBagLog.setBackTimes(payGiftBagLog.getBackTimes().add(new BigDecimal(1)));
            iPayGiftBagLogService.saveOrUpdate(payGiftBagLog);
            log.info("冲账参数");
            if (payGiftBagLog.getPayPrice().doubleValue() == 0 && payGiftBagLog.getPayStatus().equals("0")) {
                log.info("冲账进入礼包奖励分配!!!");
                iMarketingGiftBagService.paySuccess(payGiftBagLog.getMemberListId(), payGiftBagLog.getId());
            }
            result.success("礼包冲账成功！！！");
            return result;
        } else {
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {
                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    // 更新订单信息
                    // 发送通知等
                    PayGiftBagLog payGiftBagLog = iPayGiftBagLogService.getById(params.get("out_trade_no"));
                    log.info(params.get("out_trade_no"));
                    if (payGiftBagLog.getPayStatus().equals("0")) {
                        iMarketingGiftBagService.paySuccess(payGiftBagLog.getMemberListId(), payGiftBagLog.getId());
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
            //汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                if (StrUtil.isNotBlank(data)) {
                    JSONObject jsonObject = JSON.parseObject(data);
                    if (jsonObject.getString("status").equals("succeeded")) {
                        tradeNo = jsonObject.getString("order_no");
                        // 更新订单信息
                        // 发送通知等
                        PayGiftBagLog payGiftBagLog = iPayGiftBagLogService.getById(tradeNo);
                        log.info(tradeNo);
                        if (payGiftBagLog.getPayStatus().equals("0")) {
                            iMarketingGiftBagService.paySuccess(payGiftBagLog.getMemberListId(), payGiftBagLog.getId());
                        }
                    } else {
                        log.info("汇付天下微信支付失败：" + data);
                    }
                }
            }
        }
        return "SUCCESS";
    }

    /**
     * 余额支付回调
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "balance")
    @ResponseBody
    public String balancePay(HttpServletRequest request) {

        String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
        String tradeNo = null;

        //官方微信支付回调
        if (weixinMiniSoftPay.equals("0")) {
            String xmlMsg = HttpKit.readData(request);
            log.info("支付通知=" + xmlMsg);
            Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

            String returnCode = params.get("return_code");
            if (WxPayKit.codeIsOk(returnCode)) {
                // 更新订单信息
                // 发送通知等
                StoreRechargeRecord storeRechargeRecord = iStoreRechargeRecordService.getById(params.get("out_trade_no"));
                log.info(params.get("out_trade_no"));
                if (storeRechargeRecord.getTradeStatus().equals("0")) {
                    storeRechargeRecord.setTradeStatus("1");
                    StoreManage storeManage = iStoreManageService.getById(storeRechargeRecord.getStoreManageId());
                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    iStoreManageService.saveOrUpdate(storeManage);
                }
                //回调支付冲账
                storeRechargeRecord.setBackStatus("1");
                storeRechargeRecord.setBackTimes(storeRechargeRecord.getBackTimes().add(new BigDecimal(1)));
                iStoreRechargeRecordService.saveOrUpdate(storeRechargeRecord);
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        //汇付天下微信支付回调
        if (weixinMiniSoftPay.equals("1")) {
            String data = request.getParameter("data");
            log.info("回调的data数据：" + data);
            JSONObject jsonObject = JSON.parseObject(data);
            if (jsonObject.getString("status").equals("succeeded")) {
                tradeNo = jsonObject.getString("order_no");
                // 更新订单信息
                // 发送通知等
                StoreRechargeRecord storeRechargeRecord = iStoreRechargeRecordService.getById(tradeNo);
                log.info(tradeNo);
                if (storeRechargeRecord.getTradeStatus().equals("0")) {
                    storeRechargeRecord.setTradeStatus("1");
                    StoreManage storeManage = iStoreManageService.getById(storeRechargeRecord.getStoreManageId());
                    storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
                    iStoreManageService.saveOrUpdate(storeManage);
                }
                //回调支付冲账
                storeRechargeRecord.setBackStatus("1");
                storeRechargeRecord.setBackTimes(storeRechargeRecord.getBackTimes().add(new BigDecimal(1)));
                iStoreRechargeRecordService.saveOrUpdate(storeRechargeRecord);
            } else {
                log.info("汇付天下微信支付失败：" + data);
            }
        }
        return "SUCCESS";
    }

    /**
     * 采购礼包支付回调
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("marketingGiftBagBatch")
    @ResponseBody
    public Object marketingGiftBagBatch(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {

            MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = iMarketingGiftBagRecordBatchService.getById(id);

            if (marketingGiftBagRecordBatch == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
            //手机端支付冲账
            if (!memberId.equals(marketingGiftBagRecordBatch.getMemberListId())) {
                result.error500("冲账token中的信息不正确，请确认冲账的用户身份！！！");
                return JSON.toJSONString(result);
            }
            //回调支付冲账
            marketingGiftBagRecordBatch.setBackStatus("1");
            marketingGiftBagRecordBatch.setBackTimes(marketingGiftBagRecordBatch.getBackTimes().add(new BigDecimal(1)));
            iMarketingGiftBagRecordBatchService.saveOrUpdate(marketingGiftBagRecordBatch);
            result.success("礼包冲账成功！！！");
            return result;
        } else {
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {

                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    // 更新订单信息
                    // 发送通知等
                    MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = iMarketingGiftBagRecordBatchService.getById(params.get("out_trade_no"));
                    log.info(params.get("out_trade_no"));
                    if (marketingGiftBagRecordBatch.getPayStatus().equals("0")) {

                        iMarketingGiftBagBatchService.paySuccess(marketingGiftBagRecordBatch.getMemberListId(), marketingGiftBagRecordBatch.getId());
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
            //汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    // 更新订单信息
                    // 发送通知等
                    MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = iMarketingGiftBagRecordBatchService.getById(tradeNo);
                    log.info(tradeNo);
                    if (marketingGiftBagRecordBatch.getPayStatus().equals("0")) {

                        iMarketingGiftBagBatchService.paySuccess(marketingGiftBagRecordBatch.getMemberListId(), marketingGiftBagRecordBatch.getId());
                    }
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }

        }
        return "SUCCESS";
    }


    /**
     * 充值余额回调
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("rechargeBlance")
    @ResponseBody
    public Object rechargeBlance(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {

            PayBalanceLog payBalanceLog = iPayBalanceLogService.getById(id);

            if (payBalanceLog == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
            //回调支付冲账
            payBalanceLog.setBackStatus("1");
            payBalanceLog.setBackTimes(payBalanceLog.getBackTimes().add(new BigDecimal(1)));
            iPayBalanceLogService.saveOrUpdate(payBalanceLog);
            result.success("余额冲账成功！！！");
            return result;
        } else {
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {

                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    // 更新订单信息
                    // 发送通知等
                    PayBalanceLog payBalanceLog = iPayBalanceLogService.getById(params.get("out_trade_no"));
                    log.info(params.get("out_trade_no"));
                    if (payBalanceLog.getPayStatus().equals("0")) {

                        //成功
                        iMemberListService.rechargeBalance(payBalanceLog.getId());
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
            //汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    // 更新订单信息
                    // 发送通知等
                    PayBalanceLog payBalanceLog = iPayBalanceLogService.getById(tradeNo);
                    log.info(tradeNo);
                    if (payBalanceLog.getPayStatus().equals("0")) {

                        //成功
                        iMemberListService.rechargeBalance(payBalanceLog.getId());
                    }
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }

        }
        return "SUCCESS";
    }


    /**
     * 收银台收款回调
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("storeCheckstand")
    @ResponseBody
    public Object storeCheckstand(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {

            PayShouyinLog payShouyinLog = iPayShouyinLogService.getById(id);

            if (payShouyinLog == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
            //回调支付冲账
            payShouyinLog.setBackStatus("1");
            payShouyinLog.setBackTimes(payShouyinLog.getBackTimes().add(new BigDecimal(1)));
            iPayShouyinLogService.saveOrUpdate(payShouyinLog);
            result.success("余额冲账成功！！！");
            return result;
        } else {
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {

                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    // 更新订单信息
                    // 发送通知等
                    PayShouyinLog payShouyinLog = iPayShouyinLogService.getById(params.get("out_trade_no"));
                    log.info(params.get("out_trade_no"));
                    if (payShouyinLog.getPayStatus().equals("0")) {

                        //成功
                        iStoreShouyinRecordService.success(payShouyinLog.getId());
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
            //汇付天下支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    // 更新订单信息
                    // 发送通知等
                    PayShouyinLog payShouyinLog = iPayShouyinLogService.getById(tradeNo);
                    log.info(tradeNo);
                    if (payShouyinLog.getPayStatus().equals("0")) {

                        //成功
                        iStoreShouyinRecordService.success(payShouyinLog.getId());
                    }
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }

        }
        return "SUCCESS";
    }


    /**
     * 专区团支付回调
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("zoneGroup")
    @ResponseBody
    public Object zoneGroup(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {

            PayZoneGroupLog payZoneGroupLog = iPayZoneGroupLogService.getById(id);

            if (payZoneGroupLog == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
            //手机端支付冲账
            if (!memberId.equals(payZoneGroupLog.getMemberListId())) {
                result.error500("冲账token中的信息不正确，请确认冲账的用户身份！！！");
                return JSON.toJSONString(result);
            }
            //回调支付冲账
            payZoneGroupLog.setBackStatus("1");
            payZoneGroupLog.setBackTimes(payZoneGroupLog.getBackTimes().add(new BigDecimal(1)));
            iPayZoneGroupLogService.saveOrUpdate(payZoneGroupLog);
            if (payZoneGroupLog.getPayStatus().equals("0") && payZoneGroupLog.getPayPrice().doubleValue() == 0) {
                //成功
                iMarketingZoneGroupManageService.success(payZoneGroupLog.getId());
            }
            result.success("余额冲账成功！！！");
            return result;
        } else {
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {

                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    // 更新订单信息
                    // 发送通知等
                    PayZoneGroupLog payZoneGroupLog = iPayZoneGroupLogService.getById(params.get("out_trade_no"));
                    log.info(params.get("out_trade_no"));
                    if (payZoneGroupLog.getPayStatus().equals("0")) {

                        //成功
                        iMarketingZoneGroupManageService.success(payZoneGroupLog.getId());
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
            //汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    // 更新订单信息
                    // 发送通知等
                    PayZoneGroupLog payZoneGroupLog = iPayZoneGroupLogService.getById(tradeNo);
                    log.info(tradeNo);
                    if (payZoneGroupLog.getPayStatus().equals("0")) {

                        //成功
                        iMarketingZoneGroupManageService.success(payZoneGroupLog.getId());
                    }
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }

        }
        return "SUCCESS";
    }


    /**
     * 创业礼包支付回调
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("businessGift")
    @ResponseBody
    public Object businessGift(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {

            PayBusinessGiftLog payBusinessGiftLog = iPayBusinessGiftLogService.getById(id);

            if (payBusinessGiftLog == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
            //手机端支付冲账
            if (!memberId.equals(payBusinessGiftLog.getMemberListId())) {
                result.error500("冲账token中的信息不正确，请确认冲账的用户身份！！！");
                return JSON.toJSONString(result);
            }
            //回调支付冲账
            payBusinessGiftLog.setBackStatus("1");
            payBusinessGiftLog.setBackTimes(payBusinessGiftLog.getBackTimes().add(new BigDecimal(1)));
            iPayBusinessGiftLogService.saveOrUpdate(payBusinessGiftLog);
            if (payBusinessGiftLog.getPayStatus().equals("0") && payBusinessGiftLog.getPayPrice().doubleValue() == 0) {
                //成功
                iMarketingBusinessGiftListService.success(payBusinessGiftLog.getId());
            }
            result.success("余额冲账成功！！！");
            return result;
        } else {
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

            //官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {

                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
                    // 更新订单信息
                    // 发送通知等
                    PayBusinessGiftLog payBusinessGiftLog = iPayBusinessGiftLogService.getById(params.get("out_trade_no"));
                    log.info(params.get("out_trade_no"));
                    if (payBusinessGiftLog.getPayStatus().equals("0")) {

                        //成功
                        iMarketingBusinessGiftListService.success(payBusinessGiftLog.getId());
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
            //汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
                    // 更新订单信息
                    // 发送通知等
                    PayBusinessGiftLog payBusinessGiftLog = iPayBusinessGiftLogService.getById(tradeNo);
                    log.info(tradeNo);
                    if (payBusinessGiftLog.getPayStatus().equals("0")) {

                        //成功
                        iMarketingBusinessGiftListService.success(payBusinessGiftLog.getId());
                    }
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }

        }
        return "SUCCESS";
    }


    /**
     * 支付即积分的回调
     *
     * @param request
     * @return
     */
    @RequestMapping("paymentLevel")
    @ResponseBody
    public String paymentLevel(HttpServletRequest request) {
        log.info("支付级积分的参数：" + JSON.toJSONString(request.getParameterMap()));
        return "SUCCESS";
    }


    /**
     * 礼包团支付回调
     *
     * @param id
     * @param request
     * @param memberId
     * @return
     */
    @RequestMapping("marketingStoreGift")
    @ResponseBody
    public Object marketingStoreGift(String id, HttpServletRequest request, @RequestAttribute(value = "memberId", required = false) String memberId) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(memberId)) {

            PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog = iPayMarketingStoreGiftbagLogService.getById(id);

            if (payMarketingStoreGiftbagLog == null) {
                result.error500("id参数找不到相关的支付日志");
                return JSON.toJSONString(result);
            }
//回调支付冲账
//回调支付冲账
            if (payMarketingStoreGiftbagLog.getPayStatus().equals("0")) {
//成功
                iMarketingStoreGiftbagRecordService.success(payMarketingStoreGiftbagLog.getId());
            }
            result.success("余额冲账成功！！！");
            return result;
        } else {
            String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");
            String tradeNo = null;

//官方微信支付回调
            if (weixinMiniSoftPay.equals("0")) {

                String xmlMsg = HttpKit.readData(request);
                log.info("支付通知=" + xmlMsg);
                Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

                String returnCode = params.get("return_code");
                if (WxPayKit.codeIsOk(returnCode)) {
// 更新订单信息
// 发送通知等
                    PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog = iPayMarketingStoreGiftbagLogService.getById(params.get("out_trade_no"));
                    log.info(params.get("out_trade_no"));
                    if (payMarketingStoreGiftbagLog.getPayStatus().equals("0")) {

//成功
                        iMarketingStoreGiftbagRecordService.success(payMarketingStoreGiftbagLog.getId());
                    }
                    Map<String, String> xml = new HashMap<String, String>(2);
                    xml.put("return_code", "SUCCESS");
                    xml.put("return_msg", "OK");
                    return WxPayKit.toXml(xml);
                }
            }
//汇付天下微信支付回调
            if (weixinMiniSoftPay.equals("1")) {
                String data = request.getParameter("data");
                log.info("回调的data数据：" + data);
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getString("status").equals("succeeded")) {
                    tradeNo = jsonObject.getString("order_no");
// 更新订单信息
// 发送通知等
                    PayMarketingStoreGiftbagLog payMarketingStoreGiftbagLog = iPayMarketingStoreGiftbagLogService.getById(tradeNo);
                    log.info(tradeNo);
                    if (payMarketingStoreGiftbagLog.getPayStatus().equals("0")) {

//成功
                        iMarketingStoreGiftbagRecordService.success(payMarketingStoreGiftbagLog.getId());
                    }
                } else {
                    log.info("汇付天下微信支付失败：" + data);
                }
            }

        }
        return "SUCCESS";
    }
}
