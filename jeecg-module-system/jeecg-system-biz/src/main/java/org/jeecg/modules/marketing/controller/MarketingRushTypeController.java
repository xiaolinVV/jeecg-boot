package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingRushType;
import org.jeecg.modules.marketing.service.IMarketingRushTypeService;
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
 * @Description: 抢购活动-分类管理
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="抢购活动-分类管理")
@RestController
@RequestMapping("/marketingRushType/marketingRushType")
public class MarketingRushTypeController {
	@Autowired
	private IMarketingRushTypeService marketingRushTypeService;

	/**
	  * 分页列表查询
	 * @param marketingRushType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "抢购活动-分类管理-分页列表查询")
	@ApiOperation(value="抢购活动-分类管理-分页列表查询", notes="抢购活动-分类管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingRushType marketingRushType,
														  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														  HttpServletRequest req) {
		return Result.ok(marketingRushTypeService.queryPageList(new Page<Map<String,Object>>(pageNo, pageSize),
				QueryGenerator.initQueryWrapper(marketingRushType, req.getParameterMap())
                .eq("del_flag","0")
                .orderByAsc("sort")
                .orderByDesc("create_time")
        ));
	}
	
	/**
	  *   添加
	 * @param marketingRushType
	 * @return
	 */
	@AutoLog(value = "抢购活动-分类管理-添加")
	@ApiOperation(value="抢购活动-分类管理-添加", notes="抢购活动-分类管理-添加")
	@PostMapping(value = "/add")
	public Result<MarketingRushType> add(@RequestBody MarketingRushType marketingRushType) {
		Result<MarketingRushType> result = new Result<MarketingRushType>();
		try {
			if (StringUtils.isNotBlank(marketingRushType.getMarketingPrefectureTypeId())&&marketingRushType.getMarketingPrefectureTypeId().contains(",")){
				marketingRushType.setMarketingPrefectureTypeId(marketingRushType.getMarketingPrefectureTypeId().split(",")[1]);
			}
			marketingRushTypeService.save(marketingRushType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  *  编辑
	 * @param marketingRushType
	 * @return
	 */
	@AutoLog(value = "抢购活动-分类管理-编辑")
	@ApiOperation(value="抢购活动-分类管理-编辑", notes="抢购活动-分类管理-编辑")
	@PostMapping(value = "/edit")
	public Result<MarketingRushType> edit(@RequestBody MarketingRushType marketingRushType) {
		Result<MarketingRushType> result = new Result<MarketingRushType>();
		if (StringUtils.isBlank(marketingRushType.getId())){
			return result.error500("前端id未传递");
		}
		MarketingRushType marketingRushTypeEntity = marketingRushTypeService.getById(marketingRushType.getId());
		if(marketingRushTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {

			if (StringUtils.isNotBlank(marketingRushType.getMarketingPrefectureTypeId())&&marketingRushType.getMarketingPrefectureTypeId().contains(",")){
				marketingRushType.setMarketingPrefectureTypeId(marketingRushType.getMarketingPrefectureTypeId().split(",")[1]);
			}
			boolean ok = marketingRushTypeService.updateById(marketingRushType);

			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param marketingRushType
	 * @return
	 */
	@AutoLog(value = "抢购活动-分类管理-通过id删除")
	@ApiOperation(value="抢购活动-分类管理-通过id删除", notes="抢购活动-分类管理-通过id删除")
	@PostMapping(value = "/delete")
	public Result<?> delete(@RequestBody MarketingRushType marketingRushType) {
		if (StringUtils.isBlank(marketingRushType.getId())){
			return Result.error("前端id未传递!");
		}
		try {
			marketingRushTypeService.updateById(marketingRushType);
			marketingRushTypeService.removeById(marketingRushType.getId());
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
	@AutoLog(value = "抢购活动-分类管理-批量删除")
	@ApiOperation(value="抢购活动-分类管理-批量删除", notes="抢购活动-分类管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingRushType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingRushType> result = new Result<MarketingRushType>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingRushTypeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "抢购活动-分类管理-通过id查询")
	@ApiOperation(value="抢购活动-分类管理-通过id查询", notes="抢购活动-分类管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingRushType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingRushType> result = new Result<MarketingRushType>();
		MarketingRushType marketingRushType = marketingRushTypeService.getById(id);
		if(marketingRushType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingRushType);
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
      QueryWrapper<MarketingRushType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingRushType marketingRushType = JSON.parseObject(deString, MarketingRushType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingRushType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingRushType> pageList = marketingRushTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "抢购活动-分类管理列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingRushType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("抢购活动-分类管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingRushType> listMarketingRushTypes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingRushType.class, params);
              marketingRushTypeService.saveBatch(listMarketingRushTypes);
              return Result.ok("文件导入成功！数据行数:" + listMarketingRushTypes.size());
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
