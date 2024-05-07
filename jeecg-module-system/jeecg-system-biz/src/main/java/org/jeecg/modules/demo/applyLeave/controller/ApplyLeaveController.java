package org.jeecg.modules.demo.applyLeave.controller;

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
import org.jeecg.modules.demo.applyLeave.entity.ApplyLeave;
import org.jeecg.modules.demo.applyLeave.service.IApplyLeaveService;

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
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2024-05-07
 * @Version: V1.0
 */
@Api(tags="请假申请")
@RestController
@RequestMapping("/applyLeave/applyLeave")
@Slf4j
public class ApplyLeaveController extends JeecgController<ApplyLeave, IApplyLeaveService> {
	@Autowired
	private IApplyLeaveService applyLeaveService;
	
	/**
	 * 分页列表查询
	 *
	 * @param applyLeave
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "请假申请-分页列表查询")
	@ApiOperation(value="请假申请-分页列表查询", notes="请假申请-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ApplyLeave>> queryPageList(ApplyLeave applyLeave,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ApplyLeave> queryWrapper = QueryGenerator.initQueryWrapper(applyLeave, req.getParameterMap());
		Page<ApplyLeave> page = new Page<ApplyLeave>(pageNo, pageSize);
		IPage<ApplyLeave> pageList = applyLeaveService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param applyLeave
	 * @return
	 */
	@AutoLog(value = "请假申请-添加")
	@ApiOperation(value="请假申请-添加", notes="请假申请-添加")
	@RequiresPermissions("applyLeave:apply_leave:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ApplyLeave applyLeave) {
        applyLeave.setBpmStatus("1");
		applyLeaveService.save(applyLeave);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param applyLeave
	 * @return
	 */
	@AutoLog(value = "请假申请-编辑")
	@ApiOperation(value="请假申请-编辑", notes="请假申请-编辑")
	@RequiresPermissions("applyLeave:apply_leave:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ApplyLeave applyLeave) {
		applyLeaveService.updateById(applyLeave);
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
	@RequiresPermissions("applyLeave:apply_leave:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		applyLeaveService.removeById(id);
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
	@RequiresPermissions("applyLeave:apply_leave:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.applyLeaveService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<ApplyLeave> queryById(@RequestParam(name="id",required=true) String id) {
		ApplyLeave applyLeave = applyLeaveService.getById(id);
		if(applyLeave==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(applyLeave);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param applyLeave
    */
    @RequiresPermissions("applyLeave:apply_leave:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ApplyLeave applyLeave) {
        return super.exportXls(request, applyLeave, ApplyLeave.class, "请假申请");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("applyLeave:apply_leave:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ApplyLeave.class);
    }

}
