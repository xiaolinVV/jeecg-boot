package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodSpecification;
import org.jeecg.modules.marketing.service.IMarketingFreeGoodSpecificationService;
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
 * @Description: 免单商品规格
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单商品规格")
@RestController
@RequestMapping("/marketing/marketingFreeGoodSpecification")
public class MarketingFreeGoodSpecificationController {
	@Autowired
	private IMarketingFreeGoodSpecificationService marketingFreeGoodSpecificationService;


	 /**
	  *
	  * @param  "[
	  *     {
	  *         "marketingFreeGoodSpecificationId":"免单商品规格id",
	  *         "freePriceProportion":"活动折扣",
	  *         "freePrice":"免单价格"
	  *     }
	  * ]"
	  * @return
	  */
	@RequestMapping("setBatchByMarketingFreeGoodSpecificationId")
	@ResponseBody
	public Result<?> setBatchByMarketingFreeGoodSpecificationId(@RequestBody String bady){
		JSON.parseArray(bady).forEach(mfsObject->{
			JSONObject mfsJsonObject=(JSONObject)mfsObject;
			MarketingFreeGoodSpecification marketingFreeGoodSpecification=marketingFreeGoodSpecificationService.getById(mfsJsonObject.getString("marketingFreeGoodSpecificationId"));
			marketingFreeGoodSpecification.setFreePriceProportion(mfsJsonObject.getBigDecimal("freePriceProportion"));
			marketingFreeGoodSpecification.setFreePrice(mfsJsonObject.getBigDecimal("freePrice"));
			marketingFreeGoodSpecificationService.saveOrUpdate(marketingFreeGoodSpecification);
		});
		return Result.ok("修改规格数据成功");
	}




	 /**
	  * 根据免单商品id查询规格数据
	  * @param marketingFreeGoodListId
	  * @return
	  */
	@RequestMapping("selectMarketingFreeGoodSpecificationByMarketingFreeGoodListId")
	@ResponseBody
	public Result<List<Map<String,Object>>> selectMarketingFreeGoodSpecificationByMarketingFreeGoodListId(String marketingFreeGoodListId){
		Result<List<Map<String,Object>>> result=new Result<>();

		//参数验证
		if(StringUtils.isBlank(marketingFreeGoodListId)){
			result.error500("免单商品id不能为空！！！");
			return result;
		}

		result.setResult(marketingFreeGoodSpecificationService.selectMarketingFreeGoodSpecificationByMarketingFreeGoodListId(marketingFreeGoodListId));

		result.success("查询规格成功！！！");

		return result;
	}

	/**
	  * 分页列表查询
	 * @param marketingFreeGoodSpecification
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "免单商品-分页列表查询")
	@ApiOperation(value="免单商品-分页列表查询", notes="免单商品-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingFreeGoodSpecification>> queryPageList(MarketingFreeGoodSpecification marketingFreeGoodSpecification,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingFreeGoodSpecification>> result = new Result<IPage<MarketingFreeGoodSpecification>>();
		QueryWrapper<MarketingFreeGoodSpecification> queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeGoodSpecification, req.getParameterMap());
		Page<MarketingFreeGoodSpecification> page = new Page<MarketingFreeGoodSpecification>(pageNo, pageSize);
		IPage<MarketingFreeGoodSpecification> pageList = marketingFreeGoodSpecificationService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingFreeGoodSpecification
	 * @return
	 */
	@AutoLog(value = "免单商品-添加")
	@ApiOperation(value="免单商品-添加", notes="免单商品-添加")
	@PostMapping(value = "/add")
	public Result<MarketingFreeGoodSpecification> add(@RequestBody MarketingFreeGoodSpecification marketingFreeGoodSpecification) {
		Result<MarketingFreeGoodSpecification> result = new Result<MarketingFreeGoodSpecification>();
		try {
			marketingFreeGoodSpecificationService.save(marketingFreeGoodSpecification);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingFreeGoodSpecification
	 * @return
	 */
	@AutoLog(value = "免单商品-编辑")
	@ApiOperation(value="免单商品-编辑", notes="免单商品-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingFreeGoodSpecification> edit(@RequestBody MarketingFreeGoodSpecification marketingFreeGoodSpecification) {
		Result<MarketingFreeGoodSpecification> result = new Result<MarketingFreeGoodSpecification>();
		MarketingFreeGoodSpecification marketingFreeGoodSpecificationEntity = marketingFreeGoodSpecificationService.getById(marketingFreeGoodSpecification.getId());
		if(marketingFreeGoodSpecificationEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingFreeGoodSpecificationService.updateById(marketingFreeGoodSpecification);
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
	@AutoLog(value = "免单商品-通过id删除")
	@ApiOperation(value="免单商品-通过id删除", notes="免单商品-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingFreeGoodSpecificationService.removeById(id);
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
	@AutoLog(value = "免单商品-批量删除")
	@ApiOperation(value="免单商品-批量删除", notes="免单商品-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingFreeGoodSpecification> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingFreeGoodSpecification> result = new Result<MarketingFreeGoodSpecification>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingFreeGoodSpecificationService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "免单商品-通过id查询")
	@ApiOperation(value="免单商品-通过id查询", notes="免单商品-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingFreeGoodSpecification> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingFreeGoodSpecification> result = new Result<MarketingFreeGoodSpecification>();
		MarketingFreeGoodSpecification marketingFreeGoodSpecification = marketingFreeGoodSpecificationService.getById(id);
		if(marketingFreeGoodSpecification==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingFreeGoodSpecification);
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
      QueryWrapper<MarketingFreeGoodSpecification> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingFreeGoodSpecification marketingFreeGoodSpecification = JSON.parseObject(deString, MarketingFreeGoodSpecification.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeGoodSpecification, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingFreeGoodSpecification> pageList = marketingFreeGoodSpecificationService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "免单商品列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingFreeGoodSpecification.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("免单商品列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingFreeGoodSpecification> listMarketingFreeGoodSpecifications = ExcelImportUtil.importExcel(file.getInputStream(), MarketingFreeGoodSpecification.class, params);
              marketingFreeGoodSpecificationService.saveBatch(listMarketingFreeGoodSpecifications);
              return Result.ok("文件导入成功！数据行数:" + listMarketingFreeGoodSpecifications.size());
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
