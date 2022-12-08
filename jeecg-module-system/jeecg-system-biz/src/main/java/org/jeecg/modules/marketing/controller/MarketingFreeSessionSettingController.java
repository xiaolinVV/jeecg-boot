package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingFreeSessionSetting;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @Description: 免单场次设置
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="免单场次设置")
@RestController
@RequestMapping("/marketing/marketingFreeSessionSetting")
public class MarketingFreeSessionSettingController {
	@Autowired
	private IMarketingFreeSessionSettingService marketingFreeSessionSettingService;
	

	
	/**
	  *   添加或编辑
	 * @param marketingFreeSessionSetting
	 * @return
	 */
	@AutoLog(value = "免单场次设置-添加")
	@ApiOperation(value="免单场次设置-添加", notes="免单场次设置-添加")
	@PostMapping(value = "/addOrEdit")
	public Result<MarketingFreeSessionSetting> addOrEdit(@RequestBody MarketingFreeSessionSetting marketingFreeSessionSetting) {
		Result<MarketingFreeSessionSetting> result = new Result<MarketingFreeSessionSetting>();
		try {
			long count=marketingFreeSessionSettingService.count();

			//判断是否为自动
			if(marketingFreeSessionSetting.getCreateMode().equals("1")){
				//判断是否启用
				if(marketingFreeSessionSetting.getExecutingState().equals("1")){
					//设置执行次数为0
					marketingFreeSessionSetting.setExecutingDegree(new BigDecimal(0));
					//设置执行时间
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(marketingFreeSessionSetting.getExecutionDate());
					calendar.add(Calendar.DATE,Integer.parseInt(marketingFreeSessionSetting.getExecutionTime()));
					marketingFreeSessionSetting.setNextTime(calendar.getTime());
				}
			}

			if(count==0){
				marketingFreeSessionSettingService.save(marketingFreeSessionSetting);
			}else{
				marketingFreeSessionSettingService.updateById(marketingFreeSessionSetting);
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
	@AutoLog(value = "免单场次设置-通过id查询")
	@ApiOperation(value="免单场次设置-通过id查询", notes="免单场次设置-通过id查询")
	@GetMapping(value = "/queryByOne")
	public Result<MarketingFreeSessionSetting> queryByOne() {
		Result<MarketingFreeSessionSetting> result = new Result<MarketingFreeSessionSetting>();
		result.setResult(marketingFreeSessionSettingService.getOne(new LambdaQueryWrapper<>()));
		return result;
	}
}
