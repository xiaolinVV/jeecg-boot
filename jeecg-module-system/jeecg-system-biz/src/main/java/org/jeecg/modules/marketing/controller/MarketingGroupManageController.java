package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingGroupManage;
import org.jeecg.modules.marketing.service.IMarketingGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingGroupRecordService;
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
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼团管理")
@RestController
@RequestMapping("/marketing/marketingGroupManage")
public class MarketingGroupManageController {
	@Autowired
	private IMarketingGroupManageService marketingGroupManageService;

	@Autowired
	private IMarketingGroupRecordService iMarketingGroupRecordService;


	 /**
	  *拼团控制
	  *
	  * 张靠勤  2021-4-9
	  *
	  *
	  * @param marketingGroupManageId
	  * @param isSuccess   0:拼团失败，1：拼团成功
	  * @param marketingGroupJson：
	  *                          [
	  *     {
	  *         "marketingGroupRecordId":"拼团记录id",
	  *         "winning":"0:未中奖，1：中奖",
	  *         "rewardNumber":"中奖数量，中奖的可以不填，未中奖的需要填写中奖积分数量"
	  *     }
	  * ]
	  * @return
	  */
	 @RequestMapping("groupControl")
	 @ResponseBody
	public Result<?> groupControl(String marketingGroupManageId,String isSuccess,
								  @RequestParam(name = "marketingGroupJson",required = false,defaultValue = "") String marketingGroupJson){

	 	//参数验证
		 if(StringUtils.isBlank(marketingGroupManageId)){
		 	return Result.error("拼团管理id不能为空");
		 }
		 if(StringUtils.isBlank(isSuccess)){
		 	return Result.error("拼团控制不能空");
		 }
		 //拼团失败处理
		 if(isSuccess.equals("0")){
			marketingGroupManageService.closeMarketingGroupRecord(marketingGroupManageService.getById(marketingGroupManageId));
		 }
		 //拼团成功处理
		 if(isSuccess.equals("1")){
			if(StringUtils.isBlank(marketingGroupJson)){
				return Result.error("中奖控制json描述不能为空");
			}
			marketingGroupManageService.successMarketingGroupManage(marketingGroupManageId, UriUtils.decode(marketingGroupJson,"UTF-8"));
		 }

		return Result.ok("修改拼团控制成功");
	}

	
	/**
	  * 分页列表查询
	 * @param marketingGroupManage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "拼团管理-分页列表查询")
	@ApiOperation(value="拼团管理-分页列表查询", notes="拼团管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGroupManage>> queryPageList(MarketingGroupManage marketingGroupManage,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingGroupManage>> result = new Result<IPage<MarketingGroupManage>>();
		QueryWrapper<MarketingGroupManage> queryWrapper = QueryGenerator.initQueryWrapper(marketingGroupManage, req.getParameterMap());
		Page<MarketingGroupManage> page = new Page<MarketingGroupManage>(pageNo, pageSize);
		IPage<MarketingGroupManage> pageList = marketingGroupManageService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGroupManage
	 * @return
	 */
	@AutoLog(value = "拼团管理-添加")
	@ApiOperation(value="拼团管理-添加", notes="拼团管理-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGroupManage> add(@RequestBody MarketingGroupManage marketingGroupManage) {
		Result<MarketingGroupManage> result = new Result<MarketingGroupManage>();
		try {
			marketingGroupManageService.save(marketingGroupManage);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGroupManage
	 * @return
	 */
	@AutoLog(value = "拼团管理-编辑")
	@ApiOperation(value="拼团管理-编辑", notes="拼团管理-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGroupManage> edit(@RequestBody MarketingGroupManage marketingGroupManage) {
		Result<MarketingGroupManage> result = new Result<MarketingGroupManage>();
		MarketingGroupManage marketingGroupManageEntity = marketingGroupManageService.getById(marketingGroupManage.getId());
		if(marketingGroupManageEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGroupManageService.updateById(marketingGroupManage);
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
	@AutoLog(value = "拼团管理-通过id删除")
	@ApiOperation(value="拼团管理-通过id删除", notes="拼团管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGroupManageService.removeById(id);
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
	@AutoLog(value = "拼团管理-批量删除")
	@ApiOperation(value="拼团管理-批量删除", notes="拼团管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGroupManage> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGroupManage> result = new Result<MarketingGroupManage>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGroupManageService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拼团管理-通过id查询")
	@ApiOperation(value="拼团管理-通过id查询", notes="拼团管理-通过id查询")
	@RequestMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Map<String,Object>> result = new Result<>();
		MarketingGroupManage marketingGroupManage = marketingGroupManageService.getById(id);
		if(marketingGroupManage==null) {
			result.error500("未找到对应实体");
		}else {
			Map<String,Object> resultMap= Maps.newHashMap();
			resultMap.put("marketingGroupManage",marketingGroupManage);
			resultMap.put("iMarketingGroupRecords",iMarketingGroupRecordService.getMarketingGroupRecordByMarketingGroupManageId(marketingGroupManage.getId()));
			result.setResult(resultMap);
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
      QueryWrapper<MarketingGroupManage> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGroupManage marketingGroupManage = JSON.parseObject(deString, MarketingGroupManage.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGroupManage, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGroupManage> pageList = marketingGroupManageService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "拼团管理列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGroupManage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拼团管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGroupManage> listMarketingGroupManages = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGroupManage.class, params);
              marketingGroupManageService.saveBatch(listMarketingGroupManages);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGroupManages.size());
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
