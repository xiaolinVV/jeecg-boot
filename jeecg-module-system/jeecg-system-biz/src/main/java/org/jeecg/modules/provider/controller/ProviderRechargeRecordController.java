package org.jeecg.modules.provider.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.provider.dto.ProviderRechargeRecordDTO;
import org.jeecg.modules.provider.entity.ProviderBankCard;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.entity.ProviderRechargeRecord;
import org.jeecg.modules.provider.service.IProviderBankCardService;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.service.IProviderRechargeRecordService;
import org.jeecg.modules.provider.vo.ProviderRechargeRecordVO;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 供应商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="供应商余额明细")
@RestController
@RequestMapping("/providerRechargeRecord/providerRechargeRecord")
public class ProviderRechargeRecordController {
	@Autowired
	private IProviderRechargeRecordService providerRechargeRecordService;
	@Autowired
	private IProviderManageService providerManageService;
    @Autowired
	private IProviderBankCardService providerBankCardService;
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
	@AutoLog(value = "供应商余额明细-分页列表查询")
	@ApiOperation(value="供应商余额明细-分页列表查询", notes="供应商余额明细-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProviderRechargeRecordVO>> queryPageList(ProviderRechargeRecordDTO providerRechargeRecordDTO,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<ProviderRechargeRecordVO>> result = new Result<IPage<ProviderRechargeRecordVO>>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		if (roleByUserId.contains("Supplier")){
			providerRechargeRecordDTO.setSysUserId(sysUser.getId());
		}
		Page<ProviderRechargeRecord> page = new Page<ProviderRechargeRecord>(pageNo, pageSize);

