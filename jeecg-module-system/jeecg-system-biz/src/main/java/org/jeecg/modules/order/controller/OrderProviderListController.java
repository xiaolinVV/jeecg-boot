package org.jeecg.modules.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.HttpUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.order.dto.OrderProviderListDTO;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.order.vo.OrderProviderListVO;
import org.jeecg.modules.provider.dto.ProviderAddressDTO;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.service.IProviderAddressService;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "供应商订单列表")
@RestController
@RequestMapping("/orderProviderList/orderProviderList")
public class OrderProviderListController {
    @Autowired
    private IOrderProviderListService orderProviderListService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;

    @Autowired
    private IProviderAddressService providerAddressService;

    @Autowired
    private IProviderManageService iProviderManageService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;


    /**
     * 获取供应商类型
     *
     * @return
     */
    @RequestMapping("getSupplierType")
    @ResponseBody
    public Result<?> getSupplierType(){
        Map<String,Object> resultMap=Maps.newHashMap();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        for (String s : roleByUserId) {
            if ("Supplier".equals(s)){
                ProviderManage providerManage=iProviderManageService.getOne(new LambdaQueryWrapper<ProviderManage>()
                        .eq(ProviderManage::getSysUserId,userId)
                        .eq(ProviderManage::getStatus,"1")
                        .last("limit 1"));
                resultMap.put("providerType",providerManage.getProviderType());
            }else{
                resultMap.put("providerType","-1");
            }
        }
        return Result.ok(resultMap);
    }


    /**
     * 1688补单接口
     *
     * @param orderProviderGoodRecordId
     * @return
     */
    @RequestMapping("replenishment")
    @ResponseBody
    public Result<?> replenishment(String orderProviderGoodRecordId,String goodNo,String skuNo){

        if(StringUtils.isBlank(orderProviderGoodRecordId)){
            return Result.error("商品记录id不能为空");
        }
        OrderProviderGoodRecord orderProviderGoodRecord=orderProviderGoodRecordService.getById(orderProviderGoodRecordId);
        orderProviderGoodRecord.setGoodNo(goodNo);
        if(StringUtils.isNotBlank(skuNo)){
            orderProviderGoodRecord.setSkuNo(skuNo);
        }else{
            GoodSpecification goodSpecification=iGoodSpecificationService.getById(orderProviderGoodRecord.getGoodSpecificationId());
            orderProviderGoodRecord.setSkuNo(goodSpecification.getSkuNo());
        }
        orderProviderGoodRecordService.saveOrUpdate(orderProviderGoodRecord);
        return Result.ok(orderProviderListService.replenishment(orderProviderGoodRecordId));
    }


    /**
     *
     * 下单结果
     *
     * @param orderProviderListId
     * @return
     */
    @RequestMapping("getOrderProviderGoodRecordByOrderProviderListId")
    @ResponseBody
    public Result<?> getOrderProviderGoodRecordByOrderProviderListId(String orderProviderListId){
        if(StringUtils.isBlank(orderProviderListId)){
            return Result.error("供应商订单id补能为空");
        }
        List<String> orderProviderListIds= Lists.newArrayList();
        orderProviderListIds.add(orderProviderListId);
        orderProviderListService.list(new LambdaQueryWrapper<OrderProviderList>().eq(OrderProviderList::getOrderListId,orderProviderListId)).forEach(o->{
            orderProviderListIds.add(o.getId());
        });
        return Result.ok(orderProviderGoodRecordService.list(new LambdaQueryWrapper<OrderProviderGoodRecord>().in(OrderProviderGoodRecord::getOrderProviderListId,orderProviderListIds)));
    }


    /**
     * 1688下单功能模块
     *
     * @param orderProviderListIds
     * @return
     */
    @RequestMapping("placeOrder")
    @ResponseBody
    public Result<?> placeOrder(String orderProviderListIds){
        if(StringUtils.isBlank(orderProviderListIds)){
            return Result.error("订单号不能为空");
        }
        return Result.ok( orderProviderListService.placeOrder(orderProviderListIds));
    }



    /**
     * 根据订单id查询供应商信息和商品信息
     *新增：张靠勤   2021-3-18
     *
     * @param orderListId
     * @return
     */
    @RequestMapping("getOrderProviderListAndGoodListByOrderId")
    @ResponseBody
    public Result<?> getOrderProviderListAndGoodListByOrderId(String orderListId){
        Result<List<Map<String,Object>>> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(orderListId)){
            return result.error500("订单id不能为空！！！");
        }

