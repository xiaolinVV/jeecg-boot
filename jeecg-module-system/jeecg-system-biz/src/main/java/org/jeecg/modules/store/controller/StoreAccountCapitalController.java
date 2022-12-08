package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BeseController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.store.dto.StoreAccountCapitalDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.vo.StoreAccountCapitalVO;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺资金流水
 * @Author: jeecg-boot
 * @Date: 2019-12-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "店铺资金流水")
@RestController
@RequestMapping("/storeAccountCapital/storeAccountCapital")
public class StoreAccountCapitalController extends BeseController<StoreAccountCapital, IStoreAccountCapitalService> {
    @Autowired
    private IStoreAccountCapitalService storeAccountCapitalService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    /**
     * 分页列表查询
     *
     * @param storeAccountCapitalVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "店铺资金流水-分页列表查询")
    @ApiOperation(value = "店铺资金流水-分页列表查询", notes = "店铺资金流水-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<StoreAccountCapitalDTO>> queryPageList(StoreAccountCapitalVO storeAccountCapitalVO,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req) {
        Result<IPage<StoreAccountCapitalDTO>> result = new Result<IPage<StoreAccountCapitalDTO>>();
        Page<StoreAccountCapital> page = new Page<StoreAccountCapital>(pageNo, pageSize);
        //店铺权限
        String str = PermissionUtils.ifPlatform();
        if (str != null) {
            storeAccountCapitalVO.setSysUserId(str);
        }
        IPage<StoreAccountCapitalDTO> pageList = storeAccountCapitalService.getStoreAccountCapitalList(page, storeAccountCapitalVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param storeAccountCapital
     * @return
     */
    @AutoLog(value = "店铺资金流水-添加")
    @ApiOperation(value = "店铺资金流水-添加", notes = "店铺资金流水-添加")
    @PostMapping(value = "/add")
    public Result<StoreAccountCapital> add(@RequestBody StoreAccountCapital storeAccountCapital) {
        Result<StoreAccountCapital> result = new Result<StoreAccountCapital>();
        try {
            storeAccountCapitalService.save(storeAccountCapital);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param storeAccountCapital
     * @return
     */
    @AutoLog(value = "店铺资金流水-编辑")
    @ApiOperation(value = "店铺资金流水-编辑", notes = "店铺资金流水-编辑")
    @PutMapping(value = "/edit")
    public Result<StoreAccountCapital> edit(@RequestBody StoreAccountCapital storeAccountCapital) {
        Result<StoreAccountCapital> result = new Result<StoreAccountCapital>();
        StoreAccountCapital storeAccountCapitalEntity = storeAccountCapitalService.getById(storeAccountCapital.getId());
        if (storeAccountCapitalEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = storeAccountCapitalService.updateById(storeAccountCapital);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "店铺资金流水-通过id删除")
    @ApiOperation(value = "店铺资金流水-通过id删除", notes = "店铺资金流水-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            storeAccountCapitalService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "店铺资金流水-批量删除")
    @ApiOperation(value = "店铺资金流水-批量删除", notes = "店铺资金流水-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<StoreAccountCapital> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<StoreAccountCapital> result = new Result<StoreAccountCapital>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.storeAccountCapitalService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "店铺资金流水-通过id查询")
    @ApiOperation(value = "店铺资金流水-通过id查询", notes = "店铺资金流水-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<StoreAccountCapital> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<StoreAccountCapital> result = new Result<StoreAccountCapital>();
        StoreAccountCapital storeAccountCapital = storeAccountCapitalService.getById(id);
        if (storeAccountCapital == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(storeAccountCapital);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // Step.1 组装查询条件
//	  QueryWrapper<StoreAccountCapital> queryWrapper = new QueryWrapper<>();
        QueryWrapper<StoreAccountCapital> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                StoreAccountCapital storeAccountCapital = JSON.parseObject(deString, StoreAccountCapital.class);
                queryWrapper = QueryGenerator.initQueryWrapper(storeAccountCapital, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<StoreAccountCapital> pageList = storeAccountCapitalService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "店铺资金流水列表");
        mv.addObject(NormalExcelConstants.CLASS, StoreAccountCapital.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺资金流水列表数据", sysUser.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }
    //店铺资金导出demo
    @RequestMapping("/exportSW")
    public ModelAndView exportSW(StoreAccountCapital storeAccountCapital,HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {
/*// 组装查询条件
        QueryWrapper<StoreAccountCapital> queryWrapper = QueryGenerator.initQueryWrapper(storeAccountCapital, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 获取导出数据
        List<StoreAccountCapital> pageList = storeAccountCapitalService.list(queryWrapper);
        List<StoreAccountCapital> exportList = null;
        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            *//*BeanUtils.copyProperties(pageList,exportList);*//*
            exportList = pageList;
        }

        // 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "店铺资金流水列表");
        mv.addObject(NormalExcelConstants.CLASS, StoreAccountCapitalDTO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("报表", "导出人:" + sysUser.getRealname(), "店铺资金流水列表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;*/
        return super.exportSW(request,storeAccountCapital,StoreAccountCapital.class,"店铺资金");
    }
    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<StoreAccountCapital> listStoreAccountCapitals = ExcelImportUtil.importExcel(file.getInputStream(), StoreAccountCapital.class, params);
                storeAccountCapitalService.saveBatch(listStoreAccountCapitals);
                return Result.ok("文件导入成功！数据行数:" + listStoreAccountCapitals.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

}
