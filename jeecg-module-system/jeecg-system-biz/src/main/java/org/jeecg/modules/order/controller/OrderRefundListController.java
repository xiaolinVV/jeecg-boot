package org.jeecg.modules.order.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date: 2023-04-20
 * @Version: V1.0
 */
@Api(tags = "order_refund_list")
@RestController
@RequestMapping("/order/orderRefundList")
@Slf4j
public class OrderRefundListController extends JeecgController<OrderRefundList, IOrderRefundListService> {
    @Autowired
    private IOrderRefundListService orderRefundListService;

    /**
     * 售后分页列表查询
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
     * 添加
     *
     * @param orderRefundList
     * @return
     */
    @AutoLog(value = "order_refund_list-添加")
    @ApiOperation(value = "order_refund_list-添加", notes = "order_refund_list-添加")
    //@RequiresPermissions("order:order_refund_list:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody OrderRefundList orderRefundList) {
        orderRefundListService.save(orderRefundList);
        return Result.OK("添加成功！");
    }

    /**
     * 拒绝
     *
     * @param id             售后单id
     * @param refusedExplain 拒绝原因
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:add")
    @PostMapping(value = "/refused")
    public Result<String> refused(@RequestParam("id") String id, @RequestParam("refusedExplain") String refusedExplain) {
        if (StrUtil.isBlank(id)) {
            throw new JeecgBootException("id 不能为空");
        }
        if (StrUtil.isBlank(refusedExplain)) {
            throw new JeecgBootException("请填写拒绝原因");
        }
        OrderRefundList orderRefundListServiceById = orderRefundListService.getById(id);
        if (orderRefundListServiceById == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        String status = orderRefundListServiceById.getStatus();
        if (!StrUtil.containsAny(status, "0")) {
            throw new JeecgBootException("非待处理售后单无法拒绝");
        }
        orderRefundListServiceById.setStatus("5");
        orderRefundListServiceById.setRefusedExplain(refusedExplain);
        orderRefundListService.updateById(orderRefundListServiceById);
        return Result.OK("拒绝成功！");
    }


    /**
     * 仅退款：后台确认退款，点击通过后：填写退款金额：微信渠道、余额、礼品卡、福利金
     * 退款退货：后台确认退货退款，点击通过后：客服回填买家邮寄地址： 收件人+电话+地址
     * 换货：后台点击通过后（客服回填买家邮寄地址）：收件人+电话+地址
     *
     * @param id                          售后单id
     * @param actualRefundPrice           退款金额（微信）
     * @param actualRefundBalance         退款余额
     * @param merchantConsigneeName       商家收件人姓名
     * @param merchantConsigneeAddress    商家收件地址
     * @param merchantConsigneePhone      商家收件手机号
     * @param merchantConsigneeProvinceId 省
     * @param merchantConsigneeCityId     市
     * @param merchantConsigneeAreaId     区
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:add")
    @PostMapping(value = "/pass")
    public Result<String> pass(@RequestParam("id") String id,
                               @RequestParam(value = "actualRefundPrice", required = false) BigDecimal actualRefundPrice,
                               @RequestParam(value = "actualRefundBalance", required = false) BigDecimal actualRefundBalance,
                               @RequestParam(value = "merchantConsigneeName", required = false) String merchantConsigneeName,
                               @RequestParam(value = "merchantConsigneeAddress", required = false) String merchantConsigneeAddress,
                               @RequestParam(value = "merchantConsigneePhone", required = false) String merchantConsigneePhone,
                               @RequestParam(value = "merchantConsigneeProvinceId", required = false) String merchantConsigneeProvinceId,
                               @RequestParam(value = "merchantConsigneeCityId", required = false) String merchantConsigneeCityId,
                               @RequestParam(value = "merchantConsigneeAreaId", required = false) String merchantConsigneeAreaId
    ) {
        if (StrUtil.isBlank(id)) {
            throw new JeecgBootException("id 不能为空");
        }
        OrderRefundList orderRefundList = orderRefundListService.getById(id);
        if (orderRefundList == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        String status = orderRefundList.getStatus();
        if (!StrUtil.containsAny(status, "0")) {
            throw new JeecgBootException("非待处理售后单无法点击通过");
        }
        String refundType = orderRefundList.getRefundType();
        if (StrUtil.equals(refundType, "0")) {
            orderRefundListService.refund(orderRefundList, actualRefundPrice, actualRefundBalance);
        } else if (StrUtil.containsAny(refundType, "1", "2")) {
            if (StrUtil.hasBlank(merchantConsigneeName, merchantConsigneePhone, merchantConsigneeAddress, merchantConsigneeProvinceId, merchantConsigneeCityId, merchantConsigneeAreaId)) {
                throw new JeecgBootException("邮寄地址信息不能为空");
            }
            orderRefundList.setMerchantConsigneeName(merchantConsigneeName);
            orderRefundList.setMerchantConsigneePhone(merchantConsigneePhone);
            orderRefundList.setMerchantConsigneeAddress(merchantConsigneeAddress);
            orderRefundList.setMerchantConsigneeProvinceId(merchantConsigneeProvinceId);
            orderRefundList.setMerchantConsigneeCityId(merchantConsigneeCityId);
            orderRefundList.setMerchantConsigneeAreaId(merchantConsigneeAreaId);
            orderRefundList.setStatus("1");
        }
        orderRefundListService.updateById(orderRefundList);
        return Result.OK("通过成功！");
    }


    /**
     * 退款退货：后台查看买家的物流，点击确认收货后，进行退款
     *
     * @param id                  售后单id
     * @param actualRefundPrice   退款金额（微信）
     * @param actualRefundBalance 退款余额
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:add")
    @PostMapping(value = "/confirm")
    public Result<String> confirm(@RequestParam("id") String id,
                                  @RequestParam(value = "actualRefundPrice", required = false) BigDecimal actualRefundPrice,
                                  @RequestParam(value = "actualRefundBalance", required = false) BigDecimal actualRefundBalance) {
        if (StrUtil.isBlank(id)) {
            throw new JeecgBootException("id 不能为空");
        }
        OrderRefundList orderRefundList = orderRefundListService.getById(id);
        if (orderRefundList == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        String status = orderRefundList.getStatus();
        if (StrUtil.equals(status, "2")) {
            throw new JeecgBootException("售后状态不是待商家确认收货，无法操作");
        }
        orderRefundListService.refund(orderRefundList, actualRefundPrice, actualRefundBalance);
        return Result.OK("确认收货成功，退款中");
    }

    /**
     * 换货：商家发货
     *
     * @param id                       售后id
     * @param merchantLogisticsCompany 换货：商家物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；
     * @param merchantTrackingNumber   换货：商家快递单号
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:edit")
    @PostMapping(value = "/editLogisticsInfo")
    public Result<String> editLogisticsInfo(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "merchantLogisticsCompany") String merchantLogisticsCompany,
            @RequestParam(name = "merchantTrackingNumber") String merchantTrackingNumber) {
        if (StrUtil.isBlank(id)) {
            throw new JeecgBootException("id 不能为空");
        }
        if (StrUtil.hasBlank(merchantLogisticsCompany, merchantTrackingNumber)) {
            throw new JeecgBootException("物流信息不能为空");
        }
        OrderRefundList orderRefundList = orderRefundListService.getById(id);
        if (orderRefundList == null) {
            throw new JeecgBootException("该售后单不存在");
        }
        String status = orderRefundList.getStatus();
        if (!StrUtil.equals(status, "2")) {
            throw new JeecgBootException("售后单状态不是换货中，无法操作");
        }
        orderRefundList.setMerchantLogisticsCompany(merchantLogisticsCompany)
                .setMerchantTrackingNumber(merchantTrackingNumber)
                .setStatus("8");
        orderRefundListService.updateById(orderRefundList);
        return Result.OK("换货成功，商家已发货");
    }

    /**
     * 编辑
     *
     * @param orderRefundList
     * @return
     */
    @AutoLog(value = "order_refund_list-编辑")
    @ApiOperation(value = "order_refund_list-编辑", notes = "order_refund_list-编辑")
    //@RequiresPermissions("order:order_refund_list:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody OrderRefundList orderRefundList) {
        orderRefundListService.updateById(orderRefundList);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "order_refund_list-通过id删除")
    @ApiOperation(value = "order_refund_list-通过id删除", notes = "order_refund_list-通过id删除")
    //@RequiresPermissions("order:order_refund_list:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        orderRefundListService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "order_refund_list-批量删除")
    @ApiOperation(value = "order_refund_list-批量删除", notes = "order_refund_list-批量删除")
    //@RequiresPermissions("order:order_refund_list:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.orderRefundListService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<OrderRefundList> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(orderRefundListService.getOrderRefundListById(id));
    }

    /**
     * 导出excel
     *
     * @param request
     * @param orderRefundList
     */
    //@RequiresPermissions("order:order_refund_list:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OrderRefundList orderRefundList) {
        return super.exportXls(request, orderRefundList, OrderRefundList.class, "order_refund_list");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("order:order_refund_list:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OrderRefundList.class);
    }

}
