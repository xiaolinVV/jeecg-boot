package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingPrefectureGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodSpecificationService;
import org.jeecg.modules.marketing.vo.MarketingPrefectureGoodSpecificationVO;
import org.jeecg.modules.marketing.vo.MarketingPrefectureGoodVO;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="专区商品")
@RestController
@RequestMapping("/marketingPrefectureGood/marketingPrefectureGood")
public class MarketingPrefectureGoodController {
	@Autowired
	private IMarketingPrefectureGoodService marketingPrefectureGoodService;
	@Autowired
	private IMarketingPrefectureGoodSpecificationService marketingPrefectureGoodSpecificationService;
	
	/**
	  * 分页列表查询
	 * @param marketingPrefectureGoodVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专区商品-分页列表查询")
	@ApiOperation(value="专区商品-分页列表查询", notes="专区商品-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingPrefectureGoodDTO>> queryPageList(MarketingPrefectureGoodVO marketingPrefectureGoodVO,
																   String marketingPrefectureId,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		marketingPrefectureGoodVO.setMarketingPrefectureId(marketingPrefectureId);
		Result<IPage<MarketingPrefectureGoodDTO>> result = new Result<IPage<MarketingPrefectureGoodDTO>>();
		Page<MarketingPrefectureGood> page = new Page<MarketingPrefectureGood>(pageNo, pageSize);
		IPage<MarketingPrefectureGoodDTO> pageList = marketingPrefectureGoodService.getMarketingPrefectureGood(page, marketingPrefectureGoodVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingPrefectureGood
	 * @return
	 */
	@AutoLog(value = "专区商品-添加")
	@ApiOperation(value="专区商品-添加", notes="专区商品-添加")
	@PostMapping(value = "/add")
	public Result<MarketingPrefectureGood> add(@RequestBody MarketingPrefectureGood marketingPrefectureGood) {
		Result<MarketingPrefectureGood> result = new Result<MarketingPrefectureGood>();
		try {
			marketingPrefectureGoodService.save(marketingPrefectureGood);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingPrefectureGood
	 * @return
	 */
	@AutoLog(value = "专区商品-编辑")
	@ApiOperation(value="专区商品-编辑", notes="专区商品-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingPrefectureGood> edit(@RequestBody MarketingPrefectureGood marketingPrefectureGood) {
		Result<MarketingPrefectureGood> result = new Result<MarketingPrefectureGood>();
		MarketingPrefectureGood marketingPrefectureGoodEntity = marketingPrefectureGoodService.getById(marketingPrefectureGood.getId());
		if(marketingPrefectureGoodEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingPrefectureGoodService.updateById(marketingPrefectureGood);
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
	@AutoLog(value = "专区商品-通过id删除")
	@ApiOperation(value="专区商品-通过id删除", notes="专区商品-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			MarketingPrefectureGood	marketingPrefectureGood = marketingPrefectureGoodService.getById(id);
			if(marketingPrefectureGood !=null){
				marketingPrefectureGoodService.linkToDelete(id,marketingPrefectureGood.getMarketingPrefectureId(),marketingPrefectureGood.getGoodListId());
			}

			marketingPrefectureGoodService.removeById(id);
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
	@AutoLog(value = "专区商品-批量删除")
	@ApiOperation(value="专区商品-批量删除", notes="专区商品-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingPrefectureGood> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingPrefectureGood> result = new Result<MarketingPrefectureGood>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			 List<String> idList = Arrays.asList(ids.split(","));
			for(String id:idList){
				MarketingPrefectureGood	marketingPrefectureGood = marketingPrefectureGoodService.getById(id);
				if(marketingPrefectureGood !=null){
					marketingPrefectureGoodService.linkToDelete(id,marketingPrefectureGood.getMarketingPrefectureId(),marketingPrefectureGood.getGoodListId());
				}
			}

			this.marketingPrefectureGoodService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专区商品-通过id查询")
	@ApiOperation(value="专区商品-通过id查询", notes="专区商品-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingPrefectureGood> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingPrefectureGood> result = new Result<MarketingPrefectureGood>();
		MarketingPrefectureGood marketingPrefectureGood = marketingPrefectureGoodService.getById(id);
		if(marketingPrefectureGood==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingPrefectureGood);
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
      QueryWrapper<MarketingPrefectureGood> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingPrefectureGood marketingPrefectureGood = JSON.parseObject(deString, MarketingPrefectureGood.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingPrefectureGood, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingPrefectureGood> pageList = marketingPrefectureGoodService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "专区商品列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingPrefectureGood.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("专区商品列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingPrefectureGood> listMarketingPrefectureGoods = ExcelImportUtil.importExcel(file.getInputStream(), MarketingPrefectureGood.class, params);
              marketingPrefectureGoodService.saveBatch(listMarketingPrefectureGoods);
              return Result.ok("文件导入成功！数据行数:" + listMarketingPrefectureGoods.size());
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
	 @AutoLog(value = "专区商品-通过id查询")
	 @ApiOperation(value = "专区商品-通过id查询", notes = "专区商品-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<MarketingPrefectureGood> updateStatus(@RequestParam(name = "ids", required = true) String ids,
														 @RequestParam(name = "status") String status,
														 @RequestParam(name = "closeExplain") String closeExplain) {
		 Result<MarketingPrefectureGood> result = new Result<MarketingPrefectureGood>();
		 if (StringUtils.isEmpty(ids)) {
			 result.error500("参数不识别！");
		 } else {

			 try {
				 List<String> listid = Arrays.asList(ids.split(","));
				 for (String id : listid) {
					 MarketingPrefectureGood marketingPrefectureGood = marketingPrefectureGoodService.getById(id);

					 if (marketingPrefectureGood == null) {
						 result.error500("未找到对应实体");
					 } else {
						 if (status.equals("1")){
							 //判断是否可以启用
						Map<String ,Object>	  map= marketingPrefectureGoodService.linkToUpdate(id);
							  //出错处理,不可启用判断
							  if(map.get("data").equals("1")){
								  return  result.error500(map.get("msg").toString());
							  }
						 }

						 marketingPrefectureGood.setCloseExplian(closeExplain);
						 marketingPrefectureGood.setStatus(status);
						 marketingPrefectureGoodService.updateById(marketingPrefectureGood);
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
	  * 批量添加专区商品
	  * @param marketingPrefectureGoodVOListStr
	  * @param req
	  * @return
	  */
	@PostMapping(value = "/addAllMarketingPrefectureGoodAndSpecification")
  public Result<MarketingPrefectureGood>  addAllMarketingPrefectureGoodAndSpecification(@RequestBody String marketingPrefectureGoodVOListStr,
                                                                                        HttpServletRequest req){
	  Result<MarketingPrefectureGood> result = new Result<MarketingPrefectureGood>();
	   if(StringUtils.isBlank(marketingPrefectureGoodVOListStr)){
		   result.error500("添加参数不能为空!");
		   return  result;
	   }
	  JSONArray jsonArray = JSONArray.parseArray(marketingPrefectureGoodVOListStr);
	   List<MarketingPrefectureGoodVO> marketingPrefectureGoodVOList = jsonArray.toJavaList(MarketingPrefectureGoodVO.class);
	  if (marketingPrefectureGoodVOList.size() ==0){
		   result.error500("添加参数不能为空!");
		   return  result;
	   }
       QueryWrapper<MarketingPrefectureGood> queryWrapperMarketingPrefectureGood =new QueryWrapper<>();
	    for(MarketingPrefectureGoodVO marketingPrefectureGoodVO:marketingPrefectureGoodVOList){
			if(StringUtils.isBlank(marketingPrefectureGoodVO.getMarketingPrefectureId())){
				result.error500("专区id不能为空!");
				return  result;
			}
			if(StringUtils.isBlank(marketingPrefectureGoodVO.getGoodListId())){
				result.error500("商品id不能为空!");
				return  result;
			}
		 queryWrapperMarketingPrefectureGood =new QueryWrapper<>();
		 queryWrapperMarketingPrefectureGood.eq("marketing_prefecture_id",marketingPrefectureGoodVO.getMarketingPrefectureId());
		 queryWrapperMarketingPrefectureGood.eq("good_list_id",marketingPrefectureGoodVO.getGoodListId());
		 //查询该商品是否已在活动内
		 List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapperMarketingPrefectureGood);

		 if(marketingPrefectureGoodList.size() ==0){
			 //未在该专区活动内,添加商品操作
			 addMarketingPrefectureGoodAndSpecification(marketingPrefectureGoodVO);
			}
	    }
		result.setCode(200);
	    result.setMessage("添加成功!");
	  return result;
  }


	 /**
	  * 保存商品跟规格
	  * @param marketingPrefectureGoodVO
	  */
  public void addMarketingPrefectureGoodAndSpecification(MarketingPrefectureGoodVO marketingPrefectureGoodVO){
	  if(marketingPrefectureGoodVO!=null){
	  	 MarketingPrefectureGood marketingPrefectureGood = new MarketingPrefectureGood();
		  marketingPrefectureGood.setMarketingPrefectureId(marketingPrefectureGoodVO.getMarketingPrefectureId());//平台专区id
		  if (marketingPrefectureGoodVO.getMarketingPrefectureTypeId().contains(",")){
			  List<String> strings = Arrays.asList(StringUtils.split(marketingPrefectureGoodVO.getMarketingPrefectureTypeId(), ","));
			  marketingPrefectureGood.setMarketingPrefectureTypeId(strings.get(1));
		  }else {
			  marketingPrefectureGood.setMarketingPrefectureTypeId(marketingPrefectureGoodVO.getMarketingPrefectureTypeId());//平台专区类型id
		  }
		  marketingPrefectureGood.setGoodListId(marketingPrefectureGoodVO.getGoodListId());//商品列表id
		  marketingPrefectureGood.setStatus("1");//状态；0：停用；1：启用
		  marketingPrefectureGood.setSrcStatus("1");//原商品是否可用；0：不可用；1：可用
		  BigDecimal[] ArrPrefecturePrice =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()]  ;//专区价
		  BigDecimal[] ArrWelfareProportion =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()] ;//福利金限额抵扣比例
          BigDecimal[] ArrGiveWelfareProportion = new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()] ;
          BigDecimal[] ArrBuyProportionDay =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()] ;//可购买件数；-1：不限制；其他代表件数
		  BigDecimal[] ArrBuyProportionLetter =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()] ;  //可购买件数；-1：不限制；其他代表件数

          String isWelfare="";//福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣
		  String isGiveWelfare=""; //赠送福利金；0：不支持；1：支持
		  String isVipLower = "";//vip会员免福利金；0：不免；1：免
		  for(int a=0;marketingPrefectureGoodVO.getGoodListSpecificationList().size()>a;a++ ){
			  isWelfare = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getIsWelfare();
			  isGiveWelfare = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getIsGiveWelfare();
			  isVipLower = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getIsVipLower();
			  ArrPrefecturePrice[a] = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getPrefecturePrice();
			  ArrWelfareProportion[a] = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getWelfareProportion();//福利金限额抵扣比例
              ArrGiveWelfareProportion[a] = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getGiveWelfareProportion();
			  ArrBuyProportionDay[a] =  marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getBuyProportionDay();
			  ArrBuyProportionLetter[a] = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getBuyProportionLetter();
		  }
		  marketingPrefectureGood.setIsWelfare(isWelfare);
		  marketingPrefectureGood.setIsGiveWelfare(isGiveWelfare);
		  marketingPrefectureGood.setIsVipLower(isVipLower) ;
		  //marketingPrefectureGood.setIsWelfare(marketingPrefectureGoodVO.)

		  ArrPrefecturePrice= maopaoSort(ArrPrefecturePrice);//专区价
		  if(ArrPrefecturePrice[0].compareTo(ArrPrefecturePrice[ArrPrefecturePrice.length-1])==0){//两数相等
			  marketingPrefectureGood.setPrefecturePrice(ArrPrefecturePrice[0].toString());//商品销售价格
			  marketingPrefectureGood.setSmallPrefecturePrice(ArrPrefecturePrice[0]);
		  }else{
			  marketingPrefectureGood.setPrefecturePrice(ArrPrefecturePrice[0]+"--"+ArrPrefecturePrice[ArrPrefecturePrice.length-1]);
			  marketingPrefectureGood.setSmallPrefecturePrice(ArrPrefecturePrice[0]);
		  }
		  if(!"0".equals(isWelfare)){
		  ArrWelfareProportion =maopaoSort(ArrWelfareProportion);//福利金限额抵扣比例
		  if(ArrWelfareProportion[0].compareTo(ArrWelfareProportion[ArrWelfareProportion.length-1])==0){//两数相等
			  marketingPrefectureGood.setWelfareProportion(ArrWelfareProportion[0].toString());//商品销售价格

		  }else{
			  marketingPrefectureGood.setWelfareProportion(ArrWelfareProportion[0]+"--"+ArrWelfareProportion[ArrWelfareProportion.length-1]);
		  }
		  }
		  if("1".equals(isGiveWelfare)){
			  ArrGiveWelfareProportion=maopaoSort(ArrGiveWelfareProportion);//福利金限额抵扣比例
			  if(ArrGiveWelfareProportion[0].compareTo(ArrGiveWelfareProportion[ArrGiveWelfareProportion.length-1])==0){//两数相等
				  marketingPrefectureGood.setGiveWelfareProportion(ArrGiveWelfareProportion[0].toString());//商品销售价格

			  }else{
				  marketingPrefectureGood.setGiveWelfareProportion(ArrGiveWelfareProportion[0]+"--"+ArrGiveWelfareProportion[ArrGiveWelfareProportion.length-1]);
			  }
		  }

		  ArrBuyProportionDay =maopaoSort(ArrBuyProportionDay);//可购买件数；-1：不限制；其他代表件数
		  if(ArrBuyProportionDay[0].compareTo(ArrBuyProportionDay[ArrBuyProportionDay.length-1])==0){//两数相等
			  marketingPrefectureGood.setBuyProportionDay(ArrBuyProportionDay[0].toString());//商品销售价格

		  }else{
			  marketingPrefectureGood.setBuyProportionDay(ArrBuyProportionDay[0]+"--"+ArrBuyProportionDay[ArrBuyProportionDay.length-1]);
		  }
		  ArrBuyProportionLetter =maopaoSort(ArrBuyProportionLetter);  //可购买件数；-1：不限制；其他代表件数
		  if(ArrBuyProportionLetter[0].compareTo(ArrBuyProportionLetter[ArrBuyProportionLetter.length-1])==0){//两数相等
			  marketingPrefectureGood.setBuyProportionLetter(ArrBuyProportionLetter[0].toString());//商品销售价格

		  }else{
			  marketingPrefectureGood.setBuyProportionLetter(ArrBuyProportionLetter[0]+"--"+ArrBuyProportionLetter[ArrBuyProportionLetter.length-1]);
		  }
		  marketingPrefectureGoodService.save(marketingPrefectureGood);
		  //添加专区规格数据
		  for(MarketingPrefectureGoodSpecificationVO marketingPrefectureGoodSpecificationVO:marketingPrefectureGoodVO.getGoodListSpecificationList()){
			  MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification = new MarketingPrefectureGoodSpecification();
			  marketingPrefectureGoodSpecification.setDelFlag("0");
			  marketingPrefectureGoodSpecification.setMarketingPrefectureGoodId(marketingPrefectureGood.getId());//商品id
			  marketingPrefectureGoodSpecification.setGoodSpecificationId(marketingPrefectureGoodSpecificationVO.getGoodSpecificationId());//规格id
			  marketingPrefectureGoodSpecification.setPrefecturePrice(marketingPrefectureGoodSpecificationVO.getPrefecturePrice());//专区价
			  marketingPrefectureGoodSpecification.setPrefecturePriceProportion(marketingPrefectureGoodSpecificationVO.getPrefecturePriceProportion());//专区折扣
			  marketingPrefectureGoodSpecification.setIsWelfare(marketingPrefectureGoodSpecificationVO.getIsWelfare());//福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣
			  marketingPrefectureGoodSpecification.setWelfareProportion(marketingPrefectureGoodSpecificationVO.getWelfareProportion());//福利金限额抵扣比例
			  marketingPrefectureGoodSpecification.setIsGiveWelfare(marketingPrefectureGoodSpecificationVO.getIsGiveWelfare());//赠送福利金；0：不支持；1：支持
              marketingPrefectureGoodSpecification.setIsVipLower(marketingPrefectureGoodSpecificationVO.getIsVipLower());//vip会员免福利金；0：不支持；1：支持
              marketingPrefectureGoodSpecification.setGiveWelfareProportion(marketingPrefectureGoodSpecificationVO.getGiveWelfareProportion());//赠送福利金比例
			  marketingPrefectureGoodSpecification.setBuyProportionDay(marketingPrefectureGoodSpecificationVO.getBuyProportionDay());//购买天数;-1：不限制；其他代表天数
			  marketingPrefectureGoodSpecification.setBuyProportionLetter(marketingPrefectureGoodSpecificationVO.getBuyProportionLetter());//可购买件数；-1：不限制；其他代表件数
			  marketingPrefectureGoodSpecification.setProportionIntegral(marketingPrefectureGoodSpecificationVO.getProportionIntegral());
			  marketingPrefectureGoodSpecificationService.save(marketingPrefectureGoodSpecification);
		  }


	  }

  }


	 /**
	  *修改更新数据
	  * @param marketingPrefectureGoodVO
	  * @return
	  */
	 @PutMapping(value = "/updateMarketingPrefectureGoodAndSpecification")
   public Result<?> updateMarketingPrefectureGoodAndSpecification(@RequestBody MarketingPrefectureGoodVO marketingPrefectureGoodVO){
	   Result<MarketingPrefectureGood> result = new Result<MarketingPrefectureGood>();
	   if(marketingPrefectureGoodVO ==null){
		   result.error500("修改参数不能为空!");
		   return result;
	   }

		   MarketingPrefectureGood marketingPrefectureGood = marketingPrefectureGoodService.getById(marketingPrefectureGoodVO.getId())  ;
	   	  if(marketingPrefectureGood == null){
			  result.error500("未找到专区商品数据!");
			  return result;
		  }
	   	  try{
			  //修改专区商品数据
			  marketingPrefectureGoodService.updateMarketingPrefectureGoodAndSpecification(marketingPrefectureGoodVO,marketingPrefectureGood);

		  }catch (Exception e){
	   	  	e.printStackTrace();
			  result.error500("修改异常!价格比例超出");
			  return result;
		  }

	   result.setCode(200);
	   result.setMessage("修改成功!");
	   return result;
   }

	 /**
	  *查询专区商品返回商品id 商品名称
	  * @param marketingPrefectureId
	  * @return
	  */
	@GetMapping(value = "/getMarketingPrefectureGoodName")
   public Result<?> getMarketingPrefectureGoodName(String marketingPrefectureId){
	   Result<List<Map<String, Object>>> result = new Result<>();
	    if(StringUtils.isBlank(marketingPrefectureId)){
			result.error500("专区id不能为空!");
			return result;
		}
	   List<Map<String, Object>> objectMapList = marketingPrefectureGoodService.getMarketingPrefectureGoodName(marketingPrefectureId);
	   result.setResult(objectMapList);
	   result.setCode(200);
	   return result;
	 }

	 /**
	  * 冒泡排序
	  * @param a
	  */
	 public  BigDecimal[] maopaoSort(BigDecimal[] a){
		 //外层循环，是需要进行比较的轮数，一共进行5次即可
		 for(int i=0;i<a.length-1;i++){
			 //内存循环，是每一轮中进行的两两比较
			 for(int j=0;j<a.length-1;j++)
			 {
				 if(a[j].compareTo(a[j+1])  > 0)
				 {
					 BigDecimal temp = a[j];
					 a[j] = a[j+1];
					 a[j+1] = temp;
				 }
			 }
			 //System.out.println("第"+(i+1)+"轮排序后的数组为: "+Arrays.toString(a));
		 }
		 return a;
	 }

	 /**
	  * 复制商品地址
	  * @param marketingPrefectureGoodId
	  * @return
	  */
	 @AutoLog(value = "专区商品-复制链接")
	 @ApiOperation(value = "专区商品-复制链接", notes = "专区商品-复制链接")
	 @GetMapping(value = "/getMarketingPrefectureGoodUrl")
	 public  Result<Map<String,Object>> getGoodUrl(String marketingPrefectureGoodId) {
		 Result<Map<String,Object>> result = new Result<>();
		 MarketingPrefectureGood marketingPrefectureGood = marketingPrefectureGoodService.getById(marketingPrefectureGoodId);
		if(marketingPrefectureGood==null){
			result.error500("专区商品id不能为空!");
			return result;
		}
		 // pages/product/product2.html?info={"goodId":"ef2b0064eb9803b940e59bc735d7420e","isPlatform":1,"marketingPrefectureId":"f65ff445778415b9142ed0da9fb20780"}
		 Map<String,String> map = Maps.newHashMap();
		 map.put("goodId",marketingPrefectureGood.getGoodListId());
		 map.put("isPlatform","1");
		 map.put("marketingPrefectureId",marketingPrefectureGood.getMarketingPrefectureId());
		 String url = "goodAction/pages/product/product?info=";
		 Map<String,Object> mapObject = Maps.newHashMap();
		 mapObject.put("url",url);
		 mapObject.put("parameter", JSONObject.toJSONString(map));
		 result.setResult(mapObject);
		 result.setSuccess(true);
		 return result;
	 }

	 /**
	  *  专区推荐商品弹窗数据
	  * @param marketingPrefectureGoodDTO
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @GetMapping("findMarketingPrefectureGoodList")
	 public Result<IPage<Map<String,Object>>> findMarketingPrefectureGoodList(MarketingPrefectureGoodDTO marketingPrefectureGoodDTO,
																			  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																			  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
		 Result<IPage<Map<String, Object>>> result = new Result<>();
		 Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
		 if (StringUtils.isNotBlank(marketingPrefectureGoodDTO.getMarketingPrefectureTypeId())){
		 	if (marketingPrefectureGoodDTO.getMarketingPrefectureTypeId().contains(",")){
				List<String> strings = Arrays.asList(StringUtils.split(marketingPrefectureGoodDTO.getMarketingPrefectureTypeId(), ","));
				marketingPrefectureGoodDTO.setTypeTwoId(strings.get(1));
			}else {
		 		marketingPrefectureGoodDTO.setTypeOneId(marketingPrefectureGoodDTO.getMarketingPrefectureTypeId());
			}
		 }
		 result.setResult(marketingPrefectureGoodService.findMarketingPrefectureGoodList(page,marketingPrefectureGoodDTO));
		 return result;
	 }
}
