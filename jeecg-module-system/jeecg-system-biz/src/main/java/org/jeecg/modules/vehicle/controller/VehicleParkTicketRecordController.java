package org.jeecg.modules.vehicle.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.vehicle.dto.VehicleParkTicketRecordDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkTicketRecord;
import org.jeecg.modules.vehicle.service.IVehicleParkTicketRecordService;
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
 * @Description: 券记录
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="券记录")
@RestController
@RequestMapping("/vehicle/vehicleParkTicketRecord")
public class VehicleParkTicketRecordController {
	@Autowired
	private IVehicleParkTicketRecordService vehicleParkTicketRecordService;
	
	/**
	  * 分页列表查询
	 * @param vehicleParkTicketRecordDTO
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VehicleParkTicketRecordDTO vehicleParkTicketRecordDTO,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(vehicleParkTicketRecordService.getVehicleParkTicketRecordListDTO(new Page<>(pageNo,pageSize),vehicleParkTicketRecordDTO));
	}
	
	/**
	  *  编辑
	 * @param vehicleParkTicketRecord
	 * @return
	 */
	@AutoLog(value = "券记录-编辑")
	@ApiOperation(value="券记录-编辑", notes="券记录-编辑")
	@PutMapping(value = "/edit")
	public Result<VehicleParkTicketRecord> edit(@RequestBody VehicleParkTicketRecord vehicleParkTicketRecord) {
		Result<VehicleParkTicketRecord> result = new Result<VehicleParkTicketRecord>();
		VehicleParkTicketRecord vehicleParkTicketRecordEntity = vehicleParkTicketRecordService.getById(vehicleParkTicketRecord.getId());
		if(vehicleParkTicketRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = vehicleParkTicketRecordService.updateById(vehicleParkTicketRecord);
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
	@AutoLog(value = "券记录-通过id删除")
	@ApiOperation(value="券记录-通过id删除", notes="券记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			vehicleParkTicketRecordService.removeById(id);
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
	@AutoLog(value = "券记录-批量删除")
	@ApiOperation(value="券记录-批量删除", notes="券记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<VehicleParkTicketRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<VehicleParkTicketRecord> result = new Result<VehicleParkTicketRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.vehicleParkTicketRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
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
      QueryWrapper<VehicleParkTicketRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              VehicleParkTicketRecord vehicleParkTicketRecord = JSON.parseObject(deString, VehicleParkTicketRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(vehicleParkTicketRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<VehicleParkTicketRecord> pageList = vehicleParkTicketRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "券记录列表");
      mv.addObject(NormalExcelConstants.CLASS, VehicleParkTicketRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("券记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<VehicleParkTicketRecord> listVehicleParkTicketRecords = ExcelImportUtil.importExcel(file.getInputStream(), VehicleParkTicketRecord.class, params);
              vehicleParkTicketRecordService.saveBatch(listVehicleParkTicketRecords);
              return Result.ok("文件导入成功！数据行数:" + listVehicleParkTicketRecords.size());
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
