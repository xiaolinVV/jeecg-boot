package org.jeecg.modules.demo.tbLeaveApply.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.tbLeaveApply.entity.TbLeaveApply;
import org.jeecg.modules.demo.tbLeaveApply.service.ITbLeaveApplyService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2022-09-04
 * @Version: V1.0
 */
@Api(tags="请假申请")
@RestController
@RequestMapping("/tbLeaveApply/tbLeaveApply")
@Slf4j
public class TbLeaveApplyController extends JeecgController<TbLeaveApply, ITbLeaveApplyService> {
	@Autowired
	private ITbLeaveApplyService tbLeaveApplyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param tbLeaveApply
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "请假申请-分页列表查询")
	@ApiOperation(value="请假申请-分页列表查询", notes="请假申请-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TbLeaveApply>> queryPageList(TbLeaveApply tbLeaveApply,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TbLeaveApply> queryWrapper = QueryGenerator.initQueryWrapper(tbLeaveApply, req.getParameterMap());
		Page<TbLeaveApply> page = new Page<TbLeaveApply>(pageNo, pageSize);
		IPage<TbLeaveApply> pageList = tbLeaveApplyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

    /**
     *  关联流程
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/relationAct")
    public Result<?> relationAct(HttpServletRequest request, HttpServletResponse response) {
         String dataId = request.getParameter("dataId");
         tbLeaveApplyService.relationAct(dataId);
         return Result.OK();
    }
	
	/**
	 *   添加
	 *
	 * @param tbLeaveApply
	 * @return
	 */
	@AutoLog(value = "请假申请-添加")
	@ApiOperation(value="请假申请-添加", notes="请假申请-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:tb_leave_apply:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TbLeaveApply tbLeaveApply) {
        tbLeaveApply.setBpmStatus("0");
		tbLeaveApplyService.save(tbLeaveApply);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param tbLeaveApply
	 * @return
	 */
	@AutoLog(value = "请假申请-编辑")
	@ApiOperation(value="请假申请-编辑", notes="请假申请-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:tb_leave_apply:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TbLeaveApply tbLeaveApply) {
		tbLeaveApplyService.updateById(tbLeaveApply);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "请假申请-通过id删除")
	@ApiOperation(value="请假申请-通过id删除", notes="请假申请-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:tb_leave_apply:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		tbLeaveApplyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "请假申请-批量删除")
	@ApiOperation(value="请假申请-批量删除", notes="请假申请-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:tb_leave_apply:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tbLeaveApplyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "请假申请-通过id查询")
	@ApiOperation(value="请假申请-通过id查询", notes="请假申请-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TbLeaveApply> queryById(@RequestParam(name="id",required=true) String id) {
		TbLeaveApply tbLeaveApply = tbLeaveApplyService.getById(id);
		if(tbLeaveApply==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tbLeaveApply);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tbLeaveApply
    */
    //@RequiresPermissions("org.jeecg.modules.demo:tb_leave_apply:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TbLeaveApply tbLeaveApply) {
        return super.exportXls(request, tbLeaveApply, TbLeaveApply.class, "请假申请");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("tb_leave_apply:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TbLeaveApply.class);
    }

}
