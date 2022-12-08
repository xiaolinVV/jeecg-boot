package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.marketing.entity.MarketingMaterialColumn;
import org.jeecg.modules.marketing.entity.MarketingMaterialList;
import org.jeecg.modules.marketing.service.IMarketingMaterialColumnService;
import org.jeecg.modules.marketing.service.IMarketingMaterialListService;
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
 * @Description: 素材库栏目
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="素材库栏目")
@RestController
@RequestMapping("/marketingMaterialColumn/marketingMaterialColumn")
public class MarketingMaterialColumnController {
	@Autowired
	private IMarketingMaterialColumnService marketingMaterialColumnService;
	@Autowired
	private IMarketingMaterialListService iMarketingMaterialListService;
	/**
	  * 分页列表查询
	 * @param marketingMaterialColumn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "素材库栏目-分页列表查询")
	@ApiOperation(value="素材库栏目-分页列表查询", notes="素材库栏目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingMaterialColumn>> queryPageList(MarketingMaterialColumn marketingMaterialColumn,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingMaterialColumn>> result = new Result<IPage<MarketingMaterialColumn>>();
		QueryWrapper<MarketingMaterialColumn> queryWrapper = QueryGenerator.initQueryWrapper(marketingMaterialColumn, req.getParameterMap());
		Page<MarketingMaterialColumn> page = new Page<MarketingMaterialColumn>(pageNo, pageSize);
		queryWrapper.orderBy(true,true,"sort");
		queryWrapper.orderBy(false,false,"create_time");
		IPage<MarketingMaterialColumn> pageList = marketingMaterialColumnService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingMaterialColumn
	 * @return
	 */
	@AutoLog(value = "素材库栏目-添加")
	@ApiOperation(value="素材库栏目-添加", notes="素材库栏目-添加")
	@PostMapping(value = "/add")
	public Result<MarketingMaterialColumn> add(@RequestBody MarketingMaterialColumn marketingMaterialColumn) {
		Result<MarketingMaterialColumn> result = new Result<MarketingMaterialColumn>();
		try {
			marketingMaterialColumnService.save(marketingMaterialColumn);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingMaterialColumn
	 * @return
	 */
	@AutoLog(value = "素材库栏目-编辑")
	@ApiOperation(value="素材库栏目-编辑", notes="素材库栏目-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingMaterialColumn> edit(@RequestBody MarketingMaterialColumn marketingMaterialColumn) {
		Result<MarketingMaterialColumn> result = new Result<MarketingMaterialColumn>();
		MarketingMaterialColumn marketingMaterialColumnEntity = marketingMaterialColumnService.getById(marketingMaterialColumn.getId());
		if(marketingMaterialColumnEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingMaterialColumnService.updateById(marketingMaterialColumn);
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
	@AutoLog(value = "素材库栏目-通过id删除")
	@ApiOperation(value="素材库栏目-通过id删除", notes="素材库栏目-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingMaterialColumnService.removeById(id);
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
	@AutoLog(value = "素材库栏目-批量删除")
	@ApiOperation(value="素材库栏目-批量删除", notes="素材库栏目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingMaterialColumn> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingMaterialColumn> result = new Result<MarketingMaterialColumn>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingMaterialColumnService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "素材库栏目-通过id查询")
	@ApiOperation(value="素材库栏目-通过id查询", notes="素材库栏目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingMaterialColumn> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingMaterialColumn> result = new Result<MarketingMaterialColumn>();
		MarketingMaterialColumn marketingMaterialColumn = marketingMaterialColumnService.getById(id);
		if(marketingMaterialColumn==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingMaterialColumn);
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
      QueryWrapper<MarketingMaterialColumn> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingMaterialColumn marketingMaterialColumn = JSON.parseObject(deString, MarketingMaterialColumn.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingMaterialColumn, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingMaterialColumn> pageList = marketingMaterialColumnService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "素材库栏目列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingMaterialColumn.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("素材库栏目列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingMaterialColumn> listMarketingMaterialColumns = ExcelImportUtil.importExcel(file.getInputStream(), MarketingMaterialColumn.class, params);
              marketingMaterialColumnService.saveBatch(listMarketingMaterialColumns);
              return Result.ok("文件导入成功！数据行数:" + listMarketingMaterialColumns.size());
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
     @AutoLog(value = "素材库栏目-批量修改:启用停用")
     @ApiOperation(value = "素材库栏目-批量修改:启用停用", notes = "素材库栏目-批量修改:启用停用")
     @GetMapping(value = "/updateStatus")
     public Result<MarketingMaterialColumn> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status,String closeExplain) {
         Result<MarketingMaterialColumn> result = new Result<MarketingMaterialColumn>();
         if (StringUtils.isEmpty(ids)) {
             result.error500("参数不识别！");
         } else {
			 MarketingMaterialColumn marketingMaterialColumn;
             try {
                 List<String> listid = Arrays.asList(ids.split(","));
                 for (String id : listid) {
					 marketingMaterialColumn = marketingMaterialColumnService.getById(id);
                     if (marketingMaterialColumn == null) {
                         result.error500("未找到对应实体");
                     } else {

						 marketingMaterialColumn.setStatus(status);
						 marketingMaterialColumn.setCloseExplain(closeExplain);
						 marketingMaterialColumnService.updateById(marketingMaterialColumn);
						 if(status.equals("0")){
						 	//停用,同步停用素材
							List<MarketingMaterialList> marketingMaterialListList =  iMarketingMaterialListService.list(new LambdaQueryWrapper<MarketingMaterialList>()
									.eq(MarketingMaterialList::getMarketingMaterialColumnId,id)
									.eq(MarketingMaterialList::getIsPublish,"1"));
							 marketingMaterialListList.forEach(mml->{
								 //不发布
							     mml.setIsPublish("0");
								 iMarketingMaterialListService.updateById(mml);
							 });

						 }

                     }
                 }
                 result.success("修改成功!");
             } catch (Exception e) {
                 result.error500("修改失败！");
             }
         }
         return result;
     }

	 /**
	  * 获取素材栏目列表
	  * @return
	  */
	 @RequestMapping("getMarketingMaterialColumnListMap")
	 @ResponseBody
    public Result<List<Map<String,Object>>> getMarketingMaterialColumnListMap(){
		Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		List<Map<String,Object>> listMap = marketingMaterialColumnService.listMaps(new LambdaQueryWrapper<MarketingMaterialColumn>().select(MarketingMaterialColumn::getId,MarketingMaterialColumn::getName).eq(MarketingMaterialColumn::getStatus,"1"));
		result.setResult(listMap);
		result.success("请求成功!");
		return result;
	}
}
