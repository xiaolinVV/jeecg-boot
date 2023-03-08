package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.store.dto.StoreTemplateDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreTemplate;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreTemplateService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.service.ISysAreaService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/*
*
 * @Description: 运费模板
 * @Author: jeecg-boot
 * @Date:   2019-10-21
 * @Version: V1.0
 */

@Slf4j
@Api(tags="运费模板")
@RestController
@RequestMapping("/storeTemplate/storeTemplate")
public class StoreTemplateController {
	@Autowired
	private IStoreTemplateService storeTemplateService;
	@Autowired
	private ISysAreaService iSysAreaService;

	@Autowired
	private IStoreManageService iStoreManageService;




	/**
	 * 根据供应商id获取运费模板
	 *
	 * @param storeManageId
	 * @return
	 */
	@GetMapping("getStoreTemplateByStoreManageId")
	public Result<?> getProviderTemplateBySysUserId(String storeManageId){
		StoreManage storeManage=iStoreManageService.getById(storeManageId);
		return Result.ok(storeTemplateService.list(new LambdaQueryWrapper<StoreTemplate>().eq(StoreTemplate::getSysUserId,storeManage.getSysUserId())));
	}

	 @AutoLog(value = "平台新增运费模板")
	 @ApiOperation(value = "平台新增运费模板", notes = "平台新增运费模板")
	 @RequestMapping(value = "/findListByUserId", method = RequestMethod.GET)
	 public Result<List<StoreTemplateDTO>> findListByUserId(@RequestParam(value = "uId",required = true) String uId) {
		 Result<List<StoreTemplateDTO>> result = new Result<List<StoreTemplateDTO>>();
		 try {
			 List<StoreTemplateDTO> list = storeTemplateService.getlistTemplate(uId);
			 if (list.size() > 0) {
				 SysArea sysArea;
				 List<SysArea> listsysArea;
				 for (StoreTemplateDTO pt : list) {
					 listsysArea = new ArrayList<SysArea>();
					 String etp = pt.getExemptionPostage();
					 String[] strs = etp.split(",");
					 for (int i = 0; i < strs.length; i++) {
						 sysArea = iSysAreaService.getById(strs[i]);
						 if (sysArea != null) {
							 listsysArea.add(sysArea);
						 }
					 }
					 pt.setListsysArea(listsysArea);
				 }
			 }
			 result.setSuccess(true);
			 result.setResult(list);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return result;
	 }
	 @AutoLog(value = "运费模板-店铺新增编辑页面运费模板")
	 @ApiOperation(value = "运费模板-店铺新增编辑页面运费模板(店铺商品)", notes = "运费模板-店铺新增编辑页面运费模板")
	 @RequestMapping(value = "findLIst",method = RequestMethod.GET)
	 public Result<List<StoreTemplateDTO>>findLIst(String uId){
		 Result<List<StoreTemplateDTO>> result = new Result<>();
		 try {


		 	if(StringUtils.isBlank(uId)){
				LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
				uId = sysUser.getId();
			}

			 List<StoreTemplateDTO> list = storeTemplateService.getlistTemplate(uId);
			 if (list.size() > 0) {
				 SysArea sysArea;
				 List<SysArea> listsysArea;
				 for (StoreTemplateDTO pt : list) {
					 listsysArea = new ArrayList<SysArea>();
					 String etp = pt.getExemptionPostage();
					 String[] strs = etp.split(",");
					 for (int i = 0; i < strs.length; i++) {
						 sysArea = iSysAreaService.getById(strs[i]);
						 if (sysArea != null) {
							 listsysArea.add(sysArea);
						 }
					 }
					 pt.setListsysArea(listsysArea);
				 }
			 }
			 result.setSuccess(true);
			 result.setResult(list);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return result;
	 }




/*
*
	  * 分页列表查询
	 * @param storeTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */

	@AutoLog(value = "运费模板-分页列表查询")
	@ApiOperation(value="运费模板-分页列表查询", notes="运费模板-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreTemplate>> queryPageList(StoreTemplate storeTemplate,
													  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													  HttpServletRequest req) {
		Result<IPage<StoreTemplate>> result = new Result<IPage<StoreTemplate>>();
		QueryWrapper<StoreTemplate> queryWrapper = QueryGenerator.initQueryWrapper(storeTemplate, req.getParameterMap());
		PermissionUtils.accredit(queryWrapper);
		Page<StoreTemplate> page = new Page<StoreTemplate>(pageNo, pageSize);
		IPage<StoreTemplate> pageList = storeTemplateService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
/*
*
      * 返回list集合
      * @param storeTemplate
      * @param rep
      * @return
      */

	 @AutoLog(value = "运费模板-查询返回集合")
	 @ApiOperation(value="运费模板-查询返回集合", notes="运费模板-查询返回集合")
	@RequestMapping(value = "/queryList",method = RequestMethod.GET)
	public List<StoreTemplate> queryList(StoreTemplate storeTemplate,
												 HttpServletRequest rep){
		 QueryWrapper<StoreTemplate> queryWrapper = QueryGenerator.initQueryWrapper(storeTemplate, rep.getParameterMap());
		 List<StoreTemplate> list = storeTemplateService.list(queryWrapper);
		 return list;
	}

/*
	
*
	  *   添加
	 * @param storeTemplate
	 * @return
	 */

	@AutoLog(value = "运费模板-添加")
	@ApiOperation(value="运费模板-添加", notes="运费模板-添加")
	@PostMapping(value = "/add")
	public Result<StoreTemplate> add(@RequestBody StoreTemplate storeTemplate) {
		Result<StoreTemplate> result = new Result<StoreTemplate>();
		try {
			storeTemplateService.updateIsTemplateById(storeTemplate);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
/*
*
	  *  编辑
	 * @param storeTemplate
	 * @return
	 */

	@AutoLog(value = "运费模板-编辑")
	@ApiOperation(value="运费模板-编辑", notes="运费模板-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreTemplate> edit(@RequestBody StoreTemplate storeTemplate) {
		Result<StoreTemplate> result = new Result<StoreTemplate>();
		StoreTemplate storeTemplateEntity = storeTemplateService.getById(storeTemplate.getId());
		if(storeTemplateEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean b = storeTemplateService.updateIsTemplateById(storeTemplate);
			//TODO 返回false说明什么？
			if(b) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
/*
*
	  *   通过id删除
	 * @param id
	 * @return
	 */

	@AutoLog(value = "运费模板-通过id删除")
	@ApiOperation(value="运费模板-通过id删除", notes="运费模板-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeTemplateService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
/*
*
	  *  批量删除
	 * @param ids
	 * @return
	 */

	@AutoLog(value = "运费模板-批量删除")
	@ApiOperation(value="运费模板-批量删除", notes="运费模板-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreTemplate> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreTemplate> result = new Result<StoreTemplate>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeTemplateService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	/*
*
	  * 通过id查询
	 * @param id
	 * @return
	 */

	@AutoLog(value = "运费模板-通过id查询")
	@ApiOperation(value="运费模板-通过id查询", notes="运费模板-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreTemplate> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreTemplate> result = new Result<StoreTemplate>();
		StoreTemplate storeTemplate = storeTemplateService.getById(id);
		if(storeTemplate==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeTemplate);
			result.setSuccess(true);
		}
		return result;
	}
/*
*
      * 导出excel
   *
   * @param request
   * @param response
   */

  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<StoreTemplate> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreTemplate storeTemplate = JSON.parseObject(deString, StoreTemplate.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeTemplate, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreTemplate> pageList = storeTemplateService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "运费模板列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreTemplate.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("运费模板列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreTemplate> listStoreTemplates = ExcelImportUtil.importExcel(file.getInputStream(), StoreTemplate.class, params);
              storeTemplateService.saveBatch(listStoreTemplates);
              return Result.ok("文件导入成功！数据行数:" + listStoreTemplates.size());
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
