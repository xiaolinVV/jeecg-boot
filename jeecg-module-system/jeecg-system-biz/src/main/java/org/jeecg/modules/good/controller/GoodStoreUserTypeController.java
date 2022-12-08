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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.good.entity.GoodStoreType;
import org.jeecg.modules.good.entity.GoodStoreUserType;
import org.jeecg.modules.good.service.IGoodStoreTypeService;
import org.jeecg.modules.good.service.IGoodStoreUserTypeService;
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
import java.util.*;

 /**
 * @Description: 店铺商品常用分类记录
 * @Author: jeecg-boot
 * @Date:   2019-12-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺商品常用分类记录")
@RestController
@RequestMapping("/goodStoreUserType/goodStoreUserType")
public class GoodStoreUserTypeController {
	@Autowired
	private IGoodStoreUserTypeService goodStoreUserTypeService;
	 @Autowired
	 private IGoodStoreTypeService goodStoreTypeService;

	 /**
	  * 分页列表查询
	 * @param goodStoreUserType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺商品常用分类记录-分页列表查询")
	@ApiOperation(value="店铺商品常用分类记录-分页列表查询", notes="店铺商品常用分类记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodStoreUserType>> queryPageList(GoodStoreUserType goodStoreUserType,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodStoreUserType>> result = new Result<IPage<GoodStoreUserType>>();
		QueryWrapper<GoodStoreUserType> queryWrapper = QueryGenerator.initQueryWrapper(goodStoreUserType, req.getParameterMap());
		Page<GoodStoreUserType> page = new Page<GoodStoreUserType>(pageNo, pageSize);
		//权限配置
		PermissionUtils.accredit(queryWrapper);
		IPage<GoodStoreUserType> pageList = goodStoreUserTypeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodStoreUserType
	 * @return
	 */
	@AutoLog(value = "店铺商品常用分类记录-添加")
	@ApiOperation(value="店铺商品常用分类记录-添加", notes="店铺商品常用分类记录-添加")
	@PostMapping(value = "/add")
	public Result<GoodStoreUserType> add(@RequestBody GoodStoreUserType goodStoreUserType) {
		Result<GoodStoreUserType> result = new Result<GoodStoreUserType>();
		try {
			goodStoreUserTypeService.save(goodStoreUserType);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodStoreUserType
	 * @return
	 */
	@AutoLog(value = "店铺商品常用分类记录-编辑")
	@ApiOperation(value="店铺商品常用分类记录-编辑", notes="店铺商品常用分类记录-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodStoreUserType> edit(@RequestBody GoodStoreUserType goodStoreUserType) {
		Result<GoodStoreUserType> result = new Result<GoodStoreUserType>();
		GoodStoreUserType goodStoreUserTypeEntity = goodStoreUserTypeService.getById(goodStoreUserType.getId());
		if(goodStoreUserTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodStoreUserTypeService.updateById(goodStoreUserType);
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
	@AutoLog(value = "店铺商品常用分类记录-通过id删除")
	@ApiOperation(value="店铺商品常用分类记录-通过id删除", notes="店铺商品常用分类记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodStoreUserTypeService.removeById(id);
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
	@AutoLog(value = "店铺商品常用分类记录-批量删除")
	@ApiOperation(value="店铺商品常用分类记录-批量删除", notes="店铺商品常用分类记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodStoreUserType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodStoreUserType> result = new Result<GoodStoreUserType>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodStoreUserTypeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺商品常用分类记录-通过id查询")
	@ApiOperation(value="店铺商品常用分类记录-通过id查询", notes="店铺商品常用分类记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodStoreUserType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodStoreUserType> result = new Result<GoodStoreUserType>();
		GoodStoreUserType goodStoreUserType = goodStoreUserTypeService.getById(id);
		if(goodStoreUserType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodStoreUserType);
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
      QueryWrapper<GoodStoreUserType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodStoreUserType goodStoreUserType = JSON.parseObject(deString, GoodStoreUserType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodStoreUserType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodStoreUserType> pageList = goodStoreUserTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺商品常用分类记录列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodStoreUserType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺商品常用分类记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodStoreUserType> listGoodStoreUserTypes = ExcelImportUtil.importExcel(file.getInputStream(), GoodStoreUserType.class, params);
              goodStoreUserTypeService.saveBatch(listGoodStoreUserTypes);
              return Result.ok("文件导入成功！数据行数:" + listGoodStoreUserTypes.size());
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
	  * 查询常用分类集合返回id，name集合
	  * @return
	  */
	 @AutoLog(value = "最近使用的分类-分页列表查询")
	 @ApiOperation(value="最近使用的分类-分页列表查询", notes="最近使用的分类-分页列表查询")
	 @GetMapping(value = "/listGoodUserType")
	 public Result<Map<String,Object>> listGoodUserType(String sysUserId) {
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();

		 List<GoodStoreUserType>  listGoodUserType= goodStoreUserTypeService.getGoodStoreUserType(sysUserId);
		 GoodStoreType goodType;
		 String[] strings;
		 ArrayList<Map<String,String>> arrayList=new ArrayList<Map<String,String>>();
		 ArrayList<ArrayList<Map<String,String>>> arrayList1=new ArrayList<ArrayList<Map<String,String>>>();
		 Map<String,String> map=new HashMap<>();
		 Map<String,Object> map1=new HashMap<>();
		 if(listGoodUserType.size()>0){
			 for(GoodStoreUserType goodUserType:listGoodUserType){
				 arrayList=new ArrayList<Map<String,String>>();
				 strings=goodUserType.getGoodsStoreTypeDescribe().split(",");
				 if(strings.length>0){
					 for (String str:strings){
						 map=new HashMap<>();;
						 goodType=goodStoreTypeService.getById(str);
						 if(goodType!=null){
							 map.put("id",goodType.getId());
							 map.put("name",goodType.getName());
							 arrayList.add(map);
						 }
					 }
					 arrayList1.add(arrayList);
				 }
			 }
			 map1.put("listGoodUserType",arrayList1);
		 }
		 result.setSuccess(true);
		 result.setResult(map1);
		 return result;
	 }
	 /**
	  *   添加常用分类添加/addGoodUserType接口
	  * @param goodStoreUserType
	  * goodTypeId  传入分类最后一级Id
	  * sysUserId    传入供应商Id
	  * @return
	  */
	 @AutoLog(value = "最近使用的分类-添加")
	 @ApiOperation(value="最近使用的分类-添加", notes="最近使用的分类-添加")
	 @PostMapping(value = "/addGoodUserType")
	 public Result<GoodStoreUserType> addGoodUserType(@RequestBody GoodStoreUserType goodStoreUserType ) {
		 Result<GoodStoreUserType> result = new Result<GoodStoreUserType>();
		 try {
			 String str="";
			 if(goodStoreUserType.getGoodStoreTypeId()!=null ||goodStoreUserType.getGoodStoreTypeId().length()>0 ){
				 QueryWrapper<GoodStoreUserType> queryWrapper = new QueryWrapper<GoodStoreUserType>();
				 queryWrapper.eq("good_store_type_id",goodStoreUserType.getGoodStoreTypeId());
				 queryWrapper.eq("sys_user_id",goodStoreUserType.getSysUserId());
				 List<GoodStoreUserType> listgoodUserType1=goodStoreUserTypeService.list(queryWrapper);
				 if(listgoodUserType1.size()>0){//已存在处理
					 listgoodUserType1.get(0).setCreateTime(new Date());
					 goodStoreUserTypeService.updateById(listgoodUserType1.get(0));
					 result.success("已存在,修改成功！");
					 return result;
				 }
				 GoodStoreType goodType =goodStoreTypeService.getById(goodStoreUserType.getGoodStoreTypeId());
				 if(goodType!=null){
					 GoodStoreType goodType1=goodStoreTypeService.getById(goodType.getParentId());//上级菜单
					 if(goodType1!=null){
						 GoodStoreType goodType2=	goodStoreTypeService.getById(goodType1.getParentId());//上上级菜单
						 if(goodType2!=null){
							 str= str+goodType2.getId()+",";	//上上级菜单
						 }
						 str= str+goodType1.getId()+",";//上级菜单
					 }
					 str= str+goodType.getId()+",";//上级菜单
				 }
				 goodStoreUserType.setDelFlag("0");
				 goodStoreUserType.setGoodsStoreTypeDescribe(str);

				 goodStoreUserTypeService.save(goodStoreUserType);
				 result.success("添加成功！");
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }
}
