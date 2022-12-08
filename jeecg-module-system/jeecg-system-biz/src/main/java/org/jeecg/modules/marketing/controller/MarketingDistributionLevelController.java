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
import org.jeecg.modules.marketing.entity.MarketingDistributionLevel;
import org.jeecg.modules.marketing.service.IMarketingDistributionLevelService;
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
 * @Description: 分销等级
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags="分销等级")
@RestController
@RequestMapping("/marketing/marketingDistributionLevel")
public class MarketingDistributionLevelController {
	@Autowired
	private IMarketingDistributionLevelService marketingDistributionLevelService;

	/**
	  * 分页列表查询
	 * @param marketingDistributionLevel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "分销等级-分页列表查询")
	@ApiOperation(value="分销等级-分页列表查询", notes="分销等级-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingDistributionLevel>> queryPageList(MarketingDistributionLevel marketingDistributionLevel,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingDistributionLevel>> result = new Result<IPage<MarketingDistributionLevel>>();
		QueryWrapper<MarketingDistributionLevel> queryWrapper = QueryGenerator.initQueryWrapper(marketingDistributionLevel, req.getParameterMap());
		Page<MarketingDistributionLevel> page = new Page<MarketingDistributionLevel>(pageNo, pageSize);
		IPage<MarketingDistributionLevel> pageList = marketingDistributionLevelService.page(page, queryWrapper);

		pageList.getRecords().forEach(pl->{
			if (pl.getWaysObtain().equals("0")){
				pl.setWaysObtainExplain("默认获得");
			}else if (pl.getWaysObtain().equals("1")){
				pl.setWaysObtainExplain("参与拼购");
			}else if (pl.getWaysObtain().equals("2")){
				pl.setWaysObtainExplain("直推"+pl.getDirect()+"人,"+"团队"+pl.getTeamNumber()+"人");
			}else if (pl.getWaysObtain().equals("3")){
				pl.setWaysObtainExplain("直推"+pl.getDirect()+"人,团队成员有"+marketingDistributionLevelService
						.getById(pl.getMarketingDistributionLevelId()).getLevelName()+"等级"+pl.getLevelNumber()+"人");
			}else {
				pl.setWaysObtainExplain("抢购25次");
			}
		});
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingDistributionLevel
	 * @return
	 */
	@AutoLog(value = "分销等级-添加")
	@ApiOperation(value="分销等级-添加", notes="分销等级-添加")
	@PostMapping(value = "/add")
	public Result<MarketingDistributionLevel> add(@RequestBody MarketingDistributionLevel marketingDistributionLevel) {
		Result<MarketingDistributionLevel> result = new Result<MarketingDistributionLevel>();
		try {
			marketingDistributionLevelService.save(marketingDistributionLevel);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingDistributionLevel
	 * @return
	 */
	@AutoLog(value = "分销等级-编辑")
	@ApiOperation(value="分销等级-编辑", notes="分销等级-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingDistributionLevel> edit(@RequestBody MarketingDistributionLevel marketingDistributionLevel) {
		Result<MarketingDistributionLevel> result = new Result<MarketingDistributionLevel>();
		MarketingDistributionLevel marketingDistributionLevelEntity = marketingDistributionLevelService.getById(marketingDistributionLevel.getId());
		if(marketingDistributionLevelEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingDistributionLevelService.updateById(marketingDistributionLevel);
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
	@AutoLog(value = "分销等级-通过id删除")
	@ApiOperation(value="分销等级-通过id删除", notes="分销等级-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingDistributionLevelService.removeById(id);
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
	@AutoLog(value = "分销等级-批量删除")
	@ApiOperation(value="分销等级-批量删除", notes="分销等级-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingDistributionLevel> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingDistributionLevel> result = new Result<MarketingDistributionLevel>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingDistributionLevelService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "分销等级-通过id查询")
	@ApiOperation(value="分销等级-通过id查询", notes="分销等级-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingDistributionLevel> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingDistributionLevel> result = new Result<MarketingDistributionLevel>();
		MarketingDistributionLevel marketingDistributionLevel = marketingDistributionLevelService.getById(id);
		if(marketingDistributionLevel==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingDistributionLevel);
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
      QueryWrapper<MarketingDistributionLevel> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingDistributionLevel marketingDistributionLevel = JSON.parseObject(deString, MarketingDistributionLevel.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingDistributionLevel, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingDistributionLevel> pageList = marketingDistributionLevelService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "分销等级列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingDistributionLevel.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("分销等级列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingDistributionLevel> listMarketingDistributionLevels = ExcelImportUtil.importExcel(file.getInputStream(), MarketingDistributionLevel.class, params);
              marketingDistributionLevelService.saveBatch(listMarketingDistributionLevels);
              return Result.ok("文件导入成功！数据行数:" + listMarketingDistributionLevels.size());
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
