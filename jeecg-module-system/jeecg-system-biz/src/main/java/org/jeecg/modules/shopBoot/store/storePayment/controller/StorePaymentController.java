package org.jeecg.modules.shopBoot.store.storePayment.controller;

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
import org.jeecg.modules.shopBoot.store.storePayment.entity.StorePayment;
import org.jeecg.modules.shopBoot.store.storePayment.service.IStorePaymentService;

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
 * @Description: 店铺消费记录
 * @Author: jeecg-boot
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Api(tags="店铺消费记录")
@RestController
@RequestMapping("/store.storePayment/storePayment")
@Slf4j
public class StorePaymentController extends JeecgController<StorePayment, IStorePaymentService> {
	@Autowired
	private IStorePaymentService storePaymentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param storePayment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "店铺消费记录-分页列表查询")
	@ApiOperation(value="店铺消费记录-分页列表查询", notes="店铺消费记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StorePayment>> queryPageList(StorePayment storePayment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StorePayment> queryWrapper = QueryGenerator.initQueryWrapper(storePayment, req.getParameterMap());
		Page<StorePayment> page = new Page<StorePayment>(pageNo, pageSize);
		IPage<StorePayment> pageList = storePaymentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param storePayment
	 * @return
	 */
	@AutoLog(value = "店铺消费记录-添加")
	@ApiOperation(value="店铺消费记录-添加", notes="店铺消费记录-添加")
	//@RequiresPermissions("store.storePayment:store_payment:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StorePayment storePayment) {
		storePaymentService.save(storePayment);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param storePayment
	 * @return
	 */
	@AutoLog(value = "店铺消费记录-编辑")
	@ApiOperation(value="店铺消费记录-编辑", notes="店铺消费记录-编辑")
	//@RequiresPermissions("store.storePayment:store_payment:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StorePayment storePayment) {
		storePaymentService.updateById(storePayment);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺消费记录-通过id删除")
	@ApiOperation(value="店铺消费记录-通过id删除", notes="店铺消费记录-通过id删除")
	//@RequiresPermissions("store.storePayment:store_payment:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		storePaymentService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "店铺消费记录-批量删除")
	@ApiOperation(value="店铺消费记录-批量删除", notes="店铺消费记录-批量删除")
	//@RequiresPermissions("store.storePayment:store_payment:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.storePaymentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "店铺消费记录-通过id查询")
	@ApiOperation(value="店铺消费记录-通过id查询", notes="店铺消费记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StorePayment> queryById(@RequestParam(name="id",required=true) String id) {
		StorePayment storePayment = storePaymentService.getById(id);
		if(storePayment==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(storePayment);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param storePayment
    */
    //@RequiresPermissions("store.storePayment:store_payment:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StorePayment storePayment) {
        return super.exportXls(request, storePayment, StorePayment.class, "店铺消费记录");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("store.storePayment:store_payment:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, StorePayment.class);
    }

}
