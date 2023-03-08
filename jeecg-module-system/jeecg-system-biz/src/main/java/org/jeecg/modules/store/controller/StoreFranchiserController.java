package org.jeecg.modules.store.controller;

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
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.entity.StoreFranchiser;
import org.jeecg.modules.store.service.IStoreFranchiserService;
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
 * @Description: 店铺经销商
 * @Author: jeecg-boot
 * @Date:   2022-10-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺经销商")
@RestController
@RequestMapping("/store/storeFranchiser")
public class StoreFranchiserController {
	@Autowired
	private IStoreFranchiserService storeFranchiserService;

	@Autowired
	private IMemberListService iMemberListService;
	
	/**
	  * 分页列表查询
	 * @param storeFranchiser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺经销商-分页列表查询")
	@ApiOperation(value="店铺经销商-分页列表查询", notes="店铺经销商-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreFranchiser>> queryPageList(StoreFranchiser storeFranchiser,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StoreFranchiser>> result = new Result<IPage<StoreFranchiser>>();
		QueryWrapper<StoreFranchiser> queryWrapper = QueryGenerator.initQueryWrapper(storeFranchiser, req.getParameterMap());
		Page<StoreFranchiser> page = new Page<StoreFranchiser>(pageNo, pageSize);
		IPage<StoreFranchiser> pageList = storeFranchiserService.page(page, queryWrapper);
		pageList.getRecords().forEach(s ->{
			s.setMemberList(iMemberListService.getById(s.getMemberListId()));
		} );
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeFranchiser
	 * @return
	 */
	@AutoLog(value = "店铺经销商-添加")
	@ApiOperation(value="店铺经销商-添加", notes="店铺经销商-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StoreFranchiser storeFranchiser) {
		Result<StoreFranchiser> result = new Result<StoreFranchiser>();
		try {
			if(storeFranchiserService.count(new LambdaQueryWrapper<StoreFranchiser>().eq(StoreFranchiser::getStoreManageId,storeFranchiser.getStoreManageId()).eq(StoreFranchiser::getMemberListId,storeFranchiser.getMemberListId()))>0){
				return Result.error("在商户已经是经销商");
			}
			storeFranchiser.setSerialnumber(OrderNoUtils.getOrderNo());
			storeFranchiserService.save(storeFranchiser);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeFranchiser
	 * @return
	 */
	@AutoLog(value = "店铺经销商-编辑")
	@ApiOperation(value="店铺经销商-编辑", notes="店铺经销商-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreFranchiser> edit(@RequestBody StoreFranchiser storeFranchiser) {
		Result<StoreFranchiser> result = new Result<StoreFranchiser>();
		StoreFranchiser storeFranchiserEntity = storeFranchiserService.getById(storeFranchiser.getId());
		if(storeFranchiserEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeFranchiserService.updateById(storeFranchiser);
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
	@AutoLog(value = "店铺经销商-通过id删除")
	@ApiOperation(value="店铺经销商-通过id删除", notes="店铺经销商-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeFranchiserService.removeById(id);
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
	@AutoLog(value = "店铺经销商-批量删除")
	@ApiOperation(value="店铺经销商-批量删除", notes="店铺经销商-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreFranchiser> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreFranchiser> result = new Result<StoreFranchiser>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeFranchiserService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺经销商-通过id查询")
	@ApiOperation(value="店铺经销商-通过id查询", notes="店铺经销商-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreFranchiser> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreFranchiser> result = new Result<StoreFranchiser>();
		StoreFranchiser storeFranchiser = storeFranchiserService.getById(id);
		if(storeFranchiser==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeFranchiser);
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
      QueryWrapper<StoreFranchiser> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreFranchiser storeFranchiser = JSON.parseObject(deString, StoreFranchiser.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeFranchiser, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreFranchiser> pageList = storeFranchiserService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺经销商列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreFranchiser.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺经销商列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreFranchiser> listStoreFranchisers = ExcelImportUtil.importExcel(file.getInputStream(), StoreFranchiser.class, params);
              storeFranchiserService.saveBatch(listStoreFranchisers);
              return Result.ok("文件导入成功！数据行数:" + listStoreFranchisers.size());
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
