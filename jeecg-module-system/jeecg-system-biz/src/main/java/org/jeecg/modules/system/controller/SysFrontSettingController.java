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
import org.jeecg.modules.system.entity.SysFrontSetting;
import org.jeecg.modules.system.service.ISysFrontSettingService;
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
 * @Description: 小程序前端设置
 * @Author: jeecg-boot
 * @Date: 2020-02-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "小程序前端设置")
@RestController
@RequestMapping("/sysFrontSetting/sysFrontSetting")
public class SysFrontSettingController {
    @Autowired
    private ISysFrontSettingService sysFrontSettingService;

    /**
     * 分页列表查询
     *
     * @param sysFrontSetting
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "小程序前端设置-分页列表查询")
    @ApiOperation(value = "小程序前端设置-分页列表查询", notes = "小程序前端设置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SysFrontSetting>> queryPageList(SysFrontSetting sysFrontSetting,
                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest req) {
        Result<IPage<SysFrontSetting>> result = new Result<IPage<SysFrontSetting>>();
        QueryWrapper<SysFrontSetting> queryWrapper = QueryGenerator.initQueryWrapper(sysFrontSetting, req.getParameterMap());
        Page<SysFrontSetting> page = new Page<SysFrontSetting>(pageNo, pageSize);
        IPage<SysFrontSetting> pageList = sysFrontSettingService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysFrontSetting
     * @return
     */
    @AutoLog(value = "小程序前端设置-添加")
    @ApiOperation(value = "小程序前端设置-添加", notes = "小程序前端设置-添加")
    @PostMapping(value = "/add")
    public Result<SysFrontSetting> add(@RequestBody SysFrontSetting sysFrontSetting) {
        return sysFrontSettingService.add(sysFrontSetting);
    }

    /**
     * 编辑
     *
     * @param sysFrontSetting
     * @return
     */
    @AutoLog(value = "小程序前端设置-编辑")
    @ApiOperation(value = "小程序前端设置-编辑", notes = "小程序前端设置-编辑")
    @PostMapping(value = "/edit")
    public Result<SysFrontSetting> edit(@RequestBody SysFrontSetting sysFrontSetting) {
        return sysFrontSettingService.edit(sysFrontSetting);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "小程序前端设置-通过id删除")
    @ApiOperation(value = "小程序前端设置-通过id删除", notes = "小程序前端设置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            sysFrontSettingService.removeById(id);
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
    @AutoLog(value = "小程序前端设置-批量删除")
    @ApiOperation(value = "小程序前端设置-批量删除", notes = "小程序前端设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysFrontSetting> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysFrontSetting> result = new Result<SysFrontSetting>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysFrontSettingService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "小程序前端设置-通过id查询")
    @ApiOperation(value = "小程序前端设置-通过id查询", notes = "小程序前端设置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SysFrontSetting> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysFrontSetting> result = new Result<SysFrontSetting>();
        SysFrontSetting sysFrontSetting = sysFrontSettingService.getById(id);
        if (sysFrontSetting == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysFrontSetting);
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
        QueryWrapper<SysFrontSetting> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                SysFrontSetting sysFrontSetting = JSON.parseObject(deString, SysFrontSetting.class);
                queryWrapper = QueryGenerator.initQueryWrapper(sysFrontSetting, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysFrontSetting> pageList = sysFrontSettingService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "小程序前端设置列表");
        mv.addObject(NormalExcelConstants.CLASS, SysFrontSetting.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("小程序前端设置列表数据", "导出人:Jeecg", "导出信息"));
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
                List<SysFrontSetting> listSysFrontSettings = ExcelImportUtil.importExcel(file.getInputStream(), SysFrontSetting.class, params);
                sysFrontSettingService.saveBatch(listSysFrontSettings);
                return Result.ok("文件导入成功！数据行数:" + listSysFrontSettings.size());
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
     * 小程序或前端设置
     *
     * @param sysFrontSetting
     * @return
     */
    @AutoLog(value = "小程序前端设置")
    @ApiOperation(value = "小程序前端设置", notes = "小程序前端设置")
    @PostMapping(value = "/setSysFrontSetting")
    public Result<SysFrontSetting> setSysFrontSetting(@RequestBody SysFrontSetting sysFrontSetting) {
        return sysFrontSettingService.setSysFrontSetting(sysFrontSetting);
    }
    @AutoLog(value = "小程序前端返显")
    @ApiOperation(value = "小程序前端返显", notes = "小程序前端返显")
    @GetMapping(value = "/findSysFrontSetting")
    public Result<SysFrontSetting>findSysFrontSetting(){
        return sysFrontSettingService.findSysFrontSetting();
    }
}
