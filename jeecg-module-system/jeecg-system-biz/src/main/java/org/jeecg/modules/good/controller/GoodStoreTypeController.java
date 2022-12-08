package org.jeecg.modules.good.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.good.dto.GoodStoreListDto;
import org.jeecg.modules.good.dto.GoodStoreTypeDto;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreType;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreTypeService;
import org.jeecg.modules.good.vo.GoodStoreListVo;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/GoodStoreType/goodStoreType")
@Slf4j
public class GoodStoreTypeController {
	@Autowired
	private IGoodStoreTypeService goodStoreTypeService;
	@Autowired
	private IGoodStoreListService goodStoreListService;
	/**
	  * 分页列表查询
	 * @param goodStoreType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<IPage<GoodStoreType>> queryPageList(GoodStoreType goodStoreType,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		if(oConvertUtils.isEmpty(goodStoreType.getParentId())){
			goodStoreType.setParentId("0");
		}
		Result<IPage<GoodStoreType>> result = new Result<IPage<GoodStoreType>>();
		QueryWrapper<GoodStoreType> queryWrapper = QueryGenerator.initQueryWrapper(goodStoreType, req.getParameterMap());
		queryWrapper.eq("parent_id","0");
		//权限配置
		PermissionUtils.accredit(queryWrapper);
		Page<GoodStoreType> page = new Page<GoodStoreType>(pageNo, pageSize);
		IPage<GoodStoreType> pageList = goodStoreTypeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	@GetMapping(value = "/childList")
	public Result<List<GoodStoreType>> queryPageList(GoodStoreType goodStoreType,HttpServletRequest req) {
		Result<List<GoodStoreType>> result = new Result<List<GoodStoreType>>();
		QueryWrapper<GoodStoreType> queryWrapper = QueryGenerator.initQueryWrapper(goodStoreType, req.getParameterMap());
		//权限配置
		PermissionUtils.accredit(queryWrapper);
		List<GoodStoreType> list = goodStoreTypeService.list(queryWrapper);
		result.setSuccess(true);
		result.setResult(list);
		return result;
	}
	
	
	/**
	  *   添加
	 * @param goodStoreType
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<GoodStoreType> add(@RequestBody GoodStoreType goodStoreType) {
		Result<GoodStoreType> result = new Result<GoodStoreType>();
		try {
			goodStoreTypeService.addGoodStoreType(goodStoreType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodStoreType
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<GoodStoreType> edit(@RequestBody GoodStoreType goodStoreType) {
		Result<GoodStoreType> result = new Result<GoodStoreType>();
		try {
			goodStoreTypeService.updateGoodStoreType(goodStoreType);
			result.success("修改成功!");
		} catch (Exception e) {
			result.error500(e.getMessage());
		}
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<GoodStoreType> delete(@RequestParam(name="id",required=true) String id) {
		Result<GoodStoreType> result = new Result<GoodStoreType>();
		try {
			//添加删除判断是否可删除
			List<GoodStoreType>  listGoodStoreType=goodStoreTypeService.getGoodStoreTypeOrParentIdListtwo(id,null,null);
             //判断分类下是否有商品
			Page<GoodStoreList> page = new Page<GoodStoreList>(1, 10);
			GoodStoreListVo goodStoreListVo =new GoodStoreListVo();
			goodStoreListVo.setGoodStoreTypeIdOne(id);
			goodStoreListVo.setGoodStoreTypeIdTwo(id);
			IPage<GoodStoreListDto> pageList = goodStoreListService.getGoodListDto(page, goodStoreListVo, null);
			if(pageList.getRecords().size()>0){
				result.error500("你若要删除该分类，请先删除子分类或者该分类底下的商品！");
				return result;
			}

			if(listGoodStoreType.size()>0){
				result.error500("该分类底下存在子分类或者商品，无法删除！");
				result.setMessage("你若要删除该分类，请先删除子分类或者该分类底下的商品！");
				return result;
			}

			goodStoreTypeService.removeById(id);
			result.success("删除成功!");
		} catch (Exception e) {
			result.error500(e.getMessage());
		}
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodStoreType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodStoreType> result = new Result<GoodStoreType>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodStoreTypeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<GoodStoreType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodStoreType> result = new Result<GoodStoreType>();
		GoodStoreType goodStoreType = goodStoreTypeService.getById(id);
		if(goodStoreType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodStoreType);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param goodStoreType
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, GoodStoreType goodStoreType) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<GoodStoreType> queryWrapper = QueryGenerator.initQueryWrapper(goodStoreType, request.getParameterMap());
      List<GoodStoreType> pageList = goodStoreTypeService.list(queryWrapper);
      // Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      // 过滤选中数据
      String selections = request.getParameter("selections");
      if(oConvertUtils.isEmpty(selections)) {
    	  mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      }else {
    	  List<String> selectionList = Arrays.asList(selections.split(","));
    	  List<GoodStoreType> exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
    	  mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
      }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "商品分类列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodStoreType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品分类列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodStoreType> listGoodStoreTypes = ExcelImportUtil.importExcel(file.getInputStream(), GoodStoreType.class, params);
              goodStoreTypeService.saveBatch(listGoodStoreTypes);
              return Result.ok("文件导入成功！数据行数:" + listGoodStoreTypes.size());
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
	  * 修改状态及其关联分类状态修改
	  * @param id
	  * @return
	  */

