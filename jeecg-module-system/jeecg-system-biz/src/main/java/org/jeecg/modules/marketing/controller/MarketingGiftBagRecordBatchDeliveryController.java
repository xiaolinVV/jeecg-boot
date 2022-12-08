package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatchDelivery;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchDeliveryService;
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
 * @Description: 采购礼包配送信息
 * @Author: jeecg-boot
 * @Date:   2020-09-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="采购礼包配送信息")
@RestController
@RequestMapping("/marketingGiftBagRecordBatchDelivery/marketingGiftBagRecordBatchDelivery")
public class MarketingGiftBagRecordBatchDeliveryController {
	@Autowired
	private IMarketingGiftBagRecordBatchDeliveryService marketingGiftBagRecordBatchDeliveryService;
	
	/**
	  * 分页列表查询
	 * @param marketingGiftBagRecordBatchDelivery
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购礼包配送信息-分页列表查询")
	@ApiOperation(value="采购礼包配送信息-分页列表查询", notes="采购礼包配送信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGiftBagRecordBatchDelivery>> queryPageList(MarketingGiftBagRecordBatchDelivery marketingGiftBagRecordBatchDelivery,
																			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																			HttpServletRequest req) {
		Result<IPage<MarketingGiftBagRecordBatchDelivery>> result = new Result<IPage<MarketingGiftBagRecordBatchDelivery>>();
		QueryWrapper<MarketingGiftBagRecordBatchDelivery> queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagRecordBatchDelivery, req.getParameterMap());
		Page<MarketingGiftBagRecordBatchDelivery> page = new Page<MarketingGiftBagRecordBatchDelivery>(pageNo, pageSize);
		IPage<MarketingGiftBagRecordBatchDelivery> pageList = marketingGiftBagRecordBatchDeliveryService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGiftBagRecordBatchDelivery
	 * @return
	 */
	@AutoLog(value = "采购礼包配送信息-添加")
	@ApiOperation(value="采购礼包配送信息-添加", notes="采购礼包配送信息-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGiftBagRecordBatchDelivery> add(@RequestBody MarketingGiftBagRecordBatchDelivery marketingGiftBagRecordBatchDelivery) {
		Result<MarketingGiftBagRecordBatchDelivery> result = new Result<MarketingGiftBagRecordBatchDelivery>();
		try {
			marketingGiftBagRecordBatchDeliveryService.save(marketingGiftBagRecordBatchDelivery);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGiftBagRecordBatchDelivery
	 * @return
	 */
	@AutoLog(value = "采购礼包配送信息-编辑")
	@ApiOperation(value="采购礼包配送信息-编辑", notes="采购礼包配送信息-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGiftBagRecordBatchDelivery> edit(@RequestBody MarketingGiftBagRecordBatchDelivery marketingGiftBagRecordBatchDelivery) {
		Result<MarketingGiftBagRecordBatchDelivery> result = new Result<MarketingGiftBagRecordBatchDelivery>();
		MarketingGiftBagRecordBatchDelivery marketingGiftBagRecordBatchDeliveryEntity = marketingGiftBagRecordBatchDeliveryService.getById(marketingGiftBagRecordBatchDelivery.getId());
		if(marketingGiftBagRecordBatchDeliveryEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGiftBagRecordBatchDeliveryService.updateById(marketingGiftBagRecordBatchDelivery);
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
	@AutoLog(value = "采购礼包配送信息-通过id删除")
	@ApiOperation(value="采购礼包配送信息-通过id删除", notes="采购礼包配送信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGiftBagRecordBatchDeliveryService.removeById(id);
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
	@AutoLog(value = "采购礼包配送信息-批量删除")
	@ApiOperation(value="采购礼包配送信息-批量删除", notes="采购礼包配送信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGiftBagRecordBatchDelivery> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGiftBagRecordBatchDelivery> result = new Result<MarketingGiftBagRecordBatchDelivery>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGiftBagRecordBatchDeliveryService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购礼包配送信息-通过id查询")
	@ApiOperation(value="采购礼包配送信息-通过id查询", notes="采购礼包配送信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGiftBagRecordBatchDelivery> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGiftBagRecordBatchDelivery> result = new Result<MarketingGiftBagRecordBatchDelivery>();
		MarketingGiftBagRecordBatchDelivery marketingGiftBagRecordBatchDelivery = marketingGiftBagRecordBatchDeliveryService.getById(id);
		if(marketingGiftBagRecordBatchDelivery==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGiftBagRecordBatchDelivery);
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
      QueryWrapper<MarketingGiftBagRecordBatchDelivery> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGiftBagRecordBatchDelivery marketingGiftBagRecordBatchDelivery = JSON.parseObject(deString, MarketingGiftBagRecordBatchDelivery.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagRecordBatchDelivery, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGiftBagRecordBatchDelivery> pageList = marketingGiftBagRecordBatchDeliveryService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购礼包配送信息列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGiftBagRecordBatchDelivery.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购礼包配送信息列表数据", "导出人:Jeecg", "导出信息"));
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
  public Result<?> importExcel(String id,
							   HttpServletRequest request,
							   HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

	  MarketingGiftBagRecordBatchDelivery marketingGiftBagRecordBatchDelivery = marketingGiftBagRecordBatchDeliveryService.getById(id);
	  for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<MarketingGiftBagRecordBatchDelivery> listMarketingGiftBagRecordBatchDeliverys = ExcelImportUtil.importExcel(file.getInputStream(), Map.class, params);
              listMarketingGiftBagRecordBatchDeliverys.forEach(lmgr->{
              	lmgr.setPid(marketingGiftBagRecordBatchDelivery.getId())
						.setMarketingGiftBagRecordBatchId(marketingGiftBagRecordBatchDelivery.getMarketingGiftBagRecordBatchId());
			  });
              marketingGiftBagRecordBatchDeliveryService.saveBatch(listMarketingGiftBagRecordBatchDeliverys);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGiftBagRecordBatchDeliverys.size());
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
  @GetMapping("findMarketingGiftBagRecordBatchDeliveryMap")
  public Result<List<Map<String,Object>>> findMarketingGiftBagRecordBatchDeliveryMap(@RequestParam("marketingGiftBagRecordBatchId")String marketingGiftBagRecordBatchId){
	  Result<List<Map<String, Object>>> result = new Result<>();
	  result.setResult(marketingGiftBagRecordBatchDeliveryService.findMarketingGiftBagRecordBatchDeliveryMap(marketingGiftBagRecordBatchId));
	  return result;
  }
}
