package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.store.dto.StoreRechargeRecordDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreBankCardService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.store.vo.StoreRechargeRecordVO;
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
 * @Description: 店铺余额记录
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺余额记录")
@RestController
@RequestMapping("/storeRechargeRecord/storeRechargeRecord")
public class StoreRechargeRecordController {
	@Autowired
	private IStoreRechargeRecordService storeRechargeRecordService;
	@Autowired
	private IStoreManageService storeManageService;
	@Autowired
	private IStoreBankCardService storeBankCardService;
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
	@AutoLog(value = "店铺余额记录-分页列表查询")
	@ApiOperation(value="店铺余额记录-分页列表查询", notes="店铺余额记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreRechargeRecordVO>> queryPageList(StoreRechargeRecordDTO storeRechargeRecordDTO,
															@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															HttpServletRequest req) {
		Result<IPage<StoreRechargeRecordVO>> result = new Result<IPage<StoreRechargeRecordVO>>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> roles = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		if (roles.contains("Merchant")){
			storeRechargeRecordDTO.setSysUserId(sysUser.getId());
		}
		Page<StoreRechargeRecord> page = new Page<StoreRechargeRecord>(pageNo, pageSize);
		IPage<StoreRechargeRecordVO> storeRechargeRecordVOIPage = storeRechargeRecordService.queryPageList(page, storeRechargeRecordDTO);
		result.setSuccess(true);
		result.setResult(storeRechargeRecordVOIPage);
		return result;
	}

