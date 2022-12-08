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
import org.jeecg.modules.store.entity.StoreCashierSetting;
import org.jeecg.modules.store.service.IStoreCashierSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 店铺收银台设置
 * @Author: jeecg-boot
 * @Date:   2022-04-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺收银台设置")
@RestController
@RequestMapping("/store/storeCashierSetting")
public class StoreCashierSettingController {
	@Autowired
	private IStoreCashierSettingService storeCashierSettingService;
	
	/**
	  * 分页列表查询
	 * @param storeCashierSetting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺收银台设置-分页列表查询")
	@ApiOperation(value="店铺收银台设置-分页列表查询", notes="店铺收银台设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreCashierSetting>> queryPageList(StoreCashierSetting storeCashierSetting,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StoreCashierSetting>> result = new Result<IPage<StoreCashierSetting>>();
		QueryWrapper<StoreCashierSetting> queryWrapper = QueryGenerator.initQueryWrapper(storeCashierSetting, req.getParameterMap());
		Page<StoreCashierSetting> page = new Page<StoreCashierSetting>(pageNo, pageSize);
		IPage<StoreCashierSetting> pageList = storeCashierSettingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeCashierSetting
	 * @return
	 */
	@AutoLog(value = "店铺收银台设置-添加")
	@ApiOperation(value="店铺收银台设置-添加", notes="店铺收银台设置-添加")
	@PostMapping(value = "/addOrEdit")
	public Result<StoreCashierSetting> addOrEdit(@RequestBody StoreCashierSetting storeCashierSetting) {
		Result<StoreCashierSetting> result = new Result<StoreCashierSetting>();
		try {
			StoreCashierSetting storeCashierSetting1=storeCashierSettingService.getOne(new LambdaQueryWrapper<StoreCashierSetting>().eq(StoreCashierSetting::getStoreManageId,storeCashierSetting.getStoreManageId()));
			if(storeCashierSetting1==null){
				storeCashierSettingService.save(storeCashierSetting);
				result.success("添加成功！");
			}else{
				storeCashierSetting.setId(storeCashierSetting1.getId());
				boolean ok = storeCashierSettingService.updateById(storeCashierSetting);
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
	@AutoLog(value = "店铺收银台设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreCashierSetting> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreCashierSetting> result = new Result<StoreCashierSetting>();
		StoreCashierSetting storeCashierSetting = storeCashierSettingService.getOne(new LambdaQueryWrapper<StoreCashierSetting>().eq(StoreCashierSetting::getStoreManageId,id));
		if(storeCashierSetting==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeCashierSetting);
			result.setSuccess(true);
		}
		return result;
	}


}
