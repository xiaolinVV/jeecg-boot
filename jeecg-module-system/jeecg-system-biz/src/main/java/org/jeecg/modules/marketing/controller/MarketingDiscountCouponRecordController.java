package org.jeecg.modules.marketing.controller;

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
import org.jeecg.modules.marketing.entity.MarketingDiscountCouponRecord;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: marketing_discount_coupon_record
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Api(tags="marketing_discount_coupon_record")
@RestController
@RequestMapping("/marketingDiscountCouponRecord/marketingDiscountCouponRecord")
@Slf4j
public class MarketingDiscountCouponRecordController extends JeecgController<MarketingDiscountCouponRecord, IMarketingDiscountCouponRecordService> {
	@Autowired
	private IMarketingDiscountCouponRecordService marketingDiscountCouponRecordService;

	/**
	 * 分页列表查询
	 *
	 * @param marketingDiscountCouponRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "marketing_discount_coupon_record-分页列表查询")
	@ApiOperation(value="marketing_discount_coupon_record-分页列表查询", notes="marketing_discount_coupon_record-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingDiscountCouponRecord marketingDiscountCouponRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MarketingDiscountCouponRecord> queryWrapper = QueryGenerator.initQueryWrapper(marketingDiscountCouponRecord, req.getParameterMap());
		Page<MarketingDiscountCouponRecord> page = new Page<MarketingDiscountCouponRecord>(pageNo, pageSize);
		IPage<MarketingDiscountCouponRecord> pageList = marketingDiscountCouponRecordService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param marketingDiscountCouponRecord
	 * @return
	 */
	@AutoLog(value = "marketing_discount_coupon_record-添加")
	@ApiOperation(value="marketing_discount_coupon_record-添加", notes="marketing_discount_coupon_record-添加")
	//@RequiresPermissions("marketingDiscountCouponRecord:marketing_discount_coupon_record:add")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MarketingDiscountCouponRecord marketingDiscountCouponRecord) {
		marketingDiscountCouponRecordService.save(marketingDiscountCouponRecord);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param marketingDiscountCouponRecord
	 * @return
	 */
	@AutoLog(value = "marketing_discount_coupon_record-编辑")
	@ApiOperation(value="marketing_discount_coupon_record-编辑", notes="marketing_discount_coupon_record-编辑")
	//@RequiresPermissions("marketingDiscountCouponRecord:marketing_discount_coupon_record:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody MarketingDiscountCouponRecord marketingDiscountCouponRecord) {
		marketingDiscountCouponRecordService.updateById(marketingDiscountCouponRecord);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "marketing_discount_coupon_record-通过id删除")
	@ApiOperation(value="marketing_discount_coupon_record-通过id删除", notes="marketing_discount_coupon_record-通过id删除")
	//@RequiresPermissions("marketingDiscountCouponRecord:marketing_discount_coupon_record:delete")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		marketingDiscountCouponRecordService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "marketing_discount_coupon_record-批量删除")
	@ApiOperation(value="marketing_discount_coupon_record-批量删除", notes="marketing_discount_coupon_record-批量删除")
	//@RequiresPermissions("marketingDiscountCouponRecord:marketing_discount_coupon_record:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.marketingDiscountCouponRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "marketing_discount_coupon_record-通过id查询")
	@ApiOperation(value="marketing_discount_coupon_record-通过id查询", notes="marketing_discount_coupon_record-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MarketingDiscountCouponRecord marketingDiscountCouponRecord = marketingDiscountCouponRecordService.getById(id);
		if(marketingDiscountCouponRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(marketingDiscountCouponRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param marketingDiscountCouponRecord
    */
    //@RequiresPermissions("marketingDiscountCouponRecord:marketing_discount_coupon_record:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MarketingDiscountCouponRecord marketingDiscountCouponRecord) {
        return super.exportXls(request, marketingDiscountCouponRecord, MarketingDiscountCouponRecord.class, "marketing_discount_coupon_record");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("marketingDiscountCouponRecord:marketing_discount_coupon_record:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MarketingDiscountCouponRecord.class);
    }

}
