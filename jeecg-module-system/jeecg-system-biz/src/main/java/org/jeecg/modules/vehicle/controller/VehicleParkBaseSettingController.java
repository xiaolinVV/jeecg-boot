package org.jeecg.modules.vehicle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.vehicle.entity.VehicleParkBaseSetting;
import org.jeecg.modules.vehicle.service.IVehicleParkBaseSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 停车场设置
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="停车场设置")
@RestController
@RequestMapping("/vehicle/vehicleParkBaseSetting")
public class VehicleParkBaseSettingController {


	@Autowired
	private IVehicleParkBaseSettingService iVehicleParkBaseSettingService;

	 /**
	  *   添加或修改
	  *
	  *   {
	  *     "freeTime":{
	  *         "time":"15",
	  *         "unit":"0",
	  *         "include":"0",
	  *         "price":"0"
	  *     },
	  *     "convention":{
	  *         "time":"2",
	  *         "unit":"1",
	  *         "include":"1",
	  *         "price":"5"
	  *     },
	  *     "extra":{
	  *         "time":"1",
	  *         "unit":"1",
	  *         "include":"1",
	  *         "price":"2.00"
	  *     },
	  *     "height":{
	  *         "time":"24",
	  *         "price":"30.00"
	  *     }
	  * }
	  *
	  *
	  * @param vehicleParkBaseSetting
	  * @return
	  */
	 @AutoLog(value = "停车场设置-添加或编辑")
	 @ApiOperation(value="停车场设置-添加或编辑", notes="停车场设置-添加或编辑")
	 @PostMapping(value = "/addOrEdit")
	 public Result<VehicleParkBaseSetting> addOrEdit(@RequestBody VehicleParkBaseSetting vehicleParkBaseSetting) {
		 Result<VehicleParkBaseSetting> result = new Result<VehicleParkBaseSetting>();
		 try {
			 long count=iVehicleParkBaseSettingService.count();
			 if(count==0) {
				 iVehicleParkBaseSettingService.save(vehicleParkBaseSetting);
			 }else{
				 iVehicleParkBaseSettingService.updateById(vehicleParkBaseSetting);
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
	 @AutoLog(value = "停车场设置-查询")
	 @ApiOperation(value="停车场设置-查询", notes="停车场设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<VehicleParkBaseSetting> queryByOne() {
		 Result<VehicleParkBaseSetting> result = new Result<>();
		 result.setResult(iVehicleParkBaseSettingService.getOne(new LambdaQueryWrapper<>()));
		 return result;
	 }

}
