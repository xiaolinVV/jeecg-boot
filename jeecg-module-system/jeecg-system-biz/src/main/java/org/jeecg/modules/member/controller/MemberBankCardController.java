package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.dto.MemberBankCardDTO;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.vo.MemberBankCardVO;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 会员银行卡
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="会员银行卡")
@RestController
@RequestMapping("/memberBankCard/memberBankCard")
public class MemberBankCardController {
	@Autowired
	private IMemberBankCardService memberBankCardService;


	 /**
	  * 根据会员id获取收银信息
	  *
	  * @param memberId
	  * @return
	  */
	@GetMapping("getMemberBankCardByMemberId")
	public Result<?> getMemberBankCardByMemberId(String memberId){
		return Result.ok(memberBankCardService.getOne(new LambdaQueryWrapper<MemberBankCard>().eq(MemberBankCard::getMemberListId,memberId).eq(MemberBankCard::getCarType,"0")));
	}
	
	/**
	  * 分页列表查询
	 * @param memberBankCard
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "会员银行卡-分页列表查询")
	@ApiOperation(value="会员银行卡-分页列表查询", notes="会员银行卡-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MemberBankCard>> queryPageList(MemberBankCard memberBankCard,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MemberBankCard>> result = new Result<IPage<MemberBankCard>>();
		QueryWrapper<MemberBankCard> queryWrapper = QueryGenerator.initQueryWrapper(memberBankCard, req.getParameterMap());
		Page<MemberBankCard> page = new Page<MemberBankCard>(pageNo, pageSize);
		IPage<MemberBankCard> pageList = memberBankCardService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 @AutoLog(value = "会员银行卡列表查询")
	 @ApiOperation(value="会员银行卡列表查询", notes="会员银行卡列表查询")
	 @GetMapping(value = "findMemberBankCard")
	public Result<IPage<MemberBankCardVO>> findMemberBankCard( MemberBankCardDTO memberBankCardDTO,
													   @RequestParam(name = "pageNo",defaultValue = "1")Integer pageNo,
													   @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
		Result<IPage<MemberBankCardVO>> result = new Result<>();
		Page<MemberBankCardVO> page = new Page<>(pageNo,pageSize);
		IPage<MemberBankCardVO> memberBankCard = memberBankCardService.findMemberBankCard(page, memberBankCardDTO);
		 memberBankCard.getRecords().forEach(pl->{
			 if (StringUtils.isNotBlank(pl.getUpdateCertificate())){
				 pl.setUpdateCertificateOne((String) JSON.parseObject(pl.getUpdateCertificate()).get("0"));
			 }
		 });
		result.setSuccess(true);
		result.setResult(memberBankCard);
		return result;
	}
	/**
	  *   添加
	 * @param memberBankCard
	 * @return
	 */
	@AutoLog(value = "会员银行卡-添加")
	@ApiOperation(value="会员银行卡-添加", notes="会员银行卡-添加")
	@PostMapping(value = "/add")
	public Result<MemberBankCard> add(@RequestBody MemberBankCard memberBankCard) {
		Result<MemberBankCard> result = new Result<MemberBankCard>();
		try {
			memberBankCardService.save(memberBankCard);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param memberBankCard
	 * @return
	 */
	@AutoLog(value = "会员银行卡-编辑")
	@ApiOperation(value="会员银行卡-编辑", notes="会员银行卡-编辑")
	@PostMapping(value = "/edit")
	public Result<MemberBankCard> edit(@RequestBody MemberBankCard memberBankCard) {
		Result<MemberBankCard> result = new Result<MemberBankCard>();
		MemberBankCard memberBankCardEntity = memberBankCardService.getById(memberBankCard.getId());
		if(memberBankCardEntity==null) {
			result.error500("未找到对应实体");
		}else {
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			boolean ok = memberBankCardService.updateById(memberBankCard
					.setUpdateBy(sysUser.getUsername())
					.setUpdateTime(new Date())
			);
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
	@AutoLog(value = "会员银行卡-通过id删除")
	@ApiOperation(value="会员银行卡-通过id删除", notes="会员银行卡-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			memberBankCardService.removeById(id);
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
	@AutoLog(value = "会员银行卡-批量删除")
	@ApiOperation(value="会员银行卡-批量删除", notes="会员银行卡-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MemberBankCard> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MemberBankCard> result = new Result<MemberBankCard>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.memberBankCardService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "会员银行卡-通过id查询")
	@ApiOperation(value="会员银行卡-通过id查询", notes="会员银行卡-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MemberBankCard> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MemberBankCard> result = new Result<MemberBankCard>();
		MemberBankCard memberBankCard = memberBankCardService.getById(id);
		if(memberBankCard==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(memberBankCard);
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
      QueryWrapper<MemberBankCard> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MemberBankCard memberBankCard = JSON.parseObject(deString, MemberBankCard.class);
              queryWrapper = QueryGenerator.initQueryWrapper(memberBankCard, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MemberBankCard> pageList = memberBankCardService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "会员银行卡列表");
      mv.addObject(NormalExcelConstants.CLASS, MemberBankCard.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员银行卡列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MemberBankCard> listMemberBankCards = ExcelImportUtil.importExcel(file.getInputStream(), MemberBankCard.class, params);
              memberBankCardService.saveBatch(listMemberBankCards);
              return Result.ok("文件导入成功！数据行数:" + listMemberBankCards.size());
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
	  * 获取银行卡信息列表
	  *
	  * @return
	  */
	 @GetMapping("getBlankList")
	 public Result<?> getBlankList(){
		 try {
			 Map<String,Object> resultMap= Maps.newHashMap();
			 IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("blank.properties"),"utf-8").forEach(blance->{
				 resultMap.put(org.apache.commons.lang.StringUtils.substringBefore(blance,"="), org.apache.commons.lang.StringUtils.substringAfter(blance,"="));
			 });

			 return Result.ok(resultMap);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return Result.ok();
	 }
	 /**
	  * 获取银行卡城市
	  * @return
	  */
	 @GetMapping("findSrea")
	 public Result<?> findSrea(){
		 try {
			 return Result.ok(StringEscapeUtils.unescapeJava(org.apache.commons.lang.StringUtils.join(IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("pay_area.properties"),"utf-8"),"")));
		 } catch (IOException e) {
			 e.printStackTrace();
			 return Result.error("异常");
		 }
	 }
}
