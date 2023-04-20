package org.jeecg.modules.order.controller;

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
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.service.IOrderRefundListService;

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
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date:   2023-04-20
 * @Version: V1.0
 */
@Api(tags="order_refund_list")
@RestController
@RequestMapping("/order/orderRefundList")
@Slf4j
public class OrderRefundListController extends JeecgController<OrderRefundList, IOrderRefundListService> {
	@Autowired
	private IOrderRefundListService orderRefundListService;
	
	/**
	 * 分页列表查询
	 *
	 * @param orderRefundList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "order_refund_list-分页列表查询")
	@ApiOperation(value="order_refund_list-分页列表查询", notes="order_refund_list-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<OrderRefundList>> queryPageList(OrderRefundList orderRefundList,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OrderRefundList> queryWrapper = QueryGenerator.initQueryWrapper(orderRefundList, req.getParameterMap());
		Page<OrderRefundList> page = new Page<OrderRefundList>(pageNo, pageSize);
		IPage<OrderRefundList> pageList = orderRefundListService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param orderRefundList
	 * @return
	 */
	@AutoLog(value = "order_refund_list-添加")
	@ApiOperation(value="order_refund_list-添加", notes="order_refund_list-添加")
	//@RequiresPermissions("order:order_refund_list:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody OrderRefundList orderRefundList) {
		orderRefundListService.save(orderRefundList);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param orderRefundList
	 * @return
	 */
	@AutoLog(value = "order_refund_list-编辑")
	@ApiOperation(value="order_refund_list-编辑", notes="order_refund_list-编辑")
	//@RequiresPermissions("order:order_refund_list:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody OrderRefundList orderRefundList) {
		orderRefundListService.updateById(orderRefundList);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "order_refund_list-通过id删除")
	@ApiOperation(value="order_refund_list-通过id删除", notes="order_refund_list-通过id删除")
	//@RequiresPermissions("order:order_refund_list:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		orderRefundListService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "order_refund_list-批量删除")
	@ApiOperation(value="order_refund_list-批量删除", notes="order_refund_list-批量删除")
	//@RequiresPermissions("order:order_refund_list:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.orderRefundListService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "order_refund_list-通过id查询")
	@ApiOperation(value="order_refund_list-通过id查询", notes="order_refund_list-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OrderRefundList> queryById(@RequestParam(name="id",required=true) String id) {
		OrderRefundList orderRefundList = orderRefundListService.getById(id);
		if(orderRefundList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(orderRefundList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param orderRefundList
    */
    //@RequiresPermissions("order:order_refund_list:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OrderRefundList orderRefundList) {
        return super.exportXls(request, orderRefundList, OrderRefundList.class, "order_refund_list");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("order:order_refund_list:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OrderRefundList.class);
    }

}
