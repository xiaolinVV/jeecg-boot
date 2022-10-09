package org.jeecg.modules.demo.cmsCsdnBlog.controller;

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
import org.jeecg.modules.demo.cmsCsdnBlog.entity.CmsCsdnBlog;
import org.jeecg.modules.demo.cmsCsdnBlog.service.ICmsCsdnBlogService;

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
 * @Description: csdn博客表
 * @Author: jeecg-boot
 * @Date:   2022-10-09
 * @Version: V1.0
 */
@Api(tags="csdn博客表")
@RestController
@RequestMapping("/cmsCsdnBlog/cmsCsdnBlog")
@Slf4j
public class CmsCsdnBlogController extends JeecgController<CmsCsdnBlog, ICmsCsdnBlogService> {
	@Autowired
	private ICmsCsdnBlogService cmsCsdnBlogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cmsCsdnBlog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "csdn博客表-分页列表查询")
	@ApiOperation(value="csdn博客表-分页列表查询", notes="csdn博客表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CmsCsdnBlog>> queryPageList(CmsCsdnBlog cmsCsdnBlog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CmsCsdnBlog> queryWrapper = QueryGenerator.initQueryWrapper(cmsCsdnBlog, req.getParameterMap());
		Page<CmsCsdnBlog> page = new Page<CmsCsdnBlog>(pageNo, pageSize);
		IPage<CmsCsdnBlog> pageList = cmsCsdnBlogService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param cmsCsdnBlog
	 * @return
	 */
	@AutoLog(value = "csdn博客表-添加")
	@ApiOperation(value="csdn博客表-添加", notes="csdn博客表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:cms_csdn_blog:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CmsCsdnBlog cmsCsdnBlog) {
		cmsCsdnBlogService.save(cmsCsdnBlog);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cmsCsdnBlog
	 * @return
	 */
	@AutoLog(value = "csdn博客表-编辑")
	@ApiOperation(value="csdn博客表-编辑", notes="csdn博客表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:cms_csdn_blog:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CmsCsdnBlog cmsCsdnBlog) {
		cmsCsdnBlogService.updateById(cmsCsdnBlog);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "csdn博客表-通过id删除")
	@ApiOperation(value="csdn博客表-通过id删除", notes="csdn博客表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cms_csdn_blog:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cmsCsdnBlogService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "csdn博客表-批量删除")
	@ApiOperation(value="csdn博客表-批量删除", notes="csdn博客表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cms_csdn_blog:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cmsCsdnBlogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "csdn博客表-通过id查询")
	@ApiOperation(value="csdn博客表-通过id查询", notes="csdn博客表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CmsCsdnBlog> queryById(@RequestParam(name="id",required=true) String id) {
		CmsCsdnBlog cmsCsdnBlog = cmsCsdnBlogService.getById(id);
		if(cmsCsdnBlog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cmsCsdnBlog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cmsCsdnBlog
    */
    //@RequiresPermissions("org.jeecg.modules.demo:cms_csdn_blog:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CmsCsdnBlog cmsCsdnBlog) {
        return super.exportXls(request, cmsCsdnBlog, CmsCsdnBlog.class, "csdn博客表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("cms_csdn_blog:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CmsCsdnBlog.class);
    }

}
