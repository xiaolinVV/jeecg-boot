package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingFreeWinningAnnouncementDTO;
import org.jeecg.modules.marketing.entity.MarketingFreeWinningAnnouncement;
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
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 免单中奖公告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单中奖公告")
@RestController
@RequestMapping("/marketing/marketingFreeWinningAnnouncement")
public class MarketingFreeWinningAnnouncementController {
	@Autowired
	private IMarketingFreeWinningAnnouncementService marketingFreeWinningAnnouncementService;

	 /**
	  * 查询页面中奖公告列表
	  * 张靠勤   2021-3-19
	  * @param marketingFreeWinningAnnouncementDTO
	  * @return
	  */
	 @RequestMapping("selectMarketingFreeWinningAnnouncementList")
	 @ResponseBody
	 public Result<?> selectMarketingFreeWinningAnnouncementList(MarketingFreeWinningAnnouncementDTO marketingFreeWinningAnnouncementDTO,
																 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
	 	Result<IPage<Map<String,Object>>> result=new Result<>();

	 	Page<Map<String,Object>> page=new Page<>(pageNo,pageSize);

		 try {
			 result.setResult(marketingFreeWinningAnnouncementService.selectMarketingFreeWinningAnnouncementList(page, BeanUtils.describe(marketingFreeWinningAnnouncementDTO)));
		 } catch (IllegalAccessException e) {
			 e.printStackTrace();
		 } catch (InvocationTargetException e) {
			 e.printStackTrace();
		 } catch (NoSuchMethodException e) {
			 e.printStackTrace();
		 }
		 result.success("中奖公告查询成功！！！");
	 	return result;
	 }
	
	/**
	  *   添加
	 * @param marketingFreeWinningAnnouncement
	 * @return
	 */
	@AutoLog(value = "免单中奖公告-添加")
	@ApiOperation(value="免单中奖公告-添加", notes="免单中奖公告-添加")
	@PostMapping(value = "/add")
	public Result<MarketingFreeWinningAnnouncement> add(@RequestBody MarketingFreeWinningAnnouncement marketingFreeWinningAnnouncement) {
		Result<MarketingFreeWinningAnnouncement> result = new Result<MarketingFreeWinningAnnouncement>();
		try {
			marketingFreeWinningAnnouncementService.save(marketingFreeWinningAnnouncement);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingFreeWinningAnnouncement
	 * @return
	 */
	@AutoLog(value = "免单中奖公告-编辑")
	@ApiOperation(value="免单中奖公告-编辑", notes="免单中奖公告-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingFreeWinningAnnouncement> edit(@RequestBody MarketingFreeWinningAnnouncement marketingFreeWinningAnnouncement) {
		Result<MarketingFreeWinningAnnouncement> result = new Result<MarketingFreeWinningAnnouncement>();
		MarketingFreeWinningAnnouncement marketingFreeWinningAnnouncementEntity = marketingFreeWinningAnnouncementService.getById(marketingFreeWinningAnnouncement.getId());
		if(marketingFreeWinningAnnouncementEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingFreeWinningAnnouncementService.updateById(marketingFreeWinningAnnouncement);
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
	@AutoLog(value = "免单中奖公告-通过id删除")
	@ApiOperation(value="免单中奖公告-通过id删除", notes="免单中奖公告-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingFreeWinningAnnouncementService.removeById(id);
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
	@AutoLog(value = "免单中奖公告-批量删除")
	@ApiOperation(value="免单中奖公告-批量删除", notes="免单中奖公告-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingFreeWinningAnnouncement> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingFreeWinningAnnouncement> result = new Result<MarketingFreeWinningAnnouncement>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingFreeWinningAnnouncementService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}


	 /**
	  * 修改虚拟免单数
	  * @param id  中奖公告id
	  * @param virtualFreeTimes  虚拟免单数
	  * @return
	  */
	@RequestMapping("updateVirtualFreeTimes")
	@ResponseBody
	public Result<String> updateVirtualFreeTimes(String id,BigDecimal virtualFreeTimes){
		Result<String> result=new Result<>();

		MarketingFreeWinningAnnouncement marketingFreeWinningAnnouncement=marketingFreeWinningAnnouncementService.getById(id);

		marketingFreeWinningAnnouncement.setVirtualFreeTimes(virtualFreeTimes);
		marketingFreeWinningAnnouncement.setTotalFreeTimes(marketingFreeWinningAnnouncement.getPracticalFreeTimes().add(virtualFreeTimes));
		marketingFreeWinningAnnouncementService.saveOrUpdate(marketingFreeWinningAnnouncement);

		result.success("修改虚拟免单数成功！！！");
		return result;
	}


	 /**
	  * 修改发布状态
	  * @param id  中奖公告id
	  * @return
	  */
	 @RequestMapping("updateStatus")
	 @ResponseBody
	public Result<String> updateStatus(String id){
		Result<String> result=new Result<>();
		marketingFreeWinningAnnouncementService.update(new MarketingFreeWinningAnnouncement().setStatus("1").setReleaseTime(new Date()),new LambdaUpdateWrapper<MarketingFreeWinningAnnouncement>()
				.eq(MarketingFreeWinningAnnouncement::getId,id));
		result.success("修改发布状态成功！！！");
		return result;
	}



	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "免单中奖公告-通过id查询")
	@ApiOperation(value="免单中奖公告-通过id查询", notes="免单中奖公告-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingFreeWinningAnnouncement> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingFreeWinningAnnouncement> result = new Result<MarketingFreeWinningAnnouncement>();
		MarketingFreeWinningAnnouncement marketingFreeWinningAnnouncement = marketingFreeWinningAnnouncementService.getById(id);
		if(marketingFreeWinningAnnouncement==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingFreeWinningAnnouncement);
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
      QueryWrapper<MarketingFreeWinningAnnouncement> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingFreeWinningAnnouncement marketingFreeWinningAnnouncement = JSON.parseObject(deString, MarketingFreeWinningAnnouncement.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeWinningAnnouncement, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingFreeWinningAnnouncement> pageList = marketingFreeWinningAnnouncementService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "免单中奖公告列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingFreeWinningAnnouncement.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("免单中奖公告列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingFreeWinningAnnouncement> listMarketingFreeWinningAnnouncements = ExcelImportUtil.importExcel(file.getInputStream(), MarketingFreeWinningAnnouncement.class, params);
              marketingFreeWinningAnnouncementService.saveBatch(listMarketingFreeWinningAnnouncements);
              return Result.ok("文件导入成功！数据行数:" + listMarketingFreeWinningAnnouncements.size());
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
