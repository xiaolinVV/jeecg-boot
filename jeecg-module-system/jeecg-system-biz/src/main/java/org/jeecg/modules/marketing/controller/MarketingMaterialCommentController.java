package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingMaterialCommentDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialComment;
import org.jeecg.modules.marketing.service.IMarketingMaterialCommentService;
import org.jeecg.modules.marketing.vo.MarketingMaterialCommentVO;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 素材评论表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="素材评论表")
@RestController
@RequestMapping("/marketingMaterialComment/marketingMaterialComment")
public class MarketingMaterialCommentController {
	@Autowired
	private IMarketingMaterialCommentService marketingMaterialCommentService;
	
	/**
	  * 分页列表查询
	 * @param marketingMaterialCommentVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "素材评论表-分页列表查询")
	@ApiOperation(value="素材评论表-分页列表查询", notes="素材评论表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingMaterialCommentDTO>> queryPageList(MarketingMaterialCommentVO marketingMaterialCommentVO,
																 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																 HttpServletRequest req) {
		Result<IPage<MarketingMaterialCommentDTO>> result = new Result<IPage<MarketingMaterialCommentDTO>>();
		Page<MarketingMaterialComment> page = new Page<MarketingMaterialComment>(pageNo, pageSize);
		IPage<MarketingMaterialCommentDTO> pageList = marketingMaterialCommentService.getMarketingMaterialCommentDTO(page, marketingMaterialCommentVO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingMaterialComment
	 * @return
	 */
	@AutoLog(value = "素材评论表-添加")
	@ApiOperation(value="素材评论表-添加", notes="素材评论表-添加")
	@PostMapping(value = "/add")
	public Result<MarketingMaterialComment> add(@RequestBody MarketingMaterialComment marketingMaterialComment) {
		Result<MarketingMaterialComment> result = new Result<MarketingMaterialComment>();
		try {
			marketingMaterialCommentService.save(marketingMaterialComment);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingMaterialComment
	 * @return
	 */
	@AutoLog(value = "素材评论表-编辑")
	@ApiOperation(value="素材评论表-编辑", notes="素材评论表-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingMaterialComment> edit(@RequestBody MarketingMaterialComment marketingMaterialComment) {
		Result<MarketingMaterialComment> result = new Result<MarketingMaterialComment>();
		MarketingMaterialComment marketingMaterialCommentEntity = marketingMaterialCommentService.getById(marketingMaterialComment.getId());
		if(marketingMaterialCommentEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingMaterialCommentService.updateById(marketingMaterialComment);
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
	@AutoLog(value = "素材评论表-通过id删除")
	@ApiOperation(value="素材评论表-通过id删除", notes="素材评论表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		Result<MarketingMaterialComment> result = new Result<>();
		try {
			MarketingMaterialComment marketingMaterialComment = marketingMaterialCommentService.getById(id);
			if(marketingMaterialComment == null){
				return result.error500("未找到评论对象!");
			}
			//查询子集评论
		  List<MarketingMaterialComment> marketingMaterialCommentList =	marketingMaterialCommentService.list(new LambdaQueryWrapper<MarketingMaterialComment>().eq(MarketingMaterialComment::getParentId,marketingMaterialComment.getId()));
			marketingMaterialCommentList.forEach(mmc->{
				//删除子集评论
				marketingMaterialCommentService.removeById(mmc.getId());
			});

			marketingMaterialCommentService.removeById(id);
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
	@AutoLog(value = "素材评论表-批量删除")
	@ApiOperation(value="素材评论表-批量删除", notes="素材评论表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingMaterialComment> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingMaterialComment> result = new Result<MarketingMaterialComment>();
		MarketingMaterialComment marketingMaterialComment;
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			List<String> listid = Arrays.asList(ids.split(","));
			for (String id : listid) {
				marketingMaterialComment = marketingMaterialCommentService.getById(id);
				if (marketingMaterialComment == null) {
					result.error500("未找到对应实体");
				} else {
					//查询子集评论
					List<MarketingMaterialComment> marketingMaterialCommentList = marketingMaterialCommentService.list(new LambdaQueryWrapper<MarketingMaterialComment>().eq(MarketingMaterialComment::getParentId, marketingMaterialComment.getId()));
					marketingMaterialCommentList.forEach(mmc -> {
						//删除子集评论
						marketingMaterialCommentService.removeById(mmc.getId());
					});

					marketingMaterialCommentService.removeById(id);

				}
			}
		}
			result.success("删除成功!");
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "素材评论表-通过id查询")
	@ApiOperation(value="素材评论表-通过id查询", notes="素材评论表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingMaterialComment> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingMaterialComment> result = new Result<MarketingMaterialComment>();
		MarketingMaterialComment marketingMaterialComment = marketingMaterialCommentService.getById(id);
		if(marketingMaterialComment==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingMaterialComment);
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
      QueryWrapper<MarketingMaterialComment> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingMaterialComment marketingMaterialComment = JSON.parseObject(deString, MarketingMaterialComment.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingMaterialComment, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingMaterialComment> pageList = marketingMaterialCommentService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "素材评论表列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingMaterialComment.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("素材评论表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingMaterialComment> listMarketingMaterialComments = ExcelImportUtil.importExcel(file.getInputStream(), MarketingMaterialComment.class, params);
              marketingMaterialCommentService.saveBatch(listMarketingMaterialComments);
              return Result.ok("文件导入成功！数据行数:" + listMarketingMaterialComments.size());
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
	  * 批量修改:审核状态
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "素材评论表-修改审核状态")
	 @ApiOperation(value = "素材评论表-修改审核状态", notes = "素材评论表-修改审核状态")
	 @GetMapping(value = "/updateStatus")
	 public Result<MarketingMaterialComment> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status, @RequestParam(name = "closeExplain") String closeExplain) {
		 Result<MarketingMaterialComment> result = new Result<MarketingMaterialComment>();
		 if (ids == null || "".equals(ids.trim())) {
			 result.error500("参数不识别！");
		 } else {
			 MarketingMaterialComment marketingMaterialComment;
			 try {
				 List<String> listid = Arrays.asList(ids.split(","));
				 for (String id : listid) {
					 marketingMaterialComment = marketingMaterialCommentService.getById(id);
					 if (marketingMaterialComment == null) {
						 result.error500("未找到对应实体");
					 } else {
						 marketingMaterialComment.setCloseExplain(closeExplain);
						 marketingMaterialComment.setStatus(status);
						 marketingMaterialComment.setAuditTime(new Date());
						 if (status.equals("2")) {
							 marketingMaterialComment.setCloseTime(new Date());
						 }
						 marketingMaterialCommentService.updateById(marketingMaterialComment);
					 }
				 }
				 result.success("修改成功!");
			 } catch (Exception e) {
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }

	 /**
	  * 回复评论
	  * @param id 评论id
	  * @param content 评论内容
	  * @param response
	  * @return
	  */
	 @AutoLog(value = "素材评论表-回复评论")
	 @ApiOperation(value = "素材评论表-回复评论", notes = "素材评论表-回复评论")
	 @GetMapping(value = "/addReplyComment")
	 public Result<MarketingMaterialComment>  addReplyComment(String id,String content,HttpServletResponse response){
		 Result<MarketingMaterialComment> result = new Result<MarketingMaterialComment>();
		 if(StringUtils.isBlank(id)){
		  return result.error500("评论id不能为空");
		 }
		 if(StringUtils.isBlank(content)){
			 return result.error500("评论内容不能为空");
		 }
		 MarketingMaterialComment marketingMaterialComment = marketingMaterialCommentService.getById(id);
		 if(marketingMaterialComment == null){
			 return result.error500("未找到评论信息");
		 }
		 //当前登录人
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 MarketingMaterialComment marketingMaterialCommentNew = new MarketingMaterialComment();

		 marketingMaterialCommentNew.setMarketingMaterialList(marketingMaterialComment.getMarketingMaterialList());
		 if(marketingMaterialComment.getParentId().equals("0")){
		 	//对评论评论
			 marketingMaterialCommentNew.setParentId(marketingMaterialComment.getId());
		 }else{
		 	//对评论回复
			 marketingMaterialCommentNew.setParentId(marketingMaterialComment.getParentId());
			 marketingMaterialCommentNew.setByReplyId(marketingMaterialComment.getMemberListId());
		 }
		 marketingMaterialCommentNew.setContent(content);
		 marketingMaterialCommentNew.setCommentTime(new Date());
		 marketingMaterialCommentNew.setStatus("1") ;
		 marketingMaterialCommentNew.setSysUserId(sysUser.getId());//用户id
		 marketingMaterialCommentNew.setUserType("1");//评论角色：0：会员；1：管理员
		 marketingMaterialCommentService.save(marketingMaterialCommentNew);
		 result.success("回复成功!");
       return result;
	 }

 }
