package org.jeecg.modules.store.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.entity.StoreBiddingEarnestMoney;
import org.jeecg.modules.store.service.IStoreBiddingEarnestMoneyService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 竞价-保证金记录
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="竞价-保证金记录")
@RestController
@RequestMapping("/store/storeBiddingEarnestMoney")
public class StoreBiddingEarnestMoneyController {
	@Autowired
	private IStoreBiddingEarnestMoneyService storeBiddingEarnestMoneyService;
	
	/**
	  * 分页列表查询
	 * @param storeBiddingEarnestMoney
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "竞价-保证金记录-分页列表查询")
	@ApiOperation(value="竞价-保证金记录-分页列表查询", notes="竞价-保证金记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreBiddingEarnestMoney>> queryPageList(StoreBiddingEarnestMoney storeBiddingEarnestMoney,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StoreBiddingEarnestMoney>> result = new Result<IPage<StoreBiddingEarnestMoney>>();
		QueryWrapper<StoreBiddingEarnestMoney> queryWrapper = QueryGenerator.initQueryWrapper(storeBiddingEarnestMoney, req.getParameterMap());
		Page<StoreBiddingEarnestMoney> page = new Page<StoreBiddingEarnestMoney>(pageNo, pageSize);
		IPage<StoreBiddingEarnestMoney> pageList = storeBiddingEarnestMoneyService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeBiddingEarnestMoney
	 * @return
	 */
	@AutoLog(value = "竞价-保证金记录-添加")
	@ApiOperation(value="竞价-保证金记录-添加", notes="竞价-保证金记录-添加")
	@PostMapping(value = "/add")
	public Result<StoreBiddingEarnestMoney> add(@RequestBody StoreBiddingEarnestMoney storeBiddingEarnestMoney) {
		Result<StoreBiddingEarnestMoney> result = new Result<StoreBiddingEarnestMoney>();
		try {
			storeBiddingEarnestMoneyService.save(storeBiddingEarnestMoney);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeBiddingEarnestMoney
	 * @return
	 */
	@AutoLog(value = "竞价-保证金记录-编辑")
	@ApiOperation(value="竞价-保证金记录-编辑", notes="竞价-保证金记录-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreBiddingEarnestMoney> edit(@RequestBody StoreBiddingEarnestMoney storeBiddingEarnestMoney) {
		Result<StoreBiddingEarnestMoney> result = new Result<StoreBiddingEarnestMoney>();
		StoreBiddingEarnestMoney storeBiddingEarnestMoneyEntity = storeBiddingEarnestMoneyService.getById(storeBiddingEarnestMoney.getId());
		if(storeBiddingEarnestMoneyEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeBiddingEarnestMoneyService.updateById(storeBiddingEarnestMoney);
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
	@AutoLog(value = "竞价-保证金记录-通过id删除")
	@ApiOperation(value="竞价-保证金记录-通过id删除", notes="竞价-保证金记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeBiddingEarnestMoneyService.removeById(id);
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
	@AutoLog(value = "竞价-保证金记录-批量删除")
	@ApiOperation(value="竞价-保证金记录-批量删除", notes="竞价-保证金记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreBiddingEarnestMoney> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreBiddingEarnestMoney> result = new Result<StoreBiddingEarnestMoney>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeBiddingEarnestMoneyService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "竞价-保证金记录-通过id查询")
	@ApiOperation(value="竞价-保证金记录-通过id查询", notes="竞价-保证金记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreBiddingEarnestMoney> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreBiddingEarnestMoney> result = new Result<StoreBiddingEarnestMoney>();
		StoreBiddingEarnestMoney storeBiddingEarnestMoney = storeBiddingEarnestMoneyService.getById(id);
		if(storeBiddingEarnestMoney==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeBiddingEarnestMoney);
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
      QueryWrapper<StoreBiddingEarnestMoney> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreBiddingEarnestMoney storeBiddingEarnestMoney = JSON.parseObject(deString, StoreBiddingEarnestMoney.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeBiddingEarnestMoney, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreBiddingEarnestMoney> pageList = storeBiddingEarnestMoneyService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "竞价-保证金记录列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreBiddingEarnestMoney.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("竞价-保证金记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreBiddingEarnestMoney> listStoreBiddingEarnestMoneys = ExcelImportUtil.importExcel(file.getInputStream(), StoreBiddingEarnestMoney.class, params);
              storeBiddingEarnestMoneyService.saveBatch(listStoreBiddingEarnestMoneys);
              return Result.ok("文件导入成功！数据行数:" + listStoreBiddingEarnestMoneys.size());
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
