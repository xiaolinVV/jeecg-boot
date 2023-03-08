package org.jeecg.modules.store.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.store.entity.StoreOrderSetting;
import org.jeecg.modules.store.service.IStoreOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

 /**
 * @Description: 店铺订单设置
 * @Author: jeecg-boot
 * @Date:   2022-08-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺订单设置")
@RestController
@RequestMapping("/store/storeOrderSetting")
public class StoreOrderSettingController {
	@Autowired
	private IStoreOrderSettingService storeOrderSettingService;
	
	/**
	  * 分页列表查询
	 * @param storeOrderSetting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺订单设置-分页列表查询")
	@ApiOperation(value="店铺订单设置-分页列表查询", notes="店铺订单设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreOrderSetting>> queryPageList(StoreOrderSetting storeOrderSetting,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StoreOrderSetting>> result = new Result<IPage<StoreOrderSetting>>();
		QueryWrapper<StoreOrderSetting> queryWrapper = QueryGenerator.initQueryWrapper(storeOrderSetting, req.getParameterMap());
		Page<StoreOrderSetting> page = new Page<StoreOrderSetting>(pageNo, pageSize);
		IPage<StoreOrderSetting> pageList = storeOrderSettingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 /**
	  *   添加
	  * @param storeOrderSetting
	  * @return
	  */
	 @AutoLog(value = "店铺订单设置-添加")
	 @ApiOperation(value="店铺订单设置-添加", notes="店铺订单设置-添加")
	 @PostMapping(value = "/addOrEdit")
	 public Result<StoreOrderSetting> addOrEdit(@RequestBody StoreOrderSetting storeOrderSetting) {
		 Result<StoreOrderSetting> result = new Result<StoreOrderSetting>();
		 try {
			 StoreOrderSetting storeOrderSetting1=storeOrderSettingService.getOne(new LambdaQueryWrapper<StoreOrderSetting>().eq(StoreOrderSetting::getStoreManageId,storeOrderSetting.getStoreManageId()));
			 if(storeOrderSetting1==null){
				 storeOrderSettingService.save(storeOrderSetting);
				 result.success("添加成功！");
			 }else{
				 storeOrderSetting.setId(storeOrderSetting1.getId());
				 boolean ok = storeOrderSettingService.updateById(storeOrderSetting);
				 //TODO 返回false说明什么？
				 if(ok) {
					 result.success("修改成功!");
				 }
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 /**
	  * 通过id查询
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "店铺订单设置-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<StoreOrderSetting> queryById(@RequestParam(name="id",required=true) String id) {
		 Result<StoreOrderSetting> result = new Result<StoreOrderSetting>();
		 StoreOrderSetting storeOrderSetting = storeOrderSettingService.getOne(new LambdaQueryWrapper<StoreOrderSetting>().eq(StoreOrderSetting::getStoreManageId,id));
		 if(storeOrderSetting==null) {
			 result.error500("未找到对应实体");
		 }else {
			 result.setResult(storeOrderSetting);
			 result.setSuccess(true);
		 }
		 return result;
	 }

}
