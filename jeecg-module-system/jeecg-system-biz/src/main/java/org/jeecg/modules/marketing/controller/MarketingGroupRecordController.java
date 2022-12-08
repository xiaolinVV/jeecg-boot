package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingGroupRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingGroupRecord;
import org.jeecg.modules.marketing.service.IMarketingGroupRecordService;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 参团记录
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="参团记录")
@RestController
@RequestMapping("/marketing/marketingGroupRecord")
public class MarketingGroupRecordController {
	@Autowired
	private IMarketingGroupRecordService marketingGroupRecordService;


	 /**
	  * 分页列表查询
	  *
	  * 张靠勤   2021-4-6
	  *
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @AutoLog(value = "参团记录-分页列表查询")
	 @ApiOperation(value="参团记录-分页列表查询", notes="参团记录-分页列表查询")
	 @GetMapping(value = "/purchaseQualification")
	 public Result<?> purchaseQualification(MarketingGroupRecordDTO marketingGroupRecordDTO,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 Result<IPage<Map<String,Object>>> result = new Result<>();
		 Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);
		 Map<String,String> paramMap= Maps.newHashMap();
		 try {
			 paramMap=BeanUtils.describe(marketingGroupRecordDTO);
			 paramMap.put("rewardStatus","1");
		 } catch (IllegalAccessException e) {
			 e.printStackTrace();
		 } catch (InvocationTargetException e) {
			 e.printStackTrace();
		 } catch (NoSuchMethodException e) {
			 e.printStackTrace();
		 }
		 result.setResult(marketingGroupRecordService.getMarketingGroupRecordList(page,paramMap));
		 result.setSuccess(true);
		 return result;
	 }

	
	/**
	  * 分页列表查询
	 *
	 * 张靠勤   2021-4-6
	 *
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "参团记录-分页列表查询")
	@ApiOperation(value="参团记录-分页列表查询", notes="参团记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingGroupRecordDTO marketingGroupRecordDTO,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Result<IPage<Map<String,Object>>> result = new Result<>();
		Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);
		Map<String,String> paramMap= Maps.newHashMap();
		try {
			paramMap=BeanUtils.describe(marketingGroupRecordDTO);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}
		result.setResult(marketingGroupRecordService.getMarketingGroupRecordList(page,paramMap));
		result.setSuccess(true);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGroupRecord
	 * @return
	 */
	@AutoLog(value = "参团记录-添加")
	@ApiOperation(value="参团记录-添加", notes="参团记录-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGroupRecord> add(@RequestBody MarketingGroupRecord marketingGroupRecord) {
		Result<MarketingGroupRecord> result = new Result<MarketingGroupRecord>();
		try {
			marketingGroupRecordService.save(marketingGroupRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGroupRecord
	 * @return
	 */
	@AutoLog(value = "参团记录-编辑")
	@ApiOperation(value="参团记录-编辑", notes="参团记录-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGroupRecord> edit(@RequestBody MarketingGroupRecord marketingGroupRecord) {
		Result<MarketingGroupRecord> result = new Result<MarketingGroupRecord>();
		MarketingGroupRecord marketingGroupRecordEntity = marketingGroupRecordService.getById(marketingGroupRecord.getId());
		if(marketingGroupRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGroupRecordService.updateById(marketingGroupRecord);
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
	@AutoLog(value = "参团记录-通过id删除")
	@ApiOperation(value="参团记录-通过id删除", notes="参团记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGroupRecordService.removeById(id);
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
	@AutoLog(value = "参团记录-批量删除")
	@ApiOperation(value="参团记录-批量删除", notes="参团记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGroupRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGroupRecord> result = new Result<MarketingGroupRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGroupRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "参团记录-通过id查询")
	@ApiOperation(value="参团记录-通过id查询", notes="参团记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGroupRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGroupRecord> result = new Result<MarketingGroupRecord>();
		MarketingGroupRecord marketingGroupRecord = marketingGroupRecordService.getById(id);
		if(marketingGroupRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGroupRecord);
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
      QueryWrapper<MarketingGroupRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGroupRecord marketingGroupRecord = JSON.parseObject(deString, MarketingGroupRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGroupRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGroupRecord> pageList = marketingGroupRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "参团记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGroupRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("参团记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGroupRecord> listMarketingGroupRecords = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGroupRecord.class, params);
              marketingGroupRecordService.saveBatch(listMarketingGroupRecords);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGroupRecords.size());
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
