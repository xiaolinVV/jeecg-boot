package org.jeecg.modules.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.order.dto.OrderStoreListDTO;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.order.service.IOrderStoreSubListService;
import org.jeecg.modules.order.vo.OrderStoreListVO;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 商品订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="商品订单列表")
@RestController
@RequestMapping("/orderStoreList/orderStoreList")
public class OrderStoreListController {
	@Autowired
	private IOrderStoreListService orderStoreListService;
	@Autowired
	private IMemberShippingAddressService memberShippingAddressService;
	@Autowired
	private IOrderStoreSubListService orderStoreSubListService;
	@Autowired
	private IOrderStoreGoodRecordService orderStoreGoodRecordService;
	/**
	  * 分页列表查询
	 * @param orderListVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "商品订单列表-分页列表查询")
	@ApiOperation(value="商品订单列表-分页列表查询", notes="商品订单列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<OrderStoreListDTO>> queryPageList(OrderStoreListVO orderListVO,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		Result<IPage<OrderStoreListDTO>> result = new Result<IPage<OrderStoreListDTO>>();
		Page<OrderStoreList> page = new Page<OrderStoreList>(pageNo, pageSize);
		//判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		String UserId = PermissionUtils.ifPlatform();
		if(UserId!=null){
			orderListVO.setSysUserId(UserId);
		}
		IPage<OrderStoreListDTO> pageList = orderStoreListService.getOrderStoreListDto(page, orderListVO,UserId);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderStoreList
	 * @return
	 */
	@AutoLog(value = "商品订单列表-添加")
	@ApiOperation(value="商品订单列表-添加", notes="商品订单列表-添加")
	@PostMapping(value = "/add")
	public Result<OrderStoreList> add(@RequestBody OrderStoreList orderStoreList) {
		Result<OrderStoreList> result = new Result<OrderStoreList>();
		try {
			orderStoreListService.save(orderStoreList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param orderStoreList
	 * @return
	 */
	@AutoLog(value = "商品订单列表-编辑")
	@ApiOperation(value="商品订单列表-编辑", notes="商品订单列表-编辑")
	@PutMapping(value = "/edit")
	public Result<OrderStoreList> edit(@RequestBody OrderStoreList orderStoreList) {
		Result<OrderStoreList> result = new Result<OrderStoreList>();
		OrderStoreList orderStoreListEntity = orderStoreListService.getById(orderStoreList.getId());
		if(orderStoreListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderStoreListService.updateById(orderStoreList);
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
	@AutoLog(value = "商品订单列表-通过id删除")
	@ApiOperation(value="商品订单列表-通过id删除", notes="商品订单列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			orderStoreListService.removeById(id);
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
	@AutoLog(value = "商品订单列表-批量删除")
	@ApiOperation(value="商品订单列表-批量删除", notes="商品订单列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<OrderStoreList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderStoreList> result = new Result<OrderStoreList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderStoreListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品订单列表-通过id查询")
	@ApiOperation(value="商品订单列表-通过id查询", notes="商品订单列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OrderStoreList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderStoreList> result = new Result<OrderStoreList>();
		OrderStoreList orderStoreList = orderStoreListService.getById(id);
		if(orderStoreList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderStoreList);
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
      QueryWrapper<OrderStoreList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderStoreList orderStoreList = JSON.parseObject(deString, OrderStoreList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderStoreList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderStoreList> pageList = orderStoreListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "商品订单列表列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderStoreList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品订单列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderStoreList> listOrderStoreLists = ExcelImportUtil.importExcel(file.getInputStream(), OrderStoreList.class, params);
              orderStoreListService.saveBatch(listOrderStoreLists);
              return Result.ok("文件导入成功！数据行数:" + listOrderStoreLists.size());
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
/***********************************不同状态的订单列表*****************************************************/
	 /**
	  * 取消订单分页列表查询
	  * @param orderStoreListVO
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "取消订单列表-分页列表查询")
	 @ApiOperation(value="取消订单列表-分页列表查询", notes="订单列表-分页列表查询")
	 @GetMapping(value = "/queryPageListCancel")
	 public Result<IPage<OrderStoreListDTO>> queryPageListCancel(OrderStoreListVO orderStoreListVO,
															@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															HttpServletRequest req) {
		 Result<IPage<OrderStoreListDTO>> result = new Result<IPage<OrderStoreListDTO>>();
		 Page<OrderStoreList> page = new Page<OrderStoreList>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String UserId = PermissionUtils.ifPlatform();
		 if(UserId!=null){
			 orderStoreListVO.setSysUserId(UserId);
		 }
		 orderStoreListVO.setStatus("4");
		 IPage<OrderStoreListDTO> pageList = orderStoreListService.getOrderStoreListDto(page, orderStoreListVO,UserId);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	 /**
	  * 待付款订单分页列表查询
	  * @param orderStoreListVO
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "待付款订单列表-分页列表查询")
	 @ApiOperation(value="待付款订单列表-分页列表查询", notes="订单列表-分页列表查询")
	 @GetMapping(value = "/queryPageListObligation")
	 public Result<IPage<OrderStoreListDTO>> queryPageListObligation(OrderStoreListVO orderStoreListVO,
																 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																 HttpServletRequest req) {
		 Result<IPage<OrderStoreListDTO>> result = new Result<IPage<OrderStoreListDTO>>();
		 Page<OrderStoreList> page = new Page<OrderStoreList>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String UserId = PermissionUtils.ifPlatform();
		 if(UserId!=null){
			 orderStoreListVO.setSysUserId(UserId);
		 }
		 orderStoreListVO.setStatus("0");
		 IPage<OrderStoreListDTO> pageList = orderStoreListService.getOrderStoreListDto(page, orderStoreListVO,UserId);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	 /**
	  * 待发货订单分页列表查询
	  * @param orderStoreListVO
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "待发货订单列表-分页列表查询")
	 @ApiOperation(value="待发货订单列表-分页列表查询", notes="订单列表-分页列表查询")
	 @GetMapping(value = "/queryPageListToSendTheGoods")
	 public Result<IPage<OrderStoreListDTO>> queryPageListToSendTheGoods(OrderStoreListVO orderStoreListVO,
																	 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																	 HttpServletRequest req) {
		 Result<IPage<OrderStoreListDTO>> result = new Result<IPage<OrderStoreListDTO>>();
		 Page<OrderStoreList> page = new Page<OrderStoreList>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String UserId = PermissionUtils.ifPlatform();
		 if(UserId!=null){
			 orderStoreListVO.setSysUserId(UserId);
		 }
		 orderStoreListVO.setStatus("1");
		 IPage<OrderStoreListDTO> pageList = orderStoreListService.getOrderStoreListDto(page, orderStoreListVO,UserId);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	 /**
	  * 待收货订单分页列表查询
	  * @param orderStoreListVO
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "待收货订单列表-分页列表查询")
	 @ApiOperation(value="待收货订单列表-分页列表查询", notes="订单列表-分页列表查询")
	 @GetMapping(value = "/queryPageListWaitForReceiving")
	 public Result<IPage<OrderStoreListDTO>> queryPageListWaitForReceiving(OrderStoreListVO orderStoreListVO,
																		 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																		 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																		 HttpServletRequest req) {
		 Result<IPage<OrderStoreListDTO>> result = new Result<IPage<OrderStoreListDTO>>();
		 Page<OrderStoreList> page = new Page<OrderStoreList>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String UserId = PermissionUtils.ifPlatform();
		 if(UserId!=null){
			 orderStoreListVO.setSysUserId(UserId);
		 }
		 orderStoreListVO.setStatus("2");
		 IPage<OrderStoreListDTO> pageList = orderStoreListService.getOrderListDtoWaitForReceiving(page, orderStoreListVO,UserId);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	 /**
	  * 交易成功订单分页列表查询
	  * @param orderStoreListVO
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "交易成功订单列表-分页列表查询")
	 @ApiOperation(value="交易成功订单列表-分页列表查询", notes="订单列表-分页列表查询")
	 @GetMapping(value = "/queryPageListDealsAreDone")
	 public Result<IPage<OrderStoreListDTO>> queryPageListDealsAreDone(OrderStoreListVO orderStoreListVO,
																		   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																		   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																		   HttpServletRequest req) {
		 Result<IPage<OrderStoreListDTO>> result = new Result<IPage<OrderStoreListDTO>>();
		 Page<OrderStoreList> page = new Page<OrderStoreList>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String UserId = PermissionUtils.ifPlatform();
		 if(UserId!=null){
			 orderStoreListVO.setSysUserId(UserId);
		 }
		 orderStoreListVO.setStatus("3");
		 IPage<OrderStoreListDTO> pageList = orderStoreListService.getOrderListDtoWaitForReceiving(page, orderStoreListVO,UserId);
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
	 @ApiOperation(value="订单列表-通过id查询", notes="订单列表-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<OrderStoreList> updateStatus(
			 @RequestParam(name = "id", required = true) String id,
			 @RequestParam(name = "closeExplain", required = true) String closeExplain,
			 String status,String closeType) {
		 Result<OrderStoreList> result = new Result<OrderStoreList>();
		 OrderStoreList orderList = orderStoreListService.getById(id);
		 if (orderList == null) {
			 result.error500("未找到对应实体");
		 } else {
			 if(status!=null){
				 orderList.setStatus(status);
			 }

			 if(status.equals("4")){
				 orderStoreListService.abrogateOrder(id,closeExplain, closeType);
				 result.success("修改成功!");
			 }else{
				 if(closeType!=null){
					 orderList.setCloseType(closeType);
				 }
				 orderList.setCloseExplain(closeExplain);
				 boolean ok = orderStoreListService.updateById(orderList);
				 //TODO 返回false说明什么？
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
	 @ApiOperation(value="订单列表-通过id查询", notes="订单列表-通过id查询")
	 @GetMapping(value = "/updateAddress")
	 public Result<OrderStoreList> updateAddress(
			 @RequestParam(name = "id", required = true) String id,
			 @RequestParam(name = "areaAddressId", required = true) String areaAddressId) {
		 Result<OrderStoreList> result = new Result<OrderStoreList>();
		 OrderStoreList orderList = orderStoreListService.getById(id);
		 if (orderList == null) {
			 result.error500("未找到对应实体");
		 } else {
			 //根据Id获取修改地址数据
			 MemberShippingAddress memberShippingAddress = memberShippingAddressService.getById(areaAddressId) ;
			 orderList.setShippingAddress(memberShippingAddress.getAreaAddress());
			 orderList.setConsignee(memberShippingAddress.getLinkman());
			 orderList.setContactNumber(memberShippingAddress.getPhone());
			 orderList.setHouseNumber(memberShippingAddress.getHouseNumber());
			 orderList.setIsUpdateAddr("1");
			 boolean ok = orderStoreListService.updateById(orderList);
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
	  * @param listMap
	  * @return
	  */
	 @PostMapping(value = "/ordereDlivery")
	 public Result<String> ordereDlivery( @RequestBody String listMap){
		 Result<String> result = new Result<String>();
		 try{
			 JSONObject jsonObject = JSONObject.parseObject(new String(listMap));
			 List<Map<String,Object>> listObjectSec	= jsonObject.getJSONArray("listMap").toJavaObject(List.class);
			 OrderStoreSubList orderStoreSubList = new OrderStoreSubList();
			 OrderStoreGoodRecord orderStoreGoodRecord;
			 //OrderStoreSubList orderStoreSubList;
			 List<Map<String,Object>> orderProviderGoodRecordInfoList;
			 String addorderProviderId;
			 if(listObjectSec.size()>0){
				 for(Map<String,Object> map:listObjectSec){
					 System.out.println("map++++"+map);
					 //包裹数据
					 List<Map<String,Object>> listParcelMapSS = (List<Map<String,Object>>)(List)map.get("listParcel");
					 for(Map<String,Object> listParcelMap:listParcelMapSS){
						 System.out.println("listParcelMap++++"+listParcelMap);
						 //包裹内的商品修改供应商订单id
						 orderProviderGoodRecordInfoList = (List<Map<String,Object>>)(List)listParcelMap.get("orderProviderGoodRecordInfo");
						 System.out.println("orderProviderGoodRecordInfoList++++"+orderProviderGoodRecordInfoList);
						 if(orderProviderGoodRecordInfoList.size()>0){
							 //查询供应商订单信息
							 orderStoreSubList =  orderStoreSubListService.getById(listParcelMap.get("id").toString());
							 //添加包裹,并返回新增ID
							 addorderProviderId = addorderStoreSubList(orderStoreSubList,listParcelMap.get("logisticsCompany").toString(),listParcelMap.get("trackingNumber").toString());

							 for(Map<String,Object> orderProviderGoodRecordId:orderProviderGoodRecordInfoList){
								 //订单商品信息
								 orderStoreGoodRecord = orderStoreGoodRecordService.getById(orderProviderGoodRecordId.get("id").toString());
								 if(orderStoreGoodRecord!=null){
									 //修改商品的OrderProviderListId为包裹的已发货包裹
									 orderStoreGoodRecord.setOrderStoreSubListId(addorderProviderId);
									 orderStoreGoodRecordService.updateById(orderStoreGoodRecord);
								 }
							 }
						 }
					 }

				 }
				 //调用方法
				 //是否全部发货,修改orderList的状态内容
				 ShipmentOrderModification(orderStoreSubList);
				 result.success("添加成功!");
			 }
		 }catch (Exception e){
			 e.printStackTrace();
			 result.error500("添加失败！");
		 }
		 return result;
	 }

	 /**
	  * 添加已发货的供应商包裹信息
	  * @param orderStoreSubList
	  * @param logisticsCompany 物流公司
	  * @param trackingNumber 快递单号
	  */
	 public String addorderStoreSubList(OrderStoreSubList orderStoreSubList,String logisticsCompany,String trackingNumber){
		 try{
			 OrderStoreSubList opl= new OrderStoreSubList();
			 // opl.setDelFlag(orderStoreSubList.getDelFlag());
			 //opl = orderStoreSubList;
			 opl.setDelFlag(orderStoreSubList.getDelFlag());
			 opl.setMemberListId(orderStoreSubList.getMemberListId());
			 opl.setOrderStoreListId(orderStoreSubList.getOrderStoreListId());
			 opl.setSysUserId(orderStoreSubList.getSysUserId());
			 opl.setOrderNo(orderStoreSubList.getOrderNo());
			 opl.setDistribution(orderStoreSubList.getDistribution());
			 opl.setStoreTemplateId(orderStoreSubList.getStoreTemplateId());
			 opl.setTemplateName(orderStoreSubList.getTemplateName());
			 opl.setAccountingRules(orderStoreSubList.getAccountingRules());
			 opl.setChargeMode(orderStoreSubList.getChargeMode());
			 opl.setAmount(orderStoreSubList.getAmount());
			 opl.setShipFee(orderStoreSubList.getShipFee());
			 opl.setStoreAddressIdSender(orderStoreSubList.getStoreAddressIdSender());
			 opl.setStoreAddressIdTui(orderStoreSubList.getStoreAddressIdTui());
			 opl.setLogisticsTracking(orderStoreSubList.getLogisticsTracking());
			 opl.setGoodsTotal(orderStoreSubList.getGoodsTotal());
			 //opl.setGoodsTotalCost(orderStoreSubList.getGoodsTotalCost());
			 opl.setCustomaryDues(orderStoreSubList.getCustomaryDues());
			 opl.setActualPayment(orderStoreSubList.getActualPayment());
			 //修改数据
			 opl.setParentId(orderStoreSubList.getId());
			 opl.setLogisticsCompany(logisticsCompany);
			 opl.setTrackingNumber(trackingNumber);
			 opl.setStatus("2");
			 orderStoreSubListService.save(opl);
			 String id = opl.getId();
			 return id;
		 }catch (Exception e){
			 e.printStackTrace();
		 }
		 return null;
	 }

	 /**
	  * //是否全部发货,修改orderList的状态内容
	  * @param orderStoreSubList
	  * @return
	  */
	 public  void ShipmentOrderModification(OrderStoreSubList orderStoreSubList){
		 if(orderStoreSubList!=null){
			 List<OrderStoreGoodRecord>  listOrderStoreGoodRecord;
			 Boolean bl = false;
			 OrderStoreList orderStoreList  = orderStoreListService.getById(orderStoreSubList.getOrderStoreListId());
			 QueryWrapper<OrderStoreSubList> queryWrapper = new QueryWrapper<OrderStoreSubList>();
			 QueryWrapper<OrderStoreGoodRecord> queryWrapperOPGR = new QueryWrapper<OrderStoreGoodRecord>();
			 if(orderStoreList!=null){
				 queryWrapper.eq("order_store_list_id",orderStoreList.getId());
				 //queryWrapper.ne("parent_id","0");
				 //判断是否有发货
				 List<OrderStoreSubList> listOrderStoreSubList = orderStoreSubListService.list(queryWrapper);
				 if(listOrderStoreSubList.size()>0){
					 for(OrderStoreSubList oplfh:listOrderStoreSubList){

						 if(!"0".equals(oplfh.getParentId() )){
							 //部分发货
							 orderStoreList.setIsSender("1");
						 }else{
							 //是否全部发货
							 queryWrapperOPGR = new QueryWrapper<OrderStoreGoodRecord>();
							 queryWrapperOPGR.eq("order_store_sub_list_id",oplfh.getId());
							 listOrderStoreGoodRecord = orderStoreGoodRecordService.list(queryWrapperOPGR);
							 if(listOrderStoreGoodRecord.size()>0){
								 //说明还没为发货商品
								 bl = true;
							 }
						 }
					 }
					 //bl = false 为已全部发货，改变状态为待收货
					 if(!bl){
						 orderStoreList.setStatus("2");
					 }
					 if(orderStoreList.getShipmentsTime() ==null || "".equals(orderStoreList.getShipmentsTime())){
						 //第一次发货时间
						 orderStoreList.setShipmentsTime(new Date());
					 }
					 //子订单数量
					 orderStoreList.setChildOrder(new BigDecimal(listOrderStoreSubList.size()));
					 //修改订单信息
					 orderStoreListService.updateById(orderStoreList) ;
				 }
			 }
		 }
	 };

 }
