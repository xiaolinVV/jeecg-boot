package org.jeecg.modules.order.controller;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.dto.OrderEvaluateStoreDTO;
import org.jeecg.modules.order.dto.OrderStoreListDTO;
import org.jeecg.modules.order.entity.OrderEvaluateStore;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.IOrderEvaluateStoreService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 店铺订单评论表
 * @Author: jeecg-boot
 * @Date:   2019-12-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺订单评论表")
@RestController
@RequestMapping("/orderEvaluateStore/orderEvaluateStore")
public class OrderEvaluateStoreController {
	@Autowired
	private IOrderEvaluateStoreService orderEvaluateStoreService;
	@Autowired
	private IOrderStoreListService orderStoreListService;
	@Autowired
	private IMemberListService memberListService;
	/**
	  * 分页列表查询
	 * @param orderEvaluateStore
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺订单评论表-分页列表查询")
	@ApiOperation(value="店铺订单评论表-分页列表查询", notes="店铺订单评论表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<OrderEvaluateStore>> queryPageList(OrderEvaluateStore orderEvaluateStore,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<OrderEvaluateStore>> result = new Result<IPage<OrderEvaluateStore>>();
		QueryWrapper<OrderEvaluateStore> queryWrapper = QueryGenerator.initQueryWrapper(orderEvaluateStore, req.getParameterMap());
		Page<OrderEvaluateStore> page = new Page<OrderEvaluateStore>(pageNo, pageSize);
		IPage<OrderEvaluateStore> pageList = orderEvaluateStoreService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderEvaluateStore
	 * @return
	 */
	@AutoLog(value = "店铺订单评论表-添加")
	@ApiOperation(value="店铺订单评论表-添加", notes="店铺订单评论表-添加")
	@PostMapping(value = "/add")
	public Result<OrderEvaluateStore> add(@RequestBody OrderEvaluateStore orderEvaluateStore) {
		Result<OrderEvaluateStore> result = new Result<OrderEvaluateStore>();
		try {
			orderEvaluateStoreService.save(orderEvaluateStore);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param orderEvaluateStore
	 * @return
	 */
	@AutoLog(value = "店铺订单评论表-编辑")
	@ApiOperation(value="店铺订单评论表-编辑", notes="店铺订单评论表-编辑")
	@PutMapping(value = "/edit")
	public Result<OrderEvaluateStore> edit(@RequestBody OrderEvaluateStore orderEvaluateStore) {
		Result<OrderEvaluateStore> result = new Result<OrderEvaluateStore>();
		OrderEvaluateStore orderEvaluateStoreEntity = orderEvaluateStoreService.getById(orderEvaluateStore.getId());
		if(orderEvaluateStoreEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderEvaluateStoreService.updateById(orderEvaluateStore);
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
	@AutoLog(value = "店铺订单评论表-通过id删除")
	@ApiOperation(value="店铺订单评论表-通过id删除", notes="店铺订单评论表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			orderEvaluateStoreService.removeById(id);
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
	@AutoLog(value = "店铺订单评论表-批量删除")
	@ApiOperation(value="店铺订单评论表-批量删除", notes="店铺订单评论表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<OrderEvaluateStore> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderEvaluateStore> result = new Result<OrderEvaluateStore>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderEvaluateStoreService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺订单评论表-通过id查询")
	@ApiOperation(value="店铺订单评论表-通过id查询", notes="店铺订单评论表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OrderEvaluateStore> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderEvaluateStore> result = new Result<OrderEvaluateStore>();
		OrderEvaluateStore orderEvaluateStore = orderEvaluateStoreService.getById(id);
		if(orderEvaluateStore==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderEvaluateStore);
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
      QueryWrapper<OrderEvaluateStore> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderEvaluateStore orderEvaluateStore = JSON.parseObject(deString, OrderEvaluateStore.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderEvaluateStore, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderEvaluateStore> pageList = orderEvaluateStoreService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺订单评论表列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderEvaluateStore.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺订单评论表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderEvaluateStore> listOrderEvaluateStores = ExcelImportUtil.importExcel(file.getInputStream(), OrderEvaluateStore.class, params);
              orderEvaluateStoreService.saveBatch(listOrderEvaluateStores);
              return Result.ok("文件导入成功！数据行数:" + listOrderEvaluateStores.size());
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
	  * 评论内容
	  * @param orderListId
	  * @return
	  */
	 @GetMapping(value = "/reviewInformation")
	 public Result<OrderStoreListDTO> reviewInformation(@RequestParam(name="orderListId",required=true) String orderListId){
		 Result<OrderStoreListDTO> result = new Result<OrderStoreListDTO>();

		 OrderStoreList orderStoreList =  orderStoreListService.getById(orderListId);

		 if(orderStoreList!=null){
			 MemberList memberList =memberListService.getById(orderStoreList.getMemberListId());

			 OrderStoreListDTO orderStoreListDTO =new OrderStoreListDTO();

			 BeanUtils.copyProperties(orderStoreList, orderStoreListDTO);
			 if(memberList!=null){
				 orderStoreListDTO.setNickName(memberList.getNickName());
			 }
			 List<OrderEvaluateStoreDTO>  orderEvaluateStoreDTOList=   orderEvaluateStoreService.discussList(orderStoreListDTO.getId());
			 if(orderEvaluateStoreDTOList.size()>0){
				 orderStoreListDTO.setOrderEvaluateStoreDTOList(orderEvaluateStoreDTOList) ;
				 result.setResult(orderStoreListDTO);
				 result.setSuccess(true);
			 }else {
				 result.error500("未找到对应实体");
			 }
		 }else{

			 result.error500("未找到对应实体");
		 }

		 return result;
	 }


	 /**
	  * 通过id查询修改状态:启用停用
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<OrderEvaluateStore> updateStatus(@RequestParam(name="id",required=true) String id, @RequestParam(name="status",required=true)String status, String closeExplain) {
		 Result<OrderEvaluateStore> result = new Result<OrderEvaluateStore>();

		 OrderStoreList orderStoreList = orderStoreListService.getById(id);
		 if(orderStoreList==null) {
			 result.error500("未找到对应实体");
			 return result;
		 }
		 QueryWrapper<OrderEvaluateStore> queryWrapperOrderEvaluate = new QueryWrapper();
		 queryWrapperOrderEvaluate.eq("order_store_list_id",orderStoreList.getId());
		 List<OrderEvaluateStore> orderEvaluateStoreList = orderEvaluateStoreService.list(queryWrapperOrderEvaluate);
		 if(orderEvaluateStoreList.size()==0){
			 result.error500("未找到评论信息!");
			 return result;
		 }
		 for(OrderEvaluateStore orderEvaluateStore:orderEvaluateStoreList) {
			 orderEvaluateStore.setStatus(status);
			 orderEvaluateStore.setCloseExplain(closeExplain);
			 orderEvaluateStore.setAuditTime(new Date());
			 orderEvaluateStoreService.updateById(orderEvaluateStore);
		 }
		 result.success("修改成功!");
		 return result;
	 }



 }
