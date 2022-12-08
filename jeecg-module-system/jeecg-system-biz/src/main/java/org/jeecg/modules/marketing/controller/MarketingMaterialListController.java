package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingMaterialListDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialColumn;
import org.jeecg.modules.marketing.entity.MarketingMaterialList;
import org.jeecg.modules.marketing.service.IMarketingMaterialColumnService;
import org.jeecg.modules.marketing.service.IMarketingMaterialGoodService;
import org.jeecg.modules.marketing.service.IMarketingMaterialListService;
import org.jeecg.modules.marketing.vo.MarketingMaterialListVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
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
 * @Description: 素材列表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="素材列表")
@RestController
@RequestMapping("/marketingMaterialList/marketingMaterialList")
public class MarketingMaterialListController {
	@Autowired
	private IMarketingMaterialListService marketingMaterialListService;
	@Autowired
	private IMarketingMaterialGoodService marketingMaterialGoodService;
	 @Autowired
	 private IMarketingMaterialColumnService marketingMaterialColumnService;
	/**
	  * 分页列表查询
	 * @param marketingMaterialListVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "素材列表-分页列表查询")
	@ApiOperation(value="素材列表-分页列表查询", notes="素材列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingMaterialListDTO>> queryPageList(MarketingMaterialListVO marketingMaterialListVO,
									                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		Result<IPage<MarketingMaterialListDTO>> result = new Result<IPage<MarketingMaterialListDTO>>();

		Page<MarketingMaterialList> page = new Page<MarketingMaterialList>(pageNo, pageSize);
		IPage<MarketingMaterialListDTO> pageList = marketingMaterialListService.getMarketingMaterialListDTO(page, marketingMaterialListVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingMaterialList
	 * @return
	 */
	@AutoLog(value = "素材列表-添加")
	@ApiOperation(value="素材列表-添加", notes="素材列表-添加")
	@PostMapping(value = "/add")
	public Result<MarketingMaterialList> add(@RequestBody MarketingMaterialList marketingMaterialList) {
		Result<MarketingMaterialList> result = new Result<MarketingMaterialList>();
		try {
			marketingMaterialListService.save(marketingMaterialList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingMaterialList
	 * @return
	 */
	@AutoLog(value = "素材列表-编辑")
	@ApiOperation(value="素材列表-编辑", notes="素材列表-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingMaterialList> edit(@RequestBody MarketingMaterialList marketingMaterialList) {
		Result<MarketingMaterialList> result = new Result<MarketingMaterialList>();
		MarketingMaterialList marketingMaterialListEntity = marketingMaterialListService.getById(marketingMaterialList.getId());
		if(marketingMaterialListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingMaterialListService.updateById(marketingMaterialList);
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
	@AutoLog(value = "素材列表-通过id删除")
	@ApiOperation(value="素材列表-通过id删除", notes="素材列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingMaterialListService.removeById(id);
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
	@AutoLog(value = "素材列表-批量删除")
	@ApiOperation(value="素材列表-批量删除", notes="素材列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingMaterialList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingMaterialList> result = new Result<MarketingMaterialList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingMaterialListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "素材列表-通过id查询")
	@ApiOperation(value="素材列表-通过id查询", notes="素材列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingMaterialList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingMaterialList> result = new Result<MarketingMaterialList>();
		MarketingMaterialList marketingMaterialList = marketingMaterialListService.getById(id);
		if(marketingMaterialList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingMaterialList);
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
      QueryWrapper<MarketingMaterialList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingMaterialList marketingMaterialList = JSON.parseObject(deString, MarketingMaterialList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingMaterialList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingMaterialList> pageList = marketingMaterialListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "素材列表列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingMaterialList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("素材列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingMaterialList> listMarketingMaterialLists = ExcelImportUtil.importExcel(file.getInputStream(), MarketingMaterialList.class, params);
              marketingMaterialListService.saveBatch(listMarketingMaterialLists);
              return Result.ok("文件导入成功！数据行数:" + listMarketingMaterialLists.size());
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
	  * 添加修改素材
	  * @param marketingMaterialListVO
	  * @return
	  */
	 //@RequestMapping("addMarketingMaterialList")
	 @PostMapping(value = "/addMarketingMaterialList")
	 @ResponseBody
	 public Result<MarketingMaterialList> addMarketingMaterialList(@RequestBody MarketingMaterialListVO marketingMaterialListVO){
		 Result<MarketingMaterialList> result = new Result<MarketingMaterialList>();
		 if(oConvertUtils.isEmpty(marketingMaterialListVO)){
			 result.error500("添加参数不能为空!");
		 }
		 MarketingMaterialList marketingMaterialList =  new MarketingMaterialList();

		 BeanUtils.copyProperties(marketingMaterialListVO,marketingMaterialList);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 if(StringUtils.isBlank(marketingMaterialList.getSysUserId())){
			 marketingMaterialList.setSysUserId(sysUser.getId());
		 }
		 //保存素材列表
		 marketingMaterialListService.saveOrUpdate(marketingMaterialList);
         //添加修改素材商品
		 marketingMaterialGoodService.addMarketingMaterialGood(marketingMaterialList.getId(),marketingMaterialListVO);

		 result.success("添加成功!");
		return result;

	 }



	 /**
	  * 批量修改:启用停用
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "素材列表-批量修改:发布状态")
	 @ApiOperation(value = "素材列表-批量修改:发布状态", notes = "素材列表-批量修改:发布状态")
	 @GetMapping(value = "/updateIsPublish")
	 public Result<MarketingMaterialList> updateIsPublish(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "isPublish") String isPublish) {
		 Result<MarketingMaterialList> result = new Result<MarketingMaterialList>();
		 if (StringUtils.isEmpty(ids)) {
			 result.error500("参数不识别！");
		 } else {
			 MarketingMaterialList marketingMaterialList;
			 try {
				 List<String> listid = Arrays.asList(ids.split(","));
				 for (String id : listid) {
					 marketingMaterialList = marketingMaterialListService.getById(id);
					 if (marketingMaterialList == null) {
						 return result.error500("未找到对应实体");
					 } else {
					 	if(isPublish.equals("1")){
					 		//发布素材,查询素材栏目是否被停用
							MarketingMaterialColumn	marketingMaterialColumn = marketingMaterialColumnService.getById(marketingMaterialList.getMarketingMaterialColumnId());
		                 if(oConvertUtils.isEmpty(marketingMaterialColumn)){
							return result.error500(marketingMaterialList.getTitle()+" 的素材栏目不存在!素材不可启用!");
						 }
							if(marketingMaterialColumn.getStatus().equals("0")){
								return result.error500(marketingMaterialList.getTitle()+" 的素材栏目已停用!素材不可启用!");
							}

					 	}


						 marketingMaterialList.setIsPublish(isPublish);
						 marketingMaterialListService.updateById(marketingMaterialList);
					 }
				 }
				 if(isPublish.equals("1")){
					 result.success("发布成功!");

				 }else{
					 result.success("取消发布成功!");
				 }
			 } catch (Exception e) {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }



 }
