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
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.config.jwt.def.JwtConstants;
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

    @Autowired
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;

    @Autowired
    private IOrderStoreGoodRecordService orderStoreGoodRecordService;

    /**
     * 申请订单售后
     *
     * @param applyOrderRefundDto
     * @param request
     * @return
     */
    @PostMapping(value = "/apply")
    public Result<List<OrderRefundList>> applyOrderRefund(@RequestBody ApplyOrderRefundDto applyOrderRefundDto, HttpServletRequest request) {
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
        String exchangeMemberShippingAddressJson = null;
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

        List<OrderRefundList> orderRefundLists = CollUtil.newArrayList();
        if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "0")) {
            orderRefundLists = orderRefundListService.applyOrderStoreRefund(applyOrderRefundDto, memberId, exchangeMemberShippingAddressJson);
        } else if (StrUtil.equals(applyOrderRefundDto.getIsPlatform(), "1")) {
            orderRefundLists = orderRefundListService.applyOrderRefund(applyOrderRefundDto, memberId, exchangeMemberShippingAddressJson);
        }
        return Result.OK(orderRefundLists);
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
        String memberId = Convert.toStr(req.getAttribute(JwtConstants.CURRENT_USER_NAME));
        QueryWrapper<OrderRefundList> queryWrapper = QueryGenerator.initQueryWrapper(orderRefundList, req.getParameterMap());
        queryWrapper.eq("member_id", memberId);
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
        if (!StrUtil.containsAny(status, "0", "5")) {
            throw new JeecgBootException("非待处理/已拒绝售后单无法撤销申请");
        }
        String updateStatus = StrUtil.equals(orderRefundListServiceById.getRefundType(), "2") ? "7" : "6";
        orderRefundListServiceById.setStatus(updateStatus);
        orderRefundListServiceById.setCloseExplain("0");
        orderRefundListService.updateById(orderRefundListServiceById);

        String orderGoodRecordId = orderRefundListServiceById.getOrderGoodRecordId();
        String isPlatform = orderRefundListServiceById.getIsPlatform();
        if (StrUtil.equals(isPlatform,"0")) {
            OrderStoreGoodRecord orderStoreGoodRecord = new OrderStoreGoodRecord().setId(orderGoodRecordId).setStatus("0");
            orderStoreGoodRecordService.updateById(orderStoreGoodRecord);
        }else if (StrUtil.equals(isPlatform,"1")){
            OrderProviderGoodRecord orderProviderGoodRecord = new OrderProviderGoodRecord().setId(orderGoodRecordId).setStatus("0");
            orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
        }

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

    /**
     * 待退货售后单单计时器
     *
     * @param id 售后单id
     * @return
     */
    @GetMapping("/refundOrderTimer")
    public Result<Map<String, Object>> refundOrderTimer(String id) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();

        if (StringUtils.isBlank(id)) {
            result.error500("id不能为空！！！");
            return result;
        }
        //获取倒计时时间
        String timer = orderRefundListService.refundOrderTimer(id);
        if (StringUtils.isBlank(timer)) {
            result.error500("未找到售后单倒计时数据!");
            return result;
        }
        objectMap.put("timer", timer);
        result.setResult(objectMap);
        result.success("请求成功");
        return result;
    }

    /**
     * 通过id删除
     * <p>
     * 仅客户端，售后完成订单可删除
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        OrderRefundList orderRefundList = orderRefundListService.getById(id);
        String status = orderRefundList.getStatus();
        if (!StrUtil.containsAny(status, "4", "8")) {
            throw new JeecgBootException("售后完成才能删除");
        }
        orderRefundListService.removeById(id);
        return Result.OK("删除成功!");
    }

}
