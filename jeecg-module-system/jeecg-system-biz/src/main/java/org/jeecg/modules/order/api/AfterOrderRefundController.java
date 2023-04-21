package org.jeecg.modules.order.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.order.dto.ApplyOrderRefundDto;
import org.jeecg.modules.order.dto.OrderRefundListDto;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.order.service.IOrderStoreSubListService;
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

    /**
     * 申请订单售后
     *
     * @param applyOrderRefundDto
     * @param request
     * @return
     */
    @PostMapping(value = "/apply")
    public Result<String> applyOrderRefund(@RequestBody ApplyOrderRefundDto applyOrderRefundDto, HttpServletRequest request) {
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
        String memberId = Convert.toStr(request.getAttribute("memberId"));
        if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "0")) {
            if (CollUtil.isEmpty(applyOrderRefundDto.getOrderStoreRefundLists())) {
                throw new JeecgBootException("orderStoreRefundLists 不能为空");
            }
            List<String> orderStoreGoodRecordIds = applyOrderRefundDto.getOrderStoreRefundLists().stream().map(OrderRefundListDto::getOrderStoreGoodRecordId).collect(Collectors.toList());
            if (CollUtil.isEmpty(orderStoreGoodRecordIds)) {
                throw new JeecgBootException("orderStoreGoodRecordIds 不能为空");
            }

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
            orderStoreGoodRecordLambdaQueryWrapper.in(OrderStoreGoodRecord::getId, orderStoreGoodRecordIds);
            List<OrderStoreGoodRecord> orderStoreGoodRecordList = orderStoreGoodRecordService.list(orderStoreGoodRecordLambdaQueryWrapper);
            Map<String, OrderStoreGoodRecord> orderStoreGoodRecordMap = orderStoreGoodRecordList.stream().collect(Collectors.toMap(OrderStoreGoodRecord::getId, orderStoreGoodRecord -> orderStoreGoodRecord));

            //批量查询供应商订单列表
            List<String> orderStoreSubListIds = orderStoreGoodRecordList.stream().map(OrderStoreGoodRecord::getOrderStoreSubListId).collect(Collectors.toList());
            LambdaQueryWrapper<OrderStoreSubList> orderStoreSubListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderStoreSubListLambdaQueryWrapper.in(OrderStoreSubList::getId, orderStoreSubListIds);
            Map<String, OrderStoreSubList> orderStoreSubListMap = orderStoreSubListService.list().stream().collect(Collectors.toMap(OrderStoreSubList::getId, orderStoreSubList -> orderStoreSubList));

            // TODO: 2023/4/19  批量查询订单商品售后记录列表,用于售后金额、数量判断 @zhangshaolin

            //保存售后申请单
            List<OrderRefundList> orderRefundLists = applyOrderRefundDto.getOrderStoreRefundLists().stream().map(orderRefundListDto -> {
                String orderStoreGoodRecordId = orderRefundListDto.getOrderStoreGoodRecordId();
                if (StrUtil.isBlank(orderStoreGoodRecordId)) {
                    throw new JeecgBootException("orderStoreGoodRecordId 订单商品不能为空");
                }
                OrderStoreGoodRecord orderStoreGoodRecord = orderStoreGoodRecordMap.get(orderStoreGoodRecordId);
                if (orderStoreGoodRecord == null) {
                    throw new JeecgBootException("订单商品id: " + orderRefundListDto.getOrderStoreGoodRecordId() + "不存在");
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

                // TODO: 2023/4/20 这里要结合实际售后历史做计算，再判断比较准确 @zhangshaolin
                if (refundAmount.compareTo(orderStoreGoodRecord.getAmount()) > 0) {
                    throw new JeecgBootException("订单商品" + orderStoreGoodRecord.getId() + "售后数量大于实际购买数量，请重新填写");
                }
                if (refundPrice.compareTo(orderStoreGoodRecord.getActualPayment()) > 0) {
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
                        .setMemberId(memberId)
                        .setOrderListId(orderStoreList.getId())
                        .setSysUserId(orderStoreList.getSysUserId())
                        .setOrderSubListId(orderStoreGoodRecord.getOrderStoreSubListId())
                        .setRefundType(refundType)
                        .setRefundReason(orderRefundListDto.getRefundReason())
                        .setRemarks(StrUtil.blankToDefault(orderRefundListDto.getRemarks(), applyOrderRefundDto.getRemarks()))
                        .setStatus("0")
                        .setRefundCertificate(StrUtil.blankToDefault(orderRefundListDto.getRefundCertificate(), applyOrderRefundDto.getRefundCertificate()))
                        .setRefundPrice(orderRefundListDto.getRefundPrice())
                        .setRefundAmount(orderRefundListDto.getRefundAmount())
                        .setIsPlatform(isPlatform)
                        .setGoodRecordTotal(orderStoreGoodRecord.getTotal())
                        .setGoodRecordActualPayment(orderStoreGoodRecord.getActualPayment())
                        .setGoodRecordCoupon(orderStoreGoodRecord.getCoupon())
                        .setGoodRecordAmount(orderStoreGoodRecord.getAmount());
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(orderRefundLists)) {
                orderRefundListService.saveBatch(orderRefundLists);
            }
        } else if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "1")) {
            // TODO: 2023/4/20 平台订单售后申请 @zhangshaolin
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
    @ApiOperation(value = "order_refund_list-分页列表查询", notes = "order_refund_list-分页列表查询")
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
    @AutoLog(value = "order_refund_list-编辑")
    @ApiOperation(value = "order_refund_list-编辑", notes = "order_refund_list-编辑")
    //@RequiresPermissions("order:order_refund_list:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
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
     * 修改申请
     *
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:edit")
    @RequestMapping(value = "/undo", method = {RequestMethod.PUT, RequestMethod.POST})
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
    @ApiOperation(value = "order_refund_list-通过id查询", notes = "order_refund_list-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OrderRefundList> queryById(@RequestParam(name = "id", required = true) String id) {
        OrderRefundList orderRefundList = orderRefundListService.getById(id);
        if (orderRefundList == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(orderRefundList);
    }

    /**
     * 填写退货物流
     *
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:edit")
    @RequestMapping(value = "/editLogisticsInfo", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> editLogisticsInfo(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "buyerLogisticsCompany") String buyerLogisticsCompany,
            @RequestParam(name = "buyerTrackingNumber") String buyerTrackingNumber) {
        // TODO: 2023/4/21 填写退货物流 @zhangshaolin
        if (StrUtil.isBlank(id)) {
            throw new JeecgBootException("id 不能为空");
        }
        if (StrUtil.hasBlank(buyerLogisticsCompany, buyerTrackingNumber)) {
            throw new JeecgBootException("物流信息不能为空");
        }
        OrderRefundList orderRefundListServiceById = orderRefundListService.getById(id);
        if (orderRefundListServiceById == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        return Result.OK();
    }

}
