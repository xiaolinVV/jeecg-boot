package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.dto.MarketingLeagueGoodListDTO;
import org.jeecg.modules.marketing.entity.MarketingLeagueGoodList;
import org.jeecg.modules.marketing.entity.MarketingLeagueGoodSpecification;
import org.jeecg.modules.marketing.service.IMarketingLeagueGoodListService;
import org.jeecg.modules.marketing.service.IMarketingLeagueGoodSpecificationService;
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
 * @Description: 加盟专区-商品列表
 * @Author: jeecg-boot
 * @Date:   2021-12-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="加盟专区-商品列表")
@RestController
@RequestMapping("/marketing/marketingLeagueGoodList")
public class MarketingLeagueGoodListController {
	@Autowired
	private IMarketingLeagueGoodListService marketingLeagueGoodListService;

	@Autowired
	private IMarketingLeagueGoodSpecificationService iMarketingLeagueGoodSpecificationService;

	@Autowired
	private IGoodListService iGoodListService;

	@Autowired
	private IGoodSpecificationService iGoodSpecificationService;
	
	/**
	  * 分页列表查询
	 * @param marketingLeagueGoodListDTO
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "加盟专区-商品列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingLeagueGoodListDTO marketingLeagueGoodListDTO,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(marketingLeagueGoodListService.queryPageList(new Page<>(pageNo,pageSize),marketingLeagueGoodListDTO));
	}

	 /**
	  *
	  * 选择新增加盟专区商品
	  *
	  * @param leagueGoodList     格式：
	  *             [
	  *     {
	  *         "id":"商品id",
	  *         "marketingLeagueTypeId":"专区类型id",
	  *         "marketingLeagueGoodSpecification":[
	  *             {
	  *                 "goodSpecificationId":"商品规格id",
	  *                 "leaguePrice":"活动价格"
	  *             }
	  *         ]
	  *
	  *     }
	  * ]
	  * @return
	  */
	 @RequestMapping("chooseAddGood")
	 @ResponseBody
	 public Result<String> chooseAddGood(@RequestBody String leagueGoodList){
		 Result<String> resul=new Result<>();
		 log.info("选择新增专区商品:"+leagueGoodList);
		 JSONArray jsonArray=JSON.parseArray(leagueGoodList);
		 jsonArray.stream().forEach(good->{
			 JSONObject jsonObject=(JSONObject) good;
			 if(jsonObject!=null){
				 //保存免单商品信息
				 MarketingLeagueGoodList marketingLeagueeGoodList=new MarketingLeagueGoodList();
				 marketingLeagueeGoodList.setGoodListId(jsonObject.getString("id"));
				 marketingLeagueeGoodList.setMarketingLeagueTypeId(jsonObject.getString("marketingLeagueTypeId"));
				 //屏蔽已经存在的商品
				 long count=marketingLeagueGoodListService.count(new LambdaQueryWrapper<MarketingLeagueGoodList>()
						 .eq(MarketingLeagueGoodList::getGoodListId,marketingLeagueeGoodList.getGoodListId()));
				 if(count==0) {
					 marketingLeagueGoodListService.saveOrUpdate(marketingLeagueeGoodList);
					 //保存免单规格信息
					 jsonObject.getJSONArray("marketingLeagueGoodSpecification").stream().forEach(goodSpecification -> {
						 JSONObject goodSpecificationObject = (JSONObject) goodSpecification;
						 MarketingLeagueGoodSpecification marketingLeagueGoodSpecification = new MarketingLeagueGoodSpecification();
						 marketingLeagueGoodSpecification.setMarketingLeagueGoodListId(marketingLeagueeGoodList.getId());
						 marketingLeagueGoodSpecification.setGoodSpecificationId(goodSpecificationObject.getString("goodSpecificationId"));
						 marketingLeagueGoodSpecification.setLeaguePrice(goodSpecificationObject.getBigDecimal("leaguePrice"));
						 iMarketingLeagueGoodSpecificationService.saveOrUpdate(marketingLeagueGoodSpecification);
					 });
				 }else{
					 log.info("选择新增加盟专区商品，屏蔽相同的商品id："+jsonObject.getString("id")+"；加盟专区分类id："+jsonObject.getString("marketingLeagueTypeId"));
				 }
			 }
		 });
		 resul.success("选择的加盟专区商品新增成功！！！");
		 return resul;
	 }

