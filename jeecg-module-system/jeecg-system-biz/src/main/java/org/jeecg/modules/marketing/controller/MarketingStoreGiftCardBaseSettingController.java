package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 店铺礼品卡基础设置
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺礼品卡基础设置")
@RestController
@RequestMapping("/marketing/marketingStoreGiftCardBaseSetting")
public class MarketingStoreGiftCardBaseSettingController {
	@Autowired
	private IMarketingStoreGiftCardBaseSettingService marketingStoreGiftCardBaseSettingService;

	 /**
	  *   添加或修改
	  * @param marketingStoreGiftCardBaseSetting
	  * @return
	  */
	 @AutoLog(value = "店铺礼品卡基础设置-添加或编辑")
	 @ApiOperation(value="店铺礼品卡基础设置-添加或编辑", notes="店铺礼品卡基础设置-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingStoreGiftCardBaseSetting> addOrEdit(@RequestBody MarketingStoreGiftCardBaseSetting marketingStoreGiftCardBaseSetting) {
		 Result<MarketingStoreGiftCardBaseSetting> result = new Result<MarketingStoreGiftCardBaseSetting>();
		 try {
			 long count=marketingStoreGiftCardBaseSettingService.count();
			 if(count==0) {
				 marketingStoreGiftCardBaseSettingService.save(marketingStoreGiftCardBaseSetting);
			 }else{
				 marketingStoreGiftCardBaseSettingService.updateById(marketingStoreGiftCardBaseSetting);
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
	 @AutoLog(value = "店铺礼品卡基础设置-查询")
	 @ApiOperation(value="店铺礼品卡基础设置-查询", notes="店铺礼品卡基础设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingStoreGiftCardBaseSetting> queryByOne() {
		 Result<MarketingStoreGiftCardBaseSetting> result = new Result<>();
		 result.setResult(marketingStoreGiftCardBaseSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }

}
