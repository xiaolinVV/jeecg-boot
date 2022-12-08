package org.jeecg.modules.marketing.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.marketing.entity.MarketingDiscountCertificateSetting;
import org.jeecg.modules.marketing.service.IMarketingDiscountCertificateSettingService;
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
 * @Description: 券中心设置
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="券中心设置")
@RestController
@RequestMapping("/marketingDiscountCertificateSetting/marketingDiscountCertificateSetting")
public class MarketingDiscountCertificateSettingController {
	@Autowired
	private IMarketingDiscountCertificateSettingService marketingDiscountCertificateSettingService;
	
	/**
	  * 分页列表查询
	 * @param marketingDiscountCertificateSetting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "券中心设置-分页列表查询")
	@ApiOperation(value="券中心设置-分页列表查询", notes="券中心设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingDiscountCertificateSetting>> queryPageList(MarketingDiscountCertificateSetting marketingDiscountCertificateSetting,
																			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																			HttpServletRequest req) {
		Result<IPage<MarketingDiscountCertificateSetting>> result = new Result<IPage<MarketingDiscountCertificateSetting>>();
		QueryWrapper<MarketingDiscountCertificateSetting> queryWrapper = QueryGenerator.initQueryWrapper(marketingDiscountCertificateSetting, req.getParameterMap());
		Page<MarketingDiscountCertificateSetting> page = new Page<MarketingDiscountCertificateSetting>(pageNo, pageSize);
		IPage<MarketingDiscountCertificateSetting> pageList = marketingDiscountCertificateSettingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingDiscountCertificateSetting
	 * @return
	 */
	@AutoLog(value = "券中心设置-添加")
	@ApiOperation(value="券中心设置-添加", notes="券中心设置-添加")
	@PostMapping(value = "/add")
	public Result<MarketingDiscountCertificateSetting> add(@RequestBody MarketingDiscountCertificateSetting marketingDiscountCertificateSetting) {
		Result<MarketingDiscountCertificateSetting> result = new Result<MarketingDiscountCertificateSetting>();
		try {
			marketingDiscountCertificateSettingService.save(marketingDiscountCertificateSetting);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingDiscountCertificateSetting
	 * @return
	 */
	@AutoLog(value = "券中心设置-编辑")
	@ApiOperation(value="券中心设置-编辑", notes="券中心设置-编辑")
	@PostMapping(value = "/edit")
	public Result<MarketingDiscountCertificateSetting> edit(@RequestBody MarketingDiscountCertificateSetting marketingDiscountCertificateSetting) {
		Result<MarketingDiscountCertificateSetting> result = new Result<MarketingDiscountCertificateSetting>();
		MarketingDiscountCertificateSetting marketingDiscountCertificateSettingEntity = marketingDiscountCertificateSettingService.getById(marketingDiscountCertificateSetting.getId());
		if(marketingDiscountCertificateSettingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingDiscountCertificateSettingService.updateById(marketingDiscountCertificateSetting);
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
	@AutoLog(value = "券中心设置-通过id删除")
	@ApiOperation(value="券中心设置-通过id删除", notes="券中心设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingDiscountCertificateSettingService.removeById(id);
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
	@AutoLog(value = "券中心设置-批量删除")
	@ApiOperation(value="券中心设置-批量删除", notes="券中心设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingDiscountCertificateSetting> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingDiscountCertificateSetting> result = new Result<MarketingDiscountCertificateSetting>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingDiscountCertificateSettingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "券中心设置-通过id查询")
	@ApiOperation(value="券中心设置-通过id查询", notes="券中心设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingDiscountCertificateSetting> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingDiscountCertificateSetting> result = new Result<MarketingDiscountCertificateSetting>();
		MarketingDiscountCertificateSetting marketingDiscountCertificateSetting = marketingDiscountCertificateSettingService.getById(id);
		if(marketingDiscountCertificateSetting==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingDiscountCertificateSetting);
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
      QueryWrapper<MarketingDiscountCertificateSetting> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingDiscountCertificateSetting marketingDiscountCertificateSetting = JSON.parseObject(deString, MarketingDiscountCertificateSetting.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingDiscountCertificateSetting, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingDiscountCertificateSetting> pageList = marketingDiscountCertificateSettingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "券中心设置列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingDiscountCertificateSetting.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("券中心设置列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingDiscountCertificateSetting> listMarketingDiscountCertificateSettings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingDiscountCertificateSetting.class, params);
              marketingDiscountCertificateSettingService.saveBatch(listMarketingDiscountCertificateSettings);
              return Result.ok("文件导入成功！数据行数:" + listMarketingDiscountCertificateSettings.size());
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
	  * 返回券中心设置
	  * @return
	  */
  @GetMapping("findMarketingDiscountCertificateSetting")
  public Result<?> findMarketingDiscountCertificateSetting(){
	  MarketingDiscountCertificateSetting marketingDiscountCertificateSetting = marketingDiscountCertificateSettingService.getOne(new LambdaQueryWrapper<MarketingDiscountCertificateSetting>()
			  .eq(MarketingDiscountCertificateSetting::getDelFlag, "0"));
	  if (marketingDiscountCertificateSetting != null){
	  	return Result.ok(marketingDiscountCertificateSetting);
	  }else {
	  	return null;
	  }
  }

	 /**
	  * 券中心设置
	  * @param marketingDiscountCertificateSetting
	  * @return
	  */
  @PostMapping("setMarketingDiscountCertificateSetting")
  public Result<?> setMarketingDiscountCertificateSetting(@RequestBody MarketingDiscountCertificateSetting marketingDiscountCertificateSetting){
  	if (StringUtils.isBlank(marketingDiscountCertificateSetting.getId())){
  		return this.add(marketingDiscountCertificateSetting);
	}else {
  		return this.edit(marketingDiscountCertificateSetting);
	}
  }

}
