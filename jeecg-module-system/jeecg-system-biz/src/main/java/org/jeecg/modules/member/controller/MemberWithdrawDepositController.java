package org.jeecg.modules.member.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.gongke.HttpClientUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.vo.MemberWithdrawDepositVO;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.util.RSAUtil;
import org.jeecg.modules.system.util.SignUtil;
import org.jeecg.modules.system.util.XmlUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
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
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
@Slf4j
@Api(tags="会员提现审批")
@RestController
@RequestMapping("/memberWithdrawDeposit/memberWithdrawDeposit")
public class MemberWithdrawDepositController {
	@Autowired
	private IMemberWithdrawDepositService memberWithdrawDepositService;
	 @Autowired
	 private IMemberListService memberListService;
	 @Autowired
	 private IMemberAccountCapitalService memberAccountCapitalService;
	@Autowired
	private ISysDictService iSysDictService;
	@Value("${jeecg.path.cert}")
	private String cert;
	@Autowired
	private SignUtil signUtil;
	@Autowired
	private HttpClientUtil httpClientUtil;
	@Autowired
	private XmlUtil xmlUtil;
    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;
    @Autowired
	private IMemberBankCardService iMemberBankCardService;
    @Autowired
	private RSAUtil rsaUtil;


	/**
	  * 分页列表查询
	 * @param memberWithdrawDepositVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "会员提现审批-分页列表查询")
	@ApiOperation(value="会员提现审批-分页列表查询", notes="会员提现审批-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MemberWithdrawDepositVO>> queryPageList(MemberWithdrawDepositVO memberWithdrawDepositVO,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		Result<IPage<MemberWithdrawDepositVO>> result = new Result<IPage<MemberWithdrawDepositVO>>();
		Page<MemberWithdrawDeposit> page = new Page<MemberWithdrawDeposit>(pageNo, pageSize);
		IPage<MemberWithdrawDepositVO> pageList = memberWithdrawDepositService.getMemberWithdrawDeposit(page, memberWithdrawDepositVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param memberWithdrawDeposit
	 * @return
	 */
	@AutoLog(value = "会员提现审批-添加")
	@ApiOperation(value="会员提现审批-添加", notes="会员提现审批-添加")
	@PostMapping(value = "/add")
	public Result<MemberWithdrawDeposit> add(@RequestBody MemberWithdrawDeposit memberWithdrawDeposit) {
		Result<MemberWithdrawDeposit> result = new Result<MemberWithdrawDeposit>();
		try {
			memberWithdrawDepositService.save(memberWithdrawDeposit);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param memberWithdrawDeposit
	 * @return
	 */
	@AutoLog(value = "会员提现审批-编辑")
	@ApiOperation(value="会员提现审批-编辑", notes="会员提现审批-编辑")
	@PutMapping(value = "/edit")
	public Result<MemberWithdrawDeposit> edit(@RequestBody MemberWithdrawDeposit memberWithdrawDeposit) {
		Result<MemberWithdrawDeposit> result = new Result<MemberWithdrawDeposit>();
		MemberWithdrawDeposit memberWithdrawDepositEntity = memberWithdrawDepositService.getById(memberWithdrawDeposit.getId());
		if(memberWithdrawDepositEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = memberWithdrawDepositService.updateById(memberWithdrawDeposit);
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
	@AutoLog(value = "会员提现审批-通过id删除")
	@ApiOperation(value="会员提现审批-通过id删除", notes="会员提现审批-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			memberWithdrawDepositService.removeById(id);
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
	@AutoLog(value = "会员提现审批-批量删除")
	@ApiOperation(value="会员提现审批-批量删除", notes="会员提现审批-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MemberWithdrawDeposit> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MemberWithdrawDeposit> result = new Result<MemberWithdrawDeposit>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.memberWithdrawDepositService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "会员提现审批-通过id查询")
	@ApiOperation(value="会员提现审批-通过id查询", notes="会员提现审批-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MemberWithdrawDeposit> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MemberWithdrawDeposit> result = new Result<MemberWithdrawDeposit>();
		MemberWithdrawDeposit memberWithdrawDeposit = memberWithdrawDepositService.getById(id);
		if(memberWithdrawDeposit==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(memberWithdrawDeposit);
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
      QueryWrapper<MemberWithdrawDeposit> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MemberWithdrawDeposit memberWithdrawDeposit = JSON.parseObject(deString, MemberWithdrawDeposit.class);
              queryWrapper = QueryGenerator.initQueryWrapper(memberWithdrawDeposit, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MemberWithdrawDeposit> pageList = memberWithdrawDepositService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "会员提现审批列表");
      mv.addObject(NormalExcelConstants.CLASS, MemberWithdrawDeposit.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员提现审批列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MemberWithdrawDeposit> listMemberWithdrawDeposits = ExcelImportUtil.importExcel(file.getInputStream(), MemberWithdrawDeposit.class, params);
              memberWithdrawDepositService.saveBatch(listMemberWithdrawDeposits);
              return Result.ok("文件导入成功！数据行数:" + listMemberWithdrawDeposits.size());
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
	  * 通过id查询修改审核状态  弃用
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "会员提现审批-通过id查询")
	 @ApiOperation(value = "会员提现审批-通过id查询", notes = "会员提现审批-通过id查询")
	 @GetMapping(value = "/updateAuditStatus")
	 public Result<MemberWithdrawDeposit> updateAuditStatus(@RequestParam(name = "id", required = true) String id,
															@RequestParam(name = "status") String status,
															@RequestParam(name = "closeExplain") String closeExplain) {
		 Result<MemberWithdrawDeposit> result = new Result<MemberWithdrawDeposit>();
		 MemberWithdrawDeposit memberWithdrawDeposit = memberWithdrawDepositService.getById(id);
		 if (memberWithdrawDeposit == null) {
			 result.error500("未找到对应实体");
		 } else {
			 memberWithdrawDeposit.setStatus(status);
			 memberWithdrawDeposit.setCloseExplain(closeExplain);
			 boolean ok = memberWithdrawDepositService.updateById(memberWithdrawDeposit);
			 if (ok) {
				 result.success("修改成功!");
			 } else {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }


	 /**
	  * 通过id查询修改状态 打款  弃用
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "会员打款-通过id查询")
	 @ApiOperation(value = "会员打款-通过id查询", notes = "会员打款-通过id查询")
	 @GetMapping(value = "/updateStatusRemit")
	 public Result<MemberWithdrawDeposit> updateStatusRemit(@RequestParam(name = "id", required = true) String id,
															@RequestParam(name = "status") String status,
															@RequestParam(name = "remark") String remark) {
		 Result<MemberWithdrawDeposit> result = new Result<MemberWithdrawDeposit>();
		 MemberWithdrawDeposit memberWithdrawDeposit = memberWithdrawDepositService.getById(id);
		 if (memberWithdrawDeposit == null) {
			 result.error500("未找到对应实体");
		 } else {
			 //已付款
			 if(status.equals("2")){
			 	//获取会员信息
				 MemberList memberList = memberListService.getById(memberWithdrawDeposit.getMemberListId());
				 if(memberList == null){
					 result.error500("用户不存在");
				 }else{
					 //比较大小：
					// int a = bigdemical.compareTo(bigdemical2)
					 //a = -1,表示bigdemical小于bigdemical2
					 int a = memberList.getBalance().compareTo(memberWithdrawDeposit.getMoney());
					 if(a == -1){
						 result.error500("提现金额大于，余额！");
					 }else{
						 //修改打款状态
						 memberWithdrawDeposit.setStatus(status);
						 memberWithdrawDeposit.setRemark(remark);
						 boolean ok = memberWithdrawDepositService.updateById(memberWithdrawDeposit);
                         //会员余额减去提现值     减法  bignum3 = bignum1.subtract(bignum2);
					     memberList.setBalance(memberList.getBalance().subtract(memberWithdrawDeposit.getMoney()));
						 //会员冻结余额减去提现金额
						 memberList.setAccountFrozen(memberList.getAccountFrozen().subtract(memberWithdrawDeposit.getMoney()));
                         //会员已提现余额 + 提现金额
						 memberList.setHaveWithdrawal(memberList.getHaveWithdrawal().add(memberWithdrawDeposit.getMoney()));

						 memberListService.updateById(memberList);
						 //生成资金明细数据
						 MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
						 memberAccountCapital.setDelFlag("0");
						 memberAccountCapital.setMemberListId(memberWithdrawDeposit.getMemberListId());
						 memberAccountCapital.setPayType("1");
						 memberAccountCapital.setGoAndCome("1");
						 memberAccountCapital.setAmount(memberWithdrawDeposit.getMoney());
						 memberAccountCapital.setOrderNo(memberWithdrawDeposit.getOrderNo());
						 memberAccountCapital.setBalance(memberList.getBalance());
						 memberAccountCapitalService.save(memberAccountCapital);
						 if (ok) {
							 result.success("修改成功!");
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
	 public Result<MemberWithdrawDeposit> audit (@RequestBody MemberWithdrawDepositVO memberWithdrawDepositVO){
		 LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 memberWithdrawDepositVO.setRealname(user.getRealname());
	     return memberWithdrawDepositService.audit(memberWithdrawDepositVO);
     }
     @AutoLog(value = "打款")
     @ApiOperation(value = "打款", notes = "打款")
     @PostMapping("/remit")
     public Result<String> remit (@RequestBody MemberWithdrawDepositVO memberWithdrawDepositVO,HttpServletRequest request){
		 MemberWithdrawDeposit memberWithdrawDeposit = memberWithdrawDepositService.getById(memberWithdrawDepositVO.getId());
		 if (memberWithdrawDeposit.getWithdrawalType().equals("0")){
			 return this.transferPay(memberWithdrawDepositVO,request);
		 }else {
			 LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 memberWithdrawDepositVO.setRealname(user.getRealname());
			 return memberWithdrawDepositService.remit(memberWithdrawDepositVO);
		 }
     }

	/**
	 * 企业向个人支付转账 (微信提现)
	 * @param request
	 * @param memberWithdrawDepositVO
	 */
	@PostMapping(value = "/transferPay")
	@ResponseBody
	@Transactional
	public Result<String> transferPay( MemberWithdrawDepositVO memberWithdrawDepositVO, HttpServletRequest request) {
		// TODO: 2023/5/4 提现打款 @zhangshaolin
		Result<String> result = new Result<>();
        if(!memberWithdrawDepositVO.getStatus().equals("2")){
			result.setMessage("操作成功!");
			return result;
		}
		MemberWithdrawDeposit memberWithdrawDeposit =  memberWithdrawDepositService.getById(memberWithdrawDepositVO.getId()) ;
		MemberList memberList = memberListService.getById(memberWithdrawDeposit.getMemberListId());
		if (!memberWithdrawDeposit.getStatus().equals("1")){
			return result.error500("申请出错状态不是待打款!");
		}
		if (memberList.getUnusableFrozen().compareTo(memberWithdrawDeposit.getAmount()) == -1){
			return  result.error500("提现失败,不可用余额不足!");
		}
		if(memberWithdrawDeposit.getAmount().compareTo(new BigDecimal("0.3")) == -1  ||memberWithdrawDeposit.getAmount().compareTo(new BigDecimal("5000")) == 1){
			return  result.error500("申请提现金额不符:低于最小金额0.30元或高于5000.00元");
		}
		//获取常量信息
		String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID");
		//商户号
		String  mchId =iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "mchId");
		//api密钥
		String  partnerKey=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "partnerKey");
		String systemName = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "system_name");
		/** ==================================================================================================================*/
		/** ==================================================封装提现所需参数================================================*/
		/** ==================================================================================================================*/
		Map<String, String> restmap = null;
		try {
			//转账金额转换成分
			String finalmoney = memberWithdrawDeposit
					.getAmount()
					.multiply(new BigDecimal(100))
					.stripTrailingZeros()
					.toBigInteger()
					.toString();

			Map<String, Object> parm = new HashMap<String, Object>();
			parm.put("mch_appid", appid); //公众账号appid
			parm.put("mchid", mchId); //商户号
			parm.put("nonce_str", signUtil.buildRandom()); //随机字符串
			parm.put("partner_trade_no", memberWithdrawDeposit.getOrderNo()); //商户订单号
			parm.put("openid", memberList.getOpenid()); //用户openid oCVr20N2YLH9VQztnkZTaCj2aYYY
			parm.put("check_name", "NO_CHECK"); //校验用户姓名选项 OPTION_CHECK
			parm.put("amount",Integer.parseInt(finalmoney)); //转账金额
			parm.put("desc",systemName+"用户提现"); //企业付款描述信息
			parm.put("spbill_create_ip", IpUtils.getIpAddr(request)); //Ip地址
			parm.put("sign", signUtil.createSign(parm, partnerKey));// partnerKey API密钥 (签名)
			log.info("提现到微信钱包签名：" + signUtil.createSign(parm, partnerKey));
			String reqXml = signUtil.createXML(parm);
			log.info("提现到微信钱包参数：" + reqXml);
			String resXml = httpClientUtil.doSendMoney("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", reqXml);
			log.info("提现到微信钱包返回参数：" + resXml);
			restmap = xmlUtil.xmlParse(resXml);

		} catch (Exception e) {
			e.printStackTrace();
			return  result.error500("转账发生异常");

		}
		/** ========================================================提现结果处理===================================================*/

		/** ===================================================生成提现及流水记录，改变余额==================================================*/

		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
				//查出会员余额记录
				MemberRechargeRecord memberRechargeRecord = iMemberRechargeRecordService.getOne(new LambdaQueryWrapper<MemberRechargeRecord>()
						.eq(MemberRechargeRecord::getTradeNo,memberWithdrawDeposit.getOrderNo())
						.eq(MemberRechargeRecord::getMemberListId,memberWithdrawDeposit.getMemberListId()));
				if (oConvertUtils.isEmpty(memberRechargeRecord)){
					return result.error500("状态异常,请联系管理员!");
				}
			    //提现成功后数据更改
				memberWithdrawDepositService.weChatWithdrawal( memberList, memberRechargeRecord, memberWithdrawDeposit);
			log.info("转账成功：" + restmap.get("err_code_des") + "---提现到微信钱包：" + memberWithdrawDeposit.getOrderNo() + "--会员提现：" + memberList.getId());
			 result.success("提现成功!");
		}else {
			/** =================================================3.转账失败========================================================*/
			log.info("转账失败：" + restmap.get("err_code_des") + "---提现到微信钱包：" + memberWithdrawDeposit.getOrderNo() + "--会员提现：" + memberList.getId());
			return result.error500("转账失败"+restmap.get("err_code_des"));
		}
		return result;
	}

	/**
	 * 企业向个人支付转账 (微信提现到银行卡)
	 * @param bank_code
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/postWXPayToBC")
	@ResponseBody
	public Result<String> postWXPayToBC(MemberWithdrawDepositVO memberWithdrawDepositVO, String bank_code){
		Result<String> result = new Result<>();
		if (memberWithdrawDepositVO.getStatus().equals("2")){
			//查出会员余额记录
			MemberWithdrawDeposit memberWithdrawDeposit =  memberWithdrawDepositService.getById(memberWithdrawDepositVO.getId()) ;
			MemberList memberList = memberListService.getById(memberWithdrawDeposit.getMemberListId());

			MemberBankCard memberBankCard = iMemberBankCardService.getOne(new LambdaQueryWrapper<MemberBankCard>().eq(MemberBankCard::getMemberListId,memberList.getId())
			                                                                     .eq(MemberBankCard::getCarType,"0").orderByDesc(MemberBankCard::getCreateTime));
			if(oConvertUtils.isEmpty(memberBankCard)){
				return result.error500("请先去绑定银行卡!");
			}
			if (!memberWithdrawDeposit.getStatus().equals("1")){
				return result.error500("申请出错状态不是待打款!");
			}
			if (memberList.getAccountFrozen().compareTo(memberWithdrawDeposit.getAmount()) == -1){
				return  result.error500("提现失败,冻结余额不足!");
			}
			if(memberWithdrawDeposit.getAmount().compareTo(new BigDecimal("0.3")) == -1  ||memberWithdrawDeposit.getAmount().compareTo(new BigDecimal("5000")) == 1){
				return  result.error500("申请提现金额不符:低于最小金额0.30元或高于5000.00元");
			}
			String partner_trade_no = RandomStringUtils.randomAlphanumeric(32);// 生成随机号
			String nonce_str1 = RandomStringUtils.randomAlphanumeric(32);
			//获取常量信息
			String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID");
			//商户号
			String  mchId =iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "mchId");
			//api密钥
			String  partnerKey=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "partnerKey");
			String	encBankAcctNo ="";
			String	encBankAcctName ="";
			Map<String, String> restmap = null;
			try {
			//定义自己公钥的路径（需要配置字典）
			String keyfile = cert + "/apiclient_cert.p12"; //读取PKCS8密钥文件
			// 定义自己公钥的路径
			PublicKey pub = rsaUtil.getPubKey(keyfile,"RSA");
			String rsa = "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";// rsa是微信付款到银行卡要求我们填充的字符串（Java）
				byte[] estr = rsaUtil.encrypt(memberBankCard.getBankCard().getBytes(), pub, 2048, 11, rsa);
				// 对银行账号进行加密
				encBankAcctNo = Base64.encode(estr);// 并转为base64格式
				// 收款方用户名
				estr = rsaUtil.encrypt(memberBankCard.getBankName().getBytes("UTF-8"), pub, 2048, 11, rsa);
				encBankAcctName = Base64.encode(estr); // 对银行账户名加密并转为base64

            //转账金额转换成分
            String finalmoney = memberWithdrawDeposit
                        .getAmount()
                        .multiply(new BigDecimal(100))
                        .stripTrailingZeros()
                        .toString();
			//根据要传递的参数生成自己的签名
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			parameters1.put("mch_id", mchId);// 商户号
			parameters1.put("partner_trade_no", partner_trade_no);// 商户企业付款单号
			parameters1.put("nonce_str", nonce_str1);// 随机字符串
			parameters1.put("enc_bank_no", encBankAcctNo);// 收款方银行卡号
			parameters1.put("enc_true_name", encBankAcctName);// 收款方用户名
			parameters1.put("bank_code", bank_code);// 收款方开户行
			parameters1.put("amount",Integer.parseInt(finalmoney));// 付款金额
			parameters1.put("desc", memberList.getNickName()+ " 会员提现");// 付款说明
			// 调用签名方法
			String sign = SignUtil.createSign(parameters1, partnerKey);
			// 把签名放到map集合中
			parameters1.put("sign", sign);// 签名
			// 将当前的map结合转化成xml格式
			String reqXml = signUtil.createXML(parameters1);
			// 发送请求到企业付款到银行的Api。发送请求是一个方法来的POST
			String resXml = httpClientUtil.doSendMoney("https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank", reqXml);

			restmap = xmlUtil.xmlParse(resXml);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
				//提现成功

					//查出会员余额记录
					MemberRechargeRecord memberRechargeRecord = iMemberRechargeRecordService.getOne(new LambdaQueryWrapper<MemberRechargeRecord>()
							.eq(MemberRechargeRecord::getTradeNo,memberWithdrawDepositVO.getOrderNo())
							.eq(MemberRechargeRecord::getMemberListId,memberWithdrawDepositVO.getMemberListId()));
					if (oConvertUtils.isEmpty(memberRechargeRecord)){
						return result.error500("状态异常,请联系管理员!");
					}
				//提现成功后数据更改
				memberWithdrawDepositService.postWXPayToBC( memberList, memberRechargeRecord, memberWithdrawDeposit);

				result.success("提现成功!");

			}else{
				return result.error500("转账失败"+restmap.get("err_code_des"));
			}


		}




		return result;
	}
}
