package org.jeecg.modules.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardMemberListService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.dto.ApplyOrderRefundDto;
import org.jeecg.modules.order.dto.OrderRefundListDto;
import org.jeecg.modules.order.entity.*;
import org.jeecg.modules.order.mapper.OrderRefundListMapper;
import org.jeecg.modules.order.service.*;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.utils.logistics.LogisticsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
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
    private IOrderListService orderListService;

    @Autowired
    private IOrderStoreGoodRecordService orderStoreGoodRecordService;

    @Autowired
    @Lazy
    private IMarketingStoreGiftCardMemberListService marketingStoreGiftCardMemberListService;

    @Autowired
    private IOrderStoreSubListService orderStoreSubListService;

    @Autowired
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;

    @Autowired
    private IOrderProviderListService orderProviderListService;


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
        if (StrUtil.equals(isPlatform, "0")) {
            refundOrderStoreGood(orderRefundList, actualRefundPrice, actualRefundBalance);
        } else if (StrUtil.equals(isPlatform, "1")) {
            refundOrderGood(orderRefundList, actualRefundPrice, actualRefundBalance);
        }
    }

    /**
     * 平台订单商品退款逻辑
     *
     * @param orderRefundList
     * @param actualRefundPrice
     * @param actualRefundBalance
     */
    public void refundOrderGood(OrderRefundList orderRefundList, BigDecimal actualRefundPrice, BigDecimal actualRefundBalance) {
        // TODO: 2023/4/23 平台订单退款逻辑 @zhangshaolin
        OrderList orderList = orderListService.getById(orderRefundList.getOrderListId());
        if (orderList == null) {
            throw new JeecgBootException("订单不存在");
        }
        String orderType = orderList.getOrderType();

        List<OrderRefundList> ongoingOrderRefundList = getOrderRefundListByMemberIdAndOrderId(orderRefundList.getMemberId(), orderRefundList.getOrderListId());

        Map<String, BigDecimal> refundPriceMap = ongoingOrderRefundList.stream().collect(Collectors.groupingBy(OrderRefundList::getOrderGoodRecordId, Collectors.mapping(OrderRefundList::getRefundPrice, Collectors.reducing(BigDecimal.ZERO, NumberUtil::add))));
        // 普通订单，走余额、微信退款，优先退余额。 还要退福利金优惠
        BigDecimal totalRefundPrice = NumberUtil.add(actualRefundPrice, actualRefundBalance);
        if (totalRefundPrice.compareTo(orderRefundList.getRefundPrice()) > 0) {
            throw new JeecgBootException("退款总金额：" + totalRefundPrice + " 大于申请金额：" + orderRefundList.getRefundPrice() + ",无法操作");
        }
        //  先退余额，状态改为退款成功
        if (actualRefundBalance.compareTo(BigDecimal.ZERO) > 0) {
            boolean addBlance = memberListService.addBlance(orderRefundList.getMemberId(), actualRefundBalance, orderList.getOrderNo(), "2");
            if (addBlance) {
                orderRefundList.setActualRefundBalance(actualRefundBalance);
                orderRefundList.setStatus("4");
            }
        }
        // 微信退款，走汇付，状态改为退款中，走异步通知
        if (actualRefundPrice.compareTo(BigDecimal.ZERO) > 0) {
            if (StrUtil.isBlank(orderList.getHftxSerialNumber()) || StrUtil.isBlank(orderList.getSerialNumber()) || orderList.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
                throw new JeecgBootException("该订单未使用微信支付，无法使用微信退款");
            }
            if (actualRefundPrice.compareTo(orderList.getPayPrice()) > 0) {
                // TODO: 2023/4/23 这里分多次退款的话，可能存在判断bug，为了安全起见，当下先这么判断吧 @zhangshaolin
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
            balanceMap = this.payUtils.refund(actualRefundPrice, orderRefundList.getId(), orderList.getHftxSerialNumber(), refundCallbackUrl);
            if (balanceMap.get("status").equals("failed")) {
                log.error("现金退款失败：{}", JSON.toJSON(balanceMap));
                throw new JeecgBootException("现金退款失败");
            }
            orderRefundList.setRefundJson(JSON.toJSONString(balanceMap));
            orderRefundList.setStatus("3");
        }

        if (StrUtil.equals(orderType, "5")) {
            //福利金订单需要退福利金

        }
    }


    /**
     * 店铺订单商品退款逻辑
     *
     * @param orderRefundList
     * @param actualRefundPrice
     * @param actualRefundBalance
     */
    public void refundOrderStoreGood(OrderRefundList orderRefundList, BigDecimal actualRefundPrice, BigDecimal actualRefundBalance) {
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
//            if (totalRefundPrice.compareTo(NumberUtil.sub(orderRefundList.getGoodRecordActualPayment(), refundPriceMap.getOrDefault(orderRefundList.getOrderGoodRecordId(), BigDecimal.ZERO))) > 0) {
//                throw new JeecgBootException("退款总金额：" + totalRefundPrice + " 大于实际可退款金额：" + NumberUtil.sub(orderRefundList.getGoodRecordActualPayment(), refundPriceMap.getOrDefault(orderRefundList.getOrderGoodRecordId(), BigDecimal.ZERO)) + ",无法操作");
//            }
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
                // TODO: 2023/4/23 这里分多次退款的话，可能存在判断bug，为了安全起见，当下先这么判断吧 @zhangshaolin
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
            balanceMap = this.payUtils.refund(actualRefundPrice, orderRefundList.getId(), orderStoreList.getHftxSerialNumber(), refundCallbackUrl);
            if (balanceMap.get("status").equals("failed")) {
                log.error("现金退款失败：{}", JSON.toJSON(balanceMap));
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
    }

    /**
     * 店铺售后申请
     *
     * @param applyOrderRefundDto               售后请求参数对象
     * @param memberId                          会员id
     * @param exchangeMemberShippingAddressJson 换货地址json
     */
    @Override
    public void applyOrderStoreRefund(ApplyOrderRefundDto applyOrderRefundDto, String memberId, String exchangeMemberShippingAddressJson) {
        List<String> orderGoodRecordIds = applyOrderRefundDto.getOrderRefundListDtos().stream().map(OrderRefundListDto::getOrderGoodRecordId).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderGoodRecordIds)) {
            throw new JeecgBootException("orderGoodRecordIds 不能为空");
        }
        String refundType = applyOrderRefundDto.getRefundType();
        //查询订单信息
        OrderStoreList orderStoreList = orderStoreListService.getById(applyOrderRefundDto.getOrderId());
        if (orderStoreList == null) {
            throw new JeecgBootException("订单不存在");
        }
        String status = orderStoreList.getStatus();
        if (StrUtil.containsAny(status, "0", "4")) {
            throw new JeecgBootException("待付款/交易失败订单无法发起售后申请");
        }
        if (StrUtil.containsAny(applyOrderRefundDto.getRefundType(), "1", "2")) {
            // 退款退货、换货
            if (StrUtil.equals(status, "1")) {
                throw new JeecgBootException("待发货订单无法发起退货、换货售后申请");
            }
        }
        //批量查询订单商品列表
        List<OrderStoreGoodRecord> orderStoreGoodRecordList = orderStoreGoodRecordService.listByIds(orderGoodRecordIds);
        Map<String, OrderStoreGoodRecord> orderStoreGoodRecordMap = orderStoreGoodRecordList.stream().collect(Collectors.toMap(OrderStoreGoodRecord::getId, orderStoreGoodRecord -> orderStoreGoodRecord));

        //批量查询供应商订单列表
        List<String> orderStoreSubListIds = orderStoreGoodRecordList.stream().map(OrderStoreGoodRecord::getOrderStoreSubListId).collect(Collectors.toList());
        Map<String, OrderStoreSubList> orderStoreSubListMap = orderStoreSubListService.listByIds(orderStoreSubListIds).stream().collect(Collectors.toMap(OrderStoreSubList::getId, orderStoreSubList -> orderStoreSubList));

        // 批量查询用户订单商品进行中或者已完成的售后记录列表,用于售后金额、数量判断
        List<OrderRefundList> ongoingOrderRefundList = orderRefundListService.getOrderRefundListByMemberIdAndOrderId(memberId, orderStoreList.getId());

        //保存售后申请单
        String finalExchangeMemberShippingAddressJson = exchangeMemberShippingAddressJson;
        List<OrderRefundList> orderRefundLists = applyOrderRefundDto.getOrderRefundListDtos().stream().map(orderRefundListDto -> {
            String orderStoreGoodRecordId = orderRefundListDto.getOrderGoodRecordId();
            if (StrUtil.isBlank(orderStoreGoodRecordId)) {
                throw new JeecgBootException("orderGoodRecordId 订单商品不能为空");
            }
            OrderStoreGoodRecord orderStoreGoodRecord = orderStoreGoodRecordMap.get(orderStoreGoodRecordId);
            if (orderStoreGoodRecord == null) {
                throw new JeecgBootException("订单商品id: " + orderRefundListDto.getOrderGoodRecordId() + "不存在");
            }
            OrderStoreSubList orderStoreSubList = orderStoreSubListMap.get(orderStoreGoodRecord.getOrderStoreSubListId());
            if (orderStoreSubList == null) {
                throw new JeecgBootException("店铺供应商订单不存在");
            }
            if (StrUtil.containsAny(applyOrderRefundDto.getRefundType(), "1", "2") && StrUtil.equals(orderStoreSubList.getParentId(), "0")) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "还未发货，无法发起退货换货售后申请");
            }
            BigDecimal refundPrice = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundPrice(), orderStoreGoodRecord.getActualPayment());
            BigDecimal refundAmount = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundAmount(), orderStoreGoodRecord.getAmount());

            verifyApplyOrderStoreRefund(refundType, refundPrice, refundAmount, ongoingOrderRefundList, orderStoreGoodRecord);
            OrderRefundList orderRefundList = new OrderRefundList()
                    .setOrderNo(orderStoreList.getOrderNo())
                    .setOrderType(orderStoreList.getOrderType())
                    .setGoodMainPicture(orderStoreGoodRecord.getMainPicture())
                    .setGoodName(orderStoreGoodRecord.getGoodName())
                    .setGoodSpecification(orderStoreGoodRecord.getSpecification())
                    .setGoodListId(orderStoreGoodRecord.getGoodStoreListId())
                    .setGoodSpecificationId(orderStoreGoodRecord.getGoodStoreSpecificationId())
                    .setOrderGoodRecordId(orderStoreGoodRecord.getId())
                    .setOrderSubListId(orderStoreGoodRecord.getOrderStoreSubListId())
                    .setGoodRecordTotal(orderStoreGoodRecord.getTotal())
                    .setGoodRecordActualPayment(orderStoreGoodRecord.getActualPayment())
                    .setGoodRecordCoupon(orderStoreGoodRecord.getCoupon())
                    .setGoodRecordGiftCardCoupon(orderStoreGoodRecord.getGiftCardCoupon())
                    .setGoodRecordTotalCoupon(orderStoreGoodRecord.getTotalCoupon())
                    .setGoodRecordAmount(orderStoreGoodRecord.getAmount())
                    .setMemberId(memberId)
                    .setOrderListId(orderStoreList.getId())
                    .setSysUserId(orderStoreList.getSysUserId())
                    .setRefundType(applyOrderRefundDto.getRefundType())
                    .setRefundReason(orderRefundListDto.getRefundReason())
                    .setRemarks(StrUtil.blankToDefault(orderRefundListDto.getRemarks(), applyOrderRefundDto.getRemarks()))
                    .setStatus("0")
                    .setRefundCertificate(StrUtil.blankToDefault(orderRefundListDto.getRefundCertificate(), applyOrderRefundDto.getRefundCertificate()))
                    .setRefundPrice(orderRefundListDto.getRefundPrice())
                    .setRefundAmount(orderRefundListDto.getRefundAmount())
                    .setExchangeGoodSpecificationId(orderRefundListDto.getExchangeGoodSpecificationId())
                    .setExchangeGoodSpecification(orderRefundListDto.getExchangeGoodSpecification())
                    .setExchangeMemberShippingAddress(finalExchangeMemberShippingAddressJson)
                    .setIsPlatform(applyOrderRefundDto.getIsPlatform());
            return orderRefundList;
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(orderRefundLists)) {
            orderRefundListService.saveBatch(orderRefundLists);
        }
    }

    /**
     * 平台订单商品售后申请
     *
     * @param applyOrderRefundDto               售后请求参数对象
     * @param memberId                          会员id
     * @param exchangeMemberShippingAddressJson 换货地址json
     */
    @Override
    public void applyOrderRefund(ApplyOrderRefundDto applyOrderRefundDto, String memberId, String exchangeMemberShippingAddressJson) {
        List<String> orderGoodRecordIds = applyOrderRefundDto.getOrderRefundListDtos().stream().map(OrderRefundListDto::getOrderGoodRecordId).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderGoodRecordIds)) {
            throw new JeecgBootException("orderGoodRecordIds 不能为空");
        }
        OrderList orderList = orderListService.getById(applyOrderRefundDto.getOrderId());
        if (orderList == null) {
            throw new JeecgBootException("订单不存在");
        }
        String status = orderList.getStatus();
        String refundType = applyOrderRefundDto.getRefundType();
        if (StrUtil.containsAny(status, "0", "4")) {
            throw new JeecgBootException("待付款/交易失败订单无法发起售后申请");
        }
        if (StrUtil.containsAny(refundType, "1", "2")) {
            // 退款退货、换货
            if (StrUtil.equals(status, "1")) {
                throw new JeecgBootException("待发货订单无法发起退货、换货售后申请");
            }
        }
        //批量查询订单商品列表
        List<OrderProviderGoodRecord> orderProviderGoodRecordList = orderProviderGoodRecordService.listByIds(orderGoodRecordIds);
        Map<String, OrderProviderGoodRecord> orderProviderGoodRecordMap = orderProviderGoodRecordList.stream().collect(Collectors.toMap(OrderProviderGoodRecord::getId, orderProviderGoodRecord -> orderProviderGoodRecord));

        //批量查询供应商订单列表
        List<String> orderProviderIds = orderProviderGoodRecordList.stream().map(OrderProviderGoodRecord::getOrderProviderListId).collect(Collectors.toList());
        Map<String, OrderProviderList> orderProviderListMap = orderProviderListService.listByIds(orderProviderIds).stream().collect(Collectors.toMap(OrderProviderList::getId, orderProviderList -> orderProviderList));

        // 批量查询用户订单商品进行中或者已完成的售后记录列表,用于售后金额、数量判断
        List<OrderRefundList> ongoingOrderRefundList = orderRefundListService.getOrderRefundListByMemberIdAndOrderId(memberId, orderList.getId());

        //保存售后申请单
        List<OrderRefundList> orderRefundLists = applyOrderRefundDto.getOrderRefundListDtos().stream().map(orderRefundListDto -> {
            String orderGoodRecordId = orderRefundListDto.getOrderGoodRecordId();
            if (StrUtil.isBlank(orderGoodRecordId)) {
                throw new JeecgBootException("orderGoodRecordId 订单商品不能为空");
            }
            OrderProviderGoodRecord orderProviderGoodRecord = orderProviderGoodRecordMap.get(orderGoodRecordId);
            if (orderProviderGoodRecord == null) {
                throw new JeecgBootException("订单商品id: " + orderRefundListDto.getOrderGoodRecordId() + "不存在");
            }
            OrderProviderList orderProviderList = orderProviderListMap.get(orderProviderGoodRecord.getOrderProviderListId());
            if (orderProviderList == null) {
                throw new JeecgBootException("店铺供应商订单不存在");
            }
            if (StrUtil.containsAny(refundType, "1", "2") && StrUtil.equals(orderProviderList.getParentId(), "0")) {
                throw new JeecgBootException("订单商品" + orderGoodRecordId + "还未发货，无法发起退货换货售后申请");
            }

            BigDecimal refundPrice = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundPrice(), orderProviderGoodRecord.getActualPayment());
            BigDecimal refundAmount = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundAmount(), orderProviderGoodRecord.getAmount());

            verifyApplyOrderRefund(refundType, refundPrice, refundAmount, ongoingOrderRefundList, orderProviderGoodRecord);
            return new OrderRefundList()
                    .setOrderNo(orderList.getOrderNo())
                    .setOrderType(orderList.getOrderType())
                    .setGoodMainPicture(orderProviderGoodRecord.getMainPicture())
                    .setGoodName(orderProviderGoodRecord.getGoodName())
                    .setGoodSpecification(orderProviderGoodRecord.getSpecification())
                    .setGoodListId(orderProviderGoodRecord.getGoodListId())
                    .setGoodSpecificationId(orderProviderGoodRecord.getGoodSpecificationId())
                    .setOrderGoodRecordId(orderProviderGoodRecord.getId())
                    .setOrderSubListId(orderProviderGoodRecord.getOrderProviderListId())
                    .setGoodRecordTotal(orderProviderGoodRecord.getTotal())
                    .setGoodRecordActualPayment(orderProviderGoodRecord.getActualPayment())
                    .setGoodRecordCoupon(orderProviderGoodRecord.getDiscountCoupon())
                    .setGoodRecordTotalCoupon(NumberUtil.add(orderProviderGoodRecord.getWelfarePaymentsPrice(), orderProviderGoodRecord.getDiscountCoupon()))
                    .setWelfarePayments(orderProviderGoodRecord.getWelfarePayments())
                    .setWelfarePaymentsPrice(orderProviderGoodRecord.getWelfarePaymentsPrice())
                    .setGoodRecordAmount(orderProviderGoodRecord.getAmount())
                    .setSysUserId(orderProviderList.getSysUserId())
                    .setMemberId(memberId)
                    .setOrderListId(orderList.getId())
                    .setSysUserId("")
                    .setRefundType(refundType)
                    .setRefundReason(orderRefundListDto.getRefundReason())
                    .setRemarks(StrUtil.blankToDefault(orderRefundListDto.getRemarks(), applyOrderRefundDto.getRemarks()))
                    .setStatus("0")
                    .setRefundCertificate(StrUtil.blankToDefault(orderRefundListDto.getRefundCertificate(), applyOrderRefundDto.getRefundCertificate()))
                    .setRefundPrice(orderRefundListDto.getRefundPrice())
                    .setRefundAmount(orderRefundListDto.getRefundAmount())
                    .setIsPlatform(applyOrderRefundDto.getIsPlatform());
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(orderRefundLists)) {
            orderRefundListService.saveBatch(orderRefundLists);
        }
    }

    /**
     * 校验订单商品售后申请限制
     *
     * @param refundType
     * @param refundPrice
     * @param refundAmount
     * @param ongoingOrderRefundList
     * @param orderStoreGoodRecord
     */
    public void verifyApplyOrderStoreRefund(String refundType, BigDecimal refundPrice, BigDecimal refundAmount, List<OrderRefundList> ongoingOrderRefundList, OrderStoreGoodRecord orderStoreGoodRecord) {
        // 售后申请拦截校验
        if (StrUtil.containsAny(refundType, "0", "1")) {
            //退款
            long count = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "2") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2")).count();
            if (count > 0) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "存在换货中的售后单，无法发起退款退货申请");
            }
            //金额、个数检查
            BigDecimal price1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal price2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal ongoingPrice = NumberUtil.add(price1, price2);
            if (refundPrice.compareTo(NumberUtil.sub(orderStoreGoodRecord.getActualPayment(), ongoingPrice)) > 0) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + " 实付金额：" + orderStoreGoodRecord.getActualPayment() + "申请金额：" + refundPrice + "已售后金额：" + ongoingPrice);
            }
            long count1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).count();
            long count2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).count();
            if (Convert.toLong(refundAmount) > Convert.toLong(orderStoreGoodRecord.getAmount()) - count1 - count2) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "实际购买数量：" + orderStoreGoodRecord.getAmount() + "申请数量：" + refundAmount + "已售后数量：" + (count1 + count2));
            }
        } else if (StrUtil.equals(refundType, "2")) {
            //退款中数量
            long count1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).count();
            long count2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).count();
            if (count1 + count2 > 0) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "存在退款或退货退款的售后单，无法发起换货申请");
            }
        }
    }


    /**
     * 校验订单商品售后申请限制
     *
     * @param refundType
     * @param refundPrice
     * @param refundAmount
     * @param ongoingOrderRefundList
     * @param orderStoreGoodRecord
     */
    public void verifyApplyOrderRefund(String refundType, BigDecimal refundPrice, BigDecimal refundAmount, List<OrderRefundList> ongoingOrderRefundList, OrderProviderGoodRecord orderStoreGoodRecord) {
        // 售后申请拦截校验
        if (StrUtil.containsAny(refundType, "0", "1")) {
            //退款
            long count = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "2") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2")).count();
            if (count > 0) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "存在换货中的售后单，无法发起退款退货申请");
            }
            //金额、个数检查
            BigDecimal price1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal price2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal ongoingPrice = NumberUtil.add(price1, price2);
            if (refundPrice.compareTo(NumberUtil.sub(orderStoreGoodRecord.getActualPayment(), ongoingPrice)) > 0) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + " 实付金额：" + orderStoreGoodRecord.getActualPayment() + "申请金额：" + refundPrice + "已售后金额：" + ongoingPrice);
            }
            long count1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).count();
            long count2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).count();
            if (Convert.toLong(refundAmount) > Convert.toLong(orderStoreGoodRecord.getAmount()) - count1 - count2) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "实际购买数量：" + orderStoreGoodRecord.getAmount() + "申请数量：" + refundAmount + "已售后数量：" + (count1 + count2));
            }
        } else if (StrUtil.equals(refundType, "2")) {
            //退款中数量
            long count1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).count();
            long count2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).count();
            if (count1 + count2 > 0) {
                throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "存在退款或退货退款的售后单，无法发起换货申请");
            }
        }
    }
    /**
     * 查询用户在订单下的所有售后单(进行中或已完成)
     *
     * @param memberId 会员id
     * @param orderId  订单id
     * @return
     */
    @Override
    public List<OrderRefundList> getOrderRefundListByMemberIdAndOrderId(String memberId, String orderId) {
        LambdaQueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderRefundListLambdaQueryWrapper
                .eq(OrderRefundList::getMemberId, memberId)
                .notIn(OrderRefundList::getStatus, "5", "6", "7")
                .eq(OrderRefundList::getOrderListId, orderId)
                .eq(OrderRefundList::getDelFlag, "0");
        return orderRefundListService.list(orderRefundListLambdaQueryWrapper);
    }
}
