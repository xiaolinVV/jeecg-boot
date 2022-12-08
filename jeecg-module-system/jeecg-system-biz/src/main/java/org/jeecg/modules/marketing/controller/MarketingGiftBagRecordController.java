package org.jeecg.modules.marketing.controller;

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
import org.apache.shiro.subject.Subject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordService;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordVO;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.jeecg.modules.weixin.api.AfterWeixinController;
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
 * @Description: 礼包记录
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags="礼包记录")
@RestController
@RequestMapping("/marketingGiftBagRecord/marketingGiftBagRecord")
public class MarketingGiftBagRecordController {
	@Autowired
	private IMarketingGiftBagRecordService marketingGiftBagRecordService;

	@Autowired
	private ISysSmallcodeService iSysSmallcodeService;

	@Autowired
	private AfterWeixinController afterWeixinController;

	@Autowired
	private IMemberListService iMemberListService;




	 /**
	  *
	  *  获取礼包记录分享码
	  *
	  * @param marketingGiftBagId
	  * @param memberListId
	  * @return
	  */
	@RequestMapping("getShareAddress")
	@ResponseBody
	public Result<?> getShareAddress(String marketingGiftBagId,String memberListId){
		String address=iSysSmallcodeService.getShareAddress(marketingGiftBagId,memberListId);
		if(StringUtils.isBlank(address)){
			MemberList memberList=iMemberListService.getById(memberListId);
			Map<String,Object> paraMap= Maps.newHashMap();
			paraMap.put("state","1");
			paraMap.put("id",marketingGiftBagId);
			paraMap.put("giftBagType","0");
			Map<String,Object> memberInfoMap=Maps.newHashMap();
			memberInfoMap.put("TmemberName",memberList.getNickName());
			memberInfoMap.put("TmemberHeadPortrait",memberList.getHeadPortrait());
			memberInfoMap.put("sysUserId",memberList.getSysUserId());
			paraMap.put("TmemberInfo",memberInfoMap);
			String param=JSON.toJSONString(paraMap);
			afterWeixinController.getQrCodeByPage("userAction/pages/vipMember/vipMember",param,memberList.getSysUserId(),memberList.getId());
		}
		return Result.ok(iSysSmallcodeService.getShareAddress(marketingGiftBagId,memberListId));
	}

	
	/**
	  * 分页列表查询
	 * @param marketingGiftBagRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "礼包记录-分页列表查询")
	@ApiOperation(value="礼包记录-分页列表查询", notes="礼包记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGiftBagRecord>> queryPageList(MarketingGiftBagRecord marketingGiftBagRecord,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingGiftBagRecord>> result = new Result<IPage<MarketingGiftBagRecord>>();
		QueryWrapper<MarketingGiftBagRecord> queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagRecord, req.getParameterMap());
		Page<MarketingGiftBagRecord> page = new Page<MarketingGiftBagRecord>(pageNo, pageSize);
		IPage<MarketingGiftBagRecord> pageList = marketingGiftBagRecordService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGiftBagRecord
	 * @return
	 */
	@AutoLog(value = "礼包记录-添加")
	@ApiOperation(value="礼包记录-添加", notes="礼包记录-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGiftBagRecord> add(@RequestBody MarketingGiftBagRecord marketingGiftBagRecord) {
		Result<MarketingGiftBagRecord> result = new Result<MarketingGiftBagRecord>();
		try {
			marketingGiftBagRecordService.save(marketingGiftBagRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGiftBagRecord
	 * @return
	 */
	@AutoLog(value = "礼包记录-编辑")
	@ApiOperation(value="礼包记录-编辑", notes="礼包记录-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGiftBagRecord> edit(@RequestBody MarketingGiftBagRecord marketingGiftBagRecord) {
		Result<MarketingGiftBagRecord> result = new Result<MarketingGiftBagRecord>();
		MarketingGiftBagRecord marketingGiftBagRecordEntity = marketingGiftBagRecordService.getById(marketingGiftBagRecord.getId());
		if(marketingGiftBagRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGiftBagRecordService.updateById(marketingGiftBagRecord);
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
	@AutoLog(value = "礼包记录-通过id删除")
	@ApiOperation(value="礼包记录-通过id删除", notes="礼包记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGiftBagRecordService.removeById(id);
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
	@AutoLog(value = "礼包记录-批量删除")
	@ApiOperation(value="礼包记录-批量删除", notes="礼包记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGiftBagRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGiftBagRecord> result = new Result<MarketingGiftBagRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGiftBagRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "礼包记录-通过id查询")
	@ApiOperation(value="礼包记录-通过id查询", notes="礼包记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGiftBagRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGiftBagRecord> result = new Result<MarketingGiftBagRecord>();
		MarketingGiftBagRecord marketingGiftBagRecord = marketingGiftBagRecordService.getById(id);
		if(marketingGiftBagRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGiftBagRecord);
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
      QueryWrapper<MarketingGiftBagRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGiftBagRecord marketingGiftBagRecord = JSON.parseObject(deString, MarketingGiftBagRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGiftBagRecord> pageList = marketingGiftBagRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "礼包记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGiftBagRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("礼包记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGiftBagRecord> listMarketingGiftBagRecords = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGiftBagRecord.class, params);
              marketingGiftBagRecordService.saveBatch(listMarketingGiftBagRecords);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGiftBagRecords.size());
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
	 @AutoLog(value = "礼包记录")
	 @ApiOperation(value="礼包记录", notes="礼包记录")
	 @GetMapping(value = "/findGiftBagRecord")
	 public Result<IPage<MarketingGiftBagRecordVO>> findGiftBagRecord(MarketingGiftBagRecordVO marketingGiftBagRecordVO,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																HttpServletRequest req) {
		 Result<IPage<MarketingGiftBagRecordVO>> result = new Result<IPage<MarketingGiftBagRecordVO>>();
		 Subject subject = SecurityUtils.getSubject();
		 if (subject.hasRole("598lbxszy")){
		 	marketingGiftBagRecordVO.setMarketingGiftBagId("fe1054bdd371f2930018aa91585736a6");
		 }
		 LoginUser user = (LoginUser) subject.getPrincipal();
		 if (subject.hasRole("Franchisee")){
		 	marketingGiftBagRecordVO.setAllianceManageUserId(user.getId());
		 }
		 Page<MarketingGiftBagRecordVO> page = new Page<MarketingGiftBagRecordVO>(pageNo, pageSize);
		 IPage<MarketingGiftBagRecordVO> pageList = marketingGiftBagRecordService.findGiftBagRecord(page, marketingGiftBagRecordVO);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
}
