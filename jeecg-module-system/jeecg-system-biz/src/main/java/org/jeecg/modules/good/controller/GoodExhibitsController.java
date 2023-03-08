package org.jeecg.modules.good.controller;

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
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.dto.GoodExhibitsDTO;
import org.jeecg.modules.good.entity.GoodExhibits;
import org.jeecg.modules.good.service.IGoodExhibitsService;
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
 * @Description: 展品列表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="展品列表")
@RestController
@RequestMapping("/good/goodExhibits")
public class GoodExhibitsController {
	@Autowired
	private IGoodExhibitsService goodExhibitsService;


	 /**
	  * 上下架
	  *
	  * @param id
	  * @return
	  */
	@GetMapping("upAndDown")
	public Result<?> upAndDown(String id){
		GoodExhibits goodExhibits=goodExhibitsService.getById(id);
		if(goodExhibits.getFrameStatus().equals("0")){
			goodExhibits.setFrameStatus("1");
			goodExhibitsService.saveOrUpdate(goodExhibits);
			return Result.ok("上架成功！！！");
		}
		if(goodExhibits.getFrameStatus().equals("1")){
			goodExhibits.setFrameStatus("0");
			goodExhibitsService.saveOrUpdate(goodExhibits);
			return Result.ok("下架成功！！！");
		}
		return Result.error("未知错误");
	}


	/**
	  * 分页列表查询
	 * @param goodExhibits
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "展品列表-分页列表查询")
	@ApiOperation(value="展品列表-分页列表查询", notes="展品列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GoodExhibits goodExhibits,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodExhibitsDTO>> result = new Result<IPage<GoodExhibitsDTO>>();
		QueryWrapper<GoodExhibits> queryWrapper = QueryGenerator.initQueryWrapper(goodExhibits, req.getParameterMap());
		Page<GoodExhibitsDTO> page = new Page<GoodExhibitsDTO>(pageNo, pageSize);
		IPage<GoodExhibitsDTO> pageList = goodExhibitsService.queryPageList(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodExhibits
	 * @return
	 */
	@AutoLog(value = "展品列表-添加")
	@ApiOperation(value="展品列表-添加", notes="展品列表-添加")
	@PostMapping(value = "/add")
	public Result<GoodExhibits> add(@RequestBody GoodExhibits goodExhibits) {
		Result<GoodExhibits> result = new Result<GoodExhibits>();
		try {
			goodExhibits.setSerialNumber(OrderNoUtils.getOrderNo());
			goodExhibitsService.save(goodExhibits);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodExhibits
	 * @return
	 */
	@AutoLog(value = "展品列表-编辑")
	@ApiOperation(value="展品列表-编辑", notes="展品列表-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodExhibits> edit(@RequestBody GoodExhibits goodExhibits) {
		Result<GoodExhibits> result = new Result<GoodExhibits>();
		GoodExhibits goodExhibitsEntity = goodExhibitsService.getById(goodExhibits.getId());
		if(goodExhibitsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodExhibitsService.updateById(goodExhibits);
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
	@AutoLog(value = "展品列表-通过id删除")
	@ApiOperation(value="展品列表-通过id删除", notes="展品列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodExhibitsService.removeById(id);
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
	@AutoLog(value = "展品列表-批量删除")
	@ApiOperation(value="展品列表-批量删除", notes="展品列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodExhibits> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodExhibits> result = new Result<GoodExhibits>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodExhibitsService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "展品列表-通过id查询")
	@ApiOperation(value="展品列表-通过id查询", notes="展品列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodExhibits> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodExhibits> result = new Result<GoodExhibits>();
		GoodExhibits goodExhibits = goodExhibitsService.getById(id);
		if(goodExhibits==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodExhibits);
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
      QueryWrapper<GoodExhibits> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodExhibits goodExhibits = JSON.parseObject(deString, GoodExhibits.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodExhibits, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodExhibits> pageList = goodExhibitsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "展品列表列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodExhibits.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("展品列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodExhibits> listGoodExhibitss = ExcelImportUtil.importExcel(file.getInputStream(), GoodExhibits.class, params);
              goodExhibitsService.saveBatch(listGoodExhibitss);
              return Result.ok("文件导入成功！数据行数:" + listGoodExhibitss.size());
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
