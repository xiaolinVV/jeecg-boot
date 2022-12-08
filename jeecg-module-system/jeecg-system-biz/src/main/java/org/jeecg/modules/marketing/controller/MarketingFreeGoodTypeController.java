package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodList;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodType;
import org.jeecg.modules.marketing.service.IMarketingFreeGoodListService;
import org.jeecg.modules.marketing.service.IMarketingFreeGoodTypeService;
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
import java.util.List;
import java.util.Map;

 /**
 * @Description: 免单商品类型
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单商品类型")
@RestController
@RequestMapping("/marketing/marketingFreeGoodType")
public class MarketingFreeGoodTypeController {
	@Autowired
	private IMarketingFreeGoodTypeService marketingFreeGoodTypeService;

	@Autowired
	private IMarketingFreeGoodListService iMarketingFreeGoodListService;


	 /**
	  * 查询活动商品类型
	  * @return
	  */
	@RequestMapping("getAllMarketingFreeGoodType")
	@ResponseBody
	public Result<List<Map<String,Object>>> getAllMarketingFreeGoodType(){
		Result<List<Map<String,Object>>> result=new Result<>();
		result.setResult(marketingFreeGoodTypeService.getAllMarketingFreeGoodType());
		result.success("查询活动商品类型成功！！！");
		return result;
	}



	/**
	  * 分页列表查询
	 * @param marketingFreeGoodType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "免单商品类型-分页列表查询")
	@ApiOperation(value="免单商品类型-分页列表查询", notes="免单商品类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingFreeGoodType>> queryPageList(MarketingFreeGoodType marketingFreeGoodType,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingFreeGoodType>> result = new Result<IPage<MarketingFreeGoodType>>();
		QueryWrapper<MarketingFreeGoodType> queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeGoodType, req.getParameterMap());
		Page<MarketingFreeGoodType> page = new Page<MarketingFreeGoodType>(pageNo, pageSize);
		IPage<MarketingFreeGoodType> pageList = marketingFreeGoodTypeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingFreeGoodType
	 * @return
	 */
	@AutoLog(value = "免单商品类型-添加")
	@ApiOperation(value="免单商品类型-添加", notes="免单商品类型-添加")
	@PostMapping(value = "/add")
	public Result<MarketingFreeGoodType> add(@RequestBody MarketingFreeGoodType marketingFreeGoodType) {
		Result<MarketingFreeGoodType> result = new Result<MarketingFreeGoodType>();
		try {
			marketingFreeGoodTypeService.save(marketingFreeGoodType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingFreeGoodType
	 * @return
	 */
	@AutoLog(value = "免单商品类型-编辑")
	@ApiOperation(value="免单商品类型-编辑", notes="免单商品类型-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingFreeGoodType> edit(@RequestBody MarketingFreeGoodType marketingFreeGoodType) {
		Result<MarketingFreeGoodType> result = new Result<MarketingFreeGoodType>();
		MarketingFreeGoodType marketingFreeGoodTypeEntity = marketingFreeGoodTypeService.getById(marketingFreeGoodType.getId());
		if(marketingFreeGoodTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingFreeGoodTypeService.updateById(marketingFreeGoodType);
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
	@AutoLog(value = "免单商品类型-通过id删除")
	@ApiOperation(value="免单商品类型-通过id删除", notes="免单商品类型-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {

			long count=iMarketingFreeGoodListService.count(new LambdaQueryWrapper<MarketingFreeGoodList>().eq(MarketingFreeGoodList::getMarketingFreeGoodTypeId,id));
			if(count>0){
				return Result.error("您的商品类型下面还有商品，请先删除商品或者修改成其他分类!");
			}

			marketingFreeGoodTypeService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}

	 /**
	  * 启用和停用的状态修改
	  *
	  * @param id   免单商品类型id
	  * @param status   状态；0：停用；1：启用
	  * @param explain   说明
	  * @return
	  */
	@RequestMapping("startOrStop")
	@ResponseBody
	public Result<String> startOrStop(String id,String status,String explain){
		Result<String> result=new Result<>();
		marketingFreeGoodTypeService.update(new MarketingFreeGoodType().setStatus(status).setTypeExplain(explain),new LambdaUpdateWrapper<MarketingFreeGoodType>().
				eq(MarketingFreeGoodType::getId,id));

		result.success("状态修改成功！！！");
		return result;
	}


	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "免单商品类型-通过id查询")
	@ApiOperation(value="免单商品类型-通过id查询", notes="免单商品类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingFreeGoodType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingFreeGoodType> result = new Result<MarketingFreeGoodType>();
		MarketingFreeGoodType marketingFreeGoodType = marketingFreeGoodTypeService.getById(id);
		if(marketingFreeGoodType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingFreeGoodType);
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
      QueryWrapper<MarketingFreeGoodType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingFreeGoodType marketingFreeGoodType = JSON.parseObject(deString, MarketingFreeGoodType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingFreeGoodType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingFreeGoodType> pageList = marketingFreeGoodTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "免单商品类型列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingFreeGoodType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("免单商品类型列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingFreeGoodType> listMarketingFreeGoodTypes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingFreeGoodType.class, params);
              marketingFreeGoodTypeService.saveBatch(listMarketingFreeGoodTypes);
              return Result.ok("文件导入成功！数据行数:" + listMarketingFreeGoodTypes.size());
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
