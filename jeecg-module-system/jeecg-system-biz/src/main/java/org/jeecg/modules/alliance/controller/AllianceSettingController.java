package org.jeecg.modules.alliance.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.alliance.entity.AllianceSetting;
import org.jeecg.modules.alliance.service.IAllianceSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 加盟商设置
 * @Author: jeecg-boot
 * @Date:   2022-03-22
 * @Version: V1.0
 */
@Slf4j
@Api(tags="加盟商设置")
@RestController
@RequestMapping("/alliance/allianceSetting")
public class AllianceSettingController {
	@Autowired
	private IAllianceSettingService allianceSettingService;


	 /**
	  *   添加
	  * @param allianceSetting
	  * @return
	  */
	 @AutoLog(value = "加盟商设置-添加")
	 @ApiOperation(value="加盟商设置-添加", notes="加盟商设置-添加")
	 @PostMapping(value = "/add")
	 public Result<AllianceSetting> add(@RequestBody AllianceSetting allianceSetting) {
		 Result<AllianceSetting> result = new Result<>();
		 try {
			 long count=allianceSettingService.count();
			 if(count==0){
				 allianceSettingService.save(allianceSetting);
			 }else{
				 allianceSettingService.updateById(allianceSetting);
			 }

			 result.success("添加成功！");
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 /**
	  * 加盟商设置查询
	  * @return
	  */
	 @AutoLog(value = "加盟商设置查询")
	 @ApiOperation(value="加盟商设置查询", notes="加盟商设置查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<AllianceSetting> queryByOne() {
		 Result<AllianceSetting> result = new Result<>();
		 result.setResult(allianceSettingService.getOne(new LambdaQueryWrapper<>()));
		 result.setSuccess(true);
		 return result;
	 }

 }
