package org.jeecg.modules.demo.testOrderMain_erp.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderProductErp;
import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderMainErp;
import org.jeecg.modules.demo.testOrderMain_erp.service.ITestOrderMainErpService;
import org.jeecg.modules.demo.testOrderMain_erp.service.ITestOrderProductErpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 测试订单主表-ERP
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
@Api(tags="测试订单主表-ERP")
@RestController
@RequestMapping("/testOrderMain_erp/testOrderMainErp")
@Slf4j
public class TestOrderMainErpController extends JeecgController<TestOrderMainErp, ITestOrderMainErpService> {

	@Autowired
	private ITestOrderMainErpService testOrderMainErpService;

	@Autowired
	private ITestOrderProductErpService testOrderProductErpService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param testOrderMainErp
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "测试订单主表-ERP-分页列表查询")
	@ApiOperation(value="测试订单主表-ERP-分页列表查询", notes="测试订单主表-ERP-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestOrderMainErp>> queryPageList(TestOrderMainErp testOrderMainErp,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestOrderMainErp> queryWrapper = QueryGenerator.initQueryWrapper(testOrderMainErp, req.getParameterMap());
		Page<TestOrderMainErp> page = new Page<TestOrderMainErp>(pageNo, pageSize);
		IPage<TestOrderMainErp> pageList = testOrderMainErpService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param testOrderMainErp
     * @return
     */
    @AutoLog(value = "测试订单主表-ERP-添加")
    @ApiOperation(value="测试订单主表-ERP-添加", notes="测试订单主表-ERP-添加")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TestOrderMainErp testOrderMainErp) {
        testOrderMainErpService.save(testOrderMainErp);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param testOrderMainErp
     * @return
     */
    @AutoLog(value = "测试订单主表-ERP-编辑")
    @ApiOperation(value="测试订单主表-ERP-编辑", notes="测试订单主表-ERP-编辑")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody TestOrderMainErp testOrderMainErp) {
        testOrderMainErpService.updateById(testOrderMainErp);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "测试订单主表-ERP-通过id删除")
    @ApiOperation(value="测试订单主表-ERP-通过id删除", notes="测试订单主表-ERP-通过id删除")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        testOrderMainErpService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "测试订单主表-ERP-批量删除")
    @ApiOperation(value="测试订单主表-ERP-批量删除", notes="测试订单主表-ERP-批量删除")
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.testOrderMainErpService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestOrderMainErp testOrderMainErp) {
        return super.exportXls(request, testOrderMainErp, TestOrderMainErp.class, "测试订单主表-ERP");
    }

    /**
     * 导入
     * @return
     */
    //@RequiresPermissions("org.jeecg.modules.demo:test_order_main:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestOrderMainErp.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-订单产品明细-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	//@AutoLog(value = "订单产品明细-通过主表ID查询")
	@ApiOperation(value="订单产品明细-通过主表ID查询", notes="订单产品明细-通过主表ID查询")
	@GetMapping(value = "/listTestOrderProductErpByMainId")
    public Result<IPage<TestOrderProductErp>> listTestOrderProductErpByMainId(TestOrderProductErp testOrderProductErp,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<TestOrderProductErp> queryWrapper = QueryGenerator.initQueryWrapper(testOrderProductErp, req.getParameterMap());
        Page<TestOrderProductErp> page = new Page<TestOrderProductErp>(pageNo, pageSize);
        IPage<TestOrderProductErp> pageList = testOrderProductErpService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param testOrderProductErp
	 * @return
	 */
	@AutoLog(value = "订单产品明细-添加")
	@ApiOperation(value="订单产品明细-添加", notes="订单产品明细-添加")
	@PostMapping(value = "/addTestOrderProductErp")
	public Result<String> addTestOrderProductErp(@RequestBody TestOrderProductErp testOrderProductErp) {
		testOrderProductErpService.save(testOrderProductErp);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param testOrderProductErp
	 * @return
	 */
	@AutoLog(value = "订单产品明细-编辑")
	@ApiOperation(value="订单产品明细-编辑", notes="订单产品明细-编辑")
	@RequestMapping(value = "/editTestOrderProductErp", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> editTestOrderProductErp(@RequestBody TestOrderProductErp testOrderProductErp) {
		testOrderProductErpService.updateById(testOrderProductErp);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单产品明细-通过id删除")
	@ApiOperation(value="订单产品明细-通过id删除", notes="订单产品明细-通过id删除")
	@DeleteMapping(value = "/deleteTestOrderProductErp")
	public Result<String> deleteTestOrderProductErp(@RequestParam(name="id",required=true) String id) {
		testOrderProductErpService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订单产品明细-批量删除")
	@ApiOperation(value="订单产品明细-批量删除", notes="订单产品明细-批量删除")
	@DeleteMapping(value = "/deleteBatchTestOrderProductErp")
	public Result<String> deleteBatchTestOrderProductErp(@RequestParam(name="ids",required=true) String ids) {
	    this.testOrderProductErpService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportTestOrderProductErp")
    public ModelAndView exportTestOrderProductErp(HttpServletRequest request, TestOrderProductErp testOrderProductErp) {
		 // Step.1 组装查询条件
		 QueryWrapper<TestOrderProductErp> queryWrapper = QueryGenerator.initQueryWrapper(testOrderProductErp, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<TestOrderProductErp> pageList = testOrderProductErpService.list(queryWrapper);
		 List<TestOrderProductErp> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 //此处设置的filename无效,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.FILE_NAME, "订单产品明细");
		 mv.addObject(NormalExcelConstants.CLASS, TestOrderProductErp.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单产品明细报表", "导出人:" + sysUser.getRealname(), "订单产品明细"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importTestOrderProductErp/{mainId}")
    public Result<?> importTestOrderProductErp(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
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
				 List<TestOrderProductErp> list = ExcelImportUtil.importExcel(file.getInputStream(), TestOrderProductErp.class, params);
				 for (TestOrderProductErp temp : list) {
                    temp.setOrderFkId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 testOrderProductErpService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-订单产品明细-end----------------------------------------------*/




}
