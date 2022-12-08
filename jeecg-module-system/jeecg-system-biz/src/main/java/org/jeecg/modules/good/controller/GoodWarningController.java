package org.jeecg.modules.good.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.entity.GoodWarning;
import org.jeecg.modules.good.service.IGoodWarningService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 商品预警
 * @Author: jeecg-boot
 * @Date:   2021-05-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="商品预警")
@RestController
@RequestMapping("/good/goodWarning")
public class GoodWarningController {
	@Autowired
	private IGoodWarningService goodWarningService;
	
	/**
	  * 分页列表查询
	 * @param goodWarning
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "商品预警-分页列表查询")
	@ApiOperation(value="商品预警-分页列表查询", notes="商品预警-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodWarning>> queryPageList(GoodWarning goodWarning,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodWarning>> result = new Result<IPage<GoodWarning>>();
		QueryWrapper<GoodWarning> queryWrapper = QueryGenerator.initQueryWrapper(goodWarning, req.getParameterMap());
		Page<GoodWarning> page = new Page<GoodWarning>(pageNo, pageSize);
		IPage<GoodWarning> pageList = goodWarningService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodWarning
	 * @return
	 */
	@AutoLog(value = "商品预警-添加")
	@ApiOperation(value="商品预警-添加", notes="商品预警-添加")
	@PostMapping(value = "/add")
	public Result<GoodWarning> add(@RequestBody GoodWarning goodWarning) {
		Result<GoodWarning> result = new Result<GoodWarning>();
		try {
			goodWarningService.save(goodWarning);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodWarning
	 * @return
	 */
	@AutoLog(value = "商品预警-编辑")
	@ApiOperation(value="商品预警-编辑", notes="商品预警-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodWarning> edit(@RequestBody GoodWarning goodWarning) {
		Result<GoodWarning> result = new Result<GoodWarning>();
		GoodWarning goodWarningEntity = goodWarningService.getById(goodWarning.getId());
		if(goodWarningEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodWarningService.updateById(goodWarning);
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
	@AutoLog(value = "商品预警-通过id删除")
	@ApiOperation(value="商品预警-通过id删除", notes="商品预警-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodWarningService.removeById(id);
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
	@AutoLog(value = "商品预警-批量删除")
	@ApiOperation(value="商品预警-批量删除", notes="商品预警-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodWarning> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodWarning> result = new Result<GoodWarning>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodWarningService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品预警-通过id查询")
	@ApiOperation(value="商品预警-通过id查询", notes="商品预警-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodWarning> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodWarning> result = new Result<GoodWarning>();
		GoodWarning goodWarning = goodWarningService.getById(id);
		if(goodWarning==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodWarning);
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
      QueryWrapper<GoodWarning> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodWarning goodWarning = JSON.parseObject(deString, GoodWarning.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodWarning, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodWarning> pageList = goodWarningService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "商品预警列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodWarning.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品预警列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodWarning> listGoodWarnings = ExcelImportUtil.importExcel(file.getInputStream(), GoodWarning.class, params);
              goodWarningService.saveBatch(listGoodWarnings);
              return Result.ok("文件导入成功！数据行数:" + listGoodWarnings.size());
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
