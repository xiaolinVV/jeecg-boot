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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.order.dto.OrderStoreSubListDTO;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.jeecg.modules.order.service.IOrderStoreSubListService;
import org.jeecg.modules.store.dto.StoreAddressDTO;
import org.jeecg.modules.store.service.IStoreAddressService;
import org.jeecg.modules.system.entity.SysUser;
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
import java.util.List;
import java.util.Map;

 /**
 * @Description: 店铺包裹订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺包裹订单列表")
@RestController
@RequestMapping("/orderStoreSubList/orderStoreSubList")
public class OrderStoreSubListController {
	@Autowired
	private IOrderStoreSubListService orderStoreSubListService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IOrderStoreGoodRecordService orderStoreGoodRecordService;
    @Autowired
	private IStoreAddressService storeAddressService;

	 private static final String appcode = "069014e20f794f90ba7bc169d14c4b1b";
	/**
	  * 分页列表查询
	 * @param orderStoreSubList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺包裹订单列表-分页列表查询")
	@ApiOperation(value="店铺包裹订单列表-分页列表查询", notes="店铺包裹订单列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<OrderStoreSubList>> queryPageList(OrderStoreSubList orderStoreSubList,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<OrderStoreSubList>> result = new Result<IPage<OrderStoreSubList>>();
		QueryWrapper<OrderStoreSubList> queryWrapper = QueryGenerator.initQueryWrapper(orderStoreSubList, req.getParameterMap());
		Page<OrderStoreSubList> page = new Page<OrderStoreSubList>(pageNo, pageSize);
		IPage<OrderStoreSubList> pageList = orderStoreSubListService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderStoreSubList
	 * @return
	 */
	@AutoLog(value = "店铺包裹订单列表-添加")
	@ApiOperation(value="店铺包裹订单列表-添加", notes="店铺包裹订单列表-添加")
	@PostMapping(value = "/add")
	public Result<OrderStoreSubList> add(@RequestBody OrderStoreSubList orderStoreSubList) {
		Result<OrderStoreSubList> result = new Result<OrderStoreSubList>();
		try {
			orderStoreSubListService.save(orderStoreSubList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param orderStoreSubList
	 * @return
	 */
	@AutoLog(value = "店铺包裹订单列表-编辑")
	@ApiOperation(value="店铺包裹订单列表-编辑", notes="店铺包裹订单列表-编辑")
	@PutMapping(value = "/edit")
	public Result<OrderStoreSubList> edit(@RequestBody OrderStoreSubList orderStoreSubList) {
		Result<OrderStoreSubList> result = new Result<OrderStoreSubList>();
		OrderStoreSubList orderStoreSubListEntity = orderStoreSubListService.getById(orderStoreSubList.getId());
		if(orderStoreSubListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderStoreSubListService.updateById(orderStoreSubList);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺包裹订单列表-通过id删除")
	@ApiOperation(value="店铺包裹订单列表-通过id删除", notes="店铺包裹订单列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			orderStoreSubListService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "店铺包裹订单列表-批量删除")
	@ApiOperation(value="店铺包裹订单列表-批量删除", notes="店铺包裹订单列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<OrderStoreSubList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderStoreSubList> result = new Result<OrderStoreSubList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderStoreSubListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺包裹订单列表-通过id查询")
	@ApiOperation(value="店铺包裹订单列表-通过id查询", notes="店铺包裹订单列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OrderStoreSubList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderStoreSubList> result = new Result<OrderStoreSubList>();
		OrderStoreSubList orderStoreSubList = orderStoreSubListService.getById(id);
		if(orderStoreSubList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderStoreSubList);
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
      QueryWrapper<OrderStoreSubList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderStoreSubList orderStoreSubList = JSON.parseObject(deString, OrderStoreSubList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderStoreSubList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderStoreSubList> pageList = orderStoreSubListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺包裹订单列表列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderStoreSubList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺包裹订单列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderStoreSubList> listOrderStoreSubLists = ExcelImportUtil.importExcel(file.getInputStream(), OrderStoreSubList.class, params);
              orderStoreSubListService.saveBatch(listOrderStoreSubLists);
              return Result.ok("文件导入成功！数据行数:" + listOrderStoreSubLists.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
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
	  * @param orderListId
	  * @return
	  */
	 @GetMapping(value = "/parcelInformation")
	 public Result<List<OrderStoreSubListDTO>>  parcelInformation(String orderListId){
		 Result<List<OrderStoreSubListDTO>> result = new Result<List<OrderStoreSubListDTO>>();
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String UserId = PermissionUtils.ifPlatform();
		 //查询包裹集合，parentId ！=0 为包裹数据
		 List<OrderStoreSubListDTO>  orderStoreSubLists = orderStoreSubListService.selectorderStoreListId(orderListId,UserId,null,"0");
		 if(orderStoreSubLists.size()>0){
			 orderStoreSubLists.forEach(opl -> {
				 SysUser sysUser = sysUserService.getById(opl.getSysUserId());
				 if(sysUser!=null){
					 opl.setSysUserName(sysUser.getRealname());
				 }
				 //获取物流数据JSON
				 //获取物流数据JSON
				 if(opl.getLogisticsTracking()!=null){
					 JSONObject jsonObject = JSONObject.parseObject(opl.getLogisticsTracking());
					 if(jsonObject.get("status").equals("0")){
						 //已签收
						 JSONObject jsonObjectResult = JSONObject.parseObject(jsonObject.get("result").toString());

						 if(jsonObjectResult.get("issign").equals("1")){
							 //不做查询物流接口
						 }else{
							 //请求接口更新数据接口
							 String string = orderStoreSubListService.listSkip(opl.getId());
							 opl.setLogisticsTracking(string);
						 }
					 }else {
						 //请求接口更新物流数据接口
						 String string = orderStoreSubListService.listSkip(opl.getId());
						 opl.setLogisticsTracking(string);
					 }
				 }else{
					 String string = orderStoreSubListService.listSkip(opl.getId());
						 opl.setLogisticsTracking(string);
				 }
				 //供应商发货信息
				 Map<String,String> paramMap = Maps.newHashMap();
				 paramMap.put("id",opl.getStoreAddressIdSender());
				 if(opl.getStoreAddressIdSender()==null || "".equals( opl.getStoreAddressIdSender())){
					 paramMap.put("sysUserId",opl.getSysUserId());
					 paramMap.put("isDeliver","1");//发货默认
					 paramMap.put("isReturn","");//退货
				 }
				 List<StoreAddressDTO> listStoreAddressDTO= storeAddressService.getlistStoreAddress(paramMap);
				 if(listStoreAddressDTO.size()>0){
					 opl.setStoreAddressDTOFa(listStoreAddressDTO.get(0));
				 }
				 //供应商退信息
				 Map<String,String> paramMaptui = Maps.newHashMap();
				 paramMaptui.put("id",opl.getStoreAddressIdTui());
				 if(opl.getStoreAddressIdTui()== null || "".equals(opl.getStoreAddressIdTui())){
					 paramMaptui.put("sysUserId",opl.getSysUserId());
					 paramMaptui.put("isDeliver","");//发货默认
					 paramMaptui.put("isReturn","1");//退货
				 }
				 List<StoreAddressDTO> listStoreAddressDTOTui= storeAddressService.getlistStoreAddress(paramMaptui);
				 if(listStoreAddressDTOTui.size()>0){
					 opl.setStoreAddressDTOTui(listStoreAddressDTOTui.get(0));
				 }
				 //添加商品信息
				 List<OrderStoreGoodRecord>  orderStoreGoodRecords= orderStoreGoodRecordService.selectOrderStoreSubListId(opl.getId());
				 //添加供应商订单商品记录
				 if(orderStoreGoodRecords.size()>0){
					 opl.setOrderStoreGoodRecords(orderStoreGoodRecords);
				 }
			 });
		 }

		 //是否还有未发货商品
		 List<OrderStoreSubListDTO>  listOrderStoreSubList = orderStoreSubListService.selectorderStoreListId(orderListId,UserId,"0",null);
		 if(listOrderStoreSubList.size()>0){
			 for(OrderStoreSubListDTO lopl:listOrderStoreSubList){
				 SysUser sysUser = sysUserService.getById(lopl.getSysUserId());
				 if(sysUser!=null){
					 lopl.setSysUserName(sysUser.getRealname());
				 }
				 //添加商品信息
				 List<OrderStoreGoodRecord>  orderStoreGoodRecords= orderStoreGoodRecordService.selectOrderStoreSubListId(lopl.getId());
				 //添加供应商订单商品记录
				 if(orderStoreGoodRecords.size()>0){
					 lopl.setOrderStoreGoodRecords(orderStoreGoodRecords);
					 //还有未发货的商品添加一个空包裹
					 orderStoreSubLists.add(lopl);
					 break;
				 }
			 }
		 }
		 result.setResult(orderStoreSubLists);
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
	 @ApiOperation(value="订单列表-通过id查询", notes="订单列表-通过id查询")
	 @GetMapping(value = "/updateProviderAddressId")
	 public Result<OrderStoreSubList>  updateProviderAddressId(
			 @RequestParam(name = "id", required = true) String id,
			 @RequestParam(name = "providerAddressId", required = true) String providerAddressId,
			 @RequestParam(name = "AddressftIndex", required = true) String AddressftIndex) {

		 Result<OrderStoreSubList> result = new Result<OrderStoreSubList>();
		 OrderStoreSubList orderStoreSubList = orderStoreSubListService.getById(id);
		 if (orderStoreSubList == null) {
			 result.error500("未找到对应实体");
		 } else {
			 //根据Id获取修改地址数据
			 if("1".equals(AddressftIndex)){
				 orderStoreSubList.setStoreAddressIdSender(providerAddressId);

			 }else {
				 orderStoreSubList.setStoreAddressIdTui(providerAddressId);
			 }
			 boolean ok = orderStoreSubListService.updateById(orderStoreSubList);
			 //TODO 返回false说明什么？
			 if (ok) {
				 result.success("修改成功!");
			 } else {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }



 }
