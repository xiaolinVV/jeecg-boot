package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingRushBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingRushBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 抢购活动-基础设置
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="抢购活动-基础设置")
@RestController
@RequestMapping("/marketingRushBaseSetting/marketingRushBaseSetting")
public class MarketingRushBaseSettingController {
	@Autowired
	private IMarketingRushBaseSettingService marketingRushBaseSettingService;

	 /**
	  * 基础设置返显
	  *
	  * @return
	  */
	 @AutoLog(value = "基础设置返显")
	 @ApiOperation(value = "基础设置返显", notes = "基础设置返显")
	 @GetMapping(value = "/findByOne")
	 public Result<?> findByOne() {
		 return Result.ok(marketingRushBaseSettingService.getOne(new LambdaQueryWrapper<>()));
	 }

	 /**
	  * 设置
	  *
	  * @param marketingRushBaseSetting
	  * @return
	  */
	 @AutoLog(value = "设置")
	 @ApiOperation(value = "设置", notes = "设置")
	 @PostMapping(value = "/saveAndUpdate")
	 public Result<?> saveAndUpdate(@RequestBody MarketingRushBaseSetting marketingRushBaseSetting) {
		 try {
			 if (marketingRushBaseSettingService.count(new LambdaQueryWrapper<>()) == 0) {
				 marketingRushBaseSettingService.save(marketingRushBaseSetting);
			 } else {
				 marketingRushBaseSettingService.update(marketingRushBaseSetting,new LambdaQueryWrapper<>());
			 }
			 return Result.ok("操作成功！");
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 return Result.error("操作失败");
		 }
	 }
}
