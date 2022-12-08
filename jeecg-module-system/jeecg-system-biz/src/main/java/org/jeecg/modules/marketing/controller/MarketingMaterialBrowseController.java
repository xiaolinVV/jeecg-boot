package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingMaterialBrowse;
import org.jeecg.modules.marketing.service.IMarketingMaterialBrowseService;
import org.jeecg.modules.marketing.vo.MarketingMaterialBrowseVO;
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
 * @Description: 素材浏览记录
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="素材浏览记录")
@RestController
@RequestMapping("/marketingMaterialBrowse/marketingMaterialBrowse")
public class MarketingMaterialBrowseController {
	@Autowired
	private IMarketingMaterialBrowseService marketingMaterialBrowseService;
	
	/**
	  * 分页列表查询
	 * @param marketingMaterialBrowse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "素材浏览记录-分页列表查询")
	@ApiOperation(value="素材浏览记录-分页列表查询", notes="素材浏览记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingMaterialBrowse>> queryPageList(MarketingMaterialBrowse marketingMaterialBrowse,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingMaterialBrowse>> result = new Result<IPage<MarketingMaterialBrowse>>();
		QueryWrapper<MarketingMaterialBrowse> queryWrapper = QueryGenerator.initQueryWrapper(marketingMaterialBrowse, req.getParameterMap());
		Page<MarketingMaterialBrowse> page = new Page<MarketingMaterialBrowse>(pageNo, pageSize);
		IPage<MarketingMaterialBrowse> pageList = marketingMaterialBrowseService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingMaterialBrowse
	 * @return
	 */
	@AutoLog(value = "素材浏览记录-添加")
	@ApiOperation(value="素材浏览记录-添加", notes="素材浏览记录-添加")
	@PostMapping(value = "/add")
	public Result<MarketingMaterialBrowse> add(@RequestBody MarketingMaterialBrowse marketingMaterialBrowse) {
		Result<MarketingMaterialBrowse> result = new Result<MarketingMaterialBrowse>();
		try {
			marketingMaterialBrowseService.save(marketingMaterialBrowse);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingMaterialBrowse
	 * @return
	 */
	@AutoLog(value = "素材浏览记录-编辑")
	@ApiOperation(value="素材浏览记录-编辑", notes="素材浏览记录-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingMaterialBrowse> edit(@RequestBody MarketingMaterialBrowse marketingMaterialBrowse) {
		Result<MarketingMaterialBrowse> result = new Result<MarketingMaterialBrowse>();
		MarketingMaterialBrowse marketingMaterialBrowseEntity = marketingMaterialBrowseService.getById(marketingMaterialBrowse.getId());
		if(marketingMaterialBrowseEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingMaterialBrowseService.updateById(marketingMaterialBrowse);
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
	@AutoLog(value = "素材浏览记录-通过id删除")
	@ApiOperation(value="素材浏览记录-通过id删除", notes="素材浏览记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingMaterialBrowseService.removeById(id);
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
	@AutoLog(value = "素材浏览记录-批量删除")
	@ApiOperation(value="素材浏览记录-批量删除", notes="素材浏览记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingMaterialBrowse> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingMaterialBrowse> result = new Result<MarketingMaterialBrowse>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingMaterialBrowseService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "素材浏览记录-通过id查询")
	@ApiOperation(value="素材浏览记录-通过id查询", notes="素材浏览记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingMaterialBrowse> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingMaterialBrowse> result = new Result<MarketingMaterialBrowse>();
		MarketingMaterialBrowse marketingMaterialBrowse = marketingMaterialBrowseService.getById(id);
		if(marketingMaterialBrowse==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingMaterialBrowse);
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
      QueryWrapper<MarketingMaterialBrowse> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingMaterialBrowse marketingMaterialBrowse = JSON.parseObject(deString, MarketingMaterialBrowse.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingMaterialBrowse, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingMaterialBrowse> pageList = marketingMaterialBrowseService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "素材浏览记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingMaterialBrowse.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("素材浏览记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingMaterialBrowse> listMarketingMaterialBrowses = ExcelImportUtil.importExcel(file.getInputStream(), MarketingMaterialBrowse.class, params);
              marketingMaterialBrowseService.saveBatch(listMarketingMaterialBrowses);
              return Result.ok("文件导入成功！数据行数:" + listMarketingMaterialBrowses.size());
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

	 @AutoLog(value = "素材浏览记录-分页列表查询")
	 @ApiOperation(value="素材浏览记录-分页列表查询", notes="素材浏览记录-分页列表查询")
	 @GetMapping(value = "/getMarketingMaterialBrowseMap")
	 public Result<IPage<Map<String,Object>> > getMarketingMaterialBrowseMap(MarketingMaterialBrowseVO marketingMaterialBrowseVO,
																			 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																			 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																			 HttpServletRequest req){
		 Result<IPage<Map<String,Object>> > result =new Result();
		 //组织查询参数
		 Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
		 if(StringUtils.isBlank(marketingMaterialBrowseVO.getMarketingMaterialListId()) ){
			 return result.error500("素材id不能为空!");
		 }
		 IPage<Map<String,Object>>  dianzanList = marketingMaterialBrowseService.getMarketingMaterialBrowseMap(page,marketingMaterialBrowseVO);
		 result.setResult(dianzanList);
		 result.success("查询成功!");
		 return result;
	 }

}
