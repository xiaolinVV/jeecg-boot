package org.jeecg.modules.demo.testOnlineComponent.controller;

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
import org.jeecg.modules.demo.testOnlineComponent.entity.TestOnlineComponent;
import org.jeecg.modules.demo.testOnlineComponent.service.ITestOnlineComponentService;

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
 * @Description: 测试 Online 表单控件
 * @Author: jeecg-boot
 * @Date:   2022-10-27
 * @Version: V1.0
 */
@Api(tags="测试 Online 表单控件")
@RestController
@RequestMapping("/testOnlineComponent/testOnlineComponent")
@Slf4j
public class TestOnlineComponentController extends JeecgController<TestOnlineComponent, ITestOnlineComponentService> {
	@Autowired
	private ITestOnlineComponentService testOnlineComponentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testOnlineComponent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "测试 Online 表单控件-分页列表查询")
	@ApiOperation(value="测试 Online 表单控件-分页列表查询", notes="测试 Online 表单控件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestOnlineComponent>> queryPageList(TestOnlineComponent testOnlineComponent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestOnlineComponent> queryWrapper = QueryGenerator.initQueryWrapper(testOnlineComponent, req.getParameterMap());
		Page<TestOnlineComponent> page = new Page<TestOnlineComponent>(pageNo, pageSize);
		IPage<TestOnlineComponent> pageList = testOnlineComponentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param testOnlineComponent
	 * @return
	 */
	@AutoLog(value = "测试 Online 表单控件-添加")
	@ApiOperation(value="测试 Online 表单控件-添加", notes="测试 Online 表单控件-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:test_online_component:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestOnlineComponent testOnlineComponent) {
		testOnlineComponentService.save(testOnlineComponent);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testOnlineComponent
	 * @return
	 */
	@AutoLog(value = "测试 Online 表单控件-编辑")
	@ApiOperation(value="测试 Online 表单控件-编辑", notes="测试 Online 表单控件-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:test_online_component:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestOnlineComponent testOnlineComponent) {
		testOnlineComponentService.updateById(testOnlineComponent);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试 Online 表单控件-通过id删除")
	@ApiOperation(value="测试 Online 表单控件-通过id删除", notes="测试 Online 表单控件-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_online_component:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testOnlineComponentService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试 Online 表单控件-批量删除")
	@ApiOperation(value="测试 Online 表单控件-批量删除", notes="测试 Online 表单控件-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_online_component:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testOnlineComponentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "测试 Online 表单控件-通过id查询")
	@ApiOperation(value="测试 Online 表单控件-通过id查询", notes="测试 Online 表单控件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestOnlineComponent> queryById(@RequestParam(name="id",required=true) String id) {
		TestOnlineComponent testOnlineComponent = testOnlineComponentService.getById(id);
		if(testOnlineComponent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testOnlineComponent);
	}

    /**
     * 查询第一条数据,用于配置场景，通常配置只会有一条数据的
     *
     * @return
     */
    //@AutoLog(value = "测试 Online 表单控件-查询第一条数据,用于配置场景")
    @ApiOperation(value="测试 Online 表单控件-查询第一条数据,用于配置场景", notes="测试 Online 表单控件-查询第一条数据,用于配置场景")
    @GetMapping(value = "/queryFirstData")
    public Result<TestOnlineComponent> queryFirstData() {
        TestOnlineComponent testOnlineComponent = testOnlineComponentService.getOne(new QueryWrapper<>(),false);
        if(testOnlineComponent==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(testOnlineComponent);
    }

    /**
    * 导出excel
    *
    * @param request
    * @param testOnlineComponent
    */
    //@RequiresPermissions("org.jeecg.modules.demo:test_online_component:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestOnlineComponent testOnlineComponent) {
        return super.exportXls(request, testOnlineComponent, TestOnlineComponent.class, "测试 Online 表单控件");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("test_online_component:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestOnlineComponent.class);
    }

}
