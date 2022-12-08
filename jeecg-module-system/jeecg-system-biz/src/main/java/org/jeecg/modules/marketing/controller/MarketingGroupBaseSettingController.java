package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingGroupBaseSetting;
import org.jeecg.modules.marketing.service.IMarketingGroupBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="拼团基础设置")
@RestController
@RequestMapping("/marketing/marketingGroupBaseSetting")
public class MarketingGroupBaseSettingController {
	@Autowired
	private IMarketingGroupBaseSettingService marketingGroupBaseSettingService;

	/**
	  *   添加
	 * @param marketingGroupBaseSetting
	 * @return
	 */
	@AutoLog(value = "拼团基础设置-添加")
	@ApiOperation(value="拼团基础设置-添加", notes="拼团基础设置-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGroupBaseSetting> add(@RequestBody MarketingGroupBaseSetting marketingGroupBaseSetting) {
		Result<MarketingGroupBaseSetting> result = new Result<>();
		try {
			long count=marketingGroupBaseSettingService.count();
			if(count==0){
				marketingGroupBaseSettingService.save(marketingGroupBaseSetting);
			}else{
				marketingGroupBaseSettingService.updateById(marketingGroupBaseSetting);
			}

			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  * 拼团基础设置查询
	 * @return
	 */
	@AutoLog(value = "拼团基础设置查询")
	@ApiOperation(value="拼团基础设置查询", notes="拼团基础设置查询")
	@GetMapping(value = "/queryByOne")
	public Result<MarketingGroupBaseSetting> queryByOne() {
		Result<MarketingGroupBaseSetting> result = new Result<>();
			result.setResult(marketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>()));
			result.setSuccess(true);
		return result;
	}

}
