package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.marketing.entity.MarketingFreeAnnouncement;
import org.jeecg.modules.marketing.service.IMarketingFreeAnnouncementService;
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
 * @Description: 免单公告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单公告")
@RestController
@RequestMapping("/marketing/marketingFreeAnnouncement")
public class MarketingFreeAnnouncementController {
	@Autowired
	private IMarketingFreeAnnouncementService marketingFreeAnnouncementService;




	 /**
	  * 获取强推公告数量
	  *
	  * @return
	  */
	 @RequestMapping("hasStrongPushCount")
	 @ResponseBody
	 public Result<?> hasStrongPushCount(){
		 Result<Long> result=new Result<>();
		 result.setResult(marketingFreeAnnouncementService.count(new LambdaQueryWrapper<MarketingFreeAnnouncement>().eq(MarketingFreeAnnouncement::getStrongPush,"1")));
		 result.success("查询强推公告数量");
		 return result;
	 }
	
	/**
	  * 分页列表查询
	 * @param marketingFreeAnnouncement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "免单公告-分页列表查询")
	@ApiOperation(value="免单公告-分页列表查询", notes="免单公告-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingFreeAnnouncement>> queryPageList(MarketingFreeAnnouncement marketingFreeAnnouncement,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingFreeAnnouncement>> result = new Result<IPage<MarketingFreeAnnouncement>>();
		QueryWrapper<MarketingFreeAnnouncement> queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeAnnouncement, req.getParameterMap());
		Page<MarketingFreeAnnouncement> page = new Page<MarketingFreeAnnouncement>(pageNo, pageSize);
		IPage<MarketingFreeAnnouncement> pageList = marketingFreeAnnouncementService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingFreeAnnouncement
	 * @return
	 */
	@AutoLog(value = "免单公告-添加")
	@ApiOperation(value="免单公告-添加", notes="免单公告-添加")
	@PostMapping(value = "/add")
	public Result<MarketingFreeAnnouncement> add(@RequestBody MarketingFreeAnnouncement marketingFreeAnnouncement) {
		Result<MarketingFreeAnnouncement> result = new Result<MarketingFreeAnnouncement>();
		try {
			marketingFreeAnnouncementService.save(marketingFreeAnnouncement);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingFreeAnnouncement
	 * @return
	 */
	@AutoLog(value = "免单公告-编辑")
	@ApiOperation(value="免单公告-编辑", notes="免单公告-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingFreeAnnouncement> edit(@RequestBody MarketingFreeAnnouncement marketingFreeAnnouncement) {
		Result<MarketingFreeAnnouncement> result = new Result<MarketingFreeAnnouncement>();
		MarketingFreeAnnouncement marketingFreeAnnouncementEntity = marketingFreeAnnouncementService.getById(marketingFreeAnnouncement.getId());
		if(marketingFreeAnnouncementEntity==null) {
			result.error500("未找到对应实体");
		}else {
			//控制强推只有一条
			if(marketingFreeAnnouncement.getStrongPush().equals("1")){
				marketingFreeAnnouncementService.update(new MarketingFreeAnnouncement().setStrongPush("0"),new LambdaQueryWrapper<>());
			}


			boolean ok = marketingFreeAnnouncementService.updateById(marketingFreeAnnouncement);
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
	@AutoLog(value = "免单公告-通过id删除")
	@ApiOperation(value="免单公告-通过id删除", notes="免单公告-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingFreeAnnouncementService.removeById(id);
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
	@AutoLog(value = "免单公告-批量删除")
	@ApiOperation(value="免单公告-批量删除", notes="免单公告-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingFreeAnnouncement> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingFreeAnnouncement> result = new Result<MarketingFreeAnnouncement>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingFreeAnnouncementService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "免单公告-通过id查询")
	@ApiOperation(value="免单公告-通过id查询", notes="免单公告-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingFreeAnnouncement> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingFreeAnnouncement> result = new Result<MarketingFreeAnnouncement>();
		MarketingFreeAnnouncement marketingFreeAnnouncement = marketingFreeAnnouncementService.getById(id);
		if(marketingFreeAnnouncement==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingFreeAnnouncement);
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
      QueryWrapper<MarketingFreeAnnouncement> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingFreeAnnouncement marketingFreeAnnouncement = JSON.parseObject(deString, MarketingFreeAnnouncement.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeAnnouncement, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingFreeAnnouncement> pageList = marketingFreeAnnouncementService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "免单公告列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingFreeAnnouncement.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("免单公告列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingFreeAnnouncement> listMarketingFreeAnnouncements = ExcelImportUtil.importExcel(file.getInputStream(), MarketingFreeAnnouncement.class, params);
              marketingFreeAnnouncementService.saveBatch(listMarketingFreeAnnouncements);
              return Result.ok("文件导入成功！数据行数:" + listMarketingFreeAnnouncements.size());
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
