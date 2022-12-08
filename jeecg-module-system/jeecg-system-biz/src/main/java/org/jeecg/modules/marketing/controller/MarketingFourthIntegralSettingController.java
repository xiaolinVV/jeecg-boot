package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingFourthIntegralSetting;
import org.jeecg.modules.marketing.service.IMarketingFourthIntegralSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 第四积分设置
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="第四积分设置")
@RestController
@RequestMapping("/marketing/marketingFourthIntegralSetting")
public class MarketingFourthIntegralSettingController {
	 @Autowired
	 private IMarketingFourthIntegralSettingService iMarketingFourthIntegralSettingService;

	 /**
	  *   添加或修改
	  * @param marketingFourthIntegralSetting
	  * @return
	  */
	 @AutoLog(value = "第四积分设置-添加或编辑")
	 @ApiOperation(value="第四积分设置-添加或编辑", notes="第四积分设置-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingFourthIntegralSetting> addOrEdit(@RequestBody MarketingFourthIntegralSetting marketingFourthIntegralSetting) {
		 Result<MarketingFourthIntegralSetting> result = new Result<MarketingFourthIntegralSetting>();
		 try {
			 long count=iMarketingFourthIntegralSettingService.count();
			 if(count==0) {
				 iMarketingFourthIntegralSettingService.save(marketingFourthIntegralSetting);
			 }else{
				 iMarketingFourthIntegralSettingService.updateById(marketingFourthIntegralSetting);
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
	 @AutoLog(value = "第四积分设置-查询")
	 @ApiOperation(value="第四积分设置-查询", notes="第四积分设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingFourthIntegralSetting> queryByOne() {
		 Result<MarketingFourthIntegralSetting> result = new Result<>();
		 result.setResult(iMarketingFourthIntegralSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }

 }
