package org.jeecg.modules.order.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardMemberListService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.mapper.OrderRefundListMapper;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.utils.logistics.LogisticsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date: 2023-04-20
 * @Version: V1.0
 */
@Service
@Slf4j
public class OrderRefundListServiceImpl extends MPJBaseServiceImpl<OrderRefundListMapper, OrderRefundList> implements IOrderRefundListService {

    @Autowired
    private LogisticsUtil logisticsUtil;

    @Autowired
    private IOrderStoreListService orderStoreListService;

    @Autowired
    private PayUtils payUtils;

    @Autowired
    private HftxPayUtils hftxPayUtils;

    @Autowired
    @Lazy
    private IMemberListService memberListService;

    @Autowired
    private IOrderRefundListService orderRefundListService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    @Autowired
    @Lazy
    private IMarketingStoreGiftCardMemberListService marketingStoreGiftCardMemberListService;

    @Override
    public OrderRefundList getOrderRefundListById(String id) {
        OrderRefundList orderRefundList = getById(id);
        if (orderRefundList == null) {
            throw new JeecgBootException("售后单数据不存在");
        }

        //获取物流数据JSON
        if (StrUtil.isAllNotBlank(orderRefundList.getBuyerLogisticsCompany(), orderRefundList.getBuyerTrackingNumber())) {
            String buyerLogisticsTracking = orderRefundList.getBuyerLogisticsTracking();
            if (StrUtil.isNotBlank(buyerLogisticsTracking)) {
                JSONObject jsonObject = JSONObject.parseObject(buyerLogisticsTracking);
                if (jsonObject.get("status").equals("0")) {
                    //已签收
                    JSONObject jsonObjectResult = JSONObject.parseObject(jsonObject.get("result").toString());

                    if (jsonObjectResult.get("issign").equals("1")) {
                        //已签收，不做查询物流接口
                    } else {
                        //请求接口更新数据接口
                        String string = logisticsUtil.getLogisticsInfo(orderRefundList.getBuyerLogisticsCompany(), orderRefundList.getBuyerTrackingNumber());
                        orderRefundList.setBuyerLogisticsTracking(string);
                    }
                } else {
                    //请求接口更新物流数据接口
                    String string = logisticsUtil.getLogisticsInfo(orderRefundList.getBuyerLogisticsCompany(), orderRefundList.getBuyerTrackingNumber());
                    orderRefundList.setBuyerLogisticsTracking(string);
                }
            } else {
                String string = logisticsUtil.getLogisticsInfo(orderRefundList.getBuyerLogisticsCompany(), orderRefundList.getBuyerTrackingNumber());
                orderRefundList.setBuyerLogisticsTracking(string);
            }
            updateById(orderRefundList);
        }

        // TODO: 2023/4/22 返回商家物流信息 @zhangshaolin
        return orderRefundList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(OrderRefundList orderRefundList, BigDecimal actualRefundPrice, BigDecimal actualRefundBalance) {
        String isPlatform = orderRefundList.getIsPlatform();
        BigDecimal refundAmount = orderRefundList.getRefundAmount();
        if (StrUtil.equals(isPlatform, "0")) {
            //店铺订单退款处理：微信、余额、礼品卡  区分不同地点类型
            OrderStoreList orderStoreList = orderStoreListService.getById(orderRefundList.getOrderListId());
            if (orderStoreList == null) {
                throw new JeecgBootException("订单不存在");
            }
            String orderType = orderStoreList.getOrderType();
            // TODO: 2023/4/23 1、判断订单中所有参与优惠券优惠的商品全部退款后，退还优惠券 2、还库存 @zhangshaolin
            LambdaQueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderRefundListLambdaQueryWrapper
                    .eq(OrderRefundList::getMemberId, orderRefundList.getMemberId())
                    .notIn(OrderRefundList::getStatus, "5", "6", "7")
                    .eq(OrderRefundList::getOrderListId, orderRefundList.getOrderListId())
                    .eq(OrderRefundList::getDelFlag, "0");
            List<OrderRefundList> ongoingOrderRefundList = orderRefundListService.list(orderRefundListLambdaQueryWrapper);
            Map<String, BigDecimal> refundPriceMap = ongoingOrderRefundList.stream().collect(Collectors.groupingBy(OrderRefundList::getOrderGoodRecordId, Collectors.mapping(OrderRefundList::getRefundPrice, Collectors.reducing(BigDecimal.ZERO, NumberUtil::add))));
            // 普通订单，走余额、微信退款，优先退余额
            BigDecimal totalRefundPrice = NumberUtil.add(actualRefundPrice, actualRefundBalance);
            if (totalRefundPrice.compareTo(orderRefundList.getRefundPrice()) > 0) {
                throw new JeecgBootException("退款总金额：" + totalRefundPrice + " 大于申请金额：" + orderRefundList.getRefundPrice() + ",无法操作");
            }
            if (totalRefundPrice.compareTo(NumberUtil.sub(orderRefundList.getGoodRecordActualPayment(), refundPriceMap.getOrDefault(orderRefundList.getOrderGoodRecordId(), BigDecimal.ZERO))) > 0) {
                throw new JeecgBootException("退款总金额：" + totalRefundPrice + " 大于实际可退款金额：" + NumberUtil.sub(orderRefundList.getGoodRecordActualPayment(), refundPriceMap.getOrDefault(orderRefundList.getOrderGoodRecordId(), BigDecimal.ZERO)) + ",无法操作");
            }
            //  先退余额，状态改为退款成功
            if (actualRefundBalance.compareTo(BigDecimal.ZERO) > 0) {
                boolean addBlance = memberListService.addBlance(orderRefundList.getMemberId(), actualRefundBalance, orderStoreList.getOrderNo(), "2");
                if (addBlance) {
                    orderRefundList.setActualRefundBalance(actualRefundBalance);
                    orderRefundList.setStatus("4");
                }
            }
            // 微信退款，走汇付，状态改为退款中，走异步通知
            if (actualRefundPrice.compareTo(BigDecimal.ZERO) > 0) {
                if (StrUtil.isBlank(orderStoreList.getHftxSerialNumber()) || StrUtil.isBlank(orderStoreList.getSerialNumber()) || orderStoreList.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
                    throw new JeecgBootException("该订单未使用微信支付，无法使用微信退款");
                }
                if (actualRefundPrice.compareTo(orderStoreList.getPayPrice()) > 0) {
                    // TODO: 2023/4/23 这里分多次退款的话，存在判断bug，为了安全起见，当下先这么判断吧 @zhangshaolin
                    throw new JeecgBootException("微信退款金额大于该订单中微信实付款，无法发起退款");
                }
                Map<String, Object> balanceMap;
                try {
                    balanceMap = this.hftxPayUtils.getSettleAccountBalance();
                    log.info(JSON.toJSONString("账户余额信息：" + balanceMap));
                    if (!balanceMap.get("status").equals("succeeded")) {
                        throw new JeecgBootException("汇付账户的余额查询出错");
                    }

                    if (Double.parseDouble(balanceMap.get("avl_balance").toString()) < actualRefundPrice.doubleValue()) {
                        Object var10000 = balanceMap.get("avl_balance");
                        throw new JeecgBootException("汇付账户的余额：" + var10000 + "；需退金额：" + actualRefundPrice);
                    }

                } catch (BaseAdaPayException var6) {
                    var6.printStackTrace();
                }
                //设置回调地址
                String refundCallbackUrl = notifyUrlUtils.getNotify("refundCallbackUrl");
                balanceMap = this.payUtils.refund(actualRefundPrice, orderStoreList.getSerialNumber(), orderStoreList.getHftxSerialNumber(), refundCallbackUrl);
                if (balanceMap.get("status").equals("failed")) {
                    throw new JeecgBootException("现金退款失败");
                }
                orderRefundList.setRefundJson(JSON.toJSONString(balanceMap));
                orderRefundList.setStatus("3");
            }

            if (StrUtil.equals(orderType, "7") && StrUtil.isNotBlank(orderStoreList.getActiveId()) && orderRefundList.getGoodRecordGiftCardCoupon().doubleValue() > 0) {
                //实际支付礼品卡金额
                BigDecimal goodRecordGiftCardCoupon = orderRefundList.getGoodRecordGiftCardCoupon();
                // 查询已退还的礼品卡金额，已退过就不用再退了
                Map<String, BigDecimal> refundGiftCardMap = ongoingOrderRefundList.stream().collect(Collectors.groupingBy(OrderRefundList::getOrderGoodRecordId, Collectors.mapping(OrderRefundList::getActualRefundGiftCardBalance, Collectors.reducing(BigDecimal.ZERO, NumberUtil::add))));
                if (refundGiftCardMap.getOrDefault(orderRefundList.getOrderGoodRecordId(), BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0) {
                    marketingStoreGiftCardMemberListService.addBlance(orderStoreList.getActiveId(), goodRecordGiftCardCoupon, orderStoreList.getOrderNo(), "2");
                    orderRefundList.setActualRefundGiftCardBalance(goodRecordGiftCardCoupon);
                }
            }
            orderRefundListService.updateById(orderRefundList);
        } else if (StrUtil.equals(isPlatform, "1")) {
            // TODO: 2023/4/23 平台订单退款逻辑 @zhangshaolin

        }
    }
}
