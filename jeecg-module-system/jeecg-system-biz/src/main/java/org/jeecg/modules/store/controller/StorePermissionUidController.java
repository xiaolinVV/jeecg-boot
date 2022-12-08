package org.jeecg.modules.store.controller;

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
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.entity.StorePermission;
import org.jeecg.modules.store.entity.StorePermissionUid;
import org.jeecg.modules.store.service.IStorePermissionService;
import org.jeecg.modules.store.service.IStorePermissionUidService;
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
 * @Description: 客户端菜单授权
 * @Author: jeecg-boot
 * @Date:   2020-04-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="客户端菜单授权")
@RestController
@RequestMapping("/storePermissionUid/storePermissionUid")
public class StorePermissionUidController {
	@Autowired
	private IStorePermissionUidService storePermissionUidService;
	 @Autowired
	 private IStorePermissionService storePermissionService;
	/**
	  * 分页列表查询
	 * @param storePermissionUid
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "客户端菜单授权-分页列表查询")
	@ApiOperation(value="客户端菜单授权-分页列表查询", notes="客户端菜单授权-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StorePermissionUid>> queryPageList(StorePermissionUid storePermissionUid,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StorePermissionUid>> result = new Result<IPage<StorePermissionUid>>();
		QueryWrapper<StorePermissionUid> queryWrapper = QueryGenerator.initQueryWrapper(storePermissionUid, req.getParameterMap());
		Page<StorePermissionUid> page = new Page<StorePermissionUid>(pageNo, pageSize);
		IPage<StorePermissionUid> pageList = storePermissionUidService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storePermissionUid
	 * @return
	 */
	@AutoLog(value = "客户端菜单授权-添加")
	@ApiOperation(value="客户端菜单授权-添加", notes="客户端菜单授权-添加")
	@PostMapping(value = "/add")
	public Result<StorePermissionUid> add(@RequestBody StorePermissionUid storePermissionUid) {
		Result<StorePermissionUid> result = new Result<StorePermissionUid>();
		try {
			storePermissionUidService.save(storePermissionUid);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storePermissionUid
	 * @return
	 */
	@AutoLog(value = "客户端菜单授权-编辑")
	@ApiOperation(value="客户端菜单授权-编辑", notes="客户端菜单授权-编辑")
	@PutMapping(value = "/edit")
	public Result<StorePermissionUid> edit(@RequestBody StorePermissionUid storePermissionUid) {
		Result<StorePermissionUid> result = new Result<StorePermissionUid>();
		StorePermissionUid storePermissionUidEntity = storePermissionUidService.getById(storePermissionUid.getId());
		if(storePermissionUidEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storePermissionUidService.updateById(storePermissionUid);
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
	@AutoLog(value = "客户端菜单授权-通过id删除")
	@ApiOperation(value="客户端菜单授权-通过id删除", notes="客户端菜单授权-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storePermissionUidService.removeById(id);
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
	@AutoLog(value = "客户端菜单授权-批量删除")
	@ApiOperation(value="客户端菜单授权-批量删除", notes="客户端菜单授权-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StorePermissionUid> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StorePermissionUid> result = new Result<StorePermissionUid>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storePermissionUidService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户端菜单授权-通过id查询")
	@ApiOperation(value="客户端菜单授权-通过id查询", notes="客户端菜单授权-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StorePermissionUid> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StorePermissionUid> result = new Result<StorePermissionUid>();
		StorePermissionUid storePermissionUid = storePermissionUidService.getById(id);
		if(storePermissionUid==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storePermissionUid);
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
      QueryWrapper<StorePermissionUid> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StorePermissionUid storePermissionUid = JSON.parseObject(deString, StorePermissionUid.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storePermissionUid, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StorePermissionUid> pageList = storePermissionUidService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "客户端菜单授权列表");
      mv.addObject(NormalExcelConstants.CLASS, StorePermissionUid.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客户端菜单授权列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StorePermissionUid> listStorePermissionUids = ExcelImportUtil.importExcel(file.getInputStream(), StorePermissionUid.class, params);
              storePermissionUidService.saveBatch(listStorePermissionUids);
              return Result.ok("文件导入成功！数据行数:" + listStorePermissionUids.size());
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
	  * 查询商家端菜单
	  * @param sysUserId
	  * @return
	  */
	 @GetMapping(value = "/getStorePermissionUidMap")
	 public Result<List<Map<String,Object>>> getStorePermissionUidMap(String sysUserId){
		 Result<List<Map<String,Object>>> result = new Result();
		 List<Map<String,Object>> mapList = storePermissionUidService.getStorePermissionUidMap(sysUserId);
		 result.setResult(mapList);
		 result.success("查询成功");
		 return result;
	 }


	 /**
	  * 查询商家端菜单
	  * @param sysUserId
	  * @return
	  */
	 @GetMapping(value = "/addStorePermissionUid")
	 public Result<List<Map<String,Object>>> addStorePermissionUid(String sysUserId,String ids){
		 Result<List<Map<String,Object>>> result = new Result();
		 if (StringUtils.isEmpty(ids)) {
			 result.error500("参数不识别！");
		 } else {
			 StorePermissionUid storePermissionUid;
			 StorePermission storePermission;
			 try {
				 QueryWrapper<StorePermissionUid> queryWrapperStorePermissionUid =new QueryWrapper();
				 queryWrapperStorePermissionUid.eq("sys_user_id",sysUserId);
				 List<StorePermissionUid> storePermissionUidList = storePermissionUidService.list(queryWrapperStorePermissionUid);
				 List<String> listid = Arrays.asList(ids.split(","));
				 if(storePermissionUidList.size() > 0){
					 //修改/新增
					 QueryWrapper<StorePermissionUid> queryWrapper =new QueryWrapper();
					 for (String id : listid) {
						 queryWrapperStorePermissionUid = new QueryWrapper();
						 queryWrapperStorePermissionUid.eq("sys_user_id", sysUserId);
						 queryWrapperStorePermissionUid.eq("store_permission_id", id);
						 storePermissionUidList = storePermissionUidService.list(queryWrapperStorePermissionUid);
						 if(storePermissionUidList.size() == 0){
						 	//新增
							 storePermission = storePermissionService.getById(id);
							 storePermissionUid =new StorePermissionUid();
							 storePermissionUid.setName(storePermission.getName());
							 storePermissionUid.setStorePermissionId(storePermission.getId());

							 storePermissionUid.setSysUserId(sysUserId) ;
							 storePermissionUid.setStatus("1");
							 storePermissionUidService.save(storePermissionUid);
						 }else{
						 	//修改
							 storePermissionUidList.get(0).setStatus("1");
							 storePermissionUidService.updateById( storePermissionUidList.get(0));
						 }


					 }
					 queryWrapperStorePermissionUid = new QueryWrapper();
					 queryWrapperStorePermissionUid.eq("sys_user_id", sysUserId);
					 queryWrapperStorePermissionUid.notIn("store_permission_id", listid);
					//不存在去除权限的id
					 List<StorePermissionUid> delectStorePermissionUidList = storePermissionUidService.list(queryWrapperStorePermissionUid);
				     for(StorePermissionUid storePermissionUid1:delectStorePermissionUidList){
				     	//删除多余权限菜单
						 storePermissionUidService.removeById(storePermissionUid1.getId());
					 }
				 }else{

					 //新增
					 for (String id : listid) {
						 storePermission = storePermissionService.getById(id);
						 storePermissionUid =new StorePermissionUid();
						 storePermissionUid.setName(storePermission.getName());
						 storePermissionUid.setStorePermissionId(storePermission.getId());
						 storePermissionUid.setSysUserId(sysUserId) ;
						 storePermissionUid.setStatus("1");
						 storePermissionUidService.save(storePermissionUid);
					 }
				 }

				 result.success("修改成功!");
			 } catch (Exception e) {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }


 }
