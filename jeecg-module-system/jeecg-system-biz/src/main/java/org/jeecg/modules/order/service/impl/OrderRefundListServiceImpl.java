package org.jeecg.modules.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardMemberListService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.order.dto.ApplyOrderRefundDto;
import org.jeecg.modules.order.dto.OrderRefundListDto;
import org.jeecg.modules.order.entity.*;
import org.jeecg.modules.order.mapper.OrderRefundListMapper;
import org.jeecg.modules.order.service.*;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.utils.logistics.LogisticsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    private IMemberWelfarePaymentsService memberWelfarePaymentsService;

    @Autowired
    private IMarketingDiscountCouponService marketingDiscountCouponService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private IMemberShippingAddressService memberShippingAddressService;

    @Autowired
    private IMemberListService iMemberListService;


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
        if (StrUtil.isAllNotBlank(orderRefundList.getMerchantLogisticsCompany(), orderRefundList.getMerchantTrackingNumber())) {
            String merchantLogisticsTracking = orderRefundList.getMerchantLogisticsTracking();
            if (StrUtil.isNotBlank(merchantLogisticsTracking)) {
                JSONObject jsonObject = JSONObject.parseObject(merchantLogisticsTracking);
                if (jsonObject.get("status").equals("0")) {
                    //已签收
                    JSONObject jsonObjectResult = JSONObject.parseObject(jsonObject.get("result").toString());

                    if (jsonObjectResult.get("issign").equals("1")) {
                        //已签收，不做查询物流接口
                    } else {
                        //请求接口更新数据接口
                        String string = logisticsUtil.getLogisticsInfo(orderRefundList.getMerchantLogisticsCompany(), orderRefundList.getMerchantTrackingNumber());
                        orderRefundList.setMerchantLogisticsTracking(string);
                    }
                } else {
                    //请求接口更新物流数据接口
                    String string = logisticsUtil.getLogisticsInfo(orderRefundList.getMerchantLogisticsCompany(), orderRefundList.getMerchantTrackingNumber());
                    orderRefundList.setMerchantLogisticsTracking(string);
                }
            } else {
                String string = logisticsUtil.getLogisticsInfo(orderRefundList.getMerchantLogisticsCompany(), orderRefundList.getMerchantTrackingNumber());
                orderRefundList.setMerchantLogisticsTracking(string);
            }
            updateById(orderRefundList);
        }
        return orderRefundList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(OrderRefundList orderRefundList, BigDecimal actualRefundPrice, BigDecimal actualRefundBalance) {
        String isPlatform = orderRefundList.getIsPlatform();
        String status = orderRefundList.getStatus();
        // 退款、退货退款 状态拦截
        if (StrUtil.equals(orderRefundList.getRefundType(), "0")) {
            if (!StrUtil.equals(status, "0")) {
                throw new JeecgBootException("售后单不是待处理状态，无法退款！");
            }
        } else if (StrUtil.equals(orderRefundList.getRefundType(), "1")) {
            if (!StrUtil.equals(status, "2")) {
                throw new JeecgBootException("售后状态不是待商家确认收货，无法操作");
            }
        } else {
            throw new JeecgBootException("售后服务类型异常，无法退款");
        }
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
        OrderList orderList = orderListService.getById(orderRefundList.getOrderListId());
        if (orderList == null) {
            throw new JeecgBootException("订单不存在");
        }
        String orderType = orderList.getOrderType();
        // 普通订单，走余额、微信退款，优先退余额。 还要退福利金优惠
        BigDecimal totalRefundPrice = NumberUtil.add(actualRefundPrice, actualRefundBalance);
        if (totalRefundPrice.compareTo(orderRefundList.getRefundPrice()) > 0) {
            throw new JeecgBootException("退款总金额：" + totalRefundPrice + " 大于申请金额：" + orderRefundList.getRefundPrice() + ",无法操作");
        }
        // 优先按比例退还抵扣福利金
        if (StrUtil.equals(orderType, "5")) {
            //福利金订单需要退福利金
            BigDecimal welfarePayments = orderRefundList.getWelfarePayments();
            BigDecimal goodRecordAmount = orderRefundList.getGoodRecordAmount();
            List<OrderRefundList> orderRefundListList = getOrderRefundListByMemberIdAndOrderId(orderRefundList.getMemberId(), orderRefundList.getOrderListId());
            BigDecimal decimal = orderRefundListList.stream().filter(refundList -> {
                        return StrUtil.equals(refundList.getOrderGoodRecordId(), orderRefundList.getOrderGoodRecordId())
                                && refundList.getActualRefundDiscountWelfarePayments().compareTo(BigDecimal.ZERO) > 0
                                && StrUtil.equals(refundList.getStatus(),"4");
                    }
            ).map(OrderRefundList::getActualRefundDiscountWelfarePayments).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal count = orderRefundListList.stream().filter(refundList -> {
                return StrUtil.equals(refundList.getOrderGoodRecordId(), orderRefundList.getOrderGoodRecordId())
                        && refundList.getActualRefundDiscountWelfarePayments().compareTo(BigDecimal.ZERO) > 0
                        && StrUtil.equals(refundList.getStatus(),"4");
            }).map(OrderRefundList::getRefundAmount).reduce(BigDecimal.ZERO, NumberUtil::add);
            if (NumberUtil.sub(goodRecordAmount, count).compareTo(new BigDecimal("1")) == 0) {
                orderRefundList.setActualRefundDiscountWelfarePayments(NumberUtil.sub(welfarePayments, decimal));
            } else {
                //按数量比例退
                orderRefundList.setActualRefundDiscountWelfarePayments(NumberUtil.mul(NumberUtil.div(welfarePayments, goodRecordAmount, 2), orderRefundList.getRefundAmount()));
            }
            //退福利金
            if (orderRefundList.getActualRefundDiscountWelfarePayments().compareTo(BigDecimal.ZERO) > 0) {
                boolean addWelfarePayments = memberWelfarePaymentsService.addWelfarePayments(orderRefundList.getMemberId(), orderRefundList.getActualRefundDiscountWelfarePayments(), "20", orderRefundList.getId(), "平台订单售后退款");
                if (addWelfarePayments) {
                    orderRefundList.setStatus("4");
                    OrderProviderGoodRecord orderProviderGoodRecord = new OrderProviderGoodRecord().setId(orderRefundList.getOrderGoodRecordId()).setStatus("3");
                    orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
                }
            }
        }

        //  先退余额，状态改为退款成功
        if (actualRefundBalance.compareTo(BigDecimal.ZERO) > 0) {
            boolean addBlance = memberListService.addBlance(orderRefundList.getMemberId(), actualRefundBalance, orderList.getOrderNo(), "2");
            if (addBlance) {
                orderRefundList.setActualRefundBalance(actualRefundBalance);
                orderRefundList.setStatus("4");
                orderRefundList.setBalanceReceiveTime(new Date());
                OrderProviderGoodRecord orderProviderGoodRecord = new OrderProviderGoodRecord().setId(orderRefundList.getOrderGoodRecordId()).setStatus("3");
                orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
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
        orderRefundListService.updateById(orderRefundList);
        //退还优惠券
        refundForSendBackOrderMarketingDiscountCoupon(orderList, orderRefundList);

        // 退款完成后，订单状态并无变化，仍可进行发货、退款/退货退款申请 @zhangshaolin
        updateOrderForRefund(orderList, orderRefundList);
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
        // 普通订单，走余额、微信退款，优先退余额
        BigDecimal totalRefundPrice = NumberUtil.add(actualRefundPrice, actualRefundBalance);
        if (totalRefundPrice.compareTo(orderRefundList.getRefundPrice()) > 0) {
            throw new JeecgBootException("退款总金额：" + totalRefundPrice + " 大于申请金额：" + orderRefundList.getRefundPrice() + ",无法操作");
        }
//            if (totalRefundPrice.compareTo(NumberUtil.sub(orderRefundList.getGoodRecordActualPayment(), refundPriceMap.getOrDefault(orderRefundList.getOrderGoodRecordId(), BigDecimal.ZERO))) > 0) {
//                throw new JeecgBootException("退款总金额：" + totalRefundPrice + " 大于实际可退款金额：" + NumberUtil.sub(orderRefundList.getGoodRecordActualPayment(), refundPriceMap.getOrDefault(orderRefundList.getOrderGoodRecordId(), BigDecimal.ZERO)) + ",无法操作");
//            }
        // todo 设置了下单送福利金的店铺，店铺商品发生退款后，福利金没有回收 @张少林
        if (orderRefundList.getGoodRecordGiveWelfarePayments().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal goodRecordGiveWelfarePayments = orderRefundList.getGoodRecordGiveWelfarePayments();
            BigDecimal goodRecordAmount = orderRefundList.getGoodRecordAmount();
            List<OrderRefundList> orderRefundListList = getOrderRefundListByMemberIdAndOrderId(orderRefundList.getMemberId(), orderRefundList.getOrderListId());
            BigDecimal decimal = orderRefundListList.stream().filter(refundList -> {
                return StrUtil.equals(refundList.getOrderGoodRecordId(), orderRefundList.getOrderGoodRecordId())
                        && refundList.getActualReturnWelfarePayments().compareTo(BigDecimal.ZERO) > 0 && StrUtil.equals(refundList.getStatus(), "4");
            }).map(OrderRefundList::getActualReturnWelfarePayments).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal count = orderRefundListList.stream().filter(refundList -> {
                return StrUtil.equals(refundList.getOrderGoodRecordId(), orderRefundList.getOrderGoodRecordId())
                        && refundList.getActualReturnWelfarePayments().compareTo(BigDecimal.ZERO) > 0 && StrUtil.equals(refundList.getStatus(), "4");
            }).map(OrderRefundList::getRefundAmount).reduce(BigDecimal.ZERO, NumberUtil::add);
            if (NumberUtil.sub(goodRecordAmount, count).compareTo(new BigDecimal("1")) == 0) {
                orderRefundList.setActualReturnWelfarePayments(NumberUtil.sub(goodRecordGiveWelfarePayments, decimal));
            } else {
                //按数量比例退
                orderRefundList.setActualReturnWelfarePayments(NumberUtil.mul(NumberUtil.div(goodRecordGiveWelfarePayments, goodRecordAmount, 2), orderRefundList.getRefundAmount()));
            }
            if (orderRefundList.getActualReturnWelfarePayments().compareTo(BigDecimal.ZERO) > 0) {
                MemberList memberList = iMemberListService.getById(orderRefundList.getMemberId());
                if (memberList == null) {
                    throw new JeecgBootException("会员信息异常");
                }
                if (memberList.getWelfarePayments().compareTo(orderRefundList.getActualReturnWelfarePayments()) < 0) {
                    throw new JeecgBootException("会员积分不足，无法退款");
                }
                memberWelfarePaymentsService.subtractWelfarePayments(orderRefundList.getMemberId(), orderRefundList.getActualReturnWelfarePayments(), "20", orderRefundList.getId(), "售后退款归还积分");
            }
        }


        // 优先退礼品卡
        if (StrUtil.equals(orderType, "7") && StrUtil.isNotBlank(orderStoreList.getActiveId()) && orderRefundList.getGoodRecordGiftCardCoupon().doubleValue() > 0) {
            //实际支付礼品卡金额
            BigDecimal goodRecordGiftCardCoupon = orderRefundList.getGoodRecordGiftCardCoupon();
            BigDecimal goodRecordAmount = orderRefundList.getGoodRecordAmount();
            List<OrderRefundList> orderRefundListList = getOrderRefundListByMemberIdAndOrderId(orderRefundList.getMemberId(), orderRefundList.getOrderListId());
            BigDecimal decimal = orderRefundListList.stream().filter(refundList -> {
                return StrUtil.equals(refundList.getOrderGoodRecordId(), orderRefundList.getOrderGoodRecordId())
                        && refundList.getActualRefundGiftCardBalance().compareTo(BigDecimal.ZERO) > 0 && StrUtil.equals(refundList.getStatus(), "4");
            }).map(OrderRefundList::getActualRefundGiftCardBalance).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal count = orderRefundListList.stream().filter(refundList -> {
                return StrUtil.equals(refundList.getOrderGoodRecordId(), orderRefundList.getOrderGoodRecordId())
                        && refundList.getActualRefundGiftCardBalance().compareTo(BigDecimal.ZERO) > 0 && StrUtil.equals(refundList.getStatus(), "4");
            }).map(OrderRefundList::getRefundAmount).reduce(BigDecimal.ZERO, NumberUtil::add);
            if (NumberUtil.sub(goodRecordAmount, count).compareTo(new BigDecimal("1")) == 0) {
                orderRefundList.setActualRefundGiftCardBalance(NumberUtil.sub(goodRecordGiftCardCoupon, decimal));
            } else {
                //按数量比例退
                orderRefundList.setActualRefundGiftCardBalance(NumberUtil.mul(NumberUtil.div(goodRecordGiftCardCoupon, goodRecordAmount, 2), orderRefundList.getRefundAmount()));
            }
            marketingStoreGiftCardMemberListService.addBlance(orderStoreList.getActiveId(), orderRefundList.getActualRefundGiftCardBalance(), orderStoreList.getOrderNo(), "2");
            orderRefundList.setStatus("4");
            OrderStoreGoodRecord orderStoreGoodRecord = new OrderStoreGoodRecord().setId(orderRefundList.getOrderGoodRecordId()).setStatus("3");
            orderStoreGoodRecordService.updateById(orderStoreGoodRecord);
        }
        //  先退余额，状态改为退款成功
        if (actualRefundBalance.compareTo(BigDecimal.ZERO) > 0) {
            boolean addBlance = memberListService.addBlance(orderRefundList.getMemberId(), actualRefundBalance, orderStoreList.getOrderNo(), "2");
            if (addBlance) {
                orderRefundList.setActualRefundBalance(actualRefundBalance);
                orderRefundList.setStatus("4");
                orderRefundList.setBalanceReceiveTime(new Date());
                OrderStoreGoodRecord orderStoreGoodRecord = new OrderStoreGoodRecord().setId(orderRefundList.getOrderGoodRecordId()).setStatus("3");
                orderStoreGoodRecordService.updateById(orderStoreGoodRecord);
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
        orderRefundListService.updateById(orderRefundList);

        //退还优惠券
        refundForSendBackOrderStoreMarketingDiscountCoupon(orderStoreList, orderRefundList);

        // 退款完成后，订单状态并无变化，仍可进行发货、退款/退货退款申请 @zhangshaolin
        updateOrderStoreForRefund(orderStoreList, orderRefundList);
    }

    /**
     * 店铺售后申请
     *
     * @param applyOrderRefundDto               售后请求参数对象
     * @param memberId                          会员id
     * @param exchangeMemberShippingAddressJson 换货地址json
     */
    @Override
    public List<OrderRefundList> applyOrderStoreRefund(ApplyOrderRefundDto applyOrderRefundDto, String memberId, String exchangeMemberShippingAddressJson) {
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
        if (CollUtil.isEmpty(orderStoreGoodRecordList)) {
            throw new JeecgBootException("订单商品数据为空，请检查参数");
        }

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
                throw new JeecgBootException("订单商品不存在");
            }
            OrderStoreSubList orderStoreSubList = orderStoreSubListMap.get(orderStoreGoodRecord.getOrderStoreSubListId());
            if (orderStoreSubList == null) {
                throw new JeecgBootException("店铺供应商订单不存在");
            }
            if (StrUtil.containsAny(applyOrderRefundDto.getRefundType(), "1", "2") && StrUtil.equals(orderStoreSubList.getParentId(), "0")) {
                throw new JeecgBootException("订单商品还未发货，无法发起退货换货售后申请");
            }
//            if (StrUtil.equals(applyOrderRefundDto.getRefundType(), "0") && !StrUtil.equals(orderStoreSubList.getParentId(), "0")) {
//                throw new JeecgBootException("订单商品已发货，无法发起退款售后申请");
//            }
            BigDecimal refundPrice = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundPrice(), orderStoreGoodRecord.getActualPayment());
            BigDecimal refundAmount = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundAmount(), orderStoreGoodRecord.getAmount());

            verifyApplyOrderStoreRefund(refundType, refundPrice, refundAmount, ongoingOrderRefundList, orderStoreGoodRecord);

            if (StrUtil.containsAny(refundType, "0", "1")) {
                orderStoreGoodRecord.setStatus("2");
                orderStoreGoodRecordService.updateById(orderStoreGoodRecord);
            } else if (StrUtil.equals(refundType, "2")) {
                orderStoreGoodRecord.setStatus("4");
                orderStoreGoodRecordService.updateById(orderStoreGoodRecord);
            }

            OrderRefundList orderRefundList = new OrderRefundList()
                    .setApplyTime(new Date())
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
                    .setGoodUnitPrice(orderStoreGoodRecord.getUnitPrice())
                    .setGoodRecordAmount(orderStoreGoodRecord.getAmount())
                    .setMemberId(memberId)
                    .setOrderListId(orderStoreList.getId())
                    .setSysUserId(orderStoreList.getSysUserId())
                    .setRefundType(applyOrderRefundDto.getRefundType())
                    .setRefundReason(orderRefundListDto.getRefundReason())
                    .setRemarks(StrUtil.blankToDefault(orderRefundListDto.getRemarks(), applyOrderRefundDto.getRemarks()))
                    .setStatus("0")
                    .setRefundCertificate(StrUtil.blankToDefault(orderRefundListDto.getRefundCertificate(), applyOrderRefundDto.getRefundCertificate()))
                    .setRefundPrice(refundPrice)
                    .setRefundAmount(refundAmount)
                    .setExchangeGoodSpecificationId(orderRefundListDto.getExchangeGoodSpecificationId())
                    .setExchangeGoodSpecification(orderRefundListDto.getExchangeGoodSpecification())
                    .setExchangeMemberShippingAddress(finalExchangeMemberShippingAddressJson)
                    .setIsPlatform(applyOrderRefundDto.getIsPlatform())
                    .setGoodRecordGiveWelfarePayments(orderStoreGoodRecord.getGiveWelfarePayments())
                    .setGoodRecordMarketingDiscountCouponId(orderStoreGoodRecord.getMarketingDiscountCouponId());
            return orderRefundList;
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(orderRefundLists)) {
            orderRefundListService.saveBatch(orderRefundLists);
        }
        return orderRefundLists;

    }

    /**
     * 平台订单商品售后申请
     *
     * @param applyOrderRefundDto               售后请求参数对象
     * @param memberId                          会员id
     * @param exchangeMemberShippingAddressJson 换货地址json
     */
    @Override
    public List<OrderRefundList> applyOrderRefund(ApplyOrderRefundDto applyOrderRefundDto, String memberId, String exchangeMemberShippingAddressJson) {
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
                throw new JeecgBootException("订单商品还未发货，无法发起退货换货售后申请");
            }
