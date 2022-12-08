package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingLiveLottery;
import org.jeecg.modules.marketing.entity.MarketingLivePrize;
import org.jeecg.modules.marketing.service.IMarketingLiveLotteryService;
import org.jeecg.modules.marketing.service.IMarketingLivePrizeService;
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
 * @Description: 直播管理-抽奖奖品
 * @Author: jeecg-boot
 * @Date: 2021-09-14
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "直播管理-抽奖奖品")
@RestController
@RequestMapping("/marketingLivePrize/marketingLivePrize")
public class MarketingLivePrizeController {
    @Autowired
    private IMarketingLivePrizeService marketingLivePrizeService;
    @Autowired
    private IMarketingLiveLotteryService iMarketingLiveLotteryService;
    /**
     * 分页列表查询
     *
     * @param marketingLivePrize
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "直播管理-抽奖奖品-分页列表查询")
    @ApiOperation(value = "直播管理-抽奖奖品-分页列表查询", notes = "直播管理-抽奖奖品-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingLivePrize>> queryPageList(MarketingLivePrize marketingLivePrize,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        Result<IPage<MarketingLivePrize>> result = new Result<IPage<MarketingLivePrize>>();
        QueryWrapper<MarketingLivePrize> queryWrapper = QueryGenerator.initQueryWrapper(marketingLivePrize, req.getParameterMap());
        Page<MarketingLivePrize> page = new Page<MarketingLivePrize>(pageNo, pageSize);
        IPage<MarketingLivePrize> pageList = marketingLivePrizeService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingLivePrize
     * @return
     */
    @AutoLog(value = "直播管理-抽奖奖品-添加")
    @ApiOperation(value = "直播管理-抽奖奖品-添加", notes = "直播管理-抽奖奖品-添加")
    @PostMapping(value = "/add")
    public Result<MarketingLivePrize> add(@RequestBody MarketingLivePrize marketingLivePrize) {
        Result<MarketingLivePrize> result = new Result<MarketingLivePrize>();
        try {
            marketingLivePrizeService.save(marketingLivePrize.setPrizeSerialNumber(OrderNoUtils.getOrderNo()));
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
     * @param marketingLivePrize
     * @return
     */
    @AutoLog(value = "直播管理-抽奖奖品-编辑")
    @ApiOperation(value = "直播管理-抽奖奖品-编辑", notes = "直播管理-抽奖奖品-编辑")
    @PostMapping(value = "/edit")
    public Result<MarketingLivePrize> edit(@RequestBody MarketingLivePrize marketingLivePrize) {
        Result<MarketingLivePrize> result = new Result<MarketingLivePrize>();
        if (StringUtils.isBlank(marketingLivePrize.getId())) {
            return result.error500("前端id未传递");
        }

        MarketingLivePrize marketingLivePrizeEntity = marketingLivePrizeService.getById(marketingLivePrize.getId());

        if (marketingLivePrizeEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingLivePrizeService.updateById(marketingLivePrize);
            if (ok) {
                if (marketingLivePrize.getRepertory()!=null){
                    if (marketingLivePrize.getRepertory().longValue()<marketingLivePrizeEntity.getRepertory().longValue()&&
                            marketingLivePrize.getSuperInventory().equals("0")){
                        iMarketingLiveLotteryService.update(new MarketingLiveLottery()
                                .setLotteryPrizeTotal(marketingLivePrize.getRepertory()),
                                new LambdaUpdateWrapper<MarketingLiveLottery>().eq(MarketingLiveLottery::getLotteryPrizeId,marketingLivePrize.getId()));
                    }
                }
                result.success("修改成功!");
            }else {
                result.error500("修改失败");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param marketingLivePrize
     * @return
     */
    @AutoLog(value = "直播管理-抽奖奖品-通过id删除")
    @ApiOperation(value = "直播管理-抽奖奖品-通过id删除", notes = "直播管理-抽奖奖品-通过id删除")
    @PostMapping(value = "/delete")
    public Result<?> delete(@RequestBody MarketingLivePrize marketingLivePrize) {
        if (StringUtils.isBlank(marketingLivePrize.getId())) {
            return Result.error("前端id未传递!");
        }
        try {
            marketingLivePrizeService.updateById(marketingLivePrize);
            marketingLivePrizeService.removeById(marketingLivePrize.getId());
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
    @AutoLog(value = "直播管理-抽奖奖品-批量删除")
    @ApiOperation(value = "直播管理-抽奖奖品-批量删除", notes = "直播管理-抽奖奖品-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingLivePrize> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingLivePrize> result = new Result<MarketingLivePrize>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingLivePrizeService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "直播管理-抽奖奖品-通过id查询")
    @ApiOperation(value = "直播管理-抽奖奖品-通过id查询", notes = "直播管理-抽奖奖品-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingLivePrize> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingLivePrize> result = new Result<MarketingLivePrize>();
        MarketingLivePrize marketingLivePrize = marketingLivePrizeService.getById(id);
        if (marketingLivePrize == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingLivePrize);
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
        QueryWrapper<MarketingLivePrize> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingLivePrize marketingLivePrize = JSON.parseObject(deString, MarketingLivePrize.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingLivePrize, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingLivePrize> pageList = marketingLivePrizeService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "直播管理-抽奖奖品列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingLivePrize.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("直播管理-抽奖奖品列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingLivePrize> listMarketingLivePrizes = ExcelImportUtil.importExcel(file.getInputStream(), MarketingLivePrize.class, params);
                marketingLivePrizeService.saveBatch(listMarketingLivePrizes);
                return Result.ok("文件导入成功！数据行数:" + listMarketingLivePrizes.size());
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

    @GetMapping("findMarketingLivePrizeList")
    public Result<?> findMarketingLivePrizeList(MarketingLivePrize marketingLivePrize) {
        if (StringUtils.isBlank(marketingLivePrize.getPrizeType())){
            return Result.error("前端类型未传递");
        }
        QueryWrapper<MarketingLivePrize> marketingLivePrizeQueryWrapper = new QueryWrapper<MarketingLivePrize>()
                .select("id,concat(prize_serial_number,'(',prize_name,')') as name")
                .eq("del_flag","0")
                .eq("status","1")
                .eq("prize_type",marketingLivePrize.getPrizeType());
        if (StringUtils.isNotBlank(marketingLivePrize.getSuperInventory())){
            marketingLivePrizeQueryWrapper.eq("super_inventory",marketingLivePrize.getSuperInventory());
        }
        return Result.ok(marketingLivePrizeService.listMaps(marketingLivePrizeQueryWrapper));
    }
}
