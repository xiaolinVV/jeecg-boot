package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.entity.StoreTablesRecord;
import org.jeecg.modules.store.service.IStoreTablesRecordService;
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
 * @Description: 桌台列表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="桌台列表")
@RestController
@RequestMapping("/store/storeTablesRecord")
public class StoreTablesRecordController {
	@Autowired
	private IStoreTablesRecordService storeTablesRecordService;

	/**
	  * 分页列表查询
	 * @param storeManageId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "桌台列表-分页列表查询")
	@ApiOperation(value="桌台列表-分页列表查询", notes="桌台列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(String storeManageId,
									  String tableName,
									  String qrCodeName,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Map<String,Object> paramMap= Maps.newHashMap();
		paramMap.put("storeManageId",storeManageId);
		paramMap.put("tableName",tableName);
		paramMap.put("qrCodeName",qrCodeName);
		return Result.ok(storeTablesRecordService.queryPageList(new Page<>(pageNo,pageSize),paramMap));
	}
	
	/**
	  *   添加
	 * @param storeTablesRecord
	 * @return
	 */
	@AutoLog(value = "桌台列表-添加")
	@ApiOperation(value="桌台列表-添加", notes="桌台列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StoreTablesRecord storeTablesRecord) {
		Result<StoreTablesRecord> result = new Result<StoreTablesRecord>();
		try {

			//判断桌台码是否被使用
			if(storeTablesRecordService.count(new LambdaQueryWrapper<StoreTablesRecord>().eq(StoreTablesRecord::getStoreTablesId,storeTablesRecord.getStoreTablesId()))>0){
				return Result.error("桌台二维码已被使用");
			}

			storeTablesRecord.setSerialNumber(OrderNoUtils.getOrderNo());
			storeTablesRecordService.save(storeTablesRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeTablesRecord
	 * @return
	 */
	@AutoLog(value = "桌台列表-编辑")
	@ApiOperation(value="桌台列表-编辑", notes="桌台列表-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreTablesRecord> edit(@RequestBody StoreTablesRecord storeTablesRecord) {
		Result<StoreTablesRecord> result = new Result<StoreTablesRecord>();
		StoreTablesRecord storeTablesRecordEntity = storeTablesRecordService.getById(storeTablesRecord.getId());
		if(storeTablesRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeTablesRecordService.updateById(storeTablesRecord);
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
	@AutoLog(value = "桌台列表-通过id删除")
	@ApiOperation(value="桌台列表-通过id删除", notes="桌台列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeTablesRecordService.removeById(id);
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
	@AutoLog(value = "桌台列表-批量删除")
	@ApiOperation(value="桌台列表-批量删除", notes="桌台列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreTablesRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreTablesRecord> result = new Result<StoreTablesRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeTablesRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "桌台列表-通过id查询")
	@ApiOperation(value="桌台列表-通过id查询", notes="桌台列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreTablesRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreTablesRecord> result = new Result<StoreTablesRecord>();
		StoreTablesRecord storeTablesRecord = storeTablesRecordService.getById(id);
		if(storeTablesRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeTablesRecord);
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
      QueryWrapper<StoreTablesRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreTablesRecord storeTablesRecord = JSON.parseObject(deString, StoreTablesRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeTablesRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreTablesRecord> pageList = storeTablesRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "桌台列表列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreTablesRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("桌台列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreTablesRecord> listStoreTablesRecords = ExcelImportUtil.importExcel(file.getInputStream(), StoreTablesRecord.class, params);
              storeTablesRecordService.saveBatch(listStoreTablesRecords);
              return Result.ok("文件导入成功！数据行数:" + listStoreTablesRecords.size());
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
