package org.jeecg.modules.marketing.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.marketing.dto.MarketingAdvertisingDTO;
import org.jeecg.modules.marketing.entity.MarketingAdvertising;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingService;
import org.jeecg.modules.marketing.vo.MarketingAdvertisingVO;
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
import java.text.SimpleDateFormat;
import java.util.*;

 /**
 * @Description: 广告管理
 * @Author: jeecg-boot
 * @Date:   2019-10-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="广告管理")
@RestController
@RequestMapping("/marketingAdvertising/marketingAdvertising")
public class MarketingAdvertisingController {
	 @Autowired
	private IMarketingAdvertisingService marketingAdvertisingService;
	 @Autowired
	 private IGoodListService goodListService;
	 @Autowired
	 private IGoodStoreListService goodStoreListService;
	/**
	  * 分页列表查询
	 * @param marketingAdvertisingVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "广告管理-分页列表查询")
	@ApiOperation(value="广告管理-分页列表查询", notes="广告管理-分页列表查询")
	@GetMapping(value = "/list")
	@PermissionData(pageComponent="store/MarketingAdvertisingList")
	public Result<IPage<MarketingAdvertisingDTO>> queryPageList(MarketingAdvertisingVO marketingAdvertisingVO,
															 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															 HttpServletRequest req) {
		Result<IPage<MarketingAdvertisingDTO>> result = new Result<IPage<MarketingAdvertisingDTO>>();
		Page<MarketingAdvertising> page = new Page<MarketingAdvertising>(pageNo, pageSize);
		//判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		String str = PermissionUtils.ifPlatform();
		if(str!=null){
			marketingAdvertisingVO.setSysUserId(str);
		}
		IPage<MarketingAdvertisingDTO> pageList = marketingAdvertisingService.getMarketingAdvertisingDTO(page, marketingAdvertisingVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}


     /**
	  *
      * 店铺广告分页列表查询
      * @param marketingAdvertisingVO
      * @param pageNo
      * @param pageSize
      * @param req
      * @return
      */
     @AutoLog(value = "广告管理-分页列表查询")
     @ApiOperation(value="广告管理-分页列表查询", notes="广告管理-分页列表查询")
     @GetMapping(value = "/Shoplist")
     public Result<IPage<MarketingAdvertisingDTO>> queryPageShopList(MarketingAdvertisingVO marketingAdvertisingVO,
                                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                              HttpServletRequest req) {
		 Result<IPage<MarketingAdvertisingDTO>> result = new Result<IPage<MarketingAdvertisingDTO>>();
		 Page<MarketingAdvertising> page = new Page<MarketingAdvertising>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();
		 if(str!=null){
			 marketingAdvertisingVO.setSysUserId(str);
		 }
		 IPage<MarketingAdvertisingDTO> pageList = marketingAdvertisingService.getMarketingAdvertisingDTO(page, marketingAdvertisingVO);

		 result.setSuccess(true);
         result.setResult(pageList);
         return result;
     }

	/**
	  *   添加
	 * @param marketingAdvertising
	 * @return
	 */
	@AutoLog(value = "广告管理-添加")
	@ApiOperation(value="广告管理-添加", notes="广告管理-添加")
	@PostMapping(value = "/add")
	public Result<MarketingAdvertising> add(@RequestBody MarketingAdvertising marketingAdvertising) {
		Result<MarketingAdvertising> result = new Result<MarketingAdvertising>();
		try {
			marketingAdvertisingService.save(marketingAdvertising);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingAdvertising
	 * @return
	 */
	@AutoLog(value = "广告管理-编辑")
	@ApiOperation(value="广告管理-编辑", notes="广告管理-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingAdvertising> edit(@RequestBody MarketingAdvertising marketingAdvertising) {
		Result<MarketingAdvertising> result = new Result<MarketingAdvertising>();
		MarketingAdvertising marketingAdvertisingEntity = marketingAdvertisingService.getById(marketingAdvertising.getId());
		if(marketingAdvertisingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingAdvertisingService.updateById(marketingAdvertising);
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
	@AutoLog(value = "广告管理-通过id删除")
	@ApiOperation(value="广告管理-通过id删除", notes="广告管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		Result<MarketingAdvertising> result = new Result<MarketingAdvertising>();
		try {
			marketingAdvertisingService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告管理-批量删除")
	@ApiOperation(value="广告管理-批量删除", notes="广告管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingAdvertising> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingAdvertising> result = new Result<MarketingAdvertising>();

		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingAdvertisingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告管理-通过id查询")
	@ApiOperation(value="广告管理-通过id查询", notes="广告管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingAdvertising> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingAdvertising> result = new Result<MarketingAdvertising>();
		MarketingAdvertising marketingAdvertising = marketingAdvertisingService.getById(id);
		if(marketingAdvertising==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingAdvertising);
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
      QueryWrapper<MarketingAdvertising> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingAdvertising marketingAdvertising = JSON.parseObject(deString, MarketingAdvertising.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingAdvertising, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingAdvertising> pageList = marketingAdvertisingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "广告管理列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingAdvertising.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("广告管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingAdvertising> listMarketingAdvertisings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingAdvertising.class, params);
              marketingAdvertisingService.saveBatch(listMarketingAdvertisings);
              return Result.ok("文件导入成功！数据行数:" + listMarketingAdvertisings.size());
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
	  * adType 判断查询条件是 0.平台集合 1.商品集合
	  * @param adType
	  * @return
	  */
  @GetMapping(value = "/goodListOk")
public Result<Map<String,Object>> goodListOk(@RequestParam(name="adType",required=true) String adType, String sysUserId,String goodName){
	Result<Map<String,Object>> result = new Result<Map<String,Object>>();
	  Map<String,Object> map=new HashMap<String,Object>();
	if(adType==null || "".equals(adType)){
		result.error500("商品类型不能为空");
	}
//判断当前用户是否是平台

	if("0".equals(adType)){
		//权限判断
		QueryWrapper<GoodList> queryWrapper = new QueryWrapper<GoodList>();
		PermissionUtils.accredit(queryWrapper);
		if(StringUtils.isNotBlank(goodName)){
			queryWrapper.like("good_name",goodName) ;
		}
		queryWrapper.last("limit 30");
		List<GoodList>	goodlist=goodListService.list(queryWrapper);
		map.put("goodlist",goodlist);
		result.setResult(map);
		result.setSuccess(true);
	}else if("1".equals(adType)){
		//权限判断
		QueryWrapper<GoodStoreList> queryWrapper = new QueryWrapper<GoodStoreList>();
		PermissionUtils.accredit(queryWrapper);
		if(StringUtils.isNotBlank(sysUserId)){
			queryWrapper.eq("sys_user_id",sysUserId);
		}
		if(StringUtils.isNotBlank(goodName)) {
			queryWrapper.like("good_name", goodName);
		}
		queryWrapper.last("limit 30");
		List<GoodStoreList>	goodStorelist=goodStoreListService.list(queryWrapper);
		map.put("goodlist",goodStorelist);
		result.setResult(map);
		result.setSuccess(true);
	}
return result;

}



	/*******************************后台管理**************************************/

	/*************************************************************/



	 /**
	  * 轮播图前端展示
	  */

	 @GetMapping(value = "/adLocation")
	 public Result<?> queryPageList() {
		 //    ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //判断时间调用
		 endTime();
		 List<MarketingAdvertising> listMarketingAdvertising =marketingAdvertisingService.getMarketingAdvertisingByAdLocation("1");//PictureAddr
		 Map<String,Object > map=new HashMap<>();
		 map.put("listMarketingAdvertising",listMarketingAdvertising);
		 return Result.ok(map);
	 }
	 /**
	  * 通过id查询修改状态:启用停用
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "广告管理-通过id查询")
	 @ApiOperation(value="广告管理-通过id查询", notes="广告管理-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<MarketingAdvertising> updateStatus(@RequestParam(name="id",required=true) String id) {
		 Result<MarketingAdvertising> result = new Result<MarketingAdvertising>();
		 MarketingAdvertising marketingAdvertising = marketingAdvertisingService.getById(id);
		 if(marketingAdvertising==null) {
			 result.error500("未找到对应实体");
		 }else {
		 	if("1".equals(marketingAdvertising.getStatus())){
				marketingAdvertising.setStatus("0");
			}else{
				marketingAdvertising.setStatus("1");
			}
			 boolean ok = marketingAdvertisingService.updateById(marketingAdvertising);
			 //TODO 返回false说明什么？
			 if(ok) {
				 result.success("修改成功!");
			 }else{
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }
	 /***
	  * 判断结束时间到修改状态
	  */
	 public void endTime() {
		 List<MarketingAdvertising> listMarketingAdvertising =marketingAdvertisingService.allAvailable();
		if(listMarketingAdvertising.size()>0){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
			String time=dateFormat.format(date);
			long longstr1 = Long.valueOf(time.replaceAll("[-\\s:]",""));
			long longstr2;
			String longstr3;
			for(MarketingAdvertising ma:listMarketingAdvertising){
				longstr3=dateFormat.format(ma.getEndTime());
				longstr2=Long.valueOf(longstr3.replaceAll("[-\\s:]",""));
				//时间超过修改状态
				if(longstr1>longstr2){
					ma.setStatus("0");
					marketingAdvertisingService.updateById(ma);
				}
			}
		}
		}
	 /***
	  * 轮播页面跳转判断
	  * (未完成)
	  */
	 public String listMarketingAdvertisingUrl(Long id){
		 Result<MarketingAdvertising> result = new Result<MarketingAdvertising>();
		 MarketingAdvertising marketingAdvertising = marketingAdvertisingService.getById(id);
		 if(marketingAdvertising==null) {
			 result.error500("未找到对应实体");
		 }else {
			if("1".equals(marketingAdvertising.getGoToType())){
				//跳转该商户商品详情good_list_id

			}else{
				//跳转该商户店铺 good_list_id
			}
		 }
                return  null;
	 }

 }
