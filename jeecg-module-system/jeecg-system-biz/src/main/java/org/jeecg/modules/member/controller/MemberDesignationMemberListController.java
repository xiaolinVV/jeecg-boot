package org.jeecg.modules.member.controller;

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
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.member.entity.MemberDesignationMemberList;
import org.jeecg.modules.member.service.IMemberDesignationGroupService;
import org.jeecg.modules.member.service.IMemberDesignationMemberListService;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;
import org.jeecg.modules.member.vo.MemberDesignationMemberListVO;
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
 * @Description: 会员称号关系
 * @Author: jeecg-boot
 * @Date:   2021-03-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags="会员称号关系")
@RestController
@RequestMapping("/memberDesignationMemberList/memberDesignationMemberList")
public class MemberDesignationMemberListController {
	@Autowired
	private IMemberDesignationMemberListService memberDesignationMemberListService;
	@Autowired
	private IMemberDesignationGroupService iMemberDesignationGroupService;
	
	/**
	  * 分页列表查询
	 * @param memberDesignationMemberList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "会员称号关系-分页列表查询")
	@ApiOperation(value="会员称号关系-分页列表查询", notes="会员称号关系-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MemberDesignationMemberListVO>> queryPageList(MemberDesignationMemberListVO memberDesignationMemberListVO,
																	@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																	HttpServletRequest req) {
		Result<IPage<MemberDesignationMemberListVO>> result = new Result<IPage<MemberDesignationMemberListVO>>();
		QueryWrapper<MemberDesignationMemberListVO> queryWrapper = QueryGenerator.initQueryWrapper(memberDesignationMemberListVO, req.getParameterMap());
		Page<MemberDesignationMemberListVO> page = new Page<MemberDesignationMemberListVO>(pageNo, pageSize);
		IPage<MemberDesignationMemberListVO> pageList = memberDesignationMemberListService.queryPageList(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param memberDesignationMemberList
	 * @return
	 */
	@AutoLog(value = "会员称号关系-添加")
	@ApiOperation(value="会员称号关系-添加", notes="会员称号关系-添加")
	@PostMapping(value = "/add")
	public Result<MemberDesignationMemberList> add(@RequestBody MemberDesignationMemberList memberDesignationMemberList) {
		Result<MemberDesignationMemberList> result = new Result<MemberDesignationMemberList>();
		try {
			memberDesignationMemberListService.save(memberDesignationMemberList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param memberDesignationMemberList
	 * @return
	 */
	@AutoLog(value = "会员称号关系-编辑")
	@ApiOperation(value="会员称号关系-编辑", notes="会员称号关系-编辑")
	@PutMapping(value = "/edit")
	public Result<MemberDesignationMemberList> edit(@RequestBody MemberDesignationMemberList memberDesignationMemberList) {
		Result<MemberDesignationMemberList> result = new Result<MemberDesignationMemberList>();
		MemberDesignationMemberList memberDesignationMemberListEntity = memberDesignationMemberListService.getById(memberDesignationMemberList.getId());
		if(memberDesignationMemberListEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = memberDesignationMemberListService.updateById(memberDesignationMemberList);
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
	@AutoLog(value = "会员称号关系-通过id删除")
	@ApiOperation(value="会员称号关系-通过id删除", notes="会员称号关系-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			memberDesignationMemberListService.removeById(id);
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
	@AutoLog(value = "会员称号关系-批量删除")
	@ApiOperation(value="会员称号关系-批量删除", notes="会员称号关系-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MemberDesignationMemberList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MemberDesignationMemberList> result = new Result<MemberDesignationMemberList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.memberDesignationMemberListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "会员称号关系-通过id查询")
	@ApiOperation(value="会员称号关系-通过id查询", notes="会员称号关系-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MemberDesignationMemberList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MemberDesignationMemberList> result = new Result<MemberDesignationMemberList>();
		MemberDesignationMemberList memberDesignationMemberList = memberDesignationMemberListService.getById(id);
		if(memberDesignationMemberList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(memberDesignationMemberList);
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
      QueryWrapper<MemberDesignationMemberList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MemberDesignationMemberList memberDesignationMemberList = JSON.parseObject(deString, MemberDesignationMemberList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(memberDesignationMemberList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MemberDesignationMemberList> pageList = memberDesignationMemberListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "会员称号关系列表");
      mv.addObject(NormalExcelConstants.CLASS, MemberDesignationMemberList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员称号关系列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MemberDesignationMemberList> listMemberDesignationMemberLists = ExcelImportUtil.importExcel(file.getInputStream(), MemberDesignationMemberList.class, params);
              memberDesignationMemberListService.saveBatch(listMemberDesignationMemberLists);
              return Result.ok("文件导入成功！数据行数:" + listMemberDesignationMemberLists.size());
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

  @GetMapping("findMemberDesignationMemberlist")
  public Result<?> findMemberDesignationMemberlist(MemberDesignationMemberListVO memberDesignationMemberListVO){
  	return Result.ok(memberDesignationMemberListService.findMemberDesignationMemberlist(memberDesignationMemberListVO));
  }
}
