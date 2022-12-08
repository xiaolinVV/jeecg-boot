package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingActivityRewardDTO;
import org.jeecg.modules.marketing.entity.MarketingActivityReward;
import org.jeecg.modules.marketing.service.IMarketingActivityRewardService;
import org.jeecg.modules.marketing.vo.MarketingActivityRewardVO;
import org.jeecg.modules.system.service.ISysUserRoleService;
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
 * @Description: 活动奖励
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="活动奖励")
@RestController
@RequestMapping("/marketingActivityReward/marketingActivityReward")
public class MarketingActivityRewardController {
	@Autowired
	private IMarketingActivityRewardService marketingActivityRewardService;

	@Autowired
	private ISysUserRoleService iSysUserRoleService;
	/**
	  * 分页列表查询
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "活动奖励-分页列表查询")
	@ApiOperation(value="活动奖励-分页列表查询", notes="活动奖励-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingActivityRewardVO>> queryPageList(MarketingActivityRewardDTO marketingActivityRewardDTO,
																  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																  HttpServletRequest req) {
		Result<IPage<MarketingActivityRewardVO>> result = new Result<IPage<MarketingActivityRewardVO>>();
		Page<MarketingActivityRewardVO> page = new Page<MarketingActivityRewardVO>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		if (roleByUserId.contains("Merchant")){
			marketingActivityRewardDTO.setSysUserId(sysUser.getId());
		}
		IPage<MarketingActivityRewardVO> pageList = marketingActivityRewardService.queryPageList(page,marketingActivityRewardDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingActivityReward
	 * @return
	 */
	@AutoLog(value = "活动奖励-添加")
	@ApiOperation(value="活动奖励-添加", notes="活动奖励-添加")
	@PostMapping(value = "/add")
	public Result<MarketingActivityReward> add(@RequestBody MarketingActivityReward marketingActivityReward) {
		Result<MarketingActivityReward> result = new Result<MarketingActivityReward>();
		try {
			marketingActivityRewardService.save(marketingActivityReward);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingActivityReward
	 * @return
	 */
	@AutoLog(value = "活动奖励-编辑")
	@ApiOperation(value="活动奖励-编辑", notes="活动奖励-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingActivityReward> edit(@RequestBody MarketingActivityReward marketingActivityReward) {
		Result<MarketingActivityReward> result = new Result<MarketingActivityReward>();
		MarketingActivityReward marketingActivityRewardEntity = marketingActivityRewardService.getById(marketingActivityReward.getId());
		if(marketingActivityRewardEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingActivityRewardService.updateById(marketingActivityReward);
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
	@AutoLog(value = "活动奖励-通过id删除")
	@ApiOperation(value="活动奖励-通过id删除", notes="活动奖励-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingActivityRewardService.removeById(id);
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
	@AutoLog(value = "活动奖励-批量删除")
	@ApiOperation(value="活动奖励-批量删除", notes="活动奖励-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingActivityReward> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingActivityReward> result = new Result<MarketingActivityReward>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingActivityRewardService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动奖励-通过id查询")
	@ApiOperation(value="活动奖励-通过id查询", notes="活动奖励-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingActivityReward> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingActivityReward> result = new Result<MarketingActivityReward>();
		MarketingActivityReward marketingActivityReward = marketingActivityRewardService.getById(id);
		if(marketingActivityReward==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingActivityReward);
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
      QueryWrapper<MarketingActivityReward> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingActivityReward marketingActivityReward = JSON.parseObject(deString, MarketingActivityReward.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingActivityReward, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingActivityReward> pageList = marketingActivityRewardService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "活动奖励列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingActivityReward.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("活动奖励列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingActivityReward> listMarketingActivityRewards = ExcelImportUtil.importExcel(file.getInputStream(), MarketingActivityReward.class, params);
              marketingActivityRewardService.saveBatch(listMarketingActivityRewards);
              return Result.ok("文件导入成功！数据行数:" + listMarketingActivityRewards.size());
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
