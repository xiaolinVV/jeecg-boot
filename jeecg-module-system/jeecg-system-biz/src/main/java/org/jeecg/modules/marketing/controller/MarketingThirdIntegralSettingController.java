package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingThirdIntegralSetting;
import org.jeecg.modules.marketing.service.IMarketingThirdIntegralSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 第三积分设置
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="第三积分设置")
@RestController
@RequestMapping("/marketing/marketingThirdIntegralSetting")
public class MarketingThirdIntegralSettingController {
	 @Autowired
	 private IMarketingThirdIntegralSettingService iMarketingThirdIntegralSettingService;

	 /**
	  *   添加或修改
	  * @param marketingThirdIntegralSetting
	  * @return
	  */
	 @AutoLog(value = "第三积分设置-添加或编辑")
	 @ApiOperation(value="第三积分设置-添加或编辑", notes="第三积分设置-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingThirdIntegralSetting> addOrEdit(@RequestBody MarketingThirdIntegralSetting marketingThirdIntegralSetting) {
		 Result<MarketingThirdIntegralSetting> result = new Result<MarketingThirdIntegralSetting>();
		 try {
			 long count=iMarketingThirdIntegralSettingService.count();
			 if(count==0) {
				 iMarketingThirdIntegralSettingService.save(marketingThirdIntegralSetting);
			 }else{
				 iMarketingThirdIntegralSettingService.updateById(marketingThirdIntegralSetting);
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
	 @AutoLog(value = "第三积分设置-查询")
	 @ApiOperation(value="第三积分设置-查询", notes="第三积分设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingThirdIntegralSetting> queryByOne() {
		 Result<MarketingThirdIntegralSetting> result = new Result<>();
		 result.setResult(iMarketingThirdIntegralSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }


 }
