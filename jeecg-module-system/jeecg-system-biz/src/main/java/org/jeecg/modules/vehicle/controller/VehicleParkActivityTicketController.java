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
import org.jeecg.modules.vehicle.dto.VehicleParkActivityTicketDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkActivityTicket;
import org.jeecg.modules.vehicle.service.IVehicleParkActivityTicketService;
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
 * @Description: 送券活动
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="送券活动")
@RestController
@RequestMapping("/vehicle/vehicleParkActivityTicket")
public class VehicleParkActivityTicketController {
	@Autowired
	private IVehicleParkActivityTicketService vehicleParkActivityTicketService;
	
	/**
	  * 分页列表查询
	 * @param vehicleParkActivityTicketDTO
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VehicleParkActivityTicketDTO vehicleParkActivityTicketDTO,
																  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(vehicleParkActivityTicketService.getVehicleParkActivityTicketDTO(new Page<>(pageNo,pageSize),vehicleParkActivityTicketDTO));
	}
	
	/**
	  *   添加
	 * @param vehicleParkActivityTicket
	 * @return
	 */
	@AutoLog(value = "送券活动-添加")
	@ApiOperation(value="送券活动-添加", notes="送券活动-添加")
	@PostMapping(value = "/add")
	public Result<VehicleParkActivityTicket> add(@RequestBody VehicleParkActivityTicket vehicleParkActivityTicket) {
		Result<VehicleParkActivityTicket> result = new Result<VehicleParkActivityTicket>();
		try {
			vehicleParkActivityTicketService.save(vehicleParkActivityTicket);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param vehicleParkActivityTicket
	 * @return
	 */
	@AutoLog(value = "送券活动-编辑")
	@ApiOperation(value="送券活动-编辑", notes="送券活动-编辑")
	@PutMapping(value = "/edit")
	public Result<VehicleParkActivityTicket> edit(@RequestBody VehicleParkActivityTicket vehicleParkActivityTicket) {
		Result<VehicleParkActivityTicket> result = new Result<VehicleParkActivityTicket>();
		VehicleParkActivityTicket vehicleParkActivityTicketEntity = vehicleParkActivityTicketService.getById(vehicleParkActivityTicket.getId());
		if(vehicleParkActivityTicketEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = vehicleParkActivityTicketService.updateById(vehicleParkActivityTicket);
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
	@AutoLog(value = "送券活动-通过id删除")
	@ApiOperation(value="送券活动-通过id删除", notes="送券活动-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			vehicleParkActivityTicketService.removeById(id);
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
	@AutoLog(value = "送券活动-批量删除")
	@ApiOperation(value="送券活动-批量删除", notes="送券活动-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<VehicleParkActivityTicket> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<VehicleParkActivityTicket> result = new Result<VehicleParkActivityTicket>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.vehicleParkActivityTicketService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "送券活动-通过id查询")
	@ApiOperation(value="送券活动-通过id查询", notes="送券活动-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<VehicleParkActivityTicket> queryById(@RequestParam(name="id",required=true) String id) {
		Result<VehicleParkActivityTicket> result = new Result<VehicleParkActivityTicket>();
		VehicleParkActivityTicket vehicleParkActivityTicket = vehicleParkActivityTicketService.getById(id);
		if(vehicleParkActivityTicket==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(vehicleParkActivityTicket);
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
      QueryWrapper<VehicleParkActivityTicket> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              VehicleParkActivityTicket vehicleParkActivityTicket = JSON.parseObject(deString, VehicleParkActivityTicket.class);
              queryWrapper = QueryGenerator.initQueryWrapper(vehicleParkActivityTicket, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<VehicleParkActivityTicket> pageList = vehicleParkActivityTicketService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "送券活动列表");
      mv.addObject(NormalExcelConstants.CLASS, VehicleParkActivityTicket.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("送券活动列表数据", "导出人:Jeecg", "导出信息"));
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
              List<VehicleParkActivityTicket> listVehicleParkActivityTickets = ExcelImportUtil.importExcel(file.getInputStream(), VehicleParkActivityTicket.class, params);
              vehicleParkActivityTicketService.saveBatch(listVehicleParkActivityTickets);
              return Result.ok("文件导入成功！数据行数:" + listVehicleParkActivityTickets.size());
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
