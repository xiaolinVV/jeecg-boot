package org.jeecg.modules.order.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.order.dto.ApplyOrderRefundDto;
import org.jeecg.modules.order.dto.OrderRefundListDto;
import org.jeecg.modules.order.entity.*;
import org.jeecg.modules.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 张少林
 * @date 2023年04月10日 10:36 下午
 */
@RestController
@RequestMapping(value = "/after/order/refund")
public class AfterOrderRefundController {

    @Autowired
    private IOrderStoreGoodRecordService orderStoreGoodRecordService;

    @Autowired
    private IOrderRefundListService orderRefundListService;

    @Autowired
    private IOrderStoreSubListService orderStoreSubListService;

    @Autowired
    private IOrderStoreListService orderStoreListService;

    @Autowired
    private IOrderListService orderListService;

    @Autowired
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;

    @Autowired
    private IOrderProviderListService orderProviderListService;

    /**
     * 申请订单售后
     *
     * @param applyOrderRefundDto
     * @param request
     * @return
     */
    @PostMapping(value = "/apply")
    public Result<String> applyOrderRefund(@RequestBody ApplyOrderRefundDto applyOrderRefundDto, HttpServletRequest request) {
        // TODO: 2023/4/22 换货类型，一开始就要填写换货信息、邮寄信息、物流公司、快递单号。提交后状态默认 2
        String isPlatform = applyOrderRefundDto.getIsPlatform();
        String refundType = applyOrderRefundDto.getRefundType();
        if (StrUtil.isBlank(isPlatform)) {
            throw new JeecgBootException("isPlatform 不能为空");
        }
        if (StrUtil.isBlank(refundType)) {
            throw new JeecgBootException("refundType 不能为空");
        }
        if (StrUtil.isBlank(applyOrderRefundDto.getOrderId())) {
            throw new JeecgBootException("orderId 不能为空");
        }
        if (CollUtil.isEmpty(applyOrderRefundDto.getOrderRefundListDtos())) {
            throw new JeecgBootException("orderStoreRefundLists 不能为空");
        }
        List<String> orderGoodRecordIds = applyOrderRefundDto.getOrderRefundListDtos().stream().map(OrderRefundListDto::getOrderGoodRecordId).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderGoodRecordIds)) {
            throw new JeecgBootException("orderGoodRecordIds 不能为空");
        }
        String memberId = Convert.toStr(request.getAttribute("memberId"));
        if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "0")) {
            //查询订单信息
            OrderStoreList orderStoreList = orderStoreListService.getById(applyOrderRefundDto.getOrderId());
            if (orderStoreList == null) {
                throw new JeecgBootException("订单不存在");
            }
            String status = orderStoreList.getStatus();
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
            LambdaQueryWrapper<OrderStoreGoodRecord> orderStoreGoodRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderStoreGoodRecordLambdaQueryWrapper.in(OrderStoreGoodRecord::getId, orderGoodRecordIds);
            List<OrderStoreGoodRecord> orderStoreGoodRecordList = orderStoreGoodRecordService.list(orderStoreGoodRecordLambdaQueryWrapper);
            Map<String, OrderStoreGoodRecord> orderStoreGoodRecordMap = orderStoreGoodRecordList.stream().collect(Collectors.toMap(OrderStoreGoodRecord::getId, orderStoreGoodRecord -> orderStoreGoodRecord));

            //批量查询供应商订单列表
            List<String> orderStoreSubListIds = orderStoreGoodRecordList.stream().map(OrderStoreGoodRecord::getOrderStoreSubListId).collect(Collectors.toList());
            LambdaQueryWrapper<OrderStoreSubList> orderStoreSubListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderStoreSubListLambdaQueryWrapper.in(OrderStoreSubList::getId, orderStoreSubListIds);
            Map<String, OrderStoreSubList> orderStoreSubListMap = orderStoreSubListService.list().stream().collect(Collectors.toMap(OrderStoreSubList::getId, orderStoreSubList -> orderStoreSubList));

            // 批量查询用户订单商品进行中或者已完成的售后记录列表,用于售后金额、数量判断
            LambdaQueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderRefundListLambdaQueryWrapper.eq(OrderRefundList::getMemberId, memberId)
                    .notIn(OrderRefundList::getStatus, "5", "6", "7")
                    .eq(OrderRefundList::getOrderListId, applyOrderRefundDto.getOrderId())
                    .eq(OrderRefundList::getDelFlag, "0");
            List<OrderRefundList> ongoingOrderRefundList = orderRefundListService.list(orderRefundListLambdaQueryWrapper);
            Map<String, BigDecimal> refundAmountMap = ongoingOrderRefundList.stream().collect(Collectors.groupingBy(OrderRefundList::getOrderGoodRecordId, Collectors.mapping(OrderRefundList::getRefundAmount, Collectors.reducing(BigDecimal.ZERO, NumberUtil::add))));
            Map<String, BigDecimal> refundPriceMap = ongoingOrderRefundList.stream().collect(Collectors.groupingBy(OrderRefundList::getOrderGoodRecordId, Collectors.mapping(OrderRefundList::getRefundPrice, Collectors.reducing(BigDecimal.ZERO, NumberUtil::add))));

            //保存售后申请单
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
                if (StrUtil.containsAny(refundType, "1", "2") && StrUtil.equals(orderStoreSubList.getParentId(), "0")) {
                    throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "还未发货，无法发起退货换货售后申请");
                }
                BigDecimal refundPrice = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundPrice(), orderStoreGoodRecord.getActualPayment());
                BigDecimal refundAmount = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundAmount(), orderStoreGoodRecord.getAmount());

                if (refundAmount.compareTo(NumberUtil.sub(orderStoreGoodRecord.getAmount(),refundAmountMap.getOrDefault(orderStoreGoodRecordId, BigDecimal.ZERO))) > 0) {
                    throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "售后数量大于实际购买数量，请重新填写");
                }
                if (refundPrice.compareTo(NumberUtil.sub(orderStoreGoodRecord.getActualPayment(),refundPriceMap.getOrDefault(orderStoreGoodRecordId,BigDecimal.ZERO))) > 0) {
                    throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "售后金额大于实际支付金额，请重新填写");
                }

                return new OrderRefundList()
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
                        .setRefundType(refundType)
                        .setRefundReason(orderRefundListDto.getRefundReason())
                        .setRemarks(StrUtil.blankToDefault(orderRefundListDto.getRemarks(), applyOrderRefundDto.getRemarks()))
                        .setStatus("0")
                        .setRefundCertificate(StrUtil.blankToDefault(orderRefundListDto.getRefundCertificate(), applyOrderRefundDto.getRefundCertificate()))
                        .setRefundPrice(orderRefundListDto.getRefundPrice())
                        .setRefundAmount(orderRefundListDto.getRefundAmount())
                        .setIsPlatform(isPlatform);
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(orderRefundLists)) {
                orderRefundListService.saveBatch(orderRefundLists);
            }
        } else if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "1")) {
            OrderList orderList = orderListService.getById(applyOrderRefundDto.getOrderId());
            if (orderList == null) {
                throw new JeecgBootException("订单不存在");
            }
            String status = orderList.getStatus();
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
            LambdaQueryWrapper<OrderProviderGoodRecord> orderProviderGoodRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderProviderGoodRecordLambdaQueryWrapper.in(OrderProviderGoodRecord::getId,orderGoodRecordIds);
            List<OrderProviderGoodRecord> orderProviderGoodRecordList = orderProviderGoodRecordService.list(orderProviderGoodRecordLambdaQueryWrapper);
            Map<String, OrderProviderGoodRecord> orderProviderGoodRecordMap = orderProviderGoodRecordList.stream().collect(Collectors.toMap(OrderProviderGoodRecord::getId, orderProviderGoodRecord -> orderProviderGoodRecord));

            //批量查询供应商订单列表
            List<String> orderProviderIds = orderProviderGoodRecordList.stream().map(OrderProviderGoodRecord::getOrderProviderListId).collect(Collectors.toList());
            LambdaQueryWrapper<OrderProviderList> orderProviderListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderProviderListLambdaQueryWrapper.in(OrderProviderList::getId,orderProviderIds);
            Map<String, OrderProviderList> orderProviderListMap = orderProviderListService.list(orderProviderListLambdaQueryWrapper).stream().collect(Collectors.toMap(OrderProviderList::getId, orderProviderList -> orderProviderList));

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
//                BigDecimal refundPrice = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundPrice(), orderProviderGoodRecord.getActualPayment());
//                BigDecimal refundAmount = ObjectUtil.defaultIfNull(orderRefundListDto.getRefundAmount(), orderProviderGoodRecord.getAmount());
//                if (refundAmount.compareTo(NumberUtil.sub(orderProviderGoodRecord.getAmount(),refundAmountMap.getOrDefault(orderStoreGoodRecordId, BigDecimal.ZERO))) > 0) {
//                    throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "售后数量大于实际购买数量，请重新填写");
//                }
//                if (refundPrice.compareTo(NumberUtil.sub(orderStoreGoodRecord.getActualPayment(),refundPriceMap.getOrDefault(orderStoreGoodRecordId,BigDecimal.ZERO))) > 0) {
//                    throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "售后金额大于实际支付金额，请重新填写");
//                }
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
//                        .setGoodRecordActualPayment(orderProviderGoodRecord.getActualPayment())
//                        .setGoodRecordCoupon(orderProviderGoodRecord.getCoupon())
//                        .setGoodRecordGiftCardCoupon(orderProviderGoodRecord.getGiftCardCoupon())
//                        .setGoodRecordTotalCoupon(orderProviderGoodRecord.getTotalCoupon())
                        .setGoodRecordAmount(orderProviderGoodRecord.getAmount())
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
                        .setIsPlatform(isPlatform);
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(orderRefundLists)) {
                orderRefundListService.saveBatch(orderRefundLists);
            }
        }
        return Result.OK();
    }

    /**
     * 分页列表查询
     *
     * @param orderRefundList
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "order_refund_list-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OrderRefundList>> queryPageList(OrderRefundList orderRefundList,
                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest req) {
        QueryWrapper<OrderRefundList> queryWrapper = QueryGenerator.initQueryWrapper(orderRefundList, req.getParameterMap());
        Page<OrderRefundList> page = new Page<OrderRefundList>(pageNo, pageSize);
        IPage<OrderRefundList> pageList = orderRefundListService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 修改申请
     *
     * @param orderRefundList
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:edit")
    @PostMapping(value = "/edit")
    public Result<String> edit(@RequestBody OrderRefundList orderRefundList) {
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
        // TODO: 2023/4/21 退款金额、退款件数、申请类型业务字段校验 @zhangshaolin
        orderRefundListService.updateById(orderRefundList);
        return Result.OK("编辑成功!");
    }

    /**
     * 撤销申请
     *
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:edit")
    @PostMapping(value = "/undo")
    public Result<String> undo(@RequestParam(name = "id") String id) {
        if (StrUtil.isBlank(id)) {
            throw new JeecgBootException("id 不能为空");
        }
        OrderRefundList orderRefundListServiceById = orderRefundListService.getById(id);
        if (orderRefundListServiceById == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        String status = orderRefundListServiceById.getStatus();
        if (!StrUtil.containsAny(status, "0")) {
            throw new JeecgBootException("非待处理售后单无法撤销申请");
        }
        String updateStatus = StrUtil.equals(orderRefundListServiceById.getRefundType(), "2") ? "7" : "6";
        orderRefundListServiceById.setStatus(updateStatus);
        // TODO: 2023/4/21 参考 close_explain 新增关闭原因字典 @zhangshaolin
        orderRefundListServiceById.setCloseExplain("0");
        orderRefundListService.updateById(orderRefundListServiceById);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "order_refund_list-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OrderRefundList> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(orderRefundListService.getOrderRefundListById(id));
    }

    /**
     * 退货退款：填写退货物流
     *
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:edit")
    @PostMapping(value = "/editLogisticsInfo")
    public Result<String> editLogisticsInfo(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "buyerLogisticsCompany") String buyerLogisticsCompany,
            @RequestParam(name = "buyerTrackingNumber") String buyerTrackingNumber) {
        if (StrUtil.isBlank(id)) {
            throw new JeecgBootException("id 不能为空");
        }
        if (StrUtil.hasBlank(buyerLogisticsCompany, buyerTrackingNumber)) {
            throw new JeecgBootException("物流信息不能为空");
        }
        OrderRefundList orderRefundList = orderRefundListService.getById(id);
        if (orderRefundList == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        String status = orderRefundList.getStatus();
        if (!StrUtil.equals(status, "1")) {
            throw new JeecgBootException("售后单状态不是待买家退回，无法操作");
        }
        orderRefundList.setBuyerLogisticsCompany(buyerLogisticsCompany)
                .setBuyerTrackingNumber(buyerTrackingNumber)
                .setStatus("2");
        orderRefundListService.updateById(orderRefundList);
        return Result.OK();
    }

}
