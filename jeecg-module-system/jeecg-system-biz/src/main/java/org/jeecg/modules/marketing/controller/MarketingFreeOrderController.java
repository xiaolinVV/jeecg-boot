package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingFreeOrder;
import org.jeecg.modules.marketing.entity.MarketingFreeSession;
import org.jeecg.modules.marketing.service.IMarketingFreeOrderService;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionService;
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
 * @Description: 免单订单
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单订单")
@RestController
@RequestMapping("/marketing/marketingFreeOrder")
public class MarketingFreeOrderController {
	@Autowired
	private IMarketingFreeOrderService marketingFreeOrderService;

	@Autowired
	private IMarketingFreeSessionService iMarketingFreeSessionService;




	 /**
	  * 将免单的订单进行免单处理
	  *
	  * 张靠勤   2021-3-19
	  *
	  * @param marketingFreeOrderIds
	  */
	 @RequestMapping("freeChargeOrder")
	 @ResponseBody
	 public Result<?> freeChargeOrder(String marketingFreeOrderIds){

	 	//参数判断
		if(StringUtils.isBlank(marketingFreeOrderIds)){
			return Result.error("专区订单不能为空！！！");
		}

		List<String> marketingFreeOrderIdList= Arrays.asList(StringUtils.split(marketingFreeOrderIds,","));
		for(String id:marketingFreeOrderIdList){
			//处理免单流程
			String result=marketingFreeOrderService.freeChargeOrder(id);
			if(!result.equals("SUCCESS")){
				return Result.error(result);
			}
		}
	 	return Result.ok("专区订单免单成功！！！");
	 }





	 /**
	  * 场次分组订单信息
	  *
	  * 张靠勤   2021-3-18
	  *
	  * @param marketingFreeSessionId
	  * @return
	  */
	 @RequestMapping("selectMarketingFreeOrderGroupByPayTime")
	 @ResponseBody
	 public Result<Map<String,Object>> selectMarketingFreeOrderGroupByPayTime(String marketingFreeSessionId,@RequestParam(name = "isFree",required = false,defaultValue = "0") String isFree){
		 Result<Map<String,Object>> result=new Result<>();
		 //参数判断
		 if(StringUtils.isBlank(marketingFreeSessionId)){
		 	return result.error500("场次id不能为空");
		 }
		//获取场次信息
		 Map<String,Object> stringObjectMap=Maps.newHashMap();
		 MarketingFreeSession marketingFreeSession=iMarketingFreeSessionService.getById(marketingFreeSessionId);
		 stringObjectMap.put("serialNumber",marketingFreeSession.getSerialNumber());
		 stringObjectMap.put("freeDay",marketingFreeSession.getFreeDay());

		 //分组信息获取
		 Map<String,Object> paramMap=Maps.newHashMap();
		paramMap.put("marketingFreeSessionId",marketingFreeSessionId);
		paramMap.put("isFree",isFree);

		//免单活动订单
		if(isFree.equals("0")){
			List<Map<String,Object>> marketingFreeOrderMapList= Lists.newArrayList();
			//将数据存储为map格式
			Map<String,Object>  marketingFreeOrderMap=Maps.newHashMap();
			marketingFreeOrderService.selectMarketingFreeOrderGroupByPayTime(paramMap).forEach(mfo->{
				marketingFreeOrderMap.put(mfo.get("payTime").toString(),mfo);
			});
			//获取时间段内日期
			DateUtils.findDates(DateUtils.str2Date(DateUtils.formatDate(marketingFreeSession.getStartTime()),
					DateUtils.date_sdf.get()),
					DateUtils.str2Date(DateUtils.formatDate(marketingFreeSession.getEndTime()), DateUtils.date_sdf.get())).forEach(date -> {
						log.info("场次分组订单日期："+DateUtils.formatDate(date));
						if(marketingFreeOrderMap.get(DateUtils.formatDate(date))==null){
							Map<String,Object> freeOrderMap=Maps.newHashMap();
							freeOrderMap.put("payTime",DateUtils.formatDate(date));
							if(marketingFreeSession.getFreeDay()!=null&&DateUtils.formatDate(marketingFreeSession.getFreeDay()).equals(DateUtils.formatDate(date))) {
								freeOrderMap.put("isFree", "1");
							}else{
								freeOrderMap.put("isFree", "0");
							}
							freeOrderMap.put("balance",0.00);
							freeOrderMap.put("actualPayment",0.00);
							freeOrderMap.put("times",0);
							freeOrderMap.put("total",0.00);
							marketingFreeOrderMapList.add(freeOrderMap);
						}else{
							marketingFreeOrderMapList.add((Map<String,Object>)marketingFreeOrderMap.get(DateUtils.formatDate(date)));
						}
			});
			stringObjectMap.put("marketingFreeOrderGroupByPayTime",marketingFreeOrderMapList);
		}


		 if(isFree.equals("1")){
			 stringObjectMap.put("marketingFreeOrderGroupByPayTime",marketingFreeOrderService.selectMarketingFreeOrderGroupByPayTime(paramMap));
		 }
		result.setResult(stringObjectMap);


		 return result;
	 }

