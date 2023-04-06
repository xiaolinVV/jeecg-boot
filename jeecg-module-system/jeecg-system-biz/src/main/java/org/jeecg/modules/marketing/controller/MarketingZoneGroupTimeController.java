package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.MapHandleUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupTime;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupTimeService;
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
 * @Description: 拼团次数
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼团次数")
@RestController
@RequestMapping("/marketingZoneGroupTime/marketingZoneGroupTime")
public class MarketingZoneGroupTimeController {
	@Autowired
	private IMarketingZoneGroupTimeService marketingZoneGroupTimeService;
	
	/**
	  * 分页列表查询
	 * @param marketingZoneGroupTime
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "拼团次数-分页列表查询")
	@ApiOperation(value="拼团次数-分页列表查询", notes="拼团次数-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingZoneGroupTime marketingZoneGroupTime,
															   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															   HttpServletRequest req) {
		return Result.ok(marketingZoneGroupTimeService.queryPageList(new Page<>(pageNo, pageSize),
				QueryGenerator.initQueryWrapper(marketingZoneGroupTime, req.getParameterMap()),
				MapHandleUtils.handleRequestMap(req.getParameterMap())
		));
	}
	
	/**
	  *   添加
	 * @param marketingZoneGroupTime
	 * @return
	 */
	@AutoLog(value = "拼团次数-添加")
	@ApiOperation(value="拼团次数-添加", notes="拼团次数-添加")
	@PostMapping(value = "/add")
	public Result<MarketingZoneGroupTime> add(@RequestBody MarketingZoneGroupTime marketingZoneGroupTime) {
		Result<MarketingZoneGroupTime> result = new Result<MarketingZoneGroupTime>();
		try {
			marketingZoneGroupTimeService.save(marketingZoneGroupTime);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingZoneGroupTime
	 * @return
	 */
	@AutoLog(value = "拼团次数-编辑")
	@ApiOperation(value="拼团次数-编辑", notes="拼团次数-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingZoneGroupTime> edit(@RequestBody MarketingZoneGroupTime marketingZoneGroupTime) {
		Result<MarketingZoneGroupTime> result = new Result<MarketingZoneGroupTime>();
		MarketingZoneGroupTime marketingZoneGroupTimeEntity = marketingZoneGroupTimeService.getById(marketingZoneGroupTime.getId());
		if(marketingZoneGroupTimeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingZoneGroupTimeService.updateById(marketingZoneGroupTime);
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
	@AutoLog(value = "拼团次数-通过id删除")
	@ApiOperation(value="拼团次数-通过id删除", notes="拼团次数-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingZoneGroupTimeService.removeById(id);
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
	@AutoLog(value = "拼团次数-批量删除")
	@ApiOperation(value="拼团次数-批量删除", notes="拼团次数-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingZoneGroupTime> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingZoneGroupTime> result = new Result<MarketingZoneGroupTime>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingZoneGroupTimeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拼团次数-通过id查询")
	@ApiOperation(value="拼团次数-通过id查询", notes="拼团次数-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingZoneGroupTime> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingZoneGroupTime> result = new Result<MarketingZoneGroupTime>();
		MarketingZoneGroupTime marketingZoneGroupTime = marketingZoneGroupTimeService.getById(id);
		if(marketingZoneGroupTime==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingZoneGroupTime);
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
      QueryWrapper<MarketingZoneGroupTime> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingZoneGroupTime marketingZoneGroupTime = JSON.parseObject(deString, MarketingZoneGroupTime.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingZoneGroupTime, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingZoneGroupTime> pageList = marketingZoneGroupTimeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "拼团次数列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingZoneGroupTime.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拼团次数列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingZoneGroupTime> listMarketingZoneGroupTimes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingZoneGroupTime.class, params);
              marketingZoneGroupTimeService.saveBatch(listMarketingZoneGroupTimes);
              return Result.ok("文件导入成功！数据行数:" + listMarketingZoneGroupTimes.size());
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