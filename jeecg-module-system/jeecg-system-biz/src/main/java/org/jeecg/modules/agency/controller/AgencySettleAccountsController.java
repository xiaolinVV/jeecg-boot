package org.jeecg.modules.agency.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.agency.dto.AgencySettleAccountsDTO;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.entity.AgencySettleAccounts;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.service.IAgencySettleAccountsService;
import org.jeecg.modules.agency.vo.AgencySettleAccountsVO;
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
 * @Description: 代理提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="代理提现审批")
@RestController
@RequestMapping("/agencySettleAccounts/agencySettleAccounts")
public class AgencySettleAccountsController {
	@Autowired
	private IAgencySettleAccountsService agencySettleAccountsService;
	@Autowired
	private IAgencyManageService agencyManageService;
	@Autowired
	private IAgencyAccountCapitalService agencyAccountCapitalService;
	
	/**
	  * 分页列表查询
	 * @param agencySettleAccountsVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "代理提现审批-分页列表查询")
	@ApiOperation(value="代理提现审批-分页列表查询", notes="代理提现审批-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AgencySettleAccountsDTO>> queryPageList(AgencySettleAccountsVO agencySettleAccountsVO,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																HttpServletRequest req) {
		Result<IPage<AgencySettleAccountsDTO>> result = new Result<IPage<AgencySettleAccountsDTO>>();
		Page<AgencySettleAccounts> page = new Page<AgencySettleAccounts>(pageNo, pageSize);
		//省市县权限添加权限
		String str= PermissionUtils.ifPlatformArea();
		if(str!=null){
			agencySettleAccountsVO.setSysUserId(str);
		}
		IPage<AgencySettleAccountsDTO> pageList = agencySettleAccountsService.getAgencySettleAccountsList(page, agencySettleAccountsVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param agencySettleAccounts
	 * @return
	 */
	@AutoLog(value = "代理提现审批-添加")
	@ApiOperation(value="代理提现审批-添加", notes="代理提现审批-添加")
	@PostMapping(value = "/add")
	public Result<AgencySettleAccounts> add(@RequestBody AgencySettleAccounts agencySettleAccounts) {
		Result<AgencySettleAccounts> result = new Result<AgencySettleAccounts>();
		try {
			agencySettleAccountsService.save(agencySettleAccounts);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param agencySettleAccounts
	 * @return
	 */
	@AutoLog(value = "代理提现审批-编辑")
	@ApiOperation(value="代理提现审批-编辑", notes="代理提现审批-编辑")
	@PutMapping(value = "/edit")
	public Result<AgencySettleAccounts> edit(@RequestBody AgencySettleAccounts agencySettleAccounts) {
		Result<AgencySettleAccounts> result = new Result<AgencySettleAccounts>();
		AgencySettleAccounts agencySettleAccountsEntity = agencySettleAccountsService.getById(agencySettleAccounts.getId());
		if(agencySettleAccountsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = agencySettleAccountsService.updateById(agencySettleAccounts);
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
	@AutoLog(value = "代理提现审批-通过id删除")
	@ApiOperation(value="代理提现审批-通过id删除", notes="代理提现审批-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			agencySettleAccountsService.removeById(id);
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
	@AutoLog(value = "代理提现审批-批量删除")
	@ApiOperation(value="代理提现审批-批量删除", notes="代理提现审批-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<AgencySettleAccounts> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<AgencySettleAccounts> result = new Result<AgencySettleAccounts>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.agencySettleAccountsService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "代理提现审批-通过id查询")
	@ApiOperation(value="代理提现审批-通过id查询", notes="代理提现审批-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AgencySettleAccounts> queryById(@RequestParam(name="id",required=true) String id) {
		Result<AgencySettleAccounts> result = new Result<AgencySettleAccounts>();
		AgencySettleAccounts agencySettleAccounts = agencySettleAccountsService.getById(id);
		if(agencySettleAccounts==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(agencySettleAccounts);
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
      QueryWrapper<AgencySettleAccounts> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              AgencySettleAccounts agencySettleAccounts = JSON.parseObject(deString, AgencySettleAccounts.class);
              queryWrapper = QueryGenerator.initQueryWrapper(agencySettleAccounts, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<AgencySettleAccounts> pageList = agencySettleAccountsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "代理提现审批列表");
      mv.addObject(NormalExcelConstants.CLASS, AgencySettleAccounts.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("代理提现审批列表数据", "导出人:Jeecg", "导出信息"));
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
              List<AgencySettleAccounts> listAgencySettleAccountss = ExcelImportUtil.importExcel(file.getInputStream(), AgencySettleAccounts.class, params);
              agencySettleAccountsService.saveBatch(listAgencySettleAccountss);
              return Result.ok("文件导入成功！数据行数:" + listAgencySettleAccountss.size());
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
	  * 通过id查询修改审核状态：弃用
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "会员提现审批-通过id查询")
	 @ApiOperation(value = "会员提现审批-通过id查询", notes = "会员提现审批-通过id查询")
	 @GetMapping(value = "/updateAuditStatus")
	 public Result<AgencySettleAccounts> updateAuditStatus(@RequestParam(name = "id", required = true) String id,
														   @RequestParam(name = "status") String status,
														   @RequestParam(name = "closeExplain") String closeExplain) {
		 Result<AgencySettleAccounts> result = new Result<AgencySettleAccounts>();
		 AgencySettleAccounts agencySettleAccounts = agencySettleAccountsService.getById(id);
		 if (agencySettleAccounts == null) {
			 result.error500("未找到对应实体");
		 } else {
			 agencySettleAccounts.setStatus(status);
			 agencySettleAccounts.setCloseExplain(closeExplain);
			 boolean ok = agencySettleAccountsService.updateById(agencySettleAccounts);
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
	  * 通过id查询修改状态 打款： 弃用
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "会员打款-通过id查询")
	 @ApiOperation(value = "会员打款-通过id查询", notes = "会员打款-通过id查询")
	 @GetMapping(value = "/updateStatusRemit")
	 public Result<AgencySettleAccounts> updateStatusRemit(@RequestParam(name = "id", required = true) String id,
														   @RequestParam(name = "status") String status,
														   @RequestParam(name = "remark") String remark) {
		 Result<AgencySettleAccounts> result = new Result<AgencySettleAccounts>();
		 AgencySettleAccounts agencySettleAccounts = agencySettleAccountsService.getById(id);
		 if (agencySettleAccounts == null) {
			 result.error500("未找到对应实体");
		 } else {
			 //已付款
			 if(status.equals("2")){
				 //获取代理信息
				 QueryWrapper<AgencyManage> queryWrapper = new QueryWrapper<>();
				 queryWrapper.eq("sys_user_id",agencySettleAccounts.getSysUserId());
				 AgencyManage agencyManage = agencyManageService.getOne(queryWrapper);
				 if(agencyManage == null){
					 result.error500("用户不存在");
				 }else{
					 //比较大小：
					 // int a = bigdemical.compareTo(bigdemical2)
					 //a = -1,表示bigdemical小于bigdemical2
					 int a = agencyManage.getBalance().compareTo(agencySettleAccounts.getMoney());
					 if(a == -1){
						 result.error500("提现金额大于，余额!");
					 }else{
						 //修改打款状态
						 agencySettleAccounts.setStatus(status);
						 agencySettleAccounts.setRemark(remark);
						 boolean ok = agencySettleAccountsService.updateById(agencySettleAccounts);
						 //代理余额减去提现值     减法  bignum3 = bignum1.subtract(bignum2);
						 agencyManage.setBalance(agencyManage.getBalance().subtract(agencySettleAccounts.getMoney()));
						 //代理冻结余额减去提现金额
						 agencyManage.setAccountFrozen(agencyManage.getAccountFrozen().subtract(agencySettleAccounts.getMoney()));

						 agencyManageService.updateById(agencyManage);
						 //生成资金明细数据
						 AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
						 agencyAccountCapital.setDelFlag("0");
						 agencyAccountCapital.setSysUserId(agencyAccountCapital.getSysUserId());
						 agencyAccountCapital.setPayType("1");
						 agencyAccountCapital.setGoAndCome("1");
						 agencyAccountCapital.setAmount(agencySettleAccounts.getMoney());
						 agencyAccountCapital.setOrderNo(agencySettleAccounts.getOrderNo());
						 agencyAccountCapital.setBalance(agencyManage.getBalance());
						 agencyAccountCapitalService.save(agencyAccountCapital);
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
	 @AutoLog(value = "审核")
	 @ApiOperation(value = "审核", notes = "审核")
	 @PostMapping(value = "/audit")
	 public Result<AgencySettleAccounts> audit(@RequestBody AgencySettleAccountsVO agencySettleAccountsVO){
	 	return agencySettleAccountsService.audit(agencySettleAccountsVO);
	 }
	 @AutoLog(value = "打款")
	 @ApiOperation(value = "打款", notes = "打款")
	 @PostMapping(value = "/remit")
	 public Result<AgencySettleAccounts> remit(@RequestBody AgencySettleAccountsVO agencySettleAccountsVO){
		 return agencySettleAccountsService.remit(agencySettleAccountsVO);
	 }
}
