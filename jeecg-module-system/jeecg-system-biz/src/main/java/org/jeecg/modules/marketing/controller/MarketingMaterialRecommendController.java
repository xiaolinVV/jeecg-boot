package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingMaterialRecommendDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialList;
import org.jeecg.modules.marketing.entity.MarketingMaterialRecommend;
import org.jeecg.modules.marketing.service.IMarketingMaterialRecommendService;
import org.jeecg.modules.marketing.vo.MarketingMaterialRecommendVO;
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
 * @Description: 素材推荐表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="素材推荐表")
@RestController
@RequestMapping("/marketingMaterialRecommend/marketingMaterialRecommend")
public class MarketingMaterialRecommendController {
	@Autowired
	private IMarketingMaterialRecommendService marketingMaterialRecommendService;
	
	/**
	  * 分页列表查询
	 * @param marketingMaterialRecommendVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "素材推荐表-分页列表查询")
	@ApiOperation(value="素材推荐表-分页列表查询", notes="素材推荐表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingMaterialRecommendDTO>> queryPageList(MarketingMaterialRecommendVO marketingMaterialRecommendVO,
																   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																   HttpServletRequest req) {
		Result<IPage<MarketingMaterialRecommendDTO>> result = new Result<IPage<MarketingMaterialRecommendDTO>>();
		Page<MarketingMaterialRecommend> page = new Page<MarketingMaterialRecommend>(pageNo, pageSize);
		IPage<MarketingMaterialRecommendDTO> pageList = marketingMaterialRecommendService.getMarketingMaterialRecommendDTO(page, marketingMaterialRecommendVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingMaterialRecommend
	 * @return
	 */
	@AutoLog(value = "素材推荐表-添加")
	@ApiOperation(value="素材推荐表-添加", notes="素材推荐表-添加")
	@PostMapping(value = "/add")
	public Result<MarketingMaterialRecommend> add(@RequestBody MarketingMaterialRecommend marketingMaterialRecommend) {
		Result<MarketingMaterialRecommend> result = new Result<MarketingMaterialRecommend>();
		try {
			marketingMaterialRecommendService.save(marketingMaterialRecommend);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingMaterialRecommend
	 * @return
	 */
	@AutoLog(value = "素材推荐表-编辑")
	@ApiOperation(value="素材推荐表-编辑", notes="素材推荐表-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingMaterialRecommend> edit(@RequestBody MarketingMaterialRecommend marketingMaterialRecommend) {
		Result<MarketingMaterialRecommend> result = new Result<MarketingMaterialRecommend>();
		MarketingMaterialRecommend marketingMaterialRecommendEntity = marketingMaterialRecommendService.getById(marketingMaterialRecommend.getId());
		if(marketingMaterialRecommendEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingMaterialRecommendService.updateById(marketingMaterialRecommend);
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
	@AutoLog(value = "素材推荐表-通过id删除")
	@ApiOperation(value="素材推荐表-通过id删除", notes="素材推荐表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingMaterialRecommendService.removeById(id);
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
	@AutoLog(value = "素材推荐表-批量删除")
	@ApiOperation(value="素材推荐表-批量删除", notes="素材推荐表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingMaterialRecommend> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingMaterialRecommend> result = new Result<MarketingMaterialRecommend>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingMaterialRecommendService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "素材推荐表-通过id查询")
	@ApiOperation(value="素材推荐表-通过id查询", notes="素材推荐表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingMaterialRecommend> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingMaterialRecommend> result = new Result<MarketingMaterialRecommend>();
		MarketingMaterialRecommend marketingMaterialRecommend = marketingMaterialRecommendService.getById(id);
		if(marketingMaterialRecommend==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingMaterialRecommend);
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
      QueryWrapper<MarketingMaterialRecommend> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingMaterialRecommend marketingMaterialRecommend = JSON.parseObject(deString, MarketingMaterialRecommend.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingMaterialRecommend, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingMaterialRecommend> pageList = marketingMaterialRecommendService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "素材推荐表列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingMaterialRecommend.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("素材推荐表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingMaterialRecommend> listMarketingMaterialRecommends = ExcelImportUtil.importExcel(file.getInputStream(), MarketingMaterialRecommend.class, params);
              marketingMaterialRecommendService.saveBatch(listMarketingMaterialRecommends);
              return Result.ok("文件导入成功！数据行数:" + listMarketingMaterialRecommends.size());
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
	  * 批量修改:启用停用
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "素材列表-批量修改:发布状态")
	 @ApiOperation(value = "素材列表-批量修改:发布状态", notes = "素材列表-批量修改:发布状态")
	 @GetMapping(value = "/updateStatus")
	 public Result<MarketingMaterialList> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status) {
		 Result<MarketingMaterialList> result = new Result<MarketingMaterialList>();
		 if (StringUtils.isEmpty(ids)) {
			 result.error500("参数不识别！");
		 } else {
			 MarketingMaterialRecommend marketingMaterialRecommend;
			 try {
				 List<String> listid = Arrays.asList(ids.split(","));
				 for (String id : listid) {
					 marketingMaterialRecommend = marketingMaterialRecommendService.getById(id);
					 if (marketingMaterialRecommend == null) {
						 result.error500("未找到对应实体");
					 } else {

						 marketingMaterialRecommend.setStatus(status);

						 marketingMaterialRecommendService.updateById(marketingMaterialRecommend);
					 }
				 }

					 result.success("修改成功!");


			 } catch (Exception e) {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }
}
