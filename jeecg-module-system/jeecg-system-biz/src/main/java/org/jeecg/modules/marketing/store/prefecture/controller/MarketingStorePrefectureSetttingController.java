package org.jeecg.modules.marketing.store.prefecture.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureSettting;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureSetttingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 店铺专区设置
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺专区设置")
@RestController
@RequestMapping("/marketing.store.prefecture/marketingStorePrefectureSettting")
public class MarketingStorePrefectureSetttingController {
	@Autowired
	private IMarketingStorePrefectureSetttingService marketingStorePrefectureSetttingService;
	 /**
	  *   添加或修改
	  * @param marketingStorePrefectureSettting
	  * @return
	  */
	 @AutoLog(value = "店铺专区-添加或编辑")
	 @ApiOperation(value="店铺专区-添加或编辑", notes="店铺专区-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<MarketingStorePrefectureSettting> addOrEdit(@RequestBody MarketingStorePrefectureSettting marketingStorePrefectureSettting) {
		 Result<MarketingStorePrefectureSettting> result = new Result<MarketingStorePrefectureSettting>();
		 try {
			 long count=marketingStorePrefectureSetttingService.count();
			 if(count==0) {
				 marketingStorePrefectureSetttingService.save(marketingStorePrefectureSettting);
			 }else{
				 marketingStorePrefectureSetttingService.updateById(marketingStorePrefectureSettting);
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
	 @AutoLog(value = "店铺专区-查询")
	 @ApiOperation(value="店铺专区-查询", notes="店铺专区-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<MarketingStorePrefectureSettting> queryByOne() {
		 Result<MarketingStorePrefectureSettting> result = new Result<>();
		 result.setResult(marketingStorePrefectureSetttingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }
}
