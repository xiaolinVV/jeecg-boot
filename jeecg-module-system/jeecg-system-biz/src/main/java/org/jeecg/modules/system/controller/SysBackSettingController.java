package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysBackSetting;
import org.jeecg.modules.system.service.ISysBackSettingService;
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
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 后台设置
 * @Author: jeecg-boot
 * @Date: 2020-02-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "后台设置")
@RestController
@RequestMapping("/sysBackSetting/sysBackSetting")
public class SysBackSettingController {
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
    @AutoLog(value = "后台设置-分页列表查询")
    @ApiOperation(value = "后台设置-分页列表查询", notes = "后台设置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SysBackSetting>> queryPageList(SysBackSetting sysBackSetting,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        Result<IPage<SysBackSetting>> result = new Result<IPage<SysBackSetting>>();
        QueryWrapper<SysBackSetting> queryWrapper = QueryGenerator.initQueryWrapper(sysBackSetting, req.getParameterMap());
        Page<SysBackSetting> page = new Page<SysBackSetting>(pageNo, pageSize);
        IPage<SysBackSetting> pageList = sysBackSettingService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysBackSetting
     * @return
     */
    @AutoLog(value = "后台设置-添加")
    @ApiOperation(value = "后台设置-添加", notes = "后台设置-添加")
    @PostMapping(value = "/add")
    public Result<SysBackSetting> add(@RequestBody SysBackSetting sysBackSetting) {
        return sysBackSettingService.add(sysBackSetting);
    }

    /**
     * 编辑
     *
     * @param sysBackSetting
     * @return
     */
    @AutoLog(value = "后台设置-编辑")
    @ApiOperation(value = "后台设置-编辑", notes = "后台设置-编辑")
    @PostMapping(value = "/edit")
    public Result<SysBackSetting> edit(@RequestBody SysBackSetting sysBackSetting) {
        return sysBackSettingService.edit(sysBackSetting);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "后台设置-通过id删除")
    @ApiOperation(value = "后台设置-通过id删除", notes = "后台设置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            sysBackSettingService.removeById(id);
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
    @AutoLog(value = "后台设置-批量删除")
    @ApiOperation(value = "后台设置-批量删除", notes = "后台设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysBackSetting> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysBackSetting> result = new Result<SysBackSetting>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysBackSettingService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "后台设置-通过id查询")
    @ApiOperation(value = "后台设置-通过id查询", notes = "后台设置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SysBackSetting> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysBackSetting> result = new Result<SysBackSetting>();
        SysBackSetting sysBackSetting = sysBackSettingService.getById(id);
        if (sysBackSetting == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysBackSetting);
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
        // Step.1 组装查询条件
        QueryWrapper<SysBackSetting> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                SysBackSetting sysBackSetting = JSON.parseObject(deString, SysBackSetting.class);
                queryWrapper = QueryGenerator.initQueryWrapper(sysBackSetting, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysBackSetting> pageList = sysBackSettingService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "后台设置列表");
        mv.addObject(NormalExcelConstants.CLASS, SysBackSetting.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("后台设置列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
                List<SysBackSetting> listSysBackSettings = ExcelImportUtil.importExcel(file.getInputStream(), SysBackSetting.class, params);
                sysBackSettingService.saveBatch(listSysBackSettings);
                return Result.ok("文件导入成功！数据行数:" + listSysBackSettings.size());
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

    /**
     * 后台设置
     * @param sysBackSetting
     * @return
     */
    @AutoLog(value = "后台设置")
    @ApiOperation(value = "后台设置", notes = "后台设置")
    @PostMapping(value = "/setSysBackSetting")
    public Result<SysBackSetting> setSysBackSetting(@RequestBody SysBackSetting sysBackSetting){
        return sysBackSettingService.setSysBackSetting(sysBackSetting);
    }

    @AutoLog(value = "后台设置返显")
    @ApiOperation(value = "后台设置返显", notes = "后台设置返显")
    @GetMapping(value = "/findSysBackSetting")
    public Result<SysBackSetting> findSysBackSetting(){
        return sysBackSettingService.findSysBackSetting();
    }
}
