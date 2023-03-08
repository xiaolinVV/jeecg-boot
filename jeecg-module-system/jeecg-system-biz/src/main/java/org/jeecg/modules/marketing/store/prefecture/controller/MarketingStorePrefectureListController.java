package org.jeecg.modules.marketing.store.prefecture.controller;

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
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureListService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
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
 * @Description: 店铺专区
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺专区")
@RestController
@RequestMapping("/marketing.store.prefecture/marketingStorePrefectureList")
public class MarketingStorePrefectureListController {
	@Autowired
	private IMarketingStorePrefectureListService marketingStorePrefectureListService;

	@Autowired
	private IStoreManageService iStoreManageService;


	 /**
	  *
	  * 根据店铺id获取专区列表
	  *
	  * @param storeManageId
	  * @return
	  */
	 @GetMapping("getMarketingStorePrefectureListByStoreManageId")
	public Result<?> getMarketingStorePrefectureListByStoreManageId(String storeManageId){
		return Result.ok(marketingStorePrefectureListService.getMarketingStorePrefectureListByStoreManageId(storeManageId));
	}

	 /**
	  * 获取限购专区的列表
	  *
	  * @return
	  */
	@GetMapping("getMarketingStorePrefectureList")
	public Result<?> getMarketingStorePrefectureList(String storeManageId){
		return Result.ok(marketingStorePrefectureListService.getMarketingStorePrefectureList(storeManageId));
	}
	
	/**
	  * 分页列表查询
	 * @param marketingStorePrefectureList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺专区-分页列表查询")
	@ApiOperation(value="店铺专区-分页列表查询", notes="店铺专区-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingStorePrefectureList>> queryPageList(MarketingStorePrefectureList marketingStorePrefectureList,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingStorePrefectureList>> result = new Result<IPage<MarketingStorePrefectureList>>();
		QueryWrapper<MarketingStorePrefectureList> queryWrapper = QueryGenerator.initQueryWrapper(marketingStorePrefectureList, req.getParameterMap());
		Page<MarketingStorePrefectureList> page = new Page<MarketingStorePrefectureList>(pageNo, pageSize);
		IPage<MarketingStorePrefectureList> pageList = marketingStorePrefectureListService.page(page, queryWrapper);
		pageList.getRecords().forEach(m->{
			StoreManage storeManage=iStoreManageService.getById(m.getStoreManageId());
			if (storeManage.getSubStoreName() == null) {
				m.setStoreName(storeManage.getStoreName());
			} else {
				m.setStoreName( storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
			}
		});
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingStorePrefectureList
	 * @return
	 */
	@AutoLog(value = "店铺专区-添加")
	@ApiOperation(value="店铺专区-添加", notes="店铺专区-添加")
	@PostMapping(value = "/add")
	public Result<MarketingStorePrefectureList> add(@RequestBody MarketingStorePrefectureList marketingStorePrefectureList) {
		Result<MarketingStorePrefectureList> result = new Result<MarketingStorePrefectureList>();
		try {
			marketingStorePrefectureListService.save(marketingStorePrefectureList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingStorePrefectureList
	 * @return
	 */
	@AutoLog(value = "店铺专区-编辑")
	@ApiOperation(value="店铺专区-编辑", notes="店铺专区-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingStorePrefectureList> edit(@RequestBody MarketingStorePrefectureList marketingStorePrefectureList) {
		Result<MarketingStorePrefectureList> result = new Result<MarketingStorePrefectureList>();
		MarketingStorePrefectureList marketingStorePrefectureListEntity = marketingStorePrefectureListService.getById(marketingStorePrefectureList.getId());
		if(marketingStorePrefectureListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingStorePrefectureListService.updateById(marketingStorePrefectureList);
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
	@AutoLog(value = "店铺专区-通过id删除")
	@ApiOperation(value="店铺专区-通过id删除", notes="店铺专区-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingStorePrefectureListService.removeById(id);
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
	@AutoLog(value = "店铺专区-批量删除")
	@ApiOperation(value="店铺专区-批量删除", notes="店铺专区-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingStorePrefectureList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingStorePrefectureList> result = new Result<MarketingStorePrefectureList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingStorePrefectureListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺专区-通过id查询")
	@ApiOperation(value="店铺专区-通过id查询", notes="店铺专区-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingStorePrefectureList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingStorePrefectureList> result = new Result<MarketingStorePrefectureList>();
		MarketingStorePrefectureList marketingStorePrefectureList = marketingStorePrefectureListService.getById(id);
		if(marketingStorePrefectureList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingStorePrefectureList);
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
      QueryWrapper<MarketingStorePrefectureList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingStorePrefectureList marketingStorePrefectureList = JSON.parseObject(deString, MarketingStorePrefectureList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingStorePrefectureList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingStorePrefectureList> pageList = marketingStorePrefectureListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺专区列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingStorePrefectureList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺专区列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingStorePrefectureList> listMarketingStorePrefectureLists = ExcelImportUtil.importExcel(file.getInputStream(), MarketingStorePrefectureList.class, params);
              marketingStorePrefectureListService.saveBatch(listMarketingStorePrefectureLists);
              return Result.ok("文件导入成功！数据行数:" + listMarketingStorePrefectureLists.size());
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
