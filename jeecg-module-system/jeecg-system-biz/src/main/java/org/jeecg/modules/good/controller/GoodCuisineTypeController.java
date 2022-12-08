package org.jeecg.modules.good.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.entity.GoodCuisineType;
import org.jeecg.modules.good.service.IGoodCuisineTypeService;
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
 * @Description: 菜品分类
 * @Author: jeecg-boot
 * @Date:   2022-05-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="菜品分类")
@RestController
@RequestMapping("/good/goodCuisineType")
public class GoodCuisineTypeController {
	@Autowired
	private IGoodCuisineTypeService goodCuisineTypeService;


	 /**
	  * 获取店铺菜品分类
	  *
	  * @param storeManageId
	  * @return
	  */
	 @GetMapping(value = "getGoodCuisineTypeList")
	public Result<?> getGoodCuisineTypeList(String storeManageId){
		return Result.ok(goodCuisineTypeService.list(new LambdaQueryWrapper<GoodCuisineType>()
				.eq(GoodCuisineType::getStoreManageId,storeManageId).eq(GoodCuisineType::getStatus,"1")));
	}

	 /**
	  * 修改状态
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "updateStatus")
	public Result<?> updateStatus(String id){
		GoodCuisineType goodCuisineType=goodCuisineTypeService.getById(id);
		if(goodCuisineType.getStatus().equals("0")){
			goodCuisineType.setStatus("1");
		}else{
			 goodCuisineType.setStatus("0");
		 }
		 if(goodCuisineTypeService.saveOrUpdate(goodCuisineType)){
		 	return Result.ok("状态修改成功");
		 }else{
			 return Result.ok("状态修改失败");
		 }
	}

	/**
	  * 分页列表查询
	 * @param goodCuisineType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "菜品分类-分页列表查询")
	@ApiOperation(value="菜品分类-分页列表查询", notes="菜品分类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodCuisineType>> queryPageList(GoodCuisineType goodCuisineType,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodCuisineType>> result = new Result<IPage<GoodCuisineType>>();
		QueryWrapper<GoodCuisineType> queryWrapper = QueryGenerator.initQueryWrapper(goodCuisineType, req.getParameterMap());
		Page<GoodCuisineType> page = new Page<GoodCuisineType>(pageNo, pageSize);
		IPage<GoodCuisineType> pageList = goodCuisineTypeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodCuisineType
	 * @return
	 */
	@AutoLog(value = "菜品分类-添加")
	@ApiOperation(value="菜品分类-添加", notes="菜品分类-添加")
	@PostMapping(value = "/add")
	public Result<GoodCuisineType> add(@RequestBody GoodCuisineType goodCuisineType) {
		Result<GoodCuisineType> result = new Result<GoodCuisineType>();
		try {
			goodCuisineType.setSerialNumber(OrderNoUtils.getOrderNo());
			goodCuisineTypeService.save(goodCuisineType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodCuisineType
	 * @return
	 */
	@AutoLog(value = "菜品分类-编辑")
	@ApiOperation(value="菜品分类-编辑", notes="菜品分类-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodCuisineType> edit(@RequestBody GoodCuisineType goodCuisineType) {
		Result<GoodCuisineType> result = new Result<GoodCuisineType>();
		GoodCuisineType goodCuisineTypeEntity = goodCuisineTypeService.getById(goodCuisineType.getId());
		if(goodCuisineTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodCuisineTypeService.updateById(goodCuisineType);
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
	@AutoLog(value = "菜品分类-通过id删除")
	@ApiOperation(value="菜品分类-通过id删除", notes="菜品分类-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodCuisineTypeService.removeById(id);
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
	@AutoLog(value = "菜品分类-批量删除")
	@ApiOperation(value="菜品分类-批量删除", notes="菜品分类-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodCuisineType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodCuisineType> result = new Result<GoodCuisineType>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodCuisineTypeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "菜品分类-通过id查询")
	@ApiOperation(value="菜品分类-通过id查询", notes="菜品分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodCuisineType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodCuisineType> result = new Result<GoodCuisineType>();
		GoodCuisineType goodCuisineType = goodCuisineTypeService.getById(id);
		if(goodCuisineType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodCuisineType);
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
      QueryWrapper<GoodCuisineType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodCuisineType goodCuisineType = JSON.parseObject(deString, GoodCuisineType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodCuisineType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodCuisineType> pageList = goodCuisineTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "菜品分类列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodCuisineType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("菜品分类列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodCuisineType> listGoodCuisineTypes = ExcelImportUtil.importExcel(file.getInputStream(), GoodCuisineType.class, params);
              goodCuisineTypeService.saveBatch(listGoodCuisineTypes);
              return Result.ok("文件导入成功！数据行数:" + listGoodCuisineTypes.size());
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
