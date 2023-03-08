package org.jeecg.modules.marketing.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingRushAdvertising;
import org.jeecg.modules.marketing.service.IMarketingRushAdvertisingService;
import java.util.Date;
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
 * @Description: 抢购-活动广告
 * @Author: jeecg-boot
 * @Date:   2022-09-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="抢购-活动广告")
@RestController
@RequestMapping("/marketing/marketingRushAdvertising")
public class MarketingRushAdvertisingController {
	@Autowired
	private IMarketingRushAdvertisingService marketingRushAdvertisingService;
	
	/**
	  * 分页列表查询
	 * @param marketingRushAdvertising
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "抢购-活动广告-分页列表查询")
	@ApiOperation(value="抢购-活动广告-分页列表查询", notes="抢购-活动广告-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingRushAdvertising>> queryPageList(MarketingRushAdvertising marketingRushAdvertising,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingRushAdvertising>> result = new Result<IPage<MarketingRushAdvertising>>();
		QueryWrapper<MarketingRushAdvertising> queryWrapper = QueryGenerator.initQueryWrapper(marketingRushAdvertising, req.getParameterMap());
		Page<MarketingRushAdvertising> page = new Page<MarketingRushAdvertising>(pageNo, pageSize);
		IPage<MarketingRushAdvertising> pageList = marketingRushAdvertisingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingRushAdvertising
	 * @return
	 */
	@AutoLog(value = "抢购-活动广告-添加")
	@ApiOperation(value="抢购-活动广告-添加", notes="抢购-活动广告-添加")
	@PostMapping(value = "/add")
	public Result<MarketingRushAdvertising> add(@RequestBody MarketingRushAdvertising marketingRushAdvertising) {
		Result<MarketingRushAdvertising> result = new Result<MarketingRushAdvertising>();
		try {
			marketingRushAdvertisingService.save(marketingRushAdvertising);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingRushAdvertising
	 * @return
	 */
	@AutoLog(value = "抢购-活动广告-编辑")
	@ApiOperation(value="抢购-活动广告-编辑", notes="抢购-活动广告-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingRushAdvertising> edit(@RequestBody MarketingRushAdvertising marketingRushAdvertising) {
		Result<MarketingRushAdvertising> result = new Result<MarketingRushAdvertising>();
		MarketingRushAdvertising marketingRushAdvertisingEntity = marketingRushAdvertisingService.getById(marketingRushAdvertising.getId());
		if(marketingRushAdvertisingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingRushAdvertisingService.updateById(marketingRushAdvertising);
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
	@AutoLog(value = "抢购-活动广告-通过id删除")
	@ApiOperation(value="抢购-活动广告-通过id删除", notes="抢购-活动广告-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingRushAdvertisingService.removeById(id);
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
	@AutoLog(value = "抢购-活动广告-批量删除")
	@ApiOperation(value="抢购-活动广告-批量删除", notes="抢购-活动广告-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingRushAdvertising> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingRushAdvertising> result = new Result<MarketingRushAdvertising>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingRushAdvertisingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "抢购-活动广告-通过id查询")
	@ApiOperation(value="抢购-活动广告-通过id查询", notes="抢购-活动广告-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingRushAdvertising> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingRushAdvertising> result = new Result<MarketingRushAdvertising>();
		MarketingRushAdvertising marketingRushAdvertising = marketingRushAdvertisingService.getById(id);
		if(marketingRushAdvertising==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingRushAdvertising);
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
      QueryWrapper<MarketingRushAdvertising> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingRushAdvertising marketingRushAdvertising = JSON.parseObject(deString, MarketingRushAdvertising.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingRushAdvertising, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingRushAdvertising> pageList = marketingRushAdvertisingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "抢购-活动广告列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingRushAdvertising.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("抢购-活动广告列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingRushAdvertising> listMarketingRushAdvertisings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingRushAdvertising.class, params);
              marketingRushAdvertisingService.saveBatch(listMarketingRushAdvertisings);
              return Result.ok("文件导入成功！数据行数:" + listMarketingRushAdvertisings.size());
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
