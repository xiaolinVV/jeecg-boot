package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.vo.MarketingPrefectureVO;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.service.IMemberGradeService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
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
import java.util.*;

/**
 * @Description: 平台专区
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="平台专区")
@RestController
@RequestMapping("/marketingPrefecture/marketingPrefecture")
public class MarketingPrefectureController {
	@Autowired
	private IMarketingPrefectureService marketingPrefectureService;
	@Autowired
	private IMarketingPrefectureGoodService  marketingPrefectureGoodService;
	@Autowired
	private IGoodListService goodListService;
	 @Autowired
	private IGoodSpecificationService goodSpecificationService;
	 @Autowired
	 private IMarketingPrefectureGoodSpecificationService marketingPrefectureGoodSpecificationService;
	 @Autowired
	 private IMarketingAdvertisingPrefectureService marketingAdvertisingPrefectureService;
	 @Autowired
	 private IMarketingPrefectureTypeService marketingPrefectureTypeService;

	 @Autowired
	 private IMemberGradeService iMemberGradeService;




	 @RequestMapping("settingPointsDisplay")
	 @ResponseBody
	 public Result<?> settingPointsDisplay(String ids,String pointsDisplay){

		 Arrays.asList(StringUtils.split(ids,",")).forEach(mpId->{
		 	MarketingPrefecture marketingPrefecture=marketingPrefectureService.getById(mpId);
		 	marketingPrefecture.setPointsDisplay(pointsDisplay);
		 	marketingPrefectureService.saveOrUpdate(marketingPrefecture);
		 });
		return Result.ok("专区分端控制设置成功");
	 }




	/**
	  * 分页列表查询
	 * @param marketingPrefecture
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "平台专区-分页列表查询")
	@ApiOperation(value="平台专区-分页列表查询", notes="平台专区-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingPrefecture>> queryPageList(MarketingPrefecture marketingPrefecture,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingPrefecture>> result = new Result<IPage<MarketingPrefecture>>();
		QueryWrapper<MarketingPrefecture> queryWrapper = QueryGenerator.initQueryWrapper(marketingPrefecture, req.getParameterMap());
		Page<MarketingPrefecture> page = new Page<MarketingPrefecture>(pageNo, pageSize);
		IPage<MarketingPrefecture> pageList = marketingPrefectureService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingPrefecture
	 * @return
	 */
	@AutoLog(value = "平台专区-添加")
	@ApiOperation(value="平台专区-添加", notes="平台专区-添加")
	@PostMapping(value = "/add")
	public Result<MarketingPrefecture> add(@RequestBody MarketingPrefecture marketingPrefecture) {
		Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
		try {
			//参数处理
			//不限制商品
			if("0".equals(marketingPrefecture.getAstrictGood())){
				marketingPrefecture.setAstrictPriceProportion(new BigDecimal("0"));
			}
			//不支持福利金抵扣
			if("0".equals(marketingPrefecture.getIsWelfare()) ){
				marketingPrefecture.setBigWelfareProportion(new BigDecimal("0"));
			}else if("1".equals(marketingPrefecture.getIsWelfare())){
				//福利金全额抵扣
				marketingPrefecture.setBigWelfareProportion(new BigDecimal("100"));
			}
			//不支持赠送福利金
            if("0".equals(marketingPrefecture.getIsGiveWelfare())){
				marketingPrefecture.setBigGiveWelfareProportion(new BigDecimal("0"));
			}
            //valid_time 长期有效时间
			if("0".equals(marketingPrefecture.getValidTime())){
				marketingPrefecture.setEndTime(null);
			}
			//是否支持全部；0：不支持；1：支持；
			if("0".equals(marketingPrefecture.getIsViewType())){
				marketingPrefecture.setIsAllType("0");
			}else if("1".equals(marketingPrefecture.getIsViewType())){
				//全部勾选
				if("1".equals(marketingPrefecture.getIsAllType())){
					marketingPrefecture.setIsAllType("1");
				}else{
					marketingPrefecture.setIsAllType("2");
				}
			}
			marketingPrefectureService.save(marketingPrefecture);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingPrefecture
	 * @return
	 */
	@AutoLog(value = "平台专区-编辑")
	@ApiOperation(value="平台专区-编辑", notes="平台专区-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingPrefecture> edit(@RequestBody MarketingPrefecture marketingPrefecture) {
		Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
		MarketingPrefecture marketingPrefectureEntity = marketingPrefectureService.getById(marketingPrefecture.getId());
		if(marketingPrefectureEntity==null) {
			result.error500("未找到对应实体");
		}else {
			//不限制商品
			if("0".equals(marketingPrefecture.getAstrictGood())){
				marketingPrefecture.setAstrictPriceProportion(new BigDecimal("0"));
			}
			//不支持福利金抵扣
			if("0".equals(marketingPrefecture.getIsWelfare()) ){
				marketingPrefecture.setBigWelfareProportion(new BigDecimal("0"));
			}else if("1".equals(marketingPrefecture.getIsWelfare())){
				//福利金全额抵扣
				marketingPrefecture.setBigWelfareProportion(new BigDecimal("100"));
			}
			//不支持赠送福利金
			if("0".equals(marketingPrefecture.getIsGiveWelfare())){
				marketingPrefecture.setBigGiveWelfareProportion(new BigDecimal("0"));
			}
			//valid_time 长期有效时间
			if("0".equals(marketingPrefecture.getValidTime())){
				marketingPrefecture.setEndTime(null);
			}
            //是否支持全部；0：不支持；1：支持；
            if("0".equals(marketingPrefecture.getIsViewType())){
                marketingPrefecture.setIsAllType("0");
            }else if("1".equals(marketingPrefecture.getIsViewType())){
				//全部勾选
				if("1".equals(marketingPrefecture.getIsAllType())){
					marketingPrefecture.setIsAllType("1");
				}else{
					marketingPrefecture.setIsAllType("2");
				}
			}

			boolean ok = marketingPrefectureService.updateById(marketingPrefecture);
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
	@AutoLog(value = "平台专区-通过id删除")
	@ApiOperation(value="平台专区-通过id删除", notes="平台专区-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			//关联删除专区
			linkToDelete(id);


			marketingPrefectureService.removeById(id);
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
	@AutoLog(value = "平台专区-批量删除")
	@ApiOperation(value="平台专区-批量删除", notes="平台专区-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingPrefecture> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			//关联删除专区
			linkToDelete(ids);
			this.marketingPrefectureService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "平台专区-通过id查询")
	@ApiOperation(value="平台专区-通过id查询", notes="平台专区-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingPrefectureVO> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingPrefectureVO> result = new Result<MarketingPrefectureVO>();
		MarketingPrefecture marketingPrefecture = marketingPrefectureService.getById(id);
		if(oConvertUtils.isEmpty(marketingPrefecture)) {
			result.error500("未找到对应实体");
		}else {
			MarketingPrefectureVO marketingPrefectureVO = new MarketingPrefectureVO();
			BeanUtils.copyProperties(marketingPrefecture,marketingPrefectureVO);
			ArrayList<String> arrayList = new ArrayList<>();
			if (StringUtils.isNotBlank(marketingPrefectureVO.getBuyVipMemberGradeId())){
				List<String> strings = Arrays.asList(StringUtils.split(marketingPrefectureVO.getBuyVipMemberGradeId(), ","));
				strings.forEach(ss->{
					MemberGrade memberGrade = iMemberGradeService.getById(ss);
					arrayList.add(memberGrade.getGradeName());
				});
			}
			marketingPrefectureVO.setBuyerLimitName(arrayList.toString());
			result.setResult(marketingPrefectureVO);
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
      QueryWrapper<MarketingPrefecture> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingPrefecture marketingPrefecture = JSON.parseObject(deString, MarketingPrefecture.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingPrefecture, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingPrefecture> pageList = marketingPrefectureService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "平台专区列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingPrefecture.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台专区列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingPrefecture> listMarketingPrefectures = ExcelImportUtil.importExcel(file.getInputStream(), MarketingPrefecture.class, params);
              marketingPrefectureService.saveBatch(listMarketingPrefectures);
              return Result.ok("文件导入成功！数据行数:" + listMarketingPrefectures.size());
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
	 @AutoLog(value = "平台专区-通过id查询")
	 @ApiOperation(value = "平台专区-通过id查询", notes = "平台专区-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<MarketingPrefecture> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status,String closeExplain) {
		 Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
		 if (StringUtils.isEmpty(ids)) {
			 result.error500("参数不识别！");
		 } else {
			 MarketingPrefecture marketingPrefecture;
			 try {
				 List<String> listid = Arrays.asList(ids.split(","));
				 for (String id : listid) {
					 marketingPrefecture = marketingPrefectureService.getById(id);
					 if (marketingPrefecture == null) {
						 result.error500("未找到对应实体");
					 } else {

                        //短时间有效期 判断
					 	if(status.equals("1") && "1".equals(marketingPrefecture.getValidTime())){

							Date sd1=new Date();
							//有效期已过
                            if(sd1.after(marketingPrefecture.getEndTime())){
								result.error500("有效期已过!先去修改有效期!");
								return  result;
							}


						}
						 marketingPrefecture.setCloseExplain(closeExplain);
						 marketingPrefecture.setStatus(status);
						 marketingPrefectureService.updateById(marketingPrefecture);
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
	  * 分页列表查询
	  * @param prefectureName 平台专区名称 查询添加 无则查询所有
	  * @param isViewType
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "平台专区-分页列表查询")
	 @ApiOperation(value="平台专区-分页列表查询", notes="平台专区-分页列表查询")
	 @GetMapping(value = "/getMarketingPrefecture")
	 public Result<List<Map<String,Object>>> getMarketingPrefecture(@RequestParam(name = "prefectureName") String prefectureName,
																	@RequestParam(name = "isViewType")String isViewType,
																	@RequestParam(name = "isViewPrefectureRecommended")String isViewPrefectureRecommended ,
																	HttpServletRequest req) {
		 Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		 Map<String,Object> paramMap = Maps.newHashMap();
		 if(StringUtils.isNotBlank(prefectureName)){
		 	//查询条件名称搜索
			 paramMap.put("prefectureName",prefectureName);
		 }
		 if(StringUtils.isNotBlank(isViewType)){
			 //分类列表使用
			 paramMap.put("isViewType",isViewType);
		 }
		 if(StringUtils.isNotBlank(isViewPrefectureRecommended)){
			 //推荐使用
			 paramMap.put("isViewPrefectureRecommended",isViewPrefectureRecommended);
		 }
		 List<Map<String,Object>> objectMapList = marketingPrefectureService.getMarketingPrefectureIdName(paramMap);
		 result.setSuccess(true);
		 result.setCode(200);
		 result.setResult(objectMapList);
		 return result;
	 }

	/**
	 * 专区推荐商品左数据
	 * @return
	 */
	@GetMapping(value = "/getMarketingPrefectureByRecommend")
	public Result<List<Map<String,Object>>> getMarketingPrefectureByRecommend() {
		Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		List<Map<String,Object>> objectMapList = marketingPrefectureService.getMarketingPrefectureByRecommend();
		result.setResult(objectMapList);
		return result;
	}
	 /**
	  * 选择商品
 	  * @param goodListVo
	  * @return
	  */
	 @PostMapping(value = "/postGoodListList")
	 public Result<List<Map<String,Object>>> postGoodListList(@RequestBody GoodListVo goodListVo){
		 Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		 if(StringUtils.isBlank(goodListVo.getMarketingPrefectureId())){
			 result.error500("专区id不能为空!");
			 return result;
		 }
		 if(StringUtils.isBlank(goodListVo.getIds())){
			 result.error500("专区商品不能为空");
			 return result;
		 }
		 MarketingPrefecture marketingPrefecture = marketingPrefectureService.getById(goodListVo.getMarketingPrefectureId());
		 if(marketingPrefecture == null){
			 result.error500("未找到专区数据!");
			 return result;
		 }
		List<String> strList =Arrays.asList(goodListVo.getIds().split(","));
		 goodListVo.setMarketingPrefectureGoodIds(strList);
		 //已添加到专区商品过滤
		 Map<String,Object> paramMap = Maps.newHashMap();
		 paramMap.put("validTime",marketingPrefecture.getValidTime());
		 paramMap.put("startTime",marketingPrefecture.getStartTime());
		 paramMap.put("endTime",marketingPrefecture.getEndTime());
		 paramMap.put("marketingPrefectureId",marketingPrefecture.getId());
		 List<Map<String,Object>> marketingPrefectureGood = marketingPrefectureService.getFiltrationGoodIds(paramMap);

		 List<String> marketingPrefectureGoodNotIds = new ArrayList<>();
		 marketingPrefectureGood.forEach(mpg->{
			 marketingPrefectureGoodNotIds.add(mpg.get("good_list_id").toString());
		 });
		 if(marketingPrefectureGoodNotIds.size()>0){
			 //过滤已存在专区商品
			 goodListVo.setMarketingPrefectureGoodNotIds(marketingPrefectureGoodNotIds);
		 }


		 List<Map<String,Object>> mapList = goodListService.getMarketingPrefectureGoodPitchOn(goodListVo);

         //规格数据
		 if(StringUtils.isNotBlank(goodListVo.getIds())){
			 QueryWrapper  queryWrapper1 ;
			 List<Map<String,Object>> listGoodSpecification;
			 for (Map<String,Object> gd : mapList) {
			 	//添加专区id
				 gd.put("marketingPrefectureId",marketingPrefecture.getId());
				 gd.put("marketingPrefectureTypeId","");
				 gd.put("goodListId",gd.get("id"));
				 queryWrapper1 = new QueryWrapper<GoodSpecification>();
				 queryWrapper1.select("id,price,specification,repertory");
				 queryWrapper1.eq("good_list_id", gd.get("id"));
				 queryWrapper1.orderByDesc("create_time");
				 listGoodSpecification = goodSpecificationService.listMaps(queryWrapper1);
				 /*if("0".equals(marketingPrefecture.getIsWelfare())){
					 gd.put("marketPrice",marketPrice);//福利金限额抵扣比例

				 }*/
				 for(Map<String,Object> mapsp:listGoodSpecification){
					 mapsp.put("prefecturePriceProportion",marketingPrefecture.getPrefecturePriceProportion());//专区折扣
					 mapsp.put("prefecturePrice",marketingPrefecture.getPrefecturePriceProportion().multiply(new BigDecimal(mapsp.get("price").toString())).divide(new BigDecimal(100)));//专区价
					 mapsp.put("isWelfare",marketingPrefecture.getIsWelfare());//福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣
					if("0".equals(marketingPrefecture.getIsWelfare())){
						mapsp.put("welfareProportion","0");//福利金限额抵扣比例
					}
					 if("1".equals(marketingPrefecture.getIsWelfare())){
						mapsp.put("welfareProportion","100");//福利金限额抵扣比例
					}else{
						mapsp.put("welfareProportion",marketingPrefecture.getBigWelfareProportion());//福利金限额抵扣比例
					}

					 mapsp.put("isGiveWelfare",marketingPrefecture.getIsGiveWelfare());//赠送福利金比例
					 mapsp.put("giveWelfareProportion",marketingPrefecture.getBigGiveWelfareProportion());
					 mapsp.put("buyProportionDay",-1);//购买天数;-1：不限制；其他代表天数
					 mapsp.put("buyProportionLetter",-1);//可购买件数；-1：不限制；其他代表件数
					 mapsp.put("goodSpecificationId",mapsp.get("id"));//规格id
					 mapsp.put("isVipLower",marketingPrefecture.getIsVipLower());//vip会员免福利金；0：不免；1：免
				 }

				 if (listGoodSpecification.size() > 0) {
					 gd.put("goodListSpecificationList",listGoodSpecification);
				 }

			 }
		 }

		 result.setCode(200);
		 result.setResult(mapList);
		 result.setSuccess(true);
		return result;
	 }


	 /**
	  * 删除专区 关联删除 商品 分类 广告
	  */

	 public void linkToDelete(String ids) {
       Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            List<MarketingPrefectureGood>   marketingPrefectureGoodList;
            List<MarketingPrefectureGoodSpecification> marketingPrefectureGoodSpecificationList;

            List<String> listid = Arrays.asList(ids.split(","));
            QueryWrapper<MarketingPrefectureGood> queryWrapperMarketingPrefectureGood = new QueryWrapper();
            QueryWrapper<MarketingPrefectureGoodSpecification> queryWrapperMarketingPrefectureGoodSpecification = new QueryWrapper();


            //查询关联专区商品
            queryWrapperMarketingPrefectureGood = new QueryWrapper();
            queryWrapperMarketingPrefectureGood.in("marketing_prefecture_id", listid);
            marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapperMarketingPrefectureGood);

            if (marketingPrefectureGoodList.size() == 0 ) {
                // result.error500("未找到对应实体");
            } else {
                List<String> marketingPrefectureGoodids = new ArrayList<>();
                for(MarketingPrefectureGood marketingPrefectureGood:marketingPrefectureGoodList){
                    marketingPrefectureGoodids.add(marketingPrefectureGood.getId()) ;
                }
                //规格
                queryWrapperMarketingPrefectureGoodSpecification = new QueryWrapper();
                queryWrapperMarketingPrefectureGoodSpecification.in("marketing_prefecture_good_id",marketingPrefectureGoodids);
                marketingPrefectureGoodSpecificationList =  marketingPrefectureGoodSpecificationService.list(queryWrapperMarketingPrefectureGoodSpecification);
                List<String> marketingPrefectureGoodidSpecificationIds = new ArrayList<>();
                for(MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification:marketingPrefectureGoodSpecificationList){
                    marketingPrefectureGoodidSpecificationIds.add(marketingPrefectureGoodSpecification.getId()) ;
                }
                //删除规格
                marketingPrefectureGoodSpecificationService.removeByIds(marketingPrefectureGoodidSpecificationIds);
                //删除关联商品
                marketingPrefectureGoodService.removeByIds(marketingPrefectureGoodids);
            }
            //专区分类
            List<MarketingPrefectureType> marketingPrefectureTypeList;
            QueryWrapper<MarketingPrefectureType> queryWrapperMarketingPrefectureType = new QueryWrapper();
            queryWrapperMarketingPrefectureType.in("marketing_prefecture_id", listid);
            marketingPrefectureTypeList = marketingPrefectureTypeService.list(queryWrapperMarketingPrefectureType);
            List<String> marketingPrefectureTypeIds = new ArrayList<>();
            for(MarketingPrefectureType marketingPrefectureType:marketingPrefectureTypeList){
                marketingPrefectureTypeIds.add(marketingPrefectureType.getId()) ;
            }
            //删除专区分类
            if(marketingPrefectureTypeIds.size()>0){
                marketingPrefectureTypeService.removeByIds(marketingPrefectureTypeIds);
            }
            //专区广告
            List<MarketingAdvertisingPrefecture>  marketingAdvertisingPrefectureList;
            List<String> marketingAdvertisingPrefectureIds = new ArrayList<>();
            QueryWrapper<MarketingAdvertisingPrefecture> queryWrapperMarketingAdvertisingPrefecture = new QueryWrapper();
            queryWrapperMarketingAdvertisingPrefecture.in("marketing_prefecture_id", listid);
            marketingAdvertisingPrefectureList = marketingAdvertisingPrefectureService.list(queryWrapperMarketingAdvertisingPrefecture);
            for(MarketingAdvertisingPrefecture marketingAdvertisingPrefecture:marketingAdvertisingPrefectureList){
                marketingAdvertisingPrefectureIds.add(marketingAdvertisingPrefecture.getId()) ;
            }
            //删除专区广告
            if(marketingAdvertisingPrefectureIds.size()>0){
                marketingAdvertisingPrefectureService.removeByIds(marketingAdvertisingPrefectureIds);
            }

        }
	 }




	/**
	 * 复制商品地址
	 * @param marketingPrefectureId
	 * @return
	 */
	@AutoLog(value = "专区商品-复制链接")
	@ApiOperation(value = "专区商品-复制链接", notes = "专区商品-复制链接")
	@GetMapping(value = "/getMarketingPrefectureUrl")
	public  Result<Map<String,Object>> getMarketingPrefectureUrl(String marketingPrefectureId) {
		Result<Map<String,Object>> result = new Result<>();
		MarketingPrefecture marketingPrefecture = marketingPrefectureService.getById(marketingPrefectureId);
		if(marketingPrefecture==null){
			result.error500("专区id不能为空!");
			return result;
		}
		// pages/prefecture/prefecture.html?prefectureName=福利专区&id=8e853fd0bda9d0331b180ff656006544

		String url = "goodAction/pages/prefecture/prefecture?prefectureName="+marketingPrefecture.getPrefectureName()+"&id="+marketingPrefecture.getId();
		Map<String,Object> mapObject = Maps.newHashMap();
		mapObject.put("url",url);
		result.setResult(mapObject);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 专区集合
	 * @return
	 */
	@GetMapping("getPrefectureList")
	public Result<?> getPrefectureList(){
		return Result.ok(marketingPrefectureService.listMaps(new QueryWrapper<MarketingPrefecture>()
				.select("id,prefecture_name as prefectureName")
				.eq("del_flag","0")
				.eq("status","1")
		));
	}
}
