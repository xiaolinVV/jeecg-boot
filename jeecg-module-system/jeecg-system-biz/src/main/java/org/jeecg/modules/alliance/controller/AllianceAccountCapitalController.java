package org.jeecg.modules.alliance.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.alliance.dto.AllianceAccountCapitalDTO;
import org.jeecg.modules.alliance.entity.AllianceAccountCapital;
import org.jeecg.modules.alliance.service.IAllianceAccountCapitalService;
import org.jeecg.modules.alliance.vo.AllianceAccountCapitalVO;
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
 * @Description: 加盟商资金管理
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="加盟商资金管理")
@RestController
@RequestMapping("/allianceAccountCapital/allianceAccountCapital")
public class AllianceAccountCapitalController {
	@Autowired
	private IAllianceAccountCapitalService allianceAccountCapitalService;
	
	/**
	  * 分页列表查询
	 * @param allianceAccountCapitalDTO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "加盟商资金管理-分页列表查询")
	@ApiOperation(value="加盟商资金管理-分页列表查询", notes="加盟商资金管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AllianceAccountCapitalVO>> queryPageList(AllianceAccountCapitalDTO allianceAccountCapitalDTO,
																 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																 HttpServletRequest req) {
		Result<IPage<AllianceAccountCapitalVO>> result = new Result<IPage<AllianceAccountCapitalVO>>();
		Page<AllianceAccountCapital> page = new Page<AllianceAccountCapital>(pageNo, pageSize);
		String role = PermissionUtils.ifAllianceRole();
		if (StringUtils.isNotBlank(role)){
			allianceAccountCapitalDTO.setSysUserId(role);
		}
		IPage<AllianceAccountCapitalVO> pageList = allianceAccountCapitalService.queryPageList(page, allianceAccountCapitalDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param allianceAccountCapital
	 * @return
	 */
	@AutoLog(value = "加盟商资金管理-添加")
	@ApiOperation(value="加盟商资金管理-添加", notes="加盟商资金管理-添加")
	@PostMapping(value = "/add")
	public Result<AllianceAccountCapital> add(@RequestBody AllianceAccountCapital allianceAccountCapital) {
		Result<AllianceAccountCapital> result = new Result<AllianceAccountCapital>();
		try {
			allianceAccountCapitalService.save(allianceAccountCapital);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param allianceAccountCapital
	 * @return
	 */
	@AutoLog(value = "加盟商资金管理-编辑")
	@ApiOperation(value="加盟商资金管理-编辑", notes="加盟商资金管理-编辑")
	@PutMapping(value = "/edit")
	public Result<AllianceAccountCapital> edit(@RequestBody AllianceAccountCapital allianceAccountCapital) {
		Result<AllianceAccountCapital> result = new Result<AllianceAccountCapital>();
		AllianceAccountCapital allianceAccountCapitalEntity = allianceAccountCapitalService.getById(allianceAccountCapital.getId());
		if(allianceAccountCapitalEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = allianceAccountCapitalService.updateById(allianceAccountCapital);
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
	@AutoLog(value = "加盟商资金管理-通过id删除")
	@ApiOperation(value="加盟商资金管理-通过id删除", notes="加盟商资金管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			allianceAccountCapitalService.removeById(id);
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
	@AutoLog(value = "加盟商资金管理-批量删除")
	@ApiOperation(value="加盟商资金管理-批量删除", notes="加盟商资金管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<AllianceAccountCapital> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<AllianceAccountCapital> result = new Result<AllianceAccountCapital>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.allianceAccountCapitalService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "加盟商资金管理-通过id查询")
	@ApiOperation(value="加盟商资金管理-通过id查询", notes="加盟商资金管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AllianceAccountCapital> queryById(@RequestParam(name="id",required=true) String id) {
		Result<AllianceAccountCapital> result = new Result<AllianceAccountCapital>();
		AllianceAccountCapital allianceAccountCapital = allianceAccountCapitalService.getById(id);
		if(allianceAccountCapital==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(allianceAccountCapital);
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
      QueryWrapper<AllianceAccountCapital> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              AllianceAccountCapital allianceAccountCapital = JSON.parseObject(deString, AllianceAccountCapital.class);
              queryWrapper = QueryGenerator.initQueryWrapper(allianceAccountCapital, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<AllianceAccountCapital> pageList = allianceAccountCapitalService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "加盟商资金管理列表");
      mv.addObject(NormalExcelConstants.CLASS, AllianceAccountCapital.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("加盟商资金管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<AllianceAccountCapital> listAllianceAccountCapitals = ExcelImportUtil.importExcel(file.getInputStream(), AllianceAccountCapital.class, params);
              allianceAccountCapitalService.saveBatch(listAllianceAccountCapitals);
              return Result.ok("文件导入成功！数据行数:" + listAllianceAccountCapitals.size());
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
