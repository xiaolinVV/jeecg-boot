package org.jeecg.modules.marketing.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingSearchterm;
import org.jeecg.modules.marketing.service.IMarketingSearchtermService;
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
 * @Description: 推荐搜词
 * @Author: jeecg-boot
 * @Date:   2019-10-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="推荐搜词")
@RestController
@RequestMapping("/marketingSearchterm/marketingSearchterm")
public class MarketingSearchtermController {
	@Autowired
	private IMarketingSearchtermService marketingSearchtermService;
	
	/**
	  * 分页列表查询
	 * @param marketingSearchterm
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "推荐搜词-分页列表查询")
	@ApiOperation(value="推荐搜词-分页列表查询", notes="推荐搜词-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingSearchterm>> queryPageList(MarketingSearchterm marketingSearchterm,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingSearchterm>> result = new Result<IPage<MarketingSearchterm>>();
		QueryWrapper<MarketingSearchterm> queryWrapper = QueryGenerator.initQueryWrapper(marketingSearchterm, req.getParameterMap());
		Page<MarketingSearchterm> page = new Page<MarketingSearchterm>(pageNo, pageSize);
		IPage<MarketingSearchterm> pageList = marketingSearchtermService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingSearchterm
	 * @return
	 */
	@AutoLog(value = "推荐搜词-添加")
	@ApiOperation(value="推荐搜词-添加", notes="推荐搜词-添加")
	@PostMapping(value = "/add")
	public Result<MarketingSearchterm> add(@RequestBody MarketingSearchterm marketingSearchterm) {
		Result<MarketingSearchterm> result = new Result<MarketingSearchterm>();
		try {
			marketingSearchtermService.save(marketingSearchterm);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingSearchterm
	 * @return
	 */
	@AutoLog(value = "推荐搜词-编辑")
	@ApiOperation(value="推荐搜词-编辑", notes="推荐搜词-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingSearchterm> edit(@RequestBody MarketingSearchterm marketingSearchterm) {
		Result<MarketingSearchterm> result = new Result<MarketingSearchterm>();
		MarketingSearchterm marketingSearchtermEntity = marketingSearchtermService.getById(marketingSearchterm.getId());
		if(marketingSearchtermEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingSearchtermService.updateById(marketingSearchterm);
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
	@AutoLog(value = "推荐搜词-通过id删除")
	@ApiOperation(value="推荐搜词-通过id删除", notes="推荐搜词-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingSearchtermService.removeById(id);
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
	@AutoLog(value = "推荐搜词-批量删除")
	@ApiOperation(value="推荐搜词-批量删除", notes="推荐搜词-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingSearchterm> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingSearchterm> result = new Result<MarketingSearchterm>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingSearchtermService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "推荐搜词-通过id查询")
	@ApiOperation(value="推荐搜词-通过id查询", notes="推荐搜词-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingSearchterm> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingSearchterm> result = new Result<MarketingSearchterm>();
		MarketingSearchterm marketingSearchterm = marketingSearchtermService.getById(id);
		if(marketingSearchterm==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingSearchterm);
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
      QueryWrapper<MarketingSearchterm> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingSearchterm marketingSearchterm = JSON.parseObject(deString, MarketingSearchterm.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingSearchterm, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingSearchterm> pageList = marketingSearchtermService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "推荐搜词列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingSearchterm.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("推荐搜词列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingSearchterm> listMarketingSearchterms = ExcelImportUtil.importExcel(file.getInputStream(), MarketingSearchterm.class, params);
              marketingSearchtermService.saveBatch(listMarketingSearchterms);
              return Result.ok("文件导入成功！数据行数:" + listMarketingSearchterms.size());
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


	 @GetMapping(value = "/updateStatus")
	 public Result<MarketingSearchterm> updateStatus(@RequestParam(name="id",required=true) String id) {
		 Result<MarketingSearchterm> result = new Result<MarketingSearchterm>();
		 MarketingSearchterm marketingSearchterm = marketingSearchtermService.getById(id);
		 if(marketingSearchterm==null) {
			 result.error500("未找到对应实体");
		 }else {
			 if("1".equals(marketingSearchterm.getStatus())){
				 marketingSearchterm.setStatus("0");
			 }else{
				 marketingSearchterm.setStatus("1");
			 }

			 boolean ok = marketingSearchtermService.updateById(marketingSearchterm);
			 //TODO 返回false说明什么？
			 if(ok) {
				 result.success("修改成功!");
			 }
		 }
		 return result;
	 }

	 @GetMapping(value = "/getmarketingSearchtermList")
public Result<?> getmarketingSearchtermList() {
		List<MarketingSearchterm>  listMarketingSearchterm=marketingSearchtermService.getmarketingSearchtermList();
		 Map<String,Object > map=new HashMap<>();
		 map.put("listMarketingSearchterm",listMarketingSearchterm);

		 return Result.ok(map);
	 }
}
