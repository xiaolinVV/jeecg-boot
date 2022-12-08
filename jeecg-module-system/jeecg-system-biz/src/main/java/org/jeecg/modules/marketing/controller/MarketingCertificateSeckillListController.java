package org.jeecg.modules.marketing.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillList;
import org.jeecg.modules.marketing.service.IMarketingCertificateSeckillListService;
import org.jeecg.modules.marketing.vo.MarketingCertificateSeckillListVO;
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
 * @Description: 限时抢券列表
 * @Author: jeecg-boot
 * @Date: 2021-03-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "限时抢券列表")
@RestController
@RequestMapping("/marketingCertificateSeckillList/marketingCertificateSeckillList")
public class MarketingCertificateSeckillListController {
    @Autowired
    private IMarketingCertificateSeckillListService marketingCertificateSeckillListService;

    /**
     * 分页列表查询
     *
     * @param marketingCertificateSeckillList
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "限时抢券列表-分页列表查询")
    @ApiOperation(value = "限时抢券列表-分页列表查询", notes = "限时抢券列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingCertificateSeckillListVO>> queryPageList(MarketingCertificateSeckillListVO marketingCertificateSeckillListVO,
                                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                          HttpServletRequest req) {
        Result<IPage<MarketingCertificateSeckillListVO>> result = new Result<IPage<MarketingCertificateSeckillListVO>>();
        QueryWrapper<MarketingCertificateSeckillListVO> queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateSeckillListVO, req.getParameterMap());
        Page<MarketingCertificateSeckillListVO> page = new Page<MarketingCertificateSeckillListVO>(pageNo, pageSize);
        IPage<MarketingCertificateSeckillListVO> pageList = marketingCertificateSeckillListService.queryPageList(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingCertificateSeckillList
     * @return
     */
    @AutoLog(value = "限时抢券列表-添加")
    @ApiOperation(value = "限时抢券列表-添加", notes = "限时抢券列表-添加")
    @PostMapping(value = "/add")
    public Result<MarketingCertificateSeckillList> add(@RequestBody MarketingCertificateSeckillList marketingCertificateSeckillList) {
        Result<MarketingCertificateSeckillList> result = new Result<MarketingCertificateSeckillList>();
        try {
            marketingCertificateSeckillListService.save(marketingCertificateSeckillList);
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
     * @param marketingCertificateSeckillList
     * @return
     */
    @AutoLog(value = "限时抢券列表-编辑")
    @ApiOperation(value = "限时抢券列表-编辑", notes = "限时抢券列表-编辑")
    @PostMapping(value = "/edit")
    public Result<MarketingCertificateSeckillList> edit(@RequestBody MarketingCertificateSeckillList marketingCertificateSeckillList) {
        Result<MarketingCertificateSeckillList> result = new Result<MarketingCertificateSeckillList>();
        MarketingCertificateSeckillList marketingCertificateSeckillListEntity = marketingCertificateSeckillListService.getById(marketingCertificateSeckillList.getId());
        if (marketingCertificateSeckillListEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingCertificateSeckillListService.updateById(marketingCertificateSeckillList);
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
    @AutoLog(value = "限时抢券列表-通过id删除")
    @ApiOperation(value = "限时抢券列表-通过id删除", notes = "限时抢券列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingCertificateSeckillListService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 删除and说明
     *
     * @param marketingCertificateSeckillList
     * @return
     */
    @PostMapping("deleteMarketingCertificateSeckillList")
    public Result<?> deleteMarketingCertificateSeckillList(@RequestBody MarketingCertificateSeckillList marketingCertificateSeckillList) {
        marketingCertificateSeckillListService.updateById(marketingCertificateSeckillList);
        marketingCertificateSeckillListService.removeById(marketingCertificateSeckillList.getId());
        return Result.ok("删除成功");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "限时抢券列表-批量删除")
    @ApiOperation(value = "限时抢券列表-批量删除", notes = "限时抢券列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingCertificateSeckillList> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingCertificateSeckillList> result = new Result<MarketingCertificateSeckillList>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingCertificateSeckillListService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "限时抢券列表-通过id查询")
    @ApiOperation(value = "限时抢券列表-通过id查询", notes = "限时抢券列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingCertificateSeckillList> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingCertificateSeckillList> result = new Result<MarketingCertificateSeckillList>();
        MarketingCertificateSeckillList marketingCertificateSeckillList = marketingCertificateSeckillListService.getById(id);
        if (marketingCertificateSeckillList == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingCertificateSeckillList);
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
        QueryWrapper<MarketingCertificateSeckillList> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingCertificateSeckillList marketingCertificateSeckillList = JSON.parseObject(deString, MarketingCertificateSeckillList.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateSeckillList, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingCertificateSeckillList> pageList = marketingCertificateSeckillListService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "限时抢券列表列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingCertificateSeckillList.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("限时抢券列表列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingCertificateSeckillList> listMarketingCertificateSeckillLists = ExcelImportUtil.importExcel(file.getInputStream(), MarketingCertificateSeckillList.class, params);
                marketingCertificateSeckillListService.saveBatch(listMarketingCertificateSeckillLists);
                return Result.ok("文件导入成功！数据行数:" + listMarketingCertificateSeckillLists.size());
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

    @PostMapping("addBatch")
    public Result<?> addBatch(@RequestBody List<MarketingCertificateSeckillList> marketingCertificateSeckillLists) {
        int i = 1;
        for (MarketingCertificateSeckillList marketingCertificateSeckillList : marketingCertificateSeckillLists) {
            marketingCertificateSeckillList.setJoinDate(new Date());
            marketingCertificateSeckillList.setSort(new BigDecimal(i++));
        }
        marketingCertificateSeckillListService.saveBatch(marketingCertificateSeckillLists);
        return Result.ok("添加成功");
    }
}
