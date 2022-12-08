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
import org.jeecg.modules.marketing.dto.MarketingPrefectureRecommendDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureRecommend;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendService;
import org.jeecg.modules.marketing.vo.MarketingPrefectureRecommendVO;
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
 * @Description: 专区推荐
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
@Slf4j
@Api(tags="专区推荐")
@RestController
@RequestMapping("/marketingPrefectureRecommend/marketingPrefectureRecommend")
public class MarketingPrefectureRecommendController {
	@Autowired
	private IMarketingPrefectureRecommendService marketingPrefectureRecommendService;
	
	/**
	  * 分页列表查询
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专区推荐-分页列表查询")
	@ApiOperation(value="专区推荐-分页列表查询", notes="专区推荐-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingPrefectureRecommendVO>> queryPageList(MarketingPrefectureRecommendDTO marketingPrefectureRecommendDTO,
																	 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																	 HttpServletRequest req) {
		Result<IPage<MarketingPrefectureRecommendVO>> result = new Result<IPage<MarketingPrefectureRecommendVO>>();
		Page<MarketingPrefectureRecommendVO> page = new Page<MarketingPrefectureRecommendVO>(pageNo, pageSize);
		IPage<MarketingPrefectureRecommendVO> pageList = marketingPrefectureRecommendService.queryPageList(page, marketingPrefectureRecommendDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingPrefectureRecommend
	 * @return
	 */
	@AutoLog(value = "专区推荐-添加")
	@ApiOperation(value="专区推荐-添加", notes="专区推荐-添加")
	@PostMapping(value = "/add")
	public Result<MarketingPrefectureRecommend> add(@RequestBody MarketingPrefectureRecommend marketingPrefectureRecommend) {
		Result<MarketingPrefectureRecommend> result = new Result<MarketingPrefectureRecommend>();
		try {
			marketingPrefectureRecommendService.save(marketingPrefectureRecommend);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingPrefectureRecommend
	 * @return
	 */
	@AutoLog(value = "专区推荐-编辑")
	@ApiOperation(value="专区推荐-编辑", notes="专区推荐-编辑")
	@PostMapping(value = "/edit")
	public Result<MarketingPrefectureRecommend> edit(@RequestBody MarketingPrefectureRecommend marketingPrefectureRecommend) {
		Result<MarketingPrefectureRecommend> result = new Result<MarketingPrefectureRecommend>();
		MarketingPrefectureRecommend marketingPrefectureRecommendEntity = marketingPrefectureRecommendService.getById(marketingPrefectureRecommend.getId());
		if(marketingPrefectureRecommendEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingPrefectureRecommendService.updateById(marketingPrefectureRecommend);
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
	@AutoLog(value = "专区推荐-通过id删除")
	@ApiOperation(value="专区推荐-通过id删除", notes="专区推荐-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingPrefectureRecommendService.removeById(id);
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
	@AutoLog(value = "专区推荐-批量删除")
	@ApiOperation(value="专区推荐-批量删除", notes="专区推荐-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingPrefectureRecommend> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingPrefectureRecommend> result = new Result<MarketingPrefectureRecommend>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingPrefectureRecommendService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专区推荐-通过id查询")
	@ApiOperation(value="专区推荐-通过id查询", notes="专区推荐-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingPrefectureRecommend> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingPrefectureRecommend> result = new Result<MarketingPrefectureRecommend>();
		MarketingPrefectureRecommend marketingPrefectureRecommend = marketingPrefectureRecommendService.getById(id);
		if(marketingPrefectureRecommend==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingPrefectureRecommend);
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
      QueryWrapper<MarketingPrefectureRecommend> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingPrefectureRecommend marketingPrefectureRecommend = JSON.parseObject(deString, MarketingPrefectureRecommend.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingPrefectureRecommend, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingPrefectureRecommend> pageList = marketingPrefectureRecommendService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "专区推荐列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingPrefectureRecommend.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("专区推荐列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingPrefectureRecommend> listMarketingPrefectureRecommends = ExcelImportUtil.importExcel(file.getInputStream(), MarketingPrefectureRecommend.class, params);
              marketingPrefectureRecommendService.saveBatch(listMarketingPrefectureRecommends);
              return Result.ok("文件导入成功！数据行数:" + listMarketingPrefectureRecommends.size());
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
	  * 通过专区id获取专区推荐
	  * @param marketingPrefectureId
	  * @return
	  */
  @GetMapping("getMarketingPrefectureRecommendList")
  public Result<List<Map<String,Object>>> getMarketingPrefectureRecommendList(@RequestParam("marketingPrefectureId")String marketingPrefectureId){
	  Result<List<Map<String, Object>>> result = new Result<>();
	  result.setResult(marketingPrefectureRecommendService.getMarketingPrefectureRecommendList(marketingPrefectureId));
	  return result;
  }
}
