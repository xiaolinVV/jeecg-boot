package org.jeecg.modules.member.controller;

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
import org.jeecg.modules.member.dto.MemberAttentionStoreDto;
import org.jeecg.modules.member.entity.MemberAttentionStore;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberAttentionStoreService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.vo.MemberAttentionStoreVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 关注店铺
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="关注店铺")
@RestController
@RequestMapping("/memberAttentionStore/memberAttentionStore")
public class MemberAttentionStoreController {
	@Autowired
	private IMemberAttentionStoreService memberAttentionStoreService;
	@Autowired
	private IMemberListService memberListService;

	 /**
	  * 分页列表查询
	  * @param memberAttentionStoreVo
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "商品收藏-分页列表查询")
	 @ApiOperation(value="商品收藏-分页列表查询", notes="商品收藏-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<MemberAttentionStoreDto>> queryPageList(MemberAttentionStoreVo memberAttentionStoreVo,
																 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																 HttpServletRequest req) {
		 Result<IPage<MemberAttentionStoreDto>> result = new Result<IPage<MemberAttentionStoreDto>>();
		 try {
			 Page<MemberAttentionStoreDto> page = new Page<>(pageNo,pageSize);
			 IPage<MemberAttentionStoreDto> vo = memberAttentionStoreService.getMemberAttentionStoreVo(page, memberAttentionStoreVo);
			 result.setSuccess(true);
			 result.setResult(vo);
		 }catch (Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }

		 return result;
	 }
	
	/**
	  *   添加
	 * @param memberAttentionStore
	 * @return
	 */
	@AutoLog(value = "关注店铺-添加")
	@ApiOperation(value="关注店铺-添加", notes="关注店铺-添加")
	@PostMapping(value = "/add")
	public Result<MemberAttentionStore> add(@RequestBody MemberAttentionStore memberAttentionStore) {
		Result<MemberAttentionStore> result = new Result<MemberAttentionStore>();
		try {
			memberAttentionStoreService.save(memberAttentionStore);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param memberAttentionStore
	 * @return
	 */
	@AutoLog(value = "关注店铺-编辑")
	@ApiOperation(value="关注店铺-编辑", notes="关注店铺-编辑")
	@PutMapping(value = "/edit")
	public Result<MemberAttentionStore> edit(@RequestBody MemberAttentionStore memberAttentionStore) {
		Result<MemberAttentionStore> result = new Result<MemberAttentionStore>();
		MemberAttentionStore memberAttentionStoreEntity = memberAttentionStoreService.getById(memberAttentionStore.getId());
		if(memberAttentionStoreEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = memberAttentionStoreService.updateById(memberAttentionStore);
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
	@AutoLog(value = "关注店铺-通过id删除")
	@ApiOperation(value="关注店铺-通过id删除", notes="关注店铺-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			memberAttentionStoreService.removeById(id);
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
	@AutoLog(value = "关注店铺-批量删除")
	@ApiOperation(value="关注店铺-批量删除", notes="关注店铺-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MemberAttentionStore> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MemberAttentionStore> result = new Result<MemberAttentionStore>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.memberAttentionStoreService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "关注店铺-通过id查询")
	@ApiOperation(value="关注店铺-通过id查询", notes="关注店铺-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MemberAttentionStore> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MemberAttentionStore> result = new Result<MemberAttentionStore>();
		MemberAttentionStore memberAttentionStore = memberAttentionStoreService.getById(id);
		if(memberAttentionStore==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(memberAttentionStore);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request,MemberAttentionStore memberAttentionStore) {
      // Step.1 组装查询条件
	  QueryWrapper<MemberAttentionStore> queryWrapper = QueryGenerator.initQueryWrapper(memberAttentionStore, request.getParameterMap());
	  //step.2 Autopoi 导出Excel
	  ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	  //获取当前用户
	  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

	  ArrayList<MemberAttentionStoreVo> voList = new ArrayList<>();

	  List<MemberAttentionStore> memberAttentionStoreList = memberAttentionStoreService.list(queryWrapper);

	  for (MemberAttentionStore attentionStore : memberAttentionStoreList) {
		  MemberAttentionStoreVo vo = new MemberAttentionStoreVo();
		  BeanUtils.copyProperties(attentionStore,vo);
		  //查询会员列表
		  List<MemberList> memberListById = memberListService.selectMemberListById(attentionStore.getMemberListId());
		  vo.setMemberListList(memberListById);
		  voList.add(vo);
	  }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "关注店铺列表");
      mv.addObject(NormalExcelConstants.CLASS, MemberAttentionStoreVo.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("关注店铺列表数据", "导出人:Jeecg"+sysUser.getRealname(), "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, voList);
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
              List<MemberAttentionStore> listMemberAttentionStores = ExcelImportUtil.importExcel(file.getInputStream(), MemberAttentionStore.class, params);
              memberAttentionStoreService.saveBatch(listMemberAttentionStores);
              return Result.ok("文件导入成功！数据行数:" + listMemberAttentionStores.size());
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
