package org.jeecg.modules.order.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
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
    private IOrderRefundListService orderRefundListService;

    @Autowired
    private IMemberShippingAddressService memberShippingAddressService;

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
        String memberId = Convert.toStr(request.getAttribute("memberId"));
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

        // 换货收货地址
        String exchangeMemberShippingAddressJson = "";
        if (StrUtil.equals(refundType, "2")) {
            if (CollUtil.size(applyOrderRefundDto.getOrderRefundListDtos()) > 1) {
                throw new JeecgBootException("换货只能选择一种商品");
            }
            if (StringUtils.isBlank(applyOrderRefundDto.getMemberShippingAddressId())) {
                throw new JeecgBootException("收货地址id不能为空！！！");
            }
            //查询收货地址
            MemberShippingAddress memberShippingAddress = memberShippingAddressService.getById(applyOrderRefundDto.getMemberShippingAddressId());
            if (memberShippingAddress == null) {
                throw new JeecgBootException("收货地址不存在！！！");
            }
            // 设置收货地址
            JSONObject exchangeMemberShippingAddress = new JSONObject();
            exchangeMemberShippingAddress.put("consignee", memberShippingAddress.getLinkman());
            exchangeMemberShippingAddress.put("contactNumber", memberShippingAddress.getPhone());
            exchangeMemberShippingAddress.put("shippingAddress", memberShippingAddress.getAreaExplan() + memberShippingAddress.getAreaAddress());
            exchangeMemberShippingAddress.put("houseNumber", memberShippingAddress.getHouseNumber());
            exchangeMemberShippingAddressJson = exchangeMemberShippingAddress.toJSONString();
        }

        if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "0")) {
            orderRefundListService.applyOrderStoreRefund(applyOrderRefundDto, memberId, exchangeMemberShippingAddressJson);
        } else if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "1")) {
            orderRefundListService.applyOrderRefund(applyOrderRefundDto, memberId, exchangeMemberShippingAddressJson);
        }
        return Result.OK("申请成功");
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
    @PostMapping(value = "/edit")
    public Result<String> edit(@RequestBody OrderRefundList orderRefundList) {
        return orderRefundListService.editApplyRefund(orderRefundList);
    }

    /**
     * 撤销申请
     *
     * @param id 售后单id
     * @return
     */
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
        orderRefundListServiceById.setCloseExplain("0");
        orderRefundListService.updateById(orderRefundListServiceById);
        return Result.OK("撤销成功!");
    }

    /**
     * 通过id查询
     *
     * @param id 售后单id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<OrderRefundList> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(orderRefundListService.getOrderRefundListById(id));
    }


    /**
     * 退货退款(换货)：填写物流信息
     *
     * @param id                    售后单id
     * @param buyerLogisticsCompany 买家寄回物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；
     * @param buyerTrackingNumber   买家寄回快递单号
     * @return
     */
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
        return Result.OK("寄出成功");
    }

}
