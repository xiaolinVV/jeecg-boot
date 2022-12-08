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
import org.jeecg.modules.marketing.entity.MarketingGiftBagStore;
import org.jeecg.modules.marketing.service.IMarketingGiftBagStoreService;
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
 * @Description: 礼包店铺映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags="礼包店铺映射")
@RestController
@RequestMapping("/marketingGiftBagStore/marketingGiftBagStore")
public class MarketingGiftBagStoreController {
	@Autowired
	private IMarketingGiftBagStoreService marketingGiftBagStoreService;
	
	/**
	  * 分页列表查询
	 * @param marketingGiftBagStore
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "礼包店铺映射-分页列表查询")
	@ApiOperation(value="礼包店铺映射-分页列表查询", notes="礼包店铺映射-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGiftBagStore>> queryPageList(MarketingGiftBagStore marketingGiftBagStore,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		Result<IPage<MarketingGiftBagStore>> result = new Result<IPage<MarketingGiftBagStore>>();
		QueryWrapper<MarketingGiftBagStore> queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagStore, req.getParameterMap());
		Page<MarketingGiftBagStore> page = new Page<MarketingGiftBagStore>(pageNo, pageSize);
		IPage<MarketingGiftBagStore> pageList = marketingGiftBagStoreService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGiftBagStore
	 * @return
	 */
	@AutoLog(value = "礼包店铺映射-添加")
	@ApiOperation(value="礼包店铺映射-添加", notes="礼包店铺映射-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGiftBagStore> add(@RequestBody MarketingGiftBagStore marketingGiftBagStore) {
		Result<MarketingGiftBagStore> result = new Result<MarketingGiftBagStore>();
		try {
			marketingGiftBagStoreService.save(marketingGiftBagStore);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGiftBagStore
	 * @return
	 */
	@AutoLog(value = "礼包店铺映射-编辑")
	@ApiOperation(value="礼包店铺映射-编辑", notes="礼包店铺映射-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGiftBagStore> edit(@RequestBody MarketingGiftBagStore marketingGiftBagStore) {
		Result<MarketingGiftBagStore> result = new Result<MarketingGiftBagStore>();
		MarketingGiftBagStore marketingGiftBagStoreEntity = marketingGiftBagStoreService.getById(marketingGiftBagStore.getId());
		if(marketingGiftBagStoreEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGiftBagStoreService.updateById(marketingGiftBagStore);
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
	@AutoLog(value = "礼包店铺映射-通过id删除")
	@ApiOperation(value="礼包店铺映射-通过id删除", notes="礼包店铺映射-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGiftBagStoreService.removeById(id);
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
	@AutoLog(value = "礼包店铺映射-批量删除")
	@ApiOperation(value="礼包店铺映射-批量删除", notes="礼包店铺映射-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGiftBagStore> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGiftBagStore> result = new Result<MarketingGiftBagStore>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGiftBagStoreService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "礼包店铺映射-通过id查询")
	@ApiOperation(value="礼包店铺映射-通过id查询", notes="礼包店铺映射-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGiftBagStore> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGiftBagStore> result = new Result<MarketingGiftBagStore>();
		MarketingGiftBagStore marketingGiftBagStore = marketingGiftBagStoreService.getById(id);
		if(marketingGiftBagStore==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGiftBagStore);
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
      QueryWrapper<MarketingGiftBagStore> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGiftBagStore marketingGiftBagStore = JSON.parseObject(deString, MarketingGiftBagStore.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagStore, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGiftBagStore> pageList = marketingGiftBagStoreService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "礼包店铺映射列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGiftBagStore.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("礼包店铺映射列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGiftBagStore> listMarketingGiftBagStores = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGiftBagStore.class, params);
              marketingGiftBagStoreService.saveBatch(listMarketingGiftBagStores);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGiftBagStores.size());
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
