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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.dto.MarketingGroupGoodListDTO;
import org.jeecg.modules.marketing.entity.MarketingGroupBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodList;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodSpecification;
import org.jeecg.modules.marketing.service.IMarketingGroupBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodListService;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodSpecificationService;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 拼团商品列表
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼团商品列表")
@RestController
@RequestMapping("/marketing/marketingGroupGoodList")
public class MarketingGroupGoodListController {
	@Autowired
	private IMarketingGroupGoodListService marketingGroupGoodListService;


	 @Autowired
	 private IGoodListService iGoodListService;

	 @Autowired
	 private IGoodSpecificationService iGoodSpecificationService;

	 @Autowired
	 private IMarketingGroupGoodSpecificationService iMarketingGroupGoodSpecificationService;

	 @Autowired
	 private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;


	 /**
	  * 拼团商品停用接口
	  * 张靠勤   2021-3-31
	  * @param marketingGroupGoodIds
	  * @param explain
	  * @return
	  */
	 @RequestMapping("blockUpMarketingGroupGood")
	 @ResponseBody
	 public Result<?> blockUpMarketingGroupGood(String marketingGroupGoodIds,String explain){
		 //参数判断
		 if(StringUtils.isBlank(marketingGroupGoodIds)){
			 return Result.error("拼团商品id不能为空！！！");
		 }

		 //遍历拼团商品列表id
		 Arrays.asList(StringUtils.split(marketingGroupGoodIds,",")).forEach(marketingGroupGoodId->{
		 	MarketingGroupGoodList marketingGroupGoodList=marketingGroupGoodListService.getById(marketingGroupGoodId);
		 	marketingGroupGoodList.setStatus("0");
		 	marketingGroupGoodList.setStatusExplain(explain);
		 	marketingGroupGoodListService.saveOrUpdate(marketingGroupGoodList);
		 });

	 	return Result.ok("拼团商品停用失败！！！");
	 }


