package org.jeecg.modules.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.order.dto.OrderListDTO;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.order.vo.OrderListVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "订单列表")
@RestController
@RequestMapping("/orderList/orderList")
public class OrderListController {
    @Autowired
    private IOrderListService orderListService;
    @Autowired
    private IMemberShippingAddressService memberShippingAddressService;
    @Autowired
    private IOrderProviderListService orderProviderListService;
    @Autowired
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;




    /**
     * 分页列表查询
     *
     * @param orderListVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "订单列表-分页列表查询")
    @ApiOperation(value = "订单列表-分页列表查询", notes = "订单列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OrderListDTO>> queryPageList(OrderListVO orderListVO,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        Result<IPage<OrderListDTO>> result = new Result<IPage<OrderListDTO>>();
        Page<OrderList> page = new Page<OrderList>(pageNo, pageSize);
        //判断当前用户是否是平台，是 STR=NULL 不是STR= UserId
        String UserId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(UserId)) {
            orderListVO.setSysUserId(UserId);
        }
        IPage<OrderListDTO> pageList = orderListService.getOrderListDto(page, orderListVO, UserId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    @RequestMapping({"refundAndAbrogateOrder"})
    @ResponseBody
    public Result<?> refundAndAbrogateOrder(String id, String closeExplain) {
        return StringUtils.isBlank(id) ? Result.error("订单id不能为空") : this.orderListService.refundAndAbrogateOrder(id, closeExplain, "4");
    }


    /**
     * 添加
     *
     * @param orderList
     * @return
     */
    @AutoLog(value = "订单列表-添加")
    @ApiOperation(value = "订单列表-添加", notes = "订单列表-添加")
    @PostMapping(value = "/add")
    public Result<OrderList> add(@RequestBody OrderList orderList) {
        Result<OrderList> result = new Result<OrderList>();
        try {
            orderListService.save(orderList);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param orderList
     * @return
     */
    @AutoLog(value = "订单列表-编辑")
    @ApiOperation(value = "订单列表-编辑", notes = "订单列表-编辑")
    @PutMapping(value = "/edit")
    public Result<OrderList> edit(@RequestBody OrderList orderList) {
        Result<OrderList> result = new Result<OrderList>();
        OrderList orderListEntity = orderListService.getById(orderList.getId());
        if (orderListEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = orderListService.updateById(orderList);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单列表-通过id删除")
    @ApiOperation(value = "订单列表-通过id删除", notes = "订单列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            orderListService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "订单列表-批量删除")
    @ApiOperation(value = "订单列表-批量删除", notes = "订单列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<OrderList> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<OrderList> result = new Result<OrderList>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.orderListService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单列表-通过id查询")
    @ApiOperation(value = "订单列表-通过id查询", notes = "订单列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OrderList> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OrderList> result = new Result<OrderList>();
        OrderList orderList = orderListService.getById(id);
        if (orderList == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(orderList);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public void exportXls(OrderListVO orderListVO, HttpServletResponse response) throws IOException {

        String UserId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(UserId)) {
            orderListVO.setSysUserId(UserId);
        }

        Map map = JSON.parseObject(JSON.toJSONString(new BeanMap(orderListVO)), Map.class);
        orderListService.exportXls(orderListVO, response);
    }


    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<OrderList> listOrderLists = ExcelImportUtil.importExcel(file.getInputStream(), OrderList.class, params);
                orderListService.saveBatch(listOrderLists);
                return Result.ok("文件导入成功！数据行数:" + listOrderLists.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }


    /***********************************不同状态的订单列表*****************************************************/
    /**
     * 取消订单分页列表查询
     *
     * @param orderListVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "取消订单列表-分页列表查询")
    @ApiOperation(value = "取消订单列表-分页列表查询", notes = "订单列表-分页列表查询")
    @GetMapping(value = "/queryPageListCancel")
    public Result<IPage<OrderListDTO>> queryPageListCancel(OrderListVO orderListVO,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        Result<IPage<OrderListDTO>> result = new Result<IPage<OrderListDTO>>();
        Page<OrderList> page = new Page<OrderList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String UserId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(UserId)) {
            orderListVO.setSysUserId(UserId);
        }
        orderListVO.setStatus("4");
        IPage<OrderListDTO> pageList = orderListService.getOrderListDto(page, orderListVO, UserId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 待付款分页列表查询
     *
     * @param orderListVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "待付款订单列表-分页列表查询")
    @ApiOperation(value = "待付款订单列表-分页列表查询", notes = "订单列表-分页列表查询")
    @GetMapping(value = "/queryPageListObligation")
    public Result<IPage<OrderListDTO>> queryPageListObligation(OrderListVO orderListVO,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req) {
        Result<IPage<OrderListDTO>> result = new Result<IPage<OrderListDTO>>();
        Page<OrderList> page = new Page<OrderList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String UserId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(UserId)) {
            orderListVO.setSysUserId(UserId);
        }
        orderListVO.setStatus("0");
        IPage<OrderListDTO> pageList = orderListService.getOrderListDto(page, orderListVO, UserId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 待发货订单分页列表查询
     *
     * @param orderListVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "待发货订单列表-分页列表查询")
    @ApiOperation(value = "待发货订单列表-分页列表查询", notes = "订单列表-分页列表查询")
    @GetMapping(value = "/queryPageListToSendTheGoods")
    public Result<IPage<OrderListDTO>> queryPageListToSendTheGoods(OrderListVO orderListVO,
                                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                   HttpServletRequest req) {
        Result<IPage<OrderListDTO>> result = new Result<IPage<OrderListDTO>>();
        Page<OrderList> page = new Page<OrderList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String UserId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(UserId)) {
            orderListVO.setSysUserId(UserId);
        }
        orderListVO.setStatus("1");
        IPage<OrderListDTO> pageList = orderListService.getOrderListDto(page, orderListVO, UserId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 待收货订单分页列表查询
     *
     * @param orderListVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "待收货订单列表-分页列表查询")
    @ApiOperation(value = "待收货订单列表-分页列表查询", notes = "订单列表-分页列表查询")
    @GetMapping(value = "/queryPageListWaitForReceiving")
    public Result<IPage<OrderListDTO>> queryPageListWaitForReceiving(OrderListVO orderListVO,
                                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     HttpServletRequest req) {
        Result<IPage<OrderListDTO>> result = new Result<IPage<OrderListDTO>>();
        Page<OrderList> page = new Page<OrderList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String UserId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(UserId)) {
            orderListVO.setSysUserId(UserId);
        }
        orderListVO.setStatus("2");
        IPage<OrderListDTO> pageList = orderListService.getOrderListDtoToSendTheGoods(page, orderListVO, UserId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 交易成功订单分页列表查询
     *
     * @param orderListVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "交易成功订单列表-分页列表查询")
    @ApiOperation(value = "交易成功订单列表-分页列表查询", notes = "订单列表-分页列表查询")
    @GetMapping(value = "/queryPageListDealsAreDone")
    public Result<IPage<OrderListDTO>> queryPageListDealsAreDone(OrderListVO orderListVO,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<OrderListDTO>> result = new Result<IPage<OrderListDTO>>();
        Page<OrderList> page = new Page<OrderList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String UserId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(UserId)) {
            orderListVO.setSysUserId(UserId);
        }
        orderListVO.setStatus("3");
        IPage<OrderListDTO> pageList = orderListService.getOrderListDtoToSendTheGoods(page, orderListVO, UserId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 通过id查询修改订单状态：
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单列表-通过id查询")
    @ApiOperation(value = "订单列表-通过id查询", notes = "订单列表-通过id查询")
    @GetMapping(value = "/updateStatus")
    public Result<OrderList> updateStatus(
            @RequestParam(name = "id", required = true) String id,
            @RequestParam(name = "closeExplain", required = true) String closeExplain,
            String status, String closeType) {
        Result<OrderList> result = new Result<OrderList>();
        OrderList orderList = orderListService.getById(id);
        if (orderList == null) {
            result.error500("未找到对应实体");
        } else {
            if (status != null) {
                orderList.setStatus(status);
            }
            if (status.equals("4")) {
                orderListService.abrogateOrder(id, closeExplain, closeType);
                result.success("修改成功!");
            } else {

                if (closeType != null) {
                    orderList.setCloseType(closeType);
                }
                orderList.setCloseExplain(closeExplain);
                boolean ok = orderListService.updateById(orderList);
                if (ok) {
                    result.success("修改成功!");
                } else {
                    result.error500("修改失败！");
                }
            }


        }
        return result;
    }

    /**
     * 通过id查询修改地址：
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单列表-通过id查询")
    @ApiOperation(value = "订单列表-通过id查询", notes = "订单列表-通过id查询")
    @GetMapping(value = "/updateAddress")
    public Result<OrderList> updateAddress(
            @RequestParam(name = "id", required = true) String id,
            @RequestParam(name = "areaAddressId", required = true) String areaAddressId) {
        Result<OrderList> result = new Result<OrderList>();
        OrderList orderList = orderListService.getById(id);
        if (orderList == null) {
            result.error500("未找到对应实体");
        } else {
            //根据Id获取修改地址数据
            MemberShippingAddress memberShippingAddress = memberShippingAddressService.getById(areaAddressId);
            orderList.setShippingAddress(memberShippingAddress.getAreaAddress());
            orderList.setConsignee(memberShippingAddress.getLinkman());
            orderList.setContactNumber(memberShippingAddress.getPhone());
            orderList.setHouseNumber(memberShippingAddress.getHouseNumber());
            orderList.setIsUpdateAddr("1");
            boolean ok = orderListService.updateById(orderList);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /**
     * 订单发货
     *
     * @param listMap
     * @return
     */
    @PostMapping(value = "/ordereDlivery")
    public Result<String> ordereDlivery(@RequestBody String listMap) {
        Result<String> result = new Result<String>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(listMap);
            List<Map<String, Object>> listObjectSec = jsonObject.getJSONArray("listMap").toJavaObject(List.class);
            OrderProviderList orderProviderList = new OrderProviderList();
            OrderProviderGoodRecord orderProviderGoodRecord;
            List<Map<String, Object>> orderProviderGoodRecordInfoList;
            String addorderProviderId;
            if (listObjectSec.size() > 0) {
                for (Map<String, Object> map : listObjectSec) {
                    //包裹数据
                    List<Map<String, Object>> listParcelMapSS = (List<Map<String, Object>>) (List) map.get("listParcel");
                    for (Map<String, Object> listParcelMap : listParcelMapSS) {

                        //包裹内的商品修改供应商订单id
                        orderProviderGoodRecordInfoList = (List<Map<String, Object>>) (List) listParcelMap.get("orderProviderGoodRecordInfo");

                        if (orderProviderGoodRecordInfoList.size() > 0) {
                            //查询供应商订单信息
                            orderProviderList = orderProviderListService.getById(listParcelMap.get("id").toString());
                            //添加包裹,并返回新增ID
                            addorderProviderId = addorderProviderList(orderProviderList, listParcelMap.get("logisticsCompany").toString(), listParcelMap.get("trackingNumber").toString());

                            for (Map<String, Object> orderProviderGoodRecordId : orderProviderGoodRecordInfoList) {
                                //订单商品信息
                                orderProviderGoodRecord = orderProviderGoodRecordService.getById(orderProviderGoodRecordId.get("id").toString());
                                if (orderProviderGoodRecord != null) {
                                    //修改商品的OrderProviderListId为包裹的已发货包裹
                                    orderProviderGoodRecord.setOrderProviderListId(addorderProviderId);
                                    orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
                                }
                            }
                        }
                    }
                    //调用方法
                    //是否全部发货,修改orderList的状态内容
                    if (StringUtils.isNotBlank(orderProviderList.getId())){
                        orderProviderListService.ShipmentOrderModification(orderProviderList);
                    }
                }
                result.success("发货成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.error500("发货失败！");
        }
        return result;
    }

    /**
     * 添加已发货的供应商包裹信息
     *
     * @param orderProviderList
     * @param logisticsCompany  物流公司
     * @param trackingNumber    快递单号
     */
    public String addorderProviderList(OrderProviderList orderProviderList, String logisticsCompany, String trackingNumber) {
        try {
            OrderProviderList opl = new OrderProviderList();
            opl.setDelFlag(orderProviderList.getDelFlag());
            opl.setMemberListId(orderProviderList.getMemberListId());
            opl.setOrderListId(orderProviderList.getOrderListId());
            opl.setSysUserId(orderProviderList.getSysUserId());
            opl.setOrderNo(orderProviderList.getOrderNo());
            opl.setDistribution(orderProviderList.getDistribution());
            opl.setShipFee(orderProviderList.getShipFee());
            opl.setProviderAddressIdSender(orderProviderList.getProviderAddressIdSender());
            opl.setProviderAddressIdTui(orderProviderList.getProviderAddressIdTui());
            opl.setLogisticsTracking(orderProviderList.getLogisticsTracking());
            opl.setGoodsTotal(orderProviderList.getGoodsTotal());
            opl.setGoodsTotalCost(orderProviderList.getGoodsTotalCost());
            opl.setCustomaryDues(orderProviderList.getCustomaryDues());
            opl.setActualPayment(orderProviderList.getActualPayment());
            //修改数据
            opl.setParentId(orderProviderList.getId());
            opl.setLogisticsCompany(logisticsCompany);
            opl.setTrackingNumber(trackingNumber);
            opl.setStatus("2");
            orderProviderListService.save(opl);
            String id = opl.getId();
            //发送包裹消息（提醒包裹已发出）
			/*EmailSendMsgHandle eh = new EmailSendMsgHandle();
		       MemberList memberList = memberListService.getById(opl.getMemberListId()) ;
			eh.SendMsg("274794391@qq.com", "系统推送","您的包裹已发出");*/
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * //是否全部发货,修改orderList的状态内容
     *
     * @param orderProviderList
     * @return
     */
    public void ShipmentOrderModification(OrderProviderList orderProviderList) {
        if (orderProviderList != null) {
            List<OrderProviderGoodRecord> listOrderProviderGoodRecord;
            Boolean bl = false;
            OrderList orderList = orderListService.getById(orderProviderList.getOrderListId());
            QueryWrapper<OrderProviderList> queryWrapper = new QueryWrapper<OrderProviderList>();
            QueryWrapper<OrderProviderGoodRecord> queryWrapperOPGR = new QueryWrapper<OrderProviderGoodRecord>();
            if (orderList != null) {
                queryWrapper.eq("order_list_id", orderList.getId());
                //queryWrapper.ne("parent_id","0");
                //判断是否有发货
                List<OrderProviderList> listOrderProviderList = orderProviderListService.list(queryWrapper);
                if (listOrderProviderList.size() > 0) {
                    for (OrderProviderList oplfh : listOrderProviderList) {

                        if (!"0".equals(oplfh.getParentId())) {
                            //部分发货
                            orderList.setIsSender("1");
                        } else {
                            //是否全部发货
                            queryWrapperOPGR = new QueryWrapper<OrderProviderGoodRecord>();
                            queryWrapperOPGR.eq("order_provider_list_id", oplfh.getId());
                            listOrderProviderGoodRecord = orderProviderGoodRecordService.list(queryWrapperOPGR);
                            if (listOrderProviderGoodRecord.size() > 0) {
                                //说明还没为发货商品
                                bl = true;
                            }
                        }
                    }
                    //bl = false 为已全部发货，改变状态为待收货
                    if (!bl) {
                        orderList.setStatus("2");
                    }
                    if (orderList.getShipmentsTime() == null || "".equals(orderList.getShipmentsTime())) {
                        //第一次发货时间
                        orderList.setShipmentsTime(new Date());
                    }
                    //子订单数量
                    orderList.setChildOrder(new BigDecimal(listOrderProviderList.size()));
                    //修改订单信息
                    orderListService.updateById(orderList);
                }
            }
        }
    }


    /**
     * 待支付订单计时器
     *
     * @param orderId
     * @param isPlatform
     * @return
     */
    @RequestMapping("prepaidOrderTimer")
    @ResponseBody
    public Result<Map<String, Object>> prepaidOrderTimer(String orderId, Integer isPlatform) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();
        //参数判断
        if (isPlatform == null) {
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(orderId)) {
            result.error500("orderId订单id不能为空！！！");
            return result;
        }
        //获取倒计时时间
        String timer = orderListService.prepaidOrderTimer(orderId, isPlatform);
        if (org.apache.commons.lang3.StringUtils.isBlank(timer)) {
            result.error500("未找到订单倒计时数据!");
            return result;
        }
        objectMap.put("timer", timer);
        result.setResult(objectMap);
        result.success("请求成功");
        return result;

    }


    /**
     * 确认收货订单计时器
     *
     * @param orderId
     * @param isPlatform
     * @return
     */
    @RequestMapping("confirmReceiptTimer")
    @ResponseBody
    public Result<Map<String, Object>> confirmReceiptTimer(String orderId, Integer isPlatform) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();
        //参数判断
        if (isPlatform == null) {
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(orderId)) {
            result.error500("orderId订单id不能为空！！！");
            return result;
        }
        //获取倒计时时间
        String timer = orderListService.confirmReceiptTimer(orderId, isPlatform);
        if (org.apache.commons.lang3.StringUtils.isBlank(timer)) {
            result.error500("未找到订单倒计时数据!");
            return result;
        }
        objectMap.put("timer", timer);
        result.setResult(objectMap);
        result.success("请求成功");
        return result;
    }

    /**
     * 订单交易关闭
     *
     * @param
     * @return
     */
    @PostMapping("orderOff")
    public Result<String> orderOff(@RequestBody OrderListDTO orderListDTO) {
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(orderListDTO.getCloseExplain())){
            return result.error500("请选择关闭理由");
        }
        orderListService.orderOff(orderListDTO);
        return result.success("订单交易关闭成功!");
    }
}