	 @GetMapping(value = "/updateStatus")
	 public Result<GoodStoreType> updateStatus(@RequestParam(name="id",required=true) String id) {
		 Result<GoodStoreType> result = new Result<GoodStoreType>();
		 GoodStoreType goodStoreType = goodStoreTypeService.getById(id);
		 QueryWrapper<GoodStoreList> queryWrapper =new QueryWrapper<GoodStoreList>();
		 List<GoodStoreList>  listGoodStoreList;
		 if(goodStoreType==null) {
			 result.error500("未找到对应实体");
		 }else {
			 if("1".equals(goodStoreType.getStatus())){
				 //修改为停用
				 goodStoreType.setStatus("0");
				 //修改关联分类
				 relevanceType(goodStoreType.getId(),"0");

				 //该分类的商品修改
				 queryWrapper =new QueryWrapper<GoodStoreList>();
				 queryWrapper.eq("good_store_type_id",goodStoreType.getId());
				 listGoodStoreList=goodStoreListService.list(queryWrapper);
				 if(listGoodStoreList.size()>0){
					 for( GoodStoreList goodStoreList:listGoodStoreList){
						 goodStoreList.setStatus("0");
						 goodStoreListService.updateById(goodStoreList);
					 }
				 }

			 }else {
				 goodStoreType.setStatus("1");
				 //修改关联分类
				 relevanceType(goodStoreType.getId(), "1");
				 //该分类的商品修改
				 queryWrapper =new QueryWrapper<GoodStoreList>();
				 queryWrapper.eq("good_store_type_id",goodStoreType.getId());
				 listGoodStoreList=goodStoreListService.list(queryWrapper);
				 if(listGoodStoreList.size()>0){
					 for( GoodStoreList goodStoreList:listGoodStoreList){
						 goodStoreList.setStatus("1");
						 goodStoreListService.updateById(goodStoreList);
					 }
				 }
			 }
			 boolean ok = goodStoreTypeService.updateById(goodStoreType);
			 //TODO 返回false说明什么？
			 if(ok) {
				 result.success("修改成功!");
			 }else{
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }


	 /**
	  * 关联分类修改
	  * @param goodTypeId 分类Id
	  * @param status 状态
	  */
	 public void relevanceType(String goodTypeId,String status){

		 //二级分类集合
		 List<GoodStoreType>  listGoodStoreType=goodStoreTypeService.getGoodStoreTypeOrParentIdListtwo(goodTypeId,null,null);
		 QueryWrapper<GoodStoreList> queryWrapper =new QueryWrapper<GoodStoreList>();
		 List<GoodStoreList>  listGoodStoreList;
		 if(listGoodStoreType.size()>0){
			 for(GoodStoreType gst:listGoodStoreType){
				 gst.setStatus(status);
				 goodStoreTypeService.updateGoodStoreType(gst);
				 //该分类的商品修改
				 queryWrapper =new QueryWrapper<GoodStoreList>();
				 //该分类的商品修改
				 queryWrapper.eq("good_store_type_id",gst.getId());
				 listGoodStoreList=goodStoreListService.list(queryWrapper);
				 if(listGoodStoreList.size()>0){
					 for( GoodStoreList goodStoreList:listGoodStoreList){
						 goodStoreList.setStatus(status);
						 goodStoreListService.updateById(goodStoreList);
					 }
				 }
			 }
		 }
	 }
	 /***
	  * 分类级联列表查询
	  * @param  parentId 父级Id
	  * @return
	  */
	 @RequestMapping(value = "/getgoodStorTypeListcascade", method = RequestMethod.GET)
	 public Result<Map<String,Object>> getgoodTypeListcascade(@RequestParam(name="parentId",required=true) String parentId,String sysUserId){
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		 List<GoodStoreType> listGoodStorType=null;
		 Map<String,Object> map=new HashMap<String,Object>();
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();
		 //平台指定店铺查询
		 if(StringUtils.isNotBlank(sysUserId)){
			 str = sysUserId;
		 }

		 listGoodStorType=goodStoreTypeService.getGoodTypeOrParentIdListtwo(parentId,null,str);//二级或三级分类集合
		 map.put("listGoodStorType",listGoodStorType);
		 result.setSuccess(true);
		 result.setResult(map);
		 return result;
	 }
	 /***
	  * 根据分类名称取所有三级类目名称
	  * @param  nodeName 分类名称
	  * @return
	  */
	 @RequestMapping(value = "/getgoodTypeListName", method = RequestMethod.GET)
	 public Result<Map<String,Object>> getgoodTypeListName(@RequestParam(name="nodeName",required=true) String nodeName,@RequestParam(name="level",required=true)String level,String sysUserId){
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		 Map<String,Object> map=new HashMap<String,Object>();
 		 List<GoodStoreTypeDto> listGoodType=goodStoreTypeService.getgoodTypeListName(nodeName,level,sysUserId);//二级或三级分类集合
		 map.put("listGoodType",listGoodType);
		 result.setSuccess(true);
		 result.setResult(map);
		 return result;
	 }
	 /***
	  * 根据分类名称和节点名称搜列表
	  * @param  name 分类名称
	  * @return
	  */
	 @RequestMapping(value = "/getgoodTypeListNameAndParentId", method = RequestMethod.GET)
	 public Result<Map<String,Object>> getgoodTypeListNameAndParentId(@RequestParam(name="name",required=true) String name,
																	  @RequestParam(name="parentId",required=true)String parentId,
	                                                                   String sysUserId    ){
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		 Map<String,Object> map=new HashMap<String,Object>();
		 List<Map<String,String>> listGoodType=goodStoreTypeService.getGoodTypeListNameAndParentId(name,parentId,sysUserId);//二级或三级分类集合
		 map.put("listGoodType",listGoodType);
		 result.setSuccess(true);
		 result.setResult(map);
		 return result;
	 }
	 /***
	  * 封装分类树
	  * @param id
	  * @return
	  */
	 @RequestMapping(value = "/encapsulationlassificationTree", method = RequestMethod.GET)
	 public Result<Map<String,Object>> encapsulationlassificationTree(@RequestParam(name="id",required=true) String id, HttpServletRequest request){
		 // String parentId=json.getString("parentId");
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		 String userName= JwtUtil.getUserNameByToken(request);
		 //String userName="jeecg";
		 Map<String,Object> map=goodStoreTypeService.encapsulationlassificationTree(id,userName);//二级或三级分类集合
		 result.setSuccess(true);
		 result.setResult(map);
		 return result;
	 }
	 /**
	  * 判断是否是二级
	  * @return
	  */
	 @GetMapping(value = "/ClassificationJudgment")
	 public Result<Boolean>  ClassificationJudgment(@RequestParam(name = "goodTypeId")String goodTypeId){
		 Result<Boolean> result = new Result<Boolean>();
		 Boolean bl;
		 QueryWrapper<GoodStoreType> queryWrapper = new QueryWrapper<GoodStoreType>();
		 queryWrapper.eq("id",goodTypeId );
		 queryWrapper.eq("level","2" );
		 List<GoodStoreType> listGoodStoreType= goodStoreTypeService.list(queryWrapper);
		 if(listGoodStoreType.size()>0){
			 bl = true;
		 }else{
			 bl = false;
		 }
		 result.setResult(bl);
		 return  result;
	 }
	 /**
	  * 复制分类地址
	  * @param goodTypeId
	  * @return
	  */
	 @AutoLog(value = "商品列表-分页列表查询")
	 @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
	 @GetMapping(value = "/getStoreTypeUrl")
	 public  Result<Map<String,Object>> getStoreTypeUrl(@RequestParam(name = "goodTypeId")String goodTypeId,@RequestParam(name = "name") String name) {
		 Result<Map<String,Object>> result = new Result<>();
		 Map<String,String> map = Maps.newHashMap();
		 map.put("goodTypeId",goodTypeId);
		 map.put("isPlatform","0");
		 map.put("name",name);
		 String url = "pages/index/search/search?info=";
		 Map<String,Object> mapObject = Maps.newHashMap();
		 mapObject.put("url",url);
		 mapObject.put("parameter", JSONObject.toJSONString(map));
		 result.setResult(mapObject);
		 result.setSuccess(true);
		 return result;
	 }

 }
