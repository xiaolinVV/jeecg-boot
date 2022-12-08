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
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.marketing.dto.MarketingAdvertisingPrefectureDTO;
import org.jeecg.modules.marketing.entity.MarketingAdvertisingPrefecture;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingPrefectureService;
import org.jeecg.modules.marketing.vo.MarketingAdvertisingPrefectureVO;
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
 * @Description: 专区广告
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="专区广告")
@RestController
@RequestMapping("/marketingAdvertisingPrefecture/marketingAdvertisingPrefecture")
public class MarketingAdvertisingPrefectureController {
	@Autowired
	private IMarketingAdvertisingPrefectureService marketingAdvertisingPrefectureService;
	
	/**
	  * 分页列表查询
	 * @param marketingAdvertisingPrefectureVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专区广告-分页列表查询")
	@ApiOperation(value="专区广告-分页列表查询", notes="专区广告-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingAdvertisingPrefectureDTO>> queryPageList(MarketingAdvertisingPrefectureVO marketingAdvertisingPrefectureVO,
																		String marketingPrefectureId,
																	   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																	   HttpServletRequest req) {
		Result<IPage<MarketingAdvertisingPrefectureDTO>> result = new Result<IPage<MarketingAdvertisingPrefectureDTO>>();
		Page<MarketingAdvertisingPrefecture> page = new Page<MarketingAdvertisingPrefecture>(pageNo, pageSize);
		marketingAdvertisingPrefectureVO.setMarketingPrefectureId(marketingPrefectureId);
		IPage<MarketingAdvertisingPrefectureDTO> pageList = marketingAdvertisingPrefectureService.getMarketingAdvertisingPrefectureDTO(page, marketingAdvertisingPrefectureVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingAdvertisingPrefecture
	 * @return
	 */
	@AutoLog(value = "专区广告-添加")
	@ApiOperation(value="专区广告-添加", notes="专区广告-添加")
	@PostMapping(value = "/add")
	public Result<MarketingAdvertisingPrefecture> add(@RequestBody MarketingAdvertisingPrefecture marketingAdvertisingPrefecture) {
		Result<MarketingAdvertisingPrefecture> result = new Result<MarketingAdvertisingPrefecture>();
		try {
			marketingAdvertisingPrefectureService.save(marketingAdvertisingPrefecture);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingAdvertisingPrefecture
	 * @return
	 */
	@AutoLog(value = "专区广告-编辑")
	@ApiOperation(value="专区广告-编辑", notes="专区广告-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingAdvertisingPrefecture> edit(@RequestBody MarketingAdvertisingPrefecture marketingAdvertisingPrefecture) {
		Result<MarketingAdvertisingPrefecture> result = new Result<MarketingAdvertisingPrefecture>();
		MarketingAdvertisingPrefecture marketingAdvertisingPrefectureEntity = marketingAdvertisingPrefectureService.getById(marketingAdvertisingPrefecture.getId());
		if(marketingAdvertisingPrefectureEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingAdvertisingPrefectureService.updateById(marketingAdvertisingPrefecture);
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
	@AutoLog(value = "专区广告-通过id删除")
	@ApiOperation(value="专区广告-通过id删除", notes="专区广告-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingAdvertisingPrefectureService.removeById(id);
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
	@AutoLog(value = "专区广告-批量删除")
	@ApiOperation(value="专区广告-批量删除", notes="专区广告-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingAdvertisingPrefecture> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingAdvertisingPrefecture> result = new Result<MarketingAdvertisingPrefecture>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingAdvertisingPrefectureService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专区广告-通过id查询")
	@ApiOperation(value="专区广告-通过id查询", notes="专区广告-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingAdvertisingPrefecture> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingAdvertisingPrefecture> result = new Result<MarketingAdvertisingPrefecture>();
		MarketingAdvertisingPrefecture marketingAdvertisingPrefecture = marketingAdvertisingPrefectureService.getById(id);
		if(marketingAdvertisingPrefecture==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingAdvertisingPrefecture);
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
      QueryWrapper<MarketingAdvertisingPrefecture> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingAdvertisingPrefecture marketingAdvertisingPrefecture = JSON.parseObject(deString, MarketingAdvertisingPrefecture.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingAdvertisingPrefecture, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingAdvertisingPrefecture> pageList = marketingAdvertisingPrefectureService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "专区广告列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingAdvertisingPrefecture.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("专区广告列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingAdvertisingPrefecture> listMarketingAdvertisingPrefectures = ExcelImportUtil.importExcel(file.getInputStream(), MarketingAdvertisingPrefecture.class, params);
              marketingAdvertisingPrefectureService.saveBatch(listMarketingAdvertisingPrefectures);
              return Result.ok("文件导入成功！数据行数:" + listMarketingAdvertisingPrefectures.size());
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
	 @AutoLog(value = "平台专区-通过id查询")
	 @ApiOperation(value = "平台专区-通过id查询", notes = "平台专区-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<GoodList> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status,String closeExplain) {
		 Result<GoodList> result = new Result<GoodList>();
		 if (StringUtils.isEmpty(ids)) {
			 result.error500("参数不识别！");
		 } else {
			 MarketingAdvertisingPrefecture marketingAdvertisingPrefecture;
			 try {
				 List<String> listid = Arrays.asList(ids.split(","));
				 for (String id : listid) {
					 if (status.equals("1")){
						 //判断是否可以启用
						 Map<String ,Object>  map= marketingAdvertisingPrefectureService.linkToUpdate(id);
						 //出错处理,不可启用判断
						 if(map.get("data").equals("1")){
							 return  result.error500(map.get("msg").toString());
						 }
					 }

					 marketingAdvertisingPrefecture = marketingAdvertisingPrefectureService.getById(id);
					 if (marketingAdvertisingPrefecture == null) {
						 result.error500("未找到对应实体");
					 } else {
						 marketingAdvertisingPrefecture.setCloseExplain(closeExplain);
						 marketingAdvertisingPrefecture.setStatus(status);
						 marketingAdvertisingPrefectureService.updateById(marketingAdvertisingPrefecture);
					 }
				 }
				 result.setCode(200);
				 result.success("修改成功!");
			 } catch (Exception e) {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }

}
