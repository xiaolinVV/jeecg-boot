package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingFreeAdvertising;
import org.jeecg.modules.marketing.service.IMarketingFreeAdvertisingService;
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
 * @Description: 免单广告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单广告")
@RestController
@RequestMapping("/marketing/marketingFreeAdvertising")
public class MarketingFreeAdvertisingController {
	@Autowired
	private IMarketingFreeAdvertisingService marketingFreeAdvertisingService;
	
	/**
	  * 分页列表查询
	 * @param marketingFreeAdvertising
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "免单广告-分页列表查询")
	@ApiOperation(value="免单广告-分页列表查询", notes="免单广告-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingFreeAdvertising>> queryPageList(MarketingFreeAdvertising marketingFreeAdvertising,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingFreeAdvertising>> result = new Result<IPage<MarketingFreeAdvertising>>();
		QueryWrapper<MarketingFreeAdvertising> queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeAdvertising, req.getParameterMap());
		Page<MarketingFreeAdvertising> page = new Page<MarketingFreeAdvertising>(pageNo, pageSize);
		IPage<MarketingFreeAdvertising> pageList = marketingFreeAdvertisingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingFreeAdvertising
	 * @return
	 */
	@AutoLog(value = "免单广告-添加")
	@ApiOperation(value="免单广告-添加", notes="免单广告-添加")
	@PostMapping(value = "/add")
	public Result<MarketingFreeAdvertising> add(@RequestBody MarketingFreeAdvertising marketingFreeAdvertising) {
		Result<MarketingFreeAdvertising> result = new Result<MarketingFreeAdvertising>();
		try {
			marketingFreeAdvertisingService.save(marketingFreeAdvertising);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingFreeAdvertising
	 * @return
	 */
	@AutoLog(value = "免单广告-编辑")
	@ApiOperation(value="免单广告-编辑", notes="免单广告-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingFreeAdvertising> edit(@RequestBody MarketingFreeAdvertising marketingFreeAdvertising) {
		Result<MarketingFreeAdvertising> result = new Result<MarketingFreeAdvertising>();
		MarketingFreeAdvertising marketingFreeAdvertisingEntity = marketingFreeAdvertisingService.getById(marketingFreeAdvertising.getId());
		if(marketingFreeAdvertisingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingFreeAdvertisingService.updateById(marketingFreeAdvertising);
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
	@AutoLog(value = "免单广告-通过id删除")
	@ApiOperation(value="免单广告-通过id删除", notes="免单广告-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingFreeAdvertisingService.removeById(id);
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
	@AutoLog(value = "免单广告-批量删除")
	@ApiOperation(value="免单广告-批量删除", notes="免单广告-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingFreeAdvertising> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingFreeAdvertising> result = new Result<MarketingFreeAdvertising>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingFreeAdvertisingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}


	 /**
	  * 启用和停用的状态修改
	  *
	  * @param id   免单广告id
	  * @param status   状态；0：停用；1：启用
	  * @param explain   说明
	  * @return
	  */
	 @RequestMapping("startOrStop")
	 @ResponseBody
	 public Result<String> startOrStop(String id,String status,String explain){
		 Result<String> result=new Result<>();
		 marketingFreeAdvertisingService.update(new MarketingFreeAdvertising().setStatus(status).setStatusExplain(explain),new LambdaUpdateWrapper<MarketingFreeAdvertising>().
				 eq(MarketingFreeAdvertising::getId,id));

		 result.success("状态修改成功！！！");
		 return result;
	 }


	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "免单广告-通过id查询")
	@ApiOperation(value="免单广告-通过id查询", notes="免单广告-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingFreeAdvertising> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingFreeAdvertising> result = new Result<MarketingFreeAdvertising>();
		MarketingFreeAdvertising marketingFreeAdvertising = marketingFreeAdvertisingService.getById(id);
		if(marketingFreeAdvertising==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingFreeAdvertising);
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
      QueryWrapper<MarketingFreeAdvertising> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingFreeAdvertising marketingFreeAdvertising = JSON.parseObject(deString, MarketingFreeAdvertising.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeAdvertising, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingFreeAdvertising> pageList = marketingFreeAdvertisingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "免单广告列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingFreeAdvertising.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("免单广告列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingFreeAdvertising> listMarketingFreeAdvertisings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingFreeAdvertising.class, params);
              marketingFreeAdvertisingService.saveBatch(listMarketingFreeAdvertisings);
              return Result.ok("文件导入成功！数据行数:" + listMarketingFreeAdvertisings.size());
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