	 /**
	  * 查询商品信息列表
	  *
	  * @param goodNo   商品编号
	  * @param goodName  商品名称
	  * @param goodTypeId   商品类型
	  * @param discountStart  折扣开始
	  * @param discountEnd   折扣结束
	  * @param sysUserName   供应商名称
	  * @param pattern   -1：全部，0:未选择，1：已选择
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("chooseGoodList")
	 @ResponseBody
	 public Result<IPage<Map<String,Object>>> chooseGoodList(Integer pattern, String goodNo, String goodName, String goodTypeId, String discountStart, String discountEnd, String sysUserName, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
		 Result<IPage<Map<String,Object>>> result=new Result<>();

		 //组织查询参数
		 Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
		 Map<String,Object> paramObjectMap= Maps.newHashMap();
		 paramObjectMap.put("goodNo",goodNo);
		 paramObjectMap.put("goodTypeId",goodTypeId);
		 paramObjectMap.put("discountStart",discountStart);
		 paramObjectMap.put("discountEnd",discountEnd);
		 paramObjectMap.put("sysUserName",sysUserName);
		 paramObjectMap.put("goodName",goodName);
		 paramObjectMap.put("pattern",pattern);

		 //选择商品信息查询
		 IPage<Map<String,Object>> mapIPage=iGoodListService.chooseGoodList(page,paramObjectMap);

		 //查询商品规格信息
		 mapIPage.getRecords().forEach(goodMap->{
			 goodMap.put("goodSpecificationList",iGoodSpecificationService.chooseSpecificationByGoodId(goodMap.get("id").toString()));
		 });

		 result.setResult(mapIPage);
		 result.success("查询商品列表成功");
		 return result;
	 }





	/**
	  *   添加
	 * @param marketingLeagueGoodList
	 * @return
	 */
	@AutoLog(value = "加盟专区-商品列表-添加")
	@ApiOperation(value="加盟专区-商品列表-添加", notes="加盟专区-商品列表-添加")
	@PostMapping(value = "/add")
	public Result<MarketingLeagueGoodList> add(@RequestBody MarketingLeagueGoodList marketingLeagueGoodList) {
		Result<MarketingLeagueGoodList> result = new Result<MarketingLeagueGoodList>();
		try {
			marketingLeagueGoodListService.save(marketingLeagueGoodList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingLeagueGoodList
	 * @return
	 */
	@AutoLog(value = "加盟专区-商品列表-编辑")
	@ApiOperation(value="加盟专区-商品列表-编辑", notes="加盟专区-商品列表-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingLeagueGoodList> edit(@RequestBody MarketingLeagueGoodList marketingLeagueGoodList) {
		Result<MarketingLeagueGoodList> result = new Result<MarketingLeagueGoodList>();
		MarketingLeagueGoodList marketingLeagueGoodListEntity = marketingLeagueGoodListService.getById(marketingLeagueGoodList.getId());
		if(marketingLeagueGoodListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingLeagueGoodListService.updateById(marketingLeagueGoodList);
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
	@AutoLog(value = "加盟专区-商品列表-通过id删除")
	@ApiOperation(value="加盟专区-商品列表-通过id删除", notes="加盟专区-商品列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingLeagueGoodListService.removeById(id);
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
	@AutoLog(value = "加盟专区-商品列表-批量删除")
	@ApiOperation(value="加盟专区-商品列表-批量删除", notes="加盟专区-商品列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingLeagueGoodList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingLeagueGoodList> result = new Result<MarketingLeagueGoodList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingLeagueGoodListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "加盟专区-商品列表-通过id查询")
	@ApiOperation(value="加盟专区-商品列表-通过id查询", notes="加盟专区-商品列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingLeagueGoodList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingLeagueGoodList> result = new Result<MarketingLeagueGoodList>();
		MarketingLeagueGoodList marketingLeagueGoodList = marketingLeagueGoodListService.getById(id);
		if(marketingLeagueGoodList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingLeagueGoodList);
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
      QueryWrapper<MarketingLeagueGoodList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingLeagueGoodList marketingLeagueGoodList = JSON.parseObject(deString, MarketingLeagueGoodList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingLeagueGoodList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingLeagueGoodList> pageList = marketingLeagueGoodListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "加盟专区-商品列表列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingLeagueGoodList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("加盟专区-商品列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingLeagueGoodList> listMarketingLeagueGoodLists = ExcelImportUtil.importExcel(file.getInputStream(), MarketingLeagueGoodList.class, params);
              marketingLeagueGoodListService.saveBatch(listMarketingLeagueGoodLists);
              return Result.ok("文件导入成功！数据行数:" + listMarketingLeagueGoodLists.size());
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
