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
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingIntegralTask;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskService;
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
 * @Description: 积分任务
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="积分任务")
@RestController
@RequestMapping("/marketing/marketingIntegralTask")
public class MarketingIntegralTaskController {
	@Autowired
	private IMarketingIntegralTaskService marketingIntegralTaskService;
	
	/**
	  * 分页列表查询
	 * @param marketingIntegralTask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "积分任务-分页列表查询")
	@ApiOperation(value="积分任务-分页列表查询", notes="积分任务-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingIntegralTask>> queryPageList(MarketingIntegralTask marketingIntegralTask,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingIntegralTask>> result = new Result<IPage<MarketingIntegralTask>>();
		QueryWrapper<MarketingIntegralTask> queryWrapper = QueryGenerator.initQueryWrapper(marketingIntegralTask, req.getParameterMap());
		Page<MarketingIntegralTask> page = new Page<MarketingIntegralTask>(pageNo, pageSize);
		IPage<MarketingIntegralTask> pageList = marketingIntegralTaskService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingIntegralTask
	 * @return
	 */
	@AutoLog(value = "积分任务-添加")
	@ApiOperation(value="积分任务-添加", notes="积分任务-添加")
	@PostMapping(value = "/add")
	public Result<MarketingIntegralTask> add(@RequestBody MarketingIntegralTask marketingIntegralTask) {
		Result<MarketingIntegralTask> result = new Result<MarketingIntegralTask>();

		try {
			marketingIntegralTaskService.save(marketingIntegralTask
					.setSerialNumber(OrderNoUtils.getOrderNo()));
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingIntegralTask
	 * @return
	 */
	@AutoLog(value = "积分任务-编辑")
	@ApiOperation(value="积分任务-编辑", notes="积分任务-编辑")
	@PostMapping(value = "/edit")
	public Result<MarketingIntegralTask> edit(@RequestBody MarketingIntegralTask marketingIntegralTask) {
		Result<MarketingIntegralTask> result = new Result<MarketingIntegralTask>();
		if (StringUtils.isBlank(marketingIntegralTask.getId())){
			return result.error500("前端id未传递!");
		}
		MarketingIntegralTask marketingIntegralTaskEntity = marketingIntegralTaskService.getById(marketingIntegralTask.getId());
		if(marketingIntegralTaskEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingIntegralTaskService.updateById(marketingIntegralTask);
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
	@AutoLog(value = "积分任务-通过id删除")
	@ApiOperation(value="积分任务-通过id删除", notes="积分任务-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingIntegralTaskService.removeById(id);
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
	@AutoLog(value = "积分任务-批量删除")
	@ApiOperation(value="积分任务-批量删除", notes="积分任务-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingIntegralTask> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingIntegralTask> result = new Result<MarketingIntegralTask>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingIntegralTaskService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "积分任务-通过id查询")
	@ApiOperation(value="积分任务-通过id查询", notes="积分任务-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingIntegralTask> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingIntegralTask> result = new Result<MarketingIntegralTask>();
		MarketingIntegralTask marketingIntegralTask = marketingIntegralTaskService.getById(id);
		if(marketingIntegralTask==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingIntegralTask);
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
      QueryWrapper<MarketingIntegralTask> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingIntegralTask marketingIntegralTask = JSON.parseObject(deString, MarketingIntegralTask.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingIntegralTask, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingIntegralTask> pageList = marketingIntegralTaskService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "积分任务列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingIntegralTask.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("积分任务列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingIntegralTask> listMarketingIntegralTasks = ExcelImportUtil.importExcel(file.getInputStream(), MarketingIntegralTask.class, params);
              marketingIntegralTaskService.saveBatch(listMarketingIntegralTasks);
              return Result.ok("文件导入成功！数据行数:" + listMarketingIntegralTasks.size());
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
  @PostMapping("deleteAndExplain")
  public Result<?> deleteAndExplain(@RequestBody MarketingIntegralTask marketingIntegralTask){
  	marketingIntegralTaskService.updateById(marketingIntegralTask);
  	marketingIntegralTaskService.removeById(marketingIntegralTask.getId());
  	return Result.ok("删除成功");
  }
}
