package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台专区分类
 * @Author: jeecg-boot
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "平台专区分类")
@RestController
@RequestMapping("/marketingPrefectureType/marketingPrefectureType")
public class MarketingPrefectureTypeController {
    @Autowired
    private IMarketingPrefectureTypeService marketingPrefectureTypeService;
    @Autowired
    private IMarketingPrefectureService marketingPrefectureService;

    @Autowired
    private IMarketingPrefectureGoodService marketingPrefectureGoodService;

    /**
     * 分页列表查询
     *
     * @param marketingPrefectureType
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "平台专区分类-分页列表查询")
    @ApiOperation(value = "平台专区分类-分页列表查询", notes = "平台专区分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingPrefectureType>> queryPageList(MarketingPrefectureType marketingPrefectureType,
                                                                String marketingPrefectureId,
                                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                HttpServletRequest req) {
        Result<IPage<MarketingPrefectureType>> result = new Result<IPage<MarketingPrefectureType>>();
        marketingPrefectureType.setMarketingPrefectureId(marketingPrefectureId);


        QueryWrapper<MarketingPrefectureType> queryWrapper = QueryGenerator.initQueryWrapper(marketingPrefectureType, req.getParameterMap());
        Page<MarketingPrefectureType> page = new Page<MarketingPrefectureType>(pageNo, pageSize);

        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap.size() <= 4) {
            queryWrapper.eq("level", "1");
        }

        queryWrapper.orderByAsc("sort");
        IPage<MarketingPrefectureType> pageList = marketingPrefectureTypeService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 添加
     *
     * @param marketingPrefectureType
     * @return
     */
    @AutoLog(value = "平台专区分类-添加")
    @ApiOperation(value = "平台专区分类-添加", notes = "平台专区分类-添加")
    @PostMapping(value = "/add")
    public Result<MarketingPrefectureType> add(@RequestBody MarketingPrefectureType marketingPrefectureType) {
        Result<MarketingPrefectureType> result = new Result<MarketingPrefectureType>();
        if (marketingPrefectureType.getSort().intValue() < 0) {
            return result.error500("排序必须是正数!");
        }
        try {
            marketingPrefectureTypeService.save(marketingPrefectureType);
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
     * @param marketingPrefectureType
     * @return
     */
    @AutoLog(value = "平台专区分类-编辑")
    @ApiOperation(value = "平台专区分类-编辑", notes = "平台专区分类-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingPrefectureType> edit(@RequestBody MarketingPrefectureType marketingPrefectureType) {
        Result<MarketingPrefectureType> result = new Result<MarketingPrefectureType>();
        MarketingPrefectureType marketingPrefectureTypeEntity = marketingPrefectureTypeService.getById(marketingPrefectureType.getId());
        if (marketingPrefectureTypeEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingPrefectureTypeService.updateById(marketingPrefectureType);
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
    @AutoLog(value = "平台专区分类-通过id删除")
    @ApiOperation(value = "平台专区分类-通过id删除", notes = "平台专区分类-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            MarketingPrefectureType marketingPrefectureType = marketingPrefectureTypeService.getById(id);
            if (marketingPrefectureType.getLevel().intValue() == 2) {
                MarketingPrefectureType prefectureType = marketingPrefectureTypeService.list(new LambdaQueryWrapper<MarketingPrefectureType>()
                        .eq(MarketingPrefectureType::getDelFlag, "0")
                        .eq(MarketingPrefectureType::getId, marketingPrefectureType.getPid())
                ).get(0);
                if (marketingPrefectureTypeService.count(new LambdaQueryWrapper<MarketingPrefectureType>()
                        .eq(MarketingPrefectureType::getDelFlag, "0")
                        .eq(MarketingPrefectureType::getPid, prefectureType.getId())
                ) == 1) {
                    marketingPrefectureTypeService.saveOrUpdate(prefectureType.setHasChild("0"));
                }
            }
            //关联删除专区
            marketingPrefectureTypeService.linkToDelete(id);
            marketingPrefectureTypeService.removeById(id);
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
    @AutoLog(value = "平台专区分类-批量删除")
    @ApiOperation(value = "平台专区分类-批量删除", notes = "平台专区分类-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingPrefectureType> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingPrefectureType> result = new Result<MarketingPrefectureType>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            //关联删除商品
            marketingPrefectureTypeService.linkToDelete(ids);
            this.marketingPrefectureTypeService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "平台专区分类-通过id查询")
    @ApiOperation(value = "平台专区分类-通过id查询", notes = "平台专区分类-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingPrefectureType> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingPrefectureType> result = new Result<MarketingPrefectureType>();
        MarketingPrefectureType marketingPrefectureType = marketingPrefectureTypeService.getById(id);
        if (marketingPrefectureType == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingPrefectureType);
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
        QueryWrapper<MarketingPrefectureType> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingPrefectureType marketingPrefectureType = JSON.parseObject(deString, MarketingPrefectureType.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingPrefectureType, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingPrefectureType> pageList = marketingPrefectureTypeService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "平台专区分类列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingPrefectureType.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台专区分类列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingPrefectureType> listMarketingPrefectureTypes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingPrefectureType.class, params);
                marketingPrefectureTypeService.saveBatch(listMarketingPrefectureTypes);
                return Result.ok("文件导入成功！数据行数:" + listMarketingPrefectureTypes.size());
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
     * 批量修改:启用停用
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "平台专区分类-通过id查询")
    @ApiOperation(value = "平台专区分类-通过id查询", notes = "平台专区分类-通过id查询")
    @GetMapping(value = "/updateStatus")
    public Result<MarketingPrefectureType> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status, @RequestParam(name = "closeExplain") String closeExplain) {
        Result<MarketingPrefectureType> result = new Result<MarketingPrefectureType>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            MarketingPrefectureType marketingPrefectureType;
            try {
                List<String> listid = Arrays.asList(ids.split(","));
                Integer count = 0;
                for (String id : listid) {
                    if (status.equals("1")) {
                        //判断是否可以启用
                        Map<String, Object> map = marketingPrefectureTypeService.linkToUpdate(id);
                        //出错处理,不可启用判断
                        if (map.get("data").equals("1")) {
                            return result.error500(map.get("msg").toString());
                        }
                    }
                    marketingPrefectureType = marketingPrefectureTypeService.getById(id);
                    if (marketingPrefectureType == null) {
                        result.error500("未找到对应实体");
                    } else {
                        marketingPrefectureType.setCloseExplain(closeExplain);
                        marketingPrefectureType.setStatus(status);
                        marketingPrefectureTypeService.updateById(marketingPrefectureType);

                        List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(
                                new LambdaQueryWrapper<MarketingPrefectureGood>().eq(MarketingPrefectureGood::getMarketingPrefectureId, marketingPrefectureType.getMarketingPrefectureId()));
                        for (MarketingPrefectureGood marketingPrefectureGood : marketingPrefectureGoodList) {
                            //判断是否可以启用
                            Map<String, Object> map = marketingPrefectureGoodService.linkToUpdate(marketingPrefectureGood.getId());
                            //出错处理,不可启用判断
                            if (map.get("data").equals("1")) {
                                count = count + 1;
                            }
                        }


                    }
                }
                //停用修改关联数据
                if (status.equals("0")) {
                    marketingPrefectureTypeService.linkToUpdateStatus(ids);
                }
                result.setCode(200);
                result.success("修改成功!");
            } catch (Exception e) {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /**
     * 根据专区id查询对应分类
     *
     * @param marketingPrefectureId
     * @return
     */
    @GetMapping(value = "/getMarketingPrefectureType")
    public Result<List<MarketingPrefectureType>> getMarketingPrefectureType(@RequestParam(name = "marketingPrefectureId") String marketingPrefectureId) {
        Result<List<MarketingPrefectureType>> result = new Result<List<MarketingPrefectureType>>();
        if (StringUtils.isBlank(marketingPrefectureId)) {
            result.error500("专区id不能为空");
            return result;
        }
        MarketingPrefecture marketingPrefecture = marketingPrefectureService.getById(marketingPrefectureId);
        if (marketingPrefecture == null) {
            result.error500("专区不能为空");
            return result;
        }
        QueryWrapper<MarketingPrefectureType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("marketing_prefecture_id", marketingPrefectureId);
        queryWrapper.eq("del_flag", "0");
        queryWrapper.eq("status", "1");
        queryWrapper.eq("level", "1");
        queryWrapper.orderByAsc("sort");
        List<MarketingPrefectureType> marketingPrefectureTypeList = marketingPrefectureTypeService.list(queryWrapper);
        result.setResult(marketingPrefectureTypeList);
        result.setCode(200);
        return result;
    }


    /**
     * 根据专区id查询对应的所有分类包括子分类
     *
     * @param marketingPrefectureId
     * @return
     */
    @SuppressWarnings("all")
    @GetMapping(value = "/getMarketingPrefectureTypeAll")
    public Result<List<Map<String, Object>>> getMarketingPrefectureTypeAll(@RequestParam(name = "marketingPrefectureId") String marketingPrefectureId) {
        Result<List<Map<String, Object>>> result = new Result<>();
        if (org.apache.commons.lang3.StringUtils.isBlank(marketingPrefectureId)){
            return result.error500("前端id未传递");
        }
        List<Map<String, Object>> list = marketingPrefectureTypeService.getMarketingPrefectureTypeAll(marketingPrefectureId);

        result.setResult(list);
        result.setCode(200);
        return result;
    }

    /**
     * 根据专区id查询对应的所有分类包括子分类
     *
     * @param marketingPrefectureTypeId
     * @return
     */

    @GetMapping(value = "/getParentMarketingPrefectureType")
    public Result<MarketingPrefectureType> getParentMarketingPrefectureType(String marketingPrefectureTypeId) {


        QueryWrapper<MarketingPrefectureType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", marketingPrefectureTypeId);
        queryWrapper.eq("del_flag", "0");
        queryWrapper.eq("status", "1");
        MarketingPrefectureType MarketingPrefectureType = marketingPrefectureTypeService.getOne(queryWrapper);

        MarketingPrefectureType parentMarketingPrefectureType = null;
        if (MarketingPrefectureType != null && !"0".equals(MarketingPrefectureType.getPid())) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", MarketingPrefectureType.getPid());
            queryWrapper.eq("del_flag", "0");
            queryWrapper.eq("status", "1");
            parentMarketingPrefectureType = marketingPrefectureTypeService.getOne(queryWrapper);
        }
        Result<MarketingPrefectureType> result = new Result<>();
        result.setResult(parentMarketingPrefectureType);
        result.setCode(200);
        return result;
    }


    /**
     * 添加分类and添加子分类
     *
     * @param marketingPrefectureType
     * @return
     */
    @PostMapping("setMarketingPrefectureType")
    public Result<MarketingPrefectureType> setMarketingPrefectureType(@RequestBody MarketingPrefectureType marketingPrefectureType) {
        if (StringUtils.isBlank(marketingPrefectureType.getHasChild())) {
            marketingPrefectureType.setHasChild("0");
        }
        if (StringUtils.isBlank(marketingPrefectureType.getPid())) {
            marketingPrefectureType.setPid("0")
                    .setLevel(new BigDecimal(1));
        } else {
            marketingPrefectureType.setLevel(new BigDecimal(2));
            MarketingPrefectureType prefectureType = marketingPrefectureTypeService.getById(marketingPrefectureType.getPid());
            if (StringUtils.isBlank(prefectureType.getHasChild()) || prefectureType.getHasChild().equals("0")) {
                marketingPrefectureTypeService.saveOrUpdate(prefectureType.setHasChild("1"));
            }
        }
        if (StringUtils.isBlank(marketingPrefectureType.getStatus())) {
            marketingPrefectureType.setStatus("1");
        }
        return this.add(marketingPrefectureType);
    }

    /**
     * 获取下级分类列表
     *
     * @param id
     * @return
     */
    @GetMapping("getUnderlingList")
    public Result<List<MarketingPrefectureType>> getUnderlingList(@RequestParam(name = "id", required = true) String id) {
        Result<List<MarketingPrefectureType>> result = new Result<>();
        result.setResult(marketingPrefectureTypeService.getUnderlingList(id));
        return result;
    }

    /**
     * 获取下级分类列表(列表下拉框)
     *
     * @param id
     * @return
     */
    @GetMapping("findUnderlingListMap")
    public Result<List<Map<String, Object>>> findUnderlingListMap(@RequestParam(name = "id", required = true) String id) {
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(marketingPrefectureTypeService.findUnderlingListMap(id));
        return result;
    }

    /**
     * 复制商品地址
     *
     * @param marketingPrefectureId
     * @return
     */
    @AutoLog(value = "专区商品-复制链接")
    @ApiOperation(value = "专区商品-复制链接", notes = "专区商品-复制链接")
    @GetMapping(value = "/getMarketingPrefectureUrl")
    public Result<Map<String, Object>> getMarketingPrefectureUrl(String marketingPrefectureId) {
        Result<Map<String, Object>> result = new Result<>();
        MarketingPrefecture marketingPrefecture = marketingPrefectureService.getById(marketingPrefectureId);
        if (marketingPrefecture == null) {
            result.error500("专区id不能为空!");
            return result;
        }
        String url = "goodAction/pages/prefecture/prefecture?prefectureName=" + marketingPrefecture.getPrefectureName() + "&id=" + marketingPrefecture.getId();
        Map<String, Object> mapObject = Maps.newHashMap();
        mapObject.put("url", url);
        result.setResult(mapObject);
        result.setSuccess(true);
        return result;
    }
}
