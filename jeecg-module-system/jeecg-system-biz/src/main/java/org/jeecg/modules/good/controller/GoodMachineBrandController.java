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
import org.jeecg.modules.good.entity.GoodMachineBrand;
import org.jeecg.modules.good.service.IGoodMachineBrandService;
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
 * @Description: 平台商品-整机品牌
 * @Author: jeecg-boot
 * @Date:   2022-11-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="平台商品-整机品牌")
@RestController
@RequestMapping("/good/goodMachineBrand")
public class GoodMachineBrandController {
	@Autowired
	private IGoodMachineBrandService goodMachineBrandService;


	 /**
	  * 获取整机品牌
	  *
	  * @return
	  */
	@GetMapping("getGoodMachineBrandList")
	public Result<?> getGoodMachineBrandList(){
		return Result.ok(goodMachineBrandService.list());
	}

	
	/**
	  * 分页列表查询
	 * @param goodMachineBrand
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "平台商品-整机品牌-分页列表查询")
	@ApiOperation(value="平台商品-整机品牌-分页列表查询", notes="平台商品-整机品牌-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodMachineBrand>> queryPageList(GoodMachineBrand goodMachineBrand,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodMachineBrand>> result = new Result<IPage<GoodMachineBrand>>();
		QueryWrapper<GoodMachineBrand> queryWrapper = QueryGenerator.initQueryWrapper(goodMachineBrand, req.getParameterMap());
		Page<GoodMachineBrand> page = new Page<GoodMachineBrand>(pageNo, pageSize);
		IPage<GoodMachineBrand> pageList = goodMachineBrandService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodMachineBrand
	 * @return
	 */
	@AutoLog(value = "平台商品-整机品牌-添加")
	@ApiOperation(value="平台商品-整机品牌-添加", notes="平台商品-整机品牌-添加")
	@PostMapping(value = "/add")
	public Result<GoodMachineBrand> add(@RequestBody GoodMachineBrand goodMachineBrand) {
		Result<GoodMachineBrand> result = new Result<GoodMachineBrand>();
		try {
			goodMachineBrand.setSerialNumber(OrderNoUtils.getOrderNo());
			goodMachineBrandService.save(goodMachineBrand);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodMachineBrand
	 * @return
	 */
	@AutoLog(value = "平台商品-整机品牌-编辑")
	@ApiOperation(value="平台商品-整机品牌-编辑", notes="平台商品-整机品牌-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodMachineBrand> edit(@RequestBody GoodMachineBrand goodMachineBrand) {
		Result<GoodMachineBrand> result = new Result<GoodMachineBrand>();
		GoodMachineBrand goodMachineBrandEntity = goodMachineBrandService.getById(goodMachineBrand.getId());
		if(goodMachineBrandEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodMachineBrandService.updateById(goodMachineBrand);
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
	@AutoLog(value = "平台商品-整机品牌-通过id删除")
	@ApiOperation(value="平台商品-整机品牌-通过id删除", notes="平台商品-整机品牌-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodMachineBrandService.removeById(id);
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
	@AutoLog(value = "平台商品-整机品牌-批量删除")
	@ApiOperation(value="平台商品-整机品牌-批量删除", notes="平台商品-整机品牌-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodMachineBrand> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodMachineBrand> result = new Result<GoodMachineBrand>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodMachineBrandService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "平台商品-整机品牌-通过id查询")
	@ApiOperation(value="平台商品-整机品牌-通过id查询", notes="平台商品-整机品牌-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodMachineBrand> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodMachineBrand> result = new Result<GoodMachineBrand>();
		GoodMachineBrand goodMachineBrand = goodMachineBrandService.getById(id);
		if(goodMachineBrand==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodMachineBrand);
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
      QueryWrapper<GoodMachineBrand> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodMachineBrand goodMachineBrand = JSON.parseObject(deString, GoodMachineBrand.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodMachineBrand, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodMachineBrand> pageList = goodMachineBrandService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "平台商品-整机品牌列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodMachineBrand.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台商品-整机品牌列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodMachineBrand> listGoodMachineBrands = ExcelImportUtil.importExcel(file.getInputStream(), GoodMachineBrand.class, params);
              goodMachineBrandService.saveBatch(listGoodMachineBrands);
              return Result.ok("文件导入成功！数据行数:" + listGoodMachineBrands.size());
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
