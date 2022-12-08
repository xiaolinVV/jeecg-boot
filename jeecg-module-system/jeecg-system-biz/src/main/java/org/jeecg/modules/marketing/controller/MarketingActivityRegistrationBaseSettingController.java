package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingActivityRegistrationBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingActivityRegistrationBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 活动基础设置
 * @Author: jeecg-boot
 * @Date:   2021-05-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="活动基础设置")
@RestController
@RequestMapping("/marketing/marketingActivityRegistrationBaseSetting")
public class MarketingActivityRegistrationBaseSettingController {
	@Autowired
	private IMarketingActivityRegistrationBaseSettingService marketingActivityRegistrationBaseSettingService;

	 /**
	  *   添加或修改
	  * @param marketingActivityRegistrationBaseSetting
	  * @return
	  */
	 @AutoLog(value = "活动基础设置-添加或编辑")
	 @ApiOperation(value="活动基础设置-添加或编辑", notes="活动基础设置-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingActivityRegistrationBaseSetting> addOrEdit(@RequestBody MarketingActivityRegistrationBaseSetting marketingActivityRegistrationBaseSetting) {
		 Result<MarketingActivityRegistrationBaseSetting> result = new Result<MarketingActivityRegistrationBaseSetting>();
		 try {
			 long count=marketingActivityRegistrationBaseSettingService.count();
			 if(count==0) {
				 marketingActivityRegistrationBaseSettingService.save(marketingActivityRegistrationBaseSetting);
			 }else{
				 marketingActivityRegistrationBaseSettingService.updateById(marketingActivityRegistrationBaseSetting);
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
	 @AutoLog(value = "活动基础设置-查询")
	 @ApiOperation(value="活动基础设置-查询", notes="活动基础设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingActivityRegistrationBaseSetting> queryByOne() {
		 Result<MarketingActivityRegistrationBaseSetting> result = new Result<>();
		 result.setResult(marketingActivityRegistrationBaseSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }

}
