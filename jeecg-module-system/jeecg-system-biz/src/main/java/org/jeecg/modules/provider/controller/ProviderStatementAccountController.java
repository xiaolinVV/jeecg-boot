package org.jeecg.modules.provider.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.provider.dto.ProviderStatementAccountDTO;
import org.jeecg.modules.provider.entity.ProviderAccountCapital;
import org.jeecg.modules.provider.entity.ProviderStatementAccount;
import org.jeecg.modules.provider.service.IProviderAccountCapitalService;
import org.jeecg.modules.provider.service.IProviderStatementAccountService;
import org.jeecg.modules.provider.vo.ProviderAccountCapitalVO;
import org.jeecg.modules.provider.vo.ProviderStatementAccountVO;
import org.jeecg.modules.system.service.ISysUserRoleService;
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
 * @Description: 供应商对账单
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="供应商对账单")
@RestController
@RequestMapping("/providerStatementAccount/providerStatementAccount")
public class ProviderStatementAccountController {
	@Autowired
	private IProviderStatementAccountService providerStatementAccountService;
	@Autowired
	private IProviderAccountCapitalService providerAccountCapitalService;
	@Autowired
	private ISysUserRoleService iSysUserRoleService;
	/**
	  * 分页列表查询
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "供应商对账单-分页列表查询")
	@ApiOperation(value="供应商对账单-分页列表查询", notes="供应商对账单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProviderStatementAccountVO>> queryPageList(ProviderStatementAccountDTO providerStatementAccountDTO,
																   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																   HttpServletRequest req) {
		Result<IPage<ProviderStatementAccountVO>> result = new Result<IPage<ProviderStatementAccountVO>>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		if (roleByUserId.contains("Supplier")){
			providerStatementAccountDTO.setSysUserId(sysUser.getId());
		}
		Page<ProviderStatementAccount> page = new Page<ProviderStatementAccount>(pageNo, pageSize);
		IPage<ProviderStatementAccountVO> pageList = providerStatementAccountService.queryPageList(page, providerStatementAccountDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param providerStatementAccount
	 * @return
	 */
	@AutoLog(value = "供应商对账单-添加")
	@ApiOperation(value="供应商对账单-添加", notes="供应商对账单-添加")
	@PostMapping(value = "/add")
	public Result<ProviderStatementAccount> add(@RequestBody ProviderStatementAccount providerStatementAccount) {
		Result<ProviderStatementAccount> result = new Result<ProviderStatementAccount>();
		try {
			providerStatementAccountService.save(providerStatementAccount);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param providerStatementAccount
	 * @return
	 */
	@AutoLog(value = "供应商对账单-编辑")
	@ApiOperation(value="供应商对账单-编辑", notes="供应商对账单-编辑")
	@PutMapping(value = "/edit")
	public Result<ProviderStatementAccount> edit(@RequestBody ProviderStatementAccount providerStatementAccount) {
		Result<ProviderStatementAccount> result = new Result<ProviderStatementAccount>();
		ProviderStatementAccount providerStatementAccountEntity = providerStatementAccountService.getById(providerStatementAccount.getId());
		if(providerStatementAccountEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = providerStatementAccountService.updateById(providerStatementAccount);
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
	@AutoLog(value = "供应商对账单-通过id删除")
	@ApiOperation(value="供应商对账单-通过id删除", notes="供应商对账单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			providerStatementAccountService.removeById(id);
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
	@AutoLog(value = "供应商对账单-批量删除")
	@ApiOperation(value="供应商对账单-批量删除", notes="供应商对账单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<ProviderStatementAccount> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<ProviderStatementAccount> result = new Result<ProviderStatementAccount>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.providerStatementAccountService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商对账单-通过id查询")
	@ApiOperation(value="供应商对账单-通过id查询", notes="供应商对账单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProviderStatementAccount> queryById(@RequestParam(name="id",required=true) String id) {
		Result<ProviderStatementAccount> result = new Result<ProviderStatementAccount>();
		ProviderStatementAccount providerStatementAccount = providerStatementAccountService.getById(id);
		if(providerStatementAccount==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(providerStatementAccount);
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
      QueryWrapper<ProviderStatementAccount> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              ProviderStatementAccount providerStatementAccount = JSON.parseObject(deString, ProviderStatementAccount.class);
              queryWrapper = QueryGenerator.initQueryWrapper(providerStatementAccount, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<ProviderStatementAccount> pageList = providerStatementAccountService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "供应商对账单列表");
      mv.addObject(NormalExcelConstants.CLASS, ProviderStatementAccount.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商对账单列表数据", "导出人:Jeecg", "导出信息"));
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
              List<ProviderStatementAccount> listProviderStatementAccounts = ExcelImportUtil.importExcel(file.getInputStream(), ProviderStatementAccount.class, params);
              providerStatementAccountService.saveBatch(listProviderStatementAccounts);
              return Result.ok("文件导入成功！数据行数:" + listProviderStatementAccounts.size());
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
	  * 分页列表查询
	  * @param id
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "对账单查看详情数据-分页列表查询")
	 @ApiOperation(value="对账单查看详情数据-分页列表查询", notes="对账单查看详情数据-分页列表查询")
	 @GetMapping(value = "/getProviderAccountCapitalListList")
	 public Result<IPage<ProviderAccountCapitalVO>> getProviderAccountCapitalListList(@RequestParam("id") String id,
																					  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																					  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																					  HttpServletRequest req) {
		 Result<IPage<ProviderAccountCapitalVO>> result = new Result<IPage<ProviderAccountCapitalVO>>();
		 ProviderStatementAccount providerStatementAccount = providerStatementAccountService.getById(id);
		 if(providerStatementAccount == null){
			 result.error500("未找到对应实体");
		 }else{
			 Page<ProviderAccountCapital> page = new Page<ProviderAccountCapital>(pageNo, pageSize);
			 IPage<ProviderAccountCapitalVO> pageList =  providerAccountCapitalService.getProviderAccountCapitalListList(page, providerStatementAccount);
			 result.setSuccess(true);
			 result.setResult(pageList);
		 }
		 return result;
	 }




}
