package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.entity.StoreLabel;
import org.jeecg.modules.store.entity.StoreLabelRelation;
import org.jeecg.modules.store.service.IStoreLabelRelationService;
import org.jeecg.modules.store.service.IStoreLabelService;
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
 * @Description: 店铺标签
 * @Author: jeecg-boot
 * @Date:   2021-07-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺标签")
@RestController
@RequestMapping("/store/storeLabel")
public class StoreLabelController {
	@Autowired
	private IStoreLabelService storeLabelService;

	@Autowired
	private IStoreLabelRelationService iStoreLabelRelationService;


	 /**
	  * 获取标签列表
	  *
	  * @return
	  */
	 @RequestMapping("getStoreLabel")
	 @ResponseBody
	 public Result<?> getStoreLabel(String storeManageId){

	 	//参数校验
		 if(StringUtils.isBlank(storeManageId)){
		 	return Result.error("店铺id不能为空");
		 }
	 	List<Map<String,Object>> resultList= Lists.newArrayList();
	 	List<StoreLabel> storeLabels=storeLabelService.list(new LambdaQueryWrapper<StoreLabel>().eq(StoreLabel::getStatus,"1").orderByAsc(StoreLabel::getSort));
		 for (StoreLabel storeLabel:storeLabels) {
			 Map<String,Object> storeLabelMap= Maps.newHashMap();
			 storeLabelMap.put("id",storeLabel.getId());
			 storeLabelMap.put("label",storeLabel.getLabel());
			 long count=iStoreLabelRelationService.count(new LambdaQueryWrapper<StoreLabelRelation>()
					 .eq(StoreLabelRelation::getStoreLabelId,storeLabel.getId())
					 .eq(StoreLabelRelation::getStoreManageId,storeManageId));
			 if(count==0){
				 storeLabelMap.put("isSelect","0");
			 }else{
				 storeLabelMap.put("isSelect","1");
			 }
			 resultList.add(storeLabelMap);
		 }
		return Result.ok(resultList);
	 }


	 /**
	  * 选择标签
	  *
	  * @param storeManageId
	  * @param labelIds
	  * @return
	  */
	 @RequestMapping("selectLabel")
	 @ResponseBody
	 public Result<?> selectLabel(String storeManageId,String labelIds){
	 	//参数校验
		 if(StringUtils.isBlank(storeManageId)){
			 return Result.error("店铺id不能为空");
		 }
		 if(StringUtils.isBlank(labelIds)){
			 return Result.error("标签id不能为空");
		 }
		 Arrays.asList(StringUtils.split(labelIds,",")).forEach(lid->{
			 long count=iStoreLabelRelationService.count(new LambdaQueryWrapper<StoreLabelRelation>()
					 .eq(StoreLabelRelation::getStoreLabelId,lid)
					 .eq(StoreLabelRelation::getStoreManageId,storeManageId));
			 if(count==0){
			 	StoreLabelRelation storeLabelRelation=new StoreLabelRelation();
			 	storeLabelRelation.setStoreManageId(storeManageId);
			 	storeLabelRelation.setStoreLabelId(lid);
			 	iStoreLabelRelationService.save(storeLabelRelation);
			 }
		 });
		 return Result.ok("修改完成");
	 }


	
	/**
	  * 分页列表查询
	 * @param storeLabel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺标签-分页列表查询")
	@ApiOperation(value="店铺标签-分页列表查询", notes="店铺标签-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreLabel>> queryPageList(StoreLabel storeLabel,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StoreLabel>> result = new Result<IPage<StoreLabel>>();
		QueryWrapper<StoreLabel> queryWrapper = QueryGenerator.initQueryWrapper(storeLabel, req.getParameterMap());
		Page<StoreLabel> page = new Page<StoreLabel>(pageNo, pageSize);
		IPage<StoreLabel> pageList = storeLabelService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeLabel
	 * @return
	 */
	@AutoLog(value = "店铺标签-添加")
	@ApiOperation(value="店铺标签-添加", notes="店铺标签-添加")
	@PostMapping(value = "/add")
	public Result<StoreLabel> add(@RequestBody StoreLabel storeLabel) {
		Result<StoreLabel> result = new Result<StoreLabel>();
		try {
			storeLabelService.save(storeLabel);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeLabel
	 * @return
	 */
	@AutoLog(value = "店铺标签-编辑")
	@ApiOperation(value="店铺标签-编辑", notes="店铺标签-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreLabel> edit(@RequestBody StoreLabel storeLabel) {
		Result<StoreLabel> result = new Result<StoreLabel>();
		StoreLabel storeLabelEntity = storeLabelService.getById(storeLabel.getId());
		if(storeLabelEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeLabelService.updateById(storeLabel);
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
	@AutoLog(value = "店铺标签-通过id删除")
	@ApiOperation(value="店铺标签-通过id删除", notes="店铺标签-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeLabelService.removeById(id);
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
	@AutoLog(value = "店铺标签-批量删除")
	@ApiOperation(value="店铺标签-批量删除", notes="店铺标签-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreLabel> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreLabel> result = new Result<StoreLabel>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeLabelService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺标签-通过id查询")
	@ApiOperation(value="店铺标签-通过id查询", notes="店铺标签-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreLabel> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreLabel> result = new Result<StoreLabel>();
		StoreLabel storeLabel = storeLabelService.getById(id);
		if(storeLabel==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeLabel);
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
      QueryWrapper<StoreLabel> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreLabel storeLabel = JSON.parseObject(deString, StoreLabel.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeLabel, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreLabel> pageList = storeLabelService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺标签列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreLabel.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺标签列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreLabel> listStoreLabels = ExcelImportUtil.importExcel(file.getInputStream(), StoreLabel.class, params);
              storeLabelService.saveBatch(listStoreLabels);
              return Result.ok("文件导入成功！数据行数:" + listStoreLabels.size());
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