		IPage<ProviderRechargeRecordVO> pageList = providerRechargeRecordService.queryPageList(page,providerRechargeRecordDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 @AutoLog(value = "供应商余额记录-提现记录")
	 @ApiOperation(value="供应商余额记录-提现记录", notes="供应商余额记录-提现记录")
	 @GetMapping(value = "/findPayRecord")
	 public Result<IPage<ProviderRechargeRecordDTO>> findPayRecord(ProviderRechargeRecordVO providerRechargeRecordVO,
																   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																   HttpServletRequest req) {
		 Result<IPage<ProviderRechargeRecordDTO>> result = new Result<IPage<ProviderRechargeRecordDTO>>();
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();
		 if(str!=null){
			 providerRechargeRecordVO.setSysUserId(str);
		 }
		 providerRechargeRecordVO.setPayType("1");
		 Page<ProviderRechargeRecord> page = new Page<ProviderRechargeRecord>(pageNo, pageSize);
		 IPage<ProviderRechargeRecordDTO> pageList = providerRechargeRecordService.getProviderRechargeRecord(page,providerRechargeRecordVO);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }


	/**
	  *   添加
	 * @param providerRechargeRecord
	 * @return
	 */
	@AutoLog(value = "供应商余额明细-添加")
	@ApiOperation(value="供应商余额明细-添加", notes="供应商余额明细-添加")
	@PostMapping(value = "/add")
	public Result<ProviderRechargeRecord> add(@RequestBody ProviderRechargeRecord providerRechargeRecord) {
		Result<ProviderRechargeRecord> result = new Result<ProviderRechargeRecord>();
		try {
			providerRechargeRecordService.save(providerRechargeRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param providerRechargeRecord
	 * @return
	 */
	@AutoLog(value = "供应商余额明细-编辑")
	@ApiOperation(value="供应商余额明细-编辑", notes="供应商余额明细-编辑")
	@PutMapping(value = "/edit")
	public Result<ProviderRechargeRecord> edit(@RequestBody ProviderRechargeRecord providerRechargeRecord) {
		Result<ProviderRechargeRecord> result = new Result<ProviderRechargeRecord>();
		ProviderRechargeRecord providerRechargeRecordEntity = providerRechargeRecordService.getById(providerRechargeRecord.getId());
		if(providerRechargeRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = providerRechargeRecordService.updateById(providerRechargeRecord);
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
	@AutoLog(value = "供应商余额明细-通过id删除")
	@ApiOperation(value="供应商余额明细-通过id删除", notes="供应商余额明细-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			providerRechargeRecordService.removeById(id);
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
	@AutoLog(value = "供应商余额明细-批量删除")
	@ApiOperation(value="供应商余额明细-批量删除", notes="供应商余额明细-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<ProviderRechargeRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<ProviderRechargeRecord> result = new Result<ProviderRechargeRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.providerRechargeRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商余额明细-通过id查询")
	@ApiOperation(value="供应商余额明细-通过id查询", notes="供应商余额明细-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProviderRechargeRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<ProviderRechargeRecord> result = new Result<ProviderRechargeRecord>();
		ProviderRechargeRecord providerRechargeRecord = providerRechargeRecordService.getById(id);
		if(providerRechargeRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(providerRechargeRecord);
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
      QueryWrapper<ProviderRechargeRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              ProviderRechargeRecord providerRechargeRecord = JSON.parseObject(deString, ProviderRechargeRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(providerRechargeRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<ProviderRechargeRecord> pageList = providerRechargeRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "供应商余额明细列表");
      mv.addObject(NormalExcelConstants.CLASS, ProviderRechargeRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商余额明细列表数据", "导出人:Jeecg", "导出信息"));
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
              List<ProviderRechargeRecord> listProviderRechargeRecords = ExcelImportUtil.importExcel(file.getInputStream(), ProviderRechargeRecord.class, params);
              providerRechargeRecordService.saveBatch(listProviderRechargeRecords);
              return Result.ok("文件导入成功！数据行数:" + listProviderRechargeRecords.size());
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
	  * 获取当前登录用户的供应商信息
	  * @param providerRechargeRecord
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "供应商余额记录-分页列表查询")
	 @ApiOperation(value="供应商余额记录-分页列表查询", notes="供应商余额记录-分页列表查询")
	 @GetMapping(value = "/queryPageListAndManage")
	 public Result<Map<String ,Object>> queryPageListAndManage(ProviderRechargeRecord providerRechargeRecord, HttpServletRequest req) {
		 Result<Map<String ,Object> > result = new Result< Map<String ,Object>>();
		 QueryWrapper<ProviderManage> queryWrapperStoreManage = new QueryWrapper();
		 // LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 Map<String ,Object> map = Maps.newHashMap();
		 Map<String ,Object> objectMap = Maps.newHashMap();
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();//"f56d2945151165b38bf0151e1b09b8ab";//PermissionUtils.ifPlatform();
		 if(str!=null){
			 queryWrapperStoreManage.eq("sys_user_id",str);//str  "f56d2945151165b38bf0151e1b09b8ab"
			 ProviderManage providerManage = providerManageService.getOne(queryWrapperStoreManage);
             if(oConvertUtils.isNotEmpty(providerManage)){
				 map.put("id",providerManage.getId());
				 map.put("balance",providerManage.getBalance());
				 map.put("accountFrozen",providerManage.getAccountFrozen());
				 map.put("unusableFrozen",providerManage.getUnusableFrozen());
			 }

		 }else{
			 //平台登录
			 BigDecimal balance = new  BigDecimal(0);
			 BigDecimal accountFrozen = new  BigDecimal(0);
			 BigDecimal unusableFrozen = new  BigDecimal(0);

			 QueryWrapper<ProviderManage> queryWrapperProviderManageList  = new QueryWrapper();
			 queryWrapperProviderManageList.eq("status","1") ;
			 List<ProviderManage> providerManageList =  providerManageService.list(queryWrapperProviderManageList);
			 for(ProviderManage sm: providerManageList){
				 balance = balance.add(sm.getBalance());
				 accountFrozen = accountFrozen.add(sm.getAccountFrozen());
				 unusableFrozen = unusableFrozen.add(sm.getUnusableFrozen());
			 }
			 map.put("id","");
			 map.put("balance",balance);
			 map.put("accountFrozen",accountFrozen);
			 map.put("unusableFrozen",unusableFrozen);
		 }
		 //objectMap.put("storeManage",map);
		 result.setResult(map);
		 result.setSuccess(true);

		 return result;
	 }



	 /**
	  *   提现 记录 添加
	  * @param providerRechargeRecord
	  * @return
	  */
	 @AutoLog(value = "供应商余额记录-添加")
	 @ApiOperation(value="供应商余额记录-添加", notes="店铺余额记录-添加")
	 @PostMapping(value = "/addStoreRechargeRecord")
	 public Result<ProviderRechargeRecord> addStoreRechargeRecord(@RequestBody ProviderRechargeRecord providerRechargeRecord,HttpServletRequest req) {
		 Result<ProviderRechargeRecord> result = new Result<ProviderRechargeRecord>();
		 try {

			 if(StringUtils.isBlank(providerRechargeRecord.getSysUserId())){
				 result.error500("未找到实体");
				 return result;
			 }else{
				 //供应商信息
				 QueryWrapper<ProviderManage> queryWrapperProviderManage = new QueryWrapper();
				 queryWrapperProviderManage.eq("sys_user_id",providerRechargeRecord.getSysUserId());
				 ProviderManage providerManage = providerManageService.getOne(queryWrapperProviderManage);

				 if(providerManage == null){
					 result.error500("未找到实体");
				 }else{
					 //判断提现金额
					 int a = providerManage.getBalance().compareTo(providerRechargeRecord.getAmount());
					 if(a == -1){
						 result.error500("提现金额大于，余额!");
					 }else{
						 QueryWrapper<ProviderBankCard> queryWrapper = new QueryWrapper();
						 queryWrapper.eq("sys_user_id",providerRechargeRecord.getSysUserId());
						 //店铺银行
						 ProviderBankCard  providerBankCard = providerBankCardService.getOne(queryWrapper);
						 if(providerBankCard == null){
							 result.error500("请先去设置您的银行卡信息");
						 }else{

							 providerRechargeRecord.setProviderBankCardId(providerBankCard.getId());
							 //可以提现
							 providerRechargeRecord.setDelFlag("0");
							 providerRechargeRecord.setPayType("1");
							 providerRechargeRecord.setGoAndCome("1");
							 providerRechargeRecord.setTradeStatus("1");
							 //生成流水号
							 String orderNo = OrderNoUtils.getOrderNo();
							 providerRechargeRecord.setOrderNo(orderNo);
							 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
							 if(sysUser!=null){
								 providerRechargeRecord.setOperator(sysUser.getUsername());
							 }
							 providerRechargeRecordService.save(providerRechargeRecord);
							 //生成提现订单，减去
							 providerManage.setBalance(providerManage.getBalance().subtract(providerRechargeRecord.getAmount()));
							 providerManage.setAccountFrozen(providerManage.getAccountFrozen().add(providerRechargeRecord.getAmount()));
							 providerManageService.updateById(providerManage);
							 result.success("添加成功！");
						 }
					 }
				 }
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }



}
