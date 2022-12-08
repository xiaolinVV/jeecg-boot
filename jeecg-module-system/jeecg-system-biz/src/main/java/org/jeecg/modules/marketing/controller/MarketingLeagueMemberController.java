package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingLeagueMemberDTO;
import org.jeecg.modules.marketing.entity.MarketingLeagueIdentity;
import org.jeecg.modules.marketing.entity.MarketingLeagueMember;
import org.jeecg.modules.marketing.service.IMarketingLeagueIdentityService;
import org.jeecg.modules.marketing.service.IMarketingLeagueMemberService;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 加盟专区-加盟用户
 * @Author: jeecg-boot
 * @Date:   2021-12-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="加盟专区-加盟用户")
@RestController
@RequestMapping("/marketing/marketingLeagueMember")
public class MarketingLeagueMemberController {
	@Autowired
	private IMarketingLeagueMemberService marketingLeagueMemberService;

	@Autowired
	private IMarketingLeagueIdentityService iMarketingLeagueIdentityService;


	 /**
	  * 给会员添加超级合伙人身份
	  * @param mid
	  * @param rewardRatio
	  * @return
	  */
	@GetMapping("settingSupper")
	public Result<?> settingSupper(String mid, BigDecimal rewardRatio,String type){

		if(StringUtils.isBlank(mid)){
			return Result.error("会员编号不存在");
		}

		//获取称号
		MarketingLeagueIdentity marketingLeagueIdentity=iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>()
				.eq(MarketingLeagueIdentity::getGetWay,"4")
				.last("limit 1"));
		if(marketingLeagueIdentity!=null){
			if(type.equals("0")) {
				//查询身份是否存在
				long count = marketingLeagueMemberService.count(new LambdaQueryWrapper<MarketingLeagueMember>()
						.eq(MarketingLeagueMember::getMarketingLeagueIdentityId, marketingLeagueIdentity.getId())
						.eq(MarketingLeagueMember::getMemberListId, mid));
				if (count > 0) {
					return Result.error("会员身份已存在");
				}
				MarketingLeagueMember marketingLeagueMember = new MarketingLeagueMember();
				marketingLeagueMember.setMemberListId(mid);
				marketingLeagueMember.setMarketingLeagueIdentityId(marketingLeagueIdentity.getId());
				marketingLeagueMember.setRewardRatio(rewardRatio);
				marketingLeagueMember.setAdditionalIdentity(marketingLeagueIdentity.getAdditionalIdentity());
				marketingLeagueMemberService.save(marketingLeagueMember);
				return Result.ok("添加成功");
			}else{
				MarketingLeagueMember marketingLeagueMember=marketingLeagueMemberService.getOne(new LambdaQueryWrapper<MarketingLeagueMember>()
						.eq(MarketingLeagueMember::getMarketingLeagueIdentityId, marketingLeagueIdentity.getId())
						.eq(MarketingLeagueMember::getMemberListId, mid)
						.orderByDesc(MarketingLeagueMember::getCreateTime)
						.last("limit 1"));
				marketingLeagueMember.setRewardRatio(rewardRatio);
				marketingLeagueMemberService.saveOrUpdate(marketingLeagueMember);
				return Result.ok("修改成功");
			}
		}else{
			return Result.error("身份不存在");
		}
	}


	 /**
	  * 分页列表查询
	  * @param marketingLeagueMemberDTO
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @AutoLog(value = "加盟专区-加盟用户-分页列表查询")
	 @GetMapping(value = "/listByGetWay")
	 public Result<?> queryPageListByGetWay(MarketingLeagueMemberDTO marketingLeagueMemberDTO,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 return Result.ok(marketingLeagueMemberService.queryPageListByGetWay(new Page<>(pageNo,pageSize),marketingLeagueMemberDTO));
	 }
	
	/**
	  * 分页列表查询
	 * @param marketingLeagueMemberDTO
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "加盟专区-加盟用户-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingLeagueMemberDTO marketingLeagueMemberDTO,
										  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(marketingLeagueMemberService.queryPageList(new Page<>(pageNo,pageSize),marketingLeagueMemberDTO));
	}
	
	/**
	  *   添加
	 * @param marketingLeagueMember
	 * @return
	 */
	@AutoLog(value = "加盟专区-加盟用户-添加")
	@ApiOperation(value="加盟专区-加盟用户-添加", notes="加盟专区-加盟用户-添加")
	@PostMapping(value = "/add")
	public Result<MarketingLeagueMember> add(@RequestBody MarketingLeagueMember marketingLeagueMember) {
		Result<MarketingLeagueMember> result = new Result<MarketingLeagueMember>();
		try {
			marketingLeagueMemberService.save(marketingLeagueMember);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingLeagueMember
	 * @return
	 */
	@AutoLog(value = "加盟专区-加盟用户-编辑")
	@ApiOperation(value="加盟专区-加盟用户-编辑", notes="加盟专区-加盟用户-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingLeagueMember> edit(@RequestBody MarketingLeagueMember marketingLeagueMember) {
		Result<MarketingLeagueMember> result = new Result<MarketingLeagueMember>();
		MarketingLeagueMember marketingLeagueMemberEntity = marketingLeagueMemberService.getById(marketingLeagueMember.getId());
		if(marketingLeagueMemberEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingLeagueMemberService.updateById(marketingLeagueMember);
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
	@AutoLog(value = "加盟专区-加盟用户-通过id删除")
	@ApiOperation(value="加盟专区-加盟用户-通过id删除", notes="加盟专区-加盟用户-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingLeagueMemberService.removeById(id);
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
	@AutoLog(value = "加盟专区-加盟用户-批量删除")
	@ApiOperation(value="加盟专区-加盟用户-批量删除", notes="加盟专区-加盟用户-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingLeagueMember> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingLeagueMember> result = new Result<MarketingLeagueMember>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingLeagueMemberService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "加盟专区-加盟用户-通过id查询")
	@ApiOperation(value="加盟专区-加盟用户-通过id查询", notes="加盟专区-加盟用户-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingLeagueMember> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingLeagueMember> result = new Result<MarketingLeagueMember>();
		MarketingLeagueMember marketingLeagueMember = marketingLeagueMemberService.getById(id);
		if(marketingLeagueMember==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingLeagueMember);
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
      QueryWrapper<MarketingLeagueMember> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingLeagueMember marketingLeagueMember = JSON.parseObject(deString, MarketingLeagueMember.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingLeagueMember, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingLeagueMember> pageList = marketingLeagueMemberService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "加盟专区-加盟用户列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingLeagueMember.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("加盟专区-加盟用户列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingLeagueMember> listMarketingLeagueMembers = ExcelImportUtil.importExcel(file.getInputStream(), MarketingLeagueMember.class, params);
              marketingLeagueMemberService.saveBatch(listMarketingLeagueMembers);
              return Result.ok("文件导入成功！数据行数:" + listMarketingLeagueMembers.size());
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
