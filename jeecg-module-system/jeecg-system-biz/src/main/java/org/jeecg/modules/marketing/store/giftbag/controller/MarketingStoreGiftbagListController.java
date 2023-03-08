package org.jeecg.modules.marketing.store.giftbag.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

 /**
 * @Description: 礼包团-礼包设置
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="礼包团-礼包设置")
@RestController
@RequestMapping("/marketing.store.giftbag/marketingStoreGiftbagList")
public class MarketingStoreGiftbagListController {
	@Autowired
	private IMarketingStoreGiftbagListService marketingStoreGiftbagListService;


	 /**
	  * 根据店铺id查询数据
	  *
	  * @param storeManageId
	  * @return
	  */
	@GetMapping("getStoreGiftbagByStoreManageId")
	public Result<?> getStoreGiftbagByStoreManageId(String storeManageId){
		return Result.ok(marketingStoreGiftbagListService.getMarketingStoreGiftbagListByStoreManageId(storeManageId));
	}

	
	/**
	  *   添加
	 * @param marketingStoreGiftbagList
	 * @return
	 */
	@AutoLog(value = "礼包团-礼包设置-添加")
	@ApiOperation(value="礼包团-礼包设置-添加", notes="礼包团-礼包设置-添加")
	@PostMapping(value = "/addOrEdit")
	public Result<MarketingStoreGiftbagList> addOrEdit(@RequestBody MarketingStoreGiftbagList marketingStoreGiftbagList) {
		Result<MarketingStoreGiftbagList> result = new Result<MarketingStoreGiftbagList>();
		try {
			marketingStoreGiftbagListService.saveOrUpdate(marketingStoreGiftbagList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "礼包团-礼包设置-通过id删除")
	@ApiOperation(value="礼包团-礼包设置-通过id删除", notes="礼包团-礼包设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingStoreGiftbagListService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "礼包团-礼包设置-批量删除")
	@ApiOperation(value="礼包团-礼包设置-批量删除", notes="礼包团-礼包设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingStoreGiftbagList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingStoreGiftbagList> result = new Result<MarketingStoreGiftbagList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingStoreGiftbagListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "礼包团-礼包设置-通过id查询")
	@ApiOperation(value="礼包团-礼包设置-通过id查询", notes="礼包团-礼包设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingStoreGiftbagList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingStoreGiftbagList> result = new Result<MarketingStoreGiftbagList>();
		MarketingStoreGiftbagList marketingStoreGiftbagList = marketingStoreGiftbagListService.getById(id);
		if(marketingStoreGiftbagList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingStoreGiftbagList);
			result.setSuccess(true);
		}
		return result;
	}

}
