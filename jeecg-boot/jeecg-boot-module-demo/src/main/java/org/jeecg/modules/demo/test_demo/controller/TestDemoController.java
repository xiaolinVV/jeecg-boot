package org.jeecg.modules.demo.test_demo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.test_demo.entity.TestDemo;
import org.jeecg.modules.demo.test_demo.service.ITestDemoService;
import org.jeecg.modules.flowable.apithird.business.entity.FlowMyBusiness;
import org.jeecg.modules.flowable.apithird.business.service.IFlowMyBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 测试用户表
 * @Author: jeecg-boot
 * @Date:   2021-11-30
 * @Version: V1.0
 */
@Api(tags="测试用户表")
@RestController
@RequestMapping("/test_demo/testDemo")
@Slf4j
public class TestDemoController extends JeecgController<TestDemo, ITestDemoService> {
	@Autowired
	private ITestDemoService testDemoService;

	/**
	 * 分页列表查询
	 *
	 * @param testDemo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试用户表-分页列表查询")
	@ApiOperation(value="测试用户表-分页列表查询", notes="测试用户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TestDemo testDemo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestDemo> queryWrapper = QueryGenerator.initQueryWrapper(testDemo, req.getParameterMap());
		Page<TestDemo> page = new Page<TestDemo>(pageNo, pageSize);
		IPage<TestDemo> pageList = testDemoService.page(page, queryWrapper);
//		IPage<TestDemo> pageList = testDemoService.myPage(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param testDemo
	 * @return
	 */
	@AutoLog(value = "测试用户表-添加")
	@ApiOperation(value="测试用户表-添加", notes="测试用户表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TestDemo testDemo) {
		testDemoService.save(testDemo);
		return Result.OK("添加成功！");
	}
	 @RequestMapping(value = "/relationAct", method = RequestMethod.GET)
	 public Result<?> relationAct(HttpServletRequest request, HttpServletResponse response) {
		 String dataId = request.getParameter("dataId");
		 testDemoService.relationAct(dataId);
		 return Result.OK();
	 }
	/**
	 *  编辑
	 *
	 * @param testDemo
	 * @return
	 */
	@AutoLog(value = "测试用户表-编辑")
	@ApiOperation(value="测试用户表-编辑", notes="测试用户表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TestDemo testDemo) {
		testDemoService.updateById(testDemo);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用户表-通过id删除")
	@ApiOperation(value="测试用户表-通过id删除", notes="测试用户表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		testDemoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试用户表-批量删除")
	@ApiOperation(value="测试用户表-批量删除", notes="测试用户表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testDemoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用户表-通过id查询")
	@ApiOperation(value="测试用户表-通过id查询", notes="测试用户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TestDemo testDemo = testDemoService.getById(id);
		if(testDemo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testDemo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testDemo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestDemo testDemo) {
        return super.exportXls(request, testDemo, TestDemo.class, "测试用户表");
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
        return super.importExcel(request, response, TestDemo.class);
    }


}
