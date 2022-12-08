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

import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.marketing.entity.MarketingCertificateGood;
import org.jeecg.modules.marketing.service.IMarketingCertificateGoodService;
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
 * @Description: 兑换券商品映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="兑换券商品映射")
@RestController
@RequestMapping("/marketingCertificateGood/marketingCertificateGood")
public class MarketingCertificateGoodController {
	@Autowired
	private IMarketingCertificateGoodService marketingCertificateGoodService;
	
	/**
	  * 分页列表查询
	 * @param marketingCertificateGood
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "兑换券商品映射-分页列表查询")
	@ApiOperation(value="兑换券商品映射-分页列表查询", notes="兑换券商品映射-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingCertificateGood>> queryPageList(MarketingCertificateGood marketingCertificateGood,
																 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																 HttpServletRequest req) {
		Result<IPage<MarketingCertificateGood>> result = new Result<IPage<MarketingCertificateGood>>();
		QueryWrapper<MarketingCertificateGood> queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateGood, req.getParameterMap());
		Page<MarketingCertificateGood> page = new Page<MarketingCertificateGood>(pageNo, pageSize);
		IPage<MarketingCertificateGood> pageList = marketingCertificateGoodService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingCertificateGood
	 * @return
	 */
	@AutoLog(value = "兑换券商品映射-添加")
	@ApiOperation(value="兑换券商品映射-添加", notes="兑换券商品映射-添加")
	@PostMapping(value = "/add")
	public Result<MarketingCertificateGood> add(@RequestBody MarketingCertificateGood marketingCertificateGood) {
		Result<MarketingCertificateGood> result = new Result<MarketingCertificateGood>();
		try {
			marketingCertificateGoodService.save(marketingCertificateGood);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingCertificateGood
	 * @return
	 */
	@AutoLog(value = "兑换券商品映射-编辑")
	@ApiOperation(value="兑换券商品映射-编辑", notes="兑换券商品映射-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingCertificateGood> edit(@RequestBody MarketingCertificateGood marketingCertificateGood) {
		Result<MarketingCertificateGood> result = new Result<MarketingCertificateGood>();
		MarketingCertificateGood marketingCertificateGoodEntity = marketingCertificateGoodService.getById(marketingCertificateGood.getId());
		if(marketingCertificateGoodEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingCertificateGoodService.updateById(marketingCertificateGood);
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
	@AutoLog(value = "兑换券商品映射-通过id删除")
	@ApiOperation(value="兑换券商品映射-通过id删除", notes="兑换券商品映射-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingCertificateGoodService.removeById(id);
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
	@AutoLog(value = "兑换券商品映射-批量删除")
	@ApiOperation(value="兑换券商品映射-批量删除", notes="兑换券商品映射-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingCertificateGood> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingCertificateGood> result = new Result<MarketingCertificateGood>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingCertificateGoodService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "兑换券商品映射-通过id查询")
	@ApiOperation(value="兑换券商品映射-通过id查询", notes="兑换券商品映射-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingCertificateGood> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingCertificateGood> result = new Result<MarketingCertificateGood>();
		MarketingCertificateGood marketingCertificateGood = marketingCertificateGoodService.getById(id);
		if(marketingCertificateGood==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingCertificateGood);
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
      QueryWrapper<MarketingCertificateGood> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingCertificateGood marketingCertificateGood = JSON.parseObject(deString, MarketingCertificateGood.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateGood, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingCertificateGood> pageList = marketingCertificateGoodService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "兑换券商品映射列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingCertificateGood.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("兑换券商品映射列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingCertificateGood> listMarketingCertificateGoods = ExcelImportUtil.importExcel(file.getInputStream(), MarketingCertificateGood.class, params);
              marketingCertificateGoodService.saveBatch(listMarketingCertificateGoods);
              return Result.ok("文件导入成功！数据行数:" + listMarketingCertificateGoods.size());
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
