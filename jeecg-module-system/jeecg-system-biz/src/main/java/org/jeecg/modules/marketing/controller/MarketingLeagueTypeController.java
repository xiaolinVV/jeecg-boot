package org.jeecg.modules.marketing.controller;

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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingLeagueType;
import org.jeecg.modules.marketing.service.IMarketingLeagueTypeService;
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
import java.util.List;
import java.util.Map;

 /**
 * @Description: 加盟专区-专区类型
 * @Author: jeecg-boot
 * @Date:   2021-12-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="加盟专区-专区类型")
@RestController
@RequestMapping("/marketing/marketingLeagueType")
public class MarketingLeagueTypeController {
	@Autowired
	private IMarketingLeagueTypeService marketingLeagueTypeService;



	 /**
	  * 获取专区分类根据名称
	  * @return
	  */
	 @GetMapping("getMarketingLeagueTypeByTypeName")
	 public Result<?> getMarketingLeagueTypeByTypeName(String typeName){
		 return Result.ok(marketingLeagueTypeService.list(new LambdaQueryWrapper<MarketingLeagueType>().like(MarketingLeagueType::getTypeName,typeName)));
	 }


	 /**
	  * 获取所有的专区分类
	  * @return
	  */
	@GetMapping("getMarketingLeagueTypeAll")
	public Result<?> getMarketingLeagueTypeAll(){
		return Result.ok(marketingLeagueTypeService.list());
	}


	 /**
	  * 状态设置
	  *
	  * @param id
	  * @return
	  */
	@GetMapping("settingStatus")
	public Result<?> settingStatus(String id,String explainStatus){
		MarketingLeagueType marketingLeagueType=marketingLeagueTypeService.getById(id);
		if(marketingLeagueType.getStatus().equals("0")){
			marketingLeagueType.setStatus("1");
		}else{
			marketingLeagueType.setStatus("0");
		}
		marketingLeagueType.setStatusExplain(explainStatus);
		marketingLeagueTypeService.saveOrUpdate(marketingLeagueType);
		return Result.ok("状态修改成功");
	}


	 /**
	  *
	  * @param id
	  * @param sort
	  * @return
	  */
	@GetMapping("settingSort")
	public Result<?> settingSort(String id, BigDecimal sort){
		MarketingLeagueType marketingLeagueType=marketingLeagueTypeService.getById(id);
		marketingLeagueType.setSort(sort);
		marketingLeagueTypeService.saveOrUpdate(marketingLeagueType);
		return Result.ok("排序修改成功");
	}

	
	/**
	  * 分页列表查询
	 * @param marketingLeagueType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "加盟专区-专区类型-分页列表查询")
	@ApiOperation(value="加盟专区-专区类型-分页列表查询", notes="加盟专区-专区类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingLeagueType>> queryPageList(MarketingLeagueType marketingLeagueType,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingLeagueType>> result = new Result<IPage<MarketingLeagueType>>();
		QueryWrapper<MarketingLeagueType> queryWrapper = QueryGenerator.initQueryWrapper(marketingLeagueType, req.getParameterMap());
		Page<MarketingLeagueType> page = new Page<MarketingLeagueType>(pageNo, pageSize);
		IPage<MarketingLeagueType> pageList = marketingLeagueTypeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingLeagueType
	 * @return
	 */
	@AutoLog(value = "加盟专区-专区类型-添加")
	@ApiOperation(value="加盟专区-专区类型-添加", notes="加盟专区-专区类型-添加")
	@PostMapping(value = "/add")
	public Result<MarketingLeagueType> add(@RequestBody MarketingLeagueType marketingLeagueType) {
		Result<MarketingLeagueType> result = new Result<MarketingLeagueType>();
		try {
			marketingLeagueTypeService.save(marketingLeagueType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingLeagueType
	 * @return
	 */
	@AutoLog(value = "加盟专区-专区类型-编辑")
	@ApiOperation(value="加盟专区-专区类型-编辑", notes="加盟专区-专区类型-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingLeagueType> edit(@RequestBody MarketingLeagueType marketingLeagueType) {
		Result<MarketingLeagueType> result = new Result<MarketingLeagueType>();
		MarketingLeagueType marketingLeagueTypeEntity = marketingLeagueTypeService.getById(marketingLeagueType.getId());
		if(marketingLeagueTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingLeagueTypeService.updateById(marketingLeagueType);
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
	@AutoLog(value = "加盟专区-专区类型-通过id删除")
	@ApiOperation(value="加盟专区-专区类型-通过id删除", notes="加盟专区-专区类型-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingLeagueTypeService.removeById(id);
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
	@AutoLog(value = "加盟专区-专区类型-批量删除")
	@ApiOperation(value="加盟专区-专区类型-批量删除", notes="加盟专区-专区类型-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingLeagueType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingLeagueType> result = new Result<MarketingLeagueType>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingLeagueTypeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "加盟专区-专区类型-通过id查询")
	@ApiOperation(value="加盟专区-专区类型-通过id查询", notes="加盟专区-专区类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingLeagueType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingLeagueType> result = new Result<MarketingLeagueType>();
		MarketingLeagueType marketingLeagueType = marketingLeagueTypeService.getById(id);
		if(marketingLeagueType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingLeagueType);
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
      QueryWrapper<MarketingLeagueType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingLeagueType marketingLeagueType = JSON.parseObject(deString, MarketingLeagueType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingLeagueType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingLeagueType> pageList = marketingLeagueTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "加盟专区-专区类型列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingLeagueType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("加盟专区-专区类型列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingLeagueType> listMarketingLeagueTypes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingLeagueType.class, params);
              marketingLeagueTypeService.saveBatch(listMarketingLeagueTypes);
              return Result.ok("文件导入成功！数据行数:" + listMarketingLeagueTypes.size());
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
