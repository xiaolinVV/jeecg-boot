package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingFreeBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingFreeBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 免单基础设置
 * @Author: jeecg-boot
 * @Date:   2021-02-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单基础设置")
@RestController
@RequestMapping("/marketing/marketingFreeBaseSetting")
public class MarketingFreeBaseSettingController {
	@Autowired
	private IMarketingFreeBaseSettingService marketingFreeBaseSettingService;

	/**
	  *   添加或修改
	 * @param marketingFreeBaseSetting
	 * @return
	 */
	@AutoLog(value = "免单基础设置-添加或编辑")
	@ApiOperation(value="免单基础设置-添加或编辑", notes="免单基础设置-添加或编辑")
	@PostMapping(value = "/addOrEdit")
	public Result<MarketingFreeBaseSetting> addOrEdit(@RequestBody MarketingFreeBaseSetting marketingFreeBaseSetting) {
		Result<MarketingFreeBaseSetting> result = new Result<MarketingFreeBaseSetting>();
		try {
			long count=marketingFreeBaseSettingService.count();
			if(count==0) {
				marketingFreeBaseSettingService.save(marketingFreeBaseSetting);
			}else{
				marketingFreeBaseSettingService.updateById(marketingFreeBaseSetting);
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
	@AutoLog(value = "免单基础设置-查询")
	@ApiOperation(value="免单基础设置-查询", notes="免单基础设置-查询")
	@GetMapping(value = "/queryByOne")
	public Result<MarketingFreeBaseSetting> queryByOne() {
		Result<MarketingFreeBaseSetting> result = new Result<>();
		result.setResult(marketingFreeBaseSettingService.getOne(new LambdaQueryWrapper<>()));
		return result;
	}



}
