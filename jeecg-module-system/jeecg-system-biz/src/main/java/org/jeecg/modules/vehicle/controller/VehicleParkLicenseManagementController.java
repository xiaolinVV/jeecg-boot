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
import org.jeecg.modules.vehicle.dto.VehicleParkLicenseManagementDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkLicenseManagement;
import org.jeecg.modules.vehicle.service.IVehicleParkLicenseManagementService;
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
 * @Description: 车牌管理
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="车牌管理")
@RestController
@RequestMapping("/vehicle/vehicleParkLicenseManagement")
public class VehicleParkLicenseManagementController {
	@Autowired
	private IVehicleParkLicenseManagementService vehicleParkLicenseManagementService;
	
	/**
	  * 分页列表查询
	 * @param vehicleParkLicenseManagementDTO
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "车牌管理-分页列表查询")
	@ApiOperation(value="车牌管理-分页列表查询", notes="车牌管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VehicleParkLicenseManagementDTO vehicleParkLicenseManagementDTO,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(vehicleParkLicenseManagementService.getVehicleParkLicenseManagementListDTO(new Page<>(pageNo,pageSize),vehicleParkLicenseManagementDTO));
	}
	
	/**
	  *  编辑
	 * @param vehicleParkLicenseManagement
	 * @return
	 */
	@AutoLog(value = "车牌管理-编辑")
	@ApiOperation(value="车牌管理-编辑", notes="车牌管理-编辑")
	@PutMapping(value = "/edit")
	public Result<VehicleParkLicenseManagement> edit(@RequestBody VehicleParkLicenseManagement vehicleParkLicenseManagement) {
		Result<VehicleParkLicenseManagement> result = new Result<VehicleParkLicenseManagement>();
		VehicleParkLicenseManagement vehicleParkLicenseManagementEntity = vehicleParkLicenseManagementService.getById(vehicleParkLicenseManagement.getId());
		if(vehicleParkLicenseManagementEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = vehicleParkLicenseManagementService.updateById(vehicleParkLicenseManagement);
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
	@AutoLog(value = "车牌管理-通过id删除")
	@ApiOperation(value="车牌管理-通过id删除", notes="车牌管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			vehicleParkLicenseManagementService.removeById(id);
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
	@AutoLog(value = "车牌管理-批量删除")
	@ApiOperation(value="车牌管理-批量删除", notes="车牌管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<VehicleParkLicenseManagement> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<VehicleParkLicenseManagement> result = new Result<VehicleParkLicenseManagement>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.vehicleParkLicenseManagementService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车牌管理-通过id查询")
	@ApiOperation(value="车牌管理-通过id查询", notes="车牌管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<VehicleParkLicenseManagement> queryById(@RequestParam(name="id",required=true) String id) {
		Result<VehicleParkLicenseManagement> result = new Result<VehicleParkLicenseManagement>();
		VehicleParkLicenseManagement vehicleParkLicenseManagement = vehicleParkLicenseManagementService.getById(id);
		if(vehicleParkLicenseManagement==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(vehicleParkLicenseManagement);
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
      QueryWrapper<VehicleParkLicenseManagement> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              VehicleParkLicenseManagement vehicleParkLicenseManagement = JSON.parseObject(deString, VehicleParkLicenseManagement.class);
              queryWrapper = QueryGenerator.initQueryWrapper(vehicleParkLicenseManagement, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<VehicleParkLicenseManagement> pageList = vehicleParkLicenseManagementService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "车牌管理列表");
      mv.addObject(NormalExcelConstants.CLASS, VehicleParkLicenseManagement.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("车牌管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<VehicleParkLicenseManagement> listVehicleParkLicenseManagements = ExcelImportUtil.importExcel(file.getInputStream(), VehicleParkLicenseManagement.class, params);
              vehicleParkLicenseManagementService.saveBatch(listVehicleParkLicenseManagements);
              return Result.ok("文件导入成功！数据行数:" + listVehicleParkLicenseManagements.size());
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
