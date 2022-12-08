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
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoneyRecord;
import org.jeecg.modules.marketing.service.IMarketingBusinessMakeMoneyRecordService;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 创业出账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="创业出账资金")
@RestController
@RequestMapping("/marketingBusinessMakeMoneyRecord/marketingBusinessMakeMoneyRecord")
public class MarketingBusinessMakeMoneyRecordController extends JeecgController<MarketingBusinessMakeMoneyRecord, IMarketingBusinessMakeMoneyRecordService> {
	@Autowired
	private IMarketingBusinessMakeMoneyRecordService marketingBusinessMakeMoneyRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param marketingBusinessMakeMoneyRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "创业出账资金-分页列表查询")
	@ApiOperation(value="创业出账资金-分页列表查询", notes="创业出账资金-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingBusinessMakeMoneyRecord marketingBusinessMakeMoneyRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MarketingBusinessMakeMoneyRecord> queryWrapper = QueryGenerator.initQueryWrapper(marketingBusinessMakeMoneyRecord, req.getParameterMap());
		Page<MarketingBusinessMakeMoneyRecord> page = new Page<MarketingBusinessMakeMoneyRecord>(pageNo, pageSize);
		IPage<MarketingBusinessMakeMoneyRecord> pageList = marketingBusinessMakeMoneyRecordService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param marketingBusinessMakeMoneyRecord
	 * @return
	 */
	@AutoLog(value = "创业出账资金-添加")
	@ApiOperation(value="创业出账资金-添加", notes="创业出账资金-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MarketingBusinessMakeMoneyRecord marketingBusinessMakeMoneyRecord) {
		marketingBusinessMakeMoneyRecordService.save(marketingBusinessMakeMoneyRecord);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param marketingBusinessMakeMoneyRecord
	 * @return
	 */
	@AutoLog(value = "创业出账资金-编辑")
	@ApiOperation(value="创业出账资金-编辑", notes="创业出账资金-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MarketingBusinessMakeMoneyRecord marketingBusinessMakeMoneyRecord) {
		marketingBusinessMakeMoneyRecordService.updateById(marketingBusinessMakeMoneyRecord);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "创业出账资金-通过id删除")
	@ApiOperation(value="创业出账资金-通过id删除", notes="创业出账资金-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		marketingBusinessMakeMoneyRecordService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "创业出账资金-批量删除")
	@ApiOperation(value="创业出账资金-批量删除", notes="创业出账资金-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.marketingBusinessMakeMoneyRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "创业出账资金-通过id查询")
	@ApiOperation(value="创业出账资金-通过id查询", notes="创业出账资金-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MarketingBusinessMakeMoneyRecord marketingBusinessMakeMoneyRecord = marketingBusinessMakeMoneyRecordService.getById(id);
		return Result.ok(marketingBusinessMakeMoneyRecord);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param marketingBusinessMakeMoneyRecord
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, MarketingBusinessMakeMoneyRecord marketingBusinessMakeMoneyRecord) {
      return super.exportXls(request, marketingBusinessMakeMoneyRecord, MarketingBusinessMakeMoneyRecord.class, "创业出账资金");
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
      return super.importExcel(request, response, MarketingBusinessMakeMoneyRecord.class);
  }

	 /**
	  * 获取出账明细
	  * @param marketingBusinessMakeMoneyId
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
  @GetMapping("findMoneyRecordListByMarketingBusinessMakeMoneyId")
  public Result<?> findMoneyRecordListByMarketingBusinessMakeMoneyId(@RequestParam(name="marketingBusinessMakeMoneyId",required=true) String marketingBusinessMakeMoneyId,
																	 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
  	return Result.ok(marketingBusinessMakeMoneyRecordService.findMoneyRecordListByMarketingBusinessMakeMoneyId(new Page<MarketingBusinessMakeMoneyRecordVO>(pageNo,pageSize),marketingBusinessMakeMoneyId));
  }
}