	 /**
	  * 根据场次id查询订单信息
	  *
	  * 张靠勤   2021-3-18
	  *
	  * @param orderNo
	  * @param phone
	  * @param createTimeStart
	  * @param createTimeEnd
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("selectMarketingFreeOrderByMarketingFreeSessionId")
	 @ResponseBody
	public Result<IPage<Map<String,Object>>> selectMarketingFreeOrderByMarketingFreeSessionId(String marketingFreeSessionId,String orderNo,String phone,String createTimeStart,String createTimeEnd,
																							  @RequestParam(name = "isFree",required = false,defaultValue = "0") String isFree,
																							  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																							   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
		Result<IPage<Map<String,Object>>> result=new Result<>();

		if(StringUtils.isBlank(marketingFreeSessionId)){
			return  result.error500("场次id不能为空！！！");
		}

		//参数设置
		Page<Map<String,Object>> page=new Page<>(pageNo,pageSize);
		Map<String,Object> paramMap= Maps.newHashMap();
		paramMap.put("orderNo",orderNo);
		paramMap.put("phone",phone);
		paramMap.put("createTimeStart",createTimeStart);
		paramMap.put("createTimeEnd",createTimeEnd);
		paramMap.put("marketingFreeSessionId",marketingFreeSessionId);
		paramMap.put("isFree",isFree);
		result.setResult(marketingFreeOrderService.selectMarketingFreeOrderByMarketingFreeSessionId(page,paramMap));
		result.success("查询免单订单成功！！！");
		return result;
	}

	
	/**
	  * 分页列表查询
	 * @param marketingFreeOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "免单订单-分页列表查询")
	@ApiOperation(value="免单订单-分页列表查询", notes="免单订单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingFreeOrder>> queryPageList(MarketingFreeOrder marketingFreeOrder,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingFreeOrder>> result = new Result<IPage<MarketingFreeOrder>>();
		QueryWrapper<MarketingFreeOrder> queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeOrder, req.getParameterMap());
		Page<MarketingFreeOrder> page = new Page<MarketingFreeOrder>(pageNo, pageSize);
		IPage<MarketingFreeOrder> pageList = marketingFreeOrderService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingFreeOrder
	 * @return
	 */
	@AutoLog(value = "免单订单-添加")
	@ApiOperation(value="免单订单-添加", notes="免单订单-添加")
	@PostMapping(value = "/add")
	public Result<MarketingFreeOrder> add(@RequestBody MarketingFreeOrder marketingFreeOrder) {
		Result<MarketingFreeOrder> result = new Result<MarketingFreeOrder>();
		try {
			marketingFreeOrderService.save(marketingFreeOrder);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingFreeOrder
	 * @return
	 */
	@AutoLog(value = "免单订单-编辑")
	@ApiOperation(value="免单订单-编辑", notes="免单订单-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingFreeOrder> edit(@RequestBody MarketingFreeOrder marketingFreeOrder) {
		Result<MarketingFreeOrder> result = new Result<MarketingFreeOrder>();
		MarketingFreeOrder marketingFreeOrderEntity = marketingFreeOrderService.getById(marketingFreeOrder.getId());
		if(marketingFreeOrderEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingFreeOrderService.updateById(marketingFreeOrder);
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
	@AutoLog(value = "免单订单-通过id删除")
	@ApiOperation(value="免单订单-通过id删除", notes="免单订单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingFreeOrderService.removeById(id);
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
	@AutoLog(value = "免单订单-批量删除")
	@ApiOperation(value="免单订单-批量删除", notes="免单订单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingFreeOrder> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingFreeOrder> result = new Result<MarketingFreeOrder>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingFreeOrderService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "免单订单-通过id查询")
	@ApiOperation(value="免单订单-通过id查询", notes="免单订单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingFreeOrder> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingFreeOrder> result = new Result<MarketingFreeOrder>();
		MarketingFreeOrder marketingFreeOrder = marketingFreeOrderService.getById(id);
		if(marketingFreeOrder==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingFreeOrder);
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
      QueryWrapper<MarketingFreeOrder> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingFreeOrder marketingFreeOrder = JSON.parseObject(deString, MarketingFreeOrder.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeOrder, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingFreeOrder> pageList = marketingFreeOrderService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "免单订单列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingFreeOrder.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("免单订单列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingFreeOrder> listMarketingFreeOrders = ExcelImportUtil.importExcel(file.getInputStream(), MarketingFreeOrder.class, params);
              marketingFreeOrderService.saveBatch(listMarketingFreeOrders);
              return Result.ok("文件导入成功！数据行数:" + listMarketingFreeOrders.size());
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

}
