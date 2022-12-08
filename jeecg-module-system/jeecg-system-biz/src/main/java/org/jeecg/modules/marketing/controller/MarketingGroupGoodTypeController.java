package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodList;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodType;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodListService;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodTypeService;
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
 * @Description: 拼团商品分类
 * @Author: jeecg-boot
 * @Date:   2021-03-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼团商品分类")
@RestController
@RequestMapping("/marketing/marketingGroupGoodType")
public class MarketingGroupGoodTypeController {
	@Autowired
	private IMarketingGroupGoodTypeService marketingGroupGoodTypeService;

	@Autowired
	private IMarketingGroupGoodListService iMarketingGroupGoodListService;


	 /**
	  * 查询活动商品类型
	  * @return
	  */
	 @RequestMapping("getAllMarketingGroupGoodType")
	 @ResponseBody
	 public Result<?> getAllMarketingGroupGoodType(){
		 Result<List<Map<String,Object>>> result=new Result<>();
		 result.setResult(marketingGroupGoodTypeService.getAllMarketingGroupGoodType());
		 result.success("查询活动商品类型成功！！！");
		 return result;
	 }




	/**
	  * 分页列表查询
	 * @param marketingGroupGoodType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "拼团基础设置-分页列表查询")
	@ApiOperation(value="拼团基础设置-分页列表查询", notes="拼团基础设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGroupGoodType>> queryPageList(MarketingGroupGoodType marketingGroupGoodType,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingGroupGoodType>> result = new Result<IPage<MarketingGroupGoodType>>();
		QueryWrapper<MarketingGroupGoodType> queryWrapper = QueryGenerator.initQueryWrapper(marketingGroupGoodType, req.getParameterMap());
		Page<MarketingGroupGoodType> page = new Page<MarketingGroupGoodType>(pageNo, pageSize);
		IPage<MarketingGroupGoodType> pageList = marketingGroupGoodTypeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGroupGoodType
	 * @return
	 */
	@AutoLog(value = "拼团基础设置-添加")
	@ApiOperation(value="拼团基础设置-添加", notes="拼团基础设置-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGroupGoodType> add(@RequestBody MarketingGroupGoodType marketingGroupGoodType) {
		Result<MarketingGroupGoodType> result = new Result<MarketingGroupGoodType>();
		try {
			marketingGroupGoodTypeService.save(marketingGroupGoodType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGroupGoodType
	 * @return
	 */
	@AutoLog(value = "拼团基础设置-编辑")
	@ApiOperation(value="拼团基础设置-编辑", notes="拼团基础设置-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGroupGoodType> edit(@RequestBody MarketingGroupGoodType marketingGroupGoodType) {
		Result<MarketingGroupGoodType> result = new Result<MarketingGroupGoodType>();
		MarketingGroupGoodType marketingGroupGoodTypeEntity = marketingGroupGoodTypeService.getById(marketingGroupGoodType.getId());
		if(marketingGroupGoodTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGroupGoodTypeService.updateById(marketingGroupGoodType);
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
	@AutoLog(value = "拼团基础设置-通过id删除")
	@ApiOperation(value="拼团基础设置-通过id删除", notes="拼团基础设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {

			long count=iMarketingGroupGoodListService.count(new LambdaQueryWrapper<MarketingGroupGoodList>().eq(MarketingGroupGoodList::getMarketingGroupGoodTypeId,id));
			if(count>0){
				return Result.error("您的商品类型下面还有商品，请先删除商品或者修改成其他分类!");
			}
			marketingGroupGoodTypeService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拼团基础设置-通过id查询")
	@ApiOperation(value="拼团基础设置-通过id查询", notes="拼团基础设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGroupGoodType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGroupGoodType> result = new Result<MarketingGroupGoodType>();
		MarketingGroupGoodType marketingGroupGoodType = marketingGroupGoodTypeService.getById(id);
		if(marketingGroupGoodType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGroupGoodType);
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
      QueryWrapper<MarketingGroupGoodType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGroupGoodType marketingGroupGoodType = JSON.parseObject(deString, MarketingGroupGoodType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGroupGoodType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGroupGoodType> pageList = marketingGroupGoodTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "拼团基础设置列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGroupGoodType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拼团基础设置列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGroupGoodType> listMarketingGroupGoodTypes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGroupGoodType.class, params);
              marketingGroupGoodTypeService.saveBatch(listMarketingGroupGoodTypes);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGroupGoodTypes.size());
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
