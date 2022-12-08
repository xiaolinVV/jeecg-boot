package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.dto.MemberGrowthRecordDTO;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.entity.MemberGrowthRecord;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberGradeService;
import org.jeecg.modules.member.service.IMemberGrowthRecordService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.vo.MemberGrowthRecordVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
 * @Description: 成长值记录
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="成长值记录")
@RestController
@RequestMapping("/memberGrowthRecord/memberGrowthRecord")
public class MemberGrowthRecordController {
	@Autowired
	private IMemberGrowthRecordService memberGrowthRecordService;
	@Autowired
	private IMemberListService iMemberListService;
	@Autowired
	@Lazy
	private IMemberGradeService iMemberGradeService;
	/**
	  * 分页列表查询
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "成长值记录-分页列表查询")
	@ApiOperation(value="成长值记录-分页列表查询", notes="成长值记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MemberGrowthRecordVO>> queryPageList(MemberGrowthRecordDTO memberGrowthRecordDTO,
															 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															 HttpServletRequest req) {
		Result<IPage<MemberGrowthRecordVO>> result = new Result<IPage<MemberGrowthRecordVO>>();
		Page<MemberGrowthRecord> page = new Page<MemberGrowthRecord>(pageNo, pageSize);
		IPage<MemberGrowthRecordVO> pageList = memberGrowthRecordService.queryPageList(page, memberGrowthRecordDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param memberGrowthRecord
	 * @return
	 */
	@AutoLog(value = "成长值记录-添加")
	@ApiOperation(value="成长值记录-添加", notes="成长值记录-添加")
	@PostMapping(value = "/add")
	public Result<MemberGrowthRecord> add(@RequestBody MemberGrowthRecord memberGrowthRecord) {
		Result<MemberGrowthRecord> result = new Result<MemberGrowthRecord>();
		try {
			memberGrowthRecordService.save(memberGrowthRecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param memberGrowthRecord
	 * @return
	 */
	@AutoLog(value = "成长值记录-编辑")
	@ApiOperation(value="成长值记录-编辑", notes="成长值记录-编辑")
	@PutMapping(value = "/edit")
	public Result<MemberGrowthRecord> edit(@RequestBody MemberGrowthRecord memberGrowthRecord) {
		Result<MemberGrowthRecord> result = new Result<MemberGrowthRecord>();
		MemberGrowthRecord memberGrowthRecordEntity = memberGrowthRecordService.getById(memberGrowthRecord.getId());
		if(memberGrowthRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = memberGrowthRecordService.updateById(memberGrowthRecord);
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
	@AutoLog(value = "成长值记录-通过id删除")
	@ApiOperation(value="成长值记录-通过id删除", notes="成长值记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			memberGrowthRecordService.removeById(id);
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
	@AutoLog(value = "成长值记录-批量删除")
	@ApiOperation(value="成长值记录-批量删除", notes="成长值记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MemberGrowthRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MemberGrowthRecord> result = new Result<MemberGrowthRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.memberGrowthRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "成长值记录-通过id查询")
	@ApiOperation(value="成长值记录-通过id查询", notes="成长值记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MemberGrowthRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MemberGrowthRecord> result = new Result<MemberGrowthRecord>();
		MemberGrowthRecord memberGrowthRecord = memberGrowthRecordService.getById(id);
		if(memberGrowthRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(memberGrowthRecord);
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
      QueryWrapper<MemberGrowthRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MemberGrowthRecord memberGrowthRecord = JSON.parseObject(deString, MemberGrowthRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(memberGrowthRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MemberGrowthRecord> pageList = memberGrowthRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "成长值记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MemberGrowthRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("成长值记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MemberGrowthRecord> listMemberGrowthRecords = ExcelImportUtil.importExcel(file.getInputStream(), MemberGrowthRecord.class, params);
              memberGrowthRecordService.saveBatch(listMemberGrowthRecords);
              return Result.ok("文件导入成功！数据行数:" + listMemberGrowthRecords.size());
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

  @PostMapping("supplyAgain")
  public Result<MemberGrowthRecord> supplyAgain(@RequestBody MemberGrowthRecordDTO memberGrowthRecordDTO){
	  Result<MemberGrowthRecord> result = new Result<>();
	  if (StringUtils.isBlank(memberGrowthRecordDTO.getMemberListId())){
	  	return result.error500("会员编号不能为空");
	  }
	  MemberList memberList = iMemberListService.getById(memberGrowthRecordDTO.getMemberListId());
	  if (oConvertUtils.isEmpty(memberList)){
	  	return result.error500("会员信息异常");
	  }
	  iMemberListService.saveOrUpdate(memberList
			  .setGrowthValue(memberList.getGrowthValue()
					  .add(memberGrowthRecordDTO.getGrowthValue())));
	  LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
			  .eq(MemberGrade::getDelFlag,"0")
			  .eq(MemberGrade::getStatus,"1")
			  .le(MemberGrade::getGrowthValueSmall, memberList.getGrowthValue())
			  .ge(MemberGrade::getGrowthValueBig, memberList.getGrowthValue())
			  .orderByAsc(MemberGrade::getSort);
	  if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0){
		  MemberGrade memberGrade = iMemberGradeService.list(memberGradeLambdaQueryWrapper).get(0);
		  if (StringUtils.isNotBlank(memberList.getMemberGradeId())){
			  if (memberList.getGrowthValue().doubleValue()>=memberGrade.getGrowthValueSmall().doubleValue()&&iMemberGradeService.getById(memberList.getMemberGradeId()).getSort().doubleValue()<memberGrade.getSort().doubleValue()){
			  	if (memberList.getMemberType().equals("0")){
			  		memberList.setMemberType("1");
			  		memberList.setVipTime(new Date());
				}
				  iMemberListService.saveOrUpdate(memberList
						  .setMemberGradeId(memberGrade.getId())
				  );
			  }
		  }else {
		  	if (memberList.getGrowthValue().doubleValue()>=memberGrade.getGrowthValueSmall().doubleValue()){
		  		iMemberListService.saveOrUpdate(memberList.setMemberGradeId(memberGrade.getId()));
			}
		  }

	  }else {
		  MemberGrade grade = iMemberGradeService.list(new LambdaQueryWrapper<MemberGrade>()
				  .eq(MemberGrade::getDelFlag, "0")
				  .eq(MemberGrade::getStatus, "1")
				  .orderByDesc(MemberGrade::getSort)).get(0);
		  if (grade.getGrowthValueBig().doubleValue()<=memberList.getGrowthValue().doubleValue()){
			  if (memberList.getMemberType().equals("0")){
				  memberList.setMemberType("1");
				  memberList.setVipTime(new Date());
			  }
			  memberList.setMemberGradeId(grade.getId());
			  iMemberListService.saveOrUpdate(memberList);
		  }
	  }
	  String orderNo = OrderNoUtils.getOrderNo();
	  memberGrowthRecordService.save(new MemberGrowthRecord()
			  .setDelFlag("0")
			  .setMemberListId(memberGrowthRecordDTO.getMemberListId())
			  .setTradeNo(orderNo)
			  .setTradeType("4")
			  .setRemark(memberGrowthRecordDTO.getRemark())
			  .setGrowthValue(memberGrowthRecordDTO.getGrowthValue())
			  .setOrderNo(orderNo)
	  );
	  return result.success("操作成功");
  }
}
