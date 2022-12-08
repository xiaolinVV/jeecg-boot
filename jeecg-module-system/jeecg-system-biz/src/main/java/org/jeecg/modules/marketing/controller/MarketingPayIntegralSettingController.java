package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingPayIntegralSetting;
import org.jeecg.modules.marketing.service.IMarketingPayIntegralSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 支付即积分设置  
 * @Author: jeecg-boot
 * @Date:   2021-06-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="支付即积分设置  ")
@RestController
@RequestMapping("/marketing/marketingPayIntegralSetting")
public class MarketingPayIntegralSettingController {
	@Autowired
	private IMarketingPayIntegralSettingService marketingPayIntegralSettingService;

	 /**
	  *   添加或修改
	  * @param marketingPayIntegralSetting
	  * @return
	  */
	 @AutoLog(value = "支付即积分-添加或编辑")
	 @ApiOperation(value="支付即积分-添加或编辑", notes="支付即积分-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingPayIntegralSetting> addOrEdit(@RequestBody MarketingPayIntegralSetting marketingPayIntegralSetting) {
		 Result<MarketingPayIntegralSetting> result = new Result<MarketingPayIntegralSetting>();
		 try {
			 long count=marketingPayIntegralSettingService.count();
			 if(count==0) {
				 marketingPayIntegralSettingService.save(marketingPayIntegralSetting);
			 }else{
				 marketingPayIntegralSettingService.updateById(marketingPayIntegralSetting);
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
	 @AutoLog(value = "支付即积分-查询")
	 @ApiOperation(value="支付即积分-查询", notes="支付即积分-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingPayIntegralSetting> queryByOne() {
		 Result<MarketingPayIntegralSetting> result = new Result<>();
		 result.setResult(marketingPayIntegralSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }

}