	 /**
	  * 拼团商品启用接口
	  *
	  * 张靠勤   2021-3-30
	  * @param marketingGroupGoodIds
	  * @param pattern  0：原开始时间；1：新的开始时间
	  * @param startTime  开始时间
	  * @param explain  说明
	  * @return
	  */
	 @RequestMapping("startUsingMarketingGroupGood")
	 @ResponseBody
	 public Result<?> startUsingMarketingGroupGood(String marketingGroupGoodIds,String pattern,
												   String explain,
												   @RequestParam(name = "startTime",required = false,defaultValue = "") String startTime){
		//参数判断
	 	if(StringUtils.isBlank(marketingGroupGoodIds)){
	 		return Result.error("拼团商品id不能为空！！！");
		}
		//遍历拼团商品列表id
		Arrays.asList(StringUtils.split(marketingGroupGoodIds,",")).forEach(marketingGroupGoodId->{
			MarketingGroupGoodList marketingGroupGoodList=marketingGroupGoodListService.getById(marketingGroupGoodId);
			//原开始时间
			if(pattern.equals("0")){
				if(marketingGroupGoodList.getStartTime()!=null){
					marketingGroupGoodList.setStatus("1");//启用
					marketingGroupGoodList.setStatusExplain(explain);
				}else{
					marketingGroupGoodList.setStatusExplain("没有设置开始时间，启用失败！！！");
				}
			}
			//新的开始时间
			if(pattern.equals("1")){
				try {
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(DateUtils.parseDate(startTime,"yyyy-MM-dd HH:mm:ss"));
					//按照小时计算
					if(marketingGroupGoodList.getActivityUnit().equals("1")){
						marketingGroupGoodList.setStartTime(calendar.getTime());
						calendar.add(Calendar.HOUR,marketingGroupGoodList.getActivityTime().intValue());
						marketingGroupGoodList.setEndTime(calendar.getTime());
					}
					//按照天计算
					if(marketingGroupGoodList.getActivityUnit().equals("0")){
						marketingGroupGoodList.setStartTime(calendar.getTime());
						calendar.add(Calendar.DATE,marketingGroupGoodList.getActivityTime().intValue());
						marketingGroupGoodList.setEndTime(calendar.getTime());
					}
					marketingGroupGoodList.setStatus("1");//启用
					marketingGroupGoodList.setStatusExplain(explain);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			marketingGroupGoodListService.saveOrUpdate(marketingGroupGoodList);
		});
	 	return Result.ok("拼团商品启用成功！！！");
	 }


	 /**
	  * 查询已推荐商品数量
	  *
	  * 张靠勤   2021-3-30
	  *
	  * 2021-3-31修改
	  *
	  * @return
	  */
	 @RequestMapping("updateMarketingGroupGoodByIsRecommend")
	 @ResponseBody
	 public Result<?> updateMarketingGroupGoodByIsRecommend(String marketingGroupGoodListId){
	 	Result<Integer> result=new Result<>();
	 	if(StringUtils.isBlank(marketingGroupGoodListId)){
	 		return result.error500("拼团商品id不能为空");
		}
		 MarketingGroupGoodList marketingGroupGoodList=marketingGroupGoodListService.getById(marketingGroupGoodListId);
		 if(marketingGroupGoodList.getIsRecommend().equals("1")){
			 marketingGroupGoodList.setIsRecommend("0");
		 }else{
			 long count=marketingGroupGoodListService.count(new LambdaQueryWrapper<MarketingGroupGoodList>()
					 .eq(MarketingGroupGoodList::getIsRecommend,"1"));
			 MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>());
			 if(marketingGroupBaseSetting==null){
				 return result.error500("请先进行基础设置！！！");
			 }
			 if(count>=marketingGroupBaseSetting.getRecommendCount().intValue()){
				 return result.error500("设置的推荐商品超过"+marketingGroupBaseSetting.getRecommendCount()+"个");
			 }
			 marketingGroupGoodList.setIsRecommend("1");
		 }
		 marketingGroupGoodListService.saveOrUpdate(marketingGroupGoodList);
	 	return result.success("设置商品推荐成功！！！");
	 }



	 /**
	  * 中奖拼团商品列表查询
	  *
	  *
	  * 张靠勤   2021-3-30
	  *
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("selectMarketingGroupGoodList")
	 @ResponseBody
	 public Result<IPage<Map<String,Object>>> selectMarketingFreeGoodList(MarketingGroupGoodListDTO marketingGroupGoodListDTO, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																		  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
		 Result<IPage<Map<String,Object>>> result=new Result<>();

		 Page<Map<String,Object>> page=new Page<>(pageNo, pageSize);
		 Map<String,String> paramObjectMap= Maps.newHashMap();
		 try {
			 paramObjectMap= BeanUtils.describe(marketingGroupGoodListDTO);
		 } catch (IllegalAccessException e) {
			 e.printStackTrace();
		 } catch (InvocationTargetException e) {
			 e.printStackTrace();
		 } catch (NoSuchMethodException e) {
			 e.printStackTrace();
		 }
		 result.setResult(marketingGroupGoodListService.selectMarketingGroupGoodList(page,paramObjectMap));
		 result.success("中奖拼团商品列表查询成功！！！");
		 return result;
	 }



	 /**
	  *
	  * 选择新增中奖拼团商品
	  *
	  *
	  * 张靠勤   2021-3-30
	  *
	  * @param groupGoodList     格式：
	  *   [
	  *     {
	  *         "id":"商品id",
	  *         "marketingGroupGoodTypeId":"中奖拼团商品类型id",
	  *         "numberTuxedo":"参团人数",
	  *         "tuxedoWelfarePayments":"参团积分",
	  *         "returnProportion":"返还比例",
	  *         "activityTime":"活动时间",
	  *         "activityUnit":"活动单位；0：天；1：时",
	  *         "marketingGroupGoodSpecifications":[
	  *             {
	  *                 "goodSpecificationId":"商品规格id",
	  *                 "groupPrice":"拼团价格",
	  *                 "groupPriceProportion":"活动折扣"
	  *             }
	  *         ]
	  *     }
	  * ]
	  *
	  * @return
	  */
	 @RequestMapping("chooseAddGood")
	 @ResponseBody
	 public Result<String> chooseAddGood(@RequestBody String groupGoodList){
		 Result<String> resul=new Result<>();
		 log.info("选择新增中奖拼团商品:"+groupGoodList);
		 JSONArray jsonArray=JSON.parseArray(groupGoodList);
		 jsonArray.stream().forEach(good->{
			 JSONObject jsonObject=(JSONObject) good;
			 if(jsonObject!=null){
				 //保存中奖拼团商品信息
				 MarketingGroupGoodList marketingGroupGoodList=new MarketingGroupGoodList();
				 marketingGroupGoodList.setGoodListId(jsonObject.getString("id"));
				 marketingGroupGoodList.setMarketingGroupGoodTypeId(jsonObject.getString("marketingGroupGoodTypeId"));
				 marketingGroupGoodList.setNumberTuxedo(jsonObject.getBigDecimal("numberTuxedo"));
				 marketingGroupGoodList.setReturnProportion(jsonObject.getBigDecimal("returnProportion"));
				 marketingGroupGoodList.setActivityTime(jsonObject.getBigDecimal("activityTime"));
				 marketingGroupGoodList.setActivityUnit(jsonObject.getString("activityUnit"));
				 marketingGroupGoodList.setTuxedoWelfarePayments(jsonObject.getBigDecimal("tuxedoWelfarePayments"));
				 marketingGroupGoodListService.saveOrUpdate(marketingGroupGoodList);
				 //保存中奖拼团商品规格信息
				 jsonObject.getJSONArray("marketingGroupGoodSpecifications").stream().forEach(goodSpecification->{
					 JSONObject goodSpecificationObject=(JSONObject)goodSpecification;
					 MarketingGroupGoodSpecification marketingGroupGoodSpecification=new MarketingGroupGoodSpecification();
					 marketingGroupGoodSpecification.setMarketingGroupGoodListId(marketingGroupGoodList.getId());
					 marketingGroupGoodSpecification.setGoodSpecificationId(goodSpecificationObject.getString("goodSpecificationId"));
					 marketingGroupGoodSpecification.setGroupPrice(goodSpecificationObject.getBigDecimal("groupPrice"));
					 marketingGroupGoodSpecification.setGroupPriceProportion(goodSpecificationObject.getBigDecimal("groupPriceProportion"));
					 iMarketingGroupGoodSpecificationService.saveOrUpdate(marketingGroupGoodSpecification);
				 });
			 }
		 });
		 resul.success("选择的中奖拼团商品新增成功！！！");
		 return resul;
	 }




	 /**
	  * 查询商品信息列表
	  *
	  *
	  * 张靠勤   2021-3-29
	  *
	  * @param goodNo   商品编号
	  * @param goodName  商品名称
	  * @param goodTypeId   商品类型
	  * @param discountStart  折扣开始
	  * @param discountEnd   折扣结束
	  * @param sysUserName   供应商名称
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("chooseGoodList")
	 @ResponseBody
	 public Result<IPage<Map<String,Object>>> chooseGoodList(String goodNo, String goodName, String goodTypeId, String discountStart, String discountEnd, String sysUserName, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
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
		 paramObjectMap.put("groupEqual","1");

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
	  *  编辑
	 * @param marketingGroupGoodList
	 * @return
	 */
	@AutoLog(value = "拼团基础设置-编辑")
	@ApiOperation(value="拼团基础设置-编辑", notes="拼团基础设置-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGroupGoodList> edit(@RequestBody MarketingGroupGoodList marketingGroupGoodList) {
		Result<MarketingGroupGoodList> result = new Result<MarketingGroupGoodList>();
		MarketingGroupGoodList marketingGroupGoodListEntity = marketingGroupGoodListService.getById(marketingGroupGoodList.getId());
		if(marketingGroupGoodListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGroupGoodListService.updateById(marketingGroupGoodList);
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
			marketingGroupGoodListService.removeById(id);
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
	@AutoLog(value = "拼团基础设置-批量删除")
	@ApiOperation(value="拼团基础设置-批量删除", notes="拼团基础设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGroupGoodList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGroupGoodList> result = new Result<MarketingGroupGoodList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGroupGoodListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拼团基础设置-通过id查询")
	@ApiOperation(value="拼团基础设置-通过id查询", notes="拼团基础设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGroupGoodList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGroupGoodList> result = new Result<MarketingGroupGoodList>();
		MarketingGroupGoodList marketingGroupGoodList = marketingGroupGoodListService.getById(id);
		if(marketingGroupGoodList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGroupGoodList);
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
      QueryWrapper<MarketingGroupGoodList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGroupGoodList marketingGroupGoodList = JSON.parseObject(deString, MarketingGroupGoodList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGroupGoodList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGroupGoodList> pageList = marketingGroupGoodListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "拼团基础设置列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGroupGoodList.class);
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
              List<MarketingGroupGoodList> listMarketingGroupGoodLists = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGroupGoodList.class, params);
              marketingGroupGoodListService.saveBatch(listMarketingGroupGoodLists);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGroupGoodLists.size());
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
