package org.jeecg.modules.marketing.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingIntegralSetting;
import org.jeecg.modules.marketing.service.IMarketingIntegralSettingService;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 积分设置
 * @Author: jeecg-boot
 * @Date: 2021-04-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "积分设置")
@RestController
@RequestMapping("/marketing/marketingIntegralSetting")
public class MarketingIntegralSettingController {
    @Autowired
    private IMarketingIntegralSettingService marketingIntegralSettingService;

    /**
     * 分页列表查询
     *
     * @param marketingIntegralSetting
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "积分设置-分页列表查询")
    @ApiOperation(value = "积分设置-分页列表查询", notes = "积分设置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingIntegralSetting>> queryPageList(MarketingIntegralSetting marketingIntegralSetting,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<MarketingIntegralSetting>> result = new Result<IPage<MarketingIntegralSetting>>();
        QueryWrapper<MarketingIntegralSetting> queryWrapper = QueryGenerator.initQueryWrapper(marketingIntegralSetting, req.getParameterMap());
        Page<MarketingIntegralSetting> page = new Page<MarketingIntegralSetting>(pageNo, pageSize);
        IPage<MarketingIntegralSetting> pageList = marketingIntegralSettingService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingIntegralSetting
     * @return
     */
    @AutoLog(value = "积分设置-添加")
    @ApiOperation(value = "积分设置-添加", notes = "积分设置-添加")
    @PostMapping(value = "/add")
    public Result<MarketingIntegralSetting> add(@RequestBody MarketingIntegralSetting marketingIntegralSetting) {
        Result<MarketingIntegralSetting> result = new Result<MarketingIntegralSetting>();
        try {
            marketingIntegralSettingService.save(marketingIntegralSetting);
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
     * @param marketingIntegralSetting
     * @return
     */
    @AutoLog(value = "积分设置-编辑")
    @ApiOperation(value = "积分设置-编辑", notes = "积分设置-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingIntegralSetting> edit(@RequestBody MarketingIntegralSetting marketingIntegralSetting) {
        Result<MarketingIntegralSetting> result = new Result<MarketingIntegralSetting>();
        MarketingIntegralSetting marketingIntegralSettingEntity = marketingIntegralSettingService.getById(marketingIntegralSetting.getId());
        if (marketingIntegralSettingEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingIntegralSettingService.updateById(marketingIntegralSetting);
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
    @AutoLog(value = "积分设置-通过id删除")
    @ApiOperation(value = "积分设置-通过id删除", notes = "积分设置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingIntegralSettingService.removeById(id);
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
    @AutoLog(value = "积分设置-批量删除")
    @ApiOperation(value = "积分设置-批量删除", notes = "积分设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingIntegralSetting> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingIntegralSetting> result = new Result<MarketingIntegralSetting>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingIntegralSettingService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "积分设置-通过id查询")
    @ApiOperation(value = "积分设置-通过id查询", notes = "积分设置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingIntegralSetting> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingIntegralSetting> result = new Result<MarketingIntegralSetting>();
        MarketingIntegralSetting marketingIntegralSetting = marketingIntegralSettingService.getById(id);
        if (marketingIntegralSetting == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingIntegralSetting);
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
        QueryWrapper<MarketingIntegralSetting> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingIntegralSetting marketingIntegralSetting = JSON.parseObject(deString, MarketingIntegralSetting.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingIntegralSetting, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingIntegralSetting> pageList = marketingIntegralSettingService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "积分设置列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingIntegralSetting.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("积分设置列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingIntegralSetting> listMarketingIntegralSettings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingIntegralSetting.class, params);
                marketingIntegralSettingService.saveBatch(listMarketingIntegralSettings);
                return Result.ok("文件导入成功！数据行数:" + listMarketingIntegralSettings.size());
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
     * 积分设置返显
     *
     * @return
     */
    @GetMapping("returnMarketingIntegralSetting")
    public Result<?> returnMarketingIntegralSetting() {
        List<MarketingIntegralSetting> marketingIntegralSettingList = marketingIntegralSettingService.list(new LambdaQueryWrapper<MarketingIntegralSetting>()
                .eq(MarketingIntegralSetting::getDelFlag, "0"));
        if (marketingIntegralSettingList.size() > 0) {
            return Result.ok(marketingIntegralSettingList.get(0));
        } else {
            return Result.ok("");
        }
    }

    /**
     * 积分设置
     * @param marketingIntegralSetting
     * @return
     */
    @PostMapping("setMarketingIntegralSetting")
    public Result<?> setMarketingIntegralSetting(@RequestBody MarketingIntegralSetting marketingIntegralSetting) {
        if (StringUtils.isBlank(marketingIntegralSetting.getId())){
            return add(marketingIntegralSetting);
        }else {
            return edit(marketingIntegralSetting);
        }
    }
}
