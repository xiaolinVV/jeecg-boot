package org.jeecg.modules.marketing.controller;

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
import org.jeecg.modules.marketing.entity.MarketingLeagueIdentity;
import org.jeecg.modules.marketing.service.IMarketingLeagueIdentityService;
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
 * @Description: 身份设置
 * @Author: jeecg-boot
 * @Date:   2021-12-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="身份设置")
@RestController
@RequestMapping("/marketing/marketingLeagueIdentity")
public class MarketingLeagueIdentityController {
	@Autowired
	private IMarketingLeagueIdentityService marketingLeagueIdentityService;


	@GetMapping("getAll")
	@ResponseBody
	public Result<?> getAll(){
		return Result.ok(marketingLeagueIdentityService.list());
	}

	
	/**
	  * 分页列表查询
	 * @param marketingLeagueIdentity
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "身份设置-分页列表查询")
	@ApiOperation(value="身份设置-分页列表查询", notes="身份设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingLeagueIdentity>> queryPageList(MarketingLeagueIdentity marketingLeagueIdentity,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingLeagueIdentity>> result = new Result<IPage<MarketingLeagueIdentity>>();
		QueryWrapper<MarketingLeagueIdentity> queryWrapper = QueryGenerator.initQueryWrapper(marketingLeagueIdentity, req.getParameterMap());
		Page<MarketingLeagueIdentity> page = new Page<MarketingLeagueIdentity>(pageNo, pageSize);
		IPage<MarketingLeagueIdentity> pageList = marketingLeagueIdentityService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingLeagueIdentity
	 * @return
	 */
	@AutoLog(value = "身份设置-添加")
	@ApiOperation(value="身份设置-添加", notes="身份设置-添加")
	@PostMapping(value = "/add")
	public Result<MarketingLeagueIdentity> add(@RequestBody MarketingLeagueIdentity marketingLeagueIdentity) {
		Result<MarketingLeagueIdentity> result = new Result<MarketingLeagueIdentity>();
		try {
			marketingLeagueIdentityService.save(marketingLeagueIdentity);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingLeagueIdentity
	 * @return
	 */
	@AutoLog(value = "身份设置-编辑")
	@ApiOperation(value="身份设置-编辑", notes="身份设置-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingLeagueIdentity> edit(@RequestBody MarketingLeagueIdentity marketingLeagueIdentity) {
		Result<MarketingLeagueIdentity> result = new Result<MarketingLeagueIdentity>();
		MarketingLeagueIdentity marketingLeagueIdentityEntity = marketingLeagueIdentityService.getById(marketingLeagueIdentity.getId());
		if(marketingLeagueIdentityEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingLeagueIdentityService.updateById(marketingLeagueIdentity);
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
	@AutoLog(value = "身份设置-通过id删除")
	@ApiOperation(value="身份设置-通过id删除", notes="身份设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingLeagueIdentityService.removeById(id);
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
	@AutoLog(value = "身份设置-批量删除")
	@ApiOperation(value="身份设置-批量删除", notes="身份设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingLeagueIdentity> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingLeagueIdentity> result = new Result<MarketingLeagueIdentity>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingLeagueIdentityService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "身份设置-通过id查询")
	@ApiOperation(value="身份设置-通过id查询", notes="身份设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingLeagueIdentity> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingLeagueIdentity> result = new Result<MarketingLeagueIdentity>();
		MarketingLeagueIdentity marketingLeagueIdentity = marketingLeagueIdentityService.getById(id);
		if(marketingLeagueIdentity==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingLeagueIdentity);
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
      QueryWrapper<MarketingLeagueIdentity> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingLeagueIdentity marketingLeagueIdentity = JSON.parseObject(deString, MarketingLeagueIdentity.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingLeagueIdentity, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingLeagueIdentity> pageList = marketingLeagueIdentityService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "身份设置列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingLeagueIdentity.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("身份设置列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingLeagueIdentity> listMarketingLeagueIdentitys = ExcelImportUtil.importExcel(file.getInputStream(), MarketingLeagueIdentity.class, params);
              marketingLeagueIdentityService.saveBatch(listMarketingLeagueIdentitys);
              return Result.ok("文件导入成功！数据行数:" + listMarketingLeagueIdentitys.size());
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
