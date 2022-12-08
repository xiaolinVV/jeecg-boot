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
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.dto.StoreWithdrawDepositDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreWithdrawDeposit;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreWithdrawDepositService;
import org.jeecg.modules.store.vo.StoreWithdrawDepositVO;
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
 * @Description: 店铺提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺提现审批")
@RestController
@RequestMapping("/storeWithdrawDeposit/storeWithdrawDeposit")
public class StoreWithdrawDepositController {
	@Autowired
	private IStoreWithdrawDepositService storeWithdrawDepositService;
	@Autowired
	private IStoreManageService storeManageService;
    @Autowired
	private IStoreAccountCapitalService storeAccountCapitalService;
	/**
	  * 分页列表查询
	 * @param storeWithdrawDepositVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺提现审批-分页列表查询")
	@ApiOperation(value="店铺提现审批-分页列表查询", notes="店铺提现审批-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreWithdrawDepositDTO>> queryPageList(StoreWithdrawDepositVO storeWithdrawDepositVO,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																HttpServletRequest req) {
		Result<IPage<StoreWithdrawDepositDTO>> result = new Result<IPage<StoreWithdrawDepositDTO>>();
		Page<StoreWithdrawDeposit> page = new Page<StoreWithdrawDeposit>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
		List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		if (roleByUserId.contains("Merchant")){
			storeWithdrawDepositVO.setSysUserId(sysUser.getId());
		}else {
			storeWithdrawDepositVO.setSysUserId(null);
		}
		IPage<StoreWithdrawDepositDTO> pageList = storeWithdrawDepositService.getStoreWithdrawDeposit(page, storeWithdrawDepositVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param storeWithdrawDeposit
	 * @return
	 */
	@AutoLog(value = "店铺提现审批-添加")
	@ApiOperation(value="店铺提现审批-添加", notes="店铺提现审批-添加")
	@PostMapping(value = "/add")
	public Result<StoreWithdrawDeposit> add(@RequestBody StoreWithdrawDeposit storeWithdrawDeposit) {
		Result<StoreWithdrawDeposit> result = new Result<StoreWithdrawDeposit>();
		try {
			storeWithdrawDepositService.save(storeWithdrawDeposit);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeWithdrawDeposit
	 * @return
	 */
	@AutoLog(value = "店铺提现审批-编辑")
	@ApiOperation(value="店铺提现审批-编辑", notes="店铺提现审批-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreWithdrawDeposit> edit(@RequestBody StoreWithdrawDeposit storeWithdrawDeposit) {
		Result<StoreWithdrawDeposit> result = new Result<StoreWithdrawDeposit>();
		StoreWithdrawDeposit storeWithdrawDepositEntity = storeWithdrawDepositService.getById(storeWithdrawDeposit.getId());
		if(storeWithdrawDepositEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeWithdrawDepositService.updateById(storeWithdrawDeposit);
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
	@AutoLog(value = "店铺提现审批-通过id删除")
	@ApiOperation(value="店铺提现审批-通过id删除", notes="店铺提现审批-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeWithdrawDepositService.removeById(id);
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
	@AutoLog(value = "店铺提现审批-批量删除")
	@ApiOperation(value="店铺提现审批-批量删除", notes="店铺提现审批-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreWithdrawDeposit> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreWithdrawDeposit> result = new Result<StoreWithdrawDeposit>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeWithdrawDepositService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺提现审批-通过id查询")
	@ApiOperation(value="店铺提现审批-通过id查询", notes="店铺提现审批-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreWithdrawDeposit> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreWithdrawDeposit> result = new Result<StoreWithdrawDeposit>();
		StoreWithdrawDeposit storeWithdrawDeposit = storeWithdrawDepositService.getById(id);
		if(storeWithdrawDeposit==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeWithdrawDeposit);
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
      QueryWrapper<StoreWithdrawDeposit> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreWithdrawDeposit storeWithdrawDeposit = JSON.parseObject(deString, StoreWithdrawDeposit.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeWithdrawDeposit, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreWithdrawDeposit> pageList = storeWithdrawDepositService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺提现审批列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreWithdrawDeposit.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺提现审批列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreWithdrawDeposit> listStoreWithdrawDeposits = ExcelImportUtil.importExcel(file.getInputStream(), StoreWithdrawDeposit.class, params);
              storeWithdrawDepositService.saveBatch(listStoreWithdrawDeposits);
              return Result.ok("文件导入成功！数据行数:" + listStoreWithdrawDeposits.size());
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
	  * 弃用
	  * 通过id查询修改审核状态：
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "会员提现审批-通过id查询")
	 @ApiOperation(value = "会员提现审批-通过id查询", notes = "会员提现审批-通过id查询")
	 @GetMapping(value = "/updateAuditStatus")
	 public Result<StoreWithdrawDeposit> updateAuditStatus(@RequestParam(name = "id", required = true) String id,
															@RequestParam(name = "status") String status,
															@RequestParam(name = "closeExplain") String closeExplain) {
		 Result<StoreWithdrawDeposit> result = new Result<StoreWithdrawDeposit>();
		 StoreWithdrawDeposit storeWithdrawDeposit = storeWithdrawDepositService.getById(id);
		 if (storeWithdrawDeposit == null) {
			 result.error500("未找到对应实体");
		 } else {
			 storeWithdrawDeposit.setStatus(status);
			 storeWithdrawDeposit.setCloseExplain(closeExplain);
			 boolean ok = storeWithdrawDepositService.updateById(storeWithdrawDeposit);
			 //TODO 返回false说明什么？
			 if (ok) {
				 result.success("修改成功!");
			 } else {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }


	 /**
	  * 弃用
	  * 通过id查询修改状态 打款：
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "会员打款-通过id查询")
	 @ApiOperation(value = "会员打款-通过id查询", notes = "会员打款-通过id查询")
	 @GetMapping(value = "/updateStatusRemit")
	 public Result<StoreWithdrawDeposit> updateStatusRemit(@RequestParam(name = "id", required = true) String id,
															@RequestParam(name = "status") String status,
															@RequestParam(name = "remark") String remark) {
		 Result<StoreWithdrawDeposit> result = new Result<StoreWithdrawDeposit>();
		 StoreWithdrawDeposit storeWithdrawDeposit = storeWithdrawDepositService.getById(id);
		 if (storeWithdrawDeposit == null) {
			 result.error500("未找到对应实体");
		 } else {
			 //已付款
			 if(status.equals("2")){
				 //获取会员信息
				 StoreManage storeManage = storeManageService.getById(storeWithdrawDeposit.getStoreManageId());
				 if(storeManage == null){
					 result.error500("用户不存在");
				 }else{
					 //比较大小：
					 // int a = bigdemical.compareTo(bigdemical2)
					 //a = -1,表示bigdemical小于bigdemical2
					 int a = storeManage.getBalance().compareTo(storeWithdrawDeposit.getMoney());
					 if(a == -1){
						 result.error500("提现金额大于，余额!");
					 }else{
						 //修改打款状态
						 storeWithdrawDeposit.setStatus(status);
						 storeWithdrawDeposit.setRemark(remark);
						 boolean ok = storeWithdrawDepositService.updateById(storeWithdrawDeposit);
						 //会员余额减去提现值     减法  bignum3 = bignum1.subtract(bignum2);
						 storeManage.setBalance(storeManage.getBalance().subtract(storeWithdrawDeposit.getMoney()));
						 //会员冻结余额减去提现金额
						 storeManage.setUnusableFrozen(storeManage.getUnusableFrozen().subtract(storeWithdrawDeposit.getMoney()));
						 storeManageService.updateById(storeManage);
						 //生成资金明细数据
						 StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
						 storeAccountCapital.setDelFlag("0");
						 storeAccountCapital.setStoreManageId(storeWithdrawDeposit.getStoreManageId());
						 storeAccountCapital.setPayType("1");
						 storeAccountCapital.setGoAndCome("1");
						 storeAccountCapital.setAmount(storeWithdrawDeposit.getMoney());
						 storeAccountCapital.setOrderNo(storeWithdrawDeposit.getOrderNo());
						 storeAccountCapital.setBalance(storeManage.getBalance());
						 storeAccountCapitalService.save(storeAccountCapital);
						 //TODO 返回false说明什么？
						 if (ok) {
							 result.success("提现成功!");
						 }

					 }
				 }
			 }

		 }
		 return result;
	 }
	 @AutoLog(value = "审批")
	 @ApiOperation(value = "审批", notes = "审批")
	 @PostMapping("/audit")
	 public Result<StoreWithdrawDeposit> audit(@RequestBody StoreWithdrawDepositVO storeWithdrawDepositVO){
	 	return storeWithdrawDepositService.audit(storeWithdrawDepositVO);
	 }
	 @AutoLog(value = "汇款")
	 @ApiOperation(value = "汇款", notes = "汇款")
	 @PostMapping("/remit")
	 public Result<StoreWithdrawDeposit> remit(@RequestBody StoreWithdrawDepositVO storeWithdrawDepositVO){
	 	return storeWithdrawDepositService.remit(storeWithdrawDepositVO);
	 }
 }
