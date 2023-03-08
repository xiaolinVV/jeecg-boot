package org.jeecg.modules.marketing.store.giftbag.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagSetting;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 礼包团-设置
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="礼包团-设置")
@RestController
@RequestMapping("/marketing.store.giftbag/marketingStoreGiftbagSetting")
public class MarketingStoreGiftbagSettingController {
	@Autowired
	private IMarketingStoreGiftbagSettingService marketingStoreGiftbagSettingService;

	 /**
	  *   添加或修改
	  * @param marketingStoreGiftbagSetting
	  * @return
	  */
	 @AutoLog(value = "礼包团-设置-添加或编辑")
	 @ApiOperation(value="礼包团-设置-添加或编辑", notes="礼包团-设置-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingStoreGiftbagSetting> addOrEdit(@RequestBody MarketingStoreGiftbagSetting marketingStoreGiftbagSetting) {
		 Result<MarketingStoreGiftbagSetting> result = new Result<MarketingStoreGiftbagSetting>();
		 try {
			 long count=marketingStoreGiftbagSettingService.count();
			 if(count==0) {
				 marketingStoreGiftbagSettingService.save(marketingStoreGiftbagSetting);
			 }else{
				 marketingStoreGiftbagSettingService.updateById(marketingStoreGiftbagSetting);
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
	 @AutoLog(value = "礼包团-设置-查询")
	 @ApiOperation(value="礼包团-设置-查询", notes="礼包团-设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingStoreGiftbagSetting> queryByOne() {
		 Result<MarketingStoreGiftbagSetting> result = new Result<>();
		 result.setResult(marketingStoreGiftbagSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }
}
