package org.jeecg.modules.agency.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.jeecg.modules.agency.dto.AgencyRechargeRecordDTO;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.service.IAgencyRechargeRecordService;
import org.jeecg.modules.agency.vo.AgencyRechargeRecordVO;
import org.jeecg.modules.order.utils.WeixinPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.system.service.ISysDictService;
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
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="代理列表")
@RestController
@RequestMapping("/agencyRechargeRecord/agencyRechargeRecord")
public class AgencyRechargeRecordController {
	@Autowired
	private IAgencyRechargeRecordService agencyRechargeRecordService;
	@Autowired
	private IAgencyManageService agencyManageService;
	@Autowired
	 private ISysDictService iSysDictService;

	 @Autowired
	 private IAgencyAccountCapitalService agencyAccountCapitalService;
	 @Autowired
	 private ISysUserRoleService iSysUserRoleService;

	 @Autowired
	 private WeixinPayUtils weixinPayUtils;

	 @Autowired
	 private NotifyUrlUtils notifyUrlUtils;

	/**
	  * 分页列表查询
	 * @param agencyRechargeRecordDTO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "代理列表-分页列表查询")
	@ApiOperation(value="代理列表-分页列表查询", notes="代理列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AgencyRechargeRecordVO>> queryPageList(AgencyRechargeRecordDTO agencyRechargeRecordDTO,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<AgencyRechargeRecordVO>> result = new Result<IPage<AgencyRechargeRecordVO>>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		if (roleByUserId.contains("Municipal_agent")||roleByUserId.contains("Provincial_agents")||roleByUserId.contains("County_agent")){
			agencyRechargeRecordDTO.setSysUserId(sysUser.getId());
		}
		Page<AgencyRechargeRecord> page = new Page<AgencyRechargeRecord>(pageNo, pageSize);
		IPage<AgencyRechargeRecordVO> pageList = agencyRechargeRecordService.queryPageList(page, agencyRechargeRecordDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 @AutoLog(value = "代理余额记录-充值记录")
	 @ApiOperation(value="代理余额记录-充值记录", notes="代理余额记录-充值记录")
	 @GetMapping(value = "/findRechargeRecord")
	 public Result<IPage<AgencyRechargeRecord>> findRechargeRecord(AgencyRechargeRecord agencyRechargeRecord,
																  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																  HttpServletRequest req) {
		 Result<IPage<AgencyRechargeRecord>> result = new Result<IPage<AgencyRechargeRecord>>();
		 QueryWrapper<AgencyRechargeRecord> queryWrapper = QueryGenerator.initQueryWrapper(agencyRechargeRecord, req.getParameterMap());
		 //省市县权限添加权限
		 PermissionUtils.accreditArea(queryWrapper);
		 queryWrapper.eq("pay_type","3");
		 Page<AgencyRechargeRecord> page = new Page<AgencyRechargeRecord>(pageNo, pageSize);
		 IPage<AgencyRechargeRecord> pageList = agencyRechargeRecordService.page(page, queryWrapper);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	 @AutoLog(value = "代理余额记录-提现记录")
	 @ApiOperation(value="代理余额记录-提现记录", notes="代理余额记录-提现记录")
	 @GetMapping(value = "/findPayRecord")
	 public Result<IPage<AgencyRechargeRecordDTO>> findPayRecord(AgencyRechargeRecordVO agencyRechargeRecordVO,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		 Result<IPage<AgencyRechargeRecordDTO>> result = new Result<IPage<AgencyRechargeRecordDTO>>();
		//代理权限
		 String userId = PermissionUtils.ifPlatformArea();
			if(StringUtils.isNotBlank(userId)){
				agencyRechargeRecordVO.setSysUserId(userId);
			}
		 Page<AgencyRechargeRecord> page = new Page<AgencyRechargeRecord>(pageNo, pageSize);
		 agencyRechargeRecordVO.setPayType("1");
		 IPage<AgencyRechargeRecordDTO> pageList = agencyRechargeRecordService.getAgencyRechargeRecordList(page, agencyRechargeRecordVO);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	/**
	  *   添加
	 * @param agencyRechargeRecord
	 * @return
	 */
	@AutoLog(value = "代理列表-添加")
	@ApiOperation(value="代理列表-添加", notes="代理列表-添加")
	@PostMapping(value = "/add")
	public Result<AgencyRechargeRecord> add(@RequestBody AgencyRechargeRecord agencyRechargeRecord) {
		Result<AgencyRechargeRecord> result = new Result<AgencyRechargeRecord>();
		try {
			agencyRechargeRecordService.save(agencyRechargeRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param agencyRechargeRecord
	 * @return
	 */
	@AutoLog(value = "代理列表-编辑")
	@ApiOperation(value="代理列表-编辑", notes="代理列表-编辑")
	@PutMapping(value = "/edit")
	public Result<AgencyRechargeRecord> edit(@RequestBody AgencyRechargeRecord agencyRechargeRecord) {
		Result<AgencyRechargeRecord> result = new Result<AgencyRechargeRecord>();
		AgencyRechargeRecord agencyRechargeRecordEntity = agencyRechargeRecordService.getById(agencyRechargeRecord.getId());
		if(agencyRechargeRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = agencyRechargeRecordService.updateById(agencyRechargeRecord);
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
	@AutoLog(value = "代理列表-通过id删除")
	@ApiOperation(value="代理列表-通过id删除", notes="代理列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			agencyRechargeRecordService.removeById(id);
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
	@AutoLog(value = "代理列表-批量删除")
	@ApiOperation(value="代理列表-批量删除", notes="代理列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<AgencyRechargeRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<AgencyRechargeRecord> result = new Result<AgencyRechargeRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.agencyRechargeRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "代理列表-通过id查询")
	@ApiOperation(value="代理列表-通过id查询", notes="代理列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AgencyRechargeRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<AgencyRechargeRecord> result = new Result<AgencyRechargeRecord>();
		AgencyRechargeRecord agencyRechargeRecord = agencyRechargeRecordService.getById(id);
		if(agencyRechargeRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(agencyRechargeRecord);
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
      QueryWrapper<AgencyRechargeRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              AgencyRechargeRecord agencyRechargeRecord = JSON.parseObject(deString, AgencyRechargeRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(agencyRechargeRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<AgencyRechargeRecord> pageList = agencyRechargeRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "代理列表列表");
      mv.addObject(NormalExcelConstants.CLASS, AgencyRechargeRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("代理列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<AgencyRechargeRecord> listAgencyRechargeRecords = ExcelImportUtil.importExcel(file.getInputStream(), AgencyRechargeRecord.class, params);
              agencyRechargeRecordService.saveBatch(listAgencyRechargeRecords);
              return Result.ok("文件导入成功！数据行数:" + listAgencyRechargeRecords.size());
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
	  * 店铺余额充值
	  * @param agencyManageId
	  * @param
	  * @return
	  */
	 @RequestMapping("payBalance")
	 @ResponseBody
	 public Result<String> payBalance(String agencyManageId, BigDecimal price, HttpServletRequest request){

		 Result<String> result=new Result<>();

		 if(StringUtils.isBlank(agencyManageId)){
			 result.error500("agencyManageId不能为空！！！");
			 return result;
		 }
		 AgencyManage agencyManage = agencyManageService.getById(agencyManageId);
		 if(agencyManage == null){
			 result.error500("代理商不存在！！！");
			 return result;
		 }
		 AgencyRechargeRecord agencyRechargeRecord=new AgencyRechargeRecord();
		 agencyRechargeRecord.setDelFlag("0");
		 agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
		 agencyRechargeRecord.setPayType("3");
		 agencyRechargeRecord.setAmount(price);
		 agencyRechargeRecord.setGoAndCome("0");
		 agencyRechargeRecord.setTradeStatus("0");
		 agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());



		 agencyRechargeRecordService.save(agencyRechargeRecord);

		 //设置回调地址

		 String notifyUrl=notifyUrlUtils.getNotify("balance_notifyUrl");

		 //官方微信支付调起
		 Map<String,String> resultMap= weixinPayUtils.payWeixinQR(request,agencyRechargeRecord.getId(),agencyRechargeRecord.getAmount(),notifyUrl);


		 result.setResult(resultMap.get("dbpath"));

		 //支付日志
		 //agencyRechargeRecord.setPayParam(params.toString());

		 //保存支付日志
		 agencyRechargeRecordService.saveOrUpdate(agencyRechargeRecord);
		 result.success("生成支付二维码成功");
		 return result;
	 }
	 /**
	  * 充值回调
	  * @param oldBalance
	  * @param agencyManageId
	  * @param amount
	  * @return
	  */
	 //@RequestMapping("/prepaidPhoneCallback")
	 @GetMapping(value = "/prepaidPhoneCallback")
	 @ResponseBody
	 public Result<String>  prepaidPhoneCallback(BigDecimal oldBalance,String agencyManageId, BigDecimal amount){
		 Result<String> result = new Result<String>();
		 if(StringUtils.isBlank(agencyManageId)){
			 result.error500("代理Id不能为空");
			 return result;
		 }
		 AgencyManage agencyManage = agencyManageService.getById(agencyManageId);
		 if(agencyManage==null){
			 result.error500("代理不存在");
			 return result;
		 }
		 //
		 if( agencyManage.getBalance().compareTo(oldBalance)==1 && agencyManage.getBalance().subtract(oldBalance).compareTo(amount) == 0 ){
			 //支付成功
			 //修改余额记录信息
			 QueryWrapper<AgencyRechargeRecord> queryWrapper = new QueryWrapper();
			 queryWrapper.eq("sys_user_id",agencyManage.getSysUserId() );
			 queryWrapper.eq("amount",amount);
			 queryWrapper.eq("pay_type","3");
			 queryWrapper.eq("trade_status","0");
			// queryWrapper.eq("back_times","0");
			 queryWrapper.orderByDesc("create_time");
			 List<AgencyRechargeRecord>  agencyRechargeRecords = agencyRechargeRecordService.list(queryWrapper);
			 if(agencyRechargeRecords.size()>0){
				 AgencyRechargeRecord agencyRechargeRecord = agencyRechargeRecords.get(0);
				 //修改余额记录数据
				 agencyRechargeRecord.setTradeStatus("5");
				 //agencyRechargeRecord.setBackStatus("1");
				 //agencyRechargeRecord.setBackTimes(agencyRechargeRecord.getBackTimes().add(BigDecimal.valueOf(1)) );
				 //生成资金流水数据
				 AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
				 agencyAccountCapital.setDelFlag("0");
				 agencyAccountCapital.setSysUserId(agencyManage.getSysUserId());
				 agencyAccountCapital.setPayType("4");
				 agencyAccountCapital.setGoAndCome("0");
				 agencyAccountCapital.setAmount(amount);
				 agencyAccountCapital.setOrderNo(agencyRechargeRecord.getOrderNo());
				 agencyAccountCapital.setBalance(agencyManage.getBalance());
				 agencyRechargeRecordService.updateById(agencyRechargeRecord);
				 agencyAccountCapitalService.save(agencyAccountCapital);
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

	 /**
	  *   提现 记录 添加
	  * @param agencyRechargeRecord
	  * @return
	  */
	 @AutoLog(value = "代理余额记录-添加")
	 @ApiOperation(value="代理余额记录-添加", notes="代理余额记录-添加")
	 @PostMapping(value = "/addAgencyRechargeRecord")
	 public Result<AgencyRechargeRecord> addAgencyRechargeRecord(@RequestBody AgencyRechargeRecord agencyRechargeRecord) {
		 Result<AgencyRechargeRecord> result = new Result<AgencyRechargeRecord>();
		 try {

			 if(StringUtils.isBlank(agencyRechargeRecord.getSysUserId())){
				 result.error500("未找到实体");
				 return result;
			 }else{
				 QueryWrapper<AgencyManage> queryWrapperAgencyManage = new QueryWrapper();
				 //店铺信息
				 queryWrapperAgencyManage.eq("sys_user_id",agencyRechargeRecord.getSysUserId());
				 queryWrapperAgencyManage.eq("status","1");
				 queryWrapperAgencyManage.orderByDesc("create_time");
				 AgencyManage  agencyManage = agencyManageService.getOne(queryWrapperAgencyManage);
				 if(agencyManage == null){
					 result.error500("未找到实体");
				 }else{
					 //判断提现金额
					 int a = agencyManage.getBalance().compareTo(agencyRechargeRecord.getAmount());
					 if(a == -1){
						 result.error500("提现金额大于，余额!");
					 }else{
							 //可以提现
							 agencyRechargeRecord.setDelFlag("0");
							 agencyRechargeRecord.setPayType("1");
							 agencyRechargeRecord.setGoAndCome("1");
							 agencyRechargeRecord.setTradeStatus("1");
							 //生成流水号
							 String orderNo = OrderNoUtils.getOrderNo();
							 agencyRechargeRecord.setOrderNo(orderNo);
							 agencyRechargeRecordService.save(agencyRechargeRecord);
							 //生成提现订单，减去
							 agencyManage.setBalance(agencyManage.getBalance().subtract(agencyRechargeRecord.getAmount()));
						    //agencyManage.setAccountFrozen(agencyManage.getAccountFrozen().add(agencyRechargeRecord.getAmount()));
							//提现资金到不可用资金 unusable_frozen
						    agencyManage.setUnusableFrozen(agencyManage.getUnusableFrozen().add(agencyRechargeRecord.getAmount()));

						 agencyManageService.updateById(agencyManage);
							 result.success("添加成功！");
					 }
				 }
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }
	 @AutoLog(value = "提现")
	 @ApiOperation(value="提现", notes="提现")
	 @PostMapping("cashOut")
	 public Result<AgencyRechargeRecordDTO>cashOut(@RequestBody JSONObject jsonObject){
	 	return agencyRechargeRecordService.cashOut(jsonObject);
	 }
}
