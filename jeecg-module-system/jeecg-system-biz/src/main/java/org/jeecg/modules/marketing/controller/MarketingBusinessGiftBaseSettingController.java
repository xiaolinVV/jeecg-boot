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
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftBaseSettingService;
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
 * @Description: 创业礼包基础设置
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="创业礼包基础设置")
@RestController
@RequestMapping("/marketingBusinessGiftBaseSetting/marketingBusinessGiftBaseSetting")
public class MarketingBusinessGiftBaseSettingController {
	@Autowired
	private IMarketingBusinessGiftBaseSettingService marketingBusinessGiftBaseSettingService;
	
	/**
	  * 分页列表查询
	 * @param marketingBusinessGiftBaseSetting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "创业礼包基础设置-分页列表查询")
	@ApiOperation(value="创业礼包基础设置-分页列表查询", notes="创业礼包基础设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingBusinessGiftBaseSetting>> queryPageList(MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting,
																		 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																		 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																		 HttpServletRequest req) {
		Result<IPage<MarketingBusinessGiftBaseSetting>> result = new Result<IPage<MarketingBusinessGiftBaseSetting>>();
		QueryWrapper<MarketingBusinessGiftBaseSetting> queryWrapper = QueryGenerator.initQueryWrapper(marketingBusinessGiftBaseSetting, req.getParameterMap());
		Page<MarketingBusinessGiftBaseSetting> page = new Page<MarketingBusinessGiftBaseSetting>(pageNo, pageSize);
		IPage<MarketingBusinessGiftBaseSetting> pageList = marketingBusinessGiftBaseSettingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingBusinessGiftBaseSetting
	 * @return
	 */
	@AutoLog(value = "创业礼包基础设置-添加")
	@ApiOperation(value="创业礼包基础设置-添加", notes="创业礼包基础设置-添加")
	@PostMapping(value = "/add")
	public Result<MarketingBusinessGiftBaseSetting> add(@RequestBody MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting) {
		Result<MarketingBusinessGiftBaseSetting> result = new Result<MarketingBusinessGiftBaseSetting>();
		try {
			marketingBusinessGiftBaseSettingService.save(marketingBusinessGiftBaseSetting);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingBusinessGiftBaseSetting
	 * @return
	 */
	@AutoLog(value = "创业礼包基础设置-编辑")
	@ApiOperation(value="创业礼包基础设置-编辑", notes="创业礼包基础设置-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingBusinessGiftBaseSetting> edit(@RequestBody MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting) {
		Result<MarketingBusinessGiftBaseSetting> result = new Result<MarketingBusinessGiftBaseSetting>();
		MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSettingEntity = marketingBusinessGiftBaseSettingService.getById(marketingBusinessGiftBaseSetting.getId());
		if(marketingBusinessGiftBaseSettingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingBusinessGiftBaseSettingService.updateById(marketingBusinessGiftBaseSetting);
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
	@AutoLog(value = "创业礼包基础设置-通过id删除")
	@ApiOperation(value="创业礼包基础设置-通过id删除", notes="创业礼包基础设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingBusinessGiftBaseSettingService.removeById(id);
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
	@AutoLog(value = "创业礼包基础设置-批量删除")
	@ApiOperation(value="创业礼包基础设置-批量删除", notes="创业礼包基础设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingBusinessGiftBaseSetting> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingBusinessGiftBaseSetting> result = new Result<MarketingBusinessGiftBaseSetting>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingBusinessGiftBaseSettingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "创业礼包基础设置-通过id查询")
	@ApiOperation(value="创业礼包基础设置-通过id查询", notes="创业礼包基础设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingBusinessGiftBaseSetting> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingBusinessGiftBaseSetting> result = new Result<MarketingBusinessGiftBaseSetting>();
		MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting = marketingBusinessGiftBaseSettingService.getById(id);
		if(marketingBusinessGiftBaseSetting==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingBusinessGiftBaseSetting);
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
      QueryWrapper<MarketingBusinessGiftBaseSetting> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting = JSON.parseObject(deString, MarketingBusinessGiftBaseSetting.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingBusinessGiftBaseSetting, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingBusinessGiftBaseSetting> pageList = marketingBusinessGiftBaseSettingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "创业礼包基础设置列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingBusinessGiftBaseSetting.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("创业礼包基础设置列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingBusinessGiftBaseSetting> listMarketingBusinessGiftBaseSettings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingBusinessGiftBaseSetting.class, params);
              marketingBusinessGiftBaseSettingService.saveBatch(listMarketingBusinessGiftBaseSettings);
              return Result.ok("文件导入成功！数据行数:" + listMarketingBusinessGiftBaseSettings.size());
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
	  * 基础设置返显
	  *
	  * @return
	  */
	 @AutoLog(value = "基础设置返显")
	 @ApiOperation(value = "基础设置返显", notes = "基础设置返显")
	 @GetMapping(value = "/findByOne")
	 public Result<?> findByOne() {
		 return Result.ok(marketingBusinessGiftBaseSettingService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftBaseSetting>()
				 .eq(MarketingBusinessGiftBaseSetting::getDelFlag, "0")
				 .orderByDesc(MarketingBusinessGiftBaseSetting::getCreateTime)
				 .last("limit 1")
		 ));
	 }

	 /**
	  * 设置
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "设置")
	 @ApiOperation(value = "设置", notes = "设置")
	 @PostMapping(value = "/saveAndUpdate")
	 public Result<?> saveAndUpdate(@RequestBody MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting) {
		 try {
			 LambdaQueryWrapper<MarketingBusinessGiftBaseSetting> marketingBusinessGiftBaseSettingLambdaQueryWrapper = new LambdaQueryWrapper<MarketingBusinessGiftBaseSetting>()
					 .eq(MarketingBusinessGiftBaseSetting::getDelFlag, "0");
			 if (marketingBusinessGiftBaseSettingService.count(marketingBusinessGiftBaseSettingLambdaQueryWrapper) == 0) {
				 marketingBusinessGiftBaseSettingService.save(marketingBusinessGiftBaseSetting);
			 } else {
				 marketingBusinessGiftBaseSettingService.update(marketingBusinessGiftBaseSetting,marketingBusinessGiftBaseSettingLambdaQueryWrapper);
			 }
			 return Result.ok("操作成功！");
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 return Result.error("操作失败");
		 }
	 }
}
