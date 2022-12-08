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
import org.jeecg.modules.marketing.dto.MarketingRecommendTypeDTO;
import org.jeecg.modules.marketing.entity.MarketingRecommendType;
import org.jeecg.modules.marketing.service.IMarketingRecommendTypeService;
import org.jeecg.modules.marketing.vo.MarketingRecommendTypeVO;
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
 * @Description: 推荐分类
 * @Author: jeecg-boot
 * @Date:   2019-12-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="推荐分类")
@RestController
@RequestMapping("/marketingRecommendType/marketingRecommendType")
public class MarketingRecommendTypeController {
	@Autowired
	private IMarketingRecommendTypeService marketingRecommendTypeService;
	
	/**
	  * 分页列表查询
	 * @param marketingRecommendTypeVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "推荐分类-分页列表查询")
	@ApiOperation(value="推荐分类-分页列表查询", notes="推荐分类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingRecommendTypeDTO>> queryPageList(MarketingRecommendTypeVO marketingRecommendTypeVO,
																  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																  HttpServletRequest req) {
		Result<IPage<MarketingRecommendTypeDTO>> result = new Result<IPage<MarketingRecommendTypeDTO>>();
		Page<MarketingRecommendType> page = new Page<MarketingRecommendType>(pageNo, pageSize);
		IPage<MarketingRecommendTypeDTO> pageList = marketingRecommendTypeService.getMarketingRecommendTypeDTO(page,marketingRecommendTypeVO);
		if(pageList.getRecords().size()>0){
			for (MarketingRecommendTypeDTO mrt : pageList.getRecords()) {
				//分类的三级Id处理
				//  ( getGoodTypeIdSan:三级,getGoodTypeIdTwo：二级,getGoodTypeIdOne:一级)正常情况
				// ( getGoodTypeIdSan:二级,getGoodTypeIdTwo：一级,getGoodTypeIdOne:null)特殊
				// ( getGoodTypeIdSan:一级,getGoodTypeIdTwo：null,getGoodTypeIdOne:null)特殊
				if(mrt.getGoodTypeIdTwo() ==null){//只有一级分类情况
					mrt.setGoodTypeIdOne(mrt.getGoodTypeIdThree());
					mrt.setGoodTypeOneName(mrt.getGoodTypeThreeName());
					mrt.setGoodTypeIdTwo("");
					mrt.setGoodTypeTwoName("");
					mrt.setGoodTypeIdThree("");
					mrt.setGoodTypeThreeName("");
			     }else if(mrt.getGoodTypeIdOne() == null){//有一二级分类
					mrt.setGoodTypeIdOne(mrt.getGoodTypeIdTwo());
					mrt.setGoodTypeOneName(mrt.getGoodTypeTwoName());
					mrt.setGoodTypeIdTwo(mrt.getGoodTypeIdThree());
					mrt.setGoodTypeTwoName(mrt.getGoodTypeThreeName());
					mrt.setGoodTypeIdThree("");
					mrt.setGoodTypeThreeName("");
				}
			}
		}

		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingRecommendType
	 * @return
	 */
	@AutoLog(value = "推荐分类-添加")
	@ApiOperation(value="推荐分类-添加", notes="推荐分类-添加")
	@PostMapping(value = "/add")
	public Result<MarketingRecommendType> add(@RequestBody MarketingRecommendType marketingRecommendType) {
		Result<MarketingRecommendType> result = new Result<MarketingRecommendType>();
		try {
			marketingRecommendTypeService.save(marketingRecommendType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingRecommendType
	 * @return
	 */
	@AutoLog(value = "推荐分类-编辑")
	@ApiOperation(value="推荐分类-编辑", notes="推荐分类-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingRecommendType> edit(@RequestBody MarketingRecommendType marketingRecommendType) {
		Result<MarketingRecommendType> result = new Result<MarketingRecommendType>();
		MarketingRecommendType marketingRecommendTypeEntity = marketingRecommendTypeService.getById(marketingRecommendType.getId());
		if(marketingRecommendTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingRecommendTypeService.updateById(marketingRecommendType);
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
	@AutoLog(value = "推荐分类-通过id删除")
	@ApiOperation(value="推荐分类-通过id删除", notes="推荐分类-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingRecommendTypeService.removeById(id);
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
	@AutoLog(value = "推荐分类-批量删除")
	@ApiOperation(value="推荐分类-批量删除", notes="推荐分类-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingRecommendType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingRecommendType> result = new Result<MarketingRecommendType>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingRecommendTypeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "推荐分类-通过id查询")
	@ApiOperation(value="推荐分类-通过id查询", notes="推荐分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingRecommendType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingRecommendType> result = new Result<MarketingRecommendType>();
		MarketingRecommendType marketingRecommendType = marketingRecommendTypeService.getById(id);
		if(marketingRecommendType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingRecommendType);
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
      QueryWrapper<MarketingRecommendType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingRecommendType marketingRecommendType = JSON.parseObject(deString, MarketingRecommendType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingRecommendType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingRecommendType> pageList = marketingRecommendTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "推荐分类列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingRecommendType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("推荐分类列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingRecommendType> listMarketingRecommendTypes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingRecommendType.class, params);
              marketingRecommendTypeService.saveBatch(listMarketingRecommendTypes);
              return Result.ok("文件导入成功！数据行数:" + listMarketingRecommendTypes.size());
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
	  * 通过id查询修改状态:启用停用
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "广告管理-通过id查询")
	 @ApiOperation(value="广告管理-通过id查询", notes="广告管理-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<MarketingRecommendType> updateStatus(@RequestParam(name="id",required=true) String id) {
		 Result<MarketingRecommendType> result = new Result<MarketingRecommendType>();
		 MarketingRecommendType marketingRecommendType = marketingRecommendTypeService.getById(id);
		 if(marketingRecommendType==null) {
			 result.error500("未找到对应实体");
		 }else {
			 if("1".equals(marketingRecommendType.getStatus())){
				 marketingRecommendType.setStatus("0");
			 }else{
				 marketingRecommendType.setStatus("1");
			 }
			 boolean ok = marketingRecommendTypeService.updateById(marketingRecommendType);
			 //TODO 返回false说明什么？
			 if(ok) {
				 result.success("修改成功!");
			 }else{
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }




}
