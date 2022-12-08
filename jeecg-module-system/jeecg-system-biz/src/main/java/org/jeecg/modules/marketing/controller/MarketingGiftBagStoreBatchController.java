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
import org.jeecg.modules.marketing.entity.MarketingGiftBagStoreBatch;
import org.jeecg.modules.marketing.service.IMarketingGiftBagStoreBatchService;
import org.jeecg.modules.store.entity.StoreManage;
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
 * @Description: 礼包采购店铺映射
 * @Author: jeecg-boot
 * @Date:   2020-08-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="礼包采购店铺映射")
@RestController
@RequestMapping("/marketingGiftBagStoreBatch/marketingGiftBagStoreBatch")
public class MarketingGiftBagStoreBatchController {
	@Autowired
	private IMarketingGiftBagStoreBatchService marketingGiftBagStoreBatchService;
	
	/**
	  * 分页列表查询
	 * @param marketingGiftBagStoreBatch
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "礼包采购店铺映射-分页列表查询")
	@ApiOperation(value="礼包采购店铺映射-分页列表查询", notes="礼包采购店铺映射-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGiftBagStoreBatch>> queryPageList(MarketingGiftBagStoreBatch marketingGiftBagStoreBatch,
																   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																   HttpServletRequest req) {
		Result<IPage<MarketingGiftBagStoreBatch>> result = new Result<IPage<MarketingGiftBagStoreBatch>>();
		QueryWrapper<MarketingGiftBagStoreBatch> queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagStoreBatch, req.getParameterMap());
		Page<MarketingGiftBagStoreBatch> page = new Page<MarketingGiftBagStoreBatch>(pageNo, pageSize);
		IPage<MarketingGiftBagStoreBatch> pageList = marketingGiftBagStoreBatchService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGiftBagStoreBatch
	 * @return
	 */
	@AutoLog(value = "礼包采购店铺映射-添加")
	@ApiOperation(value="礼包采购店铺映射-添加", notes="礼包采购店铺映射-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGiftBagStoreBatch> add(@RequestBody MarketingGiftBagStoreBatch marketingGiftBagStoreBatch) {
		Result<MarketingGiftBagStoreBatch> result = new Result<MarketingGiftBagStoreBatch>();
		try {
			marketingGiftBagStoreBatchService.save(marketingGiftBagStoreBatch);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGiftBagStoreBatch
	 * @return
	 */
	@AutoLog(value = "礼包采购店铺映射-编辑")
	@ApiOperation(value="礼包采购店铺映射-编辑", notes="礼包采购店铺映射-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGiftBagStoreBatch> edit(@RequestBody MarketingGiftBagStoreBatch marketingGiftBagStoreBatch) {
		Result<MarketingGiftBagStoreBatch> result = new Result<MarketingGiftBagStoreBatch>();
		MarketingGiftBagStoreBatch marketingGiftBagStoreBatchEntity = marketingGiftBagStoreBatchService.getById(marketingGiftBagStoreBatch.getId());
		if(marketingGiftBagStoreBatchEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGiftBagStoreBatchService.updateById(marketingGiftBagStoreBatch);
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
	@AutoLog(value = "礼包采购店铺映射-通过id删除")
	@ApiOperation(value="礼包采购店铺映射-通过id删除", notes="礼包采购店铺映射-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGiftBagStoreBatchService.removeById(id);
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
	@AutoLog(value = "礼包采购店铺映射-批量删除")
	@ApiOperation(value="礼包采购店铺映射-批量删除", notes="礼包采购店铺映射-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGiftBagStoreBatch> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGiftBagStoreBatch> result = new Result<MarketingGiftBagStoreBatch>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGiftBagStoreBatchService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "礼包采购店铺映射-通过id查询")
	@ApiOperation(value="礼包采购店铺映射-通过id查询", notes="礼包采购店铺映射-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGiftBagStoreBatch> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGiftBagStoreBatch> result = new Result<MarketingGiftBagStoreBatch>();
		MarketingGiftBagStoreBatch marketingGiftBagStoreBatch = marketingGiftBagStoreBatchService.getById(id);
		if(marketingGiftBagStoreBatch==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGiftBagStoreBatch);
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
      QueryWrapper<MarketingGiftBagStoreBatch> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGiftBagStoreBatch marketingGiftBagStoreBatch = JSON.parseObject(deString, MarketingGiftBagStoreBatch.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagStoreBatch, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGiftBagStoreBatch> pageList = marketingGiftBagStoreBatchService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "礼包采购店铺映射列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGiftBagStoreBatch.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("礼包采购店铺映射列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGiftBagStoreBatch> listMarketingGiftBagStoreBatchs = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGiftBagStoreBatch.class, params);
              marketingGiftBagStoreBatchService.saveBatch(listMarketingGiftBagStoreBatchs);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGiftBagStoreBatchs.size());
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
	  * 通过礼包id获取店铺分页列表
	  * @param id
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
  @GetMapping("findGiftBagStoreById")
  public Result<IPage<StoreManage>> findGiftBagStoreById(@RequestParam(name="id",required=true) String id,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
	  Result<IPage<StoreManage>> result = new Result<>();
	  Page<StoreManage> page = new Page<StoreManage>(pageNo, pageSize);
	  result.setResult(marketingGiftBagStoreBatchService.findGiftBagStoreById(page,id));
	  return result;
  }
}
