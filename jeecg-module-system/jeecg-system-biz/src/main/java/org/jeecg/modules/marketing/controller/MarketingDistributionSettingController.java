package org.jeecg.modules.marketing.controller;

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
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.service.IMarketingDistributionSettingService;
import org.jeecg.modules.marketing.vo.MarketingDistributionSettingVO;
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
 * @Description: 平台分销设置
 * @Author: jeecg-boot
 * @Date: 2019-12-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "平台分销设置")
@RestController
@RequestMapping("/marketingDistributionSetting/marketingDistributionSetting")
public class MarketingDistributionSettingController {
    @Autowired
    private IMarketingDistributionSettingService marketingDistributionSettingService;

    /**
     * 分页列表查询
     *
     * @param marketingDistributionSetting
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "平台分销设置-分页列表查询")
    @ApiOperation(value = "平台分销设置-分页列表查询", notes = "平台分销设置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingDistributionSetting>> queryPageList(MarketingDistributionSetting marketingDistributionSetting,
                                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     HttpServletRequest req) {
        Result<IPage<MarketingDistributionSetting>> result = new Result<IPage<MarketingDistributionSetting>>();
        QueryWrapper<MarketingDistributionSetting> queryWrapper = QueryGenerator.initQueryWrapper(marketingDistributionSetting, req.getParameterMap());
        Page<MarketingDistributionSetting> page = new Page<MarketingDistributionSetting>(pageNo, pageSize);
        IPage<MarketingDistributionSetting> pageList = marketingDistributionSettingService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingDistributionSetting
     * @return
     */
    @AutoLog(value = "平台分销设置-添加")
    @ApiOperation(value = "平台分销设置-添加", notes = "平台分销设置-添加")
    @PostMapping(value = "/add")
    public Result<MarketingDistributionSetting> add(@RequestBody MarketingDistributionSetting marketingDistributionSetting) {
        Result<MarketingDistributionSetting> result = new Result<MarketingDistributionSetting>();
        try {
            marketingDistributionSettingService.save(marketingDistributionSetting);
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
     * @param marketingDistributionSetting
     * @return
     */
    @AutoLog(value = "平台分销设置-编辑")
    @ApiOperation(value = "平台分销设置-编辑", notes = "平台分销设置-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingDistributionSetting> edit(@RequestBody MarketingDistributionSetting marketingDistributionSetting) {
        Result<MarketingDistributionSetting> result = new Result<MarketingDistributionSetting>();
        MarketingDistributionSetting marketingDistributionSettingEntity = marketingDistributionSettingService.getById(marketingDistributionSetting.getId());
        if (marketingDistributionSettingEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingDistributionSettingService.saveOrUpdate(marketingDistributionSetting);
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
    @AutoLog(value = "平台分销设置-通过id删除")
    @ApiOperation(value = "平台分销设置-通过id删除", notes = "平台分销设置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingDistributionSettingService.removeById(id);
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
    @AutoLog(value = "平台分销设置-批量删除")
    @ApiOperation(value = "平台分销设置-批量删除", notes = "平台分销设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingDistributionSetting> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingDistributionSetting> result = new Result<MarketingDistributionSetting>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingDistributionSettingService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "平台分销设置-通过id查询")
    @ApiOperation(value = "平台分销设置-通过id查询", notes = "平台分销设置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingDistributionSetting> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingDistributionSetting> result = new Result<MarketingDistributionSetting>();
        MarketingDistributionSetting marketingDistributionSetting = marketingDistributionSettingService.getById(id);
        if (marketingDistributionSetting == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingDistributionSetting);
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
        QueryWrapper<MarketingDistributionSetting> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingDistributionSetting marketingDistributionSetting = JSON.parseObject(deString, MarketingDistributionSetting.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingDistributionSetting, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingDistributionSetting> pageList = marketingDistributionSettingService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "平台分销设置列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingDistributionSetting.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台分销设置列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingDistributionSetting> listMarketingDistributionSettings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingDistributionSetting.class, params);
                marketingDistributionSettingService.saveBatch(listMarketingDistributionSettings);
                return Result.ok("文件导入成功！数据行数:" + listMarketingDistributionSettings.size());
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

    @AutoLog(value = "平台分销设置-返显")
    @ApiOperation(value = "平台分销设置-返显", notes = "平台分销设置-返显")
    @RequestMapping(value = "findDistributionSetting", method = RequestMethod.GET)
    public Result<MarketingDistributionSetting> findDistributionSetting() {
        Result<MarketingDistributionSetting> result = new Result<>();
        QueryWrapper<MarketingDistributionSetting> marketingDistributionSettingQueryWrapper = new QueryWrapper<>();
        marketingDistributionSettingQueryWrapper.eq("del_flag", "0");
        MarketingDistributionSetting one = marketingDistributionSettingService.getOne(marketingDistributionSettingQueryWrapper);
        result.setSuccess(true);
        result.setResult(one);
        return result;
    }
    @AutoLog(value = "平台分销设置-保存")
    @ApiOperation(value = "平台分销设置-保存", notes = "平台分销设置-保存")
    @RequestMapping(value = "savaorUpdateSetting", method = RequestMethod.POST)
    public Result<MarketingDistributionSetting>savaorUpdateSetting(@RequestBody MarketingDistributionSetting marketingStoreDistributionSetting){
        Result<MarketingDistributionSetting> result = new Result<>();
        QueryWrapper<MarketingDistributionSetting> marketingDistributionSettingQueryWrapper = new QueryWrapper<>();
        marketingDistributionSettingQueryWrapper.eq("del_flag", "0");
        MarketingDistributionSetting one = marketingDistributionSettingService.getOne(marketingDistributionSettingQueryWrapper);
        if (oConvertUtils.isEmpty(one)){
            marketingDistributionSettingService.saveOrUpdate(marketingStoreDistributionSetting);
            result.setMessage("保存成功");
        }else {
            marketingDistributionSettingService.saveOrUpdate(marketingStoreDistributionSetting);
            result.setMessage("保存成功");
        }
        return result;
    }
    @AutoLog(value = "平台分销设置-分页列表查询")
    @ApiOperation(value = "平台分销设置-分页列表查询", notes = "平台分销设置-分页列表查询")
    @GetMapping(value = "/findDistributionSettingList")
    public Result<IPage<MarketingDistributionSettingVO>> findDistributionSettingList(MarketingDistributionSettingVO marketingDistributionSettingVO,
                                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MarketingDistributionSettingVO>> result = new Result<IPage<MarketingDistributionSettingVO>>();

        Page<MarketingDistributionSettingVO> page = new Page<MarketingDistributionSettingVO>(pageNo, pageSize);
        IPage<MarketingDistributionSettingVO> pageList = marketingDistributionSettingService.findDistributionSettingList(page, marketingDistributionSettingVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
}
