package org.jeecg.modules.store.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.store.entity.StoreFunctionSet;
import org.jeecg.modules.store.service.IStoreFunctionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 店铺管理-功能设置
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺管理-功能设置")
@RestController
@RequestMapping("/store/storeFunctionSet")
public class StoreFunctionSetController {
	@Autowired
	private IStoreFunctionSetService storeFunctionSetService;


	 /**
	  * 获取店铺列表
	  *
	  * @return
	  */
	@GetMapping("getStores")
	public Result<?> getStores(String type){
		return  Result.ok(storeFunctionSetService.getStore(type));
	}

	 /**
	  *   添加
	  * @param storeFunctionSet
	  * @return
	  */
	 @AutoLog(value = "店铺管理-功能设置-添加")
	 @ApiOperation(value="店铺管理-功能设置-添加", notes="店铺管理-功能设置-添加")
	 @PostMapping(value = "/addOrEdit")
	 public Result<StoreFunctionSet> addOrEdit(@RequestBody StoreFunctionSet storeFunctionSet) {
		 Result<StoreFunctionSet> result = new Result<>();
		 try {
			 long count=storeFunctionSetService.count(new LambdaQueryWrapper<StoreFunctionSet>().eq(StoreFunctionSet::getStoreManageId,storeFunctionSet.getStoreManageId()));
			 if(count==0){
				 storeFunctionSetService.save(storeFunctionSet);
			 }else{
				 storeFunctionSetService.updateById(storeFunctionSet);
			 }

			 result.success("添加成功！");
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 /**
	  * 店铺管理-功能设置查询
	  * @return
	  */
	 @AutoLog(value = "店铺管理-功能设置查询")
	 @ApiOperation(value="店铺管理-功能设置查询", notes="店铺管理-功能设置查询")
	 @GetMapping(value = "/queryById")
	 public Result<StoreFunctionSet> queryById(String storeManageId) {
		 Result<StoreFunctionSet> result = new Result<>();
		 result.setResult(storeFunctionSetService.getOne(new LambdaQueryWrapper<StoreFunctionSet>().eq(StoreFunctionSet::getStoreManageId,storeManageId)));
		 result.setSuccess(true);
		 return result;
	 }

}
