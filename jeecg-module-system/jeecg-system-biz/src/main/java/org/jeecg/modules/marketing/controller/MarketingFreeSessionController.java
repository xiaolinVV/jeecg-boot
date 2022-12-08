package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingFreeSession;
import org.jeecg.modules.marketing.entity.MarketingFreeWinningAnnouncement;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionService;
import org.jeecg.modules.marketing.service.IMarketingFreeWinningAnnouncementService;
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
import java.text.ParseException;
import java.util.*;

/**
 * @Description: 免单场次
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单场次")
@RestController
@RequestMapping("/marketing/marketingFreeSession")
public class MarketingFreeSessionController {
	@Autowired
	private IMarketingFreeSessionService marketingFreeSessionService;

	@Autowired
	private IMarketingFreeWinningAnnouncementService iMarketingFreeWinningAnnouncementService;


	/**
	 *
	 * @return
	 */
	@RequestMapping("lastExeTime")
	@ResponseBody
	public Result<?> lastExeTime(){
		Result<String> result=new Result<>();
		MarketingFreeSession marketingFreeSession=marketingFreeSessionService.getOne(new LambdaQueryWrapper<MarketingFreeSession>()
				.ne(MarketingFreeSession::getStatus,"2")
				.orderByDesc(MarketingFreeSession::getEndTime)
				.last("limit 1"));
		if(marketingFreeSession==null){
			result.setResult(DateUtils.date2Str(new Date(),DateUtils.date_sdf.get()));
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(marketingFreeSession.getEndTime());
		calendar.add(Calendar.DATE,1);
		result.setResult(DateUtils.date2Str(calendar.getTime(),DateUtils.date_sdf.get()));
		result.success("获取之后执行时间");
		return result;
	}


	/**
	 * 设置场次免单日信息
	 * 张靠勤   2021-3-18
	 * @param freeDay
	 * @param marketingFreeSessionId
	 * @return
	 */
	@RequestMapping("settingMarketingFreeSessionByFreeDay")
	@ResponseBody
	public Result<?> settingMarketingFreeSessionByFreeDay(String freeDay,String marketingFreeSessionId){
		//参数验证
		if(StringUtils.isBlank(marketingFreeSessionId)){
			return Result.error("场次id不能为空");
		}
		if(StringUtils.isBlank(freeDay)){
			return Result.error("免单日信息不能为空！！！");
		}

		try {
			//设置免单日
			MarketingFreeSession marketingFreeSession=marketingFreeSessionService.getById(marketingFreeSessionId);
			marketingFreeSession.setFreeDay(DateUtils.parseDate(freeDay,"yyyy-MM-dd"));
			marketingFreeSession.setFreeDaySetting("1");
			marketingFreeSessionService.saveOrUpdate(marketingFreeSession);
			//形成中奖公告
			iMarketingFreeWinningAnnouncementService.saveOrUpdate(new MarketingFreeWinningAnnouncement()
					.setMarketingFreeSessionId(marketingFreeSession.getId()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return  Result.ok("免单日设置成功！！！");
	}
	
	/**
	  * 分页列表查询
	 * @param marketingFreeSession
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "免单场次-分页列表查询")
	@ApiOperation(value="免单场次-分页列表查询", notes="免单场次-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingFreeSession>> queryPageList(MarketingFreeSession marketingFreeSession,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingFreeSession>> result = new Result<IPage<MarketingFreeSession>>();
		QueryWrapper<MarketingFreeSession> queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeSession, req.getParameterMap());
		Page<MarketingFreeSession> page = new Page<MarketingFreeSession>(pageNo, pageSize);
		IPage<MarketingFreeSession> pageList = marketingFreeSessionService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingFreeSession
	 * @return
	 */
	@AutoLog(value = "免单场次-添加")
	@ApiOperation(value="免单场次-添加", notes="免单场次-添加")
	@PostMapping(value = "/add")
	public Result<MarketingFreeSession> add(@RequestBody MarketingFreeSession marketingFreeSession) {
		Result<MarketingFreeSession> result = new Result<MarketingFreeSession>();
		try {

			//增加场次编号
			if(StringUtils.isBlank(marketingFreeSession.getSerialNumber())){
				Calendar calendar=Calendar.getInstance();
				marketingFreeSession.setSerialNumber("T"+calendar.get(Calendar.YEAR)+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DAY_OF_MONTH)+ RandomUtils.nextInt(10,99));
			}
			marketingFreeSessionService.save(marketingFreeSession);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingFreeSession
	 * @return
	 */
	@AutoLog(value = "免单场次-编辑")
	@ApiOperation(value="免单场次-编辑", notes="免单场次-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingFreeSession> edit(@RequestBody MarketingFreeSession marketingFreeSession) {
		Result<MarketingFreeSession> result = new Result<MarketingFreeSession>();
		MarketingFreeSession marketingFreeSessionEntity = marketingFreeSessionService.getById(marketingFreeSession.getId());
		if(marketingFreeSessionEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingFreeSessionService.updateById(marketingFreeSession);
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
	@AutoLog(value = "免单场次-通过id删除")
	@ApiOperation(value="免单场次-通过id删除", notes="免单场次-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingFreeSessionService.removeById(id);
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
	@AutoLog(value = "免单场次-批量删除")
	@ApiOperation(value="免单场次-批量删除", notes="免单场次-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingFreeSession> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingFreeSession> result = new Result<MarketingFreeSession>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingFreeSessionService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "免单场次-通过id查询")
	@ApiOperation(value="免单场次-通过id查询", notes="免单场次-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingFreeSession> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingFreeSession> result = new Result<MarketingFreeSession>();
		MarketingFreeSession marketingFreeSession = marketingFreeSessionService.getById(id);
		if(marketingFreeSession==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingFreeSession);
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
      QueryWrapper<MarketingFreeSession> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingFreeSession marketingFreeSession = JSON.parseObject(deString, MarketingFreeSession.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeSession, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingFreeSession> pageList = marketingFreeSessionService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "免单场次列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingFreeSession.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("免单场次列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingFreeSession> listMarketingFreeSessions = ExcelImportUtil.importExcel(file.getInputStream(), MarketingFreeSession.class, params);
              marketingFreeSessionService.saveBatch(listMarketingFreeSessions);
              return Result.ok("文件导入成功！数据行数:" + listMarketingFreeSessions.size());
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

}
