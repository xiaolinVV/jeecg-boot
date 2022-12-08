package org.jeecg.modules.good.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 店铺商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺商品规格")
@RestController
@RequestMapping("/goodStoreSpecification/goodStoreSpecification")
public class GoodStoreSpecificationController {
	@Autowired
	private IGoodStoreSpecificationService goodStoreSpecificationService;
	 @Autowired
	private IGoodStoreListService goodStoreListService;
	/**
	  * 分页列表查询
	 * @param goodStoreSpecification
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺商品规格-分页列表查询")
	@ApiOperation(value="店铺商品规格-分页列表查询", notes="店铺商品规格-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodStoreSpecification>> queryPageList(GoodStoreSpecification goodStoreSpecification,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodStoreSpecification>> result = new Result<IPage<GoodStoreSpecification>>();
		QueryWrapper<GoodStoreSpecification> queryWrapper = QueryGenerator.initQueryWrapper(goodStoreSpecification, req.getParameterMap());
		Page<GoodStoreSpecification> page = new Page<GoodStoreSpecification>(pageNo, pageSize);
		IPage<GoodStoreSpecification> pageList = goodStoreSpecificationService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodStoreSpecification
	 * @return
	 */
	@AutoLog(value = "店铺商品规格-添加")
	@ApiOperation(value="店铺商品规格-添加", notes="店铺商品规格-添加")
	@PostMapping(value = "/add")
	public Result<GoodStoreSpecification> add(@RequestBody GoodStoreSpecification goodStoreSpecification) {
		Result<GoodStoreSpecification> result = new Result<GoodStoreSpecification>();
		try {
			goodStoreSpecificationService.save(goodStoreSpecification);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodStoreSpecification
	 * @return
	 */
	@AutoLog(value = "店铺商品规格-编辑")
	@ApiOperation(value="店铺商品规格-编辑", notes="店铺商品规格-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodStoreSpecification> edit(@RequestBody GoodStoreSpecification goodStoreSpecification) {
		Result<GoodStoreSpecification> result = new Result<GoodStoreSpecification>();
		GoodStoreSpecification goodStoreSpecificationEntity = goodStoreSpecificationService.getById(goodStoreSpecification.getId());
		if(goodStoreSpecificationEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodStoreSpecificationService.updateById(goodStoreSpecification);
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
	@AutoLog(value = "店铺商品规格-通过id删除")
	@ApiOperation(value="店铺商品规格-通过id删除", notes="店铺商品规格-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodStoreSpecificationService.removeById(id);
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
	@AutoLog(value = "店铺商品规格-批量删除")
	@ApiOperation(value="店铺商品规格-批量删除", notes="店铺商品规格-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodStoreSpecification> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodStoreSpecification> result = new Result<GoodStoreSpecification>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodStoreSpecificationService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺商品规格-通过id查询")
	@ApiOperation(value="店铺商品规格-通过id查询", notes="店铺商品规格-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodStoreSpecification> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodStoreSpecification> result = new Result<GoodStoreSpecification>();
		GoodStoreSpecification goodStoreSpecification = goodStoreSpecificationService.getById(id);
		if(goodStoreSpecification==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodStoreSpecification);
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
      QueryWrapper<GoodStoreSpecification> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodStoreSpecification goodStoreSpecification = JSON.parseObject(deString, GoodStoreSpecification.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodStoreSpecification, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodStoreSpecification> pageList = goodStoreSpecificationService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺商品规格列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodStoreSpecification.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺商品规格列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodStoreSpecification> listGoodStoreSpecifications = ExcelImportUtil.importExcel(file.getInputStream(), GoodStoreSpecification.class, params);
              goodStoreSpecificationService.saveBatch(listGoodStoreSpecifications);
              return Result.ok("文件导入成功！数据行数:" + listGoodStoreSpecifications.size());
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
	  *修改价格获取参数列表
	  * goodList 对应商品
	  * listgoodSpecification 规格集合
	  * @param  goodListid
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 @RequestMapping(value = "/listChangePrice", method = RequestMethod.GET)
	 public Result<Map<String,Object>> listChangePrice(@RequestParam(name="goodListid",required=true) String goodListid) {
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		 GoodStoreList goodStoreList= goodStoreListService.getById(goodListid);

		 List<GoodStoreSpecification> listgoodStoreSpecification = goodStoreSpecificationService.getGoodStoreSpecificationByGoodListId(goodListid);
		 if(goodStoreList==null) {
			 result.error500("未找到对应实体");
		 }else {
			 try{
				 ArrayList<GoodStoreList> listGoodStoreList = new ArrayList<GoodStoreList>();
				 listGoodStoreList.add(goodStoreList);
				 Map<String,Object> map=new HashMap<>();
				 map.put("listGoodList",listGoodStoreList);
				 map.put("listgoodSpecification",listgoodStoreSpecification);
				 result.setResult(map);
			 }catch (Exception e){
				 result.error500("访问失败！");
			 }
		 }
		 return result;
	 }

	 /**
	  * 修改价格
	  * @param json listgoodSpecification:规格集合
	  * @return listGoodList：商品信息
	  */
	 @RequestMapping(value = "/updatePrice", method = RequestMethod.PUT)
	 public Result<String> updatePrice(@RequestBody JSONObject json) {
		 Result<String> result = new Result<String>();
		 try {
			 JSONArray listgoodStoreSpecification= json.getJSONArray("listgoodSpecification");
			 JSONArray listGoodList= json.getJSONArray("listGoodList");
			 List<GoodStoreSpecification> listgoodStoreSpecification1 = listgoodStoreSpecification.toJavaList(GoodStoreSpecification.class);
			 List<GoodStoreList> listGoodStoreList1 = listGoodList.toJavaList(GoodStoreList.class);
			 if(listgoodStoreSpecification1.size()>0){
				 goodStoreSpecificationService.updateBatchById((Collection<GoodStoreSpecification>) listgoodStoreSpecification1);
			 }
			 if(listGoodStoreList1.size()>0){
				 int result1;
				 for(GoodStoreList goodStoreList:listGoodStoreList1){
					 //最低商品价格
					 if (goodStoreList.getPrice() != null && !"".equals(goodStoreList.getPrice())) {
						 result1 = goodStoreList.getPrice().indexOf("-");
						 if (result1 != -1) {
							 String smallPrice = goodStoreList.getPrice().substring(0, goodStoreList.getPrice().indexOf("-"));
							 goodStoreList.setSmallPrice(smallPrice);
						 } else {
							 goodStoreList.setSmallPrice(goodStoreList.getPrice());
						 }
					 }
					 //最低vip价格
					 if (goodStoreList.getVipPrice() != null && !"".equals(goodStoreList.getVipPrice())) {
						 result1 = goodStoreList.getVipPrice().indexOf("-");
						 if (result1 != -1) {
							 String smallVipPrice = goodStoreList.getVipPrice().substring(0, goodStoreList.getVipPrice().indexOf("-"));
							 goodStoreList.setSmallVipPrice(smallVipPrice);
						 } else {
							 goodStoreList.setSmallVipPrice(goodStoreList.getVipPrice());
						 }
					 }
					 //最低成本价
					 if (goodStoreList.getCostPrice() != null && !"".equals(goodStoreList.getCostPrice())) {
						 result1 = goodStoreList.getCostPrice().indexOf("-");
						 if (result1 != -1) {
							 String smallCostPrice = goodStoreList.getCostPrice().substring(0, goodStoreList.getCostPrice().indexOf("-"));
							 goodStoreList.setSmallCostPrice(smallCostPrice);
						 } else {
							 goodStoreList.setSmallCostPrice(goodStoreList.getCostPrice());
						 }
					 }

				 }
				 goodStoreListService.updateBatchById((Collection<GoodStoreList>)listGoodStoreList1);
			 }

			 result.success("修改完成！");
		 }catch (Exception e){
			 log.error(e.getMessage(), e);
			 result.setSuccess(false);
			 result.setMessage("出错了: " + e.getMessage());
			 return result;
		 }

		 return result;
	 }


}