	 @AutoLog(value = "店铺余额记录-充值记录")
	 @ApiOperation(value="店铺余额记录-充值记录", notes="店铺余额记录-充值记录")
	 @GetMapping(value = "/findRechargeRecord")
	 public Result<IPage<StoreRechargeRecordVO>> findRechargeRecord(StoreRechargeRecordDTO storeRechargeRecordDTO,
															 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															 HttpServletRequest req) {
		 Result<IPage<StoreRechargeRecordVO>> result = new Result<IPage<StoreRechargeRecordVO>>();
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 List<String> roles = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		 if (roles.contains("Merchant")){
			 storeRechargeRecordDTO.setSysUserId(sysUser.getId());
		 }
		 Page<StoreRechargeRecord> page = new Page<StoreRechargeRecord>(pageNo, pageSize);
		 IPage<StoreRechargeRecordVO> pageList = storeRechargeRecordService.findRechargeRecord(page,storeRechargeRecordDTO);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	 @AutoLog(value = "店铺余额记录-提现记录")
	 @ApiOperation(value="店铺余额记录-提现记录", notes="店铺余额记录-提现记录")
	 @GetMapping(value = "/findPayRecord")
	 public Result<IPage<StoreRechargeRecordDTO>> findPayRecord(StoreRechargeRecordVO storeRechargeRecordVO,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																HttpServletRequest req) {
		 Result<IPage<StoreRechargeRecordDTO>> result = new Result<IPage<StoreRechargeRecordDTO>>();
		 String str = PermissionUtils.ifPlatform();
		 if(str!=null){
			 storeRechargeRecordVO.setSysUserId(str);
		 }
		 storeRechargeRecordVO.setPayType("1");
		 Page<StoreRechargeRecord> page = new Page<StoreRechargeRecord>(pageNo, pageSize);
		 IPage<StoreRechargeRecordDTO> pageList = storeRechargeRecordService.getStoreRechargeRecord(page,storeRechargeRecordVO);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	/**
	  *   添加
	 * @param storeRechargeRecord
	 * @return
	 */
	@AutoLog(value = "店铺余额记录-添加")
	@ApiOperation(value="店铺余额记录-添加", notes="店铺余额记录-添加")
	@PostMapping(value = "/add")
	public Result<StoreRechargeRecord> add(@RequestBody StoreRechargeRecord storeRechargeRecord) {
		Result<StoreRechargeRecord> result = new Result<StoreRechargeRecord>();
		try {
			storeRechargeRecordService.save(storeRechargeRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeRechargeRecord
	 * @return
	 */
	@AutoLog(value = "店铺余额记录-编辑")
	@ApiOperation(value="店铺余额记录-编辑", notes="店铺余额记录-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreRechargeRecord> edit(@RequestBody StoreRechargeRecord storeRechargeRecord) {
		Result<StoreRechargeRecord> result = new Result<StoreRechargeRecord>();
		StoreRechargeRecord storeRechargeRecordEntity = storeRechargeRecordService.getById(storeRechargeRecord.getId());
		if(storeRechargeRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeRechargeRecordService.updateById(storeRechargeRecord);
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
	@AutoLog(value = "店铺余额记录-通过id删除")
	@ApiOperation(value="店铺余额记录-通过id删除", notes="店铺余额记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeRechargeRecordService.removeById(id);
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
	@AutoLog(value = "店铺余额记录-批量删除")
	@ApiOperation(value="店铺余额记录-批量删除", notes="店铺余额记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreRechargeRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreRechargeRecord> result = new Result<StoreRechargeRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeRechargeRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺余额记录-通过id查询")
	@ApiOperation(value="店铺余额记录-通过id查询", notes="店铺余额记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreRechargeRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreRechargeRecord> result = new Result<StoreRechargeRecord>();
		StoreRechargeRecord storeRechargeRecord = storeRechargeRecordService.getById(id);
		if(storeRechargeRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeRechargeRecord);
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
      QueryWrapper<StoreRechargeRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreRechargeRecord storeRechargeRecord = JSON.parseObject(deString, StoreRechargeRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeRechargeRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }
      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreRechargeRecord> pageList = storeRechargeRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺余额记录列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreRechargeRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺余额记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreRechargeRecord> listStoreRechargeRecords = ExcelImportUtil.importExcel(file.getInputStream(), StoreRechargeRecord.class, params);
              storeRechargeRecordService.saveBatch(listStoreRechargeRecords);
              return Result.ok("文件导入成功！数据行数:" + listStoreRechargeRecords.size());
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
	  * 获取当前登录用户的店铺信息
	  * @param storeRechargeRecord
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "店铺余额记录-分页列表查询")
	 @ApiOperation(value="店铺余额记录-分页列表查询", notes="店铺余额记录-分页列表查询")
	 @GetMapping(value = "/queryPageListAndManage")
	 public Result<Map<String ,Object>> queryPageListAndManage(StoreRechargeRecord storeRechargeRecord, HttpServletRequest req) {
		 Result<Map<String ,Object> > result = new Result< Map<String ,Object>>();
		 QueryWrapper<StoreManage> queryWrapperStoreManage = new QueryWrapper();

		 Map<String ,Object> map = Maps.newHashMap();

		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();
		 if(str!=null){
			 queryWrapperStoreManage.eq("sys_user_id",str);
			 StoreManage storeManage = storeManageService.getOne(queryWrapperStoreManage);
			 map.put("id",storeManage.getId());
			 map.put("balance",storeManage.getBalance());
			 map.put("accountFrozen",storeManage.getAccountFrozen());
			 map.put("unusableFrozen",storeManage.getUnusableFrozen());
		 }else{
		 	//平台登录
			 List<StoreManage> storeManageList =  storeManageService.list(new LambdaQueryWrapper<StoreManage>()
					 .eq(StoreManage::getStatus,"1")
					 .in(StoreManage::getAttestationStatus,"1","2")
					 .in(StoreManage::getPayStatus,"1","2"));

			 map.put("id","");
			 map.put("balance",storeManageList.stream()
					 .map(StoreManage::getBalance)
					 .reduce(BigDecimal.ZERO,BigDecimal::add));
			 map.put("accountFrozen",storeManageList.stream()
					 .map(StoreManage::getAccountFrozen)
					 .reduce(BigDecimal.ZERO,BigDecimal::add));
			 map.put("unusableFrozen",storeManageList.stream()
					 .map(StoreManage::getUnusableFrozen)
					 .reduce(BigDecimal.ZERO,BigDecimal::add));
		 }
		 result.setResult(map);
		 result.setSuccess(true);
		 return result;
	 }

	 /**
	  *   提现 记录 添加
	  * @param storeRechargeRecord
	  * @return
	  */
	 @AutoLog(value = "店铺余额记录-添加")
	 @ApiOperation(value="店铺余额记录-添加", notes="店铺余额记录-添加")
	 @PostMapping(value = "/addStoreRechargeRecord")
	 public Result<StoreRechargeRecord> addStoreRechargeRecord(@RequestBody StoreRechargeRecord storeRechargeRecord,HttpServletRequest req) {
		 Result<StoreRechargeRecord> result = new Result<StoreRechargeRecord>();
		 try {

			if(StringUtils.isBlank(storeRechargeRecord.getStoreManageId())){
				result.error500("未找到实体");
				return result;
			}else{
				//店铺信息
				StoreManage storeManage = storeManageService.getById(storeRechargeRecord.getStoreManageId());
				QueryWrapper<StoreBankCard> queryWrapper = new QueryWrapper();
				queryWrapper.eq("store_manage_id",storeRechargeRecord.getStoreManageId());
				if(storeManage == null){
					result.error500("未找到实体");
				}else{
					//判断提现金额
					int a = storeManage.getBalance().compareTo(storeRechargeRecord.getAmount());
					if(a == -1){
						result.error500("提现金额大于，余额!");
					}else{
						//店铺银行
						StoreBankCard  storeBankCard = storeBankCardService.getOne(queryWrapper);
						if(storeBankCard == null){
							result.error500("请先去设置您的银行卡信息");
						}else{

							storeRechargeRecord.setStoreBankCardId(storeBankCard.getId());
						//可以提现
							storeRechargeRecord.setDelFlag("0");
							storeRechargeRecord.setPayType("1");
							storeRechargeRecord.setGoAndCome("1");
							storeRechargeRecord.setTradeStatus("1");
							//生成流水号
							String orderNo = OrderNoUtils.getOrderNo();
							storeRechargeRecord.setOrderNo(orderNo);
							LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
							if(sysUser!=null){
								storeRechargeRecord.setOperator(sysUser.getUsername());
							}
							storeRechargeRecordService.save(storeRechargeRecord);
                            //生成提现订单，减去
						storeManage.setBalance(storeManage.getBalance().subtract(storeRechargeRecord.getAmount()));
						//storeManage.setAccountFrozen(storeManage.getAccountFrozen().add(storeRechargeRecord.getAmount()));
						//提现金额 记录在不可用金额里
						storeManage.setUnusableFrozen(storeManage.getUnusableFrozen().add(storeRechargeRecord.getAmount()));
						storeManageService.updateById(storeManage);
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

	 /**
	  * 充值回调
	  * @param oldBalance
	  * @param storeManageId
	  * @param amount
	  * @return
	  */
	 @GetMapping(value = "/prepaidPhoneCallback")
	 @ResponseBody
	 public Result<String>  prepaidPhoneCallback(BigDecimal oldBalance,String storeManageId, BigDecimal amount){
		   Result<String> result = new Result<String>();
           if(StringUtils.isBlank(storeManageId)){
			   result.error500("店铺Id不能为空");
			   return result;
		   }
		StoreManage storeManage = storeManageService.getById(storeManageId);
         if(storeManage==null){
			 result.error500("店铺不存在");
			 return result;
		 }
         //
		 if( storeManage.getBalance().compareTo(oldBalance)==1 && storeManage.getBalance().subtract(oldBalance).compareTo(amount) == 0 ){
			 //支付成功
			 //修改余额记录信息
			 QueryWrapper<StoreRechargeRecord> queryWrapper = new QueryWrapper();
			 queryWrapper.eq("store_manage_id",storeManageId );
			 queryWrapper.eq("amount",amount);
			 queryWrapper.eq("pay_type","4");
			 queryWrapper.eq("trade_status","0");
			 queryWrapper.eq("back_times","0");
			 queryWrapper.orderByDesc("create_time");
			List<StoreRechargeRecord>  storeRechargeRecordS = storeRechargeRecordService.list(queryWrapper);
			 if(storeRechargeRecordS.size()>0){
				 StoreRechargeRecord storeRechargeRecord = storeRechargeRecordS.get(0);
				 //修改余额记录数据
				 storeRechargeRecord.setTradeStatus("5");
				 storeRechargeRecord.setBackStatus("1");
				 storeRechargeRecord.setBackTimes(storeRechargeRecord.getBackTimes().add(BigDecimal.valueOf(1)) );
				 //生成资金流水数据
				 StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
				 storeAccountCapital.setDelFlag("0");
				 storeAccountCapital.setStoreManageId(storeManageId);
				 storeAccountCapital.setPayType("4");
				 storeAccountCapital.setGoAndCome("0");
				 storeAccountCapital.setAmount(amount);
				 storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
				 storeAccountCapital.setBalance(storeManage.getBalance());
				 storeRechargeRecordService.updateById(storeRechargeRecord);
				 storeAccountCapitalService.save(storeAccountCapital);
				 result.setResult("支付成功！");
				 result.success("充值成功！");
			 }else{
				 result.error500("未找到余额记录数据");
				 return result;
			 }
		 }else{
			 //还未支付
			 result.error500("未支付");
		 }

     return result;

	 }
	 @AutoLog(value = "提现")
	 @ApiOperation(value="提现", notes="提现")
	 @PostMapping("cashOut")
	 public Result<StoreRechargeRecordDTO> cashOut(@RequestBody JSONObject jsonObject){
	 	return storeRechargeRecordService.cashOut(jsonObject);
	 }
}
