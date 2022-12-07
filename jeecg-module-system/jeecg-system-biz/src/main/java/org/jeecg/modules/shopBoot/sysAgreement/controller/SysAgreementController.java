package org.jeecg.modules.shopBoot.sysAgreement.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.shopBoot.sysAgreement.entity.SysAgreement;
import org.jeecg.modules.shopBoot.sysAgreement.service.ISysAgreementService;

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
 * @Description: 协议管理
 * @Author: jeecg-boot
 * @Date:   2022-09-29
 * @Version: V1.0
 */
@Api(tags="协议管理")
@RestController
@RequestMapping("/sysAgreement/sysAgreement")
@Slf4j
public class SysAgreementController extends JeecgController<SysAgreement, ISysAgreementService> {
	@Autowired
	private ISysAgreementService sysAgreementService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysAgreement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "协议管理-分页列表查询")
	@ApiOperation(value="协议管理-分页列表查询", notes="协议管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysAgreement>> queryPageList(SysAgreement sysAgreement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysAgreement> queryWrapper = QueryGenerator.initQueryWrapper(sysAgreement, req.getParameterMap());
		Page<SysAgreement> page = new Page<SysAgreement>(pageNo, pageSize);
		IPage<SysAgreement> pageList = sysAgreementService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param sysAgreement
	 * @return
	 */
	@AutoLog(value = "协议管理-添加")
	@ApiOperation(value="协议管理-添加", notes="协议管理-添加")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_agreement:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysAgreement sysAgreement) {
		sysAgreementService.save(sysAgreement);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysAgreement
	 * @return
	 */
	@AutoLog(value = "协议管理-编辑")
	@ApiOperation(value="协议管理-编辑", notes="协议管理-编辑")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_agreement:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysAgreement sysAgreement) {
		sysAgreementService.updateById(sysAgreement);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "协议管理-通过id删除")
	@ApiOperation(value="协议管理-通过id删除", notes="协议管理-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_agreement:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysAgreementService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "协议管理-批量删除")
	@ApiOperation(value="协议管理-批量删除", notes="协议管理-批量删除")
	//@RequiresPermissions("org.jeecg.modules.shop:sys_agreement:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysAgreementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "协议管理-通过id查询")
	@ApiOperation(value="协议管理-通过id查询", notes="协议管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysAgreement> queryById(@RequestParam(name="id",required=true) String id) {
		SysAgreement sysAgreement = sysAgreementService.getById(id);
		if(sysAgreement==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysAgreement);
	}

    /**
     * 查询第一条数据,用于配置场景，通常配置只会有一条数据的
     *
     * @return
     */
    //@AutoLog(value = "协议管理-查询第一条数据,用于配置场景")
    @ApiOperation(value="协议管理-查询第一条数据,用于配置场景", notes="协议管理-查询第一条数据,用于配置场景")
    @GetMapping(value = "/queryFirstData")
    public Result<SysAgreement> queryFirstData() {
        SysAgreement sysAgreement = sysAgreementService.getOne(new QueryWrapper<>(),false);
        if(sysAgreement==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sysAgreement);
    }

    /**
    * 导出excel
    *
    * @param request
    * @param sysAgreement
    */
    //@RequiresPermissions("org.jeecg.modules.shop:sys_agreement:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysAgreement sysAgreement) {
        return super.exportXls(request, sysAgreement, SysAgreement.class, "协议管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("sys_agreement:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysAgreement.class);
    }

}
