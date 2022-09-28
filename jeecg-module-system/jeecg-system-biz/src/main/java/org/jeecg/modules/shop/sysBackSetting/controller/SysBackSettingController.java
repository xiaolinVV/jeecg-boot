package org.jeecg.modules.shop.sysBackSetting.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.shop.sysBackSetting.entity.SysBackSetting;
import org.jeecg.modules.shop.sysBackSetting.service.ISysBackSettingService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 后台设置
 * @Author: jeecg-boot
 * @Date:   2022-09-28
 * @Version: V1.0
 */
@Api(tags="后台设置")
@RestController
@RequestMapping("/sysBackSetting/sysBackSetting")
@Slf4j
public class SysBackSettingController extends JeecgController<SysBackSetting, ISysBackSettingService> {
	@Autowired
	private ISysBackSettingService sysBackSettingService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysBackSetting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "后台设置-分页列表查询")
	@ApiOperation(value="后台设置-分页列表查询", notes="后台设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysBackSetting>> queryPageList(SysBackSetting sysBackSetting,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysBackSetting> queryWrapper = QueryGenerator.initQueryWrapper(sysBackSetting, req.getParameterMap());
		Page<SysBackSetting> page = new Page<SysBackSetting>(pageNo, pageSize);
		IPage<SysBackSetting> pageList = sysBackSettingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param sysBackSetting
	 * @return
	 */
	@AutoLog(value = "后台设置-添加")
	@ApiOperation(value="后台设置-添加", notes="后台设置-添加")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_back_setting:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysBackSetting sysBackSetting) {
		sysBackSettingService.save(sysBackSetting);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysBackSetting
	 * @return
	 */
	@AutoLog(value = "后台设置-编辑")
	@ApiOperation(value="后台设置-编辑", notes="后台设置-编辑")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_back_setting:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysBackSetting sysBackSetting) {
		sysBackSettingService.updateById(sysBackSetting);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "后台设置-通过id删除")
	@ApiOperation(value="后台设置-通过id删除", notes="后台设置-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_back_setting:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysBackSettingService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "后台设置-批量删除")
	@ApiOperation(value="后台设置-批量删除", notes="后台设置-批量删除")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_back_setting:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysBackSettingService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "后台设置-通过id查询")
	@ApiOperation(value="后台设置-通过id查询", notes="后台设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysBackSetting> queryById(@RequestParam(name="id",required=true) String id) {
		SysBackSetting sysBackSetting = sysBackSettingService.getById(id);
		if(sysBackSetting==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysBackSetting);
	}

    /**
     * 查询第一条数据,用于配置场景，通常配置只会有一条数据的
     *
     * @return
     */
    //@AutoLog(value = "后台设置-查询第一条数据,用于配置场景")
    @ApiOperation(value="后台设置-查询第一条数据,用于配置场景", notes="后台设置-查询第一条数据,用于配置场景")
    @GetMapping(value = "/queryFirstData")
    public Result<SysBackSetting> queryFirstData() {
        SysBackSetting sysBackSetting = sysBackSettingService.getOne(new QueryWrapper<>(),false);
        if(sysBackSetting==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sysBackSetting);
    }

    /**
    * 导出excel
    *
    * @param request
    * @param sysBackSetting
    */
    //@RequiresPermissions("org.jeecg.modules.shop:sys_back_setting:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysBackSetting sysBackSetting) {
        return super.exportXls(request, sysBackSetting, SysBackSetting.class, "后台设置");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("sys_back_setting:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysBackSetting.class);
    }

}