        result.setResult(orderProviderListService.getOrderProviderListAndGoodListByOrderId(orderListId));
        result.success("查询供应商和商品信息成功！！！");
        return result;
    }


    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "供应商订单列表-分页列表查询")
    @ApiOperation(value = "供应商订单列表-分页列表查询", notes = "供应商订单列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OrderProviderListDTO>> queryPageList(OrderProviderListVO orderProviderListVO,
                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                             HttpServletRequest req) throws IllegalAccessException {
        Result<IPage<OrderProviderListDTO>> result = new Result<IPage<OrderProviderListDTO>>();
        Page<OrderProviderList> page = new Page<OrderProviderList>(pageNo, pageSize);
        String userId = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(userId)) {
            orderProviderListVO.setSysUserId(userId);
        }
        IPage<OrderProviderListDTO> pageList = orderProviderListService.queryPageList(page, orderProviderListVO, userId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 添加
     *
     * @param orderProviderList
     * @return
     */
    @AutoLog(value = "供应商订单列表-添加")
    @ApiOperation(value = "供应商订单列表-添加", notes = "供应商订单列表-添加")
    @PostMapping(value = "/add")
    public Result<OrderProviderList> add(@RequestBody OrderProviderList orderProviderList) {
        Result<OrderProviderList> result = new Result<OrderProviderList>();
        try {
            orderProviderListService.save(orderProviderList);
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
     * @param orderProviderList
     * @return
     */
    @AutoLog(value = "供应商订单列表-编辑")
    @ApiOperation(value = "供应商订单列表-编辑", notes = "供应商订单列表-编辑")
    @PutMapping(value = "/edit")
    public Result<OrderProviderList> edit(@RequestBody OrderProviderList orderProviderList) {
        Result<OrderProviderList> result = new Result<OrderProviderList>();
        OrderProviderList orderProviderListEntity = orderProviderListService.getById(orderProviderList.getId());
        if (orderProviderListEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = orderProviderListService.updateById(orderProviderList);
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
    @AutoLog(value = "供应商订单列表-通过id删除")
    @ApiOperation(value = "供应商订单列表-通过id删除", notes = "供应商订单列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            orderProviderListService.removeById(id);
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
    @AutoLog(value = "供应商订单列表-批量删除")
    @ApiOperation(value = "供应商订单列表-批量删除", notes = "供应商订单列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<OrderProviderList> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<OrderProviderList> result = new Result<OrderProviderList>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.orderProviderListService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "供应商订单列表-通过id查询")
    @ApiOperation(value = "供应商订单列表-通过id查询", notes = "供应商订单列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OrderProviderList> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OrderProviderList> result = new Result<OrderProviderList>();
        OrderProviderList orderProviderList = orderProviderListService.getById(id);
        if (orderProviderList == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(orderProviderList);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<OrderProviderList> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                OrderProviderList orderProviderList = JSON.parseObject(deString, OrderProviderList.class);
                queryWrapper = QueryGenerator.initQueryWrapper(orderProviderList, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<OrderProviderList> pageList = orderProviderListService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应商订单列表列表");
        mv.addObject(NormalExcelConstants.CLASS, OrderProviderList.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商订单列表列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
                List<OrderProviderList> listOrderProviderLists = ExcelImportUtil.importExcel(file.getInputStream(), OrderProviderList.class, params);
                orderProviderListService.saveBatch(listOrderProviderLists);
                return Result.ok("文件导入成功！数据行数:" + listOrderProviderLists.size());
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

    /**
     * 包裹信息
     *
     * @param orderListId
     * @return type 0:平台查询 1:供应商查询
     */
    @GetMapping(value = "/parcelInformation")
    public Result<List<OrderProviderListDTO>> parcelInformation(String orderListId,
                                                                @RequestParam(defaultValue = "0", name = "type") String type) {
        Result<List<OrderProviderListDTO>> result = new Result<List<OrderProviderListDTO>>();
        //判断当前用户是否是平台，是 STR=NULL 不是STR= UserId
        String UserId = PermissionUtils.ifPlatform();

        //type 0:平台查询 1:供应商查询
        List<OrderProviderListDTO> orderProviderLists;
        if (type.equals("0")) {
            //查询包裹集合，parentId ！=0 为包裹数据
            orderProviderLists = orderProviderListService.selectorderListId(orderListId, UserId, null, "0");
        } else {
            orderProviderLists = orderProviderListService.selectByParentId(orderListId);
        }


        if (orderProviderLists.size() > 0) {
            for (OrderProviderListDTO opl : orderProviderLists) {
                /* orderProviderLists.forEach(opl -> {});*/
                SysUser sysUser = sysUserService.getById(opl.getSysUserId());
                if (sysUser != null) {
                    opl.setSysUserName(sysUser.getRealname());
                }
                //获取物流数据JSON
                if (opl.getLogisticsTracking() != null) {
                    JSONObject jsonObject = JSONObject.parseObject(opl.getLogisticsTracking());
                    if (jsonObject.get("status").equals("0")) {
                        //已签收
                        JSONObject jsonObjectResult = JSONObject.parseObject(jsonObject.get("result").toString());

                        if (jsonObjectResult.get("issign")!=null&&jsonObjectResult.get("issign").equals("1")) {
                            //不做查询物流接口
                        } else {
                            //请求接口更新物流数据接口
                            String string = orderProviderListService.listSkip(opl.getId());
                            opl.setLogisticsTracking(string);
                        }
                    }
                } else {
                    //请求接口更新物流数据接口
                    String string = orderProviderListService.listSkip(opl.getId());
                    if (StringUtils.isBlank(string)) {
                        result.error500("物流信息请求出错");
                        return result;
                    }
                    opl.setLogisticsTracking(string);
                }
                //供应商发货信息
                Map<String, String> paramMap = Maps.newHashMap();
                paramMap.put("id", opl.getProviderAddressIdSender());
                if (opl.getProviderAddressIdSender() == null || "".equals(opl.getProviderAddressIdSender())) {
                    paramMap.put("sysUserId", opl.getSysUserId());
                    paramMap.put("isDeliver", "1");//发货默认
                    paramMap.put("isReturn", "");//退货
                }
                List<ProviderAddressDTO> listProviderAddressDTO = providerAddressService.getlistProviderAddress(paramMap);
                if (listProviderAddressDTO.size() > 0) {
                    opl.setProviderAddressDTOFa(listProviderAddressDTO.get(0));
                }
                //供应商退信息
                Map<String, String> paramMaptui = Maps.newHashMap();
                paramMaptui.put("id", opl.getProviderAddressIdTui());
                if (opl.getProviderAddressIdTui() == null || "".equals(opl.getProviderAddressIdTui())) {
                    paramMaptui.put("sysUserId", opl.getSysUserId());
                    paramMaptui.put("isDeliver", "");//发货默认
                    paramMaptui.put("isReturn", "1");//退货
                }
                List<ProviderAddressDTO> listProviderAddressDTOTui = providerAddressService.getlistProviderAddress(paramMaptui);
                if (listProviderAddressDTOTui.size() > 0) {
                    opl.setProviderAddressDTOTui(listProviderAddressDTOTui.get(0));
                }
                //添加商品信息
                List<OrderProviderGoodRecord> orderProviderGoodRecords = orderProviderGoodRecordService.selectOrderProviderListId(opl.getId());
                //添加供应商订单商品记录
                if (orderProviderGoodRecords.size() > 0) {
                    opl.setOrderProviderGoodRecords(orderProviderGoodRecords);
                }
            }
        }
        result.setResult(orderProviderLists);
        result.setSuccess(true);
        return result;
    }

    /**
     * 通过id查询供应商发货退货Id修改地址：
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单列表-通过id查询")
    @ApiOperation(value = "订单列表-通过id查询", notes = "订单列表-通过id查询")
    @GetMapping(value = "/updateProviderAddressId")
    public Result<OrderProviderList> updateProviderAddressId(
            @RequestParam(name = "id", required = true) String id,
            @RequestParam(name = "providerAddressId", required = true) String providerAddressId,
            @RequestParam(name = "AddressftIndex", required = true) String AddressftIndex) {

        Result<OrderProviderList> result = new Result<OrderProviderList>();
        OrderProviderList orderProviderList = orderProviderListService.getById(id);
        if (orderProviderList == null) {
            result.error500("未找到对应实体");
        } else {
            //根据Id获取修改地址数据
            if ("1".equals(AddressftIndex)) {
                orderProviderList.setProviderAddressIdSender(providerAddressId);

            } else {
                orderProviderList.setProviderAddressIdTui(providerAddressId);
            }
            boolean ok = orderProviderListService.updateById(orderProviderList);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String host = "https://wuliu.market.alicloudapi.com";
        String path = "/kdi";
        String method = "GET";
        System.out.println("请先替换成自己的AppCode");
        String appcode = "069014e20f794f90ba7bc169d14c4b1b";  // !!!替换填写自己的AppCode 在买家中心查看
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode); //格式为:Authorization:APPCODE 83359fd73fe11248385f570e3c139xxx
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("no", "YT4281779470826");// !!! 请求参数
        querys.put("type", "YTO");// !!! 请求参数
        //JDK 1.8示例代码请在这里下载：  http://code.fegine.com/Tools.zip
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 或者直接下载：
             * http://code.fegine.com/HttpUtils.zip
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             * 相关jar包（非pom）直接下载：
             * http://code.fegine.com/aliyun-jar.zip
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(response.getEntity())); //输出json
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}