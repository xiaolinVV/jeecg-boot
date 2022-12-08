package org.jeecg.modules.shopBoot.storeSetting.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.shopBoot.storeSetting.entity.StoreSetting;
import org.jeecg.modules.shopBoot.storeSetting.service.IStoreSettingService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 店铺设置
 * @Author: jeecg-boot
 * @Date:   2022-09-29
 * @Version: V1.0
 */
@Api(tags="店铺设置")
@RestController
@RequestMapping("/storeSetting/storeSetting")
@Slf4j
public class StoreSettingController extends JeecgController<StoreSetting, IStoreSettingService> {
	@Autowired
	private IStoreSettingService storeSettingService;
	
	/**
	 * 分页列表查询
	 *
	 * @param storeSetting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "店铺设置-分页列表查询")
	@ApiOperation(value="店铺设置-分页列表查询", notes="店铺设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreSetting>> queryPageList(StoreSetting storeSetting,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StoreSetting> queryWrapper = QueryGenerator.initQueryWrapper(storeSetting, req.getParameterMap());
		Page<StoreSetting> page = new Page<StoreSetting>(pageNo, pageSize);
		IPage<StoreSetting> pageList = storeSettingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param storeSetting
	 * @return
	 */
	@AutoLog(value = "店铺设置-添加")
	@ApiOperation(value="店铺设置-添加", notes="店铺设置-添加")
	//@RequiresPermissions("org.jeecg.modules.shop:store_setting:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StoreSetting storeSetting) {
		storeSettingService.save(storeSetting);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param storeSetting
	 * @return
	 */
	@AutoLog(value = "店铺设置-编辑")
	@ApiOperation(value="店铺设置-编辑", notes="店铺设置-编辑")
	//@RequiresPermissions("org.jeecg.modules.shop:store_setting:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StoreSetting storeSetting) {
		storeSettingService.updateById(storeSetting);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺设置-通过id删除")
	@ApiOperation(value="店铺设置-通过id删除", notes="店铺设置-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.shop:store_setting:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		storeSettingService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "店铺设置-批量删除")
	@ApiOperation(value="店铺设置-批量删除", notes="店铺设置-批量删除")
	//@RequiresPermissions("org.jeecg.modules.shop:store_setting:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.storeSettingService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "店铺设置-通过id查询")
	@ApiOperation(value="店铺设置-通过id查询", notes="店铺设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreSetting> queryById(@RequestParam(name="id",required=true) String id) {
		StoreSetting storeSetting = storeSettingService.getById(id);
		if(storeSetting==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(storeSetting);
	}

    /**
     * 查询第一条数据,用于配置场景，通常配置只会有一条数据的
     *
     * @return
     */
    //@AutoLog(value = "店铺设置-查询第一条数据,用于配置场景")
    @ApiOperation(value="店铺设置-查询第一条数据,用于配置场景", notes="店铺设置-查询第一条数据,用于配置场景")
    @GetMapping(value = "/queryFirstData")
    public Result<StoreSetting> queryFirstData() {
        StoreSetting storeSetting = storeSettingService.getOne(new QueryWrapper<>(),false);
        if(storeSetting==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(storeSetting);
    }

    /**
    * 导出excel
    *
    * @param request
    * @param storeSetting
    */
    //@RequiresPermissions("org.jeecg.modules.shop:store_setting:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StoreSetting storeSetting) {
        return super.exportXls(request, storeSetting, StoreSetting.class, "店铺设置");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("store_setting:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, StoreSetting.class);
    }

}