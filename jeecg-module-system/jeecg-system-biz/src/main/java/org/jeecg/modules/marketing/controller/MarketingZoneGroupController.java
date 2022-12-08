package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingZoneGroup;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupService;
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
 * @Description: 拼团专区
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼团专区")
@RestController
@RequestMapping("/marketingZoneGroup/marketingZoneGroup")
public class MarketingZoneGroupController {
	@Autowired
	private IMarketingZoneGroupService marketingZoneGroupService;
	
	/**
	  * 分页列表查询
	 * @param marketingZoneGroup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "拼团专区-分页列表查询")
	@ApiOperation(value="拼团专区-分页列表查询", notes="拼团专区-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingZoneGroup>> queryPageList(MarketingZoneGroup marketingZoneGroup,
														   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														   HttpServletRequest req) {
		Result<IPage<MarketingZoneGroup>> result = new Result<IPage<MarketingZoneGroup>>();
		QueryWrapper<MarketingZoneGroup> queryWrapper = QueryGenerator.initQueryWrapper(marketingZoneGroup, req.getParameterMap());
		Page<MarketingZoneGroup> page = new Page<MarketingZoneGroup>(pageNo, pageSize);
		queryWrapper.orderByDesc("create_time");
		IPage<MarketingZoneGroup> pageList = marketingZoneGroupService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingZoneGroup
	 * @return
	 */
	@AutoLog(value = "拼团专区-添加")
	@ApiOperation(value="拼团专区-添加", notes="拼团专区-添加")
	@PostMapping(value = "/add")
	public Result<MarketingZoneGroup> add(@RequestBody MarketingZoneGroup marketingZoneGroup) {
		Result<MarketingZoneGroup> result = new Result<MarketingZoneGroup>();
		try {
			marketingZoneGroupService.save(marketingZoneGroup);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingZoneGroup
	 * @return
	 */
	@AutoLog(value = "拼团专区-编辑")
	@ApiOperation(value="拼团专区-编辑", notes="拼团专区-编辑")
	@PostMapping(value = "/edit")
	public Result<MarketingZoneGroup> edit(@RequestBody MarketingZoneGroup marketingZoneGroup) {
		Result<MarketingZoneGroup> result = new Result<MarketingZoneGroup>();
		if (StringUtils.isBlank(marketingZoneGroup.getId())){
			return result.error500("前端id未传递");
		}
		MarketingZoneGroup marketingZoneGroupEntity = marketingZoneGroupService.getById(marketingZoneGroup.getId());
		if(marketingZoneGroupEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingZoneGroupService.updateById(marketingZoneGroup);
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param marketingZoneGroup
	 * @return
	 */
	@AutoLog(value = "拼团专区-通过id删除")
	@ApiOperation(value="拼团专区-通过id删除", notes="拼团专区-通过id删除")
	@PostMapping(value = "/delete")
	public Result<?> delete(@RequestBody MarketingZoneGroup marketingZoneGroup) {
		if (StringUtils.isBlank(marketingZoneGroup.getId())){
			return Result.error("前端id未传递!");
		}
		try {
			marketingZoneGroupService.updateById(marketingZoneGroup);
			marketingZoneGroupService.removeById(marketingZoneGroup.getId());
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
	@AutoLog(value = "拼团专区-批量删除")
	@ApiOperation(value="拼团专区-批量删除", notes="拼团专区-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingZoneGroup> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingZoneGroup> result = new Result<MarketingZoneGroup>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingZoneGroupService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拼团专区-通过id查询")
	@ApiOperation(value="拼团专区-通过id查询", notes="拼团专区-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingZoneGroup> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingZoneGroup> result = new Result<MarketingZoneGroup>();
		MarketingZoneGroup marketingZoneGroup = marketingZoneGroupService.getById(id);
		if(marketingZoneGroup==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingZoneGroup);
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
      QueryWrapper<MarketingZoneGroup> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingZoneGroup marketingZoneGroup = JSON.parseObject(deString, MarketingZoneGroup.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingZoneGroup, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingZoneGroup> pageList = marketingZoneGroupService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "拼团专区列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingZoneGroup.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拼团专区列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingZoneGroup> listMarketingZoneGroups = ExcelImportUtil.importExcel(file.getInputStream(), MarketingZoneGroup.class, params);
              marketingZoneGroupService.saveBatch(listMarketingZoneGroups);
              return Result.ok("文件导入成功！数据行数:" + listMarketingZoneGroups.size());
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
