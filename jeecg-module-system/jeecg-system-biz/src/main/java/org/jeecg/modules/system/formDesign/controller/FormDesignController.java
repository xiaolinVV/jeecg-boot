package org.jeecg.modules.system.formDesign.controller;

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
import org.jeecg.modules.system.formDesign.entity.FormDesign;
import org.jeecg.modules.system.formDesign.service.IFormDesignService;

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
 * @Description: 表单设计表
 * @Author: jeecg-boot
 * @Date:   2022-08-21
 * @Version: V1.0
 */
@Api(tags="表单设计表")
@RestController
@RequestMapping("/formDesign/formDesign")
@Slf4j
public class FormDesignController extends JeecgController<FormDesign, IFormDesignService> {
	@Autowired
	private IFormDesignService formDesignService;
	
	/**
	 * 分页列表查询
	 *
	 * @param formDesign
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "表单设计表-分页列表查询")
	@ApiOperation(value="表单设计表-分页列表查询", notes="表单设计表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FormDesign>> queryPageList(FormDesign formDesign,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FormDesign> queryWrapper = QueryGenerator.initQueryWrapper(formDesign, req.getParameterMap());
		Page<FormDesign> page = new Page<FormDesign>(pageNo, pageSize);
		IPage<FormDesign> pageList = formDesignService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param formDesign
	 * @return
	 */
	@AutoLog(value = "表单设计表-添加")
	@ApiOperation(value="表单设计表-添加", notes="表单设计表-添加")
	//@RequiresPermissions("org.jeecg.modules.system:form_design:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FormDesign formDesign) {
		formDesignService.save(formDesign);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param formDesign
	 * @return
	 */
	@AutoLog(value = "表单设计表-编辑")
	@ApiOperation(value="表单设计表-编辑", notes="表单设计表-编辑")
	//@RequiresPermissions("org.jeecg.modules.system:form_design:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FormDesign formDesign) {
		formDesignService.updateById(formDesign);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "表单设计表-通过id删除")
	@ApiOperation(value="表单设计表-通过id删除", notes="表单设计表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.system:form_design:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		formDesignService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "表单设计表-批量删除")
	@ApiOperation(value="表单设计表-批量删除", notes="表单设计表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.system:form_design:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.formDesignService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "表单设计表-通过id查询")
	@ApiOperation(value="表单设计表-通过id查询", notes="表单设计表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FormDesign> queryById(@RequestParam(name="id",required=true) String id) {
		FormDesign formDesign = formDesignService.getById(id);
		if(formDesign==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(formDesign);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param formDesign
    */
    //@RequiresPermissions("org.jeecg.modules.system:form_design:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FormDesign formDesign) {
        return super.exportXls(request, formDesign, FormDesign.class, "表单设计表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("form_design:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FormDesign.class);
    }

}
