package org.jeecg.modules.member.controller;

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
import org.jeecg.modules.member.dto.MemberShippingAddressDto;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.member.vo.MemberShippingAddressVo;
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
 * @Description: 收货地址
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="收货地址")
@RestController
@RequestMapping("/memberShippingAddress/memberShippingAddress")
public class MemberShippingAddressController {
	@Autowired
	private IMemberShippingAddressService memberShippingAddressService;

	 /**
	  * 分页列表查询
	  * @param memberShippingAddressVo
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "商品收藏-分页列表查询")
	 @ApiOperation(value="商品收藏-分页列表查询", notes="商品收藏-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<MemberShippingAddressDto>> queryPageList(MemberShippingAddressVo memberShippingAddressVo,
																  @RequestParam(name="pageNo", defaultValue="0") Integer pageNo,
																  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																  HttpServletRequest req) {
		 Result<IPage<MemberShippingAddressDto>> result = new Result<IPage<MemberShippingAddressDto>>();
		 try {
			 Page<MemberShippingAddressDto> page = new Page<MemberShippingAddressDto>(pageNo, pageSize);
			 IPage<MemberShippingAddressDto> pageList = memberShippingAddressService.getMemberShippingAddressVo(page,memberShippingAddressVo);
			 result.setSuccess(true);
			 result.setResult(pageList);
		 }catch (Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }

		 return result;
	 }
	
	/**
	  *   添加
	 * @param memberShippingAddress
	 * @return
	 */
	@AutoLog(value = "收货地址-添加")
	@ApiOperation(value="收货地址-添加", notes="收货地址-添加")
	@PostMapping(value = "/add")
	public Result<MemberShippingAddress> add(@RequestBody MemberShippingAddress memberShippingAddress) {
		Result<MemberShippingAddress> result = new Result<MemberShippingAddress>();
		try {
			memberShippingAddressService.save(memberShippingAddress);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param memberShippingAddress
	 * @return
	 */
	@AutoLog(value = "收货地址-编辑")
	@ApiOperation(value="收货地址-编辑", notes="收货地址-编辑")
	@PutMapping(value = "/edit")
	public Result<MemberShippingAddress> edit(@RequestBody MemberShippingAddress memberShippingAddress) {
		Result<MemberShippingAddress> result = new Result<MemberShippingAddress>();
		MemberShippingAddress memberShippingAddressEntity = memberShippingAddressService.getById(memberShippingAddress.getId());
		if(memberShippingAddressEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = memberShippingAddressService.updateById(memberShippingAddress);
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
	@AutoLog(value = "收货地址-通过id删除")
	@ApiOperation(value="收货地址-通过id删除", notes="收货地址-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			memberShippingAddressService.removeById(id);
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
	@AutoLog(value = "收货地址-批量删除")
	@ApiOperation(value="收货地址-批量删除", notes="收货地址-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MemberShippingAddress> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MemberShippingAddress> result = new Result<MemberShippingAddress>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.memberShippingAddressService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收货地址-通过id查询")
	@ApiOperation(value="收货地址-通过id查询", notes="收货地址-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MemberShippingAddress> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MemberShippingAddress> result = new Result<MemberShippingAddress>();
		MemberShippingAddress memberShippingAddress = memberShippingAddressService.getById(id);
		if(memberShippingAddress==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(memberShippingAddress);
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
      QueryWrapper<MemberShippingAddress> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MemberShippingAddress memberShippingAddress = JSON.parseObject(deString, MemberShippingAddress.class);
              queryWrapper = QueryGenerator.initQueryWrapper(memberShippingAddress, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MemberShippingAddress> pageList = memberShippingAddressService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "收货地址列表");
      mv.addObject(NormalExcelConstants.CLASS, MemberShippingAddress.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("收货地址列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MemberShippingAddress> listMemberShippingAddresss = ExcelImportUtil.importExcel(file.getInputStream(), MemberShippingAddress.class, params);
              memberShippingAddressService.saveBatch(listMemberShippingAddresss);
              return Result.ok("文件导入成功！数据行数:" + listMemberShippingAddresss.size());
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
	 @AutoLog(value = "商品收藏-分页列表查询")
	 @ApiOperation(value="商品收藏-分页列表查询", notes="商品收藏-分页列表查询")
	 @GetMapping(value = "/memberListIdList")
	 public Result<List<MemberShippingAddress>> memberListIdList(@RequestParam(name="memberListId",required=true) String memberListId,
																  HttpServletRequest req) {
		 Result<List<MemberShippingAddress>> result = new Result<List<MemberShippingAddress>>();
		 try {
			 QueryWrapper<MemberShippingAddress> queryWrapper = new QueryWrapper<MemberShippingAddress>();
			 queryWrapper.eq("member_list_id",memberListId);
			 List<MemberShippingAddress> listMemberShippingAddress = memberShippingAddressService.list(queryWrapper);
			 result.setSuccess(true);
			 result.setResult(listMemberShippingAddress);
		 }catch (Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }

		 return result;
	 }
	 @GetMapping("findMemberShippingAddressByMemberId")
	 public Result<List<Map<String,Object>>> findMemberShippingAddressByMemberId(@RequestParam("memberListId")String memberListId){
		 Result<List<Map<String, Object>>> result = new Result<>();
		 result.setResult(memberShippingAddressService.findMemberShippingAddressByMemberId(memberListId));
		 return result;
	 }
}
