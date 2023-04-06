package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingPrefectureRecommendGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureRecommendGood;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendGoodService;
import org.jeecg.modules.marketing.vo.MarketingPrefectureRecommendGoodVO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 专区推荐商品
 * @Author: jeecg-boot
 * @Date: 2020-09-14
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "专区推荐商品")
@RestController
@RequestMapping("/marketingPrefectureRecommendGood/marketingPrefectureRecommendGood")
public class MarketingPrefectureRecommendGoodController {
    @Autowired
    private IMarketingPrefectureRecommendGoodService marketingPrefectureRecommendGoodService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "专区推荐商品-分页列表查询")
    @ApiOperation(value = "专区推荐商品-分页列表查询", notes = "专区推荐商品-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingPrefectureRecommendGoodVO>> queryPageList(MarketingPrefectureRecommendGoodDTO marketingPrefectureRecommendGoodDTO,
                                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                           HttpServletRequest req) {
        Result<IPage<MarketingPrefectureRecommendGoodVO>> result = new Result<IPage<MarketingPrefectureRecommendGoodVO>>();
        Page<MarketingPrefectureRecommendGoodVO> page = new Page<MarketingPrefectureRecommendGoodVO>(pageNo, pageSize);
        if (StringUtils.isNotBlank(marketingPrefectureRecommendGoodDTO.getMarketingPrefectureTypeId())){
            if (marketingPrefectureRecommendGoodDTO.getMarketingPrefectureTypeId().contains(",")){
                List<String> strings = Arrays.asList(StringUtils.split(marketingPrefectureRecommendGoodDTO.getMarketingPrefectureTypeId(), ","));
                marketingPrefectureRecommendGoodDTO.setTypeTwoId(strings.get(1));
            }else {
                marketingPrefectureRecommendGoodDTO.setTypeOneId(marketingPrefectureRecommendGoodDTO.getMarketingPrefectureTypeId());
            }
        }
        IPage<MarketingPrefectureRecommendGoodVO> pageList = marketingPrefectureRecommendGoodService.queryPageList(page,marketingPrefectureRecommendGoodDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingPrefectureRecommendGood
     * @return
     */
    @AutoLog(value = "专区推荐商品-添加")
    @ApiOperation(value = "专区推荐商品-添加", notes = "专区推荐商品-添加")
    @PostMapping(value = "/add")
    public Result<MarketingPrefectureRecommendGood> add(@RequestBody MarketingPrefectureRecommendGood marketingPrefectureRecommendGood) {
        Result<MarketingPrefectureRecommendGood> result = new Result<MarketingPrefectureRecommendGood>();
        try {
            marketingPrefectureRecommendGoodService.save(marketingPrefectureRecommendGood);
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
     * @param marketingPrefectureRecommendGood
     * @return
     */
    @AutoLog(value = "专区推荐商品-编辑")
    @ApiOperation(value = "专区推荐商品-编辑", notes = "专区推荐商品-编辑")
    @PostMapping(value = "/edit")
    public Result<MarketingPrefectureRecommendGood> edit(@RequestBody MarketingPrefectureRecommendGood marketingPrefectureRecommendGood) {
        Result<MarketingPrefectureRecommendGood> result = new Result<MarketingPrefectureRecommendGood>();
        MarketingPrefectureRecommendGood marketingPrefectureRecommendGoodEntity = marketingPrefectureRecommendGoodService.getById(marketingPrefectureRecommendGood.getId());
        if (marketingPrefectureRecommendGoodEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingPrefectureRecommendGoodService.updateById(marketingPrefectureRecommendGood);
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
    @AutoLog(value = "专区推荐商品-通过id删除")
    @ApiOperation(value = "专区推荐商品-通过id删除", notes = "专区推荐商品-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingPrefectureRecommendGoodService.removeById(id);
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
    @AutoLog(value = "专区推荐商品-批量删除")
    @ApiOperation(value = "专区推荐商品-批量删除", notes = "专区推荐商品-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingPrefectureRecommendGood> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingPrefectureRecommendGood> result = new Result<MarketingPrefectureRecommendGood>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingPrefectureRecommendGoodService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "专区推荐商品-通过id查询")
    @ApiOperation(value = "专区推荐商品-通过id查询", notes = "专区推荐商品-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingPrefectureRecommendGood> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingPrefectureRecommendGood> result = new Result<MarketingPrefectureRecommendGood>();
        MarketingPrefectureRecommendGood marketingPrefectureRecommendGood = marketingPrefectureRecommendGoodService.getById(id);
        if (marketingPrefectureRecommendGood == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingPrefectureRecommendGood);
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
        QueryWrapper<MarketingPrefectureRecommendGood> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingPrefectureRecommendGood marketingPrefectureRecommendGood = JSON.parseObject(deString, MarketingPrefectureRecommendGood.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingPrefectureRecommendGood, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingPrefectureRecommendGood> pageList = marketingPrefectureRecommendGoodService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "专区推荐商品列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingPrefectureRecommendGood.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("专区推荐商品列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingPrefectureRecommendGood> listMarketingPrefectureRecommendGoods = ExcelImportUtil.importExcel(file.getInputStream(), MarketingPrefectureRecommendGood.class, params);
                marketingPrefectureRecommendGoodService.saveBatch(listMarketingPrefectureRecommendGoods);
                return Result.ok("文件导入成功！数据行数:" + listMarketingPrefectureRecommendGoods.size());
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

    @PostMapping("adds")
    public Result<String> adds(@RequestBody MarketingPrefectureRecommendGoodDTO marketingPrefectureRecommendGoodDTO) {
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(marketingPrefectureRecommendGoodDTO.getMarketingPrefectureRecommendId())) {
            return result.error500("专区推荐id不能为空");
        }
        if (StringUtils.isBlank(marketingPrefectureRecommendGoodDTO.getGoodList())){
            return result.error500("请选择商品");
        }
        ArrayList<MarketingPrefectureRecommendGood> marketingPrefectureRecommendGoods = new ArrayList<>();
        JSONArray goods = JSONArray.parseArray(marketingPrefectureRecommendGoodDTO.getGoodList());
        for (int i = 0; i < goods.size(); i++) {
            marketingPrefectureRecommendGoods.add(new MarketingPrefectureRecommendGood()
                    .setDelFlag("0")
                    .setMarketingPrefectureRecommendId(marketingPrefectureRecommendGoodDTO.getMarketingPrefectureRecommendId())
                    .setMarketingPrefectureGoodId(goods.getJSONObject(i).getString("id"))
                    .setSort(goods.getJSONObject(i).getBigDecimal("sort")));
        }

        boolean b = marketingPrefectureRecommendGoodService.saveBatch(marketingPrefectureRecommendGoods);
        if (b){
            result.success("加入成功");
        }else {
            result.error500("加入失败");
        }
        return result;
    }
}