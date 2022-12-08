package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingDistributionLevel;
import org.jeecg.modules.marketing.service.IMarketingDistributionLevelService;
import org.jeecg.modules.member.dto.MemberDistributionLevelDTO;
import org.jeecg.modules.member.entity.MemberDistributionLevel;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
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
 * @Description: 会员和分销级别关系
 * @Author: jeecg-boot
 * @Date:   2021-07-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="会员和分销级别关系")
@RestController
@RequestMapping("/member/memberDistributionLevel")
public class MemberDistributionLevelController {
	@Autowired
	private IMemberDistributionLevelService memberDistributionLevelService;


	@Autowired
	private IMarketingDistributionLevelService iMarketingDistributionLevelService;


	 /**
	  * 设置等级
	  *
	  * @param id
	  * @param levelName
	  * @return
	  */
	@RequestMapping("settingLevel")
	@ResponseBody
	public Result<?> settingLevel(String id,String levelName){
		//参数校验
		if(StringUtils.isBlank(id)){
			return Result.error("关系id不能为空");
		}
		if(StringUtils.isBlank(levelName)){
			return Result.error("等级名称不能为空");
		}
		MemberDistributionLevel memberDistributionLevel=memberDistributionLevelService.getById(id);
		if(memberDistributionLevel==null){
			return Result.error("等级关系不存在");
		}
		MarketingDistributionLevel marketingDistributionLevel=iMarketingDistributionLevelService.getOne(new LambdaQueryWrapper<MarketingDistributionLevel>()
				.eq(MarketingDistributionLevel::getLevelName,levelName));
		if(marketingDistributionLevel==null){
			return Result.error("等级不存在");
		}
		memberDistributionLevel.setMarketingDistributionLevelId(marketingDistributionLevel.getId());
		memberDistributionLevelService.saveOrUpdate(memberDistributionLevel);
		return Result.ok("等级设置成功");
	}




	 /**
	  * 获取级别列表
	  *
	  * @return
	  */
	@RequestMapping("getMarketingDistributionLevelList")
	@ResponseBody
	public Result<?> getMarketingDistributionLevelList(){
		return Result.ok(iMarketingDistributionLevelService.list(new LambdaQueryWrapper<MarketingDistributionLevel>()
				.eq(MarketingDistributionLevel::getStatus,"1")
				.orderByAsc(MarketingDistributionLevel::getGrade)));
	}


	
	/**
	  * 分页列表查询
	 * @param memberDistributionLevelDTO
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value="会员和分销级别关系-分页列表查询", notes="会员和分销级别关系-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MemberDistributionLevelDTO memberDistributionLevelDTO,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(memberDistributionLevelService.queryPageList(new Page<>(pageNo,pageSize),memberDistributionLevelDTO));
	}
	
	/**
	  *   添加
	 * @param memberDistributionLevel
	 * @return
	 */
	@AutoLog(value = "会员和分销级别关系-添加")
	@ApiOperation(value="会员和分销级别关系-添加", notes="会员和分销级别关系-添加")
	@PostMapping(value = "/add")
	public Result<MemberDistributionLevel> add(@RequestBody MemberDistributionLevel memberDistributionLevel) {
		Result<MemberDistributionLevel> result = new Result<MemberDistributionLevel>();
		try {
			memberDistributionLevelService.save(memberDistributionLevel);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param memberDistributionLevel
	 * @return
	 */
	@AutoLog(value = "会员和分销级别关系-编辑")
	@ApiOperation(value="会员和分销级别关系-编辑", notes="会员和分销级别关系-编辑")
	@PutMapping(value = "/edit")
	public Result<MemberDistributionLevel> edit(@RequestBody MemberDistributionLevel memberDistributionLevel) {
		Result<MemberDistributionLevel> result = new Result<MemberDistributionLevel>();
		MemberDistributionLevel memberDistributionLevelEntity = memberDistributionLevelService.getById(memberDistributionLevel.getId());
		if(memberDistributionLevelEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = memberDistributionLevelService.updateById(memberDistributionLevel);
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
	@AutoLog(value = "会员和分销级别关系-通过id删除")
	@ApiOperation(value="会员和分销级别关系-通过id删除", notes="会员和分销级别关系-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			memberDistributionLevelService.removeById(id);
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
	@AutoLog(value = "会员和分销级别关系-批量删除")
	@ApiOperation(value="会员和分销级别关系-批量删除", notes="会员和分销级别关系-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MemberDistributionLevel> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MemberDistributionLevel> result = new Result<MemberDistributionLevel>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.memberDistributionLevelService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "会员和分销级别关系-通过id查询")
	@ApiOperation(value="会员和分销级别关系-通过id查询", notes="会员和分销级别关系-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MemberDistributionLevel> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MemberDistributionLevel> result = new Result<MemberDistributionLevel>();
		MemberDistributionLevel memberDistributionLevel = memberDistributionLevelService.getById(id);
		if(memberDistributionLevel==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(memberDistributionLevel);
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
      QueryWrapper<MemberDistributionLevel> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MemberDistributionLevel memberDistributionLevel = JSON.parseObject(deString, MemberDistributionLevel.class);
              queryWrapper = QueryGenerator.initQueryWrapper(memberDistributionLevel, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MemberDistributionLevel> pageList = memberDistributionLevelService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "会员和分销级别关系列表");
      mv.addObject(NormalExcelConstants.CLASS, MemberDistributionLevel.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员和分销级别关系列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MemberDistributionLevel> listMemberDistributionLevels = ExcelImportUtil.importExcel(file.getInputStream(), MemberDistributionLevel.class, params);
              memberDistributionLevelService.saveBatch(listMemberDistributionLevels);
              return Result.ok("文件导入成功！数据行数:" + listMemberDistributionLevels.size());
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
