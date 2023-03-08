package org.jeecg.modules.good.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.good.entity.GoodSetting;
import org.jeecg.modules.good.service.IGoodSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 平台商品-设置
 * @Author: jeecg-boot
 * @Date:   2022-11-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="平台商品-设置")
@RestController
@RequestMapping("/good/goodSetting")
public class GoodSettingController {
	@Autowired
	private IGoodSettingService goodSettingService;

	 /**
	  *   添加或修改
	  * @param goodSetting
	  * @return
	  */
	 @AutoLog(value = "商品设置-添加或编辑")
	 @ApiOperation(value="商品设置-添加或编辑", notes="商品设置-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<GoodSetting> addOrEdit(@RequestBody GoodSetting goodSetting) {
		 Result<GoodSetting> result = new Result<GoodSetting>();
		 try {
			 long count=goodSettingService.count();
			 if(count==0) {
				 goodSettingService.save(goodSetting);
			 }else{
				 goodSettingService.updateById(goodSetting);
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
	 @AutoLog(value = "商品设置-查询")
	 @ApiOperation(value="商品设置-查询", notes="商品设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<GoodSetting> queryByOne() {
		 Result<GoodSetting> result = new Result<>();
		 result.setResult(goodSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }

}
