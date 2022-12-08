package org.jeecg.modules.provider.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.provider.dto.ProviderAddressDTO;
import org.jeecg.modules.provider.entity.ProviderAddress;
import org.jeecg.modules.provider.service.IProviderAddressService;
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
 * @Description: 供应商地址库
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags="供应商地址库")
@RestController
@RequestMapping("/providerAddress/providerAddress")
public class ProviderAddressController {
	@Autowired
	private IProviderAddressService providerAddressService;
	@Autowired
	private IOrderProviderListService orderProviderListService;
	/**
	  * 分页列表查询
	 * @param providerAddress
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "供应商地址库-分页列表查询")
	@ApiOperation(value="供应商地址库-分页列表查询", notes="供应商地址库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProviderAddress>> queryPageList(ProviderAddress providerAddress,
														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														HttpServletRequest req) {
		Result<IPage<ProviderAddress>> result = new Result<IPage<ProviderAddress>>();
		QueryWrapper<ProviderAddress> queryWrapper = QueryGenerator.initQueryWrapper(providerAddress, req.getParameterMap());
		PermissionUtils.accredit(queryWrapper);
		Page<ProviderAddress> page = new Page<ProviderAddress>(pageNo, pageSize);
		IPage<ProviderAddress> pageList = providerAddressService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param providerAddress
	 * @return
	 */
	@AutoLog(value = "供应商地址库-添加")
	@ApiOperation(value="供应商地址库-添加", notes="供应商地址库-添加")
	@PostMapping(value = "/add")
	public Result<ProviderAddress> add(@RequestBody ProviderAddress providerAddress) {
		Result<ProviderAddress> result = new Result<ProviderAddress>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		providerAddress.setSysUserId(sysUser.getId());
		try {
			providerAddressService.save(providerAddress);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param providerAddress
	 * @return
	 */
	@AutoLog(value = "供应商地址库-编辑")
	@ApiOperation(value="供应商地址库-编辑", notes="供应商地址库-编辑")
	@PutMapping(value = "/edit")
	public Result<ProviderAddress> edit(@RequestBody ProviderAddress providerAddress) {
		Result<ProviderAddress> result = new Result<ProviderAddress>();
		ProviderAddress providerAddressEntity = providerAddressService.getById(providerAddress.getId());
		if(providerAddressEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = providerAddressService.updateById(providerAddress);
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
	@AutoLog(value = "供应商地址库-通过id删除")
	@ApiOperation(value="供应商地址库-通过id删除", notes="供应商地址库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			providerAddressService.removeById(id);
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
	@AutoLog(value = "供应商地址库-批量删除")
	@ApiOperation(value="供应商地址库-批量删除", notes="供应商地址库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<ProviderAddress> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<ProviderAddress> result = new Result<ProviderAddress>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.providerAddressService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商地址库-通过id查询")
	@ApiOperation(value="供应商地址库-通过id查询", notes="供应商地址库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProviderAddress> queryById(@RequestParam(name="id",required=true) String id) {
		Result<ProviderAddress> result = new Result<ProviderAddress>();
		ProviderAddress providerAddress = providerAddressService.getById(id);
		if(providerAddress==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(providerAddress);
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
      QueryWrapper<ProviderAddress> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              ProviderAddress providerAddress = JSON.parseObject(deString, ProviderAddress.class);
              queryWrapper = QueryGenerator.initQueryWrapper(providerAddress, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<ProviderAddress> pageList = providerAddressService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "供应商地址库列表");
      mv.addObject(NormalExcelConstants.CLASS, ProviderAddress.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商地址库列表数据", "导出人:Jeecg", "导出信息"));
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
              List<ProviderAddress> listProviderAddresss = ExcelImportUtil.importExcel(file.getInputStream(), ProviderAddress.class, params);
              providerAddressService.saveBatch(listProviderAddresss);
              return Result.ok("文件导入成功！数据行数:" + listProviderAddresss.size());
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
	  * 根据 member_list_id 查询列表
	  * @param

	  * @return
	  */
	 @GetMapping(value = "/ProviderAddressDTOList")
	 public Result<List<ProviderAddressDTO>> ProviderAddressDTOList(@RequestParam(name="orderProviderListId",required=true)String orderProviderListId,
																 HttpServletRequest req) {
		 Result<List<ProviderAddressDTO>> result = new Result<List<ProviderAddressDTO>>();
		 try {
			OrderProviderList orderProviderList = orderProviderListService.getById(orderProviderListId);
			if(orderProviderList!=null){
				QueryWrapper<ProviderAddressDTO> queryWrapper = new QueryWrapper<ProviderAddressDTO>();
				//供应商发货信息
				Map<String,String> paramMap = Maps.newHashMap();
				paramMap.put("sysUserId",orderProviderList.getSysUserId());
				paramMap.put("isDeliver","");//发货默认
				paramMap.put("isReturn","");//退货
				List<ProviderAddressDTO> listProviderAddressDTO= providerAddressService.getlistProviderAddress(paramMap);
				result.setSuccess(true);
				result.setResult(listProviderAddressDTO);
			}else{
				result.error500("未找到对应实体");
			}

		 }catch (Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }

		 return result;
	 }
	 /**
	  * 根据id改变是否默认收货地址
	  *
	  * @param id
	  * @param isDeliver
	  * @return
	  */
	 @RequestMapping(value = "/updataIsDeliverById", method = RequestMethod.GET)
	 public Result<ProviderAddress> updataIsDeliverById(@RequestParam(name = "id", required = true) String id,
													 @RequestParam(name = "isDeliver", required = true) String isDeliver) {
		 Result<ProviderAddress> result = new Result<>();
		 ProviderAddress providerAddress = new ProviderAddress();
		 try {
			 if (oConvertUtils.isNotEmpty(id)) {
				 providerAddressService.updateIsDeliverById(providerAddress);
				 ProviderAddress byId = providerAddressService.getById(id);
				 byId.setIsDeliver(isDeliver);
				 providerAddressService.updateById(byId);
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败" + e.getMessage());
		 }
		 result.success("操作成功");
		 return result;
	 }
	 /**
	  * 根据id改变是否默认退货地址
	  *
	  * @param id
	  * @param isReturn
	  * @return
	  */
	 @RequestMapping(value = "/updataIsReturnById", method = RequestMethod.GET)
	 public Result<ProviderAddress> updataIsReturnById(@RequestParam(name = "id", required = true) String id,
													@RequestParam(name = "isReturn", required = true) String isReturn) {
		 Result<ProviderAddress> result = new Result<>();
		 ProviderAddress providerAddress = new ProviderAddress();
		 try {
			 if (oConvertUtils.isNotEmpty(id)) {
				 providerAddressService.updateIsReturnById(providerAddress);
				 ProviderAddress byId = providerAddressService.getById(id);
				 byId.setIsReturn(isReturn);
				 providerAddressService.updateById(byId);
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败" + e.getMessage());
		 }
		 result.success("操作成功");
		 return result;
	 }
}
