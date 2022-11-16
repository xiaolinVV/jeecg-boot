package org.jeecg.modules.demo.testOrderMain_many.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.testOrderMain_many.entity.TestOrderProduct;
import org.jeecg.modules.demo.testOrderMain_many.entity.TestOrderMain;
import org.jeecg.modules.demo.testOrderMain_many.vo.TestOrderMainPage;
import org.jeecg.modules.demo.testOrderMain_many.service.ITestOrderMainService;
import org.jeecg.modules.demo.testOrderMain_many.service.ITestOrderProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 测试订单主表
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Api(tags="测试订单主表")
@RestController
@RequestMapping("/testOrderMain_many/testOrderMain")
@Slf4j
public class TestOrderMainController {
	@Autowired
	private ITestOrderMainService testOrderMainService;
	@Autowired
	private ITestOrderProductService testOrderProductService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testOrderMain
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "测试订单主表-分页列表查询")
	@ApiOperation(value="测试订单主表-分页列表查询", notes="测试订单主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestOrderMain>> queryPageList(TestOrderMain testOrderMain,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(testOrderMain, req.getParameterMap());
		Page<TestOrderMain> page = new Page<TestOrderMain>(pageNo, pageSize);
		IPage<TestOrderMain> pageList = testOrderMainService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testOrderMainPage
	 * @return
	 */
	@AutoLog(value = "测试订单主表-添加")
	@ApiOperation(value="测试订单主表-添加", notes="测试订单主表-添加")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestOrderMainPage testOrderMainPage) {
		TestOrderMain testOrderMain = new TestOrderMain();
		BeanUtils.copyProperties(testOrderMainPage, testOrderMain);
		testOrderMainService.saveMain(testOrderMain, testOrderMainPage.getTestOrderProductList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testOrderMainPage
	 * @return
	 */
	@AutoLog(value = "测试订单主表-编辑")
	@ApiOperation(value="测试订单主表-编辑", notes="测试订单主表-编辑")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestOrderMainPage testOrderMainPage) {
		TestOrderMain testOrderMain = new TestOrderMain();
		BeanUtils.copyProperties(testOrderMainPage, testOrderMain);
		TestOrderMain testOrderMainEntity = testOrderMainService.getById(testOrderMain.getId());
		if(testOrderMainEntity==null) {
			return Result.error("未找到对应数据");
		}
		testOrderMainService.updateMain(testOrderMain, testOrderMainPage.getTestOrderProductList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试订单主表-通过id删除")
	@ApiOperation(value="测试订单主表-通过id删除", notes="测试订单主表-通过id删除")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testOrderMainService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试订单主表-批量删除")
	@ApiOperation(value="测试订单主表-批量删除", notes="测试订单主表-批量删除")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testOrderMainService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "测试订单主表-通过id查询")
	@ApiOperation(value="测试订单主表-通过id查询", notes="测试订单主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestOrderMain> queryById(@RequestParam(name="id",required=true) String id) {
		TestOrderMain testOrderMain = testOrderMainService.getById(id);
		if(testOrderMain==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testOrderMain);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "订单产品明细通过主表ID查询")
	@ApiOperation(value="订单产品明细主表ID查询", notes="订单产品明细-通主表ID查询")
	@GetMapping(value = "/queryTestOrderProductByMainId")
	public Result<List<TestOrderProduct>> queryTestOrderProductListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TestOrderProduct> testOrderProductList = testOrderProductService.selectByMainId(id);
		return Result.OK(testOrderProductList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testOrderMain
    */
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestOrderMain testOrderMain) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<TestOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(testOrderMain, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<TestOrderMain> testOrderMainList = testOrderMainService.list(queryWrapper);

      // Step.3 组装pageList
      List<TestOrderMainPage> pageList = new ArrayList<TestOrderMainPage>();
      for (TestOrderMain main : testOrderMainList) {
          TestOrderMainPage vo = new TestOrderMainPage();
          BeanUtils.copyProperties(main, vo);
          List<TestOrderProduct> testOrderProductList = testOrderProductService.selectByMainId(main.getId());
          vo.setTestOrderProductList(testOrderProductList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "测试订单主表列表");
      mv.addObject(NormalExcelConstants.CLASS, TestOrderMainPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("测试订单主表数据", "导出人:"+sysUser.getRealname(), "测试订单主表"));
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
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<TestOrderMainPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TestOrderMainPage.class, params);
              for (TestOrderMainPage page : list) {
                  TestOrderMain po = new TestOrderMain();
                  BeanUtils.copyProperties(page, po);
                  testOrderMainService.saveMain(po, page.getTestOrderProductList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
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
      return Result.OK("文件导入失败！");
    }

}
