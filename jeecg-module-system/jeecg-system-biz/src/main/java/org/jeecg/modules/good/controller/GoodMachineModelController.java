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
import org.jeecg.modules.good.entity.GoodMachineModel;
import org.jeecg.modules.good.service.IGoodMachineModelService;
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
 * @Description: 平台商品-整机型号
 * @Author: jeecg-boot
 * @Date:   2022-11-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="平台商品-整机型号")
@RestController
@RequestMapping("/good/goodMachineModel")
public class GoodMachineModelController {
	@Autowired
	private IGoodMachineModelService goodMachineModelService;


	 /**
	  * 获取整机型号
	  *
	  * @return
	  */
	@GetMapping("getGoodMachineModelList")
	public Result<?> getGoodMachineModelList(){
		return Result.ok(goodMachineModelService.list());
	}
	
	/**
	  * 分页列表查询
	 * @param goodMachineModel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "平台商品-整机型号-分页列表查询")
	@ApiOperation(value="平台商品-整机型号-分页列表查询", notes="平台商品-整机型号-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodMachineModel>> queryPageList(GoodMachineModel goodMachineModel,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodMachineModel>> result = new Result<IPage<GoodMachineModel>>();
		QueryWrapper<GoodMachineModel> queryWrapper = QueryGenerator.initQueryWrapper(goodMachineModel, req.getParameterMap());
		Page<GoodMachineModel> page = new Page<GoodMachineModel>(pageNo, pageSize);
		IPage<GoodMachineModel> pageList = goodMachineModelService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodMachineModel
	 * @return
	 */
	@AutoLog(value = "平台商品-整机型号-添加")
	@ApiOperation(value="平台商品-整机型号-添加", notes="平台商品-整机型号-添加")
	@PostMapping(value = "/add")
	public Result<GoodMachineModel> add(@RequestBody GoodMachineModel goodMachineModel) {
		Result<GoodMachineModel> result = new Result<GoodMachineModel>();
		try {
			goodMachineModel.setSerialNumber(OrderNoUtils.getOrderNo());
			goodMachineModelService.save(goodMachineModel);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodMachineModel
	 * @return
	 */
	@AutoLog(value = "平台商品-整机型号-编辑")
	@ApiOperation(value="平台商品-整机型号-编辑", notes="平台商品-整机型号-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodMachineModel> edit(@RequestBody GoodMachineModel goodMachineModel) {
		Result<GoodMachineModel> result = new Result<GoodMachineModel>();
		GoodMachineModel goodMachineModelEntity = goodMachineModelService.getById(goodMachineModel.getId());
		if(goodMachineModelEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodMachineModelService.updateById(goodMachineModel);
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
	@AutoLog(value = "平台商品-整机型号-通过id删除")
	@ApiOperation(value="平台商品-整机型号-通过id删除", notes="平台商品-整机型号-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodMachineModelService.removeById(id);
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
	@AutoLog(value = "平台商品-整机型号-批量删除")
	@ApiOperation(value="平台商品-整机型号-批量删除", notes="平台商品-整机型号-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodMachineModel> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodMachineModel> result = new Result<GoodMachineModel>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodMachineModelService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "平台商品-整机型号-通过id查询")
	@ApiOperation(value="平台商品-整机型号-通过id查询", notes="平台商品-整机型号-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodMachineModel> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodMachineModel> result = new Result<GoodMachineModel>();
		GoodMachineModel goodMachineModel = goodMachineModelService.getById(id);
		if(goodMachineModel==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodMachineModel);
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
      QueryWrapper<GoodMachineModel> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodMachineModel goodMachineModel = JSON.parseObject(deString, GoodMachineModel.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodMachineModel, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodMachineModel> pageList = goodMachineModelService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "平台商品-整机型号列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodMachineModel.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台商品-整机型号列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodMachineModel> listGoodMachineModels = ExcelImportUtil.importExcel(file.getInputStream(), GoodMachineModel.class, params);
              goodMachineModelService.saveBatch(listGoodMachineModels);
              return Result.ok("文件导入成功！数据行数:" + listGoodMachineModels.size());
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
