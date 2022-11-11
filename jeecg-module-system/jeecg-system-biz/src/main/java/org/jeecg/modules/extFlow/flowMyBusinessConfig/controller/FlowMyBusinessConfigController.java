package org.jeecg.modules.extFlow.flowMyBusinessConfig.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.extFlow.flowMyBusinessConfig.entity.FlowMyBusinessConfig;
import org.jeecg.modules.extFlow.flowMyBusinessConfig.service.IFlowMyBusinessConfigService;

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
 * @Description:  流程配置表
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
@Api(tags=" 流程配置表")
@RestController
@RequestMapping("/flowMyBusinessConfig/flowMyBusinessConfig")
@Slf4j
public class FlowMyBusinessConfigController extends JeecgController<FlowMyBusinessConfig, IFlowMyBusinessConfigService> {
	@Autowired
	private IFlowMyBusinessConfigService flowMyBusinessConfigService;
	
	/**
	 * 分页列表查询
	 *
	 * @param flowMyBusinessConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = " 流程配置表-分页列表查询")
	@ApiOperation(value=" 流程配置表-分页列表查询", notes=" 流程配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FlowMyBusinessConfig>> queryPageList(FlowMyBusinessConfig flowMyBusinessConfig,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FlowMyBusinessConfig> queryWrapper = QueryGenerator.initQueryWrapper(flowMyBusinessConfig, req.getParameterMap());
		Page<FlowMyBusinessConfig> page = new Page<FlowMyBusinessConfig>(pageNo, pageSize);
		IPage<FlowMyBusinessConfig> pageList = flowMyBusinessConfigService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param flowMyBusinessConfig
	 * @return
	 */
	@AutoLog(value = " 流程配置表-添加")
	@ApiOperation(value=" 流程配置表-添加", notes=" 流程配置表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:flow_my_business_config:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FlowMyBusinessConfig flowMyBusinessConfig) {
		flowMyBusinessConfigService.save(flowMyBusinessConfig);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param flowMyBusinessConfig
	 * @return
	 */
	@AutoLog(value = " 流程配置表-编辑")
	@ApiOperation(value=" 流程配置表-编辑", notes=" 流程配置表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:flow_my_business_config:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FlowMyBusinessConfig flowMyBusinessConfig) {
		flowMyBusinessConfigService.updateById(flowMyBusinessConfig);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = " 流程配置表-通过id删除")
	@ApiOperation(value=" 流程配置表-通过id删除", notes=" 流程配置表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:flow_my_business_config:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		flowMyBusinessConfigService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = " 流程配置表-批量删除")
	@ApiOperation(value=" 流程配置表-批量删除", notes=" 流程配置表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:flow_my_business_config:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.flowMyBusinessConfigService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = " 流程配置表-通过id查询")
	@ApiOperation(value=" 流程配置表-通过id查询", notes=" 流程配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FlowMyBusinessConfig> queryById(@RequestParam(name="id",required=true) String id) {
		FlowMyBusinessConfig flowMyBusinessConfig = flowMyBusinessConfigService.getById(id);
		if(flowMyBusinessConfig==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(flowMyBusinessConfig);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param flowMyBusinessConfig
    */
    //@RequiresPermissions("org.jeecg.modules.demo:flow_my_business_config:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FlowMyBusinessConfig flowMyBusinessConfig) {
        return super.exportXls(request, flowMyBusinessConfig, FlowMyBusinessConfig.class, " 流程配置表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("flow_my_business_config:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FlowMyBusinessConfig.class);
    }

}
