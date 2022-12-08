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
import org.jeecg.common.util.MapHandleUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGood;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupGoodService;
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
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="专区商品")
@RestController
@RequestMapping("/marketingZoneGroupGood/marketingZoneGroupGood")
public class MarketingZoneGroupGoodController {
	@Autowired
	private IMarketingZoneGroupGoodService marketingZoneGroupGoodService;
	
	/**
	  * 分页列表查询
	 * @param marketingZoneGroupGood
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专区商品-分页列表查询")
	@ApiOperation(value="专区商品-分页列表查询", notes="专区商品-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingZoneGroupGood marketingZoneGroupGood,
															   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															   HttpServletRequest req) {
		if (StringUtils.isBlank(marketingZoneGroupGood.getMarketingZoneGroupId())){
			return Result.error("前端专区id未传递!");
		}
		return Result.ok(marketingZoneGroupGoodService.queryPageList(new Page<>(pageNo, pageSize),
				QueryGenerator.initQueryWrapper(marketingZoneGroupGood, req.getParameterMap()),
				MapHandleUtils.handleRequestMap(req.getParameterMap())));
	}
	
	/**
	  *   添加
	 * @param marketingZoneGroupGood
	 * @return
	 */
	@AutoLog(value = "专区商品-添加")
	@ApiOperation(value="专区商品-添加", notes="专区商品-添加")
	@PostMapping(value = "/add")
	public Result<MarketingZoneGroupGood> add(@RequestBody MarketingZoneGroupGood marketingZoneGroupGood) {
		Result<MarketingZoneGroupGood> result = new Result<MarketingZoneGroupGood>();
		try {
			marketingZoneGroupGoodService.save(marketingZoneGroupGood);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingZoneGroupGood
	 * @return
	 */
	@AutoLog(value = "专区商品-编辑")
	@ApiOperation(value="专区商品-编辑", notes="专区商品-编辑")
	@PostMapping(value = "/edit")
	public Result<MarketingZoneGroupGood> edit(@RequestBody MarketingZoneGroupGood marketingZoneGroupGood) {
		Result<MarketingZoneGroupGood> result = new Result<MarketingZoneGroupGood>();
		if (StringUtils.isBlank(marketingZoneGroupGood.getId())){
			return result.error500("前端id未传递!");
		}
		MarketingZoneGroupGood marketingZoneGroupGoodEntity = marketingZoneGroupGoodService.getById(marketingZoneGroupGood.getId());
		if(marketingZoneGroupGoodEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingZoneGroupGoodService.updateById(marketingZoneGroupGood);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param marketingZoneGroupGood
	 * @return
	 */
	@AutoLog(value = "专区商品-通过id删除")
	@ApiOperation(value="专区商品-通过id删除", notes="专区商品-通过id删除")
	@PostMapping(value = "/delete")
	public Result<?> delete(@RequestBody MarketingZoneGroupGood marketingZoneGroupGood) {
		try {
			if (StringUtils.isBlank(marketingZoneGroupGood.getId())){
				return Result.error("前端id未传递");
			}
			marketingZoneGroupGoodService.updateById(marketingZoneGroupGood);
			marketingZoneGroupGoodService.removeById(marketingZoneGroupGood.getId());
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
	@AutoLog(value = "专区商品-批量删除")
	@ApiOperation(value="专区商品-批量删除", notes="专区商品-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingZoneGroupGood> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingZoneGroupGood> result = new Result<MarketingZoneGroupGood>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingZoneGroupGoodService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专区商品-通过id查询")
	@ApiOperation(value="专区商品-通过id查询", notes="专区商品-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingZoneGroupGood> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingZoneGroupGood> result = new Result<MarketingZoneGroupGood>();
		MarketingZoneGroupGood marketingZoneGroupGood = marketingZoneGroupGoodService.getById(id);
		if(marketingZoneGroupGood==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingZoneGroupGood);
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
      QueryWrapper<MarketingZoneGroupGood> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingZoneGroupGood marketingZoneGroupGood = JSON.parseObject(deString, MarketingZoneGroupGood.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingZoneGroupGood, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingZoneGroupGood> pageList = marketingZoneGroupGoodService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "专区商品列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingZoneGroupGood.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("专区商品列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingZoneGroupGood> listMarketingZoneGroupGoods = ExcelImportUtil.importExcel(file.getInputStream(), MarketingZoneGroupGood.class, params);
              marketingZoneGroupGoodService.saveBatch(listMarketingZoneGroupGoods);
              return Result.ok("文件导入成功！数据行数:" + listMarketingZoneGroupGoods.size());
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

	 /**
	  * 添加商品
	  * @param marketingZoneGroupGoodList
	  * @return
	  */
  @PostMapping("addArr")
  public Result<?> addArr(@RequestBody List<MarketingZoneGroupGood> marketingZoneGroupGoodList){
  	if (marketingZoneGroupGoodList.size()>0){
		MarketingZoneGroupGood marketingZoneGroupGood = marketingZoneGroupGoodList.get(0);
		if (StringUtils.isBlank(marketingZoneGroupGood.getMarketingZoneGroupId())){
			return Result.error("前端拼团专区id未传递");
		}else {
			marketingZoneGroupGoodService.saveBatch(marketingZoneGroupGoodList);
			return Result.ok("添加成功!");
		}
	}else {
  		return Result.error("至少添加一条数据");
	}
  }
}
