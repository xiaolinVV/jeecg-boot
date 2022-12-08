package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupManage;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupRecordService;
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
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date: 2021-07-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "拼团管理")
@RestController
@RequestMapping("/marketingZoneGroupManage/marketingZoneGroupManage")
public class MarketingZoneGroupManageController {
    @Autowired
    private IMarketingZoneGroupManageService marketingZoneGroupManageService;
    @Autowired
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;

    /**
     * 分页列表查询
     *
     * @param marketingZoneGroupManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拼团管理-分页列表查询")
    @ApiOperation(value = "拼团管理-分页列表查询", notes = "拼团管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MarketingZoneGroupManage marketingZoneGroupManage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MarketingZoneGroupManage> queryWrapper = QueryGenerator.initQueryWrapper(marketingZoneGroupManage, req.getParameterMap());
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        return Result.ok(marketingZoneGroupManageService.queryPageList(page, queryWrapper));
    }

    /**
     * 添加
     *
     * @param marketingZoneGroupManage
     * @return
     */
    @AutoLog(value = "拼团管理-添加")
    @ApiOperation(value = "拼团管理-添加", notes = "拼团管理-添加")
    @PostMapping(value = "/add")
    public Result<MarketingZoneGroupManage> add(@RequestBody MarketingZoneGroupManage marketingZoneGroupManage) {
        Result<MarketingZoneGroupManage> result = new Result<MarketingZoneGroupManage>();
        try {
            marketingZoneGroupManageService.save(marketingZoneGroupManage);
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
     * @param marketingZoneGroupManage
     * @return
     */
    @AutoLog(value = "拼团管理-编辑")
    @ApiOperation(value = "拼团管理-编辑", notes = "拼团管理-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingZoneGroupManage> edit(@RequestBody MarketingZoneGroupManage marketingZoneGroupManage) {
        Result<MarketingZoneGroupManage> result = new Result<MarketingZoneGroupManage>();
        MarketingZoneGroupManage marketingZoneGroupManageEntity = marketingZoneGroupManageService.getById(marketingZoneGroupManage.getId());
        if (marketingZoneGroupManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingZoneGroupManageService.updateById(marketingZoneGroupManage);
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
    @AutoLog(value = "拼团管理-通过id删除")
    @ApiOperation(value = "拼团管理-通过id删除", notes = "拼团管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingZoneGroupManageService.removeById(id);
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
    @AutoLog(value = "拼团管理-批量删除")
    @ApiOperation(value = "拼团管理-批量删除", notes = "拼团管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingZoneGroupManage> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingZoneGroupManage> result = new Result<MarketingZoneGroupManage>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingZoneGroupManageService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "拼团管理-通过id查询")
    @ApiOperation(value = "拼团管理-通过id查询", notes = "拼团管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingZoneGroupManage> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingZoneGroupManage> result = new Result<MarketingZoneGroupManage>();
        MarketingZoneGroupManage marketingZoneGroupManage = marketingZoneGroupManageService.getById(id);
        if (marketingZoneGroupManage == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingZoneGroupManage);
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
        QueryWrapper<MarketingZoneGroupManage> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingZoneGroupManage marketingZoneGroupManage = JSON.parseObject(deString, MarketingZoneGroupManage.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingZoneGroupManage, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingZoneGroupManage> pageList = marketingZoneGroupManageService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "拼团管理列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingZoneGroupManage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拼团管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingZoneGroupManage> listMarketingZoneGroupManages = ExcelImportUtil.importExcel(file.getInputStream(), MarketingZoneGroupManage.class, params);
                marketingZoneGroupManageService.saveBatch(listMarketingZoneGroupManages);
                return Result.ok("文件导入成功！数据行数:" + listMarketingZoneGroupManages.size());
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
     * 获取该专区团管理全部会员
     * @param id
     * @return
     */
    @GetMapping("getZoneGroupManageMemberList")
    public Result<?>getZoneGroupManageMemberList(@RequestParam(name = "id", required = true) String id){
        if (StringUtils.isBlank(id)){
            return Result.ok("前端id未传递!");
        }
        if (marketingZoneGroupManageService.getById(id)!=null){
            return Result.ok(iMarketingZoneGroupRecordService.getZoneGroupManageMemberList(id));
        }else {
            return Result.ok("未找到专区团,前端id传递有误!");
        }
    }

    /**
     * 团控制
     * @param jsonObject
     * @return
     */
    @PostMapping("zoneGroupManageControl")
    public Result<?>zoneGroupManageControl(@RequestBody JSONObject jsonObject){
        String id = jsonObject.getString("id");
        String status = jsonObject.getString("status");
        String memberListId = jsonObject.getString("memberListId");
        if (StringUtils.isBlank(id)){
            return Result.error("前端id未传递");
        }
        if (StringUtils.isBlank(status)){
            return Result.error("前端状态未传递");
        }
        if (StringUtils.isBlank(memberListId)){
            return Result.error("前端中奖人id未传递");
        }
        return Result.ok("成功!");
    }
}
