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
import org.jeecg.modules.marketing.dto.MarketingGroupIntegralManageRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManageRecord;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageRecordService;
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
 * @Description: 拼购记录
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼购记录")
@RestController
@RequestMapping("/marketing/marketingGroupIntegralManageRecord")
public class MarketingGroupIntegralManageRecordController {
	@Autowired
	private IMarketingGroupIntegralManageRecordService marketingGroupIntegralManageRecordService;


	 /**
	  * 根据拼团列表id获取拼团记录
	  *
	  * @param marketingGroupIntegralManageListId
	  * @return
	  */
	 @RequestMapping("getByMarketingGroupIntegralManageListId")
	 @ResponseBody
	public Result<?> getByMarketingGroupIntegralManageListId(String marketingGroupIntegralManageListId, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
		//参数校验
		if(StringUtils.isBlank(marketingGroupIntegralManageListId)){
			return Result.error("拼购列表id不能为空");
		}
		return Result.ok(marketingGroupIntegralManageRecordService.getByMarketingGroupIntegralManageListId(new Page<>(pageNo,pageSize),marketingGroupIntegralManageListId));
	}
	
	/**
	  * 分页列表查询
	 * @param marketingGroupIntegralManageRecordDTO
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value="拼购记录-分页列表查询", notes="拼购记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingGroupIntegralManageRecordDTO marketingGroupIntegralManageRecordDTO,
																		   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																		   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(marketingGroupIntegralManageRecordService.queryPageList(new Page<>(pageNo,pageSize),marketingGroupIntegralManageRecordDTO));
	}
	
	/**
	  *   添加
	 * @param marketingGroupIntegralManageRecord
	 * @return
	 */
	@AutoLog(value = "拼购记录-添加")
	@ApiOperation(value="拼购记录-添加", notes="拼购记录-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGroupIntegralManageRecord> add(@RequestBody MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecord) {
		Result<MarketingGroupIntegralManageRecord> result = new Result<MarketingGroupIntegralManageRecord>();
		try {
			marketingGroupIntegralManageRecordService.save(marketingGroupIntegralManageRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGroupIntegralManageRecord
	 * @return
	 */
	@AutoLog(value = "拼购记录-编辑")
	@ApiOperation(value="拼购记录-编辑", notes="拼购记录-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGroupIntegralManageRecord> edit(@RequestBody MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecord) {
		Result<MarketingGroupIntegralManageRecord> result = new Result<MarketingGroupIntegralManageRecord>();
		MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecordEntity = marketingGroupIntegralManageRecordService.getById(marketingGroupIntegralManageRecord.getId());
		if(marketingGroupIntegralManageRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGroupIntegralManageRecordService.updateById(marketingGroupIntegralManageRecord);
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
	@AutoLog(value = "拼购记录-通过id删除")
	@ApiOperation(value="拼购记录-通过id删除", notes="拼购记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGroupIntegralManageRecordService.removeById(id);
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
	@AutoLog(value = "拼购记录-批量删除")
	@ApiOperation(value="拼购记录-批量删除", notes="拼购记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGroupIntegralManageRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGroupIntegralManageRecord> result = new Result<MarketingGroupIntegralManageRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGroupIntegralManageRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拼购记录-通过id查询")
	@ApiOperation(value="拼购记录-通过id查询", notes="拼购记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGroupIntegralManageRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGroupIntegralManageRecord> result = new Result<MarketingGroupIntegralManageRecord>();
		MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecord = marketingGroupIntegralManageRecordService.getById(id);
		if(marketingGroupIntegralManageRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGroupIntegralManageRecord);
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
      QueryWrapper<MarketingGroupIntegralManageRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecord = JSON.parseObject(deString, MarketingGroupIntegralManageRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGroupIntegralManageRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGroupIntegralManageRecord> pageList = marketingGroupIntegralManageRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "拼购记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGroupIntegralManageRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拼购记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGroupIntegralManageRecord> listMarketingGroupIntegralManageRecords = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGroupIntegralManageRecord.class, params);
              marketingGroupIntegralManageRecordService.saveBatch(listMarketingGroupIntegralManageRecords);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGroupIntegralManageRecords.size());
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
