package org.jeecg.modules.store.controller;

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
import org.jeecg.modules.store.dto.StoreStatementAccountDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreStatementAccount;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreStatementAccountService;
import org.jeecg.modules.store.vo.StoreAccountCapitalVO;
import org.jeecg.modules.store.vo.StoreStatementAccountVO;
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
 * @Description: 店铺对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺对账单")
@RestController
@RequestMapping("/storeStatementAccount/storeStatementAccount")
public class StoreStatementAccountController {
	@Autowired
	private IStoreStatementAccountService storeStatementAccountService;
	@Autowired
	private IStoreAccountCapitalService storeAccountCapitalService;
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
	@AutoLog(value = "店铺对账单-分页列表查询")
	@ApiOperation(value="店铺对账单-分页列表查询", notes="店铺对账单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreStatementAccountVO>> queryPageList(StoreStatementAccountDTO storeStatementAccountDTO,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																HttpServletRequest req) {
		Result<IPage<StoreStatementAccountVO>> result = new Result<IPage<StoreStatementAccountVO>>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> roles = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		if (roles.contains("Merchant")){
			storeStatementAccountDTO.setSysUserId(sysUser.getId());
		}
		Page<StoreStatementAccount> page = new Page<StoreStatementAccount>(pageNo, pageSize);
		IPage<StoreStatementAccountVO> pageList = storeStatementAccountService.queryPageList(page, storeStatementAccountDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeStatementAccount
	 * @return
	 */
	@AutoLog(value = "店铺对账单-添加")
	@ApiOperation(value="店铺对账单-添加", notes="店铺对账单-添加")
	@PostMapping(value = "/add")
	public Result<StoreStatementAccount> add(@RequestBody StoreStatementAccount storeStatementAccount) {
		Result<StoreStatementAccount> result = new Result<StoreStatementAccount>();
		try {
			storeStatementAccountService.save(storeStatementAccount);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeStatementAccount
	 * @return
	 */
	@AutoLog(value = "店铺对账单-编辑")
	@ApiOperation(value="店铺对账单-编辑", notes="店铺对账单-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreStatementAccount> edit(@RequestBody StoreStatementAccount storeStatementAccount) {
		Result<StoreStatementAccount> result = new Result<StoreStatementAccount>();
		StoreStatementAccount storeStatementAccountEntity = storeStatementAccountService.getById(storeStatementAccount.getId());
		if(storeStatementAccountEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeStatementAccountService.updateById(storeStatementAccount);
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
	@AutoLog(value = "店铺对账单-通过id删除")
	@ApiOperation(value="店铺对账单-通过id删除", notes="店铺对账单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeStatementAccountService.removeById(id);
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
	@AutoLog(value = "店铺对账单-批量删除")
	@ApiOperation(value="店铺对账单-批量删除", notes="店铺对账单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreStatementAccount> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreStatementAccount> result = new Result<StoreStatementAccount>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeStatementAccountService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺对账单-通过id查询")
	@ApiOperation(value="店铺对账单-通过id查询", notes="店铺对账单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreStatementAccount> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreStatementAccount> result = new Result<StoreStatementAccount>();
		StoreStatementAccount storeStatementAccount = storeStatementAccountService.getById(id);
		if(storeStatementAccount==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeStatementAccount);
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
      QueryWrapper<StoreStatementAccount> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreStatementAccount storeStatementAccount = JSON.parseObject(deString, StoreStatementAccount.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeStatementAccount, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreStatementAccount> pageList = storeStatementAccountService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺对账单列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreStatementAccount.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺对账单列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreStatementAccount> listStoreStatementAccounts = ExcelImportUtil.importExcel(file.getInputStream(), StoreStatementAccount.class, params);
              storeStatementAccountService.saveBatch(listStoreStatementAccounts);
              return Result.ok("文件导入成功！数据行数:" + listStoreStatementAccounts.size());
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
	 @GetMapping(value = "/getStoreAccountCapitalListList")
	 public Result<IPage<StoreAccountCapitalVO>> getStoreAccountCapitalListList(@RequestParam("id") String id,
																				@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																				@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																				HttpServletRequest req) {
		 Result<IPage<StoreAccountCapitalVO>> result = new Result<IPage<StoreAccountCapitalVO>>();
		 StoreStatementAccount storeStatementAccount = storeStatementAccountService.getById(id);
		 if(oConvertUtils.isEmpty(storeStatementAccount)){
			 result.error500("未找到对应实体");
		 }else{
			 Page<StoreAccountCapital> page = new Page<StoreAccountCapital>(pageNo, pageSize);
			 IPage<StoreAccountCapitalVO> pageList = storeAccountCapitalService.getStoreAccountCapitalListList(page,storeStatementAccount);
			 result.setSuccess(true);
			 result.setResult(pageList);
		 }
		 return result;
	 }
}
