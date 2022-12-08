package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 拼购活动
 * @Author: jeecg-boot
 * @Date:   2021-06-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼购活动")
@RestController
@RequestMapping("/marketing/marketingGroupIntegralBaseSetting")
public class MarketingGroupIntegralBaseSettingController {
	@Autowired
	private IMarketingGroupIntegralBaseSettingService marketingGroupIntegralBaseSettingService;


	 /**
	  *   添加或修改
	  * @param marketingGroupIntegralBaseSetting
	  * @return
	  */
	 @AutoLog(value = "拼购活动-添加或编辑")
	 @ApiOperation(value="拼购活动-添加或编辑", notes="拼购活动-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingGroupIntegralBaseSetting> addOrEdit(@RequestBody MarketingGroupIntegralBaseSetting marketingGroupIntegralBaseSetting) {
		 Result<MarketingGroupIntegralBaseSetting> result = new Result<MarketingGroupIntegralBaseSetting>();
		 try {
			 long count=marketingGroupIntegralBaseSettingService.count();
			 if(count==0) {
				 marketingGroupIntegralBaseSettingService.save(marketingGroupIntegralBaseSetting);
			 }else{
				 marketingGroupIntegralBaseSettingService.updateById(marketingGroupIntegralBaseSetting);
			 }
			 result.success("添加成功！");
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }


	 /**
	  * 通过id查询
	  * @return
	  */
	 @AutoLog(value = "拼购活动设置-查询")
	 @ApiOperation(value="拼购活动设置-查询", notes="第三积分设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingGroupIntegralBaseSetting> queryByOne() {
		 Result<MarketingGroupIntegralBaseSetting> result = new Result<>();
		 result.setResult(marketingGroupIntegralBaseSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }


 }
