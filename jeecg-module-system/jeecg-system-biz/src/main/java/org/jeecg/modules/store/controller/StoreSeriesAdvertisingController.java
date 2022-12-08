package org.jeecg.modules.store.controller;

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
import org.jeecg.modules.store.entity.StoreSeriesAdvertising;
import org.jeecg.modules.store.service.IStoreSeriesAdvertisingService;
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
 * @Description: 店铺-系列店铺-系列轮播图
 * @Author: jeecg-boot
 * @Date:   2022-07-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺-系列店铺-系列轮播图")
@RestController
@RequestMapping("/store/storeSeriesAdvertising")
public class StoreSeriesAdvertisingController {
	@Autowired
	private IStoreSeriesAdvertisingService storeSeriesAdvertisingService;


	 /**
	  * 修改状态
	  *
	  * @return
	  */
	@GetMapping("updateStatus")
	public Result<?> updateStatus(String id){
		StoreSeriesAdvertising storeSeriesAdvertising=storeSeriesAdvertisingService.getById(id);
		if(storeSeriesAdvertising.getStatus().equals("0")){
			storeSeriesAdvertising.setStatus("1");
			storeSeriesAdvertisingService.saveOrUpdate(storeSeriesAdvertising);
			return Result.ok("状态修改成功！！！");
		}
		if(storeSeriesAdvertising.getStatus().equals("1")){
			storeSeriesAdvertising.setStatus("0");
			storeSeriesAdvertisingService.saveOrUpdate(storeSeriesAdvertising);
			return Result.ok("状态修改成功！！！");
		}
		return Result.error("未知错误");
	}
	
	/**
	  * 分页列表查询
	 * @param storeSeriesAdvertising
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺-系列店铺-系列轮播图-分页列表查询")
	@ApiOperation(value="店铺-系列店铺-系列轮播图-分页列表查询", notes="店铺-系列店铺-系列轮播图-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreSeriesAdvertising>> queryPageList(StoreSeriesAdvertising storeSeriesAdvertising,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StoreSeriesAdvertising>> result = new Result<IPage<StoreSeriesAdvertising>>();
		QueryWrapper<StoreSeriesAdvertising> queryWrapper = QueryGenerator.initQueryWrapper(storeSeriesAdvertising, req.getParameterMap());
		Page<StoreSeriesAdvertising> page = new Page<StoreSeriesAdvertising>(pageNo, pageSize);
		IPage<StoreSeriesAdvertising> pageList = storeSeriesAdvertisingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeSeriesAdvertising
	 * @return
	 */
	@AutoLog(value = "店铺-系列店铺-系列轮播图-添加")
	@ApiOperation(value="店铺-系列店铺-系列轮播图-添加", notes="店铺-系列店铺-系列轮播图-添加")
	@PostMapping(value = "/add")
	public Result<StoreSeriesAdvertising> add(@RequestBody StoreSeriesAdvertising storeSeriesAdvertising) {
		Result<StoreSeriesAdvertising> result = new Result<StoreSeriesAdvertising>();
		try {
			storeSeriesAdvertisingService.save(storeSeriesAdvertising);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeSeriesAdvertising
	 * @return
	 */
	@AutoLog(value = "店铺-系列店铺-系列轮播图-编辑")
	@ApiOperation(value="店铺-系列店铺-系列轮播图-编辑", notes="店铺-系列店铺-系列轮播图-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreSeriesAdvertising> edit(@RequestBody StoreSeriesAdvertising storeSeriesAdvertising) {
		Result<StoreSeriesAdvertising> result = new Result<StoreSeriesAdvertising>();
		StoreSeriesAdvertising storeSeriesAdvertisingEntity = storeSeriesAdvertisingService.getById(storeSeriesAdvertising.getId());
		if(storeSeriesAdvertisingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeSeriesAdvertisingService.updateById(storeSeriesAdvertising);
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
	@AutoLog(value = "店铺-系列店铺-系列轮播图-通过id删除")
	@ApiOperation(value="店铺-系列店铺-系列轮播图-通过id删除", notes="店铺-系列店铺-系列轮播图-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeSeriesAdvertisingService.removeById(id);
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
	@AutoLog(value = "店铺-系列店铺-系列轮播图-批量删除")
	@ApiOperation(value="店铺-系列店铺-系列轮播图-批量删除", notes="店铺-系列店铺-系列轮播图-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreSeriesAdvertising> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreSeriesAdvertising> result = new Result<StoreSeriesAdvertising>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeSeriesAdvertisingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺-系列店铺-系列轮播图-通过id查询")
	@ApiOperation(value="店铺-系列店铺-系列轮播图-通过id查询", notes="店铺-系列店铺-系列轮播图-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreSeriesAdvertising> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreSeriesAdvertising> result = new Result<StoreSeriesAdvertising>();
		StoreSeriesAdvertising storeSeriesAdvertising = storeSeriesAdvertisingService.getById(id);
		if(storeSeriesAdvertising==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeSeriesAdvertising);
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
      QueryWrapper<StoreSeriesAdvertising> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreSeriesAdvertising storeSeriesAdvertising = JSON.parseObject(deString, StoreSeriesAdvertising.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeSeriesAdvertising, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreSeriesAdvertising> pageList = storeSeriesAdvertisingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺-系列店铺-系列轮播图列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreSeriesAdvertising.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺-系列店铺-系列轮播图列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreSeriesAdvertising> listStoreSeriesAdvertisings = ExcelImportUtil.importExcel(file.getInputStream(), StoreSeriesAdvertising.class, params);
              storeSeriesAdvertisingService.saveBatch(listStoreSeriesAdvertisings);
              return Result.ok("文件导入成功！数据行数:" + listStoreSeriesAdvertisings.size());
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