//            if (StrUtil.equals(refundType, "0") && !StrUtil.equals(orderProviderList.getParentId(), "0")) {
//                throw new JeecgBootException("订单商品已发货，无法发起退款售后申请");
//            }

            BigDecimal refundPrice = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundPrice(), orderProviderGoodRecord.getActualPayment());
            BigDecimal refundAmount = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundAmount(), orderProviderGoodRecord.getAmount());

            verifyApplyOrderRefund(refundType, refundPrice, refundAmount, ongoingOrderRefundList, orderProviderGoodRecord);

            if (StrUtil.containsAny(refundType, "0", "1")) {
                orderProviderGoodRecord.setStatus("2");
                orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
            } else if (StrUtil.equals(refundType, "2")) {
                orderProviderGoodRecord.setStatus("4");
                orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
            }

            return new OrderRefundList()
                    .setApplyTime(new Date())
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
                    .setGoodUnitPrice(orderProviderGoodRecord.getUnitPrice())
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
                    .setRefundPrice(refundPrice)
                    .setRefundAmount(refundAmount)
                    .setIsPlatform(applyOrderRefundDto.getIsPlatform())
                    .setGoodRecordMarketingDiscountCouponId(orderProviderGoodRecord.getMarketingDiscountCouponId());
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(orderRefundLists)) {
            orderRefundListService.saveBatch(orderRefundLists);
        }
        return orderRefundLists;
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
                throw new JeecgBootException("订单商品存在换货中的售后单，无法发起退款退货申请");
            }
            //金额、个数检查
            BigDecimal price1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3", "4")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal price2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3", "4")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal ongoingPrice = NumberUtil.add(price1, price2);
            if (refundPrice.compareTo(NumberUtil.sub(orderStoreGoodRecord.getActualPayment(), ongoingPrice)) > 0) {
                throw new JeecgBootException("该订单已申请售后，请勿重复提交");
            }
            QueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new QueryWrapper<>();
            orderRefundListLambdaQueryWrapper.select("IFNULL(sum(refund_amount),0) as ongoingRefundCount");
            orderRefundListLambdaQueryWrapper.eq("order_good_record_id", orderStoreGoodRecord.getId());
            orderRefundListLambdaQueryWrapper.in("status", "0", "1", "2", "3", "4", "5");
            Map<String, Object> map = orderRefundListService.getMap(orderRefundListLambdaQueryWrapper);
            if (Convert.toLong(refundAmount) > Convert.toLong(orderStoreGoodRecord.getAmount()) - Convert.toLong(map.get("ongoingRefundCount"))) {
                throw new JeecgBootException("该订单已申请售后，请勿重复提交");
            }
        } else if (StrUtil.equals(refundType, "2")) {
            //退款中数量
            long count1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).count();
            long count2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).count();
            if (count1 + count2 > 0) {
                throw new JeecgBootException("订单商品存在退款或退货退款的售后单，无法发起换货申请");
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
                throw new JeecgBootException("订单商品存在换货中的售后单，无法发起退款退货申请");
            }
            //金额、个数检查
            BigDecimal price1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3", "4")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal price2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3", "4")).map(OrderRefundList::getRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal ongoingPrice = NumberUtil.add(price1, price2);
            if (refundPrice.compareTo(NumberUtil.sub(orderStoreGoodRecord.getActualPayment(), ongoingPrice)) > 0) {
                throw new JeecgBootException("该订单已申请售后，请勿重复提交");
            }
            QueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new QueryWrapper<>();
            orderRefundListLambdaQueryWrapper.select("IFNULL(sum(refund_amount),0) as ongoingRefundCount");
            orderRefundListLambdaQueryWrapper.eq("order_good_record_id", orderStoreGoodRecord.getId());
            orderRefundListLambdaQueryWrapper.in("status", "0", "1", "2", "3", "4", "5");
            Map<String, Object> map = orderRefundListService.getMap(orderRefundListLambdaQueryWrapper);
            if (Convert.toLong(refundAmount) > Convert.toLong(orderStoreGoodRecord.getAmount()) - Convert.toLong(map.get("ongoingRefundCount"))) {
                throw new JeecgBootException("该订单已申请售后，请勿重复提交");
            }
        } else if (StrUtil.equals(refundType, "2")) {
            //退款中数量
            long count1 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "0") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "3")).count();
            long count2 = ongoingOrderRefundList.stream().filter(orderRefundList -> StrUtil.equals(orderRefundList.getOrderGoodRecordId(), orderStoreGoodRecord.getId())
                    && StrUtil.equals(orderRefundList.getRefundType(), "1") && StrUtil.containsAny(orderRefundList.getStatus(), "0", "1", "2", "3")).count();
            if (count1 + count2 > 0) {
                throw new JeecgBootException("订单商品存在退款或退货退款的售后单，无法发起换货申请");
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

    @Override
    public Result<String> editApplyRefund(OrderRefundList orderRefundList) {
        if (StrUtil.isBlank(orderRefundList.getId())) {
            throw new JeecgBootException("id 不能为空");
        }
        OrderRefundList orderRefundListServiceById = orderRefundListService.getById(orderRefundList.getId());
        if (orderRefundListServiceById == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        String status = orderRefundListServiceById.getStatus();
        if (!StrUtil.containsAny(status, "0", "5")) {
            throw new JeecgBootException("非待处理/已拒绝售后单无法修改申请");
        }
        String refundType = orderRefundListServiceById.getRefundType();
        if (StrUtil.containsAny(refundType, "0", "1")) {
            BigDecimal refundPrice = ObjectUtil.defaultIfNull(orderRefundList.getRefundPrice(), orderRefundListServiceById.getGoodRecordActualPayment());
            BigDecimal refundAmount = ObjectUtil.defaultIfNull(orderRefundList.getRefundAmount(), orderRefundListServiceById.getGoodRecordAmount());
            // 退款金额、退款件数、申请类型业务字段校验
            List<OrderRefundList> ongoingOrderRefundList = getOrderRefundListByMemberIdAndOrderId(orderRefundListServiceById.getMemberId(), orderRefundListServiceById.getOrderListId());
            if (StrUtil.equals(orderRefundListServiceById.getIsPlatform(), "0")) {
                OrderStoreGoodRecord goodRecord = orderStoreGoodRecordService.getById(orderRefundListServiceById.getOrderGoodRecordId());
                verifyApplyOrderStoreRefund(orderRefundListServiceById.getRefundType(), refundPrice, refundAmount, ongoingOrderRefundList, goodRecord);
            } else if (StrUtil.equals(orderRefundListServiceById.getIsPlatform(), "1")) {
                OrderProviderGoodRecord orderProviderGoodRecord = orderProviderGoodRecordService.getById(orderRefundListServiceById.getOrderGoodRecordId());
                verifyApplyOrderRefund(orderRefundListServiceById.getRefundType(), refundPrice, refundAmount, ongoingOrderRefundList, orderProviderGoodRecord);
            }
            orderRefundListServiceById.setRefundPrice(refundPrice);
            orderRefundListServiceById.setRefundAmount(refundAmount);
        } else if (StrUtil.equals(refundType, "2")) {
            if (StringUtils.isBlank(orderRefundList.getMemberShippingAddressId())) {
                throw new JeecgBootException("收货地址id不能为空！！！");
            }
            //查询收货地址
            MemberShippingAddress memberShippingAddress = memberShippingAddressService.getById(orderRefundList.getMemberShippingAddressId());
            if (memberShippingAddress == null) {
                throw new JeecgBootException("收货地址不存在！！！");
            }
            if (StrUtil.hasBlank(orderRefundList.getExchangeGoodSpecification(), orderRefundList.getExchangeGoodSpecificationId())) {
                throw new JeecgBootException("换货商品规格数据不能为空");
            }
            // 设置收货地址
            JSONObject exchangeMemberShippingAddress = new JSONObject();
            exchangeMemberShippingAddress.put("consignee", memberShippingAddress.getLinkman());
            exchangeMemberShippingAddress.put("contactNumber", memberShippingAddress.getPhone());
            exchangeMemberShippingAddress.put("shippingAddress", memberShippingAddress.getAreaExplan() + memberShippingAddress.getAreaAddress());
            exchangeMemberShippingAddress.put("houseNumber", memberShippingAddress.getHouseNumber());
            String exchangeMemberShippingAddressJson = exchangeMemberShippingAddress.toJSONString();
            orderRefundList.setExchangeMemberShippingAddress(exchangeMemberShippingAddressJson);
        }


        orderRefundListServiceById.setStatus("0");
        orderRefundListServiceById.setRefundReason(orderRefundList.getRefundReason());
        orderRefundListServiceById.setRemarks(orderRefundList.getRemarks());
        orderRefundListServiceById.setRefundCertificate(orderRefundList.getRefundCertificate());
        orderRefundListServiceById.setApplyTime(new Date());
        orderRefundListService.updateById(orderRefundListServiceById);
        return Result.OK("修改申请成功!");
    }

    @Override
    public void refundForSendBackOrderStoreMarketingDiscountCoupon(OrderStoreList orderStoreList, OrderRefundList orderRefundList) {
        //判断当前商品所有金额全部退款后，退还优惠券
        if (StrUtil.isNotBlank(orderRefundList.getGoodRecordMarketingDiscountCouponId())) {
            List<OrderRefundList> orderRefundListList = getOrderRefundListByMemberIdAndOrderId(orderRefundList.getMemberId(), orderRefundList.getOrderListId());
            BigDecimal price = orderRefundListList.stream().filter(o -> StrUtil.equals(o.getStatus(), "4") && StrUtil.equals(o.getGoodRecordMarketingDiscountCouponId(), orderRefundList.getGoodRecordMarketingDiscountCouponId())).map(OrderRefundList::getActualRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal balance = orderRefundListList.stream().filter(o -> StrUtil.equals(o.getStatus(), "4") && StrUtil.equals(o.getGoodRecordMarketingDiscountCouponId(), orderRefundList.getGoodRecordMarketingDiscountCouponId())).map(OrderRefundList::getActualRefundBalance).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal count = orderRefundListList.stream().filter(o -> StrUtil.equals(o.getStatus(), "4") && StrUtil.equals(o.getGoodRecordMarketingDiscountCouponId(), orderRefundList.getGoodRecordMarketingDiscountCouponId())).map(OrderRefundList::getRefundAmount).reduce(BigDecimal.ZERO, NumberUtil::add);

            List<Map<String, Object>> list = orderStoreGoodRecordService.getOrderStoreGoodRecordByOrderIdAndMarketingDiscountCouponId(orderStoreList.getId(), orderRefundList.getGoodRecordMarketingDiscountCouponId());
            BigDecimal actualPayment = list.stream().map(m -> Convert.toBigDecimal(m.get("actualPayment"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal amount = list.stream().map(m -> Convert.toBigDecimal(m.get("amount"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);

            if (NumberUtil.add(price, balance).compareTo(actualPayment) == 0 || count.compareTo(amount) == 0) {
                //退款金额等于商品实际付款或者退款数量等于实际购买数量，退还优惠券
                marketingDiscountCouponService.sendBackOrderStoreMarketingDiscountCoupon(orderStoreList);
                orderRefundList.setActualRefundMarketingDiscountCouponId(orderRefundList.getGoodRecordMarketingDiscountCouponId());
                orderRefundListService.updateById(orderRefundList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStoreForRefund(OrderStoreList orderStoreList, OrderRefundList orderRefundList) {
        //判断所有商品退款成功，更新订单状态
        LambdaQueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderRefundListLambdaQueryWrapper
                .eq(OrderRefundList::getMemberId, orderRefundList.getMemberId())
                .eq(OrderRefundList::getStatus, "4")
                .eq(OrderRefundList::getOrderListId, orderStoreList.getId())
                .eq(OrderRefundList::getDelFlag, "0");
        List<OrderRefundList> orderRefundListList = orderRefundListService.list(orderRefundListLambdaQueryWrapper);
        BigDecimal price = orderRefundListList.stream().map(OrderRefundList::getActualRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
        BigDecimal balance = orderRefundListList.stream().map(OrderRefundList::getActualRefundBalance).reduce(BigDecimal.ZERO, NumberUtil::add);
        BigDecimal count = orderRefundListList.stream().map(OrderRefundList::getRefundAmount).reduce(BigDecimal.ZERO, NumberUtil::add);

        List<Map<String, Object>> list = orderStoreGoodRecordService.getOrderStoreGoodRecordByOrderId(orderStoreList.getId());
        BigDecimal actualPayment = list.stream().map(m -> Convert.toBigDecimal(m.get("actualPayment"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);
        BigDecimal amount = list.stream().map(m -> Convert.toBigDecimal(m.get("amount"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);

        if (NumberUtil.add(price, balance).compareTo(actualPayment) == 0 || count.compareTo(amount) == 0) {
            //更新订单状态
            orderStoreList.setStatus("4");
            orderStoreList.setIsSender("0");
            orderStoreList.setCloseTime(new Date());
            orderStoreList.setCloseType("15");
            orderStoreList.setCloseExplain("7");
            orderStoreListService.updateById(orderStoreList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderForRefund(OrderList orderList, OrderRefundList orderRefundList) {
        //判断所有商品退款成功，更新订单状态
        LambdaQueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderRefundListLambdaQueryWrapper
                .eq(OrderRefundList::getMemberId, orderRefundList.getMemberId())
                .eq(OrderRefundList::getStatus, "4")
                .eq(OrderRefundList::getOrderListId, orderList.getId())
                .eq(OrderRefundList::getDelFlag, "0");
        List<OrderRefundList> orderRefundListList = orderRefundListService.list(orderRefundListLambdaQueryWrapper);
        BigDecimal price = orderRefundListList.stream().map(OrderRefundList::getActualRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
        BigDecimal balance = orderRefundListList.stream().map(OrderRefundList::getActualRefundBalance).reduce(BigDecimal.ZERO, NumberUtil::add);
        BigDecimal count = orderRefundListList.stream().map(OrderRefundList::getRefundAmount).reduce(BigDecimal.ZERO, NumberUtil::add);

        List<Map<String, Object>> list = orderProviderGoodRecordService.getOrderProviderGoodRecordByOrderId(orderList.getId());
        BigDecimal actualPayment = list.stream().map(m -> Convert.toBigDecimal(m.get("actualPayment"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);
        BigDecimal amount = list.stream().map(m -> Convert.toBigDecimal(m.get("amount"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);
        if (NumberUtil.add(price, balance).compareTo(actualPayment) == 0 || count.compareTo(amount) == 0) {
            //更新订单状态
            orderList.setStatus("4");
            orderList.setIsSender("0");
            orderList.setCloseTime(new Date());
            orderList.setCloseType("15");
            orderList.setCloseExplain("7");
            orderListService.updateById(orderList);
        }
    }

    @Override
    public void refundForSendBackOrderMarketingDiscountCoupon(OrderList orderList, OrderRefundList orderRefundList) {
        //判断当前商品所有金额全部退款后，退还优惠券
        if (StrUtil.isNotBlank(orderRefundList.getGoodRecordMarketingDiscountCouponId())) {
            List<OrderRefundList> orderRefundListList = getOrderRefundListByMemberIdAndOrderId(orderRefundList.getMemberId(), orderRefundList.getOrderListId());
            BigDecimal price = orderRefundListList.stream().filter(o -> StrUtil.equals(o.getStatus(), "4") && StrUtil.equals(o.getGoodRecordMarketingDiscountCouponId(), orderRefundList.getGoodRecordMarketingDiscountCouponId())).map(OrderRefundList::getActualRefundPrice).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal balance = orderRefundListList.stream().filter(o -> StrUtil.equals(o.getStatus(), "4") && StrUtil.equals(o.getGoodRecordMarketingDiscountCouponId(), orderRefundList.getGoodRecordMarketingDiscountCouponId())).map(OrderRefundList::getActualRefundBalance).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal count = orderRefundListList.stream().filter(o -> StrUtil.equals(o.getStatus(), "4") && StrUtil.equals(o.getGoodRecordMarketingDiscountCouponId(), orderRefundList.getGoodRecordMarketingDiscountCouponId())).map(OrderRefundList::getRefundAmount).reduce(BigDecimal.ZERO, NumberUtil::add);

            List<Map<String, Object>> list = orderProviderGoodRecordService.getOrderProviderGoodRecordByOrderIdAndMarketingDiscountCouponId(orderList.getId(), orderRefundList.getGoodRecordMarketingDiscountCouponId());
            BigDecimal actualPayment = list.stream().map(m -> Convert.toBigDecimal(m.get("actualPayment"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);
            BigDecimal amount = list.stream().map(m -> Convert.toBigDecimal(m.get("amount"), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, NumberUtil::add);

            if (NumberUtil.add(price, balance).compareTo(actualPayment) == 0 || count.compareTo(amount) == 0) {
                //退款金额等于商品实际付款或者退款数量等于实际购买数量，退还优惠券
                marketingDiscountCouponService.sendBackOrderMarketingDiscountCoupon(orderList);
                orderRefundList.setActualRefundMarketingDiscountCouponId(orderRefundList.getGoodRecordMarketingDiscountCouponId());
                orderRefundListService.updateById(orderRefundList);
            }
        }
    }

    @Override
    public String refundOrderTimer(String id) {
        //过期时间(小时)
        String hour = sysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "common_refund_return_timeout");
        return baseMapper.getRefundOrderTimer(id, hour);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReturnsTimeoutRefundOrderJob() {
        //过期时间(小时)
        String hour = sysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "common_refund_return_timeout");
        if (StringUtils.isNotBlank(hour)) {
            List<OrderRefundList> orderRefundLists = baseMapper.getCancelReturnsTimeoutRefundOrderList(hour);
            orderRefundLists.forEach(orderRefundList -> {
                if (StrUtil.equals(orderRefundList.getRefundType(), "1")) {
                    orderRefundList.setStatus("6");
                    orderRefundList.setCloseExplain("1");
                } else if (StrUtil.equals(orderRefundList.getRefundType(), "2")) {
                    orderRefundList.setStatus("7");
                    orderRefundList.setCloseExplain("1");
                }
            });
            orderRefundListService.updateBatchById(orderRefundLists);
        }
    }
}
