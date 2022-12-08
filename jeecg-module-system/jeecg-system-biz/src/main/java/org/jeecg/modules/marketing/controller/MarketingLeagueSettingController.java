package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.entity.MarketingLeagueSetting;
import org.jeecg.modules.marketing.service.IMarketingLeagueSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 加盟专区设置
 * @Author: jeecg-boot
 * @Date:   2021-12-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="加盟专区设置")
@RestController
@RequestMapping("/marketing/marketingLeagueSetting")
public class MarketingLeagueSettingController {
	@Autowired
	private IMarketingLeagueSettingService marketingLeagueSettingService;

	 /**
	  *   添加或修改
	  * @param marketingLeagueSetting
	  * @return
	  */
	 @AutoLog(value = "拼购活动-添加或编辑")
	 @ApiOperation(value="拼购活动-添加或编辑", notes="拼购活动-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingLeagueSetting> addOrEdit(@RequestBody MarketingLeagueSetting marketingLeagueSetting) {
		 Result<MarketingLeagueSetting> result = new Result<MarketingLeagueSetting>();
		 try {
			 long count=marketingLeagueSettingService.count();
			 if(count==0) {
				 marketingLeagueSettingService.save(marketingLeagueSetting);
			 }else{
				 marketingLeagueSettingService.updateById(marketingLeagueSetting);
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
	 public Result<MarketingLeagueSetting> queryByOne() {
		 Result<MarketingLeagueSetting> result = new Result<>();
		 result.setResult(marketingLeagueSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }


 }
