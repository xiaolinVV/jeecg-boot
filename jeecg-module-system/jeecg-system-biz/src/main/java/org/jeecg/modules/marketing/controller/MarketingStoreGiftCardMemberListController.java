package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.MapHandleUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardMemberList;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardMemberListService;
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
 * @Description: 店铺会员礼品卡
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺会员礼品卡")
@RestController
@RequestMapping("/marketing/marketingStoreGiftCardMemberList")
public class MarketingStoreGiftCardMemberListController {
	@Autowired
	private IMarketingStoreGiftCardMemberListService marketingStoreGiftCardMemberListService;
	
	/**
	  * 分页列表查询
	 * @param marketingStoreGiftCardMemberList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺会员礼品卡-分页列表查询")
	@ApiOperation(value="店铺会员礼品卡-分页列表查询", notes="店铺会员礼品卡-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Map<String, Object> paramMap = MapHandleUtils.handleRequestMap(req.getParameterMap());
		return Result.ok(marketingStoreGiftCardMemberListService.queryPageList(new Page<>(pageNo, pageSize),
				QueryGenerator.initQueryWrapper(marketingStoreGiftCardMemberList, req.getParameterMap()),paramMap));
	}

	 @AutoLog(value = "赠送记录列表")
	 @ApiOperation(value="赠送记录列表", notes="赠送记录列表")
	 @GetMapping(value = "/giveList")
	 public Result<?> giveList(MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Map<String, Object> paramMap = MapHandleUtils.handleRequestMap(req.getParameterMap());
		 return Result.ok(marketingStoreGiftCardMemberListService.giveList(new Page<>(pageNo, pageSize),
				 QueryGenerator.initQueryWrapper(marketingStoreGiftCardMemberList, req.getParameterMap())
						 .eq("status","1").eq("ways_obtain","1")
				 ,paramMap));
	 }
	
	/**
	  *   添加
	 * @param marketingStoreGiftCardMemberList
	 * @return
	 */
	@AutoLog(value = "店铺会员礼品卡-添加")
	@ApiOperation(value="店铺会员礼品卡-添加", notes="店铺会员礼品卡-添加")
	@PostMapping(value = "/add")
	public Result<MarketingStoreGiftCardMemberList> add(@RequestBody MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList) {
		Result<MarketingStoreGiftCardMemberList> result = new Result<MarketingStoreGiftCardMemberList>();
		try {
			marketingStoreGiftCardMemberListService.save(marketingStoreGiftCardMemberList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingStoreGiftCardMemberList
	 * @return
	 */
	@AutoLog(value = "店铺会员礼品卡-编辑")
	@ApiOperation(value="店铺会员礼品卡-编辑", notes="店铺会员礼品卡-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingStoreGiftCardMemberList> edit(@RequestBody MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList) {
		Result<MarketingStoreGiftCardMemberList> result = new Result<MarketingStoreGiftCardMemberList>();
		MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberListEntity = marketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberList.getId());
		if(marketingStoreGiftCardMemberListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingStoreGiftCardMemberListService.updateById(marketingStoreGiftCardMemberList);
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
	@AutoLog(value = "店铺会员礼品卡-通过id删除")
	@ApiOperation(value="店铺会员礼品卡-通过id删除", notes="店铺会员礼品卡-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingStoreGiftCardMemberListService.removeById(id);
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
	@AutoLog(value = "店铺会员礼品卡-批量删除")
	@ApiOperation(value="店铺会员礼品卡-批量删除", notes="店铺会员礼品卡-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingStoreGiftCardMemberList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingStoreGiftCardMemberList> result = new Result<MarketingStoreGiftCardMemberList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingStoreGiftCardMemberListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺会员礼品卡-通过id查询")
	@ApiOperation(value="店铺会员礼品卡-通过id查询", notes="店铺会员礼品卡-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingStoreGiftCardMemberList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingStoreGiftCardMemberList> result = new Result<MarketingStoreGiftCardMemberList>();
		MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList = marketingStoreGiftCardMemberListService.getById(id);
		if(marketingStoreGiftCardMemberList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingStoreGiftCardMemberList);
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
      QueryWrapper<MarketingStoreGiftCardMemberList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList = JSON.parseObject(deString, MarketingStoreGiftCardMemberList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingStoreGiftCardMemberList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingStoreGiftCardMemberList> pageList = marketingStoreGiftCardMemberListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺会员礼品卡列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingStoreGiftCardMemberList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺会员礼品卡列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingStoreGiftCardMemberList> listMarketingStoreGiftCardMemberLists = ExcelImportUtil.importExcel(file.getInputStream(), MarketingStoreGiftCardMemberList.class, params);
              marketingStoreGiftCardMemberListService.saveBatch(listMarketingStoreGiftCardMemberLists);
              return Result.ok("文件导入成功！数据行数:" + listMarketingStoreGiftCardMemberLists.size());
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
